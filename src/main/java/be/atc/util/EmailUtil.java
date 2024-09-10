package be.atc.util;

import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * La classe {@code EmailUtil} fournit des méthodes utilitaires pour envoyer des e-mails.
 * Elle permet d'envoyer des e-mails généraux ainsi que des e-mails de bienvenue
 * aux utilisateurs après une inscription réussie.
 *
 * <p>
 * Cette classe utilise la bibliothèque JavaMail pour gérer la création et l'envoi
 * des messages e-mails. Elle nécessite une configuration correcte des propriétés
 * SMTP pour fonctionner.
 * </p>
 *
 */
public class EmailUtil {

    private static final Logger logger = Logger.getLogger(EmailUtil.class);

    /**
     * Envoie un e-mail à l'utilisateur spécifié avec le sujet et le message donnés.
     *
     * @param toAddress L'adresse e-mail du destinataire.
     * @param subject   Le sujet de l'e-mail.
     * @param message   Le contenu de l'e-mail.
     * @throws MessagingException Si une erreur survient lors de l'envoi de l'e-mail.
     */
    public static void sendEmail(String toAddress, String subject, String message) throws MessagingException {
        logger.info("Préparation de l'envoi de l'e-mail à : " + toAddress);

        // Paramètres SMTP. Utilisation du service Mailtrap spécialisé dans le test d'envoi d'email.
        String host = "sandbox.smtp.mailtrap.io"; // Remplacez par votre serveur SMTP
        String port = "587"; // Le port SMTP, 587 pour TLS
        final String username = ""; // Votre adresse e-mail
        final String password = ""; // Votre mot de passe

        // Définir les propriétés de connexion
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        logger.debug("Propriétés SMTP définies.");

        // Créer une session avec authentification
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };

        Session session = Session.getInstance(properties, auth);

        // Créer un e-mail
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(username));
        InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new java.util.Date());
        msg.setText(message);

        logger.info("E-mail prêt à être envoyé à : " + toAddress);

        // Envoyer l'e-mail
        Transport.send(msg);

        logger.info("E-mail envoyé avec succès à : " + toAddress);
    }

    /**
     * Envoie un e-mail de bienvenue à l'utilisateur après une inscription réussie.
     *
     * @param toAddress L'adresse e-mail du destinataire.
     * @throws MessagingException Si une erreur survient lors de l'envoi de l'e-mail.
     */
    public static void sendWelcomeEmail(String toAddress) throws MessagingException {
        String subject = "Bienvenue sur notre site !";
        String message = "Cher utilisateur,\n\nMerci de vous être inscrit sur notre site. Nous sommes heureux de vous accueillir parmi nous !\n\nCordialement,\nL'équipe PGCS";

        logger.info("Envoi d'un e-mail de bienvenue à l'utilisateur à l'adresse : " + toAddress);
        sendEmail(toAddress, subject, message);
    }
}
