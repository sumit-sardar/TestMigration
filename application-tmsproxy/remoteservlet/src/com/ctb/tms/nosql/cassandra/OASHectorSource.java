package com.ctb.tms.nosql.cassandra;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Iterator;

import me.prettyprint.cassandra.model.IndexedSlicesQuery;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.QueryResult;
import noNamespace.TmssvcResponseDocument;
import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd;

import org.apache.xmlbeans.XmlException;

import sun.misc.BASE64Decoder;

import com.ctb.tms.bean.login.AuthenticationData;
import com.ctb.tms.bean.login.Manifest;
import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;
import com.ctb.tms.nosql.OASNoSQLSource;

public class OASHectorSource implements OASNoSQLSource {
	
	private static Cluster cluster;
	
	public OASHectorSource () {
		
	}
	
	static {
		CassandraHostConfigurator chc = new CassandraHostConfigurator("localhost:9160");
		chc.setRetryDownedHosts(true);
		chc.setRetryDownedHostsDelayInSeconds(10);
		cluster = HFactory.getOrCreateCluster("OASCluster", chc);
	}	
	
	public RosterData getRosterData(StudentCredentials creds) throws XmlException, IOException, ClassNotFoundException {
		RosterData result = new RosterData();
		Keyspace keyspace = HFactory.createKeyspace("AuthData", cluster);
		String key = creds.getUsername() + ":" + creds.getPassword() + ":" + creds.getAccesscode();
		ColumnQuery<String, String, String> columnQuery = HFactory.createStringColumnQuery(keyspace);
		columnQuery.setColumnFamily("RosterData").setKey(key).setName("login-response");
		QueryResult<HColumn<String, String>> qr = columnQuery.execute();
		if(qr != null && qr.get() != null) {
			//logger.info("*****  Login: found roster data for " + key);
			String loginResponse = qr.get().getValue();
			result.setDocument(TmssvcResponseDocument.Factory.parse(loginResponse));
			columnQuery = HFactory.createStringColumnQuery(keyspace);
			columnQuery.setColumnFamily("RosterData").setKey(key).setName("auth-data");
			String authDataString = columnQuery.execute().get().getValue();
			byte [] bytes = new BASE64Decoder().decodeBuffer(authDataString);
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			AuthenticationData authData = (AuthenticationData) ois.readObject();
			result.setAuthData(authData);
		} else {
			//logger.info("*****  Login: no roster data found for " + key);
		}
		return result;
	}
	
	public Manifest getManifest(String testRosterId) throws XmlException, IOException, ClassNotFoundException {
		Manifest manifest = null;
		Keyspace keyspace = HFactory.createKeyspace("TestData", cluster);
		String key = testRosterId;
		ColumnQuery<String, String, String> columnQuery = HFactory.createStringColumnQuery(keyspace);
		columnQuery.setColumnFamily("ManifestData").setKey(key).setName("manifest");
		QueryResult<HColumn<String, String>> qr = columnQuery.execute();
		if(qr != null && qr.get() != null) {
			String manifestData = columnQuery.execute().get().getValue();
			byte [] bytes = new BASE64Decoder().decodeBuffer(manifestData);
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			manifest = (Manifest) ois.readObject();
		}
		return manifest;
	}

	public Tsd[] getItemResponses(String testRosterId) throws IOException, ClassNotFoundException {
		ArrayList<Tsd> resulta = new ArrayList<Tsd>();
		Keyspace keyspace = HFactory.createKeyspace("TestData", cluster);
		Serializer<String> stringSerializer = new StringSerializer();
		IndexedSlicesQuery<String, String, String> indexedSlicesQuery =
		HFactory.createIndexedSlicesQuery(keyspace, stringSerializer, stringSerializer, stringSerializer);
		indexedSlicesQuery.addEqualsExpression("roster-id", testRosterId);
		indexedSlicesQuery.setColumnNames("item-response");
		indexedSlicesQuery.setColumnFamily("ResponseData");
		indexedSlicesQuery.setStartKey("");
		QueryResult<OrderedRows<String, String, String>> result = indexedSlicesQuery.execute();
		Iterator<Row<String, String, String>> it = result.get().iterator();
        while(it.hasNext()) {
        	String tsdString = it.next().getColumnSlice().getColumnByName("item-response").getValue();
        	byte [] bytes = new BASE64Decoder().decodeBuffer(tsdString);
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			Tsd tsd = (Tsd) ois.readObject();
			resulta.add(tsd);
			//logger.info("##### Got response record for roster " + testRosterId + ": " + tsd.xmlText());
        }
        return resulta.toArray(new Tsd[0]);
	}
}
