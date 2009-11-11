package test.ctb.lexington.db.mapper;

import java.sql.SQLException;
import com.ctb.lexington.db.irsdata.IrsCompositeDimData;
import com.ctb.lexington.db.mapper.IrsCompositeDimMapper;
import com.ctb.lexington.db.irsdata.IrsSubjectDimData;
import com.ctb.lexington.db.mapper.IrsSubjectDimMapper;;

/**
 * @author Rama_Rao
 *
 */

public class IrsCompositeDimMapperTest extends AbstractMapperTest{

	private IrsCompositeDimMapper irsCompositeDimMapper;
	private IrsCompositeDimData irsCompositeDimData;
	private IrsSubjectDimData irsSubjectDimData;
	private IrsSubjectDimMapper irsSubjectDimMapper;
	private static final Long NON_EXISTING_COMPOSITEID = new Long(9);
	private static final String NON_EXISTING_NAME = "TABETEST";
	private static final String NON_EXISTING_COMPOSITETYPE = "TESTTABE";
	private static final Long NON_EXISTING_POINTSPOSSIBLE = new Long(9);
	private static final Long NON_EXISTING_SUBJECTID = new Long(10);
    private static final String NON_EXISTING_SUBJECTNAME = "testsubject";
    
	public IrsCompositeDimMapperTest(String name){
		super(name);	 
	}
	
	protected void setUp()throws Exception{
        super.setUp();
        conn = getIRSConnection();
        irsCompositeDimMapper = new IrsCompositeDimMapper(conn);
        irsSubjectDimMapper = new IrsSubjectDimMapper(conn);
        irsCompositeDimData = new IrsCompositeDimData();
        irsSubjectDimData = new IrsSubjectDimData();
        irsSubjectDimData.setSubjectid(NON_EXISTING_SUBJECTID);
        irsSubjectDimData.setSubjectName(NON_EXISTING_SUBJECTNAME);
        irsCompositeDimData.setCompositeid(NON_EXISTING_COMPOSITEID);
        irsCompositeDimData.setName(NON_EXISTING_NAME);
        irsCompositeDimData.setCompositeType(NON_EXISTING_COMPOSITETYPE);
        irsCompositeDimData.setPointsPossible(NON_EXISTING_POINTSPOSSIBLE);
        irsCompositeDimData.setSubjectid(NON_EXISTING_SUBJECTID);
    }
	
	public void test1InsertNonExistingSubjectRecord()throws SQLException{
     	assertNull(irsSubjectDimMapper.findBySubjectId(NON_EXISTING_SUBJECTID));
     	irsSubjectDimMapper.insert(irsSubjectDimData);
     	assertNotNull(irsSubjectDimMapper.findBySubjectId(NON_EXISTING_SUBJECTID));
     	conn.commit();
	}
	
	public void test2InsertNonExistingRecord()throws Exception{
		assertNull(irsCompositeDimMapper.findByCompositeDimId(NON_EXISTING_COMPOSITEID));
		irsCompositeDimMapper.insert(irsCompositeDimData);
		assertNotNull(irsCompositeDimMapper.findByCompositeDimId(NON_EXISTING_COMPOSITEID));
		conn.commit();
	}
	
	public void test3Update()throws SQLException{
		assertNotNull(irsCompositeDimMapper.findByCompositeDimId(NON_EXISTING_COMPOSITEID));
		IrsCompositeDimData record = irsCompositeDimMapper.findByCompositeDimId(NON_EXISTING_COMPOSITEID);
		assertNotNull(record);
		final String oldName = record.getName();
		final String oldCompositeType = record.getCompositeType();
		final Long oldPointsPossible = record.getPointsPossible();
		final Long oldSubjectId = record.getSubjectid();
		record.setName("TESTING2");
		record.setCompositeType("TestingCA");
		record.setPointsPossible(new Long(30));
		record.setSubjectid(oldSubjectId);
		try{
			irsCompositeDimMapper.update(record);
			IrsCompositeDimData updatedRecord = irsCompositeDimMapper.findByCompositeDimId(NON_EXISTING_COMPOSITEID);
			assertEquals("TESTING2",updatedRecord.getName());
		 	}finally{
		 		record.setName(oldName);
		 		record.setCompositeType(oldCompositeType);
		 		record.setPointsPossible(oldPointsPossible);
		 		record.setSubjectid(oldSubjectId);
		 		irsCompositeDimMapper.update(record);
		 	}
	}
	
	public void test4Delete()throws SQLException{
		assertNotNull(irsCompositeDimMapper.findByCompositeDimId(NON_EXISTING_COMPOSITEID));
		irsCompositeDimMapper.delete(NON_EXISTING_COMPOSITEID);
		conn.commit();
		assertNull(irsCompositeDimMapper.findByCompositeDimId(NON_EXISTING_COMPOSITEID));
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