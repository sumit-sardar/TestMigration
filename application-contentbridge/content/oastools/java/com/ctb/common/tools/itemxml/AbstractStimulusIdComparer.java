/*
 * Created on Dec 9, 2003
 *
 */
package com.ctb.common.tools.itemxml;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public abstract class AbstractStimulusIdComparer
    implements StimulusIdComparer {

    protected Set stimulusIds = new HashSet();

    public AbstractStimulusIdComparer(Set set) {
        this.stimulusIds = set;
    }

    public Set[] getAllMatchingGroups() {
        return StimulusIdUtils.getAllMatchingStimulusIdGroups(this);
    }

    public Iterator stimulusIdIterator() {
        return this.stimulusIds.iterator();
    }

    public Set getMatchingGroup(String string) {
        Set neighbourhood = new HashSet();
        for (Iterator iter = this.stimulusIds.iterator(); iter.hasNext();) {
            String element = (String) iter.next();
            if (matches(element, string)) {
                neighbourhood.add(element);
            }
        }
        return neighbourhood;
    }

    protected abstract boolean matches(String element, String string);

}
