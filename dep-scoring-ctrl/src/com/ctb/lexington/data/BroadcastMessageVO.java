package com.ctb.lexington.data;

/*
 * BroadcastMessageBean.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 */

import java.io.Serializable;
import java.util.Date;



public class BroadcastMessageVO implements Serializable
{
    public static final String BEAN_LABEL = "com.ctb.lexington.bean.BroadcastMessageBean";

    public static final String BEAN_ARRAY_LABEL = BEAN_LABEL + ".array";

    private String message;
    private Date displayDateTime;
    private Integer createdBy;
    private Date startDate;
    private Date endDate;
    private Integer priorityValue;
    private Integer broadcastMessageLogId;
    private String messageType;
    private String application;

    public String getMessage()
    {
        return this.message;
    }

    public void setMessage( String message_ )
    {
    	this.message = message_.trim();
    }

    public Date getDisplayDateTime()
    {
        return this.displayDateTime;
    }

    public void setDisplayDateTime( Date displayDateTime_ )
    {
        this.displayDateTime = displayDateTime_;
    }

    public Integer getCreatedBy()
    {
        return this.createdBy;
    }

    public void setCreatedBy( Integer createdBy_ )
    {
        this.createdBy = createdBy_;
    }

    public Date getStartDate()
    {
        return this.startDate;
    }

    public void setStartDate( Date startDate_ )
    {
        this.startDate = startDate_;
    }

    public Date getEndDate()
    {
        return this.endDate;
    }

    public void setEndDate( Date endDate_ )
    {
        this.endDate = endDate_;
    }

    public Integer getPriorityValue()
    {
        return this.priorityValue;
    }

    public void setPriorityValue( Integer priorityValue_ )
    {
        this.priorityValue = priorityValue_;
    }

    public Integer getBroadcastMessageLogId()
    {
        return this.broadcastMessageLogId;
    }

    public void setBroadcastMessageLogId( Integer broadcastMessageLogId_ )
    {
        this.broadcastMessageLogId = broadcastMessageLogId_;
    }

    public String getMessageType()
    {
        return this.messageType;
    }

    public void setMessageType( String messageType_ )
    {
        this.messageType = messageType_;
    }
    public void setApplication(String application) {
        this.application = application;
    }
    public String getApplication() {
        return application;
    }
}
