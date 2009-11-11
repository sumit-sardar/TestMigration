package com.ctb.lexington.db.irsdata;

import com.ctb.lexington.db.record.Persistent;

/**
 * @author Rama_Rao
 *
 */
public class IrsScoreTypeDimData implements Persistent{
    private Long scoreTypeid;
    private String name;

    public Long getScoreTypeid() {
        return scoreTypeid;
    }

    public void setScoreTypeid(Long scoreTypeid) {
        this.scoreTypeid = scoreTypeid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
