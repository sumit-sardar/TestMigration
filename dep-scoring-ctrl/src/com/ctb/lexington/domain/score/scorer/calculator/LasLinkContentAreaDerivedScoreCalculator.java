package com.ctb.lexington.domain.score.scorer.calculator;

import java.math.BigDecimal;
import java.util.Map;

import com.ctb.lexington.db.utils.DatabaseHelper;
import com.ctb.lexington.domain.score.event.ContentAreaDerivedScoreEvent;
import com.ctb.lexington.domain.score.event.ContentAreaNumberCorrectEvent;
import com.ctb.lexington.domain.score.event.ContentAreaRawScoreEvent;
import com.ctb.lexington.domain.score.event.SubtestStartedEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.domain.teststructure.PerformanceLevel;
import com.ctb.lexington.domain.teststructure.ScoreLookupCode;
import com.ctb.lexington.util.SafeHashMap;

/**
 * @author TCS
*/
public class LasLinkContentAreaDerivedScoreCalculator extends AbstractDerivedScoreCalculator {
    private static final String LASLINK_FRAMEWORK_CODE = "LLEAB";
    private static final String LASLINK_FRAMEWORK_CODE_2ND_EDTN = "LL2ND";
//    private static final String TESTLEVELFORLISTENING = "K-1";
    public LasLinkContentAreaDerivedScoreCalculator(Channel channel, Scorer scorer) {
        super(channel, scorer);
        channel.subscribe(this, ContentAreaRawScoreEvent.class);
        channel.subscribe(this, ContentAreaNumberCorrectEvent.class);
        mustPrecede(SubtestStartedEvent.class, ContentAreaNumberCorrectEvent.class);
    }
    
    private final Map contentAreaRawScoreEvents = new SafeHashMap(String.class,
            ContentAreaRawScoreEvent.class);
    
    
    public void onEvent(ContentAreaRawScoreEvent event) {
            contentAreaRawScoreEvents.put(event.getContentAreaName(), event);
    }
    
    
    public void onEvent(ContentAreaNumberCorrectEvent event) {
    	
    	final Integer subtestId = DatabaseHelper.asInteger(event.getSubtestId());
        
         
       /*  if("K".equals(pTestLevel) || "1".equals(pTestLevel)) {
             pTestLevel = "1";
         }
         if( "2-3".equals(pTestLevel) ){  pTestLevel = "2"; }
         if( "4-5".equals(pTestLevel) ){  pTestLevel = "3"; }
         if( "6-8".equals(pTestLevel) ){  pTestLevel = "4"; }
         if( "9-12".equals(pTestLevel) ){ pTestLevel = "5"; }
         */
    	if("C".equals(pDupTestForm) || "D".equals(pDupTestForm) ||  "ESP B".equals(pDupTestForm) || "Espa?ol B".equals(pDupTestForm) || "Espanol B".equals(pDupTestForm) || "Español B".equals(pDupTestForm)){
    		
    		if( "K-1".equals(pTestLevel) && "K".equals(pGrade) && !"Listening".equals(event.getContentAreaName())){
    			pTestLevel = "K"; 
    			}
    		if( "K-1".equals(pTestLevel) && "1".equals(pGrade) && !"Listening".equals(event.getContentAreaName())){
    			pTestLevel = "1"; 
    			}
    		if(("K".equals(pTestLevel) ||  "1".equals(pTestLevel)) && "Listening".equals(event.getContentAreaName())){
    			pTestLevel = "K-1";
    		}
    	}
    	
        if ("A".equals(pDupTestForm) || "B".equals(pDupTestForm) || "C".equals(pDupTestForm) || "D".equals(pDupTestForm)) { 
        	 pTestForm = pDupTestForm;
        }
        if ("Espa?ol".equals(pDupTestForm) || "Espanol".equals(pDupTestForm) || "Español".equals(pDupTestForm) || "Espa?ol A".equals(pDupTestForm) || "Espanol A".equals(pDupTestForm) || "Español A".equals(pDupTestForm)) {
        	pTestForm  = "S";
        }
        if ("Espa?ol2".equals(pDupTestForm) || "ESP B".equals(pDupTestForm) || "Español2".equals(pDupTestForm) || "Espa?ol B".equals(pDupTestForm) || "Espanol B".equals(pDupTestForm) || "Español B".equals(pDupTestForm)) {
        	pTestForm  = "T";
        }
        ContentAreaRawScoreEvent contentAreaRawScoreEvent = (ContentAreaRawScoreEvent)contentAreaRawScoreEvents.get(event.getContentAreaName());
        //System.out.println("===>"+contentAreaRawScoreEvent.getPointsObtained());
        Integer pointObtained = contentAreaRawScoreEvent.getPointsObtained();
        BigDecimal obtained = new BigDecimal(pointObtained.toString());
        final BigDecimal scaleScore = (("C".equals(pDupTestForm) || "D".equals(pDupTestForm) || "ESP B".equals(pDupTestForm) || "Espa?ol B".equals(pDupTestForm) || "Espanol B".equals(pDupTestForm) || "Español B".equals(pDupTestForm)) && 
        		("Comprehension".equals(event.getContentAreaName())|| "Oral".equals(event.getContentAreaName()) 
        				||"Productive".equals(event.getContentAreaName()) ||"Literacy".equals(event.getContentAreaName())))? null :getScore(
    			subtestId,
    			event.getContentAreaName(),
				null,
				pTestForm,
				pTestLevel,
				null,
				ScoreLookupCode.SUBTEST_NUMBER_CORRECT,
				obtained,
				ScoreLookupCode.SCALED_SCORE,
				null );
        
       final BigDecimal proficencyLevelValue = getLasLinkPerformanceLevel((("C".equals(pDupTestForm) || "D".equals(pDupTestForm) || "ESP B".equals(pDupTestForm) || "Espa?ol B".equals(pDupTestForm) || "Espanol B".equals(pDupTestForm) || "Español B".equals(pDupTestForm))?LASLINK_FRAMEWORK_CODE_2ND_EDTN : LASLINK_FRAMEWORK_CODE ), event.getContentAreaName(),
    		   ("C".equals(pDupTestForm) || "D".equals(pDupTestForm) || "ESP B".equals(pDupTestForm) || "Espa?ol B".equals(pDupTestForm) || "Espanol B".equals(pDupTestForm) || "Español B".equals(pDupTestForm))? null : pTestLevel, scaleScore, pGrade, pTestForm);
       final PerformanceLevel proficencyLevel = PerformanceLevel.getByCode(String
                .valueOf(proficencyLevelValue));
       
       final BigDecimal normalCurveEquivalent = ("A".equals(pDupTestForm) || "B".equals(pDupTestForm) || "S".equals(pDupTestForm) || scaleScore==null) ? null : getLasLinkNCE(
    		   LASLINK_FRAMEWORK_CODE_2ND_EDTN,
   			event.getContentAreaName(),
				pTestLevel,
				scaleScore,
				null,
				pTestForm);
        
       final BigDecimal percentileRank = ("A".equals(pDupTestForm) || "B".equals(pDupTestForm) || "S".equals(pDupTestForm) || scaleScore==null) ? null : getLasLinkPR(
    		   LASLINK_FRAMEWORK_CODE_2ND_EDTN,
      			event.getContentAreaName(),
      			pTestLevel,
   				scaleScore,
   				null,
   				pTestForm);
       
       final BigDecimal lexileValue = ("A".equals(pDupTestForm) || "B".equals(pDupTestForm) || "S".equals(pDupTestForm) || !"Reading".equals(event.getContentAreaName())) ? null : getLasLinkLexile(
    		   LASLINK_FRAMEWORK_CODE_2ND_EDTN,
     			event.getContentAreaName(),
     			("K".equals(pTestLevel) || "1".equals(pTestLevel))?"K-1":pTestLevel,
  				scaleScore,
  				null,
  				pTestForm);
       
       //System.err.println(">>>>>ScaleScore ::"+scaleScore+"<<>>>percentileRank :: "+percentileRank+"<<>>>proficencyLevelValue :: "+proficencyLevelValue+"<<<>>>ContentArea ::"+event.getContentAreaName());
       if("A".equals(pDupTestForm) || "B".equals(pDupTestForm) || "S".equals(pDupTestForm)){
    	   
    	   channel.send(new ContentAreaDerivedScoreEvent(
                event.getTestRosterId(),
           		event.getSubtestId(), 
   				event.getContentAreaId(), 
   				event.getContentAreaName(), 
   				scaleScore, 
   				null,//standardErrorMeasurement,
   				null,//normalCurveEquivalent,
   				null,//gradeEquivalent,
   				null,//null,
   				null,//nationalStanine, 
   				null,//nationalPercentile, 
   				null,
   				null,
   				proficencyLevel,
   				pNormGroup,
                pNormYear,
   				pAgeCategory,
                pTestLevel,
                pRecommendedLevel));
       }else {
    	   channel.send(new ContentAreaDerivedScoreEvent(
                event.getTestRosterId(),
          		event.getSubtestId(), 
  				event.getContentAreaId(), 
  				event.getContentAreaName(), 
  				scaleScore, 
  				null,//standardErrorMeasurement,
  				normalCurveEquivalent,//normalCurveEquivalent,
  				null,//gradeEquivalent,
  				null,//null,
  				null,//nationalStanine, 
  				percentileRank,//percentile rank as nationalPercentile, 
  				null,
  				null,
  				proficencyLevel, //performance Level
  				pNormGroup,
                pNormYear,
  				pAgeCategory,
                pTestLevel,
                pRecommendedLevel,
                lexileValue));
       }
    }
}