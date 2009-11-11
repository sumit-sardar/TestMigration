package test.ctb.lexington.db.mapper;

import java.sql.SQLException;
import com.ctb.lexington.db.irsdata.IrsOrgNodeDimData;
import com.ctb.lexington.db.mapper.IrsOrgNodeDimMapper;
import com.ctb.lexington.db.irsdata.IrsCustomerDimData;
import com.ctb.lexington.db.mapper.IrsCustomerDimMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsOrgNodeDimMapperTest extends AbstractMapperTest{

	private IrsOrgNodeDimMapper irsOrgNodeDimMapper;
	private IrsOrgNodeDimData irsOrgNodeDimData;
	private IrsCustomerDimMapper irsCustomerDimMapper;
	private IrsCustomerDimData irsCustomerDimData;
	private static final Long NON_EXISTING_ORGNODEID = new Long(10);
	private static final Long NON_EXISTING_NUMBER_LEVELS = new Long(1);
	private static final Long NON_EXISTING_LEVEL1ID = new Long(10);
	private static final String NON_EXISTING_LEVEL1NAME ="CalifStateTest";
	private static final String NON_EXISTING_LEVEL1TYPE ="StateTest";
	private static final Long NON_EXISTING_CUSTOMERID = new Long(3);
	
	public IrsOrgNodeDimMapperTest(String name){
		super(name);	 
	}
	
	protected void setUp()throws Exception{
        super.setUp();
        conn = getIRSConnection();
        irsOrgNodeDimMapper = new IrsOrgNodeDimMapper(conn);
        irsOrgNodeDimData = new IrsOrgNodeDimData();
        irsCustomerDimMapper = new IrsCustomerDimMapper(conn);
        irsCustomerDimData = new IrsCustomerDimData();
        
        irsOrgNodeDimData.setOrgNodeid(NON_EXISTING_ORGNODEID);
        irsOrgNodeDimData.setNumLevels(NON_EXISTING_NUMBER_LEVELS);
        irsOrgNodeDimData.setLevel1Id(NON_EXISTING_LEVEL1ID);
        irsOrgNodeDimData.setLevel1Name(NON_EXISTING_LEVEL1NAME);
        irsOrgNodeDimData.setLevel1Type(NON_EXISTING_LEVEL1TYPE);
        irsOrgNodeDimData.setCustomerid(NON_EXISTING_CUSTOMERID);
        irsCustomerDimData.setCustomerid(NON_EXISTING_CUSTOMERID);
    }
	
	public void test1InsertNonExistingCustomerDimRecord()throws SQLException{
     	assertNull(irsCustomerDimMapper.findByCustomerId(NON_EXISTING_CUSTOMERID));
     	irsCustomerDimMapper.insert(irsCustomerDimData);
     	assertNotNull(irsCustomerDimMapper.findByCustomerId(NON_EXISTING_CUSTOMERID));
     	conn.commit();
	}
	
	public void test8InsertNonExistingRecord()throws SQLException{
		assertNull(irsOrgNodeDimMapper.findByOrgNodeIds(irsOrgNodeDimData));
		irsOrgNodeDimMapper.insert(irsOrgNodeDimData);
		assertNotNull(irsOrgNodeDimMapper.findByOrgNodeIds(irsOrgNodeDimData));
		conn.commit();
	}
	
	public void test9Update()throws SQLException{
		assertNotNull(irsOrgNodeDimMapper.findByOrgNodeIds(irsOrgNodeDimData));
		IrsOrgNodeDimData record = (IrsOrgNodeDimData) irsOrgNodeDimMapper.findByOrgNodeIds(irsOrgNodeDimData).get(0);
		assertNotNull(record);
		final Long oldNumberofLevels = record.getNumLevels();
		final Long oldLevel1Id = record.getLevel1Id();
		final String oldLevel1Name = record.getLevel1Name();
		final String oldLevel1Type = record.getLevel1Type();
		final Long oldCustomerId = record.getCustomerid();
		record.setNumLevels(new Long(10));
		record.setLevel1Id(new Long(10));
		record.setLevel1Name("SynthaticStateTest2");
		record.setLevel1Type("StateTest2");
		record.setCustomerid(new Long(5));
		try{
			irsOrgNodeDimMapper.update(record);
			IrsOrgNodeDimData updatedRecord = (IrsOrgNodeDimData) irsOrgNodeDimMapper.findByOrgNodeIds(irsOrgNodeDimData).get(0);
			assertEquals("SynthaticStateTest2",updatedRecord.getLevel1Name());
			conn.commit();
		 	}finally{
		 		record.setNumLevels(oldNumberofLevels);
		 		record.setLevel1Id(oldLevel1Id);
		 		record.setLevel1Name(oldLevel1Name);
		 		record.setLevel1Type(oldLevel1Type);
		 		record.setCustomerid(oldCustomerId);
		 		irsOrgNodeDimMapper.update(record);
		 		conn.commit();
		 	}
	}
	
	public void test10Delete()throws SQLException{
		assertNotNull(irsOrgNodeDimMapper.findByOrgNodeIds(irsOrgNodeDimData));
		irsOrgNodeDimMapper.delete(NON_EXISTING_ORGNODEID);
		conn.commit();
		assertNull(irsOrgNodeDimMapper.findByOrgNodeIds(irsOrgNodeDimData));
	}
	
	public void test11DeleteCustomerDim()throws SQLException{
		assertNotNull(irsCustomerDimMapper.findByCustomerId(NON_EXISTING_CUSTOMERID));
		irsCustomerDimMapper.delete(NON_EXISTING_CUSTOMERID);
		conn.commit();
		assertNull(irsCustomerDimMapper.findByCustomerId(NON_EXISTING_CUSTOMERID));
	}
	
	protected void tearDown()throws SQLException{
		conn.close();
	}
}