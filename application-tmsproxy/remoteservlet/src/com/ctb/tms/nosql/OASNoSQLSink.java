package com.ctb.tms.nosql;

import java.io.IOException;
import java.math.BigInteger;

import org.apache.xmlbeans.XmlException;

import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd;

import com.ctb.tms.bean.login.Manifest;
import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;

public interface OASNoSQLSink {

	public void putRosterData(StudentCredentials creds, RosterData rosterData) throws IOException;
	
	public void putManifest(String testRosterId, String accessCode, Manifest manifest) throws IOException;
	
	public void putAllManifests(String testRosterId, Manifest[] manifests) throws IOException ;
	
	public void putItemResponse(String testRosterId, Tsd tsd) throws IOException;
	
	public void deleteItemResponse(String testRosterId, BigInteger mseq) throws IOException;
	
	public void deleteAllManifests(String testRosterId) throws XmlException, IOException, ClassNotFoundException;

	public void deleteRosterData(StudentCredentials creds) throws IOException;
}
