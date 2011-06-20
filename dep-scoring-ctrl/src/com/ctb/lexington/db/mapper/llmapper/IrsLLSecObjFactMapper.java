package com.ctb.lexington.db.mapper.llmapper;

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.irslldata.IrsLLSecObjFactData;
import com.ctb.lexington.db.mapper.AbstractDBMapper;

/**
 * @author TCS
 *
 */

public class IrsLLSecObjFactMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_OBJ_STUDENT_SESSION = "LLfindBySecObjStudentSession";
	private static final String INSERT_INTO_LLSECOBJFACT = "LLinsertLLSecObjFact";
	private static final String UPDATE_BY_SECOBJFACTID = "LLupdateLLSecObjFact";
	private static final String DELETE_BY_SECOBJFACTID = "LLdeleteBySecObjFactId";
    private static final String UPDATE_CURRENTRESULT_STATUS = "LLupdateLLSecObjFactCurrentResult";
    
    public IrsLLSecObjFactMapper(Connection conn){
		super(conn);
	}
	
	public IrsLLSecObjFactData findByObjStudentSession(Long secObjId, Long studentId, Long sessionId){
		IrsLLSecObjFactData template = new IrsLLSecObjFactData();
		template.setSecObjid(secObjId);
		template.setStudentid(studentId);
		template.setSessionid(sessionId);
		return (IrsLLSecObjFactData) find(FIND_BY_OBJ_STUDENT_SESSION,template);    
    }
	
	public void insert(IrsLLSecObjFactData record)throws SQLException{
		insert(INSERT_INTO_LLSECOBJFACT, record);
    }	
	
	public void update(IrsLLSecObjFactData record)throws SQLException{
        update(UPDATE_BY_SECOBJFACTID, record);
    }
    
    public void updateCurrentResultStatus(IrsLLSecObjFactData record)throws SQLException {
        update(UPDATE_CURRENTRESULT_STATUS, record);
    }
		
	public void delete(IrsLLSecObjFactData record)throws SQLException{
  	  	if(record != null)
  	  		delete(DELETE_BY_SECOBJFACTID, record);
    }
}