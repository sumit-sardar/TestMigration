package com.ctb.reporting;

public class FormatterFactory {

    public static Formatter create(
        Report report,
        boolean fullReport,
        boolean verbose) {

        if (report instanceof CommandReport) {
            return new CommandReportFormatter(
                (CommandReport) report,
                fullReport);
        }

        if (report instanceof AssessmentProcessorReport) {
            return new AssessmentProcessorReportFormatter(
                (AssessmentProcessorReport) report);
        }
        
        if (report instanceof SubTestProcessorReport) {
            return new SubTestProcessorReportFormatter(
                (SubTestProcessorReport) report);
        }
        
        if (report instanceof SchedulableUnitReport) {
            return new SubTestProcessorReportFormatter(
                (SubTestProcessorReport) report);
        }

        if (report instanceof DeliverableUnitReport) {
            return new DeliverableUnitReportFormatter(
                (DeliverableUnitReport) report);
        }

        if (report instanceof ItemSetReport) {
            return new ItemSetReportFormatter((ItemSetReport) report);
        }

        if (report instanceof ItemProcessorReport) {
            return new ItemProcessorReportFormatter(
                (ItemProcessorReport) report,
                fullReport);
        }

        if (report instanceof ItemMapReport) {
            return new ItemMapReportFormatter((ItemMapReport) report);
        }

        if (report instanceof ItemImportAndMapReport) {
            return new ItemImportAndMapReportFormatter(
                (ItemImportAndMapReport) report);
        }

        if (report instanceof HierarchyReport) {
            return new HierarchyReportVisitorFormatter(
                (HierarchyReport) report,
                fullReport);
        }

        if (report instanceof FlattenedHierarchyReport) {
            return new FlattenedHierarchyReportVisitorFormatter(
                (HierarchyReport) report,
                fullReport);
        }

        if (report instanceof ProductReport) {
            return new ProductReportFormatter((ProductReport) report);
        }
        
        if (report instanceof PdfToFileReport) {
            return new DummyReportFormatter(
            new DummyReport( "PDF file genereated. Thank you." ));
        }

        if(report instanceof ValidateItemXMLReport){
            return new ValidateItemXMLReportFormatter((ValidateItemXMLReport) report);
        }

        return new DummyReportFormatter(
            new DummyReport(
                "COULD NOT FORMAT REPORT: " + report.getClass().getName()));

    }

    public static Formatter create(Report report) {
        return create(report, true, false);
    }

    public static Formatter create(Report report, boolean fullReport) {
        return create(report, fullReport, false);
    }
}
