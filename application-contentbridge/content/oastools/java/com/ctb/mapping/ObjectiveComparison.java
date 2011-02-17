package com.ctb.mapping;


import java.io.*;
import java.sql.*;
import java.util.*;

import net.sf.hibernate.Session;

import com.ctb.common.tools.*;

/** compare the contents of an objectives text file to the contents of an OAS ITEM_SET table */
public class ObjectiveComparison {

    private String objectivesFile;
    private Objectives objectives;
    private DBObjectivesGateway objectivesGateway;

    public ObjectiveComparison(Session session, String objectiveFile,
            FrameworkInfo fwInfo, ObjectiveBuilder builder) {
        this.objectivesFile = objectiveFile;
        FileReader in = null;

        try {
            in = new FileReader(objectiveFile);
        } catch (FileNotFoundException e) {
            throw new SystemException("Error opening file: " + objectiveFile);
        }
        objectives = new Objectives(fwInfo);
        ObjectivesLoader loader = new ObjectivesLoader(builder, in);

        loader.load(objectives);
        objectives.setGrades();
        objectives.setInternalProductNames();
        this.objectivesGateway = new DBObjectivesGateway(session);
    }

    public List run(boolean verbose, boolean fixnames) {
        return run(objectives.getRootObjective(), verbose, fixnames);
    }

    public List run(boolean verbose, boolean fixnames, String rootObjectiveCmsId) {
        return run(objectives.objectiveFromCurriculumId(rootObjectiveCmsId), verbose, fixnames);
    }

    private List run(Objective root, boolean verbose, boolean fixnames) {
    	List comparisonActivities = new ArrayList();
        try {
            reportObjective(root, verbose, fixnames, comparisonActivities, 0);
        } catch (SystemException sysEx) {
            sysEx.printStackTrace();
        }
        
        return comparisonActivities;
    }

    private void reportObjective(Objective obj, boolean verbose, boolean fixnames, List warnings, int level) {
        try {

            List objWarnings = compareObjectiveToDB(obj, fixnames, level);

            warnings.addAll(objWarnings);

            List childrenOnlyInDB = objectivesGateway.getChildrenExtCmsItemSetIds(obj.getCurriculumID(),
                    objectives.getFrameworkCode());
            Collection childrenInFile = objectives.getChildren(obj);

            for (Iterator iterator = childrenInFile.iterator(); iterator.hasNext();) {
                Objective child = (Objective) iterator.next();

                reportObjective(child, verbose, fixnames, warnings, level + 1);
                childrenOnlyInDB.remove(child.curriculumID);
            }
            for (Iterator iterator = childrenOnlyInDB.iterator(); iterator.hasNext();) {
                String childCmsId = (String) iterator.next();

                reportChildrenMissingFromFile(childCmsId, verbose, warnings,
                        level + 1);
            }
        } catch (SQLException sqlEx) {
            throw new SystemException("unexpected error reporting on objective "
                    + obj.getCurriculumID(),
                    sqlEx);
        }
    }

    private void reportChildrenMissingFromFile(String missingChildCmsId, boolean verbose, List warnings, int level) throws SQLException {
        warnings.add(new ObjectiveComparisonActivity(missingChildCmsId, level, true,
                "found in DB, not found in file", false));
        List childrenInDB = objectivesGateway.getChildrenExtCmsItemSetIds(missingChildCmsId,
                objectives.getFrameworkCode());

        if (childrenInDB.size() > 0) {
            if (verbose) {
                for (Iterator iterator = childrenInDB.iterator(); iterator.hasNext();) {
                    String childCmsId = (String) iterator.next();

                    reportChildrenMissingFromFile(childCmsId, verbose, warnings,
                            level + 1);
                }
            } else {
                warnings.add(new ObjectiveComparisonActivity(missingChildCmsId, level, true,
                        "grandchildren also found in db", false));
            }
        }
    }

    private List compareObjectiveToDB(Objective obj, boolean fixnames, int level) throws SQLException {
        List activities = new ArrayList();
        String objCmsId = obj.getCurriculumID();
        String fwCode = objectives.getFrameworkCode();

        if (!objectivesGateway.objectiveExistsWithinFramework(objCmsId, fwCode)) {
            activities.add(new ObjectiveComparisonActivity(objCmsId, level, true,
                    "found in file, not found in DB", false));
        } else {
            String nameFromFile = obj.getName();
            String nameFromDB = objectivesGateway.getItemSetName(objCmsId,
                    fwCode);

            if (!nameFromFile.equals(nameFromDB)) {
				ObjectiveComparisonActivity activity = new ObjectiveComparisonActivity(objCmsId, level,
                        true,
                        "name mismatch [file=\"" + nameFromFile + "\", db=\""
                        + nameFromDB + "\"]", false);

                if (fixnames) {
                    objectivesGateway.updateItemSetName(objCmsId, fwCode,
                            nameFromFile);
					activity.corrected = true;
                }
                activities.add(activity);
            }
            String displayNameFromDB = objectivesGateway.getItemSetDisplayName(objCmsId,
                    fwCode);

            if (!nameFromFile.equals(displayNameFromDB)) {
				ObjectiveComparisonActivity activity = new ObjectiveComparisonActivity(objCmsId, level,
                        true,
                        "display name mismatch [file=\"" + nameFromFile
                        + "\", db=\"" + displayNameFromDB + "\"]", false);

                if (fixnames) {
                    objectivesGateway.updateItemSetDisplayName(objCmsId, fwCode,
                            nameFromFile);
					activity.corrected = true;
                }
                activities.add(activity);
            }
            if (obj.hasParent()) {
                String parentFromFile = objectives.getParent(obj).getCurriculumID();
                String parentFromDB = objectivesGateway.getParentExtCmsItemSetId(objCmsId,
                        fwCode);

                if (!parentFromFile.equals(parentFromDB)) {
                    activities.add(new ObjectiveComparisonActivity(objCmsId, level, true,
                            "parent mismatch " + "[file=\"" + parentFromFile
                            + "\", db=\"" + parentFromDB + "\"]", false));
                }
            }
        }

        if (activities.isEmpty()) {
            activities.add(new ObjectiveComparisonActivity(objCmsId, level, false, null,
                    false));
        }
        return activities;
    }
}
