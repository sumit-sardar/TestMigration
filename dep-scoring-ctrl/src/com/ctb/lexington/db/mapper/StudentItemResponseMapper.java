package com.ctb.lexington.db.mapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ctb.lexington.db.data.StudentItemResponseDetails;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.util.SQLUtil;

public class StudentItemResponseMapper extends AbstractDBMapper {
    private static final String DELETE_NAME = "deleteStudentItemResponse";

    public StudentItemResponseMapper(Connection conn) {
        super(conn);
    }

    public void insert(final StudentItemResponseDetails record) throws CTBSystemException {
        final String sql = "INSERT INTO "
            + "STUDENT_ITEM_RESPONSE_CR "
            + "( "
            + "STUDENT_TEST_HISTORY_ID, "
            + "ITEM_ID, "
            + "ITEM_SET_ID, "
            + "CONSTRUCTED_RESPONSE "
            + ") "
            + "VALUES "
            + "( "
            + "?, "
            + "?, "
            + "?, "
            + "? "
            + ")";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setLong(1, record.getStudentTestHistoryId().longValue());
            ps.setString(2, record.getItemId());
            ps.setLong(3, record.getItemSetId().longValue());
            // TODO hack, hack, hack, clob should work
            //ps.setClob(4, record.getConstructedResponse());
            BufferedReader br = new BufferedReader(record.getConstructedResponse().getCharacterStream());
            StringBuffer cr = new StringBuffer();
            String line;
            while( (line = br.readLine()) != null )
            	cr.append(line);
            ps.setString(4, cr.toString());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CTBSystemException(e.getMessage());
        } catch (IOException e) {
			e.printStackTrace();
            throw new CTBSystemException(e.getMessage());
		} finally {
            SQLUtil.close(ps);
        }
    }

    public void delete(StudentItemResponseDetails record) throws SQLException {
        delete(DELETE_NAME, record);
    }

    public List findByStudentTestHistoryId(Long studentTestHistoryId) throws CTBSystemException {
        final String sql = "SELECT * "
            + "FROM "
            + "STUDENT_ITEM_RESPONSE_CR "
            + "WHERE STUDENT_TEST_HISTORY_ID = ?";
        final List responses = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setLong(1, studentTestHistoryId.longValue());
            rs = ps.executeQuery();
            while (rs.next()) {
                StudentItemResponseDetails response = new StudentItemResponseDetails();
                response.setStudentTestHistoryId(SQLUtil.getLong(rs, "STUDENT_TEST_HISTORY_ID"));
                response.setItemId(SQLUtil.getString(rs, "ITEM_ID"));
                response.setItemSetId(SQLUtil.getInteger(rs, "ITEM_SET_ID"));
                response.setConstructedResponse(SQLUtil.getClob(rs, "CONSTRUCTED_RESPONSE"));
                responses.add(response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CTBSystemException(e.getMessage());
        } finally {
            SQLUtil.close(rs, ps);
        }
        return responses;
    }
}