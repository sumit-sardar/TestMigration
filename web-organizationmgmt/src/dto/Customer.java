package dto; 


/**
 *@author Tata Consultancy Services
 * Customer class  have the customer ID,Code,State and Name informations. 
 */

public class Customer implements java.io.Serializable
{ 
    static final long serialVersionUID = 1L;

    private Integer id = new Integer(0);
    private String name = "";
    private String code = "";
    private String state = "";
    
    public Customer() {
        this.id = new Integer(0);
        this.name = "";
        this.code = "";
        this.state = "";
    }

    public Customer(Integer id, String name, String code, String state) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.state = state;
    }
    
	public Integer getId() {
		return this.id != null ? this.id : new Integer(0);
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return this.name != null ? this.name : "";
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return this.code != null ? this.code : "";
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getState() {
		return this.state != null ? this.state : "";
	}
	public void setState(String state) {
		this.state = state;
	}
    
} 