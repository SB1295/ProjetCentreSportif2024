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

/**
 * Implémentation de l'interface {@link AddressDao} pour effectuer des opérations CRUD sur l'entité {@link Address}.
 */
public class AddressDaoImpl implements AddressDao {

    private static final Logger logger = Logger.getLogger(AddressDaoImpl.class);

    /**
     * Crée une nouvelle adresse dans la base de données.
     *
     * @param address L'adresse à persister.
     * @throws RuntimeException Si une erreur survient lors de la transaction.
     */
    @Override
    public void createAddress(Address address) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();
            em.persist(address);
            transaction.commit();
            logger.info("Adresse persistée avec succès, ID généré : " + address.getId());
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

    /**
     * Met à jour une adresse existante dans la base de données.
     *
     * @param address L'adresse à mettre à jour.
     * @throws RuntimeException Si une erreur survient lors de la transaction.
     */
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

    /**
     * Supprime une adresse de la base de données par son ID.
     *
     * @param id L'ID de l'adresse à supprimer.
     * @throws RuntimeException Si une erreur survient lors de la transaction.
     */
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

    /**
     * Recherche une adresse par son ID.
     *
     * @param id L'ID de l'adresse à rechercher.
     * @return Un {@link Optional} contenant l'adresse si trouvée, sinon un {@link Optional} vide.
     * @throws RuntimeException Si une erreur survient lors de la recherche.
     */
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

    /**
     * Récupère toutes les adresses présentes dans la base de données.
     *
     * @return Une liste d'adresses.
     * @throws RuntimeException Si une erreur survient lors de la récupération.
     */
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

    /**
     * Recherche des adresses par le nom de rue.
     *
     * @param streetName Le nom de la rue à rechercher.
     * @return Une liste d'adresses correspondant au nom de rue.
     * @throws RuntimeException Si une erreur survient lors de la recherche.
     */
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

    /**
     * Recherche des adresses par l'ID de la localité.
     *
     * @param localityId L'ID de la localité à rechercher.
     * @return Une liste d'adresses correspondant à la localité.
     * @throws RuntimeException Si une erreur survient lors de la recherche.
     */
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
