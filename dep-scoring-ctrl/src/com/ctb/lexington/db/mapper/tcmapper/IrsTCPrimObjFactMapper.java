package com.ctb.lexington.db.mapper.tcmapper;

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.irstcdata.IrsTCPrimObjFactData;
import com.ctb.lexington.db.mapper.AbstractDBMapper;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @author Rama_Rao
 *
 */

public class IrsTCPrimObjFactMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_OBJ_STUDENT_SESSION = "TCfindByPrimObjStudentSession";
	private static final String INSERT_INTO_PRIMSECOBJFACT = "TCinsertTABEPrimObjFact";
	private static final String UPDATE_BY_PRIMOBJFACTID = "TCupdateTABEPrimObjFact";
	private static final String DELETE_BY_PRIMOBJFACTID = "TCdeleteByPrimObjFactId";
    private static final String UPDATE_CURRENTRESULT_STATUS = "TCupdateTABEPrimObjectCurrentResult";
    
	public IrsTCPrimObjFactMapper(Connection conn){
		super(conn);
	}
	
	public IrsTCPrimObjFactData findByObjStudentSession(Long primObjId, Long studentId, Long sessionId){
		IrsTCPrimObjFactData template = new IrsTCPrimObjFactData();
		template.setPrimObjid(primObjId);
		template.setStudentid(studentId);
		template.setSessionid(sessionId);
		return (IrsTCPrimObjFactData) find(FIND_BY_OBJ_STUDENT_SESSION,template);    
    }
	
	public void insert(IrsTCPrimObjFactData record)throws SQLException{
		insert(INSERT_INTO_PRIMSECOBJFACT, record);
    }	
	
	public void update(IrsTCPrimObjFactData record)throws SQLException{
        update(UPDATE_BY_PRIMOBJFACTID, record);
    }
    
    public void updateCurrentResultStatus(IrsTCPrimObjFactData record)throws SQLException{
        update(UPDATE_CURRENTRESULT_STATUS, record);
    }

	public void delete(IrsTCPrimObjFactData record)throws SQLException{
  	  	if(record != null)
  	  		delete(DELETE_BY_PRIMOBJFACTID, record);
    }
	
	
	public SqlMapClient insertBatch(IrsTCPrimObjFactData record, SqlMapClient sqlMap)throws SQLException{
		sqlMap = insertBatch(INSERT_INTO_PRIMSECOBJFACT, record, sqlMap);
		return sqlMap;
    }
	
	public SqlMapClient deleteBatch(IrsTCPrimObjFactData record, SqlMapClient sqlMap)throws SQLException{
		if(record != null)
			sqlMap = deleteBatch(DELETE_BY_PRIMOBJFACTID, record, sqlMap);
		return sqlMap;
    }
	
	public void executeItemBatch(SqlMapClient sqlClient) throws SQLException{
		executeBatchProcess(sqlClient);
	}
}