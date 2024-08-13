<%--
  Created by IntelliJ IDEA.
  User: Sofiân
  Date: 13/08/2024
  Time: 13:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%@ include file="/WEB-INF/jsp/head.jsp" %> <!-- Inclusion du head commun -->
<body>
<%@ include file="/WEB-INF/jsp/navbar.jsp" %> <!-- Inclusion de la barre de navigation -->

<div class="container mt-4">
  <h1>Gestion du Profil</h1>

  <form action="ProfileServlet" method="post">
    <div class="form-group">
      <label for="email">Adresse e-mail</label>
      <input type="email" class="form-control" id="email" name="email" value="${sessionScope.user.email}" required>
    </div>
    <div class="form-group">
      <label for="confirmEmail">Confirmer l'adresse e-mail</label>
      <input type="email" class="form-control" id="confirmEmail" name="confirmEmail" required>
    </div>
    <div class="form-group">
      <label for="firstName">Prénom</label>
      <input type="text" class="form-control" id="firstName" name="firstName" value="${sessionScope.user.firstName}" required>
    </div>
    <div class="form-group">
      <label for="lastName">Nom</label>
      <input type="text" class="form-control" id="lastName" name="lastName" value="${sessionScope.user.lastName}" required>
    </div>
    <div class="form-group">
      <label for="gender">Genre</label>
      <select class="form-control" id="gender" name="gender">
        <option value="male" ${sessionScope.user.gender == 'Male' ? 'selected' : ''}>Homme</option>
        <option value="female" ${sessionScope.user.gender == 'Female' ? 'selected' : ''}>Femme</option>
        <option value="other" ${sessionScope.user.gender == 'Other' ? 'selected' : ''}>Autre</option>
      </select>
    </div>
    <div class="form-group">
      <label for="birthdate">Date de naissance</label>
      <input type="date" class="form-control" id="birthdate" name="birthdate" value="${sessionScope.user.birthdate}">
    </div>
    <div class="form-group">
      <label for="password">Mot de passe</label>
      <input type="password" class="form-control" id="password" name="password">
    </div>
    <div class="form-group">
      <label for="confirmPassword">Confirmer le mot de passe</label>
      <input type="password" class="form-control" id="confirmPassword" name="confirmPassword">
    </div>

    <button type="submit" class="btn btn-outline-info">Mettre à jour</button>
  </form>

  <!-- Formulaire séparé pour l'adresse -->
  <h2 class="mt-5">Modifier l'adresse</h2>
  <form action="${pageContext.request.contextPath}/AddressServlet" method="post">
    <div class="form-group">
      <label for="street">Rue</label>
      <input type="text" class="form-control" id="street" name="street" value="${sessionScope.user.fkAddresse.street}" required>
    </div>
    <div class="form-group">
      <label for="city">Ville</label>
      <input type="text" class="form-control" id="city" name="city" value="${sessionScope.user.fkAddresse.city}" required>
    </div>
    <div class="form-group">
      <label for="postalCode">Code postal</label>
      <input type="text" class="form-control" id="postalCode" name="postalCode" value="${sessionScope.user.fkAddresse.postalCode}" required>
    </div>
    <div class="form-group">
      <label for="country">Pays</label>
      <input type="text" class="form-control" id="country" name="country" value="${sessionScope.user.fkAddresse.country}" required>
    </div>
    <button type="submit" class="btn btn-outline-info">Mettre à jour l'adresse</button>
  </form>
</div>

</body>
</html>

