package com.ctb.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

import com.ctb.bean.CustomerConfiguration;
import com.ctb.bean.CustomerConfigurationValue;
import com.ctb.bean.DataFileAudit;
import com.ctb.bean.DataFileTemp;
import com.ctb.bean.Node;
import com.ctb.bean.OrgNodeCategory;
import com.ctb.bean.StudentDemoGraphics;
import com.ctb.bean.StudentFileRow;
import com.ctb.bean.UploadMoveData;
import com.ctb.dao.UploadFileDao;
import com.ctb.dao.UploadFileDaoImpl;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.FileHeaderException;
import com.ctb.exception.FileNotUploadedException;

public class UploadFormUtils {
	private UploadFileDao dao = null;
	private CustomerConfiguration[] customerConfigurations = null;
	private CustomerConfigurationValue[] customerConfigurationsValue = null;
	private boolean isStudentIdConfigurable = false;
	private boolean isStudentId2Configurable = false;
	private Integer configId = 0;
	private String[] valueForStudentId = null;
	private String[] valueForStudentId2 = null;
	private boolean isStudentIdUnique = false;
	private UploadMoveData uploadMoveData = new UploadMoveData();

	public static boolean verifyFileExtension(String fileName) {
		boolean isValidFileExt = false;
		if (fileName.endsWith(".csv")) {
			isValidFileExt = true;
		}
		return isValidFileExt;
	}

	public Integer saveFileToDBTemp(File inFile) throws Exception {
		this.dao = new UploadFileDaoImpl();
		Integer uploadDataFileId = new Integer(0);
		DataFileTemp temp = new DataFileTemp();
		InputStream in = new FileInputStream(inFile);
		BufferedInputStream bin = new BufferedInputStream(in);
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte[] outputArray = new byte[20000000];
		byte[] readBuf = new byte[512 * 1024];
		int readCnt = bin.read(readBuf);
		while (0 < readCnt) {
			bout.write(readBuf, 0, readCnt);
			readCnt = bin.read(readBuf);
		}
		bin.close();
		in.close();
		outputArray = bout.toByteArray();

		uploadDataFileId = dao.getNextPKForTempFile();
		System.out.println("UploadDataFileId is --> " + uploadDataFileId);
		temp.setDataFile(outputArray);
		temp.setDataFileAuditId(uploadDataFileId);
		dao.createDataFileTemp(temp);
		uploadMoveData.setUploadDataFileId(uploadDataFileId);
		return uploadDataFileId;
	}

	/**
	 * Checks if the particular configuration is present for a customer
	 * 
	 * @param customerId
	 * @param configuration
	 * @return boolean
	 * @throws Exception
	 */
	public boolean checkCustomerConfigurationEntries(Integer customerId,
			String configuration) throws Exception {
		boolean isConfigPresent = false;
		this.dao = new UploadFileDaoImpl();
		isConfigPresent = dao.checkCustomerConfiguration(customerId,
				configuration);
		return isConfigPresent;
	}

	/**
	 * Returns OrgNodeCategory[] for a Customer
	 * 
	 * @param customerId
	 * @return OrgNodeCategory[]
	 * @throws Exception
	 */
	public OrgNodeCategory[] getOrgNodeCategories(Integer customerId)
			throws Exception {
		this.dao = new UploadFileDaoImpl();
		OrgNodeCategory[] orgNodeCategory = dao
				.getOrgNodeCategories(customerId);
		return orgNodeCategory;

	}

	/**
	 * File Header validation is Performed here .
	 * 
	 * @param file
	 * @param noOfUserColumn
	 * @param customerId
	 * @param orgNodeCategory
	 * @return String
	 * @throws Exception
	 */
	public String getUploadFileCheck(File file, int noOfUserColumn,
			Integer customerId, OrgNodeCategory[] orgNodeCategory)
			throws Exception {
		isStudentIDConfigurableCustomer(customerId);
		String fileType = "";
		CSVReader csv = null;
		StudentFileRow[] studentFileRow = new StudentFileRow[1];
		try {
			csv = new CSVReader(new FileReader(file), ',');
			List<String> orgNodeListFromTemplate = new ArrayList<String>();
			List<String> headerList = new ArrayList<String>();
			List<String> orgNodeList = new ArrayList<String>();
			List<String> headerListFromTemplate = new ArrayList<String>();
			List<String> demographicsHeaderListTemplate = new ArrayList<String>();
			int count = 0;
			String[] firstRow;
			boolean firstRowProcessed = false;

			while ((firstRow = csv.readNext()) != null && !firstRowProcessed) {
				firstRowProcessed = true;
				int totalColumns = firstRow.length;
				for (int i = noOfUserColumn; i < totalColumns; i++) {
					if (firstRow[i].equalsIgnoreCase("Gender")) {
						fileType = "Upload_Student_Data";
						break;
					}
				}
				if (fileType.equals("Upload_Student_Data")) {
					try {
						createTemplateHeader(customerId, orgNodeCategory, studentFileRow);
					} catch (Exception e) {
						throw new CTBBusinessException("FileHeader.Failed");
					}
					for (int i = 0; i < noOfUserColumn - 1; i++) {
						orgNodeListFromTemplate.add(firstRow[i]);
					}

					/*
					 * check if the uploaded excel node category header and
					 * template are same or not
					 */
					Node[] orgNodes = studentFileRow[0].getOrganizationNodes();
					for (int i = 0; i < orgNodes.length; i++) {
						orgNodeList.add(orgNodes[i].getOrgNodeCategoryName());
						orgNodeList.add(orgNodes[i].getOrgNodeCode());
						orgNodeList.add(orgNodes[i].getMdrNumber());
					}
					/*
					 * position should be at the beginning and should end before
					 * student details
					 */
					for (int i = 0; i < orgNodeListFromTemplate.size() - 1; i++) {
						if (!orgNodeList.get(i).equalsIgnoreCase(
								orgNodeListFromTemplate.get(i))) {
							throw new CTBBusinessException("FileHeader.Failed");
						}
					}

					if (isStudentIdConfigurable || isStudentId2Configurable) {
						headerListFromTemplate = getStudentIDHeaderOrderList();
					} else {
						headerListFromTemplate = HeaderOrder
								.getStudentHeaderOrderList();
					}
					for (int i = noOfUserColumn - 1; i < totalColumns; i++) {
						headerList.add(firstRow[i]);
					}
					/*
					 * check if the uploaded excel student info header and
					 * template are same or not
					 */
					for (int i = 0; i < headerListFromTemplate.size(); i++) {
						if (!headerListFromTemplate.get(i).equalsIgnoreCase(
								headerList.get(i))) {
							throw new CTBBusinessException("FileHeader.Failed");
						}
					}
					/*
					 * check if the uploaded excel student demographic info
					 * header and template are same or not
					 */
					StudentDemoGraphics[] studentDemoGraphics = studentFileRow[0]
							.getStudentDemoGraphics();
					// put the demographic details into list to maintain order
					for (int i = 0; i < studentDemoGraphics.length; i++) {
						demographicsHeaderListTemplate
								.add(studentDemoGraphics[i].getLabelName());
					}

					// if actual demographic field does not match with given
					// demographic field
					/*if ((headerList.size() - headerListFromTemplate.size()) != demographicsHeaderListTemplate
							.size()) {
						throw new CTBBusinessException("FileHeader.Failed");
					}*/
					//Modified on 09/30/2014 for OAS-820
					if ((headerList.size() - headerListFromTemplate.size()) < demographicsHeaderListTemplate
							.size()) {
						throw new CTBBusinessException("FileHeader.Failed");
					}
					// position should be after student personal details to end
					/*for (int i = headerListFromTemplate.size(); i < headerList
							.size() - 1; i++) {
						if (!headerList.get(i).equals(
								demographicsHeaderListTemplate.get(count))) {
							throw new CTBBusinessException("FileHeader.Failed");
						}
						count++;
					}*/
					//Modified on 09/30/2014 for OAS-820
					Map<String, String> remainingHeadersMap = new HashMap<String, String>();
					for(int k = headerListFromTemplate.size(); k < headerList.size(); k++){
						remainingHeadersMap.put(headerList.get(k).toUpperCase(), headerList.get(k));
					}
					
					for (int i=0; i < demographicsHeaderListTemplate.size();i++){
						if(!remainingHeadersMap.containsKey((demographicsHeaderListTemplate.get(i).toUpperCase())))
							throw new CTBBusinessException("FileHeader.Failed");
					}
				} // end of student header validation
				else {
					throw new CTBBusinessException("FileHeader.Failed");
				}

			}

		} catch (IOException e) {
			FileNotUploadedException fileNotUploadedException = new FileNotUploadedException(
					"Uploaded.Failed");
			fileNotUploadedException.setStackTrace(e.getStackTrace());
			throw fileNotUploadedException;
		} catch (CTBBusinessException e) {
			/**
			 * Send Mail
			 */
			if ("true".equalsIgnoreCase(Configuration.getEmailAlerts())) {
				EmailSender.sendMail("",
						Configuration.getEmailSender(),
						Configuration.getEmailRecipient(),
						Configuration.getEmailCC(),
						Configuration.getEmailBCC(),
						Configuration.getEmailSubjectFileHeaderValidationIssue(),
						Configuration.getEmailBodyFileHeaderValidationIssue().replace("<#FileName#>", file.getName()),
						null);
			}
			
			FileHeaderException fileHeaderException = new FileHeaderException(
					"FileHeader.Failed");
			fileHeaderException.setStackTrace(e.getStackTrace());
			throw fileHeaderException;
		} finally {

			csv.close();
		}
		uploadMoveData.setNoOfUserColumn(noOfUserColumn);
		uploadMoveData.setValueForStudentId(valueForStudentId);
		uploadMoveData.setValueForStudentId2(valueForStudentId2);
		uploadMoveData.setOrgNodeCategory(orgNodeCategory);
		uploadMoveData.setStudentFileRowHeader(studentFileRow);
		return fileType;
	}

	/**
	 * Creates the Template for Header validation
	 * 
	 * @param customerId
	 * @param OrgNodeCategory
	 * @param object
	 * @throws Exception 
	 */
	private void createTemplateHeader(Integer customerId,
			OrgNodeCategory[] OrgNodeCategory, StudentFileRow[] object) throws Exception {

		this.dao = new UploadFileDaoImpl();
		StudentFileRow[] studentFileRow = (StudentFileRow[]) object;
		StudentFileRow tempStudentFileRow = new StudentFileRow();
		Node node[] = new Node[OrgNodeCategory.length];
		for (int i = 0; i < OrgNodeCategory.length; i++) {
			Node currentHeader = new Node();
			currentHeader.setOrgNodeCategoryName(OrgNodeCategory[i]
					.getCategoryName() + " Name");
			currentHeader.setOrgNodeCode(OrgNodeCategory[i].getCategoryName()
					+ " Id");
			currentHeader.setOrgNodeCategoryId(OrgNodeCategory[i]
					.getOrgNodeCategoryId());
			currentHeader.setMdrNumber(OrgNodeCategory[i].getCategoryName()
					+ " MDR");
			node[i] = currentHeader;
		}
		StudentDemoGraphics[] StudentDemoGraphics = null;
		StudentDemoGraphics = dao.getStudentDemoGraphics(customerId);
		
		tempStudentFileRow.setOrganizationNodes(node);
		tempStudentFileRow.setStudentDemoGraphics(StudentDemoGraphics);
		StudentHeader studentHeader = new StudentHeader();
		if (isStudentIdConfigurable) {
			tempStudentFileRow.setExtPin1(this.valueForStudentId[0]);
		} else {
			tempStudentFileRow.setExtPin1(studentHeader.getStudentId());
		}
		if (isStudentId2Configurable) {
			tempStudentFileRow.setExtPin2(this.valueForStudentId2[0]);
		} else {
			tempStudentFileRow.setExtPin2(studentHeader.getStudentId2());
		}
		studentFileRow[0] = tempStudentFileRow;
	}

	private void isStudentIDConfigurableCustomer(Integer customerId) {
		try {
			getCustomerConfigurations(customerId);
			for (int i = 0; i < this.customerConfigurations.length; i++) {
				CustomerConfiguration cc = (CustomerConfiguration) this.customerConfigurations[i];
				if (cc.getCustomerConfigurationName().equalsIgnoreCase(
						"Configurable_Student_ID_2")
						&& cc.getDefaultValue().equalsIgnoreCase("T")) {
					this.isStudentId2Configurable = true;
					configId = cc.getId();
					customerConfigurationValues(configId);
					this.valueForStudentId2 = new String[8];
					for (int j = 0; j < this.customerConfigurationsValue.length; j++) {
						int sortOrder = this.customerConfigurationsValue[j]
								.getSortOrder();
						this.valueForStudentId2[sortOrder - 1] = this.customerConfigurationsValue[j]
								.getCustomerConfigurationValue();
					}
					this.valueForStudentId2 = getDefaultValue(
							valueForStudentId2, Constants.STUDENT_ID2);
				}
				if (cc.getCustomerConfigurationName().equalsIgnoreCase(
						"Configurable_Student_ID")
						&& cc.getDefaultValue().equalsIgnoreCase("T")) {
					this.isStudentIdConfigurable = true;
					configId = cc.getId();
					customerConfigurationValues(configId);
					// By default there should be 3 entries for customer
					// configurations
					this.valueForStudentId = new String[8];
					for (int j = 0; j < this.customerConfigurationsValue.length; j++) {
						int sortOrder = this.customerConfigurationsValue[j]
								.getSortOrder();
						this.valueForStudentId[sortOrder - 1] = this.customerConfigurationsValue[j]
								.getCustomerConfigurationValue();
					}
					this.valueForStudentId = getDefaultValue(valueForStudentId,
							Constants.STUDENT_ID);
				}
				this.isStudentIdUnique = true;// For the time being making this
												// Unique checking mandatory.
			}
		} catch (Exception e) {
			FileNotUploadedException dataNotfoundException = new FileNotUploadedException(
					"Upload.Failed -> isStudentIDConfigurableCustomer block");
			dataNotfoundException.setStackTrace(e.getStackTrace());

		}
		uploadMoveData.setStudentIdUnique(isStudentIdUnique);
	}

	private void getCustomerConfigurations(Integer customerId) {
		this.dao = new UploadFileDaoImpl();
		try {
			this.customerConfigurations = this.dao
					.getCustomerConfigurations(customerId);
		} catch (CTBBusinessException be) {
			be.printStackTrace();
		} catch (Exception be) {
			be.printStackTrace();
		}
	}

	private void customerConfigurationValues(Integer configId) {
		this.dao = new UploadFileDaoImpl();
		try {
			this.customerConfigurationsValue = this.dao
					.getCustomerConfigurationsValue(configId);
		} catch (CTBBusinessException be) {
			be.printStackTrace();
		} catch (Exception be) {
			be.printStackTrace();
		}
	}

	private String[] getDefaultValue(String[] arrValue, String labelName) {
		arrValue[0] = arrValue[0] != null ? arrValue[0] : labelName;
		arrValue[1] = arrValue[1] != null ? arrValue[1] : "32";
		if (labelName.equals("Student ID")) {
			arrValue[2] = arrValue[2] != null ? arrValue[2] : "F";
			if (!arrValue[2].equals("T") && !arrValue[2].equals("F")) {
				arrValue[2] = "F";
			}
			try {
				arrValue[3] = (arrValue[3] != null && new Integer(arrValue[3])
						.intValue() > 0) ? arrValue[3] : "0";
			} catch (NumberFormatException nfe) {
				arrValue[3] = "0";
			}
			arrValue[4] = arrValue[4] != null ? arrValue[4] : "AN";
			if (!arrValue[4].equals("NU") && !arrValue[4].equals("AN")) {
				arrValue[4] = "AN";
			}
			System.out.println("Student ID " + arrValue[0] + "...."
					+ arrValue[1] + "..." + arrValue[2] + "..." + arrValue[3]
					+ "..." + arrValue[4]);
		}
		if (labelName.equals("Student ID2")) {
			try {
				arrValue[2] = (arrValue[2] != null && new Integer(arrValue[2])
						.intValue() > 0) ? arrValue[2] : "0";
			} catch (NumberFormatException nfe) {
				arrValue[2] = "0";
			}
			arrValue[3] = arrValue[3] != null ? arrValue[3] : "AN";
			if (!arrValue[3].equals("NU") && !arrValue[3].equals("AN")) {
				arrValue[3] = "AN";
			}
			System.out.println("Student ID2 " + arrValue[0] + "...."
					+ arrValue[1] + "..." + arrValue[2] + "..." + arrValue[3]);
		}
		try {
		} catch (NumberFormatException nfe) {
			arrValue[1] = "32";
		}
		return arrValue;
	}

	/**
	 * Returns the List of StudentData Headers
	 * 
	 * @return ArrayList<String>
	 */
	private ArrayList<String> getStudentIDHeaderOrderList() {
		ArrayList<String> headerArray = new ArrayList<String>();
		headerArray.add(Constants.REQUIREDFIELD_FIRST_NAME);
		headerArray.add(Constants.MIDDLE_NAME);
		headerArray.add(Constants.REQUIREDFIELD_LAST_NAME);
		headerArray.add(Constants.REQUIREDFIELD_DATE_OF_BIRTH);
		headerArray.add(Constants.REQUIREDFIELD_GRADE);
		headerArray.add(Constants.REQUIREDFIELD_GENDER);
		if (isStudentIdConfigurable) {
			headerArray.add(valueForStudentId[0]);
		} else {
			headerArray.add(Constants.STUDENT_ID);
		}
		if (isStudentId2Configurable) {
			headerArray.add(valueForStudentId2[0]);
		} else {
			headerArray.add(Constants.STUDENT_ID2);
		}
		/** Commented out because of story : OAS-636 & OAS-637**/
		// headerArray.add(Constants.SCREEN_READER);
		// headerArray.add(Constants.CALCULATOR);
		// headerArray.add(Constants.TEST_PAUSE);
		// headerArray.add(Constants.UNTIMED_TEST);
		// headerArray.add(Constants.HIGHLIGHTER);
		// headerArray.add(Constants.QUESTION_BACKGROUND_COLOR);
		// headerArray.add(Constants.QUESTION_FONT_COLOR);
		// headerArray.add(Constants.ANSWER_BACKGROUND_COLOR);
		// headerArray.add(Constants.ANSWER_FONT_COLOR);
		// headerArray.add(Constants.FONT_SIZE);

		return headerArray;
	}

	public void createDataFileAudit(DataFileAudit dataFileAudit) {
		this.dao = new UploadFileDaoImpl();
		try {
			this.dao.createDataFileAudit(dataFileAudit);
		} catch (CTBBusinessException be) {
			be.printStackTrace();
		} catch (Exception be) {
			be.printStackTrace();
		}

	}

	public UploadMoveData getUploadMoveData() {
		return uploadMoveData;
	}
}
