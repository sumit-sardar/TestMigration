package com.ctb.lexington.db.mapper.llrpmapper;

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.irsllrpdata.IrsLLRPSecObjFactData;
import com.ctb.lexington.db.mapper.AbstractDBMapper;

/**
 * @author TCS
 *
 */

public class IrsLLRPSecObjFactMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_OBJ_STUDENT_SESSION = "LLRPfindBySecObjStudentSession";
	private static final String INSERT_INTO_LLSECOBJFACT = "LLRPinsertLLSecObjFact";
	private static final String UPDATE_BY_SECOBJFACTID = "LLRPupdateLLSecObjFact";
	private static final String DELETE_BY_SECOBJFACTID = "LLRPdeleteBySecObjFactId";
    private static final String UPDATE_CURRENTRESULT_STATUS = "LLRPupdateLLSecObjFactCurrentResult";
    
    public IrsLLRPSecObjFactMapper(Connection conn){
		super(conn);
	}
	
	public IrsLLRPSecObjFactData findByObjStudentSession(Long secObjId, Long studentId, Long sessionId){
		IrsLLRPSecObjFactData template = new IrsLLRPSecObjFactData();
		template.setSecObjid(secObjId);
		template.setStudentid(studentId);
		template.setSessionid(sessionId);
		return (IrsLLRPSecObjFactData) find(FIND_BY_OBJ_STUDENT_SESSION,template);    
    }
	
	public void insert(IrsLLRPSecObjFactData record)throws SQLException{
		insert(INSERT_INTO_LLSECOBJFACT, record);
    }	
	
	public void update(IrsLLRPSecObjFactData record)throws SQLException{
        update(UPDATE_BY_SECOBJFACTID, record);
    }
    
    public void updateCurrentResultStatus(IrsLLRPSecObjFactData record)throws SQLException {
        update(UPDATE_CURRENTRESULT_STATUS, record);
    }
		
	public void delete(IrsLLRPSecObjFactData record)throws SQLException{
  	  	if(record != null)
  	  		delete(DELETE_BY_SECOBJFACTID, record);
    }
}