package be.atc.projetcentresportif2024;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 * Servlet principale qui gère la navigation entre les différentes pages de l'application.
 */
@WebServlet("/main")
public class MainServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(MainServlet.class);

    /**
     * Gère les requêtes GET pour rediriger vers les différentes pages en fonction de l'action spécifiée.
     *
     * @param request  L'objet HttpServletRequest contenant la requête du client.
     * @param response L'objet HttpServletResponse contenant la réponse envoyée au client.
     * @throws ServletException Si une erreur survient lors du traitement de la requête.
     * @throws IOException      Si une erreur d'entrée/sortie survient.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Entrée dans doGet de MainServlet");

        // Récupère l'action spécifiée dans la requête
        String action = request.getParameter("action");
        logger.debug("Action reçue : " + action);

        if (action == null) {
            action = "home";
            logger.debug("Aucune action reçue, redirection par défaut vers home");
        }

        // Redirection en fonction de l'action
        switch (action) {
            case "login":
                logger.info("Redirection vers login.jsp");
                request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
                break;
            case "register":
                logger.info("Redirection vers register.jsp");
                request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
                break;
            case "home":
                logger.info("Redirection vers index.jsp (home)");
                request.getRequestDispatcher("/index.jsp").forward(request, response);
                break;
            default:
                logger.warn("Action non reconnue, redirection par défaut vers index.jsp");
                request.getRequestDispatcher("/index.jsp").forward(request, response);
                break;
        }
    }

    /**
     * Gère les requêtes POST en les redirigeant vers la méthode doGet.
     *
     * @param request  L'objet HttpServletRequest contenant la requête du client.
     * @param response L'objet HttpServletResponse contenant la réponse envoyée au client.
     * @throws ServletException Si une erreur survient lors du traitement de la requête.
     * @throws IOException      Si une erreur d'entrée/sortie survient.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Entrée dans doPost de MainServlet");
        doGet(request, response);
    }
}
