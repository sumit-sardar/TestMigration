package com.ctb.tms.bean.login;

public class StudentCredentials {
	private String username;
	private String password;
	private String accesscode;
	
	private Long manifestHash;
	
	public Long getManifestHash() {
		return manifestHash;
	}
	public void setManifestHash(Long manifestHash) {
		this.manifestHash = manifestHash;
	}
	public String getUsername() {
		return username.toUpperCase();
	}
	public void setUsername(String username) {
		this.username = username.toUpperCase();
	}
	public String getPassword() {
		return password.toUpperCase();
	}
	public void setPassword(String password) {
		this.password = password.toUpperCase();
	}
	public String getAccesscode() {
		return accesscode.toUpperCase();
	}
	public void setAccesscode(String accesscode) {
		this.accesscode = accesscode.toUpperCase();
	}
	
}
