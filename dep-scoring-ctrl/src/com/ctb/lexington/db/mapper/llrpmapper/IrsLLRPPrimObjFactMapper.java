package com.ctb.lexington.db.mapper.llrpmapper;

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.irsllrpdata.IrsLLRPPrimObjFactData;
import com.ctb.lexington.db.mapper.AbstractDBMapper;

/**
 * @author TCS
 *
 */

public class IrsLLRPPrimObjFactMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_OBJ_STUDENT_SESSION = "LLRPfindByPrimObjStudentSession";
	private static final String INSERT_INTO_PRIMSECOBJFACT = "LLRPinsertLLPrimObjFact";
	private static final String UPDATE_BY_PRIMOBJFACTID = "LLRPupdateLLPrimObjFact";
	private static final String DELETE_BY_PRIMOBJFACTID = "LLRPdeleteByPrimObjFactId";
    private static final String UPDATE_CURRENTRESULT_STATUS = "LLRPupdateLLPrimObjectCurrentResult";
    
	public IrsLLRPPrimObjFactMapper(Connection conn){
		super(conn);
	}
	
	public IrsLLRPPrimObjFactData findByObjStudentSession(Long primObjId, Long studentId, Long sessionId){
		IrsLLRPPrimObjFactData template = new IrsLLRPPrimObjFactData();
		template.setPrimObjid(primObjId);
		template.setStudentid(studentId);
		template.setSessionid(sessionId);
		return (IrsLLRPPrimObjFactData) find(FIND_BY_OBJ_STUDENT_SESSION,template);    
    }
	
	public void insert(IrsLLRPPrimObjFactData record)throws SQLException{
		insert(INSERT_INTO_PRIMSECOBJFACT, record);
    }	
	
	public void update(IrsLLRPPrimObjFactData record)throws SQLException{
        update(UPDATE_BY_PRIMOBJFACTID, record);
    }
    
    public void updateCurrentResultStatus(IrsLLRPPrimObjFactData record)throws SQLException{
        update(UPDATE_CURRENTRESULT_STATUS, record);
    }

	public void delete(IrsLLRPPrimObjFactData record)throws SQLException{
  	  	if(record != null)
  	  		delete(DELETE_BY_PRIMOBJFACTID, record);
    }
}