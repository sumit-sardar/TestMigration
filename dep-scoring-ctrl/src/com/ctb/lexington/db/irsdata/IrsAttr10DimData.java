package com.ctb.lexington.db.irsdata;
import com.ctb.lexington.db.record.Persistent;

/**
 * @author Rama_Rao
 *
 */
public class IrsAttr10DimData implements Persistent {

    private Long attr10Id;
    private String name;
    private String type;

  
    public Long getAttr10Id() {
        return attr10Id;
    }

    public void setAttr10Id(Long attr10Id) {
        this.attr10Id = attr10Id;
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
