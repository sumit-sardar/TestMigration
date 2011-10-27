package utils;

import java.util.List;

import dto.OrganizationProfileInformation;

public class Base {
	
	private String page;
	private String total;
	private String records;
	private List<Row> rows;
	private List<OrganizationProfileInformation> organizationProfileInformation;
	
	/**
	 * @return the page
	 */
	public String getPage() {
		return page;
	}
	/**
	 * @param page the page to set
	 */
	public void setPage(String page) {
		this.page = page;
	}
	/**
	 * @return the total
	 */
	public String getTotal() {
		return total;
	}
	/**
	 * @param total the total to set
	 */
	public void setTotal(String total) {
		this.total = total;
	}
	/**
	 * @return the records
	 */
	public String getRecords() {
		return records;
	}
	/**
	 * @param rows the rows to set
	 */
	public void setRecords(String records) {
		this.records = records;
	}
	/**
	 * @return the rows
	 */
	public List<Row> getRows() {
		return rows;
	}
	/**
	 * @param rows the rows to set
	 */
	public void setRows(List<Row> rows) {
		this.rows = rows;
	}
	/**
	 * @return the organizationProfileInformation
	 */
	public List<OrganizationProfileInformation> getOrganizationProfileInformation() {
		return organizationProfileInformation;
	}
	/**
	 * @param organizationProfileInformation the organizationProfileInformation to set
	 */
	public void setOrganizationProfileInformation(
			List<OrganizationProfileInformation> organizationProfileInformation) {
		this.organizationProfileInformation = organizationProfileInformation;
	}
	
	
	

}
