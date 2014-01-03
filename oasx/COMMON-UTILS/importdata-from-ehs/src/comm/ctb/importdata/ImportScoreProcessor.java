package comm.ctb.importdata;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jdom.Document;
import org.jdom.Element;

import comm.ctb.bean.ItemDataPointDetailsVO;
import comm.ctb.bean.ReadDetailsVO;
import comm.ctb.bean.StudentTestDetailsVO;

import comm.ctb.utils.Configuration;
import comm.ctb.utils.Constants;
import comm.ctb.utils.DBUtil;
import comm.ctb.utils.ExtractUtil;
import comm.ctb.utils.FtpSftpUtil;
import comm.ctb.utils.XMLUtils;

/*import java.util.concurrent.ConcurrentLinkedQueue;
import com.Main.ScoringMessage;
import com.ctb.tdc.web.utils.JMSUtils;*/
import com.jcraft.jsch.Session;

import javax.jms.JMSException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import comm.ctb.utils.QueueSend;

public class ImportScoreProcessor {
    
	static Logger logger = Logger.getLogger(ImportScoreProcessor.class);
	
	//public static final String propertiesFilePath = "/data/web/sites/wldomains/EHS_Files/properties/";//For dev testing setup
	public static final String propertiesFilePath = "/export/home/oasdev/operations/operation-tools/java/importdata-from-ehs/properties_files/";// Operational setup
	/**
	 * @param args
	 */

	public static void main(String[] args) {	
		PropertyConfigurator.configure("conf/log4j.properties");
		String envName = getPropFileFromCommandLine(args);
		//ExtractUtil.loadPropetiesFile(envName);
		ExtractUtil.loadExternalPropetiesFile(envName, propertiesFilePath);
		Configuration config = new Configuration();
		String sourceDir = config.getFtpFilepath();
		String targetDir = config.getLocalFilePath();
		String archiveDir = config.getArchiveFilePath();		
		//System.out.println(sourceDir);
		System.out.println("Import Process started..."+new Date(System.currentTimeMillis()));
		Session session = null;
		//delete previously downloaded files
		System.out.println("Deletion Start Time: "+new Date(System.currentTimeMillis()));
		deleteFiles(targetDir);
		System.out.println("Deletion End Time: "+new Date(System.currentTimeMillis()));
		try{
			session = FtpSftpUtil.getSFTPSession(config);
			System.out.println("Download Start Time: "+new Date(System.currentTimeMillis()));
			FtpSftpUtil.downloadFiles(session, sourceDir, targetDir);
			System.out.println("Download End Time: "+new Date(System.currentTimeMillis()));

			File folder = new File(targetDir);
			File[] listOfFiles = folder.listFiles();
			if(listOfFiles != null && listOfFiles.length > 0){
				for (int j = 0; j < listOfFiles.length; j++) {
					File inFile = listOfFiles[j];
					if(inFile.isFile()){
						//System.out.println(fileName+" --> "+filePath);
						List stuTestDetailsList = processScoreData(inFile);
						//TODO:: Save score data & read details for a student in OAS
						if(stuTestDetailsList != null){
							for (int i = 0; i < stuTestDetailsList.size(); i++) {
								boolean isSuccess = DBUtil.saveItemScoreData((StudentTestDetailsVO)stuTestDetailsList.get(i));
								if(isSuccess){
									String rosterId = ((StudentTestDetailsVO)stuTestDetailsList.get(i)).getStudentTestId().substring(10);
									System.out.println("Score data saved successfully for student_id = "+ rosterId);
									System.out.println("Scoring invoked for roster id = "+rosterId);
									try {
										invokeScoring(new Integer(rosterId), config);
									} catch (JMSException jmse) {
										// TODO Auto-generated catch block
										System.out.println("Error: Scoring failed to invoke for roster id = "+rosterId);
										logger.info("Scoring failed to invoke for roster id = "+rosterId);
										jmse.printStackTrace();
									} catch (NamingException ne){
										// TODO Auto-generated catch block
										System.out.println("Error: Scoring failed to invoke for roster id = "+rosterId);
										logger.info("Error: Scoring failed to invoke for roster id = "+rosterId);
										ne.printStackTrace();
									} catch (IOException ioe){
										// TODO Auto-generated catch block
										System.out.println("Error: Scoring failed to invoke for roster id = "+rosterId);
										logger.info("Error: Scoring failed to invoke for roster id = "+rosterId);
										ioe.printStackTrace();
									}
								}	
								else{
									//TODO: Maintain log for failed roster
									maintainLog((StudentTestDetailsVO)stuTestDetailsList.get(i), inFile);
									System.out.println("Score data saved failed for student_id = "+ ((StudentTestDetailsVO)stuTestDetailsList.get(i)).getVendorStudentId()+ ", roster_id = "+((StudentTestDetailsVO)stuTestDetailsList.get(i)).getStudentTestId().substring(10));
								}	
							}
							//TODO:: Archive the successfully processed file
							FtpSftpUtil.archiveProcessedFiles(session, sourceDir, archiveDir, inFile.getName());
						}					
						
					}
				}
			}else{
				System.out.println("No input files to process....");
				System.exit(1);
			}
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			FtpSftpUtil.closeSFTPClient(session);
		}
		System.out.println("Import Process completed..."+new Date(System.currentTimeMillis()));
	}
	
		
	public static List processScoreData(File inFile) throws Exception{

		Document document = null;
		List stdVOList = null;
		document = XMLUtils.parseXML(inFile);
		Element rootNode = document.getRootElement();
		//System.out.println("rootNode: "+rootNode);
		
		stdVOList = new ArrayList();  
		List stuTestDetailsList = XMLUtils.extractAllElement(".//" + Constants.ELEM_STU_TEST_DET, rootNode);
		for (Iterator iterator = stuTestDetailsList.iterator(); iterator.hasNext();) {
			Element stdElement = (Element) iterator.next();

			List idpdVOList = new ArrayList();
			
			//TODO: populate  StudentTestDetailsVO object here
			StudentTestDetailsVO stdVO = new StudentTestDetailsVO();
			stdVO.setStudentTestId(stdElement.getAttributeValue(Constants.ATTR_STU_TEST_ID));
			stdVO.setLastName(stdElement.getAttributeValue(Constants.ATTR_STU_L_NAME));
			stdVO.setFirstName(stdElement.getAttributeValue(Constants.ATTR_STU_F_NAME));
			stdVO.setMiddleInitial(stdElement.getAttributeValue(Constants.ATTR_STU_M_I));
			stdVO.setDateOfBirth(stdElement.getAttributeValue(Constants.ATTR_STU_DOB));
			stdVO.setDocumentId(Long.parseLong(stdElement.getAttributeValue(Constants.ATTR_STU_DOC_ID)));
			stdVO.setGrade(stdElement.getAttributeValue(Constants.ATTR_STU_GRADE));
			stdVO.setCommodityCode(stdElement.getAttributeValue(Constants.ATTR_COMM_CODE));
			stdVO.setLevel(stdElement.getAttributeValue(Constants.ATTR_LEVEL));
			stdVO.setForm(stdElement.getAttributeValue(Constants.ATTR_FORM));
			stdVO.setVendorStudentId(stdElement.getAttributeValue(Constants.ATTR_VENDOR_STU_ID));
			
			List itemDataPointDetailsList = XMLUtils.extractAllElement(".//" + Constants.ELEM_ITEM_DP_DET, stdElement);
			for (Iterator iterator2 = itemDataPointDetailsList.iterator(); iterator2.hasNext();) {
				Element idpdElement = (Element) iterator2.next();
				List rdVOList = new ArrayList();
				
				//TODO: populate ItemDataPointDetailsVO object here
				ItemDataPointDetailsVO idpdVO = new ItemDataPointDetailsVO();
				idpdVO.setItemId(idpdElement.getAttributeValue(Constants.ATTR_ITEM_ID));
				idpdVO.setDataPoint(idpdElement.getAttributeValue(Constants.ATTR_DATAPOINT));
				idpdVO.setItemNo(idpdElement.getAttributeValue(Constants.ATTR_ITEM_NO));
				//idpdVO.setFinalScore(idpdElement.getAttributeValue("Final_Score"));// need to apply rule and derive final score
				idpdVO.setAlertCode(idpdElement.getAttributeValue(Constants.ATTR_ALERT_CODE));
				
				List readDetailsList = XMLUtils.extractAllElement(".//" + Constants.ELEM_READ_DET, idpdElement);
				for (Iterator iterator3 = readDetailsList.iterator(); iterator3.hasNext();) {
					Element rdElement = (Element) iterator3.next();
					//TODO: populate ReadDetailsVO object here
					
					ReadDetailsVO rdVO = new ReadDetailsVO();
					rdVO.setReadNumber(new Integer(Integer.parseInt(rdElement.getAttributeValue(Constants.ATTR_READ_NO))));
					rdVO.setScoreValue(sanitizeScoreValue(rdElement.getAttributeValue(Constants.ATTR_SCORE_VAL)));
					rdVO.setReaderId(new Integer(Integer.parseInt(rdElement.getAttributeValue(Constants.ATTR_READER_ID))));
					rdVO.setDataTime(rdElement.getAttributeValue(Constants.ATTR_DATE_TIME));
					rdVO.setElapsedTime(new Integer(Integer.parseInt(rdElement.getAttributeValue(Constants.ATTR_ELAPSED_TIME))));
					
					rdVOList.add(rdVO);
				}
				String finalScore = deriveFinalScoreForItem(rdVOList);
				idpdVO.setFinalScore(finalScore);
				idpdVO.setReadDetailsList(rdVOList);
				idpdVOList.add(idpdVO);
			}
			stdVO.setItemDataPointList(idpdVOList);
			stdVOList.add(stdVO);
			//System.out.println("Attr val::"+stdElement.getAttributeValue(ATTR_STU_TEST_ID));			
		}
		return stdVOList;
	}
	
	public static String deriveFinalScoreForItem(List rdVOList){
		String finalScoreVal = "";
		HashMap readMap = new HashMap();
		int noOfReads = rdVOList.size();
		
		if(rdVOList != null && rdVOList.size() > 0){
			for (Iterator iterator = rdVOList.iterator(); iterator.hasNext();) {
				ReadDetailsVO readDetailsVO = (ReadDetailsVO) iterator.next();
				readMap.put(readDetailsVO.getReadNumber(), readDetailsVO.getScoreValue());
			}
							
			switch (noOfReads) {
				case 1:
						//finalScore = readMap.get(1);
						finalScoreVal = ""; //Assumption: There must be at least two reads. So setting score as ""
						break;
						
				case 2:
						finalScoreVal = applyRulesForTwoReads(readMap);
						break;
					
				case 3:
						finalScoreVal = applyRulesForThreeReads(readMap);
						break;
		
				default:
						finalScoreVal = applyRulesForThreeReads(readMap);
						break;
			}
		}
						
		return finalScoreVal;
	}
	
	public static String applyRulesForTwoReads(HashMap readMap){
		String finalScore = "";
		Integer deriveScore = null;
		String read1 = (String) readMap.get(new Integer(1));
		String read2 = (String) readMap.get(new Integer(2));
		
		if(isValidNumericScore(read1)){ //checking if read1 is one digit numeric value
			if(isValidNumericScore(read2)){//checking if read2 is one digit numeric value
				if(Math.abs(Integer.parseInt(read1) - Integer.parseInt(read2)) <= 1){
					deriveScore = new Integer(Integer.parseInt(read1) + Integer.parseInt(read2));
					finalScore = deriveScore.toString();
				}else{
					//Assumption: Read3 must be present in this case
					finalScore = "";
				}
			}else if(isValidConditionCode(read2)){//read2 is valid condition code
				//Assumption: Read3 must be present in this case
				finalScore = "";
			}else{
				//Invalid read2 value: Rule not defined !
				finalScore = "";
			}
		}else if(isValidConditionCode(read1)){//read1 is valid condition code
			if(isValidNumericScore(read2)){//checking if read2 is one digit numeric value
				//Assumption: Read3 must be present in this case
				finalScore = "";
			}else if(isValidConditionCode(read2)){//read2 is valid condition code
				if(read1.equalsIgnoreCase(read2)){// if same condition codes
					finalScore = read1;
				}else{//
					//Assumption: Read3 must be present in this case
					finalScore = "";
				}
			}else{
				//Invalid read2 value: Rule not defined !
				finalScore = "";
			}
		}else{
			//Invalid read1 value: Rule not defined !
			finalScore = "";
		}
		
		return finalScore;
	}
	
	public static String applyRulesForThreeReads(HashMap readMap){		
		String finalScore = "";
		Integer deriveScore = null;
		String read1 = (String) readMap.get(new Integer(1));
		String read2 = (String) readMap.get(new Integer(2));
		String read3 = (String) readMap.get(new Integer(3));
		
		if(isValidNumericScore(read1)){ //checking if read1 is one digit numeric value
			if(isValidNumericScore(read2)){//checking if read2 is numeric value
				if(Math.abs(Integer.parseInt(read1) - Integer.parseInt(read2)) <= 1){
					deriveScore = new Integer(Integer.parseInt(read1) + Integer.parseInt(read2));
					finalScore = deriveScore.toString();
				}else{
					if(isValidNumericScore(read3)){//checking if read3 is one digit numeric value
						deriveScore = new Integer(2 * Integer.parseInt(read3));
						finalScore = deriveScore.toString();
					}else if(isValidConditionCode(read3)){//read3 is valid condition code
						finalScore = read3; //Rule: All third reads that have a condition code will have the condition code stand as the final score.
					}else{
						//Invalid read3 value: Rule not defined !
						finalScore = "";
					}
					
				}
			}
			else if(isValidConditionCode(read2)){//read2 is valid condition code
				if(isValidNumericScore(read3)){//checking if read3 is one digit numeric value
					deriveScore = new Integer(2 * Integer.parseInt(read3));
					finalScore = deriveScore.toString();
				}else if(isValidConditionCode(read3)){//read3 is valid condition code
					finalScore = read3; //Rule: All third reads that have a condition code will have the condition code stand as the final score.
				}else{
					//Invalid read3 value: Rule not defined !
					finalScore = "";
				}
			}
			else{
				//Invalid read2 value: Rule not defined !
				finalScore = "";
			}
		}else if (isValidConditionCode(read1)){//read1 is valid condition code
			if(isValidNumericScore(read2)){//checking if read2 is one digit numeric value
				if(isValidNumericScore(read3)){//checking if read2 is one digit numeric value
					deriveScore = new Integer(2 * Integer.parseInt(read3));
					finalScore = deriveScore.toString();
				}else if(isValidConditionCode(read3)){//read3 is valid condition code
					finalScore = read3; //Rule: All third reads that have a condition code will have the condition code stand as the final score.
				}else{
					//Invalid read3 value: Rule not defined !
					finalScore = "";
				}
			}else if(isValidConditionCode(read2)){//read2 is condition code
				if(read1.equalsIgnoreCase(read2)){// if same condition codes
					finalScore = read1;
				}else{//
					if(isValidNumericScore(read3)){//checking if read3 is one digit numeric value
						/*deriveScore = 2 * Integer.parseInt(read3);
						finalScore = deriveScore.toString();*/
						finalScore = read3; // Rule: final score -> Numeric value of third read in this case
					}else if(isValidConditionCode(read3)){//read3 is valid condition code
						finalScore = read3; //Rule: All third reads that have a condition code will have the condition code stand as the final score.
					}else{
						//Invalid read3 value: Rule not defined !
						finalScore = "";
					}
				}				
			}else{
				//Invalid read2 value: Rule not defined !
				finalScore = "";
			}
		}
		else{
			//Invalid read1 value: Rule not defined !
			finalScore = "";
		}
		return finalScore;
	}
	
	public static void deleteFiles(String targetDir){
		File dir = new File(targetDir);
		File ff;
		if(dir.isDirectory()){
			if(dir.listFiles().length > 0){
				for (int i = 0; i < dir.listFiles().length; i++) {
				File inFile = dir.listFiles()[i];
					ff = new File(dir, inFile.getName());
					ff.delete();
				}
			}
				
		}
	}
	
	public static void maintainLog(StudentTestDetailsVO failedStuTestDetails, File inFile) throws Exception{
		/*XMLOutputter xmlOut = new XMLOutputter();
		Document document = null;
		document = XMLUtils.parseXML(inFile);
		Element rootNode = document.getRootElement();
		Element failedElement = null;
		//System.out.println("rootNode: "+rootNode);
		List<Element> stuTestDetailsList = XMLUtils.extractAllElement(".//" + Constants.ELEM_STU_TEST_DET, rootNode);
		for (Iterator<Element> iterator = stuTestDetailsList.iterator(); iterator.hasNext();) {
			Element stdElement = iterator.next();
			if(stdElement.getAttributeValue(Constants.ATTR_VENDOR_STU_ID).equalsIgnoreCase(failedStuTestDetails.getVendorStudentId())){
				failedElement = stdElement;
				break;
			}
				
		}	
		System.out.println("XML for failed roster :: "+xmlOut.outputString(failedElement));*/
		DBUtil.logForFailedRoster(failedStuTestDetails, inFile.getName());
	}
	
	public static String sanitizeScoreValue(String scoreVal){
		if(isDecimalNumber(scoreVal))
			return new Integer(Math.round(Float.parseFloat(scoreVal))).toString();
		else
			return scoreVal;
	}
	
	public static boolean isDecimalNumber(String str){
		return str.matches(Constants.regexDecimal);
	}
	
	public static boolean isValidNumericScore(String str){
		return str.matches(Constants.regexNumeric);
	}
	
	public static boolean isValidConditionCode(String str){
		return str.matches(Constants.regexAlpha);
	}
	
	private static String getPropFileFromCommandLine(String[] args)
	{
		String envName = "";
		String usage = "Usage:\n 	java -jar importdata-from-ehs.jar <properties file name>";
		if (args.length < 1) {
			System.out.println("Cannot parse command line. No command specified.");
			System.out.println(usage);
			System.exit(1);
		}
		else {
			envName = args[0].toUpperCase();
		}
		return envName;
	}

	/*Added for invoke scoring: Start*/
	private static String jndiFactory = "";
    private static String jmsFactory = "";
    private static String jmsURL = "";
    private static String jmsQueue = "";
    private static String jmsPrincipal = "";
    private static String jmsCredentials = "";
    
	private static void invokeScoring(Integer testRosterId, Configuration config) throws Exception { 
		getResourceValue(config);
	    InitialContext ic = QueueSend.getInitialContext(jndiFactory,jmsURL,jmsPrincipal,jmsCredentials);
	    QueueSend qs = new QueueSend();
	    qs.init(ic, jmsFactory, jmsQueue);
	    qs.readAndSend(qs,testRosterId);
	    qs.close();
	    ic.close();
	  }
        
    private static void getResourceValue(Configuration config) throws Exception {
	    //ResourceBundle rb = ResourceBundle.getBundle("security");
	    jndiFactory = config.getJndiFactory();
	    jmsFactory = config.getJmsFactory();
	    jmsURL = config.getJmsURL();
	    jmsQueue = config.getJmsQueue();
	    jmsPrincipal = config.getJmsPrincipal();
	    jmsCredentials = config.getJmsCredentials();
    }
	/*Added for invoke scoring: End*/	
}
