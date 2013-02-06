package com.ctb.contentBridge.core.publish.report;

import java.util.ArrayList;
import java.util.List;

public class SubTestProcessorReport extends AbstractXMLElementReport {
    private static ThreadLocal _current = new ThreadLocal();

    public static SubTestProcessorReport getCurrentReport() {
        return (SubTestProcessorReport) _current.get();
    }

    public static void setCurrentReport(SubTestProcessorReport report) {
        _current.set(report);
    }

    private String id;
    private Long productId;
    private List itemSetIds = new ArrayList();
    
    private boolean sample;
    private String subTestLevel;
    private String subTestName;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return this.productId;
    }

    public Long getItemSetId() {
        return (Long) itemSetIds.get(0);
    }

    public List getItemSetIds() {
        return itemSetIds;
    }

    public void setItemSetId(Long itemSetId) {
        this.itemSetIds.add(itemSetId);
    }

    public void setItemSetIds(List itemSetIds) {
        this.itemSetIds.addAll(itemSetIds);
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
     * @return Returns the subTestLevel.
     */
    public String getSubTestLevel() {
        return subTestLevel;
    }
    /**
     * @param subTestLevel The subTestLevel to set.
     */
    public void setSubTestLevel(String subTestLevel) {
        this.subTestLevel = subTestLevel;
    }
    /**
     * @return Returns the subtestName.
     */
    public String getSubTestName() {
        return subTestName;
    }
    /**
     * @param subtestName The subtestName to set.
     */
    public void setSubTestName(String subTestName) {
        this.subTestName = subTestName;
    }
}