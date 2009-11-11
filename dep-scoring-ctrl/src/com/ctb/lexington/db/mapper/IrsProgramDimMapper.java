package com.ctb.lexington.db.mapper; 

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.IrsProgramDimData;

/**
 * @author Rama_Rao
 *
 */

public class IrsProgramDimMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_PROGRAMID = "findByProgramId";
	private static final String INSERT_INTO_PROGRAMDIM = "insertProgramDim";
	private static final String UPDATE_PROGRAMDIM = "updateProgramDim";
    private static final String DELETE_BY_PROGRAMID = "deleteByPrgoramId";
	
	public IrsProgramDimMapper(Connection conn){
		super(conn);
	}
	
	public IrsProgramDimData findByProgramId(Long programId){
		IrsProgramDimData template = new IrsProgramDimData();
		template.setProgramid(programId);
		return (IrsProgramDimData) find(FIND_BY_PROGRAMID,template);    
    }
	
	public IrsProgramDimData insert(IrsProgramDimData record)throws SQLException{
    	insert(INSERT_INTO_PROGRAMDIM, record);
		return findByProgramId(record.getProgramid());
	}	
	
	public void update(IrsProgramDimData record)throws SQLException{
		update(UPDATE_PROGRAMDIM, record);
	}

	public void delete(Long programId)throws SQLException{
		delete(DELETE_BY_PROGRAMID, programId);
	}
}