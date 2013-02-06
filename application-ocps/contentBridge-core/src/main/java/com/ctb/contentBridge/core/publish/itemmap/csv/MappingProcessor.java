package com.ctb.contentBridge.core.publish.itemmap.csv;

import java.util.Map;

public interface MappingProcessor {
	void processMapping(String mapping);
	Map getEntries();
}
