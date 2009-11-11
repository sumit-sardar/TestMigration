package test.ctb.lexington.db.mapper;

import java.sql.SQLException;
import com.ctb.lexington.db.irsdata.IrsCustomerDimData;
import com.ctb.lexington.db.mapper.IrsCustomerDimMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsCustomerDimMapperTest extends AbstractMapperTest{

	private IrsCustomerDimMapper irsCustomerDimMapper;
	private IrsCustomerDimData irsCustomerDimData;
	private static final Long NON_EXISTING_CUSTOMERID = new Long(4);
	private static final String NON_EXISTING_NAME = "T1";
	
	public IrsCustomerDimMapperTest(String name){
		super(name);	 
	}
	
	protected void setUp()throws Exception{
        super.setUp();
        conn = getIRSConnection();
        irsCustomerDimMapper = new IrsCustomerDimMapper(conn);
        irsCustomerDimData = new IrsCustomerDimData();
        irsCustomerDimData.setCustomerid(NON_EXISTING_CUSTOMERID);
        irsCustomerDimData.setName(NON_EXISTING_NAME);
	}
	
	public void testInsertNonExistingRecord()throws SQLException{
		assertNull(irsCustomerDimMapper.findByCustomerId(NON_EXISTING_CUSTOMERID));
		irsCustomerDimMapper.insert(irsCustomerDimData);
		assertNotNull(irsCustomerDimMapper.findByCustomerId(NON_EXISTING_CUSTOMERID));
		conn.commit();
	}
	
	public void testUpdate()throws SQLException{
		assertNotNull(irsCustomerDimMapper.findByCustomerId(NON_EXISTING_CUSTOMERID));
		IrsCustomerDimData record = irsCustomerDimMapper.findByCustomerId(NON_EXISTING_CUSTOMERID);
		assertNotNull(record);
		final String oldName = record.getName();
		record.setName("T2");
		try{
			irsCustomerDimMapper.update(record);
			conn.commit();
			IrsCustomerDimData updatedRecord = irsCustomerDimMapper.findByCustomerId(NON_EXISTING_CUSTOMERID);
			assertEquals("T2",updatedRecord.getName());
		 	}finally{
		 		record.setName(oldName);
		 		irsCustomerDimMapper.update(record);
		 		conn.commit();
		 	}
	}
	
	public void testDelete()throws SQLException{
		assertNotNull(irsCustomerDimMapper.findByCustomerId(NON_EXISTING_CUSTOMERID));
		irsCustomerDimMapper.delete(NON_EXISTING_CUSTOMERID);
		conn.commit();
		assertNull(irsCustomerDimMapper.findByCustomerId(NON_EXISTING_CUSTOMERID));
	}
	
	protected void tearDown()throws SQLException{
		conn.close();
	}
}