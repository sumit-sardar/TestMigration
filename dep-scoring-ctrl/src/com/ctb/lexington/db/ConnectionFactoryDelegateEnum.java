package com.ctb.lexington.db;

import org.apache.commons.lang.enums.Enum;

public class ConnectionFactoryDelegateEnum extends Enum{
	
	public static final ConnectionFactoryDelegateEnum OSRDATASOURCE = new ConnectionFactoryDelegateEnum("OSRDelegate");
	public static final ConnectionFactoryDelegateEnum ATSDATASOURCE = new ConnectionFactoryDelegateEnum("ATSDelegate");
	public static final ConnectionFactoryDelegateEnum OASDATASOURCE = new ConnectionFactoryDelegateEnum("OASDelegate");
	
	protected ConnectionFactoryDelegateEnum(String arg0) {
		super(arg0);
	}

	public static ConnectionFactoryDelegateEnum get(String val) {
		return (ConnectionFactoryDelegateEnum) getEnum(ConnectionFactoryDelegateEnum.class, val);
	}

	public String toString() {
		return super.getName();
	}
}
