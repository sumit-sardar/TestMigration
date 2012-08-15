package com.ctb.tms.nosql.coherence;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;

import com.ctb.tms.bean.login.ItemResponseData;
import com.ctb.tms.bean.login.Manifest;
import com.ctb.tms.bean.login.ManifestData;
import com.ctb.tms.bean.login.ManifestWrapper;
import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;
import com.ctb.tms.nosql.OASNoSQLSink;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.extractor.ReflectionExtractor;

public class OASCoherenceSink implements OASNoSQLSink {

	private static NamedCache rosterCache;
	private static NamedCache manifestCache;
	private static NamedCache responseCache;
	
	static ValueExtractor extractor = new ReflectionExtractor("getTestRosterId"); 
	
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
	
	public void putRosterData(StudentCredentials creds, RosterData rosterData, boolean replicate) throws IOException {
		String key = creds.getUsername() + ":" + creds.getPassword() + ":" + creds.getAccesscode();
		//rosterData.setReplicate(replicate);
		rosterData.setCacheTime(System.currentTimeMillis());
		rosterCache.put(key, rosterData);
	}
	
	public void putManifest(String testRosterId, String accessCode, Manifest manifest, boolean replicate) throws IOException {
		String key = testRosterId;
		ManifestWrapper wrapper = (ManifestWrapper) manifestCache.get(key);
		ArrayList newManifests = new ArrayList();
		boolean foundManifest = false;
		Manifest[] manifests = null;
		if(wrapper != null) {
			manifests = wrapper.getManifests();
			for(int i=0;i<manifests.length;i++) {
				if(accessCode.equals(manifests[i].getAccessCode())) {
					newManifests.add(manifest);
					foundManifest = true;
				} else {
					newManifests.add(manifests[i]);
				}
			}
		} else {
			wrapper = new ManifestWrapper();
		}
		if(!foundManifest) {
			newManifests.add(manifest);
		}
		manifests = (Manifest[]) newManifests.toArray(new Manifest[0]);
		int latestMseq = -1;
		int restartCount = -1;
		int cid = 0;
		int randomSeed = 0;
		String rosterStatus = "";
		String tutorialTaken = null;
		String ttsSpeedStatus = "";
		boolean allcoManifest = true;
		long startTime = 0;
		long endTime = 0;
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
				cid = manifests[i].getRosterCorrelationId();
				logger.debug("found higher restart number " + restartCount + " on manifest " + manifests[i].getAccessCode());
			}
			if("TRUE".equals(manifests[i].getTutorialTaken())) {
				tutorialTaken = "TRUE";
			}
			if(manifests[i].getRandomDistractorSeed() != 0) {
				randomSeed = manifests[i].getRandomDistractorSeed();
			}
			if("IP".equals(manifest.getRosterCompletionStatus()) && !accessCode.equals(manifests[i].getAccessCode())) {
				ManifestData[] mda = manifests[i].getManifest();
				for(int n=0;n<mda.length;n++) {
					if("IP".equals(mda[n].getCompletionStatus())) {
						mda[n].setCompletionStatus("IN");
					}
				}
			}
			if(startTime == 0 && manifests[i].getRosterStartTime() != 0) {
				startTime = manifests[i].getRosterStartTime();
			} else if (manifests[i].getRosterStartTime() != 0 && manifests[i].getRosterStartTime() < startTime) {
				startTime = manifests[i].getRosterStartTime();
			}
			if(manifests[i].getRosterEndTime() > endTime) {
				endTime = manifests[i].getRosterEndTime();
			}
			if (manifests[i].getTtsSpeedStatus() != null){
				ttsSpeedStatus =  manifests[i].getTtsSpeedStatus();
			}
		}
		for(int i=0;i<manifests.length;i++) {
			manifests[i].setTutorialTaken(tutorialTaken);
			manifests[i].setRosterCompletionStatus(rosterStatus);
			manifests[i].setRosterRestartNumber(restartCount);
			manifests[i].setRosterLastMseq(latestMseq);
			manifests[i].setRosterCorrelationId(cid);
			manifests[i].setRandomDistractorSeed(randomSeed);
			manifests[i].setRosterStartTime(startTime);
			manifests[i].setRosterEndTime(endTime);
			manifests[i].setTtsSpeedStatus(ttsSpeedStatus);
		}
		wrapper.setManifests(manifests);
		if(wrapper != null && manifests != null && manifests.length > 0) {
			//wrapper.setReplicate(replicate);
			wrapper.setCacheTime(System.currentTimeMillis());
			manifestCache.put(key, wrapper);
		}
	}
	
	public void putAllManifests(String testRosterId, ManifestWrapper wrapper, boolean replicate) throws IOException {
		String key = testRosterId;
		if(wrapper != null && wrapper.getManifests() != null && wrapper.getManifests().length > 0) {
			Manifest[] manifests = wrapper.getManifests();
			int latestMseq = -1;
			int restartCount = -1;
			int cid = 0;
			int randomSeed = 0;
			long startTime = 0;
			long endTime = 0;
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
					cid = manifests[i].getRosterCorrelationId();
					logger.debug("found higher restart number " + restartCount + " on manifest " + manifests[i].getAccessCode());
				}
				if("TRUE".equals(manifests[i].getTutorialTaken())) {
					tutorialTaken = "TRUE";
				}
				if(manifests[i].getRandomDistractorSeed() != 0) {
					randomSeed = manifests[i].getRandomDistractorSeed();
				}
				if(startTime == 0 && manifests[i].getRosterStartTime() != 0) {
					startTime = manifests[i].getRosterStartTime();
				} else if (manifests[i].getRosterStartTime() != 0 && manifests[i].getRosterStartTime() < startTime) {
					startTime = manifests[i].getRosterStartTime();
				}
				if(manifests[i].getRosterEndTime() > endTime) {
					endTime = manifests[i].getRosterEndTime();
				}
			}
			for(int i=0;i<manifests.length;i++) {
				manifests[i].setTutorialTaken(tutorialTaken);
				manifests[i].setRosterCompletionStatus(rosterStatus);
				manifests[i].setRosterRestartNumber(restartCount);
				manifests[i].setRosterLastMseq(latestMseq);
				manifests[i].setRosterCorrelationId(cid);
				manifests[i].setRandomDistractorSeed(randomSeed);
				manifests[i].setRosterStartTime(startTime);
				manifests[i].setRosterEndTime(endTime);
			}
			wrapper.setManifests(manifests);
			//wrapper.setReplicate(replicate);
			wrapper.setCacheTime(System.currentTimeMillis());
			manifestCache.put(key, wrapper);
		}
	}
	
	public void putItemResponse(ItemResponseData ird, boolean replicate) throws IOException {
		String key = ird.getTestRosterId() + ":" + ird.getResponseSeqNum();
		//ird.setReplicate(replicate);
		ird.setCacheTime(System.currentTimeMillis());
		if("CR".equals(ird.getItemType()) && (ird.getConstructedResponse() == null || "".equals(ird.getConstructedResponse().trim()))) {
			ird.setConstructedResponse(ird.getResponse());
		}
		responseCache.put(key, ird);
		logger.debug("\n*****  OASCoherenceSink: putItemResponse: Stored response: " + key + ", item type: " + ird.getItemType() + ", response type: " + ird.getResponseType() + ", elapsed time: " + ird.getResponseElapsedTime() + ", response: " + ird.getResponse() + ", CR response: " + ird.getConstructedResponse());
	}
	
	public void deleteItemResponse(int testRosterId, BigInteger mseq) throws IOException {
		String key = String.valueOf(testRosterId) + ":" + mseq;
		responseCache.remove(key);
	}
	
	public void deleteAllItemResponses(int testRosterId) throws IOException {
		Filter filter = new com.tangosol.util.filter.EqualsFilter(extractor, testRosterId); 
		Set setKeys = responseCache.keySet(filter); 
		Iterator it = setKeys.iterator();
		while(it.hasNext()) {
			String key = (String) it.next();
			responseCache.remove(key);
		}
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
