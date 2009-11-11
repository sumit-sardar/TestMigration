package com.ctb.lexington.db.mapper.tvmapper;

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.irstvdata.IrsTVSecObjFactData;
import com.ctb.lexington.db.mapper.AbstractDBMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsTVSecObjFactMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_OBJ_STUDENT_SESSION = "TVfindBySecObjStudentSession";
	private static final String INSERT_INTO_TNSECOBJFACT = "TVinsertTVSecObjFact";
	private static final String UPDATE_BY_SECOBJFACTID = "TVupdateTVSecObjFact";
	private static final String DELETE_BY_SECOBJFACTID = "TVdeleteBySecObjFactId";
    private static final String UPDATE_CURRENTRESULT_STATUS = "TVupdateTVSecObjFactCurrentResult";
    
    public IrsTVSecObjFactMapper(Connection conn){
		super(conn);
	}
	
	public IrsTVSecObjFactData findByObjStudentSession(Long secObjId, Long studentId, Long sessionId){
		IrsTVSecObjFactData template = new IrsTVSecObjFactData();
		template.setSecObjid(secObjId);
		template.setStudentid(studentId);
		template.setSessionid(sessionId);
		return (IrsTVSecObjFactData) find(FIND_BY_OBJ_STUDENT_SESSION,template);    
    }
	
	public void insert(IrsTVSecObjFactData record)throws SQLException{
		insert(INSERT_INTO_TNSECOBJFACT, record);
    }	
	
	public void update(IrsTVSecObjFactData record)throws SQLException{
        update(UPDATE_BY_SECOBJFACTID, record);
    }
    
    public void updateCurrentResultStatus(IrsTVSecObjFactData record)throws SQLException {
        update(UPDATE_CURRENTRESULT_STATUS, record);
    }
		
	public void delete(IrsTVSecObjFactData record)throws SQLException{
  	  	if(record != null)
  	  		delete(DELETE_BY_SECOBJFACTID, record);
    }
}