package com.ctb.lexington.db.mapper.tbmapper;

import java.sql.Connection;
import java.sql.SQLException;
import com.ctb.lexington.db.irsdata.irstbdata.IrsTABECompositeFactData;
import com.ctb.lexington.db.mapper.AbstractDBMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsTABECompositeFactMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_COMP_STUDENT_SESSION = "TBfindByCompStudentSession";
	private static final String INSERT_INTO_CONTENTAREAFACT = "TBinsertTABECompositeFact";
	private static final String UPDATE_CONTENTAREAFACT = "TBupdateTABECompositeFact";
	private static final String DELETE_BY_CONTENTAREAFACTID = "TBdeleteByCompositeFactId";
    private static final String UPDATE_CURRENTRESULT_STATUS = "TBupdateTABECompositeFactCurrentResult";
    
	public IrsTABECompositeFactMapper(Connection conn){
		super(conn);
	}
	
	public IrsTABECompositeFactData findByCompStudentSession(Long compositeId, Long studentId, Long sessionId){
		IrsTABECompositeFactData template = new IrsTABECompositeFactData();
		template.setCompositeid(compositeId);
		template.setStudentid(studentId);
		template.setSessionid(sessionId);
		return (IrsTABECompositeFactData) find(FIND_BY_COMP_STUDENT_SESSION,template);    
    }
	
	public void insert(IrsTABECompositeFactData record)throws SQLException{
		insert(INSERT_INTO_CONTENTAREAFACT, record);
    }	
	
	public void update(IrsTABECompositeFactData record)throws SQLException{
        update(UPDATE_CONTENTAREAFACT, record);
    }
    
	public void updateCurrentResultStatus(IrsTABECompositeFactData record)throws SQLException {
        update(UPDATE_CURRENTRESULT_STATUS, record);
    }
 
	public void delete(IrsTABECompositeFactData record)throws SQLException{
  	  	if(record != null)
  	  		delete(DELETE_BY_CONTENTAREAFACTID, record);
    }
}