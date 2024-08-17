package be.atc.services.impl;

import be.atc.dao.LocalityDao;
import be.atc.dao.impl.LocalityDaoImpl;
import be.atc.entities.Locality;
import be.atc.services.LocalityService;
import org.apache.log4j.Logger;
import java.util.List;
import java.util.Optional;

public class LocalityServiceImpl implements LocalityService {

    private static final Logger logger = Logger.getLogger(LocalityServiceImpl.class);
    private final LocalityDao localityDao;

    public LocalityServiceImpl() {
        this.localityDao = new LocalityDaoImpl();
    }

    @Override
    public Optional<Locality> findById(int id) {
        logger.info("Recherche de la localité avec l'ID : " + id);
        return localityDao.findById(id);
    }

    @Override
    public List<Locality> findAll() {
        logger.info("Récupération de toutes les localités");
        return localityDao.findAll();
    }
}
