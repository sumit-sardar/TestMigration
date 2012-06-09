package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import java.util.Date;

public class StudentFileRow extends CTBBean
{ 
        static final long serialVersionUID = 1L;
        private Integer studentId;
	    private String userName;
	    private String password;
	    private String firstName;
	    private String middleName;
	    private String lastName;
        private String displayStudentName;
	    private String preferredName;
	    private String prefix;
	    private String suffix;
	    private Date birthdate;
        private String headerDateOfBirth;
	    private String gender;
	    private String ethnicity;
	    private String email;
	    private String grade;
	    private String extElmId;
	    private String extPin1;
	    private String extPin2;
	    private String extPin3;
	    private String extSchoolId;
	    private String activeSession;
	    private String potentialDuplicatedStudent;
	    private Integer createdBy;
	    private Date createdDateTime;
	    private Integer updatedBy;
	    private Date updatedDateTime;
	    private String activationStatus;
	    private Integer dataImportHistoryId;
	    private Integer customerId;
	    private Integer precodeId;
	    private String barcode;
	    private String udf;
	    private String udf1;
	    private String udf2;
	    private String studentGrade;
	    private String screenMagnifier;
	    private String screenReader;
	    private String calculator; 
	    private String testPause;
	    private String untimedTest;
	    private String questionBackgroundColor;
	    private String questionFontColor;
	    private String questionFontSize;
	    private String answerBackgroundColor;
	    private String answerFontColor;
	    private String answerFontSize;
	    private String colorFontAccommodation;
	    private String highlighter;
        private String contactName;
        private String address1;
        private String address2;
        private String address3;
        private String timeZone;
        private String city;
        private String state;
        private String zip;
        private String primaryPhone;
        private String secondaryPhone ;
        private String faxNumber;
	    private Integer orgNodeId;
	    private Node[] organizationNodes;
        private StudentDemoGraphics []studentDemoGraphics;
        private StudentDemoGraphicsData []studentDemoGraphicsData;
        private CustomerConfig []customerConfig ;
        private String primarySort;
        private String secondarySort;
        
        private DataFileRowError[] dataFilerowError;
        
        
        public CustomerConfig []getCustomerConfig() {
            return customerConfig;
        }
    
        public void setCustomerConfig(CustomerConfig []customerConfig) {
            this.customerConfig = customerConfig;
        }

        public StudentDemoGraphicsData []getStudentDemoGraphicsData() {
            return studentDemoGraphicsData;
        }
    
        public void setStudentDemoGraphicsData(StudentDemoGraphicsData []studentDemoGraphicsData) {
            this.studentDemoGraphicsData = studentDemoGraphicsData;
        }
		/**
		 * @return Returns the activationStatus.
		 */


        public StudentDemoGraphics[] getStudentDemoGraphics() {
            return studentDemoGraphics;
        }
    
        public void setStudentDemoGraphics(StudentDemoGraphics[] studentDemoGraphics) {
            this.studentDemoGraphics = studentDemoGraphics;
        }
		/**
		 * @return Returns the activationStatus.
		 */
		public String getActivationStatus() {
			return activationStatus;
		}
		/**
		 * @param activationStatus The activationStatus to set.
		 */
		public void setActivationStatus(String activationStatus) {
			this.activationStatus = activationStatus;
		}
		/**
		 * @return Returns the activeSession.
		 */
		public String getActiveSession() {
			return activeSession;
		}
		/**
		 * @param activeSession The activeSession to set.
		 */
		public void setActiveSession(String activeSession) {
			this.activeSession = activeSession;
		}
		/**
		 * @return Returns the answerBackgroundColor.
		 */
		public String getAnswerBackgroundColor() {
			return answerBackgroundColor;
		}
		/**
		 * @param answerBackgroundColor The answerBackgroundColor to set.
		 */
		public void setAnswerBackgroundColor(String answerBackgroundColor) {
			this.answerBackgroundColor = answerBackgroundColor;
		}
		/**
		 * @return Returns the answerFontColor.
		 */
		public String getAnswerFontColor() {
			return answerFontColor;
		}
		/**
		 * @param answerFontColor The answerFontColor to set.
		 */
		public void setAnswerFontColor(String answerFontColor) {
			this.answerFontColor = answerFontColor;
		}
		/**
		 * @return Returns the answerFontSize.
		 */
		public String getAnswerFontSize() {
			return answerFontSize;
		}
		/**
		 * @param answerFontSize The answerFontSize to set.
		 */
		public void setAnswerFontSize(String answerFontSize) {
			this.answerFontSize = answerFontSize;
		}
		/**
		 * @return Returns the barcode.
		 */
		public String getBarcode() {
			return barcode;
		}
		/**
		 * @param barcode The barcode to set.
		 */
		public void setBarcode(String barcode) {
			this.barcode = barcode;
		}
		/**
		 * @return Returns the birthdate.
		 */
		public Date getBirthdate() {
			return birthdate;
		}
		/**
		 * @param birthdate The birthdate to set.
		 */
		public void setBirthdate(Date birthdate) {
			this.birthdate = birthdate;
		}
		/**
		 * @return Returns the calculator.
		 */
		public String getCalculator() {
			return calculator;
		}
		/**
		 * @param calculator The calculator to set.
		 */
		public void setCalculator(String calculator) {
			this.calculator = calculator;
		}
		/**
		 * @return Returns the colorFontAccommodation.
		 */
		public String getColorFontAccommodation() {
			return colorFontAccommodation;
		}
		/**
		 * @param colorFontAccommodation The colorFontAccommodation to set.
		 */
		public void setColorFontAccommodation(String colorFontAccommodation) {
			this.colorFontAccommodation = colorFontAccommodation;
		}
		/**
		 * @return Returns the createdBy.
		 */
		public Integer getCreatedBy() {
			return createdBy;
		}
		/**
		 * @param createdBy The createdBy to set.
		 */
		public void setCreatedBy(Integer createdBy) {
			this.createdBy = createdBy;
		}
		/**
		 * @return Returns the createdDateTime.
		 */
		public Date getCreatedDateTime() {
			return createdDateTime;
		}
		/**
		 * @param createdDateTime The createdDateTime to set.
		 */
		public void setCreatedDateTime(Date createdDateTime) {
			this.createdDateTime = createdDateTime;
		}
		/**
		 * @return Returns the customerId.
		 */
		public Integer getCustomerId() {
			return customerId;
		}
		/**
		 * @param customerId The customerId to set.
		 */
		public void setCustomerId(Integer customerId) {
			this.customerId = customerId;
		}
		/**
		 * @return Returns the dataImportHistoryId.
		 */
		public Integer getDataImportHistoryId() {
			return dataImportHistoryId;
		}
		/**
		 * @param dataImportHistoryId The dataImportHistoryId to set.
		 */
		public void setDataImportHistoryId(Integer dataImportHistoryId) {
			this.dataImportHistoryId = dataImportHistoryId;
		}
		/**
		 * @return Returns the email.
		 */
		public String getEmail() {
			return email;
		}
		/**
		 * @param email The email to set.
		 */
		public void setEmail(String email) {
			this.email = email;
		}
		/**
		 * @return Returns the ethnicity.
		 */
		public String getEthnicity() {
			return ethnicity;
		}
		/**
		 * @param ethnicity The ethnicity to set.
		 */
		public void setEthnicity(String ethnicity) {
			this.ethnicity = ethnicity;
		}
		/**
		 * @return Returns the extElmId.
		 */
		public String getExtElmId() {
			return extElmId;
		}
		/**
		 * @param extElmId The extElmId to set.
		 */
		public void setExtElmId(String extElmId) {
			this.extElmId = extElmId;
		}
		/**
		 * @return Returns the extPin1.
		 */
		public String getExtPin1() {
			return extPin1;
		}
		/**
		 * @param extPin1 The extPin1 to set.
		 */
		public void setExtPin1(String extPin1) {
			this.extPin1 = extPin1;
		}
		/**
		 * @return Returns the extPin2.
		 */
		public String getExtPin2() {
			return extPin2;
		}
		/**
		 * @param extPin2 The extPin2 to set.
		 */
		public void setExtPin2(String extPin2) {
			this.extPin2 = extPin2;
		}
		/**
		 * @return Returns the extPin3.
		 */
		public String getExtPin3() {
			return extPin3;
		}
		/**
		 * @param extPin3 The extPin3 to set.
		 */
		public void setExtPin3(String extPin3) {
			this.extPin3 = extPin3;
		}
		/**
		 * @return Returns the extSchoolId.
		 */
		public String getExtSchoolId() {
			return extSchoolId;
		}
		/**
		 * @param extSchoolId The extSchoolId to set.
		 */
		public void setExtSchoolId(String extSchoolId) {
			this.extSchoolId = extSchoolId;
		}
		/**
		 * @return Returns the firstName.
		 */
		public String getFirstName() {
			return firstName;
		}
		/**
		 * @param firstName The firstName to set.
		 */
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		/**
		 * @return Returns the gender.
		 */
		public String getGender() {
			return gender;
		}
		/**
		 * @param gender The gender to set.
		 */
		public void setGender(String gender) {
			this.gender = gender;
		}
		/**
		 * @return Returns the grade.
		 */
		public String getGrade() {
			return grade;
		}
		/**
		 * @param grade The grade to set.
		 */
		public void setGrade(String grade) {
			this.grade = grade;
		}
		/**
		 * @return Returns the highlighter.
		 */
		public String getHighlighter() {
			return highlighter;
		}
		/**
		 * @param highlighter The highlighter to set.
		 */
		public void setHighlighter(String highlighter) {
			this.highlighter = highlighter;
		}
		/**
		 * @return Returns the lastName.
		 */
		public String getLastName() {
			return lastName;
		}
		/**
		 * @param lastName The lastName to set.
		 */
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		/**
		 * @return Returns the middleName.
		 */
		public String getMiddleName() {
			return middleName;
		}
		/**
		 * @param middleName The middleName to set.
		 */
		public void setMiddleName(String middleName) {
			this.middleName = middleName;
		}
		/**
		 * @return Returns the organizationNodes.
		 */
		public Node[] getOrganizationNodes() {
			return organizationNodes;
		}
		/**
		 * @param organizationNodes The organizationNodes to set.
		 */
		public void setOrganizationNodes(Node[] organizationNodes) {
			this.organizationNodes = organizationNodes;
		}
		/**
		 * @return Returns the orgNodeId.
		 */
		public Integer getOrgNodeId() {
			return orgNodeId;
		}
		/**
		 * @param orgNodeId The orgNodeId to set.
		 */
		public void setOrgNodeId(Integer orgNodeId) {
			this.orgNodeId = orgNodeId;
		}
		/**
		 * @return Returns the password.
		 */
		public String getPassword() {
			return password;
		}
		/**
		 * @param password The password to set.
		 */
		public void setPassword(String password) {
			this.password = password;
		}
		/**
		 * @return Returns the potentialDuplicatedStudent.
		 */
		public String getPotentialDuplicatedStudent() {
			return potentialDuplicatedStudent;
		}
		/**
		 * @param potentialDuplicatedStudent The potentialDuplicatedStudent to set.
		 */
		public void setPotentialDuplicatedStudent(String potentialDuplicatedStudent) {
			this.potentialDuplicatedStudent = potentialDuplicatedStudent;
		}
		/**
		 * @return Returns the precodeId.
		 */
		public Integer getPrecodeId() {
			return precodeId;
		}
		/**
		 * @param precodeId The precodeId to set.
		 */
		public void setPrecodeId(Integer precodeId) {
			this.precodeId = precodeId;
		}
		/**
		 * @return Returns the preferredName.
		 */
		public String getPreferredName() {
			return preferredName;
		}
		/**
		 * @param preferredName The preferredName to set.
		 */
		public void setPreferredName(String preferredName) {
			this.preferredName = preferredName;
		}
		/**
		 * @return Returns the prefix.
		 */
		public String getPrefix() {
			return prefix;
		}
		/**
		 * @param prefix The prefix to set.
		 */
		public void setPrefix(String prefix) {
			this.prefix = prefix;
		}
		/**
		 * @return Returns the questionBackgroundColor.
		 */
		public String getQuestionBackgroundColor() {
			return questionBackgroundColor;
		}
		/**
		 * @param questionBackgroundColor The questionBackgroundColor to set.
		 */
		public void setQuestionBackgroundColor(String questionBackgroundColor) {
			this.questionBackgroundColor = questionBackgroundColor;
		}
		/**
		 * @return Returns the questionFontColor.
		 */
		public String getQuestionFontColor() {
			return questionFontColor;
		}
		/**
		 * @param questionFontColor The questionFontColor to set.
		 */
		public void setQuestionFontColor(String questionFontColor) {
			this.questionFontColor = questionFontColor;
		}
		/**
		 * @return Returns the questionFontSize.
		 */
		public String getQuestionFontSize() {
			return questionFontSize;
		}
		/**
		 * @param questionFontSize The questionFontSize to set.
		 */
		public void setQuestionFontSize(String questionFontSize) {
			this.questionFontSize = questionFontSize;
		}
		/**
		 * @return Returns the screenMagnifier.
		 */
		public String getScreenMagnifier() {
			return screenMagnifier;
		}
		/**
		 * @param screenMagnifier The screenMagnifier to set.
		 */
		public void setScreenMagnifier(String screenMagnifier) {
			this.screenMagnifier = screenMagnifier;
		}
		/**
		 * @return Returns the screenReader.
		 */
		public String getScreenReader() {
			return screenReader;
		}
		/**
		 * @param screenReader The screenReader to set.
		 */
		public void setScreenReader(String screenReader) {
			this.screenReader = screenReader;
		}
		/**
		 * @return Returns the studentGrade.
		 */
		public String getStudentGrade() {
			return studentGrade;
		}
		/**
		 * @param studentGrade The studentGrade to set.
		 */
		public void setStudentGrade(String studentGrade) {
			this.studentGrade = studentGrade;
		}
		/**
		 * @return Returns the studentId.
		 */
		public Integer getStudentId() {
			return studentId;
		}
		/**
		 * @param studentId The studentId to set.
		 */
		public void setStudentId(Integer studentId) {
			this.studentId = studentId;
		}
		/**
		 * @return Returns the suffix.
		 */
		public String getSuffix() {
			return suffix;
		}
		/**
		 * @param suffix The suffix to set.
		 */
		public void setSuffix(String suffix) {
			this.suffix = suffix;
		}
		/**
		 * @return Returns the testPause.
		 */
		public String getTestPause() {
			return testPause;
		}
		/**
		 * @param testPause The testPause to set.
		 */
		public void setTestPause(String testPause) {
			this.testPause = testPause;
		}
		/**
		 * @return Returns the udf.
		 */
		public String getUdf() {
			return udf;
		}
		/**
		 * @param udf The udf to set.
		 */
		public void setUdf(String udf) {
			this.udf = udf;
		}
		/**
		 * @return Returns the udf1.
		 */
		public String getUdf1() {
			return udf1;
		}
		/**
		 * @param udf1 The udf1 to set.
		 */
		public void setUdf1(String udf1) {
			this.udf1 = udf1;
		}
		/**
		 * @return Returns the udf2.
		 */
		public String getUdf2() {
			return udf2;
		}
		/**
		 * @param udf2 The udf2 to set.
		 */
		public void setUdf2(String udf2) {
			this.udf2 = udf2;
		}
		/**
		 * @return Returns the untimedTest.
		 */
		public String getUntimedTest() {
			return untimedTest;
		}
		/**
		 * @param untimedTest The untimedTest to set.
		 */
		public void setUntimedTest(String untimedTest) {
			this.untimedTest = untimedTest;
		}
		/**
		 * @return Returns the updatedBy.
		 */
		public Integer getUpdatedBy() {
			return updatedBy;
		}
		/**
		 * @param updatedBy The updatedBy to set.
		 */
		public void setUpdatedBy(Integer updatedBy) {
			this.updatedBy = updatedBy;
		}
		/**
		 * @return Returns the updatedDateTime.
		 */
		public Date getUpdatedDateTime() {
			return updatedDateTime;
		}
		/**
		 * @param updatedDateTime The updatedDateTime to set.
		 */
		public void setUpdatedDateTime(Date updatedDateTime) {
			this.updatedDateTime = updatedDateTime;
		}
		/**
		 * @return Returns the userName.
		 */
		public String getUserName() {
			return userName;
		}
		/**
		 * @param userName The userName to set.
		 */
		public void setUserName(String userName) {
			this.userName = userName;
		}
        /**
         * @return the contactName
         */
        public String getContactName() {
            return contactName;
        }
        /**
         * @param contactName the contactName to set
         */
        public void setContactName(String contactName) {
            this.contactName = contactName;
        }
        /**
         * @return the address1
         */
        public String getAddress1() {
            return address1;
        }
        /**
         * @param address1 the address1 to set
         */
        public void setAddress1(String address1) {
            this.address1 = address1;
        }
        /**
         * @return the address2
         */
        public String getAddress2() {
            return address2;
        }
        /**
         * @param address2 the address2 to set
         */
        public void setAddress2(String address2) {
            this.address2 = address2;
        }
        /**
         * @return the address3
         */
        public String getAddress3() {
            return address3;
        }
        /**
         * @param address3 the address3 to set
         */
        public void setAddress3(String address3) {
            this.address3 = address3;
        }
        /**
         * @return the timeZone
         */
        public String getTimeZone() {
            return timeZone;
        }
        /**
         * @param timeZone the timeZone to set
         */
        public void setTimeZone(String timeZone) {
            this.timeZone = timeZone;
        }
        /**
         * @return the city
         */
        public String getCity() {
            return city;
        }
        /**
         * @param city the city to set
         */
        public void setCity(String city) {
            this.city = city;
        }
        /**
         * @return the state
         */
        public String getState() {
            return state;
        }
        /**
         * @param state the state to set
         */
        public void setState(String state) {
            this.state = state;
        }
        /**
         * @return the zip
         */
        public String getZip() {
            return zip;
        }
        /**
         * @param zip the zip to set
         */
        public void setZip(String zip) {
            this.zip = zip;
        }
        /**
         * @return the primaryPhone
         */
        public String getPrimaryPhone() {
            return primaryPhone;
        }
        /**
         * @param primaryPhone the primaryPhone to set
         */
        public void setPrimaryPhone(String primaryPhone) {
            this.primaryPhone = primaryPhone;
        }
        /**
         * @return the secondaryPhone
         */
        public String getSecondaryPhone() {
            return secondaryPhone;
        }
        /**
         * @param secondaryPhone the secondaryPhone to set
         */
        public void setSecondaryPhone(String secondaryPhone) {
            this.secondaryPhone = secondaryPhone;
        }
        /**
         * @return the faxNumber
         */
        public String getFaxNumber() {
            return faxNumber;
        }
        /**
         * @param faxNumber the faxNumber to set
         */
        public void setFaxNumber(String faxNumber) {
            this.faxNumber = faxNumber;
        } 

        /**
         * @return the headerDateOfBirth
         */
        public String getHeaderDateOfBirth() {
            return headerDateOfBirth;
        }
        /**
         * @param headerDateOfBirth the headerDateOfBirth to set
         */
        public void setHeaderDateOfBirth(String headerDateOfBirth) {
            this.headerDateOfBirth = headerDateOfBirth;
        }
        
        /**
		 * @return Returns the dataFilerowError.
		 */
        public DataFileRowError[] getDataFilerowError() {
			return dataFilerowError;
		}
        /**
		 * @param dataFilerowError The dataFilerowError to set.
		 */
		public void setDataFilerowError(DataFileRowError[] dataFilerowError) {
			this.dataFilerowError = dataFilerowError;
		}
         /* @return Returns the primarySort.
		 */
		public String getPrimarySort() {
			return primarySort;
		}
		/**
		 * @param primarySort The primarySort to set.
		 */
		public void setPrimarySort(String primarySort) {
			this.primarySort = primarySort;
		}
        /* @return Returns the secondarySort.
		 */
		public String getSecondarySort() {
			return secondarySort;
		}
		/**
		 * @param secondarySort The secondarySort to set.
		 */
		public void setSecondarySort(String secondarySort) {
			this.secondarySort = secondarySort;
		}
        /**
		 * @return Returns the displayStudentName.
		 */
		public String getDisplayStudentName() {
			return displayStudentName;
		}
		/**
		 * @param displayStudentName The displayStudentName to set.
		 */
		public void setDisplayStudentName(String displayStudentName) {
			this.displayStudentName = displayStudentName;
		}
                
} 
