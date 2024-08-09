package be.atc.dao.impl;

import be.atc.dao.UserDao;
import be.atc.entities.User;
import be.atc.util.JpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {

    @Override
    public void createUser(User user) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();
            em.persist(user);  // Persiste un nouvel utilisateur dans la base de données
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();  // Annule la transaction en cas d'erreur
            }
            e.printStackTrace();
        } finally {
            em.close();  // Ferme l'EntityManager pour libérer les ressources
        }
    }

    @Override
    public void updateUser(User user) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();
            em.merge(user);  // Met à jour un utilisateur existant
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();  // Annule la transaction en cas d'erreur
            }
            e.printStackTrace();
        } finally {
            em.close();  // Ferme l'EntityManager pour libérer les ressources
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            User user = em.createNamedQuery("User.findByEmail", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();  // Ferme l'EntityManager pour libérer les ressources
        }
    }

    @Override
    public List<User> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createNamedQuery("User.findAll", User.class).getResultList();
        } finally {
            em.close();  // Ferme l'EntityManager pour libérer les ressources
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Long count = em.createNamedQuery("User.existsByEmail", Long.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();  // Ferme l'EntityManager pour libérer les ressources
        }
    }

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
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();  // Annule la transaction en cas d'erreur
            }
            e.printStackTrace();
        } finally {
            em.close();  // Ferme l'EntityManager pour libérer les ressources
        }
    }
}
