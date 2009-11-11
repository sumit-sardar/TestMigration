package test.ctb.lexington.db.mapper;

import java.sql.SQLException;
import com.ctb.lexington.db.irsdata.IrsAttr4DimData;
import com.ctb.lexington.db.mapper.IrsAttr4DimMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsAttr4DimMapperTest extends AbstractMapperTest{

	private IrsAttr4DimMapper irsAttr4DimMapper;
	private IrsAttr4DimData irsAttr4DimData;
	private static final Long NON_EXISTING_ATTR4ID = new Long(7);
	private static final String NON_EXISTING_NAME = "MTEST";
	private static final String NON_EXISTING_TYPE = "GENDERTEST";
	
	public IrsAttr4DimMapperTest(String name){
		super(name);	 
	}
	
	protected void setUp()throws Exception{
        super.setUp();
        conn = getIRSConnection();
        irsAttr4DimMapper = new IrsAttr4DimMapper(conn);
        irsAttr4DimData = new IrsAttr4DimData();
        irsAttr4DimData.setAttr4Id(NON_EXISTING_ATTR4ID);
        irsAttr4DimData.setName(NON_EXISTING_NAME);
        irsAttr4DimData.setType(NON_EXISTING_TYPE);
    }
	
	public void testInsertNonExistingRecord()throws SQLException{
		assertNull(irsAttr4DimMapper.findByAttr4Id(NON_EXISTING_ATTR4ID));
		irsAttr4DimMapper.insert(irsAttr4DimData);
		assertNotNull(irsAttr4DimMapper.findByAttr4Id(NON_EXISTING_ATTR4ID));
		conn.commit();
	}
	
	public void testUpdate()throws SQLException{
		assertNotNull(irsAttr4DimMapper.findByAttr4Id(NON_EXISTING_ATTR4ID));
		IrsAttr4DimData record = irsAttr4DimMapper.findByAttr4Id(NON_EXISTING_ATTR4ID);
		assertNotNull(record);
		final String oldName = record.getName();
		final String oldType = record.getType();
		record.setName("MALETEST");
		record.setType("GENDERTEST");
		try{
			irsAttr4DimMapper.update(record);
			IrsAttr4DimData updatedRecord = irsAttr4DimMapper.findByAttr4Id(NON_EXISTING_ATTR4ID);
			assertEquals("MALETEST",updatedRecord.getName());
			conn.commit();
		 	}finally{
		 		record.setName(oldName);
		 		record.setType(oldType);
		 		irsAttr4DimMapper.update(record);
		 		conn.commit();
		 	}
	}
	
	public void testDelete()throws SQLException{
		assertNotNull(irsAttr4DimMapper.findByAttr4Id(NON_EXISTING_ATTR4ID));
		irsAttr4DimMapper.delete(NON_EXISTING_ATTR4ID);
		conn.commit();
		assertNull(irsAttr4DimMapper.findByAttr4Id(NON_EXISTING_ATTR4ID));
	}
	
	protected void tearDown()throws SQLException{
		conn.close();
	}
}