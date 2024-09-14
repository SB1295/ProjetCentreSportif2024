package be.atc.services.impl;

import be.atc.dao.RoleDao;
import be.atc.dao.impl.RoleDaoImpl;
import be.atc.entities.Role;
import be.atc.services.RoleService;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service pour gérer les rôles.
 * Cette classe utilise un DAO pour interagir avec la base de données.
 */
public class RoleServiceImpl implements RoleService {

    private static final Logger logger = Logger.getLogger(RoleServiceImpl.class);
    private final RoleDao roleDao;

    /**
     * Constructeur par défaut qui initialise le DAO pour les rôles.
     */
    public RoleServiceImpl() {
        this.roleDao = new RoleDaoImpl(); // Utilisation de l'implémentation par défaut
    }

    /**
     * Constructeur qui permet l'injection du DAO pour les rôles.
     *
     * @param roleDao DAO à injecter pour l'accès aux données des rôles.
     */
    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao; // Injection du DAO
    }

    /**
     * Recherche un rôle par son identifiant.
     *
     * @param id L'identifiant du rôle à rechercher.
     * @return Un {@link Optional} contenant le rôle s'il est trouvé, sinon un {@link Optional} vide.
     */
    @Override
    public Optional<Role> findRoleById(int id) {
        logger.info("Recherche du rôle avec l'ID : " + id);
        return roleDao.findRoleById(id);
    }

    /**
     * Récupère tous les rôles.
     *
     * @return Une liste contenant tous les rôles.
     */
    @Override
    public List<Role> findAllRoles() {
        logger.info("Récupération de tous les rôles");
        return roleDao.findAllRoles();
    }

    /**
     * Crée un nouveau rôle.
     *
     * @param role Le rôle à créer.
     */
    @Override
    public void createRole(Role role) {
        logger.info("Création d'un nouveau rôle : " + role.getRoleName());
        roleDao.createRole(role);
    }

    /**
     * Met à jour un rôle existant.
     *
     * @param role Le rôle à mettre à jour.
     */
    @Override
    public void updateRole(Role role) {
        logger.info("Mise à jour du rôle : " + role.getRoleName());
        roleDao.updateRole(role);
    }

    /**
     * Supprime un rôle par son identifiant.
     *
     * @param id L'identifiant du rôle à supprimer.
     */
    @Override
    public void deleteRoleById(int id) {
        logger.info("Suppression du rôle avec l'ID : " + id);
        roleDao.deleteRoleById(id);
    }
}
