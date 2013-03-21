/*
 * Created on Nov 26, 2003
 *
 */
package com.ctb.contentBridge.core.publish.report;

public class ReportFactory {

    private ReportFactory() {
    }

    public static ItemSetReport createItemSetReport(boolean initialSuccess) {
        ItemSetReport report = new ItemSetReport();
        ItemSetReport.setCurrentReport(report);
        report.setSuccess(initialSuccess);
        return report;
    }

    /** Sets success to true at intitalization*/
    public static ItemSetReport createItemSetReport() {
        return createItemSetReport(true);
    }
    
     public static PdfToFileReport createPdfToFileReport() {
        PdfToFileReport report = new PdfToFileReport();
   //     PdfToFileReport.setCurrentReport( report );
        report.setSuccess( true );
        return report;
    }

    /** Sets success to true at intitalization*/
    public static ItemImportAndMapReport getItemImportAndMapReport() {
        ItemImportAndMapReport report = new ItemImportAndMapReport();
        ItemImportAndMapReport.setCurrentReport(report);
        report.setSuccess(true);
        return report;
    }

    /** Sets success to true at intitalization*/
    public static AssessmentProcessorReport createAssessmentReport() {
        AssessmentProcessorReport report = new AssessmentProcessorReport();
        AssessmentProcessorReport.setCurrentReport(report);
        report.setSuccess(true);
        return report;
    }

    /** Sets success to true at intitalization*/
    public static SubTestProcessorReport createSubTestReport() {
        return createSubTestReport(true);
    }

    public static SubTestProcessorReport createSubTestReport(boolean initialSuccess) {
        SubTestProcessorReport report = new SubTestProcessorReport();
        SubTestProcessorReport.setCurrentReport(report);
        report.setSuccess(initialSuccess);
        return report;
    }
    
    public static SchedulableUnitReport createSchedulableUnitReport() {
        return createSchedulableUnitReport(true);
    }

    public static SchedulableUnitReport createSchedulableUnitReport(boolean initialSuccess) {
        SchedulableUnitReport report = new SchedulableUnitReport();
        SchedulableUnitReport.setCurrentReport(report);
        report.setSuccess(initialSuccess);
        return report;
    }
    
    public static DeliverableUnitReport createDeliverableUnitReport() {
        return createDeliverableUnitReport( false );
    }

    public static DeliverableUnitReport createDeliverableUnitReport(boolean initialSuccess) {
        DeliverableUnitReport report = new DeliverableUnitReport();
        DeliverableUnitReport.setCurrentReport(report);
        report.setSuccess(initialSuccess);
        return report;
    }

    /** Sets success to false at intitalization*/
    public static ItemProcessorReport createItemProcessorReport() {
        ItemProcessorReport report = new ItemProcessorReport();
        ItemProcessorReport.setCurrentReport(report);
        report.setSuccess(false);
        return report;
    }

    public static SampleSetReport createSampleSetReport() {
		SampleSetReport report = new SampleSetReport();
		SampleSetReport.setCurrentReport(report);
        report.setSuccess(true);
        return report;
    }

}
