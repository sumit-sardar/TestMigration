package com.ctb.tms.nosql.coherence;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;

import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd;

import com.ctb.tms.bean.login.ItemResponseWrapper;
import com.ctb.tms.bean.login.Manifest;
import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;
import com.ctb.tms.nosql.OASNoSQLSink;
import com.ctb.tms.web.servlet.TMSServlet;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

public class OASCoherenceSink implements OASNoSQLSink {

	private static NamedCache rosterCache;
	private static NamedCache manifestCache;
	private static NamedCache responseCache;
	
	static Logger logger = Logger.getLogger(OASCoherenceSink.class);
	
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
		int latestMseq = -1;
		int restartCount = 0;
		String rosterStatus = "";
		String tutorialTaken = null;
		boolean allcoManifest = true;
		for(int i=0;i<manifests.length;i++) {
			logger.debug("comparing " + manifests[i].getRosterLastMseq() + " to " + latestMseq);
			if(manifests[i].getRosterLastMseq() > latestMseq) {
				rosterStatus = manifests[i].getRosterCompletionStatus();
				latestMseq = manifests[i].getRosterLastMseq();
				if(!"CO".equals(rosterStatus)) {
					allcoManifest = false;
				} else if(!allcoManifest) {
					rosterStatus = "IS";
				}
			}
			if(manifests[i].getRosterRestartNumber() > restartCount) {
				restartCount = manifests[i].getRosterRestartNumber();
				logger.debug("found higher restart number " + restartCount + " on manifest " + manifests[i].getAccessCode());
			}
			if("TRUE".equals(manifests[i].getTutorialTaken())) {
				tutorialTaken = "TRUE";
			}
		}
		for(int i=0;i<manifests.length;i++) {
			manifests[i].setTutorialTaken(tutorialTaken);
			manifests[i].setRosterCompletionStatus(rosterStatus);
			manifests[i].setRosterRestartNumber(restartCount);
			manifests[i].setRosterLastMseq(latestMseq);
		}
		manifestCache.put(key, manifests);
	}
	
	public void putAllManifests(String testRosterId, Manifest[] manifests) throws IOException {
		String key = testRosterId;
		manifestCache.put(key, manifests);
	}
	
	public void putItemResponse(String testRosterId, ItemResponseWrapper irw) throws IOException {
		String key = testRosterId + ":" + irw.getTsd().getMseq();
		responseCache.put(key, irw);
	}
	
	public void deleteItemResponse(String testRosterId, BigInteger mseq) throws IOException {
		String key = testRosterId + ":" + mseq;
		responseCache.remove(key);
	}
	
	public void deleteAllManifests(String testRosterId) throws XmlException, IOException, ClassNotFoundException {
		String key = testRosterId;
		manifestCache.remove(key);
	}

	public void deleteRosterData(StudentCredentials creds) throws IOException {
		String key = creds.getUsername() + ":" + creds.getPassword() + ":" + creds.getAccesscode();
		rosterCache.remove(key);
	}
}
