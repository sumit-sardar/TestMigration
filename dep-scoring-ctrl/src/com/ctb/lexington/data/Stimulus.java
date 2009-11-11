package com.ctb.lexington.data;

/*
 * Stimulus.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 */

import java.io.Serializable;
import java.util.Date;

public class Stimulus implements Serializable {
    private String id = null;
    private String name = null;
    private String typeName = null;
    private String typeCode = null;
    private String xml = null;
    private String createdBy = null;
    private Date createdDate = null;
    private Date updatedDate = null;
    
    public String getId() {
        return id;
    }
    public void setId(String id_) {
        this.id = id_;
    }
    public String getName() {
        return name;
    }
    public void setName(String name_) {
        this.name = name_;
    }
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName_) {
        this.typeName = typeName_;
    }
    public String getTypeCode() {
        return typeCode;
    }
    public void setTypeCode(String typeCode_) {
        this.typeCode = typeCode_;
    }
    public String getXML() {
        return xml;
    }
    public void setXML(String xml_) {
        this.xml = xml_;
    }
    public String getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(String createdBy_) {
        this.createdBy = createdBy_;
    }
    public Date getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Date createdDate_) {
        this.createdDate = createdDate_;
    }
    public Date getUpdatedDate() {
        return updatedDate;
    }
    public void setUpdatedDate(Date updatedDate_) {
        this.updatedDate = updatedDate_;
    }
}
