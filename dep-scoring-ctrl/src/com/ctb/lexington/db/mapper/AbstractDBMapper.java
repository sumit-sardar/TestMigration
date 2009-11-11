package com.ctb.lexington.db.mapper;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.ctb.lexington.db.record.Persistent;
import com.ctb.lexington.exception.SystemException;
import com.ctb.lexington.util.Timer;
import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.ibatis.sqlmap.client.SqlMapSession;

public abstract class AbstractDBMapper {
    private final static String SQL_MAP_FILE = "sqlMapConfig.xml";
    private final static SqlMapClient sqlMapClient;

    static {
        try {
            Reader reader = Resources.getResourceAsReader(SQL_MAP_FILE);
            sqlMapClient = SqlMapClientBuilder.buildSqlMapClient(reader);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new RuntimeException(ioe.getMessage(), ioe);
        }
    }

    protected Connection conn;

    /*
     * deprecated
     */
    public AbstractDBMapper(final Connection conn) {
        this.conn = conn;
    }
    
    public static SqlMapSession getNewSqlMapSession() {
    	return sqlMapClient.openSession();
    }

    private SqlMapSession getSqlMapClient() {
    	return sqlMapClient.openSession(this.conn);
    }

    protected Long nextSequenceValue(final String findName) {
        return (Long) queryForObject(findName, null);
    }

    protected Persistent find(final String findName, final Map map) {
        return (Persistent) queryForObject(findName, map);
    }

    protected Persistent find(final String findName, final Persistent template) {
        return (Persistent) queryForObject(findName, template);
    }

    protected Persistent find(final String findName, final Object value) {
        return (Persistent) queryForObject(findName, value);
    }

    protected Long count(final String findName, final Object value) {
        return (Long) queryForObject(findName, value);
    }
    
    protected Long count(final String findName, final Map map) {
    	return (Long) queryForObject(findName, map);
    }

    private Object queryForObject(final String findName, final Object template) {
        try {
        	SqlMapSession session = getSqlMapClient();
        	Object result = session.queryForObject(findName, template);
        	return result;
        } catch (SQLException exc) {
            return throwForFailedQuery(findName, template, exc);
        }
    }

    protected List findMany(final String findName, final Persistent template) {
        return queryForList(findName, template);
    }

    protected List findMany(final String findName, final Object arg) {
        return queryForList(findName, arg);
    }

    protected List findMany(final String findName, final Map map) {
        return queryForList(findName, map);
    }

    private List queryForList(final String findName, final Object object) {
        try {
            SqlMapSession session = getSqlMapClient();
            List result = session.queryForList(findName, object);
            return result;
        } catch (SQLException exc) {
            return (List) throwForFailedQuery(findName, object, exc);
        }
    }

    protected void insert(final String insertName, final Persistent record) throws SQLException {
        	SqlMapSession session = getSqlMapClient();
        	session.insert(insertName, record);
    }

    protected void update(final String updateName, final Persistent record) throws SQLException {
            SqlMapSession session = getSqlMapClient();
            session.update(updateName, record);
    }

    protected void delete(final String deleteName, final Object object) throws SQLException {
    	SqlMapSession session = getSqlMapClient();
        session.delete(deleteName, object);
    }
    
    protected void delete(final String deleteName, final Persistent record) throws SQLException {
            SqlMapSession session = getSqlMapClient();
            session.delete(deleteName, record);
    }

    protected boolean exists(final String existsName, final Persistent persistent) {
        return null != queryForObject(existsName, persistent);
    }

    private static Object throwForFailedQuery(final String findName, final Object template,
            final SQLException exc) {
        throw new SystemException(findName + ": " + exc.getMessage() + " with " + template, exc);
    }

    private final static String QUERY_FOR_LIST = "AbstractDBMapper.queryForList(";
    private final static String QUERY_FOR_OBJECT = "AbstractDBMapper.queryForObject(";
    private final static String INSERT = "AbstractDBMapper.insert(";
    private final static String UPDATE = "AbstractDBMapper.update(";
    private final static String DELETE = "AbstractDBMapper.delete(";
    private final static String END = "): ";
}