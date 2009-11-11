package com.ctb.lexington.db.irsdata;
import com.ctb.lexington.db.record.Persistent;

/**
 * @author Rama_Rao
 *
 */
public class IrsAttr4DimData implements Persistent {

    private Long attr4Id;
    private String name;
    private String type;

  
    public Long getAttr4Id() {
        return attr4Id;
    }

    public void setAttr4Id(Long attr4Id) {
        this.attr4Id = attr4Id;
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
