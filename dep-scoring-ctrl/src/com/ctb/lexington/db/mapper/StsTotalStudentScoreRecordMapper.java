package com.ctb.lexington.db.mapper;

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.data.StsTotalStudentScoreDetail;

public class StsTotalStudentScoreRecordMapper extends AbstractDBMapper {
    private static final String FIND_BY_UNIQUE_KEY_NAME = "findStsTotalStudentScoreByUniqueKey";
    private static final String INSERT_NAME = "insertStsTotalStudentScore";
    private static final String UPDATE_NAME = "updateStsTotalStudentScore";

    /**
     * Constructor for StsTotalStudentScoreRecordMapper.
     * 
     * @param conn
     */
    public StsTotalStudentScoreRecordMapper(Connection conn) {
        super(conn);
    }

    public StsTotalStudentScoreDetail findByUniqueKey(String type, Long studentDimId,
            Long adminDimId) {
        StsTotalStudentScoreDetail template = new StsTotalStudentScoreDetail();
        template.setType(type);
        template.setStudentDimId(studentDimId);
        template.setAdminDimId(adminDimId);
        return (StsTotalStudentScoreDetail) find(FIND_BY_UNIQUE_KEY_NAME, template);
    }

    /**
     * @param record
     */
    public StsTotalStudentScoreDetail upsert(StsTotalStudentScoreDetail record, boolean updateContextData)
            throws SQLException {
        StsTotalStudentScoreDetail existing = findByUniqueKey(record.getType(), record
                .getStudentDimId(), record.getAdminDimId());
        if (existing == null) {
            insert(INSERT_NAME, record);
            return findByUniqueKey(record.getType(), record.getStudentDimId(), record
                    .getAdminDimId());
        } else {
            // copy the fields that result out of scoring
            existing.setAverageGedPredictedScore(record.getAverageGedPredictedScore());
            existing.setGradeEquivalent(record.getGradeEquivalent());
            existing.setNationalPercentile(record.getNationalPercentile());
            existing.setNationalStanine(record.getNationalStanine());
            existing.setNormalCurveEquivalent(record.getNormalCurveEquivalent());
            existing.setNormGroup(record.getNormGroup());
            existing.setNormYear(record.getNormYear());
            existing.setNumberAttempted(record.getNumberAttempted());
            existing.setPointsAttempted(record.getPointsAttempted());
            existing.setPointsObtained(record.getPointsObtained());
            existing.setPointsPossible(record.getPointsPossible());
            existing.setScaleScore(record.getScaleScore());
            existing.setValidScore(record.getValidScore());
            if(updateContextData) {
	            existing.setStudentDimVersionId(record.getStudentDimVersionId());
	            existing.setOrgNodeDimId(record.getOrgNodeDimId());
	            existing.setOrgNodeDimVersionId(record.getOrgNodeDimVersionId());
            }
            update(UPDATE_NAME, existing);
            return existing;
        }
    }
}