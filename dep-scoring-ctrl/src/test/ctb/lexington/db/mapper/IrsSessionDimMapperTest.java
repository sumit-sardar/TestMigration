package test.ctb.lexington.db.mapper;

import java.sql.SQLException;
import java.util.Date;
import com.ctb.lexington.db.irsdata.IrsSessionDimData;
import com.ctb.lexington.db.mapper.IrsSessionDimMapper;
import com.ctb.lexington.db.irsdata.IrsProgramDimData;
import com.ctb.lexington.db.irsdata.IrsSchedulerDimData;
import com.ctb.lexington.db.mapper.IrsProgramDimMapper;
import com.ctb.lexington.db.mapper.IrsSchedulerDimMapper;


/**
 * @author Rama_Rao
 *
 */

public class IrsSessionDimMapperTest extends AbstractMapperTest{

	private IrsSessionDimMapper irsSessionDimMapper;
	private IrsSessionDimData irsSessionDimData;
	private IrsSchedulerDimData irsSchedulerDimData;
	private IrsSchedulerDimMapper irsSchedulerDimMapper;
	private IrsProgramDimData irsProgramDimData;
	private IrsProgramDimMapper irsProgramDimMapper;
	
	private static final Long NON_EXISTING_SESSIONID = new Long(9999);
	private static final String NON_EXISTING_SCHDULARNAME = "TestRama";
	private static final Long NON_EXISTING_NUMBEROFSTUDENTS = new Long(999);
	private static final Long NON_EXISTING_SCHEDULARID = new Long(999);
	private static final Long NON_EXISTING_PROGRAMID = new Long(999);
	private static final Long NON_EXISTING_CUSTOMERID = new Long(5);
	private static final Date NON_EXISTING_WINDOWSTARTDATE = createDate(2006,3,1);
	private static final Date NON_EXISTING_WINDOWENDDATE = createDate(2007,2,1);
	
	public IrsSessionDimMapperTest(String name){
		super(name);	 
	}
	
	protected void setUp()throws Exception{
		super.setUp();
        conn = getIRSConnection();
        irsSessionDimMapper = new IrsSessionDimMapper(conn);
        irsSchedulerDimMapper = new IrsSchedulerDimMapper(conn);
        irsProgramDimMapper = new IrsProgramDimMapper(conn);
        irsSessionDimData = new IrsSessionDimData();
        irsProgramDimData = new IrsProgramDimData();
        irsSchedulerDimData = new IrsSchedulerDimData();
        irsProgramDimData.setProgramid(NON_EXISTING_PROGRAMID);
        irsProgramDimData.setCustomerid(NON_EXISTING_CUSTOMERID);
        irsSchedulerDimData.setSchedulerid(NON_EXISTING_SCHEDULARID);
        irsSchedulerDimData.setName(NON_EXISTING_SCHDULARNAME);
        irsSessionDimData.setSessionid(NON_EXISTING_SESSIONID);
        irsSessionDimData.setNumberOfStudents(NON_EXISTING_NUMBEROFSTUDENTS);
        irsSessionDimData.setProgramid(NON_EXISTING_PROGRAMID);
        irsSessionDimData.setWindowStartDate(NON_EXISTING_WINDOWSTARTDATE);
        irsSessionDimData.setWindowEndDate(NON_EXISTING_WINDOWENDDATE);
        irsSessionDimData.setSchedulerid(NON_EXISTING_SCHEDULARID);
    }
	
	public void test1InsertNonExistingProgramDimRecord()throws SQLException{
		assertNull(irsProgramDimMapper.findByProgramId(NON_EXISTING_PROGRAMID));
     	irsProgramDimMapper.insert(irsProgramDimData);
     	assertNotNull(irsProgramDimMapper.findByProgramId(NON_EXISTING_PROGRAMID));
     	conn.commit();
	}
	
	public void test2InsertNonExistingSchedularDimRecord()throws SQLException{
		assertNull(irsSchedulerDimMapper.findBySchedulerId(NON_EXISTING_SCHEDULARID));
		irsSchedulerDimMapper.insert(irsSchedulerDimData);
		assertNotNull(irsSchedulerDimMapper.findBySchedulerId(NON_EXISTING_SCHEDULARID));
		conn.commit();
	}
	
	public void test3InsertNonExistingRecord()throws SQLException{
		assertNull(irsSessionDimMapper.findBySessionId(NON_EXISTING_SESSIONID));
		irsSessionDimMapper.insert(irsSessionDimData);
		assertNotNull(irsSessionDimMapper.findBySessionId(NON_EXISTING_SESSIONID));
		conn.commit();
	}
	
	public void test4Update()throws SQLException{
		assertNotNull(irsSessionDimMapper.findBySessionId(NON_EXISTING_SESSIONID));
		IrsSessionDimData record = irsSessionDimMapper.findBySessionId(NON_EXISTING_SESSIONID);
		assertNotNull(record);
		final Long oldNumberOfStudents = record.getNumberOfStudents();
		final Long oldProgramId = record.getProgramid();
		final Long oldSchedularId = record.getSchedulerid();
		final Date oldWindowStartDate = record.getWindowStartDate();
		final Date oldWindowEndDate = record.getWindowEndDate();
		record.setNumberOfStudents(new Long(999));
		record.setProgramid(oldProgramId);
		record.setSchedulerid(oldSchedularId);
		record.setWindowStartDate(createDate(2005,2,1));
		record.setWindowEndDate(createDate(2008,2,1));
		try{
			irsSessionDimMapper.update(record);
			conn.commit();
			IrsSessionDimData updatedRecord = irsSessionDimMapper.findBySessionId(NON_EXISTING_SESSIONID);
			assertEquals(new Long(999),updatedRecord.getNumberOfStudents());
		 	}finally{
		 		record.setNumberOfStudents(oldNumberOfStudents);
		 		record.setProgramid(oldProgramId);
		 		record.setSchedulerid(oldSchedularId);
		 		record.setWindowStartDate(oldWindowStartDate);
		 		record.setWindowEndDate(oldWindowEndDate);
		 		irsSessionDimMapper.update(record);
		 		conn.commit();
		 	}
	}
	
	public void test5ProgramDimDelete()throws SQLException{
		assertNotNull(irsProgramDimMapper.findByProgramId(NON_EXISTING_PROGRAMID));
		irsProgramDimMapper.delete(NON_EXISTING_PROGRAMID);
		conn.commit();
		assertNull(irsProgramDimMapper.findByProgramId(NON_EXISTING_PROGRAMID));
	}
	
	public void test9Delete()throws SQLException{
		assertNotNull(irsSessionDimMapper.findBySessionId(NON_EXISTING_SESSIONID));
		irsSessionDimMapper.delete(NON_EXISTING_SESSIONID);
		conn.commit();
		assertNull(irsSessionDimMapper.findBySessionId(NON_EXISTING_SESSIONID));
	}
	
	public void test7SchedularDimDelete()throws SQLException{
		assertNotNull(irsSchedulerDimMapper.findBySchedulerId(NON_EXISTING_SCHEDULARID));
		irsSchedulerDimMapper.delete(NON_EXISTING_SCHEDULARID);
		conn.commit();
		assertNull(irsSchedulerDimMapper.findBySchedulerId(NON_EXISTING_SCHEDULARID));
	}
	
	protected void tearDown()throws SQLException{
		conn.close();
	}
}