package be.atc.util;

import java.util.regex.Pattern;

/**
 * Classe utilitaire pour la validation des données utilisateur.
 * <p>
 * Cette classe contient des méthodes statiques permettant de valider
 * différents types d'informations, telles que les adresses e-mail,
 * les mots de passe, les numéros de téléphone, les prénoms, les noms
 * de famille et les genres.
 * </p>
 */
public class ValidationUtil {

    /**
     * Expression régulière pour valider les adresses e-mail.
     * <p>
     * Cette expression vérifie que l'e-mail est conforme au format standard
     * avec des caractères alphanumériques, des points, des tirets ou des
     * underscores avant le symbole '@', suivi par un nom de domaine valide.
     * </p>
     */
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    /**
     * Expression régulière pour valider les mots de passe.
     * <p>
     * Cette expression exige que le mot de passe contienne au moins 8 caractères,
     * une lettre majuscule et un chiffre.
     * </p>
     */
    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*\\d).{8,}$";

    /**
     * Expression régulière pour valider les numéros de téléphone.
     * <p>
     * Cette expression permet des numéros de téléphone composés de chiffres,
     * de tirets et de signes plus, avec une longueur de 9 à 20 caractères.
     * </p>
     */
    private static final String PHONE_REGEX = "^[0-9\\-\\+]{9,20}$";

    /**
     * Expression régulière pour valider les prénoms et noms de famille.
     * <p>
     * Cette expression autorise les lettres, les espaces, les apostrophes
     * et les tirets, tout en permettant plusieurs mots pour les noms composés.
     * </p>
     */
    private static final String NAME_REGEX = "^[A-Za-zÀ-ÖØ-öø-ÿ\\'\\-]+(\\s[A-Za-zÀ-ÖØ-öø-ÿ\\'\\-]+)*$";

    /**
     * Valide une adresse e-mail.
     *
     * @param email L'adresse e-mail à valider.
     * @return {@code true} si l'e-mail est valide, {@code false} sinon.
     */
    public static boolean isValidEmail(String email) {
        return Pattern.matches(EMAIL_REGEX, email);
    }

    /**
     * Valide un mot de passe.
     *
     * @param password Le mot de passe à valider.
     * @return {@code true} si le mot de passe est valide, {@code false} sinon.
     */
    public static boolean isValidPassword(String password) {
        return Pattern.matches(PASSWORD_REGEX, password);
    }

    /**
     * Valide un numéro de téléphone.
     *
     * @param phone Le numéro de téléphone à valider.
     * @return {@code true} si le numéro de téléphone est valide, {@code false} sinon.
     */
    public static boolean isValidPhone(String phone) {
        return Pattern.matches(PHONE_REGEX, phone);
    }

    /**
     * Valide un prénom.
     *
     * @param firstName Le prénom à valider.
     * @return {@code true} si le prénom est valide, {@code false} sinon.
     */
    public static boolean isValidFirstName(String firstName) {
        return Pattern.matches(NAME_REGEX, firstName);
    }

    /**
     * Valide un nom de famille.
     *
     * @param lastName Le nom de famille à valider.
     * @return {@code true} si le nom de famille est valide, {@code false} sinon.
     */
    public static boolean isValidLastName(String lastName) {
        return Pattern.matches(NAME_REGEX, lastName);
    }

    /**
     * Valide un genre.
     * <p>
     * Cette méthode vérifie si la chaîne de caractères correspond à un des genres
     * valides : "Male", "Female", ou "Other".
     * </p>
     *
     * @param gender Le genre à valider.
     * @return {@code true} si le genre est valide, {@code false} sinon.
     */
    public static boolean isValidGender(String gender) {
        return "Male".equals(gender) || "Female".equals(gender) || "Other".equals(gender);
    }
}
