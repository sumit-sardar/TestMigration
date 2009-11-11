package com.ctb.lexington.db.mapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.ctb.lexington.db.data.StudentData;

public class StudentDataMapper extends AbstractDBMapper {
    private static final String FIND_BY_OAS_STUDENT_ID_NAME = "findByOasStudentId";
    private static final String FIND_BY_STUDENT_DIM_ID_NAME = "findByStudentDimId";
    private static final String UPDATE_NAME = "updateStudent";
    private static final String FIND_BY_STUDENT_DIM_ID_VERSION_ID_NAME = "findByPK";

    /**
     * Constructor for StudentDataMapper.
     * 
     * @param conn
     */
    public StudentDataMapper(Connection conn) {
        super(conn);
    }

    /**
     * @param oasStudentId
     * @return
     */
    public List findByOasStudentId(Long oasStudentId) {
    	StudentData template = new StudentData();
    	template.setOasStudentId(oasStudentId);
        return findMany(FIND_BY_OAS_STUDENT_ID_NAME, template);
    }

	/**
	 * @param studentDimID
	 * @return
	 */
	public List findByStudentDimId(Long studentDimId) {
		StudentData template = new StudentData();
    	template.setStudentDimId(studentDimId);
        return findMany(FIND_BY_STUDENT_DIM_ID_NAME, template);
	}

    /**
     * @param student
     * @throws SQLException
     */
    public void update(StudentData student) throws SQLException {
        update(UPDATE_NAME, student);
    }

    /**
     * @param studentDimId
     * @param studentDimVersionId
     * @return
     */
    public StudentData findByStudentDimIdVersionId(Long studentDimId, Long studentDimVersionId) {
        StudentData template = new StudentData();
        template.setStudentDimId(studentDimId);
        template.setStudentDimVersionId(studentDimVersionId);
        return (StudentData)find(FIND_BY_STUDENT_DIM_ID_VERSION_ID_NAME, template );
    }
}