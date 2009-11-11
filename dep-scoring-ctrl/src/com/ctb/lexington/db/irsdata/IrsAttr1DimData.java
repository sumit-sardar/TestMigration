package com.ctb.lexington.db.irsdata;
import com.ctb.lexington.db.record.Persistent;

/**
 * @author Rama_Rao
 *
 */
public class IrsAttr1DimData implements Persistent{

    private Long attr1Id;
    private String name;
    private String type;

    public Long getAttr1Id() {
        return attr1Id;
    }

    public void setAttr1Id(Long attr1Id) {
        this.attr1Id = attr1Id;
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