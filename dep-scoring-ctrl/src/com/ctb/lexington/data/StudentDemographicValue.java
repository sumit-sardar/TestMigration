package com.ctb.lexington.data;

import java.io.Serializable;

/**
 *
 * @author  Tai Truong
 */
public class StudentDemographicValue implements Serializable
{
    private Integer id = null;
    private String value = null;
    private boolean selected = false;
    
    public StudentDemographicValue() {
        id = new Integer(0);
        value = "";
        selected = false;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
}
