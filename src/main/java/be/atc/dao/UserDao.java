package be.atc.dao;

import be.atc.entities.User;
import java.util.List;
import java.util.Optional;

/**
 * Interface pour les opérations de base de données liées aux utilisateurs.
 * Fournit des méthodes pour créer, mettre à jour, trouver et supprimer des utilisateurs.
 */
public interface UserDao {

    /**
     * Crée un nouvel utilisateur dans la base de données.
     *
     * @param user L'utilisateur à créer.
     */
    void createUser(User user);

    /**
     * Met à jour un utilisateur existant dans la base de données.
     *
     * @param user L'utilisateur avec les informations mises à jour.
     */
    void updateUser(User user);

    /**
     * Trouve un utilisateur par son adresse email.
     *
     * @param email L'email de l'utilisateur à rechercher.
     * @return Un {@link Optional} contenant l'utilisateur si trouvé, ou vide sinon.
     */
    Optional<User> findByEmail(String email);

    /**
     * Récupère tous les utilisateurs dans la base de données.
     *
     * @return Une liste d'utilisateurs.
     */
    List<User> findAll();

    /**
     * Vérifie si un utilisateur avec un email donné existe déjà dans la base de données.
     *
     * @param email L'email à vérifier.
     * @return {@code true} si l'email existe, {@code false} sinon.
     */
    boolean existsByEmail(String email);

    /**
     * Supprime un utilisateur de la base de données en fonction de son adresse email.
     *
     * @param email L'email de l'utilisateur à supprimer.
     */
    void deleteByEmail(String email);

    /**
     * Supprime un utilisateur de la base de données en fonction de son ID.
     *
     * @param id L'ID de l'utilisateur à supprimer.
     */
    void deleteById(int id);

    /**
     * Trouve un utilisateur par son ID.
     *
     * @param id L'ID de l'utilisateur à rechercher.
     * @return Un {@link Optional} contenant l'utilisateur si trouvé, ou vide sinon.
     */
    Optional<User> findById(int id);
}
