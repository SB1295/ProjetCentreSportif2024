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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Servlet pour gérer le panneau d'administration des utilisateurs.
 * Permet de rechercher, modifier, et supprimer des utilisateurs.
 */
@WebServlet(name = "AdminPanelServlet", value = "/AdminPanelServlet")
public class AdminPanelServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(AdminPanelServlet.class);

    // Constantes pour les rôles et les paramètres de requête
    private static final int ADMIN_ROLE_ID = 3;
    private static final String ACTION_UPDATE_USERS = "updateUsers";
    private static final String PARAM_SEARCH_QUERY = "searchQuery";
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_ACTION = "action";
    private static final String PARAM_DELETE_USER = "deleteUser";

    private final UserService userService;

    /**
     * Constructeur par défaut qui initialise le service utilisateur.
     */
    public AdminPanelServlet() {
        this.userService = new UserServiceImpl();
    }

    /**
     * Méthode doGet pour gérer les requêtes GET.
     * Vérifie si l'utilisateur est un administrateur, applique la pagination et affiche la liste des utilisateurs.
     *
     * @param request  L'objet HttpServletRequest contenant la requête du client.
     * @param response L'objet HttpServletResponse contenant la réponse envoyée au client.
     * @throws ServletException Si une erreur survient lors du traitement de la requête.
     * @throws IOException      Si une erreur d'entrée/sortie survient.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Entrée dans doGet de AdminPanelServlet");

        HttpSession session = request.getSession(false);
        if (session == null || !isUserAdmin(session)) {
            logger.warn("Utilisateur non autorisé ou session invalide. Redirection vers la page de connexion.");
            response.sendRedirect(request.getContextPath() + "/main?action=login");
            return;
        }

        String searchQuery = request.getParameter(PARAM_SEARCH_QUERY);
        String pageParam = request.getParameter(PARAM_PAGE);
        int currentPage = (pageParam != null) ? Integer.parseInt(pageParam) : 1;
        int usersPerPage = 10;

        List<User> filteredUsers = userService.filterUsers(userService.findAll(), searchQuery);
        paginateUsers(request, filteredUsers, currentPage, usersPerPage);

        request.getRequestDispatcher("/WEB-INF/jsp/adminPanel.jsp").forward(request, response);
    }

    /**
     * Vérifie si l'utilisateur actuel est un administrateur.
     *
     * @param session La session HTTP actuelle.
     * @return true si l'utilisateur est un administrateur, sinon false.
     */
    private boolean isUserAdmin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user != null && user.getFkRole().getId() == ADMIN_ROLE_ID;
    }

    /**
     * Applique la pagination sur la liste des utilisateurs filtrés.
     *
     * @param request      L'objet HttpServletRequest contenant la requête du client.
     * @param filteredUsers La liste des utilisateurs filtrés.
     * @param currentPage  La page actuelle.
     * @param usersPerPage Le nombre d'utilisateurs par page.
     */
    private void paginateUsers(HttpServletRequest request, List<User> filteredUsers, int currentPage, int usersPerPage) {
        int totalUsers = filteredUsers.size();
        int totalPages = (int) Math.ceil((double) totalUsers / usersPerPage);
        int startIndex = (currentPage - 1) * usersPerPage;
        int endIndex = Math.min(startIndex + usersPerPage, totalUsers);

        request.setAttribute("users", filteredUsers.subList(startIndex, endIndex));
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute(PARAM_SEARCH_QUERY, request.getParameter(PARAM_SEARCH_QUERY));
    }

    /**
     * Méthode doPost pour gérer les requêtes POST.
     * Traite les actions de mise à jour ou de suppression des utilisateurs.
     *
     * @param request  L'objet HttpServletRequest contenant la requête du client.
     * @param response L'objet HttpServletResponse contenant la réponse envoyée au client.
     * @throws ServletException Si une erreur survient lors du traitement de la requête.
     * @throws IOException      Si une erreur d'entrée/sortie survient.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Entrée dans doPost de AdminPanelServlet");

        String action = request.getParameter(PARAM_ACTION);

        if (ACTION_UPDATE_USERS.equals(action)) {
            handleUserUpdates(request);
        } else if (request.getParameter(PARAM_DELETE_USER) != null) {
            handleUserDeletion(request);
        }

        response.sendRedirect(request.getContextPath() + "/AdminPanelServlet");
    }

    /**
     * Gère la mise à jour des utilisateurs en fonction des données envoyées dans la requête.
     *
     * @param request L'objet HttpServletRequest contenant la requête du client.
     */
    private void handleUserUpdates(HttpServletRequest request) {
        List<User> allUsers = userService.findAll();

        for (User user : allUsers) {
            updateUserFields(request, user);
            userService.updateUser(user);
            logger.info("Utilisateur ID " + user.getId() + " mis à jour.");
        }
    }

    /**
     * Met à jour les champs d'un utilisateur en fonction des paramètres de la requête.
     *
     * @param request L'objet HttpServletRequest contenant la requête du client.
     * @param user    L'utilisateur à mettre à jour.
     */
    private void updateUserFields(HttpServletRequest request, User user) {
        String roleParam = request.getParameter("roles_" + user.getId());
        String activeParam = request.getParameter("active_" + user.getId());
        String blacklistParam = request.getParameter("blacklist_" + user.getId());

        if (roleParam != null) {
            user.getFkRole().setId(Integer.parseInt(roleParam));
        }
        user.setActive(activeParam != null);
        user.setBlacklist(blacklistParam != null);
    }

    /**
     * Gère la suppression d'un utilisateur en fonction des données envoyées dans la requête.
     *
     * @param request L'objet HttpServletRequest contenant la requête du client.
     */
    private void handleUserDeletion(HttpServletRequest request) {
        String userIdStr = request.getParameter(PARAM_DELETE_USER);
        try {
            int userId = Integer.parseInt(userIdStr);
            userService.deleteById(userId);
            logger.info("Utilisateur avec l'ID " + userId + " supprimé avec succès.");
        } catch (NumberFormatException e) {
            logger.error("ID utilisateur invalide: " + userIdStr, e);
        }
    }
}
