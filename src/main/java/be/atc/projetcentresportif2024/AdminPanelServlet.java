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
import java.util.Optional;

@WebServlet(name = "AdminPanelServlet", value = "/AdminPanelServlet")
public class AdminPanelServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(AdminPanelServlet.class);

    private final UserService userService;

    public AdminPanelServlet() {
        this.userService = new UserServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Entrée dans doGet de AdminPanelServlet");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            logger.warn("Aucune session active ou utilisateur non connecté. Redirection vers la page de connexion.");
            response.sendRedirect(request.getContextPath() + "/main?action=login");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user == null || user.getFkRole().getId() != 3) {
            logger.warn("Utilisateur non autorisé (non administrateur). Redirection vers la page d'accueil.");
            response.sendRedirect(request.getContextPath() + "/main?action=home");
            return;
        }

        // Récupération des paramètres de recherche et de pagination
        String searchQuery = request.getParameter("searchQuery");
        String pageParam = request.getParameter("page");
        int currentPage = (pageParam != null) ? Integer.parseInt(pageParam) : 1;
        int usersPerPage = 10;

        // Récupérer la liste des utilisateurs et appliquer la recherche
        List<User> allUsers = userService.findAll();
        List<User> filteredUsers = userService.filterUsers(allUsers, searchQuery);

        // Calcul pour la pagination
        int totalUsers = filteredUsers.size();
        int totalPages = (int) Math.ceil((double) totalUsers / usersPerPage);
        int startIndex = (currentPage - 1) * usersPerPage;
        int endIndex = Math.min(startIndex + usersPerPage, totalUsers);

        // Utilisateurs pour la page actuelle
        List<User> usersToShow = filteredUsers.subList(startIndex, endIndex);

        // Définir les attributs pour l'affichage dans la JSP
        request.setAttribute("users", usersToShow);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("searchQuery", searchQuery);

        // Rediriger vers la page adminPanel.jsp
        request.getRequestDispatcher("/WEB-INF/jsp/adminPanel.jsp").forward(request, response);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Entrée dans doPost de AdminPanelServlet");

        String action = request.getParameter("action");

        if ("updateUsers".equals(action)) {
            handleUserUpdates(request);
        } else if (request.getParameter("deleteUser") != null) {
            handleUserDeletion(request);
        }

        response.sendRedirect(request.getContextPath() + "/AdminPanelServlet");
    }

    private void handleUserUpdates(HttpServletRequest request) {
        List<User> allUsers = userService.findAll();

        for (User user : allUsers) {
            String roleParam = request.getParameter("roles_" + user.getId());
            String activeParam = request.getParameter("active_" + user.getId());
            String blacklistParam = request.getParameter("blacklist_" + user.getId());

            if (roleParam != null) {
                user.getFkRole().setId(Integer.parseInt(roleParam));
            }
            if (activeParam != null) {
                user.setActive(true);
            } else {
                user.setActive(false);
            }
            if (blacklistParam != null) {
                user.setBlacklist(true);
            } else {
                user.setBlacklist(false);
            }

            userService.updateUser(user);
            logger.info("Utilisateur ID " + user.getId() + " mis à jour.");
        }
    }

    private void handleUserDeletion(HttpServletRequest request) {
        String userIdStr = request.getParameter("deleteUser");
        if (userIdStr != null && !userIdStr.isEmpty()) {
            try {
                int userId = Integer.parseInt(userIdStr);
                userService.deleteById(userId);
                logger.info("Utilisateur avec l'ID " + userId + " supprimé avec succès.");
            } catch (NumberFormatException e) {
                logger.error("ID utilisateur invalide: " + userIdStr);
            }
        }
    }
}
