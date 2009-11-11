package com.ctb.lexington.util;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.ctb.lexington.exception.CTBSystemException;

/**
 * @author jshields
 * @version
 * @created September 26, 2002
 */
public final class SQLUtil {
    private static Hashtable dataSources = new Hashtable(2);

    /** DOCUMENT ME! */
    private static final String TRACE_TAG = "SQLUtil";

    /**
     * Creates new SQLUtil
     */
    private SQLUtil() {
    }

    /**
     * Returns a {@link java.sql.Connection}from the {@linkjavax.sql.DataSource}corresponding to
     * the provided JNDI name.
     * 
     * @param name_ The JNDI name of the DataSource.
     * @return The dataSource value
     * @exception GrndsFactoryException Description of the Exception
     * @exception SQLException Description of the Exception
     * @throws NamingException 
     */
    public static Connection getConnection(String name_) throws SQLException, NamingException {
        return getDataSource(name_).getConnection();
    }

    /**
     * Returns the {@link javax.sql.DataSource}corresponding to the provided JNDI name.
     * 
     * @param name_ The JNDI name of the DataSource.
     * @return The dataSource value
     * @throws NamingException 
     * @exception GrndsFactoryException Description of the Exception
     */
    public static synchronized DataSource getDataSource(String name_) throws NamingException {
        DataSource ds = (DataSource) dataSources.get(name_);

        if (ds == null) {
        	Context ctx = new InitialContext();
        	ds = (DataSource)ctx.lookup("jdbc/" + name_);

            dataSources.put(name_, ds);
        }

        return ds;
    }

    /**
     * Handles cleanly closing the connection.
     * 
     * @param conn Description of the Parameter
     */
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
            	e.printStackTrace();
            }
        }
    }

    /**
     * Handles cleanly closing the statement.
     * 
     * @param stmt Description of the Parameter
     */
    public static void close(Statement stmt) {
        close(null, stmt);
    }

    /**
     * Handles cleanly closing the result set.
     * 
     * @param rs Description of the Parameter
     */
    public static void close(ResultSet rs) {
        close(rs, null);
    }

    /**
     * Handles cleanly closing the result set and statement.
     * 
     * @param resultSet
     * @param statement
     */
    public static void close(ResultSet resultSet, Statement statement) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException ignore) {
            System.out.println(ignore.getMessage());
        }

        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException ignore) {
            System.out.println(ignore.getMessage());
        }
    }

    /**
     * Description of the Method
     * 
     * @param ds_ Description of the Parameter
     * @param sql_ Description of the Parameter
     * @param params_ Description of the Parameter
     * @return Description of the Return Value
     * @exception CTBSystemException Description of the Exception
     */
    public static boolean doBooleanQuery(String ds_, String sql_, List params_)
            throws CTBSystemException {
        final String SUB_TRACE = TRACE_TAG + ".doBooleanQuery()";

        // create null refs
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean result = false;

        try {

            // get db connection
            conn = getDataSource(ds_).getConnection();

            // get statement
            ps = conn.prepareStatement(sql_);

            // set params
            Object param = null;
            Iterator paramIt = params_.iterator();

            for (int i = 1; paramIt.hasNext(); i++) {
                param = paramIt.next();

                if (param instanceof Integer) {
                    ps.setInt(i, ((Integer) param).intValue());
                } else if (param instanceof String) {
                    ps.setString(i, param.toString());
                } else if (param instanceof Boolean) {
                    ps.setBoolean(i, ((Boolean) param).booleanValue());
                } else {
                    ps.setObject(i, param);
                }
            }

            // get result
            rs = ps.executeQuery();

            // go to first record
            rs.next();

            // match count
            int count = rs.getInt(1);

            if (count == 0) {
                result = false;
            } else {
                result = true;
            }

            return result;
        }

        // end try
        catch (Exception e) {
            //LoggerUtils.logError("syslog", SUB_TRACE, e);

            CTBSystemException se = new CTBSystemException("1199", "Error performing query.", e);
            throw se;
        } finally {
            // close DB resources
            close(rs);
            close(ps);
            close(conn);

        }
    }

    /**
     * Uses a JNDI lookup to find the SQL with the supplied name. The name is used to cache the SQL
     * in a map, thus the name must be unique across the application. The name is appended to
     * "java:comp/env/" for the lookup. Thus, this method is primarily designed to be used from
     * within an EJB, but may work in other contexts as well.
     * 
     * @param jndiName_ Description of the Parameter
     * @return The SQL.
     * @exception NamingException Description of the Exception
     */
    public static synchronized String findSQL(String jndiName_) throws NamingException {
        InitialContext ctx = new InitialContext();
        String sql = (String) ctx.lookup("java:comp/env/" + jndiName_);

        return sql;
    }

    public static final Date asDate(Timestamp tstamp) {
        return tstamp == null ? null : new Date(tstamp.getTime());
    }

    public static final String getString(ResultSet rs, String columnName) throws SQLException {
        String result = rs.getString(columnName);
        return rs.wasNull() ? null : result;
    }

    public static final String getString(ResultSet rs, int columnIndex) throws SQLException {
        String result = rs.getString(columnIndex);
        return rs.wasNull() ? null : result;
    }

    public static final Timestamp getTimestamp(ResultSet rs, String columnName) throws SQLException {
        Timestamp result = rs.getTimestamp(columnName);
        return rs.wasNull() ? null : result;
    }

    public static final Timestamp getTimestamp(ResultSet rs, int columnIndex) throws SQLException {
        Timestamp result = rs.getTimestamp(columnIndex);
        return rs.wasNull() ? null : result;
    }

    public static final Long getLong(ResultSet rs, String columnName) throws SQLException {
        long result = rs.getLong(columnName);
        return rs.wasNull() ? null : new Long(result);
    }

    public static final Long getLong(ResultSet rs, int columnIndex) throws SQLException {
        long result = rs.getLong(columnIndex);
        return rs.wasNull() ? null : new Long(result);
    }

    public static final Integer getInteger(ResultSet rs, String columnName) throws SQLException {
        int result = rs.getInt(columnName);
        return rs.wasNull() ? null : new Integer(result);
    }

    public static final Integer getInteger(ResultSet rs, int columnIndex) throws SQLException {
        int result = rs.getInt(columnIndex);
        return rs.wasNull() ? null : new Integer(result);
    }

    public static final Clob getClob(ResultSet rs, String columnName) throws SQLException {
        Clob result = rs.getClob(columnName);
        return rs.wasNull() ? null : result;
    }

    public static final Clob getClob(ResultSet rs, int columnIndex) throws SQLException {
        Clob result = rs.getClob(columnIndex);
        return rs.wasNull() ? null : result;
    }
    
    /*
     * Only use this if you can not accomplish what you need with PreparedStatement variable
     * binding.  This will escape a string value used in a dynamically built where clause by
     * putting in ' in front of known reserved characters in Oracle:  ', %, _
     * 
     * Additionally, the $ is special for regular expressions so we'll java escape it.
     * 
     * TODO: Isn't there a way to do this with existing java.sql packages?  Also, the $ escape
     * should really be somewhere else.
     */
    public static final String getEscapedValue(String value) {
    	String escapedValue = value;
    	
    	escapedValue = escapedValue.replaceAll("'", "''");
    	//escapedValue = escapedValue.replaceAll("_", "''_");
    	//escapedValue = escapedValue.replaceAll("%", "''%");
    	escapedValue = escapedValue.replaceAll("\\$", "\\\\\\$");
    	
    	return escapedValue;
    }
    
    /*
     * Get array of Integer indices for the underscore character in the input
     * 
     * This method is to be used when setting an escaped string in a query
     * 
     */
    public static ArrayList getCharacterIndices(String input_, String character_)    {
    	ArrayList result = new ArrayList();
    	int index = 0;
    	while (index < input_.length() && index != -1){
    		index = input_.indexOf(character_, index);
    		if(index != -1){
    			result.add(new Integer(index));
    			index ++;
    		}
    	}
    	return result;
    }

    public static String setEscapeCharacter(String input_, ArrayList characterIndices_, String escapeCharacter_){
    	String result = input_;
    	if(characterIndices_.size() > 0){
        	int begin = 0;
        	int end = 0;
        	StringBuffer buf = new StringBuffer();
	  		for(Iterator it = characterIndices_.iterator(); it.hasNext();){
	    		Integer underscoreIndex = (Integer)it.next();
	    		end = underscoreIndex.intValue();
	    		buf.append(input_.substring(begin, end));
	    		buf.append(escapeCharacter_);
	    		begin = end;
	    	}
	  		buf.append(input_.substring(end, input_.length()));
	  		result = buf.toString();
    	}
    	
    	return result;
    }
    
    public static String addEscapeClause(String input_, String escapeCharacter_){
    	StringBuffer result = new StringBuffer(input_);
    	result.append(" escape '");
    	result.append(escapeCharacter_);
    	result.append("' ");
    	return result.toString();
    }

}