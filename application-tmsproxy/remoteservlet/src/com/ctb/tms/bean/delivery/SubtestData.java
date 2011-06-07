package com.ctb.tms.bean.delivery; 


public class SubtestData 
{
    private int subtestId;
    private String hash;
    private byte[] subtest;
    
    /**
	 * @return Returns the subtestId.
	 */
	public int getSubtestId() {
		return this.subtestId;
	}
	/**
	 * @param subtestId The subtestId to set.
	 */
	public void setSubtestId(int subtestId) {
		this.subtestId = subtestId;
	}

    /**
	 * @return Returns the hash.
	 */
	public String getHash() {
		return this.hash;
	}
	/**
	 * @param hash The hash to set.
	 */
	public void setHash(String hash) {
		this.hash = hash;
	}

    /**
	 * @return Returns the subtest.
	 */
	public byte[] getSubtest() {
		return this.subtest;
	}
	/**
	 * @param subtest The subtest to set.
	 */
	public void setSubtest(byte[] subtest) {
		this.subtest = subtest;
	}

 
} 
