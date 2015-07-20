package com.ctb.lexington.db.mapper.tsmapper;

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.irstsdata.IrsTASCCompositeFactData;
import com.ctb.lexington.db.mapper.AbstractDBMapper;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @author Rama_Rao
 *
 */

public class IrsTASCCompositeFactMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_COMP_STUDENT_SESSION = "TSfindByCompStudentSession";
	private static final String INSERT_INTO_COMPOSITEFACT = "TSinsertTASCCompositeFact";
	private static final String UPDATE_CONTENTAREAFACT = "TSupdateTASCCompositeFact";
	private static final String DELETE_BY_CONTENTAREAFACTID = "TSdeleteByCompositeFactId";
    private static final String UPDATE_CURRENTRESULT_STATUS = "TSupdateTASCCompositeFactCurrentResult";
    
	public IrsTASCCompositeFactMapper(Connection conn){
		super(conn);
	}
	
	public IrsTASCCompositeFactData findByCompStudentSession(Long compositeId, Long studentId, Long sessionId){
		IrsTASCCompositeFactData template = new IrsTASCCompositeFactData();
		template.setCompositeid(compositeId);
		template.setStudentid(studentId);
		template.setSessionid(sessionId);
		return (IrsTASCCompositeFactData) find(FIND_BY_COMP_STUDENT_SESSION,template);    
    }
	
	public void insert(IrsTASCCompositeFactData record)throws SQLException{
		insert(INSERT_INTO_COMPOSITEFACT, record);
    }	
	
	public void update(IrsTASCCompositeFactData record)throws SQLException{
        update(UPDATE_CONTENTAREAFACT, record);
    }
    
	public void updateCurrentResultStatus(IrsTASCCompositeFactData record)throws SQLException {
        update(UPDATE_CURRENTRESULT_STATUS, record);
    }
 
	public void delete(IrsTASCCompositeFactData record)throws SQLException{
  	  	if(record != null)
  	  		delete(DELETE_BY_CONTENTAREAFACTID, record);
    }
	
	public SqlMapClient insertBatch(IrsTASCCompositeFactData record, SqlMapClient sqlMap)throws SQLException{
		sqlMap = insertBatch(INSERT_INTO_COMPOSITEFACT, record, sqlMap);
		return sqlMap;
    }

	public SqlMapClient deleteBatch(IrsTASCCompositeFactData record, SqlMapClient sqlMap)throws SQLException{
		if(record != null)
			sqlMap = deleteBatch(DELETE_BY_CONTENTAREAFACTID, record, sqlMap);
		return sqlMap;
    }
	
	public void executeItemBatch(SqlMapClient sqlClient) throws SQLException{
		executeBatchProcess(sqlClient);
	}
}