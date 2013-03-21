package com.ctb.contentBridge.core.publish.cprocessor.decorator;

import com.ctb.contentBridge.core.publish.cprocessor.CommandProcessor;
import com.ctb.contentBridge.core.publish.mapping.MapperFactory;
import com.ctb.contentBridge.core.publish.report.HierarchyReport;
import com.ctb.contentBridge.core.publish.report.Report;

public class CommandProcessorHierarchyReportingDecorator
    implements CommandProcessor {
    CommandProcessor cp;

    public CommandProcessorHierarchyReportingDecorator(CommandProcessor cp) {
        this.cp = cp;
    }

    public Report process() {
        HierarchyReport hierarchyReport =
            new HierarchyReport(
                MapperFactory.getObjectives(),
                MapperFactory.getItemMap());
        hierarchyReport.setSuccess(true);
		HierarchyReport.setCurrentReport(hierarchyReport);
        
		Report subReport = cp.process();
        hierarchyReport.addSubReport(subReport);

        return hierarchyReport;
    }

}
