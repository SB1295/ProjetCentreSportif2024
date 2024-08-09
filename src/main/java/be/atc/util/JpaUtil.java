package be.atc.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaUtil {

    // Nom de l'unité de persistance définie dans persistence.xml
    private static final String PERSISTENCE_UNIT_NAME = "projetCentreSportifPU";

    // Singleton pour l'EntityManagerFactory
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

    // Méthode pour obtenir un EntityManager
    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    // Méthode pour fermer l'EntityManagerFactory lors de l'arrêt de l'application
    public static void closeEntityManagerFactory() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}
