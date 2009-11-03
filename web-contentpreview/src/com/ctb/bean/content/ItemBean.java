package com.ctb.bean.content; 

import java.io.Serializable;
import org.jdom.Element;

public class ItemBean implements Serializable
{ 
    private String id;
    private int index;
    private String itemType;
    private AnswerChoiceBean [] answerChoices;
    private String lml;
    private Element xml;
    
    public String getLml() {
        return this.lml;
    }
    
    public void setLml(String lml) {
        this.lml= lml;
    }
    
    public Element getXml() {
        return this.xml;
    }
    
    public void setXml( Element xml) {
        this.xml= xml;
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }
    
    public AnswerChoiceBean [] getAnswerChoices() {
        return this.answerChoices;
    }
    
    public void setAnswerChoices(AnswerChoiceBean [] answerChoices) {
        this.answerChoices = answerChoices;
    }
    
    public String getItemType() {
        return this.itemType;
    }
    
    public void setItemType(String itemType) {
        this.itemType= itemType;
    }
    
} 
