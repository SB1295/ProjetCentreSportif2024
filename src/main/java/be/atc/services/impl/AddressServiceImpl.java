package be.atc.services.impl;

import be.atc.dao.AddressDao;
import be.atc.dao.impl.AddressDaoImpl;
import be.atc.entities.Address;
import be.atc.services.AddressService;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service pour gérer les adresses.
 * Cette classe utilise un DAO pour interagir avec la base de données.
 */
public class AddressServiceImpl implements AddressService {

    private static final Logger logger = Logger.getLogger(AddressServiceImpl.class);
    private final AddressDao addressDao;

    /**
     * Constructeur par défaut qui initialise le DAO pour les adresses.
     */
    public AddressServiceImpl() {
        this.addressDao = new AddressDaoImpl(); // Utilisation de l'implémentation DAO
    }

    /**
     * Crée une nouvelle adresse.
     *
     * @param address L'adresse à créer.
     * @throws RuntimeException Si une erreur survient lors de la création de l'adresse.
     */
    @Override
    public void createAddress(Address address) {
        logger.info("Début de la création de l'adresse pour la rue : " + address.getStreetName());
        try {
            addressDao.createAddress(address);
            logger.info("Adresse créée avec succès pour la rue : " + address.getStreetName());
        } catch (Exception e) {
            logger.error("Erreur lors de la création de l'adresse pour la rue : " + address.getStreetName(), e);
            throw e;
        }
    }

    /**
     * Met à jour une adresse existante.
     *
     * @param address L'adresse à mettre à jour.
     * @throws RuntimeException Si une erreur survient lors de la mise à jour de l'adresse.
     */
    @Override
    public void updateAddress(Address address) {
        logger.info("Début de la mise à jour de l'adresse avec l'ID : " + address.getId());
        try {
            addressDao.updateAddress(address);
            logger.info("Adresse mise à jour avec succès pour l'ID : " + address.getId());
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de l'adresse avec l'ID : " + address.getId(), e);
            throw e;
        }
    }

    /**
     * Supprime une adresse par son identifiant.
     *
     * @param id L'identifiant de l'adresse à supprimer.
     * @throws RuntimeException Si une erreur survient lors de la suppression de l'adresse.
     */
    @Override
    public void deleteAddressById(int id) {
        logger.info("Début de la suppression de l'adresse avec l'ID : " + id);
        try {
            addressDao.deleteAddressById(id);
            logger.info("Adresse supprimée avec succès pour l'ID : " + id);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de l'adresse avec l'ID : " + id, e);
            throw e;
        }
    }

    /**
     * Recherche une adresse par son identifiant.
     *
     * @param id L'identifiant de l'adresse à rechercher.
     * @return Un {@link Optional} contenant l'adresse si elle est trouvée, sinon un {@link Optional} vide.
     */
    @Override
    public Optional<Address> findById(int id) {
        logger.info("Recherche de l'adresse avec l'ID : " + id);
        try {
            Optional<Address> address = addressDao.findById(id);
            if (address.isPresent()) {
                logger.info("Adresse trouvée avec l'ID : " + id);
            } else {
                logger.warn("Aucune adresse trouvée avec l'ID : " + id);
            }
            return address;
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche de l'adresse avec l'ID : " + id, e);
            throw e;
        }
    }

    /**
     * Récupère toutes les adresses.
     *
     * @return Une liste contenant toutes les adresses.
     * @throws RuntimeException Si une erreur survient lors de la récupération des adresses.
     */
    @Override
    public List<Address> findAll() {
        logger.info("Récupération de toutes les adresses");
        try {
            List<Address> addresses = addressDao.findAll();
            logger.info("Nombre d'adresses récupérées : " + addresses.size());
            return addresses;
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de toutes les adresses", e);
            throw e;
        }
    }

    /**
     * Recherche des adresses par nom de rue.
     *
     * @param streetName Le nom de la rue à rechercher.
     * @return Une liste d'adresses correspondant au nom de la rue.
     * @throws RuntimeException Si une erreur survient lors de la recherche des adresses.
     */
    @Override
    public List<Address> findByStreetName(String streetName) {
        logger.info("Recherche d'adresses pour la rue : " + streetName);
        try {
            List<Address> addresses = addressDao.findByStreetName(streetName);
            logger.info("Nombre d'adresses trouvées pour la rue " + streetName + " : " + addresses.size());
            return addresses;
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche d'adresses pour la rue : " + streetName, e);
            throw e;
        }
    }

    /**
     * Recherche des adresses par identifiant de localité.
     *
     * @param localityId L'identifiant de la localité.
     * @return Une liste d'adresses correspondant à la localité.
     * @throws RuntimeException Si une erreur survient lors de la recherche des adresses.
     */
    @Override
    public List<Address> findByLocalityId(int localityId) {
        logger.info("Recherche d'adresses pour la localité avec l'ID : " + localityId);
        try {
            List<Address> addresses = addressDao.findByLocalityId(localityId);
            logger.info("Nombre d'adresses trouvées pour la localité avec l'ID " + localityId + " : " + addresses.size());
            return addresses;
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche d'adresses pour la localité avec l'ID : " + localityId, e);
            throw e;
        }
    }
}
