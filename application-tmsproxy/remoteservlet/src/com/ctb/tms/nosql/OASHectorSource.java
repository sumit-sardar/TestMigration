package com.ctb.tms.nosql;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Iterator;

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
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import noNamespace.TmssvcResponseDocument;
import sun.misc.BASE64Decoder;

import com.bea.xml.XmlException;
import com.ctb.tms.bean.login.AuthenticationData;
import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;

public class OASHectorSource {
	
	private static Cluster cluster = HFactory.getOrCreateCluster("OASCluster", new CassandraHostConfigurator("localhost:9160"));
	
	public static RosterData getRosterData(StudentCredentials creds) throws XmlException, IOException, ClassNotFoundException {
		RosterData result = new RosterData();
		Keyspace keyspace = HFactory.createKeyspace("OAS", cluster);
		String key = creds.getUsername() + ":" + creds.getPassword() + ":" + creds.getAccesscode();
		ColumnQuery<String, String, String> columnQuery = HFactory.createStringColumnQuery(keyspace);
		columnQuery.setColumnFamily("RosterData").setKey(key).setName("login-response");
		QueryResult<HColumn<String, String>> qr = columnQuery.execute();
		if(qr != null && qr.get() != null) {
			//System.out.println("*****  Login: found roster data for " + key);
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
			//System.out.println("*****  Login: no roster data found for " + key);
		}
		return result;
	}

	

	public static String[] getItemResponses(String testRosterId) {
		ArrayList<String> resulta = new ArrayList<String>();
		Keyspace keyspace = HFactory.createKeyspace("Responses", cluster);
		String key = testRosterId;
		Serializer<String> stringSerializer = new StringSerializer();
		RangeSlicesQuery<String, String, String> rangeSlicesQuery = HFactory.createRangeSlicesQuery(keyspace, stringSerializer, stringSerializer, stringSerializer); 
        rangeSlicesQuery.setColumnFamily("ResponseData"); 
        rangeSlicesQuery.setKeys(key, key); 
        rangeSlicesQuery.setRange("", "", false, 2); 
        QueryResult<OrderedRows<String, String, String>> result = rangeSlicesQuery.execute(); 
        Iterator<Row<String, String, String>> it = result.get().iterator();
        while(it.hasNext()) {
        	resulta.add(it.next().getColumnSlice().getColumnByName("item-response").getValue());
        }
        return resulta.toArray(new String[0]);
	}
}
