package com.ctb.lexington.domain.score.scorer.calculator;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ctb.lexington.db.utils.DatabaseHelper;
import com.ctb.lexington.domain.score.event.AssessmentEndedEvent;
import com.ctb.lexington.domain.score.event.AssessmentStartedEvent;
import com.ctb.lexington.domain.score.event.ContentAreaDerivedScoreEvent;
import com.ctb.lexington.domain.score.event.ContentAreaRawScoreEvent;
import com.ctb.lexington.domain.score.event.LocatorCompositeScoreEvent;
import com.ctb.lexington.domain.score.event.SubtestContentAreaCompositeScoreEvent;
import com.ctb.lexington.domain.score.event.SubtestInvalidEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.domain.teststructure.ScoreLookupCode;
import com.ctb.lexington.util.CTBConstants;
import com.ctb.lexington.util.SafeHashMap;
import com.ctb.lexington.util.Timer;

public class TABELocatorCompositeScoreCalculator extends AbstractDerivedScoreCalculator {
    public final static String READING_CONTENT_AREA_NAME = "Reading";
    public final static String LANGUAGE_CONTENT_AREA_NAME = "Language";
    public final static String MATH_COMP_CONTENT_AREA_NAME = "Math Computation";
    public final static String MATH_APPLIED_CONTENT_AREA_NAME = "Applied Mathematics";

    public static final String MATH_COMPOSITE_TYPE = "Mathematics";
    public static final String READING_COMPOSITE_TYPE = "Reading";
    public static final String LANGUAGE_COMPOSITE_TYPE = "Language";

    private static final String TABE_FRAMEWORK_CODE = "TABE";

    private final Map contentAreaRawScoreEvents = new SafeHashMap(String.class,ContentAreaRawScoreEvent.class);
    private final ArrayList contentAreaNamesRequiredForReadingComposite = new ArrayList();
    private final ArrayList contentAreaNamesRequiredForMathComposite = new ArrayList();
    private final ArrayList contentAreaNamesRequiredForLanguageComposite = new ArrayList();

    private boolean readingCompositeCalculated;
    private boolean mathCompositeCalculated;
    private boolean languageCompositeCalculated;

    private final CompositeScoreHolder mathCompositeScores;
    private final CompositeScoreHolder readingCompositeScores;
    private final CompositeScoreHolder languageCompositeScores;
    
    private List invalidSubtestIds;

    public TABELocatorCompositeScoreCalculator(Channel channel, Scorer scorer) {
        super(channel, scorer);

        contentAreaNamesRequiredForReadingComposite.add(READING_CONTENT_AREA_NAME);

        contentAreaNamesRequiredForMathComposite.add(MATH_COMP_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForMathComposite.add(MATH_APPLIED_CONTENT_AREA_NAME);
        
        contentAreaNamesRequiredForLanguageComposite.add(LANGUAGE_CONTENT_AREA_NAME);

        readingCompositeCalculated = false;
        mathCompositeCalculated = false;
        languageCompositeCalculated = false;
        
        mathCompositeScores = new CompositeScoreHolder();
        readingCompositeScores = new CompositeScoreHolder();
        languageCompositeScores = new CompositeScoreHolder();

        channel.subscribe(this, ContentAreaRawScoreEvent.class);
        channel.subscribe(this, SubtestInvalidEvent.class);
        channel.subscribe(this, AssessmentEndedEvent.class);
    }

    public void onEvent(AssessmentStartedEvent event) {
        super.onEvent(event);
        pNormGroup = null;
        invalidSubtestIds = new ArrayList();
    }

    public void onEvent(ContentAreaRawScoreEvent event) {
        if(event.getPointsAttempted() > 0) {
            contentAreaRawScoreEvents.put(event.getContentAreaName(), event);
        }
    }

    public void onEvent(SubtestInvalidEvent event) {
        invalidSubtestIds.add(event.getItemSetId());
    }

    public void onEvent(AssessmentEndedEvent event) {
        final Long testRosterId = event.getTestRosterId();
        calculateMathCompositeScores();
        calculateReadingCompositeScores();
        calculateLanguageCompositeScores();
        if(areContentAreasRequiredForMathCompositesScored()) {
            publishScores(MATH_COMPOSITE_TYPE, testRosterId, mathCompositeScores);
        }
        if(areContentAreasRequiredForReadingCompositesScored()) {
            publishScores(READING_COMPOSITE_TYPE, testRosterId, readingCompositeScores);
        }
        if(areContentAreasRequiredForLanguageCompositesScored()) {
            publishScores(LANGUAGE_COMPOSITE_TYPE, testRosterId, languageCompositeScores);
        }
    }

    private void calculateMathCompositeScores() {
        if (!mathCompositeCalculated && areContentAreasRequiredForMathCompositesScored()) {
            ContentAreaRawScoreEvent comp = (ContentAreaRawScoreEvent) contentAreaRawScoreEvents.get(MATH_COMP_CONTENT_AREA_NAME);
            ContentAreaRawScoreEvent applied = (ContentAreaRawScoreEvent) contentAreaRawScoreEvents.get(MATH_APPLIED_CONTENT_AREA_NAME);
        
            int raw = comp.getPointsObtained() + applied.getPointsObtained();
            int recLevelId = 6;
            
            if(raw <= 6) recLevelId = 2;
            else if (raw <= 8) recLevelId = 3;
            else if (raw <= 11) recLevelId = 4;
            else if (raw >= 12) recLevelId = 5;
            
            mathCompositeScores.pointsAttempted = comp.getPointsAttempted() + applied.getPointsAttempted();
            mathCompositeScores.pointsObtained = comp.getPointsObtained() + applied.getPointsObtained();
            mathCompositeScores.recommendedLevelId = recLevelId;
        }
    }
    
    private void calculateReadingCompositeScores() {
        if (!readingCompositeCalculated && areContentAreasRequiredForReadingCompositesScored()) {
            ContentAreaRawScoreEvent read = (ContentAreaRawScoreEvent) contentAreaRawScoreEvents.get(READING_CONTENT_AREA_NAME);
        
            int raw = read.getPointsObtained();
            int recLevelId = 6;
            
            if(raw <= 6) recLevelId = 2;
            else if (raw <= 8) recLevelId = 3;
            else if (raw <= 10) recLevelId = 4;
            else if (raw >= 11) recLevelId = 5;
            
            readingCompositeScores.pointsAttempted = read.getPointsAttempted();
            readingCompositeScores.pointsObtained = read.getPointsObtained();
            readingCompositeScores.recommendedLevelId = recLevelId;
        }
    }
    
    private void calculateLanguageCompositeScores() {
        if (!languageCompositeCalculated && areContentAreasRequiredForLanguageCompositesScored()) {
            ContentAreaRawScoreEvent lang = (ContentAreaRawScoreEvent) contentAreaRawScoreEvents.get(LANGUAGE_CONTENT_AREA_NAME);
        
            int raw = lang.getPointsObtained();
            int recLevelId = 6;
            
            if(raw <= 6) recLevelId = 2;
            else if (raw <= 8) recLevelId = 3;
            else if (raw <= 10) recLevelId = 4;
            else if (raw >= 11) recLevelId = 5;
            
            languageCompositeScores.pointsAttempted = lang.getPointsAttempted();
            languageCompositeScores.pointsObtained = lang.getPointsObtained();
            languageCompositeScores.recommendedLevelId = recLevelId;
        }
    }

    private void publishScores(final String scoreType, final Long testRosterId, final CompositeScoreHolder scores) {
        channel.send(new LocatorCompositeScoreEvent(testRosterId, scoreType, new Long(scores.pointsAttempted), new Long(scores.pointsObtained), new Long(scores.recommendedLevelId)));
    }

    private ContentAreaRawScoreEvent getContentAreaRawScore(String contentAreaName) {
        return (ContentAreaRawScoreEvent) contentAreaRawScoreEvents.get(contentAreaName);
    }

    private boolean areContentAreasRequiredForMathCompositesScored() {
        if (contentAreaNamesRequiredForMathComposite.size() > contentAreaRawScoreEvents.size()) {
            return false;
        }
        return contentAreaRawScoreEvents.keySet().containsAll(contentAreaNamesRequiredForMathComposite);
    }

    private boolean areContentAreasRequiredForReadingCompositesScored() {
        if (contentAreaNamesRequiredForReadingComposite.size() > contentAreaRawScoreEvents.size()) {
            return false;            
        }
        return contentAreaRawScoreEvents.keySet().containsAll(contentAreaNamesRequiredForReadingComposite);
    }
    
    private boolean areContentAreasRequiredForLanguageCompositesScored() {
        if (contentAreaNamesRequiredForLanguageComposite.size() > contentAreaRawScoreEvents.size()) {
            return false;            
        }
        return contentAreaRawScoreEvents.keySet().containsAll(contentAreaNamesRequiredForLanguageComposite);
    }

    private boolean isContentAreaScored(String contentAreaName) {
        return contentAreaRawScoreEvents.keySet().contains(contentAreaName);
    }

    private class CompositeScoreHolder {
        int recommendedLevelId;
        int pointsObtained;
        int pointsAttempted;

        CompositeScoreHolder() {
            recommendedLevelId = 6;
        }
    }
}