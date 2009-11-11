/**
 * 
 */
package com.ctb.lexington.db.irsdata;

/**
 * @author Rama_Rao
 *
 */
import com.ctb.lexington.db.record.Persistent;

/**
 * @author Rama_Rao
 *
 */
public class IrsMasteryLevelDimData implements Persistent{
	
	private Long masteryLevelid;
    private String name;

    public Long getMasteryLevelid() {
        return masteryLevelid;
    }

    public void setMasteryLevelid(Long masteryLevelid) {
        this.masteryLevelid = masteryLevelid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
