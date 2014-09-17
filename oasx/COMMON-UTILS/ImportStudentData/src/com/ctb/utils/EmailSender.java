package com.ctb.utils;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

/**
 * This Class is used for sending e-mail and/or attachments via a SMTP server
 * 
 * @author TCS
 */

public class EmailSender {
	static Logger logger = Logger.getLogger(EmailSender.class.getName());

	/**
	 * This Method is for sending e-mail and/or attachments via a SMTP server
	 * 
	 * @param asSmtpServer
	 *            IP address of the SMTP server
	 * @param asSender
	 *            A comma-separated list of e-mail addresses as per RFC822
	 * @param asRecipient
	 *            A comma-separated list of e-mail addresses as per RFC822
	 * @param asCcRecipient
	 *            A comma-separated list of e-mail addresses as per RFC822
	 * @param asBccRecipient
	 *            A comma-separated list of e-mail addresses as per RFC822
	 * @param asSubject
	 *            Subject of the mail
	 * @param asBody
	 *            Message Body of the mail
	 * @param arAttachments
	 *            An ArrayList containing the files to be attached with the mail
	 * @return String <B>Success</B> if sent successfully, else the error
	 *         message
	 */
	@SuppressWarnings("rawtypes")
	public static String sendMail(String asSmtpServer, String asSender,
			String asRecipient, String asCcRecipient, String asBccRecipient,
			String asSubject, String asBody, List arAttachments) {

		// Initialize the return value
		String msErrorStatus = "Success";
		// Create some properties and get the default Session.
		try {
			Properties props = System.getProperties();
			props.put("mail.smtp.host", asSmtpServer);
			Session mvSession = Session.getDefaultInstance(props, null);
			// Create a message;
			MimeMessage mvMimeMessage = new MimeMessage(mvSession);
			// Extracts the senders and adds them to the message.
			// Sender is a comma-separated list of e-mail addresses as per
			// RFC822.
			if (asSender != null && asSender.length() > 0) {
				/*InternetAddress[] marrvInternetAddresses = InternetAddress
						.parse(asSender);
				mvMimeMessage.addFrom(marrvInternetAddresses);*/
				mvMimeMessage.setFrom(new InternetAddress(asSender, Configuration.getEmailAlias()));
			}
			// Extract the recipients and assign them to the message.
			// Recipient is a comma-separated list of e-mail addresses as per
			// RFC822.
			if (asRecipient != null && asRecipient.length() > 0) {
				InternetAddress[] marrvInternetAddresses = InternetAddress
						.parse(asRecipient);
				mvMimeMessage.addRecipients(Message.RecipientType.TO,
						marrvInternetAddresses);
			}
			// Extract the Cc-recipients and assign them to the message.
			// CcRecipient is a comma-separated list of e-mail addresses as per
			// RFC822.
			if (asCcRecipient != null && asCcRecipient.length() > 0) {
				InternetAddress[] marrvInternetAddresses = InternetAddress
						.parse(asCcRecipient);
				mvMimeMessage.addRecipients(Message.RecipientType.CC,
						marrvInternetAddresses);
			}
			// Extract the Bcc-recipients and assign them to the message.
			// BccRecipient is a comma-separated list of e-mail addresses as per
			// RFC822.
			if (asBccRecipient != null && asBccRecipient.length() > 0) {
				InternetAddress[] marrvInternetAddresses = InternetAddress
						.parse(asBccRecipient);
				mvMimeMessage.addRecipients(Message.RecipientType.BCC,
						marrvInternetAddresses);
			}
			// Set the subject field;
			mvMimeMessage.setSubject(asSubject);
			// Create the Multipart to be added the parts to.
			Multipart mvMp = new MimeMultipart();
			// Create and fill the first message part.
			if (asBody != null) {
				MimeBodyPart mvBp = new MimeBodyPart();
				mvBp.setText(asBody);
				// Attach the part to the multipart.
				mvMp.addBodyPart(mvBp);
			}
			// Attach the files to the message;
			if (arAttachments != null && arAttachments.size() > 0) {
				for (int i = 0; i < arAttachments.size(); i++) {
					if (arAttachments.get(i) == null
							|| arAttachments.get(i).toString().length() == 0) {
						continue;
					}
					// Create and fill other message parts.
					MimeBodyPart mvBp = new MimeBodyPart();
					FileDataSource mvFds = new FileDataSource(arAttachments
							.get(i).toString());
					mvBp.setDataHandler(new DataHandler(mvFds));
					mvBp.setFileName(mvFds.getName());
					mvMp.addBodyPart(mvBp);
				}
			}
			// Add the Multipart to the message.
			mvMimeMessage.setContent(mvMp);
			// Set the Date: header.
			mvMimeMessage.setSentDate(new Date());
			// Send the message;
			Transport.send(mvMimeMessage);
			logger.info("Mail has been successfully sent..");
		} catch (MessagingException exMsgException) {
			exMsgException.printStackTrace();
			logger.info("Mail sending failed..MessagingException Ocuured.");
		} catch (Exception exException) {
			exException.printStackTrace();
			logger.info("Mail sending failed..");
		}
		return msErrorStatus;
	}

}