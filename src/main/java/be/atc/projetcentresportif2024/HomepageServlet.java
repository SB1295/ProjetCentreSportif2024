package be.atc.projetcentresportif2024;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "HomepageServlet", value = "/HomepageServlet")
public class HomepageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Ajouter un message à la requête
        request.setAttribute("welcomeMessage", "Bienvenue sur la page d'accueil");
        // Forward la requête à la page JSP
        request.getRequestDispatcher("/WEB-INF/jsp/homepage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}