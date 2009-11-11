package test.ctb.lexington.db.mapper;

import java.sql.SQLException;
import com.ctb.lexington.db.irsdata.IrsSecObjDimData;
import com.ctb.lexington.db.mapper.IrsSecObjDimMapper;
import com.ctb.lexington.db.irsdata.IrsItemDimData;
import com.ctb.lexington.db.mapper.IrsItemDimMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsItemDimMapperTest extends AbstractMapperTest{

	private IrsSecObjDimMapper irsSecObjDimMapper;
	private IrsSecObjDimData irsSecObjDimData;
	private IrsItemDimData irsItemDimData;
	private IrsItemDimMapper irsItemDimMapper;
	private static final Long NON_EXISTING_ITEMID = new Long(9999);
	private static final String NON_EXISTING_OASITEMID = "TestId.10.9";
	private static final Long NON_EXISTING_ITEMINDEX = new Long(0);
	private static final String NON_EXISTING_ITEMTEXT = "TESTITEM";
	private static final String NON_EXISTING_ITEMTYPE = "SR";
	private static final String NON_EXISTING_CORRECTRESPONSE = "D";
	private static final Long NON_EXISTING_NATIONALAVERAGE = new Long(11);
	private static final Long NON_EXISTING_POINTSPOSSIBLE = new Long(99);
	private static final Long NON_EXISTING_SECOBJID = new Long(10);
    
	public IrsItemDimMapperTest(String name){
		super(name);	 
	}
	
	protected void setUp()throws Exception{
        super.setUp();
        conn = getIRSConnection();
        irsItemDimMapper = new IrsItemDimMapper(conn);
        irsSecObjDimMapper = new IrsSecObjDimMapper(conn);
        irsSecObjDimData = new IrsSecObjDimData();
        irsItemDimData = new IrsItemDimData();
        irsItemDimData.setItemid(NON_EXISTING_ITEMID);
        irsItemDimData.setItemIndex(NON_EXISTING_ITEMINDEX);
        irsItemDimData.setItemText(NON_EXISTING_ITEMTEXT);
        irsItemDimData.setItemType(NON_EXISTING_ITEMTYPE);
        irsItemDimData.setCorrectResponse(NON_EXISTING_CORRECTRESPONSE);
        irsItemDimData.setOasItemid(NON_EXISTING_OASITEMID);
        irsItemDimData.setPointsPossible(NON_EXISTING_POINTSPOSSIBLE);
        irsItemDimData.setNationalAverage(NON_EXISTING_NATIONALAVERAGE);
        irsItemDimData.setSecObjid(NON_EXISTING_SECOBJID);
        irsSecObjDimData.setSecObjid(NON_EXISTING_SECOBJID);
    }
	
	public void test1InsertNonExistingSecObjRecord()throws SQLException{
     	assertNull(irsSecObjDimMapper.findBySecObjId(NON_EXISTING_SECOBJID));
     	irsSecObjDimMapper.insert(irsSecObjDimData);
     	assertNotNull(irsSecObjDimMapper.findBySecObjId(NON_EXISTING_SECOBJID));
     	conn.commit();
	}
	
	public void test5InsertNonExistingRecord()throws Exception{
		assertNull(irsItemDimMapper.findByOASItemIdAndSecObjId(NON_EXISTING_OASITEMID, NON_EXISTING_SECOBJID));
		irsItemDimMapper.insert(irsItemDimData);
		conn.commit();
		assertNotNull(irsItemDimMapper.findByOASItemIdAndSecObjId(NON_EXISTING_OASITEMID, NON_EXISTING_SECOBJID));
	}
	
	public void test6Update()throws SQLException{
		assertNotNull(irsItemDimMapper.findByOASItemIdAndSecObjId(NON_EXISTING_OASITEMID, NON_EXISTING_SECOBJID));
		IrsItemDimData record = irsItemDimMapper.findByOASItemIdAndSecObjId(NON_EXISTING_OASITEMID, NON_EXISTING_SECOBJID);
		assertNotNull(record);
		final Long oldItemId = record.getItemid();
		final Long oldItemIndex = record.getItemIndex();
		final String oldItemType = record.getItemType();
		final String oldItemText = record.getItemText();
		final String oldCorrectResponse = record.getCorrectResponse();
		final Long oldPointsPossible = record.getPointsPossible();
		final Long oldNationalAverage = record.getNationalAverage();
		final Long oldSecObjId = record.getSecObjid();
		record.setItemid(new Long(99999));
		record.setItemIndex(new Long(15));
		record.setItemType("testtype");
		record.setItemText("testtext");
		record.setCorrectResponse("TD");
		record.setPointsPossible(new Long(99));
		record.setNationalAverage(new Long(99));
		record.setSecObjid(NON_EXISTING_SECOBJID);
		try{
			irsItemDimMapper.update(record);
			conn.commit();
			IrsItemDimData updatedRecord = irsItemDimMapper.findByOASItemIdAndSecObjId(NON_EXISTING_OASITEMID, NON_EXISTING_SECOBJID);
			assertEquals("testtext",updatedRecord.getItemText());
		 	}finally{
		 		record.setItemid(oldItemId);
		 		record.setItemIndex(oldItemIndex);
		 		record.setItemText(oldItemText);
		 		record.setItemType(oldItemType);
		 		record.setCorrectResponse(oldCorrectResponse);
		 		record.setPointsPossible(oldPointsPossible);
		 		record.setNationalAverage(oldNationalAverage);
		 		record.setSecObjid(oldSecObjId);
		 		irsItemDimMapper.update(record);
		 		conn.commit();
		 	}
	}
	
	public void test7Delete()throws SQLException{
		assertNotNull(irsItemDimMapper.findByOASItemIdAndSecObjId(NON_EXISTING_OASITEMID, NON_EXISTING_SECOBJID));
		irsItemDimMapper.delete(NON_EXISTING_OASITEMID);
		conn.commit();
		assertNull(irsItemDimMapper.findByOASItemIdAndSecObjId(NON_EXISTING_OASITEMID, NON_EXISTING_SECOBJID));
	}
	
	public void test8SecObjDimDelete()throws SQLException{
		assertNotNull(irsSecObjDimMapper.findBySecObjId(NON_EXISTING_SECOBJID));
		irsSecObjDimMapper.delete(NON_EXISTING_SECOBJID);
		conn.commit();
		assertNull(irsSecObjDimMapper.findBySecObjId(NON_EXISTING_SECOBJID));
	}
	
	protected void tearDown()throws SQLException{
		conn.close();
	}
}