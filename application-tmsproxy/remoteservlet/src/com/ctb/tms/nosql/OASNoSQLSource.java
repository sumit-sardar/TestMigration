package com.ctb.tms.nosql;

import java.io.IOException;

import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd;

import com.bea.xml.XmlException;
import com.ctb.tms.bean.login.Manifest;
import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;

public interface OASNoSQLSource {
	
	public RosterData getRosterData(StudentCredentials creds) throws XmlException, IOException, ClassNotFoundException;
	
	public Manifest getManifest(String testRosterId) throws XmlException, IOException, ClassNotFoundException;

	public Tsd[] getItemResponses(String testRosterId) throws IOException, ClassNotFoundException;
}
