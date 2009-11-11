package com.ctb.lexington.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author  Tai Truong
 */
public class StudentDemographicData implements Serializable
{
    private Integer id = null;
    private String dataName = null;
    private boolean multipleAllowed = false;
    private List values = null;
    
    public StudentDemographicData() {
	    multipleAllowed = false;
	    values = new ArrayList();
    }
    public String getDataName() {
        return dataName;
    }
    public void setDataName(String dataName) {
        this.dataName = dataName;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public boolean isMultipleAllowed() {
        return multipleAllowed;
    }
    public void setMultipleAllowed(boolean multipleAllowed) {
        this.multipleAllowed = multipleAllowed;
    }
    public List getValues() {
        return values;
    }
    public void setValues(List values) {
        this.values = values;
    }
}
