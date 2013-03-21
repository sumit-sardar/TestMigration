package com.ctb.contentBridge.core.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.ctb.contentBridge.core.exception.ExceptionResolver;

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
	public String ftpPort;

	private String sid;
	private String env;
	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	private String user;
	private String password;
	private String host;
	private String repositoryURI;

	private boolean useThin;
	
	public String cbDbUser;
	public String cbDbPassword;
	public String cbDbSid;
	public String cbDbHost;
	
	private String itmXsltPath;
	private String sleepTime;
	private String dateFormat;
			private String dateDisplay;
			
			private String appQa;
			private String oasTest1;
			private String oasTest2;
			private String dev;
			private String cqa;
			private String stagging;
			private String production;
			private String flashDev;
			
		
			public String getAppQa() {
				return appQa;
			}

			public void setAppQa(String appQa) {
				this.appQa = appQa;
			}

			public String getOasTest1() {
				return oasTest1;
			}

			public void setOasTest1(String oasTest1) {
				this.oasTest1 = oasTest1;
			}

			public String getOasTest2() {
				return oasTest2;
			}

			public void setOasTest2(String oasTest2) {
				this.oasTest2 = oasTest2;
			}

			public String getDev() {
				return dev;
			}

			public void setDev(String dev) {
				this.dev = dev;
			}

			public String getCqa() {
				return cqa;
			}

			public void setCqa(String cqa) {
				this.cqa = cqa;
			}

			public String getStagging() {
				return stagging;
			}

			public void setStagging(String stagging) {
				this.stagging = stagging;
			}

			public String getProduction() {
				return production;
			}

			public void setProduction(String production) {
				this.production = production;
			}

			public String getFlashDev() {
				return flashDev;
			}

			public void setFlashDev(String flashDev) {
				this.flashDev = flashDev;
			}

			public void load(File file) throws Exception {
		this.file = file;
		properties = new Properties();
		try {
			properties.load(new FileInputStream(file));
		} catch (IOException e) {
			throw ExceptionResolver.resolve(e);
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
			ftpPort ="22";
		} else  {
			try {
				ftpPort = ftpPortString.trim();
			} catch (Exception e) {
				ftpPort ="22";
			}
			
		}
		
		repositoryURI = getRequiredProperty(properties, "content.download.repository.URI", file);
		sid = getRequiredProperty(properties, "db.sid", file);
        user = getRequiredProperty(properties, "db.user", file);
        password = getRequiredProperty(properties, "db.password", file);
        String thinProperty = properties.getProperty("db.useThin");
        
        cbDbSid = getRequiredProperty(properties, "cb.sid", file);
        cbDbUser = getRequiredProperty(properties, "cb.user", file);
        cbDbPassword = getRequiredProperty(properties, "cb.password", file);
        cbDbHost = getRequiredProperty(properties, "cb.host", file);
       
        				
        appQa=getRequiredProperty(properties, "APPQA", file);
        appQa=getRequiredProperty(properties, "OASTEST1", file);
        oasTest1=getRequiredProperty(properties, "APPQAOASTEST2", file);
        oasTest2=getRequiredProperty(properties, "DEV", file);
        dev=getRequiredProperty(properties, "CQA", file);
        cqa=getRequiredProperty(properties, "STAGING", file);
        stagging=getRequiredProperty(properties, "PRODUCTION", file);
        flashDev=getRequiredProperty(properties, "FLSSHDEV", file);
        if (thinProperty == null) {
            useThin = false;
        } else {
            useThin = thinProperty.equals("true");
        }
        if (useThin) {
            host = getRequiredProperty(properties, "db.host", file);
        }
        sleepTime = getRequiredProperty(properties, "sleep.time", file);
        dateDisplay = getRequiredProperty(properties, "date.display", file);
        dateFormat = getRequiredProperty(properties, "date.format", file);
	}

	private String getRequiredProperty(Properties properties, String key,
			File file) {
		String property = properties.getProperty(key);

		/*if (property == null) {
			throw new RuntimeException("Missing property '" + key
					+ "' in file " + file.getAbsolutePath());
		}*/
		return property;
	}
	public String getRequiredProperty(String key) {
		String property = properties.getProperty(key);

		/*if (property == null) {
			throw new RuntimeException("Missing property '" + key
					+ "' in file " + file.getAbsolutePath());
		}*/
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
	 * @return the cbDbUser
	 */
	public String getCbDbUser() {
		return cbDbUser;
	}

	/**
	 * @param cbDbUser
	 *            the cbDbUser to set
	 */
	public void setCbDbUser(String cbDbUser) {
		this.cbDbUser = cbDbUser;
	}

	/**
	 * @return the cbDbPassword
	 */
	public String getCbDbPassword() {
		return cbDbPassword;
	}

	/**
	 * @param cbDbPassword
	 *            the cbDbPassword to set
	 */
	public void setCbDbPassword(String cbDbPassword) {
		this.cbDbPassword = cbDbPassword;
	}

	/**
	 * @return the cbDbSid
	 */
	public String getCbDbSid() {
		return cbDbSid;
	}

	/**
	 * @param adsCbSid
	 *            the cbDbSid to set
	 */
	public void setCbDbSid(String cbDbSid) {
		this.cbDbSid = cbDbSid;
	}

	/**
	 * @return the cbDbHost
	 */
	public String getCbDbHost() {
		return cbDbHost;
	}

	/**
	 * @param cbDbHost
	 *            the cbDbHost to set
	 */
	public void setCbDbHost(String cbDbHost) {
		this.cbDbHost = cbDbHost;
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
	 * @return the repositoryURI
	 */
	public String getRepositoryURI() {
		return repositoryURI;
	}

	public String getFtpPort() {
		return ftpPort;
	}

	public void setFtpPort(String ftpPort) {
		this.ftpPort = ftpPort;
	}

	/**
	 * @param repositoryURI the repositoryURI to set
	 */
	public void setRepositoryURI(String repositoryURI) {
		this.repositoryURI = repositoryURI;
	}
	
	/**
	 * @return the xsltPath
	 */
	public String getItmXsltPath() {
		return itmXsltPath;
	}

	/**
	 * @param xsltPath the xsltPath to set
	 */
	public void setItmXsltPath(String itmXsltPath) {
		this.itmXsltPath = itmXsltPath;
	}
	
	/**
	 * @return the sleepTime
	 */
	public String getSleepTime() {
		return sleepTime;
	}

	/**
	 * @param sleepTime the sleepTime to set
	 */
	public void setSleepTime(String sleepTime) {
		this.sleepTime = sleepTime;
	}
	
	/**
	 * @return the dateFormat
	 */
	public String getDateFormat() {
		return dateFormat;
	}

	/**
	 * @param dateFormat the dateFormat to set
	 */
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	/**
	 * @return the dateFormat
	 */
	public String getDateDisplay() {
		return dateDisplay;
	}

	/**
	 * @param dateFormat the dateFormat to set
	 */
	public void setDateDisplay(String dateDisplay) {
		this.dateDisplay = dateDisplay;
	}

	/* abstract protected void readProperties(); */
}
