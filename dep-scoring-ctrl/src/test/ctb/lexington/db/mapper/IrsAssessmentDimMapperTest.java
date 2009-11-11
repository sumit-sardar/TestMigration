package test.ctb.lexington.db.mapper;

import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.IrsAssessmentDimData;
import com.ctb.lexington.db.irsdata.IrsProductDimData;
import com.ctb.lexington.db.mapper.IrsAssessmentDimMapper;
import com.ctb.lexington.db.mapper.IrsProductDimMapper;


/**
 * @author Rama_Rao
 *
 */

public class IrsAssessmentDimMapperTest extends AbstractMapperTest{

	private IrsAssessmentDimMapper irsAssessmentDimMapper;
	private IrsAssessmentDimData irsAssessmentDimData;
	private IrsProductDimMapper irsProductDimMapper;
	private IrsProductDimData irsProductDimData;
	private static final Long NON_EXISTING_ASSESSMENT_ID = new Long(151515);
	private static final String NON_EXISTING_NAME = "TEST NATE";
	private static final Long NON_EXISTING_PRODUCT_ID = new Long(1313);
	private static final String NON_EXISTING_PRODUCT_NAME = "RAMA TABE";
	private static final Long NEW_PRODUCT_ID = new Long(6000);
	private static final String NEW_PRODUCT_NAME = "TEST RAMA";
	
	public IrsAssessmentDimMapperTest(String name){
		super(name);	 
	}
	
	protected void setUp()throws Exception{
        super.setUp();
        conn = getIRSConnection();
        irsAssessmentDimMapper = new IrsAssessmentDimMapper(conn);
        irsProductDimMapper = new IrsProductDimMapper(conn);
        irsAssessmentDimData = new IrsAssessmentDimData();
        irsProductDimData = new IrsProductDimData();
        irsAssessmentDimData.setAssessmentid(NON_EXISTING_ASSESSMENT_ID);
        irsAssessmentDimData.setName(NON_EXISTING_NAME);
        irsAssessmentDimData.setProductid(NON_EXISTING_PRODUCT_ID);
        irsProductDimData.setProductid(NON_EXISTING_PRODUCT_ID);
        irsProductDimData.setName(NON_EXISTING_PRODUCT_NAME);
    }
	
	public void testInsertingNonExistingProductDimRecord()throws SQLException{
		assertNull(irsProductDimMapper.findByProductId(NON_EXISTING_PRODUCT_ID));
		irsProductDimMapper.insert(irsProductDimData);
		assertNotNull(irsProductDimMapper.findByProductId(NON_EXISTING_PRODUCT_ID));
		conn.commit();
	}
	
	public void testInsertNonExistingAssessmentDimRecord()throws SQLException{
		assertNull(irsAssessmentDimMapper.findByAssessmentId(NON_EXISTING_ASSESSMENT_ID));
		irsAssessmentDimMapper.insert(irsAssessmentDimData);
		assertNotNull(irsAssessmentDimMapper.findByAssessmentId(NON_EXISTING_ASSESSMENT_ID));
		conn.commit();
   	}
	
	public void testUpdateProductDim()throws SQLException{
		assertNotNull(irsProductDimMapper.findByProductId(NON_EXISTING_PRODUCT_ID));
		IrsProductDimData record = irsProductDimMapper.findByProductId(NON_EXISTING_PRODUCT_ID);
		final String oldName = record.getName();		
		record.setName(NEW_PRODUCT_NAME);
			try{
				irsProductDimMapper.update(record);
				conn.commit();
				IrsProductDimData updatedRecord = irsProductDimMapper.findByProductId(NON_EXISTING_PRODUCT_ID);
				assertEquals("TEST RAMA",updatedRecord.getName());
				}finally{
					record.setName(oldName);
					irsProductDimMapper.update(record);
					conn.commit();
				}
	}
	
	public void testUpdateAssessmentDim()throws SQLException{
		assertNotNull(irsAssessmentDimMapper.findByAssessmentId(NON_EXISTING_ASSESSMENT_ID));
		IrsAssessmentDimData record = irsAssessmentDimMapper.findByAssessmentId(NON_EXISTING_ASSESSMENT_ID);
		final String oldName = record.getName();
		final Long oldProductId	= record.getProductid();
		record.setName(NEW_PRODUCT_NAME);
		record.setProductid(NEW_PRODUCT_ID);		
			try{
				irsAssessmentDimMapper.update(record);
				conn.commit();
				IrsAssessmentDimData updatedRecord = irsAssessmentDimMapper.findByAssessmentId(NON_EXISTING_ASSESSMENT_ID);
				assertNotEquals("updateTABE",updatedRecord.getName());
				}finally{
					record.setName(oldName);
					record.setProductid(oldProductId);
					irsAssessmentDimMapper.update(record);
					conn.commit();
				}
	}
	
	public void testDeleteProductDim()throws SQLException{
		assertNotNull(irsProductDimMapper.findByProductId(NON_EXISTING_PRODUCT_ID));
		irsProductDimMapper.delete(NON_EXISTING_PRODUCT_ID);
		conn.commit();
		assertNull(irsProductDimMapper.findByProductId(NON_EXISTING_PRODUCT_ID));
	}
	
	public void testDeleteAssessmentDim()throws SQLException{
		assertNotNull(irsAssessmentDimMapper.findByAssessmentId(NON_EXISTING_ASSESSMENT_ID));
		irsAssessmentDimMapper.deleteAssessmentDim(NON_EXISTING_ASSESSMENT_ID);
		conn.commit();
		assertNull(irsAssessmentDimMapper.findByAssessmentId(NON_EXISTING_ASSESSMENT_ID));
	}
	
	protected void tearDown()throws SQLException{
		conn.close();
	}	
}