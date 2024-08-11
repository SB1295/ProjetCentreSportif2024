package be.atc.services.impl;

import be.atc.dao.UserDao;
import be.atc.entities.User;
import be.atc.services.UserService;
import org.apache.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class UserServiceImpl implements UserService {

    // Initialisation du logger de UserServiceImpl
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class);
    private final UserDao userDao;

    // Injection du DAO via le constructeur
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void createUser(User user, String confirmPassword) {
        logger.info("Début de la création de l'utilisateur avec l'email : " + user.getEmail());

        // Validation de l'email
        if (!isValidEmail(user.getEmail())) {
            logger.error("Format de l'email invalide pour : " + user.getEmail());
            throw new IllegalArgumentException("INVALID_EMAIL_FORMAT");
        }

        // Vérifier si les mots de passe correspondent
        if (!user.getPassword().equals(confirmPassword)) {
            logger.error("Les mots de passe ne correspondent pas pour l'email : " + user.getEmail());
            throw new IllegalArgumentException("PASSWORDS_DO_NOT_MATCH");
        }

        // Valider le mot de passe
        if (!isValidPassword(user.getPassword())) {
            logger.error("Format de mot de passe invalide pour l'utilisateur avec l'email : " + user.getEmail());
            throw new IllegalArgumentException("INVALID_PASSWORD_FORMAT");
        }

        // Hachage du mot de passe avec BCrypt
        String hashedPassword = hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        logger.debug("Mot de passe haché pour l'utilisateur avec l'email : " + user.getEmail());

        // Logique métier : Vérifier si l'email existe déjà
        if (userDao.existsByEmail(user.getEmail())) {
            logger.error("L'email existe déjà dans la base de données : " + user.getEmail());
            throw new IllegalArgumentException("EMAIL_ALREADY_EXISTS");
        } else {
            userDao.createUser(user);
            logger.info("Utilisateur créé avec succès : " + user.getEmail());
        }
    }

    @Override
    public void updateUser(User user) {
        logger.info("Mise à jour de l'utilisateur avec l'email : " + user.getEmail());

        // Validation de l'email
        if (!isValidEmail(user.getEmail())) {
            logger.error("Format de l'email invalide pour : " + user.getEmail());
            throw new IllegalArgumentException("INVALID_EMAIL_FORMAT");
        }

        // Valider le mot de passe
        if (!isValidPassword(user.getPassword())) {
            logger.error("Format de mot de passe invalide pour l'utilisateur avec l'email : " + user.getEmail());
            throw new IllegalArgumentException("INVALID_PASSWORD_FORMAT");
        }

        // Hachage du mot de passe avec BCrypt
        String hashedPassword = hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        logger.debug("Mot de passe haché pour l'utilisateur avec l'email : " + user.getEmail());

        userDao.updateUser(user);
        logger.info("Utilisateur mis à jour avec succès : " + user.getEmail());
    }

    @Override
    public Optional<User> findByEmail(String email) {
        logger.debug("Recherche de l'utilisateur avec l'email : " + email);
        Optional<User> user = userDao.findByEmail(email);
        if (user.isPresent()) {
            logger.info("Utilisateur trouvé avec l'email : " + email);
        } else {
            logger.info("Aucun utilisateur trouvé avec l'email : " + email);
        }
        return user;
    }

    @Override
    public List<User> findAll() {
        logger.debug("Récupération de tous les utilisateurs");
        List<User> users = userDao.findAll();
        logger.info("Nombre d'utilisateurs récupérés : " + users.size());
        return users;
    }

    @Override
    public boolean existsByEmail(String email) {
        logger.debug("Vérification de l'existence de l'email : " + email);
        boolean exists = userDao.existsByEmail(email);
        logger.info("L'email " + email + " existe : " + exists);
        return exists;
    }

    @Override
    public void deleteByEmail(String email) {
        logger.info("Suppression de l'utilisateur avec l'email : " + email);
        userDao.deleteByEmail(email);
        logger.info("Utilisateur supprimé avec succès : " + email);
    }

    // Validation du mot de passe
    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[A-Z])(?=.*\\d).{8,}$";
        boolean isValid = Pattern.matches(passwordRegex, password);
        logger.debug("Validation du mot de passe : " + isValid);
        return isValid;
    }

    // Méthode de hachage du mot de passe avec BCrypt
    private String hashPassword(String password) {
        logger.debug("Hachage du mot de passe");
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Méthode pour vérifier un mot de passe avec son hash (utile pour la connexion)
    public boolean checkPassword(String plainPassword, String hashedPassword) {
        logger.debug("Vérification du mot de passe haché");
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    // Méthode pour valider l'email
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        boolean isValid = Pattern.matches(emailRegex, email);
        logger.debug("Validation de l'email : " + email + " - Valide : " + isValid);
        return isValid;
    }
}
