package com.ctb.contentBridge.core.publish.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.jdbc.driver.OracleStatement;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.engine.SessionFactoryImplementor;
import net.sf.hibernate.engine.SessionImplementor;
import net.sf.hibernate.mapping.PersistentClass;
import net.sf.hibernate.persister.ClassPersister;
import net.sf.hibernate.persister.EntityPersister;

/**
 * @author wmli
 * modify to utilize sysdate in datetime file
 * 
 */
public class CTBEntityPersister
    extends EntityPersister
    implements ClassPersister {

    private static final List INSERT_SYSDATE_COLUMN =
        new ArrayList(
            Arrays.asList(
                new String[] { "CREATED_DATE_TIME", "UPDATED_DATE_TIME" }));
    private static final List UPDATE_SYSDATE_COLUMN =
        new ArrayList(Arrays.asList(new String[] { "UPDATED_DATE_TIME" }));

    /**
     * Special persister to handle sysdate.
     * CREATED_DATE_TIME and UPDATED_DATE_TIME are replaced by sysdate in INSERT statement.
     * UPDATED_DATE_TIME is replaced by sysdate in UPDATE statement.
     * 
     * The replace is a two phase process:
     * 1) replace the ? with sysdate in the generate sql string for specific column
     * 		modify generateInsertString and generateUpdateString method to carry out replacement.
     * 2) skip setValue in the statement for the replaced columns
     * 		modify the dehydrate method to skip setting column values.
     */
    public CTBEntityPersister(
        PersistentClass model,
        SessionFactoryImplementor factory)
        throws HibernateException {
        super(model, factory);
    }

    /**
     * Override EntityPersister's generateInsertString statement.
     */
    protected String generateInsertString(
        boolean identityInsert,
        boolean[] includeProperty) {
        String insertString =
            super.generateInsertString(identityInsert, includeProperty);
        insertString =
            CTBEntityPersisterUtils.modifyInsertSQLForSysdate(
                insertString,
                INSERT_SYSDATE_COLUMN);
        return insertString;
    }

    /**
     * Override EntityPersister's generateUpdateString statement.
     */
    protected String generateUpdateString(
        boolean[] includeProperty,
        Object[] oldFields) {
        String updateString =
            super.generateUpdateString(includeProperty, oldFields);
        updateString =
            CTBEntityPersisterUtils.modifyUpdateSQLForSysdate(
                updateString,
                UPDATE_SYSDATE_COLUMN);
        return updateString;
    }

    /**
     * Filter out sysdate columns so that the value will not be set for the columns
     */
    protected int dehydrate(
        Serializable id,
        Object[] fields,
        boolean[] includeProperty,
        PreparedStatement st,
        SessionImplementor session)
        throws SQLException, HibernateException {

        return super.dehydrate(
            id,
            fields,
            filterProperties(
                ((OracleStatement) st).getOriginalSql(),
                includeProperty),
            st,
            session);

    }

    /**
     * Filter sysdate columns for insert or update statement by setting the includeProperty for
     * the column to false
     * 
     * @param sql sql statement
     * @param includeProperty boolean array indicate the corresponding column should included in the sql generateion
     */
    private boolean[] filterProperties(String sql, boolean[] includeProperty) {
        Map columnIndex = getColumnIndexMap();

        if (sql.toUpperCase().indexOf("INSERT") != -1) {
            return CTBEntityPersisterUtils.filterColumnsForInsert(
                sql,
                includeProperty,
                columnIndex,
                INSERT_SYSDATE_COLUMN);
        } else if (sql.toUpperCase().indexOf("UPDATE") != -1) {
            return CTBEntityPersisterUtils.filterColumnsForUpdate(
                sql,
                includeProperty,
                columnIndex,
                UPDATE_SYSDATE_COLUMN);
        }

        return includeProperty;
    }

    /**
     * Create a map that match column name to the corresponding index used by Hibernate
     */
    private Map getColumnIndexMap() {
        Map columnIndexMap = new HashMap();
        for (int i = 0; i < getHydrateSpan(); i++) {
            columnIndexMap.put(
                getActualPropertyColumnNames(i)[0].toUpperCase(),
                new Integer(i));
        }

        return columnIndexMap;
    }

}
