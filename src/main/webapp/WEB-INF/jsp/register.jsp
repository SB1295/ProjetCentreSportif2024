<%--
  Created by IntelliJ IDEA.
  User: Sofiân
  Date: 01/07/2024
  Time: 12:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
                    <form action="main?action=register" method="post">
                        <div class="form-group">
                            <label for="email">Email</label>
                            <input type="email" class="form-control" id="email" name="email" placeholder="Entrez votre email" required>
                        </div>
                        <div class="form-group">
                            <label for="password">Mot de passe</label>
                            <input type="password" class="form-control" id="password" name="password" placeholder="Entrez votre mot de passe" required>
                        </div>
                        <div class="form-group">
                            <label for="confirmPassword">Confirmez le mot de passe</label>
                            <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" placeholder="Confirmez votre mot de passe" required>
                        </div>
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
</body>
</html>

