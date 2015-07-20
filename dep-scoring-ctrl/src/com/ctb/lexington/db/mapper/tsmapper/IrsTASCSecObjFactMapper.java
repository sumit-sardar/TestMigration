package com.ctb.lexington.db.mapper.tsmapper;

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.irstsdata.IrsTASCSecObjFactData;
import com.ctb.lexington.db.mapper.AbstractDBMapper;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @author Rama_Rao
 *
 */

public class IrsTASCSecObjFactMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_OBJ_STUDENT_SESSION = "TSfindBySecObjStudentSession";
	private static final String INSERT_INTO_TASCSECOBJFACT = "TSinsertTASCSecObjFact";
	private static final String UPDATE_BY_SECOBJFACTID = "TSupdateTASCSecObjFact";
	private static final String DELETE_BY_SECOBJFACTID = "TSdeleteBySecObjFactId";
    private static final String UPDATE_CURRENTRESULT_STATUS = "TSupdateTASCSecObjFactCurrentResult";
    
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
		insert(INSERT_INTO_TASCSECOBJFACT, record);
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
	
	public SqlMapClient insertBatch(IrsTASCSecObjFactData record, SqlMapClient sqlMap)throws SQLException{
		sqlMap = insertBatch(INSERT_INTO_TASCSECOBJFACT, record, sqlMap);
		return sqlMap;
    }
	
	public SqlMapClient deleteBatch(IrsTASCSecObjFactData record, SqlMapClient sqlMap)throws SQLException{
		if(record != null)
			sqlMap = deleteBatch(DELETE_BY_SECOBJFACTID, record, sqlMap);
		return sqlMap;
    }
	
	public void executeItemBatch(SqlMapClient sqlClient) throws SQLException{
		executeBatchProcess(sqlClient);
	}
}