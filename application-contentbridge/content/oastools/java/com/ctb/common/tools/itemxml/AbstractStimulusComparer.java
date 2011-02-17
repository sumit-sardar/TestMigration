/*
 * Created on Dec 10, 2003
 *
 */
package com.ctb.common.tools.itemxml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import com.ctb.util.RegexUtils;

public abstract class AbstractStimulusComparer implements StimulusComparer {

    protected final String[] stimulusIds;
    protected final Element[] stimuli;
    protected final boolean[] isMatched;
    protected PrintStream out = new PrintStream(new OutputStream() {
        public void write(int b) throws IOException {
        }
    });

    public AbstractStimulusComparer(Map stimulusIdToStimulus) {
        this(stimulusIdToStimulus, null);
    }

    public AbstractStimulusComparer(
        Map stimulusIdToStimulus,
        PrintStream out) {
        if (out != null)
            this.out = out;
        this.stimulusIds =
            (String[]) stimulusIdToStimulus.keySet().toArray(
                new String[stimulusIdToStimulus.size()]);

        stimuli = new Element[stimulusIds.length];
        isMatched = new boolean[stimulusIds.length];

        for (int i = 0; i < stimuli.length; i++) {
            stimuli[i] = (Element) stimulusIdToStimulus.get(stimulusIds[i]);
            isMatched[i] = false;
        }
    }

    public Map getAllMatchingGroups() {
        Map returnMap = new HashMap();
        out.println(
            new Date() + ": Started comparing " + stimuli.length + " stimuli");
        for (int i = 0; i < stimuli.length; i++) {
            if (i % 25 == 0)
                System.out.println(new Date() + ": " + i + " compared");
            if (!isMatched[i]) {
                Set s = getMatchingGroup(i);
                s.add(stimulusIds[i]);
                if (s.size() > 1) {
                    String stimulusXML =
                        new XMLOutputter().outputString(stimuli[i]);
                    if (!isTrivial(stimulusXML))
                        returnMap.put(stimulusXML, s);
                }
            }
        }
        out.println(new Date() + ": Comparison complete");
        return returnMap;
    }

    static boolean isTrivial(String xml) {
        String[] expressions =
            {
                "<Stimulus ID=\".*\"><Heading>\\s*</Heading></Stimulus>",
                "<Stimulus ID=\".*\"><Heading><?.*?></Heading></Stimulus>",
                "<Stimulus ID=\".*\"><Heading /></Stimulus>" };

        for (int i = 0; i < expressions.length; i++) {
            if (RegexUtils.match(expressions[i], xml))
                return true;
        }
        return false;
    }

    public Set getMatchingGroup(Element stimulus) {
        return getMatchingGroupAfterIndex(Arrays.binarySearch(stimuli, stimulus));
    }

    public Set getMatchingGroup(int i) {
        return getMatchingGroupAfterIndex(i);
    }

    protected Set getMatchingGroupAfterIndex(int i) {
        Set s = new HashSet();
        Element srcStimulus = stimuli[i];
    
        for (int j = i + 1; j < stimuli.length; j++) {
            Element tgtStimulus = stimuli[j];
    
            if (areEqual(i, j)) {
                s.add(this.stimulusIds[j]);
				isMatched[i] = true;
                isMatched[j] = true;
            }
        }
        return s;
    }

    abstract protected boolean areEqual(int i, int j);

}
