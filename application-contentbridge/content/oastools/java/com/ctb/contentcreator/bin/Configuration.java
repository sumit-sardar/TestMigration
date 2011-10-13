package com.ctb.contentcreator.bin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.ctb.common.tools.SystemException;

public class Configuration {
	protected Properties properties;
	protected File file;
	private String ftpHost;
	private String ftpUser;
	private String ftpPassword;
	public String adsDbUser;
	public String adsDbPassword;
	public String adsDbSid;
	public String adsDbHost;
	public String localFilePath;
	public String remoteFilePath;
	public int ftpPort;

	private String sid;
	private String user;
	private String password;
	private String host;
	private String repositoryURI;

	private boolean useThin; 

	public void load(File file) {
		this.file = file;
		properties = new Properties();
		try {
			properties.load(new FileInputStream(file));
		} catch (IOException e) {
			throw new SystemException(e.getMessage(), e);
		}
		readProperties();
	}

	private void readProperties() {

		ftpHost = getRequiredProperty(properties, "content.download.ftp.host", file);
		ftpUser = getRequiredProperty(properties, "content.download.ftp.user", file);
		ftpPassword = getRequiredProperty(properties, "content.download.ftp.password", file);
		adsDbUser = getRequiredProperty(properties, "adsdb.user", file);
		adsDbPassword = getRequiredProperty(properties, "adsdb.password", file);
		adsDbSid = getRequiredProperty(properties, "adsdb.sid", file);
		adsDbHost = getRequiredProperty(properties, "adsdb.host", file);
		localFilePath = getRequiredProperty(properties, "content.download.local.file.path", file);
		/*if(localFilePath !=null)
			localFilePath = localFilePath.replaceAll( " ", "%20");*/
		remoteFilePath = getRequiredProperty(properties, "content.download.ftp.file.path", file);
		String ftpPortString = properties.getProperty("content.download.ftp.port");
		if (ftpPortString==null || ftpPortString.trim().length()==0) {
			ftpPort = 22;
		} else  {
			try {
				ftpPort = Integer.parseInt(ftpPortString.trim());
			} catch (Exception e) {
				ftpPort = 22;
			}
			
		}
		
		repositoryURI = getRequiredProperty(properties, "content.download.repository.URI", file);
		sid = getRequiredProperty(properties, "db.sid", file);
        user = getRequiredProperty(properties, "db.user", file);
        password = getRequiredProperty(properties, "db.password", file);
        String thinProperty = properties.getProperty("db.useThin");

        if (thinProperty == null) {
            useThin = false;
        } else {
            useThin = thinProperty.equals("true");
        }
        if (useThin) {
            host = getRequiredProperty(properties, "db.host", file);
        }
	}

	private String getRequiredProperty(Properties properties, String key,
			File file) {
		String property = properties.getProperty(key);

		if (property == null) {
			throw new RuntimeException("Missing property '" + key
					+ "' in file " + file.getAbsolutePath());
		}
		return property;
	}

	/**
	 * @return the properties
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param file
	 *            the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * @return the ftpHost
	 */
	public String getFtpHost() {
		return ftpHost;
	}

	/**
	 * @param ftpHost
	 *            the ftpHost to set
	 */
	public void setFtpHost(String ftpHost) {
		this.ftpHost = ftpHost;
	}

	/**
	 * @return the ftpUser
	 */
	public String getFtpUser() {
		return ftpUser;
	}

	/**
	 * @param ftpUser
	 *            the ftpUser to set
	 */
	public void setFtpUser(String ftpUser) {
		this.ftpUser = ftpUser;
	}

	/**
	 * @return the ftpPassword
	 */
	public String getFtpPassword() {
		return ftpPassword;
	}

	/**
	 * @param ftpPassword
	 *            the ftpPassword to set
	 */
	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}

	/**
	 * @return the adsDbUser
	 */
	public String getAdsDbUser() {
		return adsDbUser;
	}

	/**
	 * @param adsDbUser
	 *            the adsDbUser to set
	 */
	public void setAdsDbUser(String adsDbUser) {
		this.adsDbUser = adsDbUser;
	}

	/**
	 * @return the adsDbPassword
	 */
	public String getAdsDbPassword() {
		return adsDbPassword;
	}

	/**
	 * @param adsDbPassword
	 *            the adsDbPassword to set
	 */
	public void setAdsDbPassword(String adsDbPassword) {
		this.adsDbPassword = adsDbPassword;
	}

	/**
	 * @return the adsDbSid
	 */
	public String getAdsDbSid() {
		return adsDbSid;
	}

	/**
	 * @param adsDbSid
	 *            the adsDbSid to set
	 */
	public void setAdsDbSid(String adsDbSid) {
		this.adsDbSid = adsDbSid;
	}

	/**
	 * @return the adsDbHost
	 */
	public String getAdsDbHost() {
		return adsDbHost;
	}

	/**
	 * @param adsDbHost
	 *            the adsDbHost to set
	 */
	public void setAdsDbHost(String adsDbHost) {
		this.adsDbHost = adsDbHost;
	}

	/**
	 * @return the sid
	 */
	public String getSid() {
		return sid;
	}

	/**
	 * @param sid
	 *            the sid to set
	 */
	public void setSid(String sid) {
		this.sid = sid;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host
	 *            the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the useThin
	 */
	public boolean isUseThin() {
		return useThin;
	}

	/**
	 * @param useThin the useThin to set
	 */
	public void setUseThin(boolean useThin) {
		this.useThin = useThin;
	}

	/**
	 * @return the localFilePath
	 */
	public String getLocalFilePath() {
		return localFilePath;
	}

	/**
	 * @return the remoteFilePath
	 */
	public String getRemoteFilePath() {
		return remoteFilePath;
	}

	/**
	 * @param localFilePath the localFilePath to set
	 */
	public void setLocalFilePath(String localFilePath) {
		this.localFilePath = localFilePath;
	}

	/**
	 * @param remoteFilePath the remoteFilePath to set
	 */
	public void setRemoteFilePath(String remoteFilePath) {
		this.remoteFilePath = remoteFilePath;
	}

	/**
	 * @return the ftpPort
	 */
	public int getFtpPort() {
		return ftpPort;
	}

	/**
	 * @param ftpPort the ftpPort to set
	 */
	public void setFtpPort(int ftpPort) {
		this.ftpPort = ftpPort;
	}

	/**
	 * @return the repositoryURI
	 */
	public String getRepositoryURI() {
		return repositoryURI;
	}

	/**
	 * @param repositoryURI the repositoryURI to set
	 */
	public void setRepositoryURI(String repositoryURI) {
		this.repositoryURI = repositoryURI;
	}

	/* abstract protected void readProperties(); */
}
