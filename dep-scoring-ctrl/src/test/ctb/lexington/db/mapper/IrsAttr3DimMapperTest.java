package test.ctb.lexington.db.mapper;

import java.sql.SQLException;
import com.ctb.lexington.db.irsdata.IrsAttr3DimData;
import com.ctb.lexington.db.mapper.IrsAttr3DimMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsAttr3DimMapperTest extends AbstractMapperTest{

	private IrsAttr3DimMapper irsAttr3DimMapper;
	private IrsAttr3DimData irsAttr3DimData;
	private static final Long NON_EXISTING_ATTR3ID = new Long(7);
	private static final String NON_EXISTING_NAME = "YTEST";
	private static final String NON_EXISTING_TYPE = "FREE_REDUCED_LUNCH";
	
	public IrsAttr3DimMapperTest(String name){
		super(name);	 
	}
	
	protected void setUp()throws Exception{
        super.setUp();
        conn = getIRSConnection();
        irsAttr3DimMapper = new IrsAttr3DimMapper(conn);
        irsAttr3DimData = new IrsAttr3DimData();
        irsAttr3DimData.setAttr3Id(NON_EXISTING_ATTR3ID);
        irsAttr3DimData.setName(NON_EXISTING_NAME);
        irsAttr3DimData.setType(NON_EXISTING_TYPE);
    }
	
	public void testInsertNonExistingRecord()throws SQLException{
		assertNull(irsAttr3DimMapper.findByAttr3Id(NON_EXISTING_ATTR3ID));
		irsAttr3DimMapper.insert(irsAttr3DimData);
		assertNotNull(irsAttr3DimMapper.findByAttr3Id(NON_EXISTING_ATTR3ID));
		conn.commit();
	}
	
	public void testUpdate()throws SQLException{
		assertNotNull(irsAttr3DimMapper.findByAttr3Id(NON_EXISTING_ATTR3ID));
		IrsAttr3DimData record = irsAttr3DimMapper.findByAttr3Id(NON_EXISTING_ATTR3ID);
		assertNotNull(record);
		final String oldName = record.getName();
		final String oldType = record.getType();
		record.setName("YTESTRAMA");
		record.setType("FREE_REDUCED_LUNCH_TEST");
		try{
			irsAttr3DimMapper.update(record);
			IrsAttr3DimData updatedRecord = irsAttr3DimMapper.findByAttr3Id(NON_EXISTING_ATTR3ID);
			assertEquals("YTESTRAMA",updatedRecord.getName());
			conn.commit();
		 	}finally{
		 		record.setName(oldName);
		 		record.setType(oldType);
		 		irsAttr3DimMapper.update(record);
		 		conn.commit();
		 	}
	}
	
	public void testDelete()throws SQLException{
		assertNotNull(irsAttr3DimMapper.findByAttr3Id(NON_EXISTING_ATTR3ID));
		irsAttr3DimMapper.delete(NON_EXISTING_ATTR3ID);
		conn.commit();
		assertNull(irsAttr3DimMapper.findByAttr3Id(NON_EXISTING_ATTR3ID));
	}
	
	protected void tearDown()throws SQLException{
		conn.close();
	}
}