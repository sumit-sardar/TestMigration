package dto; 

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PathNode implements java.io.Serializable 
{ 
    static final long serialVersionUID = 1L;

    private Integer id = null;
    private String name = null;
    private Integer childrenNodeCount = null;
    private Integer studentCount = null;
    private String categoryName = null;
    private String selectable = null;
    private String fullPathName = null;
	 //Bulk accommodation
	private String clickable;
	private Integer filteredCount = null;
	private String hasChildren = null;
	private Integer selectedCount = null;
    
    public PathNode() {}
    
    public Integer getId() {
        return this.id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getStudentCount() {
        return this.studentCount;
    }
    public void setStudentCount(Integer studentCount) {
        this.studentCount = studentCount;
    }
    public Integer getChildrenNodeCount() {
        return this.childrenNodeCount;
    }
    public void setChildrenNodeCount(Integer childrenNodeCount) {
        this.childrenNodeCount = childrenNodeCount;
    }
    public String getHasChildren() {
        
        if ((this.childrenNodeCount != null) && (this.childrenNodeCount.intValue() > 0)) 
            return "true";
        else
            return "false";
    }
    public String getCategoryName() {
        return this.categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public void setSelectable(String selectable) {
        this.selectable = selectable;
    }
    public String getSelectable() {
        return this.selectable != null ? this.selectable : "false";
    }
    public String getFullPathName() {
        return this.fullPathName;
    }
    public void setFullPathName(String fullPathName) {
        this.fullPathName = fullPathName;
    }
	 //Bulk Accommodation
    public String getClickable() {
        if (this.clickable == null) {
            if ((this.childrenNodeCount != null) && (this.childrenNodeCount.intValue() > 0 &&
            		(this.filteredCount != null) && (this.filteredCount.intValue() > 0)))
                this.clickable = "true";
            else
                this.clickable = "false";
        }
        return this.clickable;
    }
    public void setClickable(String clickable) {
        this.clickable = clickable;
    }

	/**
	 * @return the filteredCount
	 */
	public Integer getFilteredCount() {
		return filteredCount;
	}

	/**
	 * @param filteredCount the filteredCount to set
	 */
	public void setFilteredCount(Integer filteredCount) {
		this.filteredCount = filteredCount;
	}

	/**
	 * @param hasChildren the hasChildren to set
	 */
	public void setHasChildren(String hasChildren) {
		this.hasChildren = hasChildren;
	}

	/**
	 * @return the selectedCount
	 */
	public Integer getSelectedCount() {
		return selectedCount;
	}

	/**
	 * @param selectedCount the selectedCount to set
	 */
	public void setSelectedCount(Integer selectedCount) {
		this.selectedCount = selectedCount;
	}
    
    
} 
