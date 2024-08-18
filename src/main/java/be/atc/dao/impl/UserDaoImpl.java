package be.atc.dao.impl;

import be.atc.dao.UserDao;
import be.atc.entities.User;
import be.atc.util.JpaUtil;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation de l'interface {@link UserDao}.
 * Gère les opérations de base de données pour les entités {@link User}.
 */
public class UserDaoImpl implements UserDao {

    private static final Logger logger = Logger.getLogger(UserDaoImpl.class);

    /**
     * Crée un nouvel utilisateur dans la base de données.
     *
     * @param user L'utilisateur à créer.
     */
    @Override
    public void createUser(User user) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();
            em.persist(user);  // Persiste un nouvel utilisateur dans la base de données
            transaction.commit();
            logger.info("Utilisateur créé avec succès : " + user.getEmail());
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();  // Annule la transaction en cas d'erreur
            }
            logger.error("Erreur lors de la création de l'utilisateur : " + user.getEmail(), e);
            throw e;
        } finally {
            em.close();  // Ferme l'EntityManager pour libérer les ressources
        }
    }

    /**
     * Met à jour un utilisateur existant dans la base de données.
     *
     * @param user L'utilisateur avec les informations mises à jour.
     */
    @Override
    public void updateUser(User user) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();
            em.merge(user);  // Met à jour un utilisateur existant
            transaction.commit();
            logger.info("Utilisateur mis à jour avec succès : " + user.getEmail());
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();  // Annule la transaction en cas d'erreur
            }
            logger.error("Erreur lors de la mise à jour de l'utilisateur : " + user.getEmail(), e);
            throw e;
        } finally {
            em.close();  // Ferme l'EntityManager pour libérer les ressources
        }
    }

    /**
     * Trouve un utilisateur par son adresse email.
     *
     * @param email L'email de l'utilisateur à rechercher.
     * @return Un {@link Optional} contenant l'utilisateur si trouvé, ou vide sinon.
     */
    @Override
    public Optional<User> findByEmail(String email) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            User user = em.createNamedQuery("User.findByEmail", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
            logger.debug("Utilisateur trouvé avec l'email : " + email);
            return Optional.of(user);
        } catch (NoResultException e) {
            logger.warn("Aucun utilisateur trouvé avec l'email : " + email);
            return Optional.empty();
        } finally {
            em.close();  // Ferme l'EntityManager pour libérer les ressources
        }
    }

    /**
     * Récupère tous les utilisateurs dans la base de données.
     *
     * @return Une liste d'utilisateurs.
     */
    @Override
    public List<User> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            List<User> users = em.createNamedQuery("User.findAll", User.class).getResultList();
            logger.debug("Tous les utilisateurs récupérés");
            return users;
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des utilisateurs", e);
            throw e;
        } finally {
            em.close();  // Ferme l'EntityManager pour libérer les ressources
        }
    }

    /**
     * Vérifie si un utilisateur avec un email donné existe déjà dans la base de données.
     *
     * @param email L'email à vérifier.
     * @return {@code true} si l'email existe, {@code false} sinon.
     */
    @Override
    public boolean existsByEmail(String email) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Long count = em.createNamedQuery("User.existsByEmail", Long.class)
                    .setParameter("email", email)
                    .getSingleResult();
            logger.debug("Vérification de l'existence de l'email : " + email);
            return count > 0;
        } catch (Exception e) {
            logger.error("Erreur lors de la vérification de l'existence de l'email : " + email, e);
            throw e;
        } finally {
            em.close();  // Ferme l'EntityManager pour libérer les ressources
        }
    }

    /**
     * Supprime un utilisateur de la base de données en fonction de son adresse email.
     *
     * @param email L'email de l'utilisateur à supprimer.
     */
    @Override
    public void deleteByEmail(String email) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();
            Optional<User> userOpt = findByEmail(email);
            userOpt.ifPresent(em::remove);
            transaction.commit();
            logger.info("Utilisateur supprimé avec succès : " + email);
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();  // Annule la transaction en cas d'erreur
            }
            logger.error("Erreur lors de la suppression de l'utilisateur : " + email, e);
            throw e;
        } finally {
            em.close();  // Ferme l'EntityManager pour libérer les ressources
        }
    }

    /**
     * Trouve un utilisateur par son ID.
     *
     * @param id L'ID de l'utilisateur à rechercher.
     * @return Un {@link Optional} contenant l'utilisateur si trouvé, ou vide sinon.
     */
    @Override
    public Optional<User> findById(int id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            User user = em.createNamedQuery("User.findById", User.class)
                    .setParameter("id", id)
                    .getSingleResult();
            logger.debug("Utilisateur trouvé avec l'ID : " + id);
            return Optional.of(user);
        } catch (NoResultException e) {
            logger.warn("Aucun utilisateur trouvé avec l'ID : " + id);
            return Optional.empty();
        } finally {
            em.close();  // Ferme l'EntityManager pour libérer les ressources
        }
    }

    /**
     * Supprime un utilisateur de la base de données en fonction de son ID.
     *
     * @param id L'ID de l'utilisateur à supprimer.
     */
    @Override
    public void deleteById(int id) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();

            // Récupération de l'utilisateur par ID
            Optional<User> userOpt = findById(id);
            if (userOpt.isPresent()) {
                User managedUser = em.merge(userOpt.get()); // Rattachement de l'entité détachée à l'EntityManager
                em.remove(managedUser); // Suppression de l'utilisateur géré
                logger.info("Utilisateur supprimé avec succès : " + managedUser.getEmail());
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();  // Annule la transaction en cas d'erreur
            }
            logger.error("Erreur lors de la suppression de l'utilisateur avec ID : " + id, e);
            throw e;
        } finally {
            em.close();  // Ferme l'EntityManager pour libérer les ressources
        }
    }
}
