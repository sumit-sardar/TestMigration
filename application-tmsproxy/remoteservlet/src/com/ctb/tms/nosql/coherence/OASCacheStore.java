package com.ctb.tms.nosql.coherence;

import com.tangosol.net.cache.CacheStore;
import com.tangosol.util.BinaryEntry;

public interface OASCacheStore extends CacheStore {
	
	public void storeAll(java.util.Set<BinaryEntry> setBinEntries);

}
