package com.ctb.lexington.db.mapper;

import java.sql.Connection;
import java.sql.SQLException;
import com.ctb.lexington.db.irsdata.IrsMasteryLevelDimData;

/**
 * @author Rama_Rao
 *
 */

public class IrsMasteryLevelDimMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_MASTERYLEVELID = "findByMasteryLevelID";
	private static final String INSERT_INTO_MASTERYDIM = "insertMasteryLevelDim";
    private static final String UPDATE_BY_MASTERYLEVELID = "updateMasteryLevelDim";
    private static final String DELETE_BY_MASTERYLEVELID = "deleteMasteryLevelDim";
	
	public IrsMasteryLevelDimMapper(Connection conn){
		super(conn);
	}
	
	public IrsMasteryLevelDimData findByMasteryLevelId(Long masteryLevelId){
		IrsMasteryLevelDimData template = new IrsMasteryLevelDimData();
		template.setMasteryLevelid(masteryLevelId);
		return (IrsMasteryLevelDimData) find(FIND_BY_MASTERYLEVELID,template);    
    }
	
	public IrsMasteryLevelDimData insert(IrsMasteryLevelDimData record)throws SQLException{
    	insert(INSERT_INTO_MASTERYDIM, record);
		return findByMasteryLevelId(record.getMasteryLevelid());
	}	
	
	public void update(IrsMasteryLevelDimData record)throws SQLException{
	    update(UPDATE_BY_MASTERYLEVELID, record);
	}
	
	public void delete(Long masteryLevelId)throws SQLException{
		delete(DELETE_BY_MASTERYLEVELID, masteryLevelId);
	}
}