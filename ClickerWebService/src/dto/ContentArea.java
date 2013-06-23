package dto; 

/**
* ContentArea information
* subtests is a list of subtests in this content area
*
* @author Tai_Truong
*/
public class ContentArea implements java.io.Serializable 
{ 
    static final long serialVersionUID = 1L;
    
    private Integer contentAreaId = null;
    private String contentAreaName = null;
    private String accessCode = null;
    private SubtestInfo[] subtests = null;
    
    public ContentArea() {
    	this.contentAreaId = null;
    	this.contentAreaName = null;
    	this.accessCode = null;
    	this.subtests = null;    	
    }

    public ContentArea(Integer contentAreaId, String contentAreaName, String accessCode, SubtestInfo[] subtests) {
    	this.contentAreaId = contentAreaId;
    	this.contentAreaName = contentAreaName;
    	this.accessCode = accessCode;
    	this.subtests = subtests;
    }

	public Integer getContentAreaId() {
		return contentAreaId;
	}

	public void setContentAreaId(Integer contentAreaId) {
		this.contentAreaId = contentAreaId;
	}

	public String getContentAreaName() {
		return contentAreaName;
	}

	public void setContentAreaName(String contentAreaName) {
		this.contentAreaName = contentAreaName;
	}

	public String getAccessCode() {
		return accessCode;
	}

	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}

	public SubtestInfo[] getSubtests() {
		return subtests;
	}

	public void setSubtests(SubtestInfo[] subtests) {
		this.subtests = subtests;
	}
    
    
} 
