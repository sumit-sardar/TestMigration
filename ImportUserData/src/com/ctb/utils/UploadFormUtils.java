package com.ctb.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;

import com.ctb.bean.DataFileAudit;
import com.ctb.bean.DataFileTemp;
import com.ctb.bean.Node;
import com.ctb.bean.OrgNodeCategory;
import com.ctb.bean.UploadMoveData;
import com.ctb.bean.UserFileRow;
import com.ctb.bean.UserHeader;
import com.ctb.dao.UploadFileDao;
import com.ctb.dao.UploadFileDaoImpl;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.FileHeaderException;
import com.ctb.exception.FileNotUploadedException;

public class UploadFormUtils {
	private UploadFileDao dao = null;
	private UploadMoveData uploadMoveData = new UploadMoveData();

	static Logger logger = Logger.getLogger(UploadFormUtils.class.getName());

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
		logger.info("UploadDataFileId is --> " + uploadDataFileId);
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
		String fileType = "";
		CSVReader csv = null;
		UserFileRow[] userFileRow = new UserFileRow[1];
		try {
			csv = new CSVReader(new FileReader(file), ',');
			List<String> orgNodeListFromTemplate = new ArrayList<String>();
			List<String> headerList = new ArrayList<String>();
			List<String> orgNodeList = new ArrayList<String>();
			List<String> headerListFromTemplate = new ArrayList<String>();
			String[] firstRow;
			boolean firstRowProcessed = false;

			while ((firstRow = csv.readNext()) != null && !firstRowProcessed) {
				firstRowProcessed = true;
				int totalColumns = firstRow.length;
				for (int i = noOfUserColumn; i < totalColumns; i++) {
					if (firstRow[i].equals("Role")) {
						fileType = "Upload_User_Data";
						break;
					}
				}
				if (fileType.equals("Upload_User_Data")) {

					createTemplateHeader(customerId, orgNodeCategory,
							userFileRow);

					for (int i = 0; i < noOfUserColumn - 1; i++) {
						orgNodeListFromTemplate.add(firstRow[i]);
					}

					/*
					 * check if the uploaded excel node category header and
					 * template are same or not
					 */
					Node[] orgNodes = userFileRow[0].getOrganizationNodes();
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
						if (!orgNodeList.get(i).equals(
								orgNodeListFromTemplate.get(i))) {
							throw new CTBBusinessException("FileHeader.Failed");
						}
					}

					headerListFromTemplate = HeaderOrder
							.getUserHeaderOrderList();

					for (int i = noOfUserColumn - 1; i < totalColumns; i++) {
						headerList.add(firstRow[i]);
					}
					/*
					 * check if the uploaded excel student info header and
					 * template are same or not
					 */
					for (int i = 0; i < headerListFromTemplate.size(); i++) {
						if (!headerListFromTemplate.get(i).equals(
								headerList.get(i))) {
							throw new CTBBusinessException("FileHeader.Failed");
						}
					}

				} // end of user header validation
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
			FileHeaderException fileHeaderException = new FileHeaderException(
					"FileHeader.Failed");
			fileHeaderException.setStackTrace(e.getStackTrace());
			throw fileHeaderException;
		} finally {

			csv.close();
		}
		uploadMoveData.setNoOfUserColumn(noOfUserColumn);
		uploadMoveData.setOrgNodeCategory(orgNodeCategory);
		uploadMoveData.setUserFileRowHeader(userFileRow);
		return fileType;
	}

	/**
	 * Creates the Template for Header validation
	 * 
	 * @param customerId
	 * @param OrgNodeCategory
	 * @param object
	 */
	private void createTemplateHeader(Integer customerId,
			OrgNodeCategory[] OrgNodeCategory, UserFileRow[] object) {

		UserFileRow[] userFileRow = (UserFileRow[]) object;
		UserFileRow tempUserFileRow = new UserFileRow();
		Node node[] = new Node[OrgNodeCategory.length];

		// create header for category
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

		tempUserFileRow.setOrganizationNodes(node);

		UserHeader userHeader = new UserHeader();
		tempUserFileRow.setFirstName(userHeader.getFirstName());
		tempUserFileRow.setMiddleName(userHeader.getMiddleName());
		tempUserFileRow.setLastName(userHeader.getLastName());
		tempUserFileRow.setEmail(userHeader.getEmail());
		tempUserFileRow.setTimeZone(userHeader.getTimeZone());
		tempUserFileRow.setRoleName(userHeader.getRole());
		tempUserFileRow.setExtSchoolId(userHeader.getExtSchoolId());
		tempUserFileRow.setAddress1(userHeader.getAddress1());
		tempUserFileRow.setAddress2(userHeader.getAddress2());
		tempUserFileRow.setCity(userHeader.getCity());
		tempUserFileRow.setState(userHeader.getState());
		tempUserFileRow.setZip(userHeader.getZip());
		tempUserFileRow.setPrimaryPhone(userHeader.getPrimaryPhone());
		tempUserFileRow.setSecondaryPhone(userHeader.getSecondaryPhone());
		tempUserFileRow.setFaxNumber(userHeader.getFaxNumber());
		// userFileRow's first index contains header part
		userFileRow[0] = tempUserFileRow;

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
