package be.atc.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Utilitaire pour la gestion de l'EntityManager et de l'EntityManagerFactory.
 * <p>
 * Cette classe fournit des méthodes pour obtenir un EntityManager à partir
 * d'une EntityManagerFactory et pour fermer l'EntityManagerFactory lors de
 * l'arrêt de l'application.
 * </p>
 */
public class JpaUtil {

    /**
     * Nom de l'unité de persistance définie dans persistence.xml.
     */
    private static final String PERSISTENCE_UNIT_NAME = "projetCentreSportifPU";

    /**
     * Singleton pour l'EntityManagerFactory.
     */
    private static EntityManagerFactory entityManagerFactory;

    // Initialisation de l'EntityManagerFactory
    static {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError("Initialisation de l'EntityManagerFactory a échoué");
        }
    }

    /**
     * Obtient un EntityManager à partir de l'EntityManagerFactory.
     *
     * @return Un nouvel EntityManager.
     */
    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    /**
     * Ferme l'EntityManagerFactory lors de l'arrêt de l'application.
     * <p>
     * Cette méthode doit être appelée pour libérer les ressources allouées par
     * l'EntityManagerFactory. Elle doit être invoquée lorsque l'application est
     * en cours d'arrêt.
     * </p>
     */
    public static void closeEntityManagerFactory() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}
