package com.ctb.contentBridge.core.domain;

import java.util.ArrayList;

public class ItemSet {
	private long oasid;
	private long adsid;
	private String hash;
	private String key;

	private ArrayList children = new ArrayList();

	/**
	 * @return the oasid
	 */
	public long getOasid() {
		return oasid;
	}

	/**
	 * @param oasid
	 *            the oasid to set
	 */
	public void setOasid(long oasid) {
		this.oasid = oasid;
	}

	/**
	 * @return the adsid
	 */
	public long getAdsid() {
		return adsid;
	}

	/**
	 * @param adsid
	 *            the adsid to set
	 */
	public void setAdsid(long adsid) {
		this.adsid = adsid;
	}

	/**
	 * @return the hash
	 */
	public String getHash() {
		return hash;
	}

	/**
	 * @param hash
	 *            the hash to set
	 */
	public void setHash(String hash) {
		this.hash = hash;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the children
	 */
	public ArrayList getChildren() {
		return children;
	}

	/**
	 * @param children
	 *            the children to set
	 */
	public void addChildren(Item child) {
		this.children.add(child);
	}

	

}
