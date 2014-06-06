package com.ctb.lexington.db.mapper; 

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import com.ctb.lexington.db.data.CurriculumData.ContentArea;
import com.ctb.lexington.db.irsdata.IrsSubjectDimData;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @author Rama_Rao
 *
 */

public class IrsSubjectDimMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_SUBJECTID = "findBySubjectID";
    private static final String FIND_BY_SUBJECT_NAME = "findBySubjectName";
	private static final String INSERT_INTO_SUBJECTDIM = "insertSubjectDim";
	private static final String UPDATE_BY_SUBJECTID = "updateSubjectDim";
    private static final String DELETE_BY_SUBJECTID = "deleteSubjectDim";
    private static final String FIND_BY_SUBJECT_NAME_IN_BULK = "findBySubjectNameInBulk";
	
	public IrsSubjectDimMapper(Connection conn){
		super(conn);
	}
	
	public IrsSubjectDimData findBySubjectId(Long subjectId){
		IrsSubjectDimData template = new IrsSubjectDimData();
		template.setSubjectid(subjectId);
		return (IrsSubjectDimData) find(FIND_BY_SUBJECTID,template);    
    }
    
    public IrsSubjectDimData findBySubjectName(String subjectName){
		IrsSubjectDimData template = new IrsSubjectDimData();
		template.setSubjectName(subjectName);
		return (IrsSubjectDimData) find(FIND_BY_SUBJECT_NAME,template);    
    }
	
	public IrsSubjectDimData insert(IrsSubjectDimData record)throws SQLException{
    	insert(INSERT_INTO_SUBJECTDIM, record);
		return findBySubjectName(record.getSubjectName());
	}	
	
	public void update(IrsSubjectDimData record)throws SQLException{
		update(UPDATE_BY_SUBJECTID, record);
	}
	
	public void delete(Long studentId)throws SQLException{
		delete(DELETE_BY_SUBJECTID, studentId);
	}

	public Map findForSubjectInBulk(List subjectList, String keyProp) {
		HashMap map = new HashMap();
        map.put("contentAreas", subjectList);
		return (Map) findManyInMap(FIND_BY_SUBJECT_NAME_IN_BULK, map, keyProp);
	}
	
	public SqlMapClient insertBatch(IrsSubjectDimData record, SqlMapClient sqlMap)throws SQLException{
		sqlMap = insertBatch(INSERT_INTO_SUBJECTDIM, record, sqlMap);
		return sqlMap;
    }
	
	public void executeBatch(SqlMapClient sqlMapClient) throws SQLException{
		executeBatchProcess(sqlMapClient);
	}
}