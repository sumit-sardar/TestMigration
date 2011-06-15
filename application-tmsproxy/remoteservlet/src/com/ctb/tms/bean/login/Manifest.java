package com.ctb.tms.bean.login;

import java.io.Serializable;

public class Manifest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ManifestData[] manifest;

	public ManifestData[] getManifest() {
		return manifest;
	}

	public void setManifest(ManifestData[] manifest) {
		this.manifest = manifest;
	}
	
	
}
