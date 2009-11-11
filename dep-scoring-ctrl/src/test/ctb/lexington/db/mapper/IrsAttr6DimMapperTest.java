package test.ctb.lexington.db.mapper;

import java.sql.SQLException;
import com.ctb.lexington.db.irsdata.IrsAttr6DimData;
import com.ctb.lexington.db.mapper.IrsAttr6DimMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsAttr6DimMapperTest extends AbstractMapperTest{

	private IrsAttr6DimMapper irsAttr6DimMapper;
	private IrsAttr6DimData irsAttr6DimData;
	private static final Long NON_EXISTING_ATTR6ID = new Long(7);
	private static final String NON_EXISTING_NAME = "EMPLOYEETEST";
	private static final String NON_EXISTING_TYPE = "LABORFORCE_STATUS_TEST";
	
	public IrsAttr6DimMapperTest(String name){
		super(name);	 
	}
	
	protected void setUp()throws Exception{
        super.setUp();
        conn = getIRSConnection();
        irsAttr6DimMapper = new IrsAttr6DimMapper(conn);
        irsAttr6DimData = new IrsAttr6DimData();
        irsAttr6DimData.setAttr6Id(NON_EXISTING_ATTR6ID);
        irsAttr6DimData.setName(NON_EXISTING_NAME);
        irsAttr6DimData.setType(NON_EXISTING_TYPE);
    }
	
	public void testInsertNonExistingRecord()throws SQLException{
		assertNull(irsAttr6DimMapper.findByAttr6Id(NON_EXISTING_ATTR6ID));
		irsAttr6DimMapper.insert(irsAttr6DimData);
		assertNotNull(irsAttr6DimMapper.findByAttr6Id(NON_EXISTING_ATTR6ID));
		conn.commit();
	}
	
	public void testUpdate()throws SQLException{
		assertNotNull(irsAttr6DimMapper.findByAttr6Id(NON_EXISTING_ATTR6ID));
		IrsAttr6DimData record = irsAttr6DimMapper.findByAttr6Id(NON_EXISTING_ATTR6ID);
		assertNotNull(record);
		final String oldName = record.getName();
		final String oldType = record.getType();
		record.setName("EMPLOYEE2TEST");
		record.setType("LABORFORCE_STATUS_TEST2");
		try{
			irsAttr6DimMapper.update(record);
			IrsAttr6DimData updatedRecord = irsAttr6DimMapper.findByAttr6Id(NON_EXISTING_ATTR6ID);
			assertEquals("EMPLOYEE2TEST",updatedRecord.getName());
			conn.commit();
		 	}finally{
		 		record.setName(oldName);
		 		record.setType(oldType);
		 		irsAttr6DimMapper.update(record);
		 		conn.commit();
		 	}
	}
	
	public void testDelete()throws SQLException{
		assertNotNull(irsAttr6DimMapper.findByAttr6Id(NON_EXISTING_ATTR6ID));
		irsAttr6DimMapper.delete(NON_EXISTING_ATTR6ID);
		conn.commit();
		assertNull(irsAttr6DimMapper.findByAttr6Id(NON_EXISTING_ATTR6ID));
	}	
	
	protected void tearDown()throws SQLException{
		conn.close();
	}
}