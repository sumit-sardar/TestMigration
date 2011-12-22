package com.ctb.tms.nosql.coherence;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
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
	
	static ValueExtractor extractor;
	
	static Logger logger = Logger.getLogger(OASCoherenceSource.class);
	
	public OASCoherenceSource () {
		
	}
	
	static {
		try {
			rosterCache = CacheFactory.getCache("OASRosterCache"); 
			manifestCache = CacheFactory.getCache("OASManifestCache");
			responseCache = CacheFactory.getCache("OASResponseCache");
			
			extractor = new ReflectionPofExtractor("testRosterId"); 
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

	public ItemResponseData[] getItemResponses(String testRosterId) throws IOException, ClassNotFoundException {
		String key1 = testRosterId;
		//String key2 = String.valueOf((Integer.parseInt(testRosterId) + 1));
		Filter filter = new com.tangosol.util.filter.EqualsFilter(extractor, key1); 
		Set setKeys = responseCache.keySet(filter); 
		Map mapResult = responseCache.getAll(setKeys); 
		if(mapResult != null) {
			int size = mapResult.size();
			logger.debug("*****  Found " + size + " responses for roster " + testRosterId);
			ItemResponseData[] tsda = new ItemResponseData[size];
			Iterator it = mapResult.keySet().iterator();
			int i = 0;
			while(it.hasNext()) {
				ItemResponseData irw = (ItemResponseData) mapResult.get(it.next());
				irw.setTestRosterId(testRosterId);
				tsda[i] = irw;
				i++;
			}
			return tsda;
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
