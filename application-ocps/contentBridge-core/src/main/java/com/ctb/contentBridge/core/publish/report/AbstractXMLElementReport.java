/*
 * Created on Jan 20, 2004
 *
 */
package com.ctb.contentBridge.core.publish.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class AbstractXMLElementReport extends AbstractReport {
    protected List subReports = new ArrayList();

    public Iterator getSubReports() {
        return subReports.iterator();
    }

    public void addSubReport(AbstractReport report) {
        if (!report.isSuccess())
            setSuccess(false);
        subReports.add(report);
    }

    public void addSubReport(List childReports) {
        for (Iterator iter1 = childReports.iterator(); iter1.hasNext();) {
        	Object first = iter1.next();
        	if ( first instanceof List){
        		List child = (List)first;
        		for (Iterator iter2 = child.iterator(); iter2.hasNext();) {
        			AbstractReport childReport = (AbstractReport) iter2.next();
        			addSubReport(childReport);
        			}
        		}
        	else{
        		AbstractReport childReport = (AbstractReport) first;
    			addSubReport(childReport);
        	}
        }
    }

    public List getSubReports(Class klass) {
        ArrayList result = new ArrayList();

        if (klass.isInstance(this)) {
            result.add(this);
        }

        for (Iterator subReportIter = subReports.iterator();
            subReportIter.hasNext();
            ) {
            Report subReport = (Report) subReportIter.next();

            if (subReport instanceof AbstractXMLElementReport) {
                result.addAll(
                    ((AbstractXMLElementReport) subReport).getSubReports(klass));
            }
        }
        
        return result;
    }
    
    public Map getItemMappings() {
		Map result = new HashMap();
		
    	for (Iterator iter = subReports.iterator(); iter.hasNext();) {
			AbstractXMLElementReport subReport = (AbstractXMLElementReport) iter.next();
            result.putAll(subReport.getItemMappings());
        }
        return result;
    }
}
