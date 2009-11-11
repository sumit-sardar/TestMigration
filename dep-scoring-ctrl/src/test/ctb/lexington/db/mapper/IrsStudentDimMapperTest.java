package test.ctb.lexington.db.mapper;

import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.IrsStudentDimData;
import com.ctb.lexington.db.mapper.IrsStudentDimMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsStudentDimMapperTest extends AbstractMapperTest{

	private IrsStudentDimMapper irsStudentDimMapper;
	private IrsStudentDimData irsStudentDimData;
	
	private static final Long NON_EXISTING_STUDENTID = new Long(9999999);
	private static final String NON_EXISTING_FIRST_NAME = "9999999first";
	private static final String NON_EXISTING_LAST_NAME = "9999999last";
	private static final String NON_EXISTING_MIDDLE_NAME = "9999999middle";
	private static final String NON_EXISTING_REPORT_STUDENTID = "11111111";

	public IrsStudentDimMapperTest(String name){
		super(name);	 
	}
	
	protected void setUp()throws Exception{
        super.setUp();
        conn = getIRSConnection();
        irsStudentDimMapper = new IrsStudentDimMapper(conn);
        irsStudentDimData = new IrsStudentDimData();
        
        irsStudentDimData.setStudentid(NON_EXISTING_STUDENTID);
        irsStudentDimData.setFirstName(NON_EXISTING_FIRST_NAME);
        irsStudentDimData.setLastName(NON_EXISTING_LAST_NAME);
        irsStudentDimData.setMiddleName(NON_EXISTING_MIDDLE_NAME);
        irsStudentDimData.setReportStudentId(NON_EXISTING_REPORT_STUDENTID);
    }
	
	public void testInsertNonExistingRecord()throws SQLException{
		assertNull(irsStudentDimMapper.findByStudentId(NON_EXISTING_STUDENTID));
		irsStudentDimMapper.insert(irsStudentDimData);
		assertNotNull(irsStudentDimMapper.findByStudentId(NON_EXISTING_STUDENTID));
		conn.commit();
	}
	
	public void testUpdate()throws SQLException{
		assertNotNull(irsStudentDimMapper.findByStudentId(NON_EXISTING_STUDENTID));
		IrsStudentDimData record = irsStudentDimMapper.findByStudentId(NON_EXISTING_STUDENTID);
		assertNotNull(record);
		final String oldFirstName = record.getFirstName();
		final String oldMiddleName = record.getMiddleName();
		final String oldLastName = record.getLastName();
		final String oldReport_StudentId = record.getReportStudentId();
		record.setFirstName("updatefirstrama");
		record.setMiddleName("updatemiddle");
		record.setLastName("updatelast");
		record.setReportStudentId("22222222");
		try{
			irsStudentDimMapper.update(record);
			conn.commit();
			IrsStudentDimData updatedRecord = irsStudentDimMapper.findByStudentId(NON_EXISTING_STUDENTID);
			assertEquals("updatefirstrama",updatedRecord.getFirstName());
		 	}finally{
		 		record.setFirstName(oldFirstName);
		 		record.setMiddleName(oldMiddleName);
		 		record.setLastName(oldLastName);
		 		record.setReportStudentId(oldReport_StudentId);
		 		irsStudentDimMapper.update(record);
		 		conn.commit();
		 	}
	}
	
	public void testDelete()throws SQLException{
		assertNotNull(irsStudentDimMapper.findByStudentId(NON_EXISTING_STUDENTID));
		irsStudentDimMapper.delete(NON_EXISTING_STUDENTID);
		conn.commit();
		assertNull(irsStudentDimMapper.findByStudentId(NON_EXISTING_STUDENTID));
	}
	
	protected void tearDown()throws SQLException{
		conn.close();
	}
}