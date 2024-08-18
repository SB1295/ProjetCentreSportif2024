package be.atc.services;

import be.atc.entities.Address;

import java.util.List;
import java.util.Optional;

/**
 * Interface définissant les opérations de service pour la gestion des adresses.
 */
public interface AddressService {

    /**
     * Crée une nouvelle adresse dans le système.
     *
     * @param address L'adresse à créer.
     */
    void createAddress(Address address);

    /**
     * Met à jour une adresse existante.
     *
     * @param address L'adresse à mettre à jour.
     */
    void updateAddress(Address address);

    /**
     * Supprime une adresse en fonction de son identifiant.
     *
     * @param id L'identifiant de l'adresse à supprimer.
     */
    void deleteAddressById(int id);

    /**
     * Recherche une adresse par son identifiant.
     *
     * @param id L'identifiant de l'adresse à rechercher.
     * @return Un {@link Optional} contenant l'adresse si elle est trouvée, sinon un {@link Optional} vide.
     */
    Optional<Address> findById(int id);

    /**
     * Récupère toutes les adresses du système.
     *
     * @return Une liste de toutes les adresses.
     */
    List<Address> findAll();

    /**
     * Recherche des adresses en fonction du nom de la rue.
     *
     * @param streetName Le nom de la rue à rechercher.
     * @return Une liste d'adresses correspondant au nom de la rue spécifiée.
     */
    List<Address> findByStreetName(String streetName);

    /**
     * Recherche des adresses en fonction de l'identifiant de la localité.
     *
     * @param localityId L'identifiant de la localité à rechercher.
     * @return Une liste d'adresses correspondant à l'identifiant de la localité spécifiée.
     */
    List<Address> findByLocalityId(int localityId);
}
