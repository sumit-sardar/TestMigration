package com.ctb.lexington.domain.score.event;

import java.util.Map;

import com.ctb.lexington.db.data.SubtestContentAreaCompositeAndDerivedScore;
import com.ctb.lexington.domain.score.event.common.Event;

public class SubtestContentAreaCompositeAndDerivedEvent extends Event {
	private final Map<String,SubtestContentAreaCompositeAndDerivedScore> subtestContentAreaCompositeAndDerivedScore;
    
   
	public SubtestContentAreaCompositeAndDerivedEvent(final Long testRosterId, final Map subtestContentAreaCompositeAndDerivedScore) {
        super(testRosterId);
        this.subtestContentAreaCompositeAndDerivedScore = subtestContentAreaCompositeAndDerivedScore;
        
    }

	/**
	 * @return the subtestContentAreaCompositeAndDerivedScore
	 */
	public Map getSubtestContentAreaCompositeAndDerivedScore() {
		return subtestContentAreaCompositeAndDerivedScore;
	}
	
}