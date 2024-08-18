package be.atc.projetcentresportif2024;

import be.atc.entities.Address;
import be.atc.entities.Locality;
import be.atc.entities.User;
import be.atc.services.UserService;
import be.atc.services.AddressService;
import be.atc.services.LocalityService;
import be.atc.services.impl.AddressServiceImpl;
import be.atc.services.impl.LocalityServiceImpl;
import be.atc.services.impl.UserServiceImpl;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Servlet qui gère les opérations liées aux adresses des utilisateurs, telles que l'affichage des localités et la mise à jour des adresses.
 */
@WebServlet(name = "AddressServlet", value = "/AddressServlet")
public class AddressServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(AddressServlet.class);

    // Constantes pour les attributs de session et de requête
    private static final String CURRENT_EDIT_USER = "currentEditUser";
    private static final String ERROR_MESSAGE = "errorMessage";
    private static final String SUCCESS_MESSAGE = "successMessage";

    private final AddressService addressService;
    private final LocalityService localityService;
    private final UserService userService;

    /**
     * Constructeur par défaut qui initialise les services nécessaires.
     */
    public AddressServlet() {
        this.addressService = new AddressServiceImpl();
        this.localityService = new LocalityServiceImpl();
        this.userService = new UserServiceImpl();
    }

    /**
     * Méthode doGet pour gérer les requêtes GET.
     * Charge les localités ou traite les requêtes AJAX pour récupérer les détails d'une localité spécifique.
     *
     * @param request  L'objet HttpServletRequest contenant la requête du client.
     * @param response L'objet HttpServletResponse contenant la réponse envoyée au client.
     * @throws ServletException Si une erreur survient lors du traitement de la requête.
     * @throws IOException      Si une erreur d'entrée/sortie survient.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Entrée dans doGet de AddressServlet");

        HttpSession session = validateSession(request, response);
        if (session == null) return;

        String localityIdStr = request.getParameter("localityId");
        if (localityIdStr != null) {
            handleAjaxRequest(localityIdStr, response);
        } else {
            loadLocalitiesAndForward(request, response);
        }
    }

    /**
     * Gère les requêtes AJAX pour récupérer les détails d'une localité spécifique.
     *
     * @param localityIdStr L'ID de la localité sous forme de chaîne de caractères.
     * @param response      L'objet HttpServletResponse contenant la réponse envoyée au client.
     * @throws IOException Si une erreur d'entrée/sortie survient.
     */
    private void handleAjaxRequest(String localityIdStr, HttpServletResponse response) throws IOException {
        try {
            int localityId = Integer.parseInt(localityIdStr);
            Optional<Locality> localityOpt = localityService.findById(localityId);

            if (localityOpt.isPresent()) {
                Locality locality = localityOpt.get();
                String jsonResponse = createJsonResponse(locality);

                response.setContentType("application/json");
                response.getWriter().write(jsonResponse);
                logger.info("Détails de la localité envoyés pour l'ID : " + localityId);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); // SC_NOT_FOUND = Code statut HTTP 404
                logger.warn("Localité non trouvée pour l'ID : " + localityId);
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // SC_BAD_REQUEST = Code statut HTTP 400
            logger.error("ID de localité invalide : " + localityIdStr);
        }
    }

    /**
     * Crée une réponse JSON contenant les détails d'une localité.
     *
     * @param locality L'objet Locality à convertir en JSON.
     * @return La chaîne JSON représentant la localité.
     */
    private String createJsonResponse(Locality locality) {
        return String.format(
                "{\"postalCode\": \"%s\", \"town\": \"%s\", \"province\": \"%s\", \"maintown\": \"%s\"}",
                locality.getPostalCode(),
                locality.getTown(),
                locality.getProvince(),
                locality.getMaintown()
        );
    }

    /**
     * Charge la liste des localités et redirige vers la page de profil.
     *
     * @param request  L'objet HttpServletRequest contenant la requête du client.
     * @param response L'objet HttpServletResponse contenant la réponse envoyée au client.
     * @throws ServletException Si une erreur survient lors du traitement de la requête.
     * @throws IOException      Si une erreur d'entrée/sortie survient.
     */
    private void loadLocalitiesAndForward(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = validateSession(request, response);
        if (session == null) return;

        List<Locality> localities = localityService.findAll();
        request.setAttribute("localities", localities);

        if (localities != null && !localities.isEmpty()) {
            logger.info("Nombre de localités récupérées : " + localities.size());
        } else {
            logger.warn("Aucune localité trouvée ou la liste des localités est vide.");
        }

        request.getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(request, response);
    }

    /**
     * Méthode doPost pour gérer les requêtes POST.
     * Met à jour l'adresse de l'utilisateur.
     *
     * @param request  L'objet HttpServletRequest contenant la requête du client.
     * @param response L'objet HttpServletResponse contenant la réponse envoyée au client.
     * @throws ServletException Si une erreur survient lors du traitement de la requête.
     * @throws IOException      Si une erreur d'entrée/sortie survient.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Entrée dans doPost de AddressServlet");

        HttpSession session = validateSession(request, response);
        if (session == null) return;

        User user = (User) session.getAttribute(CURRENT_EDIT_USER);

        try {
            updateUserAddress(request, user);
            session.setAttribute(CURRENT_EDIT_USER, user);
            session.setAttribute(SUCCESS_MESSAGE, "Adresse mise à jour avec succès.");
        } catch (Exception e) {
            handlePostError(request, response, e);
        }

        loadLocalitiesAndForward(request, response);
    }

    /**
     * Valide la session utilisateur. Si la session est invalide, redirige vers la page de connexion.
     *
     * @param request  L'objet HttpServletRequest contenant la requête du client.
     * @param response L'objet HttpServletResponse contenant la réponse envoyée au client.
     * @return La session utilisateur valide, ou null si la session est invalide.
     * @throws IOException Si une erreur d'entrée/sortie survient.
     */
    private HttpSession validateSession(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            logger.warn("Aucune session valide, redirection vers la page de connexion");
            response.sendRedirect(request.getContextPath() + "/main?action=login");
            return null;
        }
        return session;
    }

    /**
     * Met à jour l'adresse de l'utilisateur à partir des données de la requête.
     *
     * @param request L'objet HttpServletRequest contenant la requête du client.
     * @param user    L'utilisateur dont l'adresse doit être mise à jour.
     * @throws IllegalArgumentException Si les données fournies sont invalides.
     */
    private void updateUserAddress(HttpServletRequest request, User user) throws IllegalArgumentException {
        String streetName = request.getParameter("streetName");
        String number = request.getParameter("number");
        String boxNumber = request.getParameter("boxNumber");
        String localityIdStr = request.getParameter("locality");

        logger.debug("Données reçues : streetName=" + streetName + ", number=" + number + ", boxNumber=" + boxNumber + ", localityId=" + localityIdStr);

        int localityId = Integer.parseInt(localityIdStr);
        Locality locality = localityService.findById(localityId)
                .orElseThrow(() -> new IllegalArgumentException("INVALID_LOCALITY"));

        Address address = getAddress(user);
        updateAddressFields(address, streetName, number, boxNumber, locality);

        if (address.getId() == 0) {
            addressService.createAddress(address);
            logger.info("Nouvelle adresse créée pour l'utilisateur : " + user.getEmail());
        } else {
            addressService.updateAddress(address);
            logger.info("Adresse mise à jour pour l'utilisateur : " + user.getEmail());
        }

        user.setFkAddresse(address);
        logger.info("Adresse ID " + address.getId() + " associée à l'utilisateur ID " + user.getId() + " (Email: " + user.getEmail() + ")");
        userService.updateUser(user);
    }

    /**
     * Récupère l'adresse actuelle de l'utilisateur, ou crée une nouvelle adresse si l'utilisateur n'en a pas.
     *
     * @param user L'utilisateur dont l'adresse doit être récupérée.
     * @return L'adresse actuelle de l'utilisateur ou une nouvelle adresse.
     */
    private Address getAddress(User user) {
        Address address = user.getFkAddresse();
        if (address == null) {
            address = new Address();
        }
        return address;
    }

    /**
     * Met à jour les champs d'une adresse avec les valeurs fournies.
     *
     * @param address    L'adresse à mettre à jour.
     * @param streetName Le nom de la rue.
     * @param number     Le numéro de la rue.
     * @param boxNumber  Le numéro de boîte.
     * @param locality   La localité associée à l'adresse.
     */
    private void updateAddressFields(Address address, String streetName, String number, String boxNumber, Locality locality) {
        address.setStreetName(streetName);
        address.setNumber(number);
        address.setBoxNumber(boxNumber);
        address.setFkLocality(locality);
    }

    /**
     * Gère les erreurs lors de la mise à jour de l'adresse.
     *
     * @param request  L'objet HttpServletRequest contenant la requête du client.
     * @param response L'objet HttpServletResponse contenant la réponse envoyée au client.
     * @param e        L'exception levée lors de la mise à jour.
     * @throws IOException Si une erreur d'entrée/sortie survient.
     */
    private void handlePostError(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException {
        HttpSession session = request.getSession();
        if (e instanceof IllegalArgumentException) {
            logger.error("Erreur lors de la mise à jour de l'adresse : " + e.getMessage());
            session.setAttribute(ERROR_MESSAGE, "Erreur lors de la mise à jour de l'adresse : " + e.getMessage());
        } else {
            logger.error("Erreur inattendue lors de la mise à jour de l'adresse", e);
            session.setAttribute(ERROR_MESSAGE, "Une erreur inattendue est survenue lors de la mise à jour de l'adresse.");
        }

        response.sendRedirect(request.getContextPath() + "/AddressServlet");
    }
}
