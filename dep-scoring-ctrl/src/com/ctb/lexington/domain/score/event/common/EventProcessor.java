package com.ctb.lexington.domain.score.event.common;

import com.ctb.lexington.domain.score.event.AssessmentEndedEvent;
import com.ctb.lexington.domain.score.event.AssessmentEvent;
import com.ctb.lexington.domain.score.event.AssessmentStartedEvent;
import com.ctb.lexington.domain.score.event.ContentAreaCumulativeNumberCorrectEvent;
import com.ctb.lexington.domain.score.event.ContentAreaDerivedScoreEvent;
import com.ctb.lexington.domain.score.event.ContentAreaNumberCorrectEvent;
import com.ctb.lexington.domain.score.event.ContentAreaRawScoreEvent;
import com.ctb.lexington.domain.score.event.ContributingCorrectResponseEvent;
import com.ctb.lexington.domain.score.event.ContributingIncorrectResponseEvent;
import com.ctb.lexington.domain.score.event.ContributingPointEvent;
import com.ctb.lexington.domain.score.event.CorrectResponseEvent;
import com.ctb.lexington.domain.score.event.IncorrectResponseEvent;
import com.ctb.lexington.domain.score.event.LocatorCompositeScoreEvent;
import com.ctb.lexington.domain.score.event.NoContributingResponseEvent;
import com.ctb.lexington.domain.score.event.NoResponseEvent;
import com.ctb.lexington.domain.score.event.ObjectiveEvent;
import com.ctb.lexington.domain.score.event.ObjectiveNumberCorrectEvent;
import com.ctb.lexington.domain.score.event.ObjectivePrimaryCumulativeNumberCorrectEvent;
import com.ctb.lexington.domain.score.event.ObjectivePrimaryNumberCorrectEvent;
import com.ctb.lexington.domain.score.event.ObjectiveRawScoreEvent;
import com.ctb.lexington.domain.score.event.ObjectiveSecondaryNumberCorrectEvent;
import com.ctb.lexington.domain.score.event.PointEvent;
import com.ctb.lexington.domain.score.event.PrimaryObjectiveDerivedScoreEvent;
import com.ctb.lexington.domain.score.event.PrimaryObjectivePercentMasteryEvent;
import com.ctb.lexington.domain.score.event.ResponseEvent;
import com.ctb.lexington.domain.score.event.ResponseReceivedEvent;
import com.ctb.lexington.domain.score.event.ScoreTypeEvent;
import com.ctb.lexington.domain.score.event.ScoreTypeNumberCorrectEvent;
import com.ctb.lexington.domain.score.event.ScoreTypeRawScoreEvent;
import com.ctb.lexington.domain.score.event.ScoringStatusEvent;
import com.ctb.lexington.domain.score.event.SubtestContentAreaCompositeAndDerivedEvent;
import com.ctb.lexington.domain.score.event.SubtestContentAreaCompositeScoreEvent;
import com.ctb.lexington.domain.score.event.SubtestContentAreaItemCollectionEvent;
import com.ctb.lexington.domain.score.event.SubtestDerivedScoreEvent;
import com.ctb.lexington.domain.score.event.SubtestEndedEvent;
import com.ctb.lexington.domain.score.event.SubtestEvent;
import com.ctb.lexington.domain.score.event.SubtestInvalidEvent;
import com.ctb.lexington.domain.score.event.SubtestItemCollectionEvent;
import com.ctb.lexington.domain.score.event.SubtestNumberCorrectEvent;
import com.ctb.lexington.domain.score.event.SubtestObjectiveCollectionEvent;
import com.ctb.lexington.domain.score.event.SubtestRawScoreEvent;
import com.ctb.lexington.domain.score.event.SubtestStartedEvent;
import com.ctb.lexington.domain.score.event.SubtestValidEvent;
import com.ctb.lexington.domain.score.event.TestCompletionStatusEvent;
import com.ctb.lexington.exception.CTBSystemException;

public abstract class EventProcessor implements EventRecipient {
    protected Channel channel;

    public EventProcessor(Channel channel) {
        if (channel == null) {
            throw new IllegalArgumentException("channel cannot be null");
        }
        this.channel = channel;
    }
    
    public EventProcessor() {
    }
    
    /**
     * we have to handle all event types here explicitly
     * to avoid using reflection to obtain handler method
     * names in EventProcessorHelper, because performance
     * is degraded by huge number of such calls.
     */
    public void onEvent (Event event) throws CTBSystemException {
    	if (event instanceof AssessmentStartedEvent)
    		this.onEvent((AssessmentStartedEvent) event);
    	if (event instanceof AssessmentEndedEvent)
    		this.onEvent((AssessmentEndedEvent) event);
    	if (event instanceof AssessmentEvent)
    		this.onEvent((AssessmentEvent) event);
    	if (event instanceof ContentAreaDerivedScoreEvent)
    		this.onEvent((ContentAreaDerivedScoreEvent) event);
    	if (event instanceof ContentAreaNumberCorrectEvent)
    		this.onEvent((ContentAreaNumberCorrectEvent) event);
    	if (event instanceof ContentAreaCumulativeNumberCorrectEvent)
    		this.onEvent((ContentAreaCumulativeNumberCorrectEvent) event);
    	if (event instanceof ContentAreaRawScoreEvent)
    		this.onEvent((ContentAreaRawScoreEvent) event);
    	if (event instanceof ContributingPointEvent)
    		this.onEvent((ContributingPointEvent) event);
    	if (event instanceof ContributingCorrectResponseEvent)
    		this.onEvent((ContributingCorrectResponseEvent) event);
    	if (event instanceof ContributingIncorrectResponseEvent)
    		this.onEvent((ContributingIncorrectResponseEvent) event);
    	if (event instanceof CorrectResponseEvent)
    		this.onEvent((CorrectResponseEvent) event);
    	if (event instanceof IncorrectResponseEvent)
    		this.onEvent((IncorrectResponseEvent) event);
    	if (event instanceof NoContributingResponseEvent)
    		this.onEvent((NoContributingResponseEvent) event);
    	if (event instanceof NoResponseEvent)
    		this.onEvent((NoResponseEvent) event);
    	if (event instanceof PrimaryObjectivePercentMasteryEvent)
    		this.onEvent((PrimaryObjectivePercentMasteryEvent) event);
    	if (event instanceof ObjectiveSecondaryNumberCorrectEvent)
    		this.onEvent((ObjectiveSecondaryNumberCorrectEvent) event);
    	if (event instanceof ObjectivePrimaryNumberCorrectEvent)
    		this.onEvent((ObjectivePrimaryNumberCorrectEvent) event);
    	if (event instanceof ObjectivePrimaryCumulativeNumberCorrectEvent)
    		this.onEvent((ObjectivePrimaryCumulativeNumberCorrectEvent) event);
    	if (event instanceof ObjectiveNumberCorrectEvent)
    		this.onEvent((ObjectiveNumberCorrectEvent) event);
    	if (event instanceof ObjectiveRawScoreEvent)
    		this.onEvent((ObjectiveRawScoreEvent) event);
    	if (event instanceof PrimaryObjectiveDerivedScoreEvent)
    		this.onEvent((PrimaryObjectiveDerivedScoreEvent) event);
    	if (event instanceof ObjectiveEvent)
    		this.onEvent((ObjectiveEvent) event);
    	if (event instanceof PointEvent)
    		this.onEvent((PointEvent) event);
    	if (event instanceof ResponseReceivedEvent)
    		this.onEvent((ResponseReceivedEvent) event);
    	if (event instanceof ResponseEvent)
    		this.onEvent((ResponseEvent) event);
    	if (event instanceof ScoreTypeRawScoreEvent)
    		this.onEvent((ScoreTypeRawScoreEvent) event);
    	if (event instanceof ScoreTypeNumberCorrectEvent)
    		this.onEvent((ScoreTypeNumberCorrectEvent) event);
    	if (event instanceof ScoreTypeEvent)
    		this.onEvent((ScoreTypeEvent) event);
    	if (event instanceof ScoringStatusEvent)
    		this.onEvent((ScoringStatusEvent) event);
    	if (event instanceof SubtestContentAreaCompositeScoreEvent)
    		this.onEvent((SubtestContentAreaCompositeScoreEvent) event);
    	if (event instanceof SubtestContentAreaItemCollectionEvent)
    		this.onEvent((SubtestContentAreaItemCollectionEvent) event);
    	if (event instanceof SubtestDerivedScoreEvent)
    		this.onEvent((SubtestDerivedScoreEvent) event);
    	if (event instanceof SubtestEndedEvent)
    		this.onEvent((SubtestEndedEvent) event);
    	if (event instanceof SubtestInvalidEvent)
    		this.onEvent((SubtestInvalidEvent) event);
    	if (event instanceof SubtestItemCollectionEvent)
    		this.onEvent((SubtestItemCollectionEvent) event);
    	if (event instanceof SubtestNumberCorrectEvent)
    		this.onEvent((SubtestNumberCorrectEvent) event);
    	if (event instanceof SubtestObjectiveCollectionEvent)
    		this.onEvent((SubtestObjectiveCollectionEvent) event);
    	if (event instanceof SubtestRawScoreEvent)
    		this.onEvent((SubtestRawScoreEvent) event);
    	if (event instanceof SubtestStartedEvent)
    		this.onEvent((SubtestStartedEvent) event);
    	if (event instanceof SubtestValidEvent)
    		this.onEvent((SubtestValidEvent) event);
    	if (event instanceof SubtestEvent)
    		this.onEvent((SubtestEvent) event);
    	if (event instanceof TestCompletionStatusEvent)
    		this.onEvent((TestCompletionStatusEvent) event);
        if (event instanceof LocatorCompositeScoreEvent)
    		this.onEvent((LocatorCompositeScoreEvent) event);
        if (event instanceof SubtestContentAreaCompositeAndDerivedEvent)     // For Laslink Scoring
    		this.onEvent((SubtestContentAreaCompositeAndDerivedEvent) event);
    }

    /**
	 * @param event
	 */
	public void onEvent(LocatorCompositeScoreEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}

	/**
	 * @param event
	 */
	public void onEvent(PrimaryObjectivePercentMasteryEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}
    
	/**
	 * @param event
	 */
	public void onEvent(ObjectiveEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}

	/**
	 * @param event
	 */
	public void onEvent(ObjectiveNumberCorrectEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}

	/**
	 * @param event
	 */
    public void onEvent(TestCompletionStatusEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}

	/**
	 * @param event
	 */
	public void onEvent(SubtestEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}

	/**
	 * @param event
	 */
	public void onEvent(SubtestValidEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}

	/**
	 * @param event
	 */
	public void onEvent(SubtestStartedEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}

	/**
	 * @param event
	 */
	public void onEvent(SubtestRawScoreEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}

	/**
	 * @param event
	 */
	public void onEvent(SubtestObjectiveCollectionEvent event) throws CTBSystemException {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}

	/**
	 * @param event
	 */
	public void onEvent(SubtestNumberCorrectEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}

	/**
	 * @param event
	 */
	public void onEvent(SubtestItemCollectionEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}

	/**
	 * @param event
	 */
	public void onEvent(SubtestInvalidEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}

	/**
	 * @param event
	 */
	public void onEvent(SubtestEndedEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}

	/**
	 * @param event
	 */
	public void onEvent(SubtestDerivedScoreEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}

	/**
	 * @param event
	 */
	public void onEvent(SubtestContentAreaItemCollectionEvent event) throws CTBSystemException {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}

	/**
	 * @param event
	 */
	public void onEvent(SubtestContentAreaCompositeScoreEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}

	/**
	 * @param event
	 */
	public void onEvent(ScoringStatusEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}

	/**
	 * @param event
	 */
	public void onEvent(ScoreTypeEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}

	/**
	 * @param event
	 */
	public void onEvent(ScoreTypeRawScoreEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}

	/**
	 * @param event
	 */
	public void onEvent(ScoreTypeNumberCorrectEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}

	/**
	 * @param event
	 */
	public void onEvent(ResponseEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}

	/**
	 * @param event
	 */
	public void onEvent(ResponseReceivedEvent event) throws CTBSystemException {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}

	/**
	 * @param event
	 */
	public void onEvent(PointEvent event) throws CTBSystemException {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}

	/**
	 * @param event
	 */
	public void onEvent(PrimaryObjectiveDerivedScoreEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}

	/**
	 * @param event
	 */
	public void onEvent(ObjectiveSecondaryNumberCorrectEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}

	/**
	 * @param event
	 */
	public void onEvent(ObjectiveRawScoreEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}

	/**
	 * @param event
	 */
	public void onEvent(ObjectivePrimaryNumberCorrectEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}
	
	/**
	 * @param event
	 */
	public void onEvent(ObjectivePrimaryCumulativeNumberCorrectEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}

	/**
	 * @param event
	 */
	public void onEvent(NoResponseEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}

	/**
	 * @param event
	 */
	public void onEvent(NoContributingResponseEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}

	/**
	 * @param event
	 */
	public void onEvent(IncorrectResponseEvent event) throws CTBSystemException {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}

	/**
	 * @param event
	 */
	public void onEvent(CorrectResponseEvent event) throws CTBSystemException {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}

	/**
	 * @param event
	 */
	public void onEvent(ContributingPointEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}
	
	/**
	 * @param event
	 */
	public void onEvent(ContributingIncorrectResponseEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}

	/**
	 * @param event
	 */
	public void onEvent(ContributingCorrectResponseEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}

	/**
	 * @param event
	 */
	public void onEvent(ContentAreaRawScoreEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}

	/**
	 * @param event
	 */
	public void onEvent(ContentAreaNumberCorrectEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}
	
	/**
	 * @param event
	 */
	public void onEvent(ContentAreaCumulativeNumberCorrectEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}

	/**
	 * @param event
	 */
	public void onEvent(ContentAreaDerivedScoreEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}

	/**
	 * @param event
	 */
	public void onEvent(AssessmentStartedEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}
    
	/**
	 * @param event
	 */
	public void onEvent(AssessmentEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}

	/**
	 * @param event
	 */
	public void onEvent(AssessmentEndedEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}
	
	
	/**
	 * @param event
	 */
	public void onEvent(SubtestContentAreaCompositeAndDerivedEvent event) {
		//System.out.println("BAD!!! calling empty onEvent() for: " + event.getClass().getName());
	}
}