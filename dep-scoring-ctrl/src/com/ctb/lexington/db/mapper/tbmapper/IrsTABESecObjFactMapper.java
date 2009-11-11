package com.ctb.lexington.db.mapper.tbmapper;

import com.ctb.lexington.db.irsdata.irstbdata.IrsTABESecObjFactData;
import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.mapper.AbstractDBMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsTABESecObjFactMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_OBJ_STUDENT_SESSION = "TBfindBySecObjStudentSession";
	private static final String INSERT_INTO_TABESECOBJFACT = "TBinsertTABESecObjFact";
	private static final String UPDATE_BY_SECOBJFACTID = "TBupdateTABESecObjFact";
	private static final String DELETE_BY_SECOBJFACTID = "TBdeleteBySecObjFactId";
    private static final String UPDATE_CURRENTRESULT_STATUS = "TBupdateTABESecObjFactCurrentResult";
    
    public IrsTABESecObjFactMapper(Connection conn){
		super(conn);
	}
	
	public IrsTABESecObjFactData findByObjStudentSession(Long secObjId, Long studentId, Long sessionId){
		IrsTABESecObjFactData template = new IrsTABESecObjFactData();
		template.setSecObjid(secObjId);
		template.setStudentid(studentId);
		template.setSessionid(sessionId);
		return (IrsTABESecObjFactData) find(FIND_BY_OBJ_STUDENT_SESSION,template);    
    }
	
	public void insert(IrsTABESecObjFactData record)throws SQLException{
		insert(INSERT_INTO_TABESECOBJFACT, record);
    }	
	
	public void update(IrsTABESecObjFactData record)throws SQLException{
        update(UPDATE_BY_SECOBJFACTID, record);
    }
    
    public void updateCurrentResultStatus(IrsTABESecObjFactData record)throws SQLException {
        update(UPDATE_CURRENTRESULT_STATUS, record);
    }
		
	public void delete(IrsTABESecObjFactData record)throws SQLException{
  	  	if(record != null)
  	  		delete(DELETE_BY_SECOBJFACTID, record);
    }
}