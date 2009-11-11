package com.ctb.lexington.db.data;

import java.util.Iterator;
import java.util.Map;

import com.ctb.lexington.util.SafeHashMap;

public class StsTotalStudentScoreData {
    private Map stsTotalStudentScoreDetails = new SafeHashMap(String.class, StsTotalStudentScoreDetail.class);

    private void put(final StsTotalStudentScoreDetail detail) {
        stsTotalStudentScoreDetails.put(detail.getType(), detail);
    }

    public StsTotalStudentScoreDetail get(final String type) {
        if (!stsTotalStudentScoreDetails.containsKey(type)) {
            final StsTotalStudentScoreDetail result = new StsTotalStudentScoreDetail();
            result.setType(type);
            put(result);
        }
        return (StsTotalStudentScoreDetail) stsTotalStudentScoreDetails.get(type);
    }

    public Iterator iterator() {
        return stsTotalStudentScoreDetails.values().iterator();
    }

    public int size() {
        return stsTotalStudentScoreDetails.size();
    }
}