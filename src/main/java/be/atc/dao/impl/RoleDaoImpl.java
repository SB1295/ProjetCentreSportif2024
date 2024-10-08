package be.atc.dao.impl;

import be.atc.dao.RoleDao;
import be.atc.entities.Role;
import be.atc.util.JpaUtil;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation de l'interface {@link RoleDao} pour la gestion des entités {@link Role}.
 */
public class RoleDaoImpl implements RoleDao {

    /** Logger pour la journalisation des événements */
    private static final Logger logger = Logger.getLogger(RoleDaoImpl.class);

    /**
     * Trouve un rôle par son ID.
     *
     * @param id L'ID du rôle à rechercher.
     * @return Un {@link Optional} contenant le rôle s'il est trouvé, ou vide sinon.
     */
    @Override
    public Optional<Role> findRoleById(int id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Role role = em.find(Role.class, id);
            if (role != null) {
                logger.debug("Rôle trouvé avec l'ID : " + id);
                return Optional.of(role);
            } else {
                logger.warn("Aucun rôle trouvé avec l'ID : " + id);
                return Optional.empty();
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche du rôle avec l'ID : " + id, e);
            throw e;
        } finally {
            em.close();  // Ferme l'EntityManager pour libérer les ressources
        }
    }

    /**
     * Récupère tous les rôles de la base de données.
     *
     * @return Une liste de tous les rôles.
     */
    @Override
    public List<Role> findAllRoles() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            List<Role> roles = em.createNamedQuery("Role.findAll", Role.class).getResultList();
            logger.debug("Tous les rôles récupérés");
            return roles;
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des rôles", e);
            throw e;
        } finally {
            em.close();  // Ferme l'EntityManager pour libérer les ressources
        }
    }

    /**
     * Crée un nouveau rôle dans la base de données.
     *
     * @param role Le rôle à créer.
     */
    @Override
    public void createRole(Role role) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();
            em.persist(role);  // Persiste un nouveau rôle dans la base de données
            transaction.commit();
            logger.info("Rôle créé avec succès : " + role.getRoleName());
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();  // Annule la transaction en cas d'erreur
            }
            logger.error("Erreur lors de la création du rôle : " + role.getRoleName(), e);
            throw e;
        } finally {
            em.close();  // Ferme l'EntityManager pour libérer les ressources
        }
    }

    /**
     * Supprime un rôle de la base de données en fonction de son ID.
     *
     * @param id L'ID du rôle à supprimer.
     */
    @Override
    public void deleteRoleById(int id) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();
            Role role = em.find(Role.class, id);
            if (role != null) {
                em.remove(role);  // Supprime le rôle de la base de données
                logger.info("Rôle supprimé avec succès : " + role.getRoleName());
            } else {
                logger.warn("Aucun rôle trouvé avec l'ID : " + id + ", suppression annulée");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();  // Annule la transaction en cas d'erreur
            }
            logger.error("Erreur lors de la suppression du rôle avec l'ID : " + id, e);
            throw e;
        } finally {
            em.close();  // Ferme l'EntityManager pour libérer les ressources
        }
    }

    /**
     * Met à jour un rôle existant dans la base de données.
     *
     * @param role Le rôle avec les informations mises à jour.
     */
    @Override
    public void updateRole(Role role) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();
            em.merge(role);  // Met à jour un rôle existant
            transaction.commit();
            logger.info("Rôle mis à jour avec succès : " + role.getRoleName());
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();  // Annule la transaction en cas d'erreur
            }
            logger.error("Erreur lors de la mise à jour du rôle : " + role.getRoleName(), e);
            throw e;
        } finally {
            em.close();  // Ferme l'EntityManager pour libérer les ressources
        }
    }
}
