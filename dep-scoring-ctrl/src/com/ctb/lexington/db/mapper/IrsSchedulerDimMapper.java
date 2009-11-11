package com.ctb.lexington.db.mapper; 

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.IrsSchedulerDimData;

/**
 * @author Rama_Rao
 *
 */

public class IrsSchedulerDimMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_SCHEDULERID = "findBySchedulerID";
	private static final String INSERT_INTO_SCHEDULERDIM = "insertSchedulerDim";
	private static final String UPDATE_SCHEDULERDIM = "updateSchedulerDim";
    private static final String DELETE_BY_SCHEDULERID = "deleteSchedulerDim";
	
	public IrsSchedulerDimMapper(Connection conn){
		super(conn);
	}
	
	public IrsSchedulerDimData findBySchedulerId(Long schedulerId){
		IrsSchedulerDimData template = new IrsSchedulerDimData();
		template.setSchedulerid(schedulerId);
		return (IrsSchedulerDimData) find(FIND_BY_SCHEDULERID,template);    
    }
	
	public IrsSchedulerDimData insert(IrsSchedulerDimData record)throws SQLException{
		insert(INSERT_INTO_SCHEDULERDIM, record);
		return findBySchedulerId(record.getSchedulerid());
	}	
	
	public void update(IrsSchedulerDimData record)throws SQLException{
		update(UPDATE_SCHEDULERDIM, record);
	}
	
	public void delete(Long schedulerId)throws SQLException{
		delete(DELETE_BY_SCHEDULERID, schedulerId);
	}
}