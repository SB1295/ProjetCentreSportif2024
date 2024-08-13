package be.atc.projetcentresportif2024;

import be.atc.entities.User;

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
        // Pour le moment, on redirige simplement vers le doGet
        doGet(request, response);
    }
}

