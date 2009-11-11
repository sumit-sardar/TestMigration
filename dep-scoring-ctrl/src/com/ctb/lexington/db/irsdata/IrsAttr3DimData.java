package com.ctb.lexington.db.irsdata;
import com.ctb.lexington.db.record.Persistent;

/**
 * @author Rama_Rao
 *
 */
public class IrsAttr3DimData implements Persistent {

    private Long attr3Id;
    private String name;
    private String type;

  
    public Long getAttr3Id() {
        return attr3Id;
    }

    public void setAttr3Id(Long attr3Id) {
        this.attr3Id = attr3Id;
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
