package com.ctb.lexington.domain.score.scorer.calculator;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;

import com.ctb.lexington.domain.score.event.AssessmentStartedEvent;
import com.ctb.lexington.domain.score.event.SubtestStartedEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.domain.teststructure.ScoreLookupCode;

/**
 * @author mnkamiya
 * @version $Id$
 */
public abstract class AbstractDerivedScoreCalculator extends Calculator {
    protected Integer pItemSetId;
    protected String pTestForm;
    protected String pDupTestForm;
    protected String pTestLevel;
    protected String pGrade;
    protected String pNormGroup;
    protected String pNormYear;
    protected String pAgeCategory;
    protected String pLevel;
    protected String pContentArea;
    protected String pRecommendedLevel;
    protected Long testRosterId;
    protected Double abilityScore;
    protected Double semScore;
    protected Integer scheduledProductId;
    

    public AbstractDerivedScoreCalculator(final Channel channel, final Scorer scorer) {
        super(channel, scorer);
        //TODO: don't subscribe to events in abstract class.... or move this & onEvent() back to
        // the subclasses
        channel.subscribe(this, AssessmentStartedEvent.class);
        channel.subscribe(this, SubtestStartedEvent.class);
        mustPrecede(AssessmentStartedEvent.class, SubtestStartedEvent.class);
    }

    public void onEvent(AssessmentStartedEvent event) {
        testRosterId = event.getTestRosterId();
        pGrade = event.getGrade();
        pNormYear = this.scorer.getResultHolder().getAdminData().getNormsYear();
        pAgeCategory = "AD".equals(pGrade)?"A":"J";
        scheduledProductId = event.getProductId();
    }

    public void onEvent(SubtestStartedEvent event) {
        pItemSetId = event.getItemSetId();
        pTestForm  = "%"; 
        pDupTestForm =  event.getItemSetForm();
        pTestLevel = event.getItemSetLevel();
        pNormGroup = event.getNormGroup();
        pRecommendedLevel = event.getRecommendedLevel();
        //pAgeCategory = event.getAgeCategory();
    	abilityScore = event.getAbilityScore();
    	semScore = event.getSemScore();
    	pContentArea = event.getItemSetName();
    }

    //  NOTE: Made protected for tests
    protected BigDecimal getScore(Integer itemSetId, String normGroup,
            ScoreLookupCode sourceScoreType, BigDecimal sourceScoreValue,
            ScoreLookupCode destScoreType, String ageCategory) {
        final Connection conn = getOASConnection();
        try {
            return getScoreLookupHelper().getScore(itemSetId, normGroup, sourceScoreType,
                    sourceScoreValue, destScoreType, ageCategory, conn);
        } finally {
            closeConnection(conn);
        }
    }

    // NOTE: Made protected for tests
    protected BigDecimal getScore(Integer itemSetId, String contentArea, String normGroup,
            String testForm, String testLevel, String grade, ScoreLookupCode sourceScoreType,
            BigDecimal sourceScoreValue, ScoreLookupCode destScoreType, String ageCategory) {
        final Connection conn = getOASConnection();
        try {
            return getScoreLookupHelper().getScore(itemSetId, contentArea, normGroup, testForm,
                    testLevel, grade, sourceScoreType, sourceScoreValue, destScoreType,
                    ageCategory, conn);
        } finally {
            closeConnection(conn);
        }
    }

    protected String getGradeEquivalent(Integer itemSetId, String contentArea,
            String normGroup, String testForm, String testLevel, String grade,
            ScoreLookupCode sourceScoreType, BigDecimal sourceScoreValue,
            ScoreLookupCode destScoreType, String ageCategory) {
        final Connection conn = getOASConnection();
        try {
            return getScoreLookupHelper().getGradeEquivalent(itemSetId, contentArea, normGroup,
                    testForm, testLevel, grade, sourceScoreType, sourceScoreValue, destScoreType,
                    ageCategory, conn);
        } finally {
            closeConnection(conn);
        }
    }

    //  NOTE: Made protected for tests
    protected BigDecimal getScore(String frameworkCode, String contentArea, String normGroup,
            String testForm, String testLevel, String grade, ScoreLookupCode sourceScoreType,
            BigDecimal sourceScoreValue, ScoreLookupCode destScoreType, String ageCategory) {
        final Connection conn = getOASConnection();
        try {
            return getScoreLookupHelper().getScore(frameworkCode, contentArea, normGroup, testForm,
                    testLevel, grade, sourceScoreType, sourceScoreValue, destScoreType, conn,
                    ageCategory);
        } finally {
            closeConnection(conn);
        }
    }

    protected BigDecimal getTerraNovaPerformanceLevel(String contentArea, String testLevel,
            BigDecimal sourceScoreValue) {
        final Connection conn = getOASConnection();
        try {
            return getScoreLookupHelper().getTerraNovaPerformanceLevel(sourceScoreValue,
                    contentArea, testLevel, conn);
        } finally {
            closeConnection(conn);
        }
    }
     // START For Laslink Scoring
    protected BigDecimal getLasLinkPerformanceLevel(String frameworkCode, String contentArea, String testLevel,
            BigDecimal sourceScoreValue, String grade, String testForm) {
        final Connection conn = getOASConnection();
        try {
            return getScoreLookupHelper().getLasLinkPerformanceLevel(frameworkCode, sourceScoreValue,
                    contentArea, testLevel, grade, testForm, conn);
        } finally {
            closeConnection(conn);
        }
    }
	// END For Laslink Scoring
    protected Connection getOASConnection() {
        try {
            return scorer.getOASConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    protected void closeConnection(Connection connection) {
        try {
            scorer.close(true, connection);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected ScoreLookupHelper getScoreLookupHelper() {
        return new ScoreLookupHelper();
    }
}