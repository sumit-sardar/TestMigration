package test.ctb.lexington.db.mapper;

import java.sql.SQLException;
import com.ctb.lexington.db.irsdata.IrsResponseDimData;
import com.ctb.lexington.db.mapper.IrsResponseDimMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsResponseDimMapperTest extends AbstractMapperTest{

	private IrsResponseDimMapper irsResponseDimMapper;
	private IrsResponseDimData irsResponseDimData;
	private static final Long NON_EXISTING_RESPONSEID = new Long(7);
	private static final String NON_EXISTING_RESPONSE = "G";
	
	public IrsResponseDimMapperTest(String name){
		super(name);	 
	}
	
	protected void setUp()throws Exception{
        super.setUp();
        conn = getIRSConnection();
        irsResponseDimMapper = new IrsResponseDimMapper(conn);
        irsResponseDimData = new IrsResponseDimData();
        irsResponseDimData.setResponseid(NON_EXISTING_RESPONSEID);
        irsResponseDimData.setResponse(NON_EXISTING_RESPONSE);
    }
	
	public void testInsertNonExistingRecord()throws SQLException{
     	assertNull(irsResponseDimMapper.findByResponseId(NON_EXISTING_RESPONSEID));
     	irsResponseDimMapper.insert(irsResponseDimData);
     	assertNotNull(irsResponseDimMapper.findByResponseId(NON_EXISTING_RESPONSEID));
     	conn.commit();
	}
	
	public void testUpdate()throws SQLException{
		assertNotNull(irsResponseDimMapper.findByResponseId(NON_EXISTING_RESPONSEID));
		IrsResponseDimData record = irsResponseDimMapper.findByResponseId(NON_EXISTING_RESPONSEID);
		assertNotNull(record);
		final String oldResponse = record.getResponse();
		record.setResponse("H");
		try{
			irsResponseDimMapper.update(record);
			conn.commit();
			IrsResponseDimData updatedRecord = irsResponseDimMapper.findByResponseId(NON_EXISTING_RESPONSEID);
			assertEquals("H",updatedRecord.getResponse());
		 }finally{
			 record.setResponse(oldResponse);
			 irsResponseDimMapper.update(record);
			 conn.commit();
		 }
	}
	
	public void testDelete()throws SQLException{
		assertNotNull(irsResponseDimMapper.findByResponseId(NON_EXISTING_RESPONSEID));
		irsResponseDimMapper.delete(NON_EXISTING_RESPONSEID);
		conn.commit();
		assertNull(irsResponseDimMapper.findByResponseId(NON_EXISTING_RESPONSEID));
	}	
	
	protected void tearDown()throws SQLException{
		conn.close();
	}
}