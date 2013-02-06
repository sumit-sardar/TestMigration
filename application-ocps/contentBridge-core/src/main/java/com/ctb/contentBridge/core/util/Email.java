package com.ctb.contentBridge.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;



/**
 * zip and send files to one or a list of addressees
 */
public class Email {

    private String smtpHost;

    public Email(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public static void sendMessage(
        String smtpHostName,
        String fromAddress,
        String toInfo,
        String subject,
        String msgText)
        throws Exception {
        sendMessage(
            smtpHostName,
            fromAddress,
            toInfo,
            subject,
            msgText,
            new ArrayList(),
            null);
    }

    public void sendMessage(
        String fromAddress,
        String toInfo,
        String subject,
        String msgText,
        File file)
        throws Exception {
        List files = new ArrayList();
        files.add(file);
        sendMessage(
            fromAddress,
            toInfo,
            subject,
            msgText,
            files,
            file.getName());
    }
    
    public void sendMessage(
        String fromAddress,
        String toInfo,
        String subject,
        String msgText,
        List files,
        String zipFileName)
        throws Exception {
        sendMessage(
            smtpHost,
            fromAddress,
            toInfo,
            subject,
            msgText,
            files,
            zipFileName);
    }

    public static void sendMessage(
        String smtpHostName,
        String fromAddress,
        String toInfo,
        String subject,
        String msgText,
        List files,
        String zipFileName)
        throws Exception {
        // Start a session
        java.util.Properties properties = System.getProperties();
        Session session = Session.getInstance(properties, null);

        // Construct a message
        String[] toAddr = getEmailAddr(toInfo);
        InternetAddress[] addr = new InternetAddress[toAddr.length];

        for (int i = 0; i < addr.length; i++) {
            addr[i] = new InternetAddress(toAddr[i]);
        }
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromAddress));
        message.addRecipients(Message.RecipientType.TO, addr);
        message.setSubject(subject);

        // Create the first part
        Multipart mailBody = new MimeMultipart();
        MimeBodyPart mainBody = new MimeBodyPart();
        mainBody.setText(msgText);
        mailBody.addBodyPart(mainBody);

        // Connect to the transport
        Transport transport = session.getTransport("smtp");
        transport.connect(smtpHostName, "", "");

        // create the attachment
        if (files.size() > 0) {
            FileDataSource fds1 = new FileDataSource(IOUtils.createZip(files));
            MimeBodyPart mimeAttach1 = new MimeBodyPart();

            mimeAttach1.setDataHandler(new DataHandler(fds1));
            mimeAttach1.setFileName(zipFileName);
            mailBody.addBodyPart(mimeAttach1);
        }

        message.setContent(mailBody);

        // Send the message and close the connection
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    private static String[] getEmailAddr(String info) throws Exception {
        Vector emailAddr = new Vector();

        if (info.indexOf("@") > -1) {
            emailAddr.add(info);
        } // the argument is a file
        else {
            BufferedReader in = new BufferedReader(new FileReader(info));
            String line = null;

            while ((line = in.readLine()) != null) {
                if (line != null && line.trim().length() > 0) {
                    emailAddr.add(line);
                }
            }
        }
        String[] addrs = new String[emailAddr.size()];

        emailAddr.copyInto(addrs);
        return addrs;
    }

}
