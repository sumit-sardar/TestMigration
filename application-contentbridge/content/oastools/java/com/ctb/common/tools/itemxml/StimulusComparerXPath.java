/*
 * Created on Dec 10, 2003
 *
 */
package com.ctb.common.tools.itemxml;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Element;

import com.ctb.util.JDOMElementComparator;
import com.ctb.util.XPathTracer;

public final class StimulusComparerXPath extends AbstractStimulusComparer {
    private Map allXpaths;

    public StimulusComparerXPath(Map stimulusIdToStimulus) {
        this(stimulusIdToStimulus, null);
    }

    public StimulusComparerXPath(Map stimulusIdToStimulus, PrintStream out) {
        super(stimulusIdToStimulus, out);
        allXpaths = new HashMap();
    }

    String[] getAllXPathsExceptStimulusID(int index) {
        String stimulusId = stimulusIds[index];
        String[] xpaths = (String[]) allXpaths.get(stimulusId);

        if (xpaths == null) {
            xpaths = getAllXPathsExceptStimulusID(stimuli[index]);
            allXpaths.put(stimulusId, xpaths);
        }

        return xpaths;
    }

    String[] getAllXPathsExceptStimulusID(Element element) {
        List xpathList = XPathTracer.trace(element);
        xpathList.remove("//Stimulus/@ID");
        return(String[]) xpathList.toArray(new String[xpathList.size()]);
    }

    protected boolean areEqual(int i, int j) {
        return StimulusComparerXPath.areEqual(
            this.stimuli[i],
            this.stimuli[j],
            getAllXPathsExceptStimulusID(i),
            getAllXPathsExceptStimulusID(j));
    }

    private static boolean areEqual(
        Element e1,
        Element e2,
        String[] xpathNodes1,
        String[] xpathNodes2) {
        if (!Arrays.equals(xpathNodes1, xpathNodes2))
            return false;
        Comparator comparer = new JDOMElementComparator(xpathNodes1);
        return (comparer.compare(e1, e2) == 0);
    }

}
