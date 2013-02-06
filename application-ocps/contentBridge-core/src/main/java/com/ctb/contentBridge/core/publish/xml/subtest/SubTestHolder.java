package com.ctb.contentBridge.core.publish.xml.subtest;

import java.util.*;

import com.ctb.contentBridge.core.publish.xml.ItemSet;


/**
 * @author wmli
 */
public class SubTestHolder implements ItemSet {

    private List items = new ArrayList();
    private List subTestIds = new ArrayList();
    public List childSubTestIds = new ArrayList();
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

    private String scoreLookupId = "";
    private String scoreTypeCode = "";
    private String extTstItemSetId = "";
    private String contentArea = "";
    private Long productId = null;
    private boolean bTDType = false;
    public Boolean isAddOn = Boolean.FALSE;
    public Long content_size = new Long( 0 );
    public Long startItemNumber = new Long( 0 );
    boolean sample;
    
    private SubTestMedia media;

    public SubTestHolder(String frameworkCode, String productDisplayName, String extTstItemSetId,
            String testName, String version, String itemSetLevel, String grade, int timeLimit,
            int breakTime, String itemSetDisplayName, String itemSetDescription,
            String itemSetForm, String scoreLookupId, String scoreTypeCode, String contentArea, Long startItemNumber) {

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
        this.scoreLookupId = scoreLookupId;
        this.scoreTypeCode = scoreTypeCode;
        this.contentArea = contentArea;
        this.startItemNumber = startItemNumber;
          
    }
    
    public void setProductID( Long productId )
    {
        this.productId = productId;
    }
    
    public Long getProductID()
    {
        return productId;
    }

    public void setStartItemNumber( Long startItemNumber )
    {
        this.startItemNumber = startItemNumber;
    }
    
    public Long getStartItemNumber()
    {
        return startItemNumber;
    }
    
    public Iterator getItems() {
        return items.iterator();
    }

    public void addItem(TestItem item) {
        items.add(item);
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

    public String getScoreTypeCode() {
        return scoreTypeCode;
    }

    public String getExtTstItemSetId() {
        return extTstItemSetId;
    }

    public String getScoreLookupId() {
        return scoreLookupId;
    }

    public String getFrameworkCode() {
        return frameworkCode;
    }

    public String getProductDisplayName() {
        return productDisplayName;
    }

    public SubTestMedia getMedia() {
        return media;
    }

    public void setMedia(SubTestMedia media) {
        this.media = media;
    }

    public void setItemSetLevel(String level) {
        this.itemSetLevel = level;
    }

    public int numberOfItems() {
        return items.size();
    }

    static class TestItem {
        private String itemId;
        private String scoreTypeCode;
        private String fieldTest;
        private String suppressed;

        public TestItem(String itemId, String scoreTypeCode, String fieldTest_, String suppressed_) {
            this.itemId = itemId;
            this.scoreTypeCode = scoreTypeCode;
            fieldTest = checkNULL( fieldTest_, "F" );
            suppressed = checkNULL( suppressed_, "F" );
        }
        
        public String checkNULL( String value, String defaultValue )
        {
            String returnValue = defaultValue;
            if ( value == null )
                returnValue = defaultValue;
            else if ( value.length() > 0 && ( value.charAt( 0 ) == 'Y' || value.charAt( 0 ) == 'y' 
                || value.charAt( 0 ) == 'T' || value.charAt( 0 ) == 't'))
            	returnValue = "T";
            else
                returnValue = defaultValue;
            return returnValue;
        }

        public TestItem(String itemId, String fieldTest_, String suppressed_) {
            this(itemId, null, fieldTest_, suppressed_);
        }
        
        public TestItem(String itemId)
        {
            this( itemId, null, "F", "F" );
        }
        
        public TestItem(String itemId, String scoreTypeCode )
        {
            this( itemId, scoreTypeCode, "F", "F" );
        }
        
        public String getFieldTest()
        {
            return fieldTest;
        }
        
        public String getSuppressed()
        {
            return suppressed;
        }

        public String getItemId() {
            return itemId;
        }

        public String getScoreTypeCode() {
            return scoreTypeCode;
        }

        public boolean isScorable() {
            return (scoreTypeCode != null && !scoreTypeCode.equals("")
                    	&& suppressed.equals( "F" ));
        }
    }
    /**
     * @return Returns the sample.
     */
    public boolean isSample() {
        return sample;
    }
    /**
     * @param sample The sample to set.
     */
    public void setSample(boolean sample) {
        this.sample = sample;
    }
    /**
     * @return Returns the contentArea.
     */
    public String getContentArea() {
        return contentArea;
    }
    /**
     * @param contentArea The contentArea to set.
     */
    public void setContentArea(String contentArea) {
        this.contentArea = contentArea;
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
    
    public boolean isTDType() {
        return bTDType;
    }

    public void setSubTestType(boolean bTD ) {
        this.bTDType = bTD;
    }
    

}