package be.atc.services.impl;

import be.atc.dao.RoleDao;
import be.atc.dao.impl.RoleDaoImpl;
import be.atc.entities.Role;
import be.atc.services.RoleService;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class RoleServiceImpl implements RoleService {

    private static final Logger logger = Logger.getLogger(RoleServiceImpl.class);
    private final RoleDao roleDao;

    public RoleServiceImpl() {
        this.roleDao = new RoleDaoImpl(); // Utilisation de l'implémentation par défaut
    }

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao; // Injection du DAO
    }

    @Override
    public Optional<Role> findRoleById(int id) {
        logger.info("Recherche du rôle avec l'ID : " + id);
        return roleDao.findRoleById(id);
    }

    @Override
    public List<Role> findAllRoles() {
        logger.info("Récupération de tous les rôles");
        return roleDao.findAllRoles();
    }

    @Override
    public void createRole(Role role) {
        logger.info("Création d'un nouveau rôle : " + role.getRoleName());
        roleDao.createRole(role);
    }

    @Override
    public void updateRole(Role role) {
        logger.info("Mise à jour du rôle : " + role.getRoleName());
        roleDao.updateRole(role);
    }

    @Override
    public void deleteRoleById(int id) {
        logger.info("Suppression du rôle avec l'ID : " + id);
        roleDao.deleteRoleById(id);
    }
}
