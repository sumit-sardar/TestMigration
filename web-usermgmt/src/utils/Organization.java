package utils; 

public class Organization {
	
	private Integer orgNodeId;
	private Integer orgParentNodeId;
	private String orgName;
	private Integer orgCategoryLevel;
	private Integer customerId;
	private Boolean isAssociate = new Boolean (true);
	
	
	
	public Boolean getIsAssociate() {
		return isAssociate;
	}
	public void setIsAssociate(Boolean isAssociate) {
		this.isAssociate = isAssociate;
	}
	public Integer getOrgNodeId() {
		return orgNodeId;
	}
	public void setOrgNodeId(Integer orgNodeId) {
		this.orgNodeId = orgNodeId;
	}
	public Integer getOrgParentNodeId() {
		return orgParentNodeId;
	}
	public void setOrgParentNodeId(Integer orgParentNodeId) {
		this.orgParentNodeId = orgParentNodeId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public Integer getOrgCategoryLevel() {
		return orgCategoryLevel;
	}
	public void setOrgCategoryLevel(Integer orgCategoryLevel) {
		this.orgCategoryLevel = orgCategoryLevel;
	}
	
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		if((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		// object must be Test at this point
		Organization org = (Organization)obj;
		return (orgNodeId != null && orgNodeId.equals(org.getOrgNodeId()));
	}

	public int hashCode()
	{
		int hash = orgNodeId;

		return hash;
	}
	/**
	 * @return the customerId
	 */
	public Integer getCustomerId() {
		return customerId;
	}
	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
}
