package com.ctb.util;

import java.util.ArrayList;
import java.util.Date;
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

public class EmailSender
{
  public String sendNotificationMail(String customerEmailID, String CTBEmailID, String productName, String senderEmailID)
  {
    String body = getEmailBody(productName);
    String subject = "License Notification";
    try
    {
      sendMail("", senderEmailID, customerEmailID, "", "", subject, body, null, true);
      sendMail("", senderEmailID, CTBEmailID, "", "", subject, body, null, true);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }

  public String getEmailBody(String productName)
  {
    String msg = "";
    try {
      msg = "You have reached the threshold of your license for " + productName + " product. \nPlease contact product support to purchase more.";
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return msg;
  }

  public static String sendMail(String asSmtpServer, String asSender, String asRecipient, String asCcRecipient, String asBccRecipient, String asSubject, String asBody, ArrayList arAttachments, boolean isLaslink)
  {
    String msErrorStatus = "Success";
    String aliasName = "LAS Links - OAS Account Management";
    try
    {
      Properties props = System.getProperties();
      props.put("mail.smtp.host", asSmtpServer);
      Session mvSession = Session.getDefaultInstance(props, null);

      MimeMessage mvMimeMessage = new MimeMessage(mvSession);

      if ((asSender != null) && (asSender.length() > 0)) {
    	  if(isLaslink){
    		  InternetAddress[] marrvInternetAddresses = InternetAddress.parse(asSender);
    		  InternetAddress[] arrWithAliasName = new InternetAddress[marrvInternetAddresses.length];
    		  for (int i = 0; i < marrvInternetAddresses.length; i++) {
    			  arrWithAliasName[i] = new InternetAddress(marrvInternetAddresses[i].getAddress(), aliasName);
    		  }
    		  mvMimeMessage.addFrom(arrWithAliasName);
    	  }else{
				InternetAddress[] marrvInternetAddresses = InternetAddress.parse(asSender);
				mvMimeMessage.addFrom(marrvInternetAddresses);    		  
    	  }
      }

      if ((asRecipient != null) && (asRecipient.length() > 0)) {
        InternetAddress[] marrvInternetAddresses = InternetAddress.parse(asRecipient);
        mvMimeMessage.addRecipients(Message.RecipientType.TO, marrvInternetAddresses);
      }
      
      if ((asCcRecipient != null) && (asCcRecipient.length() > 0)) {
        InternetAddress[] marrvInternetAddresses = InternetAddress.parse(asCcRecipient);
        mvMimeMessage.addRecipients(Message.RecipientType.CC, marrvInternetAddresses);
      }

      if ((asBccRecipient != null) && (asBccRecipient.length() > 0)) {
        InternetAddress[] marrvInternetAddresses = InternetAddress.parse(asBccRecipient);
        mvMimeMessage.addRecipients(Message.RecipientType.BCC, marrvInternetAddresses);
      }

      mvMimeMessage.setSubject(asSubject);

      Multipart mvMp = new MimeMultipart();

      if (asBody != null) {
        MimeBodyPart mvBp = new MimeBodyPart();
        mvBp.setText(asBody);

        mvMp.addBodyPart(mvBp);
      }

      if ((arAttachments != null) && (arAttachments.size() > 0)) {
        for (int i = 0; i < arAttachments.size(); i++) {
          if ((arAttachments.get(i) == null) || (arAttachments.get(i).toString().length() == 0))
          {
            continue;
          }

          MimeBodyPart mvBp = new MimeBodyPart();
          FileDataSource mvFds = new FileDataSource(arAttachments.get(i).toString());

          mvBp.setDataHandler(new DataHandler(mvFds));
          mvBp.setFileName(mvFds.getName());
          mvMp.addBodyPart(mvBp);
        }
      }

      mvMimeMessage.setContent(mvMp);

      mvMimeMessage.setSentDate(new Date());

      Transport.send(mvMimeMessage);
    } catch (MessagingException exMsgException) {
      exMsgException.printStackTrace();
    } catch (Exception exException) {
      exException.printStackTrace();
    }
    return msErrorStatus;
  }

  public static void main(String[] args)
  {
    sendMail("192.168.14.6", "dssapp@ctbdbsvr.mcgrawhill.co.in", "dssapp@ctbdbsvr.mcgrawhill.co.in", null, null, "test", "test", null, true);
  }
}