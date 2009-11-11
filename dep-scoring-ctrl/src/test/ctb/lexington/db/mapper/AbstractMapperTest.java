package test.ctb.lexington.db.mapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.naming.NamingException;

import test.ctb.CTBBaseTestCase;

import com.ctb.lexington.db.JunitConnectionProvider;
import com.ctb.lexington.domain.score.ConnectionProvider;

public class AbstractMapperTest extends CTBBaseTestCase {
    private ConnectionProvider junitConnProvider;
    protected Connection conn;

    /**
     * Constructor for AbstractMapperTest.
     *
     * @param name
     */
    public AbstractMapperTest(String name) {
        super(name);
    }

    protected void tearDown() throws Exception {
        try {
            // rollback and close the connection
            getConnectionProvider().close(true, conn);
        } finally {
            super.tearDown();
        }
    }

    protected Connection getOASConnection() throws NamingException, SQLException {
        return getConnectionProvider().getOASConnection();
    }
    
    protected Connection getIRSConnection() throws NamingException, SQLException {
        return getConnectionProvider().getIRSConnection();
    }

    private ConnectionProvider getConnectionProvider() {
        if (junitConnProvider == null) {
            junitConnProvider = new JunitConnectionProvider();
        }
        return junitConnProvider;
    }

    protected void executeAndCommitSql(final String sql) throws SQLException {
  		//System.out.println("sql: " + sql);
        final Statement stmt = conn.createStatement();

        try {
            stmt.execute(sql);
            // dont commit here - since the mappers dont commit automatically
        } finally {
            stmt.close();
        }
    }

    protected void assertFindMany(List resultList) {
        assertNotNull(resultList);
        assertTrue(resultList.size() > 0);
    }
}

