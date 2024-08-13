<%--
  Created by IntelliJ IDEA.
  User: Sofiân
  Date: 11/08/2024
  Time: 18:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%@ include file="/WEB-INF/jsp/head.jsp" %> <!-- Inclusion du head commun -->
<body>
<%@ include file="/WEB-INF/jsp/navbar.jsp" %> <!-- Inclusion de la barre de navigation -->

<div class="container mt-4">
    <h1>Tableau de Bord</h1>

    <p>Bienvenue, <strong>${user.firstName} ${user.lastName}</strong>!</p> <!-- Affiche le nom de l'utilisateur connecté -->

    <div class="row">
        <div class="col-md-4">
            <div class="card">
                <div class="card-header">
                    Abonnements actifs
                </div>
                <div class="card-body">
                    <p>Contenu pour la première section du tableau de bord.</p>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card">
                <div class="card-header">
                    Réservations
                </div>
                <div class="card-body">
                    <p>Contenu pour la deuxième section du tableau de bord.</p>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>

