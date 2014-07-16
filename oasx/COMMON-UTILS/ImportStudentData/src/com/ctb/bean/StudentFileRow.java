package com.ctb.bean;

/**
 * Data bean representing the Student-data fetched from Database
 * @author TCS
 *
 */
public class StudentFileRow extends CTBBean {
	static final long serialVersionUID = 1L;
	private String key;
	private Integer studentId;
	private String userName;
	private Integer customerId;
	private Node[] organizationNodes;
	private String extPin1;
	private String extPin2;
	private StudentDemoGraphics []studentDemoGraphics;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getExtPin1() {
		return extPin1;
	}

	public void setExtPin1(String extPin1) {
		this.extPin1 = extPin1;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Node[] getOrganizationNodes() {
		return organizationNodes;
	}

	public void setOrganizationNodes(Node[] organizationNodes) {
		this.organizationNodes = organizationNodes;
	}

	public String getExtPin2() {
		return extPin2;
	}

	public void setExtPin2(String extPin2) {
		this.extPin2 = extPin2;
	}

	public StudentDemoGraphics[] getStudentDemoGraphics() {
		return studentDemoGraphics;
	}

	public void setStudentDemoGraphics(StudentDemoGraphics[] studentDemoGraphics) {
		this.studentDemoGraphics = studentDemoGraphics;
	}

}
