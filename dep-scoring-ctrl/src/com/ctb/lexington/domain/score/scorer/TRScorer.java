package com.ctb.lexington.domain.score.scorer;

import java.io.IOException;

import com.ctb.lexington.db.data.StudentSubtestScoresData;
import com.ctb.lexington.db.data.StudentSubtestScoresDetails;
import com.ctb.lexington.db.utils.DatabaseHelper;
import com.ctb.lexington.domain.score.event.AssessmentStartedEvent;
import com.ctb.lexington.domain.score.event.ContentAreaNumberCorrectEvent;
import com.ctb.lexington.domain.score.event.SubtestInvalidEvent;
import com.ctb.lexington.domain.score.event.SubtestItemCollectionEvent;
import com.ctb.lexington.domain.score.event.SubtestValidEvent;
import com.ctb.lexington.domain.score.scorer.calculator.ContentAreaNumberCorrectCalculator;
import com.ctb.lexington.domain.score.scorer.calculator.MosaicResponseProcessCalculator;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.util.CTBConstants;

public class TRScorer extends STScorer {
	
	public TRScorer() throws CTBSystemException, IOException {
		super();
		addCalculator(new MosaicResponseProcessCalculator(channel, this));
		addCalculator(new ContentAreaNumberCorrectCalculator(channel, this));
		
		channel.subscribe(this, AssessmentStartedEvent.class);
		channel.subscribe(this, SubtestItemCollectionEvent.class);
		channel.subscribe(this, ContentAreaNumberCorrectEvent.class);
	}

	public void onEvent(final SubtestItemCollectionEvent event) {
		super.onEvent(event);
		final StudentSubtestScoresData subtestScoresData = getResultHolder()
				.getStudentSubtestScoresData();
		final StudentSubtestScoresDetails subtestScoresDetail = subtestScoresData
				.get(DatabaseHelper.asLong(event.getItemSetId()));
		subtestScoresDetail.setItemSetName(event.getItemSetName());
		
		//added for field test TE item scoring 
		copyScoreHistoryForFieldTestTEItems(getResultHolder(), event.getItems()
				.iterator(), event.getItemSetName());
	}

	public void onEvent(SubtestValidEvent event) {
		setFTItemStatus(event.getContentAreaNames(), CTBConstants.VALID_SCORE);
		setStatuses(event.getContentAreaNames(), CTBConstants.VALID_SCORE);
	}

	public void onEvent(SubtestInvalidEvent event) {
		setFTItemStatus(event.getContentAreaNames(), CTBConstants.INVALID_SCORE);
		setStatuses(event.getContentAreaNames(), CTBConstants.INVALID_SCORE);
	}
	
	public void onEvent(AssessmentStartedEvent event) {
	}

}