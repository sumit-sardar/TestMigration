package com.ctb.common.tools;


import java.util.*;


public interface ReportWriter {
    public void writeHeader(String reportName);
    public void writeObjective(ReportObjective objective, Map categoryNames, int level);
    public void writeItem(ReportItem item, int level);
    public void writeFooter(String footerText);

    public String getResult();
}
