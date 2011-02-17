package com.ctb.common.tools;

import java.sql.ResultSet;

/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Nov 7, 2003
 * Time: 9:04:46 AM
 * To change this template use Options | File Templates.
 */
public interface DBMapper {
    /**
     * builds up the appropriate object from the sql result
     * @param rs
     */
    void assembleObjectFromSQLResult(ResultSet rs);
    /**
     * clears the existing build up objects in the mapper
     */
    void clear();
    /**
     *
     * @return SQL string representing the select a,b,c from table -- does not cover the where clause
     */
    String getSelectClause();
    
    /**
     * gets an array of objects representing the results of a query
     * @return
     */
    Object[] getResults();
    /**
     * updates may require a series of SQL updates on multiple tables
     * @param persistentObject
     * @return array of sql clauses with the arguments from that clause
     */
    ArgumentsSQLPair[] getArgumentsAndSQLForUpdate(Object persistentObject);

    /**
     * Moves iteration logic into the mapper to iterate through multiple rows
     */
    void assembleObjectFromMultipleRows(ResultSet rs);
    
    boolean isObjectAssembleFromMultipleRows();
    
    String[] getKeyColumns();
}
