package test.ctb.lexington.db.mapper;

import java.sql.SQLException;
import java.util.Date;
import com.ctb.lexington.db.irsdata.irstbdata.IrsTABECompositeFactData;
import com.ctb.lexington.db.mapper.tbmapper.IrsTABECompositeFactMapper;
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
import com.ctb.lexington.db.irsdata.IrsCompositeDimData;
import com.ctb.lexington.db.mapper.IrsCompositeDimMapper;
import com.ctb.lexington.db.irsdata.IrsSubjectDimData;
import com.ctb.lexington.db.mapper.IrsSubjectDimMapper;
import com.ctb.lexington.db.irsdata.IrsCustomerDimData;
import com.ctb.lexington.db.mapper.IrsCustomerDimMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsTABECompositeFactMapperTest extends AbstractMapperTest{

	private IrsTABECompositeFactData irsTABECompositeFactData;
	private IrsTABECompositeFactMapper irsTABECompositeFactMapper;
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
	private IrsCompositeDimMapper irsCompositeDimMapper;
	private IrsCompositeDimData irsCompositeDimData;
	private IrsSubjectDimData irsSubjectDimData;
	private IrsSubjectDimMapper irsSubjectDimMapper;
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
	private static final Long NON_EXISTING_COMPOSITEID = new Long(999999);
	private static final Long NON_EXISTING_SUBJECTID = new Long(9999);
	private static final String NON_EXISTING_SUBJECTNAME = "TestFact";
	private static final Long NON_EXISTING_PERCENTAGEMASTERY =  new Long(99);
	private static final Long NON_EXISTING_CUSTOMERID =  new Long(3);
	
	public IrsTABECompositeFactMapperTest(String name){
		super(name);	 
	}
	
	protected void setUp()throws Exception{
        super.setUp();
        conn = getIRSConnection();
        irsTABECompositeFactMapper = new IrsTABECompositeFactMapper(conn);
        irsTABECompositeFactData = new IrsTABECompositeFactData();
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
        irsCompositeDimMapper = new IrsCompositeDimMapper(conn);
        irsCompositeDimData = new IrsCompositeDimData();
        irsSubjectDimMapper = new IrsSubjectDimMapper(conn);
        irsSubjectDimData = new IrsSubjectDimData();
        irsCustomerDimMapper = new IrsCustomerDimMapper(conn);
        irsCustomerDimData = new IrsCustomerDimData();
        
        irsStudentDimData.setStudentid(NON_EXISTING_STUDENTID);
        irsOrgNodeDimData.setOrgNodeid(NON_EXISTING_ORGNODEID);
        irsOrgNodeDimData.setCustomerid(NON_EXISTING_CUSTOMERID);
        irsOrgNodeDimData.setNumLevels(new Long(5));
        irsFormDimData.setFormid(NON_EXISTING_FORMID);
        irsLevelDimData.setLevelid(NON_EXISTING_LEVELID);
        irsLevelDimData.setName("Z");
        irsCompositeDimData.setCompositeid(NON_EXISTING_COMPOSITEID);
        irsTABECompositeFactData.setCompositeid(NON_EXISTING_COMPOSITEID);
        irsProductDimData.setProductid(NON_EXISTING_PRODUCTID);
        irsAssessmentDimData.setAssessmentid(NON_EXISTING_ASSESSMENTID);
        irsCurrentResultDimData.setCurrentResultid(NON_EXISTING_CURRENTRESULTID);
        irsTABECompositeFactData.setFactid(NON_EXISTING_FACTID);
        irsTABECompositeFactData.setStudentid(NON_EXISTING_STUDENTID);
        irsTABECompositeFactData.setFormid(NON_EXISTING_FORMID);
        irsTABECompositeFactData.setSessionid(NON_EXISTING_SESSIONID);        
        irsTABECompositeFactData.setOrgNodeid(NON_EXISTING_ORGNODEID);
        irsTABECompositeFactData.setAssessmentid(NON_EXISTING_ASSESSMENTID);
        irsTABECompositeFactData.setCurrentResultid(NON_EXISTING_CURRENTRESULTID);
        irsTABECompositeFactData.setTestCompletionTimestamp(NON_EXISTING_TESTCOMPLITIONTIMESTAMP);
        irsTABECompositeFactData.setPointsObtained(NON_EXISTING_POINTSOBTAINED);
        irsTABECompositeFactData.setPointsPossible(NON_EXISTING_POINTSPOSSIBLE);
        irsTABECompositeFactData.setPercentageMastery(NON_EXISTING_PERCENTAGEMASTERY);
        irsSubjectDimData.setSubjectid(NON_EXISTING_SUBJECTID);
        irsSubjectDimData.setSubjectName(NON_EXISTING_SUBJECTNAME);
        irsCompositeDimData.setSubjectid(NON_EXISTING_SUBJECTID);   
        irsCustomerDimData.setCustomerid(NON_EXISTING_CUSTOMERID);
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
	
	public void test9InsertNonExistingSubjectRecord()throws SQLException{
		assertNull(irsSubjectDimMapper.findBySubjectId(NON_EXISTING_SUBJECTID));
		irsSubjectDimMapper.insert(irsSubjectDimData);
		assertNotNull(irsSubjectDimMapper.findBySubjectId(NON_EXISTING_SUBJECTID));
		conn.commit();
	}
		 
	public void test10InsertNonExistingRecordCompositeDim()throws Exception{
		assertNull(irsCompositeDimMapper.findByCompositeDimId(NON_EXISTING_COMPOSITEID));
		irsCompositeDimMapper.insert(irsCompositeDimData);
		assertNotNull(irsCompositeDimMapper.findByCompositeDimId(NON_EXISTING_COMPOSITEID));
		conn.commit();
	}
		 
	public void test11InsertNonExistingRecordTABECompositeFact()throws SQLException{
		assertNull(irsTABECompositeFactMapper.findByCompStudentSession(NON_EXISTING_COMPOSITEID, NON_EXISTING_STUDENTID, NON_EXISTING_SESSIONID));
		irsTABECompositeFactMapper.insert(irsTABECompositeFactData);
		conn.commit();
		assertNotNull(irsTABECompositeFactMapper.findByCompStudentSession(NON_EXISTING_COMPOSITEID, NON_EXISTING_STUDENTID, NON_EXISTING_SESSIONID));
	}
		 
	public void test12UpdateTABECompositeFact()throws SQLException{
		assertNotNull(irsTABECompositeFactMapper.findByCompStudentSession(NON_EXISTING_COMPOSITEID, NON_EXISTING_STUDENTID, NON_EXISTING_SESSIONID));
		IrsTABECompositeFactData record = irsTABECompositeFactMapper.findByCompStudentSession(NON_EXISTING_COMPOSITEID, NON_EXISTING_STUDENTID, NON_EXISTING_SESSIONID);
		assertNotNull(record);
		final Long oldFormId = record.getFormid();
		final Long oldPointsObtained = record.getPointsObtained();
		final Date oldTestComplitionTimestamp = record.getTestCompletionTimestamp();
		final Long oldOrgNodeId = record.getOrgNodeid();
		final Long oldAssessmentId = record.getAssessmentid();
		final Long oldPointsPossible = record.getPointsPossible();
		final Long oldCurrentResultId = record.getCurrentResultid();
		final Long oldScaleScore = record.getScaleScore();
		final Long oldNormalCurveEquivalent = record.getNormalCurveEquivalent();
		final Long oldNationalStanine = record.getNationalStanine();
		final Long oldNationalPercentile = record.getNationalPercentile();
		final String oldGradeEquivalent = record.getGradeEquivalent();
		final Long oldPredictedGED = record.getPredictedGed();
		record.setFormid(NON_EXISTING_FORMID);			
		record.setPointsObtained(new Long(55));
		record.setTestCompletionTimestamp(createDate(2006,12,3));
		record.setOrgNodeid(NON_EXISTING_ORGNODEID);
		record.setAssessmentid(NON_EXISTING_ASSESSMENTID);
		record.setPointsPossible(new Long(66));
		record.setCurrentResultid(NON_EXISTING_CURRENTRESULTID);
		record.setScaleScore(new Long(999));
		record.setNormalCurveEquivalent(new Long(9));
		record.setNationalStanine(new Long(99));
		record.setNationalPercentile(new Long(99));
		record.setGradeEquivalent("9");
		record.setPredictedGed(new Long(9));
		try{
			irsTABECompositeFactMapper.update(record);
			conn.commit();
			IrsTABECompositeFactData updatedRecord = irsTABECompositeFactMapper.findByCompStudentSession(NON_EXISTING_COMPOSITEID, NON_EXISTING_STUDENTID, NON_EXISTING_SESSIONID);
			assertEquals(new Long(55),updatedRecord.getPointsObtained());
			}finally{
				record.setFormid(oldFormId);
				record.setPointsObtained(oldPointsObtained);
				record.setTestCompletionTimestamp(oldTestComplitionTimestamp);
				record.setOrgNodeid(oldOrgNodeId);
				record.setAssessmentid(oldAssessmentId);
				record.setPointsPossible(oldPointsPossible);
				record.setCurrentResultid(oldCurrentResultId);
				record.setScaleScore(oldScaleScore);
				record.setNormalCurveEquivalent(oldNormalCurveEquivalent);
				record.setNationalStanine(oldNationalStanine);
				record.setNationalPercentile(oldNationalPercentile);
				record.setGradeEquivalent(oldGradeEquivalent);
				record.setPredictedGed(oldPredictedGED);
				irsTABECompositeFactMapper.update(record);
				conn.commit();
			 }	
	}
	
	public void test13DeleteTABECompositeFact()throws SQLException{
		assertNotNull(irsTABECompositeFactMapper.findByCompStudentSession(NON_EXISTING_COMPOSITEID, NON_EXISTING_STUDENTID, NON_EXISTING_SESSIONID));
		irsTABECompositeFactData = irsTABECompositeFactMapper.findByCompStudentSession(NON_EXISTING_COMPOSITEID, NON_EXISTING_STUDENTID, NON_EXISTING_SESSIONID);
		irsTABECompositeFactMapper.delete(irsTABECompositeFactData);
		conn.commit();
		assertNull(irsTABECompositeFactMapper.findByCompStudentSession(NON_EXISTING_COMPOSITEID, NON_EXISTING_STUDENTID, NON_EXISTING_SESSIONID));
	}	
 
	public void test14DeleteOrgNodeDim()throws SQLException{
		assertNotNull(irsOrgNodeDimMapper.findByOrgNodeIds(irsOrgNodeDimData));
		irsOrgNodeDimMapper.delete(NON_EXISTING_ORGNODEID);
		conn.commit();
		assertNull(irsOrgNodeDimMapper.findByOrgNodeIds(irsOrgNodeDimData));
	}	
	
	public void test15DeleteCustomerDim()throws SQLException{
		assertNotNull(irsCustomerDimMapper.findByCustomerId(NON_EXISTING_CUSTOMERID));
		irsCustomerDimMapper.delete(NON_EXISTING_CUSTOMERID);
		conn.commit();
		assertNull(irsCustomerDimMapper.findByCustomerId(NON_EXISTING_CUSTOMERID));
	}	
		 
	public void test16DeleteFormDim()throws SQLException{
		assertNotNull(irsFormDimMapper.findByFormId(NON_EXISTING_FORMID));
		irsFormDimMapper.delete(NON_EXISTING_FORMID);
		conn.commit();
		assertNull(irsFormDimMapper.findByFormId(NON_EXISTING_FORMID));
	}
		 
	public void test17DeleteLevelDim()throws SQLException{
		assertNotNull(irsLevelDimMapper.findByLevelId(NON_EXISTING_LEVELID));
		irsLevelDimMapper.delete(NON_EXISTING_LEVELID);
		conn.commit();
		assertNull(irsLevelDimMapper.findByLevelId(NON_EXISTING_LEVELID));
	}
		 
	public void test18DeleteProductDim()throws SQLException{
		assertNotNull(irsProductDimMapper.findByProductId(NON_EXISTING_PRODUCTID));
		irsProductDimMapper.delete(NON_EXISTING_PRODUCTID);
		conn.commit();
		assertNull(irsProductDimMapper.findByProductId(NON_EXISTING_PRODUCTID));		 
	}
		 
	public void test19DeleteAssessmentDim()throws SQLException{
		assertNotNull(irsAssessmentDimMapper.findByAssessmentId(NON_EXISTING_ASSESSMENTID));
		irsAssessmentDimMapper.deleteAssessmentDim(NON_EXISTING_ASSESSMENTID);
		conn.commit();
		assertNull(irsAssessmentDimMapper.findByAssessmentId(NON_EXISTING_ASSESSMENTID));		 
	}
		 
	public void test20DeleteCurrentResultDim()throws SQLException{
		assertNotNull(irsCurrentResultDimMapper.findByCurrentResultId(NON_EXISTING_CURRENTRESULTID));
		irsCurrentResultDimMapper.delete(NON_EXISTING_CURRENTRESULTID);
		conn.commit();
		assertNull(irsCurrentResultDimMapper.findByCurrentResultId(NON_EXISTING_CURRENTRESULTID));
	}	
		 
	public void test21DeleteStudentDim()throws SQLException{
		assertNotNull(irsStudentDimMapper.findByStudentId(NON_EXISTING_STUDENTID));
		irsStudentDimMapper.delete(NON_EXISTING_STUDENTID);
		conn.commit();
		assertNull(irsStudentDimMapper.findByStudentId(NON_EXISTING_STUDENTID));
	}
		 
	public void test22DeleteCompositeDim()throws SQLException{
		assertNotNull(irsCompositeDimMapper.findByCompositeDimId(NON_EXISTING_COMPOSITEID));
		irsCompositeDimMapper.delete(NON_EXISTING_COMPOSITEID);
		conn.commit();
		assertNull(irsCompositeDimMapper.findByCompositeDimId(NON_EXISTING_COMPOSITEID));
	}	
		 
	public void test23DeleteSubjectDim()throws SQLException{
		assertNotNull(irsSubjectDimMapper.findBySubjectId(NON_EXISTING_SUBJECTID));
		irsSubjectDimMapper.delete(NON_EXISTING_SUBJECTID);
		conn.commit();
		assertNull(irsSubjectDimMapper.findBySubjectId(NON_EXISTING_SUBJECTID));
	}
	
	protected void tearDown()throws SQLException{
		conn.close();
	}
}