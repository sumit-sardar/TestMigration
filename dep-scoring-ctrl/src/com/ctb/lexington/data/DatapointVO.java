package com.ctb.lexington.data;

// java imports
import java.io.Serializable;
import java.util.Date;

import com.ctb.lexington.db.record.Persistent;

/**
 * <p>
 * DatapointVO
 * </p>
 * <p>
 * Copyright CTB/McGraw-Hill, 2003
 * CONFIDENTIAL
 * </p>
 *
 * @author  <a href="mailto:dvu@ctb.com">Dat Vu</a>
 */

public class DatapointVO implements Persistent, Serializable, java.lang.Cloneable  
{
    public static final String VO_LABEL = "com.ctb.lexington.data.DatapointVO";
    public static final String VO_ARRAY_LABEL = VO_LABEL + ".array";

    //properties below should match 100% the entity bean ejb
    private Integer datapointId;
    private String  itemId;
    private Integer itemSetId;
    private Integer minPoints;
    private Integer maxPoints;
    private Integer createdBy;
    private Date    createdDateTime;
    private Integer updatedBy;
    private Date    updatedDateTime;


    /** Creates new DatapointVO */
    public DatapointVO() { }

    public Object clone()
    {
        try
        {
            return super.clone();
        }
        catch(CloneNotSupportedException e)
        {
            return null;
        }
    }


    //-- Get/Set Methods --//
    
    public Integer getDatapointId()
    {
        return this.datapointId;
    }

    public void setDatapointId(Integer datapointId_)
    {
        this.datapointId = datapointId_;
    }
    
    public String getItemId()
    {
        return this.itemId;
    }

    public void setItemId(String itemId_)
    {
        this.itemId = itemId_;
    }
    
    public Integer getItemSetId()
    {
        return this.itemSetId;
    }

    public void setItemSetId(Integer itemSetId_)
    {
        this.itemSetId = itemSetId_;
    }
    
    public Integer getMinPoints()
    {
        return this.minPoints;
    }

    public void setMinPoints(Integer minPoints_)
    {
        this.minPoints = minPoints_;
    }
    
    public Integer getMaxPoints()
    {
        return this.maxPoints;
    }

    public void setMaxPoints(Integer maxPoints_)
    {
        this.maxPoints = maxPoints_;
    }
    
    public Integer getCreatedBy()
    {
        return this.createdBy;
    }

    public void setCreatedBy(Integer createdBy_)
    {
        this.createdBy = createdBy_;
    }
    
    public Date getCreatedDateTime()
    {
        return this.createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime_)
    {
        this.createdDateTime = createdDateTime_;
    }
    
    public Integer getUpdatedBy()
    {
        return this.updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy_)
    {
        this.updatedBy = updatedBy_;
    }
    
    public Date getUpdatedDateTime()
    {
        return this.updatedDateTime;
    }

    public void setUpdatedDateTime(Date updatedDateTime_)
    {
        this.updatedDateTime = updatedDateTime_;
    }
    

    
    


}
