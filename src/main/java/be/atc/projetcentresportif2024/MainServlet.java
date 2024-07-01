package be.atc.projetcentresportif2024;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/main")
public class MainServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "home";
        }

        switch (action) {
            case "login":
                request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
                break;
            case "register":
                request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
                break;
            case "dashboard":
                request.getRequestDispatcher("/WEB-INF/jsp/dashboard.jsp").forward(request, response);
                break;
            default:
                request.getRequestDispatcher("/index.jsp").forward(request, response);
                break;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}