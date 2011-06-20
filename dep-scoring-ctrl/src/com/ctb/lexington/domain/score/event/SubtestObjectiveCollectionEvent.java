package com.ctb.lexington.domain.score.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.util.SafeHashMap;

/**
 * Provides for a tree data structure for objectives.
 *
 * @todo Handle non-primary/non-secondary levels (database issue)
 * @todo Constructor for Objective changed
 */
public class SubtestObjectiveCollectionEvent extends SubtestEvent {
    private final Map reportingLevelObjectives = new SafeHashMap(String.class, ObjectivePair.class);
    private final Map primaryObjectivesReverseLookup = new SafeHashMap(Long.class, Set.class);
    private final Map secondaryObjectivesReverseLookup = new SafeHashMap(Long.class, Set.class);
    private boolean objectivesRead = false;

    public SubtestObjectiveCollectionEvent(final Long testRosterId, final Integer itemSetId) {
        super(testRosterId, itemSetId);
    }

    public Iterator getReportingLevelObjectiveKeysetIterator() {
        return reportingLevelObjectives.keySet().iterator();
    }

    public Set getItemIdsForPrimaryObjectiveId(final Long objectiveId) {
        return (Set) primaryObjectivesReverseLookup.get(objectiveId);
    }

    public Set getItemIdsForSecondaryObjectiveId(final Long objectiveId) {
        return (Set) secondaryObjectivesReverseLookup.get(objectiveId);
    }

    public Set getPrimaryObjectiveIds() {
        return Collections.unmodifiableSet(primaryObjectivesReverseLookup.keySet());
    }

    /**
     * Adds a primary objective to the collection by <var>itemId </var>.
     *
     * @param itemId
     * @param objective
     */
    public void addPrimaryReportingLevelObjective(final String itemId, final Objective objective) {
        addReportingLevelObjective(itemId, objective, PRIMARY_ACCESSOR);
        addPrimaryObjectiveReverseLookup(objective.getId(), itemId);
    }

    /**
     * Adds a secondary objective to the collection by <var>itemId </var>.
     *
     * @param itemId
     * @param objective
     */
    public void addSecondaryReportingLevelObjective(final String itemId, final Objective objective) {
        addReportingLevelObjective(itemId, objective, SECONDARY_ACCESSOR);
        addSecondaryObjectiveReverseLookup(objective.getId(), itemId);
    }

    /**
     * Gets an iterator over primary objectives without duplicates.
     */
    public Iterator primaryReportingLevelObjectiveIterator() {
        return reportingLevelObjectiveIterator(PRIMARY_ACCESSOR);
    }

    /**
     * Gets the primary objective for a given <var>itemId </var>.
     *
     * @param itemId
     * @return
     */
    public List<Objective> getPrimaryReportingLevelObjective(final String itemId) throws CTBSystemException { // For Laslink Scoring
        if (!reportingLevelObjectives.containsKey(itemId))
            throw new CTBSystemException(null, "Item with ID " + itemId + " is above the primary reporting level");
        objectivesRead = true;
        return getObjectivePairForKey(itemId).primary;
    }

    /**
     * Gets an iterator over secondary objectives without duplicates.
     */
    public Iterator secondaryReportingLevelObjectiveIterator() {
        return reportingLevelObjectiveIterator(SECONDARY_ACCESSOR);
    }

    /**
     * Gets the secondary objective for a given <var>itemId </var>.
     *
     * @param itemId
     * @return
     */
    public List<Objective> getSecondaryReportingLevelObjective(final String itemId) { // For Laslink Scoring
        objectivesRead = true;
        final ObjectivePair objectivePair = (ObjectivePair) reportingLevelObjectives.get(itemId);
        if(objectivePair == null) return null;
        return objectivePair.secondary;
    }

    /*
     * ObjectivePair ensures there is exactly one primary and secondary objective for a given item.
     */
    private static class ObjectivePair {
        private List<Objective> primary = new ArrayList() ; // For Laslink Scoring
        private List<Objective> secondary = new ArrayList(); // For Laslink Scoring
    }

    /*
     * Accessor and following implement a Visitor pattern for the tree, permitting two different
     * algorithms over the same data structure with minimal effort.
     */
    private static interface Accessor {
        String getReportingLevel();

        List<Objective>get(final ObjectivePair pair); // For Laslink Scoring

        void set(final ObjectivePair pair, final Objective objective);
    }

    private static final Accessor PRIMARY_ACCESSOR = new Accessor() {
        public String getReportingLevel() {
            return Objective.PRIMARY;
        }

        public List<Objective> get(final ObjectivePair pair) { // For Laslink Scoring
            return pair.primary;
        }

        public void set(final ObjectivePair pair, final Objective objective) {
            pair.primary.add(objective); // For Laslink Scoring
        }
    };

    private static final Accessor SECONDARY_ACCESSOR = new Accessor() {
        public String getReportingLevel() {
            return Objective.SECONDARY;
        }

        public List<Objective> get(final ObjectivePair pair) { // For Laslink Scoring
            return pair.secondary;
        }

        public void set(final ObjectivePair pair, final Objective objective) {
            pair.secondary.add(objective);     // For Laslink Scoring
        }
    };

    private void addReportingLevelObjective(final String itemId, final Objective objective,
            final Accessor accessor) {
        if (objectivesRead) {
            throw new IllegalStateException("Cannot change objectives after already read.");
        }

        final ObjectivePair pair = getObjectivePairForKey(itemId);

       /* if (!accessor.get(pair).isEmpty()) {
            throw new IllegalStateException("Duplicate " + accessor.getReportingLevel()
                    + " objective for: " + itemId);
        } */

        accessor.set(pair, objective);
    }

    private Iterator reportingLevelObjectiveIterator(final Accessor selector) {
        objectivesRead = true;

        final Set objectives = new HashSet();

        for (final Iterator it = reportingLevelObjectives.values().iterator(); it.hasNext();) {
         // START- For Laslink Scoring
            final List<Objective> objectiveList = selector.get((ObjectivePair) it.next());
            if (!objectiveList.isEmpty()) {
            	for (Objective objective : objectiveList) {
            		
            		objectives.add(objective);
            	}
            //END-  For Laslink Scoring     
            }
        }

        return objectives.iterator();
    }

    private ObjectivePair getObjectivePairForKey(final String key) {
        if (reportingLevelObjectives.containsKey(key)) {
            return (ObjectivePair) reportingLevelObjectives.get(key);
        }

        final ObjectivePair pair = new ObjectivePair();

        reportingLevelObjectives.put(key, pair);

        return pair;
    }

    private void addPrimaryObjectiveReverseLookup(final Long objectiveId, final String itemId) {
        if (!primaryObjectivesReverseLookup.containsKey(objectiveId)) {
            primaryObjectivesReverseLookup.put(objectiveId, new HashSet());
        }

        ((Set) primaryObjectivesReverseLookup.get(objectiveId)).add(itemId);
    }

    private void addSecondaryObjectiveReverseLookup(final Long objectiveId, final String itemId) {
        if (!secondaryObjectivesReverseLookup.containsKey(objectiveId)) {
            secondaryObjectivesReverseLookup.put(objectiveId, new HashSet());
        }

        ((Set) secondaryObjectivesReverseLookup.get(objectiveId)).add(itemId);
    }
}