/*
 * Created on Dec 10, 2003
 *
 */
package com.ctb.common.tools.itemxml;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.Map;

import org.jdom.Element;

public final class StimulusComparerAsString extends AbstractStimulusComparer {
    public StimulusComparerAsString(Map stimulusIdToStimulus) {
        this(stimulusIdToStimulus, null);
    }

    public StimulusComparerAsString(
        Map stimulusIdToStimulus,
        PrintStream out) {
        super(stimulusIdToStimulus, out);
    }

    protected boolean areEqual(int i, int j) {
        return StimulusComparerAsString.areEqual(
            this.stimuli[i],
            this.stimuli[j]);
    }

    private static boolean areEqual(Element e1, Element e2) {
        Comparator comparer = new StimulusElementComparator();
        return (comparer.compare(e1, e2) == 0);
    }

}
