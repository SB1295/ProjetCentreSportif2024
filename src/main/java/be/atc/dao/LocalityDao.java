package be.atc.dao;

import be.atc.entities.Locality;
import java.util.List;
import java.util.Optional;

/**
 * Interface pour les opérations de base de données liées à l'entité {@link Locality}.
 */
public interface LocalityDao {

    /**
     * Recherche une localité par son ID.
     *
     * @param id L'ID de la localité à rechercher.
     * @return Un {@link Optional} contenant la localité si elle est trouvée, ou vide sinon.
     */
    Optional<Locality> findById(int id);

    /**
     * Récupère toutes les localités de la base de données.
     *
     * @return Une liste de toutes les localités.
     */
    List<Locality> findAll();
}

