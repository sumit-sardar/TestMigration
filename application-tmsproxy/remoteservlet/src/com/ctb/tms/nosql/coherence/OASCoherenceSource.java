package com.ctb.tms.nosql.coherence;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;

import com.bea.xml.XmlException;
import com.ctb.tms.bean.login.Manifest;
import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;
import com.ctb.tms.nosql.OASNoSQLSource;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.extractor.ReflectionExtractor;
import com.tangosol.util.filter.ContainsFilter;

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
	
	public Manifest getManifest(String testRosterId) throws XmlException, IOException, ClassNotFoundException {
		String key = testRosterId;
		return (Manifest) manifestCache.get(key);
	}

	public Tsd[] getItemResponses(String testRosterId) throws IOException, ClassNotFoundException {
		String key = testRosterId;
		Filter filter = new ContainsFilter("getLsid", key); 
		Set setKeys = responseCache.keySet(filter); 
		Map mapResult = responseCache.getAll(setKeys); 
		if(mapResult != null) {
			int size = mapResult.size();
			logger.info("*****  Found " + size + " responses for roster " + testRosterId);
			Tsd[] tsda = new Tsd[size];
			Iterator it = mapResult.keySet().iterator();
			int i = 0;
			while(it.hasNext()) {
				tsda[i] = (Tsd) mapResult.get(it.next());
				i++;
			}
			return tsda;
		} else {
			return null;
		}
	}
}
