package com.ctb.lexington.domain.score.scorer.calculator;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.ctb.lexington.data.ItemContentArea;
import com.ctb.lexington.db.data.CurriculumData.PrimaryObjective;
import com.ctb.lexington.db.utils.DatabaseHelper;
import com.ctb.lexington.domain.score.event.AssessmentEndedEvent;
import com.ctb.lexington.domain.score.event.Objective;
import com.ctb.lexington.domain.score.event.ObjectivePrimaryNumberCorrectEvent;
import com.ctb.lexington.domain.score.event.PrimaryObjectivePercentMasteryEvent;
import com.ctb.lexington.domain.score.event.SubtestContentAreaItemCollectionEvent;
import com.ctb.lexington.domain.score.event.SubtestObjectiveCollectionEvent;
import com.ctb.lexington.domain.score.event.SubtestScoreReceivedEvent;
import com.ctb.lexington.domain.score.event.common.EventChannel;
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.domain.score.scorer.ScorerHelper;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.util.SafeHashSet;

public class PrimaryObjectivePercentMasteryCalculator extends Calculator {
    private HashMap subtests = new HashMap();
    private HashSet contentAreaScores = new HashSet();

    public PrimaryObjectivePercentMasteryCalculator(EventChannel channel, Scorer scorer) {
        super(channel, scorer);

        channel.subscribe(this, ObjectivePrimaryNumberCorrectEvent.class);
        channel.subscribe(this, SubtestContentAreaItemCollectionEvent.class);
        channel.subscribe(this, SubtestObjectiveCollectionEvent.class);
        channel.subscribe(this, AssessmentEndedEvent.class);
        mustPrecede(SubtestContentAreaItemCollectionEvent.class, ObjectivePrimaryNumberCorrectEvent.class);
    }

    public void onEvent(SubtestObjectiveCollectionEvent socEvent) {
        getSubtestInfo(DatabaseHelper.asLong(socEvent.getItemSetId())).socEvent = socEvent;
    }

    public void onEvent(ObjectivePrimaryNumberCorrectEvent opncEvent) {
    	//if(scorer.getResultHolder().getAdminData().getProductId() != 8000) {
	        ContentAreaInfo contentArea = getContentAreaScores(opncEvent.getObjectiveId());
	        if (opncEvent.getMastery().isMastered()) {
	            contentArea.primaryObjectivesMastered.add(opncEvent.getObjectiveId());
	        }
	        contentArea.primaryObjectiveCount++;
    	//}
    }

    public void onEvent(SubtestContentAreaItemCollectionEvent event) throws CTBSystemException {
        getSubtestInfo(event.getItemSetId()).scaicEvent = event;

        for (Iterator i = event.getContentAreaNames().iterator(); i.hasNext();) {
            ContentAreaInfo caInfo = new ContentAreaInfo();
            caInfo.subtestId = event.getItemSetId();
            caInfo.contentAreaName = (String) i.next();
            caInfo.contentAreaId = getContentAreaId(event, caInfo.contentAreaName);
            if(scorer.getResultHolder().getAdminData().getProductId() != 8000) {
            	caInfo.objectiveIds = getObjectiveIDsForContentArea(caInfo.contentAreaName, event.getItemSetId());
            	 contentAreaScores.add(caInfo);
            }
            else {
            	caInfo.objectiveIds = getObjectiveIDsForContentAreaAdaptive(caInfo.contentAreaName, event.getItemSetId());
            	//System.out.println("caInfo.objectiveIds.size() -> " + caInfo.objectiveIds.size());
            	
            	// for( i in contentAreaScores)
            	 for (Iterator itr = contentAreaScores.iterator(); itr.hasNext();) {
            		 ContentAreaInfo caInfoinner = (ContentAreaInfo) itr.next();
            		 String contentAreaNames = caInfoinner.contentAreaName;
            		 if(!contentAreaNames.equalsIgnoreCase(event.getItemSetName())) {
            			 caInfo.contentAreaName = event.getItemSetName();
                    	 caInfo.contentAreaId = event.getItemSetId();
                    	 contentAreaScores.add(caInfo);
            			 break;
            		 }
            	 }
            	  if(contentAreaScores.size()<=0){
        			 caInfo.contentAreaName = event.getItemSetName();
                	 caInfo.contentAreaId = event.getItemSetId();
                	 contentAreaScores.add(caInfo);
        			 
        		 }
            	  break;
            		 
            	
            }
            
           
        }
    }

    public void onEvent(AssessmentEndedEvent event) {
        for (Iterator iterator = contentAreaScores.iterator(); iterator.hasNext();) {
            ContentAreaInfo caInfo = (ContentAreaInfo) iterator.next();
            SubtestInfo subtest = (SubtestInfo) subtests.get(caInfo.subtestId);

            channel.send(new PrimaryObjectivePercentMasteryEvent(
                    subtest.socEvent.getTestRosterId(),
                    DatabaseHelper.asInteger(caInfo.subtestId),
                    caInfo.contentAreaId, caInfo.contentAreaName,
                    caInfo.calculatePercentMastery()));
        }
    }

    private Long getContentAreaId(SubtestContentAreaItemCollectionEvent event, String contentAreaName) {
        return ((ItemContentArea)event.getItemContentAreasFor(contentAreaName).iterator().next()).getContentAreaId();
    }

    private ContentAreaInfo getContentAreaScores(Long objectiveId) {
        for (Iterator i = contentAreaScores.iterator(); i.hasNext();) {
            ContentAreaInfo info = (ContentAreaInfo) i.next();
            if (info.objectiveIds.contains(objectiveId))
                return info;
        }
        throw new IllegalStateException("Objective " + objectiveId + " not part of a known content area.");
    }

    private Collection getObjectiveIDsForContentArea(String contentArea, Long subtestId) throws CTBSystemException {
        Collection result = new SafeHashSet(Long.class);
        SubtestInfo subtest = (SubtestInfo) subtests.get(subtestId);

        for (Iterator iterator = subtest.scaicEvent.getItemContentAreasFor(contentArea).iterator(); iterator
                .hasNext();) {
            ItemContentArea itemContentArea = (ItemContentArea) iterator.next();
            
            List<Objective> primaryReportingLevelObjectiveList = subtest.socEvent.getPrimaryReportingLevelObjective(itemContentArea.getItemId());
            for(Objective primaryReportingLevelObjective: primaryReportingLevelObjectiveList ){
            result.add(primaryReportingLevelObjective.getId());
        	}
        }

        return result;
    }
    
    private Collection getObjectiveIDsForContentAreaAdaptive(String contentArea, Long subtestId) throws CTBSystemException {
        Collection result = new SafeHashSet(Long.class);
        String subtestIdVal = "8001"+String.valueOf(subtestId);
        subtestId = Long.parseLong(subtestIdVal);
        PrimaryObjective[] primObjs = scorer.getResultHolder().getCurriculumData().getPrimaryObjectives();
        if(primObjs != null) {
        	for (int i= 0; i < primObjs.length; i++) {
        		if(primObjs[i].getContentAreaId().longValue() == subtestId.longValue())
        			result.add(primObjs[i].getPrimaryObjectiveId());
        	}
        }

        return result;
    }

    private SubtestInfo getSubtestInfo(Long subtestId) {
        SubtestInfo info = (SubtestInfo) subtests.get(subtestId);
        if (null==info) {
            info = new SubtestInfo();
            subtests.put(subtestId, info);
        }
        return info;
    }

    private class ContentAreaInfo {
        private Long subtestId;
        private String contentAreaName;
        private Long contentAreaId;
        private Collection objectiveIds;
        private Set primaryObjectivesMastered = new HashSet();
        
        private int primaryObjectiveCount = 0;

        private int calculatePercentMastery() {
            int objectivesMastered = 0;

            for (final Iterator it = objectiveIds.iterator(); it.hasNext();) {
                if (primaryObjectivesMastered.contains(it.next())) {
                    objectivesMastered++;
                }
            }

            return ScorerHelper.calculatePercentage(objectivesMastered, primaryObjectiveCount);
        }
    }

    private class SubtestInfo {
        private SubtestContentAreaItemCollectionEvent scaicEvent;
        private SubtestObjectiveCollectionEvent socEvent;
    }
}