package com.ctb.bean.content; 

import java.io.Serializable;

public class TestBean implements Serializable
{ 
    private String title;
    private String index;
    
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getIndex() {
        return this.index;
    }
    
    public void setIndex(String index) {
        this.index = index;
    }
} 
