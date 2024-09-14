package be.atc.projetcentresportif2024;

import be.atc.entities.User;
import be.atc.services.UserService;
import be.atc.services.impl.UserServiceImpl;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet responsable de la gestion de l'inscription des utilisateurs.
 * Permet l'affichage du formulaire d'inscription et la création d'un nouvel utilisateur.
 */
@WebServlet(name = "RegisterServlet", value = "/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService;
    private static final Logger logger = Logger.getLogger(RegisterServlet.class);

    /**
     * Méthode d'initialisation de la servlet. Initialise le service utilisateur.
     *
     * @throws ServletException Si une erreur survient lors de l'initialisation.
     */
    @Override
    public void init() throws ServletException {
        logger.info("Initialisation de RegisterServlet");
        userService = new UserServiceImpl(); // Initialisation directe de UserServiceImpl sans passer un DAO
    }

    /**
     * Gère les requêtes GET pour afficher la page d'inscription.
     *
     * @param request  L'objet HttpServletRequest contenant la requête du client.
     * @param response L'objet HttpServletResponse contenant la réponse envoyée au client.
     * @throws ServletException Si une erreur survient lors du traitement de la requête.
     * @throws IOException      Si une erreur d'entrée/sortie survient.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Accès à la page d'inscription (GET)");
        request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
    }

    /**
     * Gère les requêtes POST pour créer un nouvel utilisateur.
     * Valide les données saisies, crée l'utilisateur ou renvoie les erreurs correspondantes.
     *
     * @param request  L'objet HttpServletRequest contenant la requête du client.
     * @param response L'objet HttpServletResponse contenant la réponse envoyée au client.
     * @throws ServletException Si une erreur survient lors du traitement de la requête.
     * @throws IOException      Si une erreur d'entrée/sortie survient.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Tentative d'inscription (POST)");

        // Suppression proactive de tous les attributs d'erreur pour éviter tout affichage accidentel
        request.removeAttribute("emailError");
        request.removeAttribute("passwordError");
        request.removeAttribute("confirmPasswordError");
        request.removeAttribute("generalError");

        // Encoding en UTF-8 pour la base de données
        setRequestEncoding(request, response);

        User user = extractUserFromRequest(request);
        String confirmPassword = request.getParameter("confirmPassword");

        try {
            userService.createUser(user, confirmPassword);
            logger.info("Utilisateur créé avec succès, redirection vers la page de connexion.");

            // Ajouter un message de succès dans la session lors d'une inscription réussie
            HttpSession session = request.getSession();
            session.setAttribute("successMessage", "Inscription réussie ! Vous pouvez maintenant vous connecter.");

            // Redirection vers la MainServlet avec l'action "login"
            response.sendRedirect(request.getContextPath() + "/main?action=login");

        } catch (IllegalArgumentException e) {
            handleRegistrationError(request, response, e, user);
        } catch (Exception e) {
            handleUnexpectedError(request, response, e);
        }
    }

    /**
     * Définit l'encodage des requêtes et des réponses pour garantir la prise en charge de l'UTF-8.
     *
     * @param request  L'objet HttpServletRequest contenant la requête du client.
     * @param response L'objet HttpServletResponse contenant la réponse envoyée au client.
     * @throws IOException Si une erreur d'entrée/sortie survient lors de la définition de l'encodage.
     */
    private void setRequestEncoding(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
    }

    /**
     * Extrait les informations utilisateur à partir des paramètres de la requête.
     *
     * @param request L'objet HttpServletRequest contenant la requête du client.
     * @return Un objet User contenant les informations extraites.
     */
    private User extractUserFromRequest(HttpServletRequest request) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");

        logger.debug("Email: " + email);
        logger.debug("First Name: " + firstName);
        logger.debug("Last Name: " + lastName);

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);

        return user;
    }

    /**
     * Gère les erreurs spécifiques survenues lors de l'inscription.
     *
     * @param request  L'objet HttpServletRequest contenant la requête du client.
     * @param response L'objet HttpServletResponse contenant la réponse envoyée au client.
     * @param e        L'exception qui a été lancée.
     * @param user     L'utilisateur en cours de création.
     * @throws ServletException Si une erreur survient lors du traitement de la requête.
     * @throws IOException      Si une erreur d'entrée/sortie survient.
     */
    private void handleRegistrationError(HttpServletRequest request, HttpServletResponse response, IllegalArgumentException e, User user) throws ServletException, IOException {
        logger.error("Erreur lors de l'inscription: " + e.getMessage());

        // Préremplir les champs avec les valeurs saisies
        request.setAttribute("email", user.getEmail());
        request.setAttribute("firstName", user.getFirstName());
        request.setAttribute("lastName", user.getLastName());

        // Gérer les erreurs spécifiques avec un switch
        handleSpecificErrors(request, e.getMessage());

        request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
    }

    /**
     * Gère les erreurs spécifiques en fonction du code d'erreur fourni.
     *
     * @param request   L'objet HttpServletRequest contenant la requête du client.
     * @param errorCode Le code d'erreur fourni.
     */
    private void handleSpecificErrors(HttpServletRequest request, String errorCode) {
        String errorMessage = null; // Initialisation de la variable à null pour l'utiliser dans le "if" après le switch. Sinon ça ne fonctionnera pas. Java Logique.

        switch (errorCode) {
            case "INVALID_EMAIL_FORMAT":
                errorMessage = "Le format de l'adresse e-mail est incorrect.";
                request.setAttribute("emailError", errorMessage);
                break;
            case "PASSWORDS_DO_NOT_MATCH":
                errorMessage = "Les mots de passe ne correspondent pas.";
                request.setAttribute("confirmPasswordError", errorMessage);
                break;
            case "INVALID_PASSWORD_FORMAT":
                errorMessage = "Le mot de passe doit contenir au moins 8 caractères, une majuscule, et un chiffre.";
                request.setAttribute("passwordError", errorMessage);
                break;
            case "EMAIL_ALREADY_EXISTS":
                errorMessage = "Cet e-mail est déjà utilisé.";
                request.setAttribute("emailError", errorMessage);
                break;
            case "INVALID_FIRST_NAME":
                errorMessage = "Le prénom est invalide. Il ne doit contenir que des lettres, des espaces, des apostrophes ou des traits d'union.";
                request.setAttribute("firstNameError", errorMessage);
                break;
            case "INVALID_LAST_NAME":
                errorMessage = "Le nom de famille est invalide. Il ne doit contenir que des lettres, des espaces, des apostrophes ou des traits d'union.";
                request.setAttribute("lastNameError", errorMessage);
                break;
            default:
                // Si l'erreur ne correspond à aucun des cas précédents
                if (errorCode != null) {
                    errorMessage = "Erreur inconnue. Veuillez réessayer.";
                    request.setAttribute("generalError", errorMessage);
                    logger.error("Erreur inconnue détectée : " + errorMessage);
                }
                break;
        }

        if (errorMessage == null) {
            logger.debug("Aucune erreur spécifique détectée, suppression de generalError.");
            request.removeAttribute("generalError");
        }
        logger.debug("Erreur détectée : " + errorCode + " avec le message : " + errorMessage);
    }

    /**
     * Gère les erreurs inattendues survenues lors du traitement de la requête.
     *
     * @param request  L'objet HttpServletRequest contenant la requête du client.
     * @param response L'objet HttpServletResponse contenant la réponse envoyée au client.
     * @param e        L'exception qui a été lancée.
     * @throws ServletException Si une erreur survient lors du traitement de la requête.
     * @throws IOException      Si une erreur d'entrée/sortie survient.
     */
    private void handleUnexpectedError(HttpServletRequest request, HttpServletResponse response, Exception e) throws ServletException, IOException {
        logger.error("Erreur inattendue: " + e.getMessage());
        request.setAttribute("errorMessage", "Une erreur inattendue s'est produite. Veuillez réessayer.");
        request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
    }
}
