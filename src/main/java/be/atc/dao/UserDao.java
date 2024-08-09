package be.atc.dao;
import be.atc.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    // Créer un utilisateur
    void createUser(User user);

    // Mettre à jour un utilisateur existant
    void updateUser(User user);

    // Trouver un utilisateur par email
    Optional<User> findByEmail(String email);

    // Récupérer tous les utilisateurs
    List<User> findAll();

    // Vérifier si un email existe déjà
    boolean existsByEmail(String email);

    // Supprimer un utilisateur par email
    void deleteByEmail(String email);

}
