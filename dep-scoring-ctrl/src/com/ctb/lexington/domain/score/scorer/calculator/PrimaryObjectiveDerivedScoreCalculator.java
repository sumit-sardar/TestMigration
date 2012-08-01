package com.ctb.lexington.domain.score.scorer.calculator;

import java.math.BigDecimal;
import java.sql.Connection;

import com.ctb.lexington.domain.score.event.AssessmentStartedEvent;
import com.ctb.lexington.domain.score.event.ObjectivePrimaryCumulativeNumberCorrectEvent;
import com.ctb.lexington.domain.score.event.PrimaryObjectiveDerivedScoreEvent;
import com.ctb.lexington.domain.score.event.SubtestStartedEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;

/**
 * @author mnkamiya
 * @version $Id: PrimaryObjectiveDerivedScoreCalculator.java,v 1.1.2.6 2004/10/01 01:06:05 vraravam
 *          Exp $
 */
public class PrimaryObjectiveDerivedScoreCalculator extends AbstractDerivedScoreCalculator {
    public PrimaryObjectiveDerivedScoreCalculator(Channel channel, Scorer scorer) {
        super(channel, scorer);

        channel.subscribe(this, AssessmentStartedEvent.class);
        channel.subscribe(this, SubtestStartedEvent.class);
        channel.subscribe(this, ObjectivePrimaryCumulativeNumberCorrectEvent.class);
    }

    public void onEvent(AssessmentStartedEvent event) {
        testRosterId = event.getTestRosterId();
        pGrade = event.getGrade();
        pNormYear = this.scorer.getResultHolder().getAdminData().getNormsYear();
        if(scorer.getResultHolder().getAdminData().getProductId() == 3700)
        	scheduledProductId = event.getProductId();
    }

    public void onEvent(SubtestStartedEvent event) {
        pNormGroup = event.getNormGroup();
        pLevel = event.getItemSetLevel();
        pTestForm = event.getItemSetForm();
    }
    
    public void onEvent(ObjectivePrimaryCumulativeNumberCorrectEvent event) {
        Connection conn = null;
        try {
            conn = getOASConnection();
            BigDecimal pValue = new BigDecimal(-1.0);
            if(scorer.getResultHolder().getAdminData().getProductId() == 3700) {
            	Integer highMR = null;
            	Integer lowMR = null;
            	highMR = getScoreLookupHelper().getObjectiveHMR(event.getObjectiveId(), "%", "%", null, null, pLevel, conn, scheduledProductId);
            	lowMR = getScoreLookupHelper().getObjectiveLMR(event.getObjectiveId(), "%", "%", null, null, pLevel, conn, scheduledProductId);
            	System.out.println(event.getObjectiveId() + " - " + highMR + " - " + lowMR);
            	if( ("1" + pGrade).equals(pLevel) || ("19/20".equals(pLevel) && ("9".equals(pGrade) || "10".equals(pGrade)))
                		|| ("11".equals(pGrade) && "21".equals(pLevel)) || ("12".equals(pGrade) && "22".equals(pLevel))) {
                    pValue = getScoreLookupHelper().getObjectivePValue(event.getObjectiveId(), "%", "%", pNormGroup, pGrade, pLevel, conn);
                }
            	channel.send(new PrimaryObjectiveDerivedScoreEvent(event.getTestRosterId(), event
            			.getObjectiveId(), pValue, event.getSubtestId(), highMR, lowMR));
            } else {
            	if( ("1" + pGrade).equals(pLevel) || ("19/20".equals(pLevel) && ("9".equals(pGrade) || "10".equals(pGrade)))
                		|| ("11".equals(pGrade) && "21".equals(pLevel)) || ("12".equals(pGrade) && "22".equals(pLevel))) {
                    if(pGrade.length() == 1) {
                    	pValue = getScoreLookupHelper().getObjectivePValueTerrab3(event.getObjectiveId(), "%", "%", pNormGroup, "0"+pGrade, pLevel, conn);
                    } else {
                    	pValue = getScoreLookupHelper().getObjectivePValueTerrab3(event.getObjectiveId(), "%", "%", pNormGroup, pGrade, pLevel, conn);
                    }
                }
            	channel.send(new PrimaryObjectiveDerivedScoreEvent(event.getTestRosterId(), event
            			.getObjectiveId(), pValue, event.getSubtestId(), null, null));
            }
        } finally {
            closeConnection(conn);
        }
    }
}