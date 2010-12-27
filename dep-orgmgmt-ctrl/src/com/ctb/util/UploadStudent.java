package com.ctb.util; 

import com.ctb.bean.studentManagement.ManageStudent;
import com.ctb.bean.studentManagement.OrganizationNode;
import com.ctb.bean.studentManagement.StudentDemographic;
import com.ctb.bean.studentManagement.StudentDemographicValue;
import com.ctb.bean.testAdmin.Address;
import com.ctb.bean.testAdmin.Customer;
//import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.CustomerConfig;
import com.ctb.bean.testAdmin.CustomerConfiguration;
import com.ctb.bean.testAdmin.CustomerConfigurationValue;
import com.ctb.bean.testAdmin.CustomerEmail;
import com.ctb.control.db.Users;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import com.ctb.control.db.UploadDataFile;
import com.ctb.bean.testAdmin.Student;
import com.ctb.bean.testAdmin.DataFileAudit;
import com.ctb.bean.testAdmin.DataFileRowError;
import com.ctb.bean.testAdmin.Node;
import com.ctb.bean.testAdmin.Role;
import com.ctb.bean.testAdmin.Student;
import com.ctb.bean.testAdmin.StudentAccommodations;
import com.ctb.bean.testAdmin.StudentDemoGraphics;
import com.ctb.bean.testAdmin.StudentDemoGraphicsData;
import com.ctb.bean.testAdmin.StudentFileRow;
import com.ctb.bean.testAdmin.StudentFile;
import com.ctb.bean.testAdmin.USState;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.UserFile;
import com.ctb.bean.testAdmin.UserFileRow;
import com.ctb.bean.testAdmin.UserNode;
import com.ctb.control.db.Addresses;
import com.ctb.control.db.OrgNode;
import com.ctb.control.db.Students;
import com.ctb.control.organizationManagement.OrganizationManagement;
import com.ctb.control.studentManagement.StudentManagement;
import com.ctb.control.userManagement.UserManagement;
import com.ctb.exception.CTBBusinessException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.poi.hssf.record.formula.functions.Char;
import com.ctb.util.CTBConstants;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.util.HSSFColor;
import com.ctb.bean.testAdmin.OrgNodeStudent;
import com.ctb.exception.uploadDownloadManagement.FileNotUploadedException;
import java.io.ByteArrayOutputStream;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;


/**
 *  This class is used to upload student data
 */
public class UploadStudent extends BatchProcessor.Process
{ 
	private HashMap storedPhaseGradeMap;
	private int phaseId;
	private String userName;
	private InputStream uploadedStream;
	private int custOeNodeId;
	private String email;
	private Date uploadDt;
	private int noOfUserColumn;
	private UploadDataFile uploadDataFile;
	private HashMap visibleStudent = new HashMap();

	private CustomerConfigurationValue customerConfigurationValue ;
	private Integer customerId;
	private int failedRecordCount;
	private int uploadRecordCount;
	private HashMap commonHierarchyMap;
	public UserFile userFile;
	public DataFileAudit dataFileAudit = new DataFileAudit();
	public Addresses addresses;
	private User loginUser = null;

	public OrgNode orgNode;
	public Users users;
	public Students students;
	public com.ctb.control.db.Customer customerdb;
	public OrganizationManagement organizationManagement;
	public StudentManagement studentManagement;
	public UserManagement userManagement;
	public com.ctb.control.db.OrgNodeCategory orgNodeCate;
	public UserNode[] usernode = null;
	public com.ctb.bean.testAdmin.OrgNodeCategory orgNodeCategory[] = null;

	private StudentFileRow []studentFileRowHeader;

	private String serverFilePath;

	public String []grades = null;
	public USState []uSState =null;

	public int traversCells = 0 ;  

	private String hideAccommodations ;
	private CustomerConfiguration[] customerConfigurations = null;

	private int noOfDemographicList = 0;

	public HashMap demoMap = new HashMap();

	public HashMap demoGraphicMap = new HashMap();
	private ArrayList colorList = new ArrayList();
	private HashMap colorCombinationMap = new HashMap();
	private HashMap colorCodeMap = new HashMap();
	private HashMap defaultAccommodationMap = new HashMap();
	private HashMap editableAccomodationMap = new HashMap();
	private HashMap demoCardinalityMap = new HashMap();

	boolean checkExternalStudent_id;
	boolean checkOrgNodeCode;
	boolean isMatchUploadOrgIds = false;
	private Node []userTopOrgNode = null;

	//Changed 04/12/2008
	private Node [] detailNodeM = null;

	//Changes for GACRCT2010CR007
	private boolean disableMandatoryBirthdate = false;

	boolean checkCustomerConfiguration;



	//Changes for GA2011CR001

	private boolean isStudentIdConfigurable = false;
	private boolean isStudentId2Configurable = false;
	private String studentIdLabel = CTBConstants.STUDENT_ID;
	private String studentId2Label = CTBConstants.STUDENT_ID2;
	private boolean isStudentIdMandatory = false;
	private String maxlengthStudentID = null;
	private String maxlengthStudentId2 = null;
	//START- GACR005 
	private String studentIdMinLength = "0";
	private String studentId2MinLength = "0";
	private String isStudentIdNumeric = "AN";
	private String isStudentId2Numeric = "AN";
	//END- GACR005


	public UploadStudent ( String serverFilePath,String username, 
			InputStream uploadedStream , 
			StudentFileRow []studentFileRowHeader,
			int noOfUserColumn , HashMap commonHierarchyMap,
			OrgNode orgNode , 
			com.ctb.control.db.OrgNodeCategory orgNodeCate, 
			Users users,com.ctb.control.db.Customer customerdb,
			UploadDataFile uploadDataFile, 
			OrganizationManagement organizationManagement,
			StudentManagement studentManagement,
			UserManagement userManagement, DataFileAudit dataFileAudit,
			com.ctb.control.db.Students students,
			Node []userTopOrgNode,String []valueForStudentId,String []valueForStudentId2 ) {





		setPriority(BatchProcessor.PRIORITY_NORMAL);

		setUsername(username);
		setUploadedFilename(uploadedStream);
		setUploadDt(new Date());
		setNoOfHeaderRows(noOfUserColumn);     
		setCommonHierarchyMap(commonHierarchyMap);


		//set UserHeader
		this.studentFileRowHeader = studentFileRowHeader;
		this.orgNode = orgNode;
		this.users = users;
		this.customerdb = customerdb;
		this.organizationManagement = organizationManagement;
		this.studentManagement = studentManagement;
		this.orgNodeCate = orgNodeCate;
		this.uploadDataFile = uploadDataFile;
		this.dataFileAudit = dataFileAudit;
		this.userManagement = userManagement;
		setPriority(BatchProcessor.PRIORITY_NORMAL);

		setUsername(username);
		setUploadedFilename(uploadedStream);
		setUploadDt(new Date());
		setNoOfHeaderRows(noOfUserColumn);     
		setCommonHierarchyMap(commonHierarchyMap);
		this.orgNode = orgNode;
		this.users = users;
		this.students = students;
		this.customerdb = customerdb;
		this.organizationManagement = organizationManagement;
		this.studentManagement = studentManagement;
		this.orgNodeCate = orgNodeCate;
		this.uploadDataFile = uploadDataFile;
		this.dataFileAudit = dataFileAudit;
		this.addresses = addresses;
		this.serverFilePath = serverFilePath;
		this.userTopOrgNode = userTopOrgNode;

		//Changes for GA2011CR001
		if( valueForStudentId != null){
			this.isStudentIdConfigurable = true;
			this.studentIdLabel = valueForStudentId[0]!=null ? valueForStudentId[0]: CTBConstants.STUDENT_ID;
			this.maxlengthStudentID = valueForStudentId[1];
			this.isStudentIdMandatory = valueForStudentId[2]!=null && valueForStudentId[2].equals("T")? true : false;
			this.studentIdMinLength = valueForStudentId[3];   //GACR005
			this.isStudentIdNumeric = valueForStudentId[4];   //GACR005

		}
		System.out.println("Student ID " + this.isStudentIdConfigurable +"...."+this.studentIdLabel +"..." +this.maxlengthStudentID  +"..." +this.studentIdMinLength +".."+this.isStudentIdNumeric  );
		
		//Changes for GA2011CR001  
		if( valueForStudentId2 != null){
			this.isStudentId2Configurable = true;
			this.studentId2Label = valueForStudentId2[0]!=null ? valueForStudentId2[0]: CTBConstants.STUDENT_ID2;
			this.maxlengthStudentId2 = valueForStudentId2[1];
			this.studentId2MinLength = valueForStudentId2[2]; //GACR005
			this.isStudentId2Numeric = valueForStudentId2[3]; //GACR005
			

		}
		System.out.println("Student ID 2" + this.isStudentId2Configurable +"...."+this.studentId2Label +"..." +this.maxlengthStudentId2  +"..." +this.studentId2MinLength +".."+this.isStudentId2Numeric  );
		// Initialize the list of color, get the customer configuration entries of customer, 

		initList();

	}


	public void run () {

		//POI details Initialize
		POIFSFileSystem pfs = null;
		HSSFSheet sheet = null;

		//User File Row Initialize
		UserFileRow userFileRow = null;
		//ErrorMap initilization
		HashMap requiredMap = new HashMap();
		HashMap maxLengthMap = new HashMap();
		HashMap minLengthMap = new HashMap();
		HashMap invalidCharMap = new HashMap();
		HashMap logicalErrorMap = new HashMap();
		HashMap hierarchyErrorMap = new HashMap();
		HashMap leafNodeErrorMap = new HashMap();
		HashMap blankRowMap = new HashMap();
		boolean isBlankRow = true;

		String strHeaderValue = "";
		String strBodyValue = "";

		String strCellName = "";
		String strCellId ="";
		String strCellHeaderName = "";
		String strCellHeaderId = "";


		HashMap studentDataMap = new HashMap();


		int loginUserPosition = 0;

		try {

			StudentFileRow[] studentFileRow = this.uploadDataFile.getExistStudentData(this.userName);
			for (int i = 0; i < studentFileRow.length; i++) {

				this.visibleStudent.put(studentFileRow[i].getStudentId(),studentFileRow[i]);

			}

			// Read UploaderFile through POI
			// retrive login userDetail
			loginUser = userManagement.getUserUpload(this.userName,this.userName); 

			pfs = new POIFSFileSystem( uploadedStream );
			HSSFWorkbook wb = new HSSFWorkbook(pfs);
			sheet = wb.getSheetAt(0);
			int totalRows = 0;

			if ( sheet != null ) {

				totalRows =  sheet.getPhysicalNumberOfRows();

			}
			//retrive each row from uploaded excel sheet
			HSSFRow rowHeader = sheet.getRow(0);
			for ( int i = 1; i < totalRows; i++ ) {

				System.out.println("    ***** Upload Control: Processing row " + i);

				HSSFRow row = sheet.getRow(i);
				if (row == null) {

					totalRows++;
					continue;

				} else {
					int totalCells = rowHeader.getPhysicalNumberOfCells();         
					// retrive each cell value for user
					for (int k = 0; k < totalCells; k++) {

						//   HSSFCell cellHeader = rowHeader.getCell((short)k);
						HSSFCell cell = row.getCell((short)k);

						if ( cell != null && (!getCellValue(cell).trim().equals("") 
								&& !(cell.getCellType() == 3)) ) {

							isBlankRow = false;                  

						}

					}

					if (isBlankRow) {

						blankRowMap.put(new Integer(i),"BlankRow");
						continue;

					}

				}
				// Get cell value of the row and validate the value and 
				// populate the error map 
				// Updated for GACR005
				getEachRowStudentDetail(i,row,rowHeader,requiredMap,maxLengthMap,
						invalidCharMap,logicalErrorMap,minLengthMap);

				//check if any required fieldmissing, invalid char, 
				//maxlength exceed, logical error has been occured        
				if (!(requiredMap.containsKey(new Integer(i)) 
						|| invalidCharMap.containsKey(new Integer(i)) 
						|| minLengthMap.containsKey(new Integer(i))
						|| maxLengthMap.containsKey(new Integer(i))
						|| logicalErrorMap.containsKey(new Integer(i)))) {
					//excel write process

					if ( isCommonPathValid (
							i, row, rowHeader, hierarchyErrorMap, isMatchUploadOrgIds,  this.userTopOrgNode) ) {

						loginUserPosition = 
							getLoginUserOrgPosition(row, rowHeader, this.userTopOrgNode);


						// create Organization process
						Node []node = this.studentFileRowHeader[0].
						getOrganizationNodes();

						int OrgHeaderLastPosition = node.length * 2;

						for ( int j = loginUserPosition + 2; 
						j < OrgHeaderLastPosition; j = j + 2 ) {

							HSSFCell cellHeaderName = rowHeader.getCell(j);
							HSSFCell cellHeaderId = rowHeader.getCell(j + 1);
							HSSFCell cellName = row.getCell(j);
							HSSFCell cellId = row.getCell(j + 1);

							strCellName = getCellValue(cellName);
							strCellId = getCellValue(cellId);
							strCellHeaderName = getCellValue(cellHeaderName);
							strCellHeaderId = getCellValue(cellHeaderId);

							// OrgName required check
							if ( strCellName.equals("") && hasOrganization(j,row) 
									&& !strCellId.equals("")) {

								/* write excel  required  
								 * with the help of cellHeaderName
								 */
								ArrayList requiredList = new ArrayList();
								requiredList.add(strCellHeaderName); 
								requiredMap.put(new Integer(i), requiredList);

								break;


							} else if (strCellName.equals("") && hasOrganization(j - 2, row) 
									&& !strCellId.equals("")) {

								// write excel  required  with the help of cellHeaderName
								ArrayList requiredList = new ArrayList();
								requiredList.add(strCellHeaderName); 
								requiredMap.put(new Integer(i), requiredList);


								break;

							} else { 

								//OrgName invalid char check
								if ( validString(strCellName) ) {

									//OrgCode invalid char check
									if ( !validString(strCellId) ) {

										/*write excel  invalid  
                                                 with the help of cellHeaderID*/
										ArrayList invalidList = new ArrayList ();
										invalidList.add(strCellHeaderId);
										invalidCharMap.put(new Integer(i), invalidList);
										break;

									} else {

										// maxlength checking
										boolean flag = false;

										if ( !isMaxLength50(strCellName) ) {

											ArrayList maxLengthList = new ArrayList();
											maxLengthList.add(strCellHeaderName);
											maxLengthMap.put(new Integer(i), maxLengthList);
											flag = true;

										}

										if ( !isMaxLength32(strCellHeaderId) ) {

											ArrayList maxLengthList = new ArrayList();
											maxLengthList.add(strCellHeaderId);
											maxLengthMap.put(new Integer(i), maxLengthList);
											flag = true;

										}

										if ( flag ) {

											break;

										}

									}

								} else {

									// write excel  invalid  with the help of cellHeaderName
									ArrayList invalidList = new ArrayList ();
									invalidList.add(strCellHeaderName);
									invalidCharMap.put(new Integer(i), invalidList);
									break;
								}

							}

						} //end for 

					}
					//Leaf Node Validation
					checkLeafNodeError (i, row, rowHeader, leafNodeErrorMap);

				} //end if

				isBlankRow = true; 

			} //end outer for 
			// Updated for GACR005
			if ( requiredMap.size() > 0 
					|| minLengthMap.size() > 0
					|| maxLengthMap.size() > 0 
					|| invalidCharMap.size() > 0 
					|| logicalErrorMap.size() > 0 
					|| hierarchyErrorMap.size() > 0 
					|| leafNodeErrorMap.size() > 0) {

				errorExcelCreation (requiredMap, maxLengthMap, 
						invalidCharMap, 
						logicalErrorMap,
						hierarchyErrorMap,
						leafNodeErrorMap,minLengthMap); 

			}  




			//create Student and organization
			createOrganizationAndStudent (requiredMap,minLengthMap, maxLengthMap, 
					invalidCharMap, logicalErrorMap, 
					hierarchyErrorMap, studentDataMap, 
					leafNodeErrorMap, blankRowMap, isMatchUploadOrgIds,
					this.userTopOrgNode);



		} catch (Exception e) {

			e.printStackTrace();    
		}


	} 
	/*
	 *
	 */ 
	private void checkLeafNodeError ( int position, HSSFRow row, HSSFRow rowHeader, 
			HashMap leafNodeErrorMap) {

		Node []node = this.studentFileRowHeader[0].getOrganizationNodes();
		HashMap commonPathMapDB = this.commonHierarchyMap;
		int OrgHeaderLastPosition = node.length ;
		//Node []loginUserNode =  orgNode.getTopNodesForUser(this.userName);
		String leafOrgName = "";
		ArrayList excelCommonPathList = new ArrayList();
		int loginUserPosition = 0;
		int lastNodePos = 0;
		int lastCellPos = 0;
		HSSFCell cellName = null;
		HSSFCell cellId = null;

		for ( int i = 0, j = 0; i < OrgHeaderLastPosition; i++, j = j + 2  ) {

			if ( !hasOrganization(j, row) ) {

				cellName = row.getCell(j);
				cellId = row.getCell(j + 1);

				if ( (!getCellValue(cellName).equals("")) 
						|| (!getCellValue(cellId).equals("")) ) {

					lastNodePos = i;
					lastCellPos = j;

				}

			}

		}

		//is leafNode

		if (lastNodePos != (OrgHeaderLastPosition - 1)) {

			leafNodeErrorMap.put(new Integer(position), 
					getCellValue(rowHeader.getCell(lastCellPos)));
		}   

	}

	/** 
	 * @param username The username to set.
	 */
	public void setUsername(String username) {
		this.userName = username;
	}

	/** 
	 * @param uploadedStream The fileStream to set.
	 */
	public void setUploadedFilename(InputStream uploadedStream) {
		this.uploadedStream = uploadedStream;
	}

	/** 
	 * @param uploadDt The date to set.
	 */
	public void setUploadDt(Date uploadDt) {
		this.uploadDt = uploadDt;
	}

	/** 
	 * @param noOfUserColumn The noOfColn to set.
	 */

	public void setNoOfHeaderRows(int noOfUserColumn) {
		this.noOfUserColumn = noOfUserColumn;
	}

	/** 
	 * @Return Failed Record coount .
	 */
	public int getFailedRecordCount() {
		return failedRecordCount;
	}

	/** 
	 * @param failedRecordCount The failed record count to set.
	 */
	public void setFailedRecordCount(int failedRecordCount) {
		this.failedRecordCount = failedRecordCount;
	}

	/** 
	 * @Return upload Record coount .
	 */
	public int getUploadRecordCount() {
		return uploadRecordCount;
	}
	/** 
	 * @set Upload Record coount .
	 */
	public void setUploadRecordCount(int uploadRecordCount) {
		this.uploadRecordCount = uploadRecordCount;
	}

	/** 
	 * @param commonHierarchyMap to set common path.
	 */
	public void setCommonHierarchyMap(HashMap commonHierarchyMap) {
		this.commonHierarchyMap = commonHierarchyMap;
	} 




	///////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////// PRIVATE METHODS//////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////

	// Validate the data coming from excel sheet of student///////////////



	/**
	 *  Validation against gender field
	 */

	private  boolean isValidGender(String str) {

		str = str.trim().toUpperCase();

		if ( ! (str.equalsIgnoreCase ( CTBConstants.MALE_CODE ) || 
				str.equalsIgnoreCase( CTBConstants.FEMALE_CODE )
				|| str.equalsIgnoreCase(CTBConstants.UNKNOWN_CODE) 
				|| str.equalsIgnoreCase(CTBConstants.MALE) 
				|| str.equalsIgnoreCase(CTBConstants.FEMALE) 
				|| str.equalsIgnoreCase(CTBConstants.UNKNOWN) ) ) {


			return false;

		}

		return true;
	}

	/**
	 * Getting gender code from gender value
	 */

	private  String getGender(String str) {

		str = str.trim().toUpperCase();

		if ( str.length()>1 ) {

			if ( str.equalsIgnoreCase(CTBConstants.MALE) )

				return CTBConstants.MALE_CODE;

			if ( str.equalsIgnoreCase(CTBConstants.FEMALE) )

				return CTBConstants.FEMALE_CODE;

			if ( str.equalsIgnoreCase( CTBConstants.UNKNOWN) )

				return CTBConstants.UNKNOWN_CODE;

		} 

		return str;

	}



	/**
	 * validate invalid /valid  Field  : for character field
	 */


	private  boolean validNameString(String str) {

		str = str.trim();
		char[] characters = str.toCharArray();

		for ( int i=0 ; i<characters.length ; i++ ) {

			char character = characters[i];

			if ( ! validNameCharacter(character) ) {

				return false;
			}
		}

		return true;
	} 

	/**
	 *  Validate the character field
	 */

	public static boolean validNameCharacter(char ch) {

		boolean A_Z = ((ch >= 65) && (ch <= 90));
		boolean a_z = ((ch >= 97) && (ch <= 122));
		boolean zero_nine = ((ch >= 48) && (ch <= 57));
		boolean validChar = ((ch == '/') || 
				(ch == '\'') || 
				(ch == '-') || 
				(ch == '_') ||
				(ch == '\\') || 
				(ch == '.') || 
				(ch == '(') || 
				(ch == ')') || 
				(ch == '&') || 
				(ch == '+') || 
				(ch == ',') || 
				(ch == ' '));

		return (zero_nine || A_Z || a_z || validChar);

	}



	/*
	 *  Validate the Email Field
	 */

	private  boolean validEmail(String email) {  

		email = email.trim();
		boolean matchFound = true;

		if ( email != null && email.length()>0 ) {

			Pattern p = 
				Pattern.compile("[a-z|A-Z|0-9|_|\\-|.]+@[a-z|A-Z|0-9|_|\\-|.]+\\.[a-z|A-Z|0-9|_|\\-|.]+");               
			//Match the given string with the pattern
			Matcher m = p.matcher(email);
			//check whether match is found 
			matchFound = m.matches();

		}      

		return matchFound;        
	}


	/*
	 *  Validate the numeric character
	 */

	private  boolean validNumber(String str) {

		str = str.trim();
		char[] characters = str.toCharArray();

		for ( int i=0 ; i < characters.length ; i++ ) {

			char character = characters[i];

			if ( !( (character >= 48) && (character <= 57) ) ) {

				return false;

			}
		} 

		return true;
	}




	/**
	 * Validate grade 
	 */

	private boolean isValidGrade (String findGrade) {

		if ( findGrade!=null && !"".equals(findGrade.trim())) {

			findGrade = findGrade.trim();

			for ( int i=0 ; i<grades.length ; i++ ) {

				String dbValue = (String) grades[i];

				if ( dbValue.equalsIgnoreCase(findGrade) ) {

					return true;

				}
			}

		}

		return false;

	}  


	/*   private boolean isValidGrade (String findGrade){

        int gradeFromDB = 0;
        int gradeFromExcel = 0;
        findGrade = findGrade.trim();

        ArrayList gradeList = new ArrayList();

        for ( int i=0 ; i < grades.length ; i++ ) {

            gradeList.add(grades[i]);
        }

        if( !isContains(gradeList, findGrade)){

            return false;
        }


        return true;
     }*/

	/*
	 * Check wheather findGrade is in gradeList
	 */ 
	private boolean isContains (List gradeList, String findGrade) {

		boolean isGradeDBString = false; 
		boolean isFindGradeString = isString(findGrade); 

		for (int i = 0; i < gradeList.size(); i++) {

			String gradeDB = (String)gradeList.get(i);

			isGradeDBString = isString(gradeDB);

			if (!isGradeDBString && !isFindGradeString) {

				if ( Integer.valueOf(gradeDB).intValue() 
						== Integer.valueOf(findGrade).intValue()) {

					return true;

				}
			}

			else {

				if (gradeDB.equalsIgnoreCase(findGrade)) {

					return true;

				} 

			}

		}

		return false;
	}

	/*
	 * Check wheather it is String value or Integer
	 */ 

	private boolean isString (String findGrade) {

		boolean isStringValue = false; 
		try {

			Integer.valueOf(findGrade).intValue();


		} catch (Exception e) {

			isStringValue = true;
		}

		return isStringValue;

	}

	/*
	 * return Uppercase of grade value if grade is AD or AE or JV or K
	 */ 

	/*     private String getGradeValue (String findGrade) {

        if( findGrade.equalsIgnoreCase(CTBConstants.GRADE_AD)
            ||  findGrade.equalsIgnoreCase(CTBConstants.GRADE_AE)
            ||  findGrade.equalsIgnoreCase(CTBConstants.GRADE_JV)
            ||  findGrade.equalsIgnoreCase(CTBConstants.GRADE_K)) {

                return findGrade.toUpperCase();

        }


        return findGrade;
     } */



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

	/**
	 *  validate color 
	 */

	private  boolean isValidColor(String asColor){

		asColor = asColor.trim().toUpperCase();

		if(!(asColor == null || "".equals(asColor.trim()))){

			if ( !colorList.contains(asColor) ) {

				return false;

			}

		}

		return true;
	}

	/**
	 * Validating combination of valid background and valid font color
	 */    

	private boolean isValidColorCombination(String asBackgroundColor, String asFontColor ){

		asBackgroundColor = asBackgroundColor.trim().toUpperCase();
		asFontColor = asFontColor.trim().toUpperCase();

		if( !(asBackgroundColor==null || "".equals(asBackgroundColor.trim()))
				&& !(asFontColor==null || "".equals(asFontColor.trim()))){

			ArrayList colorList = (ArrayList) colorCombinationMap.get(asBackgroundColor);

			if ( !colorList.contains(asFontColor) ) {

				return false;

			}

		}

		return true;
	}




	/*
	 *  Validating  the value for accommodation
	 */      

	private  boolean isValidCheckBox(String str) {

		str = str.trim().toUpperCase();

		if ( ! (str.equalsIgnoreCase(CTBConstants.T) 
				|| str.equalsIgnoreCase(CTBConstants.F)
				|| str.equalsIgnoreCase(CTBConstants.ACOMODATION_TRUE) 
				|| str.equalsIgnoreCase(CTBConstants.ACOMODATION_FALSE) 
				|| str.equalsIgnoreCase(CTBConstants.ACOMODATION_Y)
				|| str.equalsIgnoreCase(CTBConstants.ACOMODATION_N)
				|| str.equalsIgnoreCase(CTBConstants.ACOMODATION_YES)
				|| str.equalsIgnoreCase(CTBConstants.ACOMODATION_NO))) {

			return false;

		}

		return true;
	}


	/**
	 * Getting value of checkbox field as T/F
	 */

	private  String getChekBoxValue(String str) {

		str = str.trim().toUpperCase();

		if (str.equalsIgnoreCase(CTBConstants.ACOMODATION_TRUE) 
				|| str.equalsIgnoreCase(CTBConstants.T)
				|| str.equalsIgnoreCase(CTBConstants.ACOMODATION_YES)
				|| str.equalsIgnoreCase(CTBConstants.ACOMODATION_Y)){
			return CTBConstants.T;
		}
		else if (str.equalsIgnoreCase(CTBConstants.ACOMODATION_FALSE)
				|| str.equalsIgnoreCase(CTBConstants.F)
				|| str.equalsIgnoreCase(CTBConstants.ACOMODATION_NO)
				|| str.equalsIgnoreCase(CTBConstants.ACOMODATION_N)){
			return CTBConstants.F;
		}
		return CTBConstants.F;

	}






	/*
	 * Is demographic value valid or not  and set it into logical error list
	 */ 

	/*
	 * Is demographic value valid or not
	 */ 
	private boolean isLogicalError (int studentHeaderStartPosition, HSSFRow row, 
			HSSFRow rowHeader, ArrayList logicalErrorList) {

		int totalCells = rowHeader.getPhysicalNumberOfCells();  

		// retrive each cell value for user
		String msBackGroundColor="";
		String strCell = "";

		for ( int i = studentHeaderStartPosition; i < totalCells; i++ ) {

			HSSFCell cellHeader = rowHeader.getCell((short)i);
			HSSFCell cell = row.getCell((short)i);
			strCell = getCellValue(cell);

			if ( !strCell.equals("")){
				//Changes for GACRCT2010CR007.Based on the customer's configuration for Date of Birth Validation is done. 
				if(!(strCell == null ||  strCell.equals(""))){
					if ( cellHeader.getStringCellValue().
							equals(CTBConstants.REQUIREDFIELD_DATE_OF_BIRTH)
							&& isFutureDate(strCell) ) {

						logicalErrorList.add(CTBConstants.REQUIREDFIELD_DATE_OF_BIRTH);


					}
				}



				//For validating combination of question background and font color 
				else if ( cellHeader.getStringCellValue().
						equals(CTBConstants.QUESTION_BACKGROUND_COLOR) ) {

					msBackGroundColor = strCell;  

				}

				else if ( cellHeader.getStringCellValue().
						equals(CTBConstants.QUESTION_FONT_COLOR)
						&& !strCell.trim().equals("")
						&& !isValidColorCombination(msBackGroundColor,strCell)){

					logicalErrorList.add(CTBConstants.QUESTION_FONT_COLOR);
				}


				//For validating combination of answer background and font color
				else if ( cellHeader.getStringCellValue().
						equals(CTBConstants.ANSWER_BACKGROUND_COLOR) ){

					msBackGroundColor = strCell;            
				}

				else if ( cellHeader.getStringCellValue().
						equals(CTBConstants.ANSWER_FONT_COLOR)
						&& !strCell.trim().equals("")
						&& !isValidColorCombination(
								msBackGroundColor,strCell ) ) {

					logicalErrorList.add(CTBConstants.ANSWER_FONT_COLOR);

				}
			}


		}

		if ( logicalErrorList.size() == 0 ) {

			return false;

		} else {

			return true;   
		}
	}

	/**
	 * Getting customer related demographic value
	 */
	private void getValidDemographicValue(Integer customerId , HashMap demoMap){

		try{

			StudentDemoGraphics[] studentDemoGraphics = uploadDataFile.getStudentDemoGraphics(customerId);


			if ( studentDemoGraphics != null ) {

				for ( int i=0; i< studentDemoGraphics.length ; i++ ) {

					Integer customerDemographicId = studentDemoGraphics[i].getCustomerDemographicId();

					String customerDemoName = studentDemoGraphics[i].getLabelName();

					String customerDemoCardinality = studentDemoGraphics[i].getValueCardinality();

					com.ctb.bean.testAdmin.CustomerDemographicValue []customerDemographicValue = 
						uploadDataFile.getCustomerDemographicValue(customerDemographicId);

					ArrayList demographicValueList = new ArrayList();

					for ( int k=0; k<customerDemographicValue.length ;k++ ) {

						String msDemographicValue = customerDemographicValue[k].getValueName();

						demographicValueList.add(msDemographicValue);

					}

					demoMap.put(customerDemoName.toUpperCase(),demographicValueList); 
					demoGraphicMap.put(customerDemoName,customerDemographicId);
					demoCardinalityMap.put(customerDemoName,customerDemoCardinality);

				}
			}

		} catch(SQLException s){
			s.printStackTrace();
		}
	}


	/**
	 * Getting demographic value to store in db
	 */
	private String getDbDemographicValue(String fieldName, String value){

		if((value != null && !"".equals(value.trim())) ){

			fieldName = fieldName.trim().toUpperCase();

			ArrayList valueList = (ArrayList) demoMap.get(fieldName);

			if ( valueList.size()>0 ) {

				for(int i = 0 ; i < valueList.size() ; i ++) {

					String dbValue = (String) valueList.get(i);

					if( dbValue.equalsIgnoreCase(value) ){

						return dbValue; 
					}

				}

			}
		}


		return "";
	}





	/**
	 * Validate demographic values and cardinality
	 */

	private  boolean validateDemographic( String fieldName, String value ){

		String msCardinality = (String) demoCardinalityMap.get( fieldName );

		boolean checkMultipleCardinality = true;

		if( msCardinality.equals(CTBConstants.MULTIPLE_DEMOGRAPHIC)) {

			StringTokenizer stStr = new StringTokenizer 
			(value,CTBConstants.DEMOGRAPHIC_VALUSE_SEPARATOR);
			ArrayList uniqueDemoList = new ArrayList();

			while( stStr.hasMoreTokens() ){

				String msTvalue = stStr.nextToken().trim();

				//checking for duplicate demographics value
				if ( uniqueDemoList.contains(msTvalue.toUpperCase()) ) {

					return false;

				} else {
					uniqueDemoList.add( msTvalue.toUpperCase() );
				}

				checkMultipleCardinality = isValidDemographic( fieldName, msTvalue);

				if(checkMultipleCardinality == false){

					return false;
				}

			}

		} else {

			return isValidDemographic( fieldName, value);
		}

		return true;
	}


	/**
	 *  Validate the demographic value
	 */

	private boolean isValidDemographic( String fieldName, String value ){

		fieldName = fieldName.trim().toUpperCase();

		ArrayList valueList = (ArrayList) demoMap.get(fieldName);

		if ( valueList.size()>0 ) {

			for(int i = 0 ; i < valueList.size() ; i ++) {

				String dbValue = (String) valueList.get(i);

				if( dbValue.equalsIgnoreCase(value) ){

					return true; 
				}

			}

		}

		return false;
	}




	/**
	 * Validate font size 
	 */

	private boolean isValidFontSize(String value){

		value = value.trim().toUpperCase();

		if( ! ( value.equalsIgnoreCase  (CTBConstants.LARGER_FONT ) || 

				value.equalsIgnoreCase ( CTBConstants.STANDARD_FONT ) ) ) {

			return false;
		}

		return true;

	}

	/**
	 * Get the font size
	 */

	private String getFontSize(String asFont){

		asFont = asFont.trim().toUpperCase();

		if ( asFont.equalsIgnoreCase (CTBConstants.LARGER_FONT))

			return CTBConstants.LARGER_FONT_SIZE;

		else

			return CTBConstants.STANDARD_FONT_SIZE ;

	}    



	/*
	 * Initialize RoleList,TimeZoneList,StateList
	 */ 

	private void initList () {


		//List of valid color

		colorList.add(CTBConstants.WHITE);
		colorList.add(CTBConstants.BLACK);
		colorList.add(CTBConstants.LIGHT_BLUE);
		colorList.add(CTBConstants.LIGHT_PINK);
		colorList.add(CTBConstants.LIGHT_YELLOW);
		colorList.add(CTBConstants.DARK_BLUE);
		colorList.add(CTBConstants.DARK_BROWN);
		colorList.add(CTBConstants.YELLOW);
		colorList.add(CTBConstants.GREEN);


		//if background color is BLACK
		ArrayList fontBlackList = new ArrayList();
		fontBlackList.add(CTBConstants.GREEN);
		fontBlackList.add(CTBConstants.WHITE);
		fontBlackList.add(CTBConstants.YELLOW);

		//if background color is DARK BLUE		
		ArrayList fontDarkBlueList = new ArrayList();
		fontDarkBlueList.add(CTBConstants.WHITE);

		//if background color is LIGHT BLUE or LIGHT PINK or LIGHT YELLOW or WHITE 		
		ArrayList fontColorList = new ArrayList();
		fontColorList.add(CTBConstants.BLACK);
		fontColorList.add(CTBConstants.DARK_BLUE);
		fontColorList.add(CTBConstants.DARK_BROWN);

		colorCombinationMap.put(CTBConstants.BLACK, fontBlackList);
		colorCombinationMap.put(CTBConstants.DARK_BLUE, fontDarkBlueList);
		colorCombinationMap.put(CTBConstants.LIGHT_BLUE, fontColorList);
		colorCombinationMap.put(CTBConstants.LIGHT_PINK, fontColorList);
		colorCombinationMap.put(CTBConstants.LIGHT_YELLOW, fontColorList);
		colorCombinationMap.put(CTBConstants.WHITE, fontColorList);

		//Color Code
		colorCodeMap.put(CTBConstants.WHITE , CTBConstants.WHITE_CODE);
		colorCodeMap.put(CTBConstants.BLACK, CTBConstants.BLACK_CODE );
		colorCodeMap.put(CTBConstants.LIGHT_BLUE, CTBConstants.LIGHT_BLUE_CODE);
		colorCodeMap.put(CTBConstants.LIGHT_PINK, CTBConstants.LIGHT_PINK_CODE );
		colorCodeMap.put(CTBConstants.LIGHT_YELLOW, CTBConstants.LIGHT_YELLOW_CODE);
		colorCodeMap.put(CTBConstants.DARK_BLUE, CTBConstants.DARK_BLUE_CODE);
		colorCodeMap.put(CTBConstants.DARK_BROWN, CTBConstants.DARK_BROWN_CODE);
		colorCodeMap.put(CTBConstants.YELLOW, CTBConstants.YELLOW_CODE);
		colorCodeMap.put(CTBConstants.GREEN, CTBConstants.GREEN_CODE);


		try {

			com.ctb.bean.testAdmin.Customer customer = users.getCustomer(this.userName);
			this.customerId = customer.getCustomerId(); 

			//customer specefied Grade
			grades = studentManagement.getGradesForCustomer(this.userName,customerId);

			//customer specefied Demographic values
			getValidDemographicValue(customerId,demoMap);

			noOfDemographicList =  demoMap.size(); 

			//Customer specified default accommodation values

			populateDefaultAccommodationValues( customerId, 
					defaultAccommodationMap,
					editableAccomodationMap);


			isMatchUploadOrgIds = this.uploadDataFile.checkCustomerConfigurationEntries(customer.getCustomerId(),
					CTBConstants.CUSTOMER_CONF_NAME);

			//Get customer configuration Entries

			checkExternalStudent_id = this.uploadDataFile.checkCustomerConfigurationEntries(
					this.customerId,CTBConstants.MATCH_STUDENT_ID);

			checkOrgNodeCode = this.uploadDataFile.checkCustomerConfigurationEntries(
					this.customerId,CTBConstants.MATCH_ORG_CODE);
			//Changes for GACRCT2010CR007 
			//Get the customer configuration for Disable Mandatory Birth date
			checkCustomerConfiguration = this.uploadDataFile.checkCustomerConfigurationEntries(
					this.customerId,CTBConstants.DISABLE_MANDATORY_BIRTH_DATE);
			if(checkCustomerConfiguration)
			{
				String disableMandatoryBirthdateValue = this.uploadDataFile.checkCustomerConfiguration(this.customerId,CTBConstants.DISABLE_MANDATORY_BIRTH_DATE);
				if (disableMandatoryBirthdateValue.equalsIgnoreCase("T"))
				{
					disableMandatoryBirthdate = true; 
				}
			}

			//Changed 04/12/2008
			this.detailNodeM = this.uploadDataFile.
			getUserDataTemplate(this.userName);  

			//System.out.println("this.userName" + this.userName);






		} catch(SQLException se){
			se.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}



	}




	/**
	 *  set the student data from the excel list to Student
	 */


	private void setStudentPersonalData ( ManageStudent student , HashMap studentDataMap ) {


		student.setStudentIdNumber((String)studentDataMap.get(this.studentIdLabel));
		student.setFirstName(initStringCap((String)studentDataMap.get
				(CTBConstants.REQUIREDFIELD_FIRST_NAME)));
		student.setMiddleName(initStringCap((String)studentDataMap.get
				(CTBConstants.MIDDLE_NAME)));
		student.setLastName(initStringCap((String)studentDataMap.get
				(CTBConstants.REQUIREDFIELD_LAST_NAME)));

		student.setStudentIdNumber2((String)studentDataMap.get(this.studentId2Label));


		String date = (String)studentDataMap.get(CTBConstants.REQUIREDFIELD_DATE_OF_BIRTH);

		//Changes for GACRCT2010CR007.This helps in setting  null date of birth to the students.
		Date dateOfBirth = null;

		if (!(date == null ||  date.equals("")))
		{
			int month = Integer.parseInt(date.substring(0,2)) - 1;
			int day = Integer.parseInt(date.substring(3,5));
			int year = Integer.parseInt(date.substring(6,10)) - 1900;


			dateOfBirth = new Date(year, month, day );

		}
		student.setBirthDate(dateOfBirth);
		student.setGender(getGender((String)studentDataMap.get(CTBConstants.REQUIREDFIELD_GENDER)));
		student.setGrade(getGradeValue((String)studentDataMap.get(CTBConstants.REQUIREDFIELD_GRADE)));

	}


	/**
	 *  set the student Accommodation data from the excel list to StudentAccommodation
	 */


	private void setStudentAccommodationData ( StudentAccommodations studentAccommodations ,
			HashMap studentDataMap) {


		//For the accommodation of  screen reader
		if ( (studentDataMap.get(CTBConstants.SCREEN_READER).equals("")
				|| studentDataMap.get(CTBConstants.SCREEN_READER) == null )) {

			studentAccommodations.setScreenReader(getDefaultAccommodation
					(CTBConstants.ACOMOD_SCREEN_READER));
		} else  {

			studentAccommodations.setScreenReader(getAccommodationValue(
					CTBConstants.ACOMOD_SCREEN_READER,
					(String)studentDataMap.get(CTBConstants.SCREEN_READER)));  
		}


		//For the accommodation of  Calculator
		if ( ( studentDataMap.get(CTBConstants.CALCULATOR).equals("")
				|| studentDataMap.get(CTBConstants.CALCULATOR) == null )) {

			studentAccommodations.setCalculator(getDefaultAccommodation
					(CTBConstants.ACOMOD_CALCULATOR));
		} else {

			studentAccommodations.setCalculator(getAccommodationValue(
					CTBConstants.ACOMOD_CALCULATOR,
					(String)studentDataMap.get(CTBConstants.CALCULATOR)));  
		} 


		//For the accommodation of  Test Pause
		if ( ( studentDataMap.get(CTBConstants.TEST_PAUSE).equals("")
				|| studentDataMap.get(CTBConstants.TEST_PAUSE) == null)) {

			studentAccommodations.setTestPause(getDefaultAccommodation
					(CTBConstants.ACOMOD_TEST_PAUSE));

		} else {

			studentAccommodations.setTestPause(getAccommodationValue(
					CTBConstants.ACOMOD_TEST_PAUSE,
					(String)studentDataMap.get(CTBConstants.TEST_PAUSE)));
		}


		//For the accommodation of Untimed Test
		if ( (studentDataMap.get(CTBConstants.UNTIMED_TEST).equals("")
				|| studentDataMap.get(CTBConstants.UNTIMED_TEST) == null)) {

			studentAccommodations.setUntimedTest(getDefaultAccommodation
					(CTBConstants.ACOMOD_UNTIMED_TEST));

		} else {

			studentAccommodations.setUntimedTest(getAccommodationValue(
					CTBConstants.ACOMOD_UNTIMED_TEST,
					(String)studentDataMap.get(CTBConstants.UNTIMED_TEST)));  

		}


		//For the accommodation of Highlighter

		if ( (studentDataMap.get(CTBConstants.HIGHLIGHTER).equals("")
				|| studentDataMap.get(CTBConstants.HIGHLIGHTER) == null)) {

			studentAccommodations.setHighlighter(getDefaultAccommodation
					(CTBConstants.ACOMOD_HIGHLIGHTER));
		} else {

			studentAccommodations.setHighlighter(getAccommodationValue(
					CTBConstants.ACOMOD_HIGHLIGHTER,
					(String)studentDataMap.get(CTBConstants.HIGHLIGHTER)));  

		}

		boolean hasColorFont = false;

		if((studentDataMap.get(CTBConstants.QUESTION_BACKGROUND_COLOR) != null
				&& !studentDataMap.get(CTBConstants.QUESTION_BACKGROUND_COLOR).equals(""))
				|| (studentDataMap.get(CTBConstants.QUESTION_FONT_COLOR) != null 
						&& !studentDataMap.get(CTBConstants.QUESTION_FONT_COLOR).equals(""))
						|| (studentDataMap.get(CTBConstants.ANSWER_BACKGROUND_COLOR) != null
								&& !studentDataMap.get(CTBConstants.ANSWER_BACKGROUND_COLOR).equals(""))
								|| (studentDataMap.get(CTBConstants.ANSWER_FONT_COLOR) != null
										&& !studentDataMap.get(CTBConstants.ANSWER_FONT_COLOR).equals(""))
										|| (studentDataMap.get(CTBConstants.FONT_SIZE) != null
												&& !studentDataMap.get(CTBConstants.FONT_SIZE).equals(""))){

			hasColorFont = true;                 
		}

		if(hasColorFont){

			boolean hasBGColor = false;
			boolean hasFGColor = false;

			if ( !(studentDataMap.get(CTBConstants.QUESTION_BACKGROUND_COLOR).equals("") 
					|| studentDataMap.get(CTBConstants.QUESTION_BACKGROUND_COLOR)== null )) {

				hasBGColor = true;

			} 

			if ( !(studentDataMap.get(CTBConstants.QUESTION_FONT_COLOR).equals("") 
					|| studentDataMap.get(CTBConstants.QUESTION_FONT_COLOR)== null )) {

				hasFGColor = true;

			}

			if ( !hasBGColor && !hasFGColor ) {

				studentAccommodations.setQuestionBackgroundColor
				(CTBConstants.WHITE_CODE);

				studentAccommodations.setQuestionFontColor
				(CTBConstants.BLACK_CODE);


			} else if ( hasBGColor && !hasFGColor ) {

				String fontColorCode = getFontColor((String)studentDataMap.
						get(CTBConstants.QUESTION_BACKGROUND_COLOR));
				String bgColorCode = getColorCode((String)studentDataMap.
						get(CTBConstants.QUESTION_BACKGROUND_COLOR));

				studentAccommodations.setQuestionBackgroundColor(bgColorCode);    
				studentAccommodations.setQuestionFontColor(fontColorCode);                        


			} else if ( !hasBGColor && hasFGColor ) {

				String bgColorCode = getBGColor((String)studentDataMap.
						get(CTBConstants.QUESTION_FONT_COLOR));

				String fontColorCode =  getColorCode((String)studentDataMap.
						get(CTBConstants.QUESTION_FONT_COLOR)); 

				studentAccommodations.setQuestionBackgroundColor(bgColorCode);    
				studentAccommodations.setQuestionFontColor(fontColorCode);                          


			} else if ( hasBGColor && hasFGColor ) {

				studentAccommodations.setQuestionBackgroundColor(
						getColorCode((String)studentDataMap.get(
								CTBConstants.QUESTION_BACKGROUND_COLOR)));

				studentAccommodations.setQuestionFontColor(
						getColorCode((String)studentDataMap.get(
								CTBConstants.QUESTION_FONT_COLOR)));

			}

			if ( studentDataMap.get(CTBConstants.FONT_SIZE).equals("") 
					|| studentDataMap.get(CTBConstants.FONT_SIZE)== null ) {

				studentAccommodations.setQuestionFontSize(CTBConstants.STANDARD_FONT_SIZE);

			} else {

				studentAccommodations.setQuestionFontSize(
						getFontSize(
								(String)studentDataMap.get(CTBConstants.FONT_SIZE)));
			}

			hasBGColor = false;
			hasFGColor = false;    

			if ( !(studentDataMap.get(CTBConstants.ANSWER_BACKGROUND_COLOR).equals("") 
					|| studentDataMap.get(CTBConstants.ANSWER_BACKGROUND_COLOR)== null)) {

				hasBGColor = true;

			}                                         

			if (!( studentDataMap.get(CTBConstants.ANSWER_FONT_COLOR).equals("") 
					|| studentDataMap.get(CTBConstants.ANSWER_FONT_COLOR)== null)) {

				hasFGColor = true;

			}                                       

			if ( !hasBGColor && !hasFGColor ) {

				studentAccommodations.setAnswerBackgroundColor
				(CTBConstants.LIGHT_YELLOW_CODE);

				studentAccommodations.setAnswerFontColor
				(CTBConstants.BLACK_CODE);


			} else if ( hasBGColor && !hasFGColor ) {

				String fontColorCode = getFontColor((String)studentDataMap.
						get(CTBConstants.ANSWER_BACKGROUND_COLOR));
				String bgColorCode = getColorCode((String)studentDataMap.
						get(CTBConstants.ANSWER_BACKGROUND_COLOR));

				studentAccommodations.setAnswerBackgroundColor(bgColorCode);    
				studentAccommodations.setAnswerFontColor(fontColorCode);                        


			} else if ( !hasBGColor && hasFGColor ) {

				String bgColorCode = getBGColor((String)studentDataMap.
						get(CTBConstants.ANSWER_FONT_COLOR));

				String fontColorCode =  getColorCode((String)studentDataMap.
						get(CTBConstants.ANSWER_FONT_COLOR)); 

				studentAccommodations.setAnswerBackgroundColor(bgColorCode);    
				studentAccommodations.setAnswerFontColor(fontColorCode);                          


			} else if ( hasBGColor && hasFGColor ) {

				studentAccommodations.setAnswerBackgroundColor(
						getColorCode((String)studentDataMap.get(
								CTBConstants.ANSWER_BACKGROUND_COLOR)));

				studentAccommodations.setAnswerFontColor(
						getColorCode((String)studentDataMap.get(
								CTBConstants.ANSWER_FONT_COLOR)));

			}


			if ( studentDataMap.get(CTBConstants.FONT_SIZE).equals("") 
					|| studentDataMap.get(CTBConstants.FONT_SIZE)== null ) {

				studentAccommodations.setAnswerFontSize(CTBConstants.STANDARD_FONT_SIZE);

			} else {

				studentAccommodations.setAnswerFontSize(
						getFontSize(
								(String)studentDataMap.get(CTBConstants.FONT_SIZE)));
			}

		}

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


	/*
	 * First check the existence of student, if not exists then create
	 */ 

	private void createStudent(HashMap studentDataMap,
			int orgHeaderLastPosition,Node[] studentNode,
			HashMap studentDemoMap,
			ArrayList demolist)  throws CTBBusinessException {


		Student student = new Student();

		boolean isNewStudent = true;
		boolean isNewOrgAssigned = true;
		boolean isNewStudentExtId = true;
		//Changes for GA2011CR003 OAS Student Bulk Upload using Unique Student ID
		boolean matchWithOtherCriteria = false; 


		// Set into student Demographic

		StudentDemographic[] studentDemographic =
			this.getStudentDemographicData(studentDemoMap,demolist);

		//Accomodation 
		StudentAccommodations studentAccommodations =
			new StudentAccommodations();                                                                                   

		//studentDataMap.get(
		ManageStudent manageStudent = new ManageStudent();

		// Set the student personal data
		setStudentPersonalData ( manageStudent , studentDataMap ) ;
		// Assign the node to student here
		Integer  leafNode = studentNode[0].getOrgNodeId();

		// set organizationNodes of Student

		// set Organization Node
		OrganizationNode organizationNode = new OrganizationNode();
		organizationNode.setOrgNodeId(studentNode[0].getOrgNodeId());
		OrganizationNode[] studentOrgNode = new OrganizationNode[1];
		studentOrgNode[0] = organizationNode;

		// check the external Student ID
		try {

			// Get the customer configuration entries for student external id check.

			//Changed 04/12/2008
			//commented for performance tuning on 10-dec-2008
			//StudentFileRow[] studentFileRow = this.uploadDataFile.getExistStudentData(this.userName);
			//StudentFileRow[] studentFileRow = this.uploadDataFile.getStudentData(this.userName);

			StudentFileRow[] studentFileRow = (StudentFileRow[])(new ArrayList(this.visibleStudent.values())).
			toArray(new StudentFileRow[0]);
			if ( checkExternalStudent_id ) {
				// Check existence of external  student Id
				// Change for GA2011CR003 OAS Student Bulk Upload using Unique Student ID
				if (manageStudent.getStudentIdNumber()!= null && !"".equals(manageStudent.getStudentIdNumber())) {

					for ( int i = 0 ; i < studentFileRow.length ; i++ ) {

						if ( studentFileRow[i].getExtPin1() != null && !"".equals(studentFileRow[i].getExtPin1())) {
							if ( studentFileRow[i].getExtPin1().
									equalsIgnoreCase(manageStudent.getStudentIdNumber()) ){

								//  isNewStudentExtId = false; 
								matchWithOtherCriteria = false;
								isNewStudent = false;    
								manageStudent.setLoginId(studentFileRow[i].getUserName());
								manageStudent.setId(studentFileRow[i].getStudentId());

								break;
							}
						}

					}


				} else { // start of change; GA2011CR003 OAS Student Bulk Upload using Unique Student ID

					matchWithOtherCriteria = true;
				}

				if ( matchWithOtherCriteria && manageStudent.getStudentIdNumber() != null && !"".equals(manageStudent.getStudentIdNumber())) {

					matchWithOtherCriteria = false;
					isNewStudent = true;
				}

				if ( matchWithOtherCriteria ) { //End  of change; GA2011CR003 OAS Student Bulk Upload using Unique Student ID

					StudentFileRow studentFile = isStudentExists(manageStudent,studentFileRow);

					if ( studentFile != null ) {

						isNewStudent = false;
						manageStudent.setLoginId(studentFile.getUserName());
						manageStudent.setId(studentFile.getStudentId());

					}

				}

			}	  else {

				StudentFileRow studentFile = isStudentExists(manageStudent,studentFileRow);

				if ( studentFile != null ) {

					isNewStudent = false;
					manageStudent.setLoginId(studentFile.getUserName());
					manageStudent.setId(studentFile.getStudentId());


				}
			}


			if ( isNewStudent ) {


				manageStudent.setOrganizationNodes(studentOrgNode);

				// set into student Accommodation data

				setStudentAccommodationData ( studentAccommodations , studentDataMap );

				createNewStudent(manageStudent,studentAccommodations,studentDemographic,studentNode);


			} else {

				// Update student Record


				Integer studentId = manageStudent.getId();  
				//ManageStudent manageStudentData = this.studentManagement.getManageStudent(this.userName,studentId);
				//change on 10-Dec-2008 for performance tuning
				OrganizationNode[] studentOrgNodes = this.studentManagement.getStudentsOrganizationUpload (this.userName, studentId);
				/*Node []orgNode = null;
                 if (this.visibleStudent.containsKey(studentId)) {

                    orgNode = ((StudentFileRow)this.visibleStudent.
                                            get(studentId)).getOrganizationNodes();

                 }

                 studentOrgNodes = new OrganizationNode[orgNode.length];

                 copyOrgNode(orgNode, studentOrgNodes);*/

				if ( !isOrganizationPresent(studentOrgNode[0].getOrgNodeId(), studentOrgNodes ) ) {

					int size = studentOrgNodes.length;
					OrganizationNode []updateNodes = new OrganizationNode[size + 1];

					for (int i = 0; i < studentOrgNodes.length; i++) {

						updateNodes[i] = studentOrgNodes[i];
					}

					updateNodes[size] = studentOrgNode[0];
					manageStudent.setOrganizationNodes(updateNodes);

				} else {

					manageStudent.setOrganizationNodes(studentOrgNodes);

				}

				//set into student Accommodation data at the time update

				setStudentAccommodationData ( studentAccommodations , studentDataMap);

				// call the update method

				updateStudent(manageStudent,studentAccommodations,studentDemographic);

			}



		} catch ( Exception e) {

			e.printStackTrace();
		} 


	} 

	/*
	 * copy Node[] to OrganizationNode[]
	 */

	private void copyOrgNode (Node []orgNode, OrganizationNode []studentOrgNodes) {

		for (int i = 0; i < orgNode.length; i++) {

			studentOrgNodes[i].setOrgNodeId(orgNode[i].getOrgNodeId());
			studentOrgNodes[i].setOrgNodeName(orgNode[i].getOrgNodeName());
			studentOrgNodes[i].setOrgNodeCode(orgNode[i].getOrgNodeCode());
			studentOrgNodes[i].setOrgNodeCategoryName(orgNode[i].getOrgNodeCategoryName());
			studentOrgNodes[i].setOrgNodeCategoryId(orgNode[i].getOrgNodeCategoryId());
			studentOrgNodes[i].setParentOrgNodeId(orgNode[i].getParentOrgNodeId());

		}

	}

	/**
	 *  Get student Demographic Data 
	 */
	private StudentDemographic [] getStudentDemographicData(
			HashMap studentDemoMap,
			ArrayList demoList) {

		StudentDemographic [] studentDemographics 
		= new StudentDemographic[studentDemoMap.size()];
		StudentDemographicValue [] studentDemographicValues = null;
		StudentDemographicValue studentDemographicValue = null;

		for ( int i= 0 ; i < demoList.size() ; i++ ) {

			StudentDemographic studentDemographic 
			= new StudentDemographic();

			if ( demoGraphicMap.containsKey(demoList.get(i)) )  {

				String demoName = (String)demoList.get(i) ;
				Integer demoGraphicId 
				= (Integer)demoGraphicMap.get(demoName);
				String demoLabelName 
				= (String) studentDemoMap.get(demoName);

				String demoCardinality 
				= (String) demoCardinalityMap.get(demoName);

				if(demoCardinality.equals(CTBConstants.MULTIPLE_DEMOGRAPHIC)){
					StringTokenizer stStr = new StringTokenizer(demoLabelName,
							CTBConstants.DEMOGRAPHIC_VALUSE_SEPARATOR);
					int j=0;
					studentDemographicValues 
					= new StudentDemographicValue[stStr.countTokens()]; 
					while(stStr.hasMoreTokens()){

						String demoVal = stStr.nextToken().trim();
						String demoValue = 
							getDbDemographicValue(demoName ,demoVal);


						studentDemographicValue = new StudentDemographicValue();
						studentDemographicValue.setValueName(demoValue);
						studentDemographicValue.setVisible("T");
						studentDemographicValue.setSelectedFlag("true");

						studentDemographicValues[j++] = studentDemographicValue;
						studentDemographic.setId(demoGraphicId);
						studentDemographic.setStudentDemographicValues(studentDemographicValues);
						studentDemographics[i]= studentDemographic;

					}

				} else {

					studentDemographicValue = new StudentDemographicValue();
					demoLabelName = getDbDemographicValue(demoName ,demoLabelName); 
					studentDemographicValue.setValueName(demoLabelName);
					studentDemographicValue.setVisible("T");
					studentDemographicValue.setSelectedFlag("true");
					studentDemographicValues = new StudentDemographicValue[1];     
					studentDemographicValues[0] = studentDemographicValue;
					studentDemographic.setId(demoGraphicId);
					studentDemographic.setStudentDemographicValues(studentDemographicValues);
					studentDemographic.setId(demoGraphicId);
					studentDemographics[i]= studentDemographic;

				}

			}



		}

		return studentDemographics;
	}

	/**
	 *  Check whether student exists or not. If existes then return student id
	 */
	private StudentFileRow isStudentExists( ManageStudent student , StudentFileRow[] studentFileRow ) {

		if ( studentFileRow.length!=0 && studentFileRow!=null ) {

			for ( int i = 0 ; i < studentFileRow.length ; i++ ) {

				// check by first name and last name
				if ( studentFileRow[i].getFirstName().equalsIgnoreCase(student.getFirstName())
						&& studentFileRow[i].getLastName().equalsIgnoreCase(student.getLastName()) ) {

					// check by middle  name 

					boolean middleName = false;
					/* 
                        if ( studentFileRow[i].getMiddleName() != null ) {


                                middleName = studentFileRow[i].getMiddleName().equalsIgnoreCase(student.getMiddleName());
                                middleName = true;

                        }  else {

                             if (  student.getMiddleName().equals("") ||  !(student.getMiddleName()!= null) ) {

                                  middleName = true;

                             }
                        }
					 */

					if (student.getMiddleName() != null && !student.getMiddleName().trim().equals("")) {

						if ( studentFileRow[i].getMiddleName() !=null
								&& studentFileRow[i].getMiddleName().
								equalsIgnoreCase(student.getMiddleName())) {
							// Update Student
							middleName = true;

						} else {

							middleName = false;

						}

					} else {

						if ( studentFileRow[i].getMiddleName() == null  
								|| studentFileRow[i].getMiddleName().trim().equals("")) {

							middleName = true;

						} else {

							middleName = false;

						}

					}

					// check by gender

					boolean gender = false;

					if ( studentFileRow[i].getGender() != null ) {

						gender = studentFileRow[i].getGender().equals(student.getGender());

					}


					// check by date of birth  name 

					boolean dateOfBirth = false;
					Date studentBday = student.getBirthDate();
					Date dbDate = studentFileRow[i].getBirthdate();

					//Changes for GACRCT2010CR007 . 

					if (studentBday!= null && !studentBday.equals("")) {

						if ( dbDate != null && !dbDate.equals("")) {
							// Update Student
							SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
							String datefromDB   = sdf.format(dbDate);                           
							String studentBDate = sdf.format(studentBday);
							dateOfBirth = datefromDB.equals(studentBDate);

						} else {

							dateOfBirth = false;

						}

					} else {

						if ( dbDate == null  
								|| dbDate.equals("")) {

							dateOfBirth = true;

						} else {

							dateOfBirth = false;

						}

					}

					/* if (!(studentBday == null ||  studentBday.equals("")))
                        { 
	                        if ( studentFileRow[i].getBirthdate() != null ) {

	                            //Date dbDate = studentFileRow[i].getBirthdate();
	                            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	                            String datefromDB   = sdf.format(dbDate);                           
	                            String studentBDate = sdf.format(studentBday);
	                            dateOfBirth = datefromDB.equals(studentBDate);

	                        }
                        }*/

					// check middle name,gender and data of birth is true. If true then 
					// return student Id

					if (  middleName &&  gender && dateOfBirth ) {

						return studentFileRow[i];
					}

				}

			}
		}

		return null;

	}


	/**
	 * Check the node is leaf node or not
	 */
	private boolean isLeafNode(Node[] userNode, Node[] studentNode ){


		for ( int i = 0 ; i <  userNode.length ; i++ ) {

			if ( userNode[i].getParentOrgNodeId().intValue() == studentNode[0].getOrgNodeId().intValue() ) {


				return false;

			}
		}


		return true;


	}

	/**
	 *  Update student,update student accommodation and demographic details
	 */
	private void  updateStudent (ManageStudent manageStudent,StudentAccommodations studentAccommodations,StudentDemographic[] studentDemographic) throws CTBBusinessException {


		studentAccommodations.setStudentId(manageStudent.getId());
		this.studentManagement.updateStudentUpload(this.loginUser, manageStudent);
		this.studentManagement.updateStudentAccommodations(this.userName,studentAccommodations);
		this.studentManagement.updateStudentDemographicsUpload(this.loginUser,manageStudent.getId(),studentDemographic);
		StudentFileRow studentFileRow = new StudentFileRow();
		copyStudentDetail (studentFileRow, manageStudent, studentAccommodations); 
		this.visibleStudent.put(studentFileRow.getStudentId(), studentFileRow);           
	}

	/**
	 *  Update student,update student accommodation and demographic details
	 */
	private void createNewStudent(ManageStudent manageStudent,StudentAccommodations studentAccommodations,StudentDemographic[] studentDemographic,Node[] studentNode) throws CTBBusinessException {


		Student student = this.studentManagement.createStudentUpload(this.loginUser, manageStudent);
		studentAccommodations.setStudentId(student.getStudentId());
		this.studentManagement.createStudentAccommodations(this.userName,studentAccommodations);
		this.studentManagement.createStudentDemographicsUpload(this.loginUser,
				student.getStudentId(),studentDemographic);

		StudentFileRow studentFileRow = new StudentFileRow();
		copyStudentDetail (studentFileRow, student, studentAccommodations);
		this.visibleStudent.put(student.getStudentId(), studentFileRow);

	}

	/*
	 * Copy StudentDetails and Accomodations in to StudentFileRow Object for create
	 */ 

	private void copyStudentDetail (StudentFileRow studentFileRow, Student student, 
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

		//Accomodation
		/*   studentFileRow.setScreenReader(studentAccommodations.getScreenReader());
        studentFileRow.setCalculator(studentAccommodations.getCalculator());
        studentFileRow.setTestPause(studentAccommodations.getTestPause());
        studentFileRow.setUntimedTest(studentAccommodations.getUntimedTest());
        studentFileRow.setHighlighter(studentAccommodations.getHighlighter());
        studentFileRow.setQuestionBackgroundColor(studentAccommodations.getQuestionBackgroundColor());
        studentFileRow.setQuestionFontColor(studentAccommodations.getQuestionFontColor());
        studentFileRow.setAnswerBackgroundColor(studentAccommodations.getAnswerBackgroundColor());
        studentFileRow.setAnswerFontColor(studentAccommodations.getAnswerFontColor());
        studentFileRow.setAnswerFontSize(studentAccommodations.getAnswerFontSize());
		 */  

	}

	/*
	 * Copy StudentDetails and Accomodations in to StudentFileRow Object for update
	 */ 

	private void copyStudentDetail (StudentFileRow studentFileRow, ManageStudent manageStudent, 
			StudentAccommodations studentAccommodations) {

		studentFileRow.setFirstName(manageStudent.getFirstName());
		studentFileRow.setLastName(manageStudent.getLastName());
		studentFileRow.setMiddleName(manageStudent.getMiddleName());
		studentFileRow.setGender(manageStudent.getGender());
		studentFileRow.setBirthdate(manageStudent.getBirthDate());
		studentFileRow.setGrade(manageStudent.getGrade());
		studentFileRow.setOrganizationNodes(manageStudent.getOrganizationNodes());
		studentFileRow.setExtPin1(manageStudent.getStudentIdNumber());
		studentFileRow.setExtPin2(manageStudent.getStudentIdNumber2());
		studentFileRow.setStudentId(manageStudent.getId());
		studentFileRow.setUserName(manageStudent.getLoginId());

		//Accomodation
		studentFileRow.setScreenReader(studentAccommodations.getScreenReader());
		studentFileRow.setCalculator(studentAccommodations.getCalculator());
		studentFileRow.setTestPause(studentAccommodations.getTestPause());
		studentFileRow.setUntimedTest(studentAccommodations.getUntimedTest());
		studentFileRow.setHighlighter(studentAccommodations.getHighlighter());
		studentFileRow.setQuestionBackgroundColor(studentAccommodations.getQuestionBackgroundColor());
		studentFileRow.setQuestionFontColor(studentAccommodations.getQuestionFontColor());
		studentFileRow.setAnswerBackgroundColor(studentAccommodations.getAnswerBackgroundColor());
		studentFileRow.setAnswerFontColor(studentAccommodations.getAnswerFontColor());
		studentFileRow.setAnswerFontSize(studentAccommodations.getAnswerFontSize());


	}

	/*
	 * Error Excel Creation
	 */ 
	// Updated for GACR005
	private void errorExcelCreation (HashMap requiredMap,  
			HashMap maxLengthMap, HashMap invalidCharMap, 
			HashMap logicalErrorMap, HashMap hierarchyErrorMap, HashMap leafNodeErrorMap,HashMap minLengthMap ) {

		//POI details Initialize
		POIFSFileSystem pfs = null;
		HSSFSheet sheet = null; 
		int errorCount = 0;
		byte[] errorData  = null;      
		boolean isBlankRow = true;   
		String strUploadCell = "";   
		String rowHeaderCellValue = "";
		try {

			//Error Excel file create Object Initializa
			HSSFWorkbook ewb = new HSSFWorkbook ();
			HSSFSheet esheet = ewb.createSheet("ErrorSheet");
			HSSFRow erowHeader = esheet.createRow(0);
			HSSFCellStyle style = null;

			HSSFCellStyle requiredStyle = ewb.createCellStyle();
			HSSFCellStyle invalidStyle = ewb.createCellStyle();
			HSSFCellStyle maxlengthStyle = ewb.createCellStyle();
			HSSFCellStyle logicalErrorStyle = ewb.createCellStyle();
			HSSFCellStyle minlengthStyle = ewb.createCellStyle();
			

			//set required field color
			requiredStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
			requiredStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			requiredStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));

			//set invalid field color
			invalidStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
			invalidStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			invalidStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
			
			//set minlength field color
			minlengthStyle.setFillForegroundColor(HSSFColor.ROSE.index);
			minlengthStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			minlengthStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));

			//set maxlength field color
			maxlengthStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
			maxlengthStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			maxlengthStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
			
			

			//set logical field color
			logicalErrorStyle.setFillForegroundColor(HSSFColor.LIGHT_ORANGE.index);
			logicalErrorStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			logicalErrorStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));

			//Upload Excel read object initialize 


			pfs = new POIFSFileSystem( new FileInputStream(this.serverFilePath) );
			HSSFWorkbook wb = new HSSFWorkbook(pfs);
			sheet = wb.getSheetAt(0);
			int totalRows = 0;

			if ( sheet != null ) {

				totalRows =  sheet.getPhysicalNumberOfRows();

			}

			HSSFRow rowHeader = sheet.getRow(0);

			// Excel Header Creation
			for ( int i = 0; i < rowHeader.getPhysicalNumberOfCells(); i++ ) {

				HSSFCell cell = erowHeader.createCell((short)i);
				style = cell.getCellStyle();
				style.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
				esheet.setDefaultColumnStyle((short)i,style);                      

				cell.setCellValue(getCellValue(rowHeader.getCell((short)i)));

			}

			// Excel Body creation
			for ( int i = 1; i < totalRows; i++ ) {

				HSSFRow uploadRow = sheet.getRow((short)i);    
				if (uploadRow == null) {

					totalRows++;
					continue;

				} else {


					int totalCells = rowHeader.getPhysicalNumberOfCells();         
					// retrive each cell value for user
					for (int k = 0; k < totalCells; k++) {

						//   HSSFCell cellHeader = rowHeader.getCell((short)k);
						HSSFCell cell = uploadRow.getCell((short)k);

						if ( cell != null && (!getCellValue(cell).trim().equals("") 
								&& !(cell.getCellType() == 3)) ) {

							isBlankRow = false;                  

						} 
					}

					if (isBlankRow) {

						continue;

					}

				}  

				if ((requiredMap.containsKey(new Integer(i)) 
						|| invalidCharMap.containsKey(new Integer(i))
						|| minLengthMap.containsKey(new Integer(i))
						|| maxLengthMap.containsKey(new Integer(i)) 
						|| logicalErrorMap.containsKey(new Integer(i))
						|| hierarchyErrorMap.containsKey(new Integer(i))
						|| leafNodeErrorMap.containsKey(new Integer(i)))) {

					errorCount++;    



					HSSFRow errorRow = esheet.createRow((short)errorCount);
					for ( int j = 0; j < rowHeader.getPhysicalNumberOfCells(); j++ ) {

						HSSFCell errorCell = errorRow.createCell((short)j);
						HSSFCell uploadCell = uploadRow.getCell((short)j);

						strUploadCell = getCellValue(uploadCell);
						rowHeaderCellValue =  getCellValue
						(rowHeader.getCell((short)j));

						//checking for required field
						if ( requiredMap.size() > 0 ) {

							if ( requiredMap.containsKey(new Integer(i)) ) {

								ArrayList requiredList = (ArrayList)requiredMap.get(new Integer(i));

								if ( requiredList.contains(rowHeaderCellValue) ) {


									if ( !strUploadCell.equals("") ) {

										errorCell.setCellValue(strUploadCell);
										errorCell.setCellStyle(requiredStyle);

									} else {

										errorCell.setCellStyle(requiredStyle); 

									}

								}    

							}

						}

						//checking for invalid field
						if ( invalidCharMap.size() > 0 ) {

							if ( invalidCharMap.containsKey( new Integer(i) ) ) {

								ArrayList invalidCharList = (ArrayList)invalidCharMap.get(new Integer(i));

								if ( invalidCharList.contains(rowHeaderCellValue)) {

									if ( !strUploadCell.equals("") ) {

										errorCell.setCellValue(strUploadCell);
										errorCell.setCellStyle(invalidStyle);         

									} else {

										errorCell.setCellStyle(invalidStyle); 

									}

								}

							}

						}
						// Updated for GACR005
						//checking for minlength field
						if (minLengthMap.size() > 0) {

							if ( minLengthMap.containsKey(new Integer(i)) ) {

								ArrayList minlengthList = (ArrayList)minLengthMap.get(new Integer(i));

								if ( minlengthList.contains(rowHeaderCellValue) ) {

									if ( !strUploadCell.equals("") ) {

										errorCell.setCellValue(strUploadCell);
										errorCell.setCellStyle(minlengthStyle);      

									} else {

										errorCell.setCellStyle(minlengthStyle); 

									}

								}    

							}

						}

						//checking for maxlength field
						if (maxLengthMap.size() > 0) {

							if ( maxLengthMap.containsKey(new Integer(i)) ) {

								ArrayList maxlengthList = (ArrayList)maxLengthMap.get(new Integer(i));

								if ( maxlengthList.contains(rowHeaderCellValue) ) {

									if ( !strUploadCell.equals("") ) {

										errorCell.setCellValue(strUploadCell);
										errorCell.setCellStyle(maxlengthStyle);      

									} else {

										errorCell.setCellStyle(maxlengthStyle); 

									}

								}    

							}

						}

						//checking for logical error field 
						if ( logicalErrorMap.size() > 0 ) {

							if ( logicalErrorMap.containsKey(new Integer(i)) ) {

								ArrayList logicalErrorList = (ArrayList)logicalErrorMap.get(new Integer(i));

								if ( logicalErrorList.contains(rowHeaderCellValue) ) {

									if ( !strUploadCell.equals("") ) {

										errorCell.setCellValue(strUploadCell);
										errorCell.setCellStyle(logicalErrorStyle);     

									} else {

										errorCell.setCellStyle(logicalErrorStyle);

									}

								}

							}

						}

						//checking for logical error field 
						if ( hierarchyErrorMap.size() > 0 ) {

							if ( hierarchyErrorMap.containsKey(new Integer(i)) ) {

								ArrayList commonPathErrorList = (ArrayList)hierarchyErrorMap.get(new Integer(i));

								if ( commonPathErrorList.contains(rowHeaderCellValue) ) {

									if ( !strUploadCell.equals("") ) {

										errorCell.setCellValue(strUploadCell);
										errorCell.setCellStyle(logicalErrorStyle);    

									} else {

										errorCell.setCellStyle(logicalErrorStyle);

									}

								}    

							}

						}

						// checking leafNode Validation

						if (leafNodeErrorMap.size() > 0 ) {

							if (leafNodeErrorMap.containsKey(new Integer(i))) {

								String errorLeafNode = (String)leafNodeErrorMap.get(new Integer(i));

								if (errorLeafNode.equals(rowHeaderCellValue)) {

									if ( !strUploadCell.equals("") ) {

										errorCell.setCellValue(strUploadCell);
										errorCell.setCellStyle(logicalErrorStyle);    

									} else {

										errorCell.setCellStyle(logicalErrorStyle);

									}

								}
							}


						}

						if ( !strUploadCell.equals("") ) {

							errorCell.setCellValue(strUploadCell);

						}

					}

				}

				isBlankRow = true;

			}

			String uploadFileName = this.dataFileAudit.getUploadFileName();
			uploadFileName = uploadFileName.substring(0, uploadFileName.length()-4 );

			// Write to excel file

			String errorFileName = CTBConstants.SERVER_FOLDER_NAME + "/" + uploadFileName + "_" + "Error" + ".xls";
			FileOutputStream mfileOut = new FileOutputStream(errorFileName);
			ewb.write(mfileOut);
			mfileOut.close();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();             

			InputStream in = new FileInputStream(errorFileName);

			boolean moreData = true;

			while ( moreData ) {

				byte [] buffer = new byte[1024];
				int read = in.read(buffer);
				moreData = read > 0;

				if ( moreData ) {

					baos.write(buffer, 0, read);

				}
			}

			errorData  = baos.toByteArray(); 

			dataFileAudit.setFaildRec(errorData);
			//dataFileAudit.setStatus("FL");
			dataFileAudit.setFailedRecordCount(new Integer(errorCount));
			dataFileAudit.setUploadFileRecordCount(new Integer(0));
			uploadDataFile.upDateAuditTable(dataFileAudit);

			mfileOut.close();
			baos.flush();
			baos.close();


		} catch (Exception e) {

			dataFileAudit.setFaildRec(errorData);
			dataFileAudit.setStatus("FL");
			dataFileAudit.setFailedRecordCount(new Integer(errorCount));
			dataFileAudit.setUploadFileRecordCount(new Integer(0));
			try{
				uploadDataFile.upDateAuditTable(dataFileAudit);
			} catch (SQLException se) {
				se.printStackTrace();
			}

			e.printStackTrace();

		}     

	} 


	/*
	 * retrive student detail for each row and validate the data 
	 *  Updated for GACR005
	 */ 
	private void getEachRowStudentDetail(int rowPosition,HSSFRow row, HSSFRow rowHeader, 
			HashMap requiredMap, HashMap maxLengthMap, 
			HashMap invalidCharMap, HashMap logicalErrorMap,HashMap minLengthMap) throws Exception {

		//Initialize reruired,invalid,maxlength and logical error arraylist
		ArrayList requiredList = new ArrayList();
		ArrayList invalidList = new ArrayList();
		ArrayList maxLengthList = new ArrayList();
		ArrayList logicalErrorList = new ArrayList ();
		ArrayList minLengthList = new ArrayList();

		// retrive header category Array
		Node []node = this.studentFileRowHeader[0].getOrganizationNodes();

		int studentHeaderStartPosition = node.length * 2;

		// checking for required field,invalid charecter,maxlength,logical error
		if ( isRequired (studentHeaderStartPosition, row, rowHeader, requiredList) ) {

			requiredMap.put(new Integer(rowPosition), requiredList);


		} else if ( isInvalidChar(studentHeaderStartPosition, row, rowHeader, invalidList) ) {

			invalidCharMap.put(new Integer(rowPosition), invalidList);


		} else if ( isMinlength (studentHeaderStartPosition, row, rowHeader, minLengthList) ) {

			minLengthMap.put(new Integer (rowPosition), minLengthList);


		} else if ( isMaxlengthExceed (studentHeaderStartPosition, row, rowHeader, maxLengthList) ) {

			maxLengthMap.put(new Integer (rowPosition), maxLengthList);


		} else if ( isLogicalError(studentHeaderStartPosition, row, rowHeader, logicalErrorList) ) {

			logicalErrorMap.put(new Integer (rowPosition), logicalErrorList);


		}

	}

	/*
	 * Check for required field for each row
	 */ 
	private boolean isRequired (int studentHeaderStartPosition, HSSFRow row, 
			HSSFRow rowHeader, ArrayList requiredList) {

		int totalCells = rowHeader.getPhysicalNumberOfCells(); 
		String strCell = "";        
		// retrive each cell value for user
		for (int i = studentHeaderStartPosition; i < totalCells; i++) {

			HSSFCell cellHeader = rowHeader.getCell((short)i);
			HSSFCell cell = row.getCell((short)i);
			strCell = getCellValue(cell);
			//Required field checking
			if ( strCell.equals("") || cell.getCellType() == 3 || (cell.getCellType() == 1 
					&& cell.getStringCellValue().trim().equals(""))) {

				if ( cellHeader.getStringCellValue().
						equals(CTBConstants.REQUIREDFIELD_FIRST_NAME) ) {

					requiredList.add(CTBConstants.REQUIREDFIELD_FIRST_NAME);    
				} 

				/*    }

        //    if ( strCell.equals("") || cell.getCellType() == 3 || (cell.getCellType() == 1 
                    && cell.getStringCellValue().trim().equals(""))) {
				 */         
				else if ( cellHeader.getStringCellValue().
						equals(CTBConstants.REQUIREDFIELD_LAST_NAME) ) {

					requiredList.add(CTBConstants.REQUIREDFIELD_LAST_NAME);   
				} 


				/*    }


            if ( strCell.equals("") || cell.getCellType() == 3 || (cell.getCellType() == 1 
                    && cell.getStringCellValue().trim().equals(""))) {
				 */        
				//Changes for GACRCT2010CR007 .  



				else if ( cellHeader.getStringCellValue().
						equals(CTBConstants.REQUIREDFIELD_DATE_OF_BIRTH) ) {
					if(!disableMandatoryBirthdate){    
						requiredList.add(CTBConstants.REQUIREDFIELD_DATE_OF_BIRTH);   
					}      
				}

				/*      }

            if ( strCell.equals("") || cell.getCellType() == 3 || (cell.getCellType() == 1 
                    && cell.getStringCellValue().trim().equals(""))) {
				 */           
				else if ( cellHeader.getStringCellValue().
						equals(CTBConstants.REQUIREDFIELD_GRADE) ) {

					requiredList.add(CTBConstants.REQUIREDFIELD_GRADE);   
				} 


				/*      }

            if ( strCell.equals("") || cell.getCellType() == 3 || (cell.getCellType() == 1 
                    && cell.getStringCellValue().trim().equals(""))) {
				 */          
				else if ( cellHeader.getStringCellValue().
						equals(CTBConstants.REQUIREDFIELD_GENDER) ) {

					requiredList.add(CTBConstants.REQUIREDFIELD_GENDER);   
				}


				//Changes for GA2011CR001

				else if( this.isStudentIdMandatory){

					if ( cellHeader.getStringCellValue().
							equals(this.studentIdLabel) ) {

						requiredList.add(this.studentIdLabel);
					}

				}


			}


		}

		if ( requiredList.size() == 0 ) {

			return false;

		} else {

			return true;

		}                   

	}

	/**
	 * Is cell contains invalid charecter
	 */

	private boolean isInvalidChar (int studentHeaderStartPosition, HSSFRow row, 
			HSSFRow rowHeader, ArrayList invalidList) {

		int totalCells = rowHeader.getPhysicalNumberOfCells();
		String strCellHeader = "";

		//Get the position of Demographic details
		int start = totalCells - noOfDemographicList ;      
		String strCell = "";
		boolean isDemographicStart = false;


		for ( int i = studentHeaderStartPosition; i < totalCells; i++ ) {

			HSSFCell cellHeader = rowHeader.getCell((short)i);
			HSSFCell cell = row.getCell((short)i);
			strCell = getCellValue(cell);

			if ( i == start ) {

				isDemographicStart = true;

			}


			if (!strCell.equals("")) {

				if ( cellHeader.getStringCellValue().
						equals(CTBConstants.REQUIREDFIELD_FIRST_NAME) 
						&& !validNameString(strCell) ) {

					invalidList.add(CTBConstants.REQUIREDFIELD_FIRST_NAME);
				}

				else if ( cellHeader.getStringCellValue().
						equals(CTBConstants.MIDDLE_NAME)
						&& !validNameString(strCell) ) {

					invalidList.add(CTBConstants.MIDDLE_NAME);

				}

				else if ( cellHeader.getStringCellValue().
						equals(CTBConstants.REQUIREDFIELD_LAST_NAME)
						&& !validNameString(strCell) ) {

					invalidList.add(CTBConstants.REQUIREDFIELD_LAST_NAME);

				}

				else if ( cellHeader.getStringCellValue().
						equals(CTBConstants.REQUIREDFIELD_GRADE)
						&& !isValidGrade(strCell) ) {

					invalidList.add(CTBConstants.REQUIREDFIELD_GRADE);

				}

				else if ( cellHeader.getStringCellValue().
						equals(CTBConstants.REQUIREDFIELD_GENDER)
						&& !isValidGender(strCell) ) {

					invalidList.add(CTBConstants.REQUIREDFIELD_GENDER);

				}
				//Changes for GACRCT2010CR007 .
				else if (!(strCell == null ||  strCell.equals(""))
						&& cellHeader.getStringCellValue().
						equals(CTBConstants.REQUIREDFIELD_DATE_OF_BIRTH)
						&& !validateDateString(strCell) ) {

					invalidList.add(CTBConstants.REQUIREDFIELD_DATE_OF_BIRTH);

				} 
				//Student accomodation

				else if ( cellHeader.getStringCellValue().
						equals(CTBConstants.SCREEN_READER)
						&& !strCell.trim().equals("")
						&& !isValidCheckBox(strCell) ) {

					invalidList.add(CTBConstants.SCREEN_READER);

				}
				else if ( cellHeader.getStringCellValue().
						equals(CTBConstants.CALCULATOR)
						&& !strCell.trim().equals("")
						&& !isValidCheckBox(strCell) ) {

					invalidList.add(CTBConstants.CALCULATOR);

				}

				else if ( cellHeader.getStringCellValue().
						equals(CTBConstants.TEST_PAUSE)
						&& !strCell.trim().equals("")
						&& !isValidCheckBox(strCell) ) {

					invalidList.add(CTBConstants.TEST_PAUSE);

				}

				else if ( cellHeader.getStringCellValue().
						equals(CTBConstants.UNTIMED_TEST)
						&& !strCell.trim().equals("")
						&& !isValidCheckBox(strCell)) {

					invalidList.add(CTBConstants.UNTIMED_TEST);

				}
				else if ( cellHeader.getStringCellValue().
						equals(CTBConstants.HIGHLIGHTER)
						&& !strCell.trim().equals("")
						&& !isValidCheckBox(strCell) ) {

					invalidList.add(CTBConstants.HIGHLIGHTER);

				}

				else if ( cellHeader.getStringCellValue().
						equals(CTBConstants.QUESTION_BACKGROUND_COLOR)
						&& !strCell.trim().equals("")
						&& !isValidColor(strCell)) {

					invalidList.add(CTBConstants.QUESTION_BACKGROUND_COLOR);

				}

				else if ( cellHeader.getStringCellValue().
						equals(CTBConstants.QUESTION_FONT_COLOR)
						&& !strCell.trim().equals("")
						&& !isValidColor(strCell) ) {

					invalidList.add(CTBConstants.QUESTION_FONT_COLOR);

				}


				else if ( cellHeader.getStringCellValue().
						equals(CTBConstants.ANSWER_BACKGROUND_COLOR)
						&& !strCell.trim().equals("")
						&& !isValidColor(strCell) ) {

					invalidList.add(CTBConstants.ANSWER_BACKGROUND_COLOR);

				}

				else if ( cellHeader.getStringCellValue().
						equals(CTBConstants.ANSWER_FONT_COLOR)
						&& !strCell.trim().equals("")
						&& !isValidColor(strCell) ) {

					invalidList.add(CTBConstants.ANSWER_FONT_COLOR);

				}

				else if ( cellHeader.getStringCellValue().
						equals(CTBConstants.FONT_SIZE)
						&& !strCell.trim().equals("")
						&& !isValidFontSize(strCell) ) {

					invalidList.add(CTBConstants.FONT_SIZE);

				}
				//START- Changes for GA2011CR001   
				else if(this.isStudentIdNumeric.equals("AN")){
					if(cellHeader.getStringCellValue().
							equals(this.studentIdLabel)
							&& !strCell.trim().equals("")
							&& !validStudentId(strCell)) {

						invalidList.add(this.studentIdLabel);

					}
				}
				//END- Changes for GA2011CR001   
				//START- Changes for GACR005  
				else if(this.isStudentIdNumeric.equals("NU")){
					if(cellHeader.getStringCellValue().
							equals(this.studentIdLabel)
							&& !strCell.trim().equals("")
							&& !validConfigurableStudentId(strCell)) {

						invalidList.add(this.studentIdLabel);

					}
				}
				//END- Changes for GACR005  
				//START- Changes for GA2011CR001               
				else if(this.isStudentId2Numeric.equals("AN")){
					if(cellHeader.getStringCellValue().
							equals(this.studentId2Label)
							&& !strCell.trim().equals("")
							&& !validStudentId(strCell)){

						invalidList.add(this.studentId2Label);

					} 
				}
				// END- Changes for GA2011CR001   
				//START- Changes for GACR005  
				else if(this.isStudentId2Numeric.equals("NU")){
					if(cellHeader.getStringCellValue().
							equals(this.studentId2Label)
							&& !strCell.trim().equals("")
							&& !validConfigurableStudentId(strCell)){

						invalidList.add(this.studentId2Label);

					} 
				}
				//END- Changes for GACR005  



				//For validating demographics data
				else if ( i == start ) {

					strCellHeader = getCellValue(cellHeader);

					if ( !strCell.equals("") && !validateDemographic(strCellHeader,
							strCell ) ) {

						invalidList.add(strCellHeader);    

					}



				}

			} // End if cell validation start from firstName

			//increment demographic posision
			if ( isDemographicStart ) {

				start++;

			}

		}

		if ( invalidList.size() == 0 ) {

			return false;

		} else {

			return true;

		}

	}

	/**
	 *  Get date from string 
	 */ 

	private Date getDateFromDateString(String date){

		Date result = null;
		if (date == null)
			return result;

		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyLocalizedPattern("MM/dd/yyyy");
		sdf.format(date);
		try{
			result = sdf.parse(date);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return result;
	}    

	/**
	 *  Validate date
	 */

	private  boolean validateDateString(String dateStr) {

		if (isValidDateFormat(dateStr)) {


			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");

			try{
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
				while ( tokenizer.hasMoreTokens() ) {

					String token = tokenizer.nextToken();
					int value = new Integer(token).intValue();
					if ( i==0 ) {

						if ( value > 12 || value <= 0 ) {

							return false;

						}

						month=value;

					} else if ( i==1 ) {

						if ( value > 31 || value <= 0 ) {

							return false;

						}

						day = value;
					} else if ( i==2 ) {

						year = value;

					}

					i++;
				} // end while

				// check leap year  and no of days
				year = 2000+year;

				if ( month == 4 || month == 6 || month == 9 || month == 11 ) {

					if ( day > 30 ) {

						return false;

					}

				} else if ( month ==2 ) {

					if ( isLeapYear(year) && day > 29) {

						return false;

					}  else if (!isLeapYear(year) && day > 28) {

						return false;

					}


				}  

			}  catch (Exception e) {

				e.printStackTrace();
				return false;

			}


			return true;

		}  else {

			return false;   

		}


	}

	/*
	 * return Excel cell value as a String
	 */
	private String getCellValue(HSSFCell cell) {

		String cellValue = "";
		int cellType = 0;

		if (cell != null) {

			cellType = cell.getCellType();   

			if ( cellType == 0 ) {

				// Check is cell date formatted or not

				boolean datFormat = HSSFDateUtil.isCellDateFormatted(cell);

				if ( !datFormat ) {

					cellValue = String.valueOf((new Double (cell.getNumericCellValue())).
							intValue());
				}

				// date formatted is true, then format it to mm/dd/yyyy
				if ( datFormat ) {

					Date cellDate = cell.getDateCellValue();
					SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
					cellValue = sdf.format(cellDate);
				}


			} else if ( cellType == 1 ) {

				cellValue = cell.getStringCellValue();

			} else if ( cellType == 4) {

				boolean cellBoolValue = cell.getBooleanCellValue();
				cellValue = new Boolean (cellBoolValue).toString();
			}

		}


		return cellValue.trim();

	}

	/**
	 *  Check leap year 
	 */
	public  boolean isLeapYear(int year) {

		if ( year%100==0 ) {

			if ( year%400 == 0 ) {

				return true;

			} else {

				return false;
			}
		}

		if ( year%4==0 ) {

			return true;

		} else {

			return false;

		}

	}

	/**
	 *  validate the org name and org code
	 */
	private  boolean validString(String str) {

		str = str.trim();
		char[] characters = str.toCharArray();

		for ( int i=0 ; i<characters.length ; i++ ) {

			char character = characters[i];

			if ( !validOrgNameCharacter(character) ) {

				return false;
			}
		}

		return true;
	}

	/**
	 *  validate the org name and org code
	 */

	private  boolean validOrgNameCharacter(char ch) {
		boolean A_Z = ((ch >= 65) && (ch <= 90));
		boolean a_z = ((ch >= 97) && (ch <= 122));
		boolean zero_nine = ((ch >= 48) && (ch <= 57));
		// !, @, #, -, _, ', :, /, comma, period, and space will be allowed in these fields.
		boolean validChar = ((ch == '/') ||
				(ch == '\\') ||
				(ch == '-') ||
				(ch == '\'') ||
				(ch == '(') ||
				(ch == ')') ||
				(ch == '&') ||
				(ch == '+') ||
				(ch == ',') ||
				(ch == '.') ||
				(ch == ' '));

		return (zero_nine || A_Z || a_z || validChar);

	}

	/**
	 *  validate the StudentId
	 */
	private  boolean validStudentId(String str) {

		str = str.trim();
		char[] characters = str.toCharArray();

		for ( int i=0 ; i<characters.length ; i++ ) {

			char character = characters[i];

			if ( !validStudentIdCharacter(character) ) {

				return false;
			}
		}

		return true;
	}


	//Start- GACR005
	/**
	 *  validate the StudentId
	 */
	private  boolean validConfigurableStudentId(String str) {

		str = str.trim();

		if ( !validNumber(str) ) {

			return false;
		}


		return true;
	}
	//END- GACR005
	/**
	 *  validate the StudentId character
	 */

	private  boolean validStudentIdCharacter(char ch) {
		boolean A_Z = ((ch >= 65) && (ch <= 90));
		boolean a_z = ((ch >= 97) && (ch <= 122));
		boolean zero_nine = ((ch >= 48) && (ch <= 57));
		// space will be allowed in these fields.
		boolean validChar = (ch == ' ');

		return (zero_nine || A_Z || a_z || validChar);

	}

	/**
	 * This method is responsible to check the row value should be at and above the minimum length if so then return true otherwise return false
	 * @param studentHeaderStartPosition
	 * @param row
	 * @param rowHeader
	 * @param maxLengthList
	 * @return
	 */
	// Created for GACR005
	private boolean isMinlength (int studentHeaderStartPosition, HSSFRow row, 
			HSSFRow rowHeader, ArrayList minLengthList) {

		//Changes for GACR005
		int totalCells = rowHeader.getPhysicalNumberOfCells();  
		String strCell = "";

		for ( int i = studentHeaderStartPosition; i < totalCells; i++ ) {

			HSSFCell cellHeader = rowHeader.getCell((short)i);
			HSSFCell cell = row.getCell((short)i);
			strCell = getCellValue(cell);

			if ( !strCell.equals("")){
				if( cellHeader.getStringCellValue().
						equals(this.studentIdLabel)
						&& !strCell.trim().equals("")
						&& this.isStudentIdConfigurable
						&& !isMinLengthConfigurableStudentId(strCell) 
						&& this.studentIdMinLength != null ) {

					minLengthList.add(this.studentIdLabel);
				}
				else if( cellHeader.getStringCellValue().
						equals(this.studentId2Label)
						&& !strCell.trim().equals("")
						&& this.isStudentId2Configurable
						&& !isMinLengthConfigurableStudentId2(strCell)
						&& this.studentId2MinLength != null ) {

					minLengthList.add(this.studentId2Label);
				}


			}
			//Changes for GACR005 

		}
		
		if ( minLengthList.size() == 0 ) {

			return false;

		} else {

			return true;   
		}

	}


	/**
	 * Is cell value exceed Maxlength
	 */

	private boolean isMaxlengthExceed (int studentHeaderStartPosition, HSSFRow row, 
			HSSFRow rowHeader, ArrayList maxLengthList) {

		int totalCells = rowHeader.getPhysicalNumberOfCells();  
		String strCell = "";

		for ( int i = studentHeaderStartPosition; i < totalCells; i++ ) {

			HSSFCell cellHeader = rowHeader.getCell((short)i);
			HSSFCell cell = row.getCell((short)i);
			strCell = getCellValue(cell);

			if ( !strCell.equals("")){

				if ( cellHeader.getStringCellValue().
						equals(CTBConstants.REQUIREDFIELD_FIRST_NAME) 
						&& !isMaxLength32(strCell) ) {

					maxLengthList.add(CTBConstants.REQUIREDFIELD_FIRST_NAME);
				}

				else if ( cellHeader.getStringCellValue().
						equals(CTBConstants.MIDDLE_NAME)
						&& !strCell.trim().equals("")
						&& !isMaxLength32(strCell) ) {

					maxLengthList.add(CTBConstants.MIDDLE_NAME);

				}

				else if ( cellHeader.getStringCellValue().
						equals(CTBConstants.REQUIREDFIELD_LAST_NAME)
						&& !isMaxLength32(strCell) ) {

					maxLengthList.add(CTBConstants.REQUIREDFIELD_LAST_NAME);

				}
				//Changes for GA2011CR001	
				else if( cellHeader.getStringCellValue().
						equals(this.studentIdLabel)
						&& !strCell.trim().equals("")
						&& this.isStudentIdConfigurable
						&& !isMaxLengthConfigurableStudentId(strCell) 
						&& this.maxlengthStudentID != null ) {

					maxLengthList.add(this.studentIdLabel);
				}
				else if ( cellHeader.getStringCellValue().
						equals(CTBConstants.STUDENT_ID)
						&& !strCell.trim().equals("")
						&& !this.isStudentIdConfigurable
						&& !isMaxLength64(strCell) ) {

					maxLengthList.add(CTBConstants.STUDENT_ID);

				}

				//Changes for GA2011CR001
				else if( cellHeader.getStringCellValue().
						equals(this.studentId2Label)
						&& !strCell.trim().equals("")
						&& this.isStudentId2Configurable
						&& !isMaxLengthConfigurableStudentId2(strCell)
						&& this.maxlengthStudentId2 != null ) {

					maxLengthList.add(this.studentId2Label);
				}

				else if ( cellHeader.getStringCellValue().
						equals(CTBConstants.STUDENT_ID2)
						&& !strCell.trim().equals("")
						&& !this.isStudentIdConfigurable
						&& !isMaxLength64(strCell) ) {

					maxLengthList.add(CTBConstants.STUDENT_ID2);

				}
			}

		}

		if ( maxLengthList.size() == 0 ) {

			return false;

		} else {

			return true;   
		}

	}

	/**
	 * check wheather Maxlength is 32
	 */ 

	private boolean isMaxLength15 ( String value ) {

		if ( value.length() <= 15 ) {

			return true;

		} else {

			return false;

		}

	}

	/*
	 * check wheather Maxlength is 50
	 */
	private boolean isMaxLength50 ( String value ) {

		if ( value.length() <= 50 ) {

			return true;

		} else {

			return false;   
		}

	}

	/*
	 * check wheather Maxlength is 32
	 */ 

	private boolean isMaxLength32 ( String value ) {

		if ( value.length() <= 32 ) {

			return true;

		} else {

			return false;

		}

	}

	/*
	 * check wheather Maxlength is 64
	 */

	private boolean isMaxLength64 ( String value ) {

		if ( value.length() <= 64 ) {

			return true;

		} else {

			return false;   
		}

	}

	/*
	 * check wheather Maxlength is 255
	 */

	private boolean isMaxLength255 (String value) {

		if ( value.length() <= 255 ) {

			return true;

		} else {

			return false;   
		}

	}

	/*
	 * check Maxlength for gerogia Customer for studentID
	 */

	private boolean isMaxLengthConfigurableStudentId (String value){

		if (Integer.parseInt(this.maxlengthStudentID) > 32){
			this.maxlengthStudentID = "32";
		}

		if ( value.length() <= Integer.parseInt(this.maxlengthStudentID) ) {

			return true;

		} else {

			return false;   
		}


	}
	/*
	 * check Minlength for gerogia Customer for studentID
	 */
	// Created for GACR005
	private boolean isMinLengthConfigurableStudentId (String value){

		if (Integer.parseInt(this.studentIdMinLength) < 0){
			this.studentIdMinLength = "0";
		}

		if ( value.length() >= Integer.parseInt(this.studentIdMinLength) ) {

			return true;

		} else {

			return false;   
		}


	}

	/*
	 * check Maxlength for gerogia Customer for studentID2
	 */
	private boolean isMaxLengthConfigurableStudentId2 (String value){

		if (Integer.parseInt(this.maxlengthStudentId2) > 32){
			this.maxlengthStudentId2 = "32";
		}

		if ( value.length() <= Integer.parseInt(this.maxlengthStudentId2) ) {

			return true;

		} else {

			return false;   
		}

	}

	/*
	 * check Minlength for gerogia Customer for studentID2
	 * // Created for GACR005
	 */
	private boolean isMinLengthConfigurableStudentId2 (String value){

		if (Integer.parseInt(this.studentId2MinLength) < 0){
			this.studentId2MinLength = "0";
		}

		if ( value.length() >= Integer.parseInt(this.studentId2MinLength) ) {

			return true;

		} else {

			return false;   
		}

	}


	/*
	 * check wheather cutomer's organization to 
	 * loginUserOrganization path is valid or not
	 */

	private boolean isCommonPathValid ( int rowPosition,HSSFRow row, 
			HSSFRow rowHeader, 
			HashMap hierarchyErrorMap, 
			boolean isMatchUploadOrgIds,
			Node  []loginUserNode) {

		try {

			Node []node = this.studentFileRowHeader[0].getOrganizationNodes();
			HashMap commonPathMapDB = this.commonHierarchyMap;
			int OrgHeaderLastPosition = node.length ;
			//  Node []loginUserNode =  orgNode.getTopNodesForUser(this.userName);
			String leafOrgName = "";
			String loginOrgName = "";
			String leafOrgCode = "";
			String loginOrgCode = "";
			ArrayList excelCommonPathList = new ArrayList();
			String strCellId = "";
			String strCellName = "";

			int loginUserPosition = -1;

			for ( int i = 0, j = 0; i < OrgHeaderLastPosition; i++, j = j + 2 ) {

				HSSFCell cellName = row.getCell(j);
				HSSFCell cellId = row.getCell(j + 1);

				strCellName = getCellValue(cellName);
				strCellId = getCellValue(cellId);

				if (isMatchUploadOrgIds) {

					if (!strCellId.equals("")) {

						leafOrgCode = strCellId;
						loginOrgCode = strCellId;

						if ( !strCellName.equals("") ) {

							leafOrgName = strCellName;
							loginOrgName = strCellName;
							leafOrgCode = leafOrgCode+"|"+leafOrgName;

						}

						excelCommonPathList.add(leafOrgCode);

						if ( isLoginUserOrganization(loginOrgCode+"|"+loginOrgName, loginUserNode, true) ) {

							loginUserPosition = j;
							break;

						} 

					}// End CellId checking
					// if cellId is blank then check cellName

					else {

						if ( !strCellName.equals("") ) {

							leafOrgName = strCellName;
							loginOrgName = strCellName;

							if (!strCellId.equals("")) {

								leafOrgCode = strCellId;
								loginOrgCode = strCellId;
								leafOrgName = leafOrgCode+"|"+leafOrgName;  

							}

							excelCommonPathList.add(leafOrgName);

							if ( isLoginUserOrganization(loginOrgCode+"|"+loginOrgName,loginUserNode, true) ) {

								loginUserPosition = j;
								break;

							} 

						}

					}

					//End isMatchUploadOrgIds checking
				} else {

					if ( !strCellName.equals("") ) {

						leafOrgName = strCellName;
						loginOrgName = strCellName;

						if (!strCellId.equals("")) {

							leafOrgCode = strCellId;

							leafOrgName = leafOrgName+"|"+leafOrgCode;  

						}

						excelCommonPathList.add(leafOrgName);

						if ( isLoginUserOrganization(loginOrgName,loginUserNode, false) ) {

							loginUserPosition = j;
							break;

						} 

					}


				} //End Else(isMatchUploadOrgIds == false)


			} //end for

			if (loginUserPosition == -1) {

				String orgName = loginUserNode[0].getOrgNodeCategoryName();
				String orgHeader = orgName +" Name";
				ArrayList errorHierarchyList = new ArrayList();
				String strCellNameHeader = "";

				for ( int i = 0, j = 0; i < OrgHeaderLastPosition; i++, j = j + 2 ) {

					HSSFCell cellNameHeader = rowHeader.getCell(j);
					HSSFCell cellIdHeader = rowHeader.getCell(j + 1);
					strCellNameHeader = getCellValue(cellNameHeader);

					errorHierarchyList.add(strCellNameHeader);
					errorHierarchyList.add(getCellValue(cellIdHeader));

					if (strCellNameHeader.equals(orgHeader)) {

						break;   
					}

				}

				hierarchyErrorMap.put(new Integer(rowPosition), errorHierarchyList);

			} else {

				if ( excelCommonPathList.size() > 0 ) {

					Set key = commonPathMapDB.keySet();
					ArrayList commonPathListDB = null;
					Iterator it = key.iterator();
					Integer orgNodeId = null;

					while ( it.hasNext() ) {

						orgNodeId = (Integer)it.next();
						commonPathListDB = (ArrayList)commonPathMapDB.get(orgNodeId);

						//if ( commonPathListDB.size() == excelCommonPathList.size() ) {

						if (isCommonPathSame (rowPosition, loginUserPosition, 
								rowHeader, excelCommonPathList, 
								commonPathListDB, hierarchyErrorMap, isMatchUploadOrgIds)) {

							// hierarchyErrorMap.clear();
							break;


						}

						//}

					}

				} else {

					return false;

				}// End if-else

			}



			if (hierarchyErrorMap.size() > 0) {

				return false;

			} else {

				return true;   
			} 

		} catch (Exception e) {

			e.printStackTrace();
		}
		return true;
	}

	/*
	 * Is Excel common Path match to actual common path which
	 *  is comes from database
	 */ 
	private boolean isCommonPathSame (int rowPosition, int loginUserPosition,
			HSSFRow rowHeader, ArrayList excelCommonPathList, 
			ArrayList commonPathListDB, HashMap hierarchyErrorMap, boolean isMatchUploadOrgIds) {

		int currentPosition = -1;
		for ( int i = 0, j = commonPathListDB.size() - 1;
		i < excelCommonPathList.size(); i++, j-- ) {

			Node node = (Node)commonPathListDB.get(j);
			String orgNameDB = node.getOrgNodeName();
			String orgCodeDB = node.getOrgNodeCode();
			String orgNameExcel = (String)excelCommonPathList.get(i);

			String []orgNameCode = orgNameExcel.split("\\|");

			if (isMatchUploadOrgIds) {

				if (orgNameCode.length > 1 ) {

					if (!orgNameCode[0].equalsIgnoreCase("")) {

						if (!orgNameCode[0].equalsIgnoreCase(orgCodeDB)) {

							if (!orgNameCode[1].equalsIgnoreCase(orgNameDB)) {

								currentPosition = i;
								break; 

							}


						} 
					}

				} else {

					if (!orgNameCode[0].equalsIgnoreCase(orgNameDB)) {

						currentPosition = i;
						break;

					}

				}

			} else {

				if (orgNameCode[0] != null) {

					if ( !orgNameDB.equalsIgnoreCase(orgNameCode[0]) ) {

						currentPosition = i;
						break;

					} 

				}

				if (orgNameCode.length > 1) {

					if (orgNameCode[1] !=null) {

						if (!orgNameCode[1].equalsIgnoreCase(orgCodeDB)) {

							currentPosition = i;
							break;

						}

					}

				}

			} // End Else(isMatchUploadOrgIds == false)



		} // End For

		if ( currentPosition == -1 ) {

			if (hierarchyErrorMap.containsKey(new Integer(rowPosition))) {

				hierarchyErrorMap.remove(new Integer(rowPosition));

			}
			return true;



		} else {

			Node []node = this.studentFileRowHeader[0].getOrganizationNodes();
			ArrayList errorHierarchyList = new ArrayList();
			currentPosition = currentPosition * 2;
			for ( int j = currentPosition ; j < loginUserPosition + 2; j = j + 2 ) {

				HSSFCell cellNameHeader = rowHeader.getCell(j);
				HSSFCell cellIdHeader = rowHeader.getCell(j + 1);

				errorHierarchyList.add(getCellValue(cellNameHeader));
				errorHierarchyList.add(getCellValue(cellIdHeader));

			}

			hierarchyErrorMap.put(new Integer(rowPosition), errorHierarchyList);
			return false;
		}


	}
	/*
	 * Is admin belonging organization is login admin organization
	 */ 

	private boolean isLoginUserOrganization ( String orgName, Node[] loginUserNode, boolean isMatchUploadOrgIds ) {

		String []orgDetail = null;

		if (isMatchUploadOrgIds) {

			orgDetail = orgName.split("\\|");

		}

		for ( int i = 0; i < loginUserNode.length; i++ ) {

			Node tempNode = loginUserNode[i];

			if (isMatchUploadOrgIds) {


				if (!orgDetail[0].equalsIgnoreCase("")) {

					//Check for orgCode
					if (orgDetail[0].equalsIgnoreCase(tempNode.getOrgNodeCode())) {

						return true;

					} else {

						//Check for orgName
						if (orgDetail[1].equalsIgnoreCase(tempNode.getOrgNodeName())) {

							return true;

						}

					}

				} else {

					//Check for orgName
					if (orgDetail[1].equalsIgnoreCase(tempNode.getOrgNodeName())) {

						return true;

					}

				}


			} else {

				//Check for orgName
				if ( tempNode.getOrgNodeName().equalsIgnoreCase(orgName) ) {

					return true;
				}
			}

		}

		return false;

	}



	/*
	 * retrive login user organization position in Excel
	 */ 

	private int getLoginUserOrgPosition (HSSFRow row, HSSFRow rowHeader, Node []loginUserNode) {

		int loginUserPosition = 0;
		try {

			Node []node = this.studentFileRowHeader[0].getOrganizationNodes();
			int OrgHeaderLastPosition = node.length ;
			String leafOrgName = "";
			// Node []loginUserNode =  orgNode.getTopNodesForUser(this.userName);
			for ( int i = 0, j = 0; i < OrgHeaderLastPosition; i++, j = j + 2 ) {

				HSSFCell cell = row.getCell(j);


				if ( !getCellValue(cell).equals(" ") ) {

					leafOrgName = cell.getStringCellValue();

					if ( isLoginUserOrganization(leafOrgName,loginUserNode, false) ) {

						loginUserPosition = j;
						break;

					} 

				}

			} //end for 


		} catch (Exception e) {

			e.printStackTrace();
		}

		return  loginUserPosition;   
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


	/*
	 * create Organization and Student
	 */ 
	private void createOrganizationAndStudent(  HashMap requiredMap, 
			HashMap minLengthMap,
			HashMap maxLengthMap,
			HashMap invalidCharMap, 
			HashMap logicalErrorMap,
			HashMap hierarchyErrorMap, 
			HashMap studentDataMap,
			HashMap leafNodeErrorMap,
			HashMap blankRowMap,
			boolean isMatchUploadOrgIds,
			Node []loginUserNodes) 
	throws SQLException,CTBBusinessException {


		POIFSFileSystem pfs = null;
		HSSFSheet sheet = null; 
		int loginUserOrgPosition = 0; 
		Node organization = null;
		Integer orgNodeId = null;
		int uploadCount = 0;
		boolean isBlankRow = true;
		try {
			Customer customer = users.getCustomer(this.userName);
			pfs = new POIFSFileSystem( new FileInputStream(this.serverFilePath) );

			HSSFWorkbook wb = new HSSFWorkbook(pfs);
			sheet = wb.getSheetAt(0);
			int totalRows = 0;

			if ( sheet != null) {

				totalRows =  sheet.getPhysicalNumberOfRows();

			}

			HSSFRow rowHeader = sheet.getRow(0);
			//find the statr position of the user details header
			Node []nodeCategory = this.studentFileRowHeader[0].getOrganizationNodes();
			int orgHeaderLastPosition = nodeCategory.length * 2;    
			//    Node []loginUserNodes =  orgNode.getTopNodesForUser(this.username);
			//travers the entire sheet to update the db for user insertion 
			for ( int i = 1; i < totalRows; i++ ) {

				System.out.println("    ***** Upload Control: 2nd loop: Processing row " + i);

				HSSFRow bodyRow = sheet.getRow(i);

				if ( bodyRow == null ) {

					totalRows++;
					continue;

				} else {

					int totalCells = rowHeader.getPhysicalNumberOfCells();         
					// retrive each cell value for user
					for (int k = 0; k < totalCells; k++) {

						//   HSSFCell cellHeader = rowHeader.getCell((short)k);
						HSSFCell cell = bodyRow.getCell((short)k);

						if ( cell != null && (!getCellValue(cell).trim().equals("") 
								&& !(cell.getCellType() == 3)) ) {

							isBlankRow = false;                  

						} 
					}

					if (isBlankRow) {

						continue;

					}

				}

				if ( !(requiredMap.containsKey(new Integer(i)) 
						|| invalidCharMap.containsKey(new Integer(i)) 
						|| minLengthMap.containsKey(new Integer(i)) 
						|| maxLengthMap.containsKey(new Integer(i)) 
						|| logicalErrorMap.containsKey(new Integer(i)) 
						|| hierarchyErrorMap.containsKey(new Integer(i))
						|| leafNodeErrorMap.containsKey(new Integer(i))
						|| blankRowMap.containsKey(new Integer(i))) ) {

					//OrganizationCreation or Existency check process
					loginUserOrgPosition = getLoginUserOrgPosition(
							bodyRow, rowHeader,
							loginUserNodes);

					HSSFCell loginUserOrgCell = bodyRow.getCell(
							(short)loginUserOrgPosition);

					String loginUserOrgName = getCellValue(loginUserOrgCell);
					Node loginUserNode = getLoginUserOrgDetail(
							loginUserNodes, loginUserOrgName);

					//orgNodeId and parentId initialization process
					Integer parentOrgId = loginUserNode.getOrgNodeId();
					orgNodeId = loginUserNode.getOrgNodeId();
					int lastOrganization = 0; 
					String leafName="";
					for ( int ii = loginUserOrgPosition + 2; ii < orgHeaderLastPosition; ii = ii + 2 ) {

						HSSFCell OrgCellName = bodyRow.getCell((short)ii);
						HSSFCell OrgCellId = bodyRow.getCell((short)ii + 1);
						HSSFCell orgCellHeaderName = rowHeader.getCell((short)ii);
						String orgCode = getCellValue(OrgCellId);
						String orgName = getCellValue(OrgCellName);
						HSSFCell OrgCellHeaderName = rowHeader.getCell((short)ii);
						String headerName = getCellValue(OrgCellHeaderName);
						Integer categoryId = getCategoryId (headerName, nodeCategory);

						if ( !hasOrganization(ii,bodyRow) && orgName.equals("")
								&& orgCode.equals("")) {

							lastOrganization = ii;
							break;

						} else if (hasOrganization(ii,bodyRow) && orgName.equals("") 
								&& orgCode.equals("")) {
							continue;

						} else {

							// Search Organization by OrgCode    
							if ( isMatchUploadOrgIds ) {

								boolean isOrgExist = isOrganizationExist (orgCode, parentOrgId, categoryId, isMatchUploadOrgIds);
								if (!orgCode.trim().equals("") || !orgName.trim().equals("") ) {

									// Search Organization by OrgName
									if ( !isOrgExist ) {

										isOrgExist = isOrganizationExist (orgName, parentOrgId, categoryId, !isMatchUploadOrgIds);

										// No organization Exist
										if ( !isOrgExist ) {

											organization = new Node();

											organization.setCustomerId(customer.getCustomerId());
											organization.setOrgNodeName(orgName);
											organization.setOrgNodeCode(orgCode); 
											organization.setOrgNodeCategoryId(categoryId);
											organization.setParentOrgNodeId(parentOrgId);
											//create Organization
											organization = this.organizationManagement.
											createOrganization(this.userName, organization);

											//parentId and orgNodeId updated
											parentOrgId = organization.getOrgNodeId();
											orgNodeId = organization.getOrgNodeId();
											ArrayList tempList = new ArrayList( Arrays.asList(this.detailNodeM));
											tempList.add(organization);
											this.detailNodeM = (Node[])tempList.toArray(new Node[0]);

										} else {

											// retrive existing organization by passing orgName
											organization = getOrgNodeDetail(orgName, parentOrgId, categoryId, false);


											//Is Organization Exist
											if (organization != null) {

												parentOrgId = organization.getOrgNodeId();
												orgNodeId = organization.getOrgNodeId();
												continue;    

											}  else {

												//new Organization creation
												organization = new Node();

												organization.setCustomerId(customer.getCustomerId());
												organization.setOrgNodeName(orgName);
												organization.setOrgNodeCode(orgCode); 
												organization.setOrgNodeCategoryId(categoryId);
												organization.setParentOrgNodeId(parentOrgId);
												//create Organization
												organization = this.organizationManagement.
												createOrganization(this.userName, organization);

												//parentId and orgNodeId updated
												parentOrgId = organization.getOrgNodeId();
												orgNodeId = organization.getOrgNodeId();
												ArrayList tempList = new ArrayList( Arrays.asList(this.detailNodeM));
												tempList.add(organization);
												this.detailNodeM = (Node[])tempList.toArray(new Node[0]);

											}

										}

									} else {

										// retrive existing organization by passing orgCode
										organization = getOrgNodeDetail(orgCode, parentOrgId, categoryId, isMatchUploadOrgIds);

										//Is Organization Exist
										if (organization != null) {

											parentOrgId = organization.getOrgNodeId();
											orgNodeId = organization.getOrgNodeId();
											continue;    

										} else {

											//new Organization creation

											organization.setCustomerId(customer.getCustomerId());
											organization.setOrgNodeName(orgName);
											organization.setOrgNodeCode(orgCode); 
											organization.setOrgNodeCategoryId(categoryId);
											organization.setParentOrgNodeId(parentOrgId);
											//create Organization
											organization = this.organizationManagement.
											createOrganization(this.userName, organization);

											//parentId and orgNodeId updated
											parentOrgId = organization.getOrgNodeId();
											orgNodeId = organization.getOrgNodeId();
											ArrayList tempList = new ArrayList( Arrays.asList(this.detailNodeM));
											tempList.add(organization);
											this.detailNodeM = (Node[])tempList.toArray(new Node[0]);

										}

									}


								}// End if ""




							} else { // if no MatchUploadOrgIds present in customer configuration 


								boolean isOrgExist = isOrganizationExist (orgName, parentOrgId, categoryId, false);

								if (!orgName.trim().equals("")) {   
									// if no organization exist
									if (!isOrgExist) {

										//new Organization creation
										organization = new Node();

										organization.setCustomerId(customer.getCustomerId());
										organization.setOrgNodeName(orgName);
										organization.setOrgNodeCode(orgCode); 
										organization.setOrgNodeCategoryId(categoryId);
										organization.setParentOrgNodeId(parentOrgId);
										//create Organization
										organization = this.organizationManagement.
										createOrganization(this.userName, organization);

										//parentId and orgNodeId updated
										parentOrgId = organization.getOrgNodeId();
										orgNodeId = organization.getOrgNodeId();
										ArrayList tempList = new ArrayList( Arrays.asList(this.detailNodeM));
										tempList.add(organization);
										this.detailNodeM = (Node[])tempList.toArray(new Node[0]);

									} else {

										// retrive existing organization by passing orgName
										organization = getOrgNodeDetail(orgName, parentOrgId, categoryId, false);

										//Is Organization Exist
										if (organization != null) {

											parentOrgId = organization.getOrgNodeId();
											orgNodeId = organization.getOrgNodeId();
											continue;    

										} else {

											//new Organization creation
											organization = new Node();

											organization.setCustomerId(customer.getCustomerId());
											organization.setOrgNodeName(orgName);
											organization.setOrgNodeCode(orgCode); 
											organization.setOrgNodeCategoryId(categoryId);
											organization.setParentOrgNodeId(parentOrgId);
											//create Organization
											organization = this.organizationManagement.
											createOrganization(this.userName, organization);

											//parentId and orgNodeId updated
											parentOrgId = organization.getOrgNodeId();
											orgNodeId = organization.getOrgNodeId();
											ArrayList tempList = new ArrayList( Arrays.asList(this.detailNodeM));
											tempList.add(organization);
											this.detailNodeM = (Node[])tempList.toArray(new Node[0]);

										}

									}
								}// End of cheking orgName not ""

							} // Else block end

						} // Else block (Organization creation process)

					}               

					Node []orgDetail = new Node[1];
					orgDetail[0] = new Node();
					orgDetail[0].setOrgNodeId(orgNodeId);


					for ( int j = orgHeaderLastPosition ; j < rowHeader.
					getPhysicalNumberOfCells() ; j++ ) {

						HSSFCell headerCell = rowHeader.getCell(j);
						HSSFCell bodyCell  = bodyRow.getCell(j);

						String strHeaderValue = getCellValue(headerCell);
						String strBodyValue = getCellValue(bodyCell);

						studentDataMap.put(strHeaderValue, strBodyValue);




					}  

					HashMap studentDemoMap = new HashMap();
					ArrayList demolist = new ArrayList();
					//Get the position of Demographic details
					int start = rowHeader.
					getPhysicalNumberOfCells() - noOfDemographicList ;  
					for (int d = start ; d < rowHeader.
					getPhysicalNumberOfCells() ; d++) {

						HSSFCell headerCell = rowHeader.getCell(d);
						HSSFCell bodyCell  = bodyRow.getCell(d);

						String strHeaderValue = getCellValue(headerCell);
						String strBodyValue = getCellValue(bodyCell);

						if ( !(strBodyValue.equals("")) && strBodyValue!= null ) {

							studentDemoMap.put(strHeaderValue, strBodyValue);
							demolist.add(strHeaderValue);

						}




					}  
					//actual user creation using usermanagement
					createStudent(studentDataMap,orgHeaderLastPosition,
							orgDetail,studentDemoMap,demolist); 

					uploadRecordCount++;                                  

				} 

				isBlankRow = true;  

			}

			if ( this.dataFileAudit.getFailedRecordCount() == null ||
					this.dataFileAudit.getFailedRecordCount().intValue() == 0 )  {

				this.dataFileAudit.setStatus("SC");
				this.dataFileAudit.setFaildRec(null);


			} else {

				dataFileAudit.setStatus("FL");
			}

			this.dataFileAudit.setUploadFileRecordCount(new Integer(uploadRecordCount));
			uploadDataFile.upDateAuditTable(this.dataFileAudit);

			String loginUserMail = users.getUserDetails( this.userName).getEmail();

			//send email        
			if ( loginUserMail != null ) {   

				sendMail( this.userName, CTBConstants.EMAIL_TYPE_WELCOME , loginUserMail);

			}

		} catch (SQLException se) {

			dataFileAudit.setFaildRec(null);
			dataFileAudit.setStatus("FL");
			dataFileAudit.setFailedRecordCount(new Integer(0));
			dataFileAudit.setUploadFileRecordCount(new Integer(0));
			try{
				uploadDataFile.upDateAuditTable(dataFileAudit);
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			FileNotUploadedException dataNotFoundException = 
				new FileNotUploadedException
				("UploadDownloadManagement.Failed");
			dataNotFoundException.setStackTrace(se.getStackTrace());
			throw dataNotFoundException;

		} catch (Exception e) {

			dataFileAudit.setFaildRec(null);
			dataFileAudit.setStatus("FL");
			dataFileAudit.setFailedRecordCount(new Integer(0));
			dataFileAudit.setUploadFileRecordCount(new Integer(0));
			try{
				uploadDataFile.upDateAuditTable(dataFileAudit);
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			FileNotUploadedException dataNotFoundException = 
				new FileNotUploadedException
				("UploadDownloadManagement.Failed");
			dataNotFoundException.setStackTrace(e.getStackTrace());
			throw dataNotFoundException;

		}

	}


	/*
	 * retrive login user org detail
	 */ 
	private Node getLoginUserOrgDetail (Node []loginUserNodes, String organizationName) {

		Node orgDetail = null;
		for ( int i = 0; i < loginUserNodes.length; i++ ) {

			orgDetail = loginUserNodes[i];

			if ( orgDetail.getOrgNodeName().equalsIgnoreCase(organizationName) ) {

				break;
			}

		}

		return orgDetail;

	}


	/*
	 * 
	 */ 
	private boolean hasOrganization (int currentPosition, HSSFRow row) {

		Node []node = this.studentFileRowHeader[0].getOrganizationNodes();
		int OrgHeaderLastPosition = node.length * 2;
		String leafOrgName = "";
		for (int j = currentPosition + 2 ; j < OrgHeaderLastPosition; j = j + 2 ) {

			HSSFCell cellName = row.getCell(j);
			HSSFCell cellId = row.getCell(j + 1);

			if ( !getCellValue(cellName).equals("") 
					|| (!getCellValue(cellId).equals(""))) {

				return true;

			}

		} //end for 

		return false;    
	}


	/*
	 *
	 */ 
	private Node getOrgNodeDetail ( String orgName, Integer parentId, Integer categoryId, 
			boolean isMatchUploadOrgIds ) {

		Node orgNode = null;
		try {

			//Changed 04/12/2008

			//Node [] detailNode = this.uploadDataFile.getUserDataTemplate(this.userName);
			Node [] detailNode = this.detailNodeM;                            

			for ( int i = 0; i < detailNode.length; i++ ) {

				Node tempNode = detailNode[i];
				if ( isMatchUploadOrgIds ) {

					if (tempNode.getOrgNodeCode() != null) {

						if ( tempNode.getOrgNodeCode().equalsIgnoreCase(orgName) 
								&& tempNode.getParentOrgNodeId().intValue() 
								== parentId.intValue()
								&& tempNode.getOrgNodeCategoryId().intValue() == categoryId.intValue()) {

							orgNode = tempNode;
							break;

						} 

					}



				} else {

					if ( tempNode.getOrgNodeName().equalsIgnoreCase(orgName) 
							&& tempNode.getParentOrgNodeId().intValue() 
							== parentId.intValue()
							&& tempNode.getOrgNodeCategoryId().intValue() == categoryId.intValue()) {

						orgNode = tempNode;
						break;

					}

				}


			}

		} /*catch (SQLException se) {

            se.printStackTrace();

        }*/ catch (Exception e) {

        	e.printStackTrace();
        }

        return orgNode;
	}



	/*
	 *
	 */ 
	private boolean isOrganizationExist ( String searchString, Integer parentId,
			Integer categoryId, boolean isMatchUploadOrgIds) {

		boolean hasOrganization = false;
		try {

			//Changed 04/12/2008

			//Node [] detailNode = this.uploadDataFile.getUserDataTemplate(this.userName);
			Node [] detailNode = this.detailNodeM;

			for ( int i = 0; i < detailNode.length; i++ ) {

				Node tempNode = detailNode[i];

				if ( isMatchUploadOrgIds ) {

					if (tempNode.getOrgNodeCode() != null) {

						if ( !searchString.trim().equals("")
								&& tempNode.getOrgNodeCode().equalsIgnoreCase(searchString) 
								&& tempNode.getParentOrgNodeId().intValue() 
								== parentId.intValue()
								&& categoryId.intValue() == tempNode.getOrgNodeCategoryId().intValue()) {

							hasOrganization = true;
							break;
						}

					}


				} else {

					if ( !searchString.trim().equals("") 
							&& tempNode.getOrgNodeName().equalsIgnoreCase(searchString) 
							&& tempNode.getParentOrgNodeId().intValue() 
							== parentId.intValue()
							&& categoryId.intValue() == tempNode.getOrgNodeCategoryId().intValue()) {

						hasOrganization = true;  
						break; 
					}
				}

			} 

		} /*catch (SQLException se) {

            se.printStackTrace();

        }*/ catch (Exception e) {

        	e.printStackTrace();

        }

        return hasOrganization;

	}



	/**
	 * Get Category id customer from category name
	 */ 

	private Integer getCategoryId (String categoryName, Node []categoryNode) {

		Integer categoryId = null;

		for (int i = 0; i < categoryNode.length; i++) {

			Node tempNode = categoryNode[i];

			if ( tempNode.getOrgNodeCategoryName().equalsIgnoreCase(categoryName) ) {

				categoryId = tempNode.getOrgNodeCategoryId();     
			}
		}

		return categoryId;
	}

	/**
	 *  validating date 
	 */
	private  boolean isValidDateFormat (String excelDate) {

		if ( excelDate.length() != 10 ) {

			return false;

		}
		int position = excelDate.indexOf("/");

		if ( position == -1 ) {

			return false;
		}

		String month = excelDate.substring(0,position);
		excelDate = excelDate.substring(position + 1);
		position = excelDate.indexOf("/");

		if ( position == -1 ) {

			return false;

		}
		String day = excelDate.substring(0,position);
		String year = excelDate.substring(position + 1);

		try {

			int monthValue = Integer.valueOf(month).intValue();
			int dayValue = Integer.valueOf(day).intValue();


			if ( monthValue > 0 && monthValue <= 12 ) {

				if ( dayValue > 0 && dayValue <= 31 ) {

					if ( year.length() == 4 ) {

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
	 *  check if the date is future date
	 */
	private  boolean isFutureDate (String excelDate) {

		int position = excelDate.indexOf("/");

		String month = excelDate.substring(0,position);
		excelDate = excelDate.substring(position + 1);
		position = excelDate.indexOf("/");

		String day = excelDate.substring(0,position);
		String year = excelDate.substring(position + 1);

		Date systemDate = new Date();
		systemDate.setDate(31);
		systemDate.setMonth(11);
		systemDate.setYear(systemDate.getYear() + 1899);

		Date startDate = new Date();
		startDate.setDate(1);
		startDate.setMonth(0);
		startDate.setYear(startDate.getYear() + 1799);

		Date  dateFromExcel = new Date();

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
	 * This is a generic method to send mail. It retrieves the content of the body
	 * from database. value should be an empty string even If for some email_type, 
	 * there is no replacement in the body. Caller should ensure that to_address 
	 * is not null. This method suppresses any exception occured. 
	 * 
	 */
	private void sendMail(String userName, Integer emailType , String to ) {
		try {
			CustomerEmail emailData = new CustomerEmail();
			if(userName != null){ 
				emailData = users.getCustomerEmailByUserName(userName, emailType);
			}
			String content = CTBConstants.STUDENT_MAIL_BODY;

			InitialContext ic = new InitialContext();

			//the properties were configured in WebLogic through the console
			Session session = (Session) ic.lookup("UserManagementMail");

			//contruct the actual message
			Message msg =  new MimeMessage(session);
			msg.setFrom(new InternetAddress(CTBConstants.EMAIL_FROM));

			//emailTo could be a comma separated list of addresses
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
			msg.setSubject(CTBConstants.EMAIL_SUBJECT);
			msg.setText(content);
			msg.setSentDate(new Date());

			//send the message
			Transport.send(msg);

		} catch (Exception e) {
			e.printStackTrace();
			OASLogger.getLogger("UploadManagement").error(e.getMessage());
			OASLogger.getLogger("UploadManagement").error("sendMail failed for emailType: " + emailType);
		}
	}

	/*
	 * Is Student alresdy associate with orgNodeId
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


	/**
	 * Populating default customer accommodation values
	 */

	private  void populateDefaultAccommodationValues (Integer customerId, 
			HashMap defaultAccommodationMap,
			HashMap editableAccomodationMap){


		try {
			com.ctb.bean.testAdmin.CustomerConfig []customerConfigValues = 
				uploadDataFile.getCustomerConfigurationForAccommodation(customerId);

			if ( customerConfigValues != null){

				for ( int i=0; i< customerConfigValues.length ; i++ ){

					defaultAccommodationMap.put( customerConfigValues[i].getCustomerConfigurationName(),
							customerConfigValues[i].getDefaultValue());
					editableAccomodationMap.put(customerConfigValues[i].getCustomerConfigurationName(),
							customerConfigValues[i].getEditable());
				}
			}

		}catch(SQLException se){
			se.printStackTrace();
		}

	}

	/**
	 * Getting default customer accommodation value
	 */

	private  String getDefaultAccommodation (String asAccommodation){

		String msDefaultValue = CTBConstants.F;

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

		String msDefaultValue = CTBConstants.F;

		if (!(accomValue == null || "".equals(accomValue.trim()))){
			String accomodation = getChekBoxValue(accomValue);

			if ( editableAccomodationMap.containsKey(accomField)){
				String msEditable = (String) editableAccomodationMap.get(accomField);

				if (msEditable.equals(CTBConstants.F)){

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


	/*
	 * Solution for Defect 55851
	 */ 
	private String initCap ( String value ) {

		if ( value != null && !value.trim().equals("")) {

			String str[] = value.trim().split(" ");
			String initString = "";
			if (str.length > 1) {

				for (int i = 0; i < str.length; i++) {

					if(str[i].length()>0){
						String firstValue = str[i].toLowerCase();
						char ch = firstValue.charAt(0);
						String newStringChar = (new Character(ch)).toString().toUpperCase();
						firstValue = firstValue.substring(1,firstValue.length());
						if (i == 0) {

							initString = initString + newStringChar+firstValue;

						} else {


							initString = initString +" "+ newStringChar+firstValue;
						}
					}


				}
				return initString;
			} else {

				String firstValue = str[0].toLowerCase();
				char ch = firstValue.charAt(0);
				String newStringChar = (new Character(ch)).toString().toUpperCase();
				firstValue = firstValue.substring(1,firstValue.length());
				firstValue = newStringChar+firstValue;
				return firstValue;

			} 

		} else {

			return null;

		}

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





} 

