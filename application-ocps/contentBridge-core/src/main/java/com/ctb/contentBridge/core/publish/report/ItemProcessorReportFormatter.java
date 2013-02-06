package com.ctb.contentBridge.core.publish.report;

import java.io.PrintWriter;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.*;

import com.ctb.contentBridge.core.publish.mapping.Objective;

public class ItemProcessorReportFormatter implements Formatter {
    private static Logger logger =
        Logger.getLogger(ItemProcessorReportFormatter.class);
    private ItemProcessorReport report;
    private static final String UNKNOWN_ID = "Unknown ID";
    private static final String NO_NEW_ID = "No New ID";
    private final boolean fullReport;
    private boolean showHierarchy = false;

    public ItemProcessorReportFormatter(
        ItemProcessorReport report,
        boolean fullReport) {
        this.report = report;
        this.fullReport = fullReport;
    }
    public ItemProcessorReportFormatter(ItemProcessorReport report,boolean fullReport,boolean showHierachy) {
        this(report,fullReport);
        this.showHierarchy = showHierachy;
    }

    public void print(PrintWriter writer, boolean isSubReport) {
        if (!isSubReport) {
            logger.info("Printing Item Processor Report");
            writer.println(
                "\nItem processed at ["
                    + report.getStartTime()
                    + "] on Thread ["
                    + report.getThreadId()
                    + "]");
        }

        String id = (report.getID() == null) ? UNKNOWN_ID : report.getID();
        String newId =
            (report.getNewID() == null) ? NO_NEW_ID : report.getNewID();
        writer.print(getIDHeaderString(id, newId));

        if (report.isSuccess())
            onSuccess(writer, id, newId);
        else
            onFailure(writer, id, newId);

    }

    private String getIDHeaderString(String id, String newID) {
        StringBuffer buf = new StringBuffer();
        if (fullReport) {
            buf.append(
                StringUtils.leftPad(
                    getFrameworkCodeString() + "Item [" + id + "]",
                    30));
            if (changedId(id, newID))
                buf.append(StringUtils.rightPad("->[" + newID + "]", 30));
            buf.append(" " + report.getOperation());
        } else {
            buf.append(StringUtils.leftPad(getFrameworkCodeString(), 15));
            if (changedId(id, newID))
                buf.append(
                    StringUtils.rightPad(
                        report.getOperation() + " as [" + newID + "]",
                        35));
            else
                buf.append(StringUtils.rightPad(report.getOperation(), 35));
        }
        return buf.toString();
    }

    private void onSuccess(PrintWriter writer, String ID, String newID) {

        //TODO - mws - institute business and processing error codes to make the 'not applicable' more explicit
        if (report.getException() == null) {
            writer.print(" successful");
            writer.print(getActivationStatusString());
            writer.print(getInvisibleString());
            writer.println(getAnswerKeyString());
            if ( report.getWarning() != null )
            {
                writer.println( "\t\tWarning: " + report.getWarning() );
            }
            if (report.showHierarchy() && report.getObjectives() != null && !report.getObjectives().isEmpty() ) 
            {
                writer.println(getObjectiveHierarchyString());
            }
        } else {
            writer.println(" not applicable. " + report.getException().getMessage());
        }
    }

    private String getObjectiveHierarchyString() {
        StringBuffer buf = new StringBuffer();
        buf.append("\t\t");
        if (report.getObjectives() != null && !report.getObjectives().isEmpty()) {
            for (Iterator iter = report.getObjectives().iterator();iter.hasNext();) {
                Objective obj = (Objective)iter.next();
                buf.append(obj.getName() + "(" + obj.getNodeKey() + ")" + ":");
            }
        } else {
            buf.append("No Objective Hierarchy Available.");
        }
        return buf.toString();
    }

    private void onFailure(PrintWriter writer, String ID, String newID) {
        if (report.getException().getClass().getName() == "IdentityMappingException") {
	        writer.println(
		        " WARNING: "
		            + extractErrorMessageInfoFromException(report.getException()));
        } else {
	        writer.println(
	            " ERROR: "
	                + extractErrorMessageInfoFromException(report.getException()));
        }
    }

    private String extractErrorMessageInfoFromException(Exception e) {
        if (e == null)
            return "No error message";
        if (e.getMessage() == null)
            return e.toString();
        return e.getMessage();

    }

    private boolean changedId(String id, String newID) {
        return !id.equals(UNKNOWN_ID)
            && !id.equals(newID)
            && !newID.equals(NO_NEW_ID);
    }

    private String getAnswerKeyString() {
        String ak = report.getAnswerKey();
        ak =
            ((ak == null) || (ak.length() == 0)) ? "" : " [Answer: " + ak + "]";
        return ak;
    }

    private String getActivationStatusString() {
        String as = report.getActivationStatus();
        as =
            ((as == null) || (as.length() == 0)) ? "" : " [Status: " + as + "]";
        return as;
    }

    private String getInvisibleString() {
        String is = report.getInvisibleStatus();
        is =
            ((is == null) || (is.length() == 0))
                ? ""
                : " [Invisible: " + is + "]";
        return is;
    }

    private String getFrameworkCodeString() {
        String fc = report.getFrameworkCode();
        fc = ((fc == null) || (fc.length() == 0)) ? "" : fc + ": ";
        return fc;
    }

}
