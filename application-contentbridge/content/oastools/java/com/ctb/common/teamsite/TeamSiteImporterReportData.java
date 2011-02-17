package com.ctb.common.teamsite;


import java.util.*;


/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Sep 29, 2003
 * Time: 2:26:16 PM
 * To change this template use Options | File Templates.
 */
public class TeamSiteImporterReportData {
    private String fileName;
    private long numOfItems;
    private String critieriaString;
    private List itemIDList;
    private Exception error;
    private boolean failed = false;

    public TeamSiteImporterReportData() {}

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setNumOfItems(long numOfItems) {
        this.numOfItems = numOfItems;
    }

    public void setCritieriaString(String critieriaString) {
        this.critieriaString = critieriaString;
    }

    public void setItemIDList(List itemIDList) {
        this.itemIDList = itemIDList;
    }

    public TeamSiteImporterReportData(String fileName, long numOfItems, String critieriaString, List itemIDList) {
        this.fileName = fileName;
        this.numOfItems = numOfItems;
        this.critieriaString = critieriaString;
        this.itemIDList = itemIDList;
    }

    public String getFileName() {
        return fileName;
    }

    public long getNumOfItems() {
        return numOfItems;
    }

    public String getCritieriaString() {
        return critieriaString;
    }

    public List getItemIDs() {
        return itemIDList;
    }

    public void setFailure(boolean failed) {
        this.failed = failed;
    }

    public void setError(Exception e) {
        this.error = e;
        this.failed = true;
    }

    public Exception getError() {
        return error;
    }

    public boolean failed() {
        return failed;
    }

    public String toString() {
        final String NEW_LINE = "\n";
        final String INDENT = "\t";
        final String INDENT_WITH_MARK = "\t- ";
        StringBuffer buffer = new StringBuffer();

        buffer.append("Item Partition Import Result for Criteria: "
                + critieriaString + NEW_LINE);
        buffer.append(INDENT + "Import failure? " + failed + NEW_LINE);
        buffer.append(INDENT + "Total Items: " + getNumOfItems() + NEW_LINE);
        buffer.append(INDENT + "Written to: " + getFileName() + NEW_LINE);
        return buffer.toString();
    }

}
