/*
 * Created on Dec 5, 2003
 *
 */
package com.ctb.common.tools.itemxml;

import java.util.Set;

public class StimulusIdComparerSimilarity extends AbstractStimulusIdComparer {

    private double threshhold;
    public StimulusIdComparerSimilarity(Set set, double threshhold) {
        super(set);
        this.threshhold = threshhold;
    }

    protected boolean matches(String element, String string) {
        return SimilarityComparer.getSimilarity(element, string)
            >= this.threshhold;
    }

}
