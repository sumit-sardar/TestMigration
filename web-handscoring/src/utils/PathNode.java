package utils; 

public class PathNode implements java.io.Serializable 
{ 
    static final long serialVersionUID = 1L;

    private Integer id = null;
    private String name = null;
    private Integer filteredCount = null;
    private Integer selectedCount = null;
    private Integer childNodeCount = null;
    private String hasChildren = null;
    private String categoryName = null;
    private String clickable = null;
    
    public PathNode() 
    {
    }    
    
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
    public Integer getFilteredCount() {
        return this.filteredCount;
    }
    public void setFilteredCount(Integer filteredCount) {
        if (filteredCount == null)
            filteredCount = new Integer(0);
        this.filteredCount = filteredCount;
    }
    public Integer getSelectedCount() {
        return this.selectedCount;
    }
    public void setSelectedCount(Integer selectedCount) {
        if (selectedCount == null)
            selectedCount = new Integer(0);
        this.selectedCount = selectedCount;
    }
    public Integer getChildNodeCount() {
        return this.childNodeCount;
    }
    public void setChildNodeCount(Integer childNodeCount) {
        this.childNodeCount = childNodeCount;
    }    
    public String getHasChildren() {
        return this.hasChildren;
    }
    public void setHasChildren(String hasChildren) {
        this.hasChildren = hasChildren;
    }
    public String getCategoryName() {
        return this.categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
     
    public String getClickable() {
        if (this.clickable == null) {
            if ((this.hasChildren != null) && this.hasChildren.equals("true"))  
                this.clickable = "true";
            else
                this.clickable = "false";
        }
        return this.clickable;
    }
    public void setClickable(String clickable) {
        this.clickable = clickable;
    }
} 
