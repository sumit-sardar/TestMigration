package test.ctb.lexington.db.mapper;

import java.sql.SQLException;
import java.util.Date;
import com.ctb.lexington.db.irsdata.IrsProgramDimData;
import com.ctb.lexington.db.mapper.IrsProgramDimMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsProgramDimMapperTest extends AbstractMapperTest{

	private IrsProgramDimMapper irsProgramDimMapper;
	private IrsProgramDimData irsProgramDimData;
	private static final Long NON_EXISTING_PROGRAMID = new Long(22);
	private static final String NON_EXISTING_NAME = "TABETEST";
	private static final Long NON_EXISTING_CUSTOMERID = new Long(1);
	private static final Date NON_EXISTING_PROG_START_DATE = new Date();
	private static final Date NON_EXISTING_PROG_END_DATE = new Date();
	
	public IrsProgramDimMapperTest(String name){
		super(name);	 
	}
	
	protected void setUp()throws Exception{
        super.setUp();
        conn = getIRSConnection();
        irsProgramDimMapper = new IrsProgramDimMapper(conn);
        irsProgramDimData = new IrsProgramDimData();
        irsProgramDimData.setProgramid(NON_EXISTING_PROGRAMID);
        irsProgramDimData.setName(NON_EXISTING_NAME);
        irsProgramDimData.setCustomerid(NON_EXISTING_CUSTOMERID);
        irsProgramDimData.setProgStartDate(NON_EXISTING_PROG_START_DATE);
        irsProgramDimData.setProgEndDate(NON_EXISTING_PROG_END_DATE);;
    }
	
	public void testInsertNonExistingRecord()throws SQLException{
     	assertNull(irsProgramDimMapper.findByProgramId(NON_EXISTING_PROGRAMID));
     	irsProgramDimMapper.insert(irsProgramDimData);
     	assertNotNull(irsProgramDimMapper.findByProgramId(NON_EXISTING_PROGRAMID));
     	conn.commit();
	}
	
	public void testUpdate()throws SQLException{
		assertNotNull(irsProgramDimMapper.findByProgramId(NON_EXISTING_PROGRAMID));
		IrsProgramDimData record = irsProgramDimMapper.findByProgramId(NON_EXISTING_PROGRAMID);
		final String oldName = record.getName();
		final Long oldCustomerId = record.getCustomerid();
		record.setName("Test2");
		try{
			irsProgramDimMapper.update(record);
			conn.commit();
			IrsProgramDimData updatedRecord = irsProgramDimMapper.findByProgramId(NON_EXISTING_PROGRAMID);
			assertEquals("Test2",updatedRecord.getName());
		 }finally{
			 record.setName(oldName);
			 record.setCustomerid(oldCustomerId);
			 irsProgramDimMapper.update(record);
			 assertEquals("TABETEST",record.getName());
			 conn.commit();
		 }
	}
	
	public void testDelete()throws SQLException{
		assertNotNull(irsProgramDimMapper.findByProgramId(NON_EXISTING_PROGRAMID));
		irsProgramDimMapper.delete(NON_EXISTING_PROGRAMID);
		conn.commit();
		assertNull(irsProgramDimMapper.findByProgramId(NON_EXISTING_PROGRAMID));
	}	
	
	protected void tearDown()throws SQLException{
		conn.close();
	}
}