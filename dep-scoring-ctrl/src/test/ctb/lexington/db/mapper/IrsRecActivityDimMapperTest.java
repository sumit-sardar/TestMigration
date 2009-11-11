package test.ctb.lexington.db.mapper;

import java.sql.SQLException;
import com.ctb.lexington.db.irsdata.IrsRecActivityDimData;
import com.ctb.lexington.db.mapper.IrsRecActivityDimMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsRecActivityDimMapperTest extends AbstractMapperTest{

	private IrsRecActivityDimMapper irsRecActivityDimMapper;
	private IrsRecActivityDimData irsRecActivityDimData;
	private static final Long NON_EXISTING_RECACTIVITYID = new Long(5);
	private static final String NON_EXISTING_NAME = "T1";
	
	public IrsRecActivityDimMapperTest(String name){
		super(name);	 
	}
	
	protected void setUp()throws Exception{
        super.setUp();
        conn = getIRSConnection();
        irsRecActivityDimMapper = new IrsRecActivityDimMapper(conn);
        irsRecActivityDimData = new IrsRecActivityDimData();
        irsRecActivityDimData.setRecActivityid(NON_EXISTING_RECACTIVITYID);
        irsRecActivityDimData.setName(NON_EXISTING_NAME);
    }
	
	public void testInsertNonExistingRecord()throws SQLException{
		assertNull(irsRecActivityDimMapper.findByRecActivityId(NON_EXISTING_RECACTIVITYID));
		irsRecActivityDimMapper.insert(irsRecActivityDimData);
		assertNotNull(irsRecActivityDimMapper.findByRecActivityId(NON_EXISTING_RECACTIVITYID));
		conn.commit();
	}
	
	public void testUpdate()throws SQLException{
		assertNotNull(irsRecActivityDimMapper.findByRecActivityId(NON_EXISTING_RECACTIVITYID));
		IrsRecActivityDimData record = irsRecActivityDimMapper.findByRecActivityId(NON_EXISTING_RECACTIVITYID);
		assertNotNull(record);
		final String oldName = record.getName();
		record.setName("T2");
		try{
			irsRecActivityDimMapper.update(record);
			conn.commit();
			IrsRecActivityDimData updatedRecord = irsRecActivityDimMapper.findByRecActivityId(NON_EXISTING_RECACTIVITYID);
			assertEquals("T2",updatedRecord.getName());
		 	}finally{
		 		record.setName(oldName);
		 		irsRecActivityDimMapper.update(record);
		 		conn.commit();
		 	}
	}
	
	public void testDelete()throws SQLException{
		assertNotNull(irsRecActivityDimMapper.findByRecActivityId(NON_EXISTING_RECACTIVITYID));
		irsRecActivityDimMapper.delete(NON_EXISTING_RECACTIVITYID);
		conn.commit();
		assertNull(irsRecActivityDimMapper.findByRecActivityId(NON_EXISTING_RECACTIVITYID));
	}
	
	protected void tearDown()throws SQLException{
		conn.close();
	}
}