package test.ctb.lexington.db.mapper;

import java.sql.SQLException;
import com.ctb.lexington.db.irsdata.IrsContentAreaDimData;
import com.ctb.lexington.db.mapper.IrsContentAreaDimMapper;
import com.ctb.lexington.db.irsdata.IrsSubjectDimData;
import com.ctb.lexington.db.mapper.IrsSubjectDimMapper;;

/**
 * @author Rama_Rao
 *
 */

public class IrsContentAreaDimMapperTest extends AbstractMapperTest{

	private IrsContentAreaDimMapper irsContentAreaDimMapper;
	private IrsContentAreaDimData irsContentAreaDimData;
	private IrsSubjectDimData irsSubjectDimData;
	private IrsSubjectDimMapper irsSubjectDimMapper;
	private static final Long NON_EXISTING_CONTENTAREAID = new Long(999999);
	private static final String NON_EXISTING_NAME = "TABETEST";
	private static final String NON_EXISTING_CONTENTAREATYPE = "TESTTABECONTENTAREA";
	private static final Long NON_EXISTING_POINTSPOSSIBLE = new Long(99);
	private static final Long NON_EXISTING_SUBJECTID = new Long(10);
    private static final String NON_EXISTING_SUBJECTNAME = "testsubject";
    
	public IrsContentAreaDimMapperTest(String name){
		super(name);	 
	}
	
	protected void setUp()throws Exception{
        super.setUp();
        conn = getIRSConnection();
        irsContentAreaDimMapper = new IrsContentAreaDimMapper(conn);
        irsSubjectDimMapper = new IrsSubjectDimMapper(conn);
        irsContentAreaDimData = new IrsContentAreaDimData();
        irsSubjectDimData = new IrsSubjectDimData();
        irsSubjectDimData.setSubjectid(NON_EXISTING_SUBJECTID);
        irsSubjectDimData.setSubjectName(NON_EXISTING_SUBJECTNAME);
        irsContentAreaDimData.setContentAreaid(NON_EXISTING_CONTENTAREAID);
        irsContentAreaDimData.setName(NON_EXISTING_NAME);
        irsContentAreaDimData.setContentAreaType(NON_EXISTING_CONTENTAREATYPE);
        irsContentAreaDimData.setPointsPossible(NON_EXISTING_POINTSPOSSIBLE);
        irsContentAreaDimData.setSubjectid(NON_EXISTING_SUBJECTID);
    }
	
	public void test1InsertNonExistingSubjectRecord()throws SQLException{
     	assertNull(irsSubjectDimMapper.findBySubjectId(NON_EXISTING_SUBJECTID));
     	irsSubjectDimMapper.insert(irsSubjectDimData);
     	assertNotNull(irsSubjectDimMapper.findBySubjectId(NON_EXISTING_SUBJECTID));
     	conn.commit();
	}
	
	public void test2InsertNonExistingRecord()throws Exception{
		assertNull(irsContentAreaDimMapper.findByContentAreaId(NON_EXISTING_CONTENTAREAID));
		irsContentAreaDimMapper.insert(irsContentAreaDimData);
		assertNotNull(irsContentAreaDimMapper.findByContentAreaId(NON_EXISTING_CONTENTAREAID));
		conn.commit();
	}
	
	public void test3Update()throws SQLException{
		assertNotNull(irsContentAreaDimMapper.findByContentAreaId(NON_EXISTING_CONTENTAREAID));
		IrsContentAreaDimData record = irsContentAreaDimMapper.findByContentAreaId(NON_EXISTING_CONTENTAREAID);
		assertNotNull(record);
		final String oldName = record.getName();
		final String oldContentAreaType = record.getContentAreaType();
		final Long oldPointsPossible = record.getPointsPossible();
		record.setName("TESTING");
		record.setContentAreaType("TestingCA");
		record.setPointsPossible(new Long(30));
		record.setSubjectid(NON_EXISTING_SUBJECTID);
		try{
			irsContentAreaDimMapper.update(record);
			IrsContentAreaDimData updatedRecord = irsContentAreaDimMapper.findByContentAreaId(NON_EXISTING_CONTENTAREAID);
			assertEquals("TESTING",updatedRecord.getName());
		 	}finally{
		 		record.setName(oldName);
		 		record.setContentAreaType(oldContentAreaType);
		 		record.setPointsPossible(oldPointsPossible);
		 		irsContentAreaDimMapper.update(record);
		 	}
	}
	
	public void test4Delete()throws SQLException{
		assertNotNull(irsContentAreaDimMapper.findByContentAreaId(NON_EXISTING_CONTENTAREAID));
		irsContentAreaDimMapper.delete(NON_EXISTING_CONTENTAREAID);
		conn.commit();
		assertNull(irsContentAreaDimMapper.findByContentAreaId(NON_EXISTING_CONTENTAREAID));
	}
	
	public void test5SubjectDimDelete()throws SQLException{
		assertNotNull(irsSubjectDimMapper.findBySubjectId(NON_EXISTING_SUBJECTID));
		irsSubjectDimMapper.delete(NON_EXISTING_SUBJECTID);
		conn.commit();
		assertNull(irsSubjectDimMapper.findBySubjectId(NON_EXISTING_SUBJECTID));
	}
	
	protected void tearDown()throws SQLException{
		conn.close();
	}
}