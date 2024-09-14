package be.atc.services;

import be.atc.entities.Role;
import java.util.List;
import java.util.Optional;

/**
 * Interface définissant les opérations de service pour la gestion des rôles.
 */
public interface RoleService {

    /**
     * Recherche un rôle par son identifiant.
     *
     * @param id L'identifiant du rôle à rechercher.
     * @return Un {@link Optional} contenant le rôle si trouvé, sinon un {@link Optional} vide.
     */
    Optional<Role> findRoleById(int id);

    /**
     * Récupère tous les rôles du système.
     *
     * @return Une liste de tous les rôles disponibles.
     */
    List<Role> findAllRoles();

    /**
     * Crée un nouveau rôle.
     *
     * @param role Le rôle à créer.
     */
    void createRole(Role role);

    /**
     * Supprime un rôle par son identifiant.
     *
     * @param id L'identifiant du rôle à supprimer.
     */
    void deleteRoleById(int id);

    /**
     * Met à jour un rôle existant.
     *
     * @param role Le rôle à mettre à jour.
     */
    void updateRole(Role role);
}
