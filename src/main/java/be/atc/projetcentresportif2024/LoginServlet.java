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

/**
 * Servlet qui gère l'authentification des utilisateurs.
 */
@WebServlet(name = "LoginServlet", value = "/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService;
    private static final Logger logger = Logger.getLogger(LoginServlet.class);

    /**
     * Initialise la servlet et le service utilisateur.
     *
     * @throws ServletException Si une erreur survient lors de l'initialisation.
     */
    @Override
    public void init() throws ServletException {
        logger.info("Initialisation de LoginServlet");
        userService = new UserServiceImpl(); // Initialisation directe du UserServiceImpl sans passer un DAO
    }

    /**
     * Gère les requêtes GET pour afficher la page de connexion.
     *
     * @param request  L'objet HttpServletRequest contenant la requête du client.
     * @param response L'objet HttpServletResponse contenant la réponse envoyée au client.
     * @throws ServletException Si une erreur survient lors du traitement de la requête.
     * @throws IOException      Si une erreur d'entrée/sortie survient.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Accès à la page de connexion (GET)");
        forwardToLogin(request, response);
    }

    /**
     * Gère les requêtes POST pour traiter la tentative de connexion.
     *
     * @param request  L'objet HttpServletRequest contenant la requête du client.
     * @param response L'objet HttpServletResponse contenant la réponse envoyée au client.
     * @throws ServletException Si une erreur survient lors du traitement de la requête.
     * @throws IOException      Si une erreur d'entrée/sortie survient.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Tentative de connexion (POST)");

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        logger.debug("Email reçu : " + email);

        try {
            handleLogin(request, response, email, password);
        } catch (IllegalArgumentException e) {
            handleLoginException(request, response, e, email);
        }
    }

    /**
     * Traite la tentative de connexion en vérifiant les informations d'identification de l'utilisateur.
     *
     * @param request  L'objet HttpServletRequest contenant la requête du client.
     * @param response L'objet HttpServletResponse contenant la réponse envoyée au client.
     * @param email    L'adresse e-mail de l'utilisateur.
     * @param password Le mot de passe de l'utilisateur.
     * @throws IOException      Si une erreur d'entrée/sortie survient.
     * @throws ServletException Si une erreur survient lors du traitement de la requête.
     */
    private void handleLogin(HttpServletRequest request, HttpServletResponse response, String email, String password) throws IOException, ServletException {
        User user = userService.authenticateUser(email, password);

        if (user != null) {
            onLoginSuccess(request, response, user);
        } else {
            onLoginFailure(request, response, email);
        }
    }

    /**
     * Gère le succès de la connexion en enregistrant l'utilisateur dans la session et en redirigeant vers le tableau de bord.
     *
     * @param request  L'objet HttpServletRequest contenant la requête du client.
     * @param response L'objet HttpServletResponse contenant la réponse envoyée au client.
     * @param user     L'utilisateur authentifié.
     * @throws IOException Si une erreur d'entrée/sortie survient.
     */
    private void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        logger.info("Connexion réussie pour l'utilisateur : " + user.getEmail());

        HttpSession session = request.getSession();
        session.setAttribute("user", user);

        // Logger debug, inspection de la variable session
        logUserDetails(user);

        // Loggers attributs de la session
        logSessionAttributes(session);

        // Redirige vers le tableau de bord si l'authentification est validée
        response.sendRedirect(request.getContextPath() + "/DashboardServlet");
    }

    /**
     * Gère l'échec de la connexion en affichant un message d'erreur.
     *
     * @param request  L'objet HttpServletRequest contenant la requête du client.
     * @param response L'objet HttpServletResponse contenant la réponse envoyée au client.
     * @param email    L'adresse e-mail de l'utilisateur ayant échoué la connexion.
     * @throws ServletException Si une erreur survient lors du traitement de la requête.
     * @throws IOException      Si une erreur d'entrée/sortie survient.
     */
    private void onLoginFailure(HttpServletRequest request, HttpServletResponse response, String email) throws ServletException, IOException {
        logger.warn("Échec de la connexion pour l'utilisateur : " + email);
        request.setAttribute("loginError", "Email ou mot de passe incorrect.");
        forwardToLogin(request, response);
    }

    /**
     * Gère les exceptions survenant lors de la tentative de connexion, telles qu'un utilisateur désactivé.
     *
     * @param request  L'objet HttpServletRequest contenant la requête du client.
     * @param response L'objet HttpServletResponse contenant la réponse envoyée au client.
     * @param e        L'exception levée lors de la tentative de connexion.
     * @param email    L'adresse e-mail de l'utilisateur ayant échoué la connexion.
     * @throws ServletException Si une erreur survient lors du traitement de la requête.
     * @throws IOException      Si une erreur d'entrée/sortie survient.
     */
    private void handleLoginException(HttpServletRequest request, HttpServletResponse response, IllegalArgumentException e, String email) throws ServletException, IOException {
        // Gestion de l'exception pour un utilisateur non actif
        if ("USER_NOT_ACTIVE".equals(e.getMessage())) {
            logger.warn("Échec de la connexion : utilisateur désactivé - " + email);
            request.setAttribute("loginError", "Votre compte est désactivé. Veuillez contacter l'administrateur.");
        } else {
            logger.error("Erreur inattendue lors de la connexion : " + e.getMessage(), e);
            request.setAttribute("loginError", "Une erreur inattendue est survenue. Veuillez réessayer.");
        }
        forwardToLogin(request, response);
    }

    /**
     * Redirige l'utilisateur vers la page de connexion.
     *
     * @param request  L'objet HttpServletRequest contenant la requête du client.
     * @param response L'objet HttpServletResponse contenant la réponse envoyée au client.
     * @throws ServletException Si une erreur survient lors du traitement de la requête.
     * @throws IOException      Si une erreur d'entrée/sortie survient.
     */
    private void forwardToLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
    }

    /**
     * Enregistre les détails de l'utilisateur connecté dans les logs.
     *
     * @param user L'utilisateur authentifié.
     */
    private void logUserDetails(User user) {
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
    }

    /**
     * Enregistre les attributs de la session dans les logs.
     *
     * @param session La session HTTP de l'utilisateur.
     */
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
