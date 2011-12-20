package com.ctb.tms.nosql;

import java.io.IOException;
import java.math.BigInteger;

import org.apache.xmlbeans.XmlException;

import com.ctb.tms.bean.login.ItemResponseData;
import com.ctb.tms.bean.login.Manifest;
import com.ctb.tms.bean.login.ManifestWrapper;
import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;

public interface OASNoSQLSink {

	public void putRosterData(StudentCredentials creds, RosterData rosterData) throws IOException;
	
	public void putManifest(String testRosterId, String accessCode, Manifest manifest) throws IOException;
	
	public void putAllManifests(String testRosterId, ManifestWrapper manifests) throws IOException ;
	
	public void putItemResponse(ItemResponseData ird) throws IOException;
	
	public void deleteItemResponse(String testRosterId, BigInteger mseq) throws IOException;
	
	public void deleteAllItemResponses(String testRosterId) throws IOException;
	
	public void deleteAllManifests(String testRosterId) throws XmlException, IOException, ClassNotFoundException;

	public void deleteRosterData(StudentCredentials creds) throws IOException;
}
