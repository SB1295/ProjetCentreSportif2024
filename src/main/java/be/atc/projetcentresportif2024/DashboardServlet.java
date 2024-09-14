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
 * Servlet qui gère l'affichage du tableau de bord pour les utilisateurs connectés.
 */
@WebServlet(name = "DashboardServlet", value = "/DashboardServlet")
public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(DashboardServlet.class);

    /**
     * Gère les requêtes GET pour afficher le tableau de bord.
     * Vérifie si une session est active et si un utilisateur est connecté.
     * Si l'utilisateur n'est pas connecté, redirige vers la page de connexion.
     *
     * @param request  L'objet HttpServletRequest contenant la requête du client.
     * @param response L'objet HttpServletResponse contenant la réponse envoyée au client.
     * @throws ServletException Si une erreur survient lors du traitement de la requête.
     * @throws IOException      Si une erreur d'entrée/sortie survient.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Entrée dans doGet de DashboardServlet");

        HttpSession session = request.getSession(false);
        if (session == null) {
            logger.warn("Aucune session active trouvée, redirection vers la page de connexion");
            response.sendRedirect(request.getContextPath() + "/main?action=login");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            logger.warn("Aucun utilisateur trouvé dans la session, redirection vers la page de connexion");
            response.sendRedirect(request.getContextPath() + "/main?action=login");
            return;
        }

        logger.info("Utilisateur trouvé dans la session : " + user.getEmail());
        logger.info("Redirection vers le tableau de bord commun");

        // Redirige tous les utilisateurs connectés vers le tableau de bord commun
        request.getRequestDispatcher("/WEB-INF/jsp/dashboard.jsp").forward(request, response);
    }

    /**
     * Gère les requêtes POST en redirigeant vers la méthode doGet.
     *
     * @param request  L'objet HttpServletRequest contenant la requête du client.
     * @param response L'objet HttpServletResponse contenant la réponse envoyée au client.
     * @throws ServletException Si une erreur survient lors du traitement de la requête.
     * @throws IOException      Si une erreur d'entrée/sortie survient.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Entrée dans doPost de DashboardServlet");
        doGet(request, response);
    }
}
