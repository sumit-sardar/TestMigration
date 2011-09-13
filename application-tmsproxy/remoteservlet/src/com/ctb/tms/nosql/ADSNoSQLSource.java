package com.ctb.tms.nosql;

import java.io.IOException;

import org.apache.xmlbeans.XmlException;

public interface ADSNoSQLSource {
	
	public String getSubtest(int itemSetId, String hash) throws XmlException, IOException, ClassNotFoundException;
	
	public String getItem(int itemId, String hash) throws XmlException, IOException, ClassNotFoundException;

}
