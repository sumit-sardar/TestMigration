package com.ctb.lexington.db.mapper;

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.data.StudentTestDetails;

public class StsTestDimMapper extends AbstractDBMapper {
    private static final String FIND_BY_UNIQUE_KEY_NAME = "findStsTestDimByUniqueKey";
    private static final String INSERT_NAME = "insertStsTestDim";

    /**
     * Constructor for StsTestDimMapper.
     * 
     * @param conn
     */
    public StsTestDimMapper(Connection conn) {
        super(conn);
    }

    public StudentTestDetails findByUniqueKey(String customer, String testName, String subTestName,
            String testForm, String testLevel) {
        StudentTestDetails template = new StudentTestDetails();
        template.setCustomer(customer);
        template.setTestName(testName);
        template.setSubTestName(subTestName);
        template.setTestForm(testForm);
        template.setTestLevel(testLevel);
        return (StudentTestDetails) find(FIND_BY_UNIQUE_KEY_NAME, template);
    }

    public void insert(StudentTestDetails detail) throws SQLException {
        insert(INSERT_NAME, detail);
    }
}