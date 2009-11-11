package test.ctb.lexington.db.mapper;

import java.sql.SQLException;
import com.ctb.lexington.db.irsdata.IrsAttr7DimData;
import com.ctb.lexington.db.mapper.IrsAttr7DimMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsAttr7DimMapperTest extends AbstractMapperTest{

	private IrsAttr7DimMapper irsAttr7DimMapper;
	private IrsAttr7DimData irsAttr7DimData;
	private static final Long NON_EXISTING_ATTR7ID = new Long(7);
	private static final String NON_EXISTING_NAME = "YTEST";
	private static final String NON_EXISTING_TYPE = "LEP_TEST";
	
	public IrsAttr7DimMapperTest(String name){
		super(name);	 
	}
	
	protected void setUp()throws Exception{
        super.setUp();
        conn = getIRSConnection();
        irsAttr7DimMapper = new IrsAttr7DimMapper(conn);
        irsAttr7DimData = new IrsAttr7DimData();
        irsAttr7DimData.setAttr7Id(NON_EXISTING_ATTR7ID);
        irsAttr7DimData.setName(NON_EXISTING_NAME);
        irsAttr7DimData.setType(NON_EXISTING_TYPE);
    }
	
	public void testInsertNonExistingRecord()throws SQLException{
		assertNull(irsAttr7DimMapper.findByAttr7Id(NON_EXISTING_ATTR7ID));
		irsAttr7DimMapper.insert(irsAttr7DimData);
		assertNotNull(irsAttr7DimMapper.findByAttr7Id(NON_EXISTING_ATTR7ID));
		conn.commit();
	}
	
	public void testUpdate()throws SQLException{
		assertNotNull(irsAttr7DimMapper.findByAttr7Id(NON_EXISTING_ATTR7ID));
		IrsAttr7DimData record = irsAttr7DimMapper.findByAttr7Id(NON_EXISTING_ATTR7ID);
		assertNotNull(record);
		final String oldName = record.getName();
		final String oldType = record.getType();
		record.setName("YTEST2");
		record.setType("LEP_TEST2");
		try{
			irsAttr7DimMapper.update(record);
			IrsAttr7DimData updatedRecord = irsAttr7DimMapper.findByAttr7Id(NON_EXISTING_ATTR7ID);
			assertEquals("YTEST2",updatedRecord.getName());
			conn.commit();
		 	}finally{
		 		record.setName(oldName);
		 		record.setType(oldType);
		 		irsAttr7DimMapper.update(record);
		 		conn.commit();
		 	}
	}
	
	public void testDelete()throws SQLException{
		assertNotNull(irsAttr7DimMapper.findByAttr7Id(NON_EXISTING_ATTR7ID));
		irsAttr7DimMapper.delete(NON_EXISTING_ATTR7ID);
		conn.commit();
		assertNull(irsAttr7DimMapper.findByAttr7Id(NON_EXISTING_ATTR7ID));
	}	
	
	protected void tearDown()throws SQLException{
		conn.close();
	}
}