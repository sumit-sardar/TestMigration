package com.ctb.lexington.db.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.ctb.lexington.db.JunitConnectionProvider;
import com.ctb.lexington.db.monitor.TrackableDataSource;
import com.ctb.lexington.util.SQLUtil;

public class DataSourceFactory {
    private static DataSourceFactory INSTANCE = new DataSourceFactory();
    private Map dataSourceMap;
    public static final String OASDATASOURCE = "OASDataSource";
    //  IRS DataSource 
    public static final String IRSDATASOURCE = "IRSDataSource";


    private DataSourceFactory() {
        // no one should instantiate this class
    }

    public static DataSourceFactory getInstance() {
        return INSTANCE;
    }

    public DataSource getDataSource(String source) throws SQLException, NamingException {
        if (dataSourceMap == null) {
            dataSourceMap = new HashMap();
        }

        DataSource dataSource = (DataSource) dataSourceMap.get(source);
        if (dataSource == null) {
            loadDataSources();
            dataSource = (DataSource) dataSourceMap.get(source);
        }

        return dataSource;
    }

    private synchronized void loadDataSources() throws
            SQLException, NamingException {
        if (checkTestRun()) {
            loadTestConnections();
        } else {
            /* trackable DSs incur considerable overhead, only use for debugging
        	if (ATSDATASOURCE.equals(source)) {
                final DataSource atsDataSource = new TrackableDataSource((DataSource) GrndsFactory.produce("ATSDataSource"));
                dataSourceMap.put(ATSDATASOURCE, atsDataSource);
            } else if (OASDATASOURCE.equals(source)) {
                final DataSource oasDataSource = new TrackableDataSource((DataSource) GrndsFactory.produce("LexingtonDataSource"));
                dataSourceMap.put(OASDATASOURCE, oasDataSource);
            }
            */
            Context ctx = new InitialContext();
            final DataSource oasDataSource = (DataSource)ctx.lookup("oasDataSource");
            dataSourceMap.put(OASDATASOURCE, oasDataSource);
            final DataSource irsDataSource = (DataSource)ctx.lookup("irsDataSource");
            dataSourceMap.put(IRSDATASOURCE, irsDataSource);
        }
    }

    private void loadTestConnections() throws SQLException {
        JunitConnectionProvider provider = new JunitConnectionProvider();
        final DataSource oasDataSource = new TrackableDataSource(DbUtils.createMockDataSource(provider.getOASConnection()));
        dataSourceMap.put(OASDATASOURCE, oasDataSource);
        // IRSDataSource 
        final DataSource irsDataSource = new TrackableDataSource(DbUtils.createMockDataSource(provider.getIRSConnection()));
        dataSourceMap.put(IRSDATASOURCE, irsDataSource);
 
    }

    private boolean checkTestRun() {
        return Boolean.getBoolean("testRun");
    }

    public void release(Connection conn) {
        if (!checkTestRun()) {
            SQLUtil.close(conn);
        }
    }
}