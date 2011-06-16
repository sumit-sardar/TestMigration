package com.ctb.oas.normsdata;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class InconstentScoreException extends RuntimeException {
    private int expectedScore;
    private int actualScore;
    private String contentArea;
    private String scorer;

    public InconstentScoreException(int actualScore, String contentArea, int expectedScore, String scorer) {
        this.actualScore = actualScore;
        this.contentArea = contentArea;
        this.expectedScore = expectedScore;
        this.scorer = scorer;
    }

    public String getMessage() {
        return ("TerraNovaScorer :" + scorer + ", ContentArea :" + contentArea + ", Expected :" + expectedScore + ", Actual :" + actualScore);
    }
}
