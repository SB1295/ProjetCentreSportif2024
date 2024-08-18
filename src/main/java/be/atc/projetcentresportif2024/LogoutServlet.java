package be.atc.projetcentresportif2024;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 * Servlet qui gère la déconnexion des utilisateurs.
 */
@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(LogoutServlet.class);

    /**
     * Gère les requêtes GET pour déconnecter l'utilisateur.
     *
     * @param request  L'objet HttpServletRequest contenant la requête du client.
     * @param response L'objet HttpServletResponse contenant la réponse envoyée au client.
     * @throws ServletException Si une erreur survient lors du traitement de la requête.
     * @throws IOException      Si une erreur d'entrée/sortie survient.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false); // Récupère la session si elle existe, mais ne la crée pas si elle n'existe pas (renvoie null)

        if (session != null) {
            logger.info("Déconnexion de l'utilisateur : " + session.getAttribute("user"));
            session.invalidate(); // Invalide la session
        }

        response.sendRedirect(request.getContextPath() + "/main?action=home"); // Redirige vers la page d'accueil après la déconnexion
    }
}
