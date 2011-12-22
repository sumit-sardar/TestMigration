package com.ctb.tms.bean.login;


public class ManifestWrapper extends ReplicationObject {

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
