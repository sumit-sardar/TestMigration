package com.ctb.security.provider.authentication;

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
import com.ctb.security.provider.authentication.OASIACallbackHandlerImpl;
import weblogic.security.spi.PrincipalValidator;
import weblogic.security.provider.PrincipalValidatorImpl;

public final class OASIdentityAsserterProviderImpl implements
		AuthenticationProvider, IdentityAsserter {
	final static private String TOKEN_TYPE = "OASPerimeterAtnToken";

	final static private String TOKEN_PREFIX = "username=";

	final static private String TOKEN_PREFIX2 = "&password=";

	private String description;

	public void initialize(ProviderMBean mbean, SecurityServices services) {
		OASAuthLogger.getLogger().debug("OASIdentityAsserterProviderImpl.initialize");
		OASIdentityAsserterMBean myMBean = (OASIdentityAsserterMBean) mbean;
		description = myMBean.getDescription() + "\n" + myMBean.getVersion();
	}

	public String getDescription() {
		return description;
	}

	public void shutdown() {
		OASAuthLogger.getLogger().debug("OASIdentityAsserterProviderImpl.shutdown");
	}

	public AppConfigurationEntry getLoginModuleConfiguration() {
		return null;
	}

	public AppConfigurationEntry getAssertionModuleConfiguration() {
		return null;
	}

	public PrincipalValidator getPrincipalValidator() {
			return null;
	}

	public IdentityAsserter getIdentityAsserter() {
		return this;
	}

	public CallbackHandler assertIdentity(String type, Object token)
			throws IdentityAssertionException {
		OASAuthLogger.getLogger().debug("OASIdentityAsserterProviderImpl.assertIdentity");
		OASAuthLogger.getLogger().debug("\tType\t\t= " + type);
		OASAuthLogger.getLogger().debug("\tToken\t\t= " + token);
		if (!(TOKEN_TYPE.equals(type))) {
			String error = "OASIdentityAsserter received unknown token type \""
					+ type + "\"." + " Expected " + TOKEN_TYPE;
			OASAuthLogger.getLogger().error("\tError: " + error);
			throw new IdentityAssertionException(error);
		}
		if (!(token instanceof byte[])) {
			String error = "OASIdentityAsserter received unknown token class \""
					+ token.getClass() + "\"." + " Expected a byte[].";
			OASAuthLogger.getLogger().error("\tError: " + error);
			throw new IdentityAssertionException(error);
		}
		byte[] tokenBytes = (byte[]) token;
		if (tokenBytes == null || tokenBytes.length < 1) {
			String error = "OASIdentityAsserter received empty token byte array";
			OASAuthLogger.getLogger().error("\tError: " + error);
			throw new IdentityAssertionException(error);
		}
		String tokenStr = new String(tokenBytes);
		if (!(tokenStr.startsWith(TOKEN_PREFIX)) || tokenStr.indexOf(TOKEN_PREFIX2) <= 0 ) {
			String error = "OASIdentityAsserter received unknown token string \""
					+ tokenStr + "\"." + " Expected " + TOKEN_PREFIX + "username"
					+ TOKEN_PREFIX2 + "password";
			OASAuthLogger.getLogger().error("\tError: " + error);
			throw new IdentityAssertionException(error);
		}
		String userName = tokenStr.substring(TOKEN_PREFIX.length(), tokenStr.indexOf(TOKEN_PREFIX2));
		OASAuthLogger.getLogger().debug("\tuserName\t= " + userName);
		String password = tokenStr.substring(tokenStr.indexOf(TOKEN_PREFIX2) + TOKEN_PREFIX2.length());
		OASAuthLogger.getLogger().debug("\tpassword\t= " + password);
		return new OASIACallbackHandlerImpl(userName, password);
	}
}
