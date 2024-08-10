package be.atc.projetcentresportif2024;

import be.atc.entities.User;
import be.atc.services.UserService;
import be.atc.services.impl.UserServiceImpl;
import be.atc.dao.impl.UserDaoImpl;

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

    @Override
    public void init() throws ServletException {
        userService = new UserServiceImpl(new UserDaoImpl());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Accès à la page d'inscription (GET)");
        request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Tentative d'inscription (POST)");

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
            System.out.println("Utilisateur créé avec succès, redirection vers la page de connexion.");
            response.sendRedirect("login");

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

        System.out.println("Email: " + email);
        System.out.println("First Name: " + firstName);
        System.out.println("Last Name: " + lastName);

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);

        return user;
    }

    private void handleRegistrationError(HttpServletRequest request, HttpServletResponse response, IllegalArgumentException e, User user) throws ServletException, IOException {
        System.out.println("Erreur lors de l'inscription: " + e.getMessage());

        // Pré-remplir les champs avec les valeurs saisies
        request.setAttribute("email", user.getEmail());
        request.setAttribute("firstName", user.getFirstName());
        request.setAttribute("lastName", user.getLastName());

        // Vérification message d'erreur exact
        System.out.println("Message d'erreur capturé: " + e.getMessage());

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
            default:
                // Si l'erreur ne correspond à aucun des cas précédents
                if (errorCode != null) {
                    errorMessage = "Erreur inconnue. Veuillez réessayer.";
                    request.setAttribute("generalError", errorMessage);
                    System.out.println("Erreur inconnue détectée : " + errorMessage);
                }
                break;
        }

        if (errorMessage == null) {
            System.out.println("Aucune erreur spécifique détectée, suppression de generalError.");
            request.removeAttribute("generalError");
        }
        System.out.println("Erreur détectée : " + errorCode + " avec le message : " + errorMessage);
    }

    private void handleUnexpectedError(HttpServletRequest request, HttpServletResponse response, Exception e) throws ServletException, IOException {
        System.out.println("Erreur inattendue: " + e.getMessage());
        request.setAttribute("errorMessage", "Une erreur inattendue s'est produite. Veuillez réessayer.");
        request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
    }
}
