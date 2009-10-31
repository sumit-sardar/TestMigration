package dto; 

/**
 *@author Tata Consultancy Services 
 * PathNode class Deals with organization path lists.
 */

public class PathNode implements java.io.Serializable 
{ 
    static final long serialVersionUID = 1L;

    private Integer id = null;
    private String name = null;
    private Integer childrenNodeCount = null;
    private Integer sessionCount = null;
    private Integer studentCount = null;
    private String categoryName = null;
    private String selectable = null;
    private String fullPathName = null;
    private String actionPermission = null;
    
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
    public Integer getSessionCount() {
        return this.sessionCount;
    }
    public void setSessionCount(Integer sessionCount) {
        this.sessionCount = sessionCount;
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
        
        if ( ( this.childrenNodeCount != null) 
                && ( this.childrenNodeCount.intValue() > 0 ) ) {
             
            return "true";
            
        }
            
        else {
            
            return "false";
            
        }
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
    
    /**
	 * @return Returns the actionPermission.
	 */
	public String getActionPermission() {
		return this.actionPermission != null ? this.actionPermission : "TFFT";
	}

	/**
	 * @param actionPermission The actionPermission to set.
	 */
	public void setActionPermission(String actionPermission) {
		this.actionPermission = actionPermission;
	}
    
} 
