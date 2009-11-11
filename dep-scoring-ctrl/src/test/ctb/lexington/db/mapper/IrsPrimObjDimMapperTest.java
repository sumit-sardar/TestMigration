package test.ctb.lexington.db.mapper;

import java.sql.SQLException;
import com.ctb.lexington.db.irsdata.IrsPrimObjDimData;
import com.ctb.lexington.db.mapper.IrsPrimObjDimMapper;
import com.ctb.lexington.db.irsdata.IrsContentAreaDimData;
import com.ctb.lexington.db.mapper.IrsContentAreaDimMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsPrimObjDimMapperTest extends AbstractMapperTest{

	private IrsPrimObjDimMapper irsPrimObjDimMapper;
	private IrsPrimObjDimData irsPrimObjDimData;
	private IrsContentAreaDimData irsContentAreaDimData;
	private IrsContentAreaDimMapper irsContentAreaMapper;
	
	private static final Long NON_EXISTING_PRIMOBJID = new Long(999999);
	private static final String NON_EXISTING_NAME = "TABETEST";
	private static final Long NON_EXISTING_PRIMOBJINDEX = new Long(12);
	private static final String NON_EXISTING_PRIMOBJTYPE = "TABEPrimObj";
	private static final Long NON_EXISTING_NUM_ITEMS = new Long(15);
	private static final Long NON_EXISTING_NATIONALAVERAGE = new Long(11);
	private static final Long NON_EXISTING_POINTSPOSSIBLE = new Long(99);
	private static final Long NON_EXISTING_CONTENTAREAID = new Long(10);
    
	public IrsPrimObjDimMapperTest(String name){
		 super(name);	 
	}
	
	protected void setUp()throws Exception{
        super.setUp();
        conn = getIRSConnection();
        irsPrimObjDimMapper = new IrsPrimObjDimMapper(conn);
        irsContentAreaMapper = new IrsContentAreaDimMapper(conn);
        irsPrimObjDimData = new IrsPrimObjDimData();
        irsContentAreaDimData = new IrsContentAreaDimData();
        irsContentAreaDimData.setContentAreaid(NON_EXISTING_CONTENTAREAID);
        irsPrimObjDimData.setPrimObjid(NON_EXISTING_PRIMOBJID);
        irsPrimObjDimData.setName(NON_EXISTING_NAME);
        irsPrimObjDimData.setPrimObjIndex(NON_EXISTING_PRIMOBJINDEX);
        irsPrimObjDimData.setPrimObjType(NON_EXISTING_PRIMOBJTYPE);
        irsPrimObjDimData.setNumItems(NON_EXISTING_NUM_ITEMS);
        irsPrimObjDimData.setPointsPossible(NON_EXISTING_POINTSPOSSIBLE);
        irsPrimObjDimData.setContentAreaid(NON_EXISTING_CONTENTAREAID);
    }
	
	public void test1InsertNonExistingContentAreaRecord()throws SQLException{
     	assertNull(irsContentAreaMapper.findByContentAreaId(NON_EXISTING_CONTENTAREAID));
     	irsContentAreaMapper.insert(irsContentAreaDimData);
     	assertNotNull(irsContentAreaMapper.findByContentAreaId(NON_EXISTING_CONTENTAREAID));
     	conn.commit();
	}
	
	public void test4InsertNonExistingRecord()throws Exception{
		assertNull(irsPrimObjDimMapper.findByPrimObjId(NON_EXISTING_PRIMOBJID));
		irsPrimObjDimMapper.insert(irsPrimObjDimData);
		assertNotNull(irsPrimObjDimMapper.findByPrimObjId(NON_EXISTING_PRIMOBJID));
		conn.commit();
	}
	
	public void test5Update()throws SQLException{
		assertNotNull(irsPrimObjDimMapper.findByPrimObjId(NON_EXISTING_PRIMOBJID));
		IrsPrimObjDimData record = irsPrimObjDimMapper.findByPrimObjId(NON_EXISTING_PRIMOBJID);
		assertNotNull(record);
		final String oldName = record.getName();
		final Long oldPrimObjIndex = record.getPrimObjIndex();
		final String oldPrimObjType = record.getPrimObjType();
		final Long oldNumItems = record.getNumItems();
		final Long oldPointsPossible = record.getPointsPossible();
		final Long oldContentAreaId = record.getContentAreaid();
		record.setName("TESTING");
		record.setPrimObjIndex(new Long(1111));
		record.setPrimObjType("TestType");
		record.setNumItems(new Long(11));
		record.setPointsPossible(new Long(11));
		record.setContentAreaid(NON_EXISTING_CONTENTAREAID);
		try{
			irsPrimObjDimMapper.update(record);
			conn.commit();
			IrsPrimObjDimData updatedRecord = irsPrimObjDimMapper.findByPrimObjId(NON_EXISTING_PRIMOBJID);
			assertEquals("TESTING",updatedRecord.getName());
		 	}finally{
		 		record.setName(oldName);
		 		record.setPrimObjIndex(oldPrimObjIndex);
		 		record.setPrimObjType(oldPrimObjType);
		 		record.setNumItems(oldNumItems);
		 		record.setPointsPossible(oldPointsPossible);
		 		record.setContentAreaid(oldContentAreaId);
		 		irsPrimObjDimMapper.update(record);
		 		conn.commit();
		 	}
	}
	
	public void test6Delete()throws SQLException{
		assertNotNull(irsPrimObjDimMapper.findByPrimObjId(NON_EXISTING_PRIMOBJID));
		irsPrimObjDimMapper.delete(NON_EXISTING_PRIMOBJID);
		conn.commit();
		assertNull(irsPrimObjDimMapper.findByPrimObjId(NON_EXISTING_PRIMOBJID));
	}
	
	public void test7ContentAreaDimDelete()throws SQLException{
		assertNotNull(irsContentAreaMapper.findByContentAreaId(NON_EXISTING_CONTENTAREAID));
		irsContentAreaMapper.delete(NON_EXISTING_CONTENTAREAID);
		conn.commit();
		assertNull(irsContentAreaMapper.findByContentAreaId(NON_EXISTING_CONTENTAREAID));
	}	
	
	protected void tearDown()throws SQLException{
		conn.close();
	}
}