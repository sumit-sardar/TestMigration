package com.ctb.tms.nosql;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import sun.misc.BASE64Encoder;

import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;

public class OASHectorSink {

	private static Cluster cluster = HFactory.getOrCreateCluster("OASCluster", new CassandraHostConfigurator("localhost:9160"));
	
	{
		try {
			//cluster.dropKeyspace("OAS");
			//System.out.println("*****  Dropped OAS keyspace.");
		} catch (Exception e) {
			// do nothing, KS doesn't exist
		}
		
		try {
			BasicKeyspaceDefinition kd = new BasicKeyspaceDefinition() {
				public Map<String, String> getStrategyOptions() {
					Map<String, String> options = new HashMap<String, String>(2);
					options.put("replication_factor", "1");
					return options;
				}
				
				public String getStrategyClass() {
					return "org.apache.cassandra.locator.SimpleStrategy";
				}
				
				public int getReplicationFactor() {
					return 1;
				}
				
				public String getName() {
					return "OAS";
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
							return "OAS";
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
							return "OAS TMS Roster data";
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
			System.out.println(result.get().getValue());
			*/
			System.out.println("*****  Created OAS keyspace.");
		} catch (Exception e) {
			// do nothing, keyspace already exists
			//e.printStackTrace();
		}
	}
	
	public static void putRosterData(StudentCredentials creds, RosterData rosterData) throws IOException {
		Keyspace keyspace = HFactory.createKeyspace("OAS", cluster);
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
		//System.out.println("*****  Stored in Cassandra: " + result.get().getValue());
	}
}
