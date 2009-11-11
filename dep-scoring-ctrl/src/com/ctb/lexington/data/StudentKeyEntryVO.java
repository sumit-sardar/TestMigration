package com.ctb.lexington.data;

/**
 * StudentKeyEntryVO.java
 *
 * Copyright CTB/McGraw-Hill, 2005
 * CONFIDENTIAL
 */

//java imports
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author  Ron Aikins
 */
public class StudentKeyEntryVO extends StudentDetailVO
{
    /**
     * This beans's static label to be used for identification.
     */
    public static final String VO_LABEL       = "com.ctb.lexington.data.StudentKeyEntryVO";
    public static final String VO_ARRAY_LABEL =  VO_LABEL + ".array";

    /**
     * The list of org names of 3 lowest level orgs assoc. with student,
     * ordered high-to-low
    */
    private ArrayList orgPath = null;

   /**
    * Constructor.
    */
    public StudentKeyEntryVO()
    {
    }

    /**
     * Gets the list of <code>String</code> list of org names 
     *
     * @return the list.
     */
    public List getOrgPath()
    {
        return this.orgPath;
    }

   /**
    * Sets the list of <code>String</code> list of org names
    *
    * @param scoredBy_ the <code>List</code>.
    */
    public void setOrgPath( ArrayList orgPath_ )
    {
        this.orgPath = orgPath_;
    }

}
