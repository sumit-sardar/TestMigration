package com.ctb.contentBridge.core.publish.hibernate.usertypes;

import java.io.IOException;
import java.io.Writer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.UserType;

import oracle.jdbc.OracleResultSet;
import oracle.sql.CLOB;

/**
 * @author wmli
 */
public class CharArrayClobType implements UserType {

    public int[] sqlTypes() {
        return new int[] { Types.CLOB };
    }

    public Class returnedClass() {
        return byte[].class;
    }

    public boolean equals(Object x, Object y) {
        return (x == y)
            || (x != null
                && y != null
                && java.util.Arrays.equals((char[]) x, (char[]) y));
    }

    public Object nullSafeGet(ResultSet rs, String[] names, Object owner)
        throws HibernateException, SQLException {
        CLOB clob = ((OracleResultSet) rs).getCLOB(names[0]);

        if (clob == null)
            return null;

        long length = clob.length();

        if (length > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Clob too long");
        }
        char[] buffer = new char[(int) length];

        clob.getChars(1, (int) length, buffer);
        return buffer;
    }

    public void nullSafeSet(PreparedStatement st, Object value, int index)
        throws HibernateException, SQLException {
        char[] chars = (char[]) value;

        CLOB clob =
            oracle.sql.CLOB.createTemporary(
                st.getConnection(),
                false,
                oracle.sql.CLOB.DURATION_SESSION);
        clob.open(CLOB.MODE_READWRITE);

        if (chars.length < clob.length()) {
            clob.trim(chars.length);
        }

        Writer out = clob.getCharacterOutputStream();

        try {
            out.write(chars);
            out.close();
        } catch (IOException e) {
            throw new SQLException("failed write to clob" + e.getMessage());
        }

        clob.close();

        ((oracle.jdbc.OraclePreparedStatement) st).setCLOB(index, clob);
    }

    public Object deepCopy(Object value) {
        if (value == null)
            return null;

        char[] chars = (char[]) value;
        char[] result = new char[chars.length];
        System.arraycopy(chars, 0, result, 0, chars.length);

        return result;
    }

    public boolean isMutable() {
        return true;
    }

}
