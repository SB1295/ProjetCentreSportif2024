package be.atc.projetcentresportif2024;

import be.atc.entities.User;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet contrôlant l'accès au panneau d'administration.
 * Seuls les utilisateurs ayant le rôle d'administrateur (ID de rôle = 3) peuvent accéder à cette page.
 */
@WebServlet(name = "AdminPanelServlet", value = "/AdminPanelServlet")
public class AdminPanelServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Initialisation du logger pour suivre l'exécution
    private static final Logger logger = Logger.getLogger(AdminPanelServlet.class);

    /**
     * Méthode gérant les requêtes GET pour accéder au panneau d'administration.
     *
     * @param request  L'objet HttpServletRequest qui contient la requête du client.
     * @param response L'objet HttpServletResponse qui contient la réponse envoyée au client.
     * @throws ServletException Si une erreur survient pendant l'exécution de la requête.
     * @throws IOException      Si une erreur d'entrée/sortie survient.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Entrée dans doGet de AdminPanelServlet");

        // Vérification de la session pour voir si l'utilisateur est connecté
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

        logger.info("Utilisateur autorisé. Redirection vers le panneau d'administration.");
        // Rediriger l'administrateur vers le panneau d'administration
        request.getRequestDispatcher("/WEB-INF/jsp/adminPanel.jsp").forward(request, response);
    }

    /**
     * Méthode gérant les requêtes POST pour accéder au panneau d'administration.
     *
     * @param request  L'objet HttpServletRequest qui contient la requête du client.
     * @param response L'objet HttpServletResponse qui contient la réponse envoyée au client.
     * @throws ServletException Si une erreur survient pendant l'exécution de la requête.
     * @throws IOException      Si une erreur d'entrée/sortie survient.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Entrée dans doPost de AdminPanelServlet");
        doGet(request, response);
    }
}
