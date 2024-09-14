<%--
  Created by IntelliJ IDEA.
  User: Sofiân
  Date: 01/07/2024
  Time: 12:48
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
                    <h3>Inscription</h3>
                </div>
                <div class="card-body">
                    <form action="RegisterServlet" method="post">
                        <div class="form-group">
                            <label for="firstName">Prénom</label>
                            <input type="text" class="form-control" id="firstName" name="firstName" value="${firstName}" placeholder="Entrez votre prénom" required>
                            <!-- Affichage du message d'erreur pour le prénom -->
                            <c:if test="${not empty firstNameError}">
                                <small class="text-danger">${firstNameError}</small>
                            </c:if>
                        </div>
                        <div class="form-group">
                            <label for="lastName">Nom</label>
                            <input type="text" class="form-control" id="lastName" name="lastName" value="${lastName}" placeholder="Entrez votre nom" required>
                            <!-- Affichage du message d'erreur pour le nom -->
                            <c:if test="${not empty lastNameError}">
                                <small class="text-danger">${lastNameError}</small>
                            </c:if>
                        </div>
                        <div class="form-group">
                            <label for="email">Email</label>
                            <input type="email" class="form-control" id="email" name="email" value="${email}" placeholder="Entrez votre email" required>
                            <!-- Affichage du message d'erreur pour l'email -->
                            <c:if test="${not empty emailError}">
                                <small class="text-danger">${emailError}</small>
                            </c:if>
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
                            <!-- Affichage du message d'erreur pour le mot de passe -->
                            <c:if test="${not empty passwordError}">
                                <small class="text-danger">${passwordError}</small>
                            </c:if>
                        </div>
                        <div class="form-group">
                            <label for="confirmPassword">Confirmez le mot de passe</label>
                            <div class="input-group">
                                <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" placeholder="Confirmez votre mot de passe" required>
                                <div class="input-group-append">
                                    <span class="input-group-text">
                                        <i toggle="#confirmPassword" class="fa fa-fw fa-eye toggle-password"></i>
                                    </span>
                                </div>
                            </div>
                            <!-- Affichage du message d'erreur pour la confirmation du mot de passe -->
                            <c:if test="${not empty confirmPasswordError}">
                                <small class="text-danger">${confirmPasswordError}</small>
                            </c:if>
                        </div>



                        <!-- Affichage du message d'erreur général -->
                        <c:if test="${not empty generalError}">
                            <div class="alert alert-danger">${generalError}</div>
                        </c:if>
                        <button type="submit" class="btn btn-primary btn-block">S'inscrire</button>
                    </form>
                </div>
                <div class="card-footer text-center">
                    <a href="main?action=login">Déjà inscrit ? Connectez-vous</a>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Script passwordToggle  -->
<script src="${pageContext.request.contextPath}/js/passwordToggle.js"></script>

</body>
</html>
