package test.ctb.lexington.db.mapper;


import java.sql.SQLException;
import java.util.Date;
import com.ctb.lexington.db.irsdata.irstbdata.IrsTABEContentAreaFactData;
import com.ctb.lexington.db.mapper.tbmapper.IrsTABEContentAreaFactMapper;
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
import com.ctb.lexington.db.irsdata.IrsContentAreaDimData;
import com.ctb.lexington.db.mapper.IrsContentAreaDimMapper;
import com.ctb.lexington.db.irsdata.IrsCustomerDimData;
import com.ctb.lexington.db.mapper.IrsCustomerDimMapper;

/**
 * @author Rama_Rao
 *
 */

public class IrsTABEContentAreaFactMapperTest extends AbstractMapperTest{

	private IrsTABEContentAreaFactData irsTABEContentAreaFactData;
	private IrsTABEContentAreaFactMapper irsTABEContentAreaFactMapper;
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
	private IrsContentAreaDimMapper irsContentAreaDimMapper;
	private IrsContentAreaDimData irsContentAreaDimData;
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
	private static final Date NON_EXISTING_TESTCOMPLITIONTIMESTAMP = createDate(2006,12,3);
	private static final Long NON_EXISTING_PERCENTOBTAINED = new Long(85);
	private static final Long NON_EXISTING_CONTENTAREAID = new Long(9999);
	private static final Long NON_EXISTING_PERCENTAGEMASTERY =  new Long(99);
	private static final Long NON_EXISTING_CUSTOMERID =  new Long(3);
	
	public IrsTABEContentAreaFactMapperTest(String name){
		super(name);	 
	}
	
	protected void setUp()throws Exception{
        super.setUp();
        conn = getIRSConnection();
        irsTABEContentAreaFactMapper = new IrsTABEContentAreaFactMapper(conn);
        irsTABEContentAreaFactData = new IrsTABEContentAreaFactData();
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
        irsContentAreaDimMapper = new IrsContentAreaDimMapper(conn);
        irsContentAreaDimData = new IrsContentAreaDimData();
        irsCustomerDimMapper = new IrsCustomerDimMapper(conn);
        irsCustomerDimData = new IrsCustomerDimData();
        
        irsStudentDimData.setStudentid(NON_EXISTING_STUDENTID);
        irsOrgNodeDimData.setOrgNodeid(NON_EXISTING_ORGNODEID);
        irsOrgNodeDimData.setCustomerid(NON_EXISTING_CUSTOMERID);
        irsOrgNodeDimData.setNumLevels(new Long(5));
        irsFormDimData.setFormid(NON_EXISTING_FORMID);
        irsLevelDimData.setLevelid(NON_EXISTING_LEVELID);
        irsLevelDimData.setName("Z");
        irsContentAreaDimData.setContentAreaid(NON_EXISTING_CONTENTAREAID);
        irsTABEContentAreaFactData.setContentAreaid(NON_EXISTING_CONTENTAREAID);
        irsProductDimData.setProductid(NON_EXISTING_PRODUCTID);
        irsAssessmentDimData.setAssessmentid(NON_EXISTING_ASSESSMENTID);
        irsCurrentResultDimData.setCurrentResultid(NON_EXISTING_CURRENTRESULTID);
        irsCustomerDimData.setCustomerid(NON_EXISTING_CUSTOMERID);
        
        irsTABEContentAreaFactData.setFactid(NON_EXISTING_FACTID);
        irsTABEContentAreaFactData.setStudentid(NON_EXISTING_STUDENTID);
        irsTABEContentAreaFactData.setFormid(NON_EXISTING_FORMID);
        irsTABEContentAreaFactData.setSessionid(NON_EXISTING_SESSIONID);
        irsTABEContentAreaFactData.setLevelid(NON_EXISTING_LEVELID);
        irsTABEContentAreaFactData.setOrgNodeid(NON_EXISTING_ORGNODEID);
        irsTABEContentAreaFactData.setAssessmentid(NON_EXISTING_ASSESSMENTID);
        irsTABEContentAreaFactData.setCurrentResultid(NON_EXISTING_CURRENTRESULTID);
        irsTABEContentAreaFactData.setTestCompletionTimestamp(NON_EXISTING_TESTCOMPLITIONTIMESTAMP);
        irsTABEContentAreaFactData.setPointsObtained(NON_EXISTING_POINTSOBTAINED);
        irsTABEContentAreaFactData.setPointsPossible(NON_EXISTING_POINTSPOSSIBLE);
        irsTABEContentAreaFactData.setPercentObtained(NON_EXISTING_PERCENTOBTAINED);
        irsTABEContentAreaFactData.setPercentageMastery(NON_EXISTING_PERCENTAGEMASTERY);
        irsTABEContentAreaFactData.setPercentObtained(NON_EXISTING_PERCENTOBTAINED);
        
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
	
	public void test9InsertNonExistingRecordContentAreaDim()throws Exception{
		assertNull(irsContentAreaDimMapper.findByContentAreaId(NON_EXISTING_CONTENTAREAID));
		irsContentAreaDimMapper.insert(irsContentAreaDimData);
		assertNotNull(irsContentAreaDimMapper.findByContentAreaId(NON_EXISTING_CONTENTAREAID));
		conn.commit();
	}
	
	public void test10InsertNonExistingRecordTABEContentAreaFact()throws SQLException{
		assertNull(irsTABEContentAreaFactMapper.findByCAStudentSession(NON_EXISTING_CONTENTAREAID, NON_EXISTING_STUDENTID, NON_EXISTING_SESSIONID));
		irsTABEContentAreaFactMapper.insert(irsTABEContentAreaFactData);
		conn.commit();
		assertNotNull(irsTABEContentAreaFactMapper.findByCAStudentSession(NON_EXISTING_CONTENTAREAID, NON_EXISTING_STUDENTID, NON_EXISTING_SESSIONID));
	}
		 
	public void test11UpdateTABEContentAreaFact()throws SQLException{
		assertNotNull(irsTABEContentAreaFactMapper.findByCAStudentSession(NON_EXISTING_CONTENTAREAID, NON_EXISTING_STUDENTID, NON_EXISTING_SESSIONID));
		IrsTABEContentAreaFactData record = irsTABEContentAreaFactMapper.findByCAStudentSession(NON_EXISTING_CONTENTAREAID, NON_EXISTING_STUDENTID, NON_EXISTING_SESSIONID);
		assertNotNull(record);
		final Long oldFormId = record.getFormid();
		final Long oldLevelId = record.getLevelid();
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
		final Float oldGradeEquivalent = record.getGradeEquivalent();
		final Long oldPredictedGED = record.getPredictedGed();
		record.setFormid(NON_EXISTING_FORMID);
		record.setLevelid(NON_EXISTING_LEVELID);
		record.setPointsObtained(new Long(55));
		record.setTestCompletionTimestamp(createDate(2007,1,1));
		record.setOrgNodeid(NON_EXISTING_ORGNODEID);
		record.setAssessmentid(NON_EXISTING_ASSESSMENTID);
		record.setPointsPossible(new Long(66));
		record.setCurrentResultid(NON_EXISTING_CURRENTRESULTID);
		record.setScaleScore(new Long(999));
		record.setNormalCurveEquivalent(new Long(9));
		record.setNationalStanine(new Long(99));
		record.setNationalPercentile(new Long(99));
		record.setGradeEquivalent(new Float(9));
		record.setPredictedGed(new Long(9));
		try{
			irsTABEContentAreaFactMapper.update(record);
			conn.commit();
			IrsTABEContentAreaFactData updatedRecord = irsTABEContentAreaFactMapper.findByCAStudentSession(NON_EXISTING_CONTENTAREAID, NON_EXISTING_STUDENTID, NON_EXISTING_SESSIONID);
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
				record.setScaleScore(oldScaleScore);
				record.setNormalCurveEquivalent(oldNormalCurveEquivalent);
				record.setNationalStanine(oldNationalStanine);
				record.setNationalPercentile(oldNationalPercentile);
				record.setGradeEquivalent(oldGradeEquivalent);
				record.setPredictedGed(oldPredictedGED);
				irsTABEContentAreaFactMapper.update(record);
				conn.commit();
			}	
	}
	
	public void test12DeleteTABEContentAreaFact()throws SQLException{
		assertNotNull(irsTABEContentAreaFactMapper.findByCAStudentSession(NON_EXISTING_CONTENTAREAID, NON_EXISTING_STUDENTID, NON_EXISTING_SESSIONID));
		irsTABEContentAreaFactData = irsTABEContentAreaFactMapper.findByCAStudentSession(NON_EXISTING_CONTENTAREAID, NON_EXISTING_STUDENTID, NON_EXISTING_SESSIONID);
		irsTABEContentAreaFactMapper.delete(irsTABEContentAreaFactData);
		conn.commit();
		assertNull(irsTABEContentAreaFactMapper.findByCAStudentSession(NON_EXISTING_CONTENTAREAID, NON_EXISTING_STUDENTID, NON_EXISTING_SESSIONID));
	}	
		
	public void test23DeleteOrgNodeDim()throws SQLException{
		assertNotNull(irsOrgNodeDimMapper.findByOrgNodeIds(irsOrgNodeDimData));
		irsOrgNodeDimMapper.delete(NON_EXISTING_ORGNODEID);
		conn.commit();
		assertNull(irsOrgNodeDimMapper.findByOrgNodeIds(irsOrgNodeDimData));
	}	
		
	public void test24DeleteCustomerDim()throws SQLException{
		assertNotNull(irsCustomerDimMapper.findByCustomerId(NON_EXISTING_CUSTOMERID));
		irsCustomerDimMapper.delete(NON_EXISTING_CUSTOMERID);
		conn.commit();
		assertNull(irsCustomerDimMapper.findByCustomerId(NON_EXISTING_CUSTOMERID));
	}	
	
	public void test25DeleteFormDim()throws SQLException{
		assertNotNull(irsFormDimMapper.findByFormId(NON_EXISTING_FORMID));
		irsFormDimMapper.delete(NON_EXISTING_FORMID);
		conn.commit();
		assertNull(irsFormDimMapper.findByFormId(NON_EXISTING_FORMID));
	}	
		
	public void test26DeleteLevelDim()throws SQLException{
		assertNotNull(irsLevelDimMapper.findByLevelId(NON_EXISTING_LEVELID));
		irsLevelDimMapper.delete(NON_EXISTING_LEVELID);
		conn.commit();
		assertNull(irsLevelDimMapper.findByLevelId(NON_EXISTING_LEVELID));
	}
	
	public void test27DeleteProductDim()throws SQLException{
		assertNotNull(irsProductDimMapper.findByProductId(NON_EXISTING_PRODUCTID));
		irsProductDimMapper.delete(NON_EXISTING_PRODUCTID);
		conn.commit();
		assertNull(irsProductDimMapper.findByProductId(NON_EXISTING_PRODUCTID));
	}	
	
	public void test28DeleteAssessmentDim()throws SQLException{
		assertNotNull(irsAssessmentDimMapper.findByAssessmentId(NON_EXISTING_ASSESSMENTID));
		irsAssessmentDimMapper.deleteAssessmentDim(NON_EXISTING_ASSESSMENTID);
		conn.commit();
		assertNull(irsAssessmentDimMapper.findByAssessmentId(NON_EXISTING_ASSESSMENTID));
	}
	
	public void test29DeleteCurrentResultDim()throws SQLException{
		assertNotNull(irsCurrentResultDimMapper.findByCurrentResultId(NON_EXISTING_CURRENTRESULTID));
		irsCurrentResultDimMapper.delete(NON_EXISTING_CURRENTRESULTID);
		conn.commit();
		assertNull(irsCurrentResultDimMapper.findByCurrentResultId(NON_EXISTING_CURRENTRESULTID));
	}
	
	public void test30DeleteStudentDim()throws SQLException{
		assertNotNull(irsStudentDimMapper.findByStudentId(NON_EXISTING_STUDENTID));
		irsStudentDimMapper.delete(NON_EXISTING_STUDENTID);
		conn.commit();
		assertNull(irsStudentDimMapper.findByStudentId(NON_EXISTING_STUDENTID));
	}
	
	public void test31DeleteContentAreaDim()throws SQLException{
		assertNotNull(irsContentAreaDimMapper.findByContentAreaId(NON_EXISTING_CONTENTAREAID));
		irsContentAreaDimMapper.delete(NON_EXISTING_CONTENTAREAID);
		conn.commit();
		assertNull(irsContentAreaDimMapper.findByContentAreaId(NON_EXISTING_CONTENTAREAID));
	}
	
	protected void tearDown()throws SQLException{
		conn.close();
	}
}