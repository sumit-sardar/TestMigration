package com.ctb.lexington.data;

/*
 * ItemDetailVO.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.sql.DataSource;

import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.util.SQLUtil;


/**
 *
 * @author  rmariott
 * @version
 */
public class ItemDetailVO extends ItemVO implements java.io.Serializable, java.lang.Cloneable {

    public static final String VO_LABEL = "com.ctb.lexington.data.ItemDetailVO";
    public static final String VO_ARRAY_LABEL = VO_LABEL + ".array";

    private Integer orderNumber;              // primarily owned by IBSWorksheetConversation
    private Integer previousOrderNumber;      // primarily owned by IBSWorksheetConversation
    private String  isDirty;                  // primarily owned by IBSWorksheetConversation
    private Integer groupOrderNumber;         // does not have setter?
    private List    answerChoices;            // getter by EditTestSessionConversation, setter by SpecialCodeManagerEJB
    private Map     attributesMap;
    private boolean usedProductAttributes;

    /** Creates new ItemDetailVO */
    public ItemDetailVO() {
        this.isDirty = "false";
        this.usedProductAttributes = false;
    }

    public Object clone()
    {
        return super.clone();
    }

    //-- Get/Set Methods --//

    public Map getAttributesMap(Integer productId)
    {
    	try
        {
            if ( this.attributesMap == null || !this.usedProductAttributes) 
            {
                this.attributesMap = retrieveItemLevelAttributesMap( this.getItemId(), productId );
                String crType = new String( "CR" );
                if ( getItemType().equals( crType ) )
                {
                    int lastIndex = this.attributesMap.size();
                    	this.setMinPoints(getMinPoints(productId));
                    	if(this.getMinPoints() != null) {
	                    	String[] attribute = new String[3];
		                    attribute[0] = new String( "Minimum Point" );
		                    attribute[1] = this.getMinPoints().toString();
		                    attribute[2] = null;
		                    this.attributesMap.put( new Integer( lastIndex), attribute );
		                    lastIndex++;
                    	}
                    	this.setMaxPoints(getMaxPoints(productId));
                    	if(this.getMaxPoints() != null) {
                    		String[] attribute = new String[3];
		                    attribute[0] = new String( "Maximum Point" );
		                    attribute[1] = this.getMaxPoints().toString();
		                    attribute[2] = null;
		                    this.attributesMap.put( new Integer( lastIndex), attribute );
                    	}
                }
                this.usedProductAttributes = true;
            }
            return this.attributesMap;
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error getting getAttributesMap.",
                                            e);
        }
    }
    
    private Map retrieveItemLevelAttributesMap( String itemId, Integer productId )
    throws CTBSystemException
    {
        Map itemAttributes = new TreeMap();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            String sql = "select isc.item_set_category_name, ist.item_set_name, ist.ext_cms_item_set_id "
                        + "from item_set_ancestor isa, item_set ist, item_set_category isc "
                        + "where ist.item_set_category_id = isc.item_set_category_id "
                        + "and isc.item_set_category_level <> 1 "
                        + "and isa.ancestor_item_set_id = ist.item_set_id "
                        + "and isa.item_set_id = ( select ist.item_set_id from item_set ist, "
                        + "item_set_item isi, product prod, item_set_category isc "
                        + " where ist.item_set_type = 'RE' "
                        + "and isi.item_id = ? and isi.item_set_id = ist.item_set_id "
                        + "and isc.item_set_category_id = ist.item_set_category_id and "
                        + "prod.product_id = ? and prod.parent_product_id = isc.framework_product_id ) "
                        + "order by isa.degrees DESC";
            DataSource ds = SQLUtil.getDataSource("LexingtonDataSource" );
            conn = ds.getConnection();
            ps = conn.prepareStatement( sql );
            ps.setString( 1, itemId );
            ps.setInt( 2, productId.intValue() );
            rs = ps.executeQuery();  
            int i = 0;
            while( rs.next() )
            {
                String[] attribute = new String[3];
                attribute[0] = rs.getString( 1 );
                attribute[1] = rs.getString( 2 );
                attribute[2] = rs.getString( 3 );
                itemAttributes.put( new Integer(i), attribute );
                i++;
            }
        }
        catch (Exception e) 
        {
            CTBSystemException se = new CTBSystemException( "9044",
                "Error populating itemLevelAttributesMap. + " + e.getMessage(),
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
            //GrndsTrace.exitScope();
        }
        return itemAttributes;
    }
    
    public Map getAttributesMap()
    {
        return this.attributesMap;
    }

    /**
     * Set the value of this property.
     *
     * @param orderNumber_ The 1-based ordering of this item in a testlet.
     * @return void
     */
    public void setAttributesMap(Map attributesMap_)
    {
        this.attributesMap = attributesMap_;
    }


    /**
     * Get this property from this bean instance.
     *
     * @return Integer The 1-based ordering of this item in a testlet.
     */
    public Integer getOrderNumber()
    {
        return this.orderNumber;
    }

    /**
     * Set the value of this property.
     *
     * @param orderNumber_ The 1-based ordering of this item in a testlet.
     * @return void
     */
    public void setOrderNumber(Integer orderNumber_)
    {
        this.orderNumber = orderNumber_;
    }

    /**
     * Get this property from this bean instance.
     *
     * @return Integer The 1-based ordering of this item in a testlet before the
     * most recent change.  This value is the same as orderNumber before any changes.
     */
    public Integer getPreviousOrderNumber()
    {
        return this.previousOrderNumber;
    }

    /**
     * Set the value of this property.
     *
     * @param previousOrderNumber_ The 1-based ordering of this item in a testlet before the
     * most recent change.  This value is the same as orderNumber before any changes.
     * @return void
     */
    public void setPreviousOrderNumber(Integer previousOrderNumber_)
    {
        this.previousOrderNumber = previousOrderNumber_;
    }

    /**
     * Get this property from this bean instance.
     *
     * @return String Tells whether or not this ItemDetailVO has had it's order
     * changed.  If changed, isDirty is "true".  Used in ItemDetailComparator, so
     * a Boolean object could not be used (doesn't implement compareTo).
     */
    public String getIsDirty()
    {
        return this.isDirty;
    }

    /**
     * Set the value of this property.
     *
     * @param isDirty_ Tells whether or not this ItemDetailVO has had it's order
     * changed.  If changed, isDirty is "true".  Used in ItemDetailComparator, so
     * a Boolean object could not be used (doesn't implement compareTo).
     *
     * @return void
     */
    public void setIsDirty(String isDirty_)
    {
        this.isDirty = isDirty_;
    }

    /**
     * Get this property from this bean instance.
     *
     * @return Integer The value of the property.
     */
    public Integer getGroupOrderNumber()
    {
        return this.groupOrderNumber;
    }

    /**
     * Set the value of this property.
     *
     * @param groupOrderNumber_ The value to set the property to.
     * @return void
     */
    public void setGroupOrderNumber(Integer groupOrderNumber_)
    {
        this.groupOrderNumber = groupOrderNumber_;
    }

    /**
     * Gets the Answer Choices for this bean instance.
     *
     * @return List A list of AnswerChoiceVOs.
     */
    public List getAnswerChoices()
    {
        return this.answerChoices;
    }

    /**
     * Set the value of this property.
     *
     * @param choices_ The value to set the property to.
     * @return void
     */
    public void setAnswerChoices(List choices_)
    {
        this.answerChoices = choices_;
    }
    
    public Integer getMinPoints(Integer productId) throws CTBSystemException {
		if(this.getMinPoints() == null) {
		    Connection conn = null;
	        PreparedStatement ps = null;
	        ResultSet rs = null;
	        try
	        {
	            String sql = "select dp.min_points, dp.max_points " +
	            		"from datapoint dp, item_set iset, item_set_category isc, product prod " + 
	            		"where prod.product_id = ? " + 
	            		"and dp.item_set_id = iset.item_set_id " +
	            		"and dp.item_id = ? " +
						"and isc.item_set_category_id = iset.item_Set_category_id " +
						"and isc.framework_product_id = prod.parent_product_id";
	            DataSource ds = SQLUtil.getDataSource("LexingtonDataSource" );
	            conn = ds.getConnection();
	            ps = conn.prepareStatement( sql );
	            ps.setString( 2, this.getItemId() );
	            ps.setInt( 1, productId.intValue() );
	            rs = ps.executeQuery();  
	            int i = 0;
	            while( rs.next() )
	            {
	                this.setMinPoints(new Integer(rs.getInt("MIN_POINTS")));
	                this.setMaxPoints(new Integer(rs.getInt("MAX_POINTS")));
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
	            //GrndsTrace.exitScope();
	        }
		}
        return this.getMinPoints();
    }
    
    public Integer getMaxPoints(Integer productId) throws CTBSystemException {
		if(this.getMaxPoints() == null) {
		    Connection conn = null;
	        PreparedStatement ps = null;
	        ResultSet rs = null;
	        try
	        {
	            String sql = "select dp.min_points, dp.max_points " +
	            		"from datapoint dp, item_set_product isp " +
	            		"where isp.product_id = ? " +
	            		"and dp.item_set_id = isp.item_set_id " +
	            		"and dp.item_id = ?";
	            DataSource ds = SQLUtil.getDataSource("LexingtonDataSource" );
	            conn = ds.getConnection();
	            ps = conn.prepareStatement( sql );
	            ps.setString( 2, this.getItemId() );
	            ps.setInt( 1, productId.intValue() );
	            rs = ps.executeQuery();  
	            int i = 0;
	            while( rs.next() )
	            {
	                this.setMinPoints(new Integer(rs.getInt("MIN_POINTS")));
	                this.setMaxPoints(new Integer(rs.getInt("MAX_POINTS")));
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
	            //GrndsTrace.exitScope();
	        }
		}
        return this.getMaxPoints();
    }
}
/*
 * $Log$
 * Revision 1.1  2007/01/30 01:31:41  ncohen
 * port scoring to 4.x platform
 *
 * Revision 1.1  2006/02/23 20:47:40  ncohen
 * create new module for legacy OAS app
 *
 * Revision 1.6  2005/08/02 22:09:24  ncohen
 * replace trunk with iknow-whalphin
 *
 * Revision 1.4.4.2  2005/06/18 00:44:47  ncohen
 * fix merge problems
 *
 * Revision 1.4.2.7  2005/06/16 01:03:58  ncohen
 * correct DP retrieval query
 *
 * Revision 1.4.2.6  2005/06/16 00:50:58  ncohen
 * correct DP retrieval query
 *
 * Revision 1.4.2.5  2005/06/15 19:40:51  ncohen
 * always set min and max points on itemDetailVO
 *
 * Revision 1.4.2.4  2005/06/15 00:56:24  ncohen
 * use product id to obtain item min and max points
 *
 * Revision 1.4.2.3  2005/06/10 00:32:47  ncohen
 * retrieve GM grade using product and item id before passing VO to GM JSP
 *
 * Revision 1.4.2.2  2005/06/07 00:35:15  ncohen
 * retrieve item attributes by product even if they are already present on VO
 *
 * Revision 1.4.2.1  2005/06/03 19:16:24  ncohen
 * correct item worksheet retrieval to use product id
 *
 * Revision 1.4  2005/05/03 21:26:08  ncohen
 * replace HEAD with iknow-millbarge
 *
 * Revision 1.3.22.1  2004/08/17 22:01:43  binkley
 * Globally organize imports (removed 1,000+ warnings).
 *
 * Revision 1.3  2003/02/05 19:27:57  oasuser
 * merged from OASR3tmp to trunk
 *
 * Revision 1.2  2003/01/31 04:03:27  oasuser
 * merged from OASR3tmp to trunk.  Code not compiling, so java will be re-merged at a later date.
 *
 * Revision 1.1.2.4  2003/01/31 03:39:33  kgawetsk
 * Refactoring - use TOAssembler, and cleanup.
 *
 * Revision 1.1.2.3  2003/01/28 04:29:42  kgawetsk
 * refactoring - comments.
 *
 * Revision 1.1.2.2  2003/01/25 05:07:54  kgawetsk
 * R2.3 refactoring for GroupMaterialVO.
 *
 * Revision 1.1.2.1  2003/01/23 20:06:12  kgawetsk
 * R2.3 - refactoring for ItemDetailVO.
 *
 */
