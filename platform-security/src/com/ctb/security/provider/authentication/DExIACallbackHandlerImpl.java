package com.ctb.security.provider.authentication;

/**
* DExIACallbackHandlerImpl.java
* @author Tata Consultancy Services 
* This Class is created for implementing
* identity assertion  for DEx Application
*
*/

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

class DExIACallbackHandlerImpl implements CallbackHandler {
	private String userName;
	private String password;

	DExIACallbackHandlerImpl(String user, String pass) {
		userName = user;
		password = pass;
	}

	DExIACallbackHandlerImpl(String user) {
		userName = user;
	}
	
	/*
	 * @param callbacks
	 * @throws UnsupportedCallbackException
	 */
	public void handle(Callback[] callbacks)
			throws UnsupportedCallbackException {
		for (int i = 0; i < callbacks.length; i++) {
			Callback callback = callbacks[i];
			if (callback instanceof NameCallback) {
				NameCallback nameCallback = (NameCallback) callback;
				nameCallback.setName(userName);
			} else if (callback instanceof PasswordCallback) {
				PasswordCallback passwordCallback = (PasswordCallback) callback;
				passwordCallback.setPassword(password.toCharArray());
			} else {
				throw new UnsupportedCallbackException(callback, "Unrecognized Callback");
			}
		}
	}
}