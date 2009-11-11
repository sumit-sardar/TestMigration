package test.ctb.lexington.db.mapper;

import java.sql.SQLException;
import java.util.Date;
import com.ctb.lexington.db.irsdata.irstbdata.IrsTABESecObjFactData;
import com.ctb.lexington.db.mapper.tbmapper.IrsTABESecObjFactMapper;
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
import com.ctb.lexington.db.irsdata.IrsSecObjDimData;
import com.ctb.lexington.db.mapper.IrsSecObjDimMapper;
import com.ctb.lexington.db.irsdata.IrsCustomerDimData;
import com.ctb.lexington.db.mapper.IrsCustomerDimMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsTABESecObjFactMapperTest extends AbstractMapperTest{

	private IrsTABESecObjFactData irsTABESecObjFactData;
	private IrsTABESecObjFactMapper irsTABESecObjFactMapper;
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
	private IrsSecObjDimMapper irsSecObjDimMapper;
	private IrsSecObjDimData irsSecObjDimData;
	private IrsCustomerDimMapper irsCustomerDimMapper;
	private IrsCustomerDimData irsCustomerDimData;
	
	private static final Long NON_EXISTING_STUDENTID = new Long(999999);
	private static final Long NON_EXISTING_FORMID = new Long(12);
	private static final Long NON_EXISTING_LEVELID = new Long(7);
	private static final Long NON_EXISTING_ORGNODEID = new Long(1212);
	private static final Long NON_EXISTING_PRODUCTID = new Long(9999);
	private static final Long NON_EXISTING_ASSESSMENTID = new Long(9999);
	private static final Long NON_EXISTING_CURRENTRESULTID = new Long(3);
	private static final Long NON_EXISTING_SESSIONID = new Long(61251);
	private static final Long NON_EXISTING_FACTID = new Long(99);
	private static final Long NON_EXISTING_POINTSOBTAINED = new Long(90);
	private static final Long NON_EXISTING_POINTSPOSSIBLE = new Long(99);
	private static final Date NON_EXISTING_TESTCOMPLITIONTIMESTAMP = createDate(2004,3,12);
	private static final Long NON_EXISTING_POINTSATTEMPTED = new Long(90);
	private static final Long NON_EXISTING_PERCENTOBTAINED = new Long(85);
	private static final Long NON_EXISTING_SECOBJID = new Long(9999);
	private static final Long NON_EXISTING_CUSTOMERID =  new Long(3);
	
	public IrsTABESecObjFactMapperTest(String name){
		super(name);	 
	 }

	protected void setUp()throws Exception{
        super.setUp();
        conn = getIRSConnection();
        irsTABESecObjFactMapper = new IrsTABESecObjFactMapper(conn);
        irsTABESecObjFactData = new IrsTABESecObjFactData();
        irsStudentDimMapper = new IrsStudentDimMapper(conn);
        irsStudentDimData = new IrsStudentDimData();
        irsFormDimMapper = new IrsFormDimMapper(conn);
        irsFormDimData = new IrsFormDimData();
        irsLevelDimMapper = new IrsLevelDimMapper(conn);
        irsLevelDimData = new IrsLevelDimData();
        irsOrgNodeDimMapper = new IrsOrgNodeDimMapper(conn);
        irsOrgNodeDimData = new IrsOrgNodeDimData();
        irsAssessmentDimMapper = new IrsAssessmentDimMapper(conn);
        irsAssessmentDimData = new IrsAssessmentDimData();
        irsCurrentResultDimMapper = new IrsCurrentResultDimMapper(conn);
        irsCurrentResultDimData = new IrsCurrentResultDimData();
        irsProductDimMapper = new IrsProductDimMapper(conn);
        irsProductDimData = new IrsProductDimData();
        irsSecObjDimMapper = new IrsSecObjDimMapper(conn);
        irsSecObjDimData = new IrsSecObjDimData();
        irsCustomerDimMapper = new IrsCustomerDimMapper(conn);
        irsCustomerDimData = new IrsCustomerDimData();
        
        irsStudentDimData.setStudentid(NON_EXISTING_STUDENTID);
        irsOrgNodeDimData.setOrgNodeid(NON_EXISTING_ORGNODEID);
        irsOrgNodeDimData.setCustomerid(NON_EXISTING_CUSTOMERID);
        irsOrgNodeDimData.setNumLevels(new Long(5));
        irsFormDimData.setFormid(NON_EXISTING_FORMID);
        irsLevelDimData.setLevelid(NON_EXISTING_LEVELID);
        irsLevelDimData.setName("Z");
        irsSecObjDimData.setSecObjid(NON_EXISTING_SECOBJID);
        irsCustomerDimData.setCustomerid(NON_EXISTING_CUSTOMERID);
        irsTABESecObjFactData.setSecObjid(NON_EXISTING_SECOBJID);
        irsProductDimData.setProductid(NON_EXISTING_PRODUCTID);
        irsAssessmentDimData.setAssessmentid(NON_EXISTING_ASSESSMENTID);
        irsCurrentResultDimData.setCurrentResultid(NON_EXISTING_CURRENTRESULTID);

        irsTABESecObjFactData.setFactid(NON_EXISTING_FACTID);
        irsTABESecObjFactData.setStudentid(NON_EXISTING_STUDENTID);
        irsTABESecObjFactData.setFormid(NON_EXISTING_FORMID);
        irsTABESecObjFactData.setSessionid(NON_EXISTING_SESSIONID);
        irsTABESecObjFactData.setLevelid(NON_EXISTING_LEVELID);
        irsTABESecObjFactData.setOrgNodeid(NON_EXISTING_ORGNODEID);
        irsTABESecObjFactData.setAssessmentid(NON_EXISTING_ASSESSMENTID);
        irsTABESecObjFactData.setCurrentResultid(NON_EXISTING_CURRENTRESULTID);
        irsTABESecObjFactData.setTestCompletionTimestamp(NON_EXISTING_TESTCOMPLITIONTIMESTAMP);
        irsTABESecObjFactData.setPointsObtained(NON_EXISTING_POINTSOBTAINED);
        irsTABESecObjFactData.setPointsPossible(NON_EXISTING_POINTSPOSSIBLE);
        irsTABESecObjFactData.setPointsAttempted(NON_EXISTING_POINTSATTEMPTED);
        irsTABESecObjFactData.setPercentObtained(NON_EXISTING_PERCENTOBTAINED);
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
	
	public void test9InsertNonExistingRecordSecObjDim()throws Exception{
		assertNull(irsSecObjDimMapper.findBySecObjId(NON_EXISTING_SECOBJID));
		irsSecObjDimMapper.insert(irsSecObjDimData);
		assertNotNull(irsSecObjDimMapper.findBySecObjId(NON_EXISTING_SECOBJID));
		conn.commit();
	}
	
	public void test15InsertNonExistingRecord()throws SQLException{
		assertNull(irsTABESecObjFactMapper.findByObjStudentSession(NON_EXISTING_SECOBJID, NON_EXISTING_STUDENTID, NON_EXISTING_SESSIONID));
		irsTABESecObjFactMapper.insert(irsTABESecObjFactData);
		conn.commit();
		assertNotNull(irsTABESecObjFactMapper.findByObjStudentSession(NON_EXISTING_SECOBJID, NON_EXISTING_STUDENTID, NON_EXISTING_SESSIONID));
	}		 
	
	public void test16UpdateTABEItemFact()throws SQLException{
		assertNotNull(irsTABESecObjFactMapper.findByObjStudentSession(NON_EXISTING_SECOBJID, NON_EXISTING_STUDENTID, NON_EXISTING_SESSIONID));
		IrsTABESecObjFactData record = irsTABESecObjFactMapper.findByObjStudentSession(NON_EXISTING_SECOBJID, NON_EXISTING_STUDENTID, NON_EXISTING_SESSIONID);
		assertNotNull(record);
		final Long oldFormId = record.getFormid();
		final Long oldLevelId = record.getLevelid();
		final Long oldPointsObtained = record.getPointsObtained();
		final Date oldTestComplitionTimestamp = record.getTestCompletionTimestamp();
		final Long oldOrgNodeId = record.getOrgNodeid();
		final Long oldAssessmentId = record.getAssessmentid();
		final Long oldPointsPossible = record.getPointsPossible();
		final Long oldCurrentResultId = record.getCurrentResultid();
		record.setFormid(NON_EXISTING_FORMID);
		record.setLevelid(NON_EXISTING_LEVELID);
		record.setPointsObtained(new Long(55));
		record.setTestCompletionTimestamp(createDate(2005,3,12));
		record.setOrgNodeid(NON_EXISTING_ORGNODEID);
		record.setAssessmentid(NON_EXISTING_ASSESSMENTID);
		record.setPointsPossible(new Long(66));
		record.setCurrentResultid(NON_EXISTING_CURRENTRESULTID);
		try{
			irsTABESecObjFactMapper.update(record);
			conn.commit();
			IrsTABESecObjFactData updatedRecord = irsTABESecObjFactMapper.findByObjStudentSession(NON_EXISTING_SECOBJID, NON_EXISTING_STUDENTID, NON_EXISTING_SESSIONID);
			assertEquals(new Long(55),updatedRecord.getPointsObtained());
			}finally{
				record.setFormid(oldFormId);
				record.setLevelid(oldLevelId);
				record.setPointsObtained(oldPointsObtained);
				record.setTestCompletionTimestamp(oldTestComplitionTimestamp);
				record.setOrgNodeid(oldOrgNodeId);
				record.setAssessmentid(oldAssessmentId);
				record.setPointsPossible(oldPointsPossible);
				record.setCurrentResultid(oldCurrentResultId);
				irsTABESecObjFactMapper.update(record);
				conn.commit();
			 }	
	}
	
	public void test17Delete()throws SQLException{
		assertNotNull(irsTABESecObjFactMapper.findByObjStudentSession(NON_EXISTING_SECOBJID, NON_EXISTING_STUDENTID, NON_EXISTING_SESSIONID));
		irsTABESecObjFactData = irsTABESecObjFactMapper.findByObjStudentSession(NON_EXISTING_SECOBJID, NON_EXISTING_STUDENTID, NON_EXISTING_SESSIONID);
		irsTABESecObjFactMapper.delete(irsTABESecObjFactData);
		conn.commit();
		assertNull(irsTABESecObjFactMapper.findByObjStudentSession(NON_EXISTING_SECOBJID, NON_EXISTING_STUDENTID, NON_EXISTING_SESSIONID));
	}	
	
	public void test18DeleteOrgNodeDim()throws SQLException{
		assertNotNull(irsOrgNodeDimMapper.findByOrgNodeIds(irsOrgNodeDimData));
		irsOrgNodeDimMapper.delete(NON_EXISTING_ORGNODEID);
		conn.commit();
		assertNull(irsOrgNodeDimMapper.findByOrgNodeIds(irsOrgNodeDimData));
	}
	
	public void test19DeleteCustomerDim()throws SQLException{
		assertNotNull(irsCustomerDimMapper.findByCustomerId(NON_EXISTING_CUSTOMERID));
		irsCustomerDimMapper.delete(NON_EXISTING_CUSTOMERID);
		conn.commit();
		assertNull(irsCustomerDimMapper.findByCustomerId(NON_EXISTING_CUSTOMERID));
	}	
		
	public void test20DeleteFormDim()throws SQLException{
		assertNotNull(irsFormDimMapper.findByFormId(NON_EXISTING_FORMID));
		irsFormDimMapper.delete(NON_EXISTING_FORMID);
		conn.commit();
		assertNull(irsFormDimMapper.findByFormId(NON_EXISTING_FORMID));
	}
	
	public void test21DeleteLevelDim()throws SQLException{
		assertNotNull(irsLevelDimMapper.findByLevelId(NON_EXISTING_LEVELID));
		irsLevelDimMapper.delete(NON_EXISTING_LEVELID);
		conn.commit();
		assertNull(irsLevelDimMapper.findByLevelId(NON_EXISTING_LEVELID));
	}
	
	public void test22DeleteProductDim()throws SQLException{
		assertNotNull(irsProductDimMapper.findByProductId(NON_EXISTING_PRODUCTID));
		irsProductDimMapper.delete(NON_EXISTING_PRODUCTID);
		conn.commit();
		assertNull(irsProductDimMapper.findByProductId(NON_EXISTING_PRODUCTID));
	}
	
	public void test23DeleteAssessmentDim()throws SQLException{
		assertNotNull(irsAssessmentDimMapper.findByAssessmentId(NON_EXISTING_ASSESSMENTID));
		irsAssessmentDimMapper.deleteAssessmentDim(NON_EXISTING_ASSESSMENTID);
		conn.commit();
		assertNull(irsAssessmentDimMapper.findByAssessmentId(NON_EXISTING_ASSESSMENTID));
	}
	
	public void test24DeleteCurrentResultDim()throws SQLException{
		assertNotNull(irsCurrentResultDimMapper.findByCurrentResultId(NON_EXISTING_CURRENTRESULTID));
		irsCurrentResultDimMapper.delete(NON_EXISTING_CURRENTRESULTID);
		conn.commit();
		assertNull(irsCurrentResultDimMapper.findByCurrentResultId(NON_EXISTING_CURRENTRESULTID));
	}	
	
	public void test25DeleteStudentDim()throws SQLException{
		assertNotNull(irsStudentDimMapper.findByStudentId(NON_EXISTING_STUDENTID));
		irsStudentDimMapper.delete(NON_EXISTING_STUDENTID);
		conn.commit();
		assertNull(irsStudentDimMapper.findByStudentId(NON_EXISTING_STUDENTID));
	}
	
	public void test26DeleteSecObjDim()throws SQLException{
		assertNotNull(irsSecObjDimMapper.findBySecObjId(NON_EXISTING_SECOBJID));
		irsSecObjDimMapper.delete(NON_EXISTING_SECOBJID);
		conn.commit();
		assertNull(irsSecObjDimMapper.findBySecObjId(NON_EXISTING_SECOBJID));
	}
	
	protected void tearDown()throws SQLException{
		conn.close();
	}
}