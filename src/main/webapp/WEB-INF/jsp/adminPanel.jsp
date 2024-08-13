<%--
  Created by IntelliJ IDEA.
  User: Sofiân
  Date: 13/08/2024
  Time: 11:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<%@ include file="/WEB-INF/jsp/head.jsp" %> <!-- Inclusion du head commun -->
<body>
<%@ include file="/WEB-INF/jsp/navbar.jsp" %> <!-- Inclusion de la barre de navigation -->

<div class="container mt-4">
  <h1>Panneau d'administration</h1>
  <p>Bienvenue, <strong>${sessionScope.user.firstName} ${sessionScope.user.lastName}</strong>!</p>

  <!-- Ajoutez ici les fonctionnalités et les sections d'administration -->
  <div class="row">
    <div class="col-md-4">
      <div class="card">
        <div class="card-header">
          Gestion des utilisateurs
        </div>
        <div class="card-body">
          <p>Gérer les utilisateurs inscrits, modifier les rôles, etc.</p>
          <a href="#" class="btn btn-outline-primary">Gérer les utilisateurs</a>
        </div>
      </div>
    </div>
    <div class="col-md-4">
      <div class="card">
        <div class="card-header">
          Gestion des événements
        </div>
        <div class="card-body">
          <p>Créer, modifier ou supprimer des événements.</p>
          <a href="#" class="btn btn-outline-primary">Gérer les événements</a>
        </div>
      </div>
    </div>
    <div class="col-md-4">
      <div class="card">
        <div class="card-header">
          Statistiques
        </div>
        <div class="card-body">
          <p>Voir les statistiques de l'application.</p>
          <a href="#" class="btn btn-outline-primary">Voir les statistiques</a>
        </div>
      </div>
    </div>
  </div>
</div>

</body>
</html>

