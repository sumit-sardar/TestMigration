package com.ctb.lexington.db.mapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.ctb.lexington.db.data.StudentPredictedScoresData;

/**
 * @author mnkamiya
 * @version $Id$
 */
public class StudentPredictedScoresMapper extends AbstractDBMapper {
    private static final String FIND_NAME = "findStudentPredictedScore";
    private static final String CREATE_NAME = "createStudentPredictedScore";
    private static final String UPDATE_NAME = "updateStudentPredictedScore";

    public StudentPredictedScoresMapper(Connection conn) {
        super(conn);
    }

    public StudentPredictedScoresData find(final Long studentDimId, final Long studentDimVersionId, final Long adminDimId) {
        final StudentPredictedScoresData template = new StudentPredictedScoresData(studentDimId,studentDimVersionId,adminDimId);
        return (StudentPredictedScoresData) find(FIND_NAME,template);
    }

    public StudentPredictedScoresData upsert(StudentPredictedScoresData predictedScores) {
        try {
        	List existing = findMany(FIND_NAME,predictedScores);
            if (null != existing && existing.size() > 0)
                update(UPDATE_NAME,predictedScores);
            else
                insert(CREATE_NAME,predictedScores);
        } catch (SQLException e) {
            throw new RuntimeException("SQLException trying to insert/update StudentPredictedScores.  (StudentDimId: " +
                    predictedScores.getStudentDimId() + " AdminDimId: " + predictedScores.getAdminDimId() + ")", e);
        }
        return predictedScores;
    }
}