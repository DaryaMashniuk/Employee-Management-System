package by.mashnyuk.webapplication.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailUtil {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String FROM_EMAIL = System.getenv("EMAIL");
    private static final String PASSWORD = System.getenv("PASSWORD");
    private static final String HOST = "smtp.gmail.com";
    private static final int PORT = 587;

    public static void sendVerificationEmail(String toEmail, String verificationLink) {
        LOGGER.info("Sending verification email to " + toEmail);

        Properties properties = new Properties();
        properties.put("mail.smtp.host", HOST);
        properties.put("mail.smtp.port", PORT);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Email Verification");

            String htmlContent = "<html><body>"
                    + "<h2>Please verify your email</h2>"
                    + "<p>Click the link below to verify your email address:</p>"
                    + "<a href=\"" + verificationLink + "\">Verify Email</a>"
                    + "<p>Or copy this link to your browser:</p>"
                    + "<p>" + verificationLink + "</p>"
                    + "</body></html>";

            message.setContent(htmlContent, "text/html");
            Transport.send(message);
            LOGGER.info("Verification email sent to " + toEmail);
        } catch (MessagingException e) {
            LOGGER.error("Failed to send verification email", e);
            throw new RuntimeException("Failed to send verification email", e);
        }
    }
}