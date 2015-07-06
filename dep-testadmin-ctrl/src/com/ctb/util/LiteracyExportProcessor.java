package com.ctb.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.ctb.bean.testAdmin.LiteracyProExportData;
import com.ctb.bean.testAdmin.LiteracyProExportRequest;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.testAdmin.BulkReportExportException;

public class LiteracyExportProcessor implements Runnable {
	
	private Map<String, Object> paramMap;
	double rand;
	private static final String oasDtataSourceJndiName = "oasDataSource";
	private static final String irsDtataSourceJndiName = "irsDataSource";

	private Connection oasCon;
	private Connection irsCon;

	private String ROSTER_FETCH_QUERY = "SELECT TR.TEST_ROSTER_ID AS ROSTERID, "//1
		+ "        TR.TEST_ADMIN_ID AS SESSIONID, "//2
		+ "        TR.STUDENT_ID AS OASSTUDENTID, "//3
		+ "        ST.USER_NAME AS STUDENTID, "//4
		+ "        ST.LAST_NAME AS LASTNAME, "//5
		+ "        ST.MIDDLE_NAME AS MIDDLENAME, "//6
		+ "        ST.FIRST_NAME AS FIRSTNAME, "//7
		+ "        TO_CHAR(ST.BIRTHDATE, 'mm/dd/yyyy') AS DATEOFBIRTH, "//8
		+ "        ST.GENDER AS GENDER, "//9
		+ "        TO_CHAR(TA.LOGIN_START_DATE, 'mm/dd/yyyy') AS ASSESSMENTDATE, "//10
		+ "        'TABE' AS INSTRUMENT, "//11
		+ "        TC.TEST_NAME AS FORM, "//12
		+ "        ISET.ITEM_SET_LEVEL AS LVL, "//13
		+ "        ISET.ITEM_SET_NAME AS SUBTEST, "//14
		+ "        TA.TEST_ADMIN_NAME AS SESSIONNAME, "//15
		+ "        TA.PRODUCT_ID AS PRODUCTID, "//16
		+ "        SISS.ITEM_SET_ID AS SUBTESTID "//17
		+ "   FROM TEST_ROSTER             TR, "
		+ "        TEST_ADMIN              TA, "
		+ "        STUDENT                 ST, "
		+ "        ITEM_SET                ISET, "
		+ "        STUDENT_ITEM_SET_STATUS SISS, "
		+ "        TEST_CATALOG            TC, "
		+ "        ORG_NODE                ORG, "
		+ "        ORG_NODE_ANCESTOR       ONA, "
		+ "        ORG_NODE_STUDENT        ONS "
		+ "  WHERE ONS.STUDENT_ID = ST.STUDENT_ID "
		+ "    AND ONS.ORG_NODE_ID = ONA.ORG_NODE_ID "
		+ "    AND ONS.ORG_NODE_ID = TR.ORG_NODE_ID "
		+ "    AND TA.TEST_ADMIN_ID = TR.TEST_ADMIN_ID "
		+ "    AND TR.STUDENT_ID = ST.STUDENT_ID "
		+ "    AND TR.TEST_ROSTER_ID = SISS.TEST_ROSTER_ID "
		+ "    AND SISS.ITEM_SET_ID = ISET.ITEM_SET_ID "
		+ "    AND TC.PRODUCT_ID = TA.PRODUCT_ID "
		+ "    AND ORG.ORG_NODE_ID = ONA.ANCESTOR_ORG_NODE_ID "
		+ "    AND ORG.CUSTOMER_ID = TA.CUSTOMER_ID "
		+ "    AND (ISET.ITEM_SET_LEVEL <> 'L' OR TA.PRODUCT_ID = 4008) "
		+ "    AND TA.ACTIVATION_STATUS = 'AC' "
		+ "    AND TR.ACTIVATION_STATUS = 'AC' "
		+ "    AND ST.ACTIVATION_STATUS = 'AC' "
		+ "    AND TR.VALIDATION_STATUS = 'VA' "
		+ "    AND SISS.VALIDATION_STATUS = 'VA' "
		+ "    AND NVL(SISS.EXEMPTIONS, 'N') <> 'Y' "
		+ "    AND NVL(SISS.ABSENT, 'N') <> 'Y' "
		+ "    AND SISS.COMPLETION_STATUS NOT IN ('SC', 'NT', 'IP') "
		+ "    AND ISET.SAMPLE = 'F' "
		+ "    AND TA.PRODUCT_ID IN (4008, 4009, 4010, 4011, 4012) "
		+ "    AND ORG.ORG_NODE_ID = ? "
		+ "    AND ORG.CUSTOMER_ID = ? ";
	private String REPORT_LEVEL_MAPPER = " SELECT DISTINCT PROD.PRODUCT_ID || ISATD.ITEM_SET_ID AS SUBTESTPRODMAPPER, "
		+ "                 PROD.PRODUCT_ID || REISET.ITEM_SET_ID AS REPORTINGLEVELID "
		+ "   FROM TEST_CATALOG      TC, "
		+ "        ITEM_SET_ITEM     ISI, "
		+ "        ITEM              I, "
		+ "        ITEM_SET_ITEM     REISI, "
		+ "        ITEM_SET_ANCESTOR ISA, "
		+ "        PRODUCT           PROD, "
		+ "        ITEM_SET_CATEGORY ISC, "
		+ "        ITEM_SET          REISET, "
		+ "        ITEM_SET_ANCESTOR ISATD "
		+ "  WHERE I.ITEM_ID = ISI.ITEM_ID "
		+ "    AND REISI.ITEM_ID = I.ITEM_ID "
		+ "    AND ISA.ITEM_SET_ID = REISI.ITEM_SET_ID "
		+ "    AND REISET.ITEM_SET_ID = ISA.ANCESTOR_ITEM_SET_ID "
		+ "    AND REISET.ITEM_SET_TYPE = 'RE' "
		+ "    AND REISET.ITEM_SET_CATEGORY_ID = ISC.ITEM_SET_CATEGORY_ID "
		+ "    AND ISC.FRAMEWORK_PRODUCT_ID = PROD.PARENT_PRODUCT_ID "
		+ "    AND ISC.ITEM_SET_CATEGORY_LEVEL = PROD.CONTENT_AREA_LEVEL "
		+ "    AND ISATD.ITEM_SET_ID = ISI.ITEM_SET_ID "
		+ "    AND ISATD.ITEM_SET_TYPE = 'TD' "
		+ "    AND TC.ITEM_SET_ID = ISATD.ANCESTOR_ITEM_SET_ID "
		+ "    AND PROD.PRODUCT_ID = TC.PRODUCT_ID "
		+ "    AND PROD.PRODUCT_ID IN (4008, 4009, 4010, 4011, 4012) ";
	private String ROSTER_SCORE_QUERY = " SELECT REPORTING_LEVEL    AS SUBTEST, "
		+ "        SESSIONID          AS SESSIONID, "
		+ "        STUDENTID          AS OASSTUDENTID, "
		+ "        REPORTING_LEVEL_SS AS SCALEDSCORE, "
		+ "        REPORTING_LEVEL_GE AS GLE, "
		+ "        REPORT_LEVEL_ID    AS REPORTINGLEVELID "
		+ "   FROM (SELECT CAD.NAME AS REPORTING_LEVEL, "
		+ "                TBCONFACT.SESSIONID AS SESSIONID, "
		+ "                TBCONFACT.STUDENTID AS STUDENTID, "
		+ "                TBCONFACT.SCALE_SCORE AS REPORTING_LEVEL_SS, "
		+ "                TBCONFACT.GRADE_EQUIVALENT AS REPORTING_LEVEL_GE, "
		+ "                TBCONFACT.SESSIONID || TBCONFACT.STUDENTID || "
		+ "                TBCONFACT.CONTENT_AREAID AS REPORT_LEVEL_ID "
		+ "           FROM TABE_CONTENT_AREA_FACT TBCONFACT, CONTENT_AREA_DIM CAD "
		+ "          WHERE #CONTENTCLAUSE# "
		+ "            AND CAD.CONTENT_AREAID = TBCONFACT.CONTENT_AREAID "
		+ "            AND CAD.SUBJECTID <> 999999 "
		+ "         UNION ALL "
		+ "         SELECT COMD.NAME AS REPORTING_LEVEL, "
		+ "                TBCOMP.SESSIONID AS SESSIONID, "
		+ "                TBCOMP.STUDENTID AS STUDENTID, "
		+ "                TBCOMP.SCALE_SCORE AS REPORTING_LEVEL_SS, "
		+ "                TBCOMP.GRADE_EQUIVALENT AS REPORTING_LEVEL_GE, "
		+ "                TBCOMP.SESSIONID || TBCOMP.STUDENTID || TBCOMP.COMPOSITEID AS REPORT_LEVEL_ID "
		+ "           FROM TABE_COMPOSITE_FACT TBCOMP, "
		+ "                COMPOSITE_DIM       COMD, "
		+ "                ASSESSMENT_DIM      TC "
		+ "          WHERE #COMPOSITECLAUSE# "
		+ "            AND COMD.COMPOSITEID = TBCOMP.COMPOSITEID "
		+ "            AND TC.ASSESSMENTID = TBCOMP.ASSESSMENTID "
		+ "            AND TC.TYPE <> 'TL') "; 
	private String UPDATE_EXPORT_REQUEST = " UPDATE BULK_EXPORT_DATA_FILE BEDF "
		+ "    SET BEDF.STATUS = ?, BEDF.MESSAGE = BEDF.MESSAGE || chr(10) || ?, BEDF.FILE_CONTENT = ? "
		+ "  WHERE BEDF.EXPORT_REQUEST_ID = ? ";
	private String UPDATE_EXPORT_REQUEST_ERR = " UPDATE BULK_EXPORT_DATA_FILE BEDF SET BEDF.STATUS = ?, BEDF.MESSAGE = BEDF.MESSAGE || chr(10) || ? WHERE BEDF.EXPORT_REQUEST_ID = ? ";

	public LiteracyExportProcessor(Map<String, Object> paramMap) {
		this.paramMap = paramMap;
		rand = (Double) paramMap.get("rand");
	}

	@Override
	public void run() {
	    	long startTime = System.currentTimeMillis();
	    	System.out.println("("+rand+")-----------------------Starting a thread----------------------");
	    	System.out.println("("+rand+")" + paramMap);
	    	LiteracyProExportRequest literacyRequestObj = (LiteracyProExportRequest) paramMap.get("litProExportRequest");
		try {
			this.oasCon = openOASDBcon(false);
			this.irsCon = openIRSDBcon(false);
				
			LiteracyProExportData[] data = null;
        	    	LiteracyProExportData[] repotingMapper;
        	    	LiteracyProExportData[] oasData = null;
        	    	Set<String> irsKeySet = new HashSet<String>();
        	    	String dateFlagBulkReport = (String) paramMap.get("dateFlagBulkReport");
        	    	String startDtBulkReport = (String) paramMap.get("startDtBulkReport");
        	    	String endDtBulkReport = (String) paramMap.get("endDtBulkReport");
        	    	
        	    	Map<String, String> orgHierarchyMap = (HashMap<String, String>) paramMap.get("orgHierarchyMap");
        	    	Integer customerId = (Integer) paramMap.get("customerId");
	    	
	    	
	    	
        	    	//System.out.println("("+rand+")dateFlagBulkReport = " + dateFlagBulkReport);
        	    	//System.out.println("("+rand+")startDtBulkReport = " + startDtBulkReport);
        	    	//System.out.println("("+rand+")endDtBulkReport = " + endDtBulkReport);
        	    	//System.out.println("("+rand+")orgHierarchyMap = " + orgHierarchyMap);
        	    	
        	    	String searchCriteria = "";
        	    	if (!"AllDates".equals(dateFlagBulkReport)) {
        	    	    searchCriteria += " and trunc(ta.login_start_date) between to_date('" + startDtBulkReport + "', 'mm/dd/yyyy') and to_date('" + endDtBulkReport + "', 'mm/dd/yyyy')";
        	    	}
        	    	System.out.println("("+rand+")searchCriteria: " + searchCriteria);
        	    	String orgNodeId = "";
        	    	String maxKey = "0";
        	    	for (Map.Entry<String, String> entry : orgHierarchyMap.entrySet()) {
        	    	    String key = entry.getKey();
        	    	    if (Integer.parseInt(maxKey) < Integer.parseInt(key)) {
        	    		maxKey = new String(key);
        	    		String value = entry.getValue();
        		    	if (!"-1".equals(value)) {
        		    	    orgNodeId = value;
        		    	}
        	    	    }
        	    	}
        	    	System.out.println("("+rand+")orgNodeId = " + orgNodeId);
        	    	oasData = getBulkReportCSVData(Integer.valueOf(orgNodeId), searchCriteria, customerId);
        	    	
        	    	repotingMapper = getReportLevelMapper();
	    	
        	    	for(LiteracyProExportData oasBean : oasData){
        	    		// add entries into set to weed out duplicate ones
        	    	    irsKeySet.add(oasBean.getSessionID() + "~"+oasBean.getOasStudentId());
        	    		
        	    	    LiteracyProExportData d = getReportLevelMapperBean(repotingMapper, oasBean);
        	    	    String reportingLevelId = "";
        	    	    if(d !=null) {
        	    		reportingLevelId = d.getReportingLevelId();
        	    	    }
        	    	    oasBean.setReportingLevelId(oasBean.getSessionID() + oasBean.getOasStudentId() +  reportingLevelId);
        	    	}
	    	
        	    	//convert to array from set
        	    	String[] irsKeyEntries = irsKeySet.toArray(new String[irsKeySet.size()]); 
	    	
	    	
        	    	//Changes for 999 limitation of SQL IN clause : Start
			final List<LiteracyProExportData> beanList = new ArrayList<LiteracyProExportData>();
			// ExecutorService executor = Executors.newFixedThreadPool(4);
				
			int inClauselimit = 999;
			int loopCounters = irsKeyEntries.length / inClauselimit;
			if ((irsKeyEntries.length % inClauselimit) > 0) {
				loopCounters = loopCounters + 1;
			}

			for (int counter = 0; counter < loopCounters; counter++) {
			    String[] irsData = null;
				String inClause = "";
				if ((counter + 1) != loopCounters) {
				    	irsData = new String[inClauselimit];
					System.arraycopy(irsKeyEntries, (counter * inClauselimit), irsData, 0, inClauselimit);
				} else {
					int count = irsKeyEntries.length % inClauselimit;
					irsData = new String[count];
					System.arraycopy(irsKeyEntries, ((loopCounters - 1) * inClauselimit), irsData, 0, count);
				}
				inClause = generateInClauseForBulkDownload(irsData);
				final String contentfact = "(TBCONFACT.SESSIONID, TBCONFACT.STUDENTID) IN "+inClause;
				final String compositefact = "(TBCOMP.SESSIONID, TBCOMP.STUDENTID) IN "+inClause;
				System.out.println("("+rand+")Calling getRostersScoreData(): " + (counter+1) + " of " + loopCounters);
				LiteracyProExportData[] irsScoreData = getRostersScoreData(contentfact, compositefact);
				beanList.addAll(Arrays.asList(irsScoreData));
				
			}
			System.out.println("("+rand+")OAS array length :: "+oasData.length);
			System.out.println("("+rand+")IRS array length :: "+beanList.size());
			data = mergeLiteracyProExportData(oasData, beanList);
			Arrays.sort(data);
		    
			System.out.println("("+rand+")Total merged array length :: "+data.length);
			// create LiteracyProExportRequest object and update in database
			byte[] byteArrContent = LayoutUtil.getLiteracyProExportDataBytes(data);
			literacyRequestObj.setFileName(orgNodeId);
			byte[] zipData = LayoutUtil.zipBytes(literacyRequestObj.getFileName(), byteArrContent);
			//Blob blobContent = oasCon.createBlob();
			//blobContent.setBytes(1, zipData);
			//literacyRequestObj.setFileContent(zipData);
			updateBulkReportDataEntry(literacyRequestObj,zipData);
					    
		} catch (Exception e) {
			e.printStackTrace();
			try {
			    //LiteracyProExportRequest literacyRequestObj = new LiteracyProExportRequest();
			    literacyRequestObj.setStatus("Failed");
			    literacyRequestObj.setMessage(e.getMessage());
			    updateBulkReportRequestDataErr(literacyRequestObj);
			} catch (BulkReportExportException e1) {
			    // TODO Auto-generated catch block
			    e1.printStackTrace();
			}
		} finally {
			close(oasCon);
			close(irsCon);
		}
		System.out.println("("+rand+")Thread execution time: " + (System.currentTimeMillis() - startTime) + " milliseconds");
		System.out.println("("+rand+")------------------------Thread executed successfully-------------------------");
	}
	
	/**
	 * 
	 * @return
	 * @throws BulkReportExportException
	 */
	private LiteracyProExportData[] getReportLevelMapper() throws BulkReportExportException {
	    	System.out.println("("+rand+")Start: LiteracyExportProcessor.getReportLevelMapper(Query with oasCon)");
	    	long startTime = System.currentTimeMillis();
		List<LiteracyProExportData> literacyProExportList = new ArrayList<LiteracyProExportData>();
		PreparedStatement ps = null;
		ResultSet rset = null;
		
		try {
			ps = oasCon.prepareStatement(REPORT_LEVEL_MAPPER);
			rset = ps.executeQuery();
			
			while(rset.next()){
				LiteracyProExportData literacyProExportObj = new LiteracyProExportData();
				literacyProExportObj.setSubtestProdMapper(rset.getString(1));
				literacyProExportObj.setReportingLevelId(rset.getString(2));
				literacyProExportList.add(literacyProExportObj);
			}
		} catch (SQLException se) {
			BulkReportExportException oe = new BulkReportExportException("LiteracyExportProcessor: getReportLevelMapper : " + se.getMessage());
			oe.setStackTrace(se.getStackTrace());
			throw oe;
		} finally {
			close(ps,rset);
		}
		System.out.println("("+rand+")End: LiteracyExportProcessor.getReportLevelMapper(): " + (System.currentTimeMillis() - startTime) + " milliseconds");
		return literacyProExportList.toArray(new LiteracyProExportData[literacyProExportList.size()]);
	}
	
	/**
	 * 
	 * @param contentfact
	 * @param compositefact
	 * @return
	 * @throws BulkReportExportException
	 */
	private LiteracyProExportData[] getRostersScoreData(String contentfact, String compositefact) throws BulkReportExportException {
	    	System.out.println("("+rand+")Start: LiteracyExportProcessor.getRostersScoreData(Query with irsCon)");
	    	long startTime = System.currentTimeMillis();
		List<LiteracyProExportData> literacyProExportList = new ArrayList<LiteracyProExportData>();
		PreparedStatement ps = null;
		ResultSet rset = null;
		try {
		    	String sql = ROSTER_SCORE_QUERY.replaceAll("#CONTENTCLAUSE#", contentfact).replaceAll("#COMPOSITECLAUSE#", compositefact);
		    	// System.out.println(sql);
			ps = irsCon.prepareStatement(sql);
			rset = ps.executeQuery();
			while(rset.next()){
				LiteracyProExportData literacyProExportObj = new LiteracyProExportData();
				literacyProExportObj.setSubtest(rset.getString(1));
				literacyProExportObj.setSessionID(rset.getString(2));
				literacyProExportObj.setOasStudentId(rset.getString(3));
				literacyProExportObj.setScaledScore(rset.getString(4));
				literacyProExportObj.setGLE(rset.getString(5));
				literacyProExportObj.setReportingLevelId(rset.getString(6));
				literacyProExportList.add(literacyProExportObj);
			}
		} catch (SQLException se) {
			BulkReportExportException oe = new BulkReportExportException("LiteracyExportProcessor: getRostersScoreData : " + se.getMessage());
			oe.setStackTrace(se.getStackTrace());
			throw oe;
		} finally {
			close(ps,rset);
		}
		System.out.println("("+rand+")End: LiteracyExportProcessor.getRostersScoreData(): " + (System.currentTimeMillis() - startTime) + " milliseconds");
		return literacyProExportList.toArray(new LiteracyProExportData[literacyProExportList.size()]);
	}

	/**
	 * 
	 * @param orgNodeId
	 * @param searchCriteria
	 * @param customerId
	 * @return
	 * @throws BulkReportExportException
	 */
	private LiteracyProExportData[] getBulkReportCSVData(Integer orgNodeId, String searchCriteria, Integer customerId) throws BulkReportExportException{
	    	System.out.println("("+rand+")Start: LiteracyExportProcessor.getBulkReportCSVData(["+orgNodeId+"], ["+searchCriteria+"], ["+customerId+"])");
	    	System.out.println("("+rand+")Query with oasCon");
	    	long startTime = System.currentTimeMillis();
		List<LiteracyProExportData> literacyProExportList = new ArrayList<LiteracyProExportData>();
		PreparedStatement ps = null;
		ResultSet rset = null;
		StringBuilder sql = new StringBuilder(ROSTER_FETCH_QUERY);
		if(searchCriteria!= null && !searchCriteria.isEmpty()){
			sql.append(" ").append(searchCriteria);
		}
		try {
			ps = oasCon.prepareStatement(sql.toString());
			ps.setInt(1, orgNodeId);
			ps.setInt(2, customerId);
			rset = ps.executeQuery();
			while(rset.next()){
				LiteracyProExportData literacyProExportObj = new LiteracyProExportData();
				literacyProExportObj.setRosterId(rset.getString(1));
				literacyProExportObj.setSessionID(rset.getString(2));
				literacyProExportObj.setOasStudentId(rset.getString(3));
				literacyProExportObj.setStudentID(rset.getString(4));
				literacyProExportObj.setLastName(rset.getString(5));
				literacyProExportObj.setMiddleName(rset.getString(6));
				literacyProExportObj.setFirstName(rset.getString(7));
				literacyProExportObj.setDateofBirth(rset.getString(8));
				literacyProExportObj.setGender(rset.getString(9));
				literacyProExportObj.setAssessmentDate(rset.getString(10));
				literacyProExportObj.setInstrument(rset.getString(11));
				literacyProExportObj.setForm(rset.getString(12));
				literacyProExportObj.setLvl(rset.getString(13));
				literacyProExportObj.setSubtest(rset.getString(14));
				literacyProExportObj.setSessionName(rset.getString(15));
				literacyProExportObj.setProductId(rset.getString(16));
				literacyProExportObj.setSubtestId(rset.getString(17));
				literacyProExportList.add(literacyProExportObj);
			}
		} catch (SQLException se) {
			BulkReportExportException oe = new BulkReportExportException("LiteracyExportProcessor: getBulkReportCSVData : " + se.getMessage());
			oe.setStackTrace(se.getStackTrace());
			throw oe;
		} finally {
			close(ps,rset);
		}
		System.out.println("("+rand+")End: LiteracyExportProcessor.getBulkReportCSVData(): " + (System.currentTimeMillis() - startTime) + " milliseconds");
		return literacyProExportList.toArray(new LiteracyProExportData[literacyProExportList.size()]);
	}

	/**
	 * 
	 * @param literacyRequestObj
	 * @param zipData 
	 * @throws BulkReportExportException
	 */
	private void updateBulkReportDataEntry(LiteracyProExportRequest literacyRequestObj, byte[] zipData) throws BulkReportExportException {
	    	System.out.println("("+rand+")Start: LiteracyExportProcessor.updateBulkReportDataEntry()");
		literacyRequestObj.setStatus("Completed");
		literacyRequestObj.setMessage("Process Completed");
		updateBulkReportRequestData(literacyRequestObj, zipData);
		System.out.println("("+rand+")End: LiteracyExportProcessor.updateBulkReportDataEntry()");
	}

	/**
	 * 
	 * @param literacyRequestObj
	 * @param zipData 
	 * @throws BulkReportExportException
	 */
	private void updateBulkReportRequestData(LiteracyProExportRequest literacyRequestObj, byte[] zipData) throws BulkReportExportException{
	    	System.out.println("("+rand+")Start: LiteracyExportProcessor.updateBulkReportRequestData(Query with oasCon)");
	    	long startTime = System.currentTimeMillis();
		PreparedStatement ps = null;
		try {
			ps = oasCon.prepareStatement(UPDATE_EXPORT_REQUEST);
			ps.setString(1, literacyRequestObj.getStatus());
			ps.setString(2, literacyRequestObj.getMessage());
			//int blobLength = (int) literacyRequestObj.getFileContent().length();
			ps.setBytes(3, zipData);//.getBytes(1, blobLength));
			ps.setLong(4, literacyRequestObj.getExportRequestId());
			ps.executeUpdate();
			oasCon.commit();
		} catch (SQLException se) {
			BulkReportExportException bre = new BulkReportExportException("LiteracyExportProcessor: updateBulkReportRequestData : " + se.getMessage());
			bre.setStackTrace(se.getStackTrace());
			throw bre;
		} finally {
			close(ps);
		}
		System.out.println("("+rand+")End: LiteracyExportProcessor.updateBulkReportRequestData(): " + (System.currentTimeMillis() - startTime) + " milliseconds");
	}
	
	private void updateBulkReportRequestDataErr(LiteracyProExportRequest literacyRequestObj) throws BulkReportExportException{
	    	System.out.println("("+rand+")Start: LiteracyExportProcessor.updateBulkReportRequestDataErr(Query with oasCon)");
	    	long startTime = System.currentTimeMillis();
		PreparedStatement ps = null;
		try {
			ps = oasCon.prepareStatement(UPDATE_EXPORT_REQUEST_ERR);
			ps.setString(1, literacyRequestObj.getStatus());
			ps.setString(2, literacyRequestObj.getMessage());
			ps.setLong(3, literacyRequestObj.getExportRequestId());
			ps.executeUpdate();
			oasCon.commit();
		} catch (SQLException se) {
			BulkReportExportException bre = new BulkReportExportException("LiteracyExportProcessor: updateBulkReportRequestDataErr : " + se.getMessage());
			bre.setStackTrace(se.getStackTrace());
			throw bre;
		} finally {
			close(ps);
		}
		System.out.println("("+rand+")End: LiteracyExportProcessor.updateBulkReportRequestDataErr(): " + (System.currentTimeMillis() - startTime) + " milliseconds");
	}

	/**
	 * 
	 * @param oasData
	 * @param irsDataList
	 * @return
	 */
	private LiteracyProExportData[] mergeLiteracyProExportData(LiteracyProExportData[] oasData, List<LiteracyProExportData> irsDataList) {
	    List<LiteracyProExportData> finalList = new ArrayList<LiteracyProExportData>(); 
	    List<LiteracyProExportData> oasDataList = Arrays.asList(oasData);
	    for(LiteracyProExportData irsBean : irsDataList) {
		
		LiteracyProExportData oasBean = getLiteracyProExportBean(oasDataList, irsBean);
		if(oasBean == null) {
		    oasBean = getLiteracyProExportBeanUnmapped(oasDataList, irsBean);
		}
		if(oasBean != null) {
		    	oasBean.setLiteracyProExportDataOAS(irsBean);
		    	finalList.add(oasBean);
		}
	    }
	    return finalList.toArray(new LiteracyProExportData[irsDataList.size()]);
	}
	
	/**
	 * 
	 * @param list
	 * @param bean
	 * @return
	 */
	private LiteracyProExportData getLiteracyProExportBean(List<LiteracyProExportData> list, LiteracyProExportData bean) {
	    LiteracyProExportData item = null;
	    for (LiteracyProExportData data : list) {
		if(bean.getReportingLevelId().equals(data.getReportingLevelId())) {
		    item = data;
		    break;
		}
	    }
	    return item;
	}
	
	/**
	 * 
	 * @param oasList
	 * @param irsBean
	 * @return
	 */
	private LiteracyProExportData getLiteracyProExportBeanUnmapped(List<LiteracyProExportData> oasList, LiteracyProExportData irsBean) {
	    LiteracyProExportData item = null;
	    for (LiteracyProExportData oasBean : oasList) {
		if("4008".equals(oasBean.getProductId())) {
		    item = new LiteracyProExportData(oasBean);
		} else {
        		if(irsBean.getOasStudentId().equals(oasBean.getOasStudentId()) && irsBean.getSessionID().equals(oasBean.getSessionID())) {
        		    item = new LiteracyProExportData(oasBean);
        		    item.setLvl("");
        		    break;
        		}
		}
	    }
	    return item;
	}
	
	/**
	 * 
	 * @param list
	 * @param bean
	 * @return
	 */
	private LiteracyProExportData getReportLevelMapperBean(LiteracyProExportData[] list, LiteracyProExportData bean) {
	    LiteracyProExportData item = null;
	    String prodSubtestId = bean.getProductId()+bean.getSubtestId();
	    for (LiteracyProExportData data : list) {
	    	if(prodSubtestId.equals(data.getSubtestProdMapper())) {
	    		item = data;
	    		break;
	    	}
	    }
	    return item;
	}

	/**
	 * Generate In clause for IRS scoring fetch query
	 * @param keyArray
	 * @return
	 */
	private String generateInClauseForBulkDownload(String[] keyArray) {
	    StringBuilder temp = new StringBuilder("");
		String tempString = "";
		for (String key : keyArray) {
			String[] entries = new String[2];
			if(key != null) {
				entries = key.split("~");
			    temp.append("("+entries[0]+", "+entries[1]+"),");
			}
		}
		tempString = temp.toString();
		if(tempString.length()>0){
			tempString = tempString.substring(0, temp.length() - 1);
			tempString = "( "+ tempString+")";
		}
		return tempString;
	}
	
	/**
	 * Get OAS DB connection
	 * @param isCommitable
	 * @return
	 * @throws CTBBusinessException
	 */
	private static Connection openOASDBcon(boolean isCommitable)
			throws CTBBusinessException {
		Connection conn = null;
		try {
			DataSource ds = locateDataSource(oasDtataSourceJndiName);
			conn = ds.getConnection();
			if (isCommitable) {
				conn.setAutoCommit(false);
			}else{
				conn.setAutoCommit(true);
			}
		} catch (NamingException e) {
			System.err.println("NamingException:"
					+ "JNDI name for oas datasource does not exists.");
			throw new CTBBusinessException("NamingException:"
					+ "JNDI name for oas datasource does not exists.");
		} catch (SQLException e) {
			System.err.println("SQLException:"
					+ "while getting oas database connection.");
			throw new CTBBusinessException("SQLException:"
					+ "while getting oas database connection.");
		} catch (Exception e) {
			System.err.println("Exception:"
					+ "while getting oas database connection.");
			throw new CTBBusinessException("Exception:"
					+ "while getting oas database connection.");
		}

		return conn;

	}
	
	/**
	 * Get IRS DB connection
	 * @param isCommitable
	 * @return
	 * @throws CTBBusinessException
	 */
	private static Connection openIRSDBcon(boolean isCommitable)
			throws CTBBusinessException {

		Connection conn = null;
		try {
			DataSource ds = locateDataSource(irsDtataSourceJndiName);
			conn = ds.getConnection();
			if (isCommitable) {
				conn.setAutoCommit(false);
			}
		} catch (NamingException e) {
			System.err.println("NamingException:"
					+ "JNDI name for irs datasource does not found.");
			throw new CTBBusinessException("NamingException:"
					+ "JNDI name for irs datasource does not found.");
		} catch (SQLException e) {
			System.err.println("SQLException:"
					+ "while getting irs database connection.");
			throw new CTBBusinessException("NamingException:"
					+ "while getting irs database connection.");
		} catch (Exception e) {
			System.err.println("Exception:"
					+ "while getting irs database connection.");
			throw new CTBBusinessException("Exception:"
					+ "while getting  irs database connection.");
		}

		return conn;
	}
	
	
	/**
	 * 
	 * @param jndiName
	 * @return
	 * @throws NamingException
	 */
	private static DataSource locateDataSource(String jndiName ) throws NamingException{
		Context ctx = new InitialContext();
		DataSource ds =  (DataSource) ctx.lookup(jndiName);
		return ds;
	}
	
	/**
	 * Close connection
	 * @param con
	 */
	private static void close(Connection con) {
		if (con != null) {
			try {
				if(!con.getAutoCommit())
					con.rollback();
				con.close();
			} catch (SQLException e) {
				// do nothing
			}
		}

	}

	/**
	 * Close statement
	 * @param st
	 */
	private static void close(Statement st) {
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				// do nothing
			}
		}

	}

	/**
	 * Close result set
	 * @param rs
	 */
	private static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// do nothing
			}
		}

	}
	
	/**
	 * Close statement and result set
	 * @param st
	 * @param rs
	 */
	private static void close(Statement st, ResultSet rs) {
		close(rs);
		close(st);

	}
	
	/**
	 * Close connection, statement and result set 
	 * @param con
	 * @param st
	 * @param rs
	 */
	private static void close(Connection con, Statement st, ResultSet rs) {
		close(rs);
		close(st);
		close(con);

	}

	/**
	 * Close connection and statement
	 * @param con
	 * @param st
	 */
	private static void close(Connection con, Statement st) {
		close(st);
		close(con);
		
	}
}
