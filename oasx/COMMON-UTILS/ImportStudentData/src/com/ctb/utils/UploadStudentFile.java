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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import com.ctb.bean.CustomerConfig;
import com.ctb.bean.CustomerConfiguration;
import com.ctb.bean.CustomerConfigurationValue;
import com.ctb.bean.CustomerDemographicValue;
import com.ctb.bean.DataFileAudit;
import com.ctb.bean.ManageStudent;
import com.ctb.bean.Node;
import com.ctb.bean.OrgNodeCategory;
import com.ctb.bean.OrganizationNode;
import com.ctb.bean.StudentAccommodations;
import com.ctb.bean.StudentDemoGraphics;
import com.ctb.bean.StudentDemographicValue;
import com.ctb.bean.StudentFileRow;
import com.ctb.bean.UploadStudent;
import com.ctb.control.OrganizationManagementControl;
import com.ctb.control.StudentManagementControl;
import com.ctb.dao.StudentFileDao;
import com.ctb.dao.StudentFileDaoImpl;
import com.ctb.dao.UploadFileDao;
import com.ctb.dao.UploadFileDaoImpl;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.FileNotUploadedException;
import com.ctb.utils.cache.StudentDBCacheImpl;
import com.ctb.utils.cache.StudentNewRecordCacheImpl;
import com.ctb.utils.cache.StudentUpdateRecordCacheImpl;


public class UploadStudentFile {
	private Integer customerId;
	private File inFile;
	private Integer uploadFileId;
	private InputStream uploadedStream;
	private Date uploadDt;
	private int noOfUserColumn;
	StudentDBCacheImpl dbCacheImpl = new StudentDBCacheImpl();
	//private HashMap<String, StudentFileRow> visibleStudent = new HashMap<String, StudentFileRow>();
	private CustomerConfigurationValue customerConfigurationValue;
	private CustomerConfigurationValue[] customerConfigurationValues;
	private int failedRecordCount;
	private int uploadRecordCount = 0;
	public DataFileAudit dataFileAudit = new DataFileAudit();;
	public OrgNodeCategory orgNodeCategory[] = null;
	private StudentFileRow[] studentFileRowHeader;
	public String[] grades = null;
	public int traversCells = 0;
	private String hideAccommodations;
	private CustomerConfiguration[] customerConfigurations = null;
	private int noOfDemographicList = 0;
	public HashMap<String, Map<String,String>> demoMap = new HashMap<String, Map<String,String>>();
	public HashMap<String, Integer> demoGraphicMap = new HashMap<String, Integer>();
	private ArrayList<String> colorList = new ArrayList<String>();
	private HashMap<String, ArrayList<String>> colorCombinationMap = new HashMap<String, ArrayList<String>>();
	private HashMap<String, String> colorCodeMap = new HashMap<String, String>();
	private HashMap<String, String> defaultAccommodationMap = new HashMap<String, String>();
	private HashMap<String, String> editableAccomodationMap = new HashMap<String, String>();
	private HashMap<String, String> demoCardinalityMap = new HashMap<String, String>();
	boolean checkExternalStudent_id;
	boolean checkOrgNodeCode;
	boolean isMatchUploadOrgIds = false;
	private Node[] userTopOrgNode = null;
	private Node[] detailNodeM = null;
	private boolean disableMandatoryBirthdate = false;
	boolean checkCustomerConfiguration;
	private boolean isStudentIdConfigurable = false;
	private boolean isStudentId2Configurable = false;
	private String studentIdLabel = Constants.STUDENT_ID;
	private String studentId2Label = Constants.STUDENT_ID2;
	private boolean isStudentIdMandatory = false;
	private String maxlengthStudentID = null;
	private String maxlengthStudentId2 = null;
	private String studentIdMinLength = "0";
	private String studentId2MinLength = "0";
	private String isStudentIdNumeric = "AN";
	private String isStudentId2Numeric = "AN";
	private boolean isStudentIdUnique = false;
	private ArrayList<String> studentIdList = new ArrayList<String>();
	private int orgPosFact = 2;
	private String ethnicityLabel = Constants.ETHNICITY_LABEL;
	private String subEthnicityLabel = Constants.SUB_ETHNICITY_LABEL;
	UploadFormUtils uploadFormUtils = null;
	private UploadFileDao dao = null;
	private StudentFileDao studentDao = null;
	private StudentUploadUtils studentUploadUtils = new StudentUploadUtils();
	private OrganizationManagementControl organizationManagement = new OrganizationManagementControl();
	private StudentManagementControl studentManagement = new StudentManagementControl();
	private static Logger logger = Logger.getLogger(UploadStudentFile.class.getName());
	
	//new cache impl 
	StudentNewRecordCacheImpl newStdRecordCacheImpl = new StudentNewRecordCacheImpl();
	StudentUpdateRecordCacheImpl updateStdRecordCacheImpl = new StudentUpdateRecordCacheImpl();
	
	//private static List<UploadStudent> finalStudentList = new ArrayList<UploadStudent>(); // all insert students
	private static Map<String,Integer> studentIdExtPinMap = new HashMap<String, Integer>(); 
	//private static List<UploadStudent> finalUpdateStudentList = new ArrayList<UploadStudent>(); //all updatable students
	private static Set<String> studentUserNames = new HashSet<String>(); // all student user names
	
	
	public UploadStudentFile(Integer customerId, File inFile,
			Integer uploadFileId, OrgNodeCategory[] orgNodeCategory,
			StudentFileRow[] studentFileRowHeader, int noOfUserColumn,
			String[] valueForStudentId, String[] valueForStudentId2,
			boolean isStudentIdUnique) {
		this.customerId = customerId;
		this.inFile = inFile;
		this.uploadFileId = uploadFileId;
		setUploadDt(new Date());
		setNoOfHeaderRows(noOfUserColumn);
		this.studentFileRowHeader = studentFileRowHeader;
		this.orgNodeCategory = orgNodeCategory;
		this.isStudentIdUnique = isStudentIdUnique;
		if (valueForStudentId != null) {
			this.isStudentIdConfigurable = true;
			this.studentIdLabel = valueForStudentId[0] != null ? valueForStudentId[0]
					: Constants.STUDENT_ID;
			this.maxlengthStudentID = valueForStudentId[1];
			this.isStudentIdMandatory = valueForStudentId[2] != null
					&& valueForStudentId[2].equals("T") ? true : false;
			this.studentIdMinLength = valueForStudentId[3];
			this.isStudentIdNumeric = valueForStudentId[4];
		}
		if (valueForStudentId2 != null) {
			this.isStudentId2Configurable = true;
			this.studentId2Label = valueForStudentId2[0] != null ? valueForStudentId2[0]
					: Constants.STUDENT_ID2;
			this.maxlengthStudentId2 = valueForStudentId2[1];
			this.studentId2MinLength = valueForStudentId2[2];
			this.isStudentId2Numeric = valueForStudentId2[3];
		}
		logger.info("Init Process Start Time:" + new Date(System.currentTimeMillis()));
		init();
		logger.info("Init Process End Time:" + new Date(System.currentTimeMillis()));
	}

	public void startProcessing() throws Exception {
		logger.info("Data Validation Start Time:" + new Date(System.currentTimeMillis()));
		studentDao = new StudentFileDaoImpl();
		HashMap<Integer, ArrayList<String>> requiredMap = new HashMap<Integer, ArrayList<String>>();
		HashMap<Integer,ArrayList<String>> maxLengthMap = new HashMap<Integer,ArrayList<String>>();
		HashMap<Integer,ArrayList<String>> minLengthMap = new HashMap<Integer,ArrayList<String>>();
		HashMap<Integer,ArrayList<String>> invalidCharMap = new HashMap<Integer,ArrayList<String>>();
		HashMap<Integer,ArrayList<String>> logicalErrorMap = new HashMap<Integer,ArrayList<String>>();
		HashMap<Integer,ArrayList<String>> hierarchyErrorMap = new HashMap<Integer,ArrayList<String>>();
		HashMap<Integer,String> leafNodeErrorMap = new HashMap<Integer,String>();
		HashMap<Integer,String> blankRowMap = new HashMap<Integer,String>();
		boolean isBlankRow = true;
		String strCellName = "";
		String strCellId = "";
		String strCellHeaderName = "";
		String strCellHeaderId = "";
		String strCellMdr = "";
		String strCellHeaderMdr = "";
		HashMap<String,String> studentDataMap = new HashMap<String,String>();
		int loginUserPosition = 0;

		try {
			studentDao.getExistStudentData(this.customerId,dbCacheImpl);
//			for (int i = 0; i < studentFileRow.length; i++) {
//				this.visibleStudent.put(studentFileRow[i].getKey(),
//						studentFileRow[i]);
//			}
			
			CSVReader csv = new CSVReader(new BufferedReader(new FileReader(this.inFile)), ',');
			int rowIndex = 0;
			boolean isFirstRow = true;
			String[]  rowHeader = new String[0] ;
			String[]  row ;
			while ((row = csv.readNext()) != null) {
				//logger.info("rowIndex " + rowIndex );
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

				/**
				 * This function is used to check all the Data error present in
				 * file.
				 * */
				getEachRowStudentDetail(rowIndex, row, rowHeader, requiredMap,
						maxLengthMap, invalidCharMap, logicalErrorMap,
						minLengthMap);


				if (!(requiredMap.containsKey(new Integer(rowIndex))
						|| invalidCharMap.containsKey(new Integer(rowIndex))
						|| minLengthMap.containsKey(new Integer(rowIndex))
						|| maxLengthMap.containsKey(new Integer(rowIndex)) || logicalErrorMap
						.containsKey(new Integer(rowIndex)))) {

					loginUserPosition = getLoginUserOrgPosition(row, rowHeader,
							this.userTopOrgNode);

					List<String> newMDRList = new ArrayList<String>();
					Node[] node = this.studentFileRowHeader[0]
							.getOrganizationNodes();
					Integer[] parentOrgId = new Integer[1];
					String loginUserOrgCell = row[loginUserPosition];
					String loginUserOrgName = getCellValue(loginUserOrgCell);
					Node loginUserNode = getLoginUserOrgDetail(
							this.userTopOrgNode, loginUserOrgName);
					Integer parentOId = loginUserNode.getOrgNodeId();
					parentOrgId[0] = parentOId;
					int OrgHeaderLastPosition = node.length * orgPosFact;

					for (int j = loginUserPosition + orgPosFact; j < OrgHeaderLastPosition; j = j
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
						Node[] nodeCategory = this.studentFileRowHeader[0]
								.getOrganizationNodes();
						categoryId = getCategoryId(headerName, nodeCategory);

						// OrgName required check
						if (strCellName.equals("") && hasOrganization(j, row)
								&& !strCellId.equals("")) {
							/*
							 * write excel required with the help of
							 * cellHeaderName
							 */
							ArrayList<String> requiredList = new ArrayList<String>();
							requiredList.add(strCellHeaderName);
							requiredMap
									.put(new Integer(rowIndex), requiredList);
							break;
						} else if (strCellName.equals("")
								&& hasOrganization(j - orgPosFact, row)
								&& !strCellId.equals("")) {
							// write excel required with the help of
							// cellHeaderName
							ArrayList<String> requiredList = new ArrayList<String>();
							requiredList.add(strCellHeaderName);
							requiredMap
									.put(new Integer(rowIndex), requiredList);
							break;
						} else if (!isValidMDR(rowIndex, isMatchUploadOrgIds,
								strCellId, parentOrgId, categoryId,
								requiredMap, invalidCharMap, logicalErrorMap,
								newMDRList, strCellMdr, strCellName,
								strCellHeaderMdr)) {
							break;

						} else {
							// OrgName invalid char check
							if (validString(strCellName)) {
								// OrgCode invalid char check
								if (!validString(strCellId)) {
									/*
									 * write excel invalid with the help of
									 * cellHeaderID
									 */
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
								// write excel invalid with the help of
								// cellHeaderName
								ArrayList<String> invalidList = new ArrayList<String>();
								invalidList.add(strCellHeaderName);
								invalidCharMap.put(new Integer(rowIndex),
										invalidList);
								break;
							}
						}
					}// end for
					checkLeafNodeError(rowIndex, row, rowHeader,
							leafNodeErrorMap);
				}// end if
				isBlankRow = true;
				rowIndex ++;
				
			}// while loop end of total row processing
			csv.close();
			if (requiredMap.size() > 0 || minLengthMap.size() > 0
					|| maxLengthMap.size() > 0 || invalidCharMap.size() > 0
					|| logicalErrorMap.size() > 0
					|| hierarchyErrorMap.size() > 0
					|| leafNodeErrorMap.size() > 0) {
				logger.info("Error Excel Start Time:" + new Date(System.currentTimeMillis()));
				
				errorExcelCreation(requiredMap, maxLengthMap, invalidCharMap,
						logicalErrorMap, hierarchyErrorMap, leafNodeErrorMap,
						minLengthMap);
				
				logger.info("Error Excel End Time:" + new Date(System.currentTimeMillis()));
			}
			logger.info(" Data validation Complete.. Data Insertion Process in Progress...");
			
			logger.info("Data Validation End Time:" + new Date(System.currentTimeMillis()));
			
			createOrganizationAndStudent(requiredMap, minLengthMap,
					maxLengthMap, invalidCharMap, logicalErrorMap,
					hierarchyErrorMap, studentDataMap, leafNodeErrorMap,
					blankRowMap, isMatchUploadOrgIds, this.userTopOrgNode,
					isBlankRow);
			
			/**
			 * Archiving Process 
			 * */	
			logger.info("ArchiveProcessedFiles Start Time:" + new Date(System.currentTimeMillis()));
			FtpSftpUtil.archiveProcessedFiles(FtpSftpUtil.getSFTPSession(), Configuration.getFtpFilePath(), Configuration.getArchivePath(), inFile.getName());
			logger.info("ArchiveProcessedFiles End Time:" + new Date(System.currentTimeMillis()));
			logger.info("Student Upload Process Completed Time " + new Date(System.currentTimeMillis()));

		} catch (Exception ex) {
			logger.error("UploadStudentFile Error.. startProcessing() Block...");
			ex.printStackTrace();
			throw ex;
		}
	}

	private void createOrganizationAndStudent(HashMap<Integer, ArrayList<String>> requiredMap,
			HashMap<Integer, ArrayList<String>> minLengthMap, HashMap<Integer, ArrayList<String>> maxLengthMap, HashMap<Integer, ArrayList<String>> invalidCharMap,
			HashMap<Integer, ArrayList<String>> logicalErrorMap, HashMap<Integer, ArrayList<String>> hierarchyErrorMap,
			HashMap<String,String> studentDataMap, HashMap<Integer, String> leafNodeErrorMap,
			HashMap<Integer,String> blankRowMap, boolean isMatchUploadOrgIds,
			Node[] loginUserNodes, boolean isMatchMdrNo) throws Exception {
		
		logger.info("CreateOrganizationAndStudent Map Population Start Time:" + new Date(System.currentTimeMillis()));
		int loginUserOrgPosition = 0;
		Node organization = null;
		Integer orgNodeId = null;
		boolean isBlankRow = true;
		try {	
			String[] rowHeader = new String[0];
			String[] row ;
			int rowIndex = 0;
			boolean isRowHeader = true;
			Node []nodeCategory = this.studentFileRowHeader[0].getOrganizationNodes();
			int orgHeaderLastPosition = nodeCategory.length * orgPosFact;
			CSVReader csv = new CSVReader(new BufferedReader(new FileReader(this.inFile)), ',');
			while ((row = csv.readNext()) != null){
				if (isRowHeader) {
					rowHeader = new String[row.length];
					rowHeader = row;
					isRowHeader = false;
					rowIndex++;
					continue;
				}
				int totalCells = rowHeader.length;
				// retrieve each cell value for user
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
						|| minLengthMap.containsKey(new Integer(rowIndex))
						|| maxLengthMap.containsKey(new Integer(rowIndex))
						|| logicalErrorMap.containsKey(new Integer(rowIndex))
						|| hierarchyErrorMap.containsKey(new Integer(rowIndex))
						|| leafNodeErrorMap.containsKey(new Integer(rowIndex)) || blankRowMap
						.containsKey(new Integer(rowIndex)))) {
					//logger.info("Insertion will happen for" + rowIndex);

					// OrganizationCreation or Existence check process
					loginUserOrgPosition = getLoginUserOrgPosition(row,
							rowHeader, loginUserNodes);

					String loginUserOrgCell = row[loginUserOrgPosition];

					String loginUserOrgName = getCellValue(loginUserOrgCell);
					Node loginUserNode = getLoginUserOrgDetail(loginUserNodes,
							loginUserOrgName);

					// orgNodeId and parentId initialization process
					Integer parentOrgId = loginUserNode.getOrgNodeId();
					orgNodeId = loginUserNode.getOrgNodeId();
					int lastOrganization = 0;
					// For MDR columns needs to be removed for nonLaslinks
					for (int ii = loginUserOrgPosition + orgPosFact; ii < orgHeaderLastPosition; ii = ii
							+ orgPosFact) {
						String OrgCellName = row[ii];
						String OrgCellId =  row[ii + 1];
						String orgCode = getCellValue(OrgCellId);
						String orgName = getCellValue(OrgCellName);
						String OrgCellHeaderName = rowHeader[ii];
						String headerName = getCellValue(OrgCellHeaderName);
						Integer categoryId = getCategoryId(headerName,
								nodeCategory);
						String orgMdr = null;
						if (isMatchMdrNo) {
							String OrgCellMdr = row[ii + 2];
							orgMdr = getCellValue(OrgCellMdr);
						}
						if (!hasOrganization(ii, row) && orgName.equals("")
								&& orgCode.equals("")) {
							lastOrganization = ii;
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
											if (isMatchMdrNo) {
												organization
														.setMdrNumber(orgMdr);
											}
											// create Organization
											organization = this.organizationManagement
													.createOrganization(null,
															organization);
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
												if (isMatchMdrNo) {
													organization
															.setMdrNumber(orgMdr);
												}
												// create Organization
												organization = this.organizationManagement
														.createOrganization(
																null,
																organization);
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
											if (isMatchMdrNo) {
												organization
														.setMdrNumber(orgMdr);
											}
											// create Organization
											organization = this.organizationManagement
													.createOrganization(null,
															organization);
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
										if (isMatchMdrNo) {
											organization.setMdrNumber(orgMdr);
										}
										// create Organization
										organization = this.organizationManagement
												.createOrganization(null,
														organization);
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
											if (isMatchMdrNo) {
												organization
														.setMdrNumber(orgMdr);
											}
											// create Organization
											organization = this.organizationManagement
													.createOrganization(null,
															organization);
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

					Node[] orgDetail = new Node[1];
					orgDetail[0] = new Node();
					orgDetail[0].setOrgNodeId(orgNodeId);
					for (int j = orgHeaderLastPosition; j < rowHeader.length; j++) {
						String headerCell = rowHeader[j];
						String bodyCell = row[j];
						String strHeaderValue = getCellValue(headerCell);
						String strBodyValue = getCellValue(bodyCell);
						studentDataMap.put(strHeaderValue, strBodyValue);
					}

					HashMap<String, String> studentDemoMap = new HashMap<String, String>();
					ArrayList<String> demolist = new ArrayList<String>();
					// Get the position of Demographic details
					int start = rowHeader.length
							- noOfDemographicList;
					for (int d = start; d < rowHeader.length; d++) {
						String headerCell = rowHeader[d];
						String bodyCell = row[d];
						String strHeaderValue = getCellValue(headerCell);
						String strBodyValue = getCellValue(bodyCell);
						if (!(strBodyValue.equals("")) && strBodyValue != null) {
							studentDemoMap.put(strHeaderValue, strBodyValue);
							demolist.add(strHeaderValue);
						}
					}
					// populate finalStudent List
					createStudent(studentDataMap, orgHeaderLastPosition,
							orgDetail, studentDemoMap, demolist);
					studentDemoMap = null;
					demolist = null;
					uploadRecordCount++;
				}
				isBlankRow = true;
				rowIndex++;
			}
			
			requiredMap = null;
			minLengthMap = null;
			maxLengthMap = null;
			logicalErrorMap = null;
			invalidCharMap = null;
			leafNodeErrorMap = null;
			hierarchyErrorMap = null;
			System.gc();			
			csv.close();
			logger.info("CreateOrganizationAndStudent Map Population End Time:" + new Date(System.currentTimeMillis()));
			
			if(newStdRecordCacheImpl.getCacheSize() > 0){
				logger.info("Students to be Inserted::--> " +newStdRecordCacheImpl.getCacheSize());
				logger.info("ExecuteStudentCreation Start Time:" + new Date(System.currentTimeMillis()));
				this.studentManagement.executeStudentCreation(newStdRecordCacheImpl, UploadStudentFile.studentUserNames ,UploadStudentFile.studentIdExtPinMap);
				logger.info("ExecuteStudentCreation End Time:" + new Date(System.currentTimeMillis()));
			}
			
			newStdRecordCacheImpl.clearCacheContents();
			newStdRecordCacheImpl = null;
			if(updateStdRecordCacheImpl.getCacheSize() > 0){
				logger.info("Students to be Updated::--> " + updateStdRecordCacheImpl.getCacheSize());
				logger.info("ExecuteStudentUpdate Start Time:" + new Date(System.currentTimeMillis()));
				this.studentManagement.executeStudentUpdate(updateStdRecordCacheImpl,this.customerId ,UploadStudentFile.studentIdExtPinMap);
				logger.info("ExecuteStudentUpdate End Time:" + new Date(System.currentTimeMillis()));
			}
			
			
			if ( this.dataFileAudit.getFailedRecordCount() == null ||
					this.dataFileAudit.getFailedRecordCount().intValue() == 0 )  {
				this.dataFileAudit.setStatus("SC");
				this.dataFileAudit.setFaildRec(null);
			} else {
				dataFileAudit.setStatus("FL");
			}

			this.dataFileAudit.setUploadFileRecordCount(new Integer(uploadRecordCount));
			dao.upDateAuditTable(this.dataFileAudit);
	
		} catch (SQLException se) {
			se.printStackTrace();
			 dataFileAudit.setFaildRec(null);
			 dataFileAudit.setStatus("FL");
			 dataFileAudit.setFailedRecordCount(new Integer(0));
			 dataFileAudit.setUploadFileRecordCount(new Integer(0));
			 try{
				 dao.upDateAuditTable(this.dataFileAudit);
			 } catch (SQLException ex) {
				 ex.printStackTrace();
			 }
			 FileNotUploadedException dataNotFoundException = new FileNotUploadedException("UploadDownloadManagement.Failed");
			 dataNotFoundException.setStackTrace(se.getStackTrace());
			 throw dataNotFoundException;

		} catch (Exception e) {
			e.printStackTrace();

			 dataFileAudit.setFaildRec(null);
			 dataFileAudit.setStatus("FL");
			 dataFileAudit.setFailedRecordCount(new Integer(0));
			 dataFileAudit.setUploadFileRecordCount(new Integer(0));
			 try{
			 dao.upDateAuditTable(dataFileAudit);
			 } catch (SQLException ex) {
			 ex.printStackTrace();
			 }
			 FileNotUploadedException dataNotFoundException =
			 new FileNotUploadedException
			 ("UploadDownloadManagement.Failed");
			 dataNotFoundException.setStackTrace(e.getStackTrace());
			 throw dataNotFoundException;

		}
		finally{
			logger.info("Import process is completed, exiting!");
		}

	}
	
	

	
	/*
	 * 
	 */ 
	private boolean hasOrganization (int currentPosition, String[] row) {

		Node []node = this.studentFileRowHeader[0].getOrganizationNodes();
		int OrgHeaderLastPosition = node.length * orgPosFact;		 // 
		for (int j = currentPosition + orgPosFact ; j < OrgHeaderLastPosition; j = j + orgPosFact ) { 
			String cellName = row[j];
			String cellId = row[j + 1];
			if ( !getCellValue(cellName).equals("") 
					|| (!getCellValue(cellId).equals(""))) {
				return true;
			}
		} //end for 
		return false;    
	}
	

	
	/*
	 * First check the existence of student, if not exists then create
	 */ 

	private void createStudent(HashMap<String,String> studentDataMap,
			int orgHeaderLastPosition,Node[] studentNode,
			HashMap<String,String> studentDemoMap,
			ArrayList<String> demolist)  throws Exception {


		boolean isNewStudent = true;
		// Set into student Demographic
		StudentDemoGraphics[] studentDemographic = this.getStudentDemographicData(studentDemoMap,demolist);
		//Accommodation 
		StudentAccommodations studentAccommodations = new StudentAccommodations();                                                                                   
		//studentDataMap.get(
		ManageStudent manageStudent = new ManageStudent();
		// Set the student personal data
		setStudentPersonalData (manageStudent, studentDataMap) ;
		// Assign the node to student here
		// set Organization Node
		OrganizationNode organizationNode = new OrganizationNode();
		organizationNode.setOrgNodeId(studentNode[0].getOrgNodeId());
		OrganizationNode[] studentOrgNode = new OrganizationNode[1];
		studentOrgNode[0] = organizationNode;

		try {
			
			StudentFileRow studentFile = isStudentExists(manageStudent);
			
			if ( studentFile != null ) {
				isNewStudent = false;
				manageStudent.setLoginId(studentFile.getUserName());
				manageStudent.setId(studentFile.getStudentId());
				manageStudent.setCustomerId(studentFile.getCustomerId());
			}

			if (isNewStudent) { // Create new Student
				manageStudent.setOrganizationNodes(studentOrgNode);
				setStudentAccommodationData (studentAccommodations , studentDataMap);
				createNewStudent(manageStudent,studentAccommodations,studentDemographic,studentNode);
			} 
			else { // Update student Record
				manageStudent.setOrganizationNodes(studentOrgNode);
				setStudentAccommodationData ( studentAccommodations , studentDataMap);
				updateStudent(manageStudent,studentAccommodations,studentDemographic);
			}
		} catch ( Exception e) {
			e.printStackTrace();
			throw e;
		} 
	}
	
	/**
	 *  Update student,update student accommodation and demographic details
	 */
	private void  updateStudent (ManageStudent manageStudent,StudentAccommodations studentAccommodations,StudentDemoGraphics[] studentDemographic) throws Exception {

		studentAccommodations.setStudentId(manageStudent.getId());
		updateStdRecordCacheImpl.addUpdatedStudent(manageStudent.getStudentIdNumber().trim(), new UploadStudent(manageStudent, studentAccommodations, studentDemographic));
	}
	
	/**
	 *  set the student data from the excel list to Student
	 */


	private void setStudentPersonalData ( ManageStudent student , HashMap<String,String> studentDataMap ) {


		student.setStudentIdNumber(
				((String)studentDataMap.get(this.studentIdLabel)).trim());
		student.setFirstName(initStringCap((String)studentDataMap.get
				(Constants.REQUIREDFIELD_FIRST_NAME)));
		student.setMiddleName(initStringCap((String)studentDataMap.get
				(Constants.MIDDLE_NAME)));
		student.setLastName(initStringCap((String)studentDataMap.get
				(Constants.REQUIREDFIELD_LAST_NAME)));
		student.setStudentIdNumber2(
				((String)studentDataMap.get(this.studentId2Label)).trim() );
		String date = (String)studentDataMap.get(Constants.REQUIREDFIELD_DATE_OF_BIRTH);

		Date dateOfBirth = null;

		if (!(date == null ||  date.equals("")))
		{
			int month = Integer.parseInt(date.substring(0,2)) - 1;
			int day = Integer.parseInt(date.substring(3,5));
			int year = Integer.parseInt(date.substring(6,10)) - 1900;
			dateOfBirth = new Date(year, month, day );

		}
		student.setBirthDate(dateOfBirth);
		student.setGender(getGender((String)studentDataMap.get(Constants.REQUIREDFIELD_GENDER)));
		student.setGrade(getGradeValue((String)studentDataMap.get(Constants.REQUIREDFIELD_GRADE)));
	}
	
	/**
	 *  set the student Accommodation data from the excel list to StudentAccommodation
	 */


	private void setStudentAccommodationData ( StudentAccommodations studentAccommodations ,
			HashMap<String,String> studentDataMap) {


		//For the accommodation of  screen reader
		if ( (studentDataMap.get(Constants.SCREEN_READER).equals("")
				|| studentDataMap.get(Constants.SCREEN_READER) == null )) {

			studentAccommodations.setScreenReader(getDefaultAccommodation
					(Constants.ACOMOD_SCREEN_READER));
		} else  {

			studentAccommodations.setScreenReader(getAccommodationValue(
					Constants.ACOMOD_SCREEN_READER,
					(String)studentDataMap.get(Constants.SCREEN_READER)));  
		}


		//For the accommodation of  Calculator
		if ( ( studentDataMap.get(Constants.CALCULATOR).equals("")
				|| studentDataMap.get(Constants.CALCULATOR) == null )) {

			studentAccommodations.setCalculator(getDefaultAccommodation
					(Constants.ACOMOD_CALCULATOR));
		} else {

			studentAccommodations.setCalculator(getAccommodationValue(
					Constants.ACOMOD_CALCULATOR,
					(String)studentDataMap.get(Constants.CALCULATOR)));  
		} 


		//For the accommodation of  Test Pause
		if ( ( studentDataMap.get(Constants.TEST_PAUSE).equals("")
				|| studentDataMap.get(Constants.TEST_PAUSE) == null)) {

			studentAccommodations.setTestPause(getDefaultAccommodation
					(Constants.ACOMOD_TEST_PAUSE));

		} else {

			studentAccommodations.setTestPause(getAccommodationValue(
					Constants.ACOMOD_TEST_PAUSE,
					(String)studentDataMap.get(Constants.TEST_PAUSE)));
		}


		//For the accommodation of Untimed Test
		if ( (studentDataMap.get(Constants.UNTIMED_TEST).equals("")
				|| studentDataMap.get(Constants.UNTIMED_TEST) == null)) {

			studentAccommodations.setUntimedTest(getDefaultAccommodation
					(Constants.ACOMOD_UNTIMED_TEST));

		} else {

			studentAccommodations.setUntimedTest(getAccommodationValue(
					Constants.ACOMOD_UNTIMED_TEST,
					(String)studentDataMap.get(Constants.UNTIMED_TEST)));  

		}


		//For the accommodation of Highlighter

		if ( (studentDataMap.get(Constants.HIGHLIGHTER).equals("")
				|| studentDataMap.get(Constants.HIGHLIGHTER) == null)) {

			studentAccommodations.setHighlighter(getDefaultAccommodation
					(Constants.ACOMOD_HIGHLIGHTER));
		} else {

			studentAccommodations.setHighlighter(getAccommodationValue(
					Constants.ACOMOD_HIGHLIGHTER,
					(String)studentDataMap.get(Constants.HIGHLIGHTER)));  

		}

		boolean hasColorFont = false;

		if((studentDataMap.get(Constants.QUESTION_BACKGROUND_COLOR) != null
				&& !studentDataMap.get(Constants.QUESTION_BACKGROUND_COLOR).equals(""))
				|| (studentDataMap.get(Constants.QUESTION_FONT_COLOR) != null 
						&& !studentDataMap.get(Constants.QUESTION_FONT_COLOR).equals(""))
						|| (studentDataMap.get(Constants.ANSWER_BACKGROUND_COLOR) != null
								&& !studentDataMap.get(Constants.ANSWER_BACKGROUND_COLOR).equals(""))
								|| (studentDataMap.get(Constants.ANSWER_FONT_COLOR) != null
										&& !studentDataMap.get(Constants.ANSWER_FONT_COLOR).equals(""))
										|| (studentDataMap.get(Constants.FONT_SIZE) != null
												&& !studentDataMap.get(Constants.FONT_SIZE).equals(""))){

			hasColorFont = true;                 
		}

		if(hasColorFont){

			boolean hasBGColor = false;
			boolean hasFGColor = false;

			if ( !(studentDataMap.get(Constants.QUESTION_BACKGROUND_COLOR).equals("") 
					|| studentDataMap.get(Constants.QUESTION_BACKGROUND_COLOR)== null )) {

				hasBGColor = true;

			} 

			if ( !(studentDataMap.get(Constants.QUESTION_FONT_COLOR).equals("") 
					|| studentDataMap.get(Constants.QUESTION_FONT_COLOR)== null )) {

				hasFGColor = true;

			}

			if ( !hasBGColor && !hasFGColor ) {

				studentAccommodations.setQuestionBackgroundColor
				(Constants.WHITE_CODE);

				studentAccommodations.setQuestionFontColor
				(Constants.BLACK_CODE);


			} else if ( hasBGColor && !hasFGColor ) {

				String fontColorCode = getFontColor((String)studentDataMap.
						get(Constants.QUESTION_BACKGROUND_COLOR));
				String bgColorCode = getColorCode((String)studentDataMap.
						get(Constants.QUESTION_BACKGROUND_COLOR));

				studentAccommodations.setQuestionBackgroundColor(bgColorCode);    
				studentAccommodations.setQuestionFontColor(fontColorCode);                        


			} else if ( !hasBGColor && hasFGColor ) {

				String bgColorCode = getBGColor((String)studentDataMap.
						get(Constants.QUESTION_FONT_COLOR));

				String fontColorCode =  getColorCode((String)studentDataMap.
						get(Constants.QUESTION_FONT_COLOR)); 

				studentAccommodations.setQuestionBackgroundColor(bgColorCode);    
				studentAccommodations.setQuestionFontColor(fontColorCode);                          


			} else if ( hasBGColor && hasFGColor ) {

				studentAccommodations.setQuestionBackgroundColor(
						getColorCode((String)studentDataMap.get(
								Constants.QUESTION_BACKGROUND_COLOR)));

				studentAccommodations.setQuestionFontColor(
						getColorCode((String)studentDataMap.get(
								Constants.QUESTION_FONT_COLOR)));

			}

			if ( studentDataMap.get(Constants.FONT_SIZE).equals("") 
					|| studentDataMap.get(Constants.FONT_SIZE)== null ) {

				studentAccommodations.setQuestionFontSize(Constants.STANDARD_FONT_SIZE);

			} else {

				studentAccommodations.setQuestionFontSize(
						getFontSize(
								(String)studentDataMap.get(Constants.FONT_SIZE)));
			}

			hasBGColor = false;
			hasFGColor = false;    

			if ( !(studentDataMap.get(Constants.ANSWER_BACKGROUND_COLOR).equals("") 
					|| studentDataMap.get(Constants.ANSWER_BACKGROUND_COLOR)== null)) {

				hasBGColor = true;

			}                                         

			if (!( studentDataMap.get(Constants.ANSWER_FONT_COLOR).equals("") 
					|| studentDataMap.get(Constants.ANSWER_FONT_COLOR)== null)) {

				hasFGColor = true;

			}                                       

			if ( !hasBGColor && !hasFGColor ) {

				studentAccommodations.setAnswerBackgroundColor
				(Constants.LIGHT_YELLOW_CODE);

				studentAccommodations.setAnswerFontColor
				(Constants.BLACK_CODE);


			} else if ( hasBGColor && !hasFGColor ) {

				String fontColorCode = getFontColor((String)studentDataMap.
						get(Constants.ANSWER_BACKGROUND_COLOR));
				String bgColorCode = getColorCode((String)studentDataMap.
						get(Constants.ANSWER_BACKGROUND_COLOR));

				studentAccommodations.setAnswerBackgroundColor(bgColorCode);    
				studentAccommodations.setAnswerFontColor(fontColorCode);                        


			} else if ( !hasBGColor && hasFGColor ) {

				String bgColorCode = getBGColor((String)studentDataMap.
						get(Constants.ANSWER_FONT_COLOR));

				String fontColorCode =  getColorCode((String)studentDataMap.
						get(Constants.ANSWER_FONT_COLOR)); 

				studentAccommodations.setAnswerBackgroundColor(bgColorCode);    
				studentAccommodations.setAnswerFontColor(fontColorCode);                          


			} else if ( hasBGColor && hasFGColor ) {

				studentAccommodations.setAnswerBackgroundColor(
						getColorCode((String)studentDataMap.get(
								Constants.ANSWER_BACKGROUND_COLOR)));

				studentAccommodations.setAnswerFontColor(
						getColorCode((String)studentDataMap.get(
								Constants.ANSWER_FONT_COLOR)));

			}


			if ( studentDataMap.get(Constants.FONT_SIZE).equals("") 
					|| studentDataMap.get(Constants.FONT_SIZE)== null ) {

				studentAccommodations.setAnswerFontSize(Constants.STANDARD_FONT_SIZE);

			} else {

				studentAccommodations.setAnswerFontSize(
						getFontSize(
								(String)studentDataMap.get(Constants.FONT_SIZE)));
			}

		}

	}
	
	/**
	 *  Get student Demographic Data 
	 */
	private StudentDemoGraphics [] getStudentDemographicData(HashMap<String,String> studentDemoMap, ArrayList<String> demoList) {

		StudentDemoGraphics [] StudentDemoGraphics = new StudentDemoGraphics[studentDemoMap.size()];
		StudentDemographicValue [] studentDemographicValues = null;
		StudentDemographicValue studentDemographicValue = null;
		
		Integer ethnicityDemoId = new Integer(0);
		Boolean subEthnicityToBePresent = false; 
		Integer  hispanicLatinoPresent = new Integer(0);
		
		int index = 0;
		
		for ( int i= 0 ; i < demoList.size() ; i++ ) {

			StudentDemoGraphics studentDemographic = new StudentDemoGraphics();
			if ( demoGraphicMap.containsKey(demoList.get(i)) )  {

				String demoName = (String)demoList.get(i) ;
				Integer demoGraphicId = (Integer)demoGraphicMap.get(demoName);
				String demoLabelName = (String) studentDemoMap.get(demoName);
				String demoCardinality = (String) demoCardinalityMap.get(demoName);

				if(demoCardinality.equals(Constants.MULTIPLE_DEMOGRAPHIC)){
					StringTokenizer stStr = new StringTokenizer(demoLabelName,
							Constants.DEMOGRAPHIC_VALUSE_SEPARATOR);
					int j=0;
					studentDemographicValues = new StudentDemographicValue[stStr.countTokens()]; 
					while(stStr.hasMoreTokens()){

						String demoVal = stStr.nextToken().trim();
						String demoValue = getDbDemographicValue(demoName ,demoVal);						
						studentDemographicValue = new StudentDemographicValue();
						studentDemographicValue.setValueName(demoValue.equalsIgnoreCase("") ? "" :demoValue.split("~~")[0]);
						studentDemographicValue.setValueCode(demoValue.equalsIgnoreCase("") ? "" :demoValue.split("~~")[1]);
						studentDemographicValue.setVisible("T");
						studentDemographicValue.setSelectedFlag("true");
						studentDemographicValues[j++] = studentDemographicValue;
						studentDemographic.setId(demoGraphicId);
						studentDemographic.setStudentDemographicValues(studentDemographicValues);
						StudentDemoGraphics[index]= studentDemographic;
					}
				} else {
					
					if (demoName.equalsIgnoreCase("ETHNICITY")){
						if (demoLabelName.equalsIgnoreCase("HISPANIC OR LATINO")){
							// If the demographic : ethnicity selected is "HISPANIC OR LATINO" for Laslink 
							// then the subEthnicity value is to be saved.
							ethnicityDemoId = demoGraphicId ;
							subEthnicityToBePresent = true;	
							hispanicLatinoPresent = 1;
							continue;
						}
					}
					if (demoName.equalsIgnoreCase("SUB_ETHNICITY")&& subEthnicityToBePresent ){
						// If the demographic : sub-ethnicity is expected and present then insert this sub-ethnicity value with respect
						// to ethnicity demographicId. >> Same behaviour as UI. 
						demoGraphicId = ethnicityDemoId ;
						subEthnicityToBePresent = false;
						hispanicLatinoPresent = 2;
						StudentDemoGraphics = Arrays.copyOf(StudentDemoGraphics, (studentDemoMap.size()-1));	
						
					}
					
					studentDemographicValue = new StudentDemographicValue();
					demoLabelName = getDbDemographicValue(demoName ,demoLabelName); 
					studentDemographicValue.setValueName(demoLabelName.equalsIgnoreCase("") ? "" :demoLabelName.split("~~")[0]);
					studentDemographicValue.setValueCode(demoLabelName.equalsIgnoreCase("") ? "" :demoLabelName.split("~~")[1]);
					studentDemographicValue.setVisible("T");
					studentDemographicValue.setSelectedFlag("true");
					studentDemographicValues = new StudentDemographicValue[1];     
					studentDemographicValues[0] = studentDemographicValue;
					studentDemographic.setId(demoGraphicId);
					studentDemographic.setStudentDemographicValues(studentDemographicValues);
					studentDemographic.setId(demoGraphicId);
					StudentDemoGraphics[index]= studentDemographic;					
				}
			}
			index = index + 1;
		}
		
		/**
		 * This block will be executed if there is "Hispanic or Latino" in place of Ethnicity column and Sub-Ethnicity column is blank.
		 * The value of hispanicLatinoPresent will be changed to 2 if Sub-Ethnicity is present.
		 * Then we again traverse to insert "Hispanic or Latino" value in Ethnicity demographic.		
		*/
		if(hispanicLatinoPresent == 1){
			for ( int i= 0 ; i <  demoList.size() ; i++ ){
				StudentDemoGraphics studentDemographic = new StudentDemoGraphics();
				if ( demoGraphicMap.containsKey(demoList.get(i)) )  {
					String demoName = (String)demoList.get(i) ;
					Integer demoGraphicId = (Integer)demoGraphicMap.get(demoName);
					String demoLabelName = (String) studentDemoMap.get(demoName);
					
					if (demoName.equalsIgnoreCase("ETHNICITY")  && demoLabelName.equalsIgnoreCase("HISPANIC OR LATINO")){							
						studentDemographicValue = new StudentDemographicValue();
						demoLabelName = getDbDemographicValue(demoName ,demoLabelName); 
						studentDemographicValue.setValueName(demoLabelName.equalsIgnoreCase("") ? "" :demoLabelName.split("~~")[0]);
						studentDemographicValue.setValueCode(demoLabelName.equalsIgnoreCase("") ? "" :demoLabelName.split("~~")[1]);
						studentDemographicValue.setVisible("T");
						studentDemographicValue.setSelectedFlag("true");
						studentDemographicValues = new StudentDemographicValue[1];     
						studentDemographicValues[0] = studentDemographicValue;
						studentDemographic.setId(demoGraphicId);
						studentDemographic.setStudentDemographicValues(studentDemographicValues);
						studentDemographic.setId(demoGraphicId);
						StudentDemoGraphics[index]= studentDemographic;	
						
						break;
					}
					
				}						
			}
		}
		return StudentDemoGraphics;
	}
	
	
	
	/**
	 *  Check whether student exists or not. If exists then return student id
	 */
	private StudentFileRow isStudentExists( ManageStudent student) {
		String newFileKey = (student.getStudentIdNumber()!= null)? student.getStudentIdNumber().trim() : generateKey(student) ;
		return dbCacheImpl.getStudentFileRow(newFileKey);
		
//		if (this.visibleStudent.size() > 0) {
//			String newFileKey = (student.getStudentIdNumber()!= null)? student.getStudentIdNumber().trim() : generateKey(student) ;
//			if(this.visibleStudent.containsKey(newFileKey)){
//				StudentFileRow studentFileRow = this.visibleStudent.get(newFileKey);
//				return studentFileRow;
//			}
//		}
//		return null;
	}
	
	private String generateKey(ManageStudent student) {
		
		String middleName = "";
		if(null != student.getMiddleName())
			middleName = student.getMiddleName().toUpperCase();
		String key = student.getFirstName().toUpperCase()
				+ middleName
				+ student.getLastName().toUpperCase()
				+ student.getGender();
		
		String datefromDB = "";
		Date dbDate = student.getBirthDate();
		if ( dbDate != null && !dbDate.equals("")) {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			datefromDB   = sdf.format(dbDate);                           
		} 
		return key+datefromDB;
	}
	
	/**
	 *  Update student,update student accommodation and demographic details
	 */
	private void createNewStudent(ManageStudent manageStudent,StudentAccommodations studentAccommodations,
										StudentDemoGraphics[] studentDemographic,Node[] studentNode) throws CTBBusinessException {

		
		createStudentUpload(manageStudent, customerId);
		StudentFileRow studentFileRow = new StudentFileRow();
		copyStudentDetail (studentFileRow, manageStudent, studentAccommodations);
		dbCacheImpl.addStudentFileRow(manageStudent.getStudentIdNumber().trim(), studentFileRow);
		//this.visibleStudent.put(manageStudent.getStudentIdNumber().trim(), studentFileRow);
		
		newStdRecordCacheImpl.addNewStudent(manageStudent.getStudentIdNumber().trim(), new UploadStudent(manageStudent, studentAccommodations, studentDemographic));
		//UploadStudentFile.finalStudentList.add(new UploadStudent(manageStudent, studentAccommodations, studentDemographic));
	}
	
	private void createStudentUpload(ManageStudent manageStudent,
			Integer customerId2) {
		String studentUserName =  StudentUtils.generateUniqueStudentUserName(this.studentUserNames,manageStudent);
		manageStudent.setLoginId(studentUserName);
		manageStudent.setCustomerId(customerId2);
	}

	/*
	 * Is Student already associate with orgNodeId
	 */ 
	private static boolean isOrganizationPresent (Integer orgNodeId, OrganizationNode []organizationNode) {
		for (int i = 0; i < organizationNode.length ; i++) {
			OrganizationNode tempNode = organizationNode[i];
			if (orgNodeId.intValue() == tempNode.getOrgNodeId().intValue()) {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Copy StudentDetails and Accomodations in to StudentFileRow Object for update
	 */ 

	private void copyStudentDetail (StudentFileRow studentFileRow, ManageStudent manageStudent, 
			StudentAccommodations studentAccommodations) {

		/*studentFileRow.setFirstName(manageStudent.getFirstName());
		studentFileRow.setLastName(manageStudent.getLastName());
		studentFileRow.setMiddleName(manageStudent.getMiddleName());
		studentFileRow.setGender(manageStudent.getGender());
		studentFileRow.setBirthdate(manageStudent.getBirthDate());
		studentFileRow.setGrade(manageStudent.getGrade());*/
		studentFileRow.setOrganizationNodes(manageStudent.getOrganizationNodes());
		studentFileRow.setExtPin1(manageStudent.getStudentIdNumber());
		/*studentFileRow.setExtPin2(manageStudent.getStudentIdNumber2());*/
		studentFileRow.setStudentId(manageStudent.getId());
		studentFileRow.setUserName(manageStudent.getLoginId());

		//Accomodation
		/*studentFileRow.setScreenReader(studentAccommodations.getScreenReader());
		studentFileRow.setCalculator(studentAccommodations.getCalculator());
		studentFileRow.setTestPause(studentAccommodations.getTestPause());
		studentFileRow.setUntimedTest(studentAccommodations.getUntimedTest());
		studentFileRow.setHighlighter(studentAccommodations.getHighlighter());
		studentFileRow.setQuestionBackgroundColor(studentAccommodations.getQuestionBackgroundColor());
		studentFileRow.setQuestionFontColor(studentAccommodations.getQuestionFontColor());
		studentFileRow.setAnswerBackgroundColor(studentAccommodations.getAnswerBackgroundColor());
		studentFileRow.setAnswerFontColor(studentAccommodations.getAnswerFontColor());
		studentFileRow.setAnswerFontSize(studentAccommodations.getAnswerFontSize());*/


	}
	
	/*
	 * initStringCap
	 */
	private static String initStringCap ( String value ) {

		if ( value != null && !value.trim().equals("")) {

			char ch = value.charAt(0);

			if ((ch >= 65 && ch <= 90) || (ch >= 97 && ch <= 122)) {

				String newStringChar = (new Character(ch)).toString().toUpperCase();
				value = value.substring(1, value.length());
				value = newStringChar + value;


			}

		}

		return value;
	}
	
	/**
	 * Getting gender code from gender value
	 */

	private  String getGender(String str) {

		str = str.trim().toUpperCase();

		if ( str.length()>1 ) {

			if ( str.equalsIgnoreCase(Constants.MALE) )

				return Constants.MALE_CODE;

			if ( str.equalsIgnoreCase(Constants.FEMALE) )

				return Constants.FEMALE_CODE;

			if ( str.equalsIgnoreCase( Constants.UNKNOWN) )

				return Constants.UNKNOWN_CODE;

		} 

		return str;

	}
	
	private String getGradeValue (String findGrade) {     

		if( findGrade!=null && !"".equals(findGrade.trim())) {

			findGrade = findGrade.trim();

			for ( int i=0 ; i<grades.length ; i++ ) {

				String dbValue = (String) grades[i];

				if( dbValue.equalsIgnoreCase(findGrade) ) {

					return dbValue;
				}
			}

		}

		return "";
	}
	
	/*
	 * get default FontColor by passing background color
	 */ 

	private String getFontColor(String bgColor ){


		bgColor = bgColor.trim().toUpperCase();
		ArrayList colorList = (ArrayList) colorCombinationMap.get(bgColor);
		String defaultcolor = (String) colorList.get(0);
		String colorCode = getColorCode(defaultcolor);

		return colorCode;
	}

	/*
	 * get default BackGroundColor by passing font color
	 */ 

	private String getBGColor(String fontColor ){


		fontColor = fontColor.trim().toUpperCase();
		Set set = colorCombinationMap.keySet();
		Iterator it = set.iterator();
		String bgColor = "";

		while (it.hasNext()) {

			bgColor = (String)it.next();

			ArrayList fontList = (ArrayList)colorCombinationMap.get(bgColor);

			if (fontList.contains(fontColor)) {

				break;

			}

		}

		String colorCode = getColorCode(bgColor);

		return colorCode;
	}

	/**
	 * Getting default customer accommodation value
	 */

	private  String getDefaultAccommodation (String asAccommodation){

		String msDefaultValue = Constants.F;

		if(!(asAccommodation == null || "".equals(asAccommodation.trim()))){
			if( defaultAccommodationMap.containsKey(asAccommodation)){

				msDefaultValue = (String) defaultAccommodationMap.get(asAccommodation);

				return msDefaultValue;
			}

		}

		return msDefaultValue;
	}
	
	/**
	 * Getting customer accommodation value whether it is editable accommodation field or not
	 */ 

	private String getAccommodationValue ( String accomField, String accomValue ){

		String msDefaultValue = Constants.F;

		if (!(accomValue == null || "".equals(accomValue.trim()))){
			String accomodation = getChekBoxValue(accomValue);

			if ( editableAccomodationMap.containsKey(accomField)){
				String msEditable = (String) editableAccomodationMap.get(accomField);

				if (msEditable.equals(Constants.F)){

					if ( defaultAccommodationMap.containsKey(accomField)){
						msDefaultValue = (String) defaultAccommodationMap.get(accomField);
						return msDefaultValue;
					} 

				} else {

					return accomodation;
				}  

			} else {

				return accomodation;
			}

		}


		return msDefaultValue;
	}
	
	private String getColorCode(String asColor){

		String msColor="";
//		if (asColor != null && "".equals(asColor)) {

		asColor = asColor.trim().toUpperCase();

		if( colorCodeMap.containsKey(asColor) ){

			msColor = (String)colorCodeMap.get(asColor);

		} 

//		}


		return msColor;
	}
	
	/**
	 * Get the font size
	 */

	private String getFontSize(String asFont){

		asFont = asFont.trim().toUpperCase();

		if ( asFont.equalsIgnoreCase (Constants.LARGER_FONT))

			return Constants.LARGER_FONT_SIZE;

		else

			return Constants.STANDARD_FONT_SIZE ;

	}  
	
	/**
	 * Getting demographic value to store in db
	 */
	private String getDbDemographicValue(String fieldName, String value){
		if((value != null && !"".equals(value.trim())) ){
			fieldName = fieldName.trim().toUpperCase();
			Map valueMap = (HashMap) demoMap.get(fieldName);
			if ( valueMap.size()>0 ) {
				if(valueMap.containsKey(value)){
					return value+"~~"+valueMap.get(value);
				}
				/*for(int i = 0 ; i < valueList.size() ; i ++) {
					String dbValue = (String) valueList.get(i);
					if( dbValue.equalsIgnoreCase(value) ){
						return dbValue; 
					}
				}*/
			}
		}
		return "";
	}
	
	/*
	 * Copy StudentDetails and Accomodations in to StudentFileRow Object for create
	 */ 

	/*private void copyStudentDetail (StudentFileRow studentFileRow, Student student, 
			StudentAccommodations studentAccommodations) {

		studentFileRow.setFirstName(student.getFirstName());
		studentFileRow.setLastName(student.getLastName());
		if (student.getMiddleName() != null || !(student.getMiddleName().trim().equals(""))) {

			studentFileRow.setMiddleName(student.getMiddleName());

		} else {

			studentFileRow.setMiddleName(null);
		}
		studentFileRow.setUserName(student.getUserName());
		studentFileRow.setGender(student.getGender());
		studentFileRow.setBirthdate(student.getBirthdate());
		studentFileRow.setGrade(student.getGrade());
		studentFileRow.setExtPin1(student.getExtPin1());
		studentFileRow.setExtPin2(student.getExtPin2());
		studentFileRow.setStudentId(student.getStudentId());


	}*/
	
	/**
	 * Getting value of checkbox field as T/F
	 */

	private  String getChekBoxValue(String str) {

		str = str.trim().toUpperCase();

		if (str.equalsIgnoreCase(Constants.ACOMODATION_TRUE) 
				|| str.equalsIgnoreCase(Constants.T)
				|| str.equalsIgnoreCase(Constants.ACOMODATION_YES)
				|| str.equalsIgnoreCase(Constants.ACOMODATION_Y)){
			return Constants.T;
		}
		else if (str.equalsIgnoreCase(Constants.ACOMODATION_FALSE)
				|| str.equalsIgnoreCase(Constants.F)
				|| str.equalsIgnoreCase(Constants.ACOMODATION_NO)
				|| str.equalsIgnoreCase(Constants.ACOMODATION_N)){
			return Constants.F;
		}
		return Constants.F;

	}

	private void init() {
		try {
			studentDao = new StudentFileDaoImpl();
			dao = new UploadFileDaoImpl();

			setUploadedFilename(new FileInputStream(this.inFile));
			colorList.add(Constants.WHITE);
			colorList.add(Constants.BLACK);
			colorList.add(Constants.LIGHT_BLUE);
			colorList.add(Constants.LIGHT_PINK);
			colorList.add(Constants.LIGHT_YELLOW);
			colorList.add(Constants.DARK_BLUE);
			colorList.add(Constants.DARK_BROWN);
			colorList.add(Constants.YELLOW);
			colorList.add(Constants.GREEN);

			// if background color is BLACK
			ArrayList<String> fontBlackList = new ArrayList<String>();
			fontBlackList.add(Constants.GREEN);
			fontBlackList.add(Constants.WHITE);
			fontBlackList.add(Constants.YELLOW);

			// if background color is DARK BLUE
			ArrayList<String> fontDarkBlueList = new ArrayList<String>();
			fontDarkBlueList.add(Constants.WHITE);

			// if background color is LIGHT BLUE or LIGHT PINK or LIGHT YELLOW
			// or WHITE
			ArrayList<String> fontColorList = new ArrayList<String>();
			fontColorList.add(Constants.BLACK);
			fontColorList.add(Constants.DARK_BLUE);
			fontColorList.add(Constants.DARK_BROWN);

			colorCombinationMap.put(Constants.BLACK, fontBlackList);
			colorCombinationMap.put(Constants.DARK_BLUE, fontDarkBlueList);
			colorCombinationMap.put(Constants.LIGHT_BLUE, fontColorList);
			colorCombinationMap.put(Constants.LIGHT_PINK, fontColorList);
			colorCombinationMap.put(Constants.LIGHT_YELLOW, fontColorList);
			colorCombinationMap.put(Constants.WHITE, fontColorList);

			// Color Code
			colorCodeMap.put(Constants.WHITE, Constants.WHITE_CODE);
			colorCodeMap.put(Constants.BLACK, Constants.BLACK_CODE);
			colorCodeMap.put(Constants.LIGHT_BLUE, Constants.LIGHT_BLUE_CODE);
			colorCodeMap.put(Constants.LIGHT_PINK, Constants.LIGHT_PINK_CODE);
			colorCodeMap.put(Constants.LIGHT_YELLOW,
					Constants.LIGHT_YELLOW_CODE);
			colorCodeMap.put(Constants.DARK_BLUE, Constants.DARK_BLUE_CODE);
			colorCodeMap.put(Constants.DARK_BROWN, Constants.DARK_BROWN_CODE);
			colorCodeMap.put(Constants.YELLOW, Constants.YELLOW_CODE);
			colorCodeMap.put(Constants.GREEN, Constants.GREEN_CODE);

			/*
			 * Value population starts
			 */
			grades = studentUploadUtils.getGradesForCustomer(customerId);
			getValidDemographicValue(customerId, demoMap);
			noOfDemographicList = demoMap.size();
			populateDefaultAccommodationValues(customerId,
					defaultAccommodationMap, editableAccomodationMap);
			isMatchUploadOrgIds = studentDao.checkCustomerConfigurationEntries(
					customerId, Constants.CUSTOMER_CONF_NAME);
			checkExternalStudent_id = studentDao
					.checkCustomerConfigurationEntries(customerId,
							Constants.MATCH_STUDENT_ID);
			checkOrgNodeCode = studentDao.checkCustomerConfigurationEntries(
					customerId, Constants.MATCH_ORG_CODE);
			checkCustomerConfiguration = studentDao
					.checkCustomerConfigurationEntries(customerId,
							Constants.DISABLE_MANDATORY_BIRTH_DATE);
			if (checkCustomerConfiguration) {
				String disableMandatoryBirthdateValue = studentDao
						.checkCustomerConfiguration(this.customerId,
								Constants.DISABLE_MANDATORY_BIRTH_DATE);
				if (disableMandatoryBirthdateValue.equalsIgnoreCase("T")) {
					disableMandatoryBirthdate = true;
				}
			}
			this.detailNodeM = studentDao.getUserDataTemplate(customerId);
			this.userTopOrgNode = studentDao.getTopNodeDetails(customerId);
			this.dataFileAudit = dao.getUploadFile(this.uploadFileId);
			orgPosFact = 3;
		} catch (Exception e) {
			logger.error("UploadStudentFile Error.. Init() Block...");
			e.printStackTrace();
		}
	}

	private void getValidDemographicValue(Integer customerId,
			HashMap<String, Map<String,String>> demoMap) {
		try {
			StudentDemoGraphics[] studentDemoGraphics = studentUploadUtils
					.getStudentDemoGraphics(customerId);
			if (studentDemoGraphics != null) {
				for (int i = 0; i < studentDemoGraphics.length; i++) {
					Integer customerDemographicId = studentDemoGraphics[i]
							.getCustomerDemographicId();
					String customerDemoName = studentDemoGraphics[i]
							.getLabelName();
					String customerDemoCardinality = studentDemoGraphics[i]
							.getValueCardinality();
					CustomerDemographicValue[] customerDemographicValue = studentUploadUtils
							.getCustomerDemographicValue(customerDemographicId);
					//ArrayList<String> demographicValueList = new ArrayList<String>();
					Map<String,String> demographicValueMap = new HashMap<String, String>();
					
					for (int k = 0; k < customerDemographicValue.length; k++) {
						/*String msDemographicValue = customerDemographicValue[k]
								.getValueName();
						demographicValueList.add(msDemographicValue);*/
						demographicValueMap.put(customerDemographicValue[k].getValueName(), customerDemographicValue[k].getValueCode());
					}
					demoMap.put(customerDemoName.toUpperCase(),
							demographicValueMap);
					demoGraphicMap.put(customerDemoName, customerDemographicId);
					demoCardinalityMap.put(customerDemoName,
							customerDemoCardinality);
				}
			}

		} catch (Exception s) {
			s.printStackTrace();
		}
	}

	private void populateDefaultAccommodationValues(Integer customerId,
			HashMap<String, String> defaultAccommodationMap,
			HashMap<String, String> editableAccomodationMap) {
		try {
			CustomerConfig[] customerConfigValues = studentUploadUtils
					.getCustomerConfigurationForAccommodation(customerId);
			if (customerConfigValues != null) {
				for (int i = 0; i < customerConfigValues.length; i++) {
					defaultAccommodationMap.put(customerConfigValues[i]
							.getCustomerConfigurationName(),
							customerConfigValues[i].getDefaultValue());
					editableAccomodationMap.put(customerConfigValues[i]
							.getCustomerConfigurationName(),
							customerConfigValues[i].getEditable());
				}
			}
		} catch (Exception se) {
			se.printStackTrace();
		}

	}

	private int getLoginUserOrgPosition(String[] row, String[] rowHeader,
			Node[] loginUserNode) {
		int loginUserPosition = 0;
		try {
			Node[] node = this.studentFileRowHeader[0].getOrganizationNodes();
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

	private boolean isMaxLength50(String value) {
		if (value.length() <= 50) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isValidMDR(int cellPos, boolean isMatchUploadOrgIds,
			String orgCode, Integer[] parentOrgId, Integer categoryId,
			HashMap<Integer, ArrayList<String>> requiredMap,
			HashMap<Integer, ArrayList<String>> invalidCharMap,
			HashMap<Integer, ArrayList<String>> logicalErrorMap,
			List<String> newMDRList, String strCellMdr, String orgName,
			String strCellHeaderMdr) {
		if (orgName == null || orgName.trim().length() == 0) {
			return true;
		}
		if (!isOrganizationExists(isMatchUploadOrgIds, orgCode, parentOrgId,
				categoryId, newMDRList, strCellMdr, orgName)) {
			//New Organization
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
			} else if (!isUniqueMdr(strCellMdr, newMDRList)) {
				ArrayList<String> requiredList = new ArrayList<String>();
				requiredList.add(strCellHeaderMdr);
				logicalErrorMap.put(new Integer(cellPos), requiredList);
				return false;

			}
			newMDRList.add(strCellMdr);
		} else {
			//Update Organization
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
			List<String> newMDRList, String strCellMdr, String orgName) {
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
				if (isOrgExist) {
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

	// For LAS Online – 2013 – Defect 74768 – support MDR number upload-download
	private boolean isUniqueMdr(String strCellMdr, List<String> newMDRList) {
		studentDao = new StudentFileDaoImpl();
		try {
			String val = this.studentDao
					.checkUniqueMdrNumberForOrgNodes(strCellMdr);
			if ((val.equalsIgnoreCase("T"))
					&& !(newMDRList.contains(strCellMdr))) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	// For LAS Online – 2013 – Defect 74768 – support MDR number upload-download
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

	private String getCellValue(String cell) {
		return  (null == cell)?"":cell.trim() ;
	}

	private void getEachRowStudentDetail(int rowPosition, String[] row,
			String[] rowHeader, HashMap<Integer, ArrayList<String>> requiredMap,
			HashMap<Integer, ArrayList<String>> maxLengthMap,
			HashMap<Integer, ArrayList<String>> invalidCharMap,
			HashMap<Integer, ArrayList<String>> logicalErrorMap,
			HashMap<Integer, ArrayList<String>> minLengthMap) throws Exception {

		ArrayList<String> requiredList = new ArrayList<String>();
		ArrayList<String> invalidList = new ArrayList<String>();
		ArrayList<String> maxLengthList = new ArrayList<String>();
		ArrayList<String> logicalErrorList = new ArrayList<String>();
		ArrayList<String> minLengthList = new ArrayList<String>();
		// retrive header category Array
		Node[] node = this.studentFileRowHeader[0].getOrganizationNodes();
		int studentHeaderStartPosition = node.length * orgPosFact;
		// checking for required field,invalid character,maxlength,logical error
		if (isRequired(studentHeaderStartPosition, row, rowHeader, requiredList)) {
			requiredMap.put(new Integer(rowPosition), requiredList);
		} else if (isInvalidChar(studentHeaderStartPosition, row, rowHeader,
				invalidList)) {
			invalidCharMap.put(new Integer(rowPosition), invalidList);
		} else if (isMinlength(studentHeaderStartPosition, row, rowHeader,
				minLengthList)) {
			minLengthMap.put(new Integer(rowPosition), minLengthList);
		} else if (isMaxlengthExceed(studentHeaderStartPosition, row,
				rowHeader, maxLengthList)) {
			maxLengthMap.put(new Integer(rowPosition), maxLengthList);
		} else if (isLogicalError(studentHeaderStartPosition, row, rowHeader,
				logicalErrorList)) {
			logicalErrorMap.put(new Integer(rowPosition), logicalErrorList);
		}
	}

	private boolean isLogicalError(int studentHeaderStartPosition, String[] row,
			String[] rowHeader, ArrayList<String> logicalErrorList) {
		int totalCells = rowHeader.length;
		// retrieve each cell value for user
		String msBackGroundColor = "";
		String strCell = "";
		boolean isEthnicityPresent = false;
		boolean isSubEthnicityRequired = false;
		// Start demographic checking
		int start = totalCells - noOfDemographicList;
		boolean isDemographicStart = false;
		for (int i = studentHeaderStartPosition; i < totalCells; i++) {
			String cellHeader = rowHeader[i];
			String cell = row[i];
			strCell = getCellValue(cell);
			if (!(strCell == null || strCell.equals(""))) {
				if (cellHeader.equals(
						Constants.REQUIREDFIELD_DATE_OF_BIRTH)
						&& isFutureDate(strCell)) {
					logicalErrorList.add(Constants.REQUIREDFIELD_DATE_OF_BIRTH);
				}
				// For validating combination of question background and font
				// color
				else if (cellHeader.equals(
						Constants.QUESTION_BACKGROUND_COLOR)) {
					msBackGroundColor = strCell;
				} else if (cellHeader.equals(
						Constants.QUESTION_FONT_COLOR)
						&& !strCell.trim().equals("")
						&& !isValidColorCombination(msBackGroundColor, strCell)) {
					logicalErrorList.add(Constants.QUESTION_FONT_COLOR);
				} else if (cellHeader.equals(
						Constants.ANSWER_BACKGROUND_COLOR)) {
					msBackGroundColor = strCell;
				} else if (cellHeader.equals(
						Constants.ANSWER_FONT_COLOR)
						&& !strCell.trim().equals("")
						&& !isValidColorCombination(msBackGroundColor, strCell)) {
					logicalErrorList.add(Constants.ANSWER_FONT_COLOR);
				}

				/*if (this.isStudentIdUnique && (cellHeader.equals(Constants.STUDENT_ID) || cellHeader.equals(this.studentIdLabel))) {
					if (isStudentIdUnique(strCell.trim()) && !studentIdList.contains(strCell.trim())) {
							studentIdList.add(strCell.trim());
					} else {
						logicalErrorList.add((this.isStudentIdConfigurable)?this.studentIdLabel:Constants.STUDENT_ID);
					}
				}*/
			}
		}
		// Demographic checking for logical error start
		for (int i = start; i < totalCells; i++) {
			String cellHeader = rowHeader[i];
			String cell = row[i];
			strCell = getCellValue(cell);

			if (cellHeader.equalsIgnoreCase(this.ethnicityLabel)) {
				if (!strCell.trim().equals("")) {
					isEthnicityPresent = true;
					if (strCell.equalsIgnoreCase("HISPANIC OR LATINO")) {
						isSubEthnicityRequired = true;
					}
				}
			} else if (cellHeader.equalsIgnoreCase(this.subEthnicityLabel)) {
				if (isSubEthnicityRequired && strCell.trim().equals("")) {
					// TODO
				}
				if (!isSubEthnicityRequired && !strCell.trim().equals("")) {
					logicalErrorList.add(Constants.ETHNICITY_LABEL);
				}
				if (!isEthnicityPresent && !strCell.trim().equals("")) {
					logicalErrorList.add(Constants.SUB_ETHNICITY_LABEL);
				}
				break;
			}
		}// end Demographic checking for logical error
		
		if (logicalErrorList.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	private boolean isRequired(int studentHeaderStartPosition, String[] row,
			String[] rowHeader, ArrayList<String> requiredList) {
		int totalCells = rowHeader.length;
		String strCell = "";
		// retrieve each cell value for student
		for (int i = studentHeaderStartPosition; i < totalCells; i++) {
			String cellHeader = rowHeader[i];
			String cell = row[i];
			strCell = getCellValue(cell);
			// Required field checking
			if (strCell.equals("")) {
				if (cellHeader.equals(
						Constants.REQUIREDFIELD_FIRST_NAME)) {
					requiredList.add(Constants.REQUIREDFIELD_FIRST_NAME);
				} else if (cellHeader.equals(
						Constants.REQUIREDFIELD_LAST_NAME)) {
					requiredList.add(Constants.REQUIREDFIELD_LAST_NAME);
				} else if (cellHeader.equals(
						Constants.REQUIREDFIELD_DATE_OF_BIRTH)) {
					if (!disableMandatoryBirthdate) {
						requiredList.add(Constants.REQUIREDFIELD_DATE_OF_BIRTH);
					}
				} else if (cellHeader.equals(
						Constants.REQUIREDFIELD_GRADE)) {
					requiredList.add(Constants.REQUIREDFIELD_GRADE);
				} else if (cellHeader.equals(
						Constants.REQUIREDFIELD_GENDER)) {
					requiredList.add(Constants.REQUIREDFIELD_GENDER);
				} else if (this.isStudentIdMandatory) {
					if (cellHeader.equals(
							this.studentIdLabel)) {
						requiredList.add(this.studentIdLabel);
					}
				}
			}
		}
		if (requiredList.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	private boolean isInvalidChar(int studentHeaderStartPosition, String[] row,
			String[] rowHeader, List<String> invalidList) {

		int totalCells = rowHeader.length;
		String strCellHeader = "";
		
		// Get the position of Demographic details
		int start = totalCells - noOfDemographicList;
		String strCell = "";
		boolean isDemographicStart = false;
		for (int i = studentHeaderStartPosition; i < totalCells; i++) {
			String cellHeader = rowHeader[i];
			String cell = row[i];
			strCell = getCellValue(cell);
			if (i == start) {
				isDemographicStart = true;
			}
			if (!strCell.equals("")) {
				if (cellHeader.equals(
						Constants.REQUIREDFIELD_FIRST_NAME)
						&& !validNameString(strCell)) {
					invalidList.add(Constants.REQUIREDFIELD_FIRST_NAME);
				} else if (cellHeader.equals(
						Constants.MIDDLE_NAME)
						&& !validNameString(strCell)) {
					invalidList.add(Constants.MIDDLE_NAME);
				} else if (cellHeader.equals(
						Constants.REQUIREDFIELD_LAST_NAME)
						&& !validNameString(strCell)) {
					invalidList.add(Constants.REQUIREDFIELD_LAST_NAME);
				}

				else if (cellHeader.equals(
						Constants.REQUIREDFIELD_GRADE)
						&& !isValidGrade(strCell)) {
					invalidList.add(Constants.REQUIREDFIELD_GRADE);
				}

				else if (cellHeader.equals(
						Constants.REQUIREDFIELD_GENDER)
						&& !isValidGender(strCell)) {
					invalidList.add(Constants.REQUIREDFIELD_GENDER);
				}
				// Changes for GACRCT2010CR007 .
				else if (!(strCell == null || strCell.equals(""))
						&& cellHeader.equals(
								Constants.REQUIREDFIELD_DATE_OF_BIRTH)
						&& !validateDateString(strCell)) {
					invalidList.add(Constants.REQUIREDFIELD_DATE_OF_BIRTH);
				}
				// Student accomodation

				else if (cellHeader.equals(
						Constants.SCREEN_READER)
						&& !strCell.trim().equals("")
						&& !isValidCheckBox(strCell)) {
					invalidList.add(Constants.SCREEN_READER);
				} else if (cellHeader.equals(
						Constants.CALCULATOR)
						&& !strCell.trim().equals("")
						&& !isValidCheckBox(strCell)) {
					invalidList.add(Constants.CALCULATOR);
				}

				else if (cellHeader.equals(
						Constants.TEST_PAUSE)
						&& !strCell.trim().equals("")
						&& !isValidCheckBox(strCell)) {
					invalidList.add(Constants.TEST_PAUSE);
				}

				else if (cellHeader.equals(
						Constants.UNTIMED_TEST)
						&& !strCell.trim().equals("")
						&& !isValidCheckBox(strCell)) {
					invalidList.add(Constants.UNTIMED_TEST);
				} else if (cellHeader.equals(
						Constants.HIGHLIGHTER)
						&& !strCell.trim().equals("")
						&& !isValidCheckBox(strCell)) {
					invalidList.add(Constants.HIGHLIGHTER);
				}

				else if (cellHeader.equals(
						Constants.QUESTION_BACKGROUND_COLOR)
						&& !strCell.trim().equals("") && !isValidColor(strCell)) {
					invalidList.add(Constants.QUESTION_BACKGROUND_COLOR);
				}

				else if (cellHeader.equals(
						Constants.QUESTION_FONT_COLOR)
						&& !strCell.trim().equals("") && !isValidColor(strCell)) {
					invalidList.add(Constants.QUESTION_FONT_COLOR);
				} else if (cellHeader.equals(
						Constants.ANSWER_BACKGROUND_COLOR)
						&& !strCell.trim().equals("") && !isValidColor(strCell)) {
					invalidList.add(Constants.ANSWER_BACKGROUND_COLOR);
				} else if (cellHeader.equals(
						Constants.ANSWER_FONT_COLOR)
						&& !strCell.trim().equals("") && !isValidColor(strCell)) {
					invalidList.add(Constants.ANSWER_FONT_COLOR);
				} else if (cellHeader.equals(
						Constants.FONT_SIZE)
						&& !strCell.trim().equals("")
						&& !isValidFontSize(strCell)) {
					invalidList.add(Constants.FONT_SIZE);
				} else if (cellHeader.equals(
						this.studentIdLabel)
						&& !strCell.trim().equals("")) {
					if (!this.isStudentIdConfigurable
							&& !validStudentId(strCell)) {
						invalidList.add(this.studentIdLabel);
					} else if (this.isStudentIdConfigurable
							&& this.isStudentIdNumeric.equals("AN")
							&& !validAlphaNumericStudentId(strCell)) {
						invalidList.add(this.studentIdLabel);
					} else if (this.isStudentIdConfigurable
							&& this.isStudentIdNumeric.equals("NU")
							&& !validConfigurableStudentId(strCell)) {
						invalidList.add(this.studentIdLabel);
					}
				} else if (cellHeader.equals(
						this.studentId2Label)
						&& !strCell.trim().equals("")) {
					if (!this.isStudentId2Configurable
							&& !validStudentId(strCell)) {
						invalidList.add(this.studentId2Label);
					} else if (this.isStudentId2Configurable
							&& this.isStudentId2Numeric.equals("AN")
							&& !validAlphaNumericStudentId(strCell)) {
						invalidList.add(this.studentId2Label);
					} else if (this.isStudentId2Configurable
							&& this.isStudentId2Numeric.equals("NU")
							&& !validConfigurableStudentId(strCell)) {
						invalidList.add(this.studentId2Label);
					}
				} else if (i == start) {
					strCellHeader = getCellValue(cellHeader);
					if (!strCell.equals("")
							&& !validateDemographic(strCellHeader, strCell)) {
						invalidList.add(strCellHeader);
					}
				}
			} // End if cell validation start from firstName
				// increment demographic posision
			if (isDemographicStart) {
				start++;
			}
		}
		if (invalidList.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	private boolean isMaxlengthExceed(int studentHeaderStartPosition,
			String[] row, String[] rowHeader, ArrayList<String> maxLengthList) {
		int totalCells = rowHeader.length;
		String strCell = "";
		for (int i = studentHeaderStartPosition; i < totalCells; i++) {
			String cellHeader = rowHeader[i];
			String cell = row[i];
			strCell = getCellValue(cell);
			if (!strCell.equals("")) {
				if (cellHeader.equals(
						Constants.REQUIREDFIELD_FIRST_NAME)
						&& !isMaxLength32(strCell)) {
					maxLengthList.add(Constants.REQUIREDFIELD_FIRST_NAME);
				} else if (cellHeader.equals(
						Constants.MIDDLE_NAME)
						&& !strCell.trim().equals("")
						&& !isMaxLength32(strCell)) {
					maxLengthList.add(Constants.MIDDLE_NAME);
				} else if (cellHeader.equals(
						Constants.REQUIREDFIELD_LAST_NAME)
						&& !isMaxLength32(strCell)) {
					maxLengthList.add(Constants.REQUIREDFIELD_LAST_NAME);
				}
				// Changes for GA2011CR001
				else if (cellHeader.equals(
						this.studentIdLabel)
						&& !strCell.trim().equals("")
						&& this.isStudentIdConfigurable
						&& !isMaxLengthConfigurableStudentId(strCell)
						&& this.maxlengthStudentID != null) {
					maxLengthList.add(this.studentIdLabel);
				} else if (cellHeader.equals(
						Constants.STUDENT_ID)
						&& !strCell.trim().equals("")
						&& !this.isStudentIdConfigurable
						&& !isMaxLength32(strCell)) {
					maxLengthList.add(Constants.STUDENT_ID);
				} else if (cellHeader.equals(
						this.studentId2Label)
						&& !strCell.trim().equals("")
						&& this.isStudentId2Configurable
						&& !isMaxLengthConfigurableStudentId2(strCell)
						&& this.maxlengthStudentId2 != null) {
					maxLengthList.add(this.studentId2Label);
				} else if (cellHeader.equals(
						Constants.STUDENT_ID2)
						&& !strCell.trim().equals("")
						&& !this.isStudentIdConfigurable
						&& !isMaxLength32(strCell)) {
					maxLengthList.add(Constants.STUDENT_ID2);
				}
			}
		}
		if (maxLengthList.size() == 0) {
			return false;
		} else {
			return true;
		}

	}

	private boolean isMinlength(int studentHeaderStartPosition, String[] row,
			String[] rowHeader, ArrayList<String> minLengthList) {
		int totalCells = rowHeader.length;
		String strCell = "";
		for (int i = studentHeaderStartPosition; i < totalCells; i++) {
			String cellHeader = rowHeader[i];
			String cell = row[i];
			strCell = getCellValue(cell);
			if (!strCell.equals("")) {
				if (cellHeader.equals(this.studentIdLabel)
						&& !strCell.trim().equals("")
						&& this.isStudentIdConfigurable
						&& !isMinLengthConfigurableStudentId(strCell)
						&& this.studentIdMinLength != null) {
					minLengthList.add(this.studentIdLabel);
				} else if (cellHeader.equals(
						this.studentId2Label)
						&& !strCell.trim().equals("")
						&& this.isStudentId2Configurable
						&& !isMinLengthConfigurableStudentId2(strCell)
						&& this.studentId2MinLength != null) {
					minLengthList.add(this.studentId2Label);
				}
			}
		}
		if (minLengthList.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	private boolean isMinLengthConfigurableStudentId(String value) {
		if (Integer.parseInt(this.studentIdMinLength) < 0) {
			this.studentIdMinLength = "0";
		}
		if (value.length() >= Integer.parseInt(this.studentIdMinLength)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isMaxLengthConfigurableStudentId2(String value) {
		if (Integer.parseInt(this.maxlengthStudentId2) > 32) {
			this.maxlengthStudentId2 = "32";
		}
		if (value.length() <= Integer.parseInt(this.maxlengthStudentId2)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isMinLengthConfigurableStudentId2(String value) {
		if (Integer.parseInt(this.studentId2MinLength) < 0) {
			this.studentId2MinLength = "0";
		}
		if (value.length() >= Integer.parseInt(this.studentId2MinLength)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * check if the date is future date
	 */
	private boolean isFutureDate(String excelDate) {

		int position = excelDate.indexOf("/");

		String month = excelDate.substring(0, position);
		excelDate = excelDate.substring(position + 1);
		position = excelDate.indexOf("/");

		String day = excelDate.substring(0, position);
		String year = excelDate.substring(position + 1);

		Date systemDate = new Date();
		systemDate.setDate(31);
		systemDate.setMonth(11);
		systemDate.setYear(systemDate.getYear() + 1899);

		Date startDate = new Date();
		startDate.setDate(1);
		startDate.setMonth(0);
		startDate.setYear(startDate.getYear() + 1799);

		Date dateFromExcel = new Date();

		boolean isFutureDate = false;

		int monthValue = Integer.valueOf(month).intValue();
		int dayValue = Integer.valueOf(day).intValue();
		int yearValue = Integer.valueOf(year).intValue();

		dateFromExcel.setMonth(monthValue - 1);
		dateFromExcel.setDate(dayValue);
		dateFromExcel.setYear(yearValue);

		isFutureDate = dateFromExcel.after(systemDate);

		if (isFutureDate) {
			return isFutureDate;
		}

		boolean isPastDate = dateFromExcel.before(startDate);

		if (isPastDate) {
			return isPastDate;
		}

		return false;
	}

	/**
	 * Validating combination of valid background and valid font color
	 */

	private boolean isValidColorCombination(String asBackgroundColor,
			String asFontColor) {

		asBackgroundColor = asBackgroundColor.trim().toUpperCase();
		asFontColor = asFontColor.trim().toUpperCase();

		if (!(asBackgroundColor == null || "".equals(asBackgroundColor.trim()))
				&& !(asFontColor == null || "".equals(asFontColor.trim()))) {

			ArrayList colorList = (ArrayList) colorCombinationMap
					.get(asBackgroundColor);

			if (!colorList.contains(asFontColor)) {
				return false;
			}
		}
		return true;
	}

	private void checkLeafNodeError(int position, String[] row,
			String[] rowHeader, Map<Integer,String> leafNodeErrorMap) {

		Node[] node = this.studentFileRowHeader[0].getOrganizationNodes();
		int OrgHeaderLastPosition = node.length;
		int lastNodePos = 0;
		int lastCellPos = 0;
		String cellName = null;
		String cellId = null;

		for (int i = 0, j = 0; i < OrgHeaderLastPosition; i++, j = j
				+ orgPosFact) {
			if (!hasOrganization(j, row)) {
				cellName = row[j];
				cellId = row[j + 1];
				if ((!getCellValue(cellName).equals(""))
						|| (!getCellValue(cellId).equals(""))) {
					lastNodePos = i;
					lastCellPos = j;
				}
			}
		}
		if (lastNodePos != (OrgHeaderLastPosition - 1)) {
			leafNodeErrorMap.put(new Integer(position),
					getCellValue(rowHeader[lastCellPos]));
		}

	}

	private boolean isStudentIdUnique(String value) {
		
		 if(dbCacheImpl.getStudentFileRow(value) != null)
			 return false;
		 else 
			 return true;
		
	}

	/**
	 * validate invalid /valid Field : for character field
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

	/**
	 * Validate grade
	 */

	private boolean isValidGrade(String findGrade) {

		if (findGrade != null && !"".equals(findGrade.trim())) {

			findGrade = findGrade.trim();

			for (int i = 0; i < grades.length; i++) {

				String dbValue = (String) grades[i];

				if (dbValue.equalsIgnoreCase(findGrade)) {

					return true;

				}
			}

		}

		return false;

	}

	/**
	 * Validation against gender field
	 */

	private boolean isValidGender(String str) {

		str = str.trim().toUpperCase();

		if (!(str.equalsIgnoreCase(Constants.MALE_CODE)
				|| str.equalsIgnoreCase(Constants.FEMALE_CODE)
				|| str.equalsIgnoreCase(Constants.UNKNOWN_CODE)
				|| str.equalsIgnoreCase(Constants.MALE)
				|| str.equalsIgnoreCase(Constants.FEMALE) || str
				.equalsIgnoreCase(Constants.UNKNOWN))) {

			return false;

		}

		return true;
	}

	/**
	 * Validate date
	 */

	private boolean validateDateString(String dateStr) {

		if (isValidDateFormat(dateStr)) {

			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");

			try {
				Date temp = df.parse(dateStr);

			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}

			StringTokenizer tokenizer = new StringTokenizer(dateStr, "/");
			int i = 0;
			int month = 0;
			int day = 0;
			int year = 0;

			try {
				while (tokenizer.hasMoreTokens()) {

					String token = tokenizer.nextToken();
					int value = new Integer(token).intValue();
					if (i == 0) {

						if (value > 12 || value <= 0) {

							return false;

						}

						month = value;

					} else if (i == 1) {

						if (value > 31 || value <= 0) {

							return false;

						}

						day = value;
					} else if (i == 2) {

						year = value;

					}

					i++;
				} // end while

				// check leap year and no of days
				year = 2000 + year;

				if (month == 4 || month == 6 || month == 9 || month == 11) {

					if (day > 30) {

						return false;

					}

				} else if (month == 2) {

					if (isLeapYear(year) && day > 29) {

						return false;

					} else if (!isLeapYear(year) && day > 28) {

						return false;

					}

				}

			} catch (Exception e) {

				e.printStackTrace();
				return false;

			}

			return true;

		} else {

			return false;

		}

	}

	/*
	 * Validating the value for accommodation
	 */

	private boolean isValidCheckBox(String str) {

		str = str.trim().toUpperCase();

		if (!(str.equalsIgnoreCase(Constants.T)
				|| str.equalsIgnoreCase(Constants.F)
				|| str.equalsIgnoreCase(Constants.ACOMODATION_TRUE)
				|| str.equalsIgnoreCase(Constants.ACOMODATION_FALSE)
				|| str.equalsIgnoreCase(Constants.ACOMODATION_Y)
				|| str.equalsIgnoreCase(Constants.ACOMODATION_N)
				|| str.equalsIgnoreCase(Constants.ACOMODATION_YES) || str
				.equalsIgnoreCase(Constants.ACOMODATION_NO))) {

			return false;

		}

		return true;
	}

	/**
	 * validate color
	 */

	private boolean isValidColor(String asColor) {

		asColor = asColor.trim().toUpperCase();

		if (!(asColor == null || "".equals(asColor.trim()))) {

			if (!colorList.contains(asColor)) {

				return false;

			}

		}

		return true;
	}

	/**
	 * Validate font size
	 */

	private boolean isValidFontSize(String value) {

		value = value.trim().toUpperCase();

		if (!(value.equalsIgnoreCase(Constants.LARGER_FONT) ||

		value.equalsIgnoreCase(Constants.STANDARD_FONT))) {

			return false;
		}

		return true;

	}

	/**
	 * validate the StudentId
	 */
	private boolean validStudentId(String str) {

		str = str.trim();
		char[] characters = str.toCharArray();

		for (int i = 0; i < characters.length; i++) {

			char character = characters[i];

			if (!validStudentIdCharacter(character)) {

				return false;
			}
		}

		return true;
	}

	/**
	 * validate the StudentId
	 */
	private boolean validAlphaNumericStudentId(String str) {

		str = str.trim();
		char[] characters = str.toCharArray();

		for (int i = 0; i < characters.length; i++) {

			char character = characters[i];

			if (!validAlphaNumericCharacter(character)) {

				return false;
			}
		}

		return true;
	}

	/**
	 * validate the StudentId
	 */
	private boolean validConfigurableStudentId(String str) {

		str = str.trim();

		if (!validNumber(str)) {

			return false;
		}

		return true;
	}

	/**
	 * Validate demographic values and cardinality
	 */

	private boolean validateDemographic(String fieldName, String value) {

		String msCardinality = (String) demoCardinalityMap.get(fieldName);

		boolean checkMultipleCardinality = true;

		if (msCardinality.equals(Constants.MULTIPLE_DEMOGRAPHIC)) {

			StringTokenizer stStr = new StringTokenizer(value,
					Constants.DEMOGRAPHIC_VALUSE_SEPARATOR);
			ArrayList uniqueDemoList = new ArrayList();

			while (stStr.hasMoreTokens()) {

				String msTvalue = stStr.nextToken().trim();

				// checking for duplicate demographics value
				if (uniqueDemoList.contains(msTvalue.toUpperCase())) {

					return false;

				} else {
					uniqueDemoList.add(msTvalue.toUpperCase());
				}

				checkMultipleCardinality = isValidDemographic(fieldName,
						msTvalue);

				if (checkMultipleCardinality == false) {

					return false;
				}

			}

		} else {

			return isValidDemographic(fieldName, value);
		}

		return true;
	}

	/*
	 * check wheather Maxlength is 32
	 */

	private boolean isMaxLength32(String value) {
		if (value.length() <= 32) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * check Maxlength for gerogia Customer for studentID
	 */

	private boolean isMaxLengthConfigurableStudentId(String value) {
		if (Integer.parseInt(this.maxlengthStudentID) > 32) {
			this.maxlengthStudentID = "32";
		}
		if (value.length() <= Integer.parseInt(this.maxlengthStudentID)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Validate the character field
	 */

	public static boolean validNameCharacter(char ch) {

		boolean A_Z = ((ch >= 65) && (ch <= 90));
		boolean a_z = ((ch >= 97) && (ch <= 122));
		boolean zero_nine = ((ch >= 48) && (ch <= 57));
		boolean validChar = ((ch == '/') || (ch == '\'') || (ch == '-')
				|| (ch == '_') || (ch == '\\') || (ch == '.') || (ch == '(')
				|| (ch == ')') || (ch == '&') || (ch == '+') || (ch == ',') || (ch == ' '));

		return (zero_nine || A_Z || a_z || validChar);

	}

	private boolean validOrgNameCharacter(char ch) {
		boolean A_Z = ((ch >= 65) && (ch <= 90));
		boolean a_z = ((ch >= 97) && (ch <= 122));
		boolean zero_nine = ((ch >= 48) && (ch <= 57));
		// !, @, #, -, _, ', :, /, comma, period, and space will be allowed in
		// these fields.
		boolean validChar = ((ch == '/') || (ch == '\\') || (ch == '-')
				|| (ch == '\'') || (ch == '(') || (ch == ')') || (ch == '&')
				|| (ch == '+') || (ch == ',') || (ch == '.') || (ch == ' '));

		return (zero_nine || A_Z || a_z || validChar);

	}

	/**
	 * validating date
	 */
	private boolean isValidDateFormat(String excelDate) {

		if (excelDate.length() != 10) {

			return false;

		}
		int position = excelDate.indexOf("/");

		if (position == -1) {

			return false;
		}

		String month = excelDate.substring(0, position);
		excelDate = excelDate.substring(position + 1);
		position = excelDate.indexOf("/");

		if (position == -1) {

			return false;

		}
		String day = excelDate.substring(0, position);
		String year = excelDate.substring(position + 1);

		try {

			int monthValue = Integer.valueOf(month).intValue();
			int dayValue = Integer.valueOf(day).intValue();

			if (monthValue > 0 && monthValue <= 12) {

				if (dayValue > 0 && dayValue <= 31) {

					if (year.length() == 4) {

						return true;

					} else {

						return false;

					}

				} else {

					return false;

				}
			} else {

				return false;

			}

		} catch (Exception e) {

			return false;

		}
	}

	/**
	 * Check leap year
	 */
	public boolean isLeapYear(int year) {

		if (year % 100 == 0) {

			if (year % 400 == 0) {

				return true;

			} else {

				return false;
			}
		}

		if (year % 4 == 0) {

			return true;

		} else {

			return false;

		}

	}

	/**
	 * validate the StudentId character
	 */

	private boolean validStudentIdCharacter(char ch) {
		boolean A_Z = ((ch >= 65) && (ch <= 90));
		boolean a_z = ((ch >= 97) && (ch <= 122));
		boolean zero_nine = ((ch >= 48) && (ch <= 57));
		// space will be allowed in these fields.
		boolean validChar = (ch == ' ');

		return (zero_nine || A_Z || a_z || validChar);

	}

	/**
	 * validate the StudentId character
	 */

	private boolean validAlphaNumericCharacter(char ch) {
		boolean A_Z = ((ch >= 65) && (ch <= 90));
		boolean a_z = ((ch >= 97) && (ch <= 122));
		boolean zero_nine = ((ch >= 48) && (ch <= 57));

		return (zero_nine || A_Z || a_z);

	}

	/*
	 * Validate the numeric character
	 */

	private boolean validNumber(String str) {

		str = str.trim();
		char[] characters = str.toCharArray();

		for (int i = 0; i < characters.length; i++) {

			char character = characters[i];

			if (!((character >= 48) && (character <= 57))) {

				return false;

			}
		}

		return true;
	}

	/**
	 * Validate the demographic value
	 */

	private boolean isValidDemographic(String fieldName, String value) {

		fieldName = fieldName.trim().toUpperCase();

		Map valueMap = (HashMap) demoMap.get(fieldName);

		if (valueMap.size() > 0) {

			/*for (int i = 0; i < valueList.size(); i++) {

				String dbValue = (String) valueList.get(i);

				if (dbValue.equalsIgnoreCase(value)) {

					return true;
				}

			}*/
			
			if(valueMap.containsKey(value))
				return true;

		}

		return false;
	}

	/***
	 * This part is executed for Error Excel Creation
	 * 
	 * */

	private void errorExcelCreation(Map<Integer, ArrayList<String>> requiredMap, Map<Integer, ArrayList<String>> maxLengthMap,
			Map<Integer, ArrayList<String>> invalidCharMap, Map<Integer, ArrayList<String>> logicalErrorMap,
			Map<Integer, ArrayList<String>> hierarchyErrorMap, Map<Integer, String> leafNodeErrorMap,
			Map<Integer, ArrayList<String>> minLengthMap) throws Exception {

		int errorCount = 0;
		byte[] errorData = null;

		dao = new UploadFileDaoImpl();

		/*
		 * Date today = Calendar.getInstance().getTime(); String errorDate = new
		 * SimpleDateFormat("MM.dd.yyyy_HHmmss").format(today);
		 * 
		 * 
		 * String errorFileName = Configuration.getLocalFilePath() +
		 * Constants.FILE_SEPARATOR + this.inFile.getName().split("\\.")[0]+
		 * "_"+errorDate + ".errors";
		 */

		String errorFileName = Configuration.getLocalFilePath()
				+ Constants.FILE_SEPARATOR + this.inFile.getName().substring(0,this.inFile.getName().lastIndexOf(".") ) + ".errors";

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
					// rowDataList.add(Constants.ERROR_FIELD_NAME);
					rowDataList.add(Constants.ERROR_FIELD_DESCRIPTION);
					csvOutput.writeNext(rowDataList
							.toArray(new String[rowDataList.size()]));
				} else {
					if ((requiredMap.containsKey(new Integer(rowNumber))
							|| invalidCharMap
									.containsKey(new Integer(rowNumber))
							|| minLengthMap.containsKey(new Integer(rowNumber))
							|| maxLengthMap.containsKey(new Integer(rowNumber))
							|| logicalErrorMap.containsKey(new Integer(
									rowNumber))
							|| hierarchyErrorMap.containsKey(new Integer(
									rowNumber)) || leafNodeErrorMap
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
										/*
										 * rowDataList
										 * .add(rowHeaderData[cellPosition]);
										 */
										rowDataList
												.add(Constants.INVALID_FIELD_ERROR
														+ " - "
														+ rowHeaderData[cellPosition]);
										break;
									}
								}
							}

							/**
							 * checking for minimum length
							 * */
							if (minLengthMap.size() > 0) {
								if (minLengthMap.containsKey(new Integer(
										rowNumber))) {
									ArrayList<String> minLengthList = minLengthMap
											.get(new Integer(rowNumber));
									if (minLengthList
											.contains(rowHeaderData[cellPosition])) {
										/*
										 * rowDataList
										 * .add(rowHeaderData[cellPosition]);
										 */
										rowDataList
												.add(Constants.MINIMUM_FIELD_ERROR
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
										/*
										 * rowDataList
										 * .add(rowHeaderData[cellPosition]);
										 */
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
										/*
										 * rowDataList
										 * .add(rowHeaderData[cellPosition]);
										 */
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
			dataFileAudit.setUploadFileRecordCount(new Integer(0));
			dao.upDateAuditTable(dataFileAudit);
			baos.flush();
			baos.close();

			/**
			 * Error File transfer to FTP Location
			 * */
			// Session session = FtpSftpUtil.getSFTPSession();
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

	/***
	 * 
	 * 
	 * */

	/**
	 * @param uploadedStream
	 *            The fileStream to set.
	 */
	public void setUploadedFilename(InputStream uploadedStream) {
		this.uploadedStream = uploadedStream;
	}

	/**
	 * @param uploadDt
	 *            The date to set.
	 */
	public void setUploadDt(Date uploadDt) {
		this.uploadDt = uploadDt;
	}

	/**
	 * @param noOfUserColumn
	 *            The noOfColn to set.
	 */

	public void setNoOfHeaderRows(int noOfUserColumn) {
		this.noOfUserColumn = noOfUserColumn;
	}

	/**
	 * @Return Failed Record count .
	 */
	public int getFailedRecordCount() {
		return failedRecordCount;
	}

	/**
	 * @param failedRecordCount
	 *            The failed record count to set.
	 */
	public void setFailedRecordCount(int failedRecordCount) {
		this.failedRecordCount = failedRecordCount;
	}

	/**
	 * @Return upload Record count .
	 */
	public int getUploadRecordCount() {
		return uploadRecordCount;
	}

	/**
	 * @set Upload Record count .
	 */
	public void setUploadRecordCount(int uploadRecordCount) {
		this.uploadRecordCount = uploadRecordCount;
	}

}
