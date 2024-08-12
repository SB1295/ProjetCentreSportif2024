package be.atc.services.impl;


import be.atc.entities.User;
import be.atc.entities.Role; // Permet dans createUser de créer une instance de l'objet role pour définir le rôle par défaut d'un nouvel utilisateur

import be.atc.dao.UserDao;
import be.atc.dao.impl.UserDaoImpl;

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

    // Initialisation du DAO directement dans le constructeur
    public UserServiceImpl() {
        this.userDao = new UserDaoImpl();  // Ici, vous initialisez directement le UserDaoImpl
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
        }

        // Attribuer un rôle par défaut si aucun rôle n'est défini
        if (user.getFkRole() == null) {
            logger.info("Aucun rôle défini pour l'utilisateur, attribution du rôle par défaut 'USER'");

            // Créer un objet Role avec l'ID 1 pour le rôle 'USER'
            Role defaultRole = new Role();
            defaultRole.setId(1); // Supposons que le rôle 'USER' ait l'ID 1

            // Attribuer ce rôle à l'utilisateur
            user.setFkRole(defaultRole);
        }

        // Sauvegarder l'utilisateur dans la base de données
        userDao.createUser(user);
        logger.info("Utilisateur créé avec succès : " + user.getEmail());
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

    @Override
    public User authenticateUser(String email, String password) {
        logger.info("Tentative d'authentification pour l'utilisateur avec l'email : " + email);

        // Recherche de l'utilisateur par email. Utilisation de Optional pour gérer l'absence de valeur et éviter l'erreur 'NullPointerException'
        // findByEmail retourne un Optional<User>, l'attribution est donc logique dans une nouvelle variable Optional<User>
        Optional<User> userOptional = userDao.findByEmail(email);

        // Vérification de l'utilisateur et du mot de passe
        // isPresent est une méthode de Optional pour vérifier si une valeur est présente. Renvoie True / False
        if (userOptional.isPresent()) {
            User user = userOptional.get(); // get() Méthode de Optional qui retourne la valeur contenue dans Optional
            if (checkPassword(password, user.getPassword())) {
                logger.info("Authentification réussie pour l'utilisateur avec l'email : " + email);
                return user;
            } else {
                logger.warn("Échec de l'authentification : mot de passe incorrect pour l'email : " + email);
            }
        } else {
            logger.warn("Échec de l'authentification : aucun utilisateur trouvé avec l'email : " + email);
        }

        // Retourne null si l'authentification échoue
        return null;
    }

}
