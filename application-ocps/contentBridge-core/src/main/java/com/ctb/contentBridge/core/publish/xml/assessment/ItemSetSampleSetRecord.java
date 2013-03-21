/**
 * 
 */
package com.ctb.contentBridge.core.publish.xml.assessment;

/**
 * @author 
 *
 */
public class ItemSetSampleSetRecord {
	private Long itemSetId;
    private Long sampleItemSetId;
    private String subtestLevel;
    private String subtestName;
    private String testType;
    
    public Long getItemSetId() {
        return itemSetId;
    }

    public Long getSampleItemSetId() {
        return sampleItemSetId;
    }

    public String getSubtestLevel() {
        return subtestLevel;
    }

    public String getSubtestName() {
        return subtestName;
    }

    public String getTestType() {
        return testType;
    }

    public void setItemSetId(Long itemSetId) {
        this.itemSetId = itemSetId;
    }

    public void setSampleItemSetId(Long sampleItemSetId) {
        this.sampleItemSetId = sampleItemSetId;
    }

    public void setSubtestLevel(String subtestLevel) {
        this.subtestLevel = subtestLevel;
    }

    public void setSubtestName(String subtestName) {
        this.subtestName = subtestName;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }
}
