package com.ctb.lexington.db.mapper.tvmapper;

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.irstvdata.IrsTVContentAreaFactData;
import com.ctb.lexington.db.mapper.AbstractDBMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsTVContentAreaFactMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_CA_STUDENT_SESSION = "TVfindByCAStudentSession";
	private static final String INSERT_INTO_CONTENTAREAFACT = "TVinsertTVContentAreaFact";
	private static final String INSERT_INTO_CONTENTAREAFACT_WITH_SEM = "TVinsertTVContentAreaFactwithSem";
	private static final String UPDATE_CONTENTAREAFACT = "TVupdateTVContentAreaFact";
	private static final String DELETE_BY_CONTENTAREAFACTID = "TVdeleteByContentAreaFactId";
    private static final String UPDATE_CURRENTRESULT_STATUS = "TVupdateTVContentAreaFactCurrentResult";
    
	public IrsTVContentAreaFactMapper(Connection conn){
		super(conn);
	}
	
	public IrsTVContentAreaFactData findByCAStudentSession(Long contentAreaId, Long studentId, Long sessionId){
		IrsTVContentAreaFactData template = new IrsTVContentAreaFactData();
		template.setContentAreaid(contentAreaId);
		template.setStudentid(studentId);
		template.setSessionid(sessionId);
		return (IrsTVContentAreaFactData) find(FIND_BY_CA_STUDENT_SESSION,template);    
    }
    
    public Long isTNCAFactCurrent(IrsTVContentAreaFactData fact) {
        return ((IrsTVContentAreaFactData) find("TVisTVCAFactCurrent", fact)).getCurrentResultid();
    }
	
	public void insert(IrsTVContentAreaFactData record)throws SQLException{
		insert(INSERT_INTO_CONTENTAREAFACT, record);
    }
	
	public void insertWithSem(IrsTVContentAreaFactData record)throws SQLException{
		insert(INSERT_INTO_CONTENTAREAFACT_WITH_SEM, record);
    }
	
	public void update(IrsTVContentAreaFactData record)throws SQLException{
        update(UPDATE_CONTENTAREAFACT, record);
    }
    
    public void updateCurrentResultStatus(IrsTVContentAreaFactData record)throws SQLException {
        update(UPDATE_CURRENTRESULT_STATUS, record);
    }   
	
	public void delete(IrsTVContentAreaFactData record)throws SQLException{
		if(record != null)
			delete(DELETE_BY_CONTENTAREAFACTID, record);
    }
}