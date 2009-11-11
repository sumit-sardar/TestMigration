package test.ctb.lexington.db.mapper;

import java.sql.SQLException;
import com.ctb.lexington.db.irsdata.IrsNRSLevelDimData;
import com.ctb.lexington.db.mapper.IrsNRSLevelDimMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsNRSLevelDimMapperTest extends AbstractMapperTest{

	private IrsNRSLevelDimMapper irsNRSLevlDimMapper;
	private IrsNRSLevelDimData irsNRSLevlDimData;
	private static final Long NON_EXISTING_NRSLEVELID = new Long(99);
	private static final String NON_EXISTING_NAME = "T1";
	
	public IrsNRSLevelDimMapperTest(String name){
		super(name);	 
	}
	
	protected void setUp()throws Exception{
        super.setUp();
        conn = getIRSConnection();
        irsNRSLevlDimMapper = new IrsNRSLevelDimMapper(conn);
        irsNRSLevlDimData = new IrsNRSLevelDimData();
        irsNRSLevlDimData.setNrsLevelid(NON_EXISTING_NRSLEVELID);
        irsNRSLevlDimData.setName(NON_EXISTING_NAME);
    }
	
	public void testInsertNonExistingRecord()throws SQLException{
		assertNull(irsNRSLevlDimMapper.findByNRSLevelId(NON_EXISTING_NRSLEVELID));
		irsNRSLevlDimMapper.insert(irsNRSLevlDimData);
		assertNotNull(irsNRSLevlDimMapper.findByNRSLevelId(NON_EXISTING_NRSLEVELID));
		conn.commit();
	}
	
	public void testUpdate()throws SQLException{
		assertNotNull(irsNRSLevlDimMapper.findByNRSLevelId(NON_EXISTING_NRSLEVELID));
		IrsNRSLevelDimData record = irsNRSLevlDimMapper.findByNRSLevelId(NON_EXISTING_NRSLEVELID);
		assertNotNull(record);
		final String oldName = record.getName();
		record.setName("T2");
		try{
			irsNRSLevlDimMapper.update(record);
			conn.commit();
			IrsNRSLevelDimData updatedRecord = irsNRSLevlDimMapper.findByNRSLevelId(NON_EXISTING_NRSLEVELID);
			assertEquals("T2",updatedRecord.getName());
		 	}finally{
		 		record.setName(oldName);
		 		irsNRSLevlDimMapper.update(record);
		 		conn.commit();
		 	}
	}
	
	public void testDelete()throws SQLException{
		assertNotNull(irsNRSLevlDimMapper.findByNRSLevelId(NON_EXISTING_NRSLEVELID));
		irsNRSLevlDimMapper.delete(NON_EXISTING_NRSLEVELID);
		conn.commit();
		assertNull(irsNRSLevlDimMapper.findByNRSLevelId(NON_EXISTING_NRSLEVELID));
	}
	
	protected void tearDown()throws SQLException{
		conn.close();
	}
}