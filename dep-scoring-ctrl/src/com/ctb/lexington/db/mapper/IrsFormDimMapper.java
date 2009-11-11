package com.ctb.lexington.db.mapper; 

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.IrsFormDimData;

/**
 * @author Rama_Rao
 *
 */

public class IrsFormDimMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_FORMID = "findByFormID";
	private static final String INSERT_INTO_FORMDIM = "insertFormDim";
	private static final String UPDATE_BY_FORMID = "updateFormDim";
    private static final String DELETE_BY_FORMID = "deleteFormDim";
    
	public IrsFormDimMapper(Connection conn){
		super(conn);
	}
	
	public IrsFormDimData findByFormId(Long formId){
		IrsFormDimData template = new IrsFormDimData();
		template.setFormid(formId);
		return (IrsFormDimData) find(FIND_BY_FORMID,template);    
    }
	
	public IrsFormDimData insert(IrsFormDimData record)throws SQLException{
		insert(INSERT_INTO_FORMDIM, record);
		return findByFormId(record.getFormid());
    }	
	
	public void update(IrsFormDimData record)throws SQLException{
        update(UPDATE_BY_FORMID, record);
    }
	
	public void delete(Long formId)throws SQLException{
		delete(DELETE_BY_FORMID, formId);
	}
}