package com.ctb.testSessionInfo.utils;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.ctb.bean.testAdmin.LASLicenseNode;
import com.ctb.bean.testAdmin.TestElement;
import com.ctb.bean.testAdmin.TestElementData;
import com.ctb.bean.testAdmin.TestProduct;
import com.ctb.bean.testAdmin.TestSession;
import com.ctb.control.db.ItemSet;
import com.ctb.control.testAdmin.ScheduleTest;
import com.ctb.exception.CTBBusinessException;
import com.ctb.testSessionInfo.data.SubtestVO;
import com.ctb.testSessionInfo.data.TestVO;




@SuppressWarnings( "all")
public class ScheduleTestVo implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final String RD_YES = "Y";
    private static final String RD_NO = "N";
	private List<ProductBean> product = new ArrayList<ProductBean> ();
	private List<ObjectIdName> testZoneDropDownList = new ArrayList<ObjectIdName>();
	private List<ObjectIdName> topNodeDropDownList = new ArrayList<ObjectIdName>();
	private boolean hideProductNameDropDown = false;
	private String selectedProductId;
	private String userTimeZone = "";
	private List<String> accessCodeList = new ArrayList<String>();
	private boolean noTestExists = false;
	private List<String> levelOptions = new ArrayList<String>();// level for subtest
	private boolean isOkAdmin = false;
	private boolean isWVAdmin = false;
	private boolean forceTestBreak = false;
	private Boolean selectGE = false;
	private LASLicenseNode nonZeroActivePO = null;
	Map<String,ArrayList> classHierarchyMap;
	private boolean hasShowRosterAccomAndHierarchy = false;
	private Integer testingWindowDefaultDays = null;
	
	
	/**
	 * @return the classHierarchyMap
	 */
	public Map<String, ArrayList> getClassHierarchyMap() {
		return this.classHierarchyMap;
	}


	/**
	 * @param classHierarchyMap the classHierarchyMap to set
	 */
	public void setClassHierarchyMap(Map<String, ArrayList> classHierarchyMap) {
		this.classHierarchyMap = classHierarchyMap;
	}


	/**
	 * @return the hasShowRosterAccomAndHierarchy
	 */
	public boolean isHasShowRosterAccomAndHierarchy() {
		return this.hasShowRosterAccomAndHierarchy;
	}


	/**
	 * @param hasShowRosterAccomAndHierarchy the hasShowRosterAccomAndHierarchy to set
	 */
	public void setHasShowRosterAccomAndHierarchy(boolean hasShowRosterAccomAndHierarchy) {
		this.hasShowRosterAccomAndHierarchy = hasShowRosterAccomAndHierarchy;
	}
	
	public List<ObjectIdName> getLevelDropList(String[] levels) {
		List<String> result = new ArrayList<String>();
		List<ObjectIdName> levelList = new ArrayList<ObjectIdName> ();
        for (int i=0; i<levels.length; i++)
        {
            if (levels[i] != null)
                result.add(levels[i]);
        }
        Collections.sort(result);
        if (levels.length > 1)
        	levelList.add(new ObjectIdName(FilterSortPageUtils.FILTERTYPE_SHOWALL,FilterSortPageUtils.FILTERTYPE_SHOWALL));
        for (String val : result) {
        	levelList.add(new ObjectIdName(val,val));
        }
        
        return levelList;
		
	}


	public List<ObjectIdName> getGradeDropList(String[] grades) {
		List<String> result = new ArrayList<String>();
		List<ObjectIdName> gradeList = new ArrayList<ObjectIdName> ();
        for (int i=0; i<grades.length; i++)
        {
            if (grades[i] != null)
                result.add(grades[i]);
        }
        Collections.sort(result);
        if (grades.length > 1)
        	gradeList.add(new ObjectIdName(FilterSortPageUtils.FILTERTYPE_SHOWALL,FilterSortPageUtils.FILTERTYPE_SHOWALL));
           
        for (String val : result) {
        	gradeList.add(new ObjectIdName(val,val));
        }
        return gradeList;
		
	}

	private List<TestVO> buildTestList(String userName, TestProduct tp, TestElementData ted, ScheduleTest scheduleTest, String showLevelOrGrade) throws CTBBusinessException
    {
        
        List<TestVO> result = new ArrayList<TestVO>();
        //Add variable for rd_allowable
        String hasRdAllowable = null;
        if (ted == null)
            return result;
        
        TestElement [] tes = ted.getTestElements();
        String level = "";
        //todo: ask Nate to fix bug here.
        for (int i=0; i<tes.length && tes[i]!=null ; i++)
        {
            String accessCode = tes[i].getAccessCode();
            
            List<SubtestVO> subtestList = new ArrayList<SubtestVO>();
            List<SubtestVO>	tdSubtestList = new ArrayList<SubtestVO>();
            Integer itemSetId = tes[i].getItemSetId();
            TestElementData suTed = scheduleTest.getSchedulableUnitsForTestWithBlankAccessCode(userName, itemSetId, new Boolean(true), null, null, null);
            TestElement [] usTes = suTed.getTestElements();
            SubtestVO locatorSubtest = null;
            for (int j=0; j<usTes.length; j++)
            {
                int durationMinutes = usTes[j].getTimeLimit().intValue()/60;
                String duration = (durationMinutes == 0) ? "Untimed" : durationMinutes + " mins";
                SubtestVO subtestVO = new SubtestVO(usTes[j].getItemSetId(),
                                                    String.valueOf(j+1), 
                                                    usTes[j].getItemSetName(), 
                                                    duration, 
                                                    usTes[j].getAccessCode(),
                                                    usTes[j].getSessionDefault(),
                                                    usTes[j].getItemSetForm(),
                                                    false);
                if(locatorSubtest == null && usTes[j].getItemSetName().indexOf("Locator") > 0){
                	locatorSubtest = subtestVO;
                	TestElementData locatorSubtestTD = scheduleTest.getDeliverableUnitsForTestWithBlankAccessCode(userName, subtestVO.getId(), new Boolean(false), null, null, null);
                	TestElement [] testElementTD = locatorSubtestTD.getTestElements();
                	for (int z=0; z<testElementTD.length; z++)
                    {
                        int duraMin = testElementTD[z].getTimeLimit().intValue()/60;
                        String dura = (duraMin == 0) ? "Untimed" : duraMin + " mins";
                        SubtestVO subtestVOforTD = new SubtestVO(testElementTD[z].getItemSetId(),
                                                            String.valueOf(z+1), 
                                                            testElementTD[z].getItemSetName(), 
                                                            dura, 
                                                            testElementTD[z].getAccessCode(),
                                                            testElementTD[z].getSessionDefault(),
                                                            testElementTD[z].getItemSetForm(),
                                                            false);
                        tdSubtestList.add(subtestVOforTD);
                    }
                	locatorSubtest.setSubtestTestTD(tdSubtestList);
                } else {
                	subtestList.add(subtestVO);
                }
            }
    
            int durationMinutes = tes[i].getTimeLimit().intValue()/60;
            String duration = (durationMinutes == 0) ? "Untimed" : durationMinutes + " mins";
    
    
           
           
            if (showLevelOrGrade!=null && showLevelOrGrade.equals("level")) 
                level = tes[i].getItemSetLevel();
            else if (showLevelOrGrade!=null && showLevelOrGrade.equals("grade"))
                level = tes[i].getGrade();
            
            if(level== null) {
            	level = "";
            }
            if(TestSessionUtils.isTabeBatterySurveyProduct(tp.getProductType())){
            	subtestList = getDefaultSubtestsWithoutLocator(subtestList);
            }
            
            TestVO testVO = new TestVO(tes[i].getItemSetId(), tes[i].getItemSetName(), 
                    level, duration, subtestList, locatorSubtest);
                    
            if(accessCode!=null){
            	testVO.setAccessCode(accessCode);
            }
            testVO.setForms(tes[i].getForms());
            
            
            
            //  check whether Randomized Distractor is allowable  of the test    
            hasRdAllowable = tes[i].getIsRandomize();
           
            if ( hasRdAllowable != null) {
                
                
             // set isRandomize valuee in the testVO   
                if ( hasRdAllowable .equals("T")) {
        
                            testVO.setIsRandomize(RD_YES);
        
                    } else if(hasRdAllowable .equals("F")){
        
                            testVO.setIsRandomize(RD_NO);
                    } else {
                    	testVO.setIsRandomize("");
                    }
        
                } 
        
            testVO.setOverrideFormAssignment(tes[i].getOverrideFormAssignmentMethod());
            testVO.setOverrideLoginStartDate(tes[i].getOverrideLoginStartDate());
            testVO.setOverrideLoginEndDate(tes[i].getOverrideLoginEndDate());
            testVO.setOffGradeTestingDisabled(isOffGradeTestingDisabled(tp,ted,tes[i].getItemSetId() ));
            if(tes[i].getOverrideFormAssignmentMethod() != null) {
            	testVO.setFormOperand(tes[i].getOverrideFormAssignmentMethod());
            }else if (tes[i].getForms()!= null && tes[i].getForms().length > 0 ) {
            	testVO.setFormOperand(TestSession.FormAssignment.ROUND_ROBIN);
            } else {
            	testVO.setFormOperand(TestSession.FormAssignment.ROUND_ROBIN);
            }
                    
            result.add(testVO);
        }
        return result;
        
    }
	 private List<SubtestVO> getDefaultSubtestsWithoutLocator(
			List<SubtestVO> srcList) {
		
		 if (srcList == null)
	            return srcList;

	        List resultList = new ArrayList();
	        for (int i=0 ; i<srcList.size() ; i++) {
	            SubtestVO subtest = (SubtestVO)srcList.get(i);
	            //if (subtest.getSessionDefault().equals("T") && (! isLocatorSubtest(subtest))) {
	            if (subtest.getSessionDefault().equals("T") ) {
	                SubtestVO copied = new SubtestVO(subtest);
	                resultList.add(copied);
	            }
	        }

	        //resetSubtestOrder(resultList);

	        return resultList;
	}


	 private List<TestVO> getTestsForProductForUser(TestProduct tp, String userName, Integer productId, ScheduleTest scheduleTest, String showLevelOrGrade)
     throws CTBBusinessException
     {
		 
		 TestElementData ted = scheduleTest.getTestsForProduct(userName, productId, null, null, null); 
		 List<TestVO> testList = buildTestList( userName,tp, ted,scheduleTest, showLevelOrGrade);
		 return testList;
     }
	 private boolean isLasLinkProduct(String productType) {
	     	return "LL".equals(productType);
	     }

	public void populate(String userName, TestProduct[] tps, ItemSet itemSet, ScheduleTest scheduleTest) throws SQLException, CTBBusinessException {
		
		if (tps != null) {
			
			for (TestProduct val : tps) {
				val.setLevels(itemSet.getLevelsForProduct(val.getProductId()));
				if(isLasLinkProduct(val.getProductType())) {
					val.setGrades(val.getLevels());
					
				} else {
					val.setGrades(itemSet.getGradesForProduct(val.getProductId())); 
					
				}
				
				//List<TestVO> testSession= getTestsForProductForUser(userName,val.getProductId(), scheduleTest);
				ProductBean prod = new ProductBean(val.getProductId(), val.getProductName(), getLevelDropList(val.getLevels()), getGradeDropList(val.getGrades()), val.getShowStudentFeedback());
				
				
				prod.setAcknowledgmentsURL(val.getAcknowledgmentsURL());
				
				
				prod.setProductType(TestSessionUtils.getProductType(val.getProductType()));
				
				prod.setTabeProduct(TestSessionUtils.isTabeProduct(prod.getProductType()));
				prod.setTabeLocatorProduct(TestSessionUtils.isTabeLocatorProduct(prod.getProductType()));
				prod.setTabeAdaptiveProduct(TestSessionUtils.isTabeAdaptiveProduct(prod.getProductType()));
				prod.setLasLinksProduct(TestSessionUtils.isLasLinksProduct(prod.getProductType()));
				prod.setTASCProduct(TestSessionUtils.isTASCProduct(prod.getProductType()));
				prod.setTASCReadinessProduct(TestSessionUtils.isTASCReadinessProduct(prod.getProductType()));
				
				
				if (prod.getLevelDropDownList().size() > 0 ) {
					prod.setShowLevelOrGrade( "level");
	            }
	            else if (prod.getGradeDropDownList().size() > 0)
	            {
	            	prod.setShowLevelOrGrade( "grade");
	            	prod.setLevelDropDownList(prod.getGradeDropDownList());
	            } else {
	            	prod.setShowLevelOrGrade( "none");
	            }
				
				
				
				
				if(prod.getLevelDropDownList()==null || prod.getLevelDropDownList().size()<0){
					prod.setHideLevelDropDown(true);
				} else {
					prod.setHideLevelDropDown(false);
				}
				
				List<TestVO> testSession= getTestsForProductForUser(val,userName,val.getProductId(), scheduleTest, prod.getShowLevelOrGrade());
				prod.setTestSessionList(testSession);
				
				product.add(prod);


			}
			
			if(tps.length<=1){
				hideProductNameDropDown = true;
			} else {
				hideProductNameDropDown = false;
			}
			
			populateTimeZone();
		} else {
			hideProductNameDropDown = true;
		}
	}


	
	private Boolean isOffGradeTestingDisabled(TestProduct tp, TestElementData ted, Integer testId) {
        
        if (! tp.getOffGradeTestingDisabled().equals("T"))
        {
            return Boolean.FALSE;
        }

        String[] grades = tp.getGrades();
        if (grades != null)
        {
            for (int i=0; i < grades.length; i++)
            {
                if (grades[i] != null)
                {
                    TestElement[] tes = ted.getTestElements();
                    for (int j=0; j < tes.length && tes[j] != null; j++)
                    {
                        TestElement te = tes[j];
                        if (te.getItemSetId().intValue() == testId.intValue())
                        {
                        	if (isLasLinkProduct(tp.getProductType())) {
	                            if (te.getItemSetLevel() != null) {
	                                return Boolean.TRUE;                                
	                            }
                        	}
                        	else {
	                            if (te.getGrade() != null) {
	                                return Boolean.TRUE;                                
	                            }
                        	}
                        }
                    }
                }
            }                        
        }        
                
        return Boolean.FALSE;
    
	}

	@SuppressWarnings("unchecked")
	public void populateTimeZone() {
		List<String> timeZoneList = DateUtils.getTimeZoneList(); 
		Boolean contains = false;
		ObjectIdName userTimeZoneObj = new ObjectIdName(this.userTimeZone,this.userTimeZone);
		testZoneDropDownList = new ArrayList<ObjectIdName>();
		for(String val : timeZoneList){
			testZoneDropDownList.add(new ObjectIdName(val,val));	
			if(val == userTimeZoneObj.getName()){
				contains = true;
			}
		}
		if(!contains){
			testZoneDropDownList.add(userTimeZoneObj);	
		}		
	}


	/**
	 * @param selectedProductId the selectedProductId to set
	 */
	public void setSelectedProductId(String selectedProductId) {
		this.selectedProductId = selectedProductId;
	}


	public void populateTopOrgnode(Map<Integer, String> topNodesMap) {
		for(Map.Entry<Integer, String> entry : topNodesMap.entrySet()) {
			topNodeDropDownList.add(new ObjectIdName(entry.getKey().toString(), entry.getValue()));
		}
		
	}

	/**
	 * @param userTimeZone the userTimeZone to set
	 */
	public void setUserTimeZone(String userTimeZone) {
		this.userTimeZone = userTimeZone;
	}

	public void populateAccessCode(ScheduleTest scheduleTest) throws CTBBusinessException {
		int maxSubtestCount = 0;
		for(ProductBean pp :product){
			if(pp.getTestSessionList() != null) {
				for ( TestVO tt : pp.getTestSessionList()) {
					if(tt.getSubtestCount()>maxSubtestCount) {
						maxSubtestCount= tt.getSubtestCount();
					}
				}
			}
		}
		
		if(maxSubtestCount>0){
			this.accessCodeList = scheduleTest.getFixedNoAccessCode(maxSubtestCount+2);
		}
		
	}


	
	/**
	 * @return the noTestExists
	 */
	public boolean isNoTestExists() {
		return noTestExists;
	}


	
	/**
	 * @param noTestExists the noTestExists to set
	 */
	public void setNoTestExists(boolean noTestExists) {
		this.noTestExists = noTestExists;
	}

	/**
	 * @return the product
	 */
	public List<ProductBean> getProduct() {
		return product;
	}


	@Deprecated
	public void populateTestIdToTestMap(Map<Integer, TestVO> idToTestMap) {
		for(ProductBean productBean :  this.product) {
			for(TestVO testVO : productBean.getTestSessionList()) {
				idToTestMap.put(testVO.getId(), testVO);
			}
		}
		
	}

	@SuppressWarnings("deprecation")
	public void populateDefaultDateAndTime(String timeZone,String testletSessionEndDate) {
		Date now = new Date(System.currentTimeMillis());
        Date today = com.ctb.util.DateUtils.getAdjustedDate(now, TimeZone.getDefault().getID(), timeZone, now);
        Date tomorrow = com.ctb.util.DateUtils.getAdjustedDate(new Date(now.getTime() + (24 * 60 * 60 * 1000)), TimeZone.getDefault().getID(), timeZone, now);
        for(ProductBean productBean :  this.product) {
	        for(TestVO testVO : productBean.getTestSessionList()) {
	        	if (testVO.getOverrideLoginStartDate() != null && !(DateUtils.isBeforeToday(testVO.getOverrideLoginStartDate(), timeZone ))) {
	        		Date loginEndDate = (Date)testVO.getOverrideLoginStartDate().clone();
	                loginEndDate.setDate(loginEndDate.getDate() + 1);
	                testVO.setStartDate(DateUtils.formatDateToDateString(testVO.getOverrideLoginStartDate()));
	                testVO.setEndDate(DateUtils.formatDateToDateString(loginEndDate));
	        	
	        	} else {
	        		testVO.setStartDate(DateUtils.formatDateToDateString(today));
	        		if(productBean.getProductId() == 4201){
	        			testVO.setEndDate(testletSessionEndDate);
	        		}else{
	        			testVO.setEndDate(DateUtils.formatDateToDateString(tomorrow));
	        		}
	        	}
	        	
	        	if(testVO.getOverrideLoginEndDate()!= null && !(DateUtils.isAfterToday(testVO.getOverrideLoginEndDate(), timeZone )) ) {
	        		testVO.setStartDate(DateUtils.formatDateToDateString(today)); // setting today as start day
	        		testVO.setEndDate(DateUtils.formatDateToDateString(today));    // setting today as end day
	        		testVO.setMinLoginEndDate(DateUtils.formatDateToDateString(testVO.getOverrideLoginEndDate()));
	        		
	        	} else if(testVO.getOverrideLoginEndDate()!= null ){
	        		testVO.setMinLoginEndDate(DateUtils.formatDateToDateString(testVO.getOverrideLoginEndDate()));
	        	}
	        }
        }
		
	}
	
	@SuppressWarnings("deprecation")
	public void populateDefaultDateAndTime(String timeZone, Integer defualtDay) {
		Date now = new Date(System.currentTimeMillis());
        Date today = com.ctb.util.DateUtils.getAdjustedDate(now, TimeZone.getDefault().getID(), timeZone, now);
        Date tomorrow = com.ctb.util.DateUtils.getAdjustedDate(new Date(now.getTime() + ((24*(defualtDay.intValue()-1)) * 60 * 60 * 1000)), TimeZone.getDefault().getID(), timeZone, now);
        for(ProductBean productBean :  this.product) {
	        for(TestVO testVO : productBean.getTestSessionList()) {
	        	if (testVO.getOverrideLoginStartDate() != null && !(DateUtils.isBeforeToday(testVO.getOverrideLoginStartDate(), timeZone ))) {
	        		Date loginEndDate = (Date)testVO.getOverrideLoginStartDate().clone();
	                loginEndDate.setDate(loginEndDate.getDate() + 1);
	                testVO.setStartDate(DateUtils.formatDateToDateString(testVO.getOverrideLoginStartDate()));
	                testVO.setEndDate(DateUtils.formatDateToDateString(loginEndDate));
	        	
	        	} else {
	        		testVO.setStartDate(DateUtils.formatDateToDateString(today));
	        		testVO.setEndDate(DateUtils.formatDateToDateString(tomorrow));
	        	}
	        	
	        	if(testVO.getOverrideLoginEndDate()!= null && !(DateUtils.isAfterToday(testVO.getOverrideLoginEndDate(), timeZone )) ) {
	        		testVO.setStartDate(DateUtils.formatDateToDateString(today)); // setting today as start day
	        		testVO.setEndDate(DateUtils.formatDateToDateString(today));    // setting today as end day
	        		testVO.setMinLoginEndDate(DateUtils.formatDateToDateString(testVO.getOverrideLoginEndDate()));
	        		
	        	} else if(testVO.getOverrideLoginEndDate()!= null ){
	        		testVO.setMinLoginEndDate(DateUtils.formatDateToDateString(testVO.getOverrideLoginEndDate()));
	        	}
	        }
        }
		
	}
	
	public void populateLevelOptions() {
		levelOptions = new ArrayList<String>();
		levelOptions.add("E");
		levelOptions.add("M");
		levelOptions.add("D");
		levelOptions.add("A");
       
		
	}

	public List<String> getLevelOptions() {
		return levelOptions;
	}


	public void setLevelOptions(List<String> levelOptions) {
		this.levelOptions = levelOptions;
	}


	public boolean isOkAdmin() {
		return isOkAdmin;
	}


	public void setOkAdmin(boolean isOkAdmin) {
		this.isOkAdmin = isOkAdmin;
	}


	public boolean isForceTestBreak() {
		return forceTestBreak;
	}


	public void setForceTestBreak(boolean forceTestBreak) {
		this.forceTestBreak = forceTestBreak;
	}

	public Boolean isSelectGE() {
		return selectGE;
	}

	public void setSelectGE(Boolean selectGE) {
		this.selectGE = selectGE;
	}


	/**
	 * @return the nonZeroActivePO
	 */
	public LASLicenseNode getNonZeroActivePO() {
		return nonZeroActivePO;
	}


	/**
	 * @param nonZeroActivePO the nonZeroActivePO to set
	 */
	public void setNonZeroActivePO(LASLicenseNode nonZeroActivePO) {
		this.nonZeroActivePO = nonZeroActivePO;
	}


	/**
	 * @return the testingWindowDefaultDays
	 */
	public Integer getTestingWindowDefaultDays() {
		return testingWindowDefaultDays;
	}


	/**
	 * @param testingWindowDefaultDays the testingWindowDefaultDays to set
	 */
	public void setTestingWindowDefaultDays(Integer testingWindowDefaultDays) {
		this.testingWindowDefaultDays = testingWindowDefaultDays;
	}


	public boolean isWVAdmin() {
		return isWVAdmin;
	}


	public void setWVAdmin(boolean isWVAdmin) {
		this.isWVAdmin = isWVAdmin;
	}
}

@SuppressWarnings( "all")
class ProductBean implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer productId;

	private String productName ;

	private List<ObjectIdName> levelDropDownList = new ArrayList<ObjectIdName> ();

	private List<ObjectIdName> gradeDropDownList = new ArrayList<ObjectIdName> ();

	private List<TestVO> testSessionList = new ArrayList<TestVO>();

	private String showLevelOrGrade = "level";
	
	private String acknowledgmentsURL  ;
	
	private boolean hideLevelDropDown = false;
	
	private String productType = TestSessionUtils.GENERIC_PRODUCT_TYPE;
	
	private boolean isTabeProduct = false;
	
	private boolean isTabeLocatorProduct = false;
	
	private boolean isTabeAdaptiveProduct = false;
	
	private Boolean showStudentFeedback = false;
	
	private boolean isLasLinksProduct = false;

	private boolean isTASCProduct = false;
	
	private boolean isTASCReadinessProduct = false;
	
	public ProductBean(Integer productId, String productName) {
		this.productId = productId;
		this.productName = productName;
	}

	
	public ProductBean(Integer productId, String productName,
			List<ObjectIdName> levelDropList, List<ObjectIdName> gradeDropList, String showStudentFeedback) {
		this.productId = productId;
		this.productName = productName;
		this.levelDropDownList = levelDropList;
		if(gradeDropList!=null) 
			this.gradeDropDownList = gradeDropList;
		this.showStudentFeedback = new Boolean(showStudentFeedback.equals("T"));
		
		
	}


	
	/**
	 * @param showLevelOrGrade the showLevelOrGrade to set
	 */
	public void setShowLevelOrGrade(String showLevelOrGrade) {
		this.showLevelOrGrade = showLevelOrGrade;
	}


	
	/**
	 * @param acknowledgmentsURL the acknowledgmentsURL to set
	 */
	public void setAcknowledgmentsURL(String acknowledgmentsURL) {
		this.acknowledgmentsURL = acknowledgmentsURL;
	}


	
	/**
	 * @param hideLevelDropDown the hideLevelDropDown to set
	 */
	public void setHideLevelDropDown(boolean hideLevelDropDown) {
		this.hideLevelDropDown = hideLevelDropDown;
	}


	
	/**
	 * @return the levelDropDownList
	 */
	public List<ObjectIdName> getLevelDropDownList() {
		return levelDropDownList;
	}


	
	/**
	 * @param productType the productType to set
	 */
	public void setProductType(String productType) {
		this.productType = productType;
	}


	
	/**
	 * @param isTabeProduct the isTabeProduct to set
	 */
	public void setTabeProduct(boolean isTabeProduct) {
		this.isTabeProduct = isTabeProduct;
	}

	/**
	 * @param isLasLinksProduct the isLasLinksProduct to set
	 */
	public void setLasLinksProduct(boolean isLasLinksProduct) {
		this.isLasLinksProduct = isLasLinksProduct;
	}

	/**
	 * @param isTASCProduct the isLasLinksProduct to set
	 */
	public void setTASCProduct(boolean isTASCProduct) {
		this.isTASCProduct = isTASCProduct;
	}	

	/**
	 * @param isTASCReadinessProduct to set
	 */
	public void setTASCReadinessProduct(boolean isTASCReadinessProduct) {
		this.isTASCReadinessProduct = isTASCReadinessProduct;
	}
	
	/**
	 * @return the productType
	 */
	public String getProductType() {
		return productType;
	}


	
	/**
	 * @param gradeDropDownList the gradeDropDownList to set
	 */
	public void setGradeDropDownList(List<ObjectIdName> gradeDropDownList) {
		this.gradeDropDownList = gradeDropDownList;
	}


	
	/**
	 * @return the gradeDropDownList
	 */
	public List<ObjectIdName> getGradeDropDownList() {
		return gradeDropDownList;
	}


	
	/**
	 * @param levelDropDownList the levelDropDownList to set
	 */
	public void setLevelDropDownList(List<ObjectIdName> levelDropDownList) {
		this.levelDropDownList = levelDropDownList;
	}


	
	/**
	 * @param isTabeLocatorProduct the isTabeLocatorProduct to set
	 */
	public void setTabeLocatorProduct(boolean isTabeLocatorProduct) {
		this.isTabeLocatorProduct = isTabeLocatorProduct;
	}


	
	/**
	 * @return the isTabeProduct
	 */
	public boolean isTabeProduct() {
		return isTabeProduct;
	}


	
	/**
	 * @return the isTabeLocatorProduct
	 */
	public boolean isTabeLocatorProduct() {
		return isTabeLocatorProduct;
	}


	/**
	 * @return the isLasLinksProduct
	 */
	public boolean isLasLinksProduct(){
		return isLasLinksProduct;
	}

	/**
	 * @return the isTASCProduct
	 */
	public boolean isTASCProduct(){
		return isTASCProduct;
	}
	
	/**
	 * @return the isTASCReadinessProduct
	 */
	public boolean isTASCReadinessProduct(){
		return isTASCReadinessProduct;
	}
	
	/**
	 * @param testSessionList the testSessionList to set
	 */
	public void setTestSessionList(List<TestVO> testSessionList) {
		this.testSessionList = testSessionList;
	}


	
	/**
	 * @return the showLevelOrGrade
	 */
	public String getShowLevelOrGrade() {
		return showLevelOrGrade;
	}

	public Integer getProductId(){
		return productId;
	}
	
	/**
	 * @return the testSessionList
	 */
	public List<TestVO> getTestSessionList() {
		return testSessionList;
	}


	public boolean isTabeAdaptiveProduct() {
		return isTabeAdaptiveProduct;
	}


	public void setTabeAdaptiveProduct(boolean isTabeAdaptiveProduct) {
		this.isTabeAdaptiveProduct = isTabeAdaptiveProduct;
	}


}

@SuppressWarnings( "all")
class ObjectIdName implements Serializable{

	private static final long serialVersionUID = 1L;

	private String id;

	private String name ;
	
	public ObjectIdName (String id, String name) {
		this.id= id;
		this.name =name;
		
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

}
