package dto; 

/**
* Organization information in OAS hierarchy
* 
* @author Tai_Truong
*/
public class OrgNode implements java.io.Serializable 
{ 
    static final long serialVersionUID = 1L;

    private Integer id = null;
    private String name = null;

    public OrgNode(Integer id, String name) {
    	this.id = id;
    	this.name = name;
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
    
} 
