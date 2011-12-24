package com.ctb.tms.nosql.coherence;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.gridkit.coherence.utils.pof.ReflectionPofExtractor;

import com.ctb.tms.bean.login.ItemResponseData;
import com.ctb.tms.bean.login.Manifest;
import com.ctb.tms.bean.login.ManifestWrapper;
import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;
import com.ctb.tms.nosql.OASNoSQLSource;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;

public class OASCoherenceSource implements OASNoSQLSource {
	
	private static NamedCache rosterCache;
	private static NamedCache manifestCache;
	private static NamedCache responseCache;
	
	static ValueExtractor extractor = new ReflectionPofExtractor("testRosterId"); 
	
	static Logger logger = Logger.getLogger(OASCoherenceSource.class);
	
	public OASCoherenceSource () {
		
	}
	
	static {
		try {
			rosterCache = CacheFactory.getCache("OASRosterCache"); 
			manifestCache = CacheFactory.getCache("OASManifestCache");
			responseCache = CacheFactory.getCache("OASResponseCache");

			responseCache.addIndex(extractor, false, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	public RosterData getRosterData(StudentCredentials creds) throws XmlException, IOException, ClassNotFoundException {
		String key = creds.getUsername() + ":" + creds.getPassword() + ":" + creds.getAccesscode();
		/*RosterData result = null;
		String data = (String) rosterCache.get(key);
		if(data != null) {
			byte [] bytes = new BASE64Decoder().decodeBuffer(data);
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			result = (RosterData) ois.readObject();
		}*/
		Object value = rosterCache.get(key);
		if(value != null) {
			RosterData result = (RosterData) value;
			return result; 
		} else {
			return null;
		}
	}
	
	public Manifest getManifest(String testRosterId, String accessCode) throws XmlException, IOException, ClassNotFoundException {
		String key = testRosterId;
		Object value = manifestCache.get(key);
		if(value != null ) {
			ManifestWrapper wrapper = (ManifestWrapper) value;
			Manifest[] manifests = wrapper.getManifests();
			for(int i=0;i<manifests.length;i++) {
				Manifest manifest = manifests[i];
				if(accessCode.equals(manifest.getAccessCode())) {
					return manifest;
				}
			}
		}
		return null;
	}

	public ItemResponseData[] getItemResponses(int testRosterId) throws IOException, ClassNotFoundException {
		Filter filter = new com.tangosol.util.filter.EqualsFilter(extractor, testRosterId); 
		Set setVals = responseCache.entrySet(filter); 
		if(setVals != null) {
			Iterator it = setVals.iterator();
			int size = setVals.size();
			//logger.info("\n*****  OASCoherenceSource: getItemResponses: Found " + size + " response keys for roster " + testRosterId);
			ItemResponseData[] irda = new ItemResponseData[size];
			int i = 0;
			while(it.hasNext()) {
				Entry entry = (Entry) it.next();
				ItemResponseData ird = (ItemResponseData) entry.getValue();
				ird.setTestRosterId(testRosterId);
				irda[i] = ird;
				logger.debug("\n*****  OASCoherenceSource: getItemResponses: Retrieved response from cache: " + ird.getTestRosterId() + ", seqnum: " + ird.getResponseSeqNum() + ", item type: " + ird.getItemType() + ", response type: " + ird.getResponseType() + ", elapsed time: " + ird.getResponseElapsedTime() + ", response: " + ird.getResponse() + ", CR response: " + ird.getConstructedResponse());
				i++;
			}
			//logger.info("\n*****  OASCoherenceSource: getItemResponses: Retrieved " + i + " responses for roster " + testRosterId);
			return irda;
		} else {
			return null;
		}
	}
	
	public ManifestWrapper getAllManifests(String testRosterId) throws IOException, ClassNotFoundException {
		String key = testRosterId;
		Object value = manifestCache.get(key);
		if(value != null) {
			ManifestWrapper wrapper = (ManifestWrapper) value;
			return wrapper;
		} else {
			return null;
		}
	}
}
