package test.ctb.lexington.db.mapper;

import java.sql.SQLException;
import com.ctb.lexington.db.irsdata.IrsAttr9DimData;
import com.ctb.lexington.db.mapper.IrsAttr9DimMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsAttr9DimMapperTest extends AbstractMapperTest{

	private IrsAttr9DimMapper irsAttr9DimMapper;
	private IrsAttr9DimData irsAttr9DimData;
	private static final Long NON_EXISTING_ATTR9ID = new Long(9);
	private static final String NON_EXISTING_NAME = "TIMEDTEST";
	private static final String NON_EXISTING_TYPE = "OTACCOMMODATIONS_TEST";
	
	public IrsAttr9DimMapperTest(String name){
		super(name);	 
	}
	
	protected void setUp()throws Exception{
        super.setUp();
        conn = getIRSConnection();
        irsAttr9DimMapper = new IrsAttr9DimMapper(conn);
        irsAttr9DimData = new IrsAttr9DimData();
        irsAttr9DimData.setAttr9Id(NON_EXISTING_ATTR9ID);
        irsAttr9DimData.setName(NON_EXISTING_NAME);
        irsAttr9DimData.setType(NON_EXISTING_TYPE);
    }
	
	public void testInsertNonExistingRecord()throws SQLException{
		assertNull(irsAttr9DimMapper.findByAttr9Id(NON_EXISTING_ATTR9ID));
		irsAttr9DimMapper.insert(irsAttr9DimData);
		assertNotNull(irsAttr9DimMapper.findByAttr9Id(NON_EXISTING_ATTR9ID));
		conn.commit();
	}
	
	public void testUpdate()throws SQLException{
		assertNotNull(irsAttr9DimMapper.findByAttr9Id(NON_EXISTING_ATTR9ID));
		IrsAttr9DimData record = irsAttr9DimMapper.findByAttr9Id(NON_EXISTING_ATTR9ID);
		assertNotNull(record);
		final String oldName = record.getName();
		final String oldType = record.getType();
		record.setName("TEST2");
		record.setType("OTACCOMMODATIONS_TEST2");
		try{
			irsAttr9DimMapper.update(record);
			IrsAttr9DimData updatedRecord = irsAttr9DimMapper.findByAttr9Id(NON_EXISTING_ATTR9ID);
			assertEquals("TEST2",updatedRecord.getName());
			conn.commit();
		 	}finally{
		 		record.setName(oldName);
		 		record.setType(oldType);
		 		irsAttr9DimMapper.update(record);
		 		conn.commit();
		 	}
	}
	
	public void testDelete()throws SQLException{
		assertNotNull(irsAttr9DimMapper.findByAttr9Id(NON_EXISTING_ATTR9ID));
		irsAttr9DimMapper.delete(NON_EXISTING_ATTR9ID);
		conn.commit();
		assertNull(irsAttr9DimMapper.findByAttr9Id(NON_EXISTING_ATTR9ID));
	}
	
	protected void tearDown()throws SQLException{
		conn.close();
	}
}