package com.ctb.lexington.data;

// java imports

/**
 * <p>
 * ScoringStatusCodeVO
 * </p>
 * <p>
 * Copyright CTB/McGraw-Hill, 2003
 * CONFIDENTIAL
 * </p>
 *
 * @author  <a href="mailto:dvu@ctb.com">Dat Vu</a>
 */

public class ScoringStatusCodeVO extends Object implements java.io.Serializable, java.lang.Cloneable  
{
    public static final String VO_LABEL = "com.ctb.lexington.data.ScoringStatusCodeVO";
    public static final String VO_ARRAY_LABEL = VO_LABEL + ".array";

    //properties below should match 100% the entity bean ejb
    private String scoringStatus;
    private String scoringStatusDesc;


    /** Creates new ScoringStatusCodeVO */
    public ScoringStatusCodeVO() { }

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
    
    public String getScoringStatus()
    {
        return this.scoringStatus;
    }

    public void setScoringStatus(String scoringStatus_)
    {
        this.scoringStatus = scoringStatus_;
    }
    
    public String getScoringStatusDesc()
    {
        return this.scoringStatusDesc;
    }

    public void setScoringStatusDesc(String scoringStatusDesc_)
    {
        this.scoringStatusDesc = scoringStatusDesc_;
    }
    

}
