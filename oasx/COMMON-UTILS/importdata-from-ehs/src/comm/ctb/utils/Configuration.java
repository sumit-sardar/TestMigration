package comm.ctb.utils;

public class Configuration {

	static String localFilePath = "";
	static String ftphost = "";
	static String ftpuser = "";
	static String ftppassword = "";
	static String ftpFilepath = "";
	static String ftpPort = "";
	static String archivePath = "";
	/*Added for invoke scoring: Start*/
	static String jndiFactory;
	static String jmsFactory;
	static String jmsURL;
	static String jmsQueue;
	static String jmsPrincipal;
	static String jmsCredentials;

	static {

		localFilePath = ExtractUtil.getDetail("oas.importdata.local.filepath");
		ftphost = ExtractUtil.getDetail("oas.importdata.ftphost");
		ftpuser = ExtractUtil.getDetail("oas.importdata.ftpuser");
		ftppassword = ExtractUtil.getDetail("oas.importdata.ftppassword");
		ftpFilepath = ExtractUtil.getDetail("oas.importdata.ftp.filepath");
		ftpPort = ExtractUtil.getDetail("oas.importdata.ftp.port");
		archivePath = ExtractUtil.getDetail("oas.importdata.ftp.archivepath");
		/*Added for invoke scoring: Start*/
		jndiFactory = ExtractUtil.getDetail("jndiFactory");
		jmsFactory = ExtractUtil.getDetail("jmsFactory");
		jmsURL = ExtractUtil.getDetail("jmsURL");
		jmsQueue = ExtractUtil.getDetail("jmsQueue");
		jmsPrincipal = ExtractUtil.getDetail("jmsPrincipal");
		jmsCredentials = ExtractUtil.getDetail("jmsCredentials");
	}

	
	
	/**
	 * @return the localFilePath
	 */
	public static String getLocalFilePath() {
		return localFilePath;
	}

	/**
	 * @return the ftphost
	 */
	public static String getFtpHost() {
		return ftphost;
	}

	/**
	 * @return the ftpuser
	 */
	public static String getFtpUser() {
		return ftpuser;
	}

	/**
	 * @return the ftppassword
	 */
	public static String getFtpPassword() {
		return ftppassword;
	}

	/**
	 * @return the filepath
	 */
	public static String getFtpFilepath() {
		return ftpFilepath;
	}

	/**
	 * @return the ftpPort
	 */
	public static String getFtpPort() {
		return ftpPort;
	}
	
	/**
	 * @return the localFilePath
	 */
	public static String getArchiveFilePath() {
		return archivePath;
	}
	/*Added for invoke scoring: Start*/

	/**
	 * @return the jndiFactory
	 */
	public static String getJndiFactory() {
		return jndiFactory;
	}

	/**
	 * @param jndiFactory the jndiFactory to set
	 */
	public static void setJndiFactory(String jndiFactory) {
		Configuration.jndiFactory = jndiFactory;
	}

	/**
	 * @return the jmsFactory
	 */
	public static String getJmsFactory() {
		return jmsFactory;
	}

	/**
	 * @param jmsFactory the jmsFactory to set
	 */
	public static void setJmsFactory(String jmsFactory) {
		Configuration.jmsFactory = jmsFactory;
	}

	/**
	 * @return the jmsURL
	 */
	public static String getJmsURL() {
		return jmsURL;
	}

	/**
	 * @param jmsURL the jmsURL to set
	 */
	public static void setJmsURL(String jmsURL) {
		Configuration.jmsURL = jmsURL;
	}

	/**
	 * @return the jmsQueue
	 */
	public static String getJmsQueue() {
		return jmsQueue;
	}

	/**
	 * @param jmsQueue the jmsQueue to set
	 */
	public static void setJmsQueue(String jmsQueue) {
		Configuration.jmsQueue = jmsQueue;
	}

	/**
	 * @return the jmsPrincipal
	 */
	public static String getJmsPrincipal() {
		return jmsPrincipal;
	}

	/**
	 * @param jmsPrincipal the jmsPrincipal to set
	 */
	public static void setJmsPrincipal(String jmsPrincipal) {
		Configuration.jmsPrincipal = jmsPrincipal;
	}

	/**
	 * @return the jmsCredentials
	 */
	public static String getJmsCredentials() {
		return jmsCredentials;
	}

	/**
	 * @param jmsCredentials the jmsCredentials to set
	 */
	public static void setJmsCredentials(String jmsCredentials) {
		Configuration.jmsCredentials = jmsCredentials;
	}
}
