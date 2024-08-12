package be.atc.projetcentresportif2024;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import org.apache.log4j.Logger;

@WebServlet("/main")
public class MainServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(MainServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Entrée dans doGet de MainServlet");

        String action = request.getParameter("action");
        logger.debug("Action reçue : " + action);

        if (action == null) {
            action = "home";
            logger.debug("Aucune action reçue, redirection par défaut vers home");
        }

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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Entrée dans doPost de MainServlet");
        doGet(request, response);
    }
}
