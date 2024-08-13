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
import java.util.Enumeration;

import org.apache.log4j.Logger;

@WebServlet(name = "LoginServlet", value = "/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService;
    private static final Logger logger = Logger.getLogger(LoginServlet.class);

    @Override
    public void init() throws ServletException {
        logger.info("Initialisation de LoginServlet");
        userService = new UserServiceImpl(); // Initialisation directe du UserServiceImpl sans passer un DAO
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Accès à la page de connexion (GET)");
        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Tentative de connexion (POST)");

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        logger.debug("Email reçu : " + email);

        User user = userService.authenticateUser(email, password);

        if (user != null) {
            logger.info("Connexion réussie pour l'utilisateur : " + email);
            HttpSession session = request.getSession();
            // Stockage des données de mon user avec le setAttribute permettant d'avoir accès aux données depuis n'importe quelle autre servlet ou JSP tant que la session est active.
            session.setAttribute("user", user);

            // Logger debug, inspection de la variable session
            logger.debug("Détails de l'utilisateur enregistré dans la session : " +
                    "ID=" + user.getId() +
                    ", Email=" + user.getEmail() +
                    ", Prénom=" + user.getFirstName() +
                    ", Nom=" + user.getLastName() +
                    ", Date de naissance=" + user.getBirthdate() +
                    ", Telephone=" + user.getPhone() +
                    ", Genre=" + user.getGender() +
                    ", Blackliste=" + user.getBlacklist() +
                    ", Actif=" + user.getActive() +
                    ", Role ID=" + user.getFkRole().getId() +
                    ", Role Nom=" + user.getFkRole().getRoleName());

            // Loggers attributs de la session
            logger.debug("Détails de la session après enregistrement de l'utilisateur : ");
            logSessionAttributes(session);

            // Redirige vers le dashboard si l'authentification est validée
            response.sendRedirect(request.getContextPath() + "/DashboardServlet");
        } else {
            logger.warn("Échec de la connexion pour l'utilisateur : " + email);
            request.setAttribute("loginError", "Email ou mot de passe incorrect.");
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        }
    }

    private void logSessionAttributes(HttpSession session) {
        logger.debug("Session ID: " + session.getId());
        logger.debug("Création de la session : " + session.getCreationTime());
        logger.debug("Dernier accès de la session : " + session.getLastAccessedTime());

        Enumeration<String> attributeNames = session.getAttributeNames();

        // Parcours de l'Enumeration avec une boucle while
        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            Object attributeValue = session.getAttribute(attributeName);
            logger.debug("Attribut de session : " + attributeName + " = " + attributeValue);
        }
    }
}
