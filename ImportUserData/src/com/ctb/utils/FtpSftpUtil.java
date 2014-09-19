package com.ctb.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.ctb.exception.CTBBusinessException;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * This class provides FTP/SFTP utility functions.
 * 
 * @author TCS
 */
@SuppressWarnings("rawtypes")
public class FtpSftpUtil {

	static Logger logger = Logger.getLogger(FtpSftpUtil.class.getName());

	/**
	 * Returns the Session connected through Public-Private Key mechanism of the
	 * required FTP Host mentioned in the Properties File
	 * 
	 * @return Session
	 * @throws Exception
	 */
	public static Session getSFTPSession() throws Exception {

		try {
			JSch jsch = new JSch();
			Session session = null;
			/*
			 * Key authentication
			 */
			jsch.addIdentity(Configuration.getClientPrivateKeyPath());
			session = jsch.getSession(Configuration.getFtpUser(),
					Configuration.getFtpHost(),
					Integer.parseInt(Configuration.getFtpPort()));
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			return session;
		} catch (Exception e) {
			if ("true".equalsIgnoreCase(Configuration.getEmailAlerts())) {
				EmailSender.sendMail("", Configuration.getEmailSender(),
						Configuration.getEmailRecipient(),
						Configuration.getEmailCC(),
						Configuration.getEmailBCC(),
						Configuration.getEmailSubjectFtpIssue(),
						Configuration.getEmailBodyFtpIsuue(), null);
			}
			logger.info("FTP Session Connection error..");
			throw new Exception();
		}
	}

	/**
	 * Disconnects the Session
	 * 
	 * @param session
	 */
	public static void closeSFTPClient(Session session) {
		try {
			session.disconnect();
		} catch (Exception e) {

		}
	}

	/**
	 * 
	 * @param destinationPath
	 *            - The Location where the files are to be placed
	 * @param sourceFile
	 *            - The Location from where the files are to be picked up
	 * @throws Exception
	 */
	public void sendfilesSFTP(String destinationPath, String sourceFile)
			throws Exception {

		Session session = null;
		ChannelSftp sftpChannel = null;

		try {
			session = getSFTPSession();
			Channel channel = session.openChannel("sftp");
			channel.connect();
			sftpChannel = (ChannelSftp) channel;

			String destination = destinationPath;
			sftpChannel.put(sourceFile, destination);

		} catch (SftpException e) {
			logger.info("Exception : " + e.getMessage());
			/**
			 * Send mail
			 */
			if ("true".equalsIgnoreCase(Configuration.getEmailAlerts())) {
				EmailSender.sendMail(
						"",
						Configuration.getEmailSender(),
						Configuration.getEmailRecipient(),
						Configuration.getEmailCC(),
						Configuration.getEmailBCC(),
						Configuration.getEmailSubjectErrorFileFTPIssue(),
						Configuration.getEmailBodyErrorFileFTPIssue().replace(
								"<#FileName#>", sourceFile), null);
			}
		} finally {
			if (sftpChannel != null) {
				sftpChannel.exit();
			}
			if (session != null) {
				session.disconnect();
			}
		}

	}

	/**
	 * Downloads file from sourceDir to targetDir using Session
	 * 
	 * @param session
	 * @param sourceDir
	 * @param targetDir
	 * @throws Exception
	 */
	public static void downloadFiles(Session session, String sourceDir,
			String targetDir) throws Exception {
		logger.info("Download Start Time: "
				+ new Date(System.currentTimeMillis()));
		ChannelSftp sftpChannel = null;
		InputStream inStream = null;
		FileOutputStream outStream = null;
		try {
			Channel channel = session.openChannel("sftp");
			channel.connect();
			sftpChannel = (ChannelSftp) channel;
			sftpChannel.cd(sourceDir);
			Vector fileList = sftpChannel.ls("*.csv");

			if (fileList != null && fileList.size() > 0) {
				if (fileList.size() > 1) {
					logger.info("More than 1 files are present. System will exit.");
					/**
					 * Send Mail
					 */
					if ("true".equalsIgnoreCase(Configuration.getEmailAlerts())) {
						EmailSender.sendMail("",
								Configuration.getEmailSender(),
								Configuration.getEmailRecipient(),
								Configuration.getEmailCC(),
								Configuration.getEmailBCC(),
								Configuration.getEmailSubjectMoreFilesIssue(),
								Configuration.getEmailBodyMoreFilesIssue(),
								null);
					}
					throw new CTBBusinessException("More than 1 files present");
				}
				for (Iterator iterator = fileList.iterator(); iterator
						.hasNext();) {
					ChannelSftp.LsEntry entry = (ChannelSftp.LsEntry) iterator
							.next();
					try {
						inStream = sftpChannel.get(entry.getFilename());
						File filename = new File(targetDir);
						if (!filename.exists())
							filename.mkdir();
						outStream = new FileOutputStream(new File(targetDir
								+ File.separator + entry.getFilename()));

						if (inStream == null) {
							logger.info("Could not retrieve file...."
									+ entry.getFilename());
							continue;
						}

						int read = 0;
						byte[] bytes = new byte[1024];
						while ((read = inStream.read(bytes)) != -1) {
							outStream.write(bytes, 0, read);
						}
						logger.info("File ->" + entry.getFilename()
								+ " downloaded successfully...");
					} catch (IOException e) {
						logger.info("File ->" + entry.getFilename()
								+ " download failed...");
						e.getMessage();
						throw e;
					} finally {
						if (inStream != null) {
							try {
								inStream.close();
							} catch (IOException e) {
								e.getMessage();
							}
						}
						if (outStream != null) {
							try {
								outStream.close();
							} catch (IOException e) {
								e.getMessage();
							}

						}
					}
				}
			} else {
				logger.info("******No Files Present:**** "
						+ new Date(System.currentTimeMillis()));
				/**
				 * Send Mail
				 */
				if ("true".equalsIgnoreCase(Configuration.getEmailAlerts())) {
					EmailSender.sendMail("", Configuration.getEmailSender(),
							Configuration.getEmailRecipient(),
							Configuration.getEmailCC(),
							Configuration.getEmailBCC(),
							Configuration.getEmailSubjectNoFileIssue(),
							Configuration.getEmailBodyNoFileIssue(), null);
				}
			}
			logger.info("Download End Time: "
					+ new Date(System.currentTimeMillis()));
		} catch (CTBBusinessException e) {
			throw e;
		} catch (Exception e) {
			logger.info("**Downloading of Files Failed..**");
			/**
			 * Send Mail
			 */
			if ("true".equalsIgnoreCase(Configuration.getEmailAlerts())) {
				EmailSender.sendMail("", Configuration.getEmailSender(),
						Configuration.getEmailRecipient(),
						Configuration.getEmailCC(),
						Configuration.getEmailBCC(),
						Configuration.getEmailSubjectDownloadFileIssue(),
						Configuration.getEmailBodyDownloadFileIssue(), null);
			}
			throw e;

		} finally {
			if (sftpChannel != null) {
				try {
					sftpChannel.exit();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			if (session != null) {
				session.disconnect();
			}
		}
	}

	/**
	 * Archives file from sourceDir to targetDir using session
	 * 
	 * @param session
	 * @param sourceDir
	 * @param targetDir
	 * @param fileName
	 * @throws Exception
	 */
	public static void archiveProcessedFiles(Session session, String sourceDir,
			String targetDir, String fileName) throws Exception {
		ChannelSftp sftpChannel = null;
		try {
			Channel channel = session.openChannel("sftp");
			channel.connect();
			sftpChannel = (ChannelSftp) channel;
			sftpChannel.cd(targetDir);

			DateFormat df = new SimpleDateFormat("MM.dd.yyyy_HHmmss");
			Date today = Calendar.getInstance().getTime();
			String archiveDate = df.format(today);

			sftpChannel.mkdir(targetDir + archiveDate);
			sftpChannel.rename(sourceDir + fileName, targetDir + archiveDate
					+ "/" + fileName);
			logger.info("File -> \"" + fileName
					+ "\": archived successfully to " + targetDir + archiveDate);
		} catch (Exception e) {
			/**
			 * Send mail
			 */
			if ("true".equalsIgnoreCase(Configuration.getEmailAlerts())) {
				EmailSender.sendMail(
						"",
						Configuration.getEmailSender(),
						Configuration.getEmailRecipient(),
						Configuration.getEmailCC(),
						Configuration.getEmailBCC(),
						Configuration.getEmailSubjectArchiveFTPIssue(),
						Configuration.getEmailBodyArchiveFTPIssue().replace(
								"<#FileName#>", fileName), null);
			}
			e.getMessage();
			throw e;
		} finally {
			if (sftpChannel != null) {
				try {
					sftpChannel.exit();
				} catch (Exception e) {
					e.getMessage();
				}

			}
			if (session != null) {
				session.disconnect();
			}
		}
	}

}