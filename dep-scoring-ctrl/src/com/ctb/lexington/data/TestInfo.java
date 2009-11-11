package com.ctb.lexington.data;

import java.util.List;

public class TestInfo implements java.io.Serializable, java.lang.Cloneable {

    private Integer id = null;
    private String name = null;
    private String level = null;
    private List subtests = null;
    private List durations = null;
    
    public TestInfo() { 
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List getSubtests() {
        return subtests;
    }
    public void setSubtests(List subtests) {
        this.subtests = subtests;
    }
    public List getDurations() {
        return durations;
    }
    public void setDurations(List durations) {
        this.durations = durations;
    }
    public Object clone() {
        try {
            return super.clone();
        }
        catch(CloneNotSupportedException e) {
            return null;
        }
    }
    public boolean equals(Object obj) {
        if (obj instanceof TestInfo) {
            TestInfo o2 = (TestInfo) obj;
            if (o2.getId().equals(this.id)) {
                return true;
            }
        }
        return false;
    }
    
}
