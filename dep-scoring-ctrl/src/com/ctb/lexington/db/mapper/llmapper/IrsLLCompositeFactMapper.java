package com.ctb.lexington.db.mapper.llmapper;

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.irslldata.IrsLLCompositeFactData;
import com.ctb.lexington.db.mapper.AbstractDBMapper;

/**
 * @author TCS
 *
 */

public class IrsLLCompositeFactMapper  extends AbstractDBMapper {

	private static final String FIND_BY_COMP_STUDENT_SESSION = "LLfindByCompStudentSession";
	private static final String INSERT_INTO_CONTENTAREAFACT = "LLinsertLLCompositeFact";
	private static final String UPDATE_CONTENTAREAFACT = "LLupdateLLCompositeFact";
	private static final String DELETE_BY_CONTENTAREAFACTID = "LLdeleteByCompositeFactId";
    private static final String UPDATE_CURRENTRESULT_STATUS = "LLupdateLLCompositeFactCurrentResult";
    
	public IrsLLCompositeFactMapper(Connection conn){
		super(conn);
	}
	
	public IrsLLCompositeFactData findByCompStudentSession(Long compositeId, Long studentId, Long sessionId){
		IrsLLCompositeFactData template = new IrsLLCompositeFactData();
		template.setCompositeid(compositeId);
		template.setStudentid(studentId);
		template.setSessionid(sessionId);
		return (IrsLLCompositeFactData) find(FIND_BY_COMP_STUDENT_SESSION,template);    
    }
	
	public void insert(IrsLLCompositeFactData record)throws SQLException{
		insert(INSERT_INTO_CONTENTAREAFACT, record);
    }	
	
	public void update(IrsLLCompositeFactData record)throws SQLException{
        update(UPDATE_CONTENTAREAFACT, record);
    }
    
	public void updateCurrentResultStatus(IrsLLCompositeFactData record)throws SQLException {
        update(UPDATE_CURRENTRESULT_STATUS, record);
    }
 
	public void delete(IrsLLCompositeFactData record)throws SQLException{
  	  	if(record != null)
  	  		delete(DELETE_BY_CONTENTAREAFACTID, record);
    }
}
