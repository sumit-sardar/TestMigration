package test.ctb.lexington.db.mapper;

import java.sql.SQLException;
import com.ctb.lexington.db.irsdata.IrsAttr8DimData;
import com.ctb.lexington.db.mapper.IrsAttr8DimMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsAttr8DimMapperTest extends AbstractMapperTest{

	private IrsAttr8DimMapper irsAttr8DimMapper;
	private IrsAttr8DimData irsAttr8DimData;
	private static final Long NON_EXISTING_ATTR8ID = new Long(8);
	private static final String NON_EXISTING_NAME = "YTEST";
	private static final String NON_EXISTING_TYPE = "MIGRANT_TEST";
	
	public IrsAttr8DimMapperTest(String name){
		super(name);	 
	}
	
	protected void setUp()throws Exception{
        super.setUp();
        conn = getIRSConnection();
        irsAttr8DimMapper = new IrsAttr8DimMapper(conn);
        irsAttr8DimData = new IrsAttr8DimData();
        irsAttr8DimData.setAttr8Id(NON_EXISTING_ATTR8ID);
        irsAttr8DimData.setName(NON_EXISTING_NAME);
        irsAttr8DimData.setType(NON_EXISTING_TYPE);
    }
	
	public void testInsertNonExistingRecord()throws SQLException{
		assertNull(irsAttr8DimMapper.findByAttr8Id(NON_EXISTING_ATTR8ID));
		irsAttr8DimMapper.insert(irsAttr8DimData);
		assertNotNull(irsAttr8DimMapper.findByAttr8Id(NON_EXISTING_ATTR8ID));
		conn.commit();
	}
	
	public void testUpdate()throws SQLException{
		assertNotNull(irsAttr8DimMapper.findByAttr8Id(NON_EXISTING_ATTR8ID));
		IrsAttr8DimData record = irsAttr8DimMapper.findByAttr8Id(NON_EXISTING_ATTR8ID);
		assertNotNull(record);
		final String oldName = record.getName();
		final String oldType = record.getType();
		record.setName("YTEST2");
		record.setType("MIGRANT_TEST2");
		try{
			irsAttr8DimMapper.update(record);
			IrsAttr8DimData updatedRecord = irsAttr8DimMapper.findByAttr8Id(NON_EXISTING_ATTR8ID);
			assertEquals("YTEST2",updatedRecord.getName());
			conn.commit();
		 	}finally{
		 		record.setName(oldName);
		 		record.setType(oldType);
		 		irsAttr8DimMapper.update(record);
		 		conn.commit();
		 	}
	}
	
	public void testDelete()throws SQLException{
		assertNotNull(irsAttr8DimMapper.findByAttr8Id(NON_EXISTING_ATTR8ID));
		irsAttr8DimMapper.delete(NON_EXISTING_ATTR8ID);
		conn.commit();
		assertNull(irsAttr8DimMapper.findByAttr8Id(NON_EXISTING_ATTR8ID));
	}
	
	protected void tearDown()throws SQLException{
		conn.close();
	}
}