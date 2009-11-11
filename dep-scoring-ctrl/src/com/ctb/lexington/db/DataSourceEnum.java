package com.ctb.lexington.db;

import org.apache.commons.lang.enums.Enum;


public class DataSourceEnum extends Enum{
	
	public static final DataSourceEnum OSRDATASOURCE = new DataSourceEnum("OSRDataSource");
	public static final DataSourceEnum OASDATASOURCE = new DataSourceEnum("OASDataSource");
	
	protected DataSourceEnum(String arg0) {
		super(arg0);
	}

	public static DataSourceEnum get(String val) {
		return (DataSourceEnum) getEnum(DataSourceEnum.class, val);
	}

	public String toString() {
		return super.getName();
	}

}
