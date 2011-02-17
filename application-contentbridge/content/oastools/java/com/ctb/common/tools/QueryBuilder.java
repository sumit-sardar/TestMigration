package com.ctb.common.tools;


import java.io.*;
import java.util.*;
import org.apache.log4j.*;


/**
 * This class is a utility class to load the sql queries in to memory at the start
 * of the application.
 */
public class QueryBuilder {
    private static Properties preparedQueries = new Properties();
	private static Logger logger = Logger.getLogger(QueryBuilder.class);

    /**
     * Do not let anyone instantiate this object. All the queries are loaded
     * in a static block at the start of the application
     */
    private QueryBuilder() {}
    // This block loads all the queries in to memory at the time of class load.
    static {
        // Load Queries once
        loadQueries();
    }

    /**
     * This method is to load all the queries at once and has no public
     * access thus preventing repeated calls to this method.
     * Note: This method assumes the query.properties
     * file is located under /export/cim directory of this class file
     *
     */
    private static void loadQueries() {
        // Read properties file.
        Properties properties = new Properties();
        String cimDirectory = "/export/cim/";
        String propertiesNm = cimDirectory + "queries.properties";

        try {
            properties.load(new FileInputStream(propertiesNm));
        } catch (IOException e) {
            logger.error("", e);
        }
        Enumeration keys = properties.keys();

        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            String value = (String) properties.get(key);

            preparedQueries.put(key, new QueryTokens(value));
        }

    }

    /**
     * This method builds the query based on the parameters of the query. An
     * exception will be thrown if for any reason the query could not be built.  *
     * @ param query name String
     * @ param params Properties :The parameters to inserted in the query.
     * @ return The complete query String
     */

    public static String getPreparedQuery(String queryName, Properties params) throws Exception {
        try {
            QueryTokens qt = (QueryTokens) preparedQueries.get(queryName);

            if (qt == null) {
                logger.error("Query does not exist ");
            }
            return qt.getPreparedQuery(params);

        } catch (NullPointerException ne) {
			logger.error("", ne);
            throw new Exception("Query " + queryName + " is not loaded.");
        } catch (Exception qe) {
            logger.error("", qe);
            throw new Exception("Wrong or null parameter passed to Query "
                    + queryName + "\n" + qe);
        }
    } // end getPreparedQuery

} // end QueryBuilder
