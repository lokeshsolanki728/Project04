package com.rays.pro4.Util;

/**
 * EmailMessage is a data holder class that encapsulates all the information
 * required to send an email. It contains properties for the recipient, sender,
 * subject, message content, and message type (HTML or plain text).
 *
 * @author Lokesh SOlanki
 */
public class EmailMessage implements java.io.Serializable {

    /**
     * serialVersionUID is added
     */
    private static final long serialVersionUID = 1L;

    /**
     * Email address of the recipient.
     */
    private String to = null;

    /**
     * Email address of the sender.
     */
    private String from = null;

    /**
     * Comma-separated list of CC email addresses.
     */
    private String cc = null;

    /**
     * Comma-separated list of BCC email addresses.
     */
    private String bcc = null;

    /**
     * Subject of the email.
     */
    private String subject = null;

    /**
     * Body of the email message.
     */
    private String message = null;

    /**
     * Type of the email message (HTML or plain text).
     */
    private int messageType = TEXT_MSG;

    /**
     * Constant representing an HTML message type.
     */
    public static final int HTML_MSG = 1;

    /**
     * Constant representing a plain text message type.
     */
    public static final int TEXT_MSG = 2;

    /**
     * Gets the email address of the recipient.
     *
     * @return The email address of the recipient.
     */
    public String getTo() {
        return to;
    }

    /**
     * Sets the email address of the recipient.
     *
     * @param to The email address of the recipient.
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Gets the email address of the sender.
     *
     * @return The email address of the sender.
     */
    public String getFrom() {
        return from;
    }

    /**
     * Sets the email address of the sender.
     *
     * @param from The email address of the sender.
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * Gets the comma-separated list of CC email addresses.
     *
     * @return The comma-separated list of CC email addresses.
     */
    public String getCc() {
        return cc;
    }

    /**
     * Sets the comma-separated list of CC email addresses.
     *
     * @param cc The comma-separated list of CC email addresses.
     */
    public void setCc(String cc) {
        this.cc = cc;
    }

    /**
     * Gets the comma-separated list of BCC email addresses.
     *
     * @return The comma-separated list of BCC email addresses.
     */
    public String getBcc() {
        return bcc;
    }

    /**
     * Sets the comma-separated list of BCC email addresses.
     *
     * @param bcc The comma-separated list of BCC email addresses.
     */
    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    /**
     * Gets the subject of the email.
     *
     * @return The subject of the email.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the subject of the email.
     *
     * @param subject The subject of the email.
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Gets the body of the email message.
     *
     * @return The body of the email message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the body of the email message.
     *
     * @param message The body of the email message.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the type of the email message.
     *
     * @return The type of the email message.
     */
    public int getMessageType() {
        return messageType;
    }

    /**
     * Sets the type of the email message.
     *
     * @param messageType The type of the email message.
     */
    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }
}
