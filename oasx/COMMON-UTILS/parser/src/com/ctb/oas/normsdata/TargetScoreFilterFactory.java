package com.ctb.oas.normsdata;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class TargetScoreFilterFactory {
    private static Map map = new HashMap();
    private static final String DEFAULT_FILTER = "default";

    private static final String GED_FILTER = "float";

    static {
        map.put(DEFAULT_FILTER, new DefaultTargetScoreFilter());
        map.put(GED_FILTER, new GEDScoreFilter());
    }

    public static TargetScoreFilter getFilter(ScoreType targetScoreType) {
        if (targetScoreType.equals(ScoreType.GRADE_EQUIVALENT) || targetScoreType.equals(ScoreType.EXTENDED_GRADE_EQUIVALENT)
                || targetScoreType.equals(ScoreType.MEAN_GRADE_EQUIVALENT))
            return (TargetScoreFilter) map.get(GED_FILTER);

        return (TargetScoreFilter) map.get(DEFAULT_FILTER);
    }
}
