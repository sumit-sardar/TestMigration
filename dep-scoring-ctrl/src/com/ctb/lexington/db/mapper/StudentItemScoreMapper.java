package com.ctb.lexington.db.mapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ctb.lexington.db.data.StudentItemScoreDetails;

public class StudentItemScoreMapper extends AbstractDBMapper {
    private static final String FIND_NAME = "findStudentItemScore";
    private static final String FIND_BY_STUDENT_TEST_HISTORY_ID_NAME = "findStudentItemScoreByStudentTestHistoryId";
    private static final String INSERT_NAME = "insertStudentItemScore";
    private static final String UPDATE_NAME = "updateStudentItemScore";
    private static final String DELETE_NAME = "deleteStudentItemScore";

    public StudentItemScoreMapper(Connection conn) {
        super(conn);
    }

    public StudentItemScoreDetails find(Long studentTestHistoryId, String itemId) {
        StudentItemScoreDetails template = new StudentItemScoreDetails();
        template.setStudentTestHistoryId(studentTestHistoryId);
        template.setItemId(itemId);
        return (StudentItemScoreDetails) find(FIND_NAME, template);
    }

    public ArrayList findByStudentTestHistoryId(Long studentTestHistoryId) {
        StudentItemScoreDetails template = new StudentItemScoreDetails();
        template.setStudentTestHistoryId(studentTestHistoryId);
        return (ArrayList) findMany(FIND_BY_STUDENT_TEST_HISTORY_ID_NAME, template);
    }

    public void insert(StudentItemScoreDetails record) throws SQLException {
    	if(record.getReportItemSetId() != null) {
    		insert(INSERT_NAME, record);
    	} else {
    		System.out.println("Unscorable item id: " + record.getItemId());
    	}
    }

    public void update(StudentItemScoreDetails record) throws SQLException {
        update(UPDATE_NAME, record);
    }

    public void delete(Long studentTestHistoryId) throws SQLException {
        delete(DELETE_NAME, studentTestHistoryId);
    }
}