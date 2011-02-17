/*
 * Created on Nov 18, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ctb.reporting;

/**
 * @author wmli
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ItemMapReport extends AbstractItemReport {

	private static ThreadLocal _current = new ThreadLocal();

	public static ItemMapReport getCurrentReport() {
		return (ItemMapReport) _current.get();
	}
	public static void setCurrentReport(ItemMapReport report) {
		_current.set(report);
	}
	
	private String operation;
	private String description;
	
    public String getDescription() {
        return description;
    }

    public String getOperation() {
        return operation;
    }

    public void setDescription(String string) {
        description = string;
    }

    public void setOperation(String string) {
        operation = string;
    }

}
