package com.ctb.tms.nosql.coherence.push;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.tangosol.net.AddressProvider;

public class TMSRemoteAddressProvider implements AddressProvider {

	static Logger logger = Logger.getLogger(TMSRemoteAddressProvider.class);
	static HashMap<String, RemoteNode> remoteMap = new HashMap<String, RemoteNode>(10);
	static int nextIndex = 1;
	RemoteNode remote = null;
	
	private static class RemoteNode {
		private String host;
		private int port;
		private boolean status;
		private long lastStatusCheck;
		private static final long STATUS_CHECK_INTERVAL = 60000;
		
		public String getHost() {
			return host;
		}
		public void setHost(String host) {
			this.host = host;
		}
		public int getPort() {
			return port;
		}
		public void setPort(int port) {
			this.port = port;
		}
		public boolean getStatus() {
			if(!status && (System.currentTimeMillis() - lastStatusCheck) > STATUS_CHECK_INTERVAL) {
				status = true;
			}
			return status;
		}
		public void setStatus(boolean status) {
			this.status = status;
		}
		public long getLastStatusCheck() {
			return lastStatusCheck;
		}
		public void setLastStatusCheck(long lastStatusCheck) {
			this.lastStatusCheck = lastStatusCheck;
		}
	}
	
	static {
		ResourceBundle rb = ResourceBundle.getBundle("pushreplication");
		Enumeration<String> keys = rb.getKeys();
		while(keys.hasMoreElements()) {
			String fullKey = keys.nextElement();
			String key = fullKey;
			System.out.println("Full key: " + key);
			if(key.startsWith("coherence.remote.")) {
				key = key.substring(17, key.length());
				System.out.println("Remote key: " + key);
				String node = key.substring(0, key.indexOf("."));
				System.out.println("Node: " + node);
				key = key.substring(key.indexOf(".") + 1, key.length());
				System.out.println("Final key: " + key);
				if(key.startsWith("hostname")) {
					String hostname = rb.getString(fullKey);
					RemoteNode remote = remoteMap.get(node);
					if(remote == null) {
						remote = new RemoteNode();
					}
					remote.setHost(hostname);
					remote.setStatus(true);
					remoteMap.put(node, remote);
				} else if(key.startsWith("port")) {
					String port = rb.getString(fullKey);
					RemoteNode remote = remoteMap.get(node);
					if(remote == null) {
						remote = new RemoteNode();
					}
					remote.setPort(Integer.parseInt(port));
					remote.setStatus(true);
					remoteMap.put(node, remote);
				}
			}
		}
	}
	
	public void accept() {
		remote.setStatus(true);
		remote.setLastStatusCheck(System.currentTimeMillis());
		logger.info("Address accepted: " + remote.getHost() + ":" + remote.getPort());
	}

	public InetSocketAddress getNextAddress() {
		logger.info("request for address");
		int tries = remoteMap.size();
		for(int i=0;i<tries;i++) {
			synchronized(TMSRemoteAddressProvider.class) {
				if(nextIndex > tries) nextIndex = 1;
				remote = remoteMap.get(String.valueOf(nextIndex));
				nextIndex += 1;
			}
			if(remote != null && remote.getStatus()) {
				break;
			} else {
				remote = null;
			}
		}
		if(remote != null) {
			InetSocketAddress addr = new InetSocketAddress(remote.getHost(), remote.getPort());
			logger.info("Returned address: " + remote.getHost() + ":" + remote.getPort());
			return addr;
		}
		logger.warn("Could not obtain remote address, returning null!");
		return null;
	}

	public void reject(Throwable arg0) {
		remote.setStatus(false);
		remote.setLastStatusCheck(System.currentTimeMillis());
		logger.warn("Address rejected: " + remote.getHost() + ":" + remote.getPort());
	}

}
