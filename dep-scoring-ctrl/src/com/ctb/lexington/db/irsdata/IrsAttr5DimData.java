package com.ctb.lexington.db.irsdata;
import com.ctb.lexington.db.record.Persistent;

/**
 * @author Rama_Rao
 *
 */
public class IrsAttr5DimData implements Persistent {

    private Long attr5Id;
    private String name;
    private String type;

  
    public Long getAttr5Id() {
        return attr5Id;
    }

    public void setAttr5Id(Long attr5Id) {
        this.attr5Id = attr5Id;
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
