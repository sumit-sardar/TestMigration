package com.ctb.lexington.db.mapper.tsmapper;

import com.ctb.lexington.db.irsdata.irstsdata.IrsTASCPrimObjFactData;
import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.mapper.AbstractDBMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsTASCPrimObjFactMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_OBJ_STUDENT_SESSION = "TSfindByPrimObjStudentSession";
	private static final String INSERT_INTO_PRIMSECOBJFACT = "TSinsertTABEPrimObjFact";
	private static final String UPDATE_BY_PRIMOBJFACTID = "TSupdateTABEPrimObjFact";
	private static final String DELETE_BY_PRIMOBJFACTID = "TSdeleteByPrimObjFactId";
    private static final String UPDATE_CURRENTRESULT_STATUS = "TSupdateTABEPrimObjectCurrentResult";
    
	public IrsTASCPrimObjFactMapper(Connection conn){
		super(conn);
	}
	
	public IrsTASCPrimObjFactData findByObjStudentSession(Long primObjId, Long studentId, Long sessionId){
		IrsTASCPrimObjFactData template = new IrsTASCPrimObjFactData();
		template.setPrimObjid(primObjId);
		template.setStudentid(studentId);
		template.setSessionid(sessionId);
		return (IrsTASCPrimObjFactData) find(FIND_BY_OBJ_STUDENT_SESSION,template);    
    }
	
	public void insert(IrsTASCPrimObjFactData record)throws SQLException{
		insert(INSERT_INTO_PRIMSECOBJFACT, record);
    }	
	
	public void update(IrsTASCPrimObjFactData record)throws SQLException{
        update(UPDATE_BY_PRIMOBJFACTID, record);
    }
    
    public void updateCurrentResultStatus(IrsTASCPrimObjFactData record)throws SQLException{
        update(UPDATE_CURRENTRESULT_STATUS, record);
    }

	public void delete(IrsTASCPrimObjFactData record)throws SQLException{
  	  	if(record != null)
  	  		delete(DELETE_BY_PRIMOBJFACTID, record);
    }
}