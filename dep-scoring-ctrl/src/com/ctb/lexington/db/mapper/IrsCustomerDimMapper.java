package com.ctb.lexington.db.mapper;

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.IrsCustomerDimData;

/**
 * @author Rama_Rao
 *
 */

public class IrsCustomerDimMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_CUSTOMERID = "findByCustomerID";
	private static final String INSERT_INTO_CUSTOMERDIM = "insertCustomerDim";
	private static final String UPDATE_BY_CUSTOMERID = "updateCustomerDim";
    private static final String DELETE_BY_CUSTOMERID = "deleteCustomerDim";
    
	public IrsCustomerDimMapper(Connection conn){
		super(conn);
	}
	
	public IrsCustomerDimData findByCustomerId(Long customerId){
		IrsCustomerDimData template = new IrsCustomerDimData();
		template.setCustomerid(customerId);
		return (IrsCustomerDimData) find(FIND_BY_CUSTOMERID,template);    
    }
	
	public IrsCustomerDimData insert(IrsCustomerDimData record)throws SQLException{
		insert(INSERT_INTO_CUSTOMERDIM, record);
		return findByCustomerId(record.getCustomerid());
    }	
	
	public void update(IrsCustomerDimData record)throws SQLException{
        update(UPDATE_BY_CUSTOMERID, record);
    }
	
	public void delete(Long customerId)throws SQLException{
		delete(DELETE_BY_CUSTOMERID, customerId);
	}
}