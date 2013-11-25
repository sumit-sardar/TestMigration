package com.ctb.lexington.domain.score.scorer.calculator;

import gscode.Score;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ctb.lexington.data.ItemContentArea;
import com.ctb.lexington.domain.score.event.ContentAreaRawScoreEvent;
import com.ctb.lexington.domain.score.event.Objective;
import com.ctb.lexington.domain.score.event.PointEvent;
import com.ctb.lexington.domain.score.event.SubtestContentAreaItemCollectionEvent;
import com.ctb.lexington.domain.score.event.SubtestEndedEvent;
import com.ctb.lexington.domain.score.event.SubtestItemCollectionEvent;
import com.ctb.lexington.domain.score.event.SubtestObjectiveCollectionEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.domain.score.scorer.ScorerHelper;
import com.ctb.lexington.domain.score.scorer.calculator.ContentAreaNumberCorrectCalculator.ContentAreaAnswers;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.util.SafeHashMap;

/**
 * @todo The calculator depends on the order of events -- it holds an SCAIC event over as a private
 *       member
 */
public class ContentAreaRawScoreCalculator extends Calculator {
    private final Map contentAreas = new SafeHashMap(String.class, Map.class);
    private final Map possibleMap = new SafeHashMap(String.class, Integer.class);
    private SubtestContentAreaItemCollectionEvent subtestContentArea = null;
    private Set contenAreaSet = new HashSet();
    private SubtestItemCollectionEvent sicEvent;
    /**
     * Constructor for ContentAreaRawScoreCalculator.
     * 
     * @param channel
     * @param scorer
     */
    public ContentAreaRawScoreCalculator(Channel channel, Scorer scorer) {
        super(channel, scorer);
        channel.subscribe(this, SubtestItemCollectionEvent.class);
        channel.subscribe(this, SubtestContentAreaItemCollectionEvent.class);
        channel.subscribe(this, PointEvent.class);
        channel.subscribe(this, SubtestEndedEvent.class);
        mustPrecede(SubtestContentAreaItemCollectionEvent.class, PointEvent.class);
        mustPrecede(SubtestContentAreaItemCollectionEvent.class, SubtestEndedEvent.class);
    }
   
    public void onEvent(SubtestItemCollectionEvent event) {
        super.onEvent(event);
        sicEvent = event;
    }
    
    public void onEvent(SubtestContentAreaItemCollectionEvent event) {
        subtestContentArea = event;
        contenAreaSet = new HashSet();
        Iterator iter = event.getContentAreaNames().iterator();
        while (iter.hasNext()) {
            String contentAreaName = (String) iter.next();
            contenAreaSet.add(contentAreaName);
            contentAreas.put(contentAreaName, new HashMap()); // nulls permitted as values
            Map itemContentAreaMap = event.getItemContentAreasByItemId();
            Iterator itemContentAreaIter = itemContentAreaMap.values().iterator();
            int contentAreaPointsPossible = 0;
            while (itemContentAreaIter.hasNext()) {
                ItemContentArea ica = (ItemContentArea) itemContentAreaIter.next();
                if (ica.getContentAreaName().equals(contentAreaName)) {
                    contentAreaPointsPossible += ica.getMaxPoints().intValue();
                }
            }
            possibleMap.put(contentAreaName, new Integer(contentAreaPointsPossible));
        }
    }
 //  For Laslink Scoring
    public void onEvent(PointEvent event) {
    	List <String> ContentAreaNameList =  getItemContentAreaFor(event);
    	for (String ContentAreaName :ContentAreaNameList) {
    		 setItemPointsForContentArea(contentAreas, ContentAreaName, event);
    	}
       
    }
 //  For Laslink Scoring
    public void onEvent(SubtestEndedEvent event) {
    	/*if("TS".equals(sicEvent.getProductType())){
    		calculateAndSendRawScoreEventForTASC(event.getTestRosterId(), contentAreas, event.getItemSetId());
    	}else{*/
    		calculateAndSendRawScoreEvent(event.getTestRosterId(), contentAreas, event.getItemSetId());   //  For Laslink Scoring
    	//}
    }

    private void setItemPointsForContentArea(final Map map, final String contentArea,
            final PointEvent event) {
        final Map itemPointsForContentArea = (Map) map.get(contentArea);

        itemPointsForContentArea.put(event.getItemId(), new ItemScore(event.getPointsObtained(),
                event.getPointsAttempted()));
    }
 //  For Laslink Scoring
    private void calculateAndSendRawScoreEvent(final Long testRosterId, final Map contentAreaMap, final Integer itemSetId ) {
        for (final Iterator it = contentAreaMap.entrySet().iterator(); it.hasNext();) {
            final Map.Entry entry = (Map.Entry) it.next();
            final String contentArea = (String) entry.getKey();
            final Map itemMap = (Map) entry.getValue();

            int pointsObtained = 0;
            int pointsAttempted = 0;

            for (final Iterator scoreIt = itemMap.values().iterator(); scoreIt.hasNext();) {
                final ItemScore score = (ItemScore) scoreIt.next();

                if (score != null && score.getPointsObtained() != null)
                    pointsObtained += score.getPointsObtained().intValue();
                if (score != null && score.getPointsAttempted() != null)
                    pointsAttempted += score.getPointsAttempted().intValue();
            }

            final int pointsPossible = getPointsPossibleFor(contentArea);

            channel.send(new ContentAreaRawScoreEvent(testRosterId, contentArea, pointsObtained,
                    pointsAttempted, ScorerHelper.calculatePercentage(pointsObtained,
                            pointsPossible), pointsPossible, contentAreaMap, itemSetId));   //  For Laslink Scoring
        }

        contentAreas.clear();
        possibleMap.clear();
    }
 //  For Laslink Scoring
    private List<String> getItemContentAreaFor(final PointEvent event) {
    	List <String> ContentAreaNameList =  new ArrayList() ;
    	Iterator itr = contenAreaSet.iterator();
	    	while(itr.hasNext()) {
	    		String contentArea = (String)itr.next();
	    		 if(subtestContentArea.getItemContentAreasByItemId().containsKey(event.getItemId() + contentArea)){
	    		 ContentAreaNameList.add( ((ItemContentArea) subtestContentArea.getItemContentAreasByItemId().get(
	    	                event.getItemId()+ contentArea)).getContentAreaName());
	    		 }
	       
	    	}
        return ContentAreaNameList;
    }
 //  For Laslink Scoring
    private int getPointsPossibleFor(final String contentArea) {
        return ((Integer) possibleMap.get(contentArea)).intValue();
    }

    private class ItemScore {
        private Integer pointsObtained;
        private Integer pointsAttempted;

        public ItemScore(Integer pointsObtained, Integer pointsAttempted) {
            this.pointsObtained = pointsObtained;
            this.pointsAttempted = pointsAttempted;
        }

        public Integer getPointsAttempted() {
            return pointsAttempted;
        }

        public Integer getPointsObtained() {
            return pointsObtained;
        }
    }
    
    //For TASC Scoring
    private void calculateAndSendRawScoreEventForTASC(final Long testRosterId, final Map contentAreaMap, final Integer itemSetId ) {
    	HashMap<String, Object[]> contentAreaPoints = new HashMap<String, Object[]>();
    	for (final Iterator it = contentAreaMap.entrySet().iterator(); it.hasNext();) {
            final Map.Entry entry = (Map.Entry) it.next();
            final String contentArea = (String) entry.getKey();
            final Map itemMap = (Map) entry.getValue();

            int pointsObtained = 0;
            int pointsAttempted = 0;

            for (final Iterator scoreIt = itemMap.values().iterator(); scoreIt.hasNext();) {
                final ItemScore score = (ItemScore) scoreIt.next();

                if (score != null && score.getPointsObtained() != null)
                    pointsObtained += score.getPointsObtained().intValue();
                if (score != null && score.getPointsAttempted() != null)
                    pointsAttempted += score.getPointsAttempted().intValue();
            }
            
            if(contentAreaPoints.containsKey(contentArea)){
            	Object [] points = contentAreaPoints.get(contentArea);
            	Integer pointsObtainedObj = (Integer)points[0];
            	Integer pointsAttemptedObj = (Integer)points[1];
            	pointsObtainedObj += pointsObtained;
            	pointsAttemptedObj += pointsAttempted;
            	points[0] = pointsObtainedObj;
            	points[1] = pointsAttemptedObj;
            	contentAreaPoints.remove(contentArea);
            	contentAreaPoints.put(contentArea, points);
            }else{
            	Object [] obj = new Object[2];
                obj[0] = pointsObtained;
                obj[1] = pointsAttempted;
                contentAreaPoints.put(contentArea,obj);
            }
            
//            final int pointsPossible = getPointsPossibleFor(contentArea);

//            channel.send(new ContentAreaRawScoreEvent(testRosterId, contentArea, pointsObtained,
//                    pointsAttempted, ScorerHelper.calculatePercentage(pointsObtained,
//                            pointsPossible), pointsPossible, contentAreaMap, itemSetId)); 
        }
    	for(final Iterator it = contentAreaMap.entrySet().iterator(); it.hasNext();){
    		final Map.Entry entry = (Map.Entry) it.next();
    		final String contentArea = (String) entry.getKey();
    		Object[] points = contentAreaPoints.get(contentArea);
    		Integer pointsObtained = (Integer)points[0];
        	Integer pointsAttempted = (Integer)points[1];
            final int pointsPossible = getPointsPossibleFor(contentArea);
            
            channel.send(new ContentAreaRawScoreEvent(testRosterId, contentArea, pointsObtained.intValue(),
                  pointsAttempted.intValue(), ScorerHelper.calculatePercentage(pointsObtained,
                          pointsPossible), pointsPossible, contentAreaMap, itemSetId));
    	}

        contentAreas.clear();
        possibleMap.clear();
    }
}