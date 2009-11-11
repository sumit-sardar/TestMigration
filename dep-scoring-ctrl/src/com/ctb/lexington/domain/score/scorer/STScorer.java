package com.ctb.lexington.domain.score.scorer;

import java.io.IOException;

import com.ctb.lexington.db.data.StsTestResultFactData;
import com.ctb.lexington.db.data.StsTestResultFactDetails;
import com.ctb.lexington.domain.score.event.ContentAreaCumulativeNumberCorrectEvent;
import com.ctb.lexington.domain.score.event.ContentAreaNumberCorrectEvent;
import com.ctb.lexington.domain.score.event.SubtestInvalidEvent;
import com.ctb.lexington.domain.score.event.SubtestValidEvent;
import com.ctb.lexington.domain.score.scorer.calculator.ContentAreaNumberCorrectCalculator;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.util.CTBConstants;

public class STScorer extends BaseScorer {
    public STScorer() throws CTBSystemException, IOException {
        super();

        addCalculator(new ContentAreaNumberCorrectCalculator(channel, this));
        
        channel.subscribe(this, ContentAreaNumberCorrectEvent.class);
    }

    public void onEvent(ContentAreaNumberCorrectEvent event) {
        StsTestResultFactData factData = getResultHolder().getStsTestResultFactData();
        StsTestResultFactDetails factDetails = factData.get(event.getContentAreaName());
        if(factDetails.getNumberAttempted() == null) {
        	factDetails.setNumberAttempted(new Long(event.getNumberAttempted()));
        } else {
        	factDetails.setNumberAttempted(new Long(factDetails.getNumberAttempted().intValue() + event.getNumberAttempted()));
        }
        if(factDetails.getNumberCorrect() == null) {
        	factDetails.setNumberCorrect(new Long(event.getNumberCorrect()));
        } else {
        	factDetails.setNumberCorrect(new Long(factDetails.getNumberCorrect().intValue() + event.getNumberCorrect()));
        }
        if(factDetails.getNumIncorrect() == null) {
        	factDetails.setNumIncorrect(new Long(event.getNumberIncorrect()));
        } else {
        	factDetails.setNumIncorrect(new Long(factDetails.getNumIncorrect().intValue() + event.getNumberIncorrect()));
        }
        if(factDetails.getNumUnattempted() == null) {
        	factDetails.setNumUnattempted(new Long(event.getNumberUnattempted()));
        } else {
        	factDetails.setNumUnattempted(new Long(factDetails.getNumUnattempted().intValue() + event.getNumberUnattempted()));
        }
        factDetails.calculatePercentCorrect();
        
	    channel.send(new ContentAreaCumulativeNumberCorrectEvent(
	        		event.getTestRosterId(),
					event.getSubtestId(),
	                event.getContentAreaId(), 
					event.getContentAreaName(), 
					factDetails.getNumberAttempted().intValue() + factDetails.getNumUnattempted().intValue(),
		            factDetails.getNumberCorrect().intValue(), 
					factDetails.getNumIncorrect().intValue(), 
					factDetails.getNumberAttempted().intValue(),
		            factDetails.getNumUnattempted().intValue()));
    }
    
    public void onEvent(SubtestValidEvent event) {
        setStatuses(event.getContentAreaNames(), CTBConstants.VALID_SCORE);
    }

    public void onEvent(SubtestInvalidEvent event) {
        setStatuses(event.getContentAreaNames(), CTBConstants.VALID_SCORE);
    }
}
