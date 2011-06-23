package com.ctb.lexington.domain.score.scorer.calculator;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ctb.lexington.db.data.SubtestContentAreaCompositeAndDerivedScore;
import com.ctb.lexington.db.utils.DatabaseHelper;
import com.ctb.lexington.domain.score.event.AssessmentEndedEvent;
import com.ctb.lexington.domain.score.event.AssessmentStartedEvent;
import com.ctb.lexington.domain.score.event.ContentAreaDerivedScoreEvent;
import com.ctb.lexington.domain.score.event.ContentAreaRawScoreEvent;
import com.ctb.lexington.domain.score.event.SubtestContentAreaCompositeAndDerivedEvent;
import com.ctb.lexington.domain.score.event.SubtestContentAreaCompositeScoreEvent;
import com.ctb.lexington.domain.score.event.SubtestInvalidEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.domain.score.scorer.calculator.ContentAreaNumberCorrectCalculator.ContentAreaAnswers;
import com.ctb.lexington.domain.teststructure.PerformanceLevel;
import com.ctb.lexington.domain.teststructure.ScoreLookupCode;
import com.ctb.lexington.util.CTBConstants;
import com.ctb.lexington.util.SafeHashMap;
import com.ctb.lexington.util.Timer;

/**
 * @author TCS
 * Calculates composite scores for LASLINK assessments. Publishes at most two events. Both are
 * instances SubtestContentAreaCompositeScoreEvent. The first event contains the total mathematics
 * scores, and the second contains the total battery scores. Each event requires scores for specific
 * content areas.
 * 
 * @see com.ctb.lexington.domain.score.event.SubtestContentAreaCompositeScoreEvent
 */
public class LasLinkCompositeScoreCalculator extends AbstractDerivedScoreCalculator {
    
    
    
    public final static String ORAL_CONTENT_AREA_NAME = 			"Oral";
    public final static String OVERALL_CONTENT_AREA_NAME = 			"Overall";
    public final static String WRITING_CONTENT_AREA_NAME = 			"Writing";
    public final static String READING_CONTENT_AREA_NAME = 			"Reading";
    public final static String SPEAKING_CONTENT_AREA_NAME = 		"Speaking";
    public final static String LISTENING_CONTENT_AREA_NAME =		"Listening";
    public final static String COMPREHENSIVE_CONTENT_AREA_NAME = 	"Comprehension";
    
    public static String ORAL_SPEAKING_CONTENT_AREA_NAME = "OralSpeaking";
    public static String ORAL_LISTENING_CONTENT_AREA_NAME = "OralListening";
    
    public static String COMPREHENSIVE_READING_CONTENT_AREA_NAME = "ComprehensionReading";
    public static String COMPREHENSIVE_LISTENING_CONTENT_AREA_NAME = "ComprehensionListening";
    public final static String LASLINK_OVERALL_SCORE = 	 "Overall Score";
   
    
    private static final String LASLINK_FRAMEWORK_CODE = "LLEAB";

    

    private final Map contentAreaDerivedScoreEvents = new SafeHashMap(String.class,
            ContentAreaDerivedScoreEvent.class);
    private final Map contentAreaRawScoreEvents = new SafeHashMap(String.class,
            ContentAreaRawScoreEvent.class);
    private final Map contentAreaSubtestId = new SafeHashMap(String.class,String.class);
    private final Map contentAreaScaleScore = new SafeHashMap(String.class,Integer.class);
    private final Map SubtestContentAreaCompositeAndDerivedScore = new SafeHashMap(String.class,SubtestContentAreaCompositeAndDerivedScore.class);
    
    private final ArrayList contentAreaNamesRequiredForTotalComposite = new ArrayList();
    private final ArrayList contentAreaNamesRequiredForOverallComposite = new ArrayList();
    
    private final ArrayList contentAreaNamesRequiredForOralComposite = new ArrayList();
    private final ArrayList contentAreaNamesRequiredForComprehensionComposite = new ArrayList();
    
    private boolean totalCompositeDerivedScoresCalculated;
    private boolean overallCompositeDerivedScoresCalculated;
    private boolean oralCompositeDerivedScoresCalculated;
    private boolean comprehensionCompositeDerivedScoresCalculated;

    private final CompositeScoreHolder overallCompositeScores;
    private final CompositeScoreHolder oralCompositeScores;
    private final CompositeScoreHolder comprehensionCompositeScores;
    private final CompositeScoreHolder totalCompositeScores;
    
    
    
    private List invalidSubtestIds;

    public LasLinkCompositeScoreCalculator(Channel channel, Scorer scorer) {
        super(channel, scorer);

        
        contentAreaNamesRequiredForTotalComposite.add(WRITING_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForTotalComposite.add(READING_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForTotalComposite.add(SPEAKING_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForTotalComposite.add(LISTENING_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForTotalComposite.add(ORAL_LISTENING_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForTotalComposite.add(ORAL_SPEAKING_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForTotalComposite.add(COMPREHENSIVE_READING_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForTotalComposite.add(COMPREHENSIVE_LISTENING_CONTENT_AREA_NAME);

        
        contentAreaNamesRequiredForOverallComposite.add(WRITING_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForOverallComposite.add(READING_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForOverallComposite.add(SPEAKING_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForOverallComposite.add(LISTENING_CONTENT_AREA_NAME);

        
        totalCompositeDerivedScoresCalculated = false;
        totalCompositeScores = new CompositeScoreHolder();

        overallCompositeDerivedScoresCalculated  = false;
        overallCompositeScores = new CompositeScoreHolder();
        
        oralCompositeDerivedScoresCalculated = false;
        oralCompositeScores = new CompositeScoreHolder();
        
        comprehensionCompositeDerivedScoresCalculated = false;
        comprehensionCompositeScores = new  CompositeScoreHolder();
        
        channel.subscribe(this, ContentAreaDerivedScoreEvent.class);
        channel.subscribe(this, ContentAreaRawScoreEvent.class);
        channel.subscribe(this, SubtestInvalidEvent.class);
        channel.subscribe(this, AssessmentEndedEvent.class);
        mustPrecede(AssessmentStartedEvent.class, ContentAreaDerivedScoreEvent.class);
    }

    public void onEvent(AssessmentStartedEvent event) {
        super.onEvent(event);
        pNormGroup = null;
        invalidSubtestIds = new ArrayList();
    }

    public void onEvent(ContentAreaDerivedScoreEvent event) {
        if(!"L".equals(event.getTestLevel())) {
            final String message = "LasLinkCompositeScoreCalculator.onEvent(ContentAreaDerivedScoreEvent event)";
            Timer timer = Timer.startTimer();
            if(event.getContentAreaName().equals(ORAL_CONTENT_AREA_NAME) || event.getContentAreaName().equals(COMPREHENSIVE_CONTENT_AREA_NAME)) {
            	contentAreaDerivedScoreEvents.put(contentAreaSubtestId.get(event.getSubtestId().toString()+event.getContentAreaName()), event);
            } else {
            	contentAreaDerivedScoreEvents.put(event.getContentAreaName(), event);
            	if(event.getScaleScore() != null)
            		contentAreaScaleScore.put(event.getContentAreaName(), event.getScaleScore().intValue());
            }
            Timer.logElapsed(message, timer);
        }
    }

    public void onEvent(ContentAreaRawScoreEvent event) {
        // hacked to ignore auto-locator subtests
        if(event.getPointsPossible() > 12) {
        	if(event.getContentAreaName().equals(ORAL_CONTENT_AREA_NAME)) {
        		if( event.getContentAreaMap().containsKey("Speaking")) {
        			contentAreaRawScoreEvents.put(event.getContentAreaName()+ "Speaking" , event);
        			contentAreaSubtestId.put(((Integer)event.getItemSetId()).toString()+event.getContentAreaName(),event.getContentAreaName()+ "Speaking");
        		}
        		if( event.getContentAreaMap().containsKey("Listening")) {
        			contentAreaRawScoreEvents.put(event.getContentAreaName()+ "Listening" , event);
        			contentAreaSubtestId.put(((Integer)event.getItemSetId()).toString()+event.getContentAreaName(),event.getContentAreaName()+ "Listening");
        		}
        	}
        	else if(event.getContentAreaName().equals(COMPREHENSIVE_CONTENT_AREA_NAME)) {
        		if( event.getContentAreaMap().containsKey("Listening")) {
        			contentAreaRawScoreEvents.put(event.getContentAreaName()+ "Listening" , event);
        			contentAreaSubtestId.put(((Integer)event.getItemSetId()).toString()+event.getContentAreaName(),event.getContentAreaName()+ "Listening");
        		}
        		if( event.getContentAreaMap().containsKey("Reading")) {
        			contentAreaRawScoreEvents.put(event.getContentAreaName()+ "Reading" , event);
        			contentAreaSubtestId.put(((Integer)event.getItemSetId()).toString()+event.getContentAreaName(),event.getContentAreaName()+ "Reading");
        		}
        		
        	}
        	else {
        		contentAreaRawScoreEvents.put(event.getContentAreaName(), event);
        	}
        }
    }

    public void onEvent(SubtestInvalidEvent event) {
        invalidSubtestIds.add(event.getItemSetId());
    }

    public void onEvent(AssessmentEndedEvent event) {
    	 if("K".equals(pTestLevel) || "1".equals(pTestLevel)) {
             pTestLevel = "1";
         }
         if( "2-3".equals(pTestLevel) ){  pTestLevel = "2"; }
         if( "4-5".equals(pTestLevel) ){  pTestLevel = "3"; }
         if( "6-8".equals(pTestLevel) ){  pTestLevel = "4"; }
         if( "9-12".equals(pTestLevel)){  pTestLevel = "5"; }
        
        if ("A".equals(pDupTestForm) || "B".equals(pDupTestForm)) { 
        	 pTestForm = pDupTestForm;
         }
        if ("Espa?ol".equals(pDupTestForm) || "Espanol".equals(pDupTestForm) || "Español".equals(pDupTestForm)) {
        	pTestForm  = "S";
        }
        	 
         
        final Long testRosterId = event.getTestRosterId();
         calculateOralCompositeRawScores(); //pointObtained
         calculateComprehensionCompositeRawScores(); //pointObtained
         calculateOverallCompositeRawScores();
         calculateTotalOralScaleScores();
         calculateTotalComprehensionScaleScores();
         calculateTotalOverallScaleScores();
         calculateTotalCompositeDerivedScores();
         AddInSubtestContentAreaCompositeAndDerivedScoreMap();
        publishScores(LASLINK_OVERALL_SCORE, testRosterId, overallCompositeScores);
        publishSubtestScores();
      
    }

   

    private void calculateTotalCompositeDerivedScores() {
       //totalCompositeScores.validScore = computeValidScoreFlag(contentAreaNamesRequiredForTotalComposite);
       if(contentAreaScaleScore.containsKey(OVERALL_CONTENT_AREA_NAME)){
       final BigDecimal OverallScaleScore = getContentAreaScaleScore(OVERALL_CONTENT_AREA_NAME);
       	overallCompositeScores.proficencyLevel = getScore(OVERALL_CONTENT_AREA_NAME,
    		   OverallScaleScore, ScoreLookupCode.SCALED_SCORE,
               ScoreLookupCode.PERFORMANCE_LEVEL, pTestLevel, pGrade);
       	if(overallCompositeScores.proficencyLevel == null)
       		overallCompositeScores.proficencyLevel = new BigDecimal(1);
      }
       	
       if ( contentAreaScaleScore.containsKey(ORAL_CONTENT_AREA_NAME)) {
            final BigDecimal oralScaleScore = getContentAreaScaleScore(ORAL_CONTENT_AREA_NAME);
            oralCompositeScores.proficencyLevel = getScore(ORAL_CONTENT_AREA_NAME,
            		oralScaleScore, ScoreLookupCode.SCALED_SCORE,
                    ScoreLookupCode.PERFORMANCE_LEVEL, pTestLevel, pGrade);
        	if(oralCompositeScores.proficencyLevel == null)
        		oralCompositeScores.proficencyLevel = new BigDecimal(1);
        }

       if ( contentAreaScaleScore.containsKey(COMPREHENSIVE_CONTENT_AREA_NAME)) {
        	final BigDecimal comprehensionScaleScore = getContentAreaScaleScore(COMPREHENSIVE_CONTENT_AREA_NAME);
        	comprehensionCompositeScores.proficencyLevel = getScore(COMPREHENSIVE_CONTENT_AREA_NAME,
        			comprehensionScaleScore,ScoreLookupCode.SCALED_SCORE, 
        			ScoreLookupCode.PERFORMANCE_LEVEL, pTestLevel, pGrade);
        	if(comprehensionCompositeScores.proficencyLevel == null)
        		comprehensionCompositeScores.proficencyLevel = new BigDecimal(1);
        }
    }

   
    
    private void calculateOralCompositeRawScores() {
        if (oralCompositeScores.pointsPossible == 0) {
        	if (computeValidSubtestScoreFlag(ORAL_LISTENING_CONTENT_AREA_NAME) && computeValidSubtestScoreFlag(ORAL_SPEAKING_CONTENT_AREA_NAME)){
	        	ContentAreaRawScoreEvent oralListening = getContentAreaRawScore(ORAL_LISTENING_CONTENT_AREA_NAME);
	            oralCompositeScores.pointsAttempted += oralListening.getPointsAttempted();
	            oralCompositeScores.pointsObtained += oralListening.getPointsObtained();
	            oralCompositeScores.pointsPossible += oralListening.getPointsPossible();
	            
	            ContentAreaRawScoreEvent oralSpeaking = getContentAreaRawScore(ORAL_SPEAKING_CONTENT_AREA_NAME);
	            oralCompositeScores.pointsAttempted += oralSpeaking.getPointsAttempted();
	            oralCompositeScores.pointsObtained += oralSpeaking.getPointsObtained();
	            oralCompositeScores.pointsPossible += oralSpeaking.getPointsPossible();
	            oralCompositeScores.validScore = CTBConstants.VALID_SCORE;
        	}
        }
    }
    
    private void calculateComprehensionCompositeRawScores() {
        if (comprehensionCompositeScores.pointsPossible == 0) {
            if (computeValidSubtestScoreFlag(COMPREHENSIVE_LISTENING_CONTENT_AREA_NAME)&& computeValidSubtestScoreFlag(COMPREHENSIVE_READING_CONTENT_AREA_NAME)){
	        	ContentAreaRawScoreEvent comprehensionListening = getContentAreaRawScore(COMPREHENSIVE_LISTENING_CONTENT_AREA_NAME);
	        	comprehensionCompositeScores.pointsAttempted += comprehensionListening.getPointsAttempted();
	        	comprehensionCompositeScores.pointsObtained += comprehensionListening.getPointsObtained();
	        	comprehensionCompositeScores.pointsPossible += comprehensionListening.getPointsPossible();
	        	
	            ContentAreaRawScoreEvent comprehensionReading = getContentAreaRawScore(COMPREHENSIVE_READING_CONTENT_AREA_NAME);
	            comprehensionCompositeScores.pointsAttempted += comprehensionReading.getPointsAttempted();
	            comprehensionCompositeScores.pointsObtained += comprehensionReading.getPointsObtained();
	            comprehensionCompositeScores.pointsPossible += comprehensionReading.getPointsPossible();
	            comprehensionCompositeScores.validScore = CTBConstants.VALID_SCORE;
        	}
            
        }
    }
    
    private void calculateTotalOralScaleScores() {
    	final BigDecimal oralScaleScore; 
    	if(oralCompositeScores.validScore.equals(CTBConstants.VALID_SCORE)) {
	    	oralScaleScore = getScore(ORAL_CONTENT_AREA_NAME,
	    			 new BigDecimal(oralCompositeScores.pointsObtained), ScoreLookupCode.SUBTEST_NUMBER_CORRECT,
	                 ScoreLookupCode.SCALED_SCORE, pTestLevel, null);
	    	if(oralScaleScore != null) {
	    		oralCompositeScores.scaleScore = oralScaleScore;
	    		contentAreaScaleScore.put(ORAL_CONTENT_AREA_NAME, oralScaleScore.intValue());
	    	} 
    	} else {
    		contentAreaScaleScore.put(ORAL_CONTENT_AREA_NAME, new Integer (0));
    	}
    	
    }
    
   
    
    private void calculateTotalComprehensionScaleScores() {
    	 final BigDecimal ComprehensionScaleScore;
    	 if(comprehensionCompositeScores.validScore.equals(CTBConstants.VALID_SCORE)) {
	    	 ComprehensionScaleScore = getScore(COMPREHENSIVE_CONTENT_AREA_NAME,
	    			  new BigDecimal(comprehensionCompositeScores.pointsObtained), ScoreLookupCode.SUBTEST_NUMBER_CORRECT,
	                 ScoreLookupCode.SCALED_SCORE, pTestLevel, null);
	    	 if(ComprehensionScaleScore != null) {
	    		 comprehensionCompositeScores.scaleScore = ComprehensionScaleScore; 
	    		 contentAreaScaleScore.put(COMPREHENSIVE_CONTENT_AREA_NAME, ComprehensionScaleScore.intValue());
	    	 }
    	 } else {
	    	 contentAreaScaleScore.put(COMPREHENSIVE_CONTENT_AREA_NAME, new Integer (0));
	    	}
    } 
    
    
       
    private void calculateOverallCompositeRawScores() {
        if (overallCompositeScores.pointsAttempted == 0  && validOverallCompositeScoreFlag()) {
            for (Iterator i = contentAreaRawScoreEvents.values().iterator(); i.hasNext();) {
                ContentAreaRawScoreEvent event = (ContentAreaRawScoreEvent) i.next();
                if (contentAreaNamesRequiredForOverallComposite.contains(event.getContentAreaName())) {
                	overallCompositeScores.pointsAttempted += event.getPointsAttempted();
                	overallCompositeScores.pointsObtained += event.getPointsObtained();
                	overallCompositeScores.pointsPossible += event.getPointsPossible();
                  }
              
            }
        }
    }
    
    private boolean validOverallCompositeScoreFlag() {
    	boolean validFlag = false;
    	Iterator itr = contentAreaNamesRequiredForOverallComposite.iterator();
    		while(itr.hasNext()) {  
    			String contentArea = (String)itr.next(); 
    			validFlag = computeValidSubtestScoreFlag(contentArea);
    			if(!validFlag)
    				return false;
    		}
    		return validFlag;
    	
    }
    private void calculateTotalOverallScaleScores() {
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
	         if(contentAreaScaleScore.containsKey(SPEAKING_CONTENT_AREA_NAME)) {
	        	 OverallScaleScore = OverallScaleScore.add( getContentAreaScaleScore(SPEAKING_CONTENT_AREA_NAME));
	      	   count++;
	      	   
	         }
	         if(contentAreaScaleScore.containsKey(LISTENING_CONTENT_AREA_NAME)) {
	        	 OverallScaleScore = OverallScaleScore.add( getContentAreaScaleScore(LISTENING_CONTENT_AREA_NAME));
	      	   count++;
	      	  
	         }
	         if(count != 0 )  {
	        	 OverallScaleScore = OverallScaleScore.divide(new BigDecimal(count.toString()),BigDecimal.ROUND_HALF_UP);
	         }
	         
	         if(count == 0 ) 
	        	 OverallScaleScore = null;
	         
	    	if(OverallScaleScore != null) {
	    		contentAreaScaleScore.put(OVERALL_CONTENT_AREA_NAME, OverallScaleScore.intValue());
	    		overallCompositeScores.scaleScore = OverallScaleScore;
	    		overallCompositeScores.validScore = CTBConstants.VALID_SCORE;
	    	}
    	 }
    	 if(count == 0){
    		 overallCompositeScores.validScore = CTBConstants.INVALID_SCORE;
    	 }
    }
    
   

    private String computeValidScoreFlag(final List contentAreaNames) {
        for (Iterator iter = contentAreaNames.iterator(); iter.hasNext();) {
            String contentAreaName = (String) iter.next();
            if (contentAreaDerivedScoreEvents.containsKey(contentAreaName)) {
                ContentAreaDerivedScoreEvent derivedScoreEvent = (ContentAreaDerivedScoreEvent) contentAreaDerivedScoreEvents.get(contentAreaName);
                Integer subtestId = DatabaseHelper.asInteger(derivedScoreEvent.getSubtestId());
                if (invalidSubtestIds.contains(subtestId)) {
                    return CTBConstants.INVALID_SCORE;
                }
            } else {
            	return CTBConstants.INVALID_SCORE;
            }
        }
        return CTBConstants.VALID_SCORE;
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
    
    private void publishScores(final String scoreType, final Long testRosterId,
            final CompositeScoreHolder scores) {
       // if (null != scores.scaleScore || 0 != scores.pointsPossible  ) {
            channel.send(new SubtestContentAreaCompositeScoreEvent(testRosterId, scoreType,
                    scores.scaleScore, null, null, null, null, pNormGroup, pNormYear,
                    null, null, null, null, null,null, scores.getPointsObtained(), scores
                            .getPointsAttempted(), scores.getPointsPossible(), null, scores
                            .getPercentObtained(), scores.validScore, scores.proficencyLevel));
         
       // }
    }

     
    private void publishSubtestScores() {
        if (!SubtestContentAreaCompositeAndDerivedScore.isEmpty() ) {
            channel.send(new SubtestContentAreaCompositeAndDerivedEvent(testRosterId, SubtestContentAreaCompositeAndDerivedScore));
        }
    }
    protected BigDecimal getScore(String contentArea, BigDecimal sourceScoreValue,
            ScoreLookupCode sourceScoreType, ScoreLookupCode destScoreType, String level,String grade) {
        final Connection conn = getOASConnection();
        try {
            return getScoreLookupHelper().getScore(LASLINK_FRAMEWORK_CODE, contentArea, null,
                    pTestForm, level, grade, sourceScoreType, sourceScoreValue, destScoreType, conn,
                    null);
        } finally {
            closeConnection(conn);
        }
    }

    protected String getGradeEquivalent(String contentArea, BigDecimal sourceScoreValue,
            ScoreLookupCode sourceScoreType, ScoreLookupCode destScoreType, String level) {
        final Connection conn = getOASConnection();
        try {
            return getScoreLookupHelper().getGradeEquivalent(LASLINK_FRAMEWORK_CODE, contentArea,
                    null, null, level, null, sourceScoreType, sourceScoreValue,
                    destScoreType, conn, pAgeCategory);
        } finally {
            closeConnection(conn);
        }
    }
    
    protected void AddInSubtestContentAreaCompositeAndDerivedScoreMap() {
    	  for (final Iterator it = contentAreaScaleScore.entrySet().iterator(); it.hasNext();) {
              final Map.Entry entry = (Map.Entry) it.next();
              final String contentArea = (String) entry.getKey();
              final Integer scaleScore = (Integer) entry.getValue();
              SubtestContentAreaCompositeAndDerivedScore subtestContentAreaCompositeAndDerived =new SubtestContentAreaCompositeAndDerivedScore();
              if (SubtestContentAreaCompositeAndDerivedScore.containsKey(contentArea)) {
          		subtestContentAreaCompositeAndDerived = (SubtestContentAreaCompositeAndDerivedScore)
          				SubtestContentAreaCompositeAndDerivedScore.get(contentArea);
          	}
              if(contentArea.equals(ORAL_CONTENT_AREA_NAME) && oralCompositeScores.validScore.equals(CTBConstants.VALID_SCORE)){
            	  subtestContentAreaCompositeAndDerived.setScaleScore(scaleScore);
                  subtestContentAreaCompositeAndDerived.setProficencyLevelCode(oralCompositeScores.proficencyLevel.intValue());
                  subtestContentAreaCompositeAndDerived.setProficencyLevelDescription(
                		  PerformanceLevel.getByCode(String.valueOf(oralCompositeScores.proficencyLevel)).getDescription());
                  subtestContentAreaCompositeAndDerived.setPointsAttempted(oralCompositeScores.pointsAttempted);
              	  subtestContentAreaCompositeAndDerived.setPointsObtained(oralCompositeScores.pointsObtained);
              	  subtestContentAreaCompositeAndDerived.setPointsPossible(oralCompositeScores.pointsPossible);
              	  subtestContentAreaCompositeAndDerived.setPercentObtained(oralCompositeScores.getPercentObtained());
              	  subtestContentAreaCompositeAndDerived.setContentAreaName(contentArea);
              	subtestContentAreaCompositeAndDerived.setValidScore(oralCompositeScores.validScore) ; 
              	
              } else if(contentArea.equals(ORAL_CONTENT_AREA_NAME)&& oralCompositeScores.validScore.equals(CTBConstants.INVALID_SCORE)){
            	  subtestContentAreaCompositeAndDerived.setContentAreaName(contentArea);
            	  subtestContentAreaCompositeAndDerived.setValidScore(oralCompositeScores.validScore) ; 
              }
              
              if(contentArea.equals(COMPREHENSIVE_CONTENT_AREA_NAME) && comprehensionCompositeScores.validScore.equals(CTBConstants.VALID_SCORE)){
            	  subtestContentAreaCompositeAndDerived.setScaleScore(scaleScore);
                  subtestContentAreaCompositeAndDerived.setProficencyLevelCode(comprehensionCompositeScores.proficencyLevel.intValue());
                  subtestContentAreaCompositeAndDerived.setProficencyLevelDescription(
                		  PerformanceLevel.getByCode(String.valueOf(comprehensionCompositeScores.proficencyLevel)).getDescription());
                  subtestContentAreaCompositeAndDerived.setPointsAttempted(comprehensionCompositeScores.pointsAttempted);
              	  subtestContentAreaCompositeAndDerived.setPointsObtained(comprehensionCompositeScores.pointsObtained);
              	  subtestContentAreaCompositeAndDerived.setPointsPossible(comprehensionCompositeScores.pointsPossible);
              	  subtestContentAreaCompositeAndDerived.setPercentObtained(comprehensionCompositeScores.getPercentObtained());
            	  subtestContentAreaCompositeAndDerived.setContentAreaName(contentArea);
            	  subtestContentAreaCompositeAndDerived.setValidScore(comprehensionCompositeScores.validScore) ; 
            	  
              } else if (contentArea.equals(COMPREHENSIVE_CONTENT_AREA_NAME) && comprehensionCompositeScores.validScore.equals(CTBConstants.INVALID_SCORE)){
            	  subtestContentAreaCompositeAndDerived.setContentAreaName(contentArea);
            	  subtestContentAreaCompositeAndDerived.setValidScore(comprehensionCompositeScores.validScore) ; 
               }
              
              if (contentAreaDerivedScoreEvents.containsKey(contentArea)) {
                  ContentAreaDerivedScoreEvent derivedScoreEvent = (ContentAreaDerivedScoreEvent) contentAreaDerivedScoreEvents.get(contentArea);
                  subtestContentAreaCompositeAndDerived.setScaleScore(derivedScoreEvent.getScaleScore().intValue());
                  subtestContentAreaCompositeAndDerived.setProficencyLevelCode(Integer.parseInt(derivedScoreEvent.getPerformanceLevel().getCode()));
                  subtestContentAreaCompositeAndDerived.setProficencyLevelDescription(derivedScoreEvent.getPerformanceLevel().getDescription());
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

    


    private BigDecimal getContentAreaScaleScore(String contentAreaName) {
    	return new BigDecimal(contentAreaScaleScore.get(contentAreaName).toString());
    }

    private String getContentAreaLevel(String contentAreaName) {
        ContentAreaDerivedScoreEvent scoreEvent = (ContentAreaDerivedScoreEvent) contentAreaDerivedScoreEvents
                .get(contentAreaName);
        return scoreEvent.getTestLevel();
    }
    
    private ContentAreaRawScoreEvent getContentAreaRawScore(String contentAreaName) {
        return (ContentAreaRawScoreEvent) contentAreaRawScoreEvents.get(contentAreaName);
    }
    
    private ContentAreaDerivedScoreEvent getContentAreaNumberCorrectRawScore(String contentAreaName) {
        return (ContentAreaDerivedScoreEvent) contentAreaDerivedScoreEvents.get(contentAreaName);
    }

    

    private boolean isContentAreaScored(String contentAreaName) {
        return contentAreaDerivedScoreEvents.keySet().contains(contentAreaName);
    }
    
    	Long getPercentObtainedForSubtest( int pointsObtained, int pointsPossible) {
        if (pointsPossible == 0)
            return null;

        float percent = ((float) pointsObtained / (float) pointsPossible) * 100;
        return new Long(Math.round(percent));
    	}


    private class CompositeScoreHolder {
        BigDecimal scaleScore;
        BigDecimal proficencyLevel;
        BigDecimal normalCurveEquivalent;
        String gradeEquivalent;
        BigDecimal nationalStanine;
        BigDecimal nationalPercentile;
        int pointsObtained;
        int pointsAttempted;
        int pointsPossible;
        String validScore;
        BigDecimal numberCorrect;

        CompositeScoreHolder() {
            scaleScore = null;
            proficencyLevel = null;
            normalCurveEquivalent = null;
            gradeEquivalent = null;
            nationalStanine = null;
            nationalPercentile = null;
            pointsAttempted = 0;
            pointsObtained = 0;
            pointsPossible = 0;
            validScore = CTBConstants.INVALID_SCORE;
            numberCorrect = null;
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

        Float getPercentObtained() {
            if (pointsPossible == 0)
                return null;
            
            int p = (int)Math.pow(10, 1);
            pointsObtained = pointsObtained * p;
            float tmp = Math.round(((float) pointsObtained / (float) pointsPossible) * 100 );
            return tmp/p;
        }

       
    }
}