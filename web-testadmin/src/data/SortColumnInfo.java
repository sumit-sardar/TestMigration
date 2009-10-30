package data; 

public class SortColumnInfo implements java.io.Serializable
{ 
    static final long serialVersionUID = 1L;

    private String id;
    private String sortColumn;
    private String sortOrder;
    private String previousSortColumn;
    private String action;

    public SortColumnInfo() 
    {
        this.id = null;
        this.sortColumn = null;
        this.sortOrder = null;
        this.previousSortColumn = null;
        this.action = null;
    }
    public SortColumnInfo(String id, String sortColumn, String action) 
    {
        this.id = id;
        this.sortColumn = sortColumn;
        this.sortOrder = "ascending";
        this.previousSortColumn = sortColumn;
        this.action = action;
    }
    public void setId(String id) 
    {
        this.id = id;
    }    
    public String getId() 
    {
        return this.id;
    }
    public void setSortColumn(String sortColumn) 
    {
        this.sortColumn = sortColumn;
    }    
    public String getSortColumn() {
        return this.sortColumn;
    }
    public void setSortOrder(String sortOrder) 
    {
        this.sortOrder = sortOrder;
    }    
    public String getSortOrder() 
    {
        return this.sortOrder;
    }
    public void setAction(String action) 
    {
        this.action = action;
    }    
    public String getAction() 
    {
        return this.action;
    }    
    public void setPreviousSortColumn(String previousSortColumn) 
    {
        this.previousSortColumn = previousSortColumn;
    }    
    public String getPreviousSortColumn() 
    {
        return this.previousSortColumn;
    }
    public void update(String actionId) 
    {
        if (! actionId.equals(this.id))
            return;
            
        if (this.sortColumn.equals(this.previousSortColumn)) {
            if (this.sortOrder.equals("ascending"))
                this.sortOrder = "descending";
            else
                this.sortOrder = "ascending";
        }
        else {
            this.sortOrder = "ascending";
        }
        this.previousSortColumn = this.sortColumn;
    }
} 
