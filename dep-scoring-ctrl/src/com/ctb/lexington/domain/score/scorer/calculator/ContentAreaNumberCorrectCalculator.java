package com.ctb.lexington.domain.score.scorer.calculator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ctb.lexington.data.ItemContentArea;
import com.ctb.lexington.db.utils.DatabaseHelper;
import com.ctb.lexington.domain.score.event.ContentAreaNumberCorrectEvent;
import com.ctb.lexington.domain.score.event.CorrectResponseEvent;
import com.ctb.lexington.domain.score.event.IncorrectResponseEvent;
import com.ctb.lexington.domain.score.event.NoResponseEvent;
import com.ctb.lexington.domain.score.event.Objective;
import com.ctb.lexington.domain.score.event.ResponseEvent;
import com.ctb.lexington.domain.score.event.ResponseReceivedEvent;
import com.ctb.lexington.domain.score.event.SubtestContentAreaItemCollectionEvent;
import com.ctb.lexington.domain.score.event.SubtestEndedEvent;
import com.ctb.lexington.domain.score.event.SubtestItemCollectionEvent;
import com.ctb.lexington.domain.score.event.SubtestObjectiveCollectionEvent;
import com.ctb.lexington.domain.score.event.SubtestScoreReceivedEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.domain.score.scorer.TAScorer;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.util.SafeHashMap;

/**
 * This class counts the number of correct answers for all content areas for specific roster
 * instance.
 * 
 * @author mnkamiya
 * @version $Id$
 */
public class ContentAreaNumberCorrectCalculator extends AbstractResponseCalculator {
    protected Long itemSetId;
    protected final Map contentAreaAnswers = new SafeHashMap(String.class, ContentAreaAnswers.class);
    protected Map itemsByContentArea = new SafeHashMap(String.class, ItemContentArea.class);
    private SubtestItemCollectionEvent sicEvent;
    private Set contenAreaSet = new HashSet();
    protected Map adaptiveContentDetails  = new SafeHashMap(Integer.class, SubtestScoreReceivedEvent.class);

    public ContentAreaNumberCorrectCalculator(Channel channel, Scorer scorer) {
        super(channel, scorer);
        channel.subscribe(this, SubtestContentAreaItemCollectionEvent.class);
        //TODO: Uncomment this when we can handle multiple predecessors for an event
        //mustPrecede(SubtestContentAreaItemCollectionEvent.class, ResponseReceivedEvent.class);
        channel.subscribe(this, CorrectResponseEvent.class);
        mustPrecede(SubtestContentAreaItemCollectionEvent.class, CorrectResponseEvent.class);
        channel.subscribe(this, IncorrectResponseEvent.class);
        mustPrecede(SubtestContentAreaItemCollectionEvent.class, IncorrectResponseEvent.class);
        channel.subscribe(this, NoResponseEvent.class);
        mustPrecede(SubtestContentAreaItemCollectionEvent.class, NoResponseEvent.class);
        channel.subscribe(this, SubtestScoreReceivedEvent.class); // changes for TABE Adaptive
        channel.subscribe(this, SubtestEndedEvent.class);
        mustPrecede(SubtestContentAreaItemCollectionEvent.class, SubtestEndedEvent.class);
    }
    
    
    public void onEvent(SubtestItemCollectionEvent event) {
        super.onEvent(event);

        sicEvent = event;
    }
    
    
 // changes for TABE Adaptive
    public void onEvent(SubtestScoreReceivedEvent event) {
    	
    	if (!adaptiveContentDetails.containsKey(event.getItemSetId()))
    		adaptiveContentDetails.put(event.getItemSetId(), event);
    	
    }    

    public void onEvent(SubtestContentAreaItemCollectionEvent event) {
    	
        itemSetId = event.getItemSetId();
        itemsByContentArea = event.getItemContentAreasByItemId();
        contenAreaSet = new HashSet();
        for (final Iterator it = itemsByContentArea.values().iterator(); it.hasNext();) {
            final ItemContentArea itemContentArea = (ItemContentArea) it.next();
            final ContentAreaAnswers currentAreaAnswers = getContentAreaAnswersByName(itemContentArea);
            currentAreaAnswers.incrementNumberOfItems();
            // NOTE: Assume all to be unattempted till we receive a response event
            currentAreaAnswers.unattemptedAnswers.add(itemContentArea.getItemId());
            contenAreaSet.add(itemContentArea.getContentAreaName());

        }
    }

    private ContentAreaAnswers getContentAreaAnswersByName(final ItemContentArea itemContentArea) {
        final String contentAreaName = itemContentArea.getContentAreaName();

        if (!contentAreaAnswers.containsKey(contentAreaName))
            // Keyed by name instead of ID so that different ItemSets with
            // the same content area name are rolled up into a single
            // record. Could be keyed by ID if we want to send a score
            // event for each ItemSet, regardless of name.
            contentAreaAnswers.put(contentAreaName, new ContentAreaAnswers(itemContentArea
                    .getContentAreaId(), contentAreaName));

        return getContentAreaByName(contentAreaName);
    }

    private ContentAreaAnswers getContentAreaByName(final String contentAreaName) {
        return (ContentAreaAnswers) contentAreaAnswers.get(contentAreaName);
    }

    public void onEvent(ResponseReceivedEvent event) {
        validateItemSetId(event.getItemSetId());

        
        // START For Laslink Scoring
        final List<ContentAreaAnswers> summaryList = getSummaryForContentArea(event);
        final String itemId = event.getItemId();
        for (ContentAreaAnswers summary : summaryList) {
	        if (sicEvent.isAttempted(event)) {
	            summary.attemptedAnswers.add(itemId);
	            summary.unattemptedAnswers.remove(itemId);
	        } else {
	            summary.attemptedAnswers.remove(itemId);
	            summary.unattemptedAnswers.add(itemId);
	        }
	        // END For Laslink Scoring
        }
    }

    private List <ContentAreaAnswers> getSummaryForContentArea(ResponseEvent event) {
       // START For Laslink Scoring
       List <ContentAreaAnswers> contentAreaAnswersList =  new ArrayList() ;
    	Iterator itr = contenAreaSet.iterator();
	    	while(itr.hasNext()) {
	    		String contentArea = (String)itr.next();
	    		 if(itemsByContentArea.containsKey(event.getItemId() + contentArea)){
	    			 final ItemContentArea itemContentArea = (ItemContentArea) itemsByContentArea.get(event
	    		                .getItemId() + contentArea);
	    		    		contentAreaAnswersList.add( getContentAreaByName(itemContentArea.getContentAreaName()));
	    	    }
	    		
	       
	    	}
    	 return contentAreaAnswersList;
    	 // END For Laslink Scoring
    }

    public void onEvent(CorrectResponseEvent event) {
    	 //  For Laslink Scoring
        final List<ContentAreaAnswers> summaryList = getSummaryForContentArea(event);
        for (ContentAreaAnswers summary : summaryList) {
        	 summary.correctAnswers.add(event.getItemId());
             summary.incorrectAnswers.remove(event.getItemId());
    	}
    }
     //  For Laslink Scoring
  
    public void onEvent(IncorrectResponseEvent event) {
        
        final List<ContentAreaAnswers> summaryList = getSummaryForContentArea(event);
        for (ContentAreaAnswers summary : summaryList) {
        	 summary.incorrectAnswers.add(event.getItemId());
             summary.correctAnswers.remove(event.getItemId());
    	}
    }
 //  For Laslink Scoring
    public void onEvent(NoResponseEvent event) {
      
        final List<ContentAreaAnswers> summaryList = getSummaryForContentArea(event);
        for (ContentAreaAnswers summary : summaryList) {
        	summary.incorrectAnswers.remove(event.getItemId());
            summary.correctAnswers.remove(event.getItemId());
    	}
    }
 //  For Laslink Scoring
    public void onEvent(SubtestEndedEvent event) {
    	if (sicEvent.getProductId() == 8001) {
    		//System.out.println("sicEvent.getProductId() == 8001");
    		Iterator iter = adaptiveContentDetails.values().iterator();
    		while (iter.hasNext()) {
            	
                final SubtestScoreReceivedEvent summary = (SubtestScoreReceivedEvent) iter.next();
              //  System.out.println("summary.contentAreaName hostAdaptive -> " + summary.getContentAreaName());
                channel.send(new ContentAreaNumberCorrectEvent(event.getTestRosterId(), DatabaseHelper
                        .asLong(event.getItemSetId()), DatabaseHelper
                        .asLong(summary.getItemSetId()), summary.getContentAreaName(),
                        0, 0, 0, 0, 0));
            }
    	} else {
	        Iterator iter = contentAreaAnswers.values().iterator();
	        while (iter.hasNext()) {
	        	
	            final ContentAreaAnswers summary = (ContentAreaAnswers) iter.next();
	         //   System.out.println("summary.contentAreaId -> " + summary.contentAreaId);
	         //   System.out.println("summary.contentAreaName -> " + summary.contentAreaName);
	            channel.send(new ContentAreaNumberCorrectEvent(event.getTestRosterId(), DatabaseHelper
	                    .asLong(event.getItemSetId()), summary.contentAreaId, summary.contentAreaName,
	                    summary.numberOfItems, summary.correctAnswers.size(), summary.incorrectAnswers
	                            .size(), summary.attemptedAnswers.size(), summary.unattemptedAnswers
	                            .size()));
	        }
	        contentAreaAnswers.clear();
    	}
    }

    protected class ContentAreaAnswers {
        protected String contentAreaName;
        protected Long contentAreaId;
        protected int numberOfItems;
        protected Set correctAnswers;
        protected Set incorrectAnswers;
        protected Set attemptedAnswers;
        protected Set unattemptedAnswers;

        ContentAreaAnswers(Long contentAreaId, String contentAreaName) {
            correctAnswers = new HashSet();
            attemptedAnswers = new HashSet();
            incorrectAnswers = new HashSet();
            unattemptedAnswers = new HashSet();
            this.contentAreaName = contentAreaName;
            this.contentAreaId = contentAreaId;
        }

        void incrementNumberOfItems() {
            numberOfItems++;
        }

        // Adding equals/hashCode fixes several memory issues where code makes sets
        // of this object, as well as fixing logic issues
        public boolean equals(Object other) {
            if (this == other)
                return true;
            if (! (other instanceof ContentAreaAnswers))
                return false;

            final ContentAreaAnswers ca = (ContentAreaAnswers) other;

            if (!contentAreaId.equals(ca.contentAreaId))
                return false;

            return true;
        }

        public int hashCode() {
            return contentAreaId.hashCode();
        }
    }
}