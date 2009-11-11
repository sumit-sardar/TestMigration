package com.ctb.lexington.data;

/**
 * ItemResponsePointsVO.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 */
import java.io.Serializable;
import java.util.Date;

/**
 *
 *
 * @author  Tai Truong
 * @version $Id$
 * @version $Id$
 */
public class ItemResponsePointsVO implements Serializable
{
  /**
   * This beans's static label to be used for identification.
   */
	public static final String VO_LABEL       = "com.ctb.lexington.data.ItemResponsePointsVO";
    public static final String VO_ARRAY_LABEL = "com.ctb.lexington.data.ItemResponsePointsVO.array";
    
    private static final String TRACE_TAG = "ItemResponsePointsVO";

	private Integer itemSetId;
	private Integer points;
	private Integer itemResponsepointsSeqNum;
	private Integer datapointId;
	private Integer conditionCodeId;
	private Integer createBy;
	private Date createdDateTime;
	private Integer itemResponseId;
	private String comments;

	
  /**
   * Constructor.
   */
	public ItemResponsePointsVO()
	{
	}
	
  /** 
   * get/set comments
   */
	public String getComments()
	{
		return this.comments;
	}
	public void setComments(String comments_)
	{
		this.comments = comments_;
	}



  /** 
   * get/set itemSetId
   */
	public Integer getItemSetId()
	{
		return this.itemSetId;
	}
	public void setItemSetId(Integer itemSetId_)
	{
		this.itemSetId = itemSetId_;
	}

  /** 
   * get/set points
   */
	public Integer getPoints()
	{
        //GrndsTrace.enterScope(TRACE_TAG + ".getPoints()");
        //GrndsTrace.exitScope(this.points);
		return this.points;
	}
	public void setPoints(Integer points_)
	{
        //GrndsTrace.enterScope(TRACE_TAG + ".setPoints()");
        //GrndsTrace.exitScope(points_);
		this.points = points_;
	}

  /** 
   * get/set itemResponsepointsSeqNum
   */
	public Integer getItemResponsepointsSeqNum()
	{
		return this.itemResponsepointsSeqNum;
	}
	public void setItemResponsepointsSeqNum(Integer itemResponsepointsSeqNum_)
	{
		this.itemResponsepointsSeqNum = itemResponsepointsSeqNum_;
	}

  /** 
   * get/set datapointId
   */
	public Integer getDatapointId()
	{
		return this.datapointId;
	}
	public void setDatapointId(Integer dataPointId_)
	{
		this.datapointId = dataPointId_;
	}

	
  /** 
   * get/set conditionCodeId
   */
	public Integer getConditionCodeId()
	{
        //GrndsTrace.enterScope(TRACE_TAG + ".getConditionCodeId()");
        //GrndsTrace.exitScope(this.conditionCodeId);
		return this.conditionCodeId;
	}
	public void setConditionCodeId(Integer conditionCodeId_)
	{
        //GrndsTrace.enterScope(TRACE_TAG + ".setConditionCodeId()");
        //GrndsTrace.exitScope(conditionCodeId_);
		this.conditionCodeId = conditionCodeId_;
	}

  /** 
   * get/set createBy
   */
	public Integer getCreateBy()
	{
		return this.createBy;
	}
	public void setCreateBy(Integer createBy_)
	{
		this.createBy = createBy_;
	}
		
  /** 
   * get/set createdDateTime
   */
	public Date getCreatedDateTime()
	{
		return this.createdDateTime;
	}
	public void setCreatedDateTime(Date createdDateTime_)
	{
		this.createdDateTime = createdDateTime_;
	}
        
  /** 
   * get/set itemResponseId
   */
	public Integer getItemResponseId()
	{
        //GrndsTrace.enterScope(TRACE_TAG + ".getItemResponseId()");
        //GrndsTrace.exitScope(this.itemResponseId);
		return this.itemResponseId;
	}
	public void setItemResponseId(Integer itemResponseId_)
	{
        //GrndsTrace.enterScope(TRACE_TAG + ".setItemResponseId()");
        //GrndsTrace.exitScope(itemResponseId_);
		this.itemResponseId = itemResponseId_;
	}
			
}
