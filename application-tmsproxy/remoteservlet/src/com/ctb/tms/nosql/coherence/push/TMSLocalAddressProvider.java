package com.ctb.tms.nosql.coherence.push;

import java.net.InetSocketAddress;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.tangosol.net.AddressProvider;

public class TMSLocalAddressProvider implements AddressProvider {

	static Logger logger = Logger.getLogger(TMSLocalAddressProvider.class);
	static String hostname;
	static int port;
	
	static {
		ResourceBundle rb = ResourceBundle.getBundle("pushreplication");
		hostname = rb.getString("coherence.local.hostname");
		port = Integer.parseInt(rb.getString("coherence.local.port"));
	}
	
	public void accept() {
	}

	public InetSocketAddress getNextAddress() {
		InetSocketAddress addr = new InetSocketAddress(hostname, port);
		logger.info("Returned address: " + hostname + ":" + port);
		return addr;
	}

	public void reject(Throwable arg0) {
	}

}
