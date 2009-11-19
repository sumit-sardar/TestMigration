package com.ctb.security.provider.authentication;

import weblogic.logging.NonCatalogLogger;

/**
 * Creates a singleton Logger for use by all security provider code
 * @author Nate_Cohen
 */
public class OASAuthLogger {
	private static NonCatalogLogger logger;
	
	public static NonCatalogLogger getLogger() {
		if(logger == null) {
			synchronized(OASAuthLogger.class) {
				if(logger == null) logger = new NonCatalogLogger("OASAuth");
			}
		}
		return logger;
	}
}
