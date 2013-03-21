package com.ctb.contentBridge.core.publish.sofa;


import java.util.*;


/**
 * @author wmli
 */
public class PremadeTestHolder {

    private List items = new ArrayList();
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
    private String extTstItemSetId = "";

    public PremadeTestHolder(
            String frameworkCode,
            String productDisplayName,
            String extTstItemSetId,
            String testName,
            String version,
            String itemSetLevel,
            String grade,
            int timeLimit,
            int breakTime,
            String itemSetDisplayName,
            String itemSetDescription,
            String itemSetForm,
            String scoreLookupId) {

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
        return scoreLookupId;
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

    static class TestItem {
        private String itemId;
        private String scoreTypeCode;
        private String fieldTest;
        private String suppressed;

        public TestItem(String itemId, String scoreTypeCode, String fieldTest_, String suppressed_ ) {
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
                || value.charAt( 0 ) == 'T' || value.charAt( 0 ) == 't' ))
            	returnValue = "T";
            else
                returnValue = defaultValue;
            return returnValue;
        }

        public TestItem(String itemId, String fieldTest_, String suppressed_ ) {
            this(itemId, null, fieldTest_, suppressed_ );
        }
        
        public TestItem(String itemId)
        {
            this( itemId, null, "F", "F" );
        }
        
        public TestItem(String itemId, String scoreTypeCode )
        {
            this( itemId, scoreTypeCode, "F", "F" );
        }

        public String getItemId() {
            return itemId;
        }

        public String getScoreTypeCode() {
            return scoreTypeCode;
        }

        public boolean isScorable() {
            return (scoreTypeCode != null);
        }
        
        public String getFieldTest() {
            return fieldTest;
        }
        
        public String getSuppressed() {
            return suppressed;
        }
        
        public void setFieldTest(String fieldTest) {
            this.fieldTest = fieldTest;
        }

        public void setSuppressed(String suppressed) {
            this.suppressed = suppressed;
        }
    }
}
