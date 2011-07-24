package com.ctb.processor;

import java.util.Date;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.ctb.bean.testAdmin.User;
import com.ctb.db.EmailProcessorDao;
import com.ctb.utils.CTBConstants;
import com.ctb.utils.Configuration;

/**
 * @author TCS
 * This class used to perform email notification.
 *
 */
public class EmailProcessor {
	
	
	private static EmailProcessor processor = null;
	
	
	private EmailProcessor() {
		
	}
	
	static EmailProcessor getInstance(){
		if(processor == null){
			processor = new EmailProcessor();
		}
		return processor;
	}
	
	

	/**
	 * @param userName user name 
	 * @param jobid submitted job id
	 * @param message job status
	 * This method is used send email notification to the user about the submitted job status.
	 */
	public void processEmail(String userName, int jobid, String message) {

		String to = null;
		 User user = null;
		 String content = Configuration.getUserEmailBody();
		 String subject = Configuration.getUserEmailSubject();
		 String replyTo = Configuration.getUserEmailReplyTo();

		try {

			EmailProcessorDao emailProcessorDao = new EmailProcessorDao();
			user = emailProcessorDao.getUserDetails(userName);
			 to = user.getEmail();


			// System.out.println("to:"+to+", userId:"+userId);

			//System.out.println("content0:" + content);
			content = content.replaceAll(
					CTBConstants.EMAIL_PLACEHOLDER_JOB_ID, String.valueOf(jobid))
					.replaceAll(
							CTBConstants.EMAIL_CONTENT_PLACEHOLDER_JOB_STATUS,
							message);
			subject = subject.replaceAll(CTBConstants.EMAIL_PLACEHOLDER_JOB_ID, String.valueOf(jobid) );
			InitialContext ic = new InitialContext();
			// the properties were configured in WebLogic through the console
			Session session = (Session) ic.lookup("UserManagementMail");

			/*
			 * Properties props = new Properties();
			 * props.put("mail.transport.protocol", "smtp");
			 * props.put("mail.smtp.host", "mailhost"); props.put("mail.from",
			 * emailAddress); Session session2 = session.getInstance(props);
			 */

			Message msg = new MimeMessage(session);
			
			  if (replyTo == null ||  replyTo.trim().length() < 1) { 
				  replyTo = CTBConstants.EMAIL_FROM; 
				  
			  }
			 

			msg.setFrom(new InternetAddress(replyTo));

			// emailTo could be a comma separated list of addresses
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(
					to, false));
			msg.setSubject(subject);
			msg.setText(content);
			msg.setSentDate(new Date());

			// send the message
			Transport.send(msg);

		} catch (NamingException se) {
			System.err.println("Job confirmation mail failed for job " + jobid
					+ ". " + se.getMessage());
			se.printStackTrace();
		} catch (AddressException se) {
			System.err.println("Job confirmation mail failed for job " + jobid
					+ ". " + se.getMessage());
			se.printStackTrace();
		} catch (MessagingException se) {
			System.err.println("Job confirmation mail failed for job " + jobid
					+ ". " + se.getMessage());
			se.printStackTrace();
		} catch (Exception se) {
			System.err.println("Job confirmation mail failed for job " + jobid
					+ ". " + se.getMessage());
			se.printStackTrace();
			// throw be;
		}

	}

	/**
	 * @param jobid
	 * @param fileNameList file names to be copied manually
	 * T his method is used send email to devloper to copy the file manually.
	 */
	public void processEmail(Integer jobid, List<String> fileNameList) {
		String to = Configuration.getDevloperEmailId();
		String content = Configuration.getDevloperEmailBody();
		String subject = Configuration.getDevloperEmailSubject();
		String replyTo = Configuration.getDevloperEmailReplyTo();

		try {
			to = Configuration.getDevloperEmailId();

			content = content.replaceAll(CTBConstants.EMAIL_PLACEHOLDER_JOB_ID, String.valueOf(jobid))
					.replace(CTBConstants.EMAIL_PLACEHOLDER_DATAFILE, fileNameList.get(0)).replace(
							CTBConstants.EMAIL_CONTENT_PLACEHOLDER_ORDERFILE, fileNameList.get(1));

			InitialContext ic = new InitialContext();

			// the properties were configured in WebLogic through the console
			Session session = (Session) ic.lookup("UserManagementMail");
			Message msg = new MimeMessage(session);

			msg.setFrom(new InternetAddress(replyTo));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(
					to, false));
			msg.setSubject(subject);
			msg.setText(content);
			msg.setSentDate(new Date());

			// send the message
			Transport.send(msg);

		} catch (NamingException se) {
			System.err
					.println("FTP failure notification mail is failed for job "
							+ jobid + ". " + se.getMessage());
			se.printStackTrace();
		} catch (AddressException se) {
			System.err
					.println("FTP failure notification mail is failed for job "
							+ jobid + ". " + se.getMessage());
			se.printStackTrace();
		} catch (MessagingException se) {
			System.err
					.println("FTP failure notification mail is failed for job "
							+ jobid + ". " + se.getMessage());
			se.printStackTrace();
		} catch (Exception se) {
			System.err
					.println("FTP failure notification mail is failed for job "
							+ jobid + ". " + se.getMessage());
			se.printStackTrace();
		}

	}
	
	
	
}
