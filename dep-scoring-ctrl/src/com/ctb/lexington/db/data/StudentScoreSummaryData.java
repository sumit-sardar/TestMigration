package com.ctb.lexington.db.data;

import java.util.HashMap;
import java.util.Iterator;

import com.ctb.lexington.util.SafeHashMap;

public class StudentScoreSummaryData {
    private HashMap studentScoreSummaryDetailMap = new SafeHashMap(Long.class, StudentScoreSummaryDetails.class);
    private Long studentTestHistoryId;

    public Long getStudentTestHistoryId() {
        return studentTestHistoryId;
    }

    public void markObjectivesNonValidForSubtest(final Long subtestId) {
        StudentScoreSummaryDetails[] details = getStudentScoreSummaryDetails();
        for(int i=0;i<details.length;i++) {
            Long objSubtestId = details[i].getSubtestId();
            if(subtestId.equals(objSubtestId)) {
                details[i].setAtsArchive("F");
            }
        }
    }
    
    public void markObjectivesValidForSubtest(final Long subtestId) {
        StudentScoreSummaryDetails[] details = getStudentScoreSummaryDetails();
        for(int i=0;i<details.length;i++) {
            Long objSubtestId = details[i].getSubtestId();
            if(subtestId.equals(objSubtestId)) {
                details[i].setAtsArchive("T");
            }
        }
    }

    public void setStudentTestHistoryId(final Long studentTestHistoryId) {
        this.studentTestHistoryId = studentTestHistoryId;
        Iterator iter = studentScoreSummaryDetailMap.values().iterator();
        while (iter.hasNext()) {
            ((StudentScoreSummaryDetails) iter.next())
                    .setStudentTestHistoryId(studentTestHistoryId);
        }
    }

    public StudentScoreSummaryDetails get(final Long itemSetId) {
        if (!studentScoreSummaryDetailMap.containsKey(itemSetId)) {
            final StudentScoreSummaryDetails detail = new StudentScoreSummaryDetails();
            detail.setReportItemSetId(itemSetId);
            detail.setAtsArchive("T");
            studentScoreSummaryDetailMap.put(itemSetId, detail);
        }
        return (StudentScoreSummaryDetails) studentScoreSummaryDetailMap.get(itemSetId);
    }

    public int size() {
        return studentScoreSummaryDetailMap.values().size();
    }

    public Iterator iterator() {
        return studentScoreSummaryDetailMap.values().iterator();
    }
    
    public StudentScoreSummaryDetails[] getStudentScoreSummaryDetails() {
        return (StudentScoreSummaryDetails []) studentScoreSummaryDetailMap.values().toArray(new StudentScoreSummaryDetails[0]);
    }
}