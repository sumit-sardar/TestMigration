package test.ctb.lexington.db.mapper;

import java.sql.SQLException;
import com.ctb.lexington.db.irsdata.IrsAttr5DimData;
import com.ctb.lexington.db.mapper.IrsAttr5DimMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsAttr5DimMapperTest extends AbstractMapperTest{

	private IrsAttr5DimMapper irsAttr5DimMapper;
	private IrsAttr5DimData irsAttr5DimData;
	private static final Long NON_EXISTING_ATTR5ID = new Long(7);
	private static final String NON_EXISTING_NAME = "YTEST";
	private static final String NON_EXISTING_TYPE = "IEPTEST";
	
	public IrsAttr5DimMapperTest(String name){
		super(name);	 
	}
	
	protected void setUp()throws Exception{
        super.setUp();
        conn = getIRSConnection();
        irsAttr5DimMapper = new IrsAttr5DimMapper(conn);
        irsAttr5DimData = new IrsAttr5DimData();
        irsAttr5DimData.setAttr5Id(NON_EXISTING_ATTR5ID);
        irsAttr5DimData.setName(NON_EXISTING_NAME);
        irsAttr5DimData.setType(NON_EXISTING_TYPE);
    }
	
	public void testInsertNonExistingRecord()throws SQLException{
		assertNull(irsAttr5DimMapper.findByAttr5Id(NON_EXISTING_ATTR5ID));
		irsAttr5DimMapper.insert(irsAttr5DimData);
		assertNotNull(irsAttr5DimMapper.findByAttr5Id(NON_EXISTING_ATTR5ID));
		conn.commit();
	}
	
	public void testUpdate()throws SQLException{
		assertNotNull(irsAttr5DimMapper.findByAttr5Id(NON_EXISTING_ATTR5ID));
		IrsAttr5DimData record = irsAttr5DimMapper.findByAttr5Id(NON_EXISTING_ATTR5ID);
		assertNotNull(record);
		final String oldName = record.getName();
		final String oldType = record.getType();
		record.setName("NTEST");
		record.setType("IEP2TEST");
		try{
			irsAttr5DimMapper.update(record);
			IrsAttr5DimData updatedRecord = irsAttr5DimMapper.findByAttr5Id(NON_EXISTING_ATTR5ID);
			assertEquals("NTEST",updatedRecord.getName());
			conn.commit();
		 	}finally{
		 		record.setName(oldName);
		 		record.setType(oldType);
		 		irsAttr5DimMapper.update(record);
		 		conn.commit();
		 	}
	}
	
	public void testDelete()throws SQLException{
		assertNotNull(irsAttr5DimMapper.findByAttr5Id(NON_EXISTING_ATTR5ID));
		irsAttr5DimMapper.delete(NON_EXISTING_ATTR5ID);
		conn.commit();
		assertNull(irsAttr5DimMapper.findByAttr5Id(NON_EXISTING_ATTR5ID));
	}
	
	protected void tearDown()throws SQLException{
		conn.close();
	}
}