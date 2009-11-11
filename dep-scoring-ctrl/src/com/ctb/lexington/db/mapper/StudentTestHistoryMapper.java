package com.ctb.lexington.db.mapper;

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.data.StudentTestHistoryData;

public class StudentTestHistoryMapper extends AbstractDBMapper {
    public static final String FIND_BY_HISTORY_NAME = "findStudentTestHistory";
    public static final String FIND_BY_ROSTER_NAME = "findStudentTestHistoryByRoster";
    public static final String INSERT_NAME = "insertStudentTestHistory";
    public static final String UPDATE_NAME = "updateStudentTestHistory";
    public static final String DELETE_NAME = "deleteStudentTestHistory";

    public StudentTestHistoryMapper(Connection conn) {
        super(conn);
    }

    public StudentTestHistoryData findByStudentTestHistoryId(Long studentTestHistoryId) {
        StudentTestHistoryData template = new StudentTestHistoryData();
        template.setStudentTestHistoryId(studentTestHistoryId);
        return (StudentTestHistoryData) find(FIND_BY_HISTORY_NAME, template);
    }
    
    public StudentTestHistoryData findByTestRosterId(Long testRosterId) {
        StudentTestHistoryData template = new StudentTestHistoryData();
        template.setTestRosterId(testRosterId);
        return (StudentTestHistoryData) find(FIND_BY_ROSTER_NAME, template);
    }
    
    public StudentTestHistoryData insert(StudentTestHistoryData record) throws SQLException {
    	insert(INSERT_NAME, record);
		return findByTestRosterId(record.getTestRosterId());
    }

    public void update(StudentTestHistoryData record) throws SQLException {
        update(UPDATE_NAME, record);
    }

    public void delete(StudentTestHistoryData record) throws SQLException {
        delete(DELETE_NAME, record);
    }
}