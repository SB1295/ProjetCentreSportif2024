<%--
  Created by IntelliJ IDEA.
  User: Sofiân
  Date: 13/08/2024
  Time: 18:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <!-- Import JSTL -->
<!DOCTYPE html>
<html>
<%@ include file="/WEB-INF/jsp/head.jsp" %> <!-- Inclusion du head commun -->
<body>
<%@ include file="/WEB-INF/jsp/navbar.jsp" %> <!-- Inclusion de la barre de navigation -->

<div class="container mt-4">
  <h1>Gestion du Profil</h1>

  <!-- Section pour afficher les messages d'erreur ou de succès -->
  <c:if test="${not empty errorMessage}">
    <div class="alert alert-danger" role="alert">
        ${errorMessage}
    </div>
  </c:if>
  <c:if test="${not empty successMessage}">
    <div class="alert alert-success" role="alert">
        ${successMessage}
    </div>
  </c:if>

  <form action="ProfileServlet" method="post">
    <div class="row">
      <div class="col-md-6">
        <div class="form-group">
          <label for="email">Adresse e-mail</label>
          <input type="email" class="form-control" id="email" name="email" value="${sessionScope.currentEditUser.email}">
        </div>
      </div>
      <div class="col-md-6">
        <div class="form-group">
          <label for="confirmEmail">Confirmer l'adresse e-mail</label>
          <input type="email" class="form-control" id="confirmEmail" name="confirmEmail">
        </div>
      </div>
    </div>

    <div class="row">
      <div class="col-md-6">
        <div class="form-group">
          <label for="firstName">Prénom</label>
          <input type="text" class="form-control" id="firstName" name="firstName" value="${sessionScope.currentEditUser.firstName}" required>
        </div>
      </div>
      <div class="col-md-6">
        <div class="form-group">
          <label for="lastName">Nom</label>
          <input type="text" class="form-control" id="lastName" name="lastName" value="${sessionScope.currentEditUser.lastName}" required>
        </div>
      </div>
    </div>

    <div class="row">
      <div class="col-md-6">
        <div class="form-group">
          <label for="phone">Numéro de téléphone</label>
          <input type="text" class="form-control" id="phone" name="phone" value="${sessionScope.currentEditUser.phone}">
        </div>
      </div>
      <div class="col-md-6">
        <div class="form-group">
          <label for="gender">Genre</label>
          <select class="form-control" id="gender" name="gender">
            <option value="Male" ${sessionScope.currentEditUser.gender == 'Male' ? 'selected' : ''}>Homme</option>
            <option value="Female" ${sessionScope.currentEditUser.gender == 'Female' ? 'selected' : ''}>Femme</option>
            <option value="Other" ${sessionScope.currentEditUser.gender == 'Other' ? 'selected' : ''}>Autre</option>
          </select>
        </div>
      </div>
    </div>

    <div class="row">
      <div class="col-md-6">
        <div class="form-group">
          <label for="password">Mot de passe</label>
          <input type="password" class="form-control" id="password" name="password">
        </div>
      </div>
      <div class="col-md-6">
        <div class="form-group">
          <label for="confirmPassword">Confirmer le mot de passe</label>
          <input type="password" class="form-control" id="confirmPassword" name="confirmPassword">
        </div>
      </div>
      <div class="col-md-6">
        <div class="form-group">
          <label for="birthdate">Date de naissance</label>
          <input type="date" class="form-control" id="birthdate" name="birthdate" value="${sessionScope.currentEditUser.birthdate}">
        </div>
      </div>
      <!-- Champs supplémentaires visibles uniquement pour les admins -->
      <c:if test="${sessionScope.user.fkRole.id == 3}">
        <div class="col-md-6">
          <div class="form-group">
            <label for="fkRole">Rôle</label>
            <select class="form-control" id="fkRole" name="fkRole">
              <option value="1" ${sessionScope.currentEditUser.fkRole.id == 1 ? 'selected' : ''}>Utilisateur</option>
              <option value="2" ${sessionScope.currentEditUser.fkRole.id == 2 ? 'selected' : ''}>Organisateur</option>
              <option value="3" ${sessionScope.currentEditUser.fkRole.id == 3 ? 'selected' : ''}>Administrateur</option>
            </select>
          </div>
        </div>
      </c:if>
    </div>

    <!-- Champs supplémentaires visibles uniquement pour les admins -->
    <c:if test="${sessionScope.user.fkRole.id == 3}">
      <div class="row">
        <div class="col-md-4">
          <div class="form-group form-check">
            <input type="checkbox" class="form-check-input" id="blacklist" name="blacklist" ${sessionScope.currentEditUser.blacklist ? 'checked' : ''}>
            <label class="form-check-label" for="blacklist">Blacklisté</label>
          </div>
        </div>
        <div class="col-md-4">
          <div class="form-group form-check">
            <input type="checkbox" class="form-check-input" id="active" name="active" ${sessionScope.currentEditUser.active ? 'checked' : ''}>
            <label class="form-check-label" for="active">Actif</label>
          </div>
        </div>
      </div>
    </c:if>

    <button type="submit" class="btn btn-outline-info">Mettre à jour</button>
  </form>


  <!-- Formulaire séparé pour l'adresse -->
  <h2 class="mt-5">Modifier l'adresse</h2>
  <form action="AddressServlet" method="post">
    <div class="row">
      <div class="col-md-6">
        <div class="form-group">
          <label for="streetName">Rue</label>
          <input type="text" class="form-control" id="streetName" name="streetName" value="${sessionScope.currentEditUser.fkAddresse.streetName}" required>
        </div>
      </div>
      <div class="col-md-6">
        <div class="form-group">
          <label for="number">Numéro</label>
          <input type="text" class="form-control" id="number" name="number" value="${sessionScope.currentEditUser.fkAddresse.number}" required>
        </div>
      </div>
    </div>

    <div class="row">
      <div class="col-md-6">
        <div class="form-group">
          <label for="boxNumber">Boîte</label>
          <input type="text" class="form-control" id="boxNumber" name="boxNumber" value="${sessionScope.currentEditUser.fkAddresse.boxNumber}">
        </div>
      </div>
      <div class="col-md-6">
        <div class="form-group">
          <label for="locality">Localité</label>
          <select class="form-control locality-select" id="locality" name="locality" required>
            <option value="" disabled selected>Choisir une localité</option>
            <c:forEach var="locality" items="${localities}">
              <option value="${locality.id}" ${sessionScope.currentEditUser.fkAddresse.fkLocality.id == locality.id ? 'selected' : ''}>
                  ${locality.postalCode} - ${locality.town} (${locality.province})
              </option>
            </c:forEach>
          </select>
        </div>
      </div>
    </div>

    <div class="row">
      <div class="col-md-4">
        <div class="form-group">
          <label for="town">Ville</label>
          <input type="text" class="form-control" id="town" name="town" value="${sessionScope.currentEditUser.fkAddresse.fkLocality.town}" disabled>
        </div>
      </div>
      <div class="col-md-4">
        <div class="form-group">
          <label for="province">Province</label>
          <input type="text" class="form-control" id="province" name="province" value="${sessionScope.currentEditUser.fkAddresse.fkLocality.province}" disabled>
        </div>
      </div>
      <div class="col-md-4">
        <div class="form-group">
          <label for="maintown">Ville Principale</label>
          <input type="text" class="form-control" id="maintown" name="maintown" value="${sessionScope.currentEditUser.fkAddresse.fkLocality.maintown}" disabled>
        </div>
      </div>
    </div>

    <button type="submit" class="btn btn-outline-info mb-4">Mettre à jour l'adresse</button>
  </form>

</div>

<!-- Script pour activer Select2 -->
<script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
<script src="${pageContext.request.contextPath}/js/profile.js"></script>

</body>
</html>

