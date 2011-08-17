package com.ctb.tms.nosql.cassandra;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import me.prettyprint.cassandra.model.BasicColumnFamilyDefinition;
import me.prettyprint.cassandra.model.BasicKeyspaceDefinition;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.ThriftCfDef;
import me.prettyprint.cassandra.service.ThriftKsDef;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.ddl.ColumnDefinition;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ColumnIndexType;
import me.prettyprint.hector.api.ddl.ColumnType;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import noNamespace.AdssvcRequestDocument.AdssvcRequest.SaveTestingSessionData.Tsd;
import sun.misc.BASE64Encoder;

import com.ctb.tms.bean.login.Manifest;
import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;
import com.ctb.tms.nosql.OASNoSQLSink;

public class OASHectorSink implements OASNoSQLSink {

	private static Cluster cluster;
	static Logger logger = Logger.getLogger(OASHectorSink.class);
	
	public OASHectorSink () {
		
	}
	
	static {
		CassandraHostConfigurator chc = new CassandraHostConfigurator("localhost:9160");
		chc.setRetryDownedHosts(true);
		chc.setRetryDownedHostsDelayInSeconds(10);
		cluster = HFactory.getOrCreateCluster("OASCluster", chc);

		try {
			//cluster.dropKeyspace("AuthData");
			//cluster.dropKeyspace("TestData");
		} catch (Exception e) {
			// do nothing, KS doesn't exist
		}
		
		try {
			BasicKeyspaceDefinition kd = new BasicKeyspaceDefinition() {
				public Map<String, String> getStrategyOptions() {
					Map<String, String> options = new HashMap<String, String>(2);
					options.put("replication_factor", "2");
					return options;
				}
				
				public String getStrategyClass() {
					return "org.apache.cassandra.locator.SimpleStrategy";
				}
				
				public int getReplicationFactor() {
					return 1;
				}
				
				public String getName() {
					return "AuthData";
				}
				
				public List<ColumnFamilyDefinition> getCfDefs() {
					BasicColumnFamilyDefinition rosterDataCF = new BasicColumnFamilyDefinition() {
					
						public ComparatorType getSubComparatorType() {
							return null;
						}
						
						public double getRowCacheSize() {
							return 0;
						}
						
						public int getRowCacheSavePeriodInSeconds() {
							return 0;
						}
						
						public double getReadRepairChance() {
							return 0;
						}
						
						public String getName() {
							return "RosterData";
						}
						
						public int getMinCompactionThreshold() {
							return 4;
						}
						
						public int getMemtableThroughputInMb() {
							return 10;
						}
						
						public double getMemtableOperationsInMillions() {
							return 0.1;
						}
						
						public int getMemtableFlushAfterMins() {
							return 60;
						}
						
						public int getMaxCompactionThreshold() {
							return 32;
						}
						
						public String getKeyspaceName() {
							return "AuthData";
						}
						
						public double getKeyCacheSize() {
							return 0.01;
						}
						
						public int getKeyCacheSavePeriodInSeconds() {
							return 3600;
						}
						
						public int getId() {
							return 1;
						}
						
						public int getGcGraceSeconds() {
							return 0;
						}
						
						public String getDefaultValidationClass() {
							return "org.apache.cassandra.db.marshal.UTF8Type";
						}
						
						public ComparatorType getComparatorType() {
							return ComparatorType.UTF8TYPE;
						}
						
						public String getComment() {
							return "OAS TMS Auth data";
						}
						
						public ColumnType getColumnType() {
							return ColumnType.STANDARD;
						}
						
						public List<ColumnDefinition> getColumnMetadata() {
							ColumnDefinition loginResponseCD = new ColumnDefinition() {
								
								public String getValidationClass() {
									return "org.apache.cassandra.db.marshal.UTF8Type";
								}
								
								public ByteBuffer getName() {
									return ByteBuffer.wrap("login-response".getBytes());
								}
								
								public ColumnIndexType getIndexType() {
									return ColumnIndexType.KEYS;
								}
								
								public String getIndexName() {
									return "login-response-idx";
								}
							};
							ColumnDefinition authDataCD = new ColumnDefinition() {
								
								public String getValidationClass() {
									return "org.apache.cassandra.db.marshal.BytesType";
								}
								
								public ByteBuffer getName() {
									return ByteBuffer.wrap("auth-data".getBytes());
								}
								
								public ColumnIndexType getIndexType() {
									return ColumnIndexType.KEYS;
								}
								
								public String getIndexName() {
									return "auth-data-idx";
								}
							};
							List<ColumnDefinition> cdList = new ArrayList<ColumnDefinition>();
							cdList.add(loginResponseCD);
							cdList.add(authDataCD);
							return cdList;
						}
					};
					List<ColumnFamilyDefinition> cfList = new ArrayList<ColumnFamilyDefinition>();
					cfList.add(new ThriftCfDef(rosterDataCF));
					return cfList;
				}
			};
	
			cluster.addKeyspace(new ThriftKsDef(kd));
			/*
			Keyspace keyspace = HFactory.createKeyspace("OAS", cluster);
			Serializer<String> stringSerializer = new StringSerializer();
			Mutator<String> mutator = HFactory.createMutator(keyspace, stringSerializer);
			mutator.insert("jsmith", "RosterData", HFactory.createStringColumn("username", "jsmith"));
			
			ColumnQuery<String, String, String> columnQuery = HFactory.createStringColumnQuery(keyspace);
			columnQuery.setColumnFamily("RosterData").setKey("jsmith").setName("username");
			QueryResult<HColumn<String, String>> result = columnQuery.execute();
			logger.info(result.get().getValue());
			*/
			logger.info("*****  Created AuthData keyspace.");
		} catch (Exception e) {
			// do nothing, keyspace already exists
			e.printStackTrace();
		}
		
		try {
			BasicKeyspaceDefinition kd = new BasicKeyspaceDefinition() {
				public Map<String, String> getStrategyOptions() {
					Map<String, String> options = new HashMap<String, String>(2);
					options.put("replication_factor", "2");
					return options;
				}
				
				public String getStrategyClass() {
					return "org.apache.cassandra.locator.SimpleStrategy";
				}
				
				public int getReplicationFactor() {
					return 1;
				}
				
				public String getName() {
					return "TestData";
				}
				
				public List<ColumnFamilyDefinition> getCfDefs() {
					BasicColumnFamilyDefinition rosterDataCF = new BasicColumnFamilyDefinition() {
					
						public ComparatorType getSubComparatorType() {
							return null;
						}
						
						public double getRowCacheSize() {
							return 0;
						}
						
						public int getRowCacheSavePeriodInSeconds() {
							return 0;
						}
						
						public double getReadRepairChance() {
							return 0;
						}
						
						public String getName() {
							return "ResponseData";
						}
						
						public int getMinCompactionThreshold() {
							return 4;
						}
						
						public int getMemtableThroughputInMb() {
							return 10;
						}
						
						public double getMemtableOperationsInMillions() {
							return 0.1;
						}
						
						public int getMemtableFlushAfterMins() {
							return 60;
						}
						
						public int getMaxCompactionThreshold() {
							return 32;
						}
						
						public String getKeyspaceName() {
							return "TestData";
						}
						
						public double getKeyCacheSize() {
							return 0.01;
						}
						
						public int getKeyCacheSavePeriodInSeconds() {
							return 3600;
						}
						
						public int getId() {
							return 1;
						}
						
						public int getGcGraceSeconds() {
							return 0;
						}
						
						public String getDefaultValidationClass() {
							return "org.apache.cassandra.db.marshal.UTF8Type";
						}
						
						public ComparatorType getComparatorType() {
							return ComparatorType.UTF8TYPE;
						}
						
						public String getComment() {
							return "OAS TMS test data";
						}
						
						public ColumnType getColumnType() {
							return ColumnType.STANDARD;
						}
						
						public List<ColumnDefinition> getColumnMetadata() {
							ColumnDefinition itemIdCD = new ColumnDefinition() {
								
								public String getValidationClass() {
									return "org.apache.cassandra.db.marshal.UTF8Type";
								}
								
								public ByteBuffer getName() {
									return ByteBuffer.wrap("item-id".getBytes());
								}
								
								public ColumnIndexType getIndexType() {
									return ColumnIndexType.KEYS;
								}
								
								public String getIndexName() {
									return "item-id-idx";
								}
							};
							ColumnDefinition rosterIdCD = new ColumnDefinition() {
								
								public String getValidationClass() {
									return "org.apache.cassandra.db.marshal.UTF8Type";
								}
								
								public ByteBuffer getName() {
									return ByteBuffer.wrap("roster-id".getBytes());
								}
								
								public ColumnIndexType getIndexType() {
									return ColumnIndexType.KEYS;
								}
								
								public String getIndexName() {
									return "roster-id-idx";
								}
							};
							ColumnDefinition itemResponseCD = new ColumnDefinition() {
								
								public String getValidationClass() {
									return "org.apache.cassandra.db.marshal.UTF8Type";
								}
								
								public ByteBuffer getName() {
									return ByteBuffer.wrap("item-response".getBytes());
								}
								
								public ColumnIndexType getIndexType() {
									return ColumnIndexType.KEYS;
								}
								
								public String getIndexName() {
									return "item-response-idx";
								}
							};
							List<ColumnDefinition> cdList = new ArrayList<ColumnDefinition>();
							cdList.add(itemIdCD);
							cdList.add(rosterIdCD);
							cdList.add(itemResponseCD);
							return cdList;
						}
					};
					BasicColumnFamilyDefinition manifestDataCF = new BasicColumnFamilyDefinition() {
						
						public ComparatorType getSubComparatorType() {
							return null;
						}
						
						public double getRowCacheSize() {
							return 0;
						}
						
						public int getRowCacheSavePeriodInSeconds() {
							return 0;
						}
						
						public double getReadRepairChance() {
							return 0;
						}
						
						public String getName() {
							return "ManifestData";
						}
						
						public int getMinCompactionThreshold() {
							return 4;
						}
						
						public int getMemtableThroughputInMb() {
							return 10;
						}
						
						public double getMemtableOperationsInMillions() {
							return 0.1;
						}
						
						public int getMemtableFlushAfterMins() {
							return 60;
						}
						
						public int getMaxCompactionThreshold() {
							return 32;
						}
						
						public String getKeyspaceName() {
							return "TestData";
						}
						
						public double getKeyCacheSize() {
							return 0.01;
						}
						
						public int getKeyCacheSavePeriodInSeconds() {
							return 3600;
						}
						
						public int getId() {
							return 1;
						}
						
						public int getGcGraceSeconds() {
							return 0;
						}
						
						public String getDefaultValidationClass() {
							return "org.apache.cassandra.db.marshal.UTF8Type";
						}
						
						public ComparatorType getComparatorType() {
							return ComparatorType.UTF8TYPE;
						}
						
						public String getComment() {
							return "OAS TMS test data";
						}
						
						public ColumnType getColumnType() {
							return ColumnType.STANDARD;
						}
						
						public List<ColumnDefinition> getColumnMetadata() {
							ColumnDefinition rosterIdCD = new ColumnDefinition() {
								
								public String getValidationClass() {
									return "org.apache.cassandra.db.marshal.UTF8Type";
								}
								
								public ByteBuffer getName() {
									return ByteBuffer.wrap("roster-id".getBytes());
								}
								
								public ColumnIndexType getIndexType() {
									return ColumnIndexType.KEYS;
								}
								
								public String getIndexName() {
									return "roster-id-idx";
								}
							};
							ColumnDefinition manifestCD = new ColumnDefinition() {
								
								public String getValidationClass() {
									return "org.apache.cassandra.db.marshal.UTF8Type";
								}
								
								public ByteBuffer getName() {
									return ByteBuffer.wrap("manifest".getBytes());
								}
								
								public ColumnIndexType getIndexType() {
									return ColumnIndexType.KEYS;
								}
								
								public String getIndexName() {
									return "manifest-idx";
								}
							};
							List<ColumnDefinition> cdList = new ArrayList<ColumnDefinition>();
							cdList.add(rosterIdCD);
							cdList.add(manifestCD);
							return cdList;
						}
					};
					List<ColumnFamilyDefinition> cfList = new ArrayList<ColumnFamilyDefinition>();
					cfList.add(new ThriftCfDef(rosterDataCF));
					cfList.add(new ThriftCfDef(manifestDataCF));
					return cfList;
				}
			};
	
			cluster.addKeyspace(new ThriftKsDef(kd));
			logger.info("*****  Created TestData keyspace.");
		} catch (Exception e) {
			// do nothing, keyspace already exists
			e.printStackTrace();
		}
	}
	
	public void putRosterData(StudentCredentials creds, RosterData rosterData) throws IOException {
		Keyspace keyspace = HFactory.createKeyspace("AuthData", cluster);
		Serializer<String> stringSerializer = new StringSerializer();
		Mutator<String> mutator = HFactory.createMutator(keyspace, stringSerializer);
		String key = creds.getUsername() + ":" + creds.getPassword() + ":" + creds.getAccesscode();
		mutator.insert(key, "RosterData", HFactory.createStringColumn("login-response", rosterData.getDocument().xmlText()));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(rosterData.getAuthData());
		byte [] bytes = baos.toByteArray();
		String authData = new BASE64Encoder().encode(bytes);
		mutator.insert(key, "RosterData", HFactory.createStringColumn("auth-data", authData));
		
		//ColumnQuery<String, String, String> columnQuery = HFactory.createStringColumnQuery(keyspace);
		//columnQuery.setColumnFamily("RosterData").setKey(key).setName("login-response");
		//QueryResult<HColumn<String, String>> result = columnQuery.execute();
		//logger.info("*****  Stored in Cassandra: " + result.get().getValue());
	}
	
	public void putManifestData(String testRosterId, Manifest manifest) throws IOException {
		Keyspace keyspace = HFactory.createKeyspace("TestData", cluster);
		Serializer<String> stringSerializer = new StringSerializer();
		Mutator<String> mutator = HFactory.createMutator(keyspace, stringSerializer);
		String key = testRosterId;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(manifest);
		byte [] bytes = baos.toByteArray();
		String manifestData = new BASE64Encoder().encode(bytes);
		mutator.insert(key, "ManifestData", HFactory.createStringColumn("manifest", manifestData));
	}
	
	public void putItemResponse(String testRosterId, Tsd tsd) throws IOException {
		Keyspace keyspace = HFactory.createKeyspace("TestData", cluster);
		Serializer<String> stringSerializer = new StringSerializer();
		Mutator<String> mutator = HFactory.createMutator(keyspace, stringSerializer);
		String key = testRosterId + ":" + tsd.getMseq();
		mutator.insert(key, "ResponseData", HFactory.createStringColumn("item-id", tsd.getIstArray(0).getIid()));
		mutator.insert(key, "ResponseData", HFactory.createStringColumn("roster-id", testRosterId));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(tsd);
		byte [] bytes = baos.toByteArray();
		String tsdData = new BASE64Encoder().encode(bytes);
		mutator.insert(key, "ResponseData", HFactory.createStringColumn("item-response", tsdData));
		//logger.info("##### Stored response record for key " + key + ": " + tsd.xmlText());
	}
	
	public void deleteItemResponse(String testRosterId, BigInteger mseq) throws IOException {
		Keyspace keyspace = HFactory.createKeyspace("TestData", cluster);
		Serializer<String> stringSerializer = new StringSerializer();
		Mutator<String> mutator = HFactory.createMutator(keyspace, stringSerializer);
		String key = testRosterId + ":" + mseq;
		mutator.delete(key, "ResponseData", "item-response", stringSerializer);
		mutator.delete(key, "ResponseData", "item-id", stringSerializer);
		mutator.delete(key, "ResponseData", "roster-id", stringSerializer);
		//logger.info("##### Deleted response record for key " + key);
	}
}
