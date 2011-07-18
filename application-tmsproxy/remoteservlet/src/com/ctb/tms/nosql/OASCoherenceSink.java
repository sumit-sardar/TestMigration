package com.ctb.tms.nosql;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

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
		rosterCache.put(key, rosterData);
	}
	
	public void putManifestData(String testRosterId, Manifest manifest) throws IOException {
		String key = testRosterId;
		manifestCache.put(key, manifest);
	}
	
	public void putItemResponse(String testRosterId, Tsd tsd) throws IOException {
		String key = testRosterId;
		ArrayList<Tsd> responseA = (ArrayList<Tsd>) responseCache.get(key);
		if(responseA == null) responseA = new ArrayList<Tsd>();
		responseA.add(tsd);
		responseCache.put(key, responseA);
	}
	
	public void deleteItemResponse(String testRosterId, BigInteger mseq) throws IOException {
		String key = testRosterId;
		ArrayList<Tsd> responseA = (ArrayList<Tsd>) responseCache.get(key);
		ArrayList<Tsd> newResponseA = new ArrayList<Tsd>();
		Iterator<Tsd> it = responseA.iterator();
		while(it.hasNext()) {
			Tsd tsd = it.next();
			if(!tsd.getMseq().equals(mseq)) {
				newResponseA.add(tsd);
			}
		}
		responseCache.put(key, newResponseA);
	}
}
