<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<%@ include file="/WEB-INF/jsp/head.jsp" %> <!-- Inclusion du head commun -->
<body>
<%@ include file="/WEB-INF/jsp/navbar.jsp" %> <!-- Inclusion de la barre de navigation -->

<div class="container mt-4">
  <h1>Panneau d'administration</h1>

  <!-- Barre de recherche -->
  <form method="get" action="AdminPanelServlet">
    <div class="input-group mb-3">
      <input type="text" class="form-control" placeholder="Rechercher un utilisateur..." name="searchQuery" value="${param.searchQuery}">
      <div class="input-group-append">
        <button class="btn btn-outline-secondary" type="submit">Rechercher</button>
      </div>
    </div>
  </form>

  <!-- Tableau des utilisateurs -->
  <form method="post" action="AdminPanelServlet">
    <table class="table table-striped">
      <thead>
      <tr>
        <th scope="col">ID</th>
        <th scope="col">Prénom</th>
        <th scope="col">Nom</th>
        <th scope="col">Email</th>
        <th scope="col">Rôle</th>
        <th scope="col">Actif</th>
        <th scope="col">Blacklisté</th>
        <th scope="col">Action</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="user" items="${users}">
        <tr>
          <th scope="row">${user.id}</th>
          <td>${user.firstName}</td>
          <td>${user.lastName}</td>
          <td>${user.email}</td>
          <td>
            <select name="roles_${user.id}" class="form-control">
              <option value="1" ${user.fkRole.id == 1 ? 'selected' : ''}>Utilisateur</option>
              <option value="2" ${user.fkRole.id == 2 ? 'selected' : ''}>Organisateur</option>
              <option value="3" ${user.fkRole.id == 3 ? 'selected' : ''}>Administrateur</option>
            </select>
          </td>
          <td>
            <input type="checkbox" name="active_${user.id}" ${user.active ? 'checked' : ''}>
          </td>
          <td>
            <input type="checkbox" name="blacklist_${user.id}" ${user.blacklist ? 'checked' : ''}>
          </td>
          <td>
            <!-- Bouton pour accéder au profil de l'utilisateur -->
            <a href="ProfileServlet?userId=${user.id}" class="btn btn-outline-primary btn-sm">Voir Profil</a>
            <!-- Bouton pour supprimer l'utilisateur -->
            <button type="submit" name="deleteUser" value="${user.id}" class="btn btn-outline-danger btn-sm" onclick="return confirm('Êtes-vous sûr de vouloir supprimer cet utilisateur ?');">Supprimer</button>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
    <!-- Bouton de mise à jour -->
    <button type="submit" name="action" value="updateUsers" class="btn btn-outline-info">Mettre à jour les utilisateurs</button>
  </form>

  <!-- Pagination -->
  <nav aria-label="Page navigation example">
    <ul class="pagination justify-content-center">
      <c:forEach begin="1" end="${totalPages}" var="i">
        <li class="page-item ${i == currentPage ? 'active' : ''}">
          <a class="page-link" href="AdminPanelServlet?page=${i}">${i}</a>
        </li>
      </c:forEach>
    </ul>
  </nav>

</div>

</body>
</html>
