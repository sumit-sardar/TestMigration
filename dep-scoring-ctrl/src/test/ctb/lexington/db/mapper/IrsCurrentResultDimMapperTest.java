package test.ctb.lexington.db.mapper;

import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.IrsCurrentResultDimData;
import com.ctb.lexington.db.mapper.IrsCurrentResultDimMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsCurrentResultDimMapperTest extends AbstractMapperTest{

	private IrsCurrentResultDimMapper irsCurrentResultDimMapper;
	private IrsCurrentResultDimData irsCurrentResultDimData;
	private static final Long NON_EXISTING_CURRENTRESULTID = new Long(5);
	private static final String NON_EXISTING_NAME = "T1";

	public IrsCurrentResultDimMapperTest(String name){
		super(name);	 
	}
	
	protected void setUp()throws Exception{
        super.setUp();
        conn = getIRSConnection();
        irsCurrentResultDimMapper = new IrsCurrentResultDimMapper(conn);
        irsCurrentResultDimData = new IrsCurrentResultDimData();
        irsCurrentResultDimData.setCurrentResultid(NON_EXISTING_CURRENTRESULTID);
        irsCurrentResultDimData.setName(NON_EXISTING_NAME);
	}
		
	public void testInsertNonExistingRecord()throws SQLException{
		assertNull(irsCurrentResultDimMapper.findByCurrentResultId(NON_EXISTING_CURRENTRESULTID));
		irsCurrentResultDimMapper.insert(irsCurrentResultDimData);
		assertNotNull(irsCurrentResultDimMapper.findByCurrentResultId(NON_EXISTING_CURRENTRESULTID));
		conn.commit();
	}
	
	public void testUpdate()throws SQLException{
		assertNotNull(irsCurrentResultDimMapper.findByCurrentResultId(NON_EXISTING_CURRENTRESULTID));
		IrsCurrentResultDimData record = irsCurrentResultDimMapper.findByCurrentResultId(NON_EXISTING_CURRENTRESULTID);
		assertNotNull(record);
		final String oldName = record.getName();
		record.setName("TESTCURRENT");
		try{
			irsCurrentResultDimMapper.update(record);
			conn.commit();
			IrsCurrentResultDimData updatedRecord = irsCurrentResultDimMapper.findByCurrentResultId(NON_EXISTING_CURRENTRESULTID);
			assertEquals("TESTCURRENT",updatedRecord.getName());
		 	}finally{
		 		record.setName(oldName);
		 		irsCurrentResultDimMapper.update(record);
		 		conn.commit();
		 	}
	}
	
	public void testDelete()throws SQLException{
		assertNotNull(irsCurrentResultDimMapper.findByCurrentResultId(NON_EXISTING_CURRENTRESULTID));
		irsCurrentResultDimMapper.delete(NON_EXISTING_CURRENTRESULTID);
		conn.commit();
		assertNull(irsCurrentResultDimMapper.findByCurrentResultId(NON_EXISTING_CURRENTRESULTID));
	}
	
	protected void tearDown()throws SQLException{
		conn.close();
	}
}