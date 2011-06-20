package com.ctb.lexington.db.mapper.llmapper;

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.irslldata.IrsLLItemFactData;
import com.ctb.lexington.db.mapper.AbstractDBMapper;

/**
 * @author TCS
 *
 */

public class IrsLLItemFactMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_ITEM_STUDENT_SESSION = "LLfindByItemStudentSession";
	private static final String INSERT_INTO_TABEITEMFACT = "LLinsertLLItemFact";
	private static final String UPDATE_BY_FACTID = "LLupdateLLItemFact";
	private static final String DELETE_BY_FACTID = "LLdeleteByItemFactId";
    private static final String UPDATE_CURRENTRESULT_STATUS = "LLupdateLLItemFactCurrentResult";
    
    public IrsLLItemFactMapper(Connection conn){
		super(conn);
	}
	
	public IrsLLItemFactData findByItemStudentSession(Long itemId, Long studentId, Long sessionId){
		IrsLLItemFactData template = new IrsLLItemFactData();
		template.setItemid(itemId);
		template.setStudentid(studentId);
		template.setSessionid(sessionId);
		return (IrsLLItemFactData) find(FIND_BY_ITEM_STUDENT_SESSION,template);    
    }
	
	public void insert(IrsLLItemFactData record)throws SQLException{
		insert(INSERT_INTO_TABEITEMFACT, record);
    }	
	
	public void update(IrsLLItemFactData record)throws SQLException{
		update(UPDATE_BY_FACTID, record);
    }
    
    public void updateCurrentResultStatus(IrsLLItemFactData record)throws SQLException{
        update(UPDATE_CURRENTRESULT_STATUS, record);
    }
	
	public void delete(IrsLLItemFactData record)throws SQLException{
		if(record != null)	
			delete(DELETE_BY_FACTID, record);	
    }
}