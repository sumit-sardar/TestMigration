package test.ctb.lexington.db.mapper;

import java.sql.SQLException;
import com.ctb.lexington.db.irsdata.IrsAttr10DimData;
import com.ctb.lexington.db.mapper.IrsAttr10DimMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsAttr10DimMapperTest extends AbstractMapperTest{

	private IrsAttr10DimMapper irsAttr10DimMapper;
	private IrsAttr10DimData irsAttr10DimData;
	private static final Long NON_EXISTING_ATTR10ID = new Long(10);
	private static final String NON_EXISTING_NAME = "YTEST";
	private static final String NON_EXISTING_TYPE = "SECTION504_TEST";
	
	public IrsAttr10DimMapperTest(String name){
		 super(name);	 
	}
	
	protected void setUp()throws Exception{
        super.setUp();
        conn = getIRSConnection();
        irsAttr10DimMapper = new IrsAttr10DimMapper(conn);
        irsAttr10DimData = new IrsAttr10DimData();
        irsAttr10DimData.setAttr10Id(NON_EXISTING_ATTR10ID);
        irsAttr10DimData.setName(NON_EXISTING_NAME);
        irsAttr10DimData.setType(NON_EXISTING_TYPE);
    }
	
	public void testInsertNonExistingRecord()throws SQLException{
		assertNull(irsAttr10DimMapper.findByAttr10Id(NON_EXISTING_ATTR10ID));
		irsAttr10DimMapper.insert(irsAttr10DimData);
		assertNotNull(irsAttr10DimMapper.findByAttr10Id(NON_EXISTING_ATTR10ID));
		conn.commit();
	}
	
	public void testUpdate()throws SQLException{
		assertNotNull(irsAttr10DimMapper.findByAttr10Id(NON_EXISTING_ATTR10ID));
		IrsAttr10DimData record = irsAttr10DimMapper.findByAttr10Id(NON_EXISTING_ATTR10ID);
		assertNotNull(record);
		final String oldName = record.getName();
		final String oldType = record.getType();
		record.setName("YTEST2");
		record.setType("SECTION504_TEST2");
		try{
			irsAttr10DimMapper.update(record);
			IrsAttr10DimData updatedRecord = irsAttr10DimMapper.findByAttr10Id(NON_EXISTING_ATTR10ID);
			assertEquals("YTEST2",updatedRecord.getName());
			conn.commit();
		 }finally{
			 record.setName(oldName);
			 record.setType(oldType);
			 irsAttr10DimMapper.update(record);
			 conn.commit();
		 }
	}
	
	public void testDelete()throws SQLException{
		assertNotNull(irsAttr10DimMapper.findByAttr10Id(NON_EXISTING_ATTR10ID));
		irsAttr10DimMapper.delete(NON_EXISTING_ATTR10ID);
		conn.commit();
		assertNull(irsAttr10DimMapper.findByAttr10Id(NON_EXISTING_ATTR10ID));
	}
	
	protected void tearDown()throws SQLException{
		conn.close();
	}
}