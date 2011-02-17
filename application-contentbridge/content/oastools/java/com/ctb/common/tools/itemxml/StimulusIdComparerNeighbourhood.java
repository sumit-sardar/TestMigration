/*
 * Created on Dec 5, 2003
 *
 */
package com.ctb.common.tools.itemxml;

import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class StimulusIdComparerNeighbourhood
    extends AbstractStimulusIdComparer {

    private int distance;

    public StimulusIdComparerNeighbourhood(Set set, int distance) {
        super(set);
        this.distance = distance;
    }

    public StimulusIdComparerNeighbourhood(Set set) {
        this(set, 1);
    }

    protected boolean matches(String element, String string) {
        return (
            (element != null)
                && (string != null)
                && StringUtils.getLevenshteinDistance(string, element)
                    <= distance);
    }

}
