package comm.ctb.bean;

import java.util.List;

public class ItemDataPointDetailsVO {
	private String itemId;
	private String dataPoint;
	private String itemNo;
	private String finalScore;
	private String alertCode;
	private List readDetailsList;
	
	/**
	 * @return the itemId
	 */
	public String getItemId() {
		return itemId;
	}
	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	/**
	 * @return the dataPoint
	 */
	public String getDataPoint() {
		return dataPoint;
	}
	/**
	 * @param dataPoint the dataPoint to set
	 */
	public void setDataPoint(String dataPoint) {
		this.dataPoint = dataPoint;
	}
	/**
	 * @return the itemNo
	 */
	public String getItemNo() {
		return itemNo;
	}
	/**
	 * @param itemNo the itemNo to set
	 */
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	/**
	 * @return the finalScore
	 */
	public String getFinalScore() {
		return finalScore;
	}
	/**
	 * @param finalScore the finalScore to set
	 */
	public void setFinalScore(String finalScore) {
		this.finalScore = finalScore;
	}
	/**
	 * @return the alertCode
	 */
	public String getAlertCode() {
		return alertCode;
	}
	/**
	 * @param alertCode the alertCode to set
	 */
	public void setAlertCode(String alertCode) {
		this.alertCode = alertCode;
	}
	/**
	 * @return the readDetailsList
	 */
	public List getReadDetailsList() {
		return readDetailsList;
	}
	/**
	 * @param readDetailsList the readDetailsList to set
	 */
	public void setReadDetailsList(List readDetailsList) {
		this.readDetailsList = readDetailsList;
	}
	
}
