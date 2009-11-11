package com.ctb.lexington.db.data;

import java.util.HashMap;
import java.util.Iterator;

import com.ctb.lexington.util.SafeHashMap;

public class StudentItemScoreData {
    private HashMap studentItemScoreDetailMap = new SafeHashMap(String.class, StudentItemScoreDetails.class);
    private Long studentTestHistoryId;

    public Long getStudentTestHistoryId() {
        return studentTestHistoryId;
    }

    public void setStudentTestHistoryId(final Long studentTestHistoryId) {
        this.studentTestHistoryId = studentTestHistoryId;
        final Iterator iter = iterator();
        while (iter.hasNext()) {
            ((StudentItemScoreDetails) iter.next()).setStudentTestHistoryId(studentTestHistoryId);
        }
    }

    public boolean contains(final String itemId) {
        return studentItemScoreDetailMap.containsKey(itemId);
    }

    public StudentItemScoreDetails get(final String itemId) {
        if (itemId != null) {
            if (!contains(itemId)) {
                studentItemScoreDetailMap.put(itemId, new StudentItemScoreDetails(itemId));
            }
            return (StudentItemScoreDetails) studentItemScoreDetailMap.get(itemId);
        } else {
            return null;
        }
    }
    
    public void markItemsNonValidForSubtest(final Long subtestId) {
        StudentItemScoreDetails[] details = getStudentItemScoreDetails();
        for(int i=0;i<details.length;i++) {
            Long itemSubtestId = details[i].getTestItemSetId();
            if(subtestId.equals(itemSubtestId)) {
                details[i].setAtsArchive("F");
            }
        }
    }
    
    public void markItemsValidForSubtest(final Long subtestId) {
        StudentItemScoreDetails[] details = getStudentItemScoreDetails();
        for(int i=0;i<details.length;i++) {
            Long itemSubtestId = details[i].getTestItemSetId();
            if(subtestId.equals(itemSubtestId)) {
                details[i].setAtsArchive("T");
            }
        }
    }
    
    public StudentItemScoreDetails[] getStudentItemScoreDetails() {
        return (StudentItemScoreDetails []) studentItemScoreDetailMap.values().toArray(new StudentItemScoreDetails[0]);
    }

    public int size() {
        return studentItemScoreDetailMap.values().size();
    }

    public Iterator iterator() {
        return studentItemScoreDetailMap.values().iterator();
    }
}