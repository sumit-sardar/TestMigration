package com.ctb.tms.nosql.coherence.push;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;

import com.tangosol.net.AddressProvider;

public class TMSRemoteAddressProvider implements AddressProvider {

	static Logger logger = Logger.getLogger(TMSRemoteAddressProvider.class);
	
	public void accept() {
		logger.warn("Address accepted.");
	}

	public InetSocketAddress getNextAddress() {
		InetSocketAddress addr = new InetSocketAddress("nj09mhe5335", 8089);
		return addr;
	}

	public void reject(Throwable arg0) {
		logger.warn("Address rejected!");
	}

}
