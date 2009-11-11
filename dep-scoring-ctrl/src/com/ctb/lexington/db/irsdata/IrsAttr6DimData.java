package com.ctb.lexington.db.irsdata;
import com.ctb.lexington.db.record.Persistent;

/**
 * @author Rama_Rao
 *
 */
public class IrsAttr6DimData implements Persistent {

    private Long attr6Id;
    private String name;
    private String type;

  
    public Long getAttr6Id() {
        return attr6Id;
    }

    public void setAttr6Id(Long attr6Id) {
        this.attr6Id = attr6Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
