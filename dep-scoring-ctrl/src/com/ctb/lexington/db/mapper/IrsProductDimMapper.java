package com.ctb.lexington.db.mapper; 

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.IrsProductDimData;

/**
 * @author Rama_Rao
 *
 */

public class IrsProductDimMapper extends AbstractDBMapper{
	
     private static final String FIND_BY_PRODUCTID = "findByProductDimId";
     private static final String INSERT_INTO_PRODUCTDIM = "insertProductDim";
     private static final String DELETE_BY_PRODUCTID = "deleteProductDim";
     private static final String UPDATE_BY_PRODUCTID = "updateProductDim";
     
     public IrsProductDimMapper(Connection conn){
    	 super(conn);
     }
     
     public IrsProductDimData findByProductId(Long productId){
    	 IrsProductDimData template = new IrsProductDimData();
    	 template.setProductid(productId);
    	 return (IrsProductDimData) find(FIND_BY_PRODUCTID,template);
     }
     
     public IrsProductDimData insert(IrsProductDimData record)throws SQLException{
    	 insert(INSERT_INTO_PRODUCTDIM,record);
    	 return findByProductId(record.getProductid());
     }
       
     public void update(IrsProductDimData record)throws SQLException{
    	  update(UPDATE_BY_PRODUCTID, record);
     }
      
     public void delete(Long productId)throws SQLException{
    	  delete(DELETE_BY_PRODUCTID,productId);
     }
} 