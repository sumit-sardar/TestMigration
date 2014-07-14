package com.ctb.bean;

import java.util.Date;

public class StudentFileRow extends CTBBean {
	static final long serialVersionUID = 1L;
	private String key;
	private Integer studentId;
	private String userName;
	private Integer customerId;
	private Node[] organizationNodes;

	/*
	 * private String firstName; private String middleName; private String
	 * lastName; private Date birthdate; private String headerDateOfBirth;
	 * private String gender; private String grade; private String extElmId;
	 */
	private String extPin1;
	private String extPin2;
	private StudentDemoGraphics []studentDemoGraphics;

	/*
	 * private String extPin2; private String screenReader; private String
	 * calculator; private String testPause; private String untimedTest; private
	 * String questionBackgroundColor; private String questionFontColor; private
	 * String questionFontSize; private String answerBackgroundColor; private
	 * String answerFontColor; private String answerFontSize; private String
	 * highlighter; private Integer customerId; private Node[]
	 * organizationNodes; private StudentDemoGraphics []studentDemoGraphics;
	 */
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
