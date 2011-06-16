package com.ctb.oas.normsdata;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class DefaultTargetScoreFilter implements TargetScoreFilter {
    public Object filterScore(Object targetScore) {
        if (targetScore instanceof String) {
            return Integer.valueOf(((String) targetScore).trim());
        }

        return targetScore;
    }
}
