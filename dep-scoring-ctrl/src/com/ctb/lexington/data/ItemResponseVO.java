/*
 * ItemResponseVO.java Copyright CTB/McGraw-Hill, 2002 CONFIDENTIAL
 */
package com.ctb.lexington.data;

import java.io.Serializable;
import java.sql.Clob;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.ctb.lexington.db.record.Persistent;

/**
 * @author Giuseppe Gennaro
 * @version $Id: ItemResponseVO.java,v 1.4.16.1 2004/08/12 00:03:59 ghohpe Exp
 *          $
 */
public class ItemResponseVO implements Persistent, Serializable, java.lang.Cloneable {
    /**
     * This beans's static label to be used for identification.
     */
    public static final String BEAN_LABEL =
            "com.ctb.lexington.bean.ItemResponseBean";

    /**
     * This beans's static array label to be used for identification.
     */
    public static final String BEAN_ARRAY_LABEL = BEAN_LABEL + ".array";

    private Date createdDateTime;
    private Integer createdBy;
    private Integer itemResponseId;
    private Integer itemSetId;
    private Integer itemSortOrder;
    private Integer responseElapsedTime;
    private Integer responseSeqNum;
    private Integer testRosterId;
    private String extAnswerChoiceId;
    private String itemId;
    private String response;
    private String responseMethod;
    private String studentMarked;
    private Integer points;
    private Integer conditionCodeId;
    private String comments;
    private Clob constructedResponse;
    private String varcharConstructedResponse;
    private String grItemRules;
    private String grItemCorrectAnswer;
    private String answerArea;
    private String itemType;
    private String correctAnswer; // Added for tabe adaptive
    private Integer crResponse;	//Added for TASC
    private String conditionCode;	// Added for TASC

	/**
	 * @return Returns the constructedResponse.
	 */
	public Clob getConstructedResponse() {
		return constructedResponse;
	}
	/**
	 * @param constructedResponse The constructedResponse to set.
	 */
	public void setConstructedResponse(Clob constructedResponse) {
		this.constructedResponse = constructedResponse;
	}
	/**
	 * @return Returns the comments.
	 */
	public String getComments() {
		return comments;
	}
	/**
	 * @param comments The comments to set.
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}
    public ItemResponseVO() {}

    public ItemResponseVO(final Date createdDateTime, final Integer createdBy,
            final Integer itemResponseId, final Integer itemSetId,
            final Integer responseElapsedTime, final Integer responseSeqNum,
            final Integer testRosterId, final String extAnswerChoiceId,
            final String itemId, final String response,
            final String responseMethod, final String studentMarked) {
        this(createdDateTime, createdBy, itemResponseId, itemSetId,
                responseElapsedTime, responseSeqNum, testRosterId,
                extAnswerChoiceId, itemId, response, responseMethod,
                studentMarked, null, null);
    }

    public ItemResponseVO(final Date createdDateTime, final Integer createdBy,
            final Integer itemResponseId, final Integer itemSetId,
            final Integer responseElapsedTime, final Integer responseSeqNum,
            final Integer testRosterId, final String extAnswerChoiceId,
            final String itemId, final String response,
            final String responseMethod, final String studentMarked,
            final Integer points, final Integer conditionCodeId) {
        this.createdDateTime = createdDateTime;
        this.createdBy = createdBy;
        this.itemResponseId = itemResponseId;
        this.itemSetId = itemSetId;
        this.responseElapsedTime = responseElapsedTime;
        this.responseSeqNum = responseSeqNum;
        this.testRosterId = testRosterId;
        this.extAnswerChoiceId = extAnswerChoiceId;
        this.itemId = itemId;
        this.response = response;
        this.responseMethod = responseMethod;
        this.studentMarked = studentMarked;
        this.points = points;
        this.conditionCodeId = conditionCodeId;
    }

    public Integer getConditionCodeId() {
        return conditionCodeId;
    }

    public void setConditionCodeId(final Integer conditionCodeId) {
        this.conditionCodeId = conditionCodeId;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(final Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(final Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getExtAnswerChoiceId() {
        return extAnswerChoiceId;
    }

    public void setExtAnswerChoiceId(final String extAnswerChoiceId) {
        this.extAnswerChoiceId = extAnswerChoiceId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(final String itemId) {
        this.itemId = itemId;
    }

    public Integer getItemResponseId() {
        return itemResponseId;
    }

    public void setItemResponseId(final Integer itemResponseId) {
        this.itemResponseId = itemResponseId;
    }

    public Integer getItemSetId() {
        return itemSetId;
    }

    public void setItemSetId(final Integer itemSetId) {
        this.itemSetId = itemSetId;
    }

    public Integer getItemSortOrder() {
        return itemSortOrder;
    }

    public void setItemSortOrder(final Integer itemSortOrder) {
        this.itemSortOrder = itemSortOrder;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(final Integer points) {
        this.points = points;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(final String response) {
        this.response = response;
    }

    public Integer getResponseElapsedTime() {
        return responseElapsedTime;
    }

    public void setResponseElapsedTime(final Integer responseElapsedTime) {
        this.responseElapsedTime = responseElapsedTime;
    }

    public String getResponseMethod() {
        return responseMethod;
    }

    public void setResponseMethod(final String responseMethod) {
        this.responseMethod = responseMethod;
    }

    public Integer getResponseSeqNum() {
        return responseSeqNum;
    }

    public void setResponseSeqNum(final Integer responseSeqNum) {
        this.responseSeqNum = responseSeqNum;
    }

    public String getStudentMarked() {
        return studentMarked;
    }

    public void setStudentMarked(final String studentMarked) {
        this.studentMarked = studentMarked;
    }

    public Integer getTestRosterId() {
        return testRosterId;
    }

    public void setTestRosterId(final Integer testRosterId) {
        this.testRosterId = testRosterId;
    }

    // Object

    public boolean equals(final Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
    
    public Object clone() {
        try {
            return super.clone();
        }
        catch(CloneNotSupportedException e) {
            return null;
        }
    }
	public String getCorrectAnswer() {
		return correctAnswer;
	}
	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}
	/**
	 * @return the varcharConstructedResponse
	 */
	public String getVarcharConstructedResponse() {
		return varcharConstructedResponse;
	}
	/**
	 * @param varcharConstructedResponse the varcharConstructedResponse to set
	 */
	public void setVarcharConstructedResponse(String varcharConstructedResponse) {
		this.varcharConstructedResponse = varcharConstructedResponse;
	}
	/**
	 * @return the grItemRules
	 */
	public String getGrItemRules() {
		return grItemRules;
	}
	/**
	 * @param grItemRules the grItemRules to set
	 */
	public void setGrItemRules(String grItemRules) {
		this.grItemRules = grItemRules;
	}
	/**
	 * @return the grItemCorrectAnswer
	 */
	public String getGrItemCorrectAnswer() {
		return grItemCorrectAnswer;
	}
	/**
	 * @param grItemCorrectAnswer the grItemCorrectAnswer to set
	 */
	public void setGrItemCorrectAnswer(String grItemCorrectAnswer) {
		this.grItemCorrectAnswer = grItemCorrectAnswer;
	}
	/**
	 * @return the answerArea
	 */
	public String getAnswerArea() {
		return answerArea;
	}
	/**
	 * @param answerArea the answerArea to set
	 */
	public void setAnswerArea(String answerArea) {
		this.answerArea = answerArea;
	}
	/**
	 * @return the itemType
	 */
	public String getItemType() {
		return itemType;
	}
	/**
	 * @param itemType the itemType to set
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	/**
	 * @return the crResponse
	 */
	public Integer getCrResponse() {
		return crResponse;
	}
	/**
	 * @return the conditionCode
	 */
	public String getConditionCode() {
		return conditionCode;
	}
	/**
	 * @param crResponse the crResponse to set
	 */
	public void setCrResponse(Integer crResponse) {
		this.crResponse = crResponse;
	}
	/**
	 * @param conditionCode the conditionCode to set
	 */
	public void setConditionCode(String conditionCode) {
		this.conditionCode = conditionCode;
	}
    
}
