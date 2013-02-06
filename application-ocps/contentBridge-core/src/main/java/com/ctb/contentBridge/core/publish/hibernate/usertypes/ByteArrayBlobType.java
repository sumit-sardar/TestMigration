package com.ctb.contentBridge.core.publish.hibernate.usertypes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.UserType;

import com.ctb.contentBridge.core.util.Pipe;

import oracle.jdbc.OracleResultSet;
import oracle.sql.BLOB;

/**
 * @author wmli
 */
public class ByteArrayBlobType implements UserType {

    public int[] sqlTypes() {
        return new int[] { Types.BLOB };
    }

    public Class returnedClass() {
        return byte[].class;
    }

    public boolean equals(Object x, Object y) {
        return (x == y)
            || (x != null
                && y != null
                && java.util.Arrays.equals((byte[]) x, (byte[]) y));
    }

    public Object nullSafeGet(ResultSet rs, String[] names, Object owner)
        throws HibernateException, SQLException {
        BLOB blob = ((OracleResultSet) rs).getBLOB(names[0]);

        if (blob == null)
            return null;

        long length = blob.length();

        if (length > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Blob too long");
        }
        return blob.getBytes(1, (int) length);
    }

    public void nullSafeSet(PreparedStatement st, Object value, int index)
        throws HibernateException, SQLException {

        byte[] bytes = (byte[]) value;

        BLOB blob =
            oracle.sql.BLOB.createTemporary(
                st.getConnection(),
                false,
                oracle.sql.BLOB.DURATION_SESSION);

        blob.open(BLOB.MODE_READWRITE);

        if (bytes.length < blob.length()) {
            blob.trim(bytes.length);
        }

        OutputStream out = blob.getBinaryOutputStream();

        try {
            //out.write((byte[]) value);
            InputStream in = new ByteArrayInputStream(bytes);

            int chunk = blob.getChunkSize();
            new Pipe(in, out, blob.getChunkSize()).run();

            out.flush();

            in.close();
            out.close();
        } catch (IOException e) {
            throw new SQLException("failed write to blob" + e.getMessage());
        }

        blob.close();

        ((oracle.jdbc.OraclePreparedStatement) st).setBLOB(index, blob);
    }

    public Object deepCopy(Object value) {
        if (value == null)
            return null;

        byte[] bytes = (byte[]) value;
        byte[] result = new byte[bytes.length];
        System.arraycopy(bytes, 0, result, 0, bytes.length);

        return result;
    }

    public boolean isMutable() {
        return true;
    }

}
