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
import java.util.Optional;

@WebServlet(name = "ProfileServlet", value = "/ProfileServlet")
public class ProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(ProfileServlet.class);
    private final UserService userService;

    public ProfileServlet() {
        this.userService = new UserServiceImpl();
    }

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

        // Récupération de l'utilisateur connecté
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            logger.warn("Utilisateur non trouvé dans la session, redirection vers la page de connexion");
            response.sendRedirect(request.getContextPath() + "/main?action=login");
            return;
        }

        // Vérification de l'utilisateur dont le profil doit être édité
        String userIdStr = request.getParameter("userId");
        User userToEdit = currentUser; // Par défaut, l'utilisateur connecté

        if (userIdStr != null) {
            try {
                int userId = Integer.parseInt(userIdStr);
                Optional<User> userOptional = userService.findById(userId);
                if (userOptional.isPresent()) {
                    userToEdit = userOptional.get();
                } else {
                    logger.warn("Utilisateur avec l'ID " + userId + " non trouvé.");
                    session.setAttribute("errorMessage", "Utilisateur non trouvé.");
                    response.sendRedirect(request.getContextPath() + "/AdminPanelServlet");
                    return;
                }
            } catch (NumberFormatException e) {
                logger.error("ID utilisateur invalide: " + userIdStr);
                session.setAttribute("errorMessage", "ID utilisateur invalide.");
                response.sendRedirect(request.getContextPath() + "/AdminPanelServlet");
                return;
            }
        }

        // Stocker l'utilisateur à modifier dans la session sous l'attribut 'currentEditUser'
        session.setAttribute("currentEditUser", userToEdit);

        // Redirection vers AddressServlet pour charger les localités et afficher la page de profil
        response.sendRedirect(request.getContextPath() + "/AddressServlet");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setRequestEncoding(request, response);

        logger.info("Entrée dans doPost de ProfileServlet");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            logger.warn("Aucune session valide, redirection vers la page de connexion");
            response.sendRedirect(request.getContextPath() + "/main?action=login");
            return;
        }

        // Récupération de l'utilisateur à modifier (soit l'utilisateur connecté, soit un autre utilisateur)
        User userToEdit = (User) session.getAttribute("currentEditUser");
        if (userToEdit == null) {
            logger.warn("Utilisateur non trouvé pour la modification, redirection vers la page de connexion");
            response.sendRedirect(request.getContextPath() + "/main?action=login");
            return;
        }

        String newEmail = request.getParameter("email");
        String confirmEmail = request.getParameter("confirmEmail");
        String newPassword = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String phone = request.getParameter("phone");
        String gender = request.getParameter("gender");
        String birthdate = request.getParameter("birthdate");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String blacklist = request.getParameter("blacklist");
        String active = request.getParameter("active");
        String fkRole = request.getParameter("fkRole");

        boolean isAdmin = ((User) session.getAttribute("user")).getFkRole().getId() == 3;

        try {
            User updatedUser = userService.validateAndPrepareUserForUpdate(userToEdit, newEmail, confirmEmail, newPassword, confirmPassword, phone, gender, birthdate, firstName, lastName, blacklist, active, fkRole, isAdmin);
            userService.updateUser(updatedUser);

            session.setAttribute("currentEditUser", updatedUser);
            session.setAttribute("successMessage", "Profil mis à jour avec succès.");

            response.sendRedirect(request.getContextPath() + "/AddressServlet");

        } catch (IllegalArgumentException e) {
            handleUserUpdateError(request, response, e.getMessage());
        }
    }

    private void setRequestEncoding(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
    }

    private void handleUserUpdateError(HttpServletRequest request, HttpServletResponse response, String errorCode) throws IOException {
        String errorMessage;
        switch (errorCode) {
            case "INVALID_FIRST_NAME": errorMessage = "Le prénom est invalide."; break;
            case "INVALID_LAST_NAME": errorMessage = "Le nom de famille est invalide."; break;
            case "INVALID_EMAIL_FORMAT": errorMessage = "Le format de l'email est invalide."; break;
            case "PASSWORDS_DO_NOT_MATCH": errorMessage = "Les mots de passe ne correspondent pas."; break;
            case "INVALID_PASSWORD_FORMAT": errorMessage = "Le format du mot de passe est invalide."; break;
            case "INVALID_PHONE_NUMBER": errorMessage = "Le numéro de téléphone est invalide."; break;
            case "INVALID_GENDER": errorMessage = "Le genre est invalide."; break;
            case "INVALID_BIRTHDATE": errorMessage = "La date de naissance est invalide."; break;
            case "EMAIL_ALREADY_EXISTS": errorMessage = "Cet email est déjà utilisé."; break;
            case "EMAILS_DO_NOT_MATCH": errorMessage = "Les emails ne correspondent pas."; break;
            case "INVALID_ROLE": errorMessage = "Le rôle sélectionné est invalide."; break;
            default: errorMessage = "Une erreur est survenue lors de la mise à jour du profil.";
        }

        logger.error("Erreur lors de la mise à jour du profil : " + errorMessage);
        HttpSession session = request.getSession();
        session.setAttribute("errorMessage", errorMessage);
        response.sendRedirect(request.getContextPath() + "/AddressServlet");
    }
}
