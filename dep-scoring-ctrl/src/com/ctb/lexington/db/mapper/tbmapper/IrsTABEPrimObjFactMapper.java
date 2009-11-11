package com.ctb.lexington.db.mapper.tbmapper;

import com.ctb.lexington.db.irsdata.irstbdata.IrsTABEPrimObjFactData;
import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.mapper.AbstractDBMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsTABEPrimObjFactMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_OBJ_STUDENT_SESSION = "TBfindByPrimObjStudentSession";
	private static final String INSERT_INTO_PRIMSECOBJFACT = "TBinsertTABEPrimObjFact";
	private static final String UPDATE_BY_PRIMOBJFACTID = "TBupdateTABEPrimObjFact";
	private static final String DELETE_BY_PRIMOBJFACTID = "TBdeleteByPrimObjFactId";
    private static final String UPDATE_CURRENTRESULT_STATUS = "TBupdateTABEPrimObjectCurrentResult";
    
	public IrsTABEPrimObjFactMapper(Connection conn){
		super(conn);
	}
	
	public IrsTABEPrimObjFactData findByObjStudentSession(Long primObjId, Long studentId, Long sessionId){
		IrsTABEPrimObjFactData template = new IrsTABEPrimObjFactData();
		template.setPrimObjid(primObjId);
		template.setStudentid(studentId);
		template.setSessionid(sessionId);
		return (IrsTABEPrimObjFactData) find(FIND_BY_OBJ_STUDENT_SESSION,template);    
    }
	
	public void insert(IrsTABEPrimObjFactData record)throws SQLException{
		insert(INSERT_INTO_PRIMSECOBJFACT, record);
    }	
	
	public void update(IrsTABEPrimObjFactData record)throws SQLException{
        update(UPDATE_BY_PRIMOBJFACTID, record);
    }
    
    public void updateCurrentResultStatus(IrsTABEPrimObjFactData record)throws SQLException{
        update(UPDATE_CURRENTRESULT_STATUS, record);
    }

	public void delete(IrsTABEPrimObjFactData record)throws SQLException{
  	  	if(record != null)
  	  		delete(DELETE_BY_PRIMOBJFACTID, record);
    }
}