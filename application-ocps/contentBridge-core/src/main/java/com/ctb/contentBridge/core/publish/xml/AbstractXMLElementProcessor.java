package com.ctb.contentBridge.core.publish.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

import com.ctb.contentBridge.core.publish.report.AbstractXMLElementReport;

/**
 * @author wmli
 */
abstract public class AbstractXMLElementProcessor implements
		XMLElementProcessor {

	public AbstractXMLElementReport process(Element element) {
		AbstractXMLElementReport report = validateElement(element);
		if (!report.isSuccess())
			return report;

		List childReports = new ArrayList();
		List children = element.getChildren();

		for (Iterator iter = children.iterator(); iter.hasNext();) {
			Element child = (Element) iter.next();
			XMLElementProcessor childProcessor = XMLElementProcessors
					.getProcessor(child.getName());

			if (childProcessor != null) {
				childReports.add(childProcessor.process(child));
			}
		}

		return processCurrentElement(element, report, childReports);
	}

	protected abstract AbstractXMLElementReport validateElement(Element element);

	protected abstract AbstractXMLElementReport processCurrentElement(
			Element element, AbstractXMLElementReport report, List childReports);
}
