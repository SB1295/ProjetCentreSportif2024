package be.atc.services;

import be.atc.entities.Locality;
import java.util.List;
import java.util.Optional;

/**
 * Interface définissant les opérations de service pour la gestion des localités.
 */
public interface LocalityService {

    /**
     * Recherche une localité par son identifiant.
     *
     * @param id L'identifiant de la localité à rechercher.
     * @return Un {@link Optional} contenant la localité si elle est trouvée, sinon un {@link Optional} vide.
     */
    Optional<Locality> findById(int id);

    /**
     * Récupère toutes les localités du système.
     *
     * @return Une liste de toutes les localités.
     */
    List<Locality> findAll();
}
