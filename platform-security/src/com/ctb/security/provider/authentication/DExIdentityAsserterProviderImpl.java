package com.ctb.security.provider.authentication;

/**
* DExIdentityAsserterProviderImpl.java
* @author Tata Consultancy Services 
* This Class is created for implementing
* identity assertion  for DEx Application
*
*/

import java.util.HashMap;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.AppConfigurationEntry;
import weblogic.management.security.ProviderMBean;
import weblogic.security.spi.AuthenticationProvider;
import weblogic.security.spi.IdentityAsserter;
import weblogic.security.spi.IdentityAssertionException;
import weblogic.security.spi.PrincipalValidator;
import weblogic.security.spi.SecurityServices;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
import com.ctb.security.provider.authentication.DExIACallbackHandlerImpl;
import weblogic.security.spi.PrincipalValidator;
import weblogic.security.provider.PrincipalValidatorImpl;


public final class DExIdentityAsserterProviderImpl implements
		AuthenticationProvider, IdentityAsserter {
	
	final static private String TOKEN_TYPE = "DExPerimeterAtnToken";

	final static private String TOKEN_PREFIX = "username=";

	final static private String TOKEN_PREFIX2 = "&password=";

	private String description;

	/*
	 * @param mbean the mbean
	 * @param services
	 */	
	public void initialize(ProviderMBean mbean, SecurityServices services) {
		OASAuthLogger.getLogger().debug("DExIdentityAsserterProviderImpl.initialize");
		DExIdentityAsserterMBean myMBean = (DExIdentityAsserterMBean) mbean;
		description = myMBean.getDescription() + "\n" + myMBean.getVersion();
	}
	
	/*
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	public void shutdown() {
		OASAuthLogger.getLogger().debug("DExIdentityAsserterProviderImpl.shutdown");
	}
	
	/*
	 * @return null
	 */
	public AppConfigurationEntry getLoginModuleConfiguration() {
		return null;
	}
	
	/*
	 * @return null
	 */
	public AppConfigurationEntry getAssertionModuleConfiguration() {
		return null;
	}
	
	/*
	 * @return null
	 */
	public PrincipalValidator getPrincipalValidator() {
			return null;
	}
	
	/*
	 * @return this
	 */
	public IdentityAsserter getIdentityAsserter() {
		return this;
	}
	
	/*
	 * @param type
	 * @param token
	 * @return DExIACallbackHandlerImpl instance
	 * @throws IdentityAssertionException
	 */
	public CallbackHandler assertIdentity(String type, Object token)
			throws IdentityAssertionException {
		
		System.out.println(" type in dex===" + type );
		System.out.println("tokent type in dex===" + TOKEN_TYPE );
		if (!(TOKEN_TYPE.equals(type))) {
			String error = "DExIdentityAsserter received unknown token type \""
					+ type + "\"." + " Expected " + TOKEN_TYPE;
			OASAuthLogger.getLogger().error("\tError: " + error);
			System.out.println("\tError: " + error);
			throw new IdentityAssertionException(error);
		}
		if (!(token instanceof byte[])) {
			String error = "DExIdentityAsserter received unknown token class \""
					+ token.getClass() + "\"." + " Expected a byte[].";
			OASAuthLogger.getLogger().error("\tError: " + error);
			System.out.println("\tError: " + error);
			throw new IdentityAssertionException(error);
		}
		byte[] tokenBytes = (byte[]) token;
		if (tokenBytes == null || tokenBytes.length < 1) {
			String error = "DExIdentityAsserter received empty token byte array";
			OASAuthLogger.getLogger().error("\tError: " + error);
			System.out.println("\tError: " + error);
			throw new IdentityAssertionException(error);
		}
		String tokenStr = new String(tokenBytes);
		OASAuthLogger.getLogger().debug("\ttoken\t= " + tokenStr);
		System.out.println("\ttoken\t= " + tokenStr);
		/*try{
			//comment this code in weblogic 10.3 migration
			tokenStr = URLDecoder.decode(tokenStr,"UTF-8");
			
			
		}catch(Exception ex){
			String error = "Unknown token type";
			System.out.println("\tError: " + error);
			throw new IdentityAssertionException(error);
		}*/
		OASAuthLogger.getLogger().debug("\tdecoded token\t= " + tokenStr);
		System.out.println("\tdecoded token\t= " + tokenStr);
		DExAuthOAMConnection authOAM = new DExAuthOAMConnection();
		String userName = authOAM.getUserNameFromToken(tokenStr);
	
		OASAuthLogger.getLogger().debug("\tuserName\t= " + userName);
	
		return new DExIACallbackHandlerImpl(userName);
	}
}
