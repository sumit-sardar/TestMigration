package com.ctb.tms.nosql.coherence;

import com.tangosol.net.cache.CacheStore;

public interface OASCacheStore extends CacheStore {
	
	public void storeAll(java.util.Set setBinEntries);

}
