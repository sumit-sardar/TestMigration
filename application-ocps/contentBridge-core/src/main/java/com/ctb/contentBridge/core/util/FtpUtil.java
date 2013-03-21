package com.ctb.contentBridge.core.util;

import java.io.File;
import java.util.Properties;

import com.ctb.contentBridge.core.domain.Configuration;
import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPTransferType;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class FtpUtil {

	public static FTPClient getFtpConnection(Configuration conf)
			throws Exception {
		FTPClient ftpClient = new FTPClient();
		ftpClient.setRemoteHost(conf.getFtpHost());
		ftpClient.connect();
		ftpClient.login(conf.getFtpUser(), conf.getFtpPassword());
		ftpClient.setType(FTPTransferType.BINARY);
		return ftpClient;

	}

	public static void doFtp(FTPClient ftpClient, String sourceFile,
			String destination) throws Exception {
		ftpClient.put(sourceFile, destination);
	}

	public static void closeFtpClient(FTPClient ftpClient) {
		try {
			ftpClient.quit();
		} catch (Exception e) {

		}
	}

	public static Session getSFtpSession(Configuration conf)
			throws Exception {
		JSch jsch = new JSch();
		Session session = null;
		
		Properties properties = new Properties();
		properties.put("StrictHostKeyChecking", "no");
		properties.put("compression.s2c", "none");
		properties.put("compression.c2s", "none");
		String ftpHost = conf.getFtpHost();
		String ftpUser = conf.getFtpUser();
		String ftpPass = conf.getFtpPassword();
		String ftpPort = conf.getFtpPort();

		session = jsch.getSession(ftpUser, ftpHost, Integer.parseInt(ftpPort));
		session.setConfig(properties);
		session.setPassword(ftpPass);

		session.connect();
		return session;

	}
	
	
	public static Session getSFtpSession(Configuration conf,String ftpHost,String ftpUser,String ftpPass,int ftpPort)
			throws Exception {
		JSch jsch = new JSch();
		Session session = null;
		
		Properties properties = new Properties();
		properties.put("StrictHostKeyChecking", "no");
		properties.put("compression.s2c", "none");
		properties.put("compression.c2s", "none");
	

		session = jsch.getSession(ftpUser, ftpHost, ftpPort);
		session.setConfig(properties);
		session.setPassword(ftpPass);

		session.connect();
		return session;

	}

	public static void doSftp(Session session, String destinationDir, String sourceDir, String filename ) throws  JSchException, SftpException {
		ChannelSftp sftpChannel = null;
		try {
			Channel channel = session.openChannel("sftp");
			channel.connect();
			sftpChannel = (ChannelSftp) channel;
			sftpChannel.cd(destinationDir);
			sftpChannel.put(sourceDir+File.separator+filename, filename);
			//sftpChannel.rename(filename+".transferring", filename);
				
			
		} finally {
			if (sftpChannel != null) {
				try{
					sftpChannel.exit();
				} catch (Exception e){
					e.printStackTrace();
				}
				
			}
		}
		
	}
	
	
	public static void closeSFtpClient(Session session) {
		try {
			session.disconnect();
		} catch (Exception e) {

		}
	}

}
