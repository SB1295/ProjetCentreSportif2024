package be.atc.projetcentresportif2024;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "RegisterServlet", value = "/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (password.equals(confirmPassword) && registerUser(email, password)) {
            // Rediriger vers la page de connexion après une inscription réussie
            response.sendRedirect("login");
        } else {
            // Rediriger vers la page d'inscription avec un message d'erreur
            request.setAttribute("errorMessage", "Erreur lors de l'inscription. Veuillez réessayer.");
            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
        }
    }

    private boolean registerUser(String email, String password) {
        // Implémentez ici la logique d'inscription de l'utilisateur
        // Cela peut impliquer de vérifier l'unicité de l'email dans une base de données
        // et de sauvegarder les nouvelles informations utilisateur dans la base de données
        return true; // Remplacez par la logique réelle de réussite de l'inscription
    }
}