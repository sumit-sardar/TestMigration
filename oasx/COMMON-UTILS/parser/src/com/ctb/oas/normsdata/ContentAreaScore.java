package com.ctb.oas.normsdata;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class ContentAreaScore {
    private String contentArea;
    private int sourceScoreStartIndex;
    private int sourceScoreEndIndex;
    private List targetScores = new ArrayList();

    public ContentAreaScore(String contentArea) {
        this.contentArea = contentArea;
    }

    public String getContentAreaString() {
        return contentArea;
    }

    public void setContentArea(String contentArea) {
        this.contentArea = contentArea;
    }

    public int getSourceScoreEndIndex() {
        return sourceScoreEndIndex;
    }

    public void setSourceScoreEnd(int sourceScoreEndIndex) {
        this.sourceScoreEndIndex = sourceScoreEndIndex;
    }

    public int getSourceScoreStartIndex() {
        return sourceScoreStartIndex;
    }

    public void setSourceScoreStart(int sourceScoreStartIndex) {
        this.sourceScoreStartIndex = sourceScoreStartIndex;
    }

    public List getTargetScores() {
        return targetScores;
    }

    public void setTargetScores(List targetScores) {
        this.targetScores = targetScores;
    }
}

