package com.ctb.tms.bean.login;

import java.io.Serializable;

public class ManifestWrapper implements Serializable {

	private static final long serialVersionUID = 1L;

	private Manifest[] manifests;
	
	public ManifestWrapper () {
		
	}
	
	public ManifestWrapper (Manifest[] manifests) {
		this.manifests = manifests;
	}

	public Manifest[] getManifests() {
		return manifests;
	}

	public void setManifests(Manifest[] manifests) {
		this.manifests = manifests;
	}
}
