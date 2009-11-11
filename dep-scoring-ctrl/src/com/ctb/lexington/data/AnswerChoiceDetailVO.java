package com.ctb.lexington.data;


/*
 * AnswerChoiceDetailVO.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 */



/**
 *
 * @author  Kristie Kaneta
 * @version
 * $Id$
 */
public class AnswerChoiceDetailVO extends AnswerChoiceVO implements java.io.Serializable
{
    public static final String VO_LABEL       = "com.ctb.lexington.valueobject.AnswerChoiceDetailVO";
    public static final String VO_ARRAY_LABEL = VO_LABEL + ".array";

    private Integer choiceSortOrder;
    private Boolean isSelected;

    /** Creates new AnswerChoiceDetailVO */
    public AnswerChoiceDetailVO()
    {
        //default initialization
        this.isSelected      = new Boolean(false);
        this.choiceSortOrder = new Integer(0);
    }


    /**
     * Get the Sort Order of the AnswerChoice.
     *
     * @return Integer The Sort Order of the AnswerChoice..
     */
    public Integer getChoiceSortOrder(){
        return this.choiceSortOrder;
    }

    /**
     * Set the value of the Answer '<b>Choice Sort Order</b>' property.
     *
     * @param o_ The value to set the property to.
     * @return void
     */
    public void setChoiceSortOrder(Integer o_){
        this.choiceSortOrder = o_;
    }

    /**
     * Get this property from this bean instance.
     *
     * @return Boolean The value of the property.
     */
    public Boolean getIsSelected()
    {
        return this.isSelected;
    }

    /**
     * Set the value of this property.
     *
     * @param isSelected_ The value to set the property to.
     * @return void
     */
    public void setIsSelected(Boolean isSelected_)
    {
        this.isSelected = isSelected_;
    }
}
/*
 * $Log$
 * Revision 1.1  2007/01/30 01:31:41  ncohen
 * port scoring to 4.x platform
 *
 * Revision 1.1  2006/02/23 20:47:38  ncohen
 * create new module for legacy OAS app
 *
 * Revision 1.3  2005/05/03 21:26:07  ncohen
 * replace HEAD with iknow-millbarge
 *
 * Revision 1.2.22.1  2004/08/17 22:01:43  binkley
 * Globally organize imports (removed 1,000+ warnings).
 *
 * Revision 1.2  2003/02/08 01:43:46  oasuser
 * merged OASR3tmp to trunk.
 *
 * Revision 1.1.2.1  2003/02/06 17:54:48  jshields
 * Adding.
 *
 * Revision 1.3  2003/01/09 03:16:00  oasuser
 * merged from iknowR2-fixes branch as of 01-08 to trunk
 *
 * Revision 1.2.4.1  2002/12/27 17:51:09  kgawetsk
 * Added choiceSortOrder property. and its setter, getter.
 *
 */