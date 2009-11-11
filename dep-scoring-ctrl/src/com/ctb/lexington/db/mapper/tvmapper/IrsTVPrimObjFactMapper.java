package com.ctb.lexington.db.mapper.tvmapper;

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.irstvdata.IrsTVPrimObjFactData;
import com.ctb.lexington.db.mapper.AbstractDBMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsTVPrimObjFactMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_OBJ_STUDENT_SESSION = "TVfindByPrimObjStudentSession";
	private static final String INSERT_INTO_PRIMSECOBJFACT = "TVinsertTVPrimObjFact";
	private static final String UPDATE_BY_PRIMOBJFACTID = "TVupdateTVPrimObjFact";
	private static final String DELETE_BY_PRIMOBJFACTID = "TVdeleteByPrimObjFactId";
    private static final String UPDATE_CURRENTRESULT_STATUS = "TVupdateTVPrimObjectCurrentResult";
    
	public IrsTVPrimObjFactMapper(Connection conn){
		super(conn);
	}
	
	public IrsTVPrimObjFactData findByObjStudentSession(Long primObjId, Long studentId, Long sessionId){
		IrsTVPrimObjFactData template = new IrsTVPrimObjFactData();
		template.setPrimObjid(primObjId);
		template.setStudentid(studentId);
		template.setSessionid(sessionId);
		return (IrsTVPrimObjFactData) find(FIND_BY_OBJ_STUDENT_SESSION,template);    
    }
	
	public void insert(IrsTVPrimObjFactData record)throws SQLException{
		insert(INSERT_INTO_PRIMSECOBJFACT, record);
    }	
	
	public void update(IrsTVPrimObjFactData record)throws SQLException{
        update(UPDATE_BY_PRIMOBJFACTID, record);
    }
    
    public void updateCurrentResultStatus(IrsTVPrimObjFactData record)throws SQLException{
        update(UPDATE_CURRENTRESULT_STATUS, record);
    }

	public void delete(IrsTVPrimObjFactData record)throws SQLException{
  	  	if(record != null)
  	  		delete(DELETE_BY_PRIMOBJFACTID, record);
    }
}