package com.ctb.lexington.db.mapper.llmapper;

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.irslldata.IrsLLContentAreaFactData;

import com.ctb.lexington.db.mapper.AbstractDBMapper;

/**
 * @author TCS
 *
 */

public class IrsLLContentAreaFactMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_CA_STUDENT_SESSION = "LLfindByCAStudentSession";
	private static final String INSERT_INTO_CONTENTAREAFACT = "LLinsertLASLINKContentAreaFact";
	private static final String UPDATE_CONTENTAREAFACT = "LLupdateLASLINKContentAreaFact";
	private static final String DELETE_BY_CONTENTAREAFACTID = "LLdeleteByContentAreaFactId";
    private static final String UPDATE_CURRENTRESULT_STATUS = "LLupdateLASLINKContentAreaFactCurrentResult";
    
	public IrsLLContentAreaFactMapper(Connection conn){
		super(conn);
	}
	
	public IrsLLContentAreaFactData findByCAStudentSession(Long contentAreaId, Long studentId, Long sessionId){
		IrsLLContentAreaFactData template = new IrsLLContentAreaFactData();
		template.setContentAreaid(contentAreaId);
		template.setStudentid(studentId);
		template.setSessionid(sessionId);
		return (IrsLLContentAreaFactData) find(FIND_BY_CA_STUDENT_SESSION,template);    
    }
    
    public Long isLLCAFactCurrent(IrsLLContentAreaFactData fact) {
        return ((IrsLLContentAreaFactData) find("LLisLASLINKCAFactCurrent", fact)).getCurrentResultid();
    }
	
	public void insert(IrsLLContentAreaFactData record)throws SQLException{
		insert(INSERT_INTO_CONTENTAREAFACT, record);
    }	
	
	public void update(IrsLLContentAreaFactData record)throws SQLException{
        update(UPDATE_CONTENTAREAFACT, record);
    }
    
    public void updateCurrentResultStatus(IrsLLContentAreaFactData record)throws SQLException {
        update(UPDATE_CURRENTRESULT_STATUS, record);
    }
    
	
	public void delete(IrsLLContentAreaFactData record)throws SQLException{
		if(record != null)
			delete(DELETE_BY_CONTENTAREAFACTID, record);
    }
}