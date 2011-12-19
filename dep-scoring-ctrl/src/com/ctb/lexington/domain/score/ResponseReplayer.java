package com.ctb.lexington.domain.score;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import com.ctb.lexington.data.ItemResponseVO;
import com.ctb.lexington.data.ItemSetVO;
import com.ctb.lexington.data.StudentItemSetStatusRecord;
import com.ctb.lexington.db.data.TestRosterRecord;
import com.ctb.lexington.db.mapper.ItemResponseMapper;
import com.ctb.lexington.db.mapper.ItemSetMapper;
import com.ctb.lexington.db.mapper.StudentItemSetStatusMapper;
import com.ctb.lexington.db.mapper.TestRosterMapper;
import com.ctb.lexington.db.utils.DatabaseHelper;
import com.ctb.lexington.domain.score.event.ResponseReceivedEvent;
import com.ctb.lexington.domain.score.event.SubtestEndedEvent;
import com.ctb.lexington.domain.score.event.SubtestScoreReceivedEvent;
import com.ctb.lexington.domain.score.event.SubtestStartedEvent;
import com.ctb.lexington.domain.score.event.common.Event;
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.domain.teststructure.CompletionStatus;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.exception.EventChannelException;
import com.ctb.lexington.util.CTBConstants;

/**
 * <code>ResponseReplayer</code> replays events for a {@link Scorer}. It has two
 * modes: production and testing.  For production, use {@link
 * #ResponseReplayer(ConnectionProvider)} and pass in a provider.  For testing,
 * use {@link #ResponseReplayer(ItemSetMapper, ItemResponseMapper,
        * StudentItemSetStatusMapper)} and pass in mock mappers.
 *
 * The code takes great care in the constructors to ensure you cannot mix uses.
 * All instance variables are marked <code>final</code>.
 *
 * If you are in production use, the constructor tests <var>provider</var> and
 * assigns it, making the mappers <code>null</code>.  Then, if you try to use
 * the mapper instance variables directly instead of via the getters for them,
 * you will throw a <code>NullPointerException</code>.
 *
 * Contrariwise, if you are in testing use, the constructor tests the three
 * mapper parameters and assigns them, making the provider <code>null</code>.
 * Then, if you try to use the provider instance variable directly instead of
 * via the getter for it, you will throw a <code>NullPointerException</code>.
 *
 * Lastly, there is no {@link Connection} instance variable.  Instead {@link
 * #getEvents(Long, boolean)} gets one from the provider on the fly (the getters
 * ensure this only really does something if in production mode), and closes it
 * before the method returns.  There is never a leak, the connection is never
 * held for longer than needed, the method may be run more than once
 * statelessly, and the method uses the getters for the mappers, again ensuring
 * the <cite>do the right thing</cite>.
 *
 * @version $Id: ResponseReplayer.java,v 1.1.2.27 2004/09/07 16:36:14 binkley
 *          Exp $
 */
public class ResponseReplayer {
    private final ConnectionProvider provider;
    private final ItemSetMapper itemSetMapper;
    private final TestRosterMapper testRosterMapper;
    private final ItemResponseMapper itemResponseMapper;
    private final StudentItemSetStatusMapper studentItemSetStatusMapper;

    /**
     * Constructs a new <code>ResponseReplayer</code> with the given
     * <var>provider</var>.  This is the inverse of {@link
     * #ResponseReplayer(ItemResponseMapper, StudentItemSetStatusMapper,
     * ItemSetMapper)}.
     *
     * @param provider the connection provider
     */
    public ResponseReplayer(final ConnectionProvider provider) {
        if (null == provider) throw new NullPointerException();

        this.provider = provider;

        // Null to force code which refers to them directly to break -- use the
        // getters which handle use of the connection provider v. mocks
        this.itemSetMapper = null;
        this.itemResponseMapper = null;
        this.testRosterMapper = null;
        this.studentItemSetStatusMapper = null;
    }

    /**
     * Constructor for testing purposes. Allows a subclass to provide mapper
     * implementations.  This is the inverse of {@link #ResponseReplayer(ConnectionProvider)}.
     *
     * @param itemSetMapper the item set mapper
     * @param itemResponseMapper the item response mapper
     * @param studentItemSetStatusMapper the student item set status mapper
     */
    protected ResponseReplayer(final ItemSetMapper itemSetMapper, final ItemResponseMapper itemResponseMapper,
            final TestRosterMapper testRosterMapper, final StudentItemSetStatusMapper studentItemSetStatusMapper) {
        if (null == itemResponseMapper) throw new NullPointerException();
        if (null == studentItemSetStatusMapper) throw new NullPointerException();
        if (null == itemSetMapper) throw new NullPointerException();
        if (null == testRosterMapper) throw new NullPointerException();

        // Null to force code which refers to them directly to break -- use the
        // getters which handle use of the connection provider v. mocks
        this.provider = null;

        this.itemSetMapper = itemSetMapper;
        this.itemResponseMapper = itemResponseMapper;
        this.testRosterMapper = testRosterMapper;
        this.studentItemSetStatusMapper = studentItemSetStatusMapper;
    }

    /**
     * Replays all received events for the given <var>testRosterId</var> and
     * <var>productId</var> for completed subtests on the given
     * <var>channel</var>.
     *
     * @param testRosterId the test roster id
     * @param scorer the channel
     *
     * @throws IllegalStateException if any subtest of the assessment is
     * incomplete
     */
    public void replayCompleteSubtests(final Long testRosterId,
            final Scorer scorer) {
        replayEvents(testRosterId, true, scorer);
    }

    /**
     * Replays all received events for the given <var>testRosterId</var> and
     * <var>productId</var> for completed and maybe one incompete subtests on
     * the given <var>channel</var>.
     *
     * @param testRosterId the test roster id
     * @param scorer the channel
     *
     * @throws IllegalStateException if more than one subtest of the assessment
     * is incomplete
     */
    public void replaySubtests(final Long testRosterId, final Scorer scorer) {
        replayEvents(testRosterId, false, scorer);
    }

    /**
     * Gets a synthetic list of events for a given <var>testRosterId</var>
     * suitable to republish to an event channel.  Optionally require that
     * subtests be completed if <var>requireSubtestsComplete</var> is
     * <code>true</var>.
     *
     * @param testRosterId the test roster id
     * @param requireSubtestsComplete must subtests be completed?
     *
     * @return the list of synthetic events
     */
    protected List getEvents(final Long testRosterId,
            final boolean requireSubtestsComplete) {
        final Connection conn = getConnection();

        try {
            final List subtests = getSubtests(testRosterId,
                    getItemSetMapper(conn));

            if (subtests.isEmpty()) return Collections.EMPTY_LIST;

            return getEvents(testRosterId, subtests, requireSubtestsComplete,
                    getItemResponseMapper(conn),
                    getTestRosterMapper(conn),
                    getStudentItemSetStatusMapper(conn)
                    .getMapOfSubtestStatusForTestRoster(testRosterId));

        } finally {
            close(conn);
        }
    }

    private List getEvents(final Long testRosterId, final List subtests,
            final boolean requireSubtestsComplete,
            final ItemResponseMapper itemResponseMapper,
			final TestRosterMapper testRosterMapper,
            final Map subtestStatuses) {
        if (subtests.isEmpty()) throw new IllegalStateException();

        final List events = new ArrayList();

        TestRosterRecord roster = testRosterMapper.findTestRoster(testRosterId);
        
        for (final Iterator it = subtests.iterator(); it.hasNext();) {
            final ItemSetVO subtest = (ItemSetVO) it.next();

            events.addAll(getSubtestEvents(roster.getNormGroup(), roster.getAgeCategory(), subtest, requireSubtestsComplete,
                    itemResponseMapper,
                    getSubtestStatus(subtest, subtestStatuses)));
        }

        return events;
    }

    private static StudentItemSetStatusRecord getSubtestStatus(
            final ItemSetVO subtest, final Map subtestStatuses) {
        return (StudentItemSetStatusRecord) subtestStatuses.get(
                asLong(subtest.getItemSetId()));
    }

    private List getSubtests(final Long testRosterId,
            final ItemSetMapper itemSetMapper) {
        return itemSetMapper.findSubtestsByTestRoster(testRosterId);
    }

    private List getSubtestEvents(final String normGroup, final String ageCategory,
    		final ItemSetVO itemSet,
            final boolean requireSubtestsComplete,
            final ItemResponseMapper itemResponseMapper,
            final StudentItemSetStatusRecord subtestStatus) {
        final List events = new ArrayList();

        addSubtestStartedEvent(subtestStatus.getTestRosterId(), normGroup, ageCategory, events, itemSet);
       /* if(itemSet.getAbsent() != null && itemSet.getExemptions() != null){
        	if(itemSet.getValidationStatus().equals("VA") && itemSet.getAbsent().equals("N") && itemSet.getExemptions().equals("N"))
        		addResponseEvents(subtestStatus.getTestRosterId(), events, itemSet, itemResponseMapper);
        } else */
        if(itemSet.getObjectiveScore() != null) {
        	//System.out.println("-------Instead of addResponseEvent-------");
        	if(itemSet.getValidationStatus().equals("VA"))
        		addSubtestScoreEvents(subtestStatus.getTestRosterId(), events, itemSet);
        }
        else
        	addResponseEvents(subtestStatus.getTestRosterId(), events, itemSet, itemResponseMapper);
        	
        addSubtestEndedEvent(events, itemSet, requireSubtestsComplete, subtestStatus);

        return events;
    }
    
    //Added for TABE Cat Adaptive
    private static void addSubtestScoreEvents(final Long testRosterId, final List events, final ItemSetVO itemSet) {
    	
    	String[] pipeSeparated = itemSet.getObjectiveScore().split("\\|");
    	if(pipeSeparated != null && pipeSeparated.length > 0) {
    		List subtestScore = new ArrayList(pipeSeparated.length);
    		for (int i = 0; i < pipeSeparated.length; i++)
    			subtestScore.add(pipeSeparated[i]);
    		//System.out.println("pipeSeparated.length ---->>" + pipeSeparated.length);
            events.addAll(getSubtestScoreEvents(subtestScore, testRosterId, itemSet.getItemSetId(), itemSet.getAbilityScore(), itemSet.getItemSetName()));
    	}
    	
    }
    
    private static List getSubtestScoreEvents(final List scores, final Long testRosterId, final Integer itemSetId, final Double abilityScore, final String itemSetName) {
        if (scores.isEmpty()) return Collections.EMPTY_LIST;

        final List events = new ArrayList(scores.size());

        for (final Iterator it = scores.iterator(); it.hasNext();)
            events.add(createSubtestScoreReceivedEvent((String) it.next(), testRosterId, itemSetId, abilityScore, itemSetName));

        return events;
    }
    
    
    private static SubtestScoreReceivedEvent createSubtestScoreReceivedEvent(
            final String score, final Long testRosterId, final Integer itemSetId, final Double abilityScore, final String itemSetName) {
    	
        final SubtestScoreReceivedEvent event = new SubtestScoreReceivedEvent(testRosterId, itemSetId);
        
        if(score.length() > 0) {
        	String[] objectiveValues = score.split(",");
	        event.setObjectiveId(Integer.parseInt(objectiveValues[0]));
	        //System.out.println("objectiveValues[0] -> " + objectiveValues[0]);
	        event.setObjectiveRawScore(Double.parseDouble(objectiveValues[1]));
	        //System.out.println("objectiveValues[1] -> " + objectiveValues[1]);
	        event.setTotalObjectiveRawScore(Double.parseDouble(objectiveValues[2]));
	        //System.out.println("objectiveValues[2] -> " + objectiveValues[2]);
	        event.setObjectiveScore(Double.parseDouble(objectiveValues[3]));
	        //System.out.println("objectiveValues[3] -> " + objectiveValues[3]);
	        event.setObjectiveSSsem(Double.parseDouble(objectiveValues[4]));
	        //System.out.println("objectiveValues[4] -> " + objectiveValues[4]);
	        event.setObjectiveLevel(objectiveValues[5]);
	        //System.out.println("objectiveValues[5] -> " + objectiveValues[5]);
	        event.setObjectiveMasteryLevel(Integer.parseInt(objectiveValues[6]));
	        //System.out.println("objectiveValues[6] -> " + objectiveValues[6]);
        }
        event.setAbilityScore(abilityScore);
        event.setContentAreaName(itemSetName);

        return event;
    }
    
    
    

    private static void addResponseEvents(final Long testRosterId, final List events, final ItemSetVO itemSet,
            final ItemResponseMapper itemResponseMapper) {
        events.addAll(getResponseEvents(itemResponseMapper.findItemResponsesBySubtest(
                asLong(itemSet.getItemSetId()), testRosterId)));
    }
    
    private static void addSubtestEndedEvent(final List events,
            final ItemSetVO itemSet, final boolean requireSubtestsComplete,
            final StudentItemSetStatusRecord subtestStatus) {
    	if (!isSubtestEnded(subtestStatus) && requireSubtestsComplete) {
    		throw new IllegalStateException("Roster contains an incomplete subtest: "
                    + itemSet.getItemSetId());
    	}else {
    		events.add(createSubtestEndedEvent(subtestStatus.getTestRosterId(), itemSet.getItemSetId()));
    	}
    }

    private static void addSubtestStartedEvent(final Long testRosterId, final String normGroup, final String ageCategory,
    		final List events,
            final ItemSetVO itemSet) {
    	if(itemSet.getObjectiveScore() != null) {
    		if(itemSet.getUnscored() != null && itemSet.getUnscored() == 0) {
	    		events.add(createSubtestStartedEvent(testRosterId, normGroup, ageCategory, 
		        		itemSet.getItemSetId(),
		                itemSet.getItemSetForm(), itemSet.getItemSetName(),
		                itemSet.getItemSetLevel(), itemSet.getRecommendedLevel(), itemSet.getAbilityScore(), itemSet.getSemScore()));
    		} else {
    			events.add(createSubtestStartedEvent(testRosterId, normGroup, ageCategory, 
		        		itemSet.getItemSetId(),
		                itemSet.getItemSetForm(), itemSet.getItemSetName(),
		                itemSet.getItemSetLevel(), itemSet.getRecommendedLevel(), null, null));
    		}
        }
        else
	        events.add(createSubtestStartedEvent(testRosterId, normGroup, ageCategory, 
	        		itemSet.getItemSetId(),
	                itemSet.getItemSetForm(), itemSet.getItemSetName(),
	                itemSet.getItemSetLevel(), itemSet.getRecommendedLevel()));
    }

    private static List getResponseEvents(final List responses) {
        if (responses.isEmpty()) return Collections.EMPTY_LIST;

        final List events = new ArrayList(responses.size());

        for (final Iterator it = responses.iterator(); it.hasNext();)
            events.add(createResponseReceivedEvent((ItemResponseVO) it.next()));

        return events;
    }

    private void replayEvents(final Long testRosterId,
            final boolean requireCompleteSubtests, final Scorer scorer) {
        // TODO: did I break something removing the instanceof check for DoNothingScorer? --bko

    	System.out.println("***** SCORING: ResponseReplayer: replayEvents: replaying events.");
        final List eventList = getEvents(testRosterId,requireCompleteSubtests);
        
        System.out.println("***** SCORING: ResponseReplayer: replayEvents: found " + eventList.size() + "events.");
        
        for (final Iterator it = eventList.iterator(); it.hasNext();) {
            try {
                scorer.notify((Event) it.next());

            } catch (CTBSystemException e) {
                throw new EventChannelException(e);
            }
        }
    }

    private static boolean isSubtestEnded(
            final StudentItemSetStatusRecord record) {
        return CompletionStatus.getByCode(record.getCompletionStatus())
                .isSubtestEnded();
    }

    private static SubtestStartedEvent createSubtestStartedEvent(
            final Long testRosterId,
            final String normGroup,
			final String ageCategory,
			final Integer itemSetId,
            final String itemSetForm, final String itemSetName,
            final String itemSetLevel, final String recommendedLevel) {
        return new SubtestStartedEvent(testRosterId, itemSetId, itemSetForm, itemSetName,
                itemSetLevel, normGroup, ageCategory, recommendedLevel);
    }
    
    private static SubtestStartedEvent createSubtestStartedEvent(
            final Long testRosterId,
            final String normGroup,
			final String ageCategory,
			final Integer itemSetId,
            final String itemSetForm, final String itemSetName,
            final String itemSetLevel, final String recommendedLevel, final Double abilityScore, final Double semScore) {
        return new SubtestStartedEvent(testRosterId, itemSetId, itemSetForm, itemSetName,
                itemSetLevel, normGroup, ageCategory, recommendedLevel, abilityScore, semScore);
    }

    private static SubtestEndedEvent createSubtestEndedEvent(
            final Long testRosterId, final Integer itemSetId) {
        return new SubtestEndedEvent(testRosterId, itemSetId);
    }

    //  REDTAG:EXACT SAME code exists in TestDeliveryConversation. Refactor.
    private static ResponseReceivedEvent createResponseReceivedEvent(
            final ItemResponseVO response) {
        final ResponseReceivedEvent event = new ResponseReceivedEvent(
                DatabaseHelper.asLong(response.getTestRosterId()),
                response.getItemId(), response.getItemSetId());

        // event.setConditionCodeId(response.getConditionCodeId()); -- TODO: missing field
        event.setExtAnswerChoiceId(response.getExtAnswerChoiceId());
        event.setItemResponseId(response.getItemResponseId());
        event.setItemSortOrder(response.getItemSortOrder());
        //--- on-line items have no points assigned
        event.setPointsObtained(response.getPoints());
        event.setResponse(response.getResponse());
        event.setResponseElapsedTime(response.getResponseElapsedTime());
        event.setResponseMethod(response.getResponseMethod());
        event.setResponseSeqNum(response.getResponseSeqNum());
        event.setConditionCodeId(response.getConditionCodeId());
        event.setStudentMarked(
                CTBConstants.TRUE.equals(response.getStudentMarked()));
        event.setComments(response.getComments());

        return event;
    }

    private ItemResponseMapper getItemResponseMapper(final Connection conn) {
        return null != itemResponseMapper
               ? itemResponseMapper
               : new ItemResponseMapper(conn);
    }
    
    private TestRosterMapper getTestRosterMapper(final Connection conn) {
        return null != testRosterMapper
               ? testRosterMapper
               : new TestRosterMapper(conn);
    }

    private ItemSetMapper getItemSetMapper(final Connection conn) {
        return null != itemSetMapper
               ? itemSetMapper
               : new ItemSetMapper(conn);
    }

    private StudentItemSetStatusMapper getStudentItemSetStatusMapper(
            final Connection conn) {
        return null != studentItemSetStatusMapper
               ? studentItemSetStatusMapper
               : new StudentItemSetStatusMapper(conn);
    }

    private Connection getConnection() {
        try {
            return provider != null
                   ? provider.getOASConnection() // in production
                   : null; // mock testing (see mapper getters)

        } catch (SQLException e) {
            throw new EventChannelException(
                    "Unable to get a DB connection to OAS.", e);
        } catch (NamingException e) {
            throw new EventChannelException(
                    "Unable to get a DB connection to OAS.", e);
        }
    }

    private void close(final Connection conn) {
        try {
            if (provider != null)
                provider.close(false, conn); // in production
            // do nothing for mock testing (see mapper getters)

        } catch (SQLException e) {
            throw new EventChannelException(
                    "Unable to get a DB connection to OAS.", e);

        } catch (SecurityException e) {
            throw new EventChannelException(
                    "Unable to get a DB connection to OAS.", e);

        } catch (IllegalStateException e) {
            throw new EventChannelException(
                    "Unable to get a DB connection to OAS.", e);
        }
    }

    private static Long asLong(final Integer number) {
        return DatabaseHelper.asLong(number);
    }
}