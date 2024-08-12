<!-- WEB-INF/jsp/navbar.jsp -->
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/main?action=home">
        <i class="fas fa-home"></i>
    </a>

    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <!-- Ajoutez d'autres liens de navigation ici si nécessaire -->
        </ul>
        <ul class="navbar-nav">
            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <!-- Si l'utilisateur est connecté, afficher le nom et le bouton de déconnexion -->
                    <li class="nav-item">
                        <span class="navbar-text mr-3">
                            Bienvenue, ${sessionScope.user.firstName} ${sessionScope.user.lastName}
                        </span>
                    </li>
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/LogoutServlet" class="btn btn-outline-danger my-2 my-sm-0">Se déconnecter</a>
                    </li>
                </c:when>
                <c:otherwise>
                    <!-- Si l'utilisateur n'est pas connecté, afficher les boutons de connexion et d'inscription -->
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/main?action=login" class="btn btn-outline-success my-2 my-sm-0">Se connecter</a>
                    </li>
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/main?action=register" class="btn btn-outline-primary my-2 my-sm-0 ml-2">S'inscrire</a>
                    </li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>
</nav>
