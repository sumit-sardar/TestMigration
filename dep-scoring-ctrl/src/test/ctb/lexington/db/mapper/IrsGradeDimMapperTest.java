package test.ctb.lexington.db.mapper;

import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.IrsGradeDimData;
import com.ctb.lexington.db.mapper.IrsGradeDimMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsGradeDimMapperTest extends AbstractMapperTest{

	private IrsGradeDimMapper irsGradeDimMapper;
	private IrsGradeDimData irsGradeDimData;
	private static final Long NON_EXISTING_GRADEID = new Long(5);
	private static final String NON_EXISTING_GRADE = "T1";
	
	public IrsGradeDimMapperTest(String name){
		super(name);	 
	}
	
	protected void setUp()throws Exception{
        super.setUp();
        conn = getIRSConnection();
        irsGradeDimMapper = new IrsGradeDimMapper(conn);
        irsGradeDimData = new IrsGradeDimData();
        irsGradeDimData.setGradeid(NON_EXISTING_GRADEID);
        irsGradeDimData.setGrade(NON_EXISTING_GRADE);
    }
	
	public void testInsertNonExistingRecord()throws SQLException{
		assertNull(irsGradeDimMapper.findByGradeId(NON_EXISTING_GRADEID));
		irsGradeDimMapper.insert(irsGradeDimData);
		assertNotNull(irsGradeDimMapper.findByGradeId(NON_EXISTING_GRADEID));
		conn.commit();
	}
	
	public void testUpdate()throws SQLException{
		assertNotNull(irsGradeDimMapper.findByGradeId(NON_EXISTING_GRADEID));
		IrsGradeDimData record = irsGradeDimMapper.findByGradeId(NON_EXISTING_GRADEID);
		assertNotNull(record);
		final String oldGrade = record.getGrade();
		record.setGrade("T2");
		try{
			irsGradeDimMapper.update(record);
			conn.commit();
			IrsGradeDimData updatedRecord = irsGradeDimMapper.findByGradeId(NON_EXISTING_GRADEID);
			assertEquals("T2",updatedRecord.getGrade());
		 	}finally{
		 		record.setGrade(oldGrade);
		 		irsGradeDimMapper.update(record);
		 		conn.commit();
		 	}
	}
	
	public void testDelete()throws SQLException{
		assertNotNull(irsGradeDimMapper.findByGradeId(NON_EXISTING_GRADEID));
		irsGradeDimMapper.delete(NON_EXISTING_GRADEID);
		conn.commit();
		assertNull(irsGradeDimMapper.findByGradeId(NON_EXISTING_GRADEID));
	}
	
	protected void tearDown()throws SQLException{
		conn.close();
	}
}