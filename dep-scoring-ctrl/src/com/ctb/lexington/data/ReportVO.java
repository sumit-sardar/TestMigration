package com.ctb.lexington.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * Value Object for information relating to reporting.
 *
 * @author Durran Jordan, djordan@thoughtworks.com
 * @version $Id$
 */
public class ReportVO implements Serializable {

    public static final String VO_LABEL = "com.ctb.lexington.data.ReportVO";
    public static final String VO_ARRAY_LABEL = VO_LABEL + ".array";

    private List availableReports = new ArrayList();
    private HashMap asynchRun = new HashMap();

    public ReportVO() {}

    public List getAvailableReports() {
        return availableReports;
    }

    public void setAvailableReports(List availableReports) {
        this.availableReports = availableReports;
    }
    
    public boolean getAsynchRun(String reportName) {
    	Boolean result = (Boolean) asynchRun.get(reportName);
    	if(result != null) return result.booleanValue();
    	else return false;
    }

    public void setAsynchRun(String reportName, boolean value) {
    	asynchRun.put(reportName, new Boolean(value));
    }
}
