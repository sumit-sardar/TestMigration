package com.ctb.lexington.domain.score.scorer;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import com.ctb.lexington.data.ItemVO;
import com.ctb.lexington.db.data.CurriculumData;
import com.ctb.lexington.db.data.ReportingLevels;
import com.ctb.lexington.db.data.ScoreMoveData;
import com.ctb.lexington.db.data.StsTestResultFactData;
import com.ctb.lexington.db.data.StsTestResultFactDetails;
import com.ctb.lexington.db.data.StudentItemScoreData;
import com.ctb.lexington.db.data.StudentItemScoreDetails;
import com.ctb.lexington.db.data.StudentScoreSummaryData;
import com.ctb.lexington.db.data.StudentScoreSummaryDetails;
import com.ctb.lexington.db.data.TestRosterRecord;
import com.ctb.lexington.db.data.CurriculumData.ContentArea;
import com.ctb.lexington.db.data.CurriculumData.PrimaryObjective;
import com.ctb.lexington.db.mapper.AbstractDBMapper;
import com.ctb.lexington.db.mapper.ObjectiveMapper;
import com.ctb.lexington.db.mapper.TestRosterMapper;
import com.ctb.lexington.db.utils.DataSourceFactory;
import com.ctb.lexington.db.utils.DatabaseHelper;
import com.ctb.lexington.domain.score.collector.TestResultDataCollector;
import com.ctb.lexington.domain.score.controller.TestResultController;
import com.ctb.lexington.domain.score.controller.llcontroller.LLTestResultController;
import com.ctb.lexington.domain.score.controller.tbcontroller.TBTestResultController;
import com.ctb.lexington.domain.score.controller.tvcontroller.TVTestResultController;
import com.ctb.lexington.domain.score.event.AssessmentEndedEvent;
import com.ctb.lexington.domain.score.event.AssessmentStartedEvent;
import com.ctb.lexington.domain.score.event.ContentAreaRawScoreEvent;
import com.ctb.lexington.domain.score.event.Objective;
import com.ctb.lexington.domain.score.event.ObjectivePrimaryCumulativeNumberCorrectEvent;
import com.ctb.lexington.domain.score.event.ObjectivePrimaryNumberCorrectEvent;
import com.ctb.lexington.domain.score.event.ObjectiveRawScoreEvent;
import com.ctb.lexington.domain.score.event.PointEvent;
import com.ctb.lexington.domain.score.event.ResponseReceivedEvent;
import com.ctb.lexington.domain.score.event.ScoringStatusEvent;
import com.ctb.lexington.domain.score.event.SubtestInvalidEvent;
import com.ctb.lexington.domain.score.event.SubtestItemCollectionEvent;
import com.ctb.lexington.domain.score.event.SubtestObjectiveCollectionEvent;
import com.ctb.lexington.domain.score.event.SubtestValidEvent;
import com.ctb.lexington.domain.score.event.TestCompletionStatusEvent;
import com.ctb.lexington.domain.score.event.common.Event;
import com.ctb.lexington.domain.score.event.common.EventChannel;
import com.ctb.lexington.domain.score.event.common.EventProcessor;
import com.ctb.lexington.domain.score.scorer.calculator.Calculator;
import com.ctb.lexington.domain.score.scorer.calculator.ContentAreaRawScoreCalculator;
import com.ctb.lexington.domain.score.scorer.calculator.ObjectiveItemCollectionCalculator;
import com.ctb.lexington.domain.score.scorer.calculator.ObjectiveNumberCorrectCalculator;
import com.ctb.lexington.domain.score.scorer.calculator.ObjectiveRawScoreCalculator;
import com.ctb.lexington.domain.score.scorer.calculator.ResponseCorrectCalculator;
import com.ctb.lexington.domain.score.scorer.calculator.ResponsePointCalculator;
import com.ctb.lexington.domain.score.scorer.calculator.ScoringStatusCalculator;
import com.ctb.lexington.domain.score.scorer.calculator.SubtestContentAreaItemCollectionCalculator;
import com.ctb.lexington.domain.score.scorer.calculator.SubtestItemCollectionCalculator;
import com.ctb.lexington.domain.score.scorer.calculator.SubtestValidationCalculator;
import com.ctb.lexington.domain.score.scorer.calculator.TestCompletionStatusCalculator;
import com.ctb.lexington.domain.teststructure.CompletionStatus;
import com.ctb.lexington.domain.teststructure.MasteryLevel;
import com.ctb.lexington.domain.teststructure.ScoringStatus;
import com.ctb.lexington.domain.teststructure.ValidationStatus;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.exception.EventChannelException;
import com.ctb.lexington.util.CTBConstants;
import com.ctb.lexington.util.SQLUtil;
import com.ctb.lexington.util.Timer;
import com.ibatis.sqlmap.client.SqlMapSession;

public abstract class BaseScorer extends EventProcessor implements Scorer {
    private List calculators;
    private HashSet connections;
    protected EventChannel channel;
    protected ScoreMoveData resultHolder;
    private boolean hasAssessmentEndedEvent = false;
    private SqlMapSession iBatisSession;
    private SubtestObjectiveCollectionEvent subtestObjectives = null;
    public SqlMapSession getIbatisSession() {
    	return this.iBatisSession;
    }
    
    public BaseScorer() throws CTBSystemException, IOException {
        super();

        // create new iBatis session for this scorer instance
        this.iBatisSession = AbstractDBMapper.getNewSqlMapSession();
        
        channel = new EventChannel();
        calculators = new ArrayList();
        connections = new HashSet();

        //addCalculator(new DebugCalculator(channel, this));
        
        addCalculator(new SubtestItemCollectionCalculator(channel, this));
        addCalculator(new ObjectiveItemCollectionCalculator(channel, this));

        addCalculator(new ResponseCorrectCalculator(channel, this));
        addCalculator(new ResponsePointCalculator(channel, this));

        addCalculator(new ObjectiveNumberCorrectCalculator(channel, this));
        addCalculator(new ObjectiveRawScoreCalculator(channel, this));

        addCalculator(new ScoringStatusCalculator(channel, this));
        addCalculator(new TestCompletionStatusCalculator(channel, this));

        addCalculator(new SubtestContentAreaItemCollectionCalculator(channel, this));
        addCalculator(new ContentAreaRawScoreCalculator(channel, this));
        
        addCalculator(new SubtestValidationCalculator(channel, this));

        // Gather item/obj/subtest context for resultHolder
        // TODO: Rather than listenting to these context events,
        // it may be better to get this info in the collectors
        channel.subscribe(this, SubtestItemCollectionEvent.class);
        channel.subscribe(this, SubtestObjectiveCollectionEvent.class);
        

        // place subtest scores into result holder
        channel.subscribe(this, ContentAreaRawScoreEvent.class);

        // objective scores into result holder
        channel.subscribe(this, ObjectivePrimaryNumberCorrectEvent.class);
        channel.subscribe(this, ObjectiveRawScoreEvent.class);

        // place item scores into result holder
        channel.subscribe(this, ResponseReceivedEvent.class);
        channel.subscribe(this, PointEvent.class);

        channel.subscribe(this, ScoringStatusEvent.class);
        channel.subscribe(this, TestCompletionStatusEvent.class);
        
        channel.subscribe(this, SubtestValidEvent.class);
        channel.subscribe(this, SubtestInvalidEvent.class);
    }

    protected void addCalculator(Calculator calc) {
        calculators.add(calc);
    }

    // TODO: Should use overloaded methods instead of instanceof
    public void notify(Event event) throws CTBSystemException {
        final Long testRosterId = event.getTestRosterId();
        try {
            handleAssessmentStartedEvent(event);//see todo

            // if the event is for a different testRosterId, error out
            if (!resultHolder.getTestRosterId().equals(testRosterId)) {
                throw new IllegalStateException(event.getClass().getName()
                        + " received for a different roster; expected "
                        + resultHolder.getTestRosterId() + " but got " + testRosterId);
            }

            channel.send(event);
            handleAssessmentEndedEvent(event);//see todo
        } catch (RuntimeException e) {
            updateScoringStatusToError(e,testRosterId);
            throw e;
        } catch (CTBSystemException e) {
            updateScoringStatusToError(e,testRosterId);
            throw e;
        }
    }

    private void updateScoringStatusToError(Exception e, Long testRosterId) {
        // do nothing
    }

    private String getStackTrace(Exception e) {
        final StringWriter out = new StringWriter();
        e.printStackTrace(new PrintWriter(out));
        for (Throwable next = e.getCause(); next != null; next = next.getCause()) {
            next.printStackTrace(new PrintWriter(out));
        }
        return out.toString();
    }

    private void handleAssessmentStartedEvent(Event event) throws CTBSystemException {
    	//System.out.println("***** SCORING: BaseScorer: handleAssessmentStartedEvent");
        if (resultHolder == null) {
            if (event instanceof AssessmentStartedEvent) {
                try {
                    final Long testRosterId = event.getTestRosterId();
                    //updateScoringStatus(testRosterId, ScoringStatus.IN_PROGRESS);
                    //System.out.println("***** SCORING: BaseScorer: handleAssessmentStartedEvent: set roster status to IP");
                    TestResultDataCollector collector = new TestResultDataCollector();
                    System.out.println("***** SCORING: BaseScorer: handleAssessmentStartedEvent: collecting context");
                    resultHolder = collector.collect(testRosterId, this);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new CTBSystemException("9300", "Error collecting test result data", e);
                } finally {
                    forceCloseAllConnections(true);
                }
            } else {
                throw new IllegalStateException(event.getClass().getName()
                        + " received without an assessment started event.");
            }
        } else if (event instanceof AssessmentStartedEvent) {
            throw new IllegalStateException(
                    "Received AssessmentStartedEvent twice: originally for testRosterId: "
                            + resultHolder.getTestRosterId() + " and a new one: " + event);
        }
    }

    private void handleAssessmentEndedEvent(Event event) throws CTBSystemException {
        // TODO: There's no unit test for the persistence behavior on the scorers yet
        if (event instanceof AssessmentEndedEvent) {
        	System.out.println("***** SCORING: BaseScorer: handleAssessmentEndedEvent: starting persistence");
            Timer assesmentEndedTime = Timer.startTimer();
            // if we have already processed an AssessmentEndedEvent, error out
            if (hasAssessmentEndedEvent) {
                throw new IllegalStateException(
                        "Received AssessmentEndedEvent twice: originally for testRosterId: "
                                + resultHolder.getTestRosterId() + " and a new one: " + event);
            }

            try {
                TestResultController controller = null;
                if(this instanceof TBScorer || this instanceof TLScorer) {
                    controller = new TBTestResultController(getIRSConnection(), resultHolder, getReportingLevels(event.getTestRosterId()));
                    controller.run(getRosterValidationStatus(event.getTestRosterId()));

                } else if(this instanceof TVScorer) {
                    controller = new TVTestResultController(getIRSConnection(), resultHolder, getReportingLevels(event.getTestRosterId()));
                    controller.run(getRosterValidationStatus(event.getTestRosterId()));
                }
                 //START-  For Laslink Scoring
                else if(this instanceof LLScorer) {
                    controller = new LLTestResultController(getIRSConnection(), resultHolder, getReportingLevels(event.getTestRosterId()));
                    controller.run(getRosterValidationStatus(event.getTestRosterId()));
                }
                 //END- For Laslink Scoring
                System.out.println("***** SCORING: BaseScorer: handleAssessmentEndedEvent: finished persistence");
                forceCloseAllConnections(false);
            } catch (Exception e) {
                e.printStackTrace();
                forceCloseAllConnections(true);
                throw new CTBSystemException(e.getMessage());
            } finally {
                hasAssessmentEndedEvent = true;
                Timer.logElapsed("AssessmentEnded processed: ", assesmentEndedTime);
            }
        }
    }

    private ReportingLevels getReportingLevels(Long testRosterId) throws NamingException {
        Connection conn = null;
        try {
            conn = getOASConnection();
            ObjectiveMapper mapper = new ObjectiveMapper(conn);
            return mapper.findReportingLevelsForRoster(testRosterId);
        } catch (SQLException e) {
            throw new EventChannelException(e);
        } finally {
            try {
                if (conn != null)
                    close(false, conn);
            } catch (SQLException e) {
                throw new EventChannelException(e);
            }
        }
    }

    private ValidationStatus getRosterValidationStatus(Long testRosterId) throws NamingException {
        Connection conn = null;
        try {
            conn = getOASConnection();
            TestRosterMapper mapper = new TestRosterMapper(conn);
            TestRosterRecord roster = mapper.findTestRoster(testRosterId);
            return roster.getValidationStatus();
        } catch (SQLException e) {
            throw new EventChannelException(e);
        } finally {
            try {
                if (conn != null)
                    close(false, conn);
            } catch (SQLException e) {
                throw new EventChannelException(e);
            }
        }
    }

    public void onEvent(ScoringStatusEvent event) {
        updateScoringStatus(event.getTestRosterId(), event.getStatus());
    }

    public void onEvent(ObjectivePrimaryNumberCorrectEvent event) {
        StudentScoreSummaryData data = getResultHolder().getStudentScoreSummaryData();
        StudentScoreSummaryDetails details = data.get(event.getObjectiveId());
        details.setNumIncorrect(new Long(event.getNumberIncorrect()));
        details.setNumCorrect(new Long(event.getNumberCorrect()));
        details.setNumAttempted(new Long(event.getNumberAttempted()));
        details.setNumUnattempted(new Long(event.getNumberUnattempted()));
        details.setNumOfItems(new Long(details.getNumAttempted().intValue() + details.getNumUnattempted().intValue()));
        
        //System.out.println("computed NC score for objective " + event.getObjectiveId() + ": " + details.getNumCorrect() + "/" + details.getNumOfItems());
        
        MasteryLevel mastery = ObjectiveNumberCorrectCalculator.calculateMasteryLevel(details.getNumCorrect().intValue(), details.getNumAttempted().intValue() + details.getNumUnattempted().intValue());
        details.setMasteryLevel(mastery.getCode());
        details.setMastered( (mastery.isMastered() ? CTBConstants.TRUE
                : CTBConstants.FALSE));
        details.setSubtestId(event.getSubtestId());
        
        channel.send(new ObjectivePrimaryCumulativeNumberCorrectEvent(
        				event.getTestRosterId(), 
						event.getObjectiveId(),
						details.getNumAttempted().intValue() + details.getNumUnattempted().intValue(), 
						details.getNumCorrect().intValue(), 
						details.getNumIncorrect().intValue(),
						details.getNumAttempted().intValue(), 
						details.getNumUnattempted().intValue(), 
						mastery,
						event.getSubtestId()));
    }

    public void onEvent(ObjectiveRawScoreEvent event) {
        StudentScoreSummaryData data = getResultHolder().getStudentScoreSummaryData();
        StudentScoreSummaryDetails details = data.get(event.getObjectiveId());  
            
        if(details.getPointsAttempted() == null) {
            details.setPointsAttempted(new Long(event.getPointsAttempted()));
        } else if(!details.getSubtestId().equals(event.getSubtestId())) {
            details.setPointsAttempted(new Long(details.getPointsAttempted().intValue() + event.getPointsAttempted()));
        }
        if(details.getPointsObtained() == null) {
            details.setPointsObtained(new Long(event.getPointsObtained()));
        } else if(!details.getSubtestId().equals(event.getSubtestId())) {
            details.setPointsObtained(new Long(details.getPointsObtained().intValue() + event.getPointsObtained()));
        }
        details.setPointsPossible(new Long(event.getPointsPossible()));
        
        // Added for Laslink Product
        String productType = getResultHolder().getAdminData().getAssessmentType();
        if(productType.equals("LL") || productType.equals("ll"))
        	details.calculatePercentObtainedForFirstDecimal();
        else
        	details.calculatePercentObtained();
        details.setSubtestId(event.getSubtestId());
    }

    public void onEvent(ContentAreaRawScoreEvent event) {
        StsTestResultFactData factData = getResultHolder().getStsTestResultFactData();
        StsTestResultFactDetails factDetails = factData.get(event.getContentAreaName());

        if(factDetails.getPointsAttempted() == null) {
        	factDetails.setPointsAttempted(new Long(event.getPointsAttempted()));
        } else {
        	factDetails.setPointsAttempted(new Long(factDetails.getPointsAttempted().intValue() + event.getPointsAttempted()));
        }
        if(factDetails.getPointsObtained() == null) {
        	factDetails.setPointsObtained(new Long(event.getPointsObtained()));
        } else {
        	factDetails.setPointsObtained(new Long(factDetails.getPointsObtained().intValue() + event.getPointsObtained()));
        }
        List contentAreas = getResultHolder().getCurriculumData().getContentAreasByName(event.getContentAreaName());
        if(contentAreas != null && contentAreas.size() > 0) {
            factDetails.setPointsPossible(((ContentArea)contentAreas.get(0)).getContentAreaPointsPossible());
        } else {
            factDetails.setPointsPossible(new Long(event.getPointsPossible()));
        }
     // Added for Laslink Product
        String productType = getResultHolder().getAdminData().getAssessmentType();
        if(productType.equals("LL") || productType.equals("ll"))
        	factDetails.calculatePercentObtainedForFirstDecimal();
        else
        	factDetails.calculatePercentObtained();
        
    }

    public void onEvent(ResponseReceivedEvent event) {
    	 try {
	    	 List<Objective> primaryList = subtestObjectives.getPrimaryReportingLevelObjective(event.getItemId());
	   	  	for(Objective primary:primaryList){
		        StudentItemScoreDetails detail = getResultHolder().getStudentItemScoreData().get(
		                event.getItemId() + primary.getName() );
		        detail.setItemResponseId(event.getItemResponseId());
		        detail.setItemElapsedTime(DatabaseHelper.asLong(event.getResponseElapsedTime()));
		        detail.setResponse(event.getResponse());
		        detail.setComments(event.getComments());
		        if (event.getConditionCodeId() != null) {
		            detail.setConditionCodeId(DatabaseHelper.asLong(event.getConditionCodeId()));
		        } else {
		        	detail.setConditionCodeId(null);
		        }
	   	  	}
    	 } catch(CTBSystemException e){
    		 e.printStackTrace();
    		 
    	 }
    }

    public void onEvent(PointEvent event) {
    	 try {
	    	 List<Objective> primaryList = subtestObjectives.getPrimaryReportingLevelObjective(event.getItemId());
	   	  	for(Objective primary:primaryList){
		        StudentItemScoreDetails detail = getResultHolder().getStudentItemScoreData().get(
		                event.getItemId()+ primary.getName());
		        detail.setPoints(event.getPointsObtained() );
		        
	   	  	}
    	 }catch(CTBSystemException e){
    		 e.printStackTrace();
    		 
    	 }
    }

    //TODO: this should probably move into collectors/controllers
    public void onEvent(SubtestObjectiveCollectionEvent event) throws CTBSystemException {
    	 this.subtestObjectives = event;
        StudentItemScoreData studentItemScoreData = getResultHolder().getStudentItemScoreData();
        StudentScoreSummaryData studentScoreSummaryData = getResultHolder()
                .getStudentScoreSummaryData();
        Iterator iter = event.getReportingLevelObjectiveKeysetIterator();
        while (iter.hasNext()) {
            String itemId = (String) iter.next();
             //START- For Laslink Scoring
            List<Objective> primaryList = event.getPrimaryReportingLevelObjective(itemId);
            List<Objective> secondaryList = event.getSecondaryReportingLevelObjective(itemId);
            StudentItemScoreDetails itemDetail = null;
            
            for(Objective primary:primaryList){
            	itemDetail = studentItemScoreData.get(itemId+primary.getName());
            	itemDetail.setReportItemSetId(primary.getId());
                itemDetail.setReportItemSetName(primary.getName());
            }
            
            if (!secondaryList.isEmpty()) {
            	  for(Objective secondary:secondaryList){
            		  for(Objective primary:primaryList){
            		  itemDetail = studentItemScoreData.get(itemId+primary.getName()); 
            		  itemDetail.setDetailReportItemSetId(secondary.getId());
                      itemDetail.setDetailReportItemSetName(secondary.getName());
            		  }
                      
            	  }
            } else {
            	for(Objective primary:primaryList){
            		itemDetail = studentItemScoreData.get(itemId+primary.getName());
            		itemDetail.setDetailReportItemSetId(primary.getId());
                    itemDetail.setDetailReportItemSetName(primary.getName());
                }
            }
            for(Objective primary:primaryList){
            StudentScoreSummaryDetails summaryDetail = studentScoreSummaryData.get(primary.getId());

            summaryDetail.setReportItemSetName(primary.getName());
            }
             //END For Laslink Scoring
        }
    }

    public void onEvent(final SubtestItemCollectionEvent event) {
        copyScoreHistoryFromOasToAts(getResultHolder(), event.getItems().iterator(), event
                .getItemSetName(),subtestObjectives);
    }

    public static final void copyScoreHistoryFromOasToAts(final ScoreMoveData scoreMoveData,
            final Iterator subtestItemCollectionData, final String itemSetName,SubtestObjectiveCollectionEvent subtestObjectives) {
        final StudentItemScoreData studentItemScoreData = scoreMoveData.getStudentItemScoreData();

        while (subtestItemCollectionData.hasNext()) {
            final ItemVO item = (ItemVO) subtestItemCollectionData.next();
            try {
             //START-  For Laslink Scoring
            	 List<Objective> primaryList = subtestObjectives.getPrimaryReportingLevelObjective(item.getItemId());
            	  for(Objective primary:primaryList){
	            	 final StudentItemScoreDetails detail = studentItemScoreData.get(item.getItemId() + primary.getName() );
	                 detail.setTestItemNum(item.getItemSortOrder());
	                 detail.setCorrectAnswer(item.getCorrectAnswer());
	                 detail.setMaxPoints(item.getMaxPoints());
	                 detail.setMinPoints(item.getMinPoints());
	                 detail.setItemType(item.getItemType());
	                 detail.setTestItemSetName(itemSetName);
	                 detail.setTestItemSetId(item.getItemSetId());
	                 detail.setCreatedDateTime(new Timestamp(System.currentTimeMillis()));
            	  }
            } catch (CTBSystemException e){
            	
        	}
            //END- For Laslink Scoring
        }
    }

    public ScoreMoveData getResultHolder() {
        return resultHolder;
    }

    public final Connection getOASConnection() throws SQLException, NamingException {
        return this.getConnection(DataSourceFactory.OASDATASOURCE);
    }
    
    public final Connection getIRSConnection() throws SQLException, NamingException {
        return this.getConnection(DataSourceFactory.IRSDATASOURCE);
    }

    private Connection getConnection(String name) throws SQLException, NamingException {
        final Connection connection = DataSourceFactory.getInstance().getDataSource(name).getConnection();
        connections.add(connection);
        return connection;
    }

    public void forceCloseAllConnections(boolean rollback) {
        Iterator iter = ((HashSet)connections.clone()).iterator();
        while (iter.hasNext()) {
            forceClose(rollback, (Connection) iter.next());
        }
    }

    protected void forceClose(boolean rollback, Connection conn) {
        try {
            close(rollback, conn);
        } catch (SQLException exc) {
            // should not throw any Exception here
// Temporarily swallowing this exception because of excessive SPAM.  Nate and Troy had a theory that the
// isClosed() method on connection is not reliable.  ++ MK 10/29/2004
//            exc.printStackTrace();
        } catch (SecurityException e) {
            // should not throw any Exception here
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // should not throw any Exception here
            e.printStackTrace();
        }
    }

    public void close(boolean rollback, Connection connection) throws SQLException {
        try {
                if (rollback) {
                    // don't need explicit rollback for distributed transaction
                    //connection.rollback();
                } else {
                    // don't need explicit commit for distributed transaction
                    //connection.commit();
                }
            } finally {
                SQLUtil.close(connection);
                connections.remove(connection);
            }
    }

    /**
     * This method updates test_roster scoring status in a separate transaction from the rest of
     * scoring. The transaction is important because the UI needs to be able to reflect the status
     * change before the rest of scoring is complete.
     * 
     * @param status the status of this Test Roster
     * @throws NamingException 
     */
    protected void updateScoringStatus(final Long testRosterId, final ScoringStatus status) {
        if (testRosterId == null) {
            throw new EventChannelException(
                    "Encountered an error while scoring. No test roster specified. Unable to set scoring status.");
        }

        // do nothing
    }

    private void updateTestCompletionStatus(final Long testRosterId,
            final CompletionStatus newStatus, final Timestamp completedTime) throws NamingException {
        if (testRosterId == null) {
            throw new EventChannelException(
                    "Encountered an error while scoring.  No test roster specified.  Unable to set test completion status.");
        }

        Connection conn = null;
        try {
            conn = getOASConnection();
            TestRosterMapper mapper = new TestRosterMapper(conn);
            TestRosterRecord roster = mapper.findTestRoster(testRosterId);
            CompletionStatus originalStatus = CompletionStatus.getByCode(roster
                    .getTestCompletionStatus());
            // do not update the roster to a completed status if it is currently interrupted.
            if (! (originalStatus.isInterrupted()))
                mapper.updateTestCompletionStatus(testRosterId, newStatus, completedTime);
            else {
            	System.out.println("Not updating roster status from: " + originalStatus.getCode()
                        + " to: " + newStatus.getCode());
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

    public abstract void onEvent(SubtestValidEvent event);

    public abstract void onEvent(SubtestInvalidEvent event);
    
    protected void setStatuses(List contentAreaNames, String validScore) {
        CurriculumData currData = getResultHolder().getCurriculumData();
        StsTestResultFactData stsTestResultFactData = getResultHolder().getStsTestResultFactData();
        StudentScoreSummaryData summaryData = getResultHolder().getStudentScoreSummaryData();
        StudentItemScoreData itemData = getResultHolder().getStudentItemScoreData();
        for (Iterator iter = contentAreaNames.iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            StsTestResultFactDetails details = stsTestResultFactData.get(name);
            if(!(CTBConstants.VALID_SCORE.equals(details.getValidScore()) && CTBConstants.INVALID_SCORE.equals(validScore))) {
                details.setValidScore(validScore);
                if(CTBConstants.INVALID_SCORE.equals(validScore)) {
                    List contentAreas = currData.getContentAreasByName(name);
                    Iterator caIter = contentAreas.iterator();
                    while(caIter.hasNext()) {
                        ContentArea ca = (ContentArea) caIter.next();
                        summaryData.markObjectivesNonValidForSubtest(ca.getSubtestId());
                        itemData.markItemsNonValidForSubtest(ca.getSubtestId());
                    }
                } else if(CTBConstants.VALID_SCORE.equals(validScore)) {
                    List contentAreas = currData.getContentAreasByName(name);
                    Iterator caIter = contentAreas.iterator();
                    while(caIter.hasNext()) {
                        ContentArea ca = (ContentArea) caIter.next();
                        Long caid = ca.getContentAreaId();
                        PrimaryObjective[] prims = currData.getPrimaryObjectives();
                        for(int i=0;i<prims.length;i++) {
                            if(caid.equals(prims[i].getContentAreaId())) {
                                summaryData.get(prims[i].getPrimaryObjectiveId()).setAtsArchive("T");
                            }
                        }
                        itemData.markItemsValidForSubtest(ca.getSubtestId());
                    }
                }
            } 
        }
    }
}