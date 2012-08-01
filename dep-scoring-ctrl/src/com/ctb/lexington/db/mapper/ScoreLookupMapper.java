package com.ctb.lexington.db.mapper;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;

import com.ctb.lexington.db.record.ScoreLookupRecord;
import com.ctb.lexington.domain.teststructure.ScoreLookupCode;
import java.util.ArrayList;

public class ScoreLookupMapper extends AbstractDBMapper {
    private static final String FIND_SCORE_BY_ITEM_SET = "findScoreLookupForItemSet";
    private static final String FIND_SCORE_BY_UNIQUE_KEY_AND_ITEM_SET = "findScoreLookupForItemSetAndUniqueKey";
    private static final String FIND_SCORE_BY_UNIQUE_KEY_AND_FRAMEWORK_CODE = "findScoreLookupForFrameworkCode";
    private static final String FINF_SCORE_BY_UNIQUE_KEY_AND_FRAMEWORK_CODE_AGAIN = "findScoreLookupForFrameworkCodeAgain";  // For Laslink Scoring 
    private static final String FIND_TV_COMPOSITE_SCORE = "findTerraNovaCompositeScore";
    private static final String FIND_TV_PERFORMANCE_LEVEL = "findTerraNovaPerformanceLevel";
    private static final String FIND_LL_PERFORMANCE_LEVEL = "findLasLinkPerformanceLevel";  // For Laslink Scoring
    private static final String FIND_HIGH_MODERATE_MASTERY = "findScoreLookupForHighMasteryRange";
    private static final String FIND_LOW_MODERATE_MASTERY = "findScoreLookupForLowMasteryRange";

    public ScoreLookupMapper(final Connection conn) {
        super(conn);
    }

    public ScoreLookupRecord findScoreLookupByItemSetIdAndNumberCorrect(final long itemSetId,
            final String sourceScoreTypeCode, final String destinationScoreTypeCode,
            final BigDecimal sourceScoreValue, final String testForm, final String testLevel,
            final String grade, final String contentArea, final String normGroup,
            final String ageCategory) {
        final ScoreLookupRecord template = new ScoreLookupRecord(new Long(itemSetId),
                sourceScoreTypeCode, destinationScoreTypeCode, sourceScoreValue, null);
        template.setTestForm(testForm);
        template.setTestLevel(testLevel);
        template.setGrade(grade);
        template.setContentArea(contentArea);
        template.setNormGroup(normGroup);
        template.setAgeCategory(ageCategory);

        List results = findMany(FIND_SCORE_BY_UNIQUE_KEY_AND_ITEM_SET, template);

        if(results != null && results.size() > 0) {
            return (ScoreLookupRecord) results.get(0);
        } else {
            return null;
        }
    }

    public ScoreLookupRecord findScoreLookupByItemSetId(final Long itemSetId,
            final String normGroup, final String sourceScoreTypeCode,
            final String destinationScoreTypeCode, final BigDecimal sourceScoreValue) {
        final ScoreLookupRecord template = new ScoreLookupRecord(itemSetId, sourceScoreTypeCode,
                destinationScoreTypeCode, sourceScoreValue, null);
        template.setNormGroup(normGroup);

        return (ScoreLookupRecord) find(FIND_SCORE_BY_ITEM_SET, template);
    }

    public ScoreLookupRecord findScoreLookupByFrameworkCode(final String frameworkCode,
            final String sourceScoreTypeCode, final String destinationScoreTypeCode,
            final BigDecimal sourceScoreValue, final String testForm, final String testLevel,
            final String grade, final String contentArea, final String normGroup,
            final String ageCategory) {
        final ScoreLookupRecord template = new ScoreLookupRecord(null, sourceScoreTypeCode,
                destinationScoreTypeCode, sourceScoreValue, null);
        template.setFrameworkCode(frameworkCode);
        template.setTestForm(testForm);
        template.setTestLevel(testLevel);
        template.setGrade(grade);
        template.setContentArea(contentArea);
        template.setNormGroup(normGroup);
        template.setAgeCategory(ageCategory);
  // For Laslink Scoring
        ScoreLookupRecord results =  (ScoreLookupRecord) find(FIND_SCORE_BY_UNIQUE_KEY_AND_FRAMEWORK_CODE, template);

        if(results != null ) {
            return (ScoreLookupRecord) results;
        } else {
        	results =  (ScoreLookupRecord) find(FINF_SCORE_BY_UNIQUE_KEY_AND_FRAMEWORK_CODE_AGAIN, template);
        	if(results != null ) {
        		return (ScoreLookupRecord) results;
        	}else {
        		return null;
        	}
        }
        
    }

    public ScoreLookupRecord findTerraNovaCompositeScore(final String sourceScoreTypeCode,
            final String destinationScoreTypeCode, final BigDecimal sourceScoreValue,
            final String grade, final String normGroup, final String normYear) {
        final ScoreLookupRecord template = new ScoreLookupRecord(null, sourceScoreTypeCode,
                destinationScoreTypeCode, sourceScoreValue, null);
        template.setGrade(grade);
        template.setNormGroup(normGroup);
        template.setNormYear(normYear);

        return (ScoreLookupRecord) find(FIND_TV_COMPOSITE_SCORE, template);
    }

    public BigDecimal findTerraNovaPerformanceLevel(final BigDecimal sourceScoreValue,
            final String testLevel, final String contentArea) {
        final ScoreLookupRecord template = new ScoreLookupRecord(null, null, null,
                sourceScoreValue, null);
        template.setTestLevel(testLevel);
        template.setContentArea(contentArea);

        final ScoreLookupRecord perfLevel = (ScoreLookupRecord) find(FIND_TV_PERFORMANCE_LEVEL,
                template);
        if (null != perfLevel && null != perfLevel.getDestScoreValue())
            return perfLevel.getDestScoreValue();
        else
            return new BigDecimal("1");
    }
      // For Laslink Scoring
    public BigDecimal findLasLinkPerformanceLevel(final String frameworkCode, final BigDecimal sourceScoreValue,
            final String testLevel, final String contentArea, final String grade, final String testForm) {
        final ScoreLookupRecord template = new ScoreLookupRecord(null, null, null,
                sourceScoreValue, null);
        template.setTestLevel(testLevel);
        template.setContentArea(contentArea);
        template.setGrade(grade);
        template.setTestForm(testForm);
        template.setFrameworkCode(frameworkCode);

        final ScoreLookupRecord perfLevel = (ScoreLookupRecord) find(FIND_LL_PERFORMANCE_LEVEL,
                template);
        if (null != perfLevel && null != perfLevel.getDestScoreValue())
            return perfLevel.getDestScoreValue();
        else
            return new BigDecimal("1");
    }

    public BigDecimal findObjectivePValue(final Long itemSetId, final String testForm, final String contentArea, final String normGroup, final String grade, final String level) {
        final ScoreLookupRecord template = new ScoreLookupRecord(itemSetId, ScoreLookupCode.SUBTEST_NUMBER_CORRECT.getCode(),
                ScoreLookupCode.OBJECTIVE_P_VALUE.getCode(), new BigDecimal(0), null);
        template.setNormGroup(normGroup);
        template.setGrade(grade);
        template.setTestLevel(level);
        template.setTestForm(testForm);
        template.setContentArea(contentArea);

        List results = findMany(FIND_SCORE_BY_UNIQUE_KEY_AND_ITEM_SET, template);
        if (results==null || results.isEmpty()) {
            StringBuffer buf = new StringBuffer("Unable to find Objective PValue value for: ");
            buf.append("\n\tparams: " + itemSetId);
            buf.append(" | " + normGroup);
            buf.append(" | " + grade);
            buf.append(" | " + level);
            buf.append(" | " + testForm);
            buf.append(" | " + contentArea);
            buf.append(" | " + ScoreLookupCode.SUBTEST_NUMBER_CORRECT.getCode());
            buf.append(" | " + new BigDecimal(0));
            buf.append(" | " + ScoreLookupCode.OBJECTIVE_P_VALUE.getCode());
            buf.append("\n(continuing)\n");
            System.err.println(buf.toString());
            return null;
        }
        else
            return ((ScoreLookupRecord) results.get(0)).getDestScoreValue();
    }
    
    public BigDecimal findObjectivePValueForTerrab3(final Long itemSetId, final String testForm, final String contentArea, final String normGroup, final String grade, final String level) {
        final ScoreLookupRecord template = new ScoreLookupRecord(itemSetId, ScoreLookupCode.SCALED_SCORE.getCode(),
                ScoreLookupCode.OBJECTIVE_P_VALUE.getCode(), new BigDecimal(0), null);
        template.setNormGroup(normGroup);
        template.setGrade(grade);
        template.setTestLevel(level);
        template.setTestForm(testForm);
        template.setContentArea(contentArea);

        List results = findMany(FIND_SCORE_BY_UNIQUE_KEY_AND_ITEM_SET, template);
        if (results==null || results.isEmpty()) {
            StringBuffer buf = new StringBuffer("Unable to find Objective PValue value for: ");
            buf.append("\n\tparams: " + itemSetId);
            buf.append(" | " + normGroup);
            buf.append(" | " + grade);
            buf.append(" | " + level);
            buf.append(" | " + testForm);
            buf.append(" | " + contentArea);
            buf.append(" | " + ScoreLookupCode.SUBTEST_NUMBER_CORRECT.getCode());
            buf.append(" | " + new BigDecimal(0));
            buf.append(" | " + ScoreLookupCode.OBJECTIVE_P_VALUE.getCode());
            buf.append("\n(continuing)\n");
            System.err.println(buf.toString());
            return null;
        }
        else
            return ((ScoreLookupRecord) results.get(0)).getDestScoreValue();
    }
    
    public Integer findObjectiveHMR(final Long itemSetId, final String testForm, final String contentArea, 
    		final String normGroup, final String grade, final String level, final Integer productId) {
        final ScoreLookupRecord template = new ScoreLookupRecord(itemSetId, ScoreLookupCode.SCALED_SCORE.getCode(),
                ScoreLookupCode.HIGH_MODERATE_MASTERY.getCode(), new BigDecimal(0), null);
        template.setTestLevel(level);
        template.setTestForm(testForm);
        template.setContentArea(contentArea);

        List results = findMany(FIND_HIGH_MODERATE_MASTERY, template);
        if (results==null || results.isEmpty()) {
            StringBuffer buf = new StringBuffer("Unable to find Objective PValue value for: ");
            buf.append("\n\tparams: " + itemSetId);
            buf.append(" | " + normGroup);
            buf.append(" | " + grade);
            buf.append(" | " + level);
            buf.append(" | " + testForm);
            buf.append(" | " + contentArea);
            buf.append(" | " + ScoreLookupCode.SCALED_SCORE.getCode());
            buf.append(" | " + new BigDecimal(0));
            buf.append(" | " + ScoreLookupCode.HIGH_MODERATE_MASTERY.getCode());
            buf.append("\n(continuing)\n");
            System.err.println(buf.toString());
            return null;
        }
        else {
        	for (Iterator iterator = results.iterator(); iterator.hasNext();) {
				ScoreLookupRecord slr = (ScoreLookupRecord) iterator.next();
				if(slr != null && slr.getScoreLookupId() != null) {
					if(slr.getScoreLookupId().contains("CB") && productId == 3720) {
						return slr.getDestScoreValue().intValue();
					} else if(slr.getScoreLookupId().contains("SV") && productId == 3710) {
						return slr.getDestScoreValue().intValue();
					} else
						return slr.getDestScoreValue().intValue();
				} else {
					return null;
				}
				
			}
            return ((ScoreLookupRecord) results.get(0)).getDestScoreValue().intValue();
        }
    }
    
    public Integer findObjectiveLMR(final Long itemSetId, final String testForm, final String contentArea, 
    		final String normGroup, final String grade, final String level, final Integer productId) {
        final ScoreLookupRecord template = new ScoreLookupRecord(itemSetId, ScoreLookupCode.SCALED_SCORE.getCode(),
                ScoreLookupCode.LOW_MODERATE_MASTERY.getCode(), new BigDecimal(0), null);
        template.setNormGroup(normGroup);
        template.setGrade(grade);
        template.setTestLevel(level);
        template.setTestForm(testForm);
        template.setContentArea(contentArea);

        List results = findMany(FIND_LOW_MODERATE_MASTERY, template);
        if (results==null || results.isEmpty()) {
            StringBuffer buf = new StringBuffer("Unable to find Objective PValue value for: ");
            buf.append("\n\tparams: " + itemSetId);
            buf.append(" | " + normGroup);
            buf.append(" | " + grade);
            buf.append(" | " + level);
            buf.append(" | " + testForm);
            buf.append(" | " + contentArea);
            buf.append(" | " + ScoreLookupCode.SCALED_SCORE.getCode());
            buf.append(" | " + new BigDecimal(0));
            buf.append(" | " + ScoreLookupCode.LOW_MODERATE_MASTERY.getCode());
            buf.append("\n(continuing)\n");
            System.err.println(buf.toString());
            return null;
        }
        else {
        	for (Iterator iterator = results.iterator(); iterator.hasNext();) {
				ScoreLookupRecord slr = (ScoreLookupRecord) iterator.next();
				if(slr != null && slr.getScoreLookupId() != null) {
					if(slr.getScoreLookupId().contains("CB") && productId == 3720) {
						return slr.getDestScoreValue().intValue();
					} else if(slr.getScoreLookupId().contains("SV") && productId == 3710) {
						return slr.getDestScoreValue().intValue();
					} else
						return slr.getDestScoreValue().intValue();
				} else {
					return null;
				}
			}
            return ((ScoreLookupRecord) results.get(0)).getDestScoreValue().intValue();
        }
    }
}