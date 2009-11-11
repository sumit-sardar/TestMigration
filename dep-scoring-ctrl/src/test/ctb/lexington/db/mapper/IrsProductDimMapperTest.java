package test.ctb.lexington.db.mapper;

import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.IrsProductDimData;
import com.ctb.lexington.db.mapper.IrsProductDimMapper;


/**
 * @author Rama_Rao
 *
 */

public class IrsProductDimMapperTest extends AbstractMapperTest{

	private IrsProductDimMapper irsProductDimMapper;
	private IrsProductDimData irsProductDimData;

	private static final Long NON_EXISTING_PRODUCT_ID = new Long(151515);
	private static final String NON_EXISTING_NAME = "TESTPRODUCT";
	
	public IrsProductDimMapperTest(String name){
		super(name);	 
	} 
	
	protected void setUp()throws Exception{
        super.setUp();
        conn = getIRSConnection();
        irsProductDimMapper = new IrsProductDimMapper(conn);
        irsProductDimData = new IrsProductDimData();
        irsProductDimData.setProductid(NON_EXISTING_PRODUCT_ID);
        irsProductDimData.setName(NON_EXISTING_NAME);
    }
	
	public void testInsertNonExistingRecord()throws SQLException{
		assertNull(irsProductDimMapper.findByProductId(NON_EXISTING_PRODUCT_ID));
		irsProductDimMapper.insert(irsProductDimData);
		assertNotNull(irsProductDimMapper.findByProductId(NON_EXISTING_PRODUCT_ID));
		conn.commit();
	}
	
	public void testUpdate()throws SQLException{
		assertNotNull(irsProductDimMapper.findByProductId(NON_EXISTING_PRODUCT_ID));
		IrsProductDimData record = irsProductDimMapper.findByProductId(NON_EXISTING_PRODUCT_ID);
		assertNotNull(record);
		final String oldName = record.getName();
		record.setName("T2");
		try{
			irsProductDimMapper.update(record);
			conn.commit();
			IrsProductDimData updatedRecord = irsProductDimMapper.findByProductId(NON_EXISTING_PRODUCT_ID);
			assertEquals("T2",updatedRecord.getName());
		 	}finally{
		 		record.setName(oldName);
		 		irsProductDimMapper.update(record);
		 		conn.commit();
		 	}
	}
	
	public void testDelete()throws SQLException{
		assertNotNull(irsProductDimMapper.findByProductId(NON_EXISTING_PRODUCT_ID));
		irsProductDimMapper.delete(NON_EXISTING_PRODUCT_ID);
		conn.commit();
		assertNull(irsProductDimMapper.findByProductId(NON_EXISTING_PRODUCT_ID));
	}
	
	protected void tearDown()throws SQLException{
		conn.close();
	}
}	