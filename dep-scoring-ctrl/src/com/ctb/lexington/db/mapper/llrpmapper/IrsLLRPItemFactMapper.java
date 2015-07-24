package com.ctb.lexington.db.mapper.llrpmapper;

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.irsllrpdata.IrsLLRPItemFactData;
import com.ctb.lexington.db.mapper.AbstractDBMapper;

/**
 * @author TCS
 *
 */

public class IrsLLRPItemFactMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_ITEM_STUDENT_SESSION = "LLRPfindByItemStudentSession";
	private static final String INSERT_INTO_LLITEMFACT = "LLRPinsertLLItemFact";
	private static final String UPDATE_BY_FACTID = "LLRPupdateLLItemFact";
	private static final String DELETE_BY_FACTID = "LLRPdeleteByItemFactId";
    private static final String UPDATE_CURRENTRESULT_STATUS = "LLRPupdateLLItemFactCurrentResult";
    
    public IrsLLRPItemFactMapper(Connection conn){
		super(conn);
	}
	
	public IrsLLRPItemFactData findByItemStudentSession(Long itemId, Long studentId, Long sessionId){
		IrsLLRPItemFactData template = new IrsLLRPItemFactData();
		template.setItemid(itemId);
		template.setStudentid(studentId);
		template.setSessionid(sessionId);
		return (IrsLLRPItemFactData) find(FIND_BY_ITEM_STUDENT_SESSION,template);    
    }
	
	public void insert(IrsLLRPItemFactData record)throws SQLException{
		insert(INSERT_INTO_LLITEMFACT, record);
    }	
	
	public void update(IrsLLRPItemFactData record)throws SQLException{
		update(UPDATE_BY_FACTID, record);
    }
    
    public void updateCurrentResultStatus(IrsLLRPItemFactData record)throws SQLException{
        update(UPDATE_CURRENTRESULT_STATUS, record);
    }
	
	public void delete(IrsLLRPItemFactData record)throws SQLException{
		if(record != null)	
			delete(DELETE_BY_FACTID, record);	
    }
}