/*
 * Created on Jan 20, 2004
 */
package com.ctb.xmlProcessing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.sf.hibernate.Session;

import com.ctb.common.tools.ADSConfig;
import com.ctb.common.tools.SystemException;
import com.ctb.cprocessor.InvalidRootElementException;
import com.ctb.mapping.ItemMap;
import com.ctb.mapping.Objectives;
import com.ctb.xmlProcessing.assessment.AssessmentProcessorFactory;

import com.ctb.xmlProcessing.item.ItemProcessorFactory;

import com.ctb.xmlProcessing.itemset.TestItemProcessor;

import com.ctb.xmlProcessing.subtest.SubTestProcessorFactory;
import com.ctb.xmlProcessing.utils.ProductConfig;
import com.ctb.sofa.ScorableItemConfig;

public class XMLElementProcessors {
    private static ThreadLocal _current = new ThreadLocal();

    private static Map getCurrent() {
        if (_current.get() == null)
            _current.set(new HashMap());

        return (Map) _current.get();
    }

    public static XMLElementProcessor getProcessor(String elementName) {
        if (elementName == null)
            throw new SystemException("Element name is null");
        return (XMLElementProcessor) getCurrent().get(elementName);
    }

    public static void setProcessor(String elementName, XMLElementProcessor processor) {
        getCurrent().put(elementName, processor);
    }

    public static XMLElementProcessor initializeAllProcessors(String rootElementName,
            String frameworkCode, Session session, Objectives objectives, ItemMap itemMap,
            ScorableItemConfig scorableItemConfig, ProductConfig productConfig, boolean doSubtestMedia,
            ArrayList unicodeList, ADSConfig adsConfig, String maxPanelWidth, String includeAcknowledgment) {

        if (rootElementName.equals(XMLConstants.ELEMENT_NAME_ASSESSMENT)) 
        {
            initializeForStandardPush( session, objectives, itemMap, doSubtestMedia, unicodeList, adsConfig, maxPanelWidth, includeAcknowledgment );
        } else {
            throw new InvalidRootElementException("Can not process " + rootElementName);
        }

        return XMLElementProcessors.getProcessor(XMLConstants.ELEMENT_NAME_ASSESSMENT);
    }
    
    private static void initializeForStandardPush(Session session, Objectives objectives,
            ItemMap itemMap, boolean doSubtestMedia, ArrayList unicodeList, ADSConfig adsConfig, String maxPanelWidth, String includeAcknowledgment )
    {
        setProcessor(XMLConstants.ELEMENT_NAME_ASSESSMENT, 
                	AssessmentProcessorFactory.getAssessmentProcessorStandard(session));
        setProcessor(XMLConstants.ELEMENT_NAME_TS, SubTestProcessorFactory
                .getSchedulableUnitProcessor(session, doSubtestMedia ));
        setProcessor(XMLConstants.ELEMENT_NAME_TD, SubTestProcessorFactory
                .getDeliverableUnitProcessor(session, doSubtestMedia, unicodeList, adsConfig ));
        setProcessor(XMLConstants.ELEMENT_NAME_TESTITEM, new TestItemProcessor());
        setProcessor(XMLConstants.ELEMENT_NAME_ITEM, ItemProcessorFactory
                .getItemProcessorBuildTestAssessment(objectives, itemMap, session, unicodeList, adsConfig, maxPanelWidth, includeAcknowledgment));
    }
}