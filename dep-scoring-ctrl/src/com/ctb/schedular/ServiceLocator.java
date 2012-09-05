package com.ctb.schedular;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

 /**
 * @author TCS Kolkata Offshore 
 * @version 05/09/2012
 */
public class ServiceLocator {
	
	static DataSource locateDataSource(String jndiName ) throws NamingException{
		Context ctx = new InitialContext();
		DataSource ds =  (DataSource) ctx.lookup(jndiName);
		return ds;
	}

}
