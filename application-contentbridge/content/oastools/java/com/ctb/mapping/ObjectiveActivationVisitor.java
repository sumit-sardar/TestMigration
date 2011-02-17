package com.ctb.mapping;

import java.util.Map;

import com.ctb.common.tools.ObjectiveInfo;

/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Nov 20, 2003
 * Time: 5:36:22 PM
 * To change this template use Options | File Templates.
 */
public class ObjectiveActivationVisitor implements ObjectiveVisitor {
    private FrameworkInfo frameworkInfo;

    private Map dbObjectiveInfoMap;

    public ObjectiveActivationVisitor( Map dbObjectiveInfoMap) {
        this.dbObjectiveInfoMap = dbObjectiveInfoMap;
    }


    public void visitObjective(Objective objective) {
        ObjectiveInfo currentInfo = (ObjectiveInfo) dbObjectiveInfoMap.get(objective.getCurriculumID());
        if (currentInfo != null) {
            objective.setItemSetID(currentInfo.getItemSetID());
            objective.setActivationStatus(currentInfo.getActivationStatus());
        } else {
            objective.setActivationStatus("IN");
        }
    }

}
