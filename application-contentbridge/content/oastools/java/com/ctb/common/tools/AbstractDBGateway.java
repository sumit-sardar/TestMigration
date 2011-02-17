package com.ctb.common.tools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang.StringUtils;

/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Nov 7, 2003
 * Time: 8:53:52 AM
 * To change this template use Options | File Templates.
 */
public class AbstractDBGateway {
    Connection conn;

    public AbstractDBGateway(Connection conn) {
        this.conn = conn;
    }

    public void executeSqlForResult(
        String sql,
        Object[] arguments,
        DBMapper mapper) {
        sql = getFinalSQLFromArguments(arguments, sql);
        Statement stmt = null;
        ResultSet rslt = null;
        try {
            stmt = conn.createStatement();
            rslt = stmt.executeQuery(sql);

            if (mapper.isObjectAssembleFromMultipleRows()) {
                mapper.assembleObjectFromMultipleRows(rslt);
            } else {
                while (rslt.next())
                    mapper.assembleObjectFromSQLResult(rslt);
            }

        } catch (SQLException e) {
            throw new SystemException(e.getMessage(), e);
        } finally {
            DBConnection.safeClose(stmt, rslt);
        }
    }

    protected void executeDelete(String sql, Object[] arguments)
        throws SQLException {
        if (arguments != null) {
            sql = formatSql(sql, arguments);
        }
        ResultSet rslt = null;
        Statement stmt = null;
        stmt = conn.createStatement();
        stmt.executeUpdate(sql);
        DBConnection.safeClose(stmt, rslt);
    }

    protected Object executeMappingForSingleResult(
        final String WHERE_CLAUSE,
        Object[] arguments,
        DBMapper mapper) {
        executeMapping(WHERE_CLAUSE, arguments, mapper);
        Object[] objects = mapper.getResults();
        if (objects.length > 1)
            throw new SystemException(
                "Business rule constraint about uniqueness violated, more than one item found:"
                    + " "
                    + mapper.getSelectClause()
                    + WHERE_CLAUSE);
        if (objects.length == 0)
            return null;
        return objects[0];
    }

    protected Object[] executeMappingForMultipleResults(
        final String WHERE_CLAUSE,
        Object[] arguments,
        DBMapper mapper) {
        executeMapping(WHERE_CLAUSE, arguments, mapper);
        return mapper.getResults();
    }

    private void executeMapping(
        String whereClause,
        Object[] arguments,
        DBMapper mapper) {
        String sql = mapper.getSelectClause() + whereClause;
        executeSqlForResult(sql, arguments, mapper);
    }

    public int executeUpdateOnObject(
        Object persistentObject,
        DBMapper mapper) {
        int rowsAffected = 0;
        ArgumentsSQLPair[] SQLPairs =
            mapper.getArgumentsAndSQLForUpdate(persistentObject);

        for (int i = 0; i < SQLPairs.length; i++) {
            String sql =
                getFinalSQLFromArguments(
                    SQLPairs[i].getArguments(),
                    SQLPairs[i].getSQL());

            Statement stmt = null;
            try {
                stmt = conn.createStatement();
                rowsAffected = stmt.executeUpdate(sql);
            } catch (SQLException e) {
                throw new SystemException(e.getMessage(), e);
            } finally {
                DBConnection.safeClose(stmt, null);
            }
        }
        return rowsAffected;
    }

    protected String getFinalSQLFromArguments(Object[] arguments, String sql) {
        if (arguments != null) {
            sql = formatSql(sql, arguments);
        }
        return sql;
    }

    //todo - mws - duplicate code in DBConnection
    private static String formatSql(String pattern, Object[] arguments) {
        for (int i = 0; i < arguments.length; i++) {
            Object argument = arguments[i];

            if (argument instanceof String) {
                arguments[i] = escapeForSql((String) argument);
            }
        }
        String sql = Format.format(pattern, arguments);

        return sql;
    }
    //todo - mws - duplicate code in DBConnection
    public static String escapeForSql(String text) {
        return StringUtils.replace(text, "'", "''");
    }

}
