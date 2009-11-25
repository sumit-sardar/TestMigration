package com.ctb.bean.content; 

import java.io.Serializable;

public class DeliverableUnitBean implements Serializable
{ 
    private ItemBean [] items;
    private String title;
    private String index;
    private String id;
    private String timeLimit;
    private String orderReferences;
    private String itemReferences;
    private String startingQuestionNumber;
    
    
    
    
    public ItemBean [] getItems() {
        return this.items;
    }
    
    public void setItems(ItemBean [] items) {
        this.items = items;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getTimeLimit() {
        return this.timeLimit;
    }
    
    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }
    
    public String getIndex() {
        return this.index;
    }
    
    public void setIndex(String index) {
        this.index = index;
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setId(String id_ ) {
        this.id = id_;
    }
    
    public void setOrderReferences(String orderReferences_ ) {
        this.orderReferences = orderReferences_;
    }
    
    
    
    public void setItemReferences(String itemReferences_ ) {
        this.itemReferences = itemReferences_;
    }
    
    public String getOrderReferences() {
        return this.orderReferences;
    }
    
    public String getItemReferences() {
        return this.itemReferences;
    }
    
    public void setStartingQuestionNumber(String startingQuestionNumber_ ) {
        this.startingQuestionNumber = startingQuestionNumber_;
    }
    
    public String getStartingQuestionNumber() {
        return this.startingQuestionNumber;
    }
} 
