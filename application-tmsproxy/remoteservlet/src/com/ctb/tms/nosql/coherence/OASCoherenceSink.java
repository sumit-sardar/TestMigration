package com.ctb.tms.nosql.coherence;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;

import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd;

import com.ctb.tms.bean.login.Manifest;
import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;
import com.ctb.tms.nosql.OASNoSQLSink;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

public class OASCoherenceSink implements OASNoSQLSink {

	private static NamedCache rosterCache;
	private static NamedCache manifestCache;
	private static NamedCache responseCache;
	
	public OASCoherenceSink () {
		
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
		/*ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(rosterData);
		byte [] bytes = baos.toByteArray();
		String data = new BASE64Encoder().encode(bytes);
		rosterCache.put(key, data); */
		rosterCache.put(key, rosterData);
	}
	
	public void putManifest(String testRosterId, String accessCode, Manifest manifest) throws IOException {
		String key = testRosterId;
		Manifest[] manifests = (Manifest[]) manifestCache.get(key);
		ArrayList newManifests = new ArrayList();
		boolean foundManifest = false;
		for(int i=0;i<manifests.length;i++) {
			if(accessCode.equals(manifests[i].getAccessCode())) {
				newManifests.add(manifest);
				foundManifest = true;
			} else {
				newManifests.add(manifests[i]);
			}
		}
		if(!foundManifest) {
			newManifests.add(manifest);
		}
		manifests = (Manifest[]) newManifests.toArray(new Manifest[0]);
		Timestamp latestStartTime = new Timestamp(0);
		String rosterStatus = "";
		boolean allcoManifest = true;
		for(int i=0;i<manifests.length;i++) {
			if(manifests[i].getRosterStartTime() == null) {
				manifests[i].setRosterStartTime(new Timestamp(1));
			}
			if(manifests[i].getRosterStartTime().after(latestStartTime)) {
				rosterStatus = manifest.getRosterCompletionStatus();
				latestStartTime = manifest.getRosterStartTime();
				if(!"CO".equals(rosterStatus)) {
					allcoManifest = false;
				} else if(!allcoManifest) {
					rosterStatus = "IS";
				}
			}
			manifests[i].setRosterCompletionStatus(rosterStatus);
		}
		manifestCache.put(key, manifests);
	}
	
	public void putAllManifests(String testRosterId, Manifest[] manifests) throws IOException {
		String key = testRosterId;
		manifestCache.put(key, manifests);
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
