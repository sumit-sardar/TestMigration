package com.ctb.bean.content; 

import java.io.Serializable;

public class AcknowledgementBean implements Serializable
{ 
    private String itemId;
    private String text;
    
    public String getItemId() {
        return this.itemId;
    }
    
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
    
    public String getText() {
        return this.text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
} 
