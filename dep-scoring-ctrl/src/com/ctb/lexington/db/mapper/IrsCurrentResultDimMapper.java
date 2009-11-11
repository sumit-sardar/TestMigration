package com.ctb.lexington.db.mapper; 

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.IrsCurrentResultDimData;

/**
 * @author Rama_Rao
 *
 */

public class IrsCurrentResultDimMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_CURRENTRESULTID = "findBycurrentResultidID";
	private static final String INSERT_INTO_CURRENTRESULTDIM = "insertCurrentResultDim";
	private static final String UPDATE_BY_CURRENTRESULTID = "updatecurrentResultidDim";
    private static final String DELETE_BY_CURRENTRESULTID = "deletecurrentResultidDim";
    
	public IrsCurrentResultDimMapper(Connection conn){
		super(conn);
	}
	
	public IrsCurrentResultDimData findByCurrentResultId(Long currentResultId){
		IrsCurrentResultDimData template = new IrsCurrentResultDimData();
		template.setCurrentResultid(currentResultId);
		return (IrsCurrentResultDimData) find(FIND_BY_CURRENTRESULTID,template);    
    }
	
	public IrsCurrentResultDimData insert(IrsCurrentResultDimData record)throws SQLException{
		insert(INSERT_INTO_CURRENTRESULTDIM, record);
		return findByCurrentResultId(record.getCurrentResultid());
    }	
	
	public void update(IrsCurrentResultDimData record)throws SQLException{
        update(UPDATE_BY_CURRENTRESULTID, record);
    }
	
	public void delete(Long currentResultId)throws SQLException{
		delete(DELETE_BY_CURRENTRESULTID, currentResultId);
	}
}