package com.ctb.lexington.domain.score.scorer;

import java.io.IOException;

import com.ctb.lexington.db.data.StsTestResultFactData;
import com.ctb.lexington.db.data.StsTestResultFactDetails;
import com.ctb.lexington.db.data.StsTotalStudentScoreDetail;
import com.ctb.lexington.db.data.StudentSubtestScoresData;
import com.ctb.lexington.db.data.StudentSubtestScoresDetails;
import com.ctb.lexington.db.utils.DatabaseHelper;
import com.ctb.lexington.domain.score.event.ContentAreaDerivedScoreEvent;
import com.ctb.lexington.domain.score.event.ContentAreaNumberCorrectEvent;
import com.ctb.lexington.domain.score.event.LocatorCompositeScoreEvent;
import com.ctb.lexington.domain.score.event.SubtestInvalidEvent;
import com.ctb.lexington.domain.score.event.SubtestItemCollectionEvent;
import com.ctb.lexington.domain.score.event.SubtestValidEvent;
import com.ctb.lexington.domain.score.scorer.calculator.ContentAreaNumberCorrectCalculator;
import com.ctb.lexington.domain.score.scorer.calculator.TABEContentAreaDerivedScoreCalculator;
import com.ctb.lexington.domain.score.scorer.calculator.TABELocatorCompositeScoreCalculator;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.util.CTBConstants;
import com.ctb.lexington.util.Stringx;

public class TLScorer extends STScorer {
    public TLScorer() throws CTBSystemException, IOException {
        super();

        addCalculator(new ContentAreaNumberCorrectCalculator(channel, this));
        addCalculator(new TABELocatorCompositeScoreCalculator(channel,this));

        channel.subscribe(this, ContentAreaNumberCorrectEvent.class);
        channel.subscribe(this, LocatorCompositeScoreEvent.class);
    }

    public void onEvent(final SubtestItemCollectionEvent event) {
        super.onEvent(event);
        final StudentSubtestScoresData subtestScoresData = getResultHolder().getStudentSubtestScoresData();
        final StudentSubtestScoresDetails subtestScoresDetail = subtestScoresData.get(DatabaseHelper.asLong(event.getItemSetId()));
        subtestScoresDetail.setItemSetName(event.getItemSetName());
    }

    public void onEvent(LocatorCompositeScoreEvent event) {
        StsTotalStudentScoreDetail detail = getResultHolder().getStsTotalStudentScoreData().get(event.getType());
        
        detail.setValidScore("T");
        detail.setPointsAttempted(event.getPointsAttempted());
        detail.setPointsObtained(event.getPointsObtained());
        if (event.getRecommendedLevelId() != null)
            detail.setRecommendedLevelId(event.getRecommendedLevelId());
    }
    
    public void onEvent(SubtestValidEvent event) {
        setStatuses(event.getContentAreaNames(), CTBConstants.VALID_SCORE);
    }

    public void onEvent(SubtestInvalidEvent event) {
        //setStatuses(event.getContentAreaNames(), CTBConstants.INVALID_SCORE);
    }
}