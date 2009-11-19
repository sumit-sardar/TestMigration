package com.ctb.security.provider.authentication;

/**
* DExLoginModuleImpl.java
* @author Tata Consultancy Services 
* This Class is created for implementing
* single signon  for DEx Application
*
*/

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import weblogic.management.utils.NotFoundException;
import weblogic.security.principal.WLSGroupImpl;
import weblogic.security.principal.WLSUserImpl;
import weblogic.logging.NonCatalogLogger;

public final class DExLoginModuleImpl implements LoginModule {
	
	private CallbackHandler callbackHandler;

	private OASAuthenticatorDatabase database;

	// Determine whether this is a login or assert identity
	private boolean isIdentityAssertion;

	// Authentication status
	private boolean loginSucceeded;

	private Vector principalsForSubject = new Vector();

	private boolean principalsInSubject;

	private Subject subject;

	/**
	 *
	 * @return boolean
	 * @exception LoginException
	 *               
	 */
	public boolean abort() throws LoginException {
		// only called (once!) after login or commit
		// or may be? called (n times) after abort

		// loginSucceeded should be true or false
		// user should be null if !loginSucceeded, otherwise null or not-null
		// group should be null if user == null, otherwise null or not-null
		// principalsInSubject should be false if user is null, otherwise true
		// or false

		// Print debugging information
		OASAuthLogger.getLogger().debug("DExLoginModule.abort");

		if (principalsInSubject) {
			subject.getPrincipals().removeAll(principalsForSubject);
			principalsInSubject = false;
		}

		// Destroy the database connection, since we are done with it
		database = null;

		return true;
	}

	/**
	 *
	 * @return boolean
	 * @exception LoginException
	 *           
	 */
	public boolean commit() throws LoginException {
		// only called (once!) after login

		// loginSucceeded should be true or false
		// principalsInSubject should be false
		// user should be null if !loginSucceeded, null or not-null otherwise
		// group should be null if user == null, null or not-null otherwise

		// Print debugging information
		OASAuthLogger.getLogger().debug("DExLoginModule.commit");

		// Destroy the database connection, since we are done with it
		database = null;

		if (loginSucceeded) {
			subject.getPrincipals().addAll(principalsForSubject);
			principalsInSubject = true;
			OASAuthLogger.getLogger().debug("addAll( principalsForSubject ) return true");

			return true;
		} else {
			OASAuthLogger.getLogger().debug("return false");
			return false;
		}
	}

	/**
	 *
	 * @param subject
	 * @param callbackHandler
	 * @param sharedState
	 * @param options
	 * 
	 */
	public void initialize(Subject subject, CallbackHandler callbackHandler,
			Map sharedState, Map options) {
		// only called (once!) after the constructor and before login

		// Print debugging information
		OASAuthLogger.getLogger().debug("DExLoginModuleImpl.initialize");

		this.subject = subject;
		this.callbackHandler = callbackHandler;

		// Check for Identity Assertion option, this should not be on by default
		isIdentityAssertion = "true".equalsIgnoreCase((String) options
				.get("IdentityAssertion"));

		String jdbcDriver = (String) options.get("JDBCDriver");
		String databaseURL = (String) options.get("DatabaseURL");
		String dbUser = (String) options.get("User");
		String dbPassword = (String) options.get("Password");

		database = new OASAuthenticatorDatabase(jdbcDriver, databaseURL,
				dbUser, dbPassword);
	}

	/**
	 *
	 * @return boolean
	 * @exception LoginException
	 * 
	 */
	public boolean login() throws LoginException {
		// only called (once!) after initialize

		// Print debugging information
		OASAuthLogger.getLogger().debug("DExLoginModuleImpl.login");
		System.out.println("DExLoginModuleImpl.login");
		// loginSucceeded should be false
		// principalsInSubject should be false
		// user should be null
		// group should be null

		Callback[] callbacks = getCallbacks();

		String userName = getUserName(callbacks);
		System.out.println("userName:"+userName);
		System.out.println("isIdentityAssertion:"+isIdentityAssertion);
		
		if (isIdentityAssertion) {
			if (userName.length() > 0) {
				if (database.userExists(userName) == false) {
					throwFailedLoginException("Authentication Failed: User "
							+ userName + " doesn't exist.");
				}
			}
			else {
				// Print debugging information
				OASAuthLogger.getLogger().debug("\tempty userName");
			}
		} else {
			// Print debugging information
			throw new FailedLoginException("Invalid username or password.");
		}
		System.out.println("before loginSucceeded:");
		loginSucceeded = true;
		principalsForSubject.add(new WLSUserImpl(userName));
		addGroupsForSubject(userName);
		System.out.println("loginSucceeded:"+loginSucceeded);
		return loginSucceeded;
	}

	/**
	 * @return boolean true
	 * @throws LoginException
	 *               
	 */
	public boolean logout() throws LoginException {
		// should never be called

		// Print debugging information
		OASAuthLogger.getLogger().debug("DExLoginModule.logout");

		return true;
	}

	/**
	 * Assigns a GroupsForSubject to the DExLoginModuleImpl.
	 *
	 * @param userName
	 *            a GroupsForSubject to be assigned.
	 */
	private void addGroupsForSubject(String userName) {
		for (Enumeration e = database.getUserGroups(userName); e
				.hasMoreElements();) {
			String groupName = (String) e.nextElement();

			// Print debugging information
			OASAuthLogger.getLogger().debug("\tadded groupName\t= " + groupName);

			principalsForSubject.add(new WLSGroupImpl(groupName));
		}
	}

	/**
	 * Returns the Callbacks property of the DExLoginModuleImpl.
	 *
	 * @return The Callbacks value.
	 * @exception LoginException
	 *                if the <description>.
	 */
	private Callback[] getCallbacks() throws LoginException {
		OASAuthLogger.getLogger().debug("getCallbacks()");
		if (callbackHandler == null) {
			throwLoginException("No CallbackHandler Specified");
		}

		if (database == null) {
			throwLoginException("database not specified");
		}

		Callback[] callbacks;
		if (isIdentityAssertion) {
			callbacks = new Callback[1];
		} else {
			callbacks = new Callback[2];
			callbacks[1] = new PasswordCallback("password: ", false);
		}
		callbacks[0] = new NameCallback("username: ");

		try {
			callbackHandler.handle(callbacks);
		} catch (IOException e) {
			throw new LoginException(e.toString());
		} catch (UnsupportedCallbackException e) {
			throwLoginException(e.toString() + " " + e.getCallback().toString());
		}

		return callbacks;
	}

	/**
	 * Returns the PasswordHave property of the DExLoginModuleImpl.
	 *
	 * @param userName
	 * @param callbacks
	 * @return The PasswordHave value.
	 * @exception LoginException
	 * 
	 */
	private String getPasswordHave(String userName, Callback[] callbacks)
			throws LoginException {
		PasswordCallback passwordCallback = (PasswordCallback) callbacks[1];
		char[] password = passwordCallback.getPassword();
		passwordCallback.clearPassword();
		if (password == null || password.length < 1) {
			throwLoginException("Authentication Failed: User " + userName
					+ ".  Password not supplied");
		}
		String passwd = new String(password);

		// Print debugging information
		OASAuthLogger.getLogger().debug("\tpasswordHave\t= " + passwd);

		return passwd;
	}

	/**
	 * Returns the UserName property of the DExLoginModuleImpl.
	 *
	 * @param callbacks
	 * @return The UserName value.
	 * @exception LoginException
	 * 
	 */
	private String getUserName(Callback[] callbacks) throws LoginException {
		String userName = ((NameCallback) callbacks[0]).getName();
		if (userName == null) {
			throwLoginException("Username not supplied.");
		}
		// Print debugging information
		OASAuthLogger.getLogger().debug("\tuserName\t= " + userName);

		return userName;
	}

	/**
	 *
	 * @param msg
	 * @exception FailedLoginException
	 * 
	 */
	private void throwFailedLoginException(String msg)
			throws FailedLoginException {
		// Print debugging information
		OASAuthLogger.getLogger().debug("Throwing FailedLoginException(" + msg + ")");

		throw new FailedLoginException(msg);
	}

	/**
	 *
	 * @param msg
	 * @exception LoginException
	 * 
	 */
	private void throwLoginException(String msg) throws LoginException {
		// Print debugging information
		OASAuthLogger.getLogger().debug("Throwing LoginException(" + msg + ")");

		throw new LoginException(msg);
	}
}
