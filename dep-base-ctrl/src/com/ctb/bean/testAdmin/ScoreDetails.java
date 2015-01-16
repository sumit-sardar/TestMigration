package com.ctb.bean.testAdmin; 

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import com.ctb.bean.CTBBean;


public class ScoreDetails extends CTBBean
{ 
    static final long serialVersionUID = 1L;

    private Integer itemSetId;
    private String itemSetName;
    private String itemSetType;
    private String itemSetOrder;
    private String itemSetLevel;
    private String accessCode;
    private Integer totalItems;
    private String testSessionName;
    private String testName;
    private String productType;
    private String responseStatus;
    private ResponseResultDetails[] responseList;
    
    private String itemSetNameTD;
    private Integer itemSetIdTD;
    private String completionStatusTD;
    private String testIdsToBeShown;
    private List<DeliveryUnitElement> deliverableUnit;
    
    
    public static class OrderByItemSetOrder implements
		Comparator<ScoreDetails> {
		@Override
		public int compare(ScoreDetails s1, ScoreDetails s2) {
			return s1.itemSetOrder.compareTo(s2.itemSetOrder);
		}
	}
    
    /**
	 * @return the responseList
	 */
	public ResponseResultDetails[] getResponseList() {
		return responseList;
	}
	/**
	 * @param responseList the responseList to set
	 */
	public void setResponseList(ResponseResultDetails[] responseList) {
		this.responseList = responseList;
	}
	/**
	 * @return the itemSetId
	 */
	public Integer getItemSetId() {
		return itemSetId;
	}
	/**
	 * @param itemSetId the itemSetId to set
	 */
	public void setItemSetId(Integer itemSetId) {
		this.itemSetId = itemSetId;
	}
	/**
	 * @return the itemSetName
	 */
	public String getItemSetName() {
		return itemSetName;
	}
	/**
	 * @param itemSetName the itemSetName to set
	 */
	public void setItemSetName(String itemSetName) {
		this.itemSetName = itemSetName;
	}
	/**
	 * @return the itemSetType
	 */
	public String getItemSetType() {
		return itemSetType;
	}
	/**
	 * @param itemSetType the itemSetType to set
	 */
	public void setItemSetType(String itemSetType) {
		this.itemSetType = itemSetType;
	}
	/**
	 * @return the itemSetOrder
	 */
	public String getItemSetOrder() {
		return itemSetOrder;
	}
	/**
	 * @param itemSetOrder the itemSetOrder to set
	 */
	public void setItemSetOrder(String itemSetOrder) {
		this.itemSetOrder = itemSetOrder;
	}
	/**
	 * @return the itemSetLevel
	 */
	public String getItemSetLevel() {
		return itemSetLevel;
	}
	/**
	 * @param itemSetLevel the itemSetLevel to set
	 */
	public void setItemSetLevel(String itemSetLevel) {
		this.itemSetLevel = itemSetLevel;
	}
	/**
	 * @return the accessCode
	 */
	public String getAccessCode() {
		return accessCode;
	}
	/**
	 * @param accessCode the accessCode to set
	 */
	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}
	/**
	 * @return the totalItems
	 */
	public Integer getTotalItems() {
		return totalItems;
	}
	/**
	 * @param totalItems the totalItems to set
	 */
	public void setTotalItems(Integer totalItems) {
		this.totalItems = totalItems;
	}
	/**
	 * @return the deliverableUnit
	 */
	public List<DeliveryUnitElement> getDeliverableUnit() {
		return deliverableUnit;
	}
	/**
	 * @param deliverableUnit the deliverableUnit to set
	 */
	public void setDeliverableUnit(List<DeliveryUnitElement> deliverableUnit) {
		this.deliverableUnit = deliverableUnit;
	}
	/**
	 * @return the testSessionName
	 */
	public String getTestSessionName() {
		return testSessionName;
	}
	/**
	 * @param testSessionName the testSessionName to set
	 */
	public void setTestSessionName(String testSessionName) {
		this.testSessionName = testSessionName;
	}
	/**
	 * @return the testName
	 */
	public String getTestName() {
		return testName;
	}
	/**
	 * @param testName the testName to set
	 */
	public void setTestName(String testName) {
		this.testName = testName;
	}
	/**
	 * @return the productType
	 */
	public String getProductType() {
		return productType;
	}
	/**
	 * @param productType the productType to set
	 */
	public void setProductType(String productType) {
		this.productType = productType;
	}
	
	/**
	 * @return the itemSetNameTD
	 */
	public String getItemSetNameTD() {
		return itemSetNameTD;
	}
	/**
	 * @param itemSetNameTD the itemSetNameTD to set
	 */
	public void setItemSetNameTD(String itemSetNameTD) {
		this.itemSetNameTD = itemSetNameTD;
	}
	/**
	 * @return the itemSetIdTD
	 */
	public Integer getItemSetIdTD() {
		return itemSetIdTD;
	}
	/**
	 * @param itemSetIdTD the itemSetIdTD to set
	 */
	public void setItemSetIdTD(Integer itemSetIdTD) {
		this.itemSetIdTD = itemSetIdTD;
	}
	/**
	 * @return the completionStatusTD
	 */
	public String getCompletionStatusTD() {
		return completionStatusTD;
	}
	/**
	 * @param completionStatusTD the completionStatusTD to set
	 */
	public void setCompletionStatusTD(String completionStatusTD) {
		this.completionStatusTD = completionStatusTD;
	}
	/**
	 * @return the testIds
	 */
	public String getTestIds() {
		return testIdsToBeShown;
	}
	/**
	 * @param testIds the testIds to set
	 */
	public void setTestIds(String testIds) {
		this.testIdsToBeShown = testIds;
	}
	/**
	 * @param responseStatus the responseStatus to set
	 */
	public void setResponseStatus(int pointsObtained, int pointsPossible) {
		this.responseStatus = "Total Points Earned "+pointsObtained+" out of "+pointsPossible;
	}
	/**
	 * @return the responseStatus
	 */
	public String getResponseStatus() {
		return responseStatus;
	}
	
	public static class DeliveryUnitElement implements Serializable{
		static final long serialVersionUID = 1L;
		private Integer itemSetIdTD;
		private String itemSetNameTD;
		private String completionStatus;
		private Integer totalItemCounts;
		
		
		public DeliveryUnitElement(Integer itemSetIdTD, String itemSetNameTD, String completionStatus) {
			this.itemSetIdTD = itemSetIdTD;
			this.itemSetNameTD = itemSetNameTD;
			this.completionStatus = completionStatus;
		}
		/**
		 * @return the itemSetIdTD
		 */
		public Integer getItemSetIdTD() {
			return itemSetIdTD;
		}
		/**
		 * @param itemSetIdTD the itemSetIdTD to set
		 */
		public void setItemSetIdTD(Integer itemSetIdTD) {
			this.itemSetIdTD = itemSetIdTD;
		}
		/**
		 * @return the itemSetNameTD
		 */
		public String getItemSetNameTD() {
			return itemSetNameTD;
		}
		/**
		 * @param itemSetNameTD the itemSetNameTD to set
		 */
		public void setItemSetNameTD(String itemSetNameTD) {
			this.itemSetNameTD = itemSetNameTD;
		}
		/**
		 * @return the completionStatus
		 */
		public String getCompletionStatus() {
			return completionStatus;
		}
		/**
		 * @param completionStatus the completionStatus to set
		 */
		public void setCompletionStatus(String completionStatus) {
			this.completionStatus = completionStatus;
		}
		/**
		 * @return the totalItemCounts
		 */
		public Integer getTotalItemCounts() {
			return totalItemCounts;
		}
		/**
		 * @param totalItemCounts the totalItemCounts to set
		 */
		public void setTotalItemCounts(Integer totalItemCounts) {
			this.totalItemCounts = totalItemCounts;
		}
		
	}
	
	public static class ResponseResultDetails implements Serializable {
		static final long serialVersionUID = 1L;
		String itemId;
		Integer itemOrder;
		String rawScore;
		String response;
		String itemType;
		String itemAnswerArea;
		Integer itemSetIdTD;
		String itemSetNameTD;
		String completionStatusTD;
		Integer itemSetIdTS;
		String contentDomain;
		String pdfResponse;
		
		public ResponseResultDetails() {
			super();
		}
		public ResponseResultDetails(ItemResponseAndScore obj) {
			super();
			this.itemId = obj.getItemId();
			this.itemOrder = obj.getItemOrder();
			this.rawScore = obj.getRawScore();
			this.response = obj.getResponse();
			this.itemType = obj.getItemType();
			this.itemAnswerArea = obj.getItemAnswerArea();
			this.itemSetIdTD = obj.getItemSetIdTD();
			this.itemSetNameTD = obj.getItemSetNameTD();
			this.completionStatusTD = obj.getCompletionStatusTD();
			this.itemSetIdTS = obj.getItemSetIdTS();
			this.contentDomain = obj.getContentDomain();
			this.pdfResponse = obj.getPdfResponse();
		}
		
		public Integer getItemOrder() {
			return itemOrder;
		}
		public String getRawScore() {
			return rawScore;
		}
		public String getResponse() {
			return response;
		}
		public String getItemType() {
			return itemType;
		}
		public String getContentDomain() {
			return contentDomain;
		}
		public String getPdfResponse() {
			return pdfResponse;
		}
		public void setPdfResponse(String pdfResponse) {
			this.pdfResponse = pdfResponse;
		}
		public String getItemId() {
			return itemId;
		}
		public String getItemAnswerArea() {
			return itemAnswerArea;
		}
		public Integer getItemSetIdTD() {
			return itemSetIdTD;
		}
		public String getItemSetNameTD() {
			return itemSetNameTD;
		}
		public String getCompletionStatusTD() {
			return completionStatusTD;
		}
		public Integer getItemSetIdTS() {
			return itemSetIdTS;
		}
		
		public static class OrderByItemOrder implements Comparator<ResponseResultDetails> {
			@Override
			public int compare(ResponseResultDetails s1, ResponseResultDetails s2) {
				return s1.itemOrder.compareTo(s2.itemOrder);
			}
		}
	}
	
	public ScoreDetails() {
		super();
	}
	
	public ScoreDetails(Integer itemSetId, String itemSetName,
			String itemSetType, String itemSetOrder, String itemSetLevel,
			String accessCode, String testSessionName,
			String testName, String productType) {
		super();
		this.itemSetId = itemSetId;
		this.itemSetName = itemSetName;
		this.itemSetType = itemSetType;
		this.itemSetOrder = itemSetOrder;
		this.itemSetLevel = itemSetLevel;
		this.accessCode = accessCode;
		this.testSessionName = testSessionName;
		this.testName = testName;
		this.productType = productType;
	}
	
} 
