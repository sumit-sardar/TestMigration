package com.ctb.contentBridge.core.publish.report;

import java.io.PrintWriter;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class ItemSetReportFormatter implements Formatter {
    private static Logger logger =
        Logger.getLogger(ItemSetReportFormatter.class);
    private ItemSetReport report;

    public ItemSetReportFormatter(ItemSetReport report) {
        this.report = report;
    }

	public void print(PrintWriter writer, boolean isSubReport) {
		if (!isSubReport) {
			logger.info("Printing Item Set Report");
			writer.println(
				"\nItemSet processed at ["
					+ report.getStartTime()
					+ "] on Thread ["
					+ report.getThreadId()
					+ "]");
		}
		if (report.isSuccess())
			writer.println("All Item(s) processed successfully.");
		else {
			writer.println("Some Items failed.");
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
