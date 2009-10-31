package dto; 



public class OrgNavInfo implements java.io.Serializable
{ 
    static final long serialVersionUID = 1L;

    private String action;
    private Integer id; 
    private String sortColumn;
    private String sortOrderBy;
    private Integer pageRequested;
    private Integer maxPage;
    
    public OrgNavInfo() {
        this.action = "default";
        this.id = new Integer(0);
        this.sortColumn = "OrgNodeName";
        this.sortOrderBy = "asc";
        this.pageRequested = new Integer(1);
        this.maxPage = new Integer(0);
    }

    public OrgNavInfo(String action, Integer id, String sortColumn, String sortOrderBy, Integer pageRequested, Integer maxPage) {
        this.action = action;
        this.id = id;
        this.sortColumn = sortColumn;
        this.sortOrderBy = sortOrderBy;
        this.pageRequested = pageRequested;
        this.maxPage = maxPage;
    }

    public void setAction(String action)
    {
        this.action = action;
    }
    
    public String getAction()
    {
        return this.action;
    } 

    public void setId(Integer id)
    {
        this.id = id;
    }
    
    public Integer getId()
    {
        return this.id;
    } 

    public void setSortColumn(String sortColumn)
    {
        this.sortColumn = sortColumn;
    }
    
    public String getSortColumn()
    {
        return this.sortColumn;
    } 
          
    public void setSortOrderBy(String sortOrderBy)
    {
        this.sortOrderBy = sortOrderBy;
    }
    
    public String getSortOrderBy()
    {
        return this.sortOrderBy;
    }     
      
    public void setPageRequested(Integer pageRequested)
    {
        this.pageRequested = pageRequested;
    }
    public Integer getPageRequested()
    {
        return this.pageRequested;
    }     
       
    public void setMaxPage(Integer maxPage)
    {
        this.maxPage = maxPage;
    }
    
    public Integer getMaxPage()
    {
        return this.maxPage;
    }        
    
} 