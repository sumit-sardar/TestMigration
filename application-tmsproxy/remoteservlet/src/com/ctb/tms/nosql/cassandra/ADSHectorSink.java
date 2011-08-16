package com.ctb.tms.nosql.cassandra;

import java.io.IOException;
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

import com.ctb.tms.nosql.ADSNoSQLSink;

public class ADSHectorSink implements ADSNoSQLSink {

	private static Cluster cluster;
	
	public ADSHectorSink () {
		
	}
	
	static {
		CassandraHostConfigurator chc = new CassandraHostConfigurator("localhost:9160");
		chc.setRetryDownedHosts(true);
		chc.setRetryDownedHostsDelayInSeconds(10);
		cluster = HFactory.getOrCreateCluster("OASCluster", chc);

		try {
			//cluster.dropKeyspace("ADS");
			//System.out.println("*****  Dropped ADS keyspace.");
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
					return "ADS";
				}
				
				public List<ColumnFamilyDefinition> getCfDefs() {
					BasicColumnFamilyDefinition subtestDataCF = new BasicColumnFamilyDefinition() {
					
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
							return "Subtests";
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
							return "ADS";
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
							return "org.apache.cassandra.db.marshal.BytesType";
						}
						
						public ComparatorType getComparatorType() {
							return ComparatorType.BYTESTYPE;
						}
						
						public String getComment() {
							return "ADS subtest data";
						}
						
						public ColumnType getColumnType() {
							return ColumnType.STANDARD;
						}
						
						public List<ColumnDefinition> getColumnMetadata() {
							ColumnDefinition subtestCD = new ColumnDefinition() {
								
								public String getValidationClass() {
									return "org.apache.cassandra.db.marshal.BytesType";
								}
								
								public ByteBuffer getName() {
									return ByteBuffer.wrap("subtestXML".getBytes());
								}
								
								public ColumnIndexType getIndexType() {
									return ColumnIndexType.KEYS;
								}
								
								public String getIndexName() {
									return "subtest-idx";
								}
							};
							List<ColumnDefinition> cdList = new ArrayList<ColumnDefinition>();
							cdList.add(subtestCD);
							return cdList;
						}
					};
					BasicColumnFamilyDefinition itemDataCF = new BasicColumnFamilyDefinition() {
						
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
							return "Items";
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
							return "ADS";
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
							return "org.apache.cassandra.db.marshal.BytesType";
						}
						
						public ComparatorType getComparatorType() {
							return ComparatorType.BYTESTYPE;
						}
						
						public String getComment() {
							return "ADS item data";
						}
						
						public ColumnType getColumnType() {
							return ColumnType.STANDARD;
						}
						
						public List<ColumnDefinition> getColumnMetadata() {
							ColumnDefinition subtestCD = new ColumnDefinition() {
								
								public String getValidationClass() {
									return "org.apache.cassandra.db.marshal.BytesType";
								}
								
								public ByteBuffer getName() {
									return ByteBuffer.wrap("itemXML".getBytes());
								}
								
								public ColumnIndexType getIndexType() {
									return ColumnIndexType.KEYS;
								}
								
								public String getIndexName() {
									return "item-idx";
								}
							};
							List<ColumnDefinition> cdList = new ArrayList<ColumnDefinition>();
							cdList.add(subtestCD);
							return cdList;
						}
					};
					List<ColumnFamilyDefinition> cfList = new ArrayList<ColumnFamilyDefinition>();
					cfList.add(new ThriftCfDef(subtestDataCF));
					cfList.add(new ThriftCfDef(itemDataCF));
					return cfList;
				}
			};
	
			
			cluster.addKeyspace(new ThriftKsDef(kd));

			System.out.println("*****  Created ADS keyspace.");
		} catch (Exception e) {
			// do nothing, keyspace already exists
			e.printStackTrace();
		}
	}
	
	public void putSubtest(int itemSetId, String hash, String xml) throws IOException {
		Keyspace keyspace = HFactory.createKeyspace("ADS", cluster);
		Serializer<String> stringSerializer = new StringSerializer();
		Mutator<String> mutator = HFactory.createMutator(keyspace, stringSerializer);
		String key = itemSetId + ":" + hash;
		//String encXML = new BASE64Encoder().encode(xml.getBytes());
		mutator.insert(key, "Subtests", HFactory.createStringColumn("subtestXML", xml));
	}
	
	public void putItem(int itemId, String hash, String xml) throws IOException {
		Keyspace keyspace = HFactory.createKeyspace("ADS", cluster);
		Serializer<String> stringSerializer = new StringSerializer();
		Mutator<String> mutator = HFactory.createMutator(keyspace, stringSerializer);
		String key = itemId + ":" + hash;
		//String encXML = new BASE64Encoder().encode(xml.getBytes());
		mutator.insert(key, "Items", HFactory.createStringColumn("itemXML", xml));
	}
}
