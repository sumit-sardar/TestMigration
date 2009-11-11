package test.ctb.lexington.db.mapper;

import java.sql.SQLException;
import java.util.Date;
import com.ctb.lexington.db.irsdata.irstbdata.IrsTABEItemFactData;
import com.ctb.lexington.db.mapper.tbmapper.IrsTABEItemFactMapper;
import com.ctb.lexington.db.irsdata.IrsStudentDimData;
import com.ctb.lexington.db.mapper.IrsStudentDimMapper;
import com.ctb.lexington.db.irsdata.IrsFormDimData;
import com.ctb.lexington.db.mapper.IrsFormDimMapper;
import com.ctb.lexington.db.irsdata.IrsLevelDimData;
import com.ctb.lexington.db.mapper.IrsLevelDimMapper;
import com.ctb.lexington.db.irsdata.IrsOrgNodeDimData;
import com.ctb.lexington.db.mapper.IrsOrgNodeDimMapper;
import com.ctb.lexington.db.irsdata.IrsAssessmentDimData;
import com.ctb.lexington.db.mapper.IrsAssessmentDimMapper;
import com.ctb.lexington.db.irsdata.IrsCurrentResultDimData;
import com.ctb.lexington.db.mapper.IrsCurrentResultDimMapper;
import com.ctb.lexington.db.irsdata.IrsProductDimData;
import com.ctb.lexington.db.mapper.IrsProductDimMapper;
import com.ctb.lexington.db.irsdata.IrsCustomerDimData;
import com.ctb.lexington.db.mapper.IrsCustomerDimMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsTABEItemFactMapperTest extends AbstractMapperTest{

	private IrsTABEItemFactMapper irsTABEItemFactMapper;
	private IrsTABEItemFactData irsTABEItemFactData;
	private IrsStudentDimData irsStudentDimData;
	private IrsStudentDimMapper irsStudentDimMapper;
	private IrsFormDimData irsFormDimData;
	private IrsFormDimMapper irsFormDimMapper;
	private IrsLevelDimData irsLevelDimData;
	private IrsLevelDimMapper irsLevelDimMapper;
	private IrsOrgNodeDimData irsOrgNodeDimData;
	private IrsOrgNodeDimMapper irsOrgNodeDimMapper;
	private IrsAssessmentDimData irsAssessmentDimData;
	private IrsAssessmentDimMapper irsAssessmentDimMapper;
	private IrsCurrentResultDimData irsCurrentResultDimData;
	private IrsCurrentResultDimMapper irsCurrentResultDimMapper;
	private IrsProductDimMapper irsProductDimMapper;
	private IrsProductDimData irsProductDimData;
	private IrsCustomerDimMapper irsCustomerDimMapper;
	private IrsCustomerDimData irsCustomerDimData;
	
	private static final Long NON_EXISTING_STUDENTID = new Long(999999);
	private static final Long NON_EXISTING_FORMID = new Long(12);
	private static final Long NON_EXISTING_LEVELID = new Long(7);
	private static final Long NON_EXISTING_ITEMID = new Long(1212);
	private static final Long NON_EXISTING_ORGNODEID = new Long(1212);
	private static final Long NON_EXISTING_PRODUCTID = new Long(9999);
	private static final Long NON_EXISTING_ASSESSMENTID = new Long(9999);
	private static final Long NON_EXISTING_CURRENTRESULTID = new Long(3);
	private static final Long NON_EXISTING_SESSIONID = new Long(61251);
	private static final Long NON_EXISTING_FACTID = new Long(99);
	private static final Long NON_EXISTING_POINTSOBTAINED = new Long(99);
	private static final Long NON_EXISTING_POINTSPOSSIBLE = new Long(99);
	private static final Date NON_EXISTING_ITEMRESPONSETIMESTAMP = createDate(2004,12,1);
	private static final Date NON_EXISTING_TESTCOMPLITIONTIMESTAMP = createDate(2005,12,1);
	private static final Long NON_EXISTING_CUSTOMERID =  new Long(3);
	
	
	public IrsTABEItemFactMapperTest(String name){
		super(name);	 
	}

	protected void setUp()throws Exception{
        super.setUp();
        conn = getIRSConnection();
        irsTABEItemFactMapper = new IrsTABEItemFactMapper(conn);
        irsTABEItemFactData = new IrsTABEItemFactData();
        irsStudentDimMapper = new IrsStudentDimMapper(conn);
        irsStudentDimData = new IrsStudentDimData();
        irsFormDimMapper = new IrsFormDimMapper(conn);
        irsFormDimData = new IrsFormDimData();
        irsLevelDimMapper = new IrsLevelDimMapper(conn);
        irsLevelDimData = new IrsLevelDimData();
        irsLevelDimData = new IrsLevelDimData();
        irsOrgNodeDimMapper = new IrsOrgNodeDimMapper(conn);
        irsOrgNodeDimData = new IrsOrgNodeDimData();
        irsAssessmentDimMapper = new IrsAssessmentDimMapper(conn);
        irsAssessmentDimData = new IrsAssessmentDimData();
        irsCurrentResultDimMapper = new IrsCurrentResultDimMapper(conn);
        irsCurrentResultDimData = new IrsCurrentResultDimData();
        irsProductDimMapper = new IrsProductDimMapper(conn);
        irsProductDimData = new IrsProductDimData();
        irsCustomerDimMapper = new IrsCustomerDimMapper(conn);
        irsCustomerDimData = new IrsCustomerDimData();
        
        irsStudentDimData.setStudentid(NON_EXISTING_STUDENTID);
        irsOrgNodeDimData.setOrgNodeid(NON_EXISTING_ORGNODEID);
        irsOrgNodeDimData.setCustomerid(NON_EXISTING_CUSTOMERID);
        irsOrgNodeDimData.setNumLevels(new Long(5));
        irsFormDimData.setFormid(NON_EXISTING_FORMID);
        irsLevelDimData.setLevelid(NON_EXISTING_LEVELID);
        irsLevelDimData.setName("Z");
        irsProductDimData.setProductid(NON_EXISTING_PRODUCTID);
        irsAssessmentDimData.setAssessmentid(NON_EXISTING_ASSESSMENTID);
        irsCurrentResultDimData.setCurrentResultid(NON_EXISTING_CURRENTRESULTID);
        irsCustomerDimData.setCustomerid(NON_EXISTING_CUSTOMERID);
        
        irsTABEItemFactData.setFactid(NON_EXISTING_FACTID);
        irsTABEItemFactData.setStudentid(NON_EXISTING_STUDENTID);
        irsTABEItemFactData.setFormid(NON_EXISTING_FORMID);
        irsTABEItemFactData.setSessionid(NON_EXISTING_SESSIONID);
        irsTABEItemFactData.setLevelid(NON_EXISTING_LEVELID);
        irsTABEItemFactData.setItemid(NON_EXISTING_ITEMID);
        irsTABEItemFactData.setOrgNodeid(NON_EXISTING_ORGNODEID);
        irsTABEItemFactData.setAssessmentid(NON_EXISTING_ASSESSMENTID);
        irsTABEItemFactData.setCurrentResultid(NON_EXISTING_CURRENTRESULTID);
        irsTABEItemFactData.setItemResponseTimestamp(NON_EXISTING_ITEMRESPONSETIMESTAMP);
        irsTABEItemFactData.setTestCompletionTimestamp(NON_EXISTING_TESTCOMPLITIONTIMESTAMP);
        irsTABEItemFactData.setPointsObtained(NON_EXISTING_POINTSOBTAINED);
        irsTABEItemFactData.setPointsPossible(NON_EXISTING_POINTSPOSSIBLE);
    }

	public void test1InsertNonExistingCustomerDimRecord()throws SQLException{
     	assertNull(irsCustomerDimMapper.findByCustomerId(NON_EXISTING_CUSTOMERID));
     	irsCustomerDimMapper.insert(irsCustomerDimData);
     	assertNotNull(irsCustomerDimMapper.findByCustomerId(NON_EXISTING_CUSTOMERID));
     	conn.commit();
	}
 
	public void test2InsertNonExistingRecordOrgNodeDim()throws SQLException{
	 	assertNull(irsOrgNodeDimMapper.findByOrgNodeIds(irsOrgNodeDimData));
	 	irsOrgNodeDimMapper.insert(irsOrgNodeDimData);
	 	assertNotNull(irsOrgNodeDimMapper.findByOrgNodeIds(irsOrgNodeDimData));
	 	conn.commit();
	}
	
	public void test3InsertNonExistingRecordFormDim()throws SQLException{
		assertNull(irsFormDimMapper.findByFormId(NON_EXISTING_FORMID));
	 	irsFormDimMapper.insert(irsFormDimData);
	 	assertNotNull(irsFormDimMapper.findByFormId(NON_EXISTING_FORMID));
	 	conn.commit();
	}
	
	public void test4InsertNonExistingRecordLevelDim()throws SQLException{
	 	assertNull(irsLevelDimMapper.findByLevelId(NON_EXISTING_LEVELID));
	 	irsLevelDimMapper.insert(irsLevelDimData);
	 	assertNotNull(irsLevelDimMapper.findByLevelId(NON_EXISTING_LEVELID));
	 	conn.commit();
	}
	
	public void test5InsertingNonExistingProductDimRecord()throws SQLException{
		assertNull(irsProductDimMapper.findByProductId(NON_EXISTING_PRODUCTID));
		irsProductDimMapper.insert(irsProductDimData);
		assertNotNull(irsProductDimMapper.findByProductId(NON_EXISTING_PRODUCTID));
		conn.commit();
	}	
	
	public void test6InsertNonExistingAssessmentDimRecord()throws SQLException{
    	assertNull(irsAssessmentDimMapper.findByAssessmentId(NON_EXISTING_ASSESSMENTID));
		irsAssessmentDimMapper.insert(irsAssessmentDimData);
    	assertNotNull(irsAssessmentDimMapper.findByAssessmentId(NON_EXISTING_ASSESSMENTID));
    	conn.commit();
    }
	
	public void test7InsertNonExistingRecordCurrentResultDim()throws SQLException{
		assertNull(irsCurrentResultDimMapper.findByCurrentResultId(NON_EXISTING_CURRENTRESULTID));
		irsCurrentResultDimMapper.insert(irsCurrentResultDimData);
		assertNotNull(irsCurrentResultDimMapper.findByCurrentResultId(NON_EXISTING_CURRENTRESULTID));
		conn.commit();
	}
	
	public void test8InsertNonExistingRecordStudentDim()throws SQLException{
		assertNull(irsStudentDimMapper.findByStudentId(NON_EXISTING_STUDENTID));
		irsStudentDimMapper.insert(irsStudentDimData);
		assertNotNull(irsStudentDimMapper.findByStudentId(NON_EXISTING_STUDENTID));
		conn.commit();
	}
	
	public void test9InsertNonExistingRecord()throws SQLException{
		assertNull(irsTABEItemFactMapper.findByItemStudentSession(NON_EXISTING_ITEMID, NON_EXISTING_STUDENTID, NON_EXISTING_SESSIONID));
		irsTABEItemFactMapper.insert(irsTABEItemFactData);
		conn.commit();
		assertNotNull(irsTABEItemFactMapper.findByItemStudentSession(NON_EXISTING_ITEMID, NON_EXISTING_STUDENTID, NON_EXISTING_SESSIONID));
	}
	 
	public void test10UpdateTABEItemFact()throws SQLException{
		assertNotNull(irsTABEItemFactMapper.findByItemStudentSession(NON_EXISTING_ITEMID, NON_EXISTING_STUDENTID, NON_EXISTING_SESSIONID));
		IrsTABEItemFactData record = irsTABEItemFactMapper.findByItemStudentSession(NON_EXISTING_ITEMID, NON_EXISTING_STUDENTID, NON_EXISTING_SESSIONID);
		assertNotNull(record);
		final Long oldFormId = record.getFormid();
		final Long oldLevelId = record.getLevelid();
		final Long oldPointsObtained = record.getPointsObtained();
		final Date oldItemResponseTimestamp = record.getItemResponseTimestamp();
		final Date oldTestComplitionTimestamp = record.getTestCompletionTimestamp();
		final Long oldOrgNodeId = record.getOrgNodeid();
		final Long oldAssessmentId = record.getAssessmentid();
		final Long oldPointsPossible = record.getPointsPossible();
		final Long oldCurrentResultId = record.getCurrentResultid();
		record.setFormid(NON_EXISTING_FORMID);
		record.setLevelid(NON_EXISTING_LEVELID);
		record.setPointsObtained(new Long(55));
		record.setItemResponseTimestamp(new Date(6));
		record.setTestCompletionTimestamp(new Date(9));
		record.setOrgNodeid(NON_EXISTING_ORGNODEID);
		record.setAssessmentid(NON_EXISTING_ASSESSMENTID);
		record.setPointsPossible(new Long(66));
		record.setCurrentResultid(NON_EXISTING_CURRENTRESULTID);
		try{
			irsTABEItemFactMapper.update(record);
			conn.commit();
			IrsTABEItemFactData updatedRecord = irsTABEItemFactMapper.findByItemStudentSession(NON_EXISTING_ITEMID, NON_EXISTING_STUDENTID, NON_EXISTING_SESSIONID);
			assertEquals(new Long(55),updatedRecord.getPointsObtained());
		 	}finally{
		 		record.setFormid(oldFormId);
		 		record.setLevelid(oldLevelId);
		 		record.setPointsObtained(oldPointsObtained);
		 		record.setItemResponseTimestamp(oldItemResponseTimestamp);
		 		record.setTestCompletionTimestamp(oldTestComplitionTimestamp);
		 		record.setOrgNodeid(oldOrgNodeId);
		 		record.setAssessmentid(oldAssessmentId);
		 		record.setPointsPossible(oldPointsPossible);
		 		record.setCurrentResultid(oldCurrentResultId);
		 		irsTABEItemFactMapper.update(record);
		 		conn.commit();
		 	}
	}
	
	public void test11Delete()throws SQLException{
		assertNotNull(irsTABEItemFactMapper.findByItemStudentSession(NON_EXISTING_ITEMID, NON_EXISTING_STUDENTID, NON_EXISTING_SESSIONID));
		irsTABEItemFactData = irsTABEItemFactMapper.findByItemStudentSession(NON_EXISTING_ITEMID, NON_EXISTING_STUDENTID, NON_EXISTING_SESSIONID);
		irsTABEItemFactMapper.delete(irsTABEItemFactData);
		conn.commit();
		assertNull(irsTABEItemFactMapper.findByItemStudentSession(NON_EXISTING_ITEMID, NON_EXISTING_STUDENTID, NON_EXISTING_SESSIONID));
	}	
		
	public void test12DeleteOrgNodeDim()throws SQLException{
		assertNotNull(irsOrgNodeDimMapper.findByOrgNodeIds(irsOrgNodeDimData));
		irsOrgNodeDimMapper.delete(NON_EXISTING_ORGNODEID);
		conn.commit();
		assertNull(irsOrgNodeDimMapper.findByOrgNodeIds(irsOrgNodeDimData));
	}
	 
	public void test13DeleteCustomerDim()throws SQLException{
		assertNotNull(irsCustomerDimMapper.findByCustomerId(NON_EXISTING_CUSTOMERID));
		irsCustomerDimMapper.delete(NON_EXISTING_CUSTOMERID);
		conn.commit();
		assertNull(irsCustomerDimMapper.findByCustomerId(NON_EXISTING_CUSTOMERID));
	}	
			
	public void test14DeleteFormDim()throws SQLException{
		assertNotNull(irsFormDimMapper.findByFormId(NON_EXISTING_FORMID));
		irsFormDimMapper.delete(NON_EXISTING_FORMID);
		conn.commit();
		assertNull(irsFormDimMapper.findByFormId(NON_EXISTING_FORMID));
	}
	
	public void test15DeleteLevelDim()throws SQLException{
		assertNotNull(irsLevelDimMapper.findByLevelId(NON_EXISTING_LEVELID));
		irsLevelDimMapper.delete(NON_EXISTING_LEVELID);
		conn.commit();
		assertNull(irsLevelDimMapper.findByLevelId(NON_EXISTING_LEVELID));
	}
	
	public void test16DeleteProductDim()throws SQLException{
		assertNotNull(irsProductDimMapper.findByProductId(NON_EXISTING_PRODUCTID));
		irsProductDimMapper.delete(NON_EXISTING_PRODUCTID);
		conn.commit();
		assertNull(irsProductDimMapper.findByProductId(NON_EXISTING_PRODUCTID));
	}
	
	public void test17DeleteAssessmentDim()throws SQLException{
		assertNotNull(irsAssessmentDimMapper.findByAssessmentId(NON_EXISTING_ASSESSMENTID));
		irsAssessmentDimMapper.deleteAssessmentDim(NON_EXISTING_ASSESSMENTID);
		conn.commit();
		assertNull(irsAssessmentDimMapper.findByAssessmentId(NON_EXISTING_ASSESSMENTID));
	}
	
	public void test18DeleteCurrentResultDim()throws SQLException{
		assertNotNull(irsCurrentResultDimMapper.findByCurrentResultId(NON_EXISTING_CURRENTRESULTID));
		irsCurrentResultDimMapper.delete(NON_EXISTING_CURRENTRESULTID);
		conn.commit();
		assertNull(irsCurrentResultDimMapper.findByCurrentResultId(NON_EXISTING_CURRENTRESULTID));
	}
	
	public void test19DeleteStudentDim()throws SQLException{
		assertNotNull(irsStudentDimMapper.findByStudentId(NON_EXISTING_STUDENTID));
		irsStudentDimMapper.delete(NON_EXISTING_STUDENTID);
		conn.commit();
		assertNull(irsStudentDimMapper.findByStudentId(NON_EXISTING_STUDENTID));
	}		
	
	protected void tearDown()throws SQLException{
		conn.close();
	}
}