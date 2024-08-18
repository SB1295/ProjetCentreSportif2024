package be.atc.services.impl;


import be.atc.entities.User;
import be.atc.entities.Role; // Permet dans createUser de créer une instance de l'objet role pour définir le rôle par défaut d'un nouvel utilisateur

import be.atc.dao.UserDao;
import be.atc.dao.impl.UserDaoImpl;

import be.atc.services.UserService;
import org.apache.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;



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

        // Validation du prénom
        if (!isValidFirstName(user.getFirstName())) {
            logger.error("Prénom invalide : " + user.getFirstName());
            throw new IllegalArgumentException("INVALID_FIRST_NAME");
        }

        // Validation du nom de famille
        if (!isValidLastName(user.getLastName())) {
            logger.error("Nom de famille invalide : " + user.getLastName());
            throw new IllegalArgumentException("INVALID_LAST_NAME");
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
    public User validateAndPrepareUserForUpdate(User user, String newEmail, String confirmEmail, String newPassword, String confirmPassword,
                                                String phone, String gender, String birthdate, String firstName, String lastName,
                                                String blacklist, String active, String fkRole, boolean isAdmin) {
        logger.info("Validation et préparation de l'utilisateur pour la mise à jour avec l'email : " + user.getEmail());

        // Nettoyage et vérification du prénom
        if (firstName != null && !firstName.isEmpty()) {
            firstName = firstName.trim().replaceAll("\\s+", " "); // Nettoyer les espaces
            if (!isValidFirstName(firstName)) {
                logger.error("Prénom invalide pour : " + firstName);
                throw new IllegalArgumentException("INVALID_FIRST_NAME");
            }
            user.setFirstName(firstName);
            logger.debug("Prénom mis à jour : " + firstName);
        }

        // Nettoyage et vérification du nom de famille
        if (lastName != null && !lastName.isEmpty()) {
            lastName = lastName.trim().replaceAll("\\s+", " "); // Nettoyer les espaces
            if (!isValidLastName(lastName)) {
                logger.error("Nom de famille invalide pour : " + lastName);
                throw new IllegalArgumentException("INVALID_LAST_NAME");
            }
            user.setLastName(lastName);
            logger.debug("Nom de famille mis à jour : " + lastName);
        }

        // Vérification de l'email si fourni
        if (newEmail != null && !newEmail.isEmpty()) {
            // Si confirmEmail est vide, on le considère comme égal à newEmail. Permet de modifier les autres champs sans modifier l'email
            if (confirmEmail == null || confirmEmail.isEmpty()) {
                confirmEmail = newEmail; //
            }
            if (!newEmail.equals(confirmEmail)) {
                logger.error("Les emails ne correspondent pas : " + newEmail + " et " + confirmEmail);
                throw new IllegalArgumentException("EMAILS_DO_NOT_MATCH");
            }
            if (!isValidEmail(newEmail)) {
                logger.error("Format de l'email invalide pour : " + newEmail);
                throw new IllegalArgumentException("INVALID_EMAIL_FORMAT");
            }
            if (existsByEmail(newEmail) && !newEmail.equals(user.getEmail())) {
                logger.error("L'email est déjà utilisé par un autre utilisateur : " + newEmail);
                throw new IllegalArgumentException("EMAIL_ALREADY_EXISTS");
            }
            user.setEmail(newEmail);
            logger.debug("Email mis à jour : " + newEmail);
        }

        // Vérification et mise à jour du mot de passe si fourni
        if (newPassword != null && !newPassword.isEmpty()) {
            if (!newPassword.equals(confirmPassword)) {
                logger.error("Les mots de passe ne correspondent pas pour l'utilisateur avec l'email : " + user.getEmail());
                throw new IllegalArgumentException("PASSWORDS_DO_NOT_MATCH");
            }
            if (!isValidPassword(newPassword)) {
                logger.error("Format de mot de passe invalide pour : " + user.getEmail());
                throw new IllegalArgumentException("INVALID_PASSWORD_FORMAT");
            }
            String hashedPassword = hashPassword(newPassword);
            user.setPassword(hashedPassword);
            logger.debug("Mot de passe mis à jour et haché.");
        }

        // Mise à jour du numéro de téléphone si fourni
        if (phone != null && !phone.isEmpty()) {
            if (!isValidPhone(phone)) {
                logger.error("Numéro de téléphone invalide pour : " + phone);
                throw new IllegalArgumentException("INVALID_PHONE_NUMBER");
            }
            user.setPhone(phone);
            logger.debug("Numéro de téléphone mis à jour : " + phone);
        }

        // Vérification et mise à jour du genre si fourni
        if (gender != null && !gender.isEmpty()) {
            if (!isValidGender(gender)) {
                logger.error("Genre invalide pour : " + gender);
                throw new IllegalArgumentException("INVALID_GENDER");
            }
            user.setGender(gender);
            logger.debug("Genre mis à jour : " + gender);
        }

        // Vérification et mise à jour de la date de naissance si fournie
        if (birthdate != null && !birthdate.isEmpty()) {
            try {
                LocalDate parsedBirthdate = LocalDate.parse(birthdate);
                user.setBirthdate(parsedBirthdate);
                logger.debug("Date de naissance mise à jour : " + parsedBirthdate);
            } catch (DateTimeParseException e) {
                logger.error("Date de naissance invalide : " + birthdate);
                throw new IllegalArgumentException("INVALID_BIRTHDATE");
            }
        }

        // Gestion des champs réservés aux administrateurs
        if (isAdmin) {
            // Mise à jour du statut de la liste noire
            if (blacklist != null) {
                user.setBlacklist(true);
                logger.debug("Utilisateur blacklisté.");
            } else {
                user.setBlacklist(false);
                logger.debug("Utilisateur retiré de la blacklist.");
            }

            // Mise à jour du statut actif
            if (active != null) {
                user.setActive(true);
                logger.debug("Utilisateur activé.");
            } else {
                user.setActive(false);
                logger.debug("Utilisateur désactivé.");
            }

            // Mise à jour du rôle
            if (fkRole != null && !fkRole.isEmpty()) {
                try {
                    int roleId = Integer.parseInt(fkRole);
                    Role role = new Role();
                    role.setId(roleId);
                    user.setFkRole(role);
                    logger.debug("Rôle de l'utilisateur mis à jour : " + roleId);
                } catch (NumberFormatException e) {
                    logger.error("ID de rôle invalide : " + fkRole);
                    throw new IllegalArgumentException("INVALID_ROLE_ID");
                }
            }
        }

        return user;
    }


    @Override
    public void updateUser(User user) {
        logger.info("Mise à jour de l'utilisateur avec l'email : " + user.getEmail());

        // Appel au DAO pour mettre à jour l'utilisateur dans la base de données
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

        // Vérification de l'utilisateur :
        // isPresent est une méthode de Optional pour vérifier si une valeur est présente. Renvoie True / False
        if (userOptional.isPresent()) {
            User user = userOptional.get(); // get() Méthode de Optional qui retourne la valeur contenue dans Optional

            // Vérification si l'utilisateur est actif
            if (!user.getActive()) {
                logger.warn("Échec de l'authentification : utilisateur désactivé avec l'email : " + email);
                throw new IllegalArgumentException("USER_NOT_ACTIVE");
            }
            // Vérification du mot de passe
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

    private boolean isValidGender(String gender) {
        return "Male".equals(gender) || "Female".equals(gender) || "Other".equals(gender);
    }

    private boolean isValidBirthdate(String birthdate) {
        try {
            LocalDate.parse(birthdate);  // Vérifie si la date est valide
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    // Validation du numéro de téléphone
    private boolean isValidPhone(String phone) {
        String phoneRegex = "^[0-9\\-\\+]{9,20}$";
        boolean isValid = Pattern.matches(phoneRegex, phone);
        logger.debug("Validation du numéro de téléphone : " + phone + " - Valide : " + isValid);
        return isValid;
    }

    // Validation du prénom
    private boolean isValidFirstName(String firstName) {
        String nameRegex = "^[A-Za-zÀ-ÖØ-öø-ÿ\\'\\-]+(\\s[A-Za-zÀ-ÖØ-öø-ÿ\\'\\-]+)*$";
        boolean isValid = Pattern.matches(nameRegex, firstName);
        logger.debug("Validation du prénom : " + firstName + " - Valide : " + isValid);
        return isValid;
    }

    // Validation du nom de famille
    private boolean isValidLastName(String lastName) {
        String nameRegex = "^[A-Za-zÀ-ÖØ-öø-ÿ\\'\\-]+(\\s[A-Za-zÀ-ÖØ-öø-ÿ\\'\\-]+)*$";
        boolean isValid = Pattern.matches(nameRegex, lastName);
        logger.debug("Validation du nom de famille : " + lastName + " - Valide : " + isValid);
        return isValid;
    }
    // Méthode filtre de recherche sur : email, lastName, firstName
    public List<User> filterUsers(List<User> users, String searchQuery) {
        if (searchQuery == null || searchQuery.isEmpty()) {
            return users;
        }

        final String lowerCaseSearchQuery = searchQuery.toLowerCase();

        return users.stream()
                .filter(user -> user.getFirstName().toLowerCase().contains(lowerCaseSearchQuery) ||
                        user.getLastName().toLowerCase().contains(lowerCaseSearchQuery) ||
                        user.getEmail().toLowerCase().contains(lowerCaseSearchQuery))
                .collect(Collectors.toList());
    }

    // Trouver un utilisateur par Id
    @Override
    public Optional<User> findById(int id) {
        logger.debug("Recherche de l'utilisateur avec l'ID : " + id);
        Optional<User> user = userDao.findById(id);
        if (user.isPresent()) {
            logger.info("Utilisateur trouvé avec l'ID : " + id);
        } else {
            logger.warn("Aucun utilisateur trouvé avec l'ID : " + id);
        }
        return user;
    }

    // Suppression d'un utilisateur par Id
    @Override
    public void deleteById(int id) {
        logger.info("Suppression de l'utilisateur avec l'ID : " + id);
        userDao.deleteById(id);
        logger.info("Utilisateur supprimé avec succès : ID " + id);
    }

}
