package com.ctb.tms.nosql;

import java.io.IOException;

import sun.misc.BASE64Decoder;

import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.QueryResult;

import com.bea.xml.XmlException;

public interface ADSNoSQLSource {
	
	public String getSubtest(int itemSetId, String hash) throws XmlException, IOException, ClassNotFoundException;
	
	public String getItem(int itemId, String hash) throws XmlException, IOException, ClassNotFoundException;

}
