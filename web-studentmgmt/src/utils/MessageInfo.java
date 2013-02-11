package utils; 

import java.util.Map;

public class MessageInfo {
	private boolean errorFlag = false;
	private boolean successFlag = false;
	private String title ;
	private String content ;
	private String message ;
	private String type ;
	private Integer studentId ;
	private String studentLoginId ;
	private String hasAccommodation = "No";
	private Map additionalInfoMap;
	private Integer status;

	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the errorFlag
	 */
	public boolean isErrorFlag() {
		return errorFlag;
	}
	/**
	 * @param errorFlag the errorFlag to set
	 */
	public void setErrorFlag(boolean errorFlag) {
		this.errorFlag = errorFlag;
	}
	/**
	 * @return the successFlag
	 */
	public boolean isSuccessFlag() {
		return successFlag;
	}
	/**
	 * @param successFlag the successFlag to set
	 */
	public void setSuccessFlag(boolean successFlag) {
		this.successFlag = successFlag;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the studentId
	 */
	public Integer getStudentId() {
		return studentId;
	}
	/**
	 * @param studentId the studentId to set
	 */
	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}
	/**
	 * @return the studentLoginId
	 */
	public String getStudentLoginId() {
		return studentLoginId;
	}
	/**
	 * @param studentLoginId the studentLoginId to set
	 */
	public void setStudentLoginId(String studentLoginId) {
		this.studentLoginId = studentLoginId;
	}
	/**
	 * @return the hasAccommodation
	 */
	public String getHasAccommodation() {
		return hasAccommodation;
	}
	/**
	 * @param hasAccommodation the hasAccommodation to set
	 */
	public void setHasAccommodation(String hasAccommodation) {
		this.hasAccommodation = hasAccommodation;
	}
	public Map getAdditionalInfoMap() {
		return additionalInfoMap;
	}
	public void setAdditionalInfoMap(Map additionalInfoMap) {
		this.additionalInfoMap = additionalInfoMap;
	}
	
}
