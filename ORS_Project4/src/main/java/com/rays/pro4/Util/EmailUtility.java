
package com.rays.pro4.Util;

import java.util.MissingResourceException;

import org.apache.log4j.Logger;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.rays.pro4.Exception.ApplicationException;

/**
 * Email Utility provides Email Services.
 *
 * @author Lokesh SOlanki
 */
public class EmailUtility {

	private static Logger log = Logger.getLogger(EmailUtility.class);
    /**
     * Create Resource Bundle to read email properties file
     */
    static ResourceBundle rb = ResourceBundle.getBundle("com.rays.pro4.bundle.Email");

    /**
     * Email Server
     */
    private static final String SMTP_HOST_NAME = getProperty("smtp.server");

    /**
     * Email Server Port
     */
    private static final String SMTP_PORT = getProperty("smtp.port");

    /**
     * SSL Factory, A session is a connection to email server.
     */
    private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

    /**
     * Administrator's email id by which all messages are sent
     */
    private static final String emailFromAddress = getProperty("email.login");

    /**
     * Administrator email's password
     */
    private static final String emailPassword = getProperty("email.pwd");

    /**
     * Email server properties
     */
    private static Properties props = new Properties();

    /**
     * Static block to initialize static parameters
     */
    static {
        // SMTP server properties
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.socketFactory.port", SMTP_PORT);
        props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.put("mail.smtp.socketFactory.fallback", "false");
    }
    /**
     * Returns value of key
     *
     * @param key
     * @return
     */
    private static String getProperty(String key) {
        String val = "";
        try {
            val = rb.getString(key);
        } catch (MissingResourceException e) {
            val = "";
        }
        return val;
    }

    /**
     * Sends an Email
     *
     * @param emailMessageDTO : Email message
     * @throws ApplicationException if there is problem
     */
    public static void sendMail(EmailMessage emailMessageDTO) throws ApplicationException {

        try {
            //Create connection to Mail Server
            Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailFromAddress, emailPassword);
                }
            });
           //display debug messages at console
            session.setDebug(true);
            //Create message
            Message msg = new MimeMessage(session);
            InternetAddress addressFrom = new InternetAddress(emailFromAddress);
            msg.setFrom(addressFrom);
             //Set TO addresses
            String[] emailIds = new String[0];

            if (emailMessageDTO.getTo() != null) {
                emailIds = emailMessageDTO.getTo().split(",");
            }

             //Set CC addresses
            String[] emailIdsCc = new String[0];

            if (emailMessageDTO.getCc() != null) {
                emailIdsCc = emailMessageDTO.getCc().split(",");
            }
           //Set BCC addresses
            String[] emailIdsBcc = new String[0];

            if (emailMessageDTO.getBcc() != null) {
                emailIdsBcc = emailMessageDTO.getBcc().split(",");
            }
            //set to address
            InternetAddress[] addressTo = new InternetAddress[emailIds.length];

            for (int i = 0; i < emailIds.length; i++) {
                addressTo[i] = new InternetAddress(emailIds[i]);
            }
            //set cc address
            InternetAddress[] addressCc = new InternetAddress[emailIdsCc.length];

            for (int i = 0; i < emailIdsCc.length; i++) {
                addressCc[i] = new InternetAddress(emailIdsCc[i]);
            }
            //set bcc address
            InternetAddress[] addressBcc = new InternetAddress[emailIdsBcc.length];

            for (int i = 0; i < emailIdsBcc.length; i++) {
                addressBcc[i] = new InternetAddress(emailIdsBcc[i]);
            }
             //Setting to address
            if (addressTo.length > 0) {
                msg.setRecipients(Message.RecipientType.TO, addressTo);
            }
             //Setting cc address
            if (addressCc.length > 0) {
                msg.setRecipients(Message.RecipientType.CC, addressCc);
            }
            //Setting bcc address
            if (addressBcc.length > 0) {
                msg.setRecipients(Message.RecipientType.BCC, addressBcc);
            }

             //Setting the Subject
            msg.setSubject(emailMessageDTO.getSubject());

            // Set message type
            switch (emailMessageDTO.getMessageType()) {
                case EmailMessage.HTML_MSG:
                    msg.setContent(emailMessageDTO.getMessage(), "text/html");
                    break;
                case EmailMessage.TEXT_MSG:
                    msg.setContent(emailMessageDTO.getMessage(), "text/plain");
                    break;
            }
            // Send email
            Transport.send(msg);

        } catch (Exception e) {
            log.error("Error in EmailUtility.sendMail()",e);
            throw new ApplicationException("Exception in sending email " + e.getMessage());
        }
    }

}
