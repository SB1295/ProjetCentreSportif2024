package be.atc.services.impl;

import be.atc.dao.LocalityDao;
import be.atc.dao.impl.LocalityDaoImpl;
import be.atc.entities.Locality;
import be.atc.services.LocalityService;
import org.apache.log4j.Logger;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service pour gérer les localités.
 * Cette classe utilise un DAO pour interagir avec la base de données.
 */
public class LocalityServiceImpl implements LocalityService {

    private static final Logger logger = Logger.getLogger(LocalityServiceImpl.class);
    private final LocalityDao localityDao;

    /**
     * Constructeur par défaut qui initialise le DAO pour les localités.
     */
    public LocalityServiceImpl() {
        this.localityDao = new LocalityDaoImpl();
    }

    /**
     * Recherche une localité par son identifiant.
     *
     * @param id L'identifiant de la localité à rechercher.
     * @return Un {@link Optional} contenant la localité si elle est trouvée, sinon un {@link Optional} vide.
     */
    @Override
    public Optional<Locality> findById(int id) {
        logger.info("Recherche de la localité avec l'ID : " + id);
        return localityDao.findById(id);
    }

    /**
     * Récupère toutes les localités.
     *
     * @return Une liste contenant toutes les localités.
     */
    @Override
    public List<Locality> findAll() {
        logger.info("Récupération de toutes les localités");
        return localityDao.findAll();
    }
}
