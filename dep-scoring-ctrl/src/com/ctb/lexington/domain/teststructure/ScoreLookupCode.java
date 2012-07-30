package com.ctb.lexington.domain.teststructure;

import java.util.HashMap;

import com.ctb.lexington.util.SafeHashMap;

/**
 * Enumeration of score lookup codes.
 * 
 * @author Vijay Aravamudhan
 * @version $Id$
 */
public final class ScoreLookupCode extends StringConstant {
    private static final HashMap ALL_STATUSES = new SafeHashMap(String.class, ScoreLookupCode.class);

    public static final ScoreLookupCode SUBTEST_NUMBER_CORRECT = new ScoreLookupCode("NSC", "Subtest Number Correct");
    public static final ScoreLookupCode SUBTEST_RAW_SCORE = new ScoreLookupCode("SRW", "Sub-test Raw Score");
    public static final ScoreLookupCode SCALED_SCORE = new ScoreLookupCode("SCL", "Scaled Score");
    public static final ScoreLookupCode NORMAL_CURVE_EQUIVALENT = new ScoreLookupCode("NCE", "Normal Curve Equivalent");
    public static final ScoreLookupCode NATIONAL_PERCENTILE = new ScoreLookupCode("NP", "National Percentile");
    public static final ScoreLookupCode HIGH_NATIONAL_PERCENTILE = new ScoreLookupCode("NPH", "High National Percentile");
    public static final ScoreLookupCode LOW_NATIONAL_PERCENTILE = new ScoreLookupCode("NPL", "Low National Percentile");
    public static final ScoreLookupCode NATIONAL_STANINE = new ScoreLookupCode("NS", "National Stanine");
    public static final ScoreLookupCode GRADE_EQUIVALENT = new ScoreLookupCode("GE", "Grade Equivalent");
    public static final ScoreLookupCode PERFORMANCE_LEVEL = new ScoreLookupCode("PL", "Performance Level");
    public static final ScoreLookupCode OBJECTIVE_P_VALUE = new ScoreLookupCode("OPV", "Objective P Value");
    public static final ScoreLookupCode STANDARD_ERROR_MEASUREMENT = new ScoreLookupCode("SEM", "STANDARD ERROR OF MEASUREMENT");
    public static final ScoreLookupCode GRADE_MEAN_EQUIVALENT = new ScoreLookupCode("GME", "GRADE MEAN EQUIVALENT");
    
    public static final ScoreLookupCode HIGH_MODERATE_MASTERY = new ScoreLookupCode("HMR", "HIGH MODERATE MASTERY");
    public static final ScoreLookupCode LOW_MODERATE_MASTERY = new ScoreLookupCode("LMR", "LOW MODERATE MASTERY");

    public static final ScoreLookupCode EXPECTED_GED_READING = new ScoreLookupCode("EGEDRD", "EXPECTED GED FOR SUBJECT READING");
    public static final ScoreLookupCode EXPECTED_GED_MATH = new ScoreLookupCode("EGEDMA", "EXPECTED GED FOR SUBJECT MATHEMATICS");
    public static final ScoreLookupCode EXPECTED_GED_WRITING = new ScoreLookupCode("EGEDWR", "EXPECTED GED FOR SUBJECT WRITING");
    public static final ScoreLookupCode EXPECTED_GED_SOCIAL = new ScoreLookupCode("EGEDSO", "EXPECTED GED FOR SUBJECT SOCIAL STUDIES");
    public static final ScoreLookupCode EXPECTED_GED_SCIENCE = new ScoreLookupCode("EGEDSC", "EXPECTED GED FOR SUBJECT SCIENCE");
    public static final ScoreLookupCode EXPECTED_GED_AVERAGE = new ScoreLookupCode("EGEDAV", "EXPECTED GED FOR SUBJECT AVERAGE");

    private ScoreLookupCode(final String code, final String description) {
        super(code, description);
        ALL_STATUSES.put(code, this);
    }

    public static ScoreLookupCode getByCode(final String code) {
        if (!ALL_STATUSES.containsKey(code)) {
            throw new IllegalArgumentException("No ScoreLookupCode found for: " + code);
        }
        return (ScoreLookupCode) ALL_STATUSES.get(code);
    }
}