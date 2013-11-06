package com.ctb.lexington.db.mapper.tsmapper;

import com.ctb.lexington.db.irsdata.irstsdata.IrsTASCSecObjFactData;
import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.mapper.AbstractDBMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsTASCSecObjFactMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_OBJ_STUDENT_SESSION = "TSfindBySecObjStudentSession";
	private static final String INSERT_INTO_TABESECOBJFACT = "TSinsertTABESecObjFact";
	private static final String UPDATE_BY_SECOBJFACTID = "TSupdateTABESecObjFact";
	private static final String DELETE_BY_SECOBJFACTID = "TSdeleteBySecObjFactId";
    private static final String UPDATE_CURRENTRESULT_STATUS = "TSupdateTABESecObjFactCurrentResult";
    
    public IrsTASCSecObjFactMapper(Connection conn){
		super(conn);
	}
	
	public IrsTASCSecObjFactData findByObjStudentSession(Long secObjId, Long studentId, Long sessionId){
		IrsTASCSecObjFactData template = new IrsTASCSecObjFactData();
		template.setSecObjid(secObjId);
		template.setStudentid(studentId);
		template.setSessionid(sessionId);
		return (IrsTASCSecObjFactData) find(FIND_BY_OBJ_STUDENT_SESSION,template);    
    }
	
	public void insert(IrsTASCSecObjFactData record)throws SQLException{
		insert(INSERT_INTO_TABESECOBJFACT, record);
    }	
	
	public void update(IrsTASCSecObjFactData record)throws SQLException{
        update(UPDATE_BY_SECOBJFACTID, record);
    }
    
    public void updateCurrentResultStatus(IrsTASCSecObjFactData record)throws SQLException {
        update(UPDATE_CURRENTRESULT_STATUS, record);
    }
		
	public void delete(IrsTASCSecObjFactData record)throws SQLException{
  	  	if(record != null)
  	  		delete(DELETE_BY_SECOBJFACTID, record);
    }
}