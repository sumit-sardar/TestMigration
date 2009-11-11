package com.ctb.lexington.db.mapper;

import java.sql.Connection;

import com.ctb.lexington.db.data.StudentTestHistoryData;
import com.ctb.lexington.db.record.ScoringResult;

public class ScoringResultMapper extends AbstractDBMapper {
    public ScoringResultMapper(Connection conn) {
        super(conn);
    }

    public ScoringResult findByStudentTestHistoryId(Long studentTestHistoryId) {
        ScoringResult scoringResult = new ScoringResult();

        StudentTestHistoryMapper studentTestHistoryMapper = new StudentTestHistoryMapper(conn);
        StudentTestHistoryData studentTestHistoryRecord = studentTestHistoryMapper
                .findByStudentTestHistoryId(studentTestHistoryId);
        scoringResult.setStudentTestHistory(studentTestHistoryRecord);

        StudentItemScoreMapper itemScoreMapper = new StudentItemScoreMapper(conn);
        scoringResult.setItemScoreList(itemScoreMapper
                .findByStudentTestHistoryId(studentTestHistoryId));

        StudentSubtestScoresMapper subtestScoreMapper = new StudentSubtestScoresMapper(conn);
        scoringResult.setSubtestScoreList(subtestScoreMapper
                .findByStudentTestHistoryId(studentTestHistoryId));

        StudentScoreSummaryMapper scoreSummaryMapper = new StudentScoreSummaryMapper(conn);
        scoringResult.setScoreSummaryList(scoreSummaryMapper
                .findByStudentTestHistoryId(studentTestHistoryId));

        return scoringResult;
    }
}