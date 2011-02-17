package com.ctb.cprocessor.decorator;

import com.ctb.cprocessor.CommandProcessor;
import com.ctb.mapping.MapperFactory;
import com.ctb.reporting.HierarchyReport;
import com.ctb.reporting.Report;

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
