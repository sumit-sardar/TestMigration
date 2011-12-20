package com.ctb.tms.nosql;

import java.io.IOException;

import org.apache.xmlbeans.XmlException;

import com.ctb.tms.bean.login.ItemResponseData;
import com.ctb.tms.bean.login.Manifest;
import com.ctb.tms.bean.login.ManifestWrapper;
import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;

public interface OASNoSQLSource {
	
	public RosterData getRosterData(StudentCredentials creds) throws XmlException, IOException, ClassNotFoundException;
	
	public Manifest getManifest(String testRosterId, String accessCode) throws XmlException, IOException, ClassNotFoundException;
	
	public ItemResponseData[] getItemResponses(String testRosterId) throws IOException, ClassNotFoundException;
	
	public ManifestWrapper getAllManifests(String testRosterId) throws IOException, ClassNotFoundException;
}
