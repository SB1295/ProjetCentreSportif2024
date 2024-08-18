package be.atc.services.impl;

import be.atc.entities.User;
import be.atc.entities.Role;
import be.atc.dao.UserDao;
import be.atc.dao.impl.UserDaoImpl;
import be.atc.services.UserService;
import be.atc.util.ValidationUtil;
import org.apache.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implémentation du service utilisateur. Fournit des méthodes pour gérer les utilisateurs,
 * y compris la création, la mise à jour, la suppression et l'authentification des utilisateurs.
 */
public class UserServiceImpl implements UserService {

    private static final Logger logger = Logger.getLogger(UserServiceImpl.class);
    private final UserDao userDao;

    /**
     * Constructeur par défaut qui initialise le DAO pour les utilisateurs.
     */
    public UserServiceImpl() {
        this.userDao = new UserDaoImpl();
    }

    /**
     * Crée un nouvel utilisateur après avoir validé ses données.
     *
     * @param user            L'utilisateur à créer.
     * @param confirmPassword Confirmation du mot de passe.
     * @throws IllegalArgumentException Si les données de l'utilisateur sont invalides ou si l'email existe déjà.
     */
    @Override
    public void createUser(User user, String confirmPassword) {
        logger.info("Début de la création de l'utilisateur avec l'email : " + user.getEmail());

        validateUserData(user, confirmPassword);

        if (userDao.existsByEmail(user.getEmail())) {
            logger.error("L'email existe déjà dans la base de données : " + user.getEmail());
            throw new IllegalArgumentException("EMAIL_ALREADY_EXISTS");
        }

        assignDefaultRoleIfMissing(user);

        user.setPassword(hashPassword(user.getPassword()));
        userDao.createUser(user);
        logger.info("Utilisateur créé avec succès : " + user.getEmail());
    }

    /**
     * Valide les données de l'utilisateur avant de les utiliser pour créer un utilisateur.
     *
     * @param user            L'utilisateur à valider.
     * @param confirmPassword Confirmation du mot de passe.
     * @throws IllegalArgumentException Si une des validations échoue.
     */
    private void validateUserData(User user, String confirmPassword) {
        if (!ValidationUtil.isValidEmail(user.getEmail())) {
            logger.error("Format de l'email invalide pour : " + user.getEmail());
            throw new IllegalArgumentException("INVALID_EMAIL_FORMAT");
        }

        if (!ValidationUtil.isValidFirstName(user.getFirstName())) {
            logger.error("Prénom invalide : " + user.getFirstName());
            throw new IllegalArgumentException("INVALID_FIRST_NAME");
        }

        if (!ValidationUtil.isValidLastName(user.getLastName())) {
            logger.error("Nom de famille invalide : " + user.getLastName());
            throw new IllegalArgumentException("INVALID_LAST_NAME");
        }

        if (!user.getPassword().equals(confirmPassword)) {
            logger.error("Les mots de passe ne correspondent pas pour l'email : " + user.getEmail());
            throw new IllegalArgumentException("PASSWORDS_DO_NOT_MATCH");
        }

        if (!ValidationUtil.isValidPassword(user.getPassword())) {
            logger.error("Format de mot de passe invalide pour l'utilisateur avec l'email : " + user.getEmail());
            throw new IllegalArgumentException("INVALID_PASSWORD_FORMAT");
        }
    }

    /**
     * Assigne un rôle par défaut à l'utilisateur s'il n'en a pas.
     *
     * @param user L'utilisateur à qui attribuer un rôle par défaut.
     */
    private void assignDefaultRoleIfMissing(User user) {
        if (user.getFkRole() == null) {
            Role defaultRole = new Role();
            defaultRole.setId(1);
            user.setFkRole(defaultRole);
            logger.info("Aucun rôle défini pour l'utilisateur, attribution du rôle par défaut 'USER'");
        }
    }

    /**
     * Valide et prépare les données de l'utilisateur pour la mise à jour.
     *
     * @param user             L'utilisateur à mettre à jour.
     * @param newEmail         Nouvel email à mettre à jour.
     * @param confirmEmail     Confirmation du nouvel email.
     * @param newPassword      Nouveau mot de passe à mettre à jour.
     * @param confirmPassword  Confirmation du nouveau mot de passe.
     * @param phone            Numéro de téléphone à mettre à jour.
     * @param gender           Genre à mettre à jour.
     * @param birthdate        Date de naissance à mettre à jour.
     * @param firstName        Prénom à mettre à jour.
     * @param lastName         Nom de famille à mettre à jour.
     * @param blacklist        Indicateur de liste noire (pour les administrateurs).
     * @param active           Indicateur de compte actif (pour les administrateurs).
     * @param fkRole           Rôle à mettre à jour (pour les administrateurs).
     * @param isAdmin          Indicateur si l'utilisateur effectuant la mise à jour est un administrateur.
     * @return L'utilisateur mis à jour.
     * @throws IllegalArgumentException Si une des validations échoue.
     */
    @Override
    public User validateAndPrepareUserForUpdate(User user, String newEmail, String confirmEmail, String newPassword, String confirmPassword,
                                                String phone, String gender, String birthdate, String firstName, String lastName,
                                                String blacklist, String active, String fkRole, boolean isAdmin) {
        logger.info("Validation et préparation de l'utilisateur pour la mise à jour avec l'email : " + user.getEmail());

        validateAndUpdateBasicInfo(user, firstName, lastName, newEmail, confirmEmail);

        updatePasswordIfPresent(user, newPassword, confirmPassword);
        updatePhoneIfPresent(user, phone);
        updateGenderIfPresent(user, gender);
        updateBirthdateIfPresent(user, birthdate);

        if (isAdmin) {
            updateAdminFields(user, blacklist, active, fkRole);
        }

        return user;
    }

    /**
     * Valide et met à jour les informations de base de l'utilisateur.
     *
     * @param user         L'utilisateur à mettre à jour.
     * @param firstName    Nouveau prénom.
     * @param lastName     Nouveau nom de famille.
     * @param newEmail     Nouvel email.
     * @param confirmEmail Confirmation du nouvel email.
     * @throws IllegalArgumentException Si une des validations échoue.
     */
    private void validateAndUpdateBasicInfo(User user, String firstName, String lastName, String newEmail, String confirmEmail) {
        // Validation du prénom
        if (firstName != null && !firstName.isEmpty()) {
            firstName = firstName.trim().replaceAll("\\s+", " ");
            if (!ValidationUtil.isValidFirstName(firstName)) {
                logger.error("Prénom invalide pour : " + firstName);
                throw new IllegalArgumentException("INVALID_FIRST_NAME");
            }
            user.setFirstName(firstName);
            logger.debug("Prénom mis à jour : " + firstName);
        }

        // Validation du nom de famille
        if (lastName != null && !lastName.isEmpty()) {
            lastName = lastName.trim().replaceAll("\\s+", " ");
            if (!ValidationUtil.isValidLastName(lastName)) {
                logger.error("Nom de famille invalide pour : " + lastName);
                throw new IllegalArgumentException("INVALID_LAST_NAME");
            }
            user.setLastName(lastName);
            logger.debug("Nom de famille mis à jour : " + lastName);
        }

        // Validation de l'email
        if (newEmail != null && !newEmail.isEmpty()) {
            // Si le champ "Confirmer l'adresse e-mail" est vide, on considère qu'il est égal au nouvel e-mail.
            if (confirmEmail == null || confirmEmail.isEmpty()) {
                confirmEmail = newEmail;
            }

            // Vérification que les deux e-mails correspondent
            if (!newEmail.equals(confirmEmail)) {
                logger.error("Les emails ne correspondent pas : " + newEmail + " et " + confirmEmail);
                throw new IllegalArgumentException("EMAILS_DO_NOT_MATCH");
            }

            // Validation du format de l'email
            if (!ValidationUtil.isValidEmail(newEmail)) {
                logger.error("Format de l'email invalide pour : " + newEmail);
                throw new IllegalArgumentException("INVALID_EMAIL_FORMAT");
            }

            // Vérification de l'unicité de l'email
            if (userDao.existsByEmail(newEmail) && !newEmail.equals(user.getEmail())) {
                logger.error("L'email est déjà utilisé par un autre utilisateur : " + newEmail);
                throw new IllegalArgumentException("EMAIL_ALREADY_EXISTS");
            }

            // Mise à jour de l'email
            user.setEmail(newEmail);
            logger.debug("Email mis à jour : " + newEmail);
        }
    }

    /**
     * Met à jour le mot de passe de l'utilisateur s'il est présent.
     *
     * @param user            L'utilisateur dont le mot de passe doit être mis à jour.
     * @param newPassword     Nouveau mot de passe.
     * @param confirmPassword Confirmation du nouveau mot de passe.
     * @throws IllegalArgumentException Si une des validations échoue.
     */
    private void updatePasswordIfPresent(User user, String newPassword, String confirmPassword) {
        if (newPassword != null && !newPassword.isEmpty()) {
            if (!newPassword.equals(confirmPassword)) {
                logger.error("Les mots de passe ne correspondent pas pour l'utilisateur avec l'email : " + user.getEmail());
                throw new IllegalArgumentException("PASSWORDS_DO_NOT_MATCH");
            }
            if (!ValidationUtil.isValidPassword(newPassword)) {
                logger.error("Format de mot de passe invalide pour : " + user.getEmail());
                throw new IllegalArgumentException("INVALID_PASSWORD_FORMAT");
            }
            user.setPassword(hashPassword(newPassword));
            logger.debug("Mot de passe mis à jour et haché.");
        }
    }

    /**
     * Met à jour le numéro de téléphone de l'utilisateur s'il est présent.
     *
     * @param user  L'utilisateur dont le numéro de téléphone doit être mis à jour.
     * @param phone Nouveau numéro de téléphone.
     * @throws IllegalArgumentException Si le numéro de téléphone est invalide.
     */
    private void updatePhoneIfPresent(User user, String phone) {
        if (phone != null && !phone.isEmpty()) {
            if (!ValidationUtil.isValidPhone(phone)) {
                logger.error("Numéro de téléphone invalide pour : " + phone);
                throw new IllegalArgumentException("INVALID_PHONE_NUMBER");
            }
            user.setPhone(phone);
            logger.debug("Numéro de téléphone mis à jour : " + phone);
        }
    }

    /**
     * Met à jour le genre de l'utilisateur s'il est présent.
     *
     * @param user   L'utilisateur dont le genre doit être mis à jour.
     * @param gender Nouveau genre.
     * @throws IllegalArgumentException Si le genre est invalide.
     */
    private void updateGenderIfPresent(User user, String gender) {
        if (gender != null && !gender.isEmpty()) {
            if (!ValidationUtil.isValidGender(gender)) {
                logger.error("Genre invalide pour : " + gender);
                throw new IllegalArgumentException("INVALID_GENDER");
            }
            user.setGender(gender);
            logger.debug("Genre mis à jour : " + gender);
        }
    }

    /**
     * Met à jour la date de naissance de l'utilisateur si elle est présente.
     *
     * @param user      L'utilisateur dont la date de naissance doit être mise à jour.
     * @param birthdate Nouvelle date de naissance.
     * @throws IllegalArgumentException Si la date de naissance est invalide.
     */
    private void updateBirthdateIfPresent(User user, String birthdate) {
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
    }

    /**
     * Met à jour les champs administrateur de l'utilisateur si l'utilisateur courant est un administrateur.
     *
     * @param user      L'utilisateur à mettre à jour.
     * @param blacklist Indicateur si l'utilisateur doit être blacklisté.
     * @param active    Indicateur si l'utilisateur doit être activé.
     * @param fkRole    Rôle à attribuer à l'utilisateur.
     * @throws IllegalArgumentException Si l'ID de rôle est invalide.
     */
    private void updateAdminFields(User user, String blacklist, String active, String fkRole) {
        user.setBlacklist(blacklist != null);
        logger.debug("Utilisateur blacklisté : " + user.getBlacklist());

        user.setActive(active != null);
        logger.debug("Utilisateur activé : " + user.getActive());

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

    /**
     * Met à jour un utilisateur existant.
     *
     * @param user L'utilisateur à mettre à jour.
     */
    @Override
    public void updateUser(User user) {
        logger.info("Mise à jour de l'utilisateur avec l'email : " + user.getEmail());
        userDao.updateUser(user);
        logger.info("Utilisateur mis à jour avec succès : " + user.getEmail());
    }

    /**
     * Recherche un utilisateur par son email.
     *
     * @param email L'email de l'utilisateur à rechercher.
     * @return Un {@link Optional} contenant l'utilisateur trouvé, ou vide s'il n'existe pas.
     */
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

    /**
     * Récupère tous les utilisateurs.
     *
     * @return Une liste de tous les utilisateurs.
     */
    @Override
    public List<User> findAll() {
        logger.debug("Récupération de tous les utilisateurs");
        List<User> users = userDao.findAll();
        logger.info("Nombre d'utilisateurs récupérés : " + users.size());
        return users;
    }

    /**
     * Vérifie si un utilisateur existe avec l'email donné.
     *
     * @param email L'email à vérifier.
     * @return {@code true} si l'utilisateur existe, {@code false} sinon.
     */
    @Override
    public boolean existsByEmail(String email) {
        logger.debug("Vérification de l'existence de l'email : " + email);
        boolean exists = userDao.existsByEmail(email);
        logger.info("L'email " + email + " existe : " + exists);
        return exists;
    }

    /**
     * Supprime un utilisateur par son email.
     *
     * @param email L'email de l'utilisateur à supprimer.
     */
    @Override
    public void deleteByEmail(String email) {
        logger.info("Suppression de l'utilisateur avec l'email : " + email);
        userDao.deleteByEmail(email);
        logger.info("Utilisateur supprimé avec succès : " + email);
    }

    /**
     * Authentifie un utilisateur avec son email et son mot de passe.
     *
     * @param email    L'email de l'utilisateur.
     * @param password Le mot de passe de l'utilisateur.
     * @return L'utilisateur authentifié ou {@code null} si l'authentification échoue.
     * @throws IllegalArgumentException Si l'utilisateur est désactivé.
     */
    @Override
    public User authenticateUser(String email, String password) {
        logger.info("Tentative d'authentification pour l'utilisateur avec l'email : " + email);
        Optional<User> userOptional = userDao.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (!user.getActive()) {
                logger.warn("Échec de l'authentification : utilisateur désactivé avec l'email : " + email);
                throw new IllegalArgumentException("USER_NOT_ACTIVE");
            }
            if (checkPassword(password, user.getPassword())) {
                logger.info("Authentification réussie pour l'utilisateur avec l'email : " + email);
                return user;
            } else {
                logger.warn("Échec de l'authentification : mot de passe incorrect pour l'email : " + email);
            }
        } else {
            logger.warn("Échec de l'authentification : aucun utilisateur trouvé avec l'email : " + email);
        }

        return null;
    }

    /**
     * Recherche un utilisateur par son ID.
     *
     * @param id L'ID de l'utilisateur à rechercher.
     * @return Un {@link Optional} contenant l'utilisateur trouvé, ou vide s'il n'existe pas.
     */
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

    /**
     * Supprime un utilisateur par son ID.
     *
     * @param id L'ID de l'utilisateur à supprimer.
     */
    @Override
    public void deleteById(int id) {
        logger.info("Suppression de l'utilisateur avec l'ID : " + id);
        userDao.deleteById(id);
        logger.info("Utilisateur supprimé avec succès : ID " + id);
    }

    /**
     * Hache un mot de passe en utilisant BCrypt.
     *
     * @param password Le mot de passe en clair.
     * @return Le mot de passe haché.
     */
    private String hashPassword(String password) {
        logger.debug("Hachage du mot de passe");
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Vérifie si un mot de passe correspond à son hash.
     *
     * @param plainPassword  Le mot de passe en clair.
     * @param hashedPassword Le mot de passe haché.
     * @return {@code true} si le mot de passe correspond, {@code false} sinon.
     */
    public boolean checkPassword(String plainPassword, String hashedPassword) {
        logger.debug("Vérification du mot de passe haché");
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    /**
     * Filtre les utilisateurs par une requête de recherche.
     *
     * @param users       La liste des utilisateurs à filtrer.
     * @param searchQuery La requête de recherche.
     * @return Une liste d'utilisateurs correspondant à la requête de recherche.
     */
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
}
