package com.ctb.itemmap.csv;

import java.util.Map;

public interface MappingProcessor {
	void processMapping(String mapping);
	Map getEntries();
}
