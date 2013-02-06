package com.ctb.contentBridge.core.publish.report;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class ItemProcessorReport extends AbstractItemReport {

    private static ThreadLocal _current = new ThreadLocal();


    public static ItemProcessorReport getCurrentReport() {
        return (ItemProcessorReport) _current.get();
    }
    public static void setCurrentReport(ItemProcessorReport report) {
        _current.set(report);
    }

    private String newID;
    private String operation;
    private String answerKey;
    private String activationStatus;
    private String invisibleStatus;
    private String frameworkCode;
    private String warning;
    private List objectives;
    private boolean showHierarchy = false;
    public String getNewID() {
        return newID;
    }
    public String getOperation() {
        return operation;
    }
	public String getActivationStatus() {
		return activationStatus;
	}
	public String getAnswerKey() {
		return answerKey;
	}
	public String getFrameworkCode() {
		return frameworkCode;
	}
	public String getWarning() {
		return warning;
	}
	
	public String getInvisibleStatus() {
		return invisibleStatus;
	}

    public void setNewID(String newID) {
        this.newID = newID;
    }
    public void setOperation(String operation) {
        this.operation = operation;
    }
    public void setActivationStatus(String activationStatus) {
        this.activationStatus = activationStatus;
    }
    public void setWarning(String warning_) {
        this.warning = warning_;
    }
    public void setAnswerKey(String answerKey) {
        this.answerKey = answerKey;
    }
	public void setFrameworkCode(String string) {
		frameworkCode = string;
	}
    public void setInvisibleStatus(String invisibleStatus) {
        this.invisibleStatus = invisibleStatus;
    }

    public Map getItemMappings() {
        Map result = new HashMap();
        result.put(this.getID(), this.getNewID());
        return result;
    }

    public void setObjectives(List objectives) {
        this.objectives = objectives;
    }

    public List getObjectives() {
        return objectives;
    }

    public boolean showHierarchy() {
        return showHierarchy;
    }

    public void setShowHierarchy(boolean showHierarchy) {
        this.showHierarchy = showHierarchy;
    }
}
