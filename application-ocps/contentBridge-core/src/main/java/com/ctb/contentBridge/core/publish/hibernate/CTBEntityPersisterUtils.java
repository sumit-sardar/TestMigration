package com.ctb.contentBridge.core.publish.hibernate;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.ctb.contentBridge.core.util.RegexUtils;


/**
 * @author dummy
 */
public class CTBEntityPersisterUtils {
    private static final String INSERT_SQL_EXP =
        "INSERT\\s*INTO\\s*([^\\(\\s]+)\\s*\\(([^)]*)\\)\\s*VALUES\\s*\\(([^)]*)\\)";
    private static final String UPDATE_SQL_EXP =
        "UPDATE\\s*([^\\s]+)\\s*SET(.*)\\s*WHERE\\s*(.*)";

    private static final String SQL_ASSIGN_EXP =
        "\\s*([^\\=\\s]+)\\s*=\\s*([^\\s]+)\\s*";

    private static final String INSERT_SQL_TEMPLATE =
        "insert into <table>( <columns> ) values ( <values> )";

    private static final String UPDATE_SQL_TEMPLATE =
        "update <table> set <sets> where <conditions>";

    /**
     * Replace the columns' values that match the INSERT_SYSDATE_COLUMN with sysdate
     * 
     * @param insertString original insert statement
     */
    public static String modifyInsertSQLForSysdate(
        String insertString,
        List sysdateColumns) {

        String table =
            RegexUtils.getMatchedGroup(INSERT_SQL_EXP, insertString, 1);

        String[] columns =
            StringUtils.split(
                RegexUtils.getMatchedGroup(INSERT_SQL_EXP, insertString, 2),
                ",");

        String[] values =
            StringUtils.split(
                RegexUtils.getMatchedGroup(INSERT_SQL_EXP, insertString, 3),
                ",");

        for (int colIndex = 0; colIndex < columns.length; colIndex++) {
            String columnName = columns[colIndex].trim();
            if (sysdateColumns.contains(columnName)) {
                values[colIndex] = " sysdate";
            }
        }

        String modifiedInsertString =
            StringUtils.replace(INSERT_SQL_TEMPLATE, "<table>", table);
        modifiedInsertString =
            StringUtils.replace(
                modifiedInsertString,
                "<columns>",
                StringUtils.join(columns, ","));
        modifiedInsertString =
            StringUtils.replace(
                modifiedInsertString,
                "<values>",
                StringUtils.join(values, ","));

        return modifiedInsertString;
    }

    /**
    	 * Replace the columns' values that match the UPDATE_SYSDATE_COLUMN with sysdate
    	 * 
    	 * @param updateString original update statement
    	 */
    public static String modifyUpdateSQLForSysdate(
        String updateString,
        List sysdateColumns) {

        String table =
            RegexUtils.getMatchedGroup(UPDATE_SQL_EXP, updateString, 1);

        String[] sets =
            StringUtils.split(
                RegexUtils.getMatchedGroup(UPDATE_SQL_EXP, updateString, 2),
                ",");

        String conditions =
            RegexUtils.getMatchedGroup(UPDATE_SQL_EXP, updateString, 3);

        for (int setIndex = 0; setIndex < sets.length; setIndex++) {
            String columnName =
                RegexUtils
                    .getMatchedGroup(SQL_ASSIGN_EXP, sets[setIndex], 1)
                    .trim();

            if (sysdateColumns.contains(columnName)) {
                sets[setIndex] = " " + columnName + "=sysdate ";
            }
        }

        String modifiedUpdateString =
            StringUtils.replace(UPDATE_SQL_TEMPLATE, "<table>", table);
        modifiedUpdateString =
            StringUtils.replace(
                modifiedUpdateString,
                "<sets>",
                StringUtils.join(sets, ","));
        modifiedUpdateString =
            StringUtils.replace(
                modifiedUpdateString,
                "<conditions>",
                conditions);

        return modifiedUpdateString;
    }

    /**
     * Filter sysdate columns in insert statement by setting the includeProperty for the corresponding
     * column to false
     * 
     * @param sql insert sql statement
     * @param includePropert boolean array indicate the corresponding column should included in the sql generateion
     * @param columnIndex match column name to Hibernate column index.
     */
    public static boolean[] filterColumnsForInsert(
        String sql,
        boolean[] includeProperty,
        Map columnIndex,
        List sysdateColumns) {
        String[] columns =
            StringUtils.split(
                RegexUtils.getMatchedGroup(INSERT_SQL_EXP, sql, 2),
                ",");

        for (int colIndex = 0; colIndex < columns.length; colIndex++) {
            if (sysdateColumns.contains(columns[colIndex].trim())) {
                includeProperty[(
                    (Integer) columnIndex.get(
                        columns[colIndex].toUpperCase().trim()))
                    .intValue()] =
                    false;
            }
        }

        return includeProperty;

    }

    /**
     * Filter sysdate columns in update statement by setting the includeProperty for the corresponding
     * column to false
     * 
     * @param sql update sql statement
     * @param includePropert boolean array indicate the corresponding column should included in the sql generateion
     * @param columnIndex match column name to Hibernate column index.
     */
    public static boolean[] filterColumnsForUpdate(
        String sql,
        boolean[] includeProperty,
        Map columnIndex,
        List sysdateColumns) {

        String[] sets =
            StringUtils.split(
                RegexUtils.getMatchedGroup(UPDATE_SQL_EXP, sql, 2),
                ",");

        for (int setIndex = 0; setIndex < sets.length; setIndex++) {
            String columnName =
                RegexUtils
                    .getMatchedGroup(SQL_ASSIGN_EXP, sets[setIndex], 1)
                    .trim();

            if (sysdateColumns.contains(columnName)) {
                includeProperty[((Integer) columnIndex.get(columnName))
                    .intValue()] =
                    false;
            }
        }

        return includeProperty;
    }

}
