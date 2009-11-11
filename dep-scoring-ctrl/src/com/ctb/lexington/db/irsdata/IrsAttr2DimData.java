package com.ctb.lexington.db.irsdata;
import com.ctb.lexington.db.record.Persistent;
/**
 * @author Rama_Rao
 *
 */
public class IrsAttr2DimData implements Persistent{

    private Long attr2Id;
    private String name;
    private String type;

    public Long getAttr2Id() {
        return attr2Id;
    }

    public void setAttr2Id(Long attr2Id) {
        this.attr2Id = attr2Id;
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
