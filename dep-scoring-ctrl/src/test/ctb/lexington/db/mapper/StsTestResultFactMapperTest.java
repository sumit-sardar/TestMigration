package test.ctb.lexington.db.mapper;

import java.sql.SQLException;

import test.ctb.lexington.db.mapper.AbstractMapperTest;

import com.ctb.lexington.db.data.StsTestResultFactDetails;
import com.ctb.lexington.db.mapper.StsTestResultFactRecordMapper;

	public class StsTestResultFactMapperTest extends AbstractMapperTest {
	    private static final Long EXISTING_TEST_RESULT_FACT_ID = new Long(4967469);
	    private static final Long NON_EXISTING_STUDENT_TEST_HISTORY_ID = new Long(99999);
	    private static final Long STUDENT_DIM_ID = new Long(1455264);
	    private static final Long ADMIN_DIM_ID = new Long(526);
	    private static final String CONTENT_AREA_NAME = "Reading";
	    private static final Long NON_EXISTING_STUDENT_DIM_ID = new Long(99999);

	    private StsTestResultFactRecordMapper mapper;

	    /**
	     * Constructor for StsTestResultFactMapperTest.
	     * 
	     * @param name
	     */
	    public StsTestResultFactMapperTest(String name) {
	        super(name);
	    }

	    protected void setUp() throws Exception {
	        super.setUp();
	        conn = getIRSConnection();
	        mapper = new StsTestResultFactRecordMapper(conn);
	    }

	    public void testFind() throws Exception {
	        assertNotNull(mapper.find(EXISTING_TEST_RESULT_FACT_ID));
	        assertNull(mapper.find(NON_EXISTING_STUDENT_TEST_HISTORY_ID));
	    }

	    public void testFindByStudentAdminTest() throws Exception {
	        StsTestResultFactDetails data = mapper.findByStudentAdminTest(STUDENT_DIM_ID, ADMIN_DIM_ID,
	                CONTENT_AREA_NAME);
	        assertNotNull(data);
	        assertEquals(EXISTING_TEST_RESULT_FACT_ID, data.getFactId());
	        assertNull(mapper.findByStudentAdminTest(NON_EXISTING_STUDENT_DIM_ID, ADMIN_DIM_ID,
	        		CONTENT_AREA_NAME));
	    }

	    public void testUpsert() throws Exception {
	        StsTestResultFactDetails record = mapper.find(EXISTING_TEST_RESULT_FACT_ID);

	        assertNotNull("Record not found: " + EXISTING_TEST_RESULT_FACT_ID, record);

	        record.setFactId(NON_EXISTING_STUDENT_TEST_HISTORY_ID);
	        record.setStudentDimId(NON_EXISTING_STUDENT_DIM_ID);
	        try {
	            mapper.upsert(record, true);
	            assertNotNull(record.getFactId());
	        } finally {
	            delete(record);
	        }
	    }

	    private void delete(StsTestResultFactDetails record) throws SQLException {
	        final String sql = "DELETE FROM STS_TEST_RESULT_FACT WHERE FACT_ID = " + record.getFactId();
	        executeAndCommitSql(sql);
	    }
	}