package com.ctb.processor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import com.ctb.exception.CTBBusinessException;
import com.ctb.utils.CompressUtil;
import com.ctb.utils.Configuration;
import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPTransferType;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;

/**
 * @author TCS This class provides interface to transfer file via FTP and SFTP
 */
public class FileTransporter {

	private static FileTransporter transporter;
	static final String TRANSPORT_TYPE_FTP = "FTP";
	static final String TRANSPORT_TYPE_SFTP = "SFTP";
	static final String TRANSPORT_TYPE_SCP = "SCP";

	private FileTransporter() {
		super();

	}

	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	/**
	 * @return FileTransporter
	 */
	public static synchronized FileTransporter getInstance() {

		if (transporter == null) {
			transporter = new FileTransporter();
		}
		return transporter;
	}

	/**
	 * @param transportType
	 * @param fileList
	 * @throws CTBBusinessException
	 * This is generic method used to transfer file. Based on key it uses FTP or SFTP protocol. 
	 */
	public void transferFile(String transportType, List<String> fileList, boolean isLASUSDcustomer)
			throws CTBBusinessException {
		
		if(isLASUSDcustomer){
			if (transportType != null
					&& transportType.equalsIgnoreCase(TRANSPORT_TYPE_FTP)) {
				doFtpForLAUSDcustomer(fileList);
			} else if (transportType != null
					&& (transportType.equalsIgnoreCase(TRANSPORT_TYPE_SFTP) || transportType
							.equalsIgnoreCase(TRANSPORT_TYPE_SCP))) {
				doSftpForLAUSDcustomer(fileList);
			} else {
				throw new CTBBusinessException("Protocol [" + transportType
						+ "] is not valid.");
			}
		}else{
			if (transportType != null
					&& transportType.equalsIgnoreCase(TRANSPORT_TYPE_FTP)) {
				doFtp(fileList);
			} else if (transportType != null
					&& (transportType.equalsIgnoreCase(TRANSPORT_TYPE_SFTP) || transportType
							.equalsIgnoreCase(TRANSPORT_TYPE_SCP))) {
				doSftp(fileList);
			} else {
				throw new CTBBusinessException("Protocol [" + transportType
						+ "] is not valid.");
			}
		}

	}

	private void doFtp(List<String> fileList) throws CTBBusinessException {

		String destinationPathDataFile = Configuration.getFtpDataFilepath();
		String destinationPathOrderFile = Configuration.getFtpOrderFilepath();
		String destination;
		Integer i =0;
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.setRemoteHost(Configuration.getFtphost());

			ftpClient.connect();

			ftpClient.login(Configuration.getFtpuser(), Configuration
					.getFtppassword());
			ftpClient.setType(FTPTransferType.BINARY);
			
			for (String sourceFile : fileList) {
				sourceFile = sourceFile.replaceAll("%20", " ");
				String compressedFile = sourceFile + ".gz";
				String filename = getfileName(compressedFile);
				CompressUtil.gzipFile(sourceFile, compressedFile);
				if( i > 0){
					destination = destinationPathOrderFile + File.separator
					+ filename;
				}
				else {
					destination = destinationPathDataFile + File.separator
					+ filename;
				}
				 
				destination = destination.replaceAll("%20", " ");
				ftpClient.put(compressedFile, destination);
				new File(compressedFile).delete();
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new CTBBusinessException(
					"Transfer failed:IOException occurred while transfering file.");
		} catch (FTPException e) {
			e.printStackTrace();
			throw new CTBBusinessException(
					"Transfer failed: FTPException occurred while transfering file.");
		}

	}

	private void doSftp(List<String> fileList) throws CTBBusinessException {

		JSch jsch = new JSch();
		Session session = null;
		ChannelSftp sftpChannel = null;
		String localFileName = null;
		String sourceCompressedFileWithPath = null;
		Properties properties = new Properties();
		properties.put("StrictHostKeyChecking", "no");
		//properties.put("compression.s2c", "zlib@openssh.com,zlib,none");
		//properties.put("compression.c2s", "zlib@openssh.com,zlib,none");
		//properties.put("compression_level", "9");
		 properties.put("compression.s2c", "none");
		 properties.put("compression.c2s", "none");

		final String destinationPathDataFile = Configuration.getFtpDataFilepath();
		final String destinationPathOrderFile = Configuration.getFtpOrderFilepath();
		String destinationPath = null;
		String ftpHost = Configuration.getFtphost();
		String ftpUser = Configuration.getFtpuser();
		String ftpPass = Configuration.getFtppassword();
		int ftpPort = Configuration.getFtpFilePort();
		Integer fileCounter = 0;
		System.out.println("Connecting to server:"+ftpHost);
		try {
			session = jsch.getSession(ftpUser, ftpHost, ftpPort);
			session.setConfig(properties);
			session.setPassword(ftpPass);

			session.connect();
			Channel channel = session.openChannel("sftp");
			channel.connect();
			sftpChannel = (ChannelSftp) channel;

			for (String sourceFileWithPath : fileList) {
				System.out.println("File transfer started for file "
						+ getfileName(sourceFileWithPath));
				sourceFileWithPath = sourceFileWithPath.replaceAll("%20", " ");
				sourceCompressedFileWithPath = sourceFileWithPath
				+ ".gz";
				localFileName = getfileName(sourceCompressedFileWithPath);
				CompressUtil.gzipFile(sourceFileWithPath,
						sourceCompressedFileWithPath);
				if ( fileCounter > 0){
					destinationPath = destinationPathOrderFile;
				} 
				else{
					destinationPath = destinationPathDataFile;
				}
				
				String destinationFileWithPath = destinationPath
				+ File.separator + localFileName;
				destinationFileWithPath = destinationFileWithPath.replaceAll(
						"%20", " ");
				sftpChannel.cd(destinationPath);
				transferFile(sftpChannel,sourceCompressedFileWithPath, localFileName, sourceFileWithPath, destinationPath);

				fileCounter++;
			}

		} catch (JSchException e) {
			e.printStackTrace();
			throw new CTBBusinessException(
					"Transfer failed: JSchException occurred while transfering file.");

		} catch (IOException e) {
			e.printStackTrace();
			throw new CTBBusinessException(
					"Transfer failed:IOException occurred while transfering file.");

		} catch (SftpException e) {
			e.printStackTrace();
			throw new CTBBusinessException(
					"Transfer failed: SftpException occurred while transfering file.");
		} finally {
			if (sftpChannel != null) {
				sftpChannel.exit();
			}
			if (session != null) {
				session.disconnect();
			}

		}

	}

	private void transferFile(final ChannelSftp sftpChannel,final String sourceCompressedFileWithPath,final String localFileName,
			final String sourceFileWithPath,final String destinationPath)
	throws FileNotFoundException, SftpException{

		final FileInputStream inputStream = new FileInputStream(new File(sourceCompressedFileWithPath));
		sftpChannel.put(inputStream , localFileName,
				new SftpProgressMonitor(){

			@Override
			public boolean count(long arg0) {
				System.out.println("Transferred so far " + arg0 + " bytes");

				return true;
			}

			@Override
			public void end() {
				System.out.println("SFTP transfer completed for file " + sourceFileWithPath);
				try {
					//close inputstream
					inputStream.close();

					new File(sourceCompressedFileWithPath)
						.renameTo(new File(sourceCompressedFileWithPath+".transferred"));
					if (new File(sourceFileWithPath).exists())
						new File(sourceFileWithPath).delete();
				} catch (Exception e) {
					e.printStackTrace();

				}


			}

			@Override
			public void init(int arg0, String arg1, String arg2,
					long arg3) {
				System.out.println("FTP stranfer started for SRC " + arg1 + " to destination " + arg2);

			}

		});
	}
	private String getfileName(String srcfilePath) {
		String file = srcfilePath;
		int pos = srcfilePath.lastIndexOf(File.separator);
		file = file.substring(pos + 1, file.length());
		return file;
	}

	/*
	 * public static void main(String[] args) throws IOException, SftpException {
	 * 
	 * List<String> fileList = new ArrayList<String>(); ; File f = new
	 * File("C:/emetric/1"); File[] fileArray = f.listFiles(); for (File ff :
	 * fileArray) { fileList.add(ff.getPath()); }
	 * 
	 * FileTransporter transporter = new FileTransporter();
	 * 
	 * transporter.doSftp(fileList);
	 *  }
	 */

	private void doFtpForLAUSDcustomer(List<String> fileList) throws CTBBusinessException {

		String destinationPathDataFile = Configuration.getFtpDataFilepathLAUSD();
		String destinationPathOrderFile = Configuration.getFtpOrderFilepathLAUSD();
		String destination;
		Integer i =0;
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.setRemoteHost(Configuration.getFtphostLAUSD());

			ftpClient.connect();

			ftpClient.login(Configuration.getFtpuserLAUSD(), Configuration
					.getFtppasswordLAUSD());
			ftpClient.setType(FTPTransferType.BINARY);
			
			for (String sourceFile : fileList) {
				sourceFile = sourceFile.replaceAll("%20", " ");
				String compressedFile = sourceFile + ".gz";
				String filename = getfileName(compressedFile);
				CompressUtil.gzipFile(sourceFile, compressedFile);
				if( i > 0){
					destination = destinationPathOrderFile + File.separator
					+ filename;
				}
				else {
					destination = destinationPathDataFile + File.separator
					+ filename;
				}
				 
				destination = destination.replaceAll("%20", " ");
				ftpClient.put(compressedFile, destination);
				new File(compressedFile).delete();
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new CTBBusinessException(
					"Transfer failed:IOException occurred while transfering file.");
		} catch (FTPException e) {
			e.printStackTrace();
			throw new CTBBusinessException(
					"Transfer failed: FTPException occurred while transfering file.");
		}

	}
	
	private void doSftpForLAUSDcustomer(List<String> fileList) throws CTBBusinessException {

		JSch jsch = new JSch();
		Session session = null;
		ChannelSftp sftpChannel = null;
		String localFileName = null;
		String sourceCompressedFileWithPath = null;
		Properties properties = new Properties();
		properties.put("StrictHostKeyChecking", "no");
		properties.put("compression.s2c", "none");
		properties.put("compression.c2s", "none");

		final String destinationPathDataFile = Configuration.getFtpDataFilepathLAUSD();
		final String destinationPathOrderFile = Configuration.getFtpOrderFilepathLAUSD();
		final String movedDirectoryOfDataFile = Configuration.getFtpDataFileMovedDirectory();
		final String movedDirectoryOfOrderFile = Configuration.getFtpOrderFileMovedDirectory();
		String destinationPath = null;
		String ftpMovedDirectory = null;
		String ftpHost = Configuration.getFtphostLAUSD();
		String ftpUser = Configuration.getFtpuserLAUSD();
		// String ftpPass = Configuration.getFtppasswordLAUSD();
		Integer ftpPort = Configuration.getFtpFilePortLAUSD();
		String clientPrivateKeyPath = Configuration.getClientPrivateKeyPath();
		Integer i =0;
		System.out.println("Connecting to server:"+ftpHost);
		try {
			
			/*
			 * Key authentication
			 */
			jsch.addIdentity(clientPrivateKeyPath);
			session = jsch.getSession(ftpUser,
					ftpHost,
					ftpPort.intValue());
			session.setConfig(properties);
			session.connect();
			Channel channel = session.openChannel("sftp");
			channel.connect();
			sftpChannel = (ChannelSftp) channel;

			for (String sourceFileWithPath : fileList) {
				System.out.println("File transfer started for file "
						+ getfileName(sourceFileWithPath));
				sourceFileWithPath = sourceFileWithPath.replaceAll("%20", " ");
				sourceCompressedFileWithPath = sourceFileWithPath
				+ ".gz";
				System.out.println("sourceCompressedFileWithPath >> "+sourceCompressedFileWithPath);
				localFileName = getfileName(sourceCompressedFileWithPath);
				System.out.println("localFileName >> "+localFileName);
				CompressUtil.gzipFile(sourceFileWithPath,
						sourceCompressedFileWithPath);
				if ( i > 0){
					destinationPath = destinationPathOrderFile;
					ftpMovedDirectory = movedDirectoryOfOrderFile;
				} 
				else{
					destinationPath = destinationPathDataFile;
					ftpMovedDirectory = movedDirectoryOfDataFile;
				}
				
				String destinationFileWithPath = destinationPath
				+ File.separator + localFileName;
				destinationFileWithPath = destinationFileWithPath.replaceAll(
						"%20", " ");
				sftpChannel.cd(destinationPath);
				sftpChannel.put(sourceCompressedFileWithPath, localFileName);
				transferFile(sftpChannel, sourceCompressedFileWithPath, localFileName, sourceFileWithPath, destinationPath);
				System.out.println("File transfer is completed for file "
						+ getfileName(sourceFileWithPath));
				String sftpFileName = localFileName;
				fileMoveToImportLocation(sftpChannel, destinationPath, ftpMovedDirectory, sftpFileName);
				i++;
			}

		} catch (JSchException e) {
			e.printStackTrace();
			throw new CTBBusinessException(
					"Transfer failed: JSchException occurred while transfering file.");

		} catch (IOException e) {
			e.printStackTrace();
			throw new CTBBusinessException(
					"Transfer failed:IOException occurred while transfering file.");

		} catch (SftpException e) {
			e.printStackTrace();
			throw new CTBBusinessException(
					"Transfer failed: SftpException occurred while transfering file.");
		} finally {
			if (sftpChannel != null) {
				sftpChannel.exit();
			}
			if (session != null) {
				session.disconnect();
			}

		}

	}

	private void fileMoveToImportLocation(ChannelSftp sftpChannel,
			String destinationPath, String movedDirectory, String sftpFileName)
			throws SftpException {
		String sourcepath = destinationPath + File.separator + sftpFileName;
		String destinationpath = movedDirectory + File.separator + sftpFileName;
		System.out.println("sourcepath >> "+sourcepath);
		System.out.println("destinationpath >> "+destinationpath);
		sftpChannel.cd(movedDirectory);
		sftpChannel.rename(sourcepath, destinationpath);
	}
}
