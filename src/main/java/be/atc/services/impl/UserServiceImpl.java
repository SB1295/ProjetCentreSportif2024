package be.atc.services.impl;

import be.atc.dao.UserDao;
import be.atc.entities.User;
import be.atc.services.UserService;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    // Injection du DAO via le constructeur
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void createUser(User user, String confirmPassword) {
        // Vérifier si les mots de passe correspondent
        if (!user.getPassword().equals(confirmPassword)) {
            throw new IllegalArgumentException("Les mots de passe ne correspondent pas.");
        }
        // Valider le mot de passe
        if (!isValidPassword(user.getPassword())) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 8 caractères, une majuscule, et un chiffre.");
        }

        // Hachage du mot de passe avec BCrypt
        String hashedPassword = hashPassword(user.getPassword());
        user.setPassword(hashedPassword);

        // Logique métier : Vérifier si l'email existe déjà
        if (userDao.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("L'email est déjà utilisé.");
        } else {
            userDao.createUser(user);
        }
    }

    @Override
    public void updateUser(User user) {
        // Valider le mot de passe
        if (!isValidPassword(user.getPassword())) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 8 caractères, une majuscule, et un chiffre.");
        }

        // Hachage du mot de passe avec BCrypt
        String hashedPassword = hashPassword(user.getPassword());
        user.setPassword(hashedPassword);

        userDao.updateUser(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public boolean existsByEmail(String email) {
        return userDao.existsByEmail(email);
    }

    @Override
    public void deleteByEmail(String email) {
        userDao.deleteByEmail(email);
    }

    // Validation du mot de passe
    private boolean isValidPassword(String password) {
        // Expression régulière pour valider les règles du mot de passe
        String passwordRegex = "^(?=.*[A-Z])(?=.*\\d).{8,}$";
        return Pattern.matches(passwordRegex, password);
    }

    // Méthode de hachage du mot de passe avec BCrypt
    private String hashPassword(String password) {
        // BCrypt génère automatiquement un sel pour chaque mot de passe
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Méthode pour vérifier un mot de passe avec son hash (utile pour la connexion)
    public boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
