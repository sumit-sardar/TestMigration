package com.ctb.lexington.db.mapper.llrpmapper;

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.irsllrpdata.IrsLLRPCompositeFactData;
import com.ctb.lexington.db.mapper.AbstractDBMapper;

/**
 * @author TCS
 *
 */

public class IrsLLRPCompositeFactMapper  extends AbstractDBMapper {

	private static final String FIND_BY_COMP_STUDENT_SESSION = "LLRPfindByCompStudentSession";
	private static final String INSERT_INTO_CONTENTAREAFACT = "LLRPinsertLLCompositeFact";
	private static final String UPDATE_CONTENTAREAFACT = "LLRPupdateLLCompositeFact";
	private static final String DELETE_BY_CONTENTAREAFACTID = "LLRPdeleteByCompositeFactId";
    private static final String UPDATE_CURRENTRESULT_STATUS = "LLRPupdateLLCompositeFactCurrentResult";
    
	public IrsLLRPCompositeFactMapper(Connection conn){
		super(conn);
	}
	
	public IrsLLRPCompositeFactData findByCompStudentSession(Long compositeId, Long studentId, Long sessionId){
		IrsLLRPCompositeFactData template = new IrsLLRPCompositeFactData();
		template.setCompositeid(compositeId);
		template.setStudentid(studentId);
		template.setSessionid(sessionId);
		return (IrsLLRPCompositeFactData) find(FIND_BY_COMP_STUDENT_SESSION,template);    
    }
	
	public void insert(IrsLLRPCompositeFactData record)throws SQLException{
		insert(INSERT_INTO_CONTENTAREAFACT, record);
    }	
	
	public void update(IrsLLRPCompositeFactData record)throws SQLException{
        update(UPDATE_CONTENTAREAFACT, record);
    }
    
	public void updateCurrentResultStatus(IrsLLRPCompositeFactData record)throws SQLException {
        update(UPDATE_CURRENTRESULT_STATUS, record);
    }
 
	public void delete(IrsLLRPCompositeFactData record)throws SQLException{
  	  	if(record != null)
  	  		delete(DELETE_BY_CONTENTAREAFACTID, record);
    }
}
