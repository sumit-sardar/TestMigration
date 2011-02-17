package com.ctb.xmlProcessing.assessment;

import java.util.ArrayList;

import java.util.List;


import com.ctb.hibernate.persist.ItemSetSampleSetRecord;
import com.ctb.xmlProcessing.ItemSet;

/**
 * @author wmli
 */
public class AssessmentHolder implements ItemSet {
    private String frameworkCode = "";
    private String productDisplayName = "";
    private String testName = "";
    private String version = "";
    private String itemSetLevel = "";
    private String grade = "";
    private int timeLimit = 0;
    private int breakTime = 0;
    private String itemSetDisplayName = "";
    private String itemSetDescription = "";
    private String itemSetForm = "";
    private Long productId = null;
    private String commodityCode = "";
    private String extTstItemSetId = "";
    private String contentArea = "";
    private List subTestIds = new ArrayList();
    public Boolean isAddOn = Boolean.FALSE;
    private List itemSampleSets = new ArrayList();


    public AssessmentHolder(Long productId, String frameworkCode, String productDisplayName,
            String extTstItemSetId, String testName, String version, String itemSetLevel,
            String grade, int timeLimit, int breakTime, String itemSetDisplayName,
            String itemSetDescription, String itemSetForm, String commodityCode) {

        this.frameworkCode = frameworkCode;
        this.productDisplayName = productDisplayName;
        this.extTstItemSetId = extTstItemSetId;
        this.testName = testName;
        this.version = version;
        this.itemSetLevel = itemSetLevel;
        this.grade = grade;
        this.timeLimit = timeLimit;
        this.breakTime = breakTime;
        this.itemSetDisplayName = itemSetDisplayName;
        this.itemSetDescription = itemSetDescription;
        this.itemSetForm = itemSetForm;
        this.productId = productId;
        this.commodityCode = commodityCode;
    }
    
    public void setProductID( Long productId )
    {
        this.productId = productId;
    }
    
    public Long getProductID()
    {
        return productId;
    }

    public int getBreakTime() {
        return breakTime;
    }

    public String getGrade() {
        return grade;
    }

    public String getItemSetDescription() {
        return itemSetDescription;
    }

    public String getItemSetDisplayName() {
        return itemSetDisplayName;
    }

    public String getTestName() {
        return testName;
    }

    public String getItemSetForm() {
        return itemSetForm;
    }

    public String getItemSetLevel() {
        return itemSetLevel;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public String getVersion() {
        return version;
    }

    public String getExtTstItemSetId() {
        return extTstItemSetId;
    }

    public String getFrameworkCode() {
        return frameworkCode;
    }

    public String getProductDisplayName() {
        return productDisplayName;
    }

    public List getSubTestIds() {
        return subTestIds;
    }

    public void setSubTestIds(List subTests) {
        this.subTestIds = subTests;
    }

    public void addSubTestId(Long subTestId) {
        this.subTestIds.add(subTestId);
    }

    public void addSubTestId(List subTestIds) {
        this.subTestIds.addAll(subTestIds);
    }

    /**
     * @return Returns the itemSampleSets.
     */
    public List getItemSampleSets() {
        return itemSampleSets;
    }

    /**
     * @param itemSampleSets The itemSampleSets to set.
     */
    public void setItemSampleSets(List itemSampleSets) {
        this.itemSampleSets = itemSampleSets;
    }

    public void addItemSampleSet(ItemSetSampleSetRecord record) {
        itemSampleSets.add(record);
    }
    
    public String getCommodityCode() {
        return commodityCode;
    }

    public void setCommodityCode(String commodityCode) {
    	this.commodityCode = commodityCode;
    }
    
    public String getContentArea() {
        return contentArea;
    }
}