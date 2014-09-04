package com.ctb.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import com.ctb.bean.DataFileAudit;
import com.ctb.bean.Node;
import com.ctb.bean.OrgNodeCategory;
import com.ctb.bean.TimeZones;
import com.ctb.bean.USState;
import com.ctb.bean.UserFile;
import com.ctb.bean.UserFileRow;
import com.ctb.bean.UserNode;
import com.ctb.control.OrganizationManagementControl;
import com.ctb.control.UserManagementControl;
import com.ctb.dao.UploadFileDao;
import com.ctb.dao.UploadFileDaoImpl;
import com.ctb.dao.UserFileDao;
import com.ctb.dao.UserFileDaoImpl;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.FileNotUploadedException;
import com.ctb.utils.cache.OrgMDRDBCacheImpl;
import com.ctb.utils.cache.UserDBCacheImpl;
import com.ctb.utils.cache.UserNewRecordCacheImpl;
import com.ctb.utils.cache.UserUpdateRecordCacheImpl;

@SuppressWarnings("unused")
public class UploadUserFile {

	private static Logger logger = Logger.getLogger(UploadUserFile.class
			.getName());

	private Integer customerId;
	private File inFile;
	private Integer uploadFileId;
	private Date uploadDt;
	private int noOfUserColumn;
	private UserDBCacheImpl dbCacheImpl = new UserDBCacheImpl();
	private OrgMDRDBCacheImpl orgMDRImpl = new OrgMDRDBCacheImpl();
	private UserUpdateRecordCacheImpl userUpdateCacheImpl = new UserUpdateRecordCacheImpl();
	private UserNewRecordCacheImpl userNewCacheImpl = new UserNewRecordCacheImpl();

	private String username;
	private int failedRecordCount;
	private int uploadRecordCount;
	private UserFile userFile;
	private DataFileAudit dataFileAudit = new DataFileAudit();
	private UserNode[] usernode = null;
	private OrgNodeCategory orgNodeCategory[] = null;
	private UserFileRow[] userFileRowHeader;
	private String serverFilePath;
	private Map<String, Integer> roleMap = new HashMap<String, Integer>();
	private Map<String, String> stateMap = new HashMap<String, String>();
	private Map<String, String> timeZoneMap = new HashMap<String, String>();
	private Map<String, Integer> keyUserIdMap = new HashMap<String, Integer>();
	private Map<String, Integer> keyAddressIdMap = new HashMap<String, Integer>();

	private Node[] userTopOrgNode = null;

	private TimeZones[] timeZones;
	private USState[] usState;
	private Node[] detailNodeM = null;
	private int orgPosFact = 3;
	private boolean isMatchUploadOrgIds = false;

	private UserFileDao userFileDao = new UserFileDaoImpl();
	private UploadFileDao uploadFileDao = new UploadFileDaoImpl();
	private OrganizationManagementControl organizationManagement = new OrganizationManagementControl();
	private UserManagementControl userManagement = new UserManagementControl();

	public UploadUserFile(Integer customerId, File inFile,
			Integer uploadFileId, OrgNodeCategory[] orgNodeCategory,
			UserFileRow[] userFileRowHeader, int noOfUserColumn) {

		this.customerId = customerId;
		this.inFile = inFile;
		this.uploadFileId = uploadFileId;
		setUploadDt(new Date());
		setNoOfHeaderRows(noOfUserColumn);
		this.userFileRowHeader = userFileRowHeader;
		this.orgNodeCategory = orgNodeCategory;
		init();

	}

	public void startProcessing() throws Exception {

		logger.info("Data Validation Start Time:"
				+ new Date(System.currentTimeMillis()));

		// ErrorMap initialization
		Map<Integer, ArrayList<String>> requiredMap = new HashMap<Integer, ArrayList<String>>();
		Map<Integer, ArrayList<String>> maxLengthMap = new HashMap<Integer, ArrayList<String>>();
		Map<Integer, ArrayList<String>> invalidCharMap = new HashMap<Integer, ArrayList<String>>();
		Map<Integer, ArrayList<String>> logicalErrorMap = new HashMap<Integer, ArrayList<String>>();
		Map<Integer, ArrayList<String>> hierarchyErrorMap = new HashMap<Integer, ArrayList<String>>();
		Map<String, String> userDataMap = new HashMap<String, String>();
		Map<Integer, String> blankRowMap = new HashMap<Integer, String>();

		boolean isBlankRow = true;

		String strCellName = "";
		String strCellId = "";
		String strCellHeaderName = "";
		String strCellHeaderId = "";
		String strCellMdr = "";
		String strCellHeaderMdr = "";
		int loginUserPosition = 0;

		try {
			/**
			 * Caching of user data present in the DataBase.
			 */
			logger.info("Caching Data in progess..");
			userFileDao.getExistUserData(customerId, dbCacheImpl);
			logger.info("User Data Cached:"
					+ new Date(System.currentTimeMillis()));

			userFileDao.getExistOrgData(customerId, orgMDRImpl);
			logger.info("Org Data Cached:"
					+ new Date(System.currentTimeMillis()));
			logger.info("Caching data completed.");

			CSVReader csv = new CSVReader(new BufferedReader(new FileReader(
					this.inFile)), ',');

			int rowIndex = 0;
			boolean isFirstRow = true;
			String[] rowHeader = new String[0];
			String[] row;
			while ((row = csv.readNext()) != null) {

				if (isFirstRow) {
					rowHeader = new String[row.length];
					rowHeader = row;
					isFirstRow = false;
					rowIndex++;
					continue;
				}
				int totalCells = row.length;

				for (int k = 0; k < totalCells; k++) {
					String cell = row[k];
					if (cell != null && (!getCellValue(cell).trim().equals(""))) {
						isBlankRow = false;
					}

				}
				if (isBlankRow) {
					blankRowMap.put(new Integer(rowIndex), "BlankRow");
					continue;
				}
				getEachRowUserDetail(rowIndex, row, rowHeader, requiredMap,
						maxLengthMap, invalidCharMap, logicalErrorMap,
						isMatchUploadOrgIds);

				if (!(requiredMap.containsKey(new Integer(rowIndex))
						|| invalidCharMap.containsKey(new Integer(rowIndex))
						|| maxLengthMap.containsKey(new Integer(rowIndex)) || logicalErrorMap
							.containsKey(new Integer(rowIndex)))) {

					loginUserPosition = getLoginUserOrgPosition(row, rowHeader,
							this.userTopOrgNode);
					List<String> newMDRList = new ArrayList<String>();
					// create Organization process
					Node[] node = this.userFileRowHeader[0]
							.getOrganizationNodes();
					int orgHeaderLastPosition = node.length * orgPosFact;
					Integer[] parentOrgId = new Integer[1];

					String loginUserOrgCell = row[loginUserPosition];
					String loginUserOrgName = getCellValue(loginUserOrgCell);
					Node loginUserNode = getLoginUserOrgDetail(
							this.userTopOrgNode, loginUserOrgName);
					Integer parentOId = loginUserNode.getOrgNodeId();
					parentOrgId[0] = parentOId;

					for (int j = loginUserPosition + orgPosFact; j < orgHeaderLastPosition; j = j
							+ orgPosFact) {
						String cellHeaderName = rowHeader[j];
						String cellHeaderId = rowHeader[j + 1];
						String cellName = row[j];
						String cellId = row[j + 1];

						strCellName = getCellValue(cellName);
						strCellId = getCellValue(cellId);
						strCellHeaderName = getCellValue(cellHeaderName);
						strCellHeaderId = getCellValue(cellHeaderId);
						Integer categoryId = null;

						String cellHeaderMdr = rowHeader[j + 2];
						String cellMdr = row[j + 2];
						strCellMdr = getCellValue(cellMdr);
						strCellHeaderMdr = getCellValue(cellHeaderMdr);
						String OrgCellHeaderName = rowHeader[j];
						String headerName = getCellValue(OrgCellHeaderName);
						Node[] nodeCategory = this.userFileRowHeader[0]
								.getOrganizationNodes();
						categoryId = getCategoryId(headerName, nodeCategory);

						// OrgName required check
						if (strCellName.equals("") && hasOrganization(j, row)
								&& !strCellId.equals("")) {
							ArrayList<String> requiredList = new ArrayList<String>();
							requiredList.add(strCellHeaderName);
							requiredMap
									.put(new Integer(rowIndex), requiredList);
							break;
						} else if (strCellName.equals("")
								&& hasOrganization(j - orgPosFact, row)
								&& !strCellId.equals("")) {
							ArrayList<String> requiredList = new ArrayList<String>();
							requiredList.add(strCellHeaderName);
							requiredMap
									.put(new Integer(rowIndex), requiredList);
							break;
						} else if (strCellName.equals("")
								&& !strCellMdr.equals("")) {
							ArrayList<String> requiredList = new ArrayList<String>();
							requiredList.add(strCellHeaderName);
							requiredMap
									.put(new Integer(rowIndex), requiredList);
							break;
						} else if (!isValidMDR(rowIndex, isMatchUploadOrgIds,
								strCellId, parentOrgId, categoryId,
								requiredMap, invalidCharMap, logicalErrorMap,
								newMDRList, strCellMdr, strCellName,
								strCellHeaderMdr, orgMDRImpl)) {
							break;

						} else {
							// OrgName invalid char check
							if (validString(strCellName)) {
								// OrgCode invalid char check
								if (!validString(strCellId)) {
									ArrayList<String> invalidList = new ArrayList<String>();
									invalidList.add(strCellHeaderId);
									invalidCharMap.put(new Integer(rowIndex),
											invalidList);
									break;
								} else {
									// maxlength checking
									boolean flag = false;
									if (!isMaxLength50(strCellName)) {
										ArrayList<String> maxLengthList = new ArrayList<String>();
										maxLengthList.add(strCellHeaderName);
										maxLengthMap.put(new Integer(rowIndex),
												maxLengthList);
										flag = true;
									}
									if (!isMaxLength32(strCellHeaderId)) {
										ArrayList<String> maxLengthList = new ArrayList<String>();
										maxLengthList.add(strCellHeaderId);
										maxLengthMap.put(new Integer(rowIndex),
												maxLengthList);
										flag = true;
									}
									if (flag) {
										break;
									}
								}
							} else {
								ArrayList<String> invalidList = new ArrayList<String>();
								invalidList.add(strCellHeaderName);
								invalidCharMap.put(new Integer(rowIndex),
										invalidList);
								break;
							}
						}
					}
				}
				isBlankRow = true;
				rowIndex++;
			}// while loop end of total row processing
			csv.close();

			/**
			 * Creation of Org and Users
			 */
			createOrganizationAndUser(requiredMap, maxLengthMap,
					invalidCharMap, logicalErrorMap, hierarchyErrorMap,
					userDataMap, blankRowMap, isMatchUploadOrgIds,
					this.userTopOrgNode, orgMDRImpl);

			logger.info("Total Rows Present in the file : " + (rowIndex - 1));

			/***
			 * Error Excel to be created if any error records are present.
			 */
			if (requiredMap.size() > 0 || maxLengthMap.size() > 0
					|| invalidCharMap.size() > 0 || logicalErrorMap.size() > 0
					|| hierarchyErrorMap.size() > 0) {
				logger.info("Error CSV Start Time:"
						+ new Date(System.currentTimeMillis()));
				errorExcelCreation(requiredMap, maxLengthMap, invalidCharMap,
						logicalErrorMap, hierarchyErrorMap);
				logger.info("Error CSV End Time:"
						+ new Date(System.currentTimeMillis()));
			}
			requiredMap = null;
			maxLengthMap = null;
			logicalErrorMap = null;
			invalidCharMap = null;
			hierarchyErrorMap = null;
			System.gc();

			/**
			 * Archiving Process
			 * */
			logger.info("ArchiveProcessedFiles Start Time:"
					+ new Date(System.currentTimeMillis()));
			FtpSftpUtil.archiveProcessedFiles(FtpSftpUtil.getSFTPSession(),
					Configuration.getFtpFilePath(),
					Configuration.getArchivePath(), inFile.getName());
			logger.info("ArchiveProcessedFiles End Time:"
					+ new Date(System.currentTimeMillis()));
			logger.info("User Upload Process Completed Time "
					+ new Date(System.currentTimeMillis()));

		} catch (Exception e) {
			logger.error("UploadUserFile Error.. startProcessing() Block..."
					+ e.getMessage());
			e.printStackTrace();
			throw e;
		}

	}

	private boolean isValidMDR(int cellPos, boolean isMatchUploadOrgIds,
			String orgCode, Integer[] parentOrgId, Integer categoryId,
			Map<Integer, ArrayList<String>> requiredMap,
			Map<Integer, ArrayList<String>> invalidCharMap,
			Map<Integer, ArrayList<String>> logicalErrorMap,
			List<String> newMDRList, String strCellMdr, String orgName,
			String strCellHeaderMdr, OrgMDRDBCacheImpl orgMDRImpl) {

		if (orgName == null || orgName.length() == 0) {
			return true;
		}
		if (!isOrganizationExists(isMatchUploadOrgIds, orgCode, parentOrgId,
				categoryId, strCellMdr, orgName)) {

			if (!validMdrNo(strCellMdr)) {
				ArrayList<String> requiredList = new ArrayList<String>();
				requiredList.add(strCellHeaderMdr);
				requiredMap.put(new Integer(cellPos), requiredList);
				return false;

			} else if (!validMdrNoLength(strCellMdr)) {
				ArrayList<String> requiredList = new ArrayList<String>();
				requiredList.add(strCellHeaderMdr);
				invalidCharMap.put(new Integer(cellPos), requiredList);
				return false;

			} else if (!validMdrNoNumeric(strCellMdr)) {
				ArrayList<String> requiredList = new ArrayList<String>();
				requiredList.add(strCellHeaderMdr);
				invalidCharMap.put(new Integer(cellPos), requiredList);
				return false;

			} else if (!isUniqueMdr(strCellMdr, newMDRList, orgMDRImpl)) {
				ArrayList<String> requiredList = new ArrayList<String>();
				requiredList.add(strCellHeaderMdr);
				logicalErrorMap.put(new Integer(cellPos), requiredList);
				return false;
			}
			newMDRList.add(strCellMdr);
		} else {
			// Update Organization
			if (!validMdrNo(strCellMdr)) {
				ArrayList<String> requiredList = new ArrayList<String>();
				requiredList.add(strCellHeaderMdr);
				requiredMap.put(new Integer(cellPos), requiredList);
				return false;
			} else if (!validMdrNoLength(strCellMdr)) {
				ArrayList<String> requiredList = new ArrayList<String>();
				requiredList.add(strCellHeaderMdr);
				invalidCharMap.put(new Integer(cellPos), requiredList);
				return false;
			} else if (!validMdrNoNumeric(strCellMdr)) {
				ArrayList<String> requiredList = new ArrayList<String>();
				requiredList.add(strCellHeaderMdr);
				invalidCharMap.put(new Integer(cellPos), requiredList);
				return false;
			}
		}

		return true;
	}

	private boolean isOrganizationExists(boolean isMatchUploadOrgIds,
			String orgCode, Integer[] parentOrgIdArray, Integer categoryId,
			String strCellMdr, String orgName) {

		Node organization = null;
		boolean isOrgExist = false;
		Integer parentOrgId = parentOrgIdArray[0];
		if (isMatchUploadOrgIds) {

			isOrgExist = isOrganizationExist(orgCode, parentOrgId, categoryId,
					isMatchUploadOrgIds);
			if (!orgCode.trim().equals("") || !orgName.trim().equals("")) {
				// Search Organization by OrgName
				if (!isOrgExist) {
					isOrgExist = isOrganizationExist(orgName, parentOrgId,
							categoryId, !isMatchUploadOrgIds);
					if (isOrgExist) {

						// retrieve existing organization by passing orgName
						organization = getOrgNodeDetail(orgName, parentOrgId,
								categoryId, false);
						// Is Organization Exist
						if (organization != null) {
							parentOrgId = organization.getOrgNodeId();
							parentOrgIdArray[0] = parentOrgId;
						} else {
							isOrgExist = false;
						}
					}
				} else {
					// retrieve existing organization by passing orgCode
					organization = getOrgNodeDetail(orgCode, parentOrgId,
							categoryId, isMatchUploadOrgIds);
					// Is Organization Exist
					if (organization != null) {
						parentOrgId = organization.getOrgNodeId();
						parentOrgIdArray[0] = parentOrgId;
					} else {
						isOrgExist = false;
					}
				}
			}
		} else { // if no MatchUploadOrgIds present in customer configuration
			isOrgExist = isOrganizationExist(orgName, parentOrgId, categoryId,
					false);
			if (!orgName.trim().equals("")) {
				// if no organization exist
				if (!isOrgExist) {
					isOrgExist = false;
				} else {
					// retrive existing organization by passing orgName
					organization = getOrgNodeDetail(orgName, parentOrgId,
							categoryId, false);
					// Is Organization Exist
					if (organization != null) {
						parentOrgId = organization.getOrgNodeId();
						parentOrgIdArray[0] = parentOrgId;
					} else {
						isOrgExist = false;
					}
				}
			}

		}
		return isOrgExist;
	}

	private boolean validMdrNo(String strCellMdr) {
		if (strCellMdr == null || (strCellMdr.trim().length() == 0)) {
			return false;
		}
		return true;
	}

	private boolean validMdrNoLength(String strCellMdr) {
		if ((strCellMdr.trim().length() != 8)) {
			return false;
		}
		return true;
	}

	private boolean isUniqueMdr(String strCellMdr, List<String> newMDRList,
			OrgMDRDBCacheImpl orgMDRImpl) {
		try {
			String val = orgMDRImpl.getOrgMDRNumber(strCellMdr);
			if (val == null && !(newMDRList.contains(strCellMdr))) {
				return true;
			}

		} catch (Exception e) {
			return false;
		}
		return false;
	}

	private boolean validMdrNoNumeric(String strCellMdr) {
		try {
			Integer.parseInt(strCellMdr.trim());
			if (strCellMdr.trim().indexOf(".") != -1) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Init() is used for general data collection .
	 */
	private void init() {
		try {
			roleMap = this.userFileDao.getRoles();

			TimeZones[] timezones = this.userFileDao.getTimeZones();
			for (int i = 0; i < timezones.length; i++) {
				timeZoneMap.put(timezones[i].getTimeZoneDesc(),
						timezones[i].getTimeZone());
			}

			USState[] states = this.userFileDao.getStates();
			for (int i = 0; i < states.length; i++) {
				stateMap.put(initCap(states[i].getStatePrDesc()),
						states[i].getStatePr());
			}

			this.detailNodeM = this.userFileDao.getUserDataTemplate(customerId);
			this.userTopOrgNode = this.userFileDao
					.getTopNodeDetails(customerId);
			this.dataFileAudit = this.userFileDao
					.getUploadFile(this.uploadFileId);

			this.isMatchUploadOrgIds = this.uploadFileDao
					.checkCustomerConfiguration(customerId,
							Constants.MATCH_ORG_CODE);

		} catch (Exception e) {
			logger.info("Exception in init()" + e.getMessage());
		}

	}

	/**
	 * Creates organization and users.
	 * 
	 * @param categoryName
	 * @param categoryNode
	 * @return void
	 */
	private void createOrganizationAndUser(
			Map<Integer, ArrayList<String>> requiredMap,
			Map<Integer, ArrayList<String>> maxLengthMap,
			Map<Integer, ArrayList<String>> invalidCharMap,
			Map<Integer, ArrayList<String>> logicalErrorMap,
			Map<Integer, ArrayList<String>> hierarchyErrorMap,
			Map<String, String> userDataMap, Map<Integer, String> blankRowMap,
			boolean isMatchUploadOrgIds, Node[] loginUserNodes,
			OrgMDRDBCacheImpl orgMDRImpl) throws SQLException,
			CTBBusinessException {

		int loginUserOrgPosition = 0;
		Node organization = null;
		Integer orgNodeId = null;
		int uploadRecordCount = 0;
		boolean isBlankRow = true;
		try {
			String[] rowHeader = new String[0];
			String[] row;
			int rowIndex = 0;
			boolean isRowHeader = true;
			boolean mdrCorrect = true;
			Node[] nodeCategory = this.userFileRowHeader[0]
					.getOrganizationNodes();
			int orgHeaderLastPosition = nodeCategory.length * orgPosFact;

			CSVReader csv = new CSVReader(new BufferedReader(new FileReader(
					this.inFile)), ',');

			while ((row = csv.readNext()) != null) {
				mdrCorrect = true;
				if (isRowHeader) {
					rowHeader = new String[row.length];
					rowHeader = row;
					isRowHeader = false;
					rowIndex++;
					continue;
				}
				int totalCells = rowHeader.length;
				for (int k = 0; k < totalCells; k++) {
					String cell = row[k];
					if (cell != null && (!getCellValue(cell).trim().equals(""))) {
						isBlankRow = false;
					}
				}
				if (isBlankRow) {
					continue;
				}
				if (!(requiredMap.containsKey(new Integer(rowIndex))
						|| invalidCharMap.containsKey(new Integer(rowIndex))
						|| maxLengthMap.containsKey(new Integer(rowIndex))
						|| logicalErrorMap.containsKey(new Integer(rowIndex))
						|| hierarchyErrorMap.containsKey(new Integer(rowIndex)) || blankRowMap
							.containsKey(new Integer(rowIndex)))) {

					// OrganizationCreation or Existency check process
					loginUserOrgPosition = getLoginUserOrgPosition(row,
							rowHeader, loginUserNodes);

					String loginUserOrgCell = row[loginUserOrgPosition];

					String loginUserOrgName = getCellValue(loginUserOrgCell);
					Node loginUserNode = getLoginUserOrgDetail(loginUserNodes,
							loginUserOrgName);

					// orgNodeId and parentId initialization process
					Integer parentOrgId = loginUserNode.getOrgNodeId();
					orgNodeId = loginUserNode.getOrgNodeId();
					for (int ii = loginUserOrgPosition + orgPosFact; ii < orgHeaderLastPosition
							&& mdrCorrect; ii = ii + orgPosFact) {
						String OrgCellName = row[ii];
						String OrgCellId = row[ii + 1];
						String orgCode = getCellValue(OrgCellId);
						String orgName = getCellValue(OrgCellName);
						String OrgCellHeaderName = rowHeader[ii];
						String headerName = getCellValue(OrgCellHeaderName);
						Integer categoryId = getCategoryId(headerName,
								nodeCategory);
						String orgMdr = null;
						String OrgCellMdr = row[ii + 2];
						orgMdr = getCellValue(OrgCellMdr);
						if (!hasOrganization(ii, row) && orgName.equals("")
								&& orgCode.equals("")) {
							break;
						} else if (hasOrganization(ii, row)
								&& orgName.equals("") && orgCode.equals("")) {
							continue;
						} else {
							// Search Organization by OrgCode
							if (isMatchUploadOrgIds) {
								boolean isOrgExist = isOrganizationExist(
										orgCode, parentOrgId, categoryId,
										isMatchUploadOrgIds);
								if (!orgCode.trim().equals("")
										|| !orgName.trim().equals("")) {
									// Search Organization by OrgName
									if (!isOrgExist) {
										isOrgExist = isOrganizationExist(
												orgName, parentOrgId,
												categoryId,
												!isMatchUploadOrgIds);
										// No organization Exist
										if (!isOrgExist) {
											organization = new Node();
											organization
													.setCustomerId(customerId);
											organization
													.setOrgNodeName(orgName);
											organization
													.setOrgNodeCode(orgCode);
											organization
													.setOrgNodeCategoryId(categoryId);
											organization
													.setParentOrgNodeId(parentOrgId);
											organization.setMdrNumber(orgMdr);

											// create Organization
											organization = this.organizationManagement
													.createOrganization(null,
															organization,
															orgMDRImpl);

											if (organization.getOrgNodeId() == -99) {
												mdrCorrect = false;
												ArrayList<String> requiredList = new ArrayList<String>();
												requiredList
														.add(rowHeader[ii + 2]);
												logicalErrorMap.put(
														new Integer(rowIndex),
														requiredList);
												break;
											}

											// parentId and orgNodeId updated
											parentOrgId = organization
													.getOrgNodeId();
											orgNodeId = organization
													.getOrgNodeId();
											ArrayList<Node> tempList = new ArrayList<Node>(
													Arrays.asList(this.detailNodeM));
											tempList.add(organization);
											this.detailNodeM = (Node[]) tempList
													.toArray(new Node[0]);
										} else {
											// retrieve existing organization by
											// passing orgName
											organization = getOrgNodeDetail(
													orgName, parentOrgId,
													categoryId, false);
											// Is Organization Exist
											if (organization != null) {
												parentOrgId = organization
														.getOrgNodeId();
												orgNodeId = organization
														.getOrgNodeId();
												continue;
											} else {
												// new Organization creation
												organization = new Node();
												organization
														.setCustomerId(customerId);
												organization
														.setOrgNodeName(orgName);
												organization
														.setOrgNodeCode(orgCode);
												organization
														.setOrgNodeCategoryId(categoryId);
												organization
														.setParentOrgNodeId(parentOrgId);
												organization
														.setMdrNumber(orgMdr);
												// create Organization
												organization = this.organizationManagement
														.createOrganization(
																null,
																organization,
																orgMDRImpl);

												if (organization.getOrgNodeId() == -99) {
													mdrCorrect = false;
													ArrayList<String> requiredList = new ArrayList<String>();
													requiredList
															.add(rowHeader[ii + 2]);
													logicalErrorMap.put(
															new Integer(
																	rowIndex),
															requiredList);
													break;
												}
												// parentId and orgNodeId
												// updated
												parentOrgId = organization
														.getOrgNodeId();
												orgNodeId = organization
														.getOrgNodeId();
												ArrayList<Node> tempList = new ArrayList<Node>(
														Arrays.asList(this.detailNodeM));
												tempList.add(organization);
												this.detailNodeM = (Node[]) tempList
														.toArray(new Node[0]);
											}
										}
									} else {
										// retrieve existing organization by
										// passing orgCode
										organization = getOrgNodeDetail(
												orgCode, parentOrgId,
												categoryId, isMatchUploadOrgIds);
										// Is Organization Exist
										if (organization != null) {
											parentOrgId = organization
													.getOrgNodeId();
											orgNodeId = organization
													.getOrgNodeId();
											continue;
										} else {
											// new Organization creation
											organization = new Node();
											organization
													.setCustomerId(customerId);
											organization
													.setOrgNodeName(orgName);
											organization
													.setOrgNodeCode(orgCode);
											organization
													.setOrgNodeCategoryId(categoryId);
											organization
													.setParentOrgNodeId(parentOrgId);
											organization.setMdrNumber(orgMdr);
											// create Organization
											organization = this.organizationManagement
													.createOrganization(null,
															organization,
															orgMDRImpl);

											if (organization.getOrgNodeId() == -99) {
												mdrCorrect = false;
												ArrayList<String> requiredList = new ArrayList<String>();
												requiredList
														.add(rowHeader[ii + 2]);
												logicalErrorMap.put(
														new Integer(rowIndex),
														requiredList);
												break;
											}
											// parentId and orgNodeId updated
											parentOrgId = organization
													.getOrgNodeId();
											orgNodeId = organization
													.getOrgNodeId();
											ArrayList<Node> tempList = new ArrayList<Node>(
													Arrays.asList(this.detailNodeM));
											tempList.add(organization);
											this.detailNodeM = (Node[]) tempList
													.toArray(new Node[0]);
										}
									}
								}// End if
							} else { // if no MatchUploadOrgIds present in
										// customer configuration
								boolean isOrgExist = isOrganizationExist(
										orgName, parentOrgId, categoryId, false);
								if (!orgName.trim().equals("")) {
									// if no organization exist
									if (!isOrgExist) {
										// new Organization creation
										organization = new Node();
										organization.setCustomerId(customerId);
										organization.setOrgNodeName(orgName);
										organization.setOrgNodeCode(orgCode);
										organization
												.setOrgNodeCategoryId(categoryId);
										organization
												.setParentOrgNodeId(parentOrgId);
										organization.setMdrNumber(orgMdr);
										// create Organization
										organization = this.organizationManagement
												.createOrganization(null,
														organization,
														orgMDRImpl);

										if (organization.getOrgNodeId() == -99) {
											mdrCorrect = false;
											ArrayList<String> requiredList = new ArrayList<String>();
											requiredList.add(rowHeader[ii + 2]);
											logicalErrorMap.put(new Integer(
													rowIndex), requiredList);
											break;
										}
										// parentId and orgNodeId updated
										parentOrgId = organization
												.getOrgNodeId();
										orgNodeId = organization.getOrgNodeId();
										ArrayList<Node> tempList = new ArrayList<Node>(
												Arrays.asList(this.detailNodeM));
										tempList.add(organization);
										this.detailNodeM = (Node[]) tempList
												.toArray(new Node[0]);
									} else {
										// retrieve existing organization by
										// passing orgName
										organization = getOrgNodeDetail(
												orgName, parentOrgId,
												categoryId, false);
										// Is Organization Exist
										if (organization != null) {
											parentOrgId = organization
													.getOrgNodeId();
											orgNodeId = organization
													.getOrgNodeId();
											continue;
										} else {
											// new Organization creation
											organization = new Node();
											organization
													.setCustomerId(customerId);
											organization
													.setOrgNodeName(orgName);
											organization
													.setOrgNodeCode(orgCode);
											organization
													.setOrgNodeCategoryId(categoryId);
											organization
													.setParentOrgNodeId(parentOrgId);
											organization.setMdrNumber(orgMdr);
											// create Organization
											organization = this.organizationManagement
													.createOrganization(null,
															organization,
															orgMDRImpl);

											if (organization.getOrgNodeId() == -99) {
												mdrCorrect = false;
												ArrayList<String> requiredList = new ArrayList<String>();
												requiredList
														.add(rowHeader[ii + 2]);
												logicalErrorMap.put(
														new Integer(rowIndex),
														requiredList);
												break;
											}
											// parentId and orgNodeId updated
											parentOrgId = organization
													.getOrgNodeId();
											orgNodeId = organization
													.getOrgNodeId();
											ArrayList<Node> tempList = new ArrayList<Node>(
													Arrays.asList(this.detailNodeM));
											tempList.add(organization);
											this.detailNodeM = (Node[]) tempList
													.toArray(new Node[0]);
										}
									}
								}// End of checking orgName
							} // Else block end
						} // Else block (Organization creation process)
					}

					if (!mdrCorrect) {
						continue;
					}

					Node[] orgDetail = new Node[1];
					orgDetail[0] = new Node();
					orgDetail[0].setOrgNodeId(orgNodeId);
					// User creation or User Existence check process
					for (int j = orgHeaderLastPosition; j < rowHeader.length; j++) {
						String headerCell = rowHeader[j];
						String bodyCell = row[j];
						String strHeaderValue = getCellValue(headerCell);
						String strBodyValue = getCellValue(bodyCell);
						if (strHeaderValue.equals(Constants.REQUIREDFIELD_ROLE)) {
							userDataMap.put(strHeaderValue,
									initCap(strBodyValue));
						} else {
							userDataMap.put(strHeaderValue, strBodyValue);
						}
					}

					/**
					 * This method determines the Insert/Update Flag of an User.
					 */
					createUpdateUser(userDataMap, orgDetail);

					uploadRecordCount++;
				}
				isBlankRow = true;
				rowIndex++;
			}// While loop end.
			csv.close();

			/**
			 * User Insert Execution Process
			 */
			if (this.userNewCacheImpl.getCacheSize() > 0) {
				logger.info("Users to be Inserted::--> "
						+ this.userNewCacheImpl.getCacheSize());
				logger.info("ExecuteUserCreation Start Time:"
						+ new Date(System.currentTimeMillis()));
				this.userManagement.executeUserCreation(this.userNewCacheImpl,
						this.customerId, this.keyUserIdMap,
						this.keyAddressIdMap);
				logger.info("ExecuteUserCreation End Time:"
						+ new Date(System.currentTimeMillis()));
			}

			this.userNewCacheImpl.clearCacheContents();
			this.userNewCacheImpl = null;

			/**
			 * User Update Execution Process
			 */
			if (this.userUpdateCacheImpl.getCacheSize() > 0) {
				logger.info("Users to be Updated::--> "
						+ this.userUpdateCacheImpl.getCacheSize());
				logger.info("ExecuteUserUpdate Start Time:"
						+ new Date(System.currentTimeMillis()));
				this.userManagement.executeUserUpdate(this.userUpdateCacheImpl,
						this.customerId, this.keyUserIdMap,
						this.keyAddressIdMap);
				logger.info("ExecuteUserUpdate End Time:"
						+ new Date(System.currentTimeMillis()));
			}

			this.userUpdateCacheImpl.clearCacheContents();
			this.userUpdateCacheImpl = null;

			if (this.dataFileAudit.getFailedRecordCount() == null
					|| this.dataFileAudit.getFailedRecordCount().intValue() == 0) {
				this.dataFileAudit.setStatus("SC");
				this.dataFileAudit.setFaildRec(null);
			} else {
				this.dataFileAudit.setStatus("FL");
			}
			this.dataFileAudit.setUploadFileRecordCount(new Integer(
					uploadRecordCount));
			uploadFileDao.upDateAuditTable(this.dataFileAudit);

			/**
			 * Mail Sending process to the user.
			 */
			/*
			 * String loginUserMail = users.getUserDetails(this.username)
			 * .getEmail(); // send email if (loginUserMail != null) {
			 * sendMail(this.username, Constants.EMAIL_TYPE_WELCOME,
			 * loginUserMail); }
			 */

		} catch (SQLException se) {
			dataFileAudit.setFaildRec(null);
			dataFileAudit.setStatus("FL");
			dataFileAudit.setFailedRecordCount(new Integer(0));
			dataFileAudit.setUploadFileRecordCount(new Integer(0));
			try {
				uploadFileDao.upDateAuditTable(dataFileAudit);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			FileNotUploadedException dataNotFoundException = new FileNotUploadedException(
					"UploadDownloadManagement.Failed");
			dataNotFoundException.setStackTrace(se.getStackTrace());
			throw dataNotFoundException;

		} catch (Exception e) {
			dataFileAudit.setFaildRec(null);
			dataFileAudit.setStatus("FL");
			dataFileAudit.setFailedRecordCount(new Integer(0));
			dataFileAudit.setUploadFileRecordCount(new Integer(0));
			try {
				uploadFileDao.upDateAuditTable(dataFileAudit);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			FileNotUploadedException dataNotFoundException = new FileNotUploadedException(
					"UploadDownloadManagement.Failed");
			dataNotFoundException.setStackTrace(e.getStackTrace());
			throw dataNotFoundException;

		}

	}

	/*
   *
   */

	private Integer getCategoryId(String categoryName, Node[] categoryNode) {
		Integer categoryId = null;
		for (int i = 0; i < categoryNode.length; i++) {
			Node tempNode = categoryNode[i];
			if (tempNode.getOrgNodeCategoryName()
					.equalsIgnoreCase(categoryName)) {
				categoryId = tempNode.getOrgNodeCategoryId();
			}
		}
		return categoryId;
	}

	/*
	 * retrieve login user org detail
	 */
	private Node getLoginUserOrgDetail(Node[] loginUserNodes,
			String organizationName) {
		Node orgDetail = null;
		for (int i = 0; i < loginUserNodes.length; i++) {
			orgDetail = loginUserNodes[i];
			if (orgDetail.getOrgNodeName().equalsIgnoreCase(organizationName)) {
				break;
			}
		}
		return orgDetail;
	}

	/*
   *
   */
	private boolean isOrganizationExist(String searchString, Integer parentId,
			Integer categoryId, boolean isMatchUploadOrgIds) {

		boolean hasOrganization = false;
		try {

			Node[] detailNode = this.detailNodeM;
			for (int i = 0; i < detailNode.length; i++) {
				Node tempNode = detailNode[i];
				if (isMatchUploadOrgIds) {
					if (tempNode.getOrgNodeCode() != null) {
						if (!searchString.trim().equals("")
								&& tempNode.getOrgNodeCode().equalsIgnoreCase(
										searchString)
								&& tempNode.getParentOrgNodeId().intValue() == parentId
										.intValue()
								&& categoryId.intValue() == tempNode
										.getOrgNodeCategoryId().intValue()) {
							hasOrganization = true;
							break;
						}
					}
				} else {
					if (!searchString.trim().equals("")
							&& tempNode.getOrgNodeName().equalsIgnoreCase(
									searchString)
							&& tempNode.getParentOrgNodeId().intValue() == parentId
									.intValue()
							&& categoryId.intValue() == tempNode
									.getOrgNodeCategoryId().intValue()) {
						hasOrganization = true;
						break;
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return hasOrganization;
	}

	/**
	 * 
	 * @param orgName
	 * @param parentId
	 * @param categoryId
	 * @param isMatchUploadOrgIds
	 * @return
	 */
	private Node getOrgNodeDetail(String orgName, Integer parentId,
			Integer categoryId, boolean isMatchUploadOrgIds) {

		Node orgNode = null;
		try {
			Node[] detailNode = this.detailNodeM;
			for (int i = 0; i < detailNode.length; i++) {
				Node tempNode = detailNode[i];
				if (isMatchUploadOrgIds) {
					if (tempNode.getOrgNodeCode() != null) {
						if (tempNode.getOrgNodeCode().equalsIgnoreCase(orgName)
								&& tempNode.getParentOrgNodeId().intValue() == parentId
										.intValue()
								&& tempNode.getOrgNodeCategoryId().intValue() == categoryId
										.intValue()) {
							orgNode = tempNode;
							break;
						}
					}
				} else {
					if (tempNode.getOrgNodeName().equalsIgnoreCase(orgName)
							&& tempNode.getParentOrgNodeId().intValue() == parentId
									.intValue()
							&& tempNode.getOrgNodeCategoryId().intValue() == categoryId
									.intValue()) {
						orgNode = tempNode;
						break;

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orgNode;
	}

	/**
	 * 
	 * @param userDataMap
	 * @param userNode
	 * @throws Exception
	 * @throws CTBBusinessException
	 */
	private void createUpdateUser(Map<String, String> userDataMap,
			Node[] userNode) throws Exception, CTBBusinessException {

		UserFileRow user = new UserFileRow();

		// Set address details
		user.setAddress1((String) userDataMap.get(Constants.ADDRESS_LINE_1));
		user.setAddress2((String) userDataMap.get(Constants.ADDRESS_LINE_2));
		user.setCity((String) userDataMap.get(Constants.CITY));
		if (userDataMap.get(Constants.STATE_NAME) != null) {
			user.setState((String) stateMap.get(initCap((String) userDataMap
					.get(Constants.STATE_NAME))));
		}
		String zipCode = (String) userDataMap.get(Constants.ZIP);
		if (zipCode != null && !"".equals(zipCode)) {
			user.setZip(zipCode.substring(0, 5).trim());
			if (zipCode.length() > 6) {
				int extStartPos = zipCode.indexOf("-") + 1;
				user.setZipCodeExt(zipCode.substring(extStartPos,
						zipCode.length()).trim());
			}
		}

		String primaryPhoneNumber = (String) userDataMap
				.get(Constants.PRIMARY_PHONE);
		primaryPhoneNumber = getPhoneFax(primaryPhoneNumber);
		user.setPrimaryPhone(primaryPhoneNumber);

		String secondaryPhoneNumber = (String) userDataMap
				.get(Constants.SECONDARY_PHONE);
		secondaryPhoneNumber = getPhoneFax(secondaryPhoneNumber);
		user.setSecondaryPhone(secondaryPhoneNumber);
		String faxNumber = (String) userDataMap.get(Constants.FAX);
		faxNumber = getPhoneFax(faxNumber);
		user.setFaxNumber(faxNumber);
		// User personal details
		user.setFirstName(initStringCap((String) userDataMap
				.get(Constants.REQUIREDFIELD_FIRST_NAME)));
		user.setMiddleName(initStringCap((String) userDataMap
				.get(Constants.MIDDLE_NAME)));
		user.setLastName(initStringCap((String) userDataMap
				.get(Constants.REQUIREDFIELD_LAST_NAME)));
		user.setEmail((String) userDataMap.get(Constants.EMAIL));
		user.setTimeZone((String) timeZoneMap.get(initCap((String) userDataMap
				.get(Constants.REQUIREDFIELD_TIME_ZONE))));

		// Set role Details
		user.setRoleId((Integer) roleMap.get((String) userDataMap
				.get(Constants.REQUIREDFIELD_ROLE)));
		user.setRoleName((String) userDataMap.get(Constants.REQUIREDFIELD_ROLE));

		// Set External User Id for CR
		user.setExtSchoolId((String) userDataMap.get(Constants.EXT_SCHOOL_ID));

		/**
		 * Key for identifying User is User-name
		 */
		String generatedUserName = UserUtils.generateEscapeUsername(user);
		user.setKey(user.getExtSchoolId());
		user.setBasicUserName(generatedUserName);
		user.setPassword(UserUtils
				.generateRandomPassword(Constants.PASSWORD_LENGTH));
		user.setAddressPresent(isAddressPresent(user));

		/**
		 * This method will determine if the User is existing in the database or
		 * not?
		 */

		UserFileRow existingUserDetails = isUserExists(user);
		/**
		 * New Record if not found in the Database populated Cache
		 */
		if (existingUserDetails == null) {
			user.setOrganizationNodes(userNode);
			this.dbCacheImpl.addUserFileRow(user.getExtSchoolId(), user);
			this.userNewCacheImpl.addNewUser(user.getExtSchoolId(), user);

		} else {
			/**
			 * Update Record if found in the Database populated Cache
			 */
			Node[] orgNodes = existingUserDetails.getOrganizationNodes();
			int size = orgNodes.length;
			Node[] updateNodes = new Node[size + 1];
			user.setUserId(existingUserDetails.getUserId());
			if (!isOrganizationPresent(userNode[0].getOrgNodeId(), orgNodes)) {
				for (int i = 0; i < orgNodes.length; i++) {
					updateNodes[i] = orgNodes[i];
				}
				updateNodes[size] = userNode[0];
				user.setOrganizationNodes(updateNodes);
			} else {
				user.setOrganizationNodes(orgNodes);
			}
			this.dbCacheImpl.addUserFileRow(user.getExtSchoolId(), user);
			this.userUpdateCacheImpl
					.addUpdatedUser(user.getExtSchoolId(), user);
		}

	}

	private boolean isAddressPresent(UserFileRow user) {
		return ((user.getAddress1() != null && !"".equals(user.getAddress1()
				.trim()))
				|| (user.getAddress2() != null && !"".equals(user.getAddress2()
						.trim()))
				|| (user.getCity() != null && !"".equals(user.getCity().trim()))
				|| (user.getFaxNumber() != null && !"".equals(user
						.getFaxNumber().trim()))
				|| (user.getState() != null && !"".equals(user.getState()
						.trim()))
				|| (user.getPrimaryPhone() != null && !"".equals(user
						.getPrimaryPhone().trim())) || (user
				.getSecondaryPhone() != null && !"".equals(user
				.getSecondaryPhone().trim())));
	}

	private UserFileRow isUserExists(UserFileRow user) {
		return this.dbCacheImpl.getUserFileRow(user.getKey());
	}

	/*
	 * Is user alresdy associate with orgNodeId
	 */
	private static boolean isOrganizationPresent(Integer orgNodeId,
			Node[] orgNodes) {
		for (int i = 0; i < orgNodes.length; i++) {
			Node tempNode = orgNodes[i];
			if (orgNodeId.intValue() == tempNode.getOrgNodeId().intValue()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This part is executed for Error Excel Creation
	 * 
	 * @param requiredMap
	 * @param maxLengthMap
	 * @param invalidCharMap
	 * @param logicalErrorMap
	 * @param hierarchyErrorMap
	 * @param leafNodeErrorMap
	 * @param minLengthMap
	 * @throws Exception
	 */
	private void errorExcelCreation(
			Map<Integer, ArrayList<String>> requiredMap,
			Map<Integer, ArrayList<String>> maxLengthMap,
			Map<Integer, ArrayList<String>> invalidCharMap,
			Map<Integer, ArrayList<String>> logicalErrorMap,
			Map<Integer, ArrayList<String>> hierarchyErrorMap) throws Exception {

		int errorCount = 0;
		byte[] errorData = null;

		UploadFileDao dao = new UploadFileDaoImpl();

		String errorFileName = Configuration.getLocalFilePath()
				+ Constants.FILE_SEPARATOR + this.inFile.getName() + ".errors";

		BufferedWriter bWriter = new BufferedWriter(new FileWriter(
				errorFileName, true), ',');
		CSVWriter csvOutput = new CSVWriter(bWriter);
		CSVReader csvReader = new CSVReader(new BufferedReader(new FileReader(
				this.inFile)), ',');

		try {
			String[] rowData;
			String[] rowHeaderData = new String[0];
			boolean isFileHeader = true;
			ArrayList<String> rowDataList;
			int rowNumber = 0;
			int totalCells = 0;
			while ((rowData = csvReader.readNext()) != null) {

				totalCells = rowData.length;
				if (isFileHeader) {
					rowDataList = new ArrayList<String>(Arrays.asList(rowData));
					rowHeaderData = new String[totalCells];
					rowHeaderData = rowData;
					rowDataList.add(Constants.ERROR_FIELD_DESCRIPTION);
					csvOutput.writeNext(rowDataList
							.toArray(new String[rowDataList.size()]));
				} else {
					if ((requiredMap.containsKey(new Integer(rowNumber))
							|| invalidCharMap
									.containsKey(new Integer(rowNumber))
							|| maxLengthMap.containsKey(new Integer(rowNumber))
							|| logicalErrorMap.containsKey(new Integer(
									rowNumber)) || hierarchyErrorMap
								.containsKey(new Integer(rowNumber)))) {

						rowDataList = new ArrayList<String>(
								Arrays.asList(rowData));
						errorCount++;

						for (int cellPosition = 0; cellPosition < totalCells; cellPosition++) {

							/**
							 * checking for required field
							 * */
							if (requiredMap.size() > 0) {
								if (requiredMap.containsKey(new Integer(
										rowNumber))) {
									ArrayList<String> requiredList = requiredMap
											.get(new Integer(rowNumber));
									if (requiredList
											.contains(rowHeaderData[cellPosition])) {
										/*
										 * rowDataList
										 * .add(rowHeaderData[cellPosition]);
										 */
										rowDataList
												.add(Constants.REQUIRED_FIELD_ERROR
														+ " - "
														+ rowHeaderData[cellPosition]);
										break;
									}
								}
							}

							/**
							 * checking for invalid field
							 * */
							if (invalidCharMap.size() > 0) {
								if (invalidCharMap.containsKey(new Integer(
										rowNumber))) {
									ArrayList<String> invalidCharList = invalidCharMap
											.get(new Integer(rowNumber));
									if (invalidCharList
											.contains(rowHeaderData[cellPosition])) {
										rowDataList
												.add(Constants.INVALID_FIELD_ERROR
														+ " - "
														+ rowHeaderData[cellPosition]);
										break;
									}
								}
							}

							/**
							 * checking for maximum length field
							 * */
							if (maxLengthMap.size() > 0) {
								if (maxLengthMap.containsKey(new Integer(
										rowNumber))) {
									ArrayList<String> maxLengthList = maxLengthMap
											.get(new Integer(rowNumber));
									if (maxLengthList
											.contains(rowHeaderData[cellPosition])) {
										rowDataList
												.add(Constants.MAXIMUM_FIELD_ERROR
														+ " - "
														+ rowHeaderData[cellPosition]);
										break;
									}
								}
							}

							/**
							 * checking for logical error
							 * */
							if (logicalErrorMap.size() > 0) {
								if (logicalErrorMap.containsKey(new Integer(
										rowNumber))) {
									ArrayList<String> logicalErrorList = logicalErrorMap
											.get(new Integer(rowNumber));
									if (logicalErrorList
											.contains(rowHeaderData[cellPosition])) {
										rowDataList
												.add(Constants.LOGICAL_FIELD_ERROR
														+ " - "
														+ rowHeaderData[cellPosition]);
										break;
									}
								}
							}

						}// for block

						csvOutput.writeNext(rowDataList
								.toArray(new String[rowDataList.size()]));
					}

				}// else block

				isFileHeader = false;
				rowNumber++;
			}

			bWriter.close();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			InputStream in = new FileInputStream(errorFileName);
			BufferedInputStream bin = new BufferedInputStream(in);
			errorData = new byte[20000000];
			byte[] readBuf = new byte[512 * 1024];
			int readCnt = bin.read(readBuf);
			while (0 < readCnt) {
				baos.write(readBuf, 0, readCnt);
				readCnt = bin.read(readBuf);
			}
			bin.close();
			in.close();
			errorData = baos.toByteArray();

			dataFileAudit.setFaildRec(errorData);
			dataFileAudit.setFailedRecordCount(new Integer(errorCount));

			if (this.dataFileAudit.getFailedRecordCount() == null
					|| this.dataFileAudit.getFailedRecordCount().intValue() == 0) {
				this.dataFileAudit.setStatus("SC");
				this.dataFileAudit.setFaildRec(null);
			} else {
				this.dataFileAudit.setStatus("FL");
			}

			dao.upDateAuditTable(dataFileAudit);
			baos.flush();
			baos.close();

			/**
			 * Error File transfer to FTP Location
			 * */
			if (errorCount > 0) {
				logger.info("Total error records present are : " + errorCount);
				new FtpSftpUtil().sendfilesSFTP(Configuration.getErrorPath(),
						errorFileName);
				logger.info("Error File is Created and Placed at specified Location..");
			}

		} catch (Exception e) {
			dataFileAudit.setFaildRec(errorData);
			dataFileAudit.setStatus("FL");
			dataFileAudit.setFailedRecordCount(new Integer(errorCount));
			dataFileAudit.setUploadFileRecordCount(new Integer(0));
			try {
				dao.upDateAuditTable(dataFileAudit);
			} catch (Exception se) {
				se.printStackTrace();
			}
			e.printStackTrace();
			throw e;
		} finally {
			csvOutput.close();
			csvReader.close();
		}

	}

	/**
	 * 
	 * @param currentPosition
	 * @param row
	 * @return boolean
	 */
	private boolean hasOrganization(int currentPosition, String[] row) {
		Node[] node = this.userFileRowHeader[0].getOrganizationNodes();
		int OrgHeaderLastPosition = node.length * orgPosFact;
		for (int j = currentPosition + orgPosFact; j < OrgHeaderLastPosition; j = j
				+ orgPosFact) {
			String cellName = row[j];
			String cellId = row[j + 1];
			if (!getCellValue(cellName).equals("")
					|| !getCellValue(cellId).equals("")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get the Login position of the User uploading the file.
	 * 
	 * @param row
	 * @param rowHeader
	 * @param loginUserNode
	 * @return integer
	 */
	private int getLoginUserOrgPosition(String[] row, String[] rowHeader,
			Node[] loginUserNode) {
		int loginUserPosition = 0;
		try {
			Node[] node = this.userFileRowHeader[0].getOrganizationNodes();
			int OrgHeaderLastPosition = node.length;
			String leafOrgName = "";
			for (int i = 0, j = 0; i < OrgHeaderLastPosition; i++, j = j
					+ orgPosFact) {
				String cell = row[j];
				if (!getCellValue(cell).equals(" ")) {
					leafOrgName = cell;
					if (isLoginUserOrganization(leafOrgName, loginUserNode,
							false)) {
						loginUserPosition = j;
						break;
					}
				}
			} // end for
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loginUserPosition;
	}

	/**
	 * Validation of data present in the User File
	 * 
	 * @param rowPosition
	 * @param row
	 * @param rowHeader
	 * @param requiredMap
	 * @param maxLengthMap
	 * @param invalidCharMap
	 * @param logicalErrorMap
	 * @param isMatchUploadOrgIds
	 * @throws Exception
	 */
	private void getEachRowUserDetail(int rowPosition, String[] row,
			String[] rowHeader, Map<Integer, ArrayList<String>> requiredMap,
			Map<Integer, ArrayList<String>> maxLengthMap,
			Map<Integer, ArrayList<String>> invalidCharMap,
			Map<Integer, ArrayList<String>> logicalErrorMap,
			boolean isMatchUploadOrgIds) throws Exception {

		ArrayList<String> requiredList = new ArrayList<String>();
		ArrayList<String> invalidList = new ArrayList<String>();
		ArrayList<String> maxLengthList = new ArrayList<String>();
		ArrayList<String> logicalErrorList = new ArrayList<String>();

		Node[] node = this.userFileRowHeader[0].getOrganizationNodes();
		int userHeaderStartPosition = node.length * orgPosFact;

		if (isRequired(userHeaderStartPosition, row, rowHeader, requiredList)) {
			requiredMap.put(new Integer(rowPosition), requiredList);
		} else if (isInvalidChar(userHeaderStartPosition, row, rowHeader,
				invalidList)) {
			invalidCharMap.put(new Integer(rowPosition), invalidList);
		} else if (isMaxlengthExceed(userHeaderStartPosition, row, rowHeader,
				maxLengthList)) {
			maxLengthMap.put(new Integer(rowPosition), maxLengthList);
		} else if (isLogicalError(userHeaderStartPosition, row, rowHeader,
				logicalErrorList, isMatchUploadOrgIds)) {
			logicalErrorMap.put(new Integer(rowPosition), logicalErrorList);
		}

	}

	/**
	 * This is a generic method to send mail. It retrieves the content of the
	 * body from database. value should be an empty string even If for some
	 * email_type, there is no replacement in the body. Caller should ensure
	 * that to_address is not null. This method suppresses any exception
	 * occurred.
	 * 
	 */
	/*
	 * private void sendMail(String userName, Integer emailType, String to) {
	 * try { CustomerEmail emailData = new CustomerEmail(); if (userName !=
	 * null) { emailData = this.userFileDao.getCustomerEmailByUserName(
	 * userName, emailType); } String content = Constants.USER_MAIL_BODY;
	 * 
	 * InitialContext ic = new InitialContext();
	 * 
	 * // the properties were configured in WebLogic through the console
	 * javax.mail.Session session = (Session) ic .lookup("UserManagementMail");
	 * 
	 * // contruct the actual message Message msg = new MimeMessage(session);
	 * msg.setFrom(new InternetAddress(Constants.EMAIL_FROM));
	 * 
	 * // emailTo could be a comma separated list of addresses
	 * msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to,
	 * false)); msg.setSubject(Constants.EMAIL_SUBJECT); msg.setText(content);
	 * msg.setSentDate(new Date());
	 * 
	 * // send the message Transport.send(msg);
	 * 
	 * } catch (Exception e) { e.printStackTrace();
	 * logger.error(e.getMessage());
	 * logger.error("sendMail failed for emailType: " + emailType); } }
	 */

	/**
	 * Checks if uploaded values of TimeZone , Role, State name are having
	 * correct values as per Database entries.
	 * 
	 * @param userHeaderStartPosition
	 * @param row
	 * @param rowHeader
	 * @param logicalErrorList
	 * @param isMatchUploadOrgIds
	 * @return boolean
	 */
	private boolean isLogicalError(int userHeaderStartPosition, String[] row,
			String[] rowHeader, List<String> logicalErrorList,
			boolean isMatchUploadOrgIds) {
		int totalCells = rowHeader.length;
		String strCell = "";
		for (int i = userHeaderStartPosition; i < totalCells; i++) {
			String cellHeader = rowHeader[i];
			String cell = row[i];
			strCell = getCellValue(cell);
			if (!strCell.equals("")) {
				if (cellHeader.equals(Constants.REQUIREDFIELD_TIME_ZONE)
						&& !isTimeZoneSame(strCell)) {
					logicalErrorList.add(Constants.REQUIREDFIELD_TIME_ZONE);
				} else if (cellHeader.equals(Constants.REQUIREDFIELD_ROLE)
						&& !isRoleNameSame(strCell)) {
					logicalErrorList.add(Constants.REQUIREDFIELD_ROLE);
				} else if (cellHeader.equals(Constants.STATE_NAME)
						&& !isStateSame(strCell)) {
					logicalErrorList.add(Constants.STATE_NAME);
				}
			}
		}
		if (logicalErrorList.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	/*
	 * Is Upload Excel rolename and database role name are same
	 */
	private boolean isRoleNameSame(String value) {

		if (roleMap.containsKey(initCap(value))) {

			return true;

		} else {

			return false;

		}

	}

	/*
	 * Is Upload Excel timezone and database timezone name are same
	 */
	private boolean isTimeZoneSame(String value) {
		if (timeZoneMap.containsKey(initCap(value))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Converts first characters as Caps
	 * 
	 * @param value
	 * @return String
	 */
	private String initCap(String value) {
		if (value != null && !value.trim().equals("")) {
			String str[] = value.trim().split(" ");
			String initString = "";
			if (str.length > 1) {

				for (int i = 0; i < str.length; i++) {

					if (str[i].length() > 0) {
						String firstValue = str[i].trim().toLowerCase();
						char ch = firstValue.charAt(0);
						String newStringChar = (new Character(ch)).toString()
								.toUpperCase();
						firstValue = firstValue.substring(1,
								firstValue.length());
						if (i == 0) {

							initString = initString + newStringChar
									+ firstValue;

						} else {

							initString = initString + " " + newStringChar
									+ firstValue;
						}

					}

				}
				return initString;
			} else {

				String firstValue = str[0].toLowerCase();
				char ch = firstValue.charAt(0);
				String newStringChar = (new Character(ch)).toString()
						.toUpperCase();
				firstValue = firstValue.substring(1, firstValue.length());
				firstValue = newStringChar + firstValue;
				return firstValue;

			}

		} else {

			return null;

		}

	}

	/*
	 * initStringCap
	 */
	private static String initStringCap(String value) {

		if (value != null && !value.trim().equals("")) {

			char ch = value.charAt(0);

			if ((ch >= 65 && ch <= 90) || (ch >= 97 && ch <= 122)) {

				String newStringChar = (new Character(ch)).toString()
						.toUpperCase();
				value = value.substring(1, value.length());
				value = newStringChar + value;

			}

		}

		return value;
	}

	/*
	 * Is Upload Excel state and database state name are same
	 */
	private boolean isStateSame(String value) {

		if (stateMap.containsKey(initCap(value))) {

			return true;

		} else {

			return false;

		}

	}

	/*
	 * Is admin belonging organization is login admin organization
	 */

	private boolean isLoginUserOrganization(String orgName,
			Node[] loginUserNode, boolean isMatchUploadOrgIds) {

		String[] orgDetail = null;

		if (isMatchUploadOrgIds) {

			orgDetail = orgName.split("\\|");

		}

		for (int i = 0; i < loginUserNode.length; i++) {

			Node tempNode = loginUserNode[i];

			if (isMatchUploadOrgIds) {

				if (!orgDetail[0].equalsIgnoreCase("")) {

					// Check for orgCode
					if (orgDetail[0]
							.equalsIgnoreCase(tempNode.getOrgNodeCode())) {

						return true;

					} else {

						// Check for orgName
						if (orgDetail[1].equalsIgnoreCase(tempNode
								.getOrgNodeName())) {

							return true;

						}

					}

				} else {

					// Check for orgName
					if (orgDetail[1]
							.equalsIgnoreCase(tempNode.getOrgNodeName())) {

						return true;

					}

				}

			} else {

				// Check for orgName
				if (tempNode.getOrgNodeName().equalsIgnoreCase(orgName)) {

					return true;
				}
			}

		}

		return false;

	}

	/**
	 * Checks for Required fields in the File
	 * 
	 * @param userHeaderStartPosition
	 * @param row
	 * @param rowHeader
	 * @param requiredList
	 * @return boolean
	 */
	private boolean isRequired(int userHeaderStartPosition, String[] row,
			String[] rowHeader, List<String> requiredList) {

		int totalCells = rowHeader.length;
		String strCellValue = "";
		for (int i = userHeaderStartPosition; i < totalCells; i++) {
			String cellHeader = rowHeader[i];
			String cell = row[i];
			strCellValue = getCellValue(cell);
			// Required field checking
			if (strCellValue.equals("")) {
				if (cellHeader.equals(Constants.REQUIREDFIELD_FIRST_NAME)) {
					requiredList.add(Constants.REQUIREDFIELD_FIRST_NAME);
				} else if (cellHeader.equals(Constants.REQUIREDFIELD_LAST_NAME)) {
					requiredList.add(Constants.REQUIREDFIELD_LAST_NAME);
				}

				else if (cellHeader.equals(Constants.REQUIREDFIELD_ROLE)) {
					requiredList.add(Constants.REQUIREDFIELD_ROLE);
				}

				else if (cellHeader.equals(Constants.REQUIREDFIELD_TIME_ZONE)) {
					requiredList.add(Constants.REQUIREDFIELD_TIME_ZONE);
				} else if (cellHeader.equals(Constants.EXT_SCHOOL_ID)) {
					requiredList.add(Constants.EXT_SCHOOL_ID);
				}
			}
		}

		if (requiredList.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Checks for any Invalid characters present in the File
	 * 
	 * @param userHeaderStartPosition
	 * @param row
	 * @param rowHeader
	 * @param invalidList
	 * @return boolean
	 */
	private boolean isInvalidChar(int userHeaderStartPosition, String[] row,
			String[] rowHeader, List<String> invalidList) {
		int totalCells = rowHeader.length;
		String strCell = "";
		for (int i = userHeaderStartPosition; i < totalCells; i++) {
			String cellHeader = rowHeader[i];
			String cell = row[i];
			strCell = getCellValue(cell);
			if (!strCell.equals("")) {
				if (cellHeader.equals(Constants.REQUIREDFIELD_FIRST_NAME)
						&& !validNameString(strCell)) {
					invalidList.add(Constants.REQUIREDFIELD_FIRST_NAME);
				} else if (cellHeader.equals(Constants.MIDDLE_NAME)
						&& !strCell.trim().equals("")
						&& !validNameString(strCell)) {
					invalidList.add(Constants.MIDDLE_NAME);
				} else if (cellHeader.equals(Constants.REQUIREDFIELD_LAST_NAME)
						&& !validNameString(strCell)) {
					invalidList.add(Constants.REQUIREDFIELD_LAST_NAME);
				} else if (cellHeader.equals(Constants.EMAIL)
						&& !strCell.trim().equals("") && !validEmail(strCell)) {
					invalidList.add(Constants.EMAIL);
				} else if (cellHeader.equals(Constants.ADDRESS_LINE_1)
						&& !strCell.trim().equals("")
						&& !validAddressString(strCell)) {
					invalidList.add(Constants.ADDRESS_LINE_1);
				} else if (cellHeader.equals(Constants.ADDRESS_LINE_2)
						&& !strCell.trim().equals("")
						&& !validAddressString(strCell)) {
					invalidList.add(Constants.ADDRESS_LINE_2);
				} else if (cellHeader.equals(Constants.CITY)
						&& !strCell.trim().equals("")
						&& !validNameString(strCell)) {
					invalidList.add(Constants.CITY);
				} else if (cellHeader.equals(Constants.ZIP)
						&& !strCell.trim().equals("")
						&& !isValidZipFormat(strCell)) {
					invalidList.add(Constants.ZIP);
				} else if (cellHeader.equals(Constants.PRIMARY_PHONE)
						&& !strCell.trim().equals("") && !isValidPhone(strCell)) {
					invalidList.add(Constants.PRIMARY_PHONE);
				} else if (cellHeader.equals(Constants.SECONDARY_PHONE)
						&& !strCell.trim().equals("") && !isValidPhone(strCell)) {
					invalidList.add(Constants.SECONDARY_PHONE);
				} else if (cellHeader.equals(Constants.FAX)
						&& !strCell.trim().equals("") && !isValidFax(strCell)) {
					invalidList.add(Constants.FAX);
				}
			}
		}
		if (invalidList.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Retrieve string value after
	 * 
	 * @param cell
	 * @return String
	 */
	private String getCellValue(String cell) {
		return (null == cell) ? "" : cell.trim();
	}

	/**
	 * Maxlength check
	 * 
	 * @param userHeaderStartPosition
	 * @param row
	 * @param rowHeader
	 * @param maxLengthList
	 * @return boolean
	 */
	private boolean isMaxlengthExceed(int userHeaderStartPosition,
			String[] row, String[] rowHeader, List<String> maxLengthList) {

		int totalCells = rowHeader.length;
		String strCell = "";
		for (int i = userHeaderStartPosition; i < totalCells; i++) {
			String cellHeader = rowHeader[i];
			String cell = row[i];
			strCell = getCellValue(cell);
			if (!strCell.equals("")) {
				if (cellHeader.equals(Constants.REQUIREDFIELD_FIRST_NAME)
						&& !isMaxLength32(strCell)) {
					maxLengthList.add(Constants.REQUIREDFIELD_FIRST_NAME);
				} else if (cellHeader.equals(Constants.MIDDLE_NAME)
						&& !isMaxLength32(strCell)) {
					maxLengthList.add(Constants.MIDDLE_NAME);
				} else if (cellHeader.equals(Constants.REQUIREDFIELD_LAST_NAME)
						&& !isMaxLength32(strCell)) {
					maxLengthList.add(Constants.REQUIREDFIELD_LAST_NAME);
				} else if (cellHeader.equals(Constants.EMAIL)
						&& !strCell.trim().equals("")
						&& !isMaxLength64(strCell)) {
					maxLengthList.add(Constants.EMAIL);
				} else if (cellHeader.equals(Constants.EXT_SCHOOL_ID)
						&& !strCell.trim().equals("")
						&& !isMaxLength32(strCell)) {
					maxLengthList.add(Constants.EXT_SCHOOL_ID);
				} else if (cellHeader.equals(Constants.REQUIREDFIELD_TIME_ZONE)
						&& !isMaxLength255(strCell)) {
					maxLengthList.add(Constants.REQUIREDFIELD_TIME_ZONE);
				} else if (cellHeader.equals(Constants.ADDRESS_LINE_1)
						&& !strCell.trim().equals("")
						&& !isMaxLength64(strCell)) {
					maxLengthList.add(Constants.ADDRESS_LINE_1);
				} else if (cellHeader.equals(Constants.ADDRESS_LINE_2)
						&& !strCell.trim().equals("")
						&& !isMaxLength64(strCell)) {
					maxLengthList.add(Constants.ADDRESS_LINE_2);
				} else if (cellHeader.equals(Constants.CITY)
						&& !strCell.trim().equals("")
						&& !isMaxLength64(strCell)) {
					maxLengthList.add(Constants.CITY);
				} else if (cellHeader.equals(Constants.ZIP)
						&& !strCell.trim().equals("")
						&& !isMaxLength15(strCell)) {
					maxLengthList.add(Constants.ZIP);
				} else if (cellHeader.equals(Constants.PRIMARY_PHONE)
						&& !strCell.trim().equals("")
						&& !isMaxLength32(strCell)) {
					maxLengthList.add(Constants.PRIMARY_PHONE);
				} else if (cellHeader.equals(Constants.SECONDARY_PHONE)
						&& !strCell.trim().equals("")
						&& !isMaxLength32(strCell)) {
					maxLengthList.add(Constants.SECONDARY_PHONE);
				} else if (cellHeader.equals(Constants.FAX)
						&& !strCell.trim().equals("")
						&& !isMaxLength32(strCell)) {
					maxLengthList.add(Constants.FAX);
				}
			}
		}
		if (maxLengthList.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Check whether maxlength is 15
	 * 
	 * @param value
	 * @return boolean - False if length is more than 15
	 */
	private boolean isMaxLength15(String value) {
		if (value.length() <= 15) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Check whether maxlength is 32
	 * 
	 * @param value
	 * @return boolean - False if length is more than 32
	 */
	private boolean isMaxLength32(String value) {
		if (value.length() <= 32) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Check whether maxlength is 64
	 * 
	 * @param value
	 * @return boolean - False if length is more than 64
	 */
	private boolean isMaxLength64(String value) {
		if (value.length() <= 64) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Check whether maxlength is 255
	 * 
	 * @param value
	 * @return boolean - False if length is more than 255
	 */
	private boolean isMaxLength255(String value) {
		if (value.length() <= 255) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Check whether maxlength is 50
	 * 
	 * @param value
	 * @return boolean - False if length is more than 50
	 */
	private boolean isMaxLength50(String value) {
		if (value.length() <= 50) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Address character validation
	 * 
	 * @param str
	 * @return boolean
	 */
	private static boolean validAddressString(String str) {
		str = str.trim();
		char[] characters = str.toCharArray();
		for (int i = 0; i < characters.length; i++) {
			char character = characters[i];
			if (!validAddressCharacter(character)) {
				return false;
			}
		}
		return true;
	}

	//
	private static boolean validAddressCharacter(char ch) {
		boolean valid = validNameCharacter(ch);
		if (ch == '#') {
			valid = true;
		}
		return valid;
	}

	/**
	 * 
	 * @param str
	 * @return boolean
	 */
	private boolean validNameString(String str) {
		str = str.trim();
		char[] characters = str.toCharArray();
		for (int i = 0; i < characters.length; i++) {

			char character = characters[i];
			if (!validNameCharacter(character)) {

				return false;

			}
		}
		return true;
	}

	private static boolean validNameCharacter(char ch) {
		boolean A_Z = ((ch >= 65) && (ch <= 90));
		boolean a_z = ((ch >= 97) && (ch <= 122));
		boolean zero_nine = ((ch >= 48) && (ch <= 57));
		boolean validChar = ((ch == '/') || (ch == '\'') || (ch == '-')
				|| (ch == '_') || (ch == '\\') || (ch == '.') || (ch == '(')
				|| (ch == ')') || (ch == '&') || (ch == '+') || (ch == ',') || (ch == ' '));
		return (zero_nine || A_Z || a_z || validChar);
	}

	private boolean validString(String str) {
		str = str.trim();
		char[] characters = str.toCharArray();
		for (int i = 0; i < characters.length; i++) {
			char character = characters[i];
			if (!validOrgNameCharacter(character)) {
				return false;
			}
		}
		return true;
	}

	private static boolean validOrgNameCharacter(char ch) {
		boolean A_Z = ((ch >= 65) && (ch <= 90));
		boolean a_z = ((ch >= 97) && (ch <= 122));
		boolean zero_nine = ((ch >= 48) && (ch <= 57));
		// !, @, #, -, _, ', :, /, comma, period, and space will be allowed in
		// these fields.
		boolean validChar = ((ch == '/') || (ch == '\\') || (ch == '-')
				|| (ch == '\'') || (ch == '(') || (ch == ')') || (ch == '&')
				|| (ch == '+') || (ch == ',') || (ch == '.') || (ch == ' '))
				|| (ch == '_');

		return (zero_nine || A_Z || a_z || validChar);
	}

	private static boolean validEmail(String email) {
		email = email.trim();
		boolean matchFound = true;
		if (email != null && email.length() > 0) {
			Pattern p = Pattern
					.compile("[a-z|A-Z|0-9|_|\\-|.]+@[a-z|A-Z|0-9|_|\\-|.]+\\.[a-z|A-Z|0-9|_|\\-|.]+");
			// Match the given string with the pattern
			Matcher m = p.matcher(email);
			// check whether match is found
			matchFound = m.matches();
		}
		return matchFound;
	}

	private static boolean isValidPhone(String str) {

		String[] pieces = tokenize(str, "-");
		if (pieces == null || pieces.length < 3 || pieces.length > 4) {
			return false;
		}

		for (int i = 0; i < pieces.length; i++) {
			String phonePart = pieces[i];
			if (i == 0) {
				if (phonePart.length() != 3 || !isValidNumber(phonePart)) {
					return false;
				}
			}

			else if (i == 1) {
				if (phonePart.length() != 3 || !isValidNumber(phonePart)) {
					return false;
				}
			}

			else if (i == 2) {
				if (phonePart.length() != 4 || !isValidNumber(phonePart)) {
					return false;
				}
			}

			else if (i == 3) {
				if (phonePart.length() > 4 || !isValidNumber(phonePart)) {
					return false;
				}
			}
		}

		return true;
	}

	private static boolean isValidFax(String str) {
		String[] pieces = tokenize(str, "-");
		if (pieces == null || pieces.length != 3) {
			return false;
		}
		for (int i = 0; i < pieces.length; i++) {
			String phonePart = pieces[i];
			if (i == 0) {
				if (phonePart.length() != 3 || !isValidNumber(phonePart)) {
					return false;
				}
			} else if (i == 1) {
				if (phonePart.length() != 3 || !isValidNumber(phonePart)) {
					return false;
				}
			} else if (i == 2) {
				if (phonePart.length() != 4 || !isValidNumber(phonePart)) {
					return false;
				}
			}
		}
		return true;
	}

	private String getPhoneFax(String str) {

		String[] pieces = tokenize(str, " ()-eExt.:,");
		String result = "";
		for (int i = 0; i < pieces.length; i++) {
			String phonePart = pieces[i];
			if (i == 0) {
				result += "(" + phonePart + ") ";
			}

			else if (i == 1) {
				result += phonePart;
			}

			else if (i == 2) {
				result += "-" + phonePart;
			}

			else if (i == 3) {
				result += " x " + phonePart;
			}
		}

		return result;
	}

	final public static String[] tokenize(String parameter, String delimiter) {
		String tokens[];
		int nextItem = 0;
		StringTokenizer stoke = new StringTokenizer(parameter, delimiter);
		tokens = new String[stoke.countTokens()];
		while (stoke.hasMoreTokens()) {
			tokens[nextItem] = stoke.nextToken();
			nextItem = (nextItem + 1) % tokens.length;
		}
		return tokens;
	}

	// valid numeric field
	private static boolean isValidNumber(String number) {

		for (int i = 0; i < number.length(); i++) {
			char ch = number.charAt(i);
			if (!((ch >= 48) && (ch <= 57))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks for Valid Zip Field
	 * 
	 * @param zip
	 * @return
	 */
	private static boolean isValidZipFormat(String zip) {
		String[] splitNumber = zip.split("-");
		boolean isValid = false;
		if (splitNumber[0].trim().length() == 5
				&& isValidNumber(splitNumber[0].trim())) {
			isValid = true;
			if (splitNumber.length > 1) {
				if (splitNumber[1].trim().length() <= 5
						&& isValidNumber(splitNumber[1].trim())) {
					isValid = true;
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
		if (isValid) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @return Returns the serverFilePath.
	 */
	public String getServerFilePath() {
		return serverFilePath;
	}

	/**
	 * @param serverFilePath
	 *            The serverFilePath to set.
	 */
	public void setServerFilePath(String serverFilePath) {
		this.serverFilePath = serverFilePath;
	}

	/**
	 * @return Returns the userFileRowHeader.
	 */
	public UserFileRow[] getUserFileRowHeader() {
		return userFileRowHeader;
	}

	/**
	 * @param userFileRowHeader
	 *            The userFileRowHeader to set.
	 */
	public void setUserFileRowHeader(UserFileRow[] userFileRowHeader) {
		this.userFileRowHeader = userFileRowHeader;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setUploadDt(Date uploadDt) {
		this.uploadDt = uploadDt;
	}

	public void setNoOfHeaderRows(int noOfUserColumn) {
		this.noOfUserColumn = noOfUserColumn;
	}

	public int getFailedRecordCount() {
		return failedRecordCount;
	}

	public void setFailedRecordCount(int failedRecordCount) {
		this.failedRecordCount = failedRecordCount;
	}

	public int getUploadRecordCount() {
		return uploadRecordCount;
	}

	public void setUploadRecordCount(int uploadRecordCount) {
		this.uploadRecordCount = uploadRecordCount;
	}

}
