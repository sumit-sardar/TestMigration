package com.ctb.reporting;

public abstract class AbstractItemReport extends AbstractXMLElementReport {
    private String id;

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

}
