package comm.ctb.utils;

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

import comm.ctb.utils.Configuration;

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

	/**
	 * @param args
	 */
	
	public static Session getSFTPSession(Configuration conf) throws Exception {
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
		
	
	public static void doSFTP(Session session, String destinationDir, String sourceDir, String filename ) throws  JSchException, SftpException {
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
	
	
	public static void closeSFTPClient(Session session) {
		try {
			session.disconnect();
		} catch (Exception e) {

		}
	}
	
	/* Consolidated :: */
	public void sendfilesSFTP(Configuration config, String destinationPath, String sourceFile) throws Exception {

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
	
	public static void downloadFiles(Session session, String sourceDir, String targetDir){
		ChannelSftp sftpChannel = null;
		InputStream inStream  = null;
		FileOutputStream outStream = null;
		try {
			Channel channel = session.openChannel("sftp");
			channel.connect();
			sftpChannel = (ChannelSftp) channel;
			sftpChannel.cd(sourceDir);
			Vector fileList = sftpChannel.ls("*.xml");
			
			if(fileList != null && fileList.size() > 0){
				//System.out.println("Start Time: "+new Date(System.currentTimeMillis()));
				for (Iterator iterator = fileList.iterator(); iterator
						.hasNext();) {
					ChannelSftp.LsEntry entry = (ChannelSftp.LsEntry) iterator.next();
					//System.out.println("File: "+entry.getFilename());															
					try {
						inStream = sftpChannel.get(entry.getFilename());
						outStream = new FileOutputStream(new File(targetDir + File.separator + entry.getFilename()));
						
						if(inStream ==  null){
							System.out.println("Could not retrieve file...."+entry.getFilename());
							continue;
						}
						
						int read = 0;
						byte[] bytes = new byte[1024];
						while((read = inStream.read(bytes)) != -1){
							outStream.write(bytes, 0, read);
						}
						System.out.println("File ->"+ entry.getFilename() +" downloaded successfully...");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.println("File ->"+ entry.getFilename() +" download failed...");
						e.printStackTrace();
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
				//System.out.println("End Time: "+new Date(System.currentTimeMillis()));
			}
			
		}
		catch(Exception e){
				e.printStackTrace();
		
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
	
	public static void archiveProcessedFiles(Session session, String sourceDir, String targetDir, String fileName){
		//TODO:
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
			sftpChannel.rename(sourceDir + fileName, targetDir + archiveDate + "/"+ fileName);
			//sftpChannel.rename(filename+".transferring", filename);
			//sftpChannel.rm(sourceDir + fileName);
			System.out.println("File -> \""+ fileName +"\": archived successfully to "+targetDir+archiveDate);
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			if (sftpChannel != null) {
				try{
					sftpChannel.exit();
				} catch (Exception e){
					e.printStackTrace();
				}
				
			}
		}
	}
	
	public static void accessSftpFolder(Session session, String sourceDir){
		ChannelSftp sftpChannel = null;
		try {
			Channel channel = session.openChannel("sftp");
			channel.connect();
			sftpChannel = (ChannelSftp) channel;
			sftpChannel.cd(sourceDir);
			Vector fileList = sftpChannel.ls("*.xml");
			for (Iterator iterator = fileList.iterator(); iterator.hasNext();) {
				ChannelSftp.LsEntry entry = (ChannelSftp.LsEntry) iterator.next();
				//sftpChannel.get(entry.getFilename(), destinationPath + entry.getFileName());
				System.out.println("File: "+entry.getFilename());
				InputStream is = sftpChannel.get(entry.getFilename());
				System.out.println("Start Time: "+new Date(System.currentTimeMillis()));
				//parseXMLFile(is);
				System.out.println("End Time: "+new Date(System.currentTimeMillis()));
			}
		
		}
		catch(Exception e){
				e.printStackTrace();
		
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
	

}


