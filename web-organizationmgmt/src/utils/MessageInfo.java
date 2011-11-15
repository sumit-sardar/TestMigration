package utils; 

import java.util.ArrayList;
import java.util.List;

import com.ctb.bean.testAdmin.Node;

public class MessageInfo {
	private boolean errorFlag = false;
	private boolean successFlag = false;
	private String title ;
	private String content ;
	private String message ;
	private String type ;
	private Node organizationDetail;
	private Boolean isEdit = Boolean.FALSE;
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
	 * @return the organizationDetail
	 */
	public Node getOrganizationDetail() {
		return organizationDetail;
	}
	/**
	 * @param organizationDetail the organizationDetail to set
	 */
	public void setOrganizationDetail(Node organizationDetail) {
		this.organizationDetail = organizationDetail;
	}
	public Boolean getIsEdit() {
		return isEdit;
	}
	public void setIsEdit(Boolean isEdit) {
		this.isEdit = isEdit;
	}	

}
