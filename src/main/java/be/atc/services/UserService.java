package be.atc.services;

import be.atc.entities.User;

import java.util.List;
import java.util.Optional;

/**
 * Interface définissant les opérations de service pour la gestion des utilisateurs.
 */
public interface UserService {

    /**
     * Crée un nouvel utilisateur.
     *
     * @param user            L'utilisateur à créer.
     * @param confirmPassword Le mot de passe de confirmation pour valider l'utilisateur.
     * @throws IllegalArgumentException Si les informations fournies ne sont pas valides.
     */
    void createUser(User user, String confirmPassword);

    /**
     * Met à jour un utilisateur existant.
     *
     * @param user L'utilisateur à mettre à jour.
     */
    void updateUser(User user);

    /**
     * Valide et prépare les informations de l'utilisateur pour la mise à jour.
     *
     * @param user             L'utilisateur à valider et préparer.
     * @param newEmail         Le nouvel email, si applicable.
     * @param confirmEmail     L'email de confirmation.
     * @param newPassword      Le nouveau mot de passe, si applicable.
     * @param confirmPassword  Le mot de passe de confirmation.
     * @param phone            Le numéro de téléphone, si applicable.
     * @param gender           Le genre de l'utilisateur.
     * @param birthdate        La date de naissance de l'utilisateur.
     * @param firstName        Le prénom de l'utilisateur.
     * @param lastName         Le nom de famille de l'utilisateur.
     * @param blacklist        Statut de la liste noire (pour les administrateurs).
     * @param active           Statut actif (pour les administrateurs).
     * @param fkRole           Le rôle de l'utilisateur (pour les administrateurs).
     * @param isAdmin          Indicateur si l'utilisateur est administrateur.
     * @return L'utilisateur avec les informations mises à jour.
     * @throws IllegalArgumentException Si les informations fournies ne sont pas valides.
     */
    User validateAndPrepareUserForUpdate(User user, String newEmail, String confirmEmail,
                                         String newPassword, String confirmPassword, String phone,
                                         String gender, String birthdate, String firstName, String lastName,
                                         String blacklist, String active, String fkRole, boolean isAdmin);

    /**
     * Recherche un utilisateur par son email.
     *
     * @param email L'email de l'utilisateur à rechercher.
     * @return Un {@link Optional} contenant l'utilisateur s'il est trouvé, sinon un {@link Optional} vide.
     */
    Optional<User> findByEmail(String email);

    /**
     * Récupère tous les utilisateurs du système.
     *
     * @return Une liste de tous les utilisateurs.
     */
    List<User> findAll();

    /**
     * Vérifie si un email existe déjà dans le système.
     *
     * @param email L'email à vérifier.
     * @return {@code true} si l'email existe, sinon {@code false}.
     */
    boolean existsByEmail(String email);

    /**
     * Supprime un utilisateur par son email.
     *
     * @param email L'email de l'utilisateur à supprimer.
     */
    void deleteByEmail(String email);

    /**
     * Authentifie un utilisateur en fonction de son email et de son mot de passe.
     *
     * @param email    L'email de l'utilisateur.
     * @param password Le mot de passe de l'utilisateur.
     * @return L'utilisateur authentifié si les informations sont correctes.
     * @throws IllegalArgumentException Si l'authentification échoue.
     */
    User authenticateUser(String email, String password);

    /**
     * Filtre une liste d'utilisateurs en fonction d'une requête de recherche.
     *
     * @param users       La liste d'utilisateurs à filtrer.
     * @param searchQuery La requête de recherche à utiliser pour le filtrage.
     * @return Une liste d'utilisateurs filtrés correspondant à la requête de recherche.
     */
    List<User> filterUsers(List<User> users, String searchQuery);

    /**
     * Recherche un utilisateur par son identifiant.
     *
     * @param id L'identifiant de l'utilisateur à rechercher.
     * @return Un {@link Optional} contenant l'utilisateur s'il est trouvé, sinon un {@link Optional} vide.
     */
    Optional<User> findById(int id);

    /**
     * Supprime un utilisateur par son identifiant.
     *
     * @param id L'identifiant de l'utilisateur à supprimer.
     */
    void deleteById(int id);
}
