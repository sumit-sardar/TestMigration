package com.ctb.lexington.db.irsdata;
import com.ctb.lexington.db.record.Persistent;

/**
 * @author Rama_Rao
 *
 */
public class IrsAttr7DimData implements Persistent {

    private Long attr7Id;
    private String name;
    private String type;

  
    public Long getAttr7Id() {
        return attr7Id;
    }

    public void setAttr7Id(Long attr7Id) {
        this.attr7Id = attr7Id;
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
