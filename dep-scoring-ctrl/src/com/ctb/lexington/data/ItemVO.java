package com.ctb.lexington.data;


/*
 * ItemVO.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.sql.DataSource;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.ctb.lexington.db.record.Persistent;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.util.CTBConstants;
import com.ctb.lexington.util.SQLUtil;

/**
 *
 * @author  rmariott
 * @version
 */
public class ItemVO extends Object implements Persistent, java.io.Serializable, java.lang.Cloneable, Comparable  {

    public static final String VO_LABEL = "com.ctb.lexington.data.ItemVO";
    public static final String VO_ARRAY_LABEL = VO_LABEL + ".array";

    public static final String ITEM_TYPE_SR = "SR";
    public static final String ITEM_TYPE_MR = "MR";
    public static final String ITEM_TYPE_CR = "CR";
    public static final String ITEM_TYPE_ER = "ER";

    public static final String ITEM_MEDIA_TABLE_NAME       = "ITEM_MEDIA";
    public static final String ITEM_MEDIA_COLUMN_PKEY_NAME = "ITEM_ID";

    public static final String ITEM_MEDIA_TYPE_IBXML = "IBXML";
    public static final String ITEM_MEDIA_TYPE_IBSWF = "IBSWF";
    public static final String ITEM_MEDIA_TYPE_IBPDF = "IBPDF";
    public static final String ITEM_MEDIA_TYPE_AKSWF = "AKSWF";
    public static final String ITEM_MEDIA_TYPE_AKPDF = "AKPDF";

    //properties below should match 100% the entity bean ejb
    private String itemId;
    private String description;
    private String extStimulusId;
    private String extStimulusTitle;
    private String correctAnswer;
    private String name;
    private String displayCode;
    private String itemType;
    private String onlineCr;
    private Integer minPoints;
    private Integer maxPoints;

    private String activationStatus; // r2.3
    private String templateId;      // r2.3

     private String itemDisplayName;
     private Long itemSetId;
     private Collection conditionCodes = new ArrayList();

     private Long itemSortOrder;
     
     private String isStimulusShared;

     private String createdBy = "CTB";
      
    /** Creates new ItemVO */
    public ItemVO() {
    }

    public ItemVO(final Long itemSetId, final String itemId) {
        this.itemSetId = itemSetId;
        this.itemId = itemId;
    }

    public Object clone(){
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



    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property. This ID is a String since the
     * item identification is not just an integer, but a complex string (e.g.
     * 3R.12.2.0).
     */
    public String getItemId()
    {
        return this.itemId;
    }

    /**
     * Set the value of this property.
     *
     * @param itemId_ The value to set the property to.  This ID is a String
     * since the item identification is not just an integer, but a complex
     * string (e.g. 3R.12.2.0).
     */
    public void setItemId(String itemId_)
    {
        this.itemId = itemId_;
    }

    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getDescription()
    {
 /*       if (this.description == null)
        {
            return "Click on the question number to view the question details.";
        }
        else
        {
            return this.description;
        } */
        return this.description;
    }

    /**
     * Set the value of this property.
     *
     * @param description_ The value to set the property to.
     */
    public void setDescription(String description_)
    {
        this.description = description_;
    }

    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getExtStimulusId()
    {
        return this.extStimulusId;
    }

    public void setExtStimulusId(String extStimulusId_)
    {
        this.extStimulusId = extStimulusId_;
    }

    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getExtStimulusTitle()
    {
        return this.extStimulusTitle;
    }

    public void setExtStimulusTitle(String extStimulusTitle_)
    {
        this.extStimulusTitle = extStimulusTitle_;
    }

    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getCorrectAnswer()
    {
        return this.correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer_)
    {
        this.correctAnswer = correctAnswer_;
    }

    /**
     * Get the name for this bean instance.
     *
     * @return String The name of the this special code.
     */
    public String getName()
    {
        return this.name;
    }

    public void setName(String name_)
    {
        this.name = name_;
    }

    /**
     * Get the display code for this bean instance.
     *
     * @return String The display code for this special code.
     */
    public String getDisplayCode()
    {
        return this.displayCode;
    }

    public void setDisplayCode(String code_)
    {
        this.displayCode = code_;
    }

    /**
     * Get the item type of this bean instance.
     *
     * @return String The item type of this special code. <br>
     * <br>
     * The type can be one of the following:<br>
     * - "SR" Single Response<br>
     * - "MR" Multiple Response<br>
     * - "CR" Constructed Response (not supported for R2)<br>
     * - "ER" Extended Response (not supported for R2)
     *
     */
    public String getItemType()
    {
        return this.itemType;
    }

    public void setItemType(String itemType_)
    {
        this.itemType = itemType_;
    }

    public String getOnlineCr()
    {
        return this.onlineCr;
    }
    
    public String getCr()
    {
        return String.valueOf(this.itemType.equals("CR"));
    }

    public void setOnlineCr(String onlineCr_)
    {
        this.onlineCr = onlineCr_;
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

    public String getActivationStatus() { return this.activationStatus; }
    public void   setActivationStatus(String s){
        this.activationStatus = s;
    }

    public String getTemplateId() { return this.templateId; }
    public void    setTemplateId(String s){
        this.templateId = s;
    }

    public String getItemDisplayName() { return this.itemDisplayName; }
    public void    setItemDisplayName(String i){
        this.itemDisplayName = i;
    }

    public int[] getConditionCodeIds() {
        int[] result = new int[conditionCodes.size()];
        int i = 0;
        for (Iterator iter = conditionCodes.iterator(); iter.hasNext();i++) {
            Integer conditionCodeId = (Integer) iter.next();

            result[i] = conditionCodeId.intValue();
        }
        return result;
    }

    public Long getItemSetId() {
        return itemSetId;
    }

    public void setItemSetId(Long itemSetId) {
        this.itemSetId = itemSetId;
    }

    /**
     * @param conditionCodeId
     */
    public void addConditionCodeId(Integer conditionCodeId) {
        conditionCodes.add(conditionCodeId);
    }

    /**
	 * @return Returns the itemSortOrder.
	 */
	public Long getItemSortOrder() {
		return itemSortOrder;
	}
	/**
	 * @param itemSortOrder The itemSortOrder to set.
	 */
	public void setItemSortOrder(Long itemSortOrder) {
		this.itemSortOrder = itemSortOrder;
	}

    // Object

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    // Comparable

    public int compareTo(Object o) {
        int result = 0;

        ItemVO otherItem = (ItemVO)o;
        if( !itemSetId.equals(otherItem.itemSetId)) {
            throw new IllegalArgumentException("Cannot compare two items of a different item set");
        }

        return itemSortOrder.compareTo(otherItem.itemSortOrder);
    }
    
    public String getIsStimulusShared() throws CTBSystemException {
		if(this.isStimulusShared == null) {
		    Connection conn = null;
	        PreparedStatement ps = null;
	        ResultSet rs = null;
	        try
	        {
	            String sql = "select " + 
								   "item2.item_id, " + 
								   "item1.ext_stimulus_id " + 
							"from " + 
								 "item item1, " + 
								 "item item2 " + 
							"where " +
								 "item1.item_id != item2.item_id " +
								 "and item1.ext_stimulus_id = item2.ext_stimulus_id " +
								 "and item1.item_id = ?";
	            DataSource ds = SQLUtil.getDataSource("LexingtonDataSource" );
	            conn = ds.getConnection();
	            ps = conn.prepareStatement( sql );
	            ps.setString( 1, this.getItemId() );
	            rs = ps.executeQuery();  
	            if( rs.next() )
	            {
	            	this.isStimulusShared = CTBConstants.TRUE;
	            } else {
	            	this.isStimulusShared = CTBConstants.FALSE;
	            }
	        }
	        catch (Exception e) 
	        {
	            CTBSystemException se = new CTBSystemException( "9044",
	                "Error getting min points. + " + e.getMessage(),
	                 e );
	            throw se;
	        }
	        finally
	        {
	            try
	            {
	                if ( ps != null )
	                    ps.close();
	                if ( rs != null )
	                    rs.close();
	                if ( conn != null )
	                    conn.close();
	            }
	            catch ( Exception e )
	            {
	                throw new CTBSystemException( "9045", e.getMessage(), e);
	            }
	        }
		}
        return this.isStimulusShared;
    }
    
    public String getCreatedBy() {
	    if (createdBy.equalsIgnoreCase("system, system") ||
	        createdBy.equalsIgnoreCase("test_content_import, test_content_import") ||
	        createdBy.equalsIgnoreCase("org_import, org_import") ||
	        createdBy.equalsIgnoreCase("root_user, root_user") ||
	        createdBy.equalsIgnoreCase("Admin, Actuate") ||
	        createdBy.equalsIgnoreCase("Client, Test") ||
	        createdBy.equalsIgnoreCase("CTB, CTB")) {
	        createdBy = "CTB";
	    }
        return this.createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}