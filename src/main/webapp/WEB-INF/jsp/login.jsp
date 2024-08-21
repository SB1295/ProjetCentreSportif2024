<%--
  Created by IntelliJ IDEA.
  User: Sofiân
  Date: 01/07/2024
  Time: 12:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <!-- Import JSTL -->
<!DOCTYPE html>
<html>
<%@ include file="/WEB-INF/jsp/head.jsp" %>
<body>
<%@ include file="/WEB-INF/jsp/navbar.jsp" %>

<div class="container mt-4">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card">
                <div class="card-header">
                    <h3>Connexion</h3>
                </div>
                <div class="card-body">

                    <!-- Afficher le message de succès s'il existe -->
                    <c:if test="${not empty successMessage}">
                        <div class="alert alert-success" role="alert">
                                ${successMessage}
                        </div>
                        <c:remove var="successMessage" scope="session"/>
                    </c:if>

                    <!-- Afficher les messages d'erreur -->
                    <c:if test="${not empty loginError}">
                        <div class="alert alert-danger" role="alert">
                                ${loginError}
                        </div>
                    </c:if>

                    <form action="LoginServlet" method="post">
                        <div class="form-group">
                            <label for="email">Email</label>
                            <input type="email" class="form-control" id="email" name="email" placeholder="Entrez votre email" required>
                        </div>
                        <div class="form-group">
                            <label for="password">Mot de passe</label>
                            <div class="input-group">
                                <input type="password" class="form-control" id="password" name="password" placeholder="Entrez votre mot de passe" required>
                                <div class="input-group-append">
                                    <span class="input-group-text">
                                        <i toggle="#password" class="fa fa-fw fa-eye toggle-password"></i>
                                    </span>
                                </div>
                            </div>
                        </div>

                        <button type="submit" class="btn btn-primary btn-block">Se connecter</button>
                    </form>
                </div>
                <div class="card-footer text-center">
                    <a href="main?action=register">Pas encore inscrit ? Créez un compte</a>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Script passwordToggle  -->
<script src="${pageContext.request.contextPath}/js/passwordToggle.js"></script>
</body>
</html>
