package dto; 

/**
 *@author Tata Consultancy Services 
 */

import com.ctb.bean.testAdmin.RosterElement;
import java.util.List;
import utils.FilterSortPageUtils;

public class StudentSubtestInfo implements java.io.Serializable 
{
    static final long serialVersionUID = 1L;

    private String name = null;
    
    public StudentSubtestInfo() {
    }   
     
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
} 
