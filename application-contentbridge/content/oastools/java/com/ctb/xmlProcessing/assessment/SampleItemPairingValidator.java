package com.ctb.xmlProcessing.assessment;

import org.jdom.Element;

import java.util.List;
import java.util.ArrayList;


import com.ctb.xmlProcessing.XMLConstants;

/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Sep 9, 2004
 * Time: 9:26:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class SampleItemPairingValidator {
    private Element assessmentElement;
    public SampleItemPairingValidator(Element assessmentElement) {
        this.assessmentElement = assessmentElement;
    }

    public List validate() {
        List errors = new ArrayList();
        return errors;
    }

    private void compareCurrentToRecentSample(Element subtest, Element mostRecentSampleSubTest, List errors) {
        if (mostRecentSampleSubTest == null)
            errors.add("Incremental Assessments most begin with a SubTest containing a Sample Set");
        if (subtest.getAttributeValue(XMLConstants.LEVEL).
                equals(mostRecentSampleSubTest.getAttributeValue(XMLConstants.LEVEL)))
            return;
        errors.add("The level of a SubTest must match the previous level of a SubTest containing a sample set");
    }

}
