package com.ctb.lexington.db.data;

import java.util.Map;

import com.ctb.lexington.util.CTBConstants;
import com.ctb.lexington.util.SafeHashMap;

public class StsTestResultFactData {
    private Map testResultFactDetails = new SafeHashMap(String.class,
            StsTestResultFactDetails.class);

    public StsTestResultFactDetails get(String contentAreaName) {
        if (!testResultFactDetails.containsKey(contentAreaName)) {
            final StsTestResultFactDetails detail = new StsTestResultFactDetails();
            detail.setContentAreaName(contentAreaName);
            detail.setValidScore(CTBConstants.INVALID_SCORE);
            testResultFactDetails.put(contentAreaName, detail);
        }
        return (StsTestResultFactDetails) testResultFactDetails.get(contentAreaName);
    }

    public int size() {
        return testResultFactDetails.size();
    }
}