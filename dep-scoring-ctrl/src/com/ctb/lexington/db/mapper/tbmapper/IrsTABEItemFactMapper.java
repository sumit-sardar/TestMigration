package com.ctb.lexington.db.mapper.tbmapper;

import com.ctb.lexington.db.irsdata.irstbdata.IrsTABEItemFactData;
import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.mapper.AbstractDBMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsTABEItemFactMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_ITEM_STUDENT_SESSION = "TBfindByItemStudentSession";
	private static final String INSERT_INTO_TABEITEMFACT = "TBinsertTABEItemFact";
	private static final String UPDATE_BY_FACTID = "TBupdateTABEItemFact";
	private static final String DELETE_BY_FACTID = "TBdeleteByItemFactId";
    private static final String UPDATE_CURRENTRESULT_STATUS = "TBupdateTABEItemFactCurrentResult";
    
    public IrsTABEItemFactMapper(Connection conn){
		super(conn);
	}
	
	public IrsTABEItemFactData findByItemStudentSession(Long itemId, Long studentId, Long sessionId){
		IrsTABEItemFactData template = new IrsTABEItemFactData();
		template.setItemid(itemId);
		template.setStudentid(studentId);
		template.setSessionid(sessionId);
		return (IrsTABEItemFactData) find(FIND_BY_ITEM_STUDENT_SESSION,template);    
    }
	
	public void insert(IrsTABEItemFactData record)throws SQLException{
		insert(INSERT_INTO_TABEITEMFACT, record);
    }	
	
	public void update(IrsTABEItemFactData record)throws SQLException{
		update(UPDATE_BY_FACTID, record);
    }
    
    public void updateCurrentResultStatus(IrsTABEItemFactData record)throws SQLException{
        update(UPDATE_CURRENTRESULT_STATUS, record);
    }
	
	public void delete(IrsTABEItemFactData record)throws SQLException{
		if(record != null)	
			delete(DELETE_BY_FACTID, record);	
    }
}