package com.ctb.lexington.domain.score.scorer.calculator;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ctb.lexington.db.data.SubtestContentAreaCompositeAndDerivedScore;
import com.ctb.lexington.domain.score.event.AssessmentEndedEvent;
import com.ctb.lexington.domain.score.event.AssessmentStartedEvent;
import com.ctb.lexington.domain.score.event.ContentAreaDerivedScoreEvent;
import com.ctb.lexington.domain.score.event.ContentAreaRawScoreEvent;
import com.ctb.lexington.domain.score.event.SubtestContentAreaCompositeAndDerivedEvent;
import com.ctb.lexington.domain.score.event.SubtestContentAreaCompositeScoreEvent;
import com.ctb.lexington.domain.score.event.SubtestInvalidEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.domain.teststructure.PerformanceLevel;
import com.ctb.lexington.domain.teststructure.ScoreLookupCode;
import com.ctb.lexington.util.CTBConstants;
import com.ctb.lexington.util.SafeHashMap;
import com.ctb.lexington.util.Timer;

/**
 * @author TCS
 * Calculates composite scores for LASLINK second edition assessments.
 */


public class LasLinkCompositeScoreCalculatorSecondEdition extends AbstractDerivedScoreCalculator {
	
	public final static String ORAL_CONTENT_AREA_NAME = 			"Oral";
    public final static String OVERALL_CONTENT_AREA_NAME = 			"Overall";
    public final static String WRITING_CONTENT_AREA_NAME = 			"Writing";
    public final static String READING_CONTENT_AREA_NAME = 			"Reading";
    public final static String SPEAKING_CONTENT_AREA_NAME = 		"Speaking";
    public final static String LISTENING_CONTENT_AREA_NAME =		"Listening";
    public final static String COMPREHENSIVE_CONTENT_AREA_NAME = 	"Comprehension";
    public final static String PRODUCTIVE_CONTENT_AREA_NAME = 		"Productive";
    public final static String LITERACY_CONTENT_AREA_NAME = 		"Literacy";
    
    public static String ORAL_SPEAKING_CONTENT_AREA_NAME = "OralSpeaking";
    public static String ORAL_LISTENING_CONTENT_AREA_NAME = "OralListening";
    public static String COMPREHENSIVE_READING_CONTENT_AREA_NAME = "ComprehensionReading";
    public static String COMPREHENSIVE_LISTENING_CONTENT_AREA_NAME = "ComprehensionListening";
    public static String PRODUCTIVE_SPEAKING_CONTENT_AREA_NAME = "ProductiveSpeaking";
    public static String PRODUCTIVE_WRITING_CONTENT_AREA_NAME = "ProductiveWriting";
    public static String LITERACY_READING_CONTENT_AREA_NAME = "LiteracyReading";
    public static String LITERACY_WRITING_CONTENT_AREA_NAME = "LiteracyWriting";
    
    public final static String LASLINK_OVERALL_SCORE = 	 "Overall Score";
    
    private static final String LASLINK_FRAMEWORK_CODE = "LL2ND";
    
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
    private final ArrayList contentAreaNamesRequiredForProductiveComposite = new ArrayList();
    private final ArrayList contentAreaNamesRequiredForLiteracyComposite = new ArrayList();

    private final CompositeScoreHolder overallCompositeScores;
    private final CompositeScoreHolder oralCompositeScores;
    private final CompositeScoreHolder comprehensionCompositeScores;
    private final CompositeScoreHolder productiveCompositeScores;
    private final CompositeScoreHolder literacyCompositeScores;
	
	private List invalidSubtestIds;
	
	public LasLinkCompositeScoreCalculatorSecondEdition(Channel channel, Scorer scorer) {
        super(channel, scorer);
        
        contentAreaNamesRequiredForTotalComposite.add(WRITING_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForTotalComposite.add(READING_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForTotalComposite.add(SPEAKING_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForTotalComposite.add(LISTENING_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForTotalComposite.add(ORAL_LISTENING_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForTotalComposite.add(ORAL_SPEAKING_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForTotalComposite.add(COMPREHENSIVE_READING_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForTotalComposite.add(COMPREHENSIVE_LISTENING_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForTotalComposite.add(PRODUCTIVE_SPEAKING_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForTotalComposite.add(PRODUCTIVE_WRITING_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForTotalComposite.add(LITERACY_READING_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForTotalComposite.add(LITERACY_WRITING_CONTENT_AREA_NAME);

        
        contentAreaNamesRequiredForOverallComposite.add(WRITING_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForOverallComposite.add(READING_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForOverallComposite.add(SPEAKING_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForOverallComposite.add(LISTENING_CONTENT_AREA_NAME);
        
        contentAreaNamesRequiredForOralComposite.add(LISTENING_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForOralComposite.add(SPEAKING_CONTENT_AREA_NAME);
        
        contentAreaNamesRequiredForComprehensionComposite.add(LISTENING_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForComprehensionComposite.add(READING_CONTENT_AREA_NAME);
        
        contentAreaNamesRequiredForProductiveComposite.add(SPEAKING_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForProductiveComposite.add(WRITING_CONTENT_AREA_NAME);
        
        contentAreaNamesRequiredForLiteracyComposite.add(READING_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForLiteracyComposite.add(WRITING_CONTENT_AREA_NAME);

        overallCompositeScores = new CompositeScoreHolder();
        oralCompositeScores = new CompositeScoreHolder();
        comprehensionCompositeScores = new  CompositeScoreHolder();
        productiveCompositeScores = new CompositeScoreHolder();
        literacyCompositeScores = new  CompositeScoreHolder();
        
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
            if(event.getContentAreaName().equals(ORAL_CONTENT_AREA_NAME) 
            		|| event.getContentAreaName().equals(COMPREHENSIVE_CONTENT_AREA_NAME)
            		|| event.getContentAreaName().equals(PRODUCTIVE_CONTENT_AREA_NAME)
            		|| event.getContentAreaName().equals(LITERACY_CONTENT_AREA_NAME)) {
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
        	} else if(event.getContentAreaName().equals(COMPREHENSIVE_CONTENT_AREA_NAME)) {
        		if( event.getContentAreaMap().containsKey("Listening")) {
        			contentAreaRawScoreEvents.put(event.getContentAreaName()+ "Listening" , event);
        			contentAreaSubtestId.put(((Integer)event.getItemSetId()).toString()+event.getContentAreaName(),event.getContentAreaName()+ "Listening");
        		}
        		if( event.getContentAreaMap().containsKey("Reading")) {
        			contentAreaRawScoreEvents.put(event.getContentAreaName()+ "Reading" , event);
        			contentAreaSubtestId.put(((Integer)event.getItemSetId()).toString()+event.getContentAreaName(),event.getContentAreaName()+ "Reading");
        		}
        		
        	} else if(event.getContentAreaName().equals(PRODUCTIVE_CONTENT_AREA_NAME)) {
        		if( event.getContentAreaMap().containsKey("Speaking")) {
        			contentAreaRawScoreEvents.put(event.getContentAreaName()+ "Speaking" , event);
        			contentAreaSubtestId.put(((Integer)event.getItemSetId()).toString()+event.getContentAreaName(),event.getContentAreaName()+ "Listening");
        		}
        		if( event.getContentAreaMap().containsKey("Writing")) {
        			contentAreaRawScoreEvents.put(event.getContentAreaName()+ "Writing" , event);
        			contentAreaSubtestId.put(((Integer)event.getItemSetId()).toString()+event.getContentAreaName(),event.getContentAreaName()+ "Reading");
        		}
        		
        	} else if(event.getContentAreaName().equals(LITERACY_CONTENT_AREA_NAME)) {
        		if( event.getContentAreaMap().containsKey("Reading")) {
        			contentAreaRawScoreEvents.put(event.getContentAreaName()+ "Reading" , event);
        			contentAreaSubtestId.put(((Integer)event.getItemSetId()).toString()+event.getContentAreaName(),event.getContentAreaName()+ "Listening");
        		}
        		if( event.getContentAreaMap().containsKey("Writing")) {
        			contentAreaRawScoreEvents.put(event.getContentAreaName()+ "Writing" , event);
        			contentAreaSubtestId.put(((Integer)event.getItemSetId()).toString()+event.getContentAreaName(),event.getContentAreaName()+ "Reading");
        		}
        		
        	} else {
        		contentAreaRawScoreEvents.put(event.getContentAreaName(), event);
        	}
        }
    }
	
	public void onEvent(SubtestInvalidEvent event) {
        invalidSubtestIds.add(event.getItemSetId());
    }
	
	public void onEvent(AssessmentEndedEvent event) {
       
       if ("A".equals(pDupTestForm) || "B".equals(pDupTestForm) || "C".equals(pDupTestForm) || "D".equals(pDupTestForm)) { 
    	   pTestForm = pDupTestForm;
       }
       if ("Espa?ol".equals(pDupTestForm) || "Espanol".equals(pDupTestForm) || "Español".equals(pDupTestForm)) {
    	   pTestForm  = "S";
       }
       
       if ("Espa?ol2".equals(pDupTestForm) || "ESP B".equals(pDupTestForm) || "Español2".equals(pDupTestForm)) {
          	pTestForm  = "T";
        }       	 
        
       final Long testRosterId = event.getTestRosterId();
        calculateOralCompositeRawScores(); //pointObtained
        calculateComprehensionCompositeRawScores(); //pointObtained
        calculateProductiveCompositeRawScores(); //pointObtained
        calculateLiteracyCompositeRawScores(); //pointObtained
        calculateOverallCompositeRawScores();
        calculateTotalOralScaleScores();
        calculateTotalComprehensionScaleScores();
        calculateTotalProductiveScaleScores();
        calculateTotalLiteracyScaleScores();
        calculateTotalOverallScaleScores();
        calculateTotalCompositeDerivedScores();
        AddInSubtestContentAreaCompositeAndDerivedScoreMap();
        publishScores(LASLINK_OVERALL_SCORE, testRosterId, overallCompositeScores);
        publishSubtestScores();
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
        	if (computeValidSubtestScoreFlag(COMPREHENSIVE_LISTENING_CONTENT_AREA_NAME) && computeValidSubtestScoreFlag(COMPREHENSIVE_READING_CONTENT_AREA_NAME)){
	        	ContentAreaRawScoreEvent comprehensiveListening = getContentAreaRawScore(COMPREHENSIVE_LISTENING_CONTENT_AREA_NAME);
	        	comprehensionCompositeScores.pointsAttempted += comprehensiveListening.getPointsAttempted();
	        	comprehensionCompositeScores.pointsObtained += comprehensiveListening.getPointsObtained();
	        	comprehensionCompositeScores.pointsPossible += comprehensiveListening.getPointsPossible();
	            
	            ContentAreaRawScoreEvent comprehensiveReading = getContentAreaRawScore(COMPREHENSIVE_READING_CONTENT_AREA_NAME);
	            comprehensionCompositeScores.pointsAttempted += comprehensiveReading.getPointsAttempted();
	            comprehensionCompositeScores.pointsObtained += comprehensiveReading.getPointsObtained();
	            comprehensionCompositeScores.pointsPossible += comprehensiveReading.getPointsPossible();
	            comprehensionCompositeScores.validScore = CTBConstants.VALID_SCORE;
        	}
        }
    }
	
	private void calculateProductiveCompositeRawScores() {
        if (productiveCompositeScores.pointsPossible == 0) {
        	if (computeValidSubtestScoreFlag(PRODUCTIVE_SPEAKING_CONTENT_AREA_NAME) && computeValidSubtestScoreFlag(PRODUCTIVE_WRITING_CONTENT_AREA_NAME)){
	        	ContentAreaRawScoreEvent productiveSpeaking = getContentAreaRawScore(PRODUCTIVE_SPEAKING_CONTENT_AREA_NAME);
	        	productiveCompositeScores.pointsAttempted += productiveSpeaking.getPointsAttempted();
	        	productiveCompositeScores.pointsObtained += productiveSpeaking.getPointsObtained();
	        	productiveCompositeScores.pointsPossible += productiveSpeaking.getPointsPossible();
	            
	            ContentAreaRawScoreEvent productiveWriting = getContentAreaRawScore(PRODUCTIVE_WRITING_CONTENT_AREA_NAME);
	            productiveCompositeScores.pointsAttempted += productiveWriting.getPointsAttempted();
	            productiveCompositeScores.pointsObtained += productiveWriting.getPointsObtained();
	            productiveCompositeScores.pointsPossible += productiveWriting.getPointsPossible();
	            productiveCompositeScores.validScore = CTBConstants.VALID_SCORE;
        	}
        }
    }
	
	private void calculateLiteracyCompositeRawScores() {
        if (literacyCompositeScores.pointsPossible == 0) {
        	if (computeValidSubtestScoreFlag(LITERACY_READING_CONTENT_AREA_NAME) && computeValidSubtestScoreFlag(LITERACY_WRITING_CONTENT_AREA_NAME)){
	        	ContentAreaRawScoreEvent literacyReading = getContentAreaRawScore(LITERACY_READING_CONTENT_AREA_NAME);
	        	literacyCompositeScores.pointsAttempted += literacyReading.getPointsAttempted();
	        	literacyCompositeScores.pointsObtained += literacyReading.getPointsObtained();
	        	literacyCompositeScores.pointsPossible += literacyReading.getPointsPossible();
	            
	            ContentAreaRawScoreEvent literacyWriting = getContentAreaRawScore(LITERACY_WRITING_CONTENT_AREA_NAME);
	            literacyCompositeScores.pointsAttempted += literacyWriting.getPointsAttempted();
	            literacyCompositeScores.pointsObtained += literacyWriting.getPointsObtained();
	            literacyCompositeScores.pointsPossible += literacyWriting.getPointsPossible();
	            literacyCompositeScores.validScore = CTBConstants.VALID_SCORE;
        	}
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
	
	private void calculateTotalOralScaleScores() {
		Integer count = 0;
		if(validOralCompositeScoreFlag()) {
			BigDecimal OverallOralScaleScore = BigDecimal.ZERO;
			if(contentAreaScaleScore.containsKey(SPEAKING_CONTENT_AREA_NAME)) {
				OverallOralScaleScore = OverallOralScaleScore.add( getContentAreaScaleScore(SPEAKING_CONTENT_AREA_NAME));
				count++;

			}
			if(contentAreaScaleScore.containsKey(LISTENING_CONTENT_AREA_NAME)) {
				OverallOralScaleScore = OverallOralScaleScore.add( getContentAreaScaleScore(LISTENING_CONTENT_AREA_NAME));
				count++;

			}
			if(count != 0 )  {
				OverallOralScaleScore = OverallOralScaleScore.divide(new BigDecimal(count.toString()),BigDecimal.ROUND_FLOOR);
			}

			if(count == 0 ) 
				OverallOralScaleScore = null;

			if(OverallOralScaleScore != null) {
				contentAreaScaleScore.put(ORAL_CONTENT_AREA_NAME, OverallOralScaleScore.intValue());
				oralCompositeScores.scaleScore = OverallOralScaleScore;
				oralCompositeScores.validScore = CTBConstants.VALID_SCORE;
			}
		}
		if(count == 0){
			oralCompositeScores.validScore = CTBConstants.INVALID_SCORE;
		}
	}
	
	private void calculateTotalComprehensionScaleScores() {
		Integer count = 0;
		if(validComprehensionCompositeScoreFlag()) {
			BigDecimal OverallComprehensionScaleScore = BigDecimal.ZERO;
			if(contentAreaScaleScore.containsKey(LISTENING_CONTENT_AREA_NAME)) {
				OverallComprehensionScaleScore = OverallComprehensionScaleScore.add( getContentAreaScaleScore(LISTENING_CONTENT_AREA_NAME));
				count++;

			}
			if(contentAreaScaleScore.containsKey(READING_CONTENT_AREA_NAME)) {
				OverallComprehensionScaleScore = OverallComprehensionScaleScore.add( getContentAreaScaleScore(READING_CONTENT_AREA_NAME));
				count++;

			}
			if(count != 0 )  {
				OverallComprehensionScaleScore = OverallComprehensionScaleScore.divide(new BigDecimal(count.toString()),BigDecimal.ROUND_FLOOR);
			}

			if(count == 0 ) 
				OverallComprehensionScaleScore = null;

			if(OverallComprehensionScaleScore != null) {
				contentAreaScaleScore.put(COMPREHENSIVE_CONTENT_AREA_NAME, OverallComprehensionScaleScore.intValue());
				comprehensionCompositeScores.scaleScore = OverallComprehensionScaleScore;
				comprehensionCompositeScores.validScore = CTBConstants.VALID_SCORE;
			}
		}
		if(count == 0){
			comprehensionCompositeScores.validScore = CTBConstants.INVALID_SCORE;
		}
	}
	
	private void calculateTotalProductiveScaleScores() {
		Integer count = 0;
		if(validProductiveCompositeScoreFlag()) {
			BigDecimal OverallProductiveScaleScore = BigDecimal.ZERO;
			if(contentAreaScaleScore.containsKey(SPEAKING_CONTENT_AREA_NAME)) {
				OverallProductiveScaleScore = OverallProductiveScaleScore.add( getContentAreaScaleScore(SPEAKING_CONTENT_AREA_NAME));
				count++;

			}
			if(contentAreaScaleScore.containsKey(WRITING_CONTENT_AREA_NAME)) {
				OverallProductiveScaleScore = OverallProductiveScaleScore.add( getContentAreaScaleScore(WRITING_CONTENT_AREA_NAME));
				count++;

			}
			if(count != 0 )  {
				OverallProductiveScaleScore = OverallProductiveScaleScore.divide(new BigDecimal(count.toString()),BigDecimal.ROUND_FLOOR);
			}

			if(count == 0 ) 
				OverallProductiveScaleScore = null;

			if(OverallProductiveScaleScore != null) {
				contentAreaScaleScore.put(PRODUCTIVE_CONTENT_AREA_NAME, OverallProductiveScaleScore.intValue());
				productiveCompositeScores.scaleScore = OverallProductiveScaleScore;
				productiveCompositeScores.validScore = CTBConstants.VALID_SCORE;
			}
		}
		if(count == 0){
			productiveCompositeScores.validScore = CTBConstants.INVALID_SCORE;
		}
	}
	
	private void calculateTotalLiteracyScaleScores() {
		Integer count = 0;
		if(validLiteracyCompositeScoreFlag()) {
			BigDecimal OverallLiteracyScaleScore = BigDecimal.ZERO;
			if(contentAreaScaleScore.containsKey(READING_CONTENT_AREA_NAME)) {
				OverallLiteracyScaleScore = OverallLiteracyScaleScore.add( getContentAreaScaleScore(READING_CONTENT_AREA_NAME));
				count++;

			}
			if(contentAreaScaleScore.containsKey(WRITING_CONTENT_AREA_NAME)) {
				OverallLiteracyScaleScore = OverallLiteracyScaleScore.add( getContentAreaScaleScore(WRITING_CONTENT_AREA_NAME));
				count++;

			}
			if(count != 0 )  {
				OverallLiteracyScaleScore = OverallLiteracyScaleScore.divide(new BigDecimal(count.toString()),BigDecimal.ROUND_FLOOR);
			}

			if(count == 0 ) 
				OverallLiteracyScaleScore = null;

			if(OverallLiteracyScaleScore != null) {
				contentAreaScaleScore.put(LITERACY_CONTENT_AREA_NAME, OverallLiteracyScaleScore.intValue());
				literacyCompositeScores.scaleScore = OverallLiteracyScaleScore;
				literacyCompositeScores.validScore = CTBConstants.VALID_SCORE;
			}
		}
		if(count == 0){
			literacyCompositeScores.validScore = CTBConstants.INVALID_SCORE;
		}
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
	        	 OverallScaleScore = OverallScaleScore.divide(new BigDecimal(count.toString()),BigDecimal.ROUND_FLOOR);
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
	
	private void calculateTotalCompositeDerivedScores() {
	       if(contentAreaScaleScore.containsKey(OVERALL_CONTENT_AREA_NAME)){
	       final BigDecimal OverallScaleScore = getContentAreaScaleScore(OVERALL_CONTENT_AREA_NAME);
	       	overallCompositeScores.proficencyLevel = getScore(OVERALL_CONTENT_AREA_NAME,
	    		   OverallScaleScore, ScoreLookupCode.SCALED_SCORE,
	               ScoreLookupCode.PERFORMANCE_LEVEL, pTestLevel, pGrade);
	       	if(overallCompositeScores.proficencyLevel == null)
	       		overallCompositeScores.proficencyLevel = new BigDecimal(1);
	       	overallCompositeScores.nationalPercentile = getScore(OVERALL_CONTENT_AREA_NAME,OverallScaleScore,
	       			ScoreLookupCode.SCALED_SCORE,ScoreLookupCode.PERCENTILE_RANK, pTestLevel,null);
	       	if(overallCompositeScores.nationalPercentile == null)
	       		overallCompositeScores.nationalPercentile = new BigDecimal(1);
	       	overallCompositeScores.normalCurveEquivalent = getScore(OVERALL_CONTENT_AREA_NAME,OverallScaleScore,
	       			ScoreLookupCode.SCALED_SCORE,ScoreLookupCode.NORMAL_CURVE_EQUIVALENT, pTestLevel,null);
	       	if(overallCompositeScores.normalCurveEquivalent == null)
	       		overallCompositeScores.normalCurveEquivalent = new BigDecimal(1);
	      }
	       	
	       if ( contentAreaScaleScore.containsKey(ORAL_CONTENT_AREA_NAME)) {
	            final BigDecimal oralScaleScore = getContentAreaScaleScore(ORAL_CONTENT_AREA_NAME);
	            oralCompositeScores.proficencyLevel = getScore(ORAL_CONTENT_AREA_NAME,
	            		oralScaleScore, ScoreLookupCode.SCALED_SCORE,
	                    ScoreLookupCode.PERFORMANCE_LEVEL, pTestLevel, pGrade);
	        	if(oralCompositeScores.proficencyLevel == null)
	        		oralCompositeScores.proficencyLevel = new BigDecimal(1);
	        	oralCompositeScores.nationalPercentile = getScore(ORAL_CONTENT_AREA_NAME,oralScaleScore,
		       			ScoreLookupCode.SCALED_SCORE,ScoreLookupCode.PERCENTILE_RANK, pTestLevel,null);
		       	if(oralCompositeScores.nationalPercentile == null)
		       		oralCompositeScores.nationalPercentile = new BigDecimal(1);
		       	oralCompositeScores.normalCurveEquivalent = getScore(ORAL_CONTENT_AREA_NAME,oralScaleScore,
		       			ScoreLookupCode.SCALED_SCORE,ScoreLookupCode.NORMAL_CURVE_EQUIVALENT, pTestLevel,null);
		       	if(oralCompositeScores.normalCurveEquivalent == null)
		       		oralCompositeScores.normalCurveEquivalent = new BigDecimal(1);
	        }

	       if ( contentAreaScaleScore.containsKey(COMPREHENSIVE_CONTENT_AREA_NAME)) {
	        	final BigDecimal comprehensionScaleScore = getContentAreaScaleScore(COMPREHENSIVE_CONTENT_AREA_NAME);
	        	comprehensionCompositeScores.proficencyLevel = getScore(COMPREHENSIVE_CONTENT_AREA_NAME,
	        			comprehensionScaleScore,ScoreLookupCode.SCALED_SCORE, 
	        			ScoreLookupCode.PERFORMANCE_LEVEL, pTestLevel, pGrade);
	        	if(comprehensionCompositeScores.proficencyLevel == null)
	        		comprehensionCompositeScores.proficencyLevel = new BigDecimal(1);
	        	comprehensionCompositeScores.nationalPercentile = getScore(COMPREHENSIVE_CONTENT_AREA_NAME,comprehensionScaleScore,
		       			ScoreLookupCode.SCALED_SCORE,ScoreLookupCode.PERCENTILE_RANK, pTestLevel,null);
		       	if(comprehensionCompositeScores.nationalPercentile == null)
		       		comprehensionCompositeScores.nationalPercentile = new BigDecimal(1);
		       	comprehensionCompositeScores.normalCurveEquivalent = getScore(COMPREHENSIVE_CONTENT_AREA_NAME,comprehensionScaleScore,
		       			ScoreLookupCode.SCALED_SCORE,ScoreLookupCode.NORMAL_CURVE_EQUIVALENT, pTestLevel,null);
		       	if(comprehensionCompositeScores.normalCurveEquivalent == null)
		       		comprehensionCompositeScores.normalCurveEquivalent = new BigDecimal(1);
	        }
	       
	       if ( contentAreaScaleScore.containsKey(PRODUCTIVE_CONTENT_AREA_NAME)) {
	        	final BigDecimal productiveScaleScore = getContentAreaScaleScore(PRODUCTIVE_CONTENT_AREA_NAME);
	        	productiveCompositeScores.proficencyLevel = getScore(PRODUCTIVE_CONTENT_AREA_NAME,
	        			productiveScaleScore,ScoreLookupCode.SCALED_SCORE, 
	        			ScoreLookupCode.PERFORMANCE_LEVEL, pTestLevel, pGrade);
	        	if(productiveCompositeScores.proficencyLevel == null)
	        		productiveCompositeScores.proficencyLevel = new BigDecimal(1);
	        	productiveCompositeScores.nationalPercentile = getScore(PRODUCTIVE_CONTENT_AREA_NAME,productiveScaleScore,
		       			ScoreLookupCode.SCALED_SCORE,ScoreLookupCode.PERCENTILE_RANK, pTestLevel,null);
		       	if(productiveCompositeScores.nationalPercentile == null)
		       		productiveCompositeScores.nationalPercentile = new BigDecimal(1);
		       	productiveCompositeScores.normalCurveEquivalent = getScore(PRODUCTIVE_CONTENT_AREA_NAME,productiveScaleScore,
		       			ScoreLookupCode.SCALED_SCORE,ScoreLookupCode.NORMAL_CURVE_EQUIVALENT, pTestLevel,null);
		       	if(productiveCompositeScores.normalCurveEquivalent == null)
		       		productiveCompositeScores.normalCurveEquivalent = new BigDecimal(1);
	        }
	       
	       if ( contentAreaScaleScore.containsKey(LITERACY_CONTENT_AREA_NAME)) {
	        	final BigDecimal literacyScaleScore = getContentAreaScaleScore(LITERACY_CONTENT_AREA_NAME);
	        	literacyCompositeScores.proficencyLevel = getScore(LITERACY_CONTENT_AREA_NAME,
	        			literacyScaleScore,ScoreLookupCode.SCALED_SCORE, 
	        			ScoreLookupCode.PERFORMANCE_LEVEL, pTestLevel, pGrade);
	        	if(literacyCompositeScores.proficencyLevel == null)
	        		literacyCompositeScores.proficencyLevel = new BigDecimal(1);
	        	literacyCompositeScores.nationalPercentile = getScore(LITERACY_CONTENT_AREA_NAME,literacyScaleScore,
		       			ScoreLookupCode.SCALED_SCORE,ScoreLookupCode.PERCENTILE_RANK, pTestLevel,null);
		       	if(literacyCompositeScores.nationalPercentile == null)
		       		literacyCompositeScores.nationalPercentile = new BigDecimal(1);
		       	literacyCompositeScores.normalCurveEquivalent = getScore(LITERACY_CONTENT_AREA_NAME,literacyScaleScore,
		       			ScoreLookupCode.SCALED_SCORE,ScoreLookupCode.NORMAL_CURVE_EQUIVALENT, pTestLevel,null);
		       	if(literacyCompositeScores.normalCurveEquivalent == null)
		       		literacyCompositeScores.normalCurveEquivalent = new BigDecimal(1);
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
                subtestContentAreaCompositeAndDerived.setNationalPercentile(oralCompositeScores.nationalPercentile.intValue());
                subtestContentAreaCompositeAndDerived.setNormCurveEquivalent(oralCompositeScores.normalCurveEquivalent.intValue());
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
                subtestContentAreaCompositeAndDerived.setNationalPercentile(comprehensionCompositeScores.nationalPercentile.intValue());
                subtestContentAreaCompositeAndDerived.setNormCurveEquivalent(comprehensionCompositeScores.normalCurveEquivalent.intValue());
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
            
            if(contentArea.equals(PRODUCTIVE_CONTENT_AREA_NAME) && productiveCompositeScores.validScore.equals(CTBConstants.VALID_SCORE)){
            	  subtestContentAreaCompositeAndDerived.setScaleScore(scaleScore);
                  subtestContentAreaCompositeAndDerived.setProficencyLevelCode(productiveCompositeScores.proficencyLevel.intValue());
                  subtestContentAreaCompositeAndDerived.setProficencyLevelDescription(
                		  PerformanceLevel.getByCode(String.valueOf(productiveCompositeScores.proficencyLevel)).getDescription());
                  subtestContentAreaCompositeAndDerived.setNationalPercentile(productiveCompositeScores.nationalPercentile.intValue());
                  subtestContentAreaCompositeAndDerived.setNormCurveEquivalent(productiveCompositeScores.normalCurveEquivalent.intValue());
                  subtestContentAreaCompositeAndDerived.setPointsAttempted(productiveCompositeScores.pointsAttempted);
              	  subtestContentAreaCompositeAndDerived.setPointsObtained(productiveCompositeScores.pointsObtained);
              	  subtestContentAreaCompositeAndDerived.setPointsPossible(productiveCompositeScores.pointsPossible);
              	  subtestContentAreaCompositeAndDerived.setPercentObtained(productiveCompositeScores.getPercentObtained());
            	  subtestContentAreaCompositeAndDerived.setContentAreaName(contentArea);
            	  subtestContentAreaCompositeAndDerived.setValidScore(productiveCompositeScores.validScore) ; 
            	  
              } else if (contentArea.equals(PRODUCTIVE_CONTENT_AREA_NAME) && productiveCompositeScores.validScore.equals(CTBConstants.INVALID_SCORE)){
            	  subtestContentAreaCompositeAndDerived.setContentAreaName(contentArea);
            	  subtestContentAreaCompositeAndDerived.setValidScore(productiveCompositeScores.validScore) ; 
              }
            
            if(contentArea.equals(LITERACY_CONTENT_AREA_NAME) && literacyCompositeScores.validScore.equals(CTBConstants.VALID_SCORE)){
          	  subtestContentAreaCompositeAndDerived.setScaleScore(scaleScore);
                subtestContentAreaCompositeAndDerived.setProficencyLevelCode(literacyCompositeScores.proficencyLevel.intValue());
                subtestContentAreaCompositeAndDerived.setProficencyLevelDescription(
              		  PerformanceLevel.getByCode(String.valueOf(literacyCompositeScores.proficencyLevel)).getDescription());
                subtestContentAreaCompositeAndDerived.setNationalPercentile(literacyCompositeScores.nationalPercentile.intValue());
                subtestContentAreaCompositeAndDerived.setNormCurveEquivalent(literacyCompositeScores.normalCurveEquivalent.intValue());
                subtestContentAreaCompositeAndDerived.setPointsAttempted(literacyCompositeScores.pointsAttempted);
            	  subtestContentAreaCompositeAndDerived.setPointsObtained(literacyCompositeScores.pointsObtained);
            	  subtestContentAreaCompositeAndDerived.setPointsPossible(literacyCompositeScores.pointsPossible);
            	  subtestContentAreaCompositeAndDerived.setPercentObtained(literacyCompositeScores.getPercentObtained());
          	  subtestContentAreaCompositeAndDerived.setContentAreaName(contentArea);
          	  subtestContentAreaCompositeAndDerived.setValidScore(literacyCompositeScores.validScore) ; 
          	  
            } else if (contentArea.equals(LITERACY_CONTENT_AREA_NAME) && literacyCompositeScores.validScore.equals(CTBConstants.INVALID_SCORE)){
          	  subtestContentAreaCompositeAndDerived.setContentAreaName(contentArea);
          	  subtestContentAreaCompositeAndDerived.setValidScore(literacyCompositeScores.validScore) ; 
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
	
	private void publishScores(final String scoreType, final Long testRosterId,
			final CompositeScoreHolder scores) {
		channel.send(new SubtestContentAreaCompositeScoreEvent(testRosterId, scoreType,
				scores.scaleScore, scores.normalCurveEquivalent, null, null, scores.nationalPercentile, pNormGroup, pNormYear,
				null, null, null, null, null,null, scores.getPointsObtained(), scores
				.getPointsAttempted(), scores.getPointsPossible(), null, scores
				.getPercentObtained(), scores.validScore, scores.proficencyLevel));
	}
	
	private void publishSubtestScores() {
        if (!SubtestContentAreaCompositeAndDerivedScore.isEmpty() ) {
            channel.send(new SubtestContentAreaCompositeAndDerivedEvent(testRosterId, SubtestContentAreaCompositeAndDerivedScore));
        }
    }
	
	private boolean validOralCompositeScoreFlag() {
    	boolean validFlag = false;
    	Iterator itr = contentAreaNamesRequiredForOralComposite.iterator();
    		while(itr.hasNext()) {  
    			String contentArea = (String)itr.next(); 
    			validFlag = computeValidSubtestScoreFlag(contentArea);
    			if(!validFlag)
    				return false;
    		}
    		return validFlag;
    	
    }
	
	private boolean validComprehensionCompositeScoreFlag() {
    	boolean validFlag = false;
    	Iterator itr = contentAreaNamesRequiredForComprehensionComposite.iterator();
    		while(itr.hasNext()) {  
    			String contentArea = (String)itr.next(); 
    			validFlag = computeValidSubtestScoreFlag(contentArea);
    			if(!validFlag)
    				return false;
    		}
    		return validFlag;
    	
    }
	
	private boolean validProductiveCompositeScoreFlag() {
    	boolean validFlag = false;
    	Iterator itr = contentAreaNamesRequiredForProductiveComposite.iterator();
    		while(itr.hasNext()) {  
    			String contentArea = (String)itr.next(); 
    			validFlag = computeValidSubtestScoreFlag(contentArea);
    			if(!validFlag)
    				return false;
    		}
    		return validFlag;
    	
    }
	
	private boolean validLiteracyCompositeScoreFlag() {
    	boolean validFlag = false;
    	Iterator itr = contentAreaNamesRequiredForLiteracyComposite.iterator();
    		while(itr.hasNext()) {  
    			String contentArea = (String)itr.next(); 
    			validFlag = computeValidSubtestScoreFlag(contentArea);
    			if(!validFlag)
    				return false;
    		}
    		return validFlag;
    	
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
	
	private ContentAreaRawScoreEvent getContentAreaRawScore(String contentAreaName) {
        return (ContentAreaRawScoreEvent) contentAreaRawScoreEvents.get(contentAreaName);
    }
	
	private BigDecimal getContentAreaScaleScore(String contentAreaName) {
    	return new BigDecimal(contentAreaScaleScore.get(contentAreaName).toString());
    }
	
	Float getPercentObtainedForSubtest( int pointsObtained, int pointsPossible) {
        if (pointsPossible == 0)
            return null;

        int p = (int)Math.pow(10, 1);
        pointsObtained = pointsObtained * p;
        float percent = Math.round(((float) pointsObtained / (float) pointsPossible) * 100 );
        return percent/p;
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
