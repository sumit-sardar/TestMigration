package com.ctb.tms.nosql.coherence;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;

import com.ctb.tms.bean.login.ItemResponseWrapper;
import com.ctb.tms.bean.login.Manifest;
import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;
import com.ctb.tms.nosql.OASNoSQLSource;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.extractor.ReflectionExtractor;

public class OASCoherenceSource implements OASNoSQLSource {
	
	private static NamedCache rosterCache;
	private static NamedCache manifestCache;
	private static NamedCache responseCache;
	
	static Logger logger = Logger.getLogger(OASCoherenceSource.class);
	
	public OASCoherenceSource () {
		
	}
	
	static {
		try {
			rosterCache = CacheFactory.getCache("OASRosterCache"); 
			manifestCache = CacheFactory.getCache("OASManifestCache");
			responseCache = CacheFactory.getCache("OASResponseCache");
			
			ValueExtractor extractor = new ReflectionExtractor("getLsid"); 
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
		RosterData result = (RosterData) rosterCache.get(key);
		return result; 
	}
	
	public Manifest getManifest(String testRosterId, String accessCode) throws XmlException, IOException, ClassNotFoundException {
		/*String key = testRosterId + ":" + accessCode;
		return (Manifest) manifestCache.get(key); */
		String key = testRosterId;
		Manifest[] manifests = (Manifest[]) manifestCache.get(key);
		for(int i=0;i<manifests.length;i++) {
			Manifest manifest = manifests[i];
			if(accessCode.equals(manifest.getAccessCode())) {
				return manifest;
			}
		}
		return null;
	}

	public ItemResponseWrapper[] getItemResponses(String testRosterId) throws IOException, ClassNotFoundException {
		String key1 = testRosterId;
		String key2 = String.valueOf((Integer.parseInt(testRosterId) + 1));
		Filter filter = new com.tangosol.util.filter.BetweenFilter("getLsid", key1, key2); 
		Set setKeys = responseCache.keySet(filter); 
		Map mapResult = responseCache.getAll(setKeys); 
		if(mapResult != null) {
			int size = mapResult.size();
			logger.debug("*****  Found " + size + " responses for roster " + testRosterId);
			ItemResponseWrapper[] tsda = new ItemResponseWrapper[size];
			Iterator it = mapResult.keySet().iterator();
			int i = 0;
			while(it.hasNext()) {
				ItemResponseWrapper irw = (ItemResponseWrapper) mapResult.get(it.next());
				tsda[i] = irw;
				i++;
			}
			return tsda;
		} else {
			return null;
		}
	}
	
	public Manifest[] getAllManifests(String testRosterId) throws IOException, ClassNotFoundException {
		/* String key = testRosterId;
		Filter filter = new EqualsFilter("getTestRosterId", key); 
		Set setKeys = manifestCache.keySet(filter); 
		Map mapResult = manifestCache.getAll(setKeys); 
		if(mapResult != null) {
			int size = mapResult.size();
			logger.debug("*****  Found " + size + " manifests for roster " + testRosterId);
			Manifest[] manifesta = new Manifest[size];
			Iterator it = mapResult.keySet().iterator();
			int i = 0;
			while(it.hasNext()) {
				manifesta[i] = (Manifest) mapResult.get(it.next());
				i++;
			}
			return manifesta;
		} else {
			return null;
		} */
		String key = testRosterId;
		return (Manifest[]) manifestCache.get(key);
	}
}
