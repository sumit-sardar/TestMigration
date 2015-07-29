package com.mhe.ctb.oas.BMTSync.spring;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mhe.ctb.oas.BMTSync.exception.ServerException;
import com.mhe.ctb.oas.BMTSync.spring.jms.LoggingOnlyScoringQueue;
import com.mhe.ctb.oas.BMTSync.spring.jms.ScoringQueue;
import com.mhe.ctb.oas.BMTSync.spring.jms.WeblogicScoringQueue;

/**
 * Spring configuration in Java format. Specifically used to create a data source for DAOs that can depend on test versus QA/Prod.
 * @author kristy_tracer
 */
@Configuration
public class DataSourceConfiguration {

    private static final Logger logger = Logger.getLogger(DataSourceConfiguration.class);
    
    private DataSource writeOnceDataSource;
    
    private ScoringQueue writeOnceScoringQueue;
    
    /**
     * DataSource bean, returns a direct connection if on desktop, or a Weblogic connection if on QA/Prod.
     * @return DataSource representing a connection to the database.
     * @throws ServerException If anything goes wrong, blocking starting.
     */
	@Bean
	public DataSource oasBmtSyncDataSource() throws ServerException {
		if (writeOnceDataSource != null) {
			return writeOnceDataSource;
		}
		
        final String query = "select 1 from dual";
        Context envContext;
        JdbcTemplate jdbcTemplate;
        
        try {
        	// Try Weblogic JNDI context
        	envContext = new InitialContext();
            final DataSource weblogicDS = (DataSource)envContext.lookup("oasDataSource");
            jdbcTemplate = new JdbcTemplate(weblogicDS);
            jdbcTemplate.queryForObject(query, Integer.class);
            logger.info("[DataSource] Using Weblogic JNDI syntax");
            writeOnceDataSource = weblogicDS;
            return weblogicDS;
        } catch (Exception e) {
            logger.info("[DataSource] Couldn't lookup oasBmtSyncDataSource or execute test SQL using Weblogic JNDI syntax: " + e.getMessage());
        }
     
        // Default to direct database access, only used for desktop testing.
        BasicDataSource remoteDS = new BasicDataSource();
        remoteDS.setDriverClassName("oracle.jdbc.OracleDriver");
        remoteDS.setUrl("jdbc:oracle:thin:@//nj09mhe0394-vip.mhe.mhc:1521/OASR5T");
        remoteDS.setUsername("oas");
        remoteDS.setPassword("qoasr5");
        logger.info("[DataSource] Using direct database access via JDBC driver.");
        writeOnceDataSource = remoteDS;
        return remoteDS;
	}
	
	@Bean
	public ScoringQueue scoringQueue() throws ServerException {
		if (writeOnceScoringQueue != null) {
			return writeOnceScoringQueue;
		}
		
		ScoringQueue sq;
		// If we're in Weblogic context, we want to read the resource bundle. Otherwise, we want to log and return null.
        try {
        	// Try Weblogic JNDI context
            final String query = "select 1 from dual";
            Context envContext = new InitialContext();
            final DataSource weblogicDS = (DataSource)envContext.lookup("oasDataSource");
            JdbcTemplate jdbcTemplate = new JdbcTemplate(weblogicDS);
            jdbcTemplate.queryForObject(query, Integer.class);
            logger.info("[ScoringQueue] Successfully used Weblogic context!");
        } catch (Exception e) {
            logger.info("[ScoringQueue] Failed to use Weblogic context; creating dummy scoring object.");
            sq = new LoggingOnlyScoringQueue();
            writeOnceScoringQueue = sq;
            return sq;
        }

		FileInputStream file = null;
		ResourceBundle rb = null;
		try {
			file = new FileInputStream("/local/apps/oas/irs/etc/security.properties");
		} catch (final FileNotFoundException fnfe) {
			logger.error("[PropertyResourceBundle] Can't find security.properties file; creating dummy scoring object.", fnfe);
            sq = new LoggingOnlyScoringQueue();
            writeOnceScoringQueue = sq;
            return sq;
		} finally {
			try {
				if (file != null) {
					file.close();
				}
			} catch (final IOException ioe) {
				logger.error("[PropertyResourceBundle] IOException on trying to close file; creating dummy scoring object.", ioe);
	            sq = new LoggingOnlyScoringQueue();
	            writeOnceScoringQueue = sq;
	            return sq;
			}
		}
		try {
			rb = new PropertyResourceBundle(file);		
		} catch (final IOException ioe) {
			logger.error("[PropertyResourceBundle] IOException on trying to read properties; creating dummy scoring object.", ioe);
		    sq = new LoggingOnlyScoringQueue();
		    writeOnceScoringQueue = sq;
		    return sq;
		}
        logger.info("[ScoringQueue] Using IRS security properties file to load scoring queue information.");
        try {
    		Hashtable<String,String> env = new Hashtable<String,String>();
    		env.put(Context.INITIAL_CONTEXT_FACTORY, rb.getString("jndiFactory"));
    		env.put(Context.PROVIDER_URL, rb.getString("jmsURL"));
    		env.put("java.naming.security.principal", rb.getString("jmsPrincipal"));
    		env.put("java.naming.security.credentials", rb.getString("jmsCredentials"));
    		InitialContext ic = new InitialContext(env);		
        	sq = new WeblogicScoringQueue();
        	sq.init(ic, rb.getString("jmsFactory"), rb.getString("jmsQueue"));
        	ic.close();
        } catch (final NamingException ne) {
        	logger.warn("[ScoringQueue] Unable to create ScoringQueue object: " + ne.getMessage() + "; creating dummy scoring object.", ne);
            sq = new LoggingOnlyScoringQueue();
        } catch (final JMSException jmse) {
        	logger.warn("[ScoringQueue] Unable to create ScoringQueue object: " + jmse.getMessage() + "; creating dummy scoring object.", jmse);
            sq = new LoggingOnlyScoringQueue();
        }
    	writeOnceScoringQueue = sq;
    	return sq;
    }
}
