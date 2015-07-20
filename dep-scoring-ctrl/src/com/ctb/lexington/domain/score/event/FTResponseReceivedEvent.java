package com.ctb.lexington.domain.score.event;

import java.sql.Clob;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FTResponseReceivedEvent extends ResponseEvent {

	protected Integer itemResponseId;
    protected Integer itemSortOrder;
    protected Integer responseElapsedTime;
    protected Integer responseSeqNum;
    protected String extAnswerChoiceId;//
    protected String response;
    protected String responseMethod;
    protected boolean studentMarked;
    protected Integer pointsObtained;
    protected Integer conditionCodeId;
    protected String comments;
    protected String scoringStatus;
    protected Integer crResponse;	//Added for TASC
    protected String conditionCode; //Added for TASC
    protected String answerArea; //Added for TASC
    protected String itemType; //Added for TASC
    protected Clob teItemResponse;
    protected Integer tePoints;
    protected String dasItemId;
    protected List<ChildDASItems> childItems;
    
	
	public FTResponseReceivedEvent(Long testRosterId, String itemId,
			Integer itemSetId) {
		super(testRosterId, itemId, itemSetId);
	}

	public Integer getItemResponseId() {
		return itemResponseId;
	}

	public void setItemResponseId(Integer itemResponseId) {
		this.itemResponseId = itemResponseId;
	}

	public Integer getItemSortOrder() {
		return itemSortOrder;
	}

	public void setItemSortOrder(Integer itemSortOrder) {
		this.itemSortOrder = itemSortOrder;
	}

	public Integer getResponseElapsedTime() {
		return responseElapsedTime;
	}

	public void setResponseElapsedTime(Integer responseElapsedTime) {
		this.responseElapsedTime = responseElapsedTime;
	}

	public Integer getResponseSeqNum() {
		return responseSeqNum;
	}

	public void setResponseSeqNum(Integer responseSeqNum) {
		this.responseSeqNum = responseSeqNum;
	}

	public String getExtAnswerChoiceId() {
		return extAnswerChoiceId;
	}

	public void setExtAnswerChoiceId(String extAnswerChoiceId) {
		this.extAnswerChoiceId = extAnswerChoiceId;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getResponseMethod() {
		return responseMethod;
	}

	public void setResponseMethod(String responseMethod) {
		this.responseMethod = responseMethod;
	}

	public boolean isStudentMarked() {
		return studentMarked;
	}

	public void setStudentMarked(boolean studentMarked) {
		this.studentMarked = studentMarked;
	}

	public Integer getPointsObtained() {
		return pointsObtained;
	}

	public void setPointsObtained(Integer pointsObtained) {
		this.pointsObtained = pointsObtained;
	}

	public Integer getConditionCodeId() {
		return conditionCodeId;
	}

	public void setConditionCodeId(Integer conditionCodeId) {
		this.conditionCodeId = conditionCodeId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getScoringStatus() {
		return scoringStatus;
	}

	public void setScoringStatus(String scoringStatus) {
		this.scoringStatus = scoringStatus;
	}

	public Integer getCrResponse() {
		return crResponse;
	}

	public void setCrResponse(Integer crResponse) {
		this.crResponse = crResponse;
	}

	public String getConditionCode() {
		return conditionCode;
	}

	public void setConditionCode(String conditionCode) {
		this.conditionCode = conditionCode;
	}

	public String getAnswerArea() {
		return answerArea;
	}

	public void setAnswerArea(String answerArea) {
		this.answerArea = answerArea;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public Clob getTeItemResponse() {
		return teItemResponse;
	}

	public void setTeItemResponse(Clob teItemResponse) {
		this.teItemResponse = teItemResponse;
	}

	public Integer getTePoints() {
		return tePoints;
	}

	public void setTePoints(Integer tePoints) {
		this.tePoints = tePoints;
	}

	public String getDasItemId() {
		return dasItemId;
	}

	public void setDasItemId(String dasItemId) {
		this.dasItemId = dasItemId;
	}

	public List<ChildDASItems> getChildItems() {
		return childItems;
	}

	public void addChildItems(String childDasItemId, String interationType, Integer itemOrder) {
		if(childDasItemId == null){
			this.childItems = Collections.emptyList();
		}else {
			if(this.childItems==null) {
				this.childItems=new ArrayList<ChildDASItems>();
			}
			this.childItems.add(new ChildDASItems(childDasItemId, interationType, itemOrder));
		}
	}
	
	public static class ChildDASItems {
		protected String childDasItemId;
		protected String interationType;
		protected Integer itemOrder;
		
		public ChildDASItems(String childDasItemId, String interationType, Integer itemOrder) {
			super();
			this.childDasItemId = childDasItemId;
			this.interationType = interationType;
			this.itemOrder = itemOrder;
		}

		public String getChildDasItemId() {
			return childDasItemId;
		}

		public void setChildDasItemId(String childDasItemId) {
			this.childDasItemId = childDasItemId;
		}

		public String getInterationType() {
			return interationType;
		}

		public void setInterationType(String interationType) {
			this.interationType = interationType;
		}

		public Integer getItemOrder() {
			return itemOrder;
		}

		public void setItemOrder(Integer itemOrder) {
			this.itemOrder = itemOrder;
		}
		
		
	}
}