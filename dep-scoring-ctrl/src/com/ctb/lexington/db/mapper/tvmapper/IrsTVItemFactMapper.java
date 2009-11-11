package com.ctb.lexington.db.mapper.tvmapper;

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.irstvdata.IrsTVItemFactData;
import com.ctb.lexington.db.mapper.AbstractDBMapper;
/**
 * @author Rama_Rao
 *
 */

public class IrsTVItemFactMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_ITEM_STUDENT_SESSION = "TVfindByItemStudentSession";
	private static final String INSERT_INTO_TNITEMFACT = "TVinsertTVItemFact";
	private static final String UPDATE_BY_FACTID = "TVupdateTVItemFact";
	private static final String DELETE_BY_FACTID = "TVdeleteByItemFactId";
    private static final String UPDATE_CURRENTRESULT_STATUS = "TVupdateTVItemFactCurrentResult";
    
    public IrsTVItemFactMapper(Connection conn){
		super(conn);
	}
	
	public IrsTVItemFactData findByItemStudentSession(Long itemId, Long studentId, Long sessionId){
		IrsTVItemFactData template = new IrsTVItemFactData();
		template.setItemid(itemId);
		template.setStudentid(studentId);
		template.setSessionid(sessionId);
		return (IrsTVItemFactData) find(FIND_BY_ITEM_STUDENT_SESSION,template);    
    }
	
	public void insert(IrsTVItemFactData record)throws SQLException{
		insert(INSERT_INTO_TNITEMFACT, record);
    }	
	
	public void update(IrsTVItemFactData record)throws SQLException{
		update(UPDATE_BY_FACTID, record);
    }
    
    public void updateCurrentResultStatus(IrsTVItemFactData record)throws SQLException{
        update(UPDATE_CURRENTRESULT_STATUS, record);
    }
	
	public void delete(IrsTVItemFactData record)throws SQLException{
		if(record != null)	
			delete(DELETE_BY_FACTID, record);	
    }
}