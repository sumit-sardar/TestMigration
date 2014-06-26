package com.ctb.dao;

import java.sql.SQLException;

import com.ctb.bean.DataFileAudit;

public interface IUploadDataFileDAO {
	
	public void upDateAuditTable(DataFileAudit dataFileAudit) throws SQLException;

}
