/*
 * Created on Aug 18, 2004 TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package com.ctb.xmlProcessing.assessment;

import org.jdom.Element;

import com.ctb.reporting.AbstractXMLElementReport;
import com.ctb.xmlProcessing.XMLElementProcessor;

/**
 * @author wmli TODO To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Style - Code Templates
 */
public abstract class AssessmentConverter implements XMLElementProcessor {
    XMLElementProcessor assessmentProcessor;

    public AssessmentConverter(XMLElementProcessor assessmentProcessor) {
        this.assessmentProcessor = assessmentProcessor;
    }

    public AbstractXMLElementReport process(Element element) {
        Element e = convert(element);
        return assessmentProcessor.process(e);
    }

    public abstract Element convert(Element element);
}