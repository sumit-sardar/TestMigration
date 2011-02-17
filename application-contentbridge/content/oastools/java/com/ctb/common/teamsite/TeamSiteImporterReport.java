package com.ctb.common.teamsite;


import java.util.*;


/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Sep 29, 2003
 * Time: 1:55:38 PM
 * To change this template use Options | File Templates.
 */
public class TeamSiteImporterReport {

    private List tsiReportData = new ArrayList();

    public void addData(TeamSiteImporterReportData data) {
        tsiReportData.add(data);
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        long totalUnique = 0;
        long dupes = 0;

        buf.append("TEAMSITE CMS IMPORT REPORT\n");
        for (Iterator iter = tsiReportData.iterator(); iter.hasNext();) {
            TeamSiteImporterReportData data = (TeamSiteImporterReportData) iter.next();

            buf.append("\t" + data + "\n");
            if (data.getCritieriaString().equals("Duplicates")) {
                dupes = data.getNumOfItems();
            } else {
                totalUnique += data.getNumOfItems();
            }
        }
        buf.append("TOTAL UNIQUE ITEMS: " + totalUnique + "\t\tTOTAL DUPES: "
                + dupes);
        return buf.toString();
    }
    
    public List getData() {
    	return tsiReportData;
    }

}
