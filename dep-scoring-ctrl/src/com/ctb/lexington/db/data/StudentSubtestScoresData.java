package com.ctb.lexington.db.data;

import java.util.Iterator;
import java.util.Map;

import com.ctb.lexington.util.SafeHashMap;

public class StudentSubtestScoresData {
    private Map studentSubtestScoresDetails = new SafeHashMap(Long.class, StudentSubtestScoresDetails.class);
    private Long studentTestHistoryId;

    public Long getStudentTestHistoryId() {
        return studentTestHistoryId;
    }

    public void setStudentTestHistoryId(final Long studentTestHistoryId) {
        this.studentTestHistoryId = studentTestHistoryId;
        Iterator iter = iterator();
        while (iter.hasNext()) {
            ((StudentSubtestScoresDetails) iter.next())
                    .setStudentTestHistoryId(studentTestHistoryId);
        }
    }

    public StudentSubtestScoresDetails get(final Long itemSetId) {
        if (!studentSubtestScoresDetails.containsKey(itemSetId)) {
            final StudentSubtestScoresDetails detail = new StudentSubtestScoresDetails();
            detail.setItemSetId(itemSetId);
            studentSubtestScoresDetails.put(itemSetId, detail);
        }
        return (StudentSubtestScoresDetails) studentSubtestScoresDetails.get(itemSetId);
    }

    public Iterator iterator() {
        return studentSubtestScoresDetails.values().iterator();
    }

    public int size() {
        return studentSubtestScoresDetails.values().size();
    }
}