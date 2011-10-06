package com.ctb.tms.nosql;

import java.io.IOException;

public interface ADSNoSQLSink {

	public void putSubtest(int itemSetId, String hash, String xml) throws IOException;
	
	public void putItem(int itemId, String hash, String xml) throws IOException;
}
