package com.ctb.lexington.domain.score.scorer;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.ctb.lexington.db.data.StsTestResultFactData;
import com.ctb.lexington.db.data.StsTestResultFactDetails;
import com.ctb.lexington.db.data.StudentSubtestScoresData;
import com.ctb.lexington.db.data.StudentSubtestScoresDetails;
import com.ctb.lexington.db.utils.DatabaseHelper;
import com.ctb.lexington.domain.score.event.SubtestDerivedScoreEvent;
import com.ctb.lexington.domain.score.event.SubtestItemCollectionEvent;
import com.ctb.lexington.domain.score.scorer.calculator.ContributingResponseCalculator;
import com.ctb.lexington.domain.score.scorer.calculator.ScoreTypeNumberCorrectCalculator;
import com.ctb.lexington.domain.score.scorer.calculator.SubtestDerivedScoreCalculator;
import com.ctb.lexington.exception.CTBSystemException;

public class SFScorer extends STScorer {
    public SFScorer() throws CTBSystemException, IOException {
        super();

        addCalculator(new ContributingResponseCalculator(channel, this));
        addCalculator(new ScoreTypeNumberCorrectCalculator(channel, this));
        // SOFA derived score lookup is strictly based on subtest item
        // set and score type codes.
        addCalculator(new SubtestDerivedScoreCalculator(channel, this));

        channel.subscribe(this, SubtestDerivedScoreEvent.class);
    }

    public void onEvent(final SubtestItemCollectionEvent event) {
        super.onEvent(event);
        final StudentSubtestScoresData subtestScoresData = getResultHolder()
                .getStudentSubtestScoresData();
        final StudentSubtestScoresDetails subtestScoresDetail = subtestScoresData.get(DatabaseHelper.asLong(
                event.getItemSetId()));
        subtestScoresDetail.setItemSetName(event.getItemSetName());
    }

    public void onEvent(SubtestDerivedScoreEvent event) {
        // set scale score into the test result fact record
        // set scale score on the student subtest score details
        StudentSubtestScoresData subtestScoresData = getResultHolder()
                .getStudentSubtestScoresData();
        StsTestResultFactData factData = getResultHolder().getStsTestResultFactData();
        /*List contentAreaCurriculumDetails = getResultHolder().getCurriculumData()
                .getAllContentAreaCurriculumDetails();
        for (Iterator iter = contentAreaCurriculumDetails.iterator(); iter.hasNext();) {
            CurriculumDetail detail = (CurriculumDetail) iter.next();
            StsTestResultFactDetails factDetails = factData.get(detail.getName());
            if(event.getScaleScore() != null)
            	factDetails.setScaleScore(event.getScaleScore());

            StudentSubtestScoresDetails scoreDetail = subtestScoresData.get(DatabaseHelper.asLong(event
                    .getItemSetId()));
            scoreDetail.setScoreTypeCode(event.getScoreTypeCode());
            if (event.getScaleScore() != null)
            	scoreDetail.setScoreValue(event.getScaleScore());
        }*/
    }
}