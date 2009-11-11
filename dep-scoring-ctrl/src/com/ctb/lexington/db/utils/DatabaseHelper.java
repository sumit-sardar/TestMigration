package com.ctb.lexington.db.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import com.ctb.lexington.util.CTBConstants;

public class DatabaseHelper {
    private DatabaseHelper() {
        // private ctor so that no one instantiates this class
    }

    public static void setString(PreparedStatement ps, int i, String string) throws SQLException {
        if (string == null) {
            ps.setNull(i, Types.VARCHAR);
        } else
            ps.setString(i, string);
    }

    public static void setStringValue(String sql, String val, Statement s, int i) {
        if (val == null) {
            sql = val + " is null ";
        } else {
            sql = val + "=val ";
        }
    }

    /**
     * @param truthFlag
     * @return
     */
    public static boolean isTrue(String truthFlag) {
        return CTBConstants.TRUE.equalsIgnoreCase(truthFlag);
    }

    public static Integer asInteger(final Long number) {
        if(number == null)
            return null;
        return new Integer(number.intValue());
    }

    public static Long asLong(final Integer number) {
        if(number == null)
            return null;
        return new Long(number.longValue());
    }
}