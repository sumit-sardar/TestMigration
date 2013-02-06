package com.ctb.contentBridge.core.publish.tools;

import java.util.Collection;
import java.util.Iterator;

import com.ctb.contentBridge.core.publish.mapping.Objective;

/**
 * User: mwshort
 * Date: Feb 11, 2004
 * Time: 10:41:29 AM
 * 
 *
 */
public class DefaultObjectiveGradeProcessor implements ObjectiveGradeProcessor {

    private Collection objectives;
    private Integer gradeLevel;

    public DefaultObjectiveGradeProcessor(Collection objectives, Integer gradeLevel) {
        this.objectives = objectives;
        this.gradeLevel = gradeLevel;
    }

    public void processAllObjectives() {
        for (Iterator iter = objectives.iterator();iter.hasNext();) {
            Objective currentObjective = (Objective)iter.next();
            currentObjective.setGrade(GradeParser.VOCATIONAL_DEFAULT);
        }
    }
}
