package be.atc.dao.impl;

import be.atc.dao.AddressDao;
import be.atc.entities.Address;
import be.atc.util.JpaUtil;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

public class AddressDaoImpl implements AddressDao {

    // Logger pour suivre les événements
    private static final Logger logger = Logger.getLogger(AddressDaoImpl.class);

    @Override
    public void createAddress(Address address) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();
            em.persist(address);
            transaction.commit();
            logger.info("Adresse créée avec succès : " + address.getStreetName());
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Erreur lors de la création de l'adresse : " + address.getStreetName(), e);
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void updateAddress(Address address) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();
            em.merge(address);
            transaction.commit();
            logger.info("Adresse mise à jour avec succès : " + address.getStreetName());
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Erreur lors de la mise à jour de l'adresse : " + address.getStreetName(), e);
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteAddressById(int id) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();
            Optional<Address> addressOpt = findById(id);
            addressOpt.ifPresent(em::remove);
            transaction.commit();
            logger.info("Adresse supprimée avec succès : ID " + id);
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Erreur lors de la suppression de l'adresse : ID " + id, e);
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Address> findById(int id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Address address = em.find(Address.class, id);
            return Optional.ofNullable(address);
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche de l'adresse : ID " + id, e);
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Address> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            List<Address> addresses = em.createNamedQuery("Address.findAll", Address.class).getResultList();
            logger.debug("Toutes les adresses récupérées");
            return addresses;
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des adresses", e);
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Address> findByStreetName(String streetName) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            List<Address> addresses = em.createNamedQuery("Address.findByStreetName", Address.class)
                    .setParameter("streetName", streetName)
                    .getResultList();
            logger.debug("Adresses trouvées avec le nom de rue : " + streetName);
            return addresses;
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche des adresses avec le nom de rue : " + streetName, e);
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Address> findByLocalityId(int localityId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            List<Address> addresses = em.createNamedQuery("Address.findByLocalityId", Address.class)
                    .setParameter("localityId", localityId)
                    .getResultList();
            logger.debug("Adresses trouvées avec l'ID de la localité : " + localityId);
            return addresses;
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche des adresses avec l'ID de la localité : " + localityId, e);
            throw e;
        } finally {
            em.close();
        }
    }
}
