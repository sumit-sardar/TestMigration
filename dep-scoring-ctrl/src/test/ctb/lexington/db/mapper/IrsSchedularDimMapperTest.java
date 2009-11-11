package test.ctb.lexington.db.mapper;

import com.ctb.lexington.db.irsdata.IrsSchedulerDimData;
import com.ctb.lexington.db.mapper.IrsSchedulerDimMapper;
import java.sql.SQLException;

/**
 * @author Rama_Rao
 *
 */

public class IrsSchedularDimMapperTest extends AbstractMapperTest{

	private IrsSchedulerDimMapper irsSchedulerDimMapper;
	private IrsSchedulerDimData irsSchedulerDimData;
	private static final Long NON_EXISTING_SCHEDULARID = new Long(22);
	private static final String NON_EXISTING_NAME = "TABETEST";
	
	public IrsSchedularDimMapperTest(String name){
		super(name);	 
	}
	
	protected void setUp()throws Exception{
        super.setUp();
        conn = getIRSConnection();
        irsSchedulerDimMapper = new IrsSchedulerDimMapper(conn);
        irsSchedulerDimData = new IrsSchedulerDimData();
        irsSchedulerDimData.setSchedulerid(NON_EXISTING_SCHEDULARID);
        irsSchedulerDimData.setName(NON_EXISTING_NAME);
    }
	
	public void testInsertNonExistingRecord()throws SQLException{
     	assertNull(irsSchedulerDimMapper.findBySchedulerId(NON_EXISTING_SCHEDULARID));
     	irsSchedulerDimMapper.insert(irsSchedulerDimData);
     	assertNotNull(irsSchedulerDimMapper.findBySchedulerId(NON_EXISTING_SCHEDULARID));
     	conn.commit();
	}
	
	public void testUpdate()throws SQLException{
		assertNotNull(irsSchedulerDimMapper.findBySchedulerId(NON_EXISTING_SCHEDULARID));
		IrsSchedulerDimData record = irsSchedulerDimMapper.findBySchedulerId(NON_EXISTING_SCHEDULARID);
		assertNotNull(record);
		final String oldName = record.getName();
		record.setName("Test2");
		try{
			irsSchedulerDimMapper.update(record);
			conn.commit();
			IrsSchedulerDimData updatedRecord = irsSchedulerDimMapper.findBySchedulerId(NON_EXISTING_SCHEDULARID);
			assertEquals("Test2",updatedRecord.getName());
		 	}finally{
		 		record.setName(oldName);
		 		irsSchedulerDimMapper.update(record);
		 		conn.commit();
		 	}
	}
	
	public void testDelete()throws SQLException{
		assertNotNull(irsSchedulerDimMapper.findBySchedulerId(NON_EXISTING_SCHEDULARID));
		irsSchedulerDimMapper.delete(NON_EXISTING_SCHEDULARID);
		conn.commit();
		assertNull(irsSchedulerDimMapper.findBySchedulerId(NON_EXISTING_SCHEDULARID));
	}
	
	protected void tearDown()throws SQLException{
		conn.close();
	}
}