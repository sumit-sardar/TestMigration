/*
 * Created on Nov 9, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ctb.reporting;

import java.io.PrintWriter;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author wen-jin_chang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DeliverableUnitReportFormatter implements Formatter
{
    private static Logger logger =
        Logger.getLogger(DeliverableUnitReportFormatter.class);
    private DeliverableUnitReport report;

    public DeliverableUnitReportFormatter(DeliverableUnitReport report) {
        this.report = report;
    }

	public void print(PrintWriter writer, boolean isSubReport) {
		if (!isSubReport) {
			logger.info("Printing Deliverable Unit Report");
			writer.println(
				"\nDeliverableUnit processed at ["
					+ report.getStartTime()
					+ "] on Thread ["
					+ report.getThreadId()
					+ "]");
		}
		writer.println("Deliverable Unit ID [" + report.getId() + "]");
		if (report.isSuccess())
			writer.println("All Item(s) processed successfully.");
		else 
		{
//			writer.println("Some Items failed.");
			Exception e = report.getException();
            if (e == null)
                writer.println("No error message");
            else
                writer.println(e.getMessage());
		}

		Iterator itemReports = report.getSubReports();
		int i = 0;
		while (itemReports.hasNext()) {
			writer.print(StringUtils.leftPad("" + ++i + ".", 5));
			FormatterFactory.create((Report) itemReports.next()).print(
				writer,
				true);
		}
	}
}
