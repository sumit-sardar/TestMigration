package com.ctb.itemmap.csv;

import java.util.Iterator;
import java.util.List;

import com.ctb.mapping.Objective;
import com.ctb.mapping.Objectives;

public class MappingEntry implements Comparable {
    public static final String HIERARCHY_TAG = "<HIERARCHY>";

    private String index = "";
    private String answerKey = "";
    private String itemId = "";
    private String objectiveId = "";
    private List hierarchy;
    private String notes = "";

    public MappingEntry() {
    }

    public MappingEntry(
        String idx,
        String answerKey,
        String itemId,
        String objectiveId,
        String notes,
        List hierarchy) {
        this.index = idx;
        this.answerKey = answerKey;
        this.itemId = itemId;
        this.objectiveId = objectiveId;
        this.notes = notes;
        this.hierarchy = hierarchy;
    }

    public MappingEntry(String mappingEntry, Objectives objectives) {

        List entry = MappingUtils.getValuesForCommaDelimitedList(mappingEntry);
        this.index = ((String) entry.get(0)).trim();
        this.answerKey = ((String) entry.get(1)).trim();
        this.itemId = ((String) entry.get(2)).trim();
        this.objectiveId = ((String) entry.get(3)).trim();

        if (entry.size() == 7) {
            this.notes = (String) entry.get(6);
        }

        if (objectives.objectiveFromCurriculumId(this.objectiveId) != null) {
            this.hierarchy =
                objectives.getHierarchyObjectiveList(this.objectiveId);
        }

    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(index);
        buffer.append(",");
        buffer.append(answerKey);
        buffer.append(",");
        buffer.append(itemId);
        buffer.append(",");
        buffer.append(objectiveId);
        buffer.append(",");
        buffer.append(HIERARCHY_TAG);
        buffer.append(",");
        buffer.append(notes);

        return buffer.toString();
    }

    public String getHierarchyString(int level) {
    	if (objectiveId.equals("")) {
    		return ",";
    	}
    	
        if (!objectiveId.equals("") && hierarchy == null) {
            return "\"Error: Cannot find objective in Objectives.txt\",\"Error: Cannot find objective in Objectives.txt\"";
        }

        StringBuffer buffer = new StringBuffer();
        getObjectiveNameForLevel(level, buffer);
        buffer.append(",");
        getObjectiveNameForLevel(level + 1, buffer);

        return buffer.toString();
    }

    private void getObjectiveNameForLevel(int level, StringBuffer buffer) {
        Objective objective = getObjectiveForLevel(level);
        if (objective != null) {
            buffer.append("\"");
            buffer.append(objective.getName());
            buffer.append("\"");
        }
    }

    private Objective getObjectiveForLevel(int level) {
        for (Iterator hierarchyIter = hierarchy.iterator();
            hierarchyIter.hasNext();
            ) {
            Objective objective = (Objective) hierarchyIter.next();

            if (objective.getLevel().equals(new Integer(level))) {
                return objective;
            }
        }

        return null;
    }

    public int getMappedObjectiveLevel() {
        if (hierarchy != null && hierarchy.size() > 0) {
            return ((Objective) hierarchy.get(hierarchy.size() - 1))
                .getLevel()
                .intValue();
        } else {
            // default to use level 2 for entry display
            return 2;
        }
    }
    public String getAnswerKey() {
        return answerKey;
    }

    public String getIndex() {
        return index;
    }

    public String getItemId() {
        return itemId;
    }

    public String getNotes() {
        return notes;
    }

    public String getObjectiveId() {
        return objectiveId;
    }

    public void setAnswerKey(String string) {
        answerKey = string;
    }

    public void setIndex(String string) {
        index = string;
    }

    public void setItemId(String string) {
        itemId = string;
    }

    public void setNotes(String string) {
        notes = string;
    }

    public void setObjectiveId(String string) {
        objectiveId = string;
    }

    public int compareTo(Object o) {
        return new Integer(index).compareTo(
            new Integer(((MappingEntry) o).getIndex()));
    }

}
