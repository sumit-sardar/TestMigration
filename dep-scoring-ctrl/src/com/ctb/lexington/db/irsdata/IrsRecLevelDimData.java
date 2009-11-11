package com.ctb.lexington.db.irsdata;
import com.ctb.lexington.db.record.Persistent;
/**
 * @author Rama_Rao
 *
 */
public class IrsRecLevelDimData implements Persistent{

    private Long recLevelid;
    private String name;
    
    public Long getRecLevelid() {
        return recLevelid;
    }
    public void setRecLevelid(Long recLevelid) {
        this.recLevelid = recLevelid;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
