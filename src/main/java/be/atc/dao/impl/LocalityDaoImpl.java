package be.atc.dao.impl;

import be.atc.dao.LocalityDao;
import be.atc.entities.Locality;
import be.atc.util.JpaUtil;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import java.util.List;
import java.util.Optional;

public class LocalityDaoImpl implements LocalityDao {

    private static final Logger logger = Logger.getLogger(LocalityDaoImpl.class);

    @Override
    public Optional<Locality> findById(int id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            logger.info("Recherche de la localité avec l'ID : " + id);
            Locality locality = em.find(Locality.class, id);
            if (locality != null) {
                logger.info("Localité trouvée avec l'ID : " + id);
            } else {
                logger.warn("Aucune localité trouvée avec l'ID : " + id);
            }
            return Optional.ofNullable(locality);
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche de la localité avec l'ID : " + id, e);
            throw e;
        } finally {
            em.close();
            logger.debug("EntityManager fermé après recherche de la localité avec l'ID : " + id);
        }
    }

    @Override
    public List<Locality> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            logger.info("Récupération de toutes les localités");
            List<Locality> localities = em.createNamedQuery("Locality.findAll", Locality.class).getResultList();
            logger.info("Nombre de localités récupérées : " + localities.size());
            return localities;
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de toutes les localités", e);
            throw e;
        } finally {
            em.close();
            logger.debug("EntityManager fermé après récupération de toutes les localités");
        }
    }
}

