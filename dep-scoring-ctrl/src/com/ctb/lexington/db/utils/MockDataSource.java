package com.ctb.lexington.db.utils;

import java.io.PrintWriter;
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

import javax.sql.DataSource;

public class MockDataSource implements DataSource {
    private Connection conn;
    private PrintWriter logWriter;
    private int loginTimeOut;

    public MockDataSource(Connection conn) {
        this.conn = new ConnectionWrapper(conn);
    }

    public Connection getConnection() throws SQLException {
        return conn;
    }

    public Connection getConnection(String arg0, String arg1) throws SQLException {
        return conn;
    }

    public PrintWriter getLogWriter() throws SQLException {
        return logWriter;
    }

    public int getLoginTimeout() throws SQLException {
        return loginTimeOut;
    }

    public void setLogWriter(PrintWriter arg0) throws SQLException {
        this.logWriter = arg0;
    }

    public void setLoginTimeout(int arg0) throws SQLException {
        this.loginTimeOut = arg0;
    }

    /**
     * TODO: This class prevents closing a connection during tests, but if there is a real problem
     * where we are closing connections out of place, we will never know.
     */
    public class ConnectionWrapper implements Connection {

        private final Connection delegate;

        ConnectionWrapper(Connection connection) {
            delegate = connection;
        }

        public Connection getDelegate() {
            return delegate;
        }

        public Statement createStatement() throws SQLException {
            return delegate.createStatement();
        }

        public PreparedStatement prepareStatement(String sql) throws SQLException {
            return delegate.prepareStatement(sql);
        }

        public CallableStatement prepareCall(String sql) throws SQLException {
            return delegate.prepareCall(sql);
        }

        public String nativeSQL(String sql) throws SQLException {
            return delegate.nativeSQL(sql);
        }

        public void setAutoCommit(boolean autoCommit) throws SQLException {
            delegate.setAutoCommit(autoCommit);
        }

        public boolean getAutoCommit() throws SQLException {
            return delegate.getAutoCommit();
        }

        public void commit() throws SQLException {
            delegate.commit();
        }

        public void rollback() throws SQLException {
            delegate.rollback();
        }

        public void close() throws SQLException {
            // don't close in tests because this is one shared connection...
        }

        public boolean isClosed() throws SQLException {
            return delegate.isClosed();
        }

        public DatabaseMetaData getMetaData() throws SQLException {
            return delegate.getMetaData();
        }

        public void setReadOnly(boolean readOnly) throws SQLException {
            delegate.setReadOnly(readOnly);
        }

        public boolean isReadOnly() throws SQLException {
            return delegate.isReadOnly();
        }

        public void setCatalog(String catalog) throws SQLException {
            delegate.setCatalog(catalog);
        }

        public String getCatalog() throws SQLException {
            return delegate.getCatalog();
        }

        public void setTransactionIsolation(int level) throws SQLException {
            delegate.setTransactionIsolation(level);
        }

        public int getTransactionIsolation() throws SQLException {
            return delegate.getTransactionIsolation();
        }

        public SQLWarning getWarnings() throws SQLException {
            return delegate.getWarnings();
        }

        public void clearWarnings() throws SQLException {
            delegate.clearWarnings();
        }

        public Statement createStatement(int resultSetType, int resultSetConcurrency)
                throws SQLException {
            return delegate.createStatement(resultSetType, resultSetConcurrency);
        }

        public PreparedStatement prepareStatement(String sql, int resultSetType,
                int resultSetConcurrency) throws SQLException {
            return delegate.prepareStatement(sql, resultSetType, resultSetConcurrency);
        }

        public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
                throws SQLException {
            return delegate.prepareCall(sql, resultSetType, resultSetConcurrency);
        }

        public Map getTypeMap() throws SQLException {
            return delegate.getTypeMap();
        }

        public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
            delegate.setTypeMap(map);
        }

        public void setHoldability(int holdability) throws SQLException {
            delegate.setHoldability(holdability);
        }

        public int getHoldability() throws SQLException {
            return delegate.getHoldability();
        }

        public Savepoint setSavepoint() throws SQLException {
            return delegate.setSavepoint();
        }

        public Savepoint setSavepoint(String name) throws SQLException {
            return delegate.setSavepoint(name);
        }

        public void rollback(Savepoint savepoint) throws SQLException {
            delegate.rollback(savepoint);
        }

        public void releaseSavepoint(Savepoint savepoint) throws SQLException {
            delegate.releaseSavepoint(savepoint);
        }

        public Statement createStatement(int resultSetType, int resultSetConcurrency,
                int resultSetHoldability) throws SQLException {
            return delegate.createStatement(resultSetType, resultSetConcurrency,
                    resultSetHoldability);
        }

        public PreparedStatement prepareStatement(String sql, int resultSetType,
                int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return delegate.prepareStatement(sql, resultSetType, resultSetConcurrency,
                    resultSetHoldability);
        }

        public CallableStatement prepareCall(String sql, int resultSetType,
                int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return delegate.prepareCall(sql, resultSetType, resultSetConcurrency,
                    resultSetHoldability);
        }

        public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
                throws SQLException {
            return delegate.prepareStatement(sql, autoGeneratedKeys);
        }

        public PreparedStatement prepareStatement(String sql, int columnIndexes[])
                throws SQLException {
            return delegate.prepareStatement(sql, columnIndexes);
        }

        public PreparedStatement prepareStatement(String sql, String columnNames[])
                throws SQLException {
            return delegate.prepareStatement(sql, columnNames);
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

//		@Override
//		public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
//			// TODO Auto-generated method stub
//			
//		}

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