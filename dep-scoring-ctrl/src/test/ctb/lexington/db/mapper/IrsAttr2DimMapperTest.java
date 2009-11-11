package test.ctb.lexington.db.mapper;

import java.sql.SQLException;
import com.ctb.lexington.db.irsdata.IrsAttr2DimData;
import com.ctb.lexington.db.mapper.IrsAttr2DimMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsAttr2DimMapperTest extends AbstractMapperTest{

	private IrsAttr2DimMapper irsAttr2DimMapper;
	private IrsAttr2DimData irsAttr2DimData;
	private static final Long NON_EXISTING_ATTR2ID = new Long(7);
	private static final String NON_EXISTING_NAME = "ASIAN";
	private static final String NON_EXISTING_TYPE = "ETHNICITY";
	
	public IrsAttr2DimMapperTest(String name){
		super(name);	 
	}
	
	protected void setUp()throws Exception{
        super.setUp();
        conn = getIRSConnection();
        irsAttr2DimMapper = new IrsAttr2DimMapper(conn);
        irsAttr2DimData = new IrsAttr2DimData();
        irsAttr2DimData.setAttr2Id(NON_EXISTING_ATTR2ID);
        irsAttr2DimData.setName(NON_EXISTING_NAME);
        irsAttr2DimData.setType(NON_EXISTING_TYPE);
    }
	
	public void testInsertNonExistingRecord()throws SQLException{
		assertNull(irsAttr2DimMapper.findByAttr2DimId(NON_EXISTING_ATTR2ID));
		irsAttr2DimMapper.insert(irsAttr2DimData);
		assertNotNull(irsAttr2DimMapper.findByAttr2DimId(NON_EXISTING_ATTR2ID));
		conn.commit();
	}
	
	public void testUpdate()throws SQLException{
		assertNotNull(irsAttr2DimMapper.findByAttr2DimId(NON_EXISTING_ATTR2ID));
		IrsAttr2DimData record = irsAttr2DimMapper.findByAttr2DimId(NON_EXISTING_ATTR2ID);
		assertNotNull(record);
		final String oldName = record.getName();
		final String oldType = record.getType();
		record.setName("AsianTest");
		record.setType("ETHNICITY");
		try{
			irsAttr2DimMapper.update(record);
			IrsAttr2DimData updatedRecord = irsAttr2DimMapper.findByAttr2DimId(NON_EXISTING_ATTR2ID);
			assertEquals("AsianTest",updatedRecord.getName());
			conn.commit();
		 	}finally{
		 		record.setName(oldName);
		 		record.setType(oldType);
		 		irsAttr2DimMapper.update(record);
		 		conn.commit();
		 	}
	}
	
	public void testDelete()throws SQLException{
		assertNotNull(irsAttr2DimMapper.findByAttr2DimId(NON_EXISTING_ATTR2ID));
		irsAttr2DimMapper.delete(NON_EXISTING_ATTR2ID);
		conn.commit();
		assertNull(irsAttr2DimMapper.findByAttr2DimId(NON_EXISTING_ATTR2ID));
	}
	
	protected void tearDown()throws SQLException{
		conn.close();
	}
}