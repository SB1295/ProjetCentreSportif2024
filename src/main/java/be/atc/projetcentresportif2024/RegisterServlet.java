package be.atc.projetcentresportif2024;

import be.atc.entities.User;
import be.atc.services.UserService;
import be.atc.services.impl.UserServiceImpl;
import org.apache.log4j.Logger;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RegisterServlet", value = "/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService;
    private static final Logger logger = Logger.getLogger(RegisterServlet.class);

    @Override
    public void init() throws ServletException {
        logger.info("Initialisation de RegisterServlet");
        userService = new UserServiceImpl(); // Initialisation directe de UserServiceImpl sans passer un DAO
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Accès à la page d'inscription (GET)");
        request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
    }

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
            // Redirection vers la MainServlet avec l'action "login"
            response.sendRedirect(request.getContextPath() + "/main?action=login");

        } catch (IllegalArgumentException e) {
            handleRegistrationError(request, response, e, user);
        } catch (Exception e) {
            handleUnexpectedError(request, response, e);
        }
    }

    private void setRequestEncoding(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
    }

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

    private void handleUnexpectedError(HttpServletRequest request, HttpServletResponse response, Exception e) throws ServletException, IOException {
        logger.error("Erreur inattendue: " + e.getMessage());
        request.setAttribute("errorMessage", "Une erreur inattendue s'est produite. Veuillez réessayer.");
        request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
    }
}
