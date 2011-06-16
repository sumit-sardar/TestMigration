package com.ctb.oas.normsdata;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class ScorerFactory {
    private static Map scorers = new HashMap();
    private static Map lasLinksScorers = new HashMap();

    static {
        scorers.put(ScoreType.SCALE_SCORE, ScaleScoreTerraNovaScorer.class);
        scorers.put(ScoreType.STANDARD_ERROR_OF_MEASUREMENT, SEMTerraNovaScorer.class);
        scorers.put(ScoreType.GRADE_EQUIVALENT, GEDTerraNovaScorer.class);
        scorers.put(ScoreType.MEAN_GRADE_EQUIVALENT, MeanGEDTerraNovaScorer.class);
        scorers.put(ScoreType.EXTENDED_GRADE_EQUIVALENT, ExtendedGradeEquivalentTerraNovaScorer.class);
        scorers.put(ScoreType.NORMAL_CURVE_EQUIVALENT, NCETerraNovaScorer.class);
        scorers.put(ScoreType.NATIONAL_PERCENTILE, NPTerraNovaScorer.class);
        
        lasLinksScorers.put(ScoreType.SCALE_SCORE, ScaleScoreLasLinksScorer.class);
    	lasLinksScorers.put(ScoreType.STANDARD_ERROR_OF_MEASUREMENT, SEMLasLinksScorer.class);
    	lasLinksScorers.put(ScoreType.GRADE_EQUIVALENT, GEDLasLinksScorer.class);
    	lasLinksScorers.put(ScoreType.MEAN_GRADE_EQUIVALENT, MeanGEDLasLinksScorer.class);
    	lasLinksScorers.put(ScoreType.EXTENDED_GRADE_EQUIVALENT, ExtendedGradeEquivalentLasLinksScorer.class);
    	lasLinksScorers.put(ScoreType.NORMAL_CURVE_EQUIVALENT, NCELasLinksScorer.class);
    	lasLinksScorers.put(ScoreType.NATIONAL_PERCENTILE, NPLasLinksScorer.class);
    	
    	lasLinksScorers.put(ScoreType.PERFORMANCE, PerformanceLevelLasLinksScore.class);
    	lasLinksScorers.put(ScoreType.REFERENCIAL_NATIONAL_PERCENTILE, ReferencialPercentileLaslinksScore.class);
    	lasLinksScorers.put(ScoreType.REFERENCIAL_NATIONAL_CURVE, ReferencialCurveLaslinksScore.class);
    }

    public static TerraNovaScorer getScorer(ScoreType scoreType) {
        try {
            Class scorerClass = (Class) scorers.get(scoreType);
            return (TerraNovaScorer) scorerClass.newInstance();
        }
        catch (Exception e) {
            throw new RuntimeException("cannot create scorer for " + scoreType.getTypeString(), e);
        }
    }
    
    public static LasLinksScorer getLasLinksScorer(ScoreType scoreType) {
        try {
            Class scorerClass = (Class) lasLinksScorers.get(scoreType);
            return (LasLinksScorer) scorerClass.newInstance();
        }
        catch (Exception e) {
            throw new RuntimeException("cannot create scorer for " + scoreType.getTypeString(), e);
        }
    }
}
