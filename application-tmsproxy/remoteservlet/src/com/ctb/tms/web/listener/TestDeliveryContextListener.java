package com.ctb.tms.web.listener;

import java.sql.Connection;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;

import com.ctb.tms.bean.login.Manifest;
import com.ctb.tms.bean.login.ManifestWrapper;
import com.ctb.tms.bean.login.RosterData;
import com.ctb.tms.bean.login.StudentCredentials;
import com.ctb.tms.nosql.NoSQLStorageFactory;
import com.ctb.tms.nosql.OASNoSQLSink;
import com.ctb.tms.nosql.OASNoSQLSource;
import com.ctb.tms.rdb.OASRDBSink;
import com.ctb.tms.rdb.OASRDBSource;
import com.ctb.tms.rdb.RDBStorageFactory;
import com.ctb.tms.util.JMSUtils;
import com.tangosol.net.Cluster;
import com.tangosol.net.Member;

public class TestDeliveryContextListener implements
		javax.servlet.ServletContextListener {

	public static final int batchSize = 10000;

	private static int checkFrequency = 120; // was default to 30
//	private static int postFrequency = 5;
	private static RosterThread rosterThread;
	private static ScoringThread scoringThread;
	private static ConcurrentLinkedQueue<ScoringMessage> rosterQueue;
	private static Cluster cluster;
	public static String clusterName;
	static Logger logger = Logger.getLogger(TestDeliveryContextListener.class);

    private static int PREPOP_THREAD_COUNT = 3; //default to 3
    static {
    	try {
    		PREPOP_THREAD_COUNT = Integer.valueOf(System.getProperty("PREPOP_THREAD_COUNT", "3")).intValue();
    	}
    	catch (NumberFormatException nfe) {
    		logger.error("Incorrect system property PREPOP_THREAD_COUNT: "+nfe.getMessage());    		
    	}
    	logger.info("PREPOP_THREAD_COUNT="+PREPOP_THREAD_COUNT);

    	try {
    		checkFrequency = Integer.valueOf(System.getProperty("PREPOP_INTERVAL", "120")).intValue();
    	}
    	catch (NumberFormatException nfe) {
    		logger.error("Incorrect system property PREPOP_INTERVAL: "+nfe.getMessage());    		
    	}
    	logger.info("checkFrequency="+checkFrequency);
    
    }	
	
	OASRDBSource oasDBSource = RDBStorageFactory.getOASSource();
	OASRDBSink oasDBSink = RDBStorageFactory.getOASSink();

	public static class ScoringMessage {
		private long timestamp;
		private String testRosterId;

		public ScoringMessage(long timestamp, String testRosterId) {
			this.timestamp = timestamp;
			this.testRosterId = testRosterId;
		}

		public long getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}

		public String getTestRosterId() {
			return testRosterId;
		}

		public void setTestRosterId(String testRosterId) {
			this.testRosterId = testRosterId;
		}
	}

	public static void enqueueRoster(ScoringMessage message) {
		rosterQueue.add(message);
	}

	public void contextDestroyed(ServletContextEvent sce) {
		oasDBSource.shutdown();
		oasDBSink.shutdown();
		TestDeliveryContextListener.rosterThread.stop();
		TestDeliveryContextListener.scoringThread.stop();
	}

	private static RosterThread getRosterThread(OASNoSQLSource oasSource,
			OASNoSQLSink oasSink, OASRDBSource oasDBSource, OASRDBSink oasDBSink) {
		// synchronized (TestDeliveryContextListener.class) {
		if (rosterThread == null) {
			rosterThread = new RosterThread(oasSource, oasSink, oasDBSource,
					oasDBSink);
		}
		// }
		return rosterThread;
	}

	private static ScoringThread getScoringThread() {
		// synchronized (TestDeliveryContextListener.class) {
		if (scoringThread == null) {
			scoringThread = new ScoringThread();
		}
		// }
		return scoringThread;
	}

	public void contextInitialized(ServletContextEvent sce) {
		logger.info("*****  Context Listener Startup");
		try {
			OASNoSQLSource oasSource = NoSQLStorageFactory.getOASSource();
			OASNoSQLSink oasSink = NoSQLStorageFactory.getOASSink();

			cluster = com.tangosol.net.CacheFactory.ensureCluster();
			clusterName = cluster.getClusterName();

			logger.info("*****  Starting active roster check background thread . . .");
			TestDeliveryContextListener.rosterThread = getRosterThread(
					oasSource, oasSink, oasDBSource, oasDBSink);
			TestDeliveryContextListener.rosterThread.start();
			logger.info(" started.");

			logger.info("*****  Starting scoring queue thread . . .");
			TestDeliveryContextListener.rosterQueue = new ConcurrentLinkedQueue<ScoringMessage>();
			TestDeliveryContextListener.scoringThread = getScoringThread();
			TestDeliveryContextListener.scoringThread.start();
			logger.info(" started.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static class RosterThread extends Thread {

		OASNoSQLSource oasSource;
		OASNoSQLSink oasSink;

		OASRDBSource oasDBSource;
		OASRDBSink oasDBSink;

		public RosterThread(OASNoSQLSource oasSource, OASNoSQLSink oasSink,
				OASRDBSource oasDBSource, OASRDBSink oasDBSink) {
			this.oasSource = oasSource;
			this.oasSink = oasSink;

			this.oasDBSource = oasDBSource;
			this.oasDBSink = oasDBSink;
		}

		public void run() {
			Connection conn = null;
			// Connection sinkConn = null;
			while (true) {
				int fetchedCount = 0;
				try {
					conn = oasDBSource.getOASConnection();
					fetchedCount = 0;

					Member localMember = cluster.getLocalMember();
					int nodeId = Integer.parseInt(localMember.getMemberName());
					
					//use this as cluster id
					int clusterId=1; //1 as default
			    	try {
			    		clusterId = Integer.valueOf(System.getProperty("TMS_CLUSTER_ID", "1")).intValue();
			    	}
			    	catch (NumberFormatException nfe) {
			    		logger.error("Incorrect system property TMS_CLUSTER_ID: "+nfe.getMessage());    		
			    	}
			    	logger.debug("TMS_CLUSTER_ID="+clusterId);					

					oasDBSource.markActiveRosters(conn, clusterName, clusterId, nodeId);
					StudentCredentials[] creds = oasDBSource.getActiveRosters(
							conn, clusterName, clusterId, nodeId);


					PrepopulateResult resultCounts = new PrepopulateResult();

					if ((creds != null) && (creds.length > 0)) {
						ThreadPoolExecutor executor = new ThreadPoolExecutor(
								PREPOP_THREAD_COUNT, PREPOP_THREAD_COUNT, 10, TimeUnit.SECONDS,
								new LinkedBlockingQueue<Runnable>());

						fetchedCount = creds.length;

						
			            List<Future> futureList = new LinkedList<Future>();

						for (int i = 0; i < creds.length; i++) {
//							executor.execute(new PrepopulateRunner( resultCounts, creds[i]));
		                    Future f = add2ThreadPool(executor, resultCounts, creds[i]);
		                    futureList.add(f);
						}

		                rendezVousPoint(executor,futureList,resultCounts);
						
						// join the thread pool back when it's done executing
						executor.shutdown();
					}

					if (resultCounts.errorCount > 0) {
						logger.warn("Failed to store data in cache for "
								+ resultCounts.errorCount
								+ " rosters! Last exception: "
								+ resultCounts.lastError.getMessage());
					}
					logger.info("clusterId="+clusterId +" nodeId="+nodeId +" - Stored data in cache for "
							+ resultCounts.storedCount + " rosters.");
					creds = null;
					oasDBSource.sweepActiveRosters(conn, clusterName, clusterId, nodeId);
					localMember = null;
				} catch (Exception e) {
					logger.error(
							"Caught Exception during active roster check.", e);
					e.printStackTrace();
				} finally {
					try {
						if (conn != null) {
							conn.close();
						}
						/*
						 * if(sinkConn != null) { sinkConn.close(); }
						 */
						if (fetchedCount == batchSize) {
							int sleepSeconds = (TestDeliveryContextListener.checkFrequency / 10);
							logger.info("Fetched full batch. Sleeping for "
									+ sleepSeconds + " seconds.");
							Thread.sleep(sleepSeconds * 1000);
						} else {
							logger.info("*****  Completed active roster check. Sleeping for "
									+ checkFrequency + " seconds.");
							Thread.sleep(TestDeliveryContextListener.checkFrequency * 1000);
						}
					} catch (Exception ie) {
						// do nothing
					}
				}
			}
		}
		
	    private Future add2ThreadPool(ThreadPoolExecutor executor, PrepopulateResult result, StudentCredentials cred) {
	        boolean tryAgain = true;
	        Future future=null;
	        while (tryAgain) {
	            try {
	                future=executor.submit(new PrepopulateRunner( result, cred));
	               
	                tryAgain = false;
	            } catch (RejectedExecutionException x) {
	                // This exception is being thrown because the thread pool is full. In this case,
	                // we simply want to wait, and try again.
	                try {
	                    Thread.sleep(30);
	                } catch (InterruptedException ex) {
	                    logger.fatal(ex);
	                }
	            }
	        }
	        return future;
	    }

	    private void rendezVousPoint(ThreadPoolExecutor executor, List fList, PrepopulateResult result) {
	        try {
	            while (!executor.getQueue().isEmpty() && executor.getActiveCount() != 0) {
	                // This is going to cause a spin lock.  We could optimize based on size?
	                int size = executor.getQueue().size();
	                int sleep = size / PREPOP_THREAD_COUNT; 
	                if (sleep > 10) {
	                    sleep = 10;
	                }
	                if (sleep < 1) {
	                    sleep = 1;
	                }
	                //logger.debug("rendezVousPoint sleeping for "+sleep +" ms");
	                Thread.sleep(sleep);
	            }
	        } catch (InterruptedException interrupt) {
	            // If we are interrupted, something wants us to shut down
	            logger.error("Writeback thread was interrupted!");
	            result.errorCount++;
	            result.lastError = interrupt;
	        }
	        boolean notDone = true;
	        while (notDone){
	            notDone =false;
	            for (Future f : (LinkedList<Future>)fList){
	                if (!f.isDone()){
	                    notDone = true;
	                }
	            }
	            try {
//	                logger.debug("rendezVousPoint future check sleeping for 20 ms");
	                Thread.sleep(20);
	            } catch (InterruptedException ex) {
	                logger.fatal(ex);
	            }
	        }
	        
	        if (result.errorCount > 0) {
	            logger.warn(
	                    String.format("Prepop has errors: SuccessCount(%s) ErrorCount(%s) lastError(%s)", result.storedCount, result.errorCount, (result.lastError != null ? result.lastError.getMessage() : "(unknown error)"))
	            );

//	          throw result.lastError;
	        }
	    }
	    
		class PrepopulateRunner implements Runnable {
			private StudentCredentials cred;
			private PrepopulateResult resultCounts;

			PrepopulateRunner(PrepopulateResult resultCounts,
					StudentCredentials cred) {

				this.cred = cred;
				this.resultCounts = resultCounts;
			}

			public void run() {
				Connection conn = null;
				try {
					conn = oasDBSource.getOASConnection();

					if (cred != null) {
						String key = cred.getUsername() + ":"
								+ cred.getPassword() + ":"
								+ cred.getAccesscode();
						if (cred.getUsername() == null
								|| cred.getPassword() == null
								|| cred.getAccesscode() == null) {
							if (cred.getTestRosterId() != null) {
								logger.info("Invalid or deleted roster in pre-pop table, marking manifest unusable for roster: "
										+ cred.getTestRosterId());
								ManifestWrapper wrapper = oasSource
										.getAllManifests(cred
												.getTestRosterId());
								Manifest[] manifests = wrapper
										.getManifests();
								for (int j = 0; j < manifests.length; j++) {
									manifests[j]
											.setUsable(false);
								}
								wrapper.setManifests(manifests);
								oasSink.putAllManifests(
										cred.getTestRosterId(),
										wrapper, false);
							}
						} else {
							if (cred.getUsername().startsWith(
									"pt-student")
									&& cred.getAccesscode()
											.startsWith("PTest")) {
								oasSink.deleteAllItemResponses(Integer.parseInt(cred
										.getTestRosterId()));
							}
							RosterData rd = oasDBSource
									.getRosterData(conn, key);
							Manifest[] manifests = oasDBSource
									.getManifest(conn, cred
											.getTestRosterId());
							if (manifests != null
									&& manifests.length > 0) {
								oasSink.putRosterData(cred, rd,
										false);
								oasSink.putAllManifests(cred
										.getTestRosterId(),
										new ManifestWrapper(
												manifests),
										false);
								rd = null;
								manifests = null;
								resultCounts.storedCount++;
							} else {
								resultCounts.errorCount++;
								resultCounts.lastError = new Exception(
										"Couldn't retrieve manifest for "
												+ key);
							}
						}
						key = null;
					}
				} catch (Exception e) {
					resultCounts.errorCount++;
					resultCounts.lastError = e;
				} finally {
					if (conn != null) {
						try {
							conn.close();
						} catch (Exception ex) {
						}
					}
				}
			}

		}
	    
	    
		class PrepopulateResult {
			public int errorCount = 0;
			public int storedCount = 0;
			public Exception lastError = null;
		}

		
	}

	private static class ScoringThread extends Thread {
		public ScoringThread() {
		}

		public void run() {
			while (true) {
				try {
					while (!rosterQueue.isEmpty()) {
						ScoringMessage message = rosterQueue.peek();
						if (message != null
								&& (System.currentTimeMillis() - message.timestamp) > 60000) {
							message = rosterQueue.poll();
							JMSUtils.sendMessage(Integer.valueOf(message
									.getTestRosterId()));
							logger.info("*****  Sent scoring message for roster "
									+ message.getTestRosterId());
						} else {
							Thread.sleep(1000);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						Thread.sleep(1000);
					} catch (Exception ie) {
						// do nothing
					}
				}
			}
		}
	}
}
