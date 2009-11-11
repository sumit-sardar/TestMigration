package com.ctb.lexington.db.mapper.tbmapper;

import com.ctb.lexington.db.irsdata.irstbdata.IrsTABEContentAreaFactData;
import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.mapper.AbstractDBMapper;;

/**
 * @author Rama_Rao
 *
 */

public class IrsTABEContentAreaFactMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_CA_STUDENT_SESSION = "TBfindByCAStudentSession";
	private static final String INSERT_INTO_CONTENTAREAFACT = "TBinsertTABEContentAreaFact";
	private static final String UPDATE_CONTENTAREAFACT = "TBupdateTABEContentAreaFact";
	private static final String DELETE_BY_CONTENTAREAFACTID = "TBdeleteByContentAreaFactId";
    private static final String UPDATE_CURRENTRESULT_STATUS = "TBupdateTABEContentAreaFactCurrentResult";
    
	public IrsTABEContentAreaFactMapper(Connection conn){
		super(conn);
	}
	
	public IrsTABEContentAreaFactData findByCAStudentSession(Long contentAreaId, Long studentId, Long sessionId){
		IrsTABEContentAreaFactData template = new IrsTABEContentAreaFactData();
		template.setContentAreaid(contentAreaId);
		template.setStudentid(studentId);
		template.setSessionid(sessionId);
		return (IrsTABEContentAreaFactData) find(FIND_BY_CA_STUDENT_SESSION,template);    
    }
    
    public Long isTABECAFactCurrent(IrsTABEContentAreaFactData fact) {
        return ((IrsTABEContentAreaFactData) find("TBisTABECAFactCurrent", fact)).getCurrentResultid();
    }
	
	public void insert(IrsTABEContentAreaFactData record)throws SQLException{
		insert(INSERT_INTO_CONTENTAREAFACT, record);
    }	
	
	public void update(IrsTABEContentAreaFactData record)throws SQLException{
        update(UPDATE_CONTENTAREAFACT, record);
    }
    
    public void updateCurrentResultStatus(IrsTABEContentAreaFactData record)throws SQLException {
        update(UPDATE_CURRENTRESULT_STATUS, record);
    }
    
	
	public void delete(IrsTABEContentAreaFactData record)throws SQLException{
		if(record != null)
			delete(DELETE_BY_CONTENTAREAFACTID, record);
    }
}