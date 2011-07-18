package com.ctb.tms.nosql;

import java.io.IOException;
import java.util.ArrayList;

import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd;

import com.bea.xml.XmlException;
import com.ctb.tms.bean.login.Manifest;
import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

public class OASCoherenceSource implements OASNoSQLSource {
	
	private static NamedCache rosterCache;
	private static NamedCache manifestCache;
	private static NamedCache responseCache;
	
	protected OASCoherenceSource () {
		
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
	
	public RosterData getRosterData(StudentCredentials creds) throws XmlException, IOException, ClassNotFoundException {
		String key = creds.getUsername() + ":" + creds.getPassword() + ":" + creds.getAccesscode();
		return (RosterData) rosterCache.get(key);
	}
	
	public Manifest getManifest(String testRosterId) throws XmlException, IOException, ClassNotFoundException {
		String key = testRosterId;
		return (Manifest) manifestCache.get(key);
	}

	public Tsd[] getItemResponses(String testRosterId) throws IOException, ClassNotFoundException {
		String key = testRosterId;
		ArrayList<Tsd> responseA = (ArrayList<Tsd>) responseCache.get(key);
        return responseA.toArray(new Tsd[0]);
	}
}
