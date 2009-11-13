package com.ctb.util.testDelivery; 

import com.ctb.bean.testDelivery.studentTestData.RecommendedSubtestLevel;
import com.ctb.bean.testDelivery.studentTestData.RosterSubtestStatus;
import java.util.ArrayList;
import java.util.List;

/**
 * @author john_wang
 *
 * TabeLocatorUtils
 */

public class TabeLocatorUtils 
{ 
    private static final String MATH = "Math";
    private static final String READING = "Reading";
    private static final String LANGUAGE = "Language";
    
    public static List calculateRecommendSubtestLevel(RosterSubtestStatus [] subtests) {
        List result = new ArrayList();        
        boolean foundFirstMath = false;
        boolean foundSecondMath = false;
        RosterSubtestStatus firstMathSubtest = null;
        for (int i = 0; i <  subtests.length; i ++) {
            RosterSubtestStatus subtest = subtests[i];
            if (subtest.getItemSetName().indexOf("Sample") < 0 //not sample
                && subtest.getRecommendedLevel() == null //not recommended yet
                && subtest.getSubtestCompletionStatus().equals("CO")) //completed
            {
                int score = subtest.getRawScore();
                String locatorName = null;
                if (subtest.getItemSetName().indexOf("Mathematics") > 0) {
                    if (!foundFirstMath) {
                        firstMathSubtest = subtest;
                        foundFirstMath = true;
                    }
                    else {
                        foundSecondMath = true;
                        score = score + firstMathSubtest.getRawScore();
                        locatorName = MATH;
                    }
                    
                }
                else if (subtest.getItemSetName().equals("TABE Locator Reading")) {
                    locatorName = READING;
                }
                else if (subtest.getItemSetName().equals("TABE Locator Language")) {
                    locatorName = LANGUAGE;
                }

                if ( locatorName != null) {
                    List recommendedList;
                    if (locatorName.equals(MATH)) {
                        recommendedList = getRecommendSubtestLevel(locatorName, score, firstMathSubtest.getItemSetId());
                        result.addAll(recommendedList);
                        recommendedList = getRecommendSubtestLevel(locatorName, score, subtest.getItemSetId());
                        result.addAll(recommendedList);
                    }
                    else {
                        recommendedList = getRecommendSubtestLevel(locatorName, score, subtest.getItemSetId());
                        result.addAll(recommendedList);
                    }
                                 
                }
                
            }
        }   
		return result;
	}

    private static List getRecommendSubtestLevel(String locatorName, int score, int itemSetId) {
        String level = null;
        List result = new ArrayList();
        
        if (locatorName.equals(MATH)) {
            if (score >= 12)
                level = "A";
            else if (score >= 9)
                level = "D";
            else if (score >= 7)
                level = "M";
            else if (score >= 0)
                level = "E"; 
            result.add(new RecommendedSubtestLevel(itemSetId, "TABE Mathematics Computation", level));
            result.add(new RecommendedSubtestLevel(itemSetId, "TABE Applied Mathematics", level));
                            
        }
        else if (locatorName.equals(READING)) {
            if (score >= 11)
                level = "A";
            else if (score >= 9)
                level = "D";
            else if (score >= 7)
                level = "M";
            else if (score >= 0)
                level = "E"; 
            result.add(new RecommendedSubtestLevel(itemSetId, "TABE Reading", level));
            result.add(new RecommendedSubtestLevel(itemSetId, "TABE Vocabulary", level));
        }
        else if (locatorName.equals(LANGUAGE)) {
            if (score >= 11)
                level = "A";
            else if (score >= 9)
                level = "D";
            else if (score >= 7)
                level = "M";
            else if (score >= 0)
                level = "E"; 
            result.add(new RecommendedSubtestLevel(itemSetId, "TABE Language", level));
            result.add(new RecommendedSubtestLevel(itemSetId, "TABE Language Mechanics", level));
            result.add(new RecommendedSubtestLevel(itemSetId, "TABE Spelling", level));
        }

		return result;
	}


} 
