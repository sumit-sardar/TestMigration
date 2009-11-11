package com.ctb.lexington.db.mapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ctb.lexington.db.data.StudentSubtestScoresDetails;

public class StudentSubtestScoresMapper extends AbstractDBMapper {
    public static final String FIND_NAME = "findStudentSubtestScores";
    public static final String FIND_BY_STUDENT_TEST_HISTORY_ID_NAME = "findStudentSubtestScoresByStudentTestHistoryId";
    public static final String INSERT_NAME = "insertStudentSubtestScores";
    public static final String UPDATE_NAME = "updateStudentSubtestScores";
    public static final String DELETE_NAME = "deleteStudentSubtestScores";

    public StudentSubtestScoresMapper(Connection conn) {
        super(conn);
    }

    public StudentSubtestScoresDetails find(Long itemSetId, Long studentTestHistoryId) {
        StudentSubtestScoresDetails template = new StudentSubtestScoresDetails();
        template.setItemSetId(itemSetId);
        template.setStudentTestHistoryId(studentTestHistoryId);
        return (StudentSubtestScoresDetails) find(FIND_NAME, template);
    }

    public ArrayList findByStudentTestHistoryId(Long studentTestHistoryId) {
        StudentSubtestScoresDetails template = new StudentSubtestScoresDetails();
        template.setStudentTestHistoryId(studentTestHistoryId);
        return (ArrayList) findMany(FIND_BY_STUDENT_TEST_HISTORY_ID_NAME, template);
    }

    public void insert(StudentSubtestScoresDetails record) throws SQLException {
        insert(INSERT_NAME, record);
    }

    public void update(StudentSubtestScoresDetails record) throws SQLException {
        update(UPDATE_NAME, record);
    }

    public void delete(StudentSubtestScoresDetails record) throws SQLException {
        delete(DELETE_NAME, record);
    }
}