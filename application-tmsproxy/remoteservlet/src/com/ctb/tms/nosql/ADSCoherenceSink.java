package com.ctb.tms.nosql;

import java.io.IOException;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

public class ADSCoherenceSink implements ADSNoSQLSink {

	private static NamedCache itemCache;
	private static NamedCache itemSetCache;
	
	protected ADSCoherenceSink () {
		
	}
	
	static {
		try {
			itemCache = CacheFactory.getCache("ADSItemCache"); 
			itemSetCache = CacheFactory.getCache("ADSItemSetCache");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void putSubtest(int itemSetId, String hash, String xml) throws IOException {
		String key = itemSetId + ":" + hash;
		//String encXML = new BASE64Encoder().encode(xml.getBytes());
		itemSetCache.put(key, xml);
	}
	
	public void putItem(int itemId, String hash, String xml) throws IOException {
		String key = itemId + ":" + hash;
		//String encXML = new BASE64Encoder().encode(xml.getBytes());
		itemCache.put(key, xml);
	}
}
