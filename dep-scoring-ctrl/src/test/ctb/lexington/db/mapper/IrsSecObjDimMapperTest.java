package test.ctb.lexington.db.mapper;

import java.sql.SQLException;
import com.ctb.lexington.db.irsdata.IrsSecObjDimData;
import com.ctb.lexington.db.mapper.IrsSecObjDimMapper;
import com.ctb.lexington.db.irsdata.IrsPrimObjDimData;
import com.ctb.lexington.db.mapper.IrsPrimObjDimMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsSecObjDimMapperTest extends AbstractMapperTest{

	private IrsSecObjDimMapper irsSecObjDimMapper;
	private IrsSecObjDimData irsSecObjDimData;
	private IrsPrimObjDimData irsPrimObjDimData;
	private IrsPrimObjDimMapper irsPrimObjDimMapper;
	private static final Long NON_EXISTING_SECOBJID = new Long(999999);
	private static final String NON_EXISTING_NAME = "TABETEST";
	private static final Long NON_EXISTING_SECOBJINDEX = new Long(12);
	private static final String NON_EXISTING_SECOBJTYPE = "TESTTABECONTENTAREA";
	private static final Long NON_EXISTING_NUM_ITEMS = new Long(15);
	private static final Long NON_EXISTING_NATIONALAVERAGE = new Long(11);
	private static final Long NON_EXISTING_POINTSPOSSIBLE = new Long(99);
	private static final Long NON_EXISTING_PRIMOBJID = new Long(10);
    
	public IrsSecObjDimMapperTest(String name){
		super(name);	 
	}
	
	protected void setUp()throws Exception{
        super.setUp();
        conn = getIRSConnection();
        irsSecObjDimMapper = new IrsSecObjDimMapper(conn);
        irsPrimObjDimMapper = new IrsPrimObjDimMapper(conn);
        irsSecObjDimData = new IrsSecObjDimData();
        irsPrimObjDimData = new IrsPrimObjDimData();
        irsSecObjDimData.setSecObjid(NON_EXISTING_SECOBJID);
        irsSecObjDimData.setName(NON_EXISTING_NAME);
        irsSecObjDimData.setSecObjIndex(NON_EXISTING_SECOBJINDEX);
        irsSecObjDimData.setSecObjType(NON_EXISTING_SECOBJTYPE);
        irsSecObjDimData.setNumItems(NON_EXISTING_NUM_ITEMS);
        irsSecObjDimData.setNationalAverage(NON_EXISTING_NATIONALAVERAGE);
        irsSecObjDimData.setPointsPossible(NON_EXISTING_POINTSPOSSIBLE);
        irsPrimObjDimData.setPrimObjid(NON_EXISTING_PRIMOBJID);
        irsSecObjDimData.setPrimObjid(NON_EXISTING_PRIMOBJID);
    }
	
	public void test1InsertNonExistingPrimObjRecord()throws SQLException{
     	assertNull(irsPrimObjDimMapper.findByPrimObjId(NON_EXISTING_PRIMOBJID));
     	irsPrimObjDimMapper.insert(irsPrimObjDimData);
     	assertNotNull(irsPrimObjDimMapper.findByPrimObjId(NON_EXISTING_PRIMOBJID));
     	conn.commit();
	}
	
	public void test6InsertNonExistingRecord()throws Exception{
		assertNull(irsSecObjDimMapper.findBySecObjId(NON_EXISTING_SECOBJID));
		irsSecObjDimMapper.insert(irsSecObjDimData);
		assertNotNull(irsSecObjDimMapper.findBySecObjId(NON_EXISTING_SECOBJID));
		conn.commit();
	}
	
	public void test7Update()throws SQLException{
		assertNotNull(irsSecObjDimMapper.findBySecObjId(NON_EXISTING_SECOBJID));
		IrsSecObjDimData record = irsSecObjDimMapper.findBySecObjId(NON_EXISTING_SECOBJID);
		assertNotNull(record);
		final String oldName = record.getName();
		final Long oldSecObjIndex = record.getSecObjIndex();
		final String oldSecObjType = record.getSecObjType();
		final Long oldPointsPossible = record.getPointsPossible();
		final Long oldNumItems = record.getNumItems();
		final Long oldNationalAverage = record.getNationalAverage();
		final Long oldPrimObjId = record.getPrimObjid();
		record.setName("TESTING");
		record.setSecObjType("TestingCA");
		record.setSecObjIndex(new Long(123));
		record.setPointsPossible(new Long(30));
		record.setNationalAverage(new Long(111));
		record.setNumItems(new Long(99));
		record.setPrimObjid(NON_EXISTING_PRIMOBJID);
		try{
			irsSecObjDimMapper.update(record);
			conn.commit();
			IrsSecObjDimData updatedRecord = irsSecObjDimMapper.findBySecObjId(NON_EXISTING_SECOBJID);
			assertEquals("TESTING",updatedRecord.getName());
		 	}finally{
		 		record.setName(oldName);
		 		record.setSecObjType(oldSecObjType);
		 		record.setSecObjIndex(oldSecObjIndex);
		 		record.setPointsPossible(oldPointsPossible);
		 		record.setNationalAverage(oldNationalAverage);
		 		record.setNumItems(oldNumItems);
		 		record.setPrimObjid(oldPrimObjId);
		 		irsSecObjDimMapper.update(record);
		 		conn.commit();
		 	}
	}
	
	public void test8Delete()throws SQLException{
		assertNotNull(irsSecObjDimMapper.findBySecObjId(NON_EXISTING_SECOBJID));
		irsSecObjDimMapper.delete(NON_EXISTING_SECOBJID);
		conn.commit();
		assertNull(irsSecObjDimMapper.findBySecObjId(NON_EXISTING_SECOBJID));
	}
	
	public void test9PrimObjDimDelete()throws SQLException{
		assertNotNull(irsPrimObjDimMapper.findByPrimObjId(NON_EXISTING_PRIMOBJID));
		irsPrimObjDimMapper.delete(NON_EXISTING_PRIMOBJID);
		conn.commit();
		assertNull(irsPrimObjDimMapper.findByPrimObjId(NON_EXISTING_PRIMOBJID));
	}	
	
	protected void tearDown()throws SQLException{
		conn.close();
	}
}