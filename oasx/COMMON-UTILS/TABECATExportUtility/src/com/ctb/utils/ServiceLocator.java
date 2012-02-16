package com.ctb.utils;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ServiceLocator {
	
	static DataSource locateDataSource(String jndiName ) throws NamingException{
		Context ctx = new InitialContext();
		DataSource ds =  (DataSource) ctx.lookup(jndiName);
		return ds;
	}

}
