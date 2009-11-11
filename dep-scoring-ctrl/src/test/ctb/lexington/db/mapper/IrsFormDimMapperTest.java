package test.ctb.lexington.db.mapper;

import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.IrsFormDimData;
import com.ctb.lexington.db.mapper.IrsFormDimMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsFormDimMapperTest extends AbstractMapperTest{

	private IrsFormDimMapper irsFormDimMapper;
	private IrsFormDimData irsFormDimData;
	private static final Long NON_EXISTING_FORMID = new Long(5);
	private static final String NON_EXISTING_NAME = "T1";
	
	public IrsFormDimMapperTest(String name){
		super(name);	 
	}
	
	protected void setUp()throws Exception{
        super.setUp();
        conn = getIRSConnection();
        irsFormDimMapper = new IrsFormDimMapper(conn);
        irsFormDimData = new IrsFormDimData();
        irsFormDimData.setFormid(NON_EXISTING_FORMID);
        irsFormDimData.setName(NON_EXISTING_NAME);
    }
	
	public void testInsertNonExistingRecord()throws SQLException{
		assertNull(irsFormDimMapper.findByFormId(NON_EXISTING_FORMID));
		irsFormDimMapper.insert(irsFormDimData);
		assertNotNull(irsFormDimMapper.findByFormId(NON_EXISTING_FORMID));
		conn.commit();
	}
	
	public void testUpdate()throws SQLException{
		assertNotNull(irsFormDimMapper.findByFormId(NON_EXISTING_FORMID));
		IrsFormDimData record = irsFormDimMapper.findByFormId(NON_EXISTING_FORMID);
		assertNotNull(record);
		final String oldName = record.getName();
		record.setName("T2");
		try{
			irsFormDimMapper.update(record);
			conn.commit();
			IrsFormDimData updatedRecord = irsFormDimMapper.findByFormId(NON_EXISTING_FORMID);
			assertEquals("T2",updatedRecord.getName());
		 	}finally{
		 		record.setName(oldName);
		 		irsFormDimMapper.update(record);
		 		conn.commit();
		 	}
	}
	
	public void testDelete()throws SQLException{
		assertNotNull(irsFormDimMapper.findByFormId(NON_EXISTING_FORMID));
		irsFormDimMapper.delete(NON_EXISTING_FORMID);
		conn.commit();
		assertNull(irsFormDimMapper.findByFormId(NON_EXISTING_FORMID));
	}
	
	protected void tearDown()throws SQLException{
		conn.close();
	}
}