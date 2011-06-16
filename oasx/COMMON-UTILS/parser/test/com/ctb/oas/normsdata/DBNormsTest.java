package com.ctb.oas.normsdata;

import junit.framework.TestCase;
import oracle.jdbc.pool.OracleDataSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * This class is for testing norms data in the database
 *
 * @author Sreenivas  Ananthakrishna
 */
public class DBNormsTest extends TestCase {
    File terraNovaInput = new File("test/data/db_test/terraNova_input.csv");
    File tabeInput = new File("test/data/db_test/tabe_input.csv");
    private static final String PROPERTIES_FILE = "conf/schema.properties";
    private static final List MANDATORY_COLUMNS = new ArrayList();
    private boolean testPassed = true;

    protected void setUp() throws Exception {
        super.setUp();
        assertTrue(terraNovaInput.exists());
        assertTrue(tabeInput.exists());
        initMandatoryColumnsList();
    }

    private void initMandatoryColumnsList() {
        MANDATORY_COLUMNS.add("FRAMEWORK_CODE");
        MANDATORY_COLUMNS.add("NORM_YEAR");
        MANDATORY_COLUMNS.add("SOURCE_SCORE_TYPE_CODE");
        MANDATORY_COLUMNS.add("DEST_SCORE_TYPE_CODE");
        MANDATORY_COLUMNS.add("SOURCE_SCORE_VALUE");
        MANDATORY_COLUMNS.add("DEST_SCORE_VALUE");
    }


    public void testTeraNovaData() {
        testData(tabeInput);
    }

    public void testTabeData() {
        testData(terraNovaInput);
    }

    private void testData(File inputFile) {
        try {
            System.out.println("Running test for " + inputFile.getName());
            LineNumberReader reader = new LineNumberReader(new FileReader(inputFile));
            String[] columns = reader.readLine().split(",");
            final String sql = getSQL(columns);
            String line = null;
            Connection connection = getConnection();
            testConnection(connection);
            PreparedStatement statement = connection.prepareStatement(sql);

            while ((line = reader.readLine()) != null) {
                if (line.trim().length() == 0 || line.trim().startsWith("#"))
                    continue;
                setStatementParams(line, columns, statement);
                ResultSet rs = statement.executeQuery();

                int resultCount = 0;

                if (rs.next())
                    resultCount = rs.getInt(1);

                if (resultCount != 1) {
                    System.out.println("result failed for query at line # " + reader.getLineNumber() + " expected count 1: actual:" + resultCount);
                    testPassed = false;
                }
            }

            if (!testPassed)
                fail("missing score(s)!!");
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setStatementParams(String line, String[] columns, PreparedStatement statement) throws SQLException {
        String[] input = line.split(",");
        int paramIndex = 1;
        for (int i = 0; i < columns.length; i++) {
            if (MANDATORY_COLUMNS.contains(columns[i])) {
                statement.setString(paramIndex++, input[i].trim());
            }
            else {
                final String value = getValue(input[i]);
                statement.setString(paramIndex++, value);
                statement.setString(paramIndex++, value);
            }
        }
    }

    private String getValue(String s) {
        if (s.length() == 0)
            return "%";
        return
                s;
    }


    private void testConnection(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            statement.executeQuery("select dummy from dual");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() {
        try {
            final Properties properties = new Properties();
            final String URL = "jdbc_url";

            properties.load(new FileInputStream(PROPERTIES_FILE));
            final OracleDataSource dataSource = new OracleDataSource();
            dataSource.setURL(properties.getProperty(URL));
            return dataSource.getConnection();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private String getSQL(String[] columns) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT COUNT(*) FROM SCORE_LOOKUP WHERE ");
        for (int i = 0; i < columns.length; i++) {
            if (i != 0)
                buffer.append(" AND ");

            if (MANDATORY_COLUMNS.contains(columns[i])) {
                buffer.append(columns[i]);
                buffer.append(" = ?");
            }
            else {
                buffer.append("(");
                buffer.append(columns[i]);
                buffer.append("=?");
                buffer.append(" OR ");
                buffer.append("?='%')");
            }
        }
        return buffer.toString();
    }
}
