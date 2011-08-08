package com.ctb.tms.nosql;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;

import sun.misc.BASE64Encoder;

import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd;

import com.ctb.tms.bean.login.Manifest;
import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

public class OASCoherenceSink implements OASNoSQLSink {

	private static NamedCache rosterCache;
	private static NamedCache manifestCache;
	private static NamedCache responseCache;
	
	protected OASCoherenceSink () {
		
	}
	
	static {
		try {
			rosterCache = CacheFactory.getCache("OASRosterCache"); 
			manifestCache = CacheFactory.getCache("OASManifestCache");
			responseCache = CacheFactory.getCache("OASResponseCache");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void putRosterData(StudentCredentials creds, RosterData rosterData) throws IOException {
		String key = creds.getUsername() + ":" + creds.getPassword() + ":" + creds.getAccesscode();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(rosterData);
		byte [] bytes = baos.toByteArray();
		String data = new BASE64Encoder().encode(bytes);
		rosterCache.put(key, data);
	}
	
	public void putManifestData(String testRosterId, Manifest manifest) throws IOException {
		String key = testRosterId;
		manifestCache.put(key, manifest);
	}
	
	public void putItemResponse(String testRosterId, Tsd tsd) throws IOException {
		String key = testRosterId + ":" + tsd.getMseq();
		responseCache.put(key, tsd);
	}
	
	public void deleteItemResponse(String testRosterId, BigInteger mseq) throws IOException {
		String key = testRosterId + ":" + mseq;
		responseCache.remove(key);
	}
}
