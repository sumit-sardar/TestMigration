package utils; 

import java.util.ArrayList;
import java.util.List;

public class BaseTree {
	
	private List<TreeData> data = new ArrayList<TreeData> ();
	
	private Integer leafNodeCategoryId;
	
	private Boolean isLeafNodeAdmin = Boolean.FALSE;
	
	

	/**
	 * @return the isLeafNodeAdmin
	 */
	public Boolean getIsLeafNodeAdmin() {
		return isLeafNodeAdmin;
	}

	/**
	 * @param isLeafNodeAdmin the isLeafNodeAdmin to set
	 */
	public void setIsLeafNodeAdmin(Boolean isLeafNodeAdmin) {
		this.isLeafNodeAdmin = isLeafNodeAdmin;
	}

	/**
	 * @return the leafNodeCategoryId
	 */
	public Integer getLeafNodeCategoryId() {
		return leafNodeCategoryId;
	}

	/**
	 * @param leafNodeCategoryId the leafNodeCategoryId to set
	 */
	public void setLeafNodeCategoryId(Integer leafNodeCategoryId) {
		this.leafNodeCategoryId = leafNodeCategoryId;
	}

	public List<TreeData> getData() {
		return data;
	}

	public void setData(List<TreeData> data) {
		this.data = data;
	}

	
	

}
