package com.ctb.lexington.domain.score.scorer.calculator;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ctb.lexington.db.data.SubtestContentAreaCompositeAndDerivedScore;
import com.ctb.lexington.db.data.CurriculumData.ContentArea;
import com.ctb.lexington.domain.score.event.AssessmentEndedEvent;
import com.ctb.lexington.domain.score.event.AssessmentStartedEvent;
import com.ctb.lexington.domain.score.event.ContentAreaDerivedScoreEvent;
import com.ctb.lexington.domain.score.event.ContentAreaRawScoreEvent;
import com.ctb.lexington.domain.score.event.SubtestContentAreaCompositeAndDerivedEvent;
import com.ctb.lexington.domain.score.event.SubtestContentAreaCompositeScoreEvent;
import com.ctb.lexington.domain.score.event.SubtestInvalidEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.domain.score.scorer.ScorerHelper;
import com.ctb.lexington.domain.teststructure.ScoreLookupCode;
import com.ctb.lexington.util.CTBConstants;
import com.ctb.lexington.util.SafeHashMap;
import com.ctb.lexington.util.Timer;

/**
 * Calculates composite scores for TABE assessments. Publishes at most two events. Both are
 * instances SubtestContentAreaCompositeScoreEvent. The first event contains the total mathematics
 * scores, and the second contains the total battery scores. Each event requires scores for specific
 * content areas.
 * 
 * @see com.ctb.lexington.domain.score.event.SubtestContentAreaCompositeScoreEvent
 */
public class TASCCompositeScoreCalculator extends AbstractDerivedScoreCalculator {
    public final static String READING_CONTENT_AREA_NAME = "Reading";
    public final static String WRITING_CONTENT_AREA_NAME = "Writing";
    public final static String MATH_CONTENT_AREA_NAME = "Mathematics";
    public final static String SCIENCE_CONTENT_AREA_NAME = "Science";
    public final static String SOCIAL_CONTENT_AREA_NAME = "Social Studies";

    public static final String OVERALL_CONTENT_AREA_NAME = "Overall score";
    public static final String ELA_CONTENT_AREA_NAME = "ELA (Composite of Reading and Writing)";

    public static final String OVERALL_COMPOSITE_TYPE = "Overall score";
    public static final String ELA_COMPOSITE_TYPE = "ELA (Composite of Reading and Writing)";

    private static final String TASC_FRAMEWORK_CODE = "TASC";

    private final Map<String,ContentAreaDerivedScoreEvent> contentAreaDerivedScoreEvents = new SafeHashMap(String.class, ContentAreaDerivedScoreEvent.class);
    private final Map<String,ContentAreaRawScoreEvent> contentAreaRawScoreEvents = new SafeHashMap(String.class, ContentAreaRawScoreEvent.class);
    private final Map<String,String> contentAreaSubtestId = new SafeHashMap(String.class,String.class);
    private final Map<String,Integer> contentAreaScaleScore = new SafeHashMap(String.class,Integer.class);
    
    private final ArrayList<String> contentAreaNamesRequiredForOverallComposite = new ArrayList<String>();
    private final ArrayList<String> contentAreaNamesRequiredForELAComposite = new ArrayList<String>();

    
    private boolean OverallCompositeDerivedScoresCalculated;
    private boolean ELACompositeDerivedScoresCalculated;

    private final CompositeScoreHolder OverallCompositeScores;
    private final CompositeScoreHolder ELACompositeScores;
    private List<Integer> invalidSubtestIds;
    
    private final Map<String,SubtestContentAreaCompositeAndDerivedScore> SubtestContentAreaCompositeAndDerivedScore = new SafeHashMap(String.class,SubtestContentAreaCompositeAndDerivedScore.class);

    public TASCCompositeScoreCalculator(Channel channel, Scorer scorer) {
        super(channel, scorer);

        contentAreaNamesRequiredForOverallComposite.add(READING_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForOverallComposite.add(WRITING_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForOverallComposite.add(MATH_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForOverallComposite.add(SCIENCE_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForOverallComposite.add(SOCIAL_CONTENT_AREA_NAME);

        contentAreaNamesRequiredForELAComposite.add(READING_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForELAComposite.add(WRITING_CONTENT_AREA_NAME);

        OverallCompositeDerivedScoresCalculated = false;
        OverallCompositeScores = new CompositeScoreHolder();

        ELACompositeDerivedScoresCalculated = false;
        ELACompositeScores = new CompositeScoreHolder();

        channel.subscribe(this, ContentAreaDerivedScoreEvent.class);
        channel.subscribe(this, ContentAreaRawScoreEvent.class);
        channel.subscribe(this, SubtestInvalidEvent.class);
        channel.subscribe(this, AssessmentEndedEvent.class);
        mustPrecede(AssessmentStartedEvent.class, ContentAreaDerivedScoreEvent.class);
    }

    public void onEvent(AssessmentStartedEvent event) {
        super.onEvent(event);
        pNormGroup = null;
        invalidSubtestIds = new ArrayList<Integer>();
    }

    public void onEvent(ContentAreaDerivedScoreEvent event) {
    	if(!"L".equals(event.getTestLevel())) {
            final String message = "TASCCompositeScoreCalculator.onEvent(ContentAreaDerivedScoreEvent event)";
            Timer timer = Timer.startTimer();
        	contentAreaDerivedScoreEvents.put(event.getContentAreaName(), event);
        	if(event.getScaleScore() != null)
        		contentAreaScaleScore.put(event.getContentAreaName(), event.getScaleScore().intValue());
            Timer.logElapsed(message, timer);
        }
    }

    public void onEvent(ContentAreaRawScoreEvent event) {
    	if(contentAreaRawScoreEvents.containsKey(event.getContentAreaName())){
    		ContentAreaRawScoreEvent eventObj = (ContentAreaRawScoreEvent)contentAreaRawScoreEvents.get(event.getContentAreaName());
    		int pointObtained = eventObj.getPointsObtained() + event.getPointsObtained();
    		int pointAttempted = eventObj.getPointsAttempted() + event.getPointsAttempted();
    		int pointPossible = eventObj.getPointsPossible() + event.getPointsPossible();
    		contentAreaRawScoreEvents
					.put(event.getContentAreaName(),
							new ContentAreaRawScoreEvent(event
									.getTestRosterId(), event
									.getContentAreaName(), pointObtained,
									pointAttempted, ScorerHelper
											.calculatePercentage(pointObtained,
													event.getPointsPossible()),
									pointPossible, event
											.getContentAreaMap(), event
											.getItemSetId()));
    	}else{
            contentAreaRawScoreEvents.put(event.getContentAreaName(), event);
    	}
    }

    public void onEvent(SubtestInvalidEvent event) {
        invalidSubtestIds.add(event.getItemSetId());
    }

    public void onEvent(AssessmentEndedEvent event) {
        final Long testRosterId = event.getTestRosterId();
        
        calculateELACompositeRawScores();
        calculateOverallCompositeRawScores();
        
        calculateELAScaleScores();
        calculateOverallScaleScores();
        
        calculateTotalCompositeDerivedScores();
        
        AddInSubtestContentAreaCompositeAndDerivedScoreMap();
        
        publishScores(ELA_COMPOSITE_TYPE, testRosterId, ELACompositeScores);
        publishScores(OVERALL_COMPOSITE_TYPE, testRosterId, OverallCompositeScores);
        
        publishSubtestScores();
    }

    private void calculateTotalCompositeDerivedScores() {
        
    	if(contentAreaScaleScore.containsKey(ELA_CONTENT_AREA_NAME)){
 	       final BigDecimal ELAScaleScore = new BigDecimal(contentAreaScaleScore.get(ELA_CONTENT_AREA_NAME).toString());
 	       	ELACompositeScores.proficencyLevel = getTASCPerformanceLevel(TASC_FRAMEWORK_CODE, ELA_CONTENT_AREA_NAME,
 	       			pTestLevel, ELAScaleScore, (pGrade != null)?pGrade:null, pDupTestForm);
 	       	if(ELACompositeScores.proficencyLevel == null)
 	       		ELACompositeScores.proficencyLevel = new BigDecimal(0);
 	       	
 	       	ELACompositeScores.nationalPercentile = getTASCPR(TASC_FRAMEWORK_CODE, ELA_CONTENT_AREA_NAME,
 	    		   pTestLevel, ELAScaleScore, (pGrade != null)?pGrade:null, pDupTestForm);
 	       	if(ELACompositeScores.nationalPercentile == null)
 	       		ELACompositeScores.nationalPercentile = new BigDecimal(0);
 	       	
 	       	ELACompositeScores.normalCurveEquivalent = getTASCNCE(TASC_FRAMEWORK_CODE, ELA_CONTENT_AREA_NAME,
 	    		   pTestLevel, ELAScaleScore, (pGrade != null)?pGrade:null, pDupTestForm);
 	       	if(ELACompositeScores.normalCurveEquivalent == null)
 	       		ELACompositeScores.normalCurveEquivalent = new BigDecimal(0);
 	       	
 	       	/*ELACompositeScores.scaleScoreRange = getTASCScaleScoreRangeForCutScore(TASC_FRAMEWORK_CODE, ELA_CONTENT_AREA_NAME, 
 	    		   pTestLevel, null, (pGrade != null)?pGrade:null, pDupTestForm);*/
 	       	ELACompositeScores.scaleScoreRange = "";
 	    }
    	
    	if(contentAreaScaleScore.containsKey(OVERALL_CONTENT_AREA_NAME)){
  	       final BigDecimal overallScaleScore = new BigDecimal(contentAreaScaleScore.get(OVERALL_CONTENT_AREA_NAME).toString());
  	       	OverallCompositeScores.proficencyLevel = getTASCPerformanceLevel(TASC_FRAMEWORK_CODE, OVERALL_CONTENT_AREA_NAME,
 	       			pTestLevel, overallScaleScore, (pGrade != null)?pGrade:null, pDupTestForm);
  	       	if(OverallCompositeScores.proficencyLevel == null)
  	       		OverallCompositeScores.proficencyLevel = new BigDecimal(0);
  	       	
  	       	OverallCompositeScores.nationalPercentile = getTASCPR(TASC_FRAMEWORK_CODE, OVERALL_CONTENT_AREA_NAME,
  	    		   pTestLevel, overallScaleScore, (pGrade != null)?pGrade:null, pDupTestForm);
  	       	if(OverallCompositeScores.nationalPercentile == null)
  	       		OverallCompositeScores.nationalPercentile = new BigDecimal(0);
  	       	
  	       	OverallCompositeScores.normalCurveEquivalent = getTASCNCE(TASC_FRAMEWORK_CODE, OVERALL_CONTENT_AREA_NAME,
  	    		   pTestLevel, overallScaleScore, (pGrade != null)?pGrade:null, pDupTestForm);
  	       	if(OverallCompositeScores.normalCurveEquivalent == null)
  	       		OverallCompositeScores.normalCurveEquivalent = new BigDecimal(0);
  	       	
  	       	/*OverallCompositeScores.scaleScoreRange = getTASCScaleScoreRangeForCutScore(TASC_FRAMEWORK_CODE, OVERALL_CONTENT_AREA_NAME, 
	    		   pTestLevel, null, (pGrade != null)?pGrade:null, pDupTestForm);*/
  	       	
  	       	OverallCompositeScores.scaleScoreRange = "";
  	    }

    }

    private void calculateELACompositeRawScores() {
        if (ELACompositeScores.pointsPossible == 0) {
        	if(computeValidSubtestScoreFlag(READING_CONTENT_AREA_NAME) && computeValidSubtestScoreFlag(WRITING_CONTENT_AREA_NAME)){
        		ContentAreaRawScoreEvent reading = getContentAreaRawScore(READING_CONTENT_AREA_NAME);
                ContentAreaRawScoreEvent writing = getContentAreaRawScore(WRITING_CONTENT_AREA_NAME);
                ELACompositeScores.pointsAttempted = reading.getPointsAttempted() + writing.getPointsAttempted();
                ELACompositeScores.pointsObtained = reading.getPointsObtained() + writing.getPointsObtained();
                ELACompositeScores.pointsPossible = reading.getPointsPossible() + writing.getPointsPossible();
                
        	}
        }
    }

    private void calculateOverallCompositeRawScores() {
        
    	if (OverallCompositeScores.pointsPossible == 0  && validOverallCompositeScoreFlag()) {
            for (Iterator<ContentAreaRawScoreEvent> i = contentAreaRawScoreEvents.values().iterator(); i.hasNext();) {
                ContentAreaRawScoreEvent event = (ContentAreaRawScoreEvent) i.next();
                if (contentAreaNamesRequiredForOverallComposite.contains(event.getContentAreaName())) {
                	OverallCompositeScores.pointsAttempted += event.getPointsAttempted();
                	OverallCompositeScores.pointsObtained += event.getPointsObtained();
                	OverallCompositeScores.pointsPossible += event.getPointsPossible();
                	
                  }
              
            }
        }
    }
    
    private void calculateELAScaleScores() {
		Integer count = 0;
		if(validELACompositeScoreFlag()) {
			BigDecimal ELAScaleScore = BigDecimal.ZERO;
			if(contentAreaScaleScore.containsKey(READING_CONTENT_AREA_NAME)) {
				ELAScaleScore = ELAScaleScore.add( getContentAreaScaleScore(READING_CONTENT_AREA_NAME));
				count++;
			}
			if(contentAreaScaleScore.containsKey(WRITING_CONTENT_AREA_NAME)) {
				ELAScaleScore = ELAScaleScore.add( getContentAreaScaleScore(WRITING_CONTENT_AREA_NAME));
				count++;
			}
			if(count != 0 )  {
				ELAScaleScore = ELAScaleScore.divide(new BigDecimal(count.toString()),BigDecimal.ROUND_HALF_UP);
			}

			if(count == 0 ) 
				ELAScaleScore = null;

			if(ELAScaleScore != null) {
				contentAreaScaleScore.put(ELA_CONTENT_AREA_NAME, ELAScaleScore.intValue());
				ELACompositeScores.scaleScore = ELAScaleScore;
				ELACompositeScores.validScore = CTBConstants.VALID_SCORE;
			}
		}
		if(count == 0){
			ELACompositeScores.validScore = CTBConstants.INVALID_SCORE;
		}
	}
    
    private void calculateOverallScaleScores() {
      	 Integer count = 0;
      	 if(validOverallCompositeScoreFlag()) {
   	         BigDecimal OverallScaleScore = BigDecimal.ZERO;
   	         if (contentAreaScaleScore.containsKey(WRITING_CONTENT_AREA_NAME)){
   	        	 OverallScaleScore = OverallScaleScore.add(getContentAreaScaleScore(WRITING_CONTENT_AREA_NAME));
   	        	 count++;
   	      	  
   	         }
   	         if(contentAreaScaleScore.containsKey(READING_CONTENT_AREA_NAME)) {
   	        	 OverallScaleScore = OverallScaleScore.add(getContentAreaScaleScore(READING_CONTENT_AREA_NAME));
   	        	 count++;
   	      	  
   	         }
   	         if(contentAreaScaleScore.containsKey(MATH_CONTENT_AREA_NAME)) {
   	        	 OverallScaleScore = OverallScaleScore.add( getContentAreaScaleScore(MATH_CONTENT_AREA_NAME));
   	        	 count++;
   	      	   
   	         }
   	         if(contentAreaScaleScore.containsKey(SCIENCE_CONTENT_AREA_NAME)) {
   	        	 OverallScaleScore = OverallScaleScore.add( getContentAreaScaleScore(SCIENCE_CONTENT_AREA_NAME));
   	        	 count++;
   	      	  
   	         }
   	         if(contentAreaScaleScore.containsKey(SOCIAL_CONTENT_AREA_NAME)) {
   	        	 OverallScaleScore = OverallScaleScore.add( getContentAreaScaleScore(SOCIAL_CONTENT_AREA_NAME));
   	        	 count++;
     	       
     	     }
   	         if(count != 0 )  {
   	        	 OverallScaleScore = OverallScaleScore.divide(new BigDecimal(count.toString()),BigDecimal.ROUND_HALF_UP);
   	         }
   	         
   	         if(count == 0 ) 
   	        	 OverallScaleScore = null;
   	         
   	    	if(OverallScaleScore != null) {
   	    		contentAreaScaleScore.put(OVERALL_CONTENT_AREA_NAME, OverallScaleScore.intValue());
   	    		OverallCompositeScores.scaleScore = OverallScaleScore;
   	    		OverallCompositeScores.validScore = CTBConstants.VALID_SCORE;
   	    	}
      	 }
      	 if(count == 0){
      		 OverallCompositeScores.validScore = CTBConstants.INVALID_SCORE;
      	 }
      }
    
    
    private boolean validOverallCompositeScoreFlag() {
    	boolean validFlag = false;
    	Iterator<String> itr = contentAreaNamesRequiredForOverallComposite.iterator();
    		while(itr.hasNext()) {  
    			String contentArea = (String)itr.next(); 
    			validFlag = computeValidSubtestScoreFlag(contentArea);
    			if(!validFlag)
    				return false;
    		}
    		return validFlag;
    	
    }

    private boolean validELACompositeScoreFlag() {
    	boolean validFlag = false;
    	Iterator<String> itr = contentAreaNamesRequiredForELAComposite.iterator();
    		while(itr.hasNext()) {  
    			String contentArea = (String)itr.next(); 
    			validFlag = computeValidSubtestScoreFlag(contentArea);
    			if(!validFlag)
    				return false;
    		}
    		return validFlag;
    	
    }

    private void publishScores(final String scoreType, final Long testRosterId,
            final CompositeScoreHolder scores) {
    	
    	
    	if (ELA_COMPOSITE_TYPE.equals(scoreType) && validateELACompositeScore()){
	      channel.send(new SubtestContentAreaCompositeScoreEvent(testRosterId, scoreType,
	                    scores.scaleScore, scores.normalCurveEquivalent, null, null, 
	                    scores.nationalPercentile, pNormGroup, pNormYear, null, null, null,
	                    null, null,
	                    null, scores.getPointsObtained(), scores
	                            .getPointsAttempted(), scores.getPointsPossible(), scores
	                            .getPercentObtained(), null, scores.validScore, scores.proficencyLevel, scores.scaleScoreRange));
	      
    	}else if (OVERALL_COMPOSITE_TYPE.equals(scoreType) && validateOverallCompositeScore()){
    		channel.send(new SubtestContentAreaCompositeScoreEvent(testRosterId, scoreType,
                    scores.scaleScore, scores.normalCurveEquivalent, null, null, 
                    scores.nationalPercentile, pNormGroup, pNormYear, null, null, null,
                    null, null,
                    null, scores.getPointsObtained(), scores
                            .getPointsAttempted(), scores.getPointsPossible(), scores
                            .getPercentObtained(), null, scores.validScore, scores.proficencyLevel, scores.scaleScoreRange));
        }
    }
    
    private void publishSubtestScores() {
        if (!SubtestContentAreaCompositeAndDerivedScore.isEmpty() ) {
            channel.send(new SubtestContentAreaCompositeAndDerivedEvent(testRosterId, SubtestContentAreaCompositeAndDerivedScore));
        }
    }
	

    protected BigDecimal getScore(String contentArea, BigDecimal sourceScoreValue,
            ScoreLookupCode sourceScoreType, ScoreLookupCode destScoreType, String level, String grade) {
        final Connection conn = getOASConnection();
        try {
            return getScoreLookupHelper().getScore(TASC_FRAMEWORK_CODE, contentArea, null,
                    null, level, null, sourceScoreType, sourceScoreValue, destScoreType, conn,
                    pAgeCategory);
        } finally {
            closeConnection(conn);
        }
    }
    

    protected String getGradeEquivalent(String contentArea, BigDecimal sourceScoreValue,
            ScoreLookupCode sourceScoreType, ScoreLookupCode destScoreType, String level) {
        final Connection conn = getOASConnection();
        try {
            return getScoreLookupHelper().getGradeEquivalent(TASC_FRAMEWORK_CODE, contentArea,
                    null, null, level, null, sourceScoreType, sourceScoreValue,
                    destScoreType, conn, pAgeCategory);
        } finally {
            closeConnection(conn);
        }
    }


    private BigDecimal getContentAreaScaleScore(String contentAreaName) {
        ContentAreaDerivedScoreEvent scoreEvent = (ContentAreaDerivedScoreEvent) contentAreaDerivedScoreEvents
                .get(contentAreaName);
        return scoreEvent.getScaleScore();
    }


    private ContentAreaRawScoreEvent getContentAreaRawScore(String contentAreaName) {
        return (ContentAreaRawScoreEvent) contentAreaRawScoreEvents.get(contentAreaName);
    }



    private boolean validateELACompositeScore(){
    	
    	if (scorer.getResultHolder().getCurriculumData().getContentAreas().length > 0) {
    		int ELAContentAreaCount = 0;
    		
    		for(String contentAreaName : contentAreaNamesRequiredForELAComposite){
	    		for (int indx =0; indx < scorer.getResultHolder().getCurriculumData().getContentAreas().length; indx++) {
	    			ContentArea ca = scorer.getResultHolder().getCurriculumData().getContentAreas()[indx];
	    			if(ca.getContentAreaName().equals(contentAreaName.toString())){
	    				ELAContentAreaCount++;
	    				break;
	    			}
	    		}
    		}
			if(ELAContentAreaCount == 2){
				return true;
			}else{
				return false;
			}
    	}else{
    		return false;
    	}
    }
    
    private boolean validateOverallCompositeScore(){
    	if(scorer.getResultHolder().getCurriculumData().getContentAreas().length > 0){
    		int OverallContentAreaCount = 0;
    		
    		for(String contentAreaName : contentAreaNamesRequiredForOverallComposite){
	    		for (int indx =0; indx < scorer.getResultHolder().getCurriculumData().getContentAreas().length; indx++) {
	    			ContentArea ca = scorer.getResultHolder().getCurriculumData().getContentAreas()[indx];
	    			if(ca.getContentAreaName().equals(contentAreaName.toString())){
	    				OverallContentAreaCount++;
	    				break;
	    			}
	    		}
    		}
			if(OverallContentAreaCount == 5){
				return true;
			}else{
				return false;
			}
       	}else{
    		return false;
    	}
    }


    protected void AddInSubtestContentAreaCompositeAndDerivedScoreMap() {
    	for (final Iterator<Map.Entry<String,Integer>> it = contentAreaScaleScore.entrySet().iterator(); it.hasNext();) {
            final Map.Entry entry = (Map.Entry) it.next();
            final String contentArea = (String) entry.getKey();
            //final Integer scaleScore = (Integer) entry.getValue();
            
            SubtestContentAreaCompositeAndDerivedScore subtestContentAreaCompositeAndDerived =new SubtestContentAreaCompositeAndDerivedScore();
            if (SubtestContentAreaCompositeAndDerivedScore.containsKey(contentArea)) {
        		subtestContentAreaCompositeAndDerived = (SubtestContentAreaCompositeAndDerivedScore)
        				SubtestContentAreaCompositeAndDerivedScore.get(contentArea);
        	}
            
            if (contentAreaDerivedScoreEvents.containsKey(contentArea)) {
            	ContentAreaDerivedScoreEvent derivedScoreEvent = (ContentAreaDerivedScoreEvent) contentAreaDerivedScoreEvents.get(contentArea);
                subtestContentAreaCompositeAndDerived.setScaleScore(derivedScoreEvent.getScaleScore().intValue());
                subtestContentAreaCompositeAndDerived.setProficencyLevelCode(Integer.parseInt(derivedScoreEvent.getPerformanceLevel().getCode()));
                subtestContentAreaCompositeAndDerived.setProficencyLevelDescription(derivedScoreEvent.getPerformanceLevel().getDescription());
                subtestContentAreaCompositeAndDerived.setNationalPercentile(derivedScoreEvent.getNationalPercentile().intValue());
                subtestContentAreaCompositeAndDerived.setNormCurveEquivalent(derivedScoreEvent.getNormalCurveEquivalent().intValue());
            }
            
            if (contentAreaRawScoreEvents.containsKey(contentArea)) {
          	  	ContentAreaRawScoreEvent rawScoreEvent = (ContentAreaRawScoreEvent) contentAreaRawScoreEvents.get(contentArea);
          	  	subtestContentAreaCompositeAndDerived.setPointsAttempted(rawScoreEvent.getPointsAttempted());
          	  	subtestContentAreaCompositeAndDerived.setPointsObtained(rawScoreEvent.getPointsObtained());
          	  	subtestContentAreaCompositeAndDerived.setPointsPossible(rawScoreEvent.getPointsPossible());
          	  	subtestContentAreaCompositeAndDerived.setPercentObtained(getPercentObtainedForSubtest(
					  rawScoreEvent.getPointsObtained(), rawScoreEvent.getPointsPossible()));
          	  	subtestContentAreaCompositeAndDerived.setContentAreaName(contentArea);
          	  	
          	  	boolean flag = computeValidSubtestScoreFlag(contentArea);
          	  	if(flag)
          	  		subtestContentAreaCompositeAndDerived.setValidScore(CTBConstants.VALID_SCORE) ; 
          	  	else
          	  		subtestContentAreaCompositeAndDerived.setValidScore(CTBConstants.INVALID_SCORE) ;
            }          
        	SubtestContentAreaCompositeAndDerivedScore.put(contentArea, subtestContentAreaCompositeAndDerived); 
            
    	}
    }
    
    Float getPercentObtainedForSubtest( int pointsObtained, int pointsPossible) {
        if (pointsPossible == 0)
            return null;

        int p = (int)Math.pow(10, 1);
        pointsObtained = pointsObtained * p;
        float percent = Math.round(((float) pointsObtained / (float) pointsPossible) * 100 );
        return percent/p;
    }
    
    private boolean computeValidSubtestScoreFlag(final String contentAreaName) {
        if (contentAreaRawScoreEvents.containsKey(contentAreaName)) {
      	  ContentAreaRawScoreEvent  contentAreaRawScore	= getContentAreaRawScore(contentAreaName);
              Integer subtestId = contentAreaRawScore.getItemSetId();
              if (invalidSubtestIds.contains(subtestId)) {
                  return false;
              }
          } else {
          	return false;
          }
      return true;
  }
    
    private class CompositeScoreHolder {
        BigDecimal scaleScore;
        BigDecimal proficencyLevel;
        BigDecimal normalCurveEquivalent;
        BigDecimal nationalPercentile;
        int pointsObtained;
        int pointsAttempted;
        int pointsPossible;
        String validScore;
        String scaleScoreRange;

        CompositeScoreHolder() {
            scaleScore = null;
            proficencyLevel = null;
            normalCurveEquivalent = null;
            nationalPercentile = null;
            pointsAttempted = 0;
            pointsObtained = 0;
            pointsPossible = 0;
            validScore = null;
            scaleScoreRange = null;
        }

        Long toLong(int score) {
            if (pointsPossible == 0)
                return null;
            return new Long(score);
        }

        Long getPointsAttempted() {
            return toLong(pointsAttempted);
        }

        Long getPointsObtained() {
            return toLong(pointsObtained);
        }

        Long getPointsPossible() {
            return toLong(pointsPossible);
        }

        Long getPercentObtained() {
            if (pointsPossible == 0)
                return null;

            float percent = ((float) pointsObtained / (float) pointsPossible) * 100;
            return new Long(Math.round(percent));
        }
    }
}