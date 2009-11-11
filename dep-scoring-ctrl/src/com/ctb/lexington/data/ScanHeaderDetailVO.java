package com.ctb.lexington.data;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

/**
 * <p>Title: ScanHeaderDetailVO</p>
 * <p>Description: holds metadata and associated
 * student response data for a header group
 * (test session access code) within an upload</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: CTB/McGraw-Hill</p>
 * @author Nate Cohen
 * @version 1.0
 */

public class ScanHeaderDetailVO extends ScanHeaderVO{

    public  static final String VO_LABEL       = "com.ctb.lexington.data.ScanHeaderDetailVO";
    public  static final String VO_ARRAY_LABEL = VO_LABEL + ".array";

    public ScanHeaderDetailVO() {
    }
    private java.util.Collection scanStudents;
    private String sessionName;
    private String test;
    private Date startDate;
    private Date endDate;
    private String scheduledBy;
    
    public String getSessionName(){
    	return this.sessionName;
    }
    public String getTest(){
    	return this.test;
    }
    public Date getStartDate(){
    	return this.startDate;
    }
    public Date getEndDate(){
    	return this.endDate;
    }
    public String getScheduledBy(){
    	return this.scheduledBy;
    }
    public void setSessionName(String sessionName_){
    	this.sessionName = sessionName_;
    }
    public void setTest(String test_){
    	this.test = test_;
    }
    public void setStartDate(Date startDate_){
    	this.startDate = startDate_;
    }
    public void setEndDate(Date endDate_){
    	this.endDate = endDate_;
    }
    public void setScheduledBy(String scheduledBy_){
    	this.scheduledBy = scheduledBy_;
    }
    public java.util.Collection getScanStudents() {
        return scanStudents;
    }
    public void setScanStudents(java.util.Collection scanStudents_) {
        this.scanStudents = new java.util.ArrayList();
        Iterator it = scanStudents_.iterator();
        while(it.hasNext()) {
            this.scanStudents.add(it.next());
        }
    }
	public boolean equals(Object object){
		boolean isEqual = false;
		try{
			ScanHeaderDetailVO shd = (ScanHeaderDetailVO) object;
			if(super.equals(shd)){
				Collection shdss = shd.getScanStudents();
				if(scanStudents == null){
					if(shdss == null) {
						isEqual = true;
					}
				}
				else{ // compare collections
					boolean stillEqual = true;
					Iterator ssIt = scanStudents.iterator();
					Iterator shdssIt = shdss.iterator();
					while (ssIt.hasNext() && stillEqual){
						ScanStudentVO ss = (ScanStudentVO)ssIt.next();
						ScanStudentVO shss = (ScanStudentVO)shdssIt.next();
						if(!ss.equals(shss)){
							stillEqual = false;
						}
					}
					if(stillEqual){
						isEqual = true;
					}
				}
			}
		}
		catch (Exception e){
		}
		return isEqual;
	}

}