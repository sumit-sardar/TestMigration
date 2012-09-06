package com.ctb.lexington.domain.score.scorer.calculator;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.ctb.lexington.data.ItemVO;
import com.ctb.lexington.db.data.StudentItemScoreData;
import com.ctb.lexington.db.data.StudentItemScoreDetails;
import com.ctb.lexington.db.data.CurriculumData.ContentArea;
import com.ctb.lexington.db.mapper.StudentItemSetStatusMapper;
import com.ctb.lexington.db.utils.DatabaseHelper;
import com.ctb.lexington.domain.score.event.Objective;
import com.ctb.lexington.domain.score.event.SubtestEndedEvent;
import com.ctb.lexington.domain.score.event.SubtestEvent;
import com.ctb.lexington.domain.score.event.SubtestInvalidEvent;
import com.ctb.lexington.domain.score.event.SubtestItemCollectionEvent;
import com.ctb.lexington.domain.score.event.SubtestObjectiveCollectionEvent;
import com.ctb.lexington.domain.score.event.SubtestValidEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.exception.CTBSystemException;

/**
 * <p>
 * Validates subtests including at least one of:
 * </p>
 * <ul>
 * <li>The test-taker answered at least {@link #MINIMUM_ANSWERED_ITEMS} items.</li>
 * <li>The test-taker answered correctly at least {@link #MINIMUM_CORRECT_ANSWERS} items.</li>
 * </ul>
 * <p>
 * These rules apply <strong>only </strong> for online tests, not for key entry or scan score, and
 * they apply <strong>only </strong> to <em>SR</em> items. But after introduction of laslink product, it is 
 * also done for CR items.
 * </p>
 * 
 * @todo Need to check capture method since key entry isn't restricted the way online is
 * @todo Update class javadocs to reflect the business rules
 */
public class SubtestValidationCalculator extends Calculator {
    /**
     * At least this many must be correctly answered.
     */
    public static final int MINIMUM_CORRECT_ANSWERS = 1;
    /**
     * At least this many must be attempted.
     */
    public static final int MINIMUM_ANSWERED_ITEMS = 5;

    //private SubtestItemCollectionEvent subtestItemCollectionEvent;
    private Collection items = null;
    private String subtestName = null;
    private SubtestObjectiveCollectionEvent subtestObjectives = null;
    private Long testRosterId;
    private Integer ItemSetId;
    
    
    /**
     * Constructs a new <code>SubtestValidationCalculator</code> with the given <var>channel
     * </var> and <var>scorer </var>.
     * 
     * @param channel the channel
     * @param scorer the scorer
     */
    public SubtestValidationCalculator(final Channel channel, final Scorer scorer) {
        super(channel, scorer);

        channel.subscribe(this, SubtestItemCollectionEvent.class);
        channel.subscribe(this, SubtestObjectiveCollectionEvent.class);
       // mustPrecede(SubtestItemCollectionEvent.class, SubtestObjectiveCollectionEvent.class);
        mustPrecede(SubtestItemCollectionEvent.class, SubtestEndedEvent.class);
        channel.subscribe(this, SubtestEndedEvent.class);
    }
    
    public void onEvent(SubtestObjectiveCollectionEvent event) {
        this.subtestObjectives = event;
    }
    public void onEvent(final SubtestEndedEvent event) {
    	// We want only content areas that are part of this subtest
        List contentAreaNames = extractContentAreaNames(new Long(event.getItemSetId().intValue()));
        final SubtestEvent publishedEvent;
        this.testRosterId = event.getTestRosterId();
        this.ItemSetId = event.getItemSetId();

        if (isValid()) {
            publishedEvent = new SubtestValidEvent(
            		event.getTestRosterId(), 
					event.getItemSetId(),
					contentAreaNames);
        } else {
            publishedEvent = new SubtestInvalidEvent(event.getTestRosterId(), event.getItemSetId(),
                    contentAreaNames);
        }

        channel.send(publishedEvent);
    }

    public void onEvent(final SubtestItemCollectionEvent subtestItemCollectionEvent) {
        if(this.items == null || !(
          subtestItemCollectionEvent.getItemSetName().replaceAll("Part 1", "").equals(this.subtestName.replaceAll("Part 2", "")) || 
          subtestItemCollectionEvent.getItemSetName().replaceAll("Part 2", "").equals(this.subtestName.replaceAll("Part 1", "")))) {
            this.subtestName = subtestItemCollectionEvent.getItemSetName();
            this.items = new ArrayList();  
        }
        this.items.addAll(subtestItemCollectionEvent.getItems());
        
    }

    private List extractContentAreaNames(Long subtestId) {
        ContentArea [] contentAreas = scorer.getResultHolder().getCurriculumData().getAllContentAreas();
        if (contentAreas == null || contentAreas.length < 1) {
            return new ArrayList();
        }
        
        List result = new ArrayList(contentAreas.length);
        for (int i = 0; i< contentAreas.length; i++) {
            ContentArea contentArea = contentAreas[i];
            // the curriculum name is the name of the framework, we want the node name.
            if(subtestId.equals(contentArea.getSubtestId()))
            	result.add(contentArea.getContentAreaName());
        }
        return result;
    }

    private boolean isValid() {
        final StudentItemScoreData studentData = scorer.getResultHolder().getStudentItemScoreData();
        //Added for Laslink Product
        String productType = scorer.getResultHolder().getAdminData().getAssessmentType();
        if(productType.equals("LL") || productType.equals("ll")) {
        	String valid = "IN";
        	
        	Connection conn = null;
            try {
                conn = scorer.getOASConnection();
                StudentItemSetStatusMapper mapper = new StudentItemSetStatusMapper(conn);
                valid = mapper.getSubtestValidationStatusForTestRosterAndItemSetId(this.testRosterId, 
                		DatabaseHelper.asLong(this.ItemSetId));
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    scorer.close(true, conn);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        	if(valid.equals("VA"))
        		return true;
        	else
        		return false;
        }
        
      //Added for Terra Nova For G for which subtest level invalidation need to be handled.
        if(scorer.getResultHolder().getAdminData().getProductId() == 3700) {
        	String valid = "IN";
        	Connection conn = null;
            try {
                conn = scorer.getOASConnection();
                StudentItemSetStatusMapper mapper = new StudentItemSetStatusMapper(conn);
                valid = mapper.getSubtestValidationStatusForTestRosterAndItemSetId(this.testRosterId, 
                		DatabaseHelper.asLong(this.ItemSetId));
                //System.out.println(this.ItemSetId + " ==== " + valid);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    scorer.close(true, conn);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        	if(!valid.equals("VA"))
        		return false;
        }
        
        int attemptedItems = 0;
        int correctAnswers = 0;
        java.util.Set<String> attemptedSet = new java.util.HashSet<String>();

        for (Iterator iter = this.items.iterator(); iter.hasNext();) {
            final ItemVO item = (ItemVO) iter.next();

            if (!isItemToCheck(item))
                continue;
           
            final String itemId = item.getItemId();
            try{
            List<Objective> primaryList = subtestObjectives.getPrimaryReportingLevelObjective(itemId);
            
            for(Objective primary:primaryList){
            if (!hasItemScoreDetails(studentData, itemId+primary.getName()))
                throw new IllegalStateException("Missing item score details: " + itemId);

            final StudentItemScoreDetails detail = studentData.get(itemId + primary.getName());
            if (!hasResponse(detail))
                continue;
            if(ItemVO.ITEM_TYPE_SR.equals(item.getItemType())){
            if (isCorrectAnswer(detail)) {
                ++correctAnswers;
            }
                if (answeredEnoughCorrectly(correctAnswers))
                    return true;
            }

            // for subtest validation, the existence of any response indicates an attempt,
            // regardless of whether or not the corresponding condition code has the 
            // attempted flag set.
            if (hasResponse(detail)) {
            	attemptedSet.add(itemId);//++attemptedItems;
            	attemptedItems = attemptedSet.size();

                if (attemptedEnoughItems(attemptedItems))
                    return true;
            }
            }
            } catch(CTBSystemException e){
            	
            }
            
        }

        return false;
    }

    private static boolean answeredEnoughCorrectly(final int correctAnswers) {
        return correctAnswers >= MINIMUM_CORRECT_ANSWERS;
    }

    private static boolean attemptedEnoughItems(final int attemptedItem) {
        return attemptedItem >= MINIMUM_ANSWERED_ITEMS;
    }

    private static boolean hasItemScoreDetails(final StudentItemScoreData studentData,
            final String itemId) {
        return studentData.contains(itemId);
    }

    private boolean isCorrectAnswer(final StudentItemScoreDetails detail) {
        return detail.getCorrectAnswer().equals(detail.getResponse());
    }

    /**
     * Only works for SR items.
     */
    private static boolean isItemToCheck(final ItemVO item) {
        return ItemVO.ITEM_TYPE_SR.equals(item.getItemType());
    }

    private static boolean hasResponse(final StudentItemScoreDetails detail) {
        return detail.getResponse() != null && !detail.getResponse().equals("-");
    }
}