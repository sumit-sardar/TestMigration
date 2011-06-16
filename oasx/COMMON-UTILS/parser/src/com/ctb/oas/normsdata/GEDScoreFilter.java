package com.ctb.oas.normsdata;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class GEDScoreFilter implements TargetScoreFilter {
    public Object filterScore(Object targetScore) {
        String scoreString = null;
        if (targetScore instanceof String)
            scoreString = (String) targetScore;
        else
            scoreString = String.valueOf(targetScore);

        Float result = null;

        if (targetScore == null)
            return null;

        if (scoreString.indexOf('+') != -1)
            scoreString = stripPlusSign(scoreString);

        if (scoreString.indexOf(".") != -1)
            result = Float.valueOf(scoreString);
        else {
            float value = Float.parseFloat(scoreString);
            result = new Float(value / 10);
        }

        return result;
    }

    private String stripPlusSign(String scoreString) {
        return scoreString = scoreString.trim().substring(0, scoreString.length() - 1);
    }
}
