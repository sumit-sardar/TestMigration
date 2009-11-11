package com.ctb.lexington.db.data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;

public class StudentTestData {
    private ArrayList studentTestDetailList = new ArrayList();

    public boolean add(StudentTestDetails details) {
        return studentTestDetailList.add(details);
    }

    public StudentTestDetails get(int index) {
        return (StudentTestDetails) studentTestDetailList.get(index);
    }

    public StudentTestDetails getBySubtestName(String subtestName) {
        if (subtestName == null) {
            throw new IllegalArgumentException(
                    "Cant find a StudentTestDetails object for null name.");
        }

        for (Iterator iter = studentTestDetailList.iterator(); iter.hasNext();) {
            StudentTestDetails detail = (StudentTestDetails) iter.next();
            if (detail.getSubTestName().equals(subtestName)) {
                return detail;
            }
        }
        // this should never happen
        throw new IllegalStateException(
                "Couldnt find a StudentTestDetails object for subtestName: " + subtestName);
    }
    
    public StudentTestDetails getBySubtestId(Long subtestId) {
        if (subtestId == null) {
            throw new IllegalArgumentException(
                    "Cant find a StudentTestDetails object for null id.");
        }

        for (Iterator iter = studentTestDetailList.iterator(); iter.hasNext();) {
            StudentTestDetails detail = (StudentTestDetails) iter.next();
            if (detail.getSubtestId().equals(subtestId)) {
                return detail;
            }
        }
        // this should never happen
        throw new IllegalStateException(
                "Couldnt find a StudentTestDetails object for subtestID: " + subtestId);
    }
    
    public Timestamp getMaxCompletionTime(String timezone) {
        Timestamp max = null;
        
        for (Iterator iter = studentTestDetailList.iterator(); iter.hasNext();) {
            StudentTestDetails detail = (StudentTestDetails) iter.next();
            if(detail != null && detail.getSubtestCompletionTimestamp(timezone) != null) {
                if(max == null || detail.getSubtestCompletionTimestamp(timezone).after(max)) {
                    max = detail.getSubtestCompletionTimestamp(timezone);
                }
            }
        }
        
        return max;
    }

    public int size() {
        return studentTestDetailList.size();
    }

    public Iterator iterator() {
        return studentTestDetailList.iterator();
    }
}