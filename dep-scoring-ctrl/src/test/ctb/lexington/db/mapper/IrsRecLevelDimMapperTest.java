package test.ctb.lexington.db.mapper;

import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.IrsRecLevelDimData;
import com.ctb.lexington.db.mapper.IrsRecLevelDimMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsRecLevelDimMapperTest extends AbstractMapperTest{

	private IrsRecLevelDimMapper irsRecLevlDimMapper;
	private IrsRecLevelDimData irsRecLevlDimData;
	private static final Long NON_EXISTING_RECLEVELID = new Long(5);
	private static final String NON_EXISTING_NAME = "T1";
	
	public IrsRecLevelDimMapperTest(String name){
		super(name);	 
	}
	
	protected void setUp()throws Exception{
        super.setUp();
        conn = getIRSConnection();
        irsRecLevlDimMapper = new IrsRecLevelDimMapper(conn);
        irsRecLevlDimData = new IrsRecLevelDimData();
        irsRecLevlDimData.setRecLevelid(NON_EXISTING_RECLEVELID);
        irsRecLevlDimData.setName(NON_EXISTING_NAME);
    }
	
	public void testInsertNonExistingRecord()throws SQLException{
		assertNull(irsRecLevlDimMapper.findByRecLevelId(NON_EXISTING_RECLEVELID));
		irsRecLevlDimMapper.insert(irsRecLevlDimData);
		assertNotNull(irsRecLevlDimMapper.findByRecLevelId(NON_EXISTING_RECLEVELID));
		conn.commit();
	}
	
	public void testUpdate()throws SQLException{
		assertNotNull(irsRecLevlDimMapper.findByRecLevelId(NON_EXISTING_RECLEVELID));
		IrsRecLevelDimData record = irsRecLevlDimMapper.findByRecLevelId(NON_EXISTING_RECLEVELID);
		assertNotNull(record);
		final String oldName = record.getName();
		record.setName("T2");
		try{
			irsRecLevlDimMapper.update(record);
			conn.commit();
			IrsRecLevelDimData updatedRecord = irsRecLevlDimMapper.findByRecLevelId(NON_EXISTING_RECLEVELID);
			assertEquals("T2",updatedRecord.getName());
		 	}finally{
		 		record.setName(oldName);
		 		irsRecLevlDimMapper.update(record);
		 		conn.commit();
		 	}
	}
		
	public void testDelete()throws SQLException{
		assertNotNull(irsRecLevlDimMapper.findByRecLevelId(NON_EXISTING_RECLEVELID));
		irsRecLevlDimMapper.delete(NON_EXISTING_RECLEVELID);
		conn.commit();
		assertNull(irsRecLevlDimMapper.findByRecLevelId(NON_EXISTING_RECLEVELID));
	}
	
	protected void tearDown()throws SQLException{
		conn.close();
	}
}