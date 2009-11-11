package test.ctb.lexington.db.mapper;

import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.IrsAttr1DimData;
import com.ctb.lexington.db.mapper.IrsAttr1DimMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsAttr1DimMapperTest extends AbstractMapperTest{

	private IrsAttr1DimMapper irsAttr1DimMapper;
	private IrsAttr1DimData irsAttr1DimData;
	private static final Long NON_EXISTING_ATTR1_ID = new Long(3);
	private static final String NON_EXISTING_NAME = "U";
	private static final String NON_EXISTING_TYPE = "EELTEST";
	
	public IrsAttr1DimMapperTest(String name){
		super(name);	 
	}
	
	protected void setUp()throws Exception{
        super.setUp();
        conn = getIRSConnection();
        irsAttr1DimMapper = new IrsAttr1DimMapper(conn);
        irsAttr1DimData = new IrsAttr1DimData();
        irsAttr1DimData.setAttr1Id(NON_EXISTING_ATTR1_ID);
        irsAttr1DimData.setName(NON_EXISTING_NAME);
        irsAttr1DimData.setType(NON_EXISTING_TYPE);
    }
	
	public void testInsertNonExistingRecord()throws SQLException{
     	assertNull(irsAttr1DimMapper.findByAttr1DimId(NON_EXISTING_ATTR1_ID));
     	irsAttr1DimMapper.insert(irsAttr1DimData);
     	assertNotNull(irsAttr1DimMapper.findByAttr1DimId(NON_EXISTING_ATTR1_ID));
     	conn.commit();
	}
	
	public void testUpdate()throws SQLException{
		assertNotNull(irsAttr1DimMapper.findByAttr1DimId(NON_EXISTING_ATTR1_ID));
		IrsAttr1DimData record = irsAttr1DimMapper.findByAttr1DimId(NON_EXISTING_ATTR1_ID);
		assertNotNull(record);
		final String oldName = record.getName();
		final String oldType = record.getType();
		record.setName("YTest");
		record.setType("ELL");
		try{
			irsAttr1DimMapper.update(record);
			IrsAttr1DimData updatedRecord = irsAttr1DimMapper.findByAttr1DimId(NON_EXISTING_ATTR1_ID);
			assertEquals("YTest",updatedRecord.getName());
			conn.commit();
		 }finally{
			record.setName(oldName);
			record.setType(oldType);
			irsAttr1DimMapper.update(record);
			conn.commit();
		 }
	}
	
	public void testDelete()throws SQLException{
		assertNotNull(irsAttr1DimMapper.findByAttr1DimId(NON_EXISTING_ATTR1_ID));
		irsAttr1DimMapper.delete(NON_EXISTING_ATTR1_ID);
		conn.commit();
		assertNull(irsAttr1DimMapper.findByAttr1DimId(NON_EXISTING_ATTR1_ID));
	}
	
	protected void tearDown()throws SQLException{
		conn.close();
	}
}