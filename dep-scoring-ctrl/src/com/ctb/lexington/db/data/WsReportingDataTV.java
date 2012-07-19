package com.ctb.lexington.db.data;

import com.ctb.lexington.db.irsdata.irstvdata.WsTVContentAreaFactData;
import com.ctb.lexington.db.irsdata.irstvdata.WsTVPrimObjFactData;


/**
 * @author TCS
 * This class is designed to hold data to be sent to Acuity for
 * Acuity students taking tests in OAS client.
 */

public class WsReportingDataTV {
	
	private Long studentid;
	private WsTVContentAreaFactData[] irsTVContentAreaFactData;
	private WsTVPrimObjFactData[] irsTVPrimObjFactData;

	public Long getStudentid() {
		return studentid;
	}

	public void setStudentid(Long studentid) {
		this.studentid = studentid;
	}

	public WsTVContentAreaFactData[] getIrsTVContentAreaFactData() {
		return irsTVContentAreaFactData;
	}

	public void setIrsTVContentAreaFactData(
			WsTVContentAreaFactData[] irsTVContentAreaFactData) {
		this.irsTVContentAreaFactData = irsTVContentAreaFactData;
	}

	public WsTVPrimObjFactData[] getIrsTVPrimObjFactData() {
		return irsTVPrimObjFactData;
	}

	public void setIrsTVPrimObjFactData(WsTVPrimObjFactData[] irsTVPrimObjFactData) {
		this.irsTVPrimObjFactData = irsTVPrimObjFactData;
	}

}
