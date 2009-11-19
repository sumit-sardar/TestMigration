package com.ctb.security.provider.authentication;

/**
 * DExAuthenticationProviderImpl.java
 * @author Tata Consultancy Services 
 * This Class is created for implementing
 * authentication provider for DEx Application
 *
 */

import java.util.HashMap;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
import weblogic.management.security.ProviderMBean;
import weblogic.security.provider.PrincipalValidatorImpl;
import weblogic.security.spi.AuthenticationProvider;
import weblogic.security.spi.IdentityAsserter;
import weblogic.security.spi.PrincipalValidator;
import weblogic.security.spi.SecurityServices;
import weblogic.logging.NonCatalogLogger;

public final class DExAuthenticationProviderImpl implements
		AuthenticationProvider {
	
	private String description;

	private LoginModuleControlFlag controlFlag;

	private String jdbcDriver;

	private String dbURL;

	private String dbUser;

	private String dbPassword;
	
	/*
	 *@param mbean
	 *@param services
	 *@throws IllegalArgumentException
	 */
	public void initialize(ProviderMBean mbean, SecurityServices services) {
		
		// Print debugging information
		OASAuthLogger.getLogger().debug("DExAuthenticationProviderImpl.initialize");

		DExAuthenticatorMBean myMBean = (DExAuthenticatorMBean) mbean;
		description = myMBean.getDescription() + "\n" + myMBean.getVersion();
		jdbcDriver = myMBean.getJDBCDriver();
		dbURL = myMBean.getDatabaseURL();
		dbUser = myMBean.getUser();
		dbPassword = myMBean.getPassword();

		String flag = myMBean.getControlFlag();
		if (flag.equalsIgnoreCase("REQUIRED")) {
			controlFlag = LoginModuleControlFlag.REQUIRED;
		} else if (flag.equalsIgnoreCase("OPTIONAL")) {
			controlFlag = LoginModuleControlFlag.OPTIONAL;
		} else if (flag.equalsIgnoreCase("REQUISITE")) {
			controlFlag = LoginModuleControlFlag.REQUISITE;
		} else if (flag.equalsIgnoreCase("SUFFICIENT")) {
			controlFlag = LoginModuleControlFlag.SUFFICIENT;
		} else {
			throw new IllegalArgumentException("invalid flag value" + flag);
		}
	}

	/*
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	public void shutdown() {
	
		OASAuthLogger.getLogger().debug("DExAuthenticationProviderImpl.shutdown");
	}
	
	/*
	 * @param options
	 * @return AppConfigurationEntry instance
	 */
	private AppConfigurationEntry getConfiguration(HashMap options) {
		
		OASAuthLogger.getLogger().debug("Requesting login module configurations");

		options.put("JDBCDriver", jdbcDriver);
		options.put("DatabaseURL", dbURL);
		options.put("User", dbUser);
		options.put("Password", dbPassword);

		return new AppConfigurationEntry(
				"com.ctb.security.provider.authentication.DExLoginModuleImpl",
				controlFlag, options);
	}

	/*
	 * @return AppConfigurationEntry instance
	 */
	public AppConfigurationEntry getLoginModuleConfiguration() {
		HashMap options = new HashMap();
		return getConfiguration(options);
	}
	
	/*
	 * @return AppConfigurationEntry instance
	 */
	public AppConfigurationEntry getAssertionModuleConfiguration() {
		HashMap options = new HashMap();
		options.put("IdentityAssertion", "true");
		return getConfiguration(options);
	}
	
	/*
	 * @return PrincipalValidatorImpl instance
	 */
	public PrincipalValidator getPrincipalValidator() {
		return new PrincipalValidatorImpl();
	}
	
	/*
	 * @return null
	 */
	public IdentityAsserter getIdentityAsserter() {
		return null;
	}
}
