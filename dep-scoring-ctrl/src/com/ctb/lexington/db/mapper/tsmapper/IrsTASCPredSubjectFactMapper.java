package com.ctb.lexington.db.mapper.tsmapper;

import com.ctb.lexington.db.irsdata.irstsdata.IrsTASCPredSubjectFactData;

import java.sql.Connection;
import java.sql.SQLException;
import com.ctb.lexington.db.mapper.AbstractDBMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsTASCPredSubjectFactMapper extends AbstractDBMapper{
	
	private static final String INSERT_INTO_PREDSUBJFACT = "TSinsertTABEPredSubjFact";
	private static final String DELETE_BY_PREDSUBJFACTID = "TSdeleteByPredSubjFactId";
    private static final String UPDATE_CURRENTRESULT_STATUS = "TSupdateTABEPredSubjFactCurrentResult";

    public IrsTASCPredSubjectFactMapper(Connection conn){
		super(conn);
	}
	
	public void insert(IrsTASCPredSubjectFactData record)throws SQLException{
		insert(INSERT_INTO_PREDSUBJFACT, record);
    }	
	
	public void updateCurrentResultStatus(IrsTASCPredSubjectFactData record)throws SQLException {
        update(UPDATE_CURRENTRESULT_STATUS, record);
    }
 
	public void delete(IrsTASCPredSubjectFactData record)throws SQLException{
  	  	if(record != null)
  	  		delete(DELETE_BY_PREDSUBJFACTID, record);
    }
}