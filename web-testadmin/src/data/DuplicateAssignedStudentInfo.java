package data; 

import java.io.Serializable;
import java.util.List;

public class DuplicateAssignedStudentInfo implements Serializable 
{ 
    static final long serialVersionUID = 1L;
    
    private Integer id = null;
    private String firstName = null;
    private String lastName = null;
    private String middleName = null;
    private List orgNodeIds = null;
    private List orgNodeNames = null;
    private String selectedOrgNodeName = null;
    private String categoryName = null;
    
    public DuplicateAssignedStudentInfo() {
    }
        
    public Integer getId() {
        return this.id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getMiddleName() {
        return middleName;
    }
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    public List getOrgNodeIds() {
        return orgNodeIds;
    }
    public void setOrgNodeIds(List orgNodeIds) {
        this.orgNodeIds = orgNodeIds;
    }
    public List getOrgNodeNames() {
        return orgNodeNames;
    }
    public void setOrgNodeNames(List orgNodeNames) {
        this.orgNodeNames = orgNodeNames;
    }
    public String getSelectedOrgNodeName() {
        return selectedOrgNodeName;
    }
    public void setSelectedOrgNodeName(String selectedOrgNodeName) {
        this.selectedOrgNodeName = selectedOrgNodeName;
    }
    public String getCategoryName() {
        return this.categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}  
