package com.ctb.lexington.db.mapper.llrpmapper;

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.irsllrpdata.IrsLLRPContentAreaFactData;
import com.ctb.lexington.db.mapper.AbstractDBMapper;

/**
 * @author TCS
 *
 */

public class IrsLLRPContentAreaFactMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_CA_STUDENT_SESSION = "LLRPfindByCAStudentSession";
	private static final String INSERT_INTO_CONTENTAREAFACT = "LLRPinsertLASLINKContentAreaFact";
	private static final String UPDATE_CONTENTAREAFACT = "LLRPupdateLASLINKContentAreaFact";
	private static final String DELETE_BY_CONTENTAREAFACTID = "LLRPdeleteByContentAreaFactId";
    private static final String UPDATE_CURRENTRESULT_STATUS = "LLRPupdateLASLINKContentAreaFactCurrentResult";
    
	public IrsLLRPContentAreaFactMapper(Connection conn){
		super(conn);
	}
	
	public IrsLLRPContentAreaFactData findByCAStudentSession(Long contentAreaId, Long studentId, Long sessionId){
		IrsLLRPContentAreaFactData template = new IrsLLRPContentAreaFactData();
		template.setContentAreaid(contentAreaId);
		template.setStudentid(studentId);
		template.setSessionid(sessionId);
		return (IrsLLRPContentAreaFactData) find(FIND_BY_CA_STUDENT_SESSION,template);    
    }
    
    public Long isLLCAFactCurrent(IrsLLRPContentAreaFactData fact) {
        return ((IrsLLRPContentAreaFactData) find("LLisLASLINKCAFactCurrent", fact)).getCurrentResultid();
    }
	
	public void insert(IrsLLRPContentAreaFactData record)throws SQLException{
		insert(INSERT_INTO_CONTENTAREAFACT, record);
    }	
	
	public void update(IrsLLRPContentAreaFactData record)throws SQLException{
        update(UPDATE_CONTENTAREAFACT, record);
    }
    
    public void updateCurrentResultStatus(IrsLLRPContentAreaFactData record)throws SQLException {
        update(UPDATE_CURRENTRESULT_STATUS, record);
    }
    
	
	public void delete(IrsLLRPContentAreaFactData record)throws SQLException{
		if(record != null)
			delete(DELETE_BY_CONTENTAREAFACTID, record);
    }
}