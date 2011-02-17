package com.ctb.common.tools.oneoff.peid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class PEIDObjective {
    private String id;
    private String description;

    private PEIDObjective parent;
    private List children = new ArrayList();

    public PEIDObjective(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PEIDObjective getParent() {
        return parent;
    }

    public void setParent(PEIDObjective parent) {
        if (parent != null) {
            this.parent = parent;
            parent.addChild(this);
        }
    }

    public List getChildren() {
        return children;
    }

    public void addChild(PEIDObjective objective) {
        children.add(objective);
    }

    public String toString() {
        return id + ": " + description;
    }

    public int getCategoryLevel() {
        PEIDObjective obj = this;
        int level = 1;

        while (obj.getParent() != null) {
            level++;
            obj = obj.getParent();
        }

        return level;
    }

    public void traverse(ObjectiveVisitor visitor) {
        accept(visitor);

        Collections.sort(children, new Comparator() {

            public int compare(Object arg0, Object arg1) {
                return ((PEIDObjective) arg0).getId().compareTo(
                        ((PEIDObjective) arg1).getId());
            }
        });
        for (Iterator iter = children.iterator(); iter.hasNext();) {
            PEIDObjective child = (PEIDObjective) iter.next();
            child.traverse(visitor);
        }
    }
    public void accept(ObjectiveVisitor visitor) {
        visitor.visitObjective(this);
    }
}