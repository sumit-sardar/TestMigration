package test.ctb.lexington.db.mapper;

import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.IrsLevelDimData;
import com.ctb.lexington.db.mapper.IrsLevelDimMapper;


/**
 * @author Rama_Rao
 *
 */

public class IrsLevelDimMapperTest extends AbstractMapperTest{

	private IrsLevelDimMapper irsLevelDimMapper;
	private IrsLevelDimData irsLevelDimData;
	private static final Long NON_EXISTING_LEVELID = new Long(7);
	private static final String NON_EXISTING_NAME = "T1";
	
	public IrsLevelDimMapperTest(String name){
		super(name);	 
	}
	
	protected void setUp()throws Exception{
        super.setUp();
        conn = getIRSConnection();
        irsLevelDimMapper = new IrsLevelDimMapper(conn);
        irsLevelDimData = new IrsLevelDimData();
        irsLevelDimData.setLevelid(NON_EXISTING_LEVELID);
        irsLevelDimData.setName(NON_EXISTING_NAME);
    }
	
	public void testInsertNonExistingRecord()throws SQLException{
		assertNull(irsLevelDimMapper.findByLevelId(NON_EXISTING_LEVELID));
		irsLevelDimMapper.insert(irsLevelDimData);
		assertNotNull(irsLevelDimMapper.findByLevelId(NON_EXISTING_LEVELID));
		conn.commit();
	}
	
	public void testUpdate()throws SQLException{
		assertNotNull(irsLevelDimMapper.findByLevelId(NON_EXISTING_LEVELID));
		IrsLevelDimData record = irsLevelDimMapper.findByLevelId(NON_EXISTING_LEVELID);
		assertNotNull(record);
		final String oldName = record.getName();
		record.setName("T2");
		try{
			irsLevelDimMapper.update(record);
			conn.commit();
			IrsLevelDimData updatedRecord = irsLevelDimMapper.findByLevelId(NON_EXISTING_LEVELID);
			assertEquals("T2",updatedRecord.getName());
		 	}finally{
		 		record.setName(oldName);
		 		irsLevelDimMapper.update(record);
		 		conn.commit();
		 	}	
	}
	
	public void testDelete()throws SQLException{
		assertNotNull(irsLevelDimMapper.findByLevelId(NON_EXISTING_LEVELID));
		irsLevelDimMapper.delete(NON_EXISTING_LEVELID);
		conn.commit();
		assertNull(irsLevelDimMapper.findByLevelId(NON_EXISTING_LEVELID));
	}
	
	protected void tearDown()throws SQLException{
		conn.close();
	}
}