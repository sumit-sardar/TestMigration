package dto;

/**
 * This object contains information for sub-tests which describes a test structure.   
 * The values which required from ACUITY through input are: 
 * 				id, name, order   
 * The values which OAS platform populated in return are:  
 * 				accessCode
 * 
 * @author Tai_Truong
 */
public class Subtest implements java.io.Serializable {
    static final long serialVersionUID = 1L;

    private Integer id = null;
    private String name = null;			// 512 chars
    private String accessCode = null;	// 32 chars
    private Integer order = null;
    private Integer duration = null;

	public Subtest() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccessCode() {
		return accessCode;
	}

	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}
    
	
}
