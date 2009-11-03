package com.ctb.util;

import com.bea.wlw.util.Logger;
import weblogic.logging.NonCatalogLogger;

/**
 * Creates a singleton Logger for use by all content previewer code
 * @author Nate_Cohen
 */
public class OASContentPreviewLogger {
	private static NonCatalogLogger logger;
	
	public static NonCatalogLogger getLogger() {
		if(logger == null) {
			synchronized(OASContentPreviewLogger.class) {
				if(logger == null) logger = new NonCatalogLogger("OASContentPreview");
			}
		}
		return logger;
	}
}
