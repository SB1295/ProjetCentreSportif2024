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

@WebServlet(name = "AddressServlet", value = "/AddressServlet")
public class AddressServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(AddressServlet.class);

    private final AddressService addressService;
    private final LocalityService localityService;
    //
    private final UserService userService;

    public AddressServlet() {
        this.addressService = new AddressServiceImpl();
        this.localityService = new LocalityServiceImpl(); // Initialisation de Locality
        this.userService = new UserServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Entrée dans doGet de AddressServlet");

        // Vérification de la session
        HttpSession session = validateSession(request, response);
        if (session == null) return;

        // Vérifier si c'est une requête AJAX pour récupérer les détails de la localité
        String localityIdStr = request.getParameter("localityId");
        if (localityIdStr != null) {
            handleAjaxRequest(localityIdStr, response);
        } else {
            loadLocalitiesAndForward(request, response);
        }
    }

    private void handleAjaxRequest(String localityIdStr, HttpServletResponse response) throws IOException {
        try {
            int localityId = Integer.parseInt(localityIdStr);
            Optional<Locality> localityOpt = localityService.findById(localityId);

            if (localityOpt.isPresent()) {
                Locality locality = localityOpt.get();

                String jsonResponse = String.format(
                        "{\"postalCode\": \"%s\", \"town\": \"%s\", \"province\": \"%s\", \"maintown\": \"%s\"}",
                        locality.getPostalCode(),
                        locality.getTown(),
                        locality.getProvince(),
                        locality.getMaintown()
                );

                response.setContentType("application/json");
                response.getWriter().write(jsonResponse);
                logger.info("Détails de la localité envoyés pour l'ID : " + localityId);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                logger.warn("Localité non trouvée pour l'ID : " + localityId);
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.error("ID de localité invalide : " + localityIdStr);
        }
    }

    // Chargement des localités et redirection vers profile.jsp
    private void loadLocalitiesAndForward(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = validateSession(request, response);
        if (session == null) return;

        List<Locality> localities = localityService.findAll();
        request.setAttribute("localities", localities);

        if (localities != null && !localities.isEmpty()) {
            logger.info("Nombre de localités récupérées : " + localities.size());

            /* Logger pour vérifier l'import de toutes les localités
            for (Locality locality : localities) {
                logger.debug("Localité : " + locality.getId() + " - " + locality.getPostalCode() + " - " + locality.getTown());
            }
            */
        } else {
            logger.warn("Aucune localité trouvée ou la liste des localités est vide.");
        }

        // Redirection vers profile.jsp avec les localités chargées
        request.getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Entrée dans doPost de AddressServlet");

        HttpSession session = validateSession(request, response);
        if (session == null) return;

        User user = (User) session.getAttribute("user");

        try {
            updateUserAddress(request, user);
            session.setAttribute("user", user);
            // Stocker le message de succès dans la session
            session.setAttribute("successMessage", "Adresse mise à jour avec succès.");
        } catch (Exception e) {
            handlePostError(request, response, e);
        }

        loadLocalitiesAndForward(request, response);
    }

    private HttpSession validateSession(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            logger.warn("Aucune session valide, redirection vers la page de connexion");
            response.sendRedirect(request.getContextPath() + "/main?action=login");
            return null;
        }
        return session;
    }

    private void updateUserAddress(HttpServletRequest request, User user) throws IllegalArgumentException {
        String streetName = request.getParameter("streetName");
        String number = request.getParameter("number");
        String boxNumber = request.getParameter("boxNumber");
        String localityIdStr = request.getParameter("locality");

        logger.debug("Données reçues : streetName=" + streetName + ", number=" + number + ", boxNumber=" + boxNumber + ", localityId=" + localityIdStr);

        int localityId = Integer.parseInt(localityIdStr);
        Optional<Locality> localityOpt = localityService.findById(localityId);

        if (!localityOpt.isPresent()) {
            logger.error("Localité non trouvée pour l'ID : " + localityId);
            throw new IllegalArgumentException("INVALID_LOCALITY");
        }

        Locality locality = localityOpt.get();

        Address address = user.getFkAddresse();  // Récupérer l'adresse actuelle de l'utilisateur
        if (address == null) {
            address = new Address();  // Créer une nouvelle adresse si l'utilisateur n'en a pas encore
        }

        address.setStreetName(streetName);
        address.setNumber(number);
        address.setBoxNumber(boxNumber);
        address.setFkLocality(locality);

        if (address.getId() == 0) {
            addressService.createAddress(address);
            logger.info("Nouvelle adresse créée pour l'utilisateur : " + user.getEmail());
        } else {
            addressService.updateAddress(address);
            logger.info("Adresse mise à jour pour l'utilisateur : " + user.getEmail());
        }

        // Associe l'adresse à l'utilisateur
        user.setFkAddresse(address);
        logger.info("Adresse ID " + address.getId() + " associée à l'utilisateur ID " + user.getId() + " (Email: " + user.getEmail() + ")");
        // Sauvegarde l'utilisateur avec l'adresse associée
        userService.updateUser(user);
    }

    private void handlePostError(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException {
        HttpSession session = request.getSession();
        if (e instanceof IllegalArgumentException) {
            logger.error("Erreur lors de la mise à jour de l'adresse : " + e.getMessage());
            session.setAttribute("errorMessage", "Erreur lors de la mise à jour de l'adresse : " + e.getMessage());
        } else {
            logger.error("Erreur inattendue lors de la mise à jour de l'adresse", e);
            session.setAttribute("errorMessage", "Une erreur inattendue est survenue lors de la mise à jour de l'adresse.");
        }

        // Rediriger vers le doGet pour charger les localités et afficher la page
        response.sendRedirect(request.getContextPath() + "/AddressServlet");
    }
}

