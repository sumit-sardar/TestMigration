package com.ctb.testSessionInfo.data; 

import java.util.List;
import java.util.Iterator;

public class SubtestVO implements java.io.Serializable
{ 
    static final long serialVersionUID = 1L;

    private Integer id = null;
    private String sequence = null;
    private String subtestName = null;
    private String duration = null;
    private String testAccessCode = null;
    
    private String level = null;
    private boolean selected = true;
    private String sessionDefault = null;
    
    public SubtestVO() {
        this.id = new Integer(0);
        this.sequence = "";
        this.subtestName = "";
        this.duration = "";
        this.testAccessCode = "";
        this.sessionDefault = "T";        
        
        this.level = null;
        this.selected = false;
    }
    
    public SubtestVO(Integer id, String sequence, String subtestName, String duration, String testAccessCode, String sessionDefault) {
        this.id = id;
        this.sequence = sequence;
        this.subtestName = subtestName;
        this.duration = duration;
        this.testAccessCode = testAccessCode;
        this.sessionDefault = sessionDefault;
        
        this.level = null;
        this.selected = false;
    }
    
    public SubtestVO(Integer id, String sequence, String subtestName, String duration, String testAccessCode, String sessionDefault, String level, boolean selected) {
        this.id = id;
        this.sequence = sequence;
        this.subtestName = subtestName;
        this.duration = duration;
        this.testAccessCode = testAccessCode;
        this.sessionDefault = sessionDefault;
        
        this.level = level;
        this.selected = selected;
    }
    
    public SubtestVO(SubtestVO src) {
        this.id = src.getId();
        this.sequence = src.getSequence();
        this.subtestName = src.getSubtestName();
        this.duration = src.getDuration();
        this.testAccessCode = src.getTestAccessCode();
        this.sessionDefault = src.getSessionDefault();        
        
        this.level = src.getLevel();
        this.selected = src.getSelected();
    }
    
    public SubtestVO(SubtestVO src, String level, String sequence, boolean selected) {
        this.id = src.getId();
        this.subtestName = src.getSubtestName();
        this.duration = src.getDuration();
        this.testAccessCode = src.getTestAccessCode();
        this.sessionDefault = src.getSessionDefault(); 
        
        this.level = level;
        this.sequence = sequence;
        this.selected = selected;        
    }

    public Integer getId() {
        return this.id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getSequence() {
        return this.sequence;
    }
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }
    public String getSubtestName() {
        return this.subtestName;
    }
    public void setSubtestName(String subtestName) {
        this.subtestName = subtestName;
    }   
    
    public String getDuration() {
        return this.duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }   
    
    public String getTestAccessCode() {
        return this.testAccessCode.trim();
    }
    public void setTestAccessCode(String testAccessCode) {
        this.testAccessCode = testAccessCode;
    }   

    public String getLevel() {
        return this.level;
    }
    public void setLevel(String level) {
        this.level = level;
    }
    public boolean getSelected() {
        return this.selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
	public String getSessionDefault() {
		return sessionDefault;
	}
	public void setSessionDefault(String sessionDefault) {
		this.sessionDefault = sessionDefault;
	}

}