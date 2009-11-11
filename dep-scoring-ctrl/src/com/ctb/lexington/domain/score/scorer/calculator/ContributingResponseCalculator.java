package com.ctb.lexington.domain.score.scorer.calculator;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.ctb.lexington.db.mapper.ScorableItemMapper;
import com.ctb.lexington.db.record.ScorableItemRecord;
import com.ctb.lexington.db.utils.DatabaseHelper;
import com.ctb.lexington.domain.score.event.ContributingCorrectResponseEvent;
import com.ctb.lexington.domain.score.event.ContributingIncorrectResponseEvent;
import com.ctb.lexington.domain.score.event.ContributingPointEvent;
import com.ctb.lexington.domain.score.event.CorrectResponseEvent;
import com.ctb.lexington.domain.score.event.IncorrectResponseEvent;
import com.ctb.lexington.domain.score.event.PointEvent;
import com.ctb.lexington.domain.score.event.ResponseEvent;
import com.ctb.lexington.domain.score.event.ScoreTypeNumberCorrectEvent;
import com.ctb.lexington.domain.score.event.ScoreTypeRawScoreEvent;
import com.ctb.lexington.domain.score.event.SubtestEndedEvent;
import com.ctb.lexington.domain.score.event.SubtestStartedEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.domain.teststructure.ScoreLookupCode;
import com.ctb.lexington.util.AutoHashMap;
import com.ctb.lexington.util.SafeHashSet;

public class ContributingResponseCalculator extends Calculator {
    private final Map scorableItems = new AutoHashMap(String.class, Set.class, HashSet.class);
    private Integer itemSetId;
    private final Set notProcessedScoreTypes = new SafeHashSet(String.class);

    public ContributingResponseCalculator(Channel channel, Scorer scorer) {
        super(channel, scorer);
        channel.subscribe(this, SubtestStartedEvent.class);
        channel.subscribe(this, CorrectResponseEvent.class);
        mustPrecede(SubtestStartedEvent.class, CorrectResponseEvent.class);
        channel.subscribe(this, IncorrectResponseEvent.class);
        mustPrecede(SubtestStartedEvent.class, IncorrectResponseEvent.class);
        channel.subscribe(this, PointEvent.class);
        mustPrecede(SubtestStartedEvent.class, PointEvent.class);
        channel.subscribe(this, SubtestEndedEvent.class);
        mustPrecede(SubtestStartedEvent.class, SubtestEndedEvent.class);
    }

    public void onEvent(final SubtestStartedEvent event) {
        itemSetId = event.getItemSetId();
        initScorableItems(itemSetId);
        initScoreTypes();
    }

    private void initScoreTypes() {
        for (Iterator it = scorableItems.values().iterator(); it.hasNext();) {
            for (Iterator jt = ((Set) it.next()).iterator(); jt.hasNext();) {
                ScorableItemRecord scorableItem = (ScorableItemRecord) jt.next();
                notProcessedScoreTypes.add(scorableItem.getScoreTypeCode());
            }
        }
    }

    private void initScorableItems(Integer itemSetId) {
        for (final Iterator it = retrieveScorableItemRecords(itemSetId).iterator(); it.hasNext();) {
            ScorableItemRecord record = (ScorableItemRecord) it.next();
            getScorableItemRecords(record.getItemId()).add(record);
        }
    }

    protected Collection retrieveScorableItemRecords(Integer itemSetId) {
        Connection oasConnection = null;
        try {
            oasConnection = scorer.getOASConnection();
            return new ScorableItemMapper(oasConnection)
                    .findScorableItemsByItemSetId(DatabaseHelper.asLong(itemSetId));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                scorer.close(true, oasConnection);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Set getScorableItemRecords(String itemId) {
        return (Set) scorableItems.get(itemId);
    }

    public void onEvent(final CorrectResponseEvent event) {
        validateItemSetId(event);
        Collection scorableItemrecords = getScorableItemRecords(event.getItemId());
        for (Iterator iter = scorableItemrecords.iterator(); iter.hasNext();) {
            final ScorableItemRecord record = (ScorableItemRecord) iter.next();
            final String scoreTypeCode = record.getScoreTypeCode();
            notProcessedScoreTypes.remove(scoreTypeCode);
            channel.send(new ContributingCorrectResponseEvent(event.getTestRosterId(), event
                    .getItemId(), event.getItemSetId(), ScoreLookupCode.getByCode(scoreTypeCode)));
        }
    }

    public void onEvent(final IncorrectResponseEvent event) {
        validateItemSetId(event);
        Collection scorableItemrecords = getScorableItemRecords(event.getItemId());
        for (Iterator iter = scorableItemrecords.iterator(); iter.hasNext();) {
            final ScorableItemRecord record = (ScorableItemRecord) iter.next();
            final String scoreTypeCode = record.getScoreTypeCode();
            notProcessedScoreTypes.remove(scoreTypeCode);
            channel.send(new ContributingIncorrectResponseEvent(event.getTestRosterId(), event
                    .getItemId(), event.getItemSetId(), ScoreLookupCode.getByCode(scoreTypeCode)));
        }
    }
    
    public void onEvent(final PointEvent event) {
        validateItemSetId(event);
        Collection scorableItemrecords = getScorableItemRecords(event.getItemId());
        for (Iterator iter = scorableItemrecords.iterator(); iter.hasNext();) {
            final ScorableItemRecord record = (ScorableItemRecord) iter.next();
            final String scoreTypeCode = record.getScoreTypeCode();
            notProcessedScoreTypes.remove(scoreTypeCode);
            channel.send(new ContributingPointEvent(event.getTestRosterId(), event
                    .getItemId(), event.getItemSetId(), ScoreLookupCode.getByCode(scoreTypeCode), event.getPointsObtained()));
        }
    }

    public void onEvent(final SubtestEndedEvent event) {
        for (Iterator it = notProcessedScoreTypes.iterator(); it.hasNext();) {
            final ScoreLookupCode scoreLookupCode = ScoreLookupCode.getByCode((String) it.next());
            // in case there are no contributing respones matching a score lookup code, we need to
            // send a zero number correct and zero raw score, instead of sending no 
            // number correct or point events at all.
            channel.send(new ScoreTypeNumberCorrectEvent(event.getTestRosterId(), scoreLookupCode,
                    event.getItemSetId(), 0));
            channel.send(new ScoreTypeRawScoreEvent(event.getTestRosterId(), scoreLookupCode,
                    event.getItemSetId(), 0));
        }
    }

    private void validateItemSetId(ResponseEvent event) {
        if (!itemSetId.equals(event.getItemSetId()))
            throw new IllegalStateException(
                    "Response event ItemSetId does not match Subtest ItemSetId: "
                            + event.getItemSetId() + " vs. " + itemSetId);
    }
}