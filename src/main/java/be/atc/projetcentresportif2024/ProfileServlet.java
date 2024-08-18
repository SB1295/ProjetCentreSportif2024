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

/**
 * Servlet responsable de la gestion du profil utilisateur.
 * Permet la visualisation et la mise à jour des informations de l'utilisateur connecté ou d'un utilisateur sélectionné.
 */
@WebServlet(name = "ProfileServlet", value = "/ProfileServlet")
public class ProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(ProfileServlet.class);
    private final UserService userService;

    /**
     * Constructeur par défaut initialisant le service utilisateur.
     */
    public ProfileServlet() {
        this.userService = new UserServiceImpl();
    }

    /**
     * Gère les requêtes GET pour afficher le profil utilisateur.
     * Vérifie la session, charge l'utilisateur à éditer et redirige vers la page de profil.
     *
     * @param request  L'objet HttpServletRequest contenant la requête du client.
     * @param response L'objet HttpServletResponse contenant la réponse envoyée au client.
     * @throws ServletException Si une erreur survient lors du traitement de la requête.
     * @throws IOException      Si une erreur d'entrée/sortie survient.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Entrée dans doGet de ProfileServlet");

        // Vérification de la session
        HttpSession session = validateSession(request, response);
        if (session == null) return;

        // Gérer les messages de la session
        manageSessionMessages(request, session);

        User currentUser = (User) session.getAttribute("user");
        User userToEdit = getUserToEdit(request, response, session, currentUser);
        if (userToEdit == null) return;

        // Stocker l'utilisateur à modifier dans la session sous l'attribut 'currentEditUser'
        session.setAttribute("currentEditUser", userToEdit);

        // Redirection vers AddressServlet pour charger les localités et afficher la page de profil
        response.sendRedirect(request.getContextPath() + "/AddressServlet");
    }

    /**
     * Gère les requêtes POST pour mettre à jour les informations du profil utilisateur.
     * Valide et met à jour les informations de l'utilisateur, puis redirige vers la page de profil.
     *
     * @param request  L'objet HttpServletRequest contenant la requête du client.
     * @param response L'objet HttpServletResponse contenant la réponse envoyée au client.
     * @throws ServletException Si une erreur survient lors du traitement de la requête.
     * @throws IOException      Si une erreur d'entrée/sortie survient.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setRequestEncoding(request, response);
        logger.info("Entrée dans doPost de ProfileServlet");

        HttpSession session = validateSession(request, response);
        if (session == null) return;

        User userToEdit = (User) session.getAttribute("currentEditUser");
        if (userToEdit == null) {
            logger.warn("Utilisateur non trouvé pour la modification, redirection vers la page de connexion");
            response.sendRedirect(request.getContextPath() + "/main?action=login");
            return;
        }

        try {
            User updatedUser = updateUserFromRequest(request, session, userToEdit);

            // Ajout de la méthode updateUser pour persister les changements
            logger.debug("Tentative de mise à jour de l'utilisateur : " + updatedUser.getId());
            userService.updateUser(updatedUser);
            logger.debug("Mise à jour effectuée pour l'utilisateur : " + updatedUser.getId());

            session.setAttribute("currentEditUser", updatedUser);
            session.setAttribute("successMessage", "Profil mis à jour avec succès.");
            response.sendRedirect(request.getContextPath() + "/AddressServlet");
        } catch (IllegalArgumentException e) {
            handleUserUpdateError(request, response, e.getMessage());
        }
    }

    /**
     * Valide la session en cours. Si elle est invalide, redirige vers la page de connexion.
     *
     * @param request  L'objet HttpServletRequest contenant la requête du client.
     * @param response L'objet HttpServletResponse contenant la réponse envoyée au client.
     * @return La session valide, ou null si la session est invalide.
     * @throws IOException Si une erreur d'entrée/sortie survient lors de la redirection.
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
     * Récupère l'utilisateur à éditer en fonction des paramètres de la requête et de la session.
     *
     * @param request      L'objet HttpServletRequest contenant la requête du client.
     * @param response     L'objet HttpServletResponse contenant la réponse envoyée au client.
     * @param session      La session HTTP en cours.
     * @param currentUser  L'utilisateur actuellement connecté.
     * @return L'utilisateur à éditer, ou null si l'utilisateur n'est pas trouvé.
     * @throws IOException Si une erreur d'entrée/sortie survient lors de la redirection.
     */
    private User getUserToEdit(HttpServletRequest request, HttpServletResponse response, HttpSession session, User currentUser) throws IOException {
        String userIdStr = request.getParameter("userId");
        User userToEdit = currentUser;

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
                    return null;
                }
            } catch (NumberFormatException e) {
                logger.error("ID utilisateur invalide: " + userIdStr);
                session.setAttribute("errorMessage", "ID utilisateur invalide.");
                response.sendRedirect(request.getContextPath() + "/AdminPanelServlet");
                return null;
            }
        }
        return userToEdit;
    }

    /**
     * Met à jour les informations de l'utilisateur à partir des paramètres de la requête.
     *
     * @param request     L'objet HttpServletRequest contenant la requête du client.
     * @param session     La session HTTP en cours.
     * @param userToEdit  L'utilisateur à modifier.
     * @return L'utilisateur avec les informations mises à jour.
     */
    private User updateUserFromRequest(HttpServletRequest request, HttpSession session, User userToEdit) {
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

        return userService.validateAndPrepareUserForUpdate(userToEdit, newEmail, confirmEmail, newPassword, confirmPassword, phone, gender, birthdate, firstName, lastName, blacklist, active, fkRole, isAdmin);
    }

    /**
     * Définit l'encodage des requêtes et des réponses pour assurer la prise en charge de l'UTF-8.
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
     * Gère les erreurs survenues lors de la mise à jour du profil utilisateur.
     *
     * @param request     L'objet HttpServletRequest contenant la requête du client.
     * @param response    L'objet HttpServletResponse contenant la réponse envoyée au client.
     * @param errorCode   Le code d'erreur renvoyé par la mise à jour.
     * @throws IOException Si une erreur d'entrée/sortie survient lors de la redirection.
     */
    private void handleUserUpdateError(HttpServletRequest request, HttpServletResponse response, String errorCode) throws IOException {
        String errorMessage = getErrorMessage(errorCode);

        logger.error("Erreur lors de la mise à jour du profil : " + errorMessage);
        HttpSession session = request.getSession();
        session.setAttribute("errorMessage", errorMessage);
        response.sendRedirect(request.getContextPath() + "/AddressServlet");
    }

    /**
     * Retourne un message d'erreur approprié en fonction du code d'erreur fourni.
     *
     * @param errorCode Le code d'erreur.
     * @return Le message d'erreur correspondant.
     */
    private String getErrorMessage(String errorCode) {
        switch (errorCode) {
            case "INVALID_FIRST_NAME": return "Le prénom est invalide.";
            case "INVALID_LAST_NAME": return "Le nom de famille est invalide.";
            case "INVALID_EMAIL_FORMAT": return "Le format de l'email est invalide.";
            case "PASSWORDS_DO_NOT_MATCH": return "Les mots de passe ne correspondent pas.";
            case "INVALID_PASSWORD_FORMAT": return "Le format du mot de passe est invalide.";
            case "INVALID_PHONE_NUMBER": return "Le numéro de téléphone est invalide.";
            case "INVALID_GENDER": return "Le genre est invalide.";
            case "INVALID_BIRTHDATE": return "La date de naissance est invalide.";
            case "EMAIL_ALREADY_EXISTS": return "Cet email est déjà utilisé.";
            case "EMAILS_DO_NOT_MATCH": return "Les emails ne correspondent pas.";
            case "INVALID_ROLE": return "Le rôle sélectionné est invalide.";
            default: return "Une erreur est survenue lors de la mise à jour du profil.";
        }
    }

    /**
     * Gère les messages de succès ou d'erreur stockés dans la session.
     * Supprime les messages après les avoir transférés dans les attributs de requête.
     *
     * @param request  L'objet HttpServletRequest contenant la requête du client.
     * @param session  La session HTTP en cours.
     */
    private void manageSessionMessages(HttpServletRequest request, HttpSession session) {
        // Gestion des messages de succès ou d'erreur
        String successMessage = (String) session.getAttribute("successMessage");
        if (successMessage != null) {
            request.setAttribute("successMessage", successMessage);
            session.removeAttribute("successMessage"); // Supprimer l'attribut une fois affiché
        }

        String errorMessage = (String) session.getAttribute("errorMessage");
        if (errorMessage != null) {
            request.setAttribute("errorMessage", errorMessage);
            session.removeAttribute("errorMessage"); // Supprimer l'attribut une fois affiché
        }
    }

}
