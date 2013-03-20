package com.ctb.utils;

import java.io.File;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.ctb.utils.Configuration;

public class SftpUtil {
	
	public static Session getSFtpSession(Configuration conf) throws Exception {
		JSch jsch = new JSch();
		Session session = null;
		
		Properties properties = new Properties();
		properties.put("StrictHostKeyChecking", "no");
		properties.put("compression.s2c", "none");
		properties.put("compression.c2s", "none");
		String ftpHost = conf.getFtpHost();
		String ftpUser = conf.getFtpUser();
		String ftpPass = conf.getFtpPassword();
		int ftpPort = Integer.parseInt(conf.getFtpPort());
		
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
	
	/* Consolidated :: */
	public void sendfiles_sftp(Configuration config, String destinationPath, String sourceFile) throws Exception {

		JSch jsch = new JSch();
		Session session = null;
		ChannelSftp sftpChannel = null;
		Properties properties = new Properties();
		properties.put("StrictHostKeyChecking", "no");
		properties.put("compression.s2c", "none");
		properties.put("compression.c2s", "none");

		String ftpHost = config.getFtpHost();
		String ftpUser = config.getFtpUser();
		String ftpPass = config.getFtpPassword();
		int port = Integer.parseInt(config.getFtpPort());
		
		try {
			session = jsch.getSession(ftpUser, ftpHost, port);
			session.setConfig(properties);
			session.setPassword(ftpPass);
			session.connect();
			Channel channel = session.openChannel("sftp");
			channel.connect();
			sftpChannel = (ChannelSftp) channel;

			String destination = destinationPath;
			// sftpChannel.cd(destinationPath);
			sftpChannel.put(sourceFile, destination);

		} catch (SftpException e) {
			System.err.println("Exception : " + e.getMessage());
			e.printStackTrace();
		}
		finally {
			if (sftpChannel != null) {
				sftpChannel.exit();
			}
			if (session != null) {
				session.disconnect();
			}
		}

	}
}
