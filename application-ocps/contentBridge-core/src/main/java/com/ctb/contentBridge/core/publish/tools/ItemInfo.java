package com.ctb.contentBridge.core.publish.tools;

/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Nov 20, 2003
 * Time: 9:54:08 AM
 * To change this template use Options | File Templates.
 */
public class ItemInfo {
    private String itemID;
    private String displayID;
    private String activationStatus;
    private String invisibleStatus;
    private String objectiveID;
    private String correctAnswer;

    public String getObjectiveID() {
        return objectiveID;
    }

    public String getItemID() {
        return itemID;
    }

    public String getDisplayID() {
        return displayID;
    }

    public String getActivationStatus() {
        return activationStatus;
    }

    public String getInvisibleStatus() {
        return invisibleStatus;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public ItemInfo(String itemID, String displayID, String activationStatus,
                    String invisibleStatus, String objectiveID, String correctAnswer) {
        this.itemID = itemID;
        this.displayID = displayID;
        this.activationStatus = activationStatus;
        this.invisibleStatus = invisibleStatus;
        this.objectiveID = objectiveID;
        this.correctAnswer = correctAnswer;
        if (displayID == null)
            this.displayID = itemID;


    }

}
