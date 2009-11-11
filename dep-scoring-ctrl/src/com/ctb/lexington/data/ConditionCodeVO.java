package com.ctb.lexington.data;

// java imports
import java.io.Serializable;

import com.ctb.lexington.db.record.Persistent;

/**
 * <p>
 * ConditionCodeVO
 * </p>
 * <p>
 * Copyright CTB/McGraw-Hill, 2003
 * CONFIDENTIAL
 * </p>
 *
 * @author  <a href="mailto:dvu@ctb.com">Dat Vu</a>
 */

public class ConditionCodeVO extends Object implements Persistent, Serializable, java.lang.Cloneable  
{
    public static final String VO_LABEL = "com.ctb.lexington.data.ConditionCodeVO";
    public static final String VO_ARRAY_LABEL = VO_LABEL + ".array";

    //properties below should match 100% the entity bean ejb
    private Integer conditionCodeId;
    private String  conditionCode;
    private String  conditionCodeLabel;
    private String  conditionCodeDesc;
    private String  attempted;

    /** Creates new ConditionCodeVO */
    public ConditionCodeVO() { }
    
    public ConditionCodeVO(Integer conditionCodeId, 
                           String  conditionCode,
                           String  conditionCodeLabel,
                           String  conditionCodeDesc,
                           String  attempted)
    {
        this.conditionCodeId = conditionCodeId;
        this.conditionCode = conditionCode;
        this.conditionCodeLabel = conditionCodeLabel;
        this.conditionCodeDesc = conditionCodeDesc;
        this.attempted = attempted;
    }

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
    
    public Integer getConditionCodeId()
    {
        return this.conditionCodeId;
    }

    public void setConditionCodeId(Integer conditionCodeId_)
    {
        this.conditionCodeId = conditionCodeId_;
    }

    public String getConditionCode()
    {
        return this.conditionCode;
    }

    public void setConditionCode(String conditionCode_)
    {
        this.conditionCode = conditionCode_;
    }
    
    public String getConditionCodeLabel()
    {
        return this.conditionCodeLabel;
    }

    public void setConditionCodeLabel(String conditionCodeLabel_)
    {
        this.conditionCodeLabel = conditionCodeLabel_;
    }
    
    public String getConditionCodeDesc()
    {
        return this.conditionCodeDesc;
    }

    public void setConditionCodeDesc(String conditionCodeDesc_)
    {
        this.conditionCodeDesc = conditionCodeDesc_;
    }
    
    public String getAttempted()
    {
        return this.attempted;
    }

    public boolean isAttempted() {
        return "T".equalsIgnoreCase(attempted);
    }

    public void setAttempted(String attempted_)
    {
        this.attempted = attempted_;
    }
}
