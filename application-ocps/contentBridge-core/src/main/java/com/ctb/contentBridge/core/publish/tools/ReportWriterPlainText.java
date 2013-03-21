package com.ctb.contentBridge.core.publish.tools;


import java.util.*;

import org.apache.commons.lang.*;


public class ReportWriterPlainText implements ReportWriter {
    StringBuffer buffer = new StringBuffer(1000);

    public void writeHeader(String reportName) {
        buffer.append("*** " + reportName + " ***\n");
    }

    public void writeObjective(ReportObjective objective, Map categoryNames, int level) {
        buffer.append(StringUtils.repeat(" ", level * 2));
        String levelName = (String) categoryNames.get(objective.CategoryId);

        buffer.append(levelName + ": ");
        buffer.append(objective.CmsId + " - " + objective.Name + " "
                + getActivationStatusLabel(objective.ActivationStatus) + "\n");
    }

    public void writeItem(ReportItem item, int level) {
        buffer.append(StringUtils.repeat(" ", level * 2 + 2));
        buffer.append("Item " + item.Id + " " + item.Name + " "
                + getActivationStatusLabel(item.ActivationStatus) + " " + item.extraInfo +"\n");
    }

    public void writeFooter(String footerText) {
        buffer.append("\n" + footerText);
    }

    public String getResult() {
        return buffer.toString();
    }

    private String getActivationStatusLabel(String status) {
        if (status == null || status.length() == 0) {
            return "";
        }
        if (status.equals("AC")) {
            return "(Active)";
        } else if (status.equals("IN")) {
            return "(Inactive)";
        } else {
            return "(Unknown)";
        }
    }
}
