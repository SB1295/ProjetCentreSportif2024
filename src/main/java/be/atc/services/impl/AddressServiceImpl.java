package be.atc.services.impl;

import be.atc.dao.AddressDao;
import be.atc.dao.impl.AddressDaoImpl;
import be.atc.entities.Address;
import be.atc.services.AddressService;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class AddressServiceImpl implements AddressService {

    private static final Logger logger = Logger.getLogger(AddressServiceImpl.class);

    private final AddressDao addressDao;

    public AddressServiceImpl() {
        this.addressDao = new AddressDaoImpl(); // Utilisation de l'implémentation DAO
    }

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
