package test.ctb.lexington.db.mapper;

import java.sql.SQLException;
import com.ctb.lexington.db.irsdata.IrsScoreTypeDimData;
import com.ctb.lexington.db.mapper.IrsScoreTypeDimMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsScoreTypeDimMapperTest extends AbstractMapperTest{

	private IrsScoreTypeDimMapper irsScoreTypeDimMapper;
	private IrsScoreTypeDimData irsScoreTypeDimData;
	private static final Long NON_EXISTING_SCORETYPEID = new Long(22);
	private static final String NON_EXISTING_NAME = "TEST1";
	
	public IrsScoreTypeDimMapperTest(String name){
		super(name);	 
	}
	
	protected void setUp()throws Exception{
        super.setUp();
        conn = getIRSConnection();
        irsScoreTypeDimMapper = new IrsScoreTypeDimMapper(conn);
        irsScoreTypeDimData = new IrsScoreTypeDimData();
        irsScoreTypeDimData.setScoreTypeid(NON_EXISTING_SCORETYPEID);
        irsScoreTypeDimData.setName(NON_EXISTING_NAME);
    }
	
	public void testInsertNonExistingRecord()throws SQLException{
     	assertNull(irsScoreTypeDimMapper.findByScoreTypeId(NON_EXISTING_SCORETYPEID));
     	irsScoreTypeDimMapper.insert(irsScoreTypeDimData);
     	assertNotNull(irsScoreTypeDimMapper.findByScoreTypeId(NON_EXISTING_SCORETYPEID));
     	conn.commit();
	}
	
	public void testUpdate()throws SQLException{
		assertNotNull(irsScoreTypeDimMapper.findByScoreTypeId(NON_EXISTING_SCORETYPEID));
		IrsScoreTypeDimData record = irsScoreTypeDimMapper.findByScoreTypeId(NON_EXISTING_SCORETYPEID);
		assertNotNull(record);
		final String oldName = record.getName();
		record.setName("TEST2");
		try{
			irsScoreTypeDimMapper.update(record);
			conn.commit();
			IrsScoreTypeDimData updatedRecord = irsScoreTypeDimMapper.findByScoreTypeId(NON_EXISTING_SCORETYPEID);
			assertEquals("TEST2",updatedRecord.getName());
		 	}finally{
		 		record.setName(oldName);
		 		irsScoreTypeDimMapper.update(record);
		 		conn.commit();
		 	}
	}
	
	public void testDelete()throws SQLException{
		assertNotNull(irsScoreTypeDimMapper.findByScoreTypeId(NON_EXISTING_SCORETYPEID));
		irsScoreTypeDimMapper.delete(NON_EXISTING_SCORETYPEID);
		conn.commit();
		assertNull(irsScoreTypeDimMapper.findByScoreTypeId(NON_EXISTING_SCORETYPEID));
	}
	
	protected void tearDown()throws SQLException{
		conn.close();
	}
}