package com.ctb.contentBridge.core.publish.tools;

import java.util.Collection;
import java.util.Iterator;

import com.ctb.contentBridge.core.publish.mapping.Objective;
import com.ctb.contentBridge.core.publish.mapping.Objectives;

/**
 * User: mwshort
 * Date: Feb 11, 2004
 * Time: 10:41:57 AM
 * 
 *
 */
public class StandardObjectiveGradeProcessor implements ObjectiveGradeProcessor {
    private Collection objectiveList;
    private Integer gradeLevel;
    private Objectives objectiveTree;

    public StandardObjectiveGradeProcessor(Objectives objectiveTree ,Collection objectives, Integer gradeLevel) {
        this.objectiveList = objectives;
        this.gradeLevel = gradeLevel;
        this.objectiveTree = objectiveTree;
    }

    public void processAllObjectives() {
        for (Iterator iter = objectiveList.iterator();
            iter.hasNext();
            ) {
            Objective objective = (Objective) iter.next();
            assignGradeLevelToObjective(objective);
        }

    }
    private void assignGradeLevelToObjective(Objective objective) {

        if (isTooHighInHierarchyForGrades(objective))
            return;
        assignAncestorGradeLevelToObjective(objective);
    }

    private boolean isTooHighInHierarchyForGrades(Objective objective) {
        return objective
                    .getLevel()
                    .compareTo(gradeLevel)
                    < 0;
    }

    private void assignAncestorGradeLevelToObjective(Objective objective) {
        Objective gradeLevelParent = objective;
        while (gradeLevelParent
            .getLevel()
            .compareTo(gradeLevel)
            > 0) {
            gradeLevelParent = objectiveTree.getParent(gradeLevelParent);
        }

        getGradeFromGradeLevelParent(objective, gradeLevelParent);
    }

    private void getGradeFromGradeLevelParent(Objective objective, Objective gradeLevelParent) {
        if (gradeLevelParent.getGrade() == null) {
            GradeParser gp = new GradeParser();
            gradeLevelParent.setGrade(gp.parseForGrade(gradeLevelParent.getName()));
        }
        objective.setGrade(gradeLevelParent.getGrade());
    }
}
