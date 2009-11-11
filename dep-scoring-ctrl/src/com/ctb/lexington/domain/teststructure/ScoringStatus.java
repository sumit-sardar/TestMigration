package com.ctb.lexington.domain.teststructure;

import java.util.HashMap;

import com.ctb.lexington.util.SafeHashMap;

/**
 * Enumeration of scoring statuses
 * 
 * @author Vijay Aravamudhan
 * @version $Id$
 */
public class ScoringStatus extends StringConstant {
    private static final HashMap ALL_STATUSES = new SafeHashMap(String.class, ScoringStatus.class);

    public static final ScoringStatus NOT_SCORED = new ScoringStatus("NA", "Not Scored");
    public static final ScoringStatus SCORING = new ScoringStatus("SC", "Scoring");
    public static final ScoringStatus SCORED = new ScoringStatus("CO", "Scored");
    public static final ScoringStatus IN_PROGRESS = new ScoringStatus("IP", "Calculating Scores");
    public static final ScoringStatus PARTIALLY_SCORED = new ScoringStatus("PC", "Partially Scored");
    public static final ScoringStatus SCORING_PROBLEM = new ScoringStatus("ER", "Scoring Problem");

    private ScoringStatus(final String code, final String description) {
        super(code, description);
        ALL_STATUSES.put(code, this);
    }

    public static ScoringStatus getByCode(final String code) {
        if (!ALL_STATUSES.containsKey(code)) {
            throw new IllegalArgumentException("No ScoringStatus found for: " + code);
        }
        return (ScoringStatus) ALL_STATUSES.get(code);
    }
}