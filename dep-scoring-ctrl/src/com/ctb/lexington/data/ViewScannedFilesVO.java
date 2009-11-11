/*
 * Created on Mar 22, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.ctb.lexington.data;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author arathore
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ViewScannedFilesVO implements Serializable {

    private Integer userId;
    private Collection orgNodeIdsAtAndBelow;
    
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
    public Collection getOrgNodeIdsAtAndBelow() {
        return this.orgNodeIdsAtAndBelow;
    }
    
    public void setOrgNodeIdsAtAndBelow(Collection orgNodeIdsAtAndBelow) {
        this.orgNodeIdsAtAndBelow = orgNodeIdsAtAndBelow;
    }
}
