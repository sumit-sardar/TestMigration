package com.ctb.mapping;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import net.sf.hibernate.Session;

import org.apache.commons.lang.StringUtils;

import com.ctb.common.tools.DBContentTempGateway;
import com.ctb.common.tools.DBObjectivesGateway;
import com.ctb.common.tools.SystemException;


/**
 * Imports from a file or a list into the content_tmp table and invokes the SP_FRAMEWORK_IMPORT stored proc
 */
public class ObjectivesImporter {

    private Objectives objectives;
    private DBContentTempGateway gateway;
    private DBObjectivesGateway objectivesGateway;

    ObjectivesImporter(Session session, Objectives objectives) {
        init(session, objectives);
    }

    public ObjectivesImporter(Session session, String objectiveFile, FrameworkInfo fwInfo, ObjectiveBuilder builder) {
        init(session, loadObjectives(objectiveFile, fwInfo, builder));
    }

    private void init(Session session, Objectives objectives) {
        if (session == null) {
            throw new NullPointerException("ObjectivesImporter Error: Session is null");
        }
        if (objectives == null) {
            throw new NullPointerException("ObjectivesImporter Error: objectives is null");
        }
        this.objectives = objectives;
        this.gateway = new DBContentTempGateway(session);
        this.objectivesGateway = new DBObjectivesGateway(session);
    }

    public Collection getObjectivesToAdd(String importRootCurriculumID) {
        Objective obj = objectives.objectiveFromCurriculumId(importRootCurriculumID);
        ArrayList items = new ArrayList();

        objectives.getSubtreeChildren(obj, items);
        return items;
    }

    public int importFramework() {
        validateObjectives();
        Collection allObjectives = Collections.unmodifiableCollection(objectives.getAllObjectiveValues());

        return importObjectives(allObjectives.iterator());
    }

    public int importSubtree(String importRootCurriculumID) {
        if (importRootCurriculumID == null) {
            throw new IllegalArgumentException("importRootCurriculumID cannot be null");
        }

        validateObjectives();

        Objective subtreeObjective = objectives.objectiveFromCurriculumId(importRootCurriculumID);

        if (subtreeObjective == null) {
            throw new SystemException("The subtree root node has to be defined in the objectives file: "
                    + importRootCurriculumID);
        }

        Objective parentObjective = objectives.getParent(subtreeObjective);

        if (parentObjective == null) {
            throw new SystemException("The root node of a new subtree has to have a parent : "
                    + importRootCurriculumID);
        }

        if (!objectivesGateway.objectiveExistsWithinFramework(parentObjective.getCurriculumID(),
                objectives.getFrameworkCode())) {
            throw new SystemException("The parent of the root node of a new subtree must exist in the database : "
                    + importRootCurriculumID);
        }

        Iterator iter = getObjectivesToAdd(importRootCurriculumID).iterator();

        return importObjectives(iter);
    }

    int importObjectives(Iterator objectivesIter) {
        gateway.clearTable();
        while (objectivesIter.hasNext()) {
            writeObjective((Objective) objectivesIter.next());
        }
        System.out.println("\n\tExecuting stored proc");
        gateway.executeFrameworkImportProc(objectives.getFrameworkCode()
                	, objectives.getFrameworkInfo().getInternalProductNameLevel().intValue() );
        int count = gateway.countRows();
        System.out.println("\nObjectives imported:  " + count);
        return count;
    }

    private void writeObjective(Objective obj) {
        System.out.println("\tWriting objective: " + obj.getNodeKey());
        String parentObjectiveId = "NO PARENT";

        if (obj.hasParent()) {
            parentObjectiveId = objectives.objectiveFromKey(obj.getParentKey()).getCurriculumID();
        }
        gateway.insertContentTemp(obj.getGrade(), obj.getName(),
                obj.getCurriculumID(), parentObjectiveId,
                obj.getLevel().intValue(), obj.getInternalProductDisplayName());
    }

    /**
     * validate objectives -- primary effort here is to prevent
     * SP_FRAMEWORK_IMPORT form blowing up from trying to insert bad data
     */
    private void validateObjectives() {
        // check for objectives wihtou valid parents
        Collection itemsWithoutValidParent = objectives.getObjectivesWithoutValidParent();

        checkDBForMissingParent(itemsWithoutValidParent);

        if (!itemsWithoutValidParent.isEmpty()) {
            throw new SystemException("objectives contains items w/o parent:  "
                    + itemsWithoutValidParent);
        }
        // check for objectives with a dubious level
        Collection objectivesWithDubiousLevel = objectives.getObjectivesWithDubiousLevel();

        // checkDBForDubiousLevels(objectivesWithDubiousLevel);
        if (!objectivesWithDubiousLevel.isEmpty()) {
            throw new SystemException("objectives contains items with dubious level:  "
                    + objectivesWithDubiousLevel);
        }

        Set categoryLevels = objectivesGateway.getCategoryLevels(objectives.getFrameworkCode());

        // perform additional validation on individual objectives
        for (Iterator objIter = objectives.getAllObjectives(); objIter.hasNext();) {
            Objective objective = objectives.objectiveFromCurriculumId((String) objIter.next());

            validateObjective(objective, categoryLevels);
        }
    }

    /**
     * more checking to prevent SP_FRAMEWORK_IMPORT from blowing up
     * @param obj the Objective to validate
     * @param categoryLevels levels for this objective that exist in DB
     */
    private void validateObjective(Objective obj, Set categoryLevels) {
        // check for internal project display name -- this is the FK to item_set_category
        if (StringUtils.isEmpty(obj.getInternalProductDisplayName())) {
            throw new SystemException("internal product display name not set on objective "
                    + obj.toString());
        }
        // check that grade is set for grade level and below
        if (obj.getLevel().compareTo(objectives.getFrameworkInfo().getGradeLevel())
                        >= 0
                && StringUtils.isEmpty(obj.getGrade())) {
            throw new SystemException("grade level not set for objective "
                    + obj.getCurriculumID());
        }
        // for actual product level nodes check that the internal product display name exists in product table
 /*       if (obj.getLevel().equals(this.objectives.getFrameworkInfo().getInternalProductNameLevel())) {
            objectivesGateway.getProductID(objectives.getFrameworkCode(),
                    obj.getName(), "ST");
        } */
        // check that db contains a matching category level
        if (!categoryLevels.contains(obj.getLevel())) {
            throw new SystemException("database does not contain category level for objective "
                    + obj.toString());
        }
    }

    void checkDBForMissingParent(Collection objectivesWithoutParent) {
        for (Iterator iter = objectivesWithoutParent.iterator(); iter.hasNext();) {
            Objective o = objectives.objectiveFromCurriculumId((String) iter.next());

            if (objectivesGateway.objectiveExistsWithinFramework(o.getParentKey(),
                    objectives.getFrameworkCode())) {
                iter.remove();
                continue;
            }
        }
    }

    private Objectives loadObjectives(String objectiveFile, FrameworkInfo fwInfo, ObjectiveBuilder builder) {
        FileReader in = null;

        try {
            in = new FileReader(objectiveFile);
        } catch (FileNotFoundException e) {
            throw new SystemException("Error opening file: " + objectiveFile);
        }
        Objectives objectives = new Objectives(fwInfo);
        ObjectivesLoader loader = new ObjectivesLoader(builder, in);

        loader.load(objectives);
        // --- added error handling

        objectives.setGrades();
        objectives.setInternalProductNames();
        return objectives;
    }

}
