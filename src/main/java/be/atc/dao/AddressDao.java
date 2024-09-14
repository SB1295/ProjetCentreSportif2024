package be.atc.dao;

import be.atc.entities.Address;

import java.util.List;
import java.util.Optional;

/**
 * Interface pour les opérations de base de données liées à l'entité {@link Address}.
 */
public interface AddressDao {

    /**
     * Crée une nouvelle adresse dans la base de données.
     *
     * @param address L'adresse à créer.
     */
    void createAddress(Address address);

    /**
     * Met à jour une adresse existante dans la base de données.
     *
     * @param address L'adresse à mettre à jour.
     */
    void updateAddress(Address address);

    /**
     * Supprime une adresse de la base de données par son ID.
     *
     * @param id L'ID de l'adresse à supprimer.
     */
    void deleteAddressById(int id);

    /**
     * Recherche une adresse par son ID.
     *
     * @param id L'ID de l'adresse à rechercher.
     * @return Un {@link Optional} contenant l'adresse si elle est trouvée, ou vide sinon.
     */
    Optional<Address> findById(int id);

    /**
     * Récupère toutes les adresses de la base de données.
     *
     * @return Une liste de toutes les adresses.
     */
    List<Address> findAll();

    /**
     * Recherche des adresses par nom de rue.
     *
     * @param streetName Le nom de la rue à rechercher.
     * @return Une liste d'adresses correspondant au nom de rue.
     */
    List<Address> findByStreetName(String streetName);

    /**
     * Recherche des adresses par ID de localité.
     *
     * @param localityId L'ID de la localité à rechercher.
     * @return Une liste d'adresses correspondant à l'ID de la localité.
     */
    List<Address> findByLocalityId(int localityId);
}
