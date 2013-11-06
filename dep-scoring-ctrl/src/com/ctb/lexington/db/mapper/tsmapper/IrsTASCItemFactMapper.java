package com.ctb.lexington.db.mapper.tsmapper;

import com.ctb.lexington.db.irsdata.irstbdata.IrsTABEItemFactData;
import com.ctb.lexington.db.irsdata.irstsdata.IrsTASCItemFactData;

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.mapper.AbstractDBMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsTASCItemFactMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_ITEM_STUDENT_SESSION = "TSfindByItemStudentSession";
	private static final String INSERT_INTO_TABEITEMFACT = "TSinsertTABEItemFact";
	private static final String UPDATE_BY_FACTID = "TSupdateTABEItemFact";
	private static final String DELETE_BY_FACTID = "TSdeleteByItemFactId";
    private static final String UPDATE_CURRENTRESULT_STATUS = "TSupdateTABEItemFactCurrentResult";
    
    public IrsTASCItemFactMapper(Connection conn){
		super(conn);
	}
	
	public IrsTASCItemFactData findByItemStudentSession(Long itemId, Long studentId, Long sessionId){
		IrsTASCItemFactData template = new IrsTASCItemFactData();
		template.setItemid(itemId);
		template.setStudentid(studentId);
		template.setSessionid(sessionId);
		return (IrsTASCItemFactData) find(FIND_BY_ITEM_STUDENT_SESSION,template);    
    }
	
	public void insert(IrsTASCItemFactData record)throws SQLException{
		insert(INSERT_INTO_TABEITEMFACT, record);
    }	
	
	public void update(IrsTASCItemFactData record)throws SQLException{
		update(UPDATE_BY_FACTID, record);
    }
    
    public void updateCurrentResultStatus(IrsTASCItemFactData record)throws SQLException{
        update(UPDATE_CURRENTRESULT_STATUS, record);
    }
	
	public void delete(IrsTASCItemFactData record)throws SQLException{
		if(record != null)	
			delete(DELETE_BY_FACTID, record);	
    }
}