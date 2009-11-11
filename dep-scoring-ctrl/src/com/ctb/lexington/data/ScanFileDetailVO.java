package com.ctb.lexington.data;

import java.util.Collection;
import java.util.Iterator;

import com.ctb.lexington.util.CTBConstants;

/**
 * <p>Title: ScanFileDetailVO</p>
 * <p>Description: Holds actual binary file content</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: CTB/McGraw-Hill</p>
 * @author Nate Cohen
 * @version 1.0
 */

public class ScanFileDetailVO extends ScanFileVO {

    public  static final String VO_LABEL       = "com.ctb.lexington.data.ScanFileDetailVO";
    public  static final String VO_ARRAY_LABEL = VO_LABEL + ".array";

    public ScanFileDetailVO() {
    }

    private java.util.Collection scanHeaders;
    
    public java.util.Collection getScanHeaders() {
        return scanHeaders;
    }
    public void setScanHeaders(java.util.Collection scanHeaders_) {
        this.scanHeaders = new java.util.ArrayList();
        Iterator it = scanHeaders_.iterator();
        while(it.hasNext()) {
            this.scanHeaders.add(it.next());
        }
    }
	public boolean equals(Object object){
		boolean isEqual = false;
		try{
			ScanFileDetailVO sfd = (ScanFileDetailVO) object;
			if(super.equals(sfd)){
				Collection sfdsh = sfd.getScanHeaders();
				if(scanHeaders == null){
					if(sfdsh == null) {
						isEqual = true;
					}
				}
				else{ // compare collections
					boolean stillEqual = true;
					Iterator shIt = scanHeaders.iterator();
					Iterator sfdshIt = sfdsh.iterator();
					while (shIt.hasNext() && stillEqual){
						ScanHeaderVO sh = (ScanHeaderVO)shIt.next();
						ScanHeaderVO sfsh = (ScanHeaderVO)sfdshIt.next();
						if(!sh.equals(sfsh)){
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

	public boolean areAllSessionsMatched() {
		for (Iterator i = scanHeaders.iterator(); i.hasNext();) 
			if (!((ScanHeaderVO) i.next()).getScanHeaderStatus().equalsIgnoreCase(CTBConstants.HEADER_STATUS_SUCCESS))
				return false;
		return true;
	}
	
}