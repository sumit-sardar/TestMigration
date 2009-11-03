package com.ctb.bean.content; 

import java.io.Serializable;

public class SchedulableUnitBean  implements Serializable
{
    private DeliverableUnitBean [] deliverableUnits;
    private String title;
    
    public DeliverableUnitBean [] getDeliverableUnits() {
        return this.deliverableUnits;
    }
    
    public void setDeliverableUnits(DeliverableUnitBean [] deliverableUnits) {
        this.deliverableUnits = deliverableUnits;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
} 
