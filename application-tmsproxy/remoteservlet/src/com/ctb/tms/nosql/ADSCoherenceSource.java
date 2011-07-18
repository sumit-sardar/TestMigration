package com.ctb.tms.nosql;

import java.io.IOException;

import com.bea.xml.XmlException;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

public class ADSCoherenceSource implements ADSNoSQLSource{
	
	private static NamedCache itemCache;
	private static NamedCache itemSetCache;
	
	protected ADSCoherenceSource () {
		
	}
	
	static {
		try {
			itemCache = CacheFactory.getCache("ADSItemCache"); 
			itemSetCache = CacheFactory.getCache("ADSItemSetCache");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getSubtest(int itemSetId, String hash) throws XmlException, IOException, ClassNotFoundException {
		String result = null;
		String key = itemSetId + ":" + hash;
		result = (String) itemSetCache.get(key);
		//byte [] bytes = new BASE64Decoder().decodeBuffer(result);
		//result = new String(bytes);
		return result;
	}
	
	public String getItem(int itemId, String hash) throws XmlException, IOException, ClassNotFoundException {
		String result = null;
		String key = itemId + ":" + hash;
		result = (String) itemCache.get(key);
		//byte [] bytes = new BASE64Decoder().decodeBuffer(result);
		//result = new String(bytes);
		return result;
	}

}
