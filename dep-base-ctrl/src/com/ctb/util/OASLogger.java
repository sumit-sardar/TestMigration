package com.ctb.util;

import weblogic.logging.NonCatalogLogger;

/**
 * Creates a singleton Logger for use by all OAS code
 * @author Nate_Cohen
 */
public class OASLogger {
	private static NonCatalogLogger logger;
	
	public static NonCatalogLogger getLogger(String name) {
		if(logger == null) {
			synchronized(OASLogger.class) {
				if(logger == null) logger = new NonCatalogLogger(name);
			}
		}
		return logger;
	}
	
    public static void debugStackTrace(String name, StackTraceElement [] trace) {
        for(int i=0;i<trace.length;i++) {
            getLogger(name).debug("     " + trace[i].toString());
        }
    }
}
