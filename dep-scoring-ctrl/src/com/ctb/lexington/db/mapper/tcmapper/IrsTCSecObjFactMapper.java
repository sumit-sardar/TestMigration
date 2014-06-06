package com.ctb.lexington.db.mapper.tcmapper;

import com.ctb.lexington.db.irsdata.irstcdata.IrsTCSecObjFactData;

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.mapper.AbstractDBMapper;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @author Rama_Rao
 *
 */

public class IrsTCSecObjFactMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_OBJ_STUDENT_SESSION = "TCfindBySecObjStudentSession";
	private static final String INSERT_INTO_TABESECOBJFACT = "TCinsertTABESecObjFact";
	private static final String UPDATE_BY_SECOBJFACTID = "TCupdateTABESecObjFact";
	private static final String DELETE_BY_SECOBJFACTID = "TCdeleteBySecObjFactId";
    private static final String UPDATE_CURRENTRESULT_STATUS = "TCupdateTABESecObjFactCurrentResult";
    
    public IrsTCSecObjFactMapper(Connection conn){
		super(conn);
	}
	
	public IrsTCSecObjFactData findByObjStudentSession(Long secObjId, Long studentId, Long sessionId){
		IrsTCSecObjFactData template = new IrsTCSecObjFactData();
		template.setSecObjid(secObjId);
		template.setStudentid(studentId);
		template.setSessionid(sessionId);
		return (IrsTCSecObjFactData) find(FIND_BY_OBJ_STUDENT_SESSION,template);    
    }
	
	public void insert(IrsTCSecObjFactData record)throws SQLException{
		insert(INSERT_INTO_TABESECOBJFACT, record);
    }	
	
	public void update(IrsTCSecObjFactData record)throws SQLException{
        update(UPDATE_BY_SECOBJFACTID, record);
    }
    
    public void updateCurrentResultStatus(IrsTCSecObjFactData record)throws SQLException {
        update(UPDATE_CURRENTRESULT_STATUS, record);
    }
		
	public void delete(IrsTCSecObjFactData record)throws SQLException{
  	  	if(record != null)
  	  		delete(DELETE_BY_SECOBJFACTID, record);
    }
	
	public SqlMapClient insertBatch(IrsTCSecObjFactData record, SqlMapClient sqlMap)throws SQLException{
		sqlMap = insertBatch(INSERT_INTO_TABESECOBJFACT, record, sqlMap);
		return sqlMap;
    }
	
	public SqlMapClient deleteBatch(IrsTCSecObjFactData record, SqlMapClient sqlMap)throws SQLException{
		if(record != null)
			sqlMap = deleteBatch(DELETE_BY_SECOBJFACTID, record, sqlMap);
		return sqlMap;
    }
	
	public void executeItemBatch(SqlMapClient sqlClient) throws SQLException{
		executeBatchProcess(sqlClient);
	}
}