package test.ctb.lexington.db.mapper;

import java.sql.SQLException;
import com.ctb.lexington.db.irsdata.IrsSubjectDimData;
import com.ctb.lexington.db.mapper.IrsSubjectDimMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsSubjectDimMapperTest extends AbstractMapperTest{

	private IrsSubjectDimMapper irsSubjectDimMapper;
	private IrsSubjectDimData irsSubjectDimData;
	
	private static final Long NON_EXISTING_SUBJECTID = new Long(10);
	private static final String NON_EXISTING_SUBJECTNAME = "testsubject";
	
	public IrsSubjectDimMapperTest(String name){
		super(name);	 
	}
	
	protected void setUp()throws Exception{
        super.setUp();
        conn = getIRSConnection();
        irsSubjectDimMapper = new IrsSubjectDimMapper(conn);
        irsSubjectDimData = new IrsSubjectDimData();
        irsSubjectDimData.setSubjectid(NON_EXISTING_SUBJECTID);
        irsSubjectDimData.setSubjectName(NON_EXISTING_SUBJECTNAME);
    }
	
	public void testInsertNonExistingRecord()throws SQLException{
     	assertNull(irsSubjectDimMapper.findBySubjectId(NON_EXISTING_SUBJECTID));
     	irsSubjectDimMapper.insert(irsSubjectDimData);
     	assertNotNull(irsSubjectDimMapper.findBySubjectId(NON_EXISTING_SUBJECTID));
     	conn.commit();
	}
	
	public void testUpdate()throws SQLException{
		assertNotNull(irsSubjectDimMapper.findBySubjectId(NON_EXISTING_SUBJECTID));
		IrsSubjectDimData record = irsSubjectDimMapper.findBySubjectId(NON_EXISTING_SUBJECTID);
		assertNotNull(record);
		final String oldSubjectName = record.getSubjectName();
		record.setSubjectName("TABETEST2");
		try{
			irsSubjectDimMapper.update(record);
			conn.commit();
			IrsSubjectDimData updatedRecord = irsSubjectDimMapper.findBySubjectId(NON_EXISTING_SUBJECTID);
			assertEquals("TABETEST2",updatedRecord.getSubjectName());
		 	}finally{
		 		record.setSubjectName(oldSubjectName);
		 		irsSubjectDimMapper.update(record);
		 		conn.commit();
		 	}
	}
	
	public void testDelete()throws SQLException{
		assertNotNull(irsSubjectDimMapper.findBySubjectId(NON_EXISTING_SUBJECTID));
		irsSubjectDimMapper.delete(NON_EXISTING_SUBJECTID);
		conn.commit();
		assertNull(irsSubjectDimMapper.findBySubjectId(NON_EXISTING_SUBJECTID));
	}
	
	protected void tearDown()throws SQLException{
		conn.close();
	}
}