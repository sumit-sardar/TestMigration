package com.ctb.bean;

/**
 * Data bean used to process UserFile Bean
 * 
 * @author TCS
 * 
 */
public class UserFile extends CTBBean {
	static final long serialVersionUID = 1L;
	private UserFileRow[] userFileRows;

	public UserFileRow[] getUserFileRows() {
		return userFileRows;
	}

	public void setUserFileRows(UserFileRow[] userFileRows) {
		this.userFileRows = userFileRows;
	}

}
