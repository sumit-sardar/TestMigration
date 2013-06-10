package com.ctb.lexington.domain.score.scorer;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;

import javax.naming.NamingException;

import com.ctb.lexington.db.data.TestRosterRecord;
import com.ctb.lexington.db.mapper.TestRosterMapper;
import com.ctb.lexington.db.utils.DataSourceFactory;
import com.ctb.lexington.db.utils.DatabaseHelper;
import com.ctb.lexington.domain.score.ResponseReplayer;
import com.ctb.lexington.domain.score.event.AssessmentEndedEvent;
import com.ctb.lexington.domain.score.event.AssessmentStartedEvent;
import com.ctb.lexington.domain.teststructure.CompletionStatus;
import com.ctb.lexington.domain.teststructure.ProductType;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.exception.EventChannelException;
import com.ctb.lexington.util.SQLUtil;
import com.ctb.lexington.util.SafeHashMap;
import com.ibatis.sqlmap.client.SqlMapSession;

/**
 * This class contains a map of Scorer instances keyed off of a testRosterId. In case a Scorer does
 * not exist, it looks up the scorer instance type from an entry in the product table, creates an
 * instance, passing in the testRosterId.
 */
public class ScorerFactory {
    private static final Map SCORER_MAP = new SafeHashMap(Long.class, Scorer.class);
    private SqlMapSession iBatisSession;

    private ScorerFactory() {
        // so that no one instantiates this class
    }

    /**
     * Creates a new <code>Scorer</code> for the given <var>assessment </var> or
     * retrieves an existing one based on the test roster id. Instantiating a
     * new scorer will replay existing events on that scorer by reading them
     * from the database. Replay reads from two tables: <dl>
     * <dt>ITEM_RESPONSE</dt> <dd>Holds the item reponses</dd>
     * <dt>STUDENT_ITEM_SET_STATUS</dt> <dd>Holds the subtest status</dd> </dl>
     *
     * @param assessment the assessment started event
     */
    public static Scorer createScorer(final AssessmentStartedEvent assessment) {
        final Long testRosterId = assessment.getTestRosterId();

        if (!containsScorer(testRosterId)) {
            try {
                final Scorer scorer = instantiateScorer(assessment);
                System.out.println("***** SCORING: ScorerFactory: createScorer: creating new scorer");
                if (!(scorer instanceof DoNothingScorer)) {
                	System.out.println("***** SCORING: ScorerFactory: createScorer: creating a non-do-nothing scorer");
                    // Kick off the events, and replay existing ones
                    scorer.notify(assessment);
                    new ResponseReplayer(scorer).replaySubtests(assessment.getTestRosterId(), scorer);
                } else {
                	//setCompletionStatusForUnscoredTest(assessment.getTestRosterId());
                }

                putScorer(testRosterId, scorer);
            } catch (final Exception e) {
                rethrowAsEventChannelException(e);
            }
        }

        return getScorer(testRosterId);
    }

    /**
	 * @param testRosterId
     * @throws NamingException 
	 */
	private static void setCompletionStatusForUnscoredTest(Long testRosterId) throws NamingException {
		Connection conn = null;
        try {
            conn = getOASConnection();
            TestRosterMapper mapper = new TestRosterMapper(conn);
            TestRosterRecord roster = mapper.findTestRoster(testRosterId);
            CompletionStatus originalStatus = CompletionStatus.getByCode(roster
                    .getTestCompletionStatus());
            // do not update the roster to a completed status if it is currently interrupted.
            if (! (originalStatus.isInterrupted()))
                mapper.updateTestCompletionStatus(testRosterId, CompletionStatus.COMPLETED, new Timestamp(System.currentTimeMillis()));
            else {
            	System.out.println("Not updating roster status from: " + originalStatus.getCode()
                        + " to: " + CompletionStatus.COMPLETED.getCode());
            }
        } catch (SQLException e) {
            throw new EventChannelException(e);
        } finally {
            try {
                close(false, conn);
            } catch (SQLException e) {
                throw new EventChannelException(e);
            }
        }
		
	}
	
    public static final Connection getOASConnection() throws SQLException, NamingException {
        return getConnection(DataSourceFactory.OASDATASOURCE);
    }
    
    public static final Connection getIRSConnection() throws SQLException, NamingException {
        return getConnection(DataSourceFactory.IRSDATASOURCE);
    }

    private static Connection getConnection(String name) throws SQLException, NamingException {
        final Connection connection = DataSourceFactory.getInstance().getDataSource(name).getConnection();
        return connection;
    }
    
    public static void close(boolean rollback, Connection connection) throws SQLException {
        try {
                if (rollback) {
                    connection.rollback();
                } else {
                    connection.commit();
                }
            } finally {
                SQLUtil.close(connection);
            }
    }

	/**
     * Releases the scorer for the given <var>assessment </var> based on test roster id.
     *
     * @param testRosterId test roster id
     */
    public static void releaseFailedScorer(final Long testRosterId) {
        Scorer scorer = null;
    	try {
	    	if (containsScorer(testRosterId)) {
	            scorer = removeScorer(testRosterId);
	            scorer.forceCloseAllConnections(true);
	    	}
        } finally {
        	// clean up the iBatis session
        	if(scorer != null && scorer.getIbatisSession() != null)
        		scorer.getIbatisSession().close();
        }
    }

    /**
     * Releases the scorer for the given <var>assessment </var> based on test roster id.
     *
     * @param assessment the assessment ended event
     */
    public static void releaseScorer(final AssessmentEndedEvent assessment, final boolean updateContextData) {
    	Scorer scorer = null;
    	try {
            final Long testRosterId = assessment.getTestRosterId();

            if (containsScorer(testRosterId)) {
                // Finish off the last event
                scorer = removeScorer(testRosterId);
                if(scorer != null && scorer.getResultHolder() != null) {
                	scorer.getResultHolder().setUpdateContextData(updateContextData);
                }
                scorer.notify(assessment);
                System.out.println("***** SCORING: ScorerFactory: releaseScorer: finished persistence");
                scorer.forceCloseAllConnections(false);
            }
        } catch (final Exception e) {
        	//e.printStackTrace();
            rethrowAsEventChannelException(e);
        } finally {
        	// clean up the iBatis session
        	if(scorer != null && scorer.getIbatisSession() != null)
        		scorer.getIbatisSession().close();
        }
    }

    /**
     * Releases the scorer for the given <var>testRosterId </var>.
     *
     * @param testRosterId the test roster id
     */
    public static void releaseScorer(final Long testRosterId, final boolean updateContextData) {
        releaseScorer(new AssessmentEndedEvent(testRosterId), updateContextData);
    }

    /**
     * Plays the scoring events for this scoring conversation, injecting them into the channel for a
     * scorer created temporarily for this purpose, and running in a background thread. It
     * recapitulates the common idiom:
     *
     * <pre>
     * createScorer(testRosterId, productType);
     * releaseScorer(testRosterId);
     * </pre>
     *
     * @param testRosterId the test roster id from the selected student
     * @return the background scoring thread
     */
    public static Thread scoreWithEventsInBackground(final Long testRosterId,
            final Integer productId, final String productType, final String grade,
			final boolean updateContextData, final boolean performMatching) {
        final Thread scoringThread = new Thread() {
            public void run() {
                try {
                     // replays events
                    Scorer scorer = createScorer(new AssessmentStartedEvent(testRosterId, productId, productType, grade));
                    if(scorer != null && scorer.getResultHolder() != null) {
                    	scorer.getResultHolder().getStudentData().setPerformMatching(performMatching);
                    }
                    releaseScorer(testRosterId, updateContextData);
                } catch (Exception e) {
                    releaseFailedScorer(testRosterId);
                    throw new RuntimeException(e);
                }
            }
        };

        scoringThread.start();

        return scoringThread;
    }
    
    public static void scoreWithEventsInForeground(final Long testRosterId,
            final Integer productId, final String productType, final String grade,
			final boolean updateContextData, final boolean performMatching) {
        try {
            Scorer scorer = createScorer(new AssessmentStartedEvent(testRosterId, productId, productType, grade)); // replays events
            scorer.getResultHolder().getStudentData().setPerformMatching(performMatching);
            System.out.println("***** SCORING: ScorerFactory: scoreWithEventsInForeground: finished event replay");
            releaseScorer(testRosterId, updateContextData);
            System.out.println("***** SCORING: ScorerFactory: scoreWithEventsInForeground: finished persistence");
        } catch (Exception e) {
            releaseFailedScorer(testRosterId);
            throw new RuntimeException(e);
        }
    }

    private static Scorer instantiateScorer(final AssessmentStartedEvent assessment)
            throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        try {
        	System.out.println("*** PRODUCT TYPE :: "+assessment.getProductTypeValue()+" && PRODUCT ID :: "+assessment.getProductId());
        	if("LL".equalsIgnoreCase(assessment.getProductTypeValue()) && !(assessment.getProductId().intValue() == 7001 
        			|| assessment.getProductId().intValue() == 7002 || assessment.getProductId().intValue() == 7003)) { // Added for laslink second edition
        		Class[] cls = new Class[1];
            	cls[0] = assessment.getProductId().getClass();
            	Integer[] inte = new Integer[1];
            	inte[0] = assessment.getProductId();
            	
                return (Scorer) getScorerClass(assessment.getProductType())
                        .getConstructor(cls).newInstance(inte);
        	} else {
        		return (Scorer) getScorerClass(assessment.getProductType())
                .getConstructor(null).newInstance(null);
        	}
            
        } catch (ClassNotFoundException e) {
            // TODO: what is with the know-nothing scorer? --bko
            // If the scorer class could not be found, default to a no-op implementation
            return new DoNothingScorer();
        }
    }
    
    public static void invokeScoring(Integer testRosterId, boolean runInBackGround, boolean updateContextData) throws CTBSystemException, NamingException {
    	invokeScoring(testRosterId, runInBackGround, updateContextData, true);
    }
    
    public static void invokeScoring(Integer testRosterId, boolean runInBackGround, boolean updateContextData, boolean performMatching) throws CTBSystemException, NamingException {
    	System.out.println("***** SCORING: ScorerFactory: invokeScoring: called for roster: " + testRosterId);
    	Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Integer productId = null;
        String productType = null;
        String studentGrade = null;
        try {
            final String sql =
            "select " +
                "tr.test_roster_id, " +
                "tr.test_admin_id, " +
                "stu.grade, " +
                "prod.product_id, " +
                "prod.product_type " +
            "from  " +
                "test_roster tr, " +
                "student stu, " +
                "product prod, " +
                "item_set_product isetp, " +
                "test_admin admin " +
            "where " +
                "stu.student_id = tr.student_id and " +
                "admin.test_admin_id = tr.test_admin_id and " +
                "isetp.item_set_id = admin.ITEM_SET_ID and " +
                "prod.product_id = isetp.product_id and " +
                "tr.test_roster_id = ? ";

            conn = DataSourceFactory.getInstance().getDataSource(DataSourceFactory.OASDATASOURCE).getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, testRosterId.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
            	System.out.println("***** SCORING: ScorerFactory: invokeScoring: found roster info. Admin id: " + SQLUtil.getInteger(rs, "TEST_ADMIN_ID"));
                productId = SQLUtil.getInteger(rs, "PRODUCT_ID");
                productType = SQLUtil.getString(rs, "PRODUCT_TYPE");
                studentGrade = SQLUtil.getString(rs, "GRADE");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CTBSystemException(e.getMessage());
        } finally {
            SQLUtil.close(rs, ps);
            SQLUtil.close(conn);
        }
        if(productId != null) {
	        if (runInBackGround) {
	        	System.out.println("***** SCORING: ScorerFactory: invokeScoring: scoring in background");
	            scoreWithEventsInBackground(DatabaseHelper.asLong(testRosterId), productId, productType, studentGrade, updateContextData, performMatching);
	        } else {
	        	System.out.println("***** SCORING: ScorerFactory: invokeScoring: scoring in foreground");
	            scoreWithEventsInForeground(DatabaseHelper.asLong(testRosterId), productId, productType, studentGrade, updateContextData, performMatching);
	        }
        }else {
        	System.out.println("ScorerFactory.invokeScoring(testRosterId, runInBackGround): no product info for roster: " + testRosterId);
        }
    }

    private static Class getScorerClass(final ProductType productType) throws ClassNotFoundException {
        return ScorerFactory.class.getClassLoader().loadClass(getScorerClassName(productType));
    }

    private static String getScorerClassName(final ProductType productType) {
        String productTypeCode = productType == null ? null : productType.getCode();
        return "com.ctb.lexington.domain.score.scorer." + productTypeCode + "Scorer";
    }

    private static boolean containsScorer(final Long testRosterId) {
        return SCORER_MAP.containsKey(testRosterId);
    }

    private static Scorer getScorer(final Long testRosterId) {
        return (Scorer) SCORER_MAP.get(testRosterId);
    }

    private static void putScorer(final Long testRosterId, final Scorer scorer) {
        SCORER_MAP.put(testRosterId, scorer);
    }

    private static Scorer removeScorer(final Long testRosterId) {
        return (Scorer) SCORER_MAP.remove(testRosterId);
    }

    private static void rethrowAsEventChannelException(final Exception e) {
        // Only wrap if not already a event channel exception
        throw e instanceof EventChannelException ? (EventChannelException) e
                : new EventChannelException(e);
    }
}