package com.ctb.lexington.db.mapper.tcmapper;

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.irstcdata.IrsTCContentAreaFactData;
import com.ctb.lexington.db.mapper.AbstractDBMapper;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @author Rama_Rao
 *
 */

public class IrsTCContentAreaFactMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_CA_STUDENT_SESSION = "TCfindByCAStudentSession";
	private static final String INSERT_INTO_CONTENTAREAFACT = "TCinsertTABEContentAreaFact";
	private static final String UPDATE_CONTENTAREAFACT = "TCupdateTABEContentAreaFact";
	private static final String DELETE_BY_CONTENTAREAFACTID = "TCdeleteByContentAreaFactId";
    private static final String UPDATE_CURRENTRESULT_STATUS = "TCupdateTABEContentAreaFactCurrentResult";
    
	public IrsTCContentAreaFactMapper(Connection conn){
		super(conn);
	}
	
	public IrsTCContentAreaFactData findByCAStudentSession(Long contentAreaId, Long studentId, Long sessionId){
		IrsTCContentAreaFactData template = new IrsTCContentAreaFactData();
		template.setContentAreaid(contentAreaId);
		template.setStudentid(studentId);
		template.setSessionid(sessionId);
		return (IrsTCContentAreaFactData) find(FIND_BY_CA_STUDENT_SESSION,template);    
    }
    
    public Long isTABECCSSCAFactCurrent(IrsTCContentAreaFactData fact) {
        return ((IrsTCContentAreaFactData) find("TCisTABECAFactCurrent", fact)).getCurrentResultid();
    }
	
	public void insert(IrsTCContentAreaFactData record)throws SQLException{
		insert(INSERT_INTO_CONTENTAREAFACT, record);
    }	
	
	public void update(IrsTCContentAreaFactData record)throws SQLException{
        update(UPDATE_CONTENTAREAFACT, record);
    }
    
    public void updateCurrentResultStatus(IrsTCContentAreaFactData record)throws SQLException {
        update(UPDATE_CURRENTRESULT_STATUS, record);
    }
    
	
	public void delete(IrsTCContentAreaFactData record)throws SQLException{
		if(record != null)
			delete(DELETE_BY_CONTENTAREAFACTID, record);
    }
	
	public SqlMapClient insertBatch(IrsTCContentAreaFactData record, SqlMapClient sqlMap)throws SQLException{
		sqlMap = insertBatch(INSERT_INTO_CONTENTAREAFACT, record, sqlMap);
		return sqlMap;
    }
	
	public SqlMapClient deleteBatch(IrsTCContentAreaFactData record, SqlMapClient sqlMap)throws SQLException{
		if(record != null)
			sqlMap = deleteBatch(DELETE_BY_CONTENTAREAFACTID, record, sqlMap);
		return sqlMap;
    }
	
	public void executeItemBatch(SqlMapClient sqlClient) throws SQLException{
		executeBatchProcess(sqlClient);
	}
}