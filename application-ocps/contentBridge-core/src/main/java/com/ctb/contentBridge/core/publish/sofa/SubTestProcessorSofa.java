package com.ctb.contentBridge.core.publish.sofa;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.apache.log4j.Logger;
import org.jdom.Element;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.dao.DBItemGateway;
import com.ctb.contentBridge.core.publish.iknowxml.R2XmlTools;
import com.ctb.contentBridge.core.publish.report.AbstractXMLElementReport;
import com.ctb.contentBridge.core.publish.report.ReportFactory;
import com.ctb.contentBridge.core.publish.report.SubTestProcessorReport;
import com.ctb.contentBridge.core.publish.xml.XMLElementProcessor;
import com.ctb.contentBridge.core.publish.xml.subtest.SubTestMedia;
import com.ctb.contentBridge.core.publish.xml.subtest.SubTestMediaGeneratorSofa;


public class SubTestProcessorSofa implements XMLElementProcessor {

    DBPremadeTestGateway ptgw;
    DBItemGateway igw;
    private static Logger logger = Logger.getLogger(SubTestProcessorSofa.class);

    public SubTestProcessorSofa(Session session) {
        ptgw = new DBPremadeTestGateway(session);
        igw = new DBItemGateway(session);

    }

    public AbstractXMLElementReport process(Element element) {
        SubTestProcessorReport report =
            ReportFactory.createSubTestReport(false);
        processSubTest(element);
        return SubTestProcessorReport.getCurrentReport();
    }

    public void processSubTest(Element subtestElement) {

        SubTestProcessorReport report =
            SubTestProcessorReport.getCurrentReport();

        // make a copy of the subtest element and update the item id
        Element subtestSofa = R2XmlTools.deepCopyElement(subtestElement);

        String subTestID = subtestSofa.getAttributeValue("ID");
        subTestID = (subTestID == null) ? "<Unknown>" : subTestID;
        logger.info("Processing SubTest [" + subTestID + "]");
        report.setId(subTestID);

        logger.debug("SubTest [" + subTestID + "]: Parsing XML ...");
        PremadeTestHolderAssembler assembler = new PremadeTestHolderAssembler();
        PremadeTestHolder holder = assembler.parseSubTest(subtestSofa);

        try {
            // Add the product id attribute into the SubTest element.
            Long productId = ptgw.getProductID(holder);
            subtestSofa.setAttribute("ProductID", productId.toString());
            logger.debug(
                "SubTest ["
                    + subTestID
                    + "]: Retrieved Product ID ["
                    + productId.toString()
                    + "]");
            report.setProductId(productId);

            logger.debug("SubTest [" + subTestID + "]: Generating media...");
            SubTestMediaGeneratorSofa generator =
                new SubTestMediaGeneratorSofa();
            SubTestMedia media = generator.generate(subtestSofa);

            logger.debug("SubTest [" + subTestID + "]: Writing to database...");
            ptgw.writePremadeTestIntoDatabase(holder, media);
            logger.info("SubTest [" + subTestID + "] written to database.");
        } catch (HibernateException e) {
            throw new SystemException(e.getMessage());
        }
    }
}
