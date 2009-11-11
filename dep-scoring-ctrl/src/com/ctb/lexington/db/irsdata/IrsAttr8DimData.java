package com.ctb.lexington.db.irsdata;
import com.ctb.lexington.db.record.Persistent;

/**
 * @author Rama_Rao
 *
 */
public class IrsAttr8DimData implements Persistent {

    private Long attr8Id;
    private String name;
    private String type;

  
    public Long getAttr8Id() {
        return attr8Id;
    }

    public void setAttr8Id(Long attr8Id) {
        this.attr8Id = attr8Id;
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
