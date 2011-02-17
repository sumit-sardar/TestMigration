/*
 * Created on Nov 18, 2003
 *
 */
package com.ctb.reporting;

import java.io.PrintWriter;

import org.apache.commons.lang.StringUtils;

/**
 * @author wmli
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ItemMapReportFormatter implements Formatter {
    private static final String UNKNOWN_ID = "Unknown ID";
    ItemMapReport itemMapReport;

    public ItemMapReportFormatter(ItemMapReport itemMapReport) {
        this.itemMapReport = itemMapReport;
    }

    public void print(PrintWriter writer, boolean isSubReport) {
        String id =
            (itemMapReport.getID() == null)
                ? UNKNOWN_ID
                : itemMapReport.getID();
        writer.print(getIDHeaderString(id));

        if (itemMapReport.isSuccess())
            onSuccess(writer, id);
        else
            onFailure(writer, id);
    }

    private String getIDHeaderString(String id) {
        return "Item " + StringUtils.rightPad( "[" + id + "]", 16);

    }

    private void onFailure(PrintWriter writer, String id) {
        writer.println(
            itemMapReport.getOperation()
                + " failed: "
                + getErrorMessage());

    }


    private void onSuccess(PrintWriter writer, String id) {
        writer.println(
            itemMapReport.getOperation()
                + " successful. "
                + getDescription());
    }

    private String getDescription() {
        if (itemMapReport.getDescription() != null) {
            return "Changes: " + itemMapReport.getDescription();
        } else {
            return "";
        }
    }
    
	private String getErrorMessage() {
		if (itemMapReport.getException() != null) { 
			return itemMapReport.getException().getMessage();
		} else {
			return "No Error Message.";
		}
	}

}
