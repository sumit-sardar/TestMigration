package com.ctb.lexington.db.mapper.llmapper;

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.irslldata.IrsLLPrimObjFactData;

import com.ctb.lexington.db.mapper.AbstractDBMapper;

/**
 * @author TCS
 *
 */

public class IrsLLPrimObjFactMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_OBJ_STUDENT_SESSION = "LLfindByPrimObjStudentSession";
	private static final String INSERT_INTO_PRIMSECOBJFACT = "LLinsertLLPrimObjFact";
	private static final String UPDATE_BY_PRIMOBJFACTID = "LLupdateLLPrimObjFact";
	private static final String DELETE_BY_PRIMOBJFACTID = "LLdeleteByPrimObjFactId";
    private static final String UPDATE_CURRENTRESULT_STATUS = "LLupdateLLPrimObjectCurrentResult";
    
	public IrsLLPrimObjFactMapper(Connection conn){
		super(conn);
	}
	
	public IrsLLPrimObjFactData findByObjStudentSession(Long primObjId, Long studentId, Long sessionId){
		IrsLLPrimObjFactData template = new IrsLLPrimObjFactData();
		template.setPrimObjid(primObjId);
		template.setStudentid(studentId);
		template.setSessionid(sessionId);
		return (IrsLLPrimObjFactData) find(FIND_BY_OBJ_STUDENT_SESSION,template);    
    }
	
	public void insert(IrsLLPrimObjFactData record)throws SQLException{
		insert(INSERT_INTO_PRIMSECOBJFACT, record);
    }	
	
	public void update(IrsLLPrimObjFactData record)throws SQLException{
        update(UPDATE_BY_PRIMOBJFACTID, record);
    }
    
    public void updateCurrentResultStatus(IrsLLPrimObjFactData record)throws SQLException{
        update(UPDATE_CURRENTRESULT_STATUS, record);
    }

	public void delete(IrsLLPrimObjFactData record)throws SQLException{
  	  	if(record != null)
  	  		delete(DELETE_BY_PRIMOBJFACTID, record);
    }
}