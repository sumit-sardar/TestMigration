package com.ctb.contentBridge.core.publish.mapping;

import java.util.*;
import org.jdom.*;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.tools.DefaultObjectiveGradeProcessor;
import com.ctb.contentBridge.core.publish.tools.ObjectiveGradeProcessor;
import com.ctb.contentBridge.core.publish.tools.StandardObjectiveGradeProcessor;

public class Objectives {
    public static final String IGNORE_INTERNAL_PROD_NAME = "IGNORE";

    private SortedMap mapByCurriculumId = new TreeMap();
    private Map mapByKey = new HashMap();
    private String frameworkCode;
    private FrameworkInfo frameworkInfo;

    public Objectives(FrameworkInfo frameworkInfo) {
        this.frameworkCode = frameworkInfo.getFrameworkCode();

        if (frameworkCode == null) {
            throw new NullPointerException("framework code cannot be null");
        }
        if (frameworkInfo == null) {
            throw new NullPointerException("frameworkInfo cannot be null");
        }
        this.frameworkInfo = frameworkInfo;
    }
    public void add(Objective objective) {
        add(objective,-1);
    }
    public void add(Objective objective, int lineNumber) {
        String lineError = "Line " + lineNumber + ": ";
        if (mapByKey.containsKey(objective.getNodeKey())) {
            throw new IllegalArgumentException(
                lineError + "objective has duplicate key:  " + objective.getNodeKey());
        }
        if (mapByCurriculumId.containsKey(objective.getCurriculumID())) {
            throw new IllegalArgumentException(
                lineError + "objective has duplicate objective id:  "
                    + objective.getCurriculumID());
        }
        if (!objective.hasParent()
            && !objective.getCurriculumID().equals(
                frameworkInfo.getFrameworkCode())) {
            throw new IllegalArgumentException(
                lineError + "Root node curriculum ID ('"
                    + objective.getCurriculumID()
                    + "') has to match framework code ('"
                    + frameworkInfo.getFrameworkCode()
                    + "')");
        }
        mapByKey.put(objective.getNodeKey(), objective);
        mapByCurriculumId.put(objective.getCurriculumID(), objective);
    }

    public Objective objectiveFromCurriculumId(String curriculumID) {
		try {
			return (Objective) mapByCurriculumId.get(curriculumID);
		} catch(Exception e) {
			return null;
		}
        
    }

    Objective objectiveFromKey(String key) {
        return (Objective) mapByKey.get(key);
    }

    public Collection getChildren(Objective parent) {
        Collection children = new ArrayList();
        String parentKey = parent.getNodeKey();

        // -- this is inefficient, but good enough for this report
        for (Iterator iter = mapByCurriculumId.values().iterator();
            iter.hasNext();
            ) {
            Objective o = (Objective) iter.next();

            if (o.hasParent() && o.getParentKey().equals(parentKey)) {
                children.add(o);
            }
        }
        return children;
    }

    public Objective getRootObjective() {
        int rootLevel = frameworkInfo.getRootLevel();

        // -- this is inefficient, but good enough for this report
        for (Iterator iter = mapByCurriculumId.values().iterator();
            iter.hasNext();
            ) {
            Objective o = (Objective) iter.next();

            if (o.getLevel().intValue() == rootLevel) {
                return o;
            }
        }
        throw new SystemException("No element exists at the root framework level");
    }

    public Element hierarchy(String curriculumID) {
        Objective objective = objectiveFromCurriculumId(curriculumID);

        if (objective == null) {
            return null;
        }
        return hierarchy(objective);
    }

    private Element hierarchy(Objective objective) {
        if (!objective.hasParent()) {
            return objective.asElement();
        }

        Element element = objective.asElement();
        Objective parent = getParent(objective);

        if (parent == null) {
            throw new RuntimeException(
                "Could not find parent for " + objective);
        }

        Element parentHierarchy = hierarchy(parent);

        addToBottom(parentHierarchy, element);
        return parentHierarchy;
    }

    public Collection getObjectivesWithoutValidParent() {
        ArrayList badObjectives = new ArrayList();
        Iterator iter = mapByCurriculumId.keySet().iterator();

        while (iter.hasNext()) {
            String key = (String) iter.next();
            Objective o = (Objective) mapByCurriculumId.get(key);

            if (o.hasParent()) {
                String parentId = o.getParentKey();

                if (!mapByKey.containsKey(parentId)) {
                    badObjectives.add(o.getCurriculumID());
                }
            }
        }
        return badObjectives;
    }

    public Collection getObjectivesWithDubiousLevel() {
        ArrayList badObjectives = new ArrayList();
        Iterator iter = mapByCurriculumId.keySet().iterator();

        while (iter.hasNext()) {
            String key = (String) iter.next();
            Objective objective = objectiveFromCurriculumId(key);

            if (objective.hasParent()) {
                Objective parent = getParent(objective);

                if (parent == null) {
                    badObjectives.add(objective.getCurriculumID());
                    continue;
                }
                int parentLevel = parent.getLevel().intValue();
                int itemLevel = objective.getLevel().intValue();

                if (parentLevel != itemLevel - 1) {
                    badObjectives.add(objective.getCurriculumID());
                }
            }
        }
        return badObjectives;
    }

    private Element addToBottom(Element hierarchyRoot, Element element) {
        Element hierarchyNode = hierarchyRoot;

        while (hierarchyNode.getChild("Hierarchy") != null) {
            hierarchyNode = hierarchyNode.getChild("Hierarchy");
        }
        return hierarchyNode.addContent(element);
    }

    public Objective getParent(Objective objective) {
        return objectiveFromKey(objective.getParentKey());
    }

    public String getFrameworkCode() {
        return frameworkCode;
    }

    public FrameworkInfo getFrameworkInfo() {
        return frameworkInfo;
    }

    public Iterator getAllObjectives() {
        return mapByCurriculumId.keySet().iterator();
    }

    public Collection getAllObjectiveValues() {
        return mapByCurriculumId.values();
    }

    public int getObjectiveCount() {
        return mapByKey.size();
    }

    /**
     * i do post-processing that sets internal product display name on each node by
     * looking up hierarchy for the parent with a level
     * that matches the productDisplayNameLevel member.
     */
    public void setInternalProductNames() {
        for (Iterator iter = mapByCurriculumId.values().iterator();
            iter.hasNext();
            ) {
            Objective objective = (Objective) iter.next();

            if (objective
                .getLevel()
                .compareTo(getFrameworkInfo().getInternalProductNameLevel())
                < 0) {
                objective.setInternalProductDisplayName(
                    IGNORE_INTERNAL_PROD_NAME);
                continue;
            }
            Objective productNameLevelParent = objective;

            while (productNameLevelParent
                .getLevel()
                .compareTo(getFrameworkInfo().getInternalProductNameLevel())
                > 0) {
                productNameLevelParent = getParent(productNameLevelParent);
            }
            objective.setInternalProductDisplayName(
                getFrameworkCode() + " " + productNameLevelParent.getName());
        }
    }

    /**
     * i do post-processing that sets grade on each node by
     * looking up hierarchy for the parent with a level
     * that matches the gradeLevel member.
     */
    public void setGrades() {

        ObjectiveGradeProcessor ogp = null;
        if (getFrameworkInfo().getGradeLevel().equals(new Integer(0)))
            ogp = new DefaultObjectiveGradeProcessor(mapByCurriculumId.values(),
                    getFrameworkInfo().getGradeLevel());
        else
            ogp = new StandardObjectiveGradeProcessor(this, mapByCurriculumId.values(),
                    getFrameworkInfo().getGradeLevel());
        ogp.processAllObjectives();
    }


    /**
     * Collects all the child objectives under a given objective
     * @param objective Root to find the children from
     * @param subTreeChildren the collection to add the children objectives
     */
    void getSubtreeChildren(Objective objective, Collection subTreeChildren) {
        subTreeChildren.add(objective);
        Collection children = getChildren(objective);

        if (children == null) {
            return;
        }
        for (Iterator iter = children.iterator(); iter.hasNext();) {
            getSubtreeChildren((Objective) iter.next(), subTreeChildren);
        }
    }

    // get list of objective for the given objective ID and it's ancestors.
    public List getHierarchyObjectiveList(String objectiveId) {
        if (objectiveFromCurriculumId(objectiveId) == null) {
            return null;
        }

        List hierarchy = new ArrayList();

        // find the objective for the given objective id
        Objective objective = objectiveFromCurriculumId(objectiveId);
        hierarchy.add(objective);

        while (objective.hasParent()) {
            objective = getParent(objective);
            hierarchy.add(objective);
        }

        Collections.reverse(hierarchy);
        return hierarchy;
    }

    // traverse through the objective tree and carry out action in the visitor
    public void traverse(ObjectiveVisitor visitor) {
        Objective rootObjective = getRootObjective();
        traverse(rootObjective, visitor);
    }

    public void traverse(Objective objective, ObjectiveVisitor visitor) {
        objective.accept(visitor);
        Collection children = getChildren(objective);
        for (Iterator iter = children.iterator(); iter.hasNext();) {
            Objective child = (Objective) iter.next();
            traverse(child, visitor);
        }
    }

    // create a objective structure that only include the list objectives and their ancestors.
    public Objectives extractObjectives(List objectiveIds) {
        //		building a small subset of objective for the mapped items for the report purpose
        Objectives resultObjectives = new Objectives(frameworkInfo);

        for (Iterator objectiveIdIter = objectiveIds.iterator();
            objectiveIdIter.hasNext();
            ) {
            String objectiveId = (String) objectiveIdIter.next();

            List hierarchy = getHierarchyObjectiveList(objectiveId);
            if (hierarchy != null)
	            for (Iterator objectiveIter = hierarchy.iterator();
	                objectiveIter.hasNext();
	                ) {
	                Objective objective = (Objective) objectiveIter.next();
	
	                // add the objective if the objective is not in the mapped objectives collection
	                if (resultObjectives
	                    .objectiveFromCurriculumId(objective.getCurriculumID())
	                    == null) {
	                    resultObjectives.add(objective);
	                }
	            }
        }

        return resultObjectives;
    }
}
