package com.ctb.lexington.db.record;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ctb.lexington.db.data.StudentTestHistoryData;

public class ScoringResult {
    private StudentTestHistoryData studentTestHistory;
    private ArrayList itemScoreList = new ArrayList();
    private ArrayList scoreSummaryList = new ArrayList();
    private ArrayList subtestScoreList = new ArrayList();

    public List getItemScoreList() {
        return itemScoreList;
    }

    public void setItemScoreList(final ArrayList itemScoreList) {
        this.itemScoreList = itemScoreList;
    }

    public List getScoreSummaryList() {
        return scoreSummaryList;
    }

    public void setScoreSummaryList(final ArrayList scoreSummaryList) {
        this.scoreSummaryList = scoreSummaryList;
    }

    public StudentTestHistoryData getStudentTestHistory() {
        return studentTestHistory;
    }

    public void setStudentTestHistory(final StudentTestHistoryData studentTestHistory) {
        this.studentTestHistory = studentTestHistory;
    }

    public List getSubtestScoreList() {
        return subtestScoreList;
    }

    public void setSubtestScoreList(final ArrayList subtestScoreList) {
        this.subtestScoreList = subtestScoreList;
    }

    /**
     * Sort the item scoring list for testing.
     */
    public void sortItemScoreList() {
        Collections.sort(itemScoreList);
    }
}