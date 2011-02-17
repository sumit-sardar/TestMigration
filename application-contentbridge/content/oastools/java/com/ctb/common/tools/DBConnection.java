package com.ctb.common.tools;

import java.io.*;
import java.sql.*;
import java.util.*;

import oracle.jdbc.*;
import oracle.sql.*;

import org.apache.commons.lang.*;
import org.apache.log4j.*;

import com.ctb.common.tools.media.*;
import com.ctb.util.Pipe;

public class DBConnection {
    private Connection connection;
    private static Logger logger = Logger.getLogger(DBConnection.class);

    public boolean debug = false;

    public DBConnection(Connection connection) {
        this.connection = connection;
    }

    public void setDebug(boolean val) {
        debug = val;
    }

    public Connection getConnection() {
        return connection;
    }

    public Statement createStatement() throws SQLException {
        return getConnection().createStatement();
    }

    public CallableStatement prepareCall(String sql) throws SQLException {
        return connection.prepareCall(sql);
    }

    public static void safeClose(Statement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException ignore) {
            logger.error("IGNORE: ", ignore);
        }

        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException ignore) {
            logger.error("IGNORE: ", ignore);
        }
    }

    public int executeUpdate(String sql) {
        return executeUpdate(sql, null);
    }

    public int executeUpdate(String sql, Object[] arguments) {
        int rowsAffected = 0;

        if (arguments != null) {
            sql = formatSql(sql, arguments);
        }
        Statement stmt = null;

        try {
            stmt = getConnection().createStatement();
            log(sql);
            rowsAffected = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            logger.error(e.toString() + "\n" + sql);
            throw new SystemException(e.getMessage(), e);
        } finally {
            safeClose(stmt, null);
        }
        return rowsAffected;
    }

    private void log(String sql) {
        if (debug) {
            logger.debug("SQL: " + sql);
        }
    }

    public String executeSqlWithSingleItemReturn(
        String sql,
        Object[] arguments)
        throws SQLException {
        if (arguments != null) {
            sql = formatSql(sql, arguments);
        }
        ResultSet rslt = null;
        Statement stmt = null;
        String result = null;

        try {
            stmt = getConnection().createStatement();
            log(sql);
            rslt = stmt.executeQuery(sql);
            if (rslt.next()) {
                result = rslt.getString(1);
            }
            if (rslt.next()) {
                throw new SystemException(
                    "Result set returned multiple rows  ("
                        + sql
                        + "). Extra data ignored");
            }
        } finally {
            safeClose(stmt, rslt);
        }

        return result;
    }

    public int[] executeSqlWithReturnIntArray(String sql, Object[] arguments)
        throws SQLException {
        if (arguments != null) {
            sql = formatSql(sql, arguments);
        }
        ResultSet rslt = null;
        Statement stmt = null;
        int[] result = new int[3];
        int i = 0;

        try {
            stmt = getConnection().createStatement();
            log(sql);
            rslt = stmt.executeQuery(sql);
            while (rslt.next()) {
                result[i] = rslt.getInt(1);
                i++;
            }
        } finally {
            safeClose(stmt, rslt);
        }
        return result;
    }

    public Iterator selectStrings(String sql, Object[] arguments)
        throws SQLException {
        if (arguments != null) {
            sql = formatSql(sql, arguments);
        }
        ResultSet rslt = null;
        Statement stmt = null;
        List results = new ArrayList();

        try {
            stmt = getConnection().createStatement();
            log(sql);
            rslt = stmt.executeQuery(sql);
            while (rslt.next()) {
                results.add(rslt.getString(1));
            }
        } finally {
            safeClose(stmt, rslt);
        }
        return results.iterator();
    }

    public ResultSet executeQuery(String sql, Object[] arguments)
        throws SQLException {
        if (arguments != null) {
            sql = formatSql(sql, arguments);
        }
        Statement stmt = null;

        stmt = getConnection().createStatement();
        log(sql);
        return stmt.executeQuery(sql);
    }

    public int executeCountQuery(String cmd, Object[] arguments)
        throws SQLException {
        String val = executeSqlWithSingleItemReturn(cmd, arguments);
        int count = Integer.parseInt(val);

        return count;
    }

    public int executeFieldSizeQuery(String table, String col)
        throws SQLException {
        String sql =
            "SELECT DATA_LENGTH FROM ALL_TAB_COLUMNS WHERE UPPER(TABLE_NAME) = '"
                + table.toUpperCase()
                + "' AND UPPER(COLUMN_NAME) = '"
                + col.toUpperCase()
                + "'";
        ResultSet rslt = null;
        Statement stmt = null;
        int result = 0;

        try {
            stmt = getConnection().createStatement();
            log(sql);
            rslt = stmt.executeQuery(sql);
            if (rslt.next()) {
                result = rslt.getInt(1);

            }
        } finally {
            safeClose(stmt, rslt);
        }

        return result;
    }

    public Map selectFields(String selectSql, List fieldNames)
        throws SQLException {
        Map map = new HashMap();
        Statement st = null;
        ResultSet rs = null;

        try {
            st = getConnection().createStatement();
            log(selectSql);
            rs = st.executeQuery(selectSql);
            if (rs.next()) {
                for (Iterator iterator = fieldNames.iterator();
                    iterator.hasNext();
                    ) {
                    String fieldName = (String) iterator.next();
                    String fieldValue = rs.getString(fieldName);

                    map.put(fieldName, fieldValue);
                }
            }
        } finally {
            safeClose(st, rs);
        }
        return map;
    }

    // todo - ??? - this works only if the BLOB is the second column

    public void writeBlobOrClob(String sql,Object bytesOrChars,int column, MediaType mediaType) throws SQLException,
            IOException {
        if (mediaType.getLobType().equals(MediaType.BLOB))
            writeBlob(sql,(byte[])bytesOrChars,column);
        else
            writeClob(sql,(char[])bytesOrChars,column);

    }


    public void writeBlob(String sql,byte[] bytes) throws SQLException, IOException {
        writeBlob(sql,bytes,2);
    }
    
    public void writeBlob(String sql, byte[] bytes,int column)
        throws SQLException, IOException {
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = createStatement();
            log(sql);
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
            safeClose(statement, resultSet);
        }
    }
    public void writeClob(String sql,char[] chars) throws SQLException, IOException {
        writeClob(sql,chars,2);
    }

    public void writeClob(String sql, char[] chars, int column)
        throws SQLException, IOException {
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = createStatement();
            log(sql);
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
            safeClose(statement, resultSet);
        }
    }

    private void writeBlob(BLOB blob, byte[] bytes)
        throws SQLException, IOException {
        OutputStream outstream = blob.getBinaryOutputStream();
        InputStream instream = new ByteArrayInputStream(bytes);

        int chunk = blob.getChunkSize();
        new Pipe(instream, outstream, blob.getChunkSize()).run();

        instream.close();
        outstream.close();
    }

    private void writeClob(CLOB clob, char[] chars)
        throws SQLException, IOException {
        Writer outstream = clob.getCharacterOutputStream();

        outstream.write(chars);
        outstream.close();
    }

    // todo: this works only if the BLOB is the second column

    public byte[] readBlob(String sql) throws SQLException {
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = getConnection().createStatement();
            log(sql);
            resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                if (resultSet instanceof OracleResultSet) {
                    Blob blob = ((OracleResultSet) resultSet).getBLOB(2);

                    long length = blob.length();

                    if (length > Integer.MAX_VALUE) {
                        throw new IllegalArgumentException("Blob too long");
                    }
                    return blob.getBytes(1, (int) length);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } finally {
            safeClose(statement, resultSet);
        }
    }

    public char[] readClob(String sql) throws SQLException {
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = getConnection().createStatement();
            log(sql);
            resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                if (resultSet instanceof OracleResultSet) {
                    CLOB clob = ((OracleResultSet) resultSet).getCLOB(2);

                    long length = clob.length();

                    if (length > Integer.MAX_VALUE) {
                        throw new IllegalArgumentException("Clob too long");
                    }
                    char[] buffer = new char[(int) length];

                    clob.getChars(1, (int) length, buffer);
                    return buffer;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } finally {
            safeClose(statement, resultSet);
        }
    }

    public static String escapeForSql(String text) {
        return StringUtils.replace(text, "'", "''");
    }

    public static String formatSql(String pattern, Object[] arguments) {
        for (int i = 0; i < arguments.length; i++) {
            Object argument = arguments[i];

            if (argument instanceof String) {
                arguments[i] = escapeForSql((String) argument);
            }
        }
        String sql = Format.format(pattern, arguments);

        return sql;
    }

    public long getNextSequenceNumber(String SequenceName)
        throws SQLException {
        String value =
            executeSqlWithSingleItemReturn(
                "select " + SequenceName + ".nextval from dual",
                null);

        return Long.parseLong(value);
    }
}
