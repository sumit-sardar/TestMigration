package test.ctb.lexington.db.mapper;

import java.sql.SQLException;
import com.ctb.lexington.db.irsdata.IrsMasteryLevelDimData;
import com.ctb.lexington.db.mapper.IrsMasteryLevelDimMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsMasteryLevelDimMapperTest extends AbstractMapperTest{

	private IrsMasteryLevelDimMapper irsMasteryLevelDimMapper;
	private IrsMasteryLevelDimData irsMasteryLevelDimData;
	private static final Long NON_EXISTING_MASTERYLEVELID = new Long(7);
	private static final String NON_EXISTING_NAME = "T1";
	
	public IrsMasteryLevelDimMapperTest(String name){
		super(name);	 
	}
	
	protected void setUp()throws Exception{
        super.setUp();
        conn = getIRSConnection();
        irsMasteryLevelDimMapper = new IrsMasteryLevelDimMapper(conn);
        irsMasteryLevelDimData = new IrsMasteryLevelDimData();
        irsMasteryLevelDimData.setMasteryLevelid(NON_EXISTING_MASTERYLEVELID);
        irsMasteryLevelDimData.setName(NON_EXISTING_NAME);
    }
	
	public void testInsertNonExistingRecord()throws SQLException{
		assertNull(irsMasteryLevelDimMapper.findByMasteryLevelId(NON_EXISTING_MASTERYLEVELID));
		irsMasteryLevelDimMapper.insert(irsMasteryLevelDimData);
		assertNotNull(irsMasteryLevelDimMapper.findByMasteryLevelId(NON_EXISTING_MASTERYLEVELID));
		conn.commit();
	}
	
	public void testUpdate()throws SQLException{
		assertNotNull(irsMasteryLevelDimMapper.findByMasteryLevelId(NON_EXISTING_MASTERYLEVELID));
		IrsMasteryLevelDimData record = irsMasteryLevelDimMapper.findByMasteryLevelId(NON_EXISTING_MASTERYLEVELID);
		assertNotNull(record);
		final String oldName = record.getName();
		record.setName("T2");
		try{
			irsMasteryLevelDimMapper.update(record);
			conn.commit();
			IrsMasteryLevelDimData updatedRecord = irsMasteryLevelDimMapper.findByMasteryLevelId(NON_EXISTING_MASTERYLEVELID);
			assertEquals("T2",updatedRecord.getName());
		 	}finally{
		 		record.setName(oldName);
		 		irsMasteryLevelDimMapper.update(record);
		 	}
	}
	
	public void testDelete()throws SQLException{
		assertNotNull(irsMasteryLevelDimMapper.findByMasteryLevelId(NON_EXISTING_MASTERYLEVELID));
		irsMasteryLevelDimMapper.delete(NON_EXISTING_MASTERYLEVELID);
		conn.commit();
		assertNull(irsMasteryLevelDimMapper.findByMasteryLevelId(NON_EXISTING_MASTERYLEVELID));
	}
	
	protected void tearDown()throws SQLException{
		conn.close();
	}
}