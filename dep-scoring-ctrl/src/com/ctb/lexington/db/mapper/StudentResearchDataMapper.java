package com.ctb.lexington.db.mapper;

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.record.StudentResearchDataRecord;

public class StudentResearchDataMapper extends AbstractDBMapper {
    private static final String EXISTS_NAME = "studentResearchDataExists";
    private static final String INSERT_NAME = "insertStudentResearchData";
    private static final String UPDATE_NAME = "updateStudentResearchData";

    public StudentResearchDataMapper(Connection conn) {
        super(conn);
    }

    public void upsert(StudentResearchDataRecord data) throws SQLException {
        if (!exists(EXISTS_NAME, data)) {
            insert(INSERT_NAME, data);
        } else {
            update(UPDATE_NAME, data);
        }
    }
}