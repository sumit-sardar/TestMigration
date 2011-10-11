package utils;

import java.util.List;

import dto.UserProfileInformation;

public class Base {
	
	private String page;
	private String total;
	private String records;
	private List<Row> rows;
	private List<UserProfileInformation> userProfileInformation;
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getRecords() {
		return records;
	}
	public void setRecords(String records) {
		this.records = records;
	}
	public List<Row> getRows() {
		return rows;
	}
	public void setRows(List<Row> rows) {
		this.rows = rows;
	}
	/**
	 * @return the userProfileInformation
	 */
	public List<UserProfileInformation> getUserProfileInformation() {
		return userProfileInformation;
	}
	/**
	 * @param userProfileInformation the userProfileInformation to set
	 */
	public void setUserProfileInformation(
			List<UserProfileInformation> userProfileInformation) {
		this.userProfileInformation = userProfileInformation;
	}
	
	
	

}
