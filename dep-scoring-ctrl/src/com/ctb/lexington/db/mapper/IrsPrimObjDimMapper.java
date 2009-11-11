package com.ctb.lexington.db.mapper; 

import java.sql.Connection;
import java.sql.SQLException;
import com.ctb.lexington.db.irsdata.IrsPrimObjDimData;

/**
 * @author Rama_Rao
 *
 */

public class IrsPrimObjDimMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_PRIMOBJID = "findByPrimObjID";
	private static final String INSERT_INTO_PRIMOBJDIM = "insertPrimObjDim";
    private static final String UPDATE_BY_PRMIOBJID = "updatePrimObjDim";
    private static final String DELETE_BY_PRIMOBJID = "deletePrimObjDim";
  
	public IrsPrimObjDimMapper(Connection conn){
		super(conn);
	}
	
	public IrsPrimObjDimData findByPrimObjId(Long primObjId){
		IrsPrimObjDimData template = new IrsPrimObjDimData();
		template.setPrimObjid(primObjId);
		return (IrsPrimObjDimData) find(FIND_BY_PRIMOBJID,template);    
    }
	
	public IrsPrimObjDimData insert(IrsPrimObjDimData record)throws SQLException{
		insert(INSERT_INTO_PRIMOBJDIM, record);
	    return findByPrimObjId(record.getPrimObjid());
    }	
	
	public void update(IrsPrimObjDimData record)throws SQLException{
        update(UPDATE_BY_PRMIOBJID, record);
    }
	
	public void delete(Long primObjId)throws SQLException{
		delete(DELETE_BY_PRIMOBJID, primObjId);
	}
}