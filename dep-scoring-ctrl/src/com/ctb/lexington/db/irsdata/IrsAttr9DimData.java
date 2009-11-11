package com.ctb.lexington.db.irsdata;
import com.ctb.lexington.db.record.Persistent;

/**
 * @author Rama_Rao
 *
 */
public class IrsAttr9DimData implements Persistent {

    private Long attr9Id;
    private String name;
    private String type;

  
    public Long getAttr9Id() {
        return attr9Id;
    }

    public void setAttr9Id(Long attr9Id) {
        this.attr9Id = attr9Id;
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
