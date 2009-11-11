package com.ctb.lexington.db.mapper.tbmapper;

import com.ctb.lexington.db.irsdata.irstbdata.IrsTABEPredSubjectFactData;
import java.sql.Connection;
import java.sql.SQLException;
import com.ctb.lexington.db.mapper.AbstractDBMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsTABEPredSubjectFactMapper extends AbstractDBMapper{
	
	private static final String INSERT_INTO_PREDSUBJFACT = "TBinsertTABEPredSubjFact";
	private static final String DELETE_BY_PREDSUBJFACTID = "TBdeleteByPredSubjFactId";
    private static final String UPDATE_CURRENTRESULT_STATUS = "TBupdateTABEPredSubjFactCurrentResult";

    public IrsTABEPredSubjectFactMapper(Connection conn){
		super(conn);
	}
	
	public void insert(IrsTABEPredSubjectFactData record)throws SQLException{
		insert(INSERT_INTO_PREDSUBJFACT, record);
    }	
	
	public void updateCurrentResultStatus(IrsTABEPredSubjectFactData record)throws SQLException {
        update(UPDATE_CURRENTRESULT_STATUS, record);
    }
 
	public void delete(IrsTABEPredSubjectFactData record)throws SQLException{
  	  	if(record != null)
  	  		delete(DELETE_BY_PREDSUBJFACTID, record);
    }
}