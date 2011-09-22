package com.ctb.tms.util; 

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    
    public static class RosterSubtestStatus 
    { 
        private int testRosterId;
    	private String testCompletionStatus;
    	private int itemSetId;
        private String itemSetName;
        private String subtestCompletionStatus;
        private Date updatedDateTime;
        private int lastMseq;
        private int rawScore;
        private String recommendedLevel;
        private String scorable;
        
        public Date getUpdatedDateTime() {
            return this.updatedDateTime;
        }
        
        public void setUpdatedDateTime(Date updatedDateTime) {
            this.updatedDateTime = updatedDateTime;
        }
        
        public int getLastMseq() {
            return this.lastMseq;
        }
        
        public void setLastMseq(int lastMseq) {
            this.lastMseq = lastMseq;
        }
        
        public int getTestRosterId() {
            return this.testRosterId;
        }
        
        public void setTestRosterId(int testRosterId) {
            this.testRosterId = testRosterId;
        }
        
        public String getTestCompletionStatus() {
            return this.testCompletionStatus;
        }
        
        public void setTestCompletionStatus(String testCompletionStatus) {
            this.testCompletionStatus = testCompletionStatus;
        }
        
        public int getItemSetId() {
            return this.itemSetId;
        }
        
        public void setItemSetId(int itemSetId) {
            this.itemSetId = itemSetId;
        }
        
        public String getSubtestCompletionStatus() {
            return this.subtestCompletionStatus;
        }
        
        public void setSubtestCompletionStatus(String subtestCompletionStatus) {
            this.subtestCompletionStatus = subtestCompletionStatus;
        }

    	/**
    	 * @return the rawScore
    	 */
    	public int getRawScore() {
    		return rawScore;
    	}

    	/**
    	 * @param rawScore the rawScore to set
    	 */
    	public void setRawScore(int rawScore) {
    		this.rawScore = rawScore;
    	}

    	/**
    	 * @return the itemSetName
    	 */
    	public String getItemSetName() {
    		return itemSetName;
    	}

    	/**
    	 * @param itemSetName the itemSetName to set
    	 */
    	public void setItemSetName(String itemSetName) {
    		this.itemSetName = itemSetName;
    	}

    	/**
    	 * @return the recommendedLevel
    	 */
    	public String getRecommendedLevel() {
    		return recommendedLevel;
    	}

    	/**
    	 * @param recommendedLevel the recommendedLevel to set
    	 */
    	public void setRecommendedLevel(String recommendedLevel) {
    		this.recommendedLevel = recommendedLevel;
    	}

    	public String getScorable() {
    		return scorable;
    	}

    	public void setScorable(String scorable) {
    		this.scorable = scorable;
    	}
    	
    	
    } 
    
    public static class RecommendedSubtestLevel 
    { 
    	private int itemSetId;
        private String recommendedItemSetName;
        private String recommendedLevel;
        
    	/**
    	 * @param itemSetId
    	 * @param recommendedItemSetName
    	 * @param recommendedLevel
    	 */
    	public RecommendedSubtestLevel(int itemSetId, String recommendedItemSetName, String recommendedLevel) {
    		super();
    		this.itemSetId = itemSetId;
    		this.recommendedItemSetName = recommendedItemSetName;
    		this.recommendedLevel = recommendedLevel;
    	}
        
    	/**
    	 * @return the itemSetId
    	 */
    	public int getItemSetId() {
    		return itemSetId;
    	}
    	/**
    	 * @param itemSetId the itemSetId to set
    	 */
    	public void setItemSetId(int itemSetId) {
    		this.itemSetId = itemSetId;
    	}
    	/**
    	 * @return the recommendedItemSetName
    	 */
    	public String getRecommendedItemSetName() {
    		return recommendedItemSetName;
    	}
    	/**
    	 * @param recommendedItemSetName the recommendedItemSetName to set
    	 */
    	public void setRecommendedItemSetName(String recommendedItemSetName) {
    		this.recommendedItemSetName = recommendedItemSetName;
    	}
    	/**
    	 * @return the recommendedLevel
    	 */
    	public String getRecommendedLevel() {
    		return recommendedLevel;
    	}
    	/**
    	 * @param recommendedLevel the recommendedLevel to set
    	 */
    	public void setRecommendedLevel(String recommendedLevel) {
    		this.recommendedLevel = recommendedLevel;
    	}
        
    } 
    
    public static HashMap calculateRecommendSubtestLevel(RosterSubtestStatus [] subtests) {
        HashMap result = new HashMap(subtests.length);        
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
                        Iterator it = recommendedList.iterator();
                        while(it.hasNext()) {
                        	RecommendedSubtestLevel rsl = (RecommendedSubtestLevel) it.next();
                        	result.put(rsl.recommendedItemSetName.trim(), rsl);
                        }
                        recommendedList = getRecommendSubtestLevel(locatorName, score, subtest.getItemSetId());
                        it = recommendedList.iterator();
                        while(it.hasNext()) {
                        	RecommendedSubtestLevel rsl = (RecommendedSubtestLevel) it.next();
                        	result.put(rsl.recommendedItemSetName.trim(), rsl);
                        }
                    }
                    else {
                        recommendedList = getRecommendSubtestLevel(locatorName, score, subtest.getItemSetId());
                        Iterator it = recommendedList.iterator();
                        while(it.hasNext()) {
                        	RecommendedSubtestLevel rsl = (RecommendedSubtestLevel) it.next();
                        	result.put(rsl.recommendedItemSetName.trim(), rsl);
                        }
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
