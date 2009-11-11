package com.ctb.lexington.db.mapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ctb.lexington.db.data.StudentScoreSummaryDetails;

public class StudentScoreSummaryMapper extends AbstractDBMapper {
    public static final String FIND_NAME = "findStudentScoreSummary";
    public static final String FIND_BY_STUDENT_TEST_HISTORY_ID_NAME = "findStudentScoreSummaryByStudentTestHistoryId";
    public static final String INSERT_NAME = "insertStudentScoreSummary";
    public static final String UPDATE_NAME = "updateStudentScoreSummary";
    public static final String DELETE_NAME = "deleteStudentScoreSummary";

    public StudentScoreSummaryMapper(Connection conn) {
        super(conn);
    }

    public StudentScoreSummaryDetails find(Long reportItemSetId, Long studentTestHistoryId) {
        StudentScoreSummaryDetails template = new StudentScoreSummaryDetails();
        template.setReportItemSetId(reportItemSetId);
        template.setStudentTestHistoryId(studentTestHistoryId);
        return (StudentScoreSummaryDetails) find(FIND_NAME, template);
    }

    public ArrayList findByStudentTestHistoryId(Long studentTestHistoryId) {
        StudentScoreSummaryDetails template = new StudentScoreSummaryDetails();
        template.setStudentTestHistoryId(studentTestHistoryId);
        return (ArrayList) findMany(FIND_BY_STUDENT_TEST_HISTORY_ID_NAME, template);
    }

    public void insert(StudentScoreSummaryDetails record) throws SQLException {
        insert(INSERT_NAME, record);
    }

    public void update(StudentScoreSummaryDetails record) throws SQLException {
        update(UPDATE_NAME, record);
    }

    public void delete(StudentScoreSummaryDetails record) throws SQLException {
        delete(DELETE_NAME, record);
    }
}