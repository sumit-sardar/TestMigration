package com.ctb.contentBridge.core.publish.tools;

/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Nov 21, 2003
 * Time: 8:38:58 AM
 * To change this template use Options | File Templates.
 */
public class ObjectiveInfo {

    private long itemSetID;
    private String curriculumID;
    private long frameworkProductID;
    private String activationStatus;


    public ObjectiveInfo(long itemSetID, String curriculumID, long frameworkProductID, String activationStatus) {
        this.itemSetID = itemSetID;
        this.curriculumID = curriculumID;
        this.frameworkProductID = frameworkProductID;
        this.activationStatus = activationStatus;

    }

    public long getItemSetID() {
        return itemSetID;
    }

    public String getCurriculumID() {
        return curriculumID;
    }

    public long getFrameworkProductID() {
        return frameworkProductID;
    }

    public String getActivationStatus() {
        return activationStatus;
    }
}
