package com.ctb.lexington.db.mapper.tvmapper;

import java.sql.Connection;
import java.sql.SQLException;
import com.ctb.lexington.db.irsdata.irstvdata.IrsTVCompositeFactData;
import com.ctb.lexington.db.mapper.AbstractDBMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsTVCompositeFactMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_COMP_STUDENT_SESSION = "TVfindByCompStudentSession";
	private static final String INSERT_INTO_CONTENTAREAFACT = "TVinsertTVCompositeFact";
	private static final String UPDATE_CONTENTAREAFACT = "TVupdateTVCompositeFact";
	private static final String DELETE_BY_CONTENTAREAFACTID = "TVdeleteByCompositeFactId";
    private static final String UPDATE_CURRENTRESULT_STATUS = "TVupdateTVCompositeFactCurrentResult";
    
	public IrsTVCompositeFactMapper(Connection conn){
		super(conn);
	}
	
	public IrsTVCompositeFactData findByCompStudentSession(Long compositeId, Long studentId, Long sessionId){
		IrsTVCompositeFactData template = new IrsTVCompositeFactData();
		template.setCompositeid(compositeId);
		template.setStudentid(studentId);
		template.setSessionid(sessionId);
		return (IrsTVCompositeFactData) find(FIND_BY_COMP_STUDENT_SESSION,template);    
    }
	
	public void insert(IrsTVCompositeFactData record)throws SQLException{
		insert(INSERT_INTO_CONTENTAREAFACT, record);
    }	
	
	public void update(IrsTVCompositeFactData record)throws SQLException{
        update(UPDATE_CONTENTAREAFACT, record);
    }
    
	public void updateCurrentResultStatus(IrsTVCompositeFactData record)throws SQLException {
        update(UPDATE_CURRENTRESULT_STATUS, record);
    }
 
	public void delete(IrsTVCompositeFactData record)throws SQLException{
  	  	if(record != null)
  	  		delete(DELETE_BY_CONTENTAREAFACTID, record);
    }
}