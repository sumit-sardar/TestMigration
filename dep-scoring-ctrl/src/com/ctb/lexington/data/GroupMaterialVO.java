package com.ctb.lexington.data;


/*
 * GroupMaterialVO.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 */


import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author  rmariott
 * @version
 */
public class GroupMaterialVO extends Object implements java.io.Serializable {

    public static final String VO_LABEL = "com.ctb.lexington.data.GroupMaterialVO";
    public static final String VO_ARRAY_LABEL = VO_LABEL + ".array";

    private String  stimulusId;
    private String  stimulusTitle;
    private String  level;
    private String  creatorName;
    private String  content;
    private boolean hasGraphic;
    private List    categoryValuesList = new ArrayList();
    private String  categoryName       = null;

    /** Creates new GroupMaterialVO */
    public GroupMaterialVO() {

    }


    //-- Get/Set Methods --//

    /**
     * Get this property from this bean instance.
     *
     * @return List of Strings representing all the category values
     * that this test's questions cover. This is the category at the
     * scoring/reporting level.
     */
    public List getCategoryValuesList()
    {
        return this.categoryValuesList;
    }

    /**
     * Set the value of this property.
     *
     * @param strandsList_ List of Strings representing all the strands that
     * this test's questions cover.
     *
     * @return void
     */
    public void setCategoryValuesList(List categoryValuesList_)
    {
        this.categoryValuesList = categoryValuesList_;
    }


    /**
     * Get this property from this bean instance.
     *
     * @return String representing the name of the category whose values are
     * in the categoryValuesList.
     */
    public String getCategoryName()
    {
        return this.categoryName;
    }

    /**
     * Set the value of this property.
     *
     * @param categoryName_ name of the category whose valuesList is in this VO.
     *
     * @return void
     */
    public void setCategoryName(String categoryName_)
    {
        this.categoryName = categoryName_;
    }


    /**
     * Get this property from this bean instance.
     *
     * @return boolean The value of the property.
     */
    public boolean getHasGraphic()
    {
        return this.hasGraphic;
    }

    /**
     * Set the value of this property.
     *
     * @param hasGraphic_ The value to set the property to.
     * @return void
     */
    public void setHasGraphic(boolean hasGraphic_)
    {
        this.hasGraphic = hasGraphic_;
    }
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getStimulusId()
    {
        return this.stimulusId;
    }

    /**
     * Set the value of this property.
     *
     * @param stimulusId_ The value to set the property to.
     * @return void
     */
    public void setStimulusId(String stimulusId_)
    {
        this.stimulusId = stimulusId_;
    }

    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getStimulusTitle()
    {
        return this.stimulusTitle;
    }

    /**
     * Set the value of this property.
     *
     * @param stimulusTitle_ The value to set the property to.
     * @return void
     */
    public void setStimulusTitle(String stimulusTitle_)
    {
        this.stimulusTitle = stimulusTitle_;
    }

    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getLevel()
    {
        return this.level;
    }

    /**
     * Set the value of this property.
     *
     * @param level_ The value to set the property to.
     * @return void
     */
    public void setLevel(String level_)
    {
        this.level = level_;
    }

    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getCreatorName()
    {
        return this.creatorName;
    }

    /**
     * Set the value of this property.
     *
     * @param creatorName_ The value to set the property to.
     * @return void
     */
    public void setCreatorName(String creatorName_)
    {
        this.creatorName = creatorName_;
    }

    /**
     * Get this property from this bean instance.
     *
     * @return String Full XML content as stored in the item repository.  This
     * XML may already be formatted into HTML for immediate display into JSPs.
     */
    public String getContent()
    {
        return this.content;
    }

    /**
     * Set the value of this property.
     *
     * @param content The value to set the property to.
     * @return void
     */
    public void setContent(String content_)
    {
        this.content = content_;
    }

}
/*
 * $Log$
 * Revision 1.1  2007/01/30 01:31:41  ncohen
 * port scoring to 4.x platform
 *
 * Revision 1.1  2006/02/23 20:47:39  ncohen
 * create new module for legacy OAS app
 *
 * Revision 1.3  2005/05/03 21:26:09  ncohen
 * replace HEAD with iknow-millbarge
 *
 * Revision 1.2.22.1  2004/08/17 22:01:43  binkley
 * Globally organize imports (removed 1,000+ warnings).
 *
 * Revision 1.2  2003/01/31 04:03:26  oasuser
 * merged from OASR3tmp to trunk.  Code not compiling, so java will be re-merged at a later date.
 *
 * Revision 1.1.2.2  2003/01/28 04:29:36  kgawetsk
 * refactoring - cosmetics.
 *
 */