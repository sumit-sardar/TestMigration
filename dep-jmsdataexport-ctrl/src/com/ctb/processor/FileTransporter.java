package com.ctb.processor;

import java.io.IOException;
import java.util.List;

import com.ctb.utils.Configuration;
import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPTransferType;

public class FileTransporter {

	public void doFtp(List<String> fileList) throws IOException, FTPException {
		
		
		String destinationPath = Configuration.getFtpFilepath();
		FTPClient ftpClient = new FTPClient();
		ftpClient
				.setRemoteHost(Configuration.getFtphost());
		ftpClient.connect();

		ftpClient.login(Configuration.getFtpuser(),Configuration.getFtppassword());
		ftpClient.setType(FTPTransferType.BINARY);
		for (String sourceFile : fileList) {
			sourceFile = sourceFile.replaceAll("%20", " ");
			String filename = getfileName(sourceFile);
			String destination = destinationPath + filename;
			destination = destination.replaceAll("%20", " ");
			ftpClient.put(sourceFile, destination);

		}

	}

	public void doSftp(List<String> fileList) throws IOException, FTPException {

	}

	public String getfileName(String srcfilePath) {
		String file = srcfilePath;
		int pos = srcfilePath.lastIndexOf("/");
		file = file.substring(pos + 1, file.length());
		return file;
	}
}
