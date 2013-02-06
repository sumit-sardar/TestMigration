/*
 * Created on Jan 20, 2004
 *
 */
package com.ctb.contentBridge.core.publish.report;

public class AssessmentProcessorReport extends AbstractXMLElementReport {
    private static ThreadLocal _current = new ThreadLocal();
    public static AssessmentProcessorReport getCurrentReport() {
        return (AssessmentProcessorReport) _current.get();
    }
    public static void setCurrentReport(AssessmentProcessorReport report) {
        _current.set(report);
    }
    
    String id;
    private Long productId;
    
    public String getId() {
        return id;
    }

    public void setId(String string) {
        id = string;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return this.productId;
    }

}
