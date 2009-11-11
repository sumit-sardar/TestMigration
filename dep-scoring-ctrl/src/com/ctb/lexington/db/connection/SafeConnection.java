package com.ctb.lexington.db.connection;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;

public class SafeConnection implements Connection {
    protected Connection realConnection;

    public SafeConnection(Connection conn) throws SQLException {
        realConnection = conn;
        realConnection.setAutoCommit(false);
    }

    /**
     * @throws java.sql.SQLException
     */
    public void clearWarnings() throws SQLException {
        realConnection.clearWarnings();
    }
    /**
     * @throws java.sql.SQLException
     */
    public void close() throws SQLException {
        realConnection.close();
    }
    /**
     * @throws java.sql.SQLException
     */
    public void commit() throws SQLException {
        realConnection.commit();
    }
    /**
     * @return @throws
     *         java.sql.SQLException
     */
    public Statement createStatement() throws SQLException {
        return realConnection.createStatement();
    }
    /**
     * @param arg0
     * @param arg1
     * @return @throws
     *         java.sql.SQLException
     */
    public Statement createStatement(int arg0, int arg1) throws SQLException {
        return realConnection.createStatement(arg0, arg1);
    }
    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @return @throws
     *         java.sql.SQLException
     */
    public Statement createStatement(int arg0, int arg1, int arg2)
            throws SQLException {
        return realConnection.createStatement(arg0, arg1, arg2);
    }
    /**
     * @param arg0
     * @return
     */
    public boolean equals(Object arg0) {
        return realConnection.equals(arg0);
    }
    /**
     * @return @throws
     *         java.sql.SQLException
     */
    public boolean getAutoCommit() throws SQLException {
        return realConnection.getAutoCommit();
    }
    /**
     * @return @throws
     *         java.sql.SQLException
     */
    public String getCatalog() throws SQLException {
        return realConnection.getCatalog();
    }
    /**
     * @return @throws
     *         java.sql.SQLException
     */
    public int getHoldability() throws SQLException {
        return realConnection.getHoldability();
    }
    /**
     * @return @throws
     *         java.sql.SQLException
     */
    public DatabaseMetaData getMetaData() throws SQLException {
        return realConnection.getMetaData();
    }
    /**
     * @return @throws
     *         java.sql.SQLException
     */
    public int getTransactionIsolation() throws SQLException {
        return realConnection.getTransactionIsolation();
    }
    /**
     * @return @throws
     *         java.sql.SQLException
     */
    public Map getTypeMap() throws SQLException {
        return realConnection.getTypeMap();
    }
    /**
     * @return @throws
     *         java.sql.SQLException
     */
    public SQLWarning getWarnings() throws SQLException {
        return realConnection.getWarnings();
    }
    /**
     * @return
     */
    public int hashCode() {
        return realConnection.hashCode();
    }
    /**
     * @return @throws
     *         java.sql.SQLException
     */
    public boolean isClosed() throws SQLException {
        return realConnection.isClosed();
    }
    /**
     * @return @throws
     *         java.sql.SQLException
     */
    public boolean isReadOnly() throws SQLException {
        return realConnection.isReadOnly();
    }
    /**
     * @param arg0
     * @return @throws
     *         java.sql.SQLException
     */
    public String nativeSQL(String arg0) throws SQLException {
        return realConnection.nativeSQL(arg0);
    }
    /**
     * @param arg0
     * @return @throws
     *         java.sql.SQLException
     */
    public CallableStatement prepareCall(String arg0) throws SQLException {
        return realConnection.prepareCall(arg0);
    }
    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @return @throws
     *         java.sql.SQLException
     */
    public CallableStatement prepareCall(String arg0, int arg1, int arg2)
            throws SQLException {
        return realConnection.prepareCall(arg0, arg1, arg2);
    }
    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     * @return @throws
     *         java.sql.SQLException
     */
    public CallableStatement prepareCall(String arg0, int arg1, int arg2,
            int arg3) throws SQLException {
        return realConnection.prepareCall(arg0, arg1, arg2, arg3);
    }
    /**
     * @param arg0
     * @return @throws
     *         java.sql.SQLException
     */
    public PreparedStatement prepareStatement(String arg0) throws SQLException {
        return realConnection.prepareStatement(arg0);
    }
    /**
     * @param arg0
     * @param arg1
     * @return @throws
     *         java.sql.SQLException
     */
    public PreparedStatement prepareStatement(String arg0, int arg1)
            throws SQLException {
        return realConnection.prepareStatement(arg0, arg1);
    }
    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @return @throws
     *         java.sql.SQLException
     */
    public PreparedStatement prepareStatement(String arg0, int arg1, int arg2)
            throws SQLException {
        return realConnection.prepareStatement(arg0, arg1, arg2);
    }
    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     * @return @throws
     *         java.sql.SQLException
     */
    public PreparedStatement prepareStatement(String arg0, int arg1, int arg2,
            int arg3) throws SQLException {
        return realConnection.prepareStatement(arg0, arg1, arg2, arg3);
    }
    /**
     * @param arg0
     * @param arg1
     * @return @throws
     *         java.sql.SQLException
     */
    public PreparedStatement prepareStatement(String arg0, int[] arg1)
            throws SQLException {
        return realConnection.prepareStatement(arg0, arg1);
    }
    /**
     * @param arg0
     * @param arg1
     * @return @throws
     *         java.sql.SQLException
     */
    public PreparedStatement prepareStatement(String arg0, String[] arg1)
            throws SQLException {
        return realConnection.prepareStatement(arg0, arg1);
    }
    /**
     * @param arg0
     * @throws java.sql.SQLException
     */
    public void releaseSavepoint(Savepoint arg0) throws SQLException {
        realConnection.releaseSavepoint(arg0);
    }
    /**
     * @throws java.sql.SQLException
     */
    public void rollback() throws SQLException {
        realConnection.rollback();
    }
    /**
     * @param arg0
     * @throws java.sql.SQLException
     */
    public void rollback(Savepoint arg0) throws SQLException {
        realConnection.rollback(arg0);
    }
    /**
     * @param arg0
     * @throws java.sql.SQLException
     */
    public void setAutoCommit(boolean arg0) throws SQLException {
        throw new RuntimeException("Auto commit is not allowed.");
    }
    /**
     * @param arg0
     * @throws java.sql.SQLException
     */
    public void setCatalog(String arg0) throws SQLException {
        realConnection.setCatalog(arg0);
    }
    /**
     * @param arg0
     * @throws java.sql.SQLException
     */
    public void setHoldability(int arg0) throws SQLException {
        realConnection.setHoldability(arg0);
    }
    /**
     * @param arg0
     * @throws java.sql.SQLException
     */
    public void setReadOnly(boolean arg0) throws SQLException {
        realConnection.setReadOnly(arg0);
    }
    /**
     * @return @throws
     *         java.sql.SQLException
     */
    public Savepoint setSavepoint() throws SQLException {
        return realConnection.setSavepoint();
    }
    /**
     * @param arg0
     * @return @throws
     *         java.sql.SQLException
     */
    public Savepoint setSavepoint(String arg0) throws SQLException {
        return realConnection.setSavepoint(arg0);
    }
    /**
     * @param arg0
     * @throws java.sql.SQLException
     */
    public void setTransactionIsolation(int arg0) throws SQLException {
        realConnection.setTransactionIsolation(arg0);
    }
    /**
     * @param arg0
     * @throws java.sql.SQLException
     */
    public void setTypeMap(Map<String, Class<?>> arg0) throws SQLException {
        realConnection.setTypeMap(arg0);
    }
    /**
     * @return
     */
    public String toString() {
        return realConnection.toString();
    }

	@Override
	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Blob createBlob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Clob createClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NClob createNClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getClientInfo(String name) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isValid(int timeout) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
//		// TODO Auto-generated method stub
//		
//	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
}