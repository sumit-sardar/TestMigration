package data; 

import java.io.Serializable;

public class OrganizationVO implements Serializable
{ 
    static final long serialVersionUID = 1L;

    private Integer id = null;
    private String organizationName = null;
    private Integer filteredCount = null;
    private Integer selectedCount = null; 
        
    public OrganizationVO(Integer id, String organizationName, 
        Integer filteredCount, Integer selectedCount) {
        this.id = id;
        this.organizationName = organizationName;
        this.filteredCount = filteredCount;
        this.selectedCount = selectedCount;
    }
    
    public OrganizationVO() {
    }
    
    public Integer getId() {
        return this.id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getOrganizationName() {
        return this.organizationName;
    }
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }   
    
    public Integer getFilteredCount() {
        return this.filteredCount;
    }
    public void setFilteredCount(Integer filteredCount) {
        this.filteredCount = filteredCount;
    }

    public Integer getSelectedCount() {
        return this.selectedCount;
    }
    public void setSelectedCount(Integer selectedCount) {
        this.selectedCount = selectedCount;
    }
         
} 
