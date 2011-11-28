package com.ctb.testSessionInfo.utils;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.ctb.bean.testAdmin.TestElement;
import com.ctb.bean.testAdmin.TestElementData;
import com.ctb.bean.testAdmin.TestProduct;
import com.ctb.control.db.ItemSet;
import com.ctb.control.testAdmin.ScheduleTest;
import com.ctb.exception.CTBBusinessException;
import com.ctb.testSessionInfo.data.SubtestVO;
import com.ctb.testSessionInfo.data.TestVO;





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

	private List<TestVO> buildTestList(String userName, TestElementData ted, ScheduleTest scheduleTest, String showLevelOrGrade) throws CTBBusinessException
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

            Integer itemSetId = tes[i].getItemSetId();
            TestElementData suTed = scheduleTest.getSchedulableUnitsForTestWithBlankAccessCode(userName, itemSetId, new Boolean(true), null, null, null);
            TestElement [] usTes = suTed.getTestElements();
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
                subtestList.add(subtestVO);
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
            
            TestVO testVO = new TestVO(tes[i].getItemSetId(), tes[i].getItemSetName(), 
                    level, duration, subtestList);
                    
            if(accessCode!=null){
            	testVO.setAccessCode(accessCode);
            }
            //testVO.setForms(tes[i].getForms());
            
            
            
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
                    
            result.add(testVO);
        }
        return result;
        
    }
	 private List<TestVO> getTestsForProductForUser(String userName, Integer productId, ScheduleTest scheduleTest, String showLevelOrGrade)
     throws CTBBusinessException
     {
		 
		 TestElementData ted = scheduleTest.getTestsForProduct(userName, productId, null, null, null); 
		 List<TestVO> testList = buildTestList(userName, ted,scheduleTest, showLevelOrGrade);
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
				ProductBean prod = new ProductBean(val.getProductId(), val.getProductName(), getLevelDropList(val.getLevels()), getGradeDropList(val.getGrades()));
				
				
				prod.setAcknowledgmentsURL(val.getAcknowledgmentsURL());
				
				
				prod.setProductType(TestSessionUtils.getProductType(val.getProductType()));
				
				prod.setTabeProduct(TestSessionUtils.isTabeProduct(prod.getProductType()));
				prod.setTabeLocatorProduct(TestSessionUtils.isTabeLocatorProduct(prod.getProductType()));
				
				
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
				
				List<TestVO> testSession= getTestsForProductForUser(userName,val.getProductId(), scheduleTest, prod.getShowLevelOrGrade());
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


	
	@SuppressWarnings("unchecked")
	private void populateTimeZone() {
		List<String> timeZoneList = DateUtils.getTimeZoneList(); 
		for(String val : timeZoneList){
			testZoneDropDownList.add(new ObjectIdName(val,val));
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
			this.accessCodeList = scheduleTest.getFixedNoAccessCode(maxSubtestCount);
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
	
	
}


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
	
	
	public ProductBean(Integer productId, String productName) {
		this.productId = productId;
		this.productName = productName;
	}

	
	public ProductBean(Integer productId, String productName,
			List<ObjectIdName> levelDropList, List<ObjectIdName> gradeDropList) {
		this.productId = productId;
		this.productName = productName;
		this.levelDropDownList = levelDropList;
		if(gradeDropList!=null) 
			this.gradeDropDownList = gradeDropList;
		
		
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


	
	/**
	 * @return the testSessionList
	 */
	public List<TestVO> getTestSessionList() {
		return testSessionList;
	}


}


class ObjectIdName implements Serializable{

	private static final long serialVersionUID = 1L;

	private String id;

	private String name ;
	
	public ObjectIdName (String id, String name) {
		this.id= id;
		this.name =name;
		
	}
}
