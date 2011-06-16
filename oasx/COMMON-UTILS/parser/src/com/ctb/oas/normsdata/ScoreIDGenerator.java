package com.ctb.oas.normsdata;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public interface ScoreIDGenerator {
    public static final ScoreIDGenerator INSTANCE = new DefaultScoreIDGenerator();

    public String generateId(NormsData data, String contentAreaName);

}
