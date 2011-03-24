package data; 

import com.ctb.util.web.sanitizer.JavaScriptSanitizer;
import com.ctb.util.web.sanitizer.SanitizedFormField;

public class StatePathList extends SanitizedFormField  
{ 
    static final long serialVersionUID = 1L;

    private Integer pageRequested;
    private Integer maxPageRequested;
    private String sortOrderBy;
    private String sortColumn;

    public StatePathList(String sortOrderBy, String sortColumn) 
    {
        this.pageRequested = new Integer(1);
        this.maxPageRequested = new Integer(1);        
        this.sortOrderBy = sortOrderBy;
        this.sortColumn = sortColumn;
    }    


    public Integer getPageRequested() {
    	//Changed for the defect of table pager text box
        return this.pageRequested ==null || this.pageRequested == 0  ? 1 : this.pageRequested ;
    }
    public void setPageRequested(Integer pageRequested) {
        this.pageRequested = pageRequested;
    }

    public Integer getMaxPageRequested() {
        return this.maxPageRequested;
    }
    public void setMaxPageRequested(Integer maxPageRequested) {
        this.maxPageRequested = maxPageRequested;
    }

    public String getSortOrderBy() {
        return JavaScriptSanitizer.sanitizeInput(this.sortOrderBy);
    }
    public void setSortOrderBy(String sortOrderBy) {
        this.sortOrderBy = sortOrderBy;
    }

    public String getSortColumn() {
        
        return JavaScriptSanitizer.sanitizeInput(this.sortColumn);
    }
    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }


    public void resetValues(String sortOrderBy, String sortColumn) {
        this.pageRequested = new Integer(1);
        this.sortOrderBy = sortOrderBy;
        this.sortColumn = sortColumn;
    }
    
    public void validateValues(String sortOrderBy, String sortColumn) {
        if (this.sortOrderBy == null)
            this.sortOrderBy = sortOrderBy;
            
        if (this.sortColumn == null)
            this.sortColumn = sortColumn;

        if (this.maxPageRequested == null)
            this.maxPageRequested = new Integer(1);
            
        if (this.pageRequested == null)
            this.pageRequested = new Integer(1);
        else if (this.pageRequested.intValue() <= 0)
            this.pageRequested = new Integer(1);
        else if (this.pageRequested.intValue() > this.maxPageRequested.intValue())
            this.pageRequested = this.maxPageRequested;
    }
    
} 
