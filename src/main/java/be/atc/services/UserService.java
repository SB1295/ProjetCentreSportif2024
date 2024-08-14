package be.atc.services;

import be.atc.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    // Créer un utilisateur
    void createUser(User user, String confirmPassword);

    // Mettre à jour un utilisateur existant
    void updateUser(User user);

    // Validation des informations de l'user avant la mise à jour
    User validateAndPrepareUserForUpdate(User user, String newEmail, String confirmEmail,
                                         String newPassword, String confirmPassword, String phone,
                                         String gender, String birthdate, String firstName, String lastName,
                                         String blacklist, String active, String fkRole, boolean isAdmin);

    // Trouver un utilisateur par email
    Optional<User> findByEmail(String email);

    // Récupérer tous les utilisateurs
    List<User> findAll();

    // Vérifier si un email existe déjà
    boolean existsByEmail(String email);

    // Supprimer un utilisateur par email
    void deleteByEmail(String email);

    // Authentification de l'utilisateur
    User authenticateUser(String email, String password);

}

