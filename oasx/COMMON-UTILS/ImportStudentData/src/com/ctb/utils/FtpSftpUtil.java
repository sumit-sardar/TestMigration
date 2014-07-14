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
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * This class provides FTP/SFTP utility functions.
 * 
 * @author TCS
 * 
 */
public class FtpSftpUtil {

	static Logger logger = Logger.getLogger(FtpSftpUtil.class.getName());

	/**
	 * @param args
	 */

	public static Session getSFTPSession() throws Exception {

		try {
			JSch jsch = new JSch();
			Session session = null;
			/*
			 * Key authentication
			 */
			jsch.addIdentity(Configuration.getClientPrivateKeyPath(),
					"engrade-auth");
			session = jsch.getSession(Configuration.getFtpUser(),Configuration.getFtpHost(),  Integer.parseInt(Configuration.getFtpPort()));
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			return session;
		} catch (JSchException e) {
			e.printStackTrace();
			throw new Exception();
		} catch (Exception e) {
			throw new Exception();
		}
	}


	public static void closeSFTPClient(Session session) {
		try {
			session.disconnect();
		} catch (Exception e) {

		}
	}

	/* Consolidated :: */
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
			// sftpChannel.cd(destinationPath);
			sftpChannel.put(sourceFile, destination);

		} catch (SftpException e) {
			System.err.println("Exception : " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (sftpChannel != null) {
				sftpChannel.exit();
			}
			if (session != null) {
				session.disconnect();
			}
		}

	}

	public static void downloadFiles(Session session, String sourceDir,
			String targetDir) throws Exception {
		System.out.println("Download Start Time: "
				+ new Date(System.currentTimeMillis()));
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
					System.out
							.println("More than 1 files are present. System will exit.");
					throw new Exception();
				}
				// System.out.println("Start Time: "+new
				// Date(System.currentTimeMillis()));
				for (Iterator iterator = fileList.iterator(); iterator
						.hasNext();) {
					ChannelSftp.LsEntry entry = (ChannelSftp.LsEntry) iterator
							.next();
					// System.out.println("File: "+entry.getFilename());
					try {
						inStream = sftpChannel.get(entry.getFilename());
						File filename = new File(targetDir);
						if (!filename.exists())
							filename.mkdir();
						outStream = new FileOutputStream(new File(targetDir
								+ File.separator + entry.getFilename()));

						if (inStream == null) {
							System.out.println("Could not retrieve file...."
									+ entry.getFilename());
							continue;
						}

						int read = 0;
						byte[] bytes = new byte[1024];
						while ((read = inStream.read(bytes)) != -1) {
							outStream.write(bytes, 0, read);
						}
						System.out.println("File ->" + entry.getFilename()
								+ " downloaded successfully...");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.println("File ->" + entry.getFilename()
								+ " download failed...");
						e.printStackTrace();
						throw e;
					} finally {
						if (inStream != null) {
							try {
								inStream.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						if (outStream != null) {
							try {
								// outputStream.flush();
								outStream.close();
							} catch (IOException e) {
								e.printStackTrace();
							}

						}
					}
				}
			} else {
				System.out.println("******No Files Present:**** "
						+ new Date(System.currentTimeMillis()));
			}
			System.out.println("Download End Time: "
					+ new Date(System.currentTimeMillis()));
			logger.info("Download End Time: "
					+ new Date(System.currentTimeMillis()));
		} catch (Exception e) {
			e.printStackTrace();
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
			System.out
					.println("File -> \"" + fileName
							+ "\": archived successfully to " + targetDir
							+ archiveDate);
		} catch (Exception e) {
			e.printStackTrace();
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

	public static void accessSftpFolder(Session session, String sourceDir) {
		ChannelSftp sftpChannel = null;
		try {
			Channel channel = session.openChannel("sftp");
			channel.connect();
			sftpChannel = (ChannelSftp) channel;
			sftpChannel.cd(sourceDir);
			Vector fileList = sftpChannel.ls("*.xml");
			for (Iterator iterator = fileList.iterator(); iterator.hasNext();) {
				ChannelSftp.LsEntry entry = (ChannelSftp.LsEntry) iterator
						.next();
				// sftpChannel.get(entry.getFilename(), destinationPath +
				// entry.getFileName());
				System.out.println("File: " + entry.getFilename());
				InputStream is = sftpChannel.get(entry.getFilename());
				System.out.println("Start Time: "
						+ new Date(System.currentTimeMillis()));
				// parseXMLFile(is);
				System.out.println("End Time: "
						+ new Date(System.currentTimeMillis()));
			}

		} catch (Exception e) {
			e.printStackTrace();

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

}
