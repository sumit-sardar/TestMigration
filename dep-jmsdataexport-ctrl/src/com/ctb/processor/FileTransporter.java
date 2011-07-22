package com.ctb.processor;

import java.io.File;
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

public class FileTransporter {

	public static FileTransporter transporter;
	static final String TRANSPORT_TYPE_FTP = "FTP";
	static final String TRANSPORT_TYPE_SFTP = "SFTP";
	static final String TRANSPORT_TYPE_SCP = "SCP";

	private FileTransporter(){
		super();
		
	}
	

	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	static FileTransporter getInstance() {

		if (transporter == null) {
			transporter = new FileTransporter();
		}
		return transporter;
	}

	public void transferFile(String transportType, List<String> fileList)
			throws CTBBusinessException {
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

	private void doFtp(List<String> fileList) throws CTBBusinessException {

		String destinationPath = Configuration.getFtpFilepath();
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
	
				String destination = destinationPath + File.separator + filename;
				destination = destination.replaceAll("%20", " ");
				ftpClient.put(compressedFile, destination);
				new File(compressedFile).delete();
	
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

	private void doSftp(List<String> fileList) throws CTBBusinessException  {

		JSch jsch = new JSch();
		Session session = null;
		ChannelSftp sftpChannel = null;
		Properties properties = new Properties();
		properties.put("StrictHostKeyChecking", "no");
		properties.put("compression.s2c", "zlib@openssh.com,zlib,none");
		properties.put("compression.c2s", "zlib@openssh.com,zlib,none");
		properties.put("compression_level", "9");
		// properties.put("compression.s2c", "none");
		// properties.put("compression.c2s", "none");

		String destinationPath = Configuration.getFtpFilepath();
		String ftpHost = Configuration.getFtphost();
		String ftpUser = Configuration.getFtpuser();
		String ftpPass = Configuration.getFtppassword();
		try {
			session = jsch.getSession(ftpUser, ftpHost, 22);
			session.setConfig(properties);
			session.setPassword(ftpPass);

			session.connect();
			Channel channel = session.openChannel("sftp");
			channel.connect();
			sftpChannel = (ChannelSftp) channel;

			for (String sourceFileWithPath : fileList) {
				System.out.println("File transfer started for file "+getfileName(sourceFileWithPath));
				sourceFileWithPath = sourceFileWithPath.replaceAll("%20", " ");
				String sourceCompressedFileWithPath = sourceFileWithPath + ".gz";
				String filename = getfileName(sourceCompressedFileWithPath);
				CompressUtil.gzipFile(sourceFileWithPath,sourceCompressedFileWithPath);
				String destinationFileWithPath = destinationPath + File.separator + filename;
				destinationFileWithPath = destinationFileWithPath.replaceAll("%20", " ");
				sftpChannel.cd(destinationPath);
				//System.out.println(sftpChannel.pwd());
				//System.out.println(new File(sourceCompressedFileWithPath).exists());
				sftpChannel.put(sourceCompressedFileWithPath, filename);
				for (Object o : sftpChannel.ls(destinationPath)) {
					String val = String.valueOf(o);
					if (val.endsWith(filename)) {
						new File(sourceCompressedFileWithPath)
								.renameTo(new File(sourceCompressedFileWithPath
										+ ".transfered"));
						if (new File(sourceFileWithPath).exists())
							new File(sourceFileWithPath).delete();
					}

				}
				System.out.println("File transfer is completed for file "+getfileName(sourceFileWithPath));
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

	private String getfileName(String srcfilePath) {
		String file = srcfilePath;
		int pos = srcfilePath.lastIndexOf(File.separator);
		file = file.substring(pos + 1, file.length());
		return file;
	}

	/*public static void main(String[] args) throws IOException, SftpException {

		List<String> fileList = new ArrayList<String>();
		;
		File f = new File("C:/emetric/1");
		File[] fileArray = f.listFiles();
		for (File ff : fileArray) {
			fileList.add(ff.getPath());
		}

		FileTransporter transporter = new FileTransporter();

		transporter.doSftp(fileList);

	}*/


}
