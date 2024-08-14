package be.atc.projetcentresportif2024;

import be.atc.entities.User;
import be.atc.services.UserService;
import be.atc.services.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import org.apache.log4j.Logger;

@WebServlet(name = "ProfileServlet", value = "/ProfileServlet")
public class ProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Initialisation du logger pour le suivi des événements
    private static final Logger logger = Logger.getLogger(ProfileServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Entrée dans doGet de ProfileServlet");

        // Vérification de la session
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            logger.warn("Aucune session valide, redirection vers la page de connexion");
            response.sendRedirect(request.getContextPath() + "/main?action=login");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            logger.warn("Utilisateur non trouvé dans la session, redirection vers la page de connexion");
            response.sendRedirect(request.getContextPath() + "/main?action=login");
            return;
        }

        // Rediriger l'utilisateur vers la page de profil
        logger.info("Utilisateur trouvé dans la session, redirection vers profile.jsp");
        request.getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Défini l'encodage pour les requêtes et réponses en UFT-8
        setRequestEncoding(request, response);

        logger.info("Entrée dans doPost de ProfileServlet");

        // Vérification de la session
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            logger.warn("Aucune session valide, redirection vers la page de connexion");
            response.sendRedirect(request.getContextPath() + "/main?action=login");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            logger.warn("Utilisateur non trouvé dans la session, redirection vers la page de connexion");
            response.sendRedirect(request.getContextPath() + "/main?action=login");
            return;
        }

        // Récupération des données du formulaire
        String newEmail = request.getParameter("email");
        String confirmEmail = request.getParameter("confirmEmail");
        String newPassword = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String phone = request.getParameter("phone");
        String gender = request.getParameter("gender");
        String birthdate = request.getParameter("birthdate");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");

        // Champs spécifiques à l'administration
        String blacklist = request.getParameter("blacklist");
        String active = request.getParameter("active");
        String fkRole = request.getParameter("fkRole");

        // Déterminer si l'utilisateur est un administrateur
        boolean isAdmin = user.getFkRole().getId() == 3;

        try {
            UserService userService = new UserServiceImpl();
            User updatedUser = userService.validateAndPrepareUserForUpdate(user, newEmail, confirmEmail, newPassword, confirmPassword, phone, gender, birthdate, firstName, lastName, blacklist, active, fkRole, isAdmin);

            // Appel de la méthode updateUser pour sauvegarder les modifications
            userService.updateUser(updatedUser);

            // Mise à jour de l'objet User dans la session après modification
            session.setAttribute("user", updatedUser);

            request.setAttribute("successMessage", "Profil mis à jour avec succès.");
            request.getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(request, response);

        } catch (IllegalArgumentException e) {
            handleUserUpdateError(request, response, e.getMessage());
        }
    }
    private void setRequestEncoding(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
    }

    private void handleUserUpdateError(HttpServletRequest request, HttpServletResponse response, String errorCode) throws ServletException, IOException {
        String errorMessage;
        switch (errorCode) {
            case "INVALID_FIRST_NAME":
                errorMessage = "Le prénom est invalide.";
                break;
            case "INVALID_LAST_NAME":
                errorMessage = "Le nom de famille est invalide.";
                break;
            case "INVALID_EMAIL_FORMAT":
                errorMessage = "Le format de l'email est invalide.";
                break;
            case "PASSWORDS_DO_NOT_MATCH":
                errorMessage = "Les mots de passe ne correspondent pas.";
                break;
            case "INVALID_PASSWORD_FORMAT":
                errorMessage = "Le format du mot de passe est invalide.";
                break;
            case "INVALID_PHONE_NUMBER":
                errorMessage = "Le numéro de téléphone est invalide.";
                break;
            case "INVALID_GENDER":
                errorMessage = "Le genre est invalide.";
                break;
            case "INVALID_BIRTHDATE":
                errorMessage = "La date de naissance est invalide.";
                break;
            case "EMAIL_ALREADY_EXISTS":
                errorMessage = "Cet email est déjà utilisé.";
                break;
            case "EMAILS_DO_NOT_MATCH":
                errorMessage = "Les emails ne correspondent pas.";
                break;
            case "INVALID_ROLE":
                errorMessage = "Le rôle sélectionné est invalide.";
                break;
            default:
                errorMessage = "Une erreur est survenue lors de la mise à jour du profil.";
        }

        logger.error("Erreur lors de la mise à jour du profil : " + errorMessage);
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(request, response);
    }

}
