package com.ctb.common.tools;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import oracle.jdbc.driver.OracleResultSet;
import oracle.sql.BLOB;
import oracle.sql.CLOB;

import org.apache.commons.lang.StringUtils;

import com.ctb.util.Pipe;
import com.ctb.util.RegexUtils;

/**
 * @author wmli
 */
public class DBLOBGateway extends AbstractDBGateway {
    public DBLOBGateway(Connection conn) {
        super(conn);
    }

    public int executeUpdateOnObject(
        Object persistentObject,
        DBMapper mapper) {

        ArgumentsSQLPair[] SQLPairs =
            mapper.getArgumentsAndSQLForUpdate(persistentObject);

        return executeUpdate(SQLPairs, mapper);
    }

    public int executeUpdate(ArgumentsSQLPair[] SQLPairs, DBMapper mapper) {
        int rowsAffected = 0;

        for (int i = 0; i < SQLPairs.length; i++) {
            // if any of the argument is LOB, replace the LOB with "empty_clob" or "empty_blob"
            ArgumentsSQLPair sqlPair = SQLPairs[i];

            String sql =
                getFinalSQLFromArguments(
                    filterLOB(sqlPair.getArguments()),
                    sqlPair.getSQL());

            Statement stmt = null;
            try {
                stmt = conn.createStatement();
                rowsAffected = stmt.executeUpdate(sql);
            } catch (SQLException e) {
                throw new SystemException(e.getMessage(), e);
            } finally {
                DBConnection.safeClose(stmt, null);
            }

            executeUpdateLOB(sqlPair, mapper);
        }
        return rowsAffected;
    }

    /**
     * filter out the byte[] and char[] argument and replace them with call to the
     * proper locator initialization method (empty_blob and empty_clob)
     * 
     * @param arguments list of all the argument for the sql
     */
    private Object[] filterLOB(Object[] arguments) {
        Object[] filteredArguements = new Object[arguments.length];
        for (int arguIdx = 0; arguIdx < arguments.length; arguIdx++) {
            Object argument = arguments[arguIdx];
            if (argument instanceof byte[]) {
                filteredArguements[arguIdx] = "empty_blob()";
            } else if (argument instanceof char[]) {
                filteredArguements[arguIdx] = "empty_clob()";
            } else {
                filteredArguements[arguIdx] = argument;
            }
        }
        return filteredArguements;
    }

    /**
     * update LOB
     * 
     * @param sqlPair the sql to update the LOB
     * @param mapper database mapper
     */
    private void executeUpdateLOB(ArgumentsSQLPair sqlPair, DBMapper mapper) {
        // extract all LOB column and generate corresponding sql statement
        List LOBSqls =
            extractLOB(sqlPair.getSQL(), sqlPair.getArguments(), mapper);

        // execute LOB update sql statement
        for (Iterator LOBSqlIter = LOBSqls.iterator(); LOBSqlIter.hasNext();) {
            ArgumentsSQLPair argumentSQLPair =
                (ArgumentsSQLPair) LOBSqlIter.next();

            try {
                writeBlobOrClob(
                    argumentSQLPair.getSQL(),
                    argumentSQLPair.getArguments()[0],
                    1);
            } catch (Exception e) {
                throw new SystemException("Cannot write LOB");
            }
        }
    }

    private List extractLOB(String sql, Object[] arguments, DBMapper mapper) {
        String INSERT_SQL_EXP =
            "INSERT\\sINTO\\s([^\\(\\s]+)\\s\\(([^)]*)\\)\\sVALUES\\s\\(([^)]*)\\)";
        String UPDATE_SQL_EXP = "UPDATE\\s([^\\s]+)\\sSET(.*)\\sWHERE\\s(.*)";

        String table = null;
        String[] columns = null;
        String[] sqlArguments = null;
        Map columnValues = null;

        // extract the value list and the arguments for insert statement
        if (RegexUtils.match(INSERT_SQL_EXP, sql)) {
            table = RegexUtils.getMatchedGroup(INSERT_SQL_EXP, sql, 1);
            columns =
                StringUtils.split(
                    RegexUtils.getMatchedGroup(INSERT_SQL_EXP, sql, 2),
                    ",");
            sqlArguments =
                StringUtils.split(
                    RegexUtils.getMatchedGroup(INSERT_SQL_EXP, sql, 3),
                    ",");

            // extract the value list and the arguments for update statement
        } else if (RegexUtils.match(UPDATE_SQL_EXP, sql)) {
            table = RegexUtils.getMatchedGroup(UPDATE_SQL_EXP, sql, 1);
            String[] setClause =
                StringUtils.split(
                    RegexUtils.getMatchedGroup(UPDATE_SQL_EXP, sql, 2),
                    ",");
            String[] whereClause =
                StringUtils.split(
                    RegexUtils.getMatchedGroup(UPDATE_SQL_EXP, sql, 3),
                    "and");

            // convert the set and where portion of the update statement to 
            columns = getGroupFromSqlAssignment(setClause, whereClause, 1);
            sqlArguments = getGroupFromSqlAssignment(setClause, whereClause, 2);

        }

        columnValues = getColumnValueMap(columns, sqlArguments, arguments);

        // create ArgumentsSQLPair for each LOB argument
        return createSQLArgumentPairForLOB(
            table,
            mapper,
            arguments,
            columns,
            sqlArguments,
            columnValues);
    }

    /**
     * create SQLArugmentPair for each LOB arguement
     * 
     * @param table name of the target table
     * @param mapper database mapper
     * @param arguemnts list of argument values
     * @param columns name of the columns
     * @param sqlArgument argument in the sql
     * @param columnValues map of column name to the actual values.
     */
    private List createSQLArgumentPairForLOB(
        String table,
        DBMapper mapper,
        Object[] arguments,
        String[] columns,
        String[] sqlArguments,
        Map columnValues) {
        List LOBsqls = new ArrayList();

        for (int arguIdx = 0; arguIdx < arguments.length; arguIdx++) {
            Object argument = arguments[arguIdx];

            if ((argument instanceof byte[]) || (argument instanceof char[])) {
                // find the index of the arg in the sql argument list
                String col =
                    findColumnNameForArgument(columns, sqlArguments, arguIdx);

                if (col != null) {
                    ArgumentsSQLPair argumentSQLPair =
                        new ArgumentsSQLPair(
                            new Object[] { argument },
                            "select "
                                + col
                                + " from "
                                + table
                                + getWhereClause(mapper, columnValues)
                                + " for update");
                    LOBsqls.add(argumentSQLPair);
                } else {
                    throw new SystemException(
                        "Cannot match column with LOB for argument #: "
                            + arguIdx);
                }
            }
        }

        return LOBsqls;
    }

    /**
     * extract column name and arguments from the sql set clause and where clause. Each assignment is
     * break into colunmn and value pair.
     * 
     * @param setClause set portion of the sql
     * 
     * 
     */
    private String[] getGroupFromSqlAssignment(
        String[] setClause,
        String[] whereClause,
        int group) {
        List result = new ArrayList();
        final String ASSIGNMENT_EXP = "\\s*([^\\s=]+)\\s*=\\s*(.*)";

        getGroupFromSqlAssignment(setClause, group, result, ASSIGNMENT_EXP);
        getGroupFromSqlAssignment(whereClause, group, result, ASSIGNMENT_EXP);

        return (String[]) result.toArray(new String[result.size()]);
    }

    private void getGroupFromSqlAssignment(
        String[] sqlSection,
        int group,
        List groups,
        final String ASSIGNMENT_EXP) {
        for (int i = 0; i < sqlSection.length; i++) {
            String groupStr =
                RegexUtils.getMatchedGroup(
                    ASSIGNMENT_EXP,
                    sqlSection[i],
                    group);

            if (groupStr != null)
                groups.add(groupStr);
        }
    }

    // TODO - sli - duplicate code from DBConnection
    public void writeBlobOrClob(String sql, Object bytesOrChars, int column)
        throws SQLException, IOException {
        if (bytesOrChars instanceof byte[])
            writeBlob(sql, (byte[]) bytesOrChars, column);
        else
            writeClob(sql, (char[]) bytesOrChars, column);
    }

    //	TODO - sli - duplicate code from DBConnection
    public void writeBlob(String sql, byte[] bytes, int column)
        throws SQLException, IOException {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = conn.createStatement();
            resultSet = statement.executeQuery(sql);
            BLOB blob;
            if (resultSet.next()) {
                if (resultSet instanceof OracleResultSet) {
                    blob = ((OracleResultSet) resultSet).getBLOB(column);
                    if (bytes.length < blob.length()) {
                        blob.trim(bytes.length);
                    }

                    writeBlob(blob, bytes);
                }
            } else {
                throw new RuntimeException("Didn't insert the BLOB value");
            }
        } finally {
            DBConnection.safeClose(statement, resultSet);
        }
    }

    //	TODO - sli - duplicate code from DBConnection
    public void writeClob(String sql, char[] chars, int column)
        throws SQLException, IOException {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = conn.createStatement();
            resultSet = statement.executeQuery(sql);
            CLOB clob;
            if (resultSet.next()) {
                if (resultSet instanceof OracleResultSet) {
                    clob = ((OracleResultSet) resultSet).getCLOB(column);
                    if (chars.length < clob.length()) {
                        clob.trim(chars.length);
                    }

                    writeClob(clob, chars);
                }
            } else {
                throw new RuntimeException("Didn't insert the CLOB value");
            }
        } finally {
            DBConnection.safeClose(statement, resultSet);
        }
    }

    //	TODO - sli - duplicate code from DBConnection
    private void writeBlob(BLOB blob, byte[] bytes)
        throws SQLException, IOException {
        OutputStream outstream = blob.getBinaryOutputStream();
        InputStream instream = new ByteArrayInputStream(bytes);
        int chunk = blob.getChunkSize();
        new Pipe(instream, outstream, blob.getChunkSize()).run();
        instream.close();
        outstream.close();
    }

    //	TODO - sli - duplicate code from DBConnection
    private void writeClob(CLOB clob, char[] chars)
        throws SQLException, IOException {
        Writer outstream = clob.getCharacterOutputStream();
        outstream.write(chars);
        outstream.close();
    }

    public Map getColumnValueMap(
        String[] columns,
        String[] sqlArguments,
        Object[] arguments) {
        Map columnValues = new HashMap();

        for (int arguIdx = 0; arguIdx < arguments.length; arguIdx++) {
            Object argument = arguments[arguIdx];
            if (argument == null) {
                continue;
            } else {
                // find the index of the arg in the sql argument list
                String col = null;
                col = findColumnNameForArgument(columns, sqlArguments, arguIdx);

                if (col != null) {
                    columnValues.put(
                        StringUtils.trim(col).toUpperCase(),
                        argument);
                } else {
                    throw new SystemException(
                        "Cannot match column with LOB for argument #: "
                            + arguIdx);
                }
            }
        }

        return columnValues;
    }

    private String findColumnNameForArgument(
        String[] columns,
        String[] sqlArguments,
        int arguIdx) {
        for (int i = 0; i < sqlArguments.length; i++) {
            if (sqlArguments[i].indexOf("{" + arguIdx + "}") != -1) {
                return columns[i];
            }
        }

        return null;
    }

    /**
     * generate where clause using the key column defined in the mapper
     * 
     * @param mapper table mapper.
     * @columnValues mapper of column name to the actual values.
     */
    public String getWhereClause(DBMapper mapper, Map columnValues) {
        // cannot procceed if key columns are not defined in the mapper
        if (mapper.getKeyColumns() == null) {
            return "";
        }

        StringBuffer whereClause = new StringBuffer();
        String[] keyColumn = mapper.getKeyColumns();

        for (int i = 0; i < keyColumn.length; i++) {
            String columnName = keyColumn[i];
            Object value = columnValues.get(columnName.toUpperCase());

            buildWhereClauseForColumn(whereClause, columnName, value);
        }

        return whereClause.toString();
    }

    private void buildWhereClauseForColumn(
        StringBuffer whereClause,
        String columnName,
        Object value) {
        if (value != null) {
            // add the conjunction
            if (whereClause.length() == 0)
                whereClause.append(" where ");
            else
                whereClause.append(" and ");
        
            // add quote for string attribute
            if (value instanceof String) {
                whereClause.append(columnName);
                whereClause.append("='");
                whereClause.append(value);
                whereClause.append("'");
            } else {
                whereClause.append(columnName);
                whereClause.append("=");
                whereClause.append(value);
            }
        }
    }
}
