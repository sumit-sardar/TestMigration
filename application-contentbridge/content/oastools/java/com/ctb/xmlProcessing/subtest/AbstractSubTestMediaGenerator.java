package com.ctb.xmlProcessing.subtest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Attribute;
import org.jdom.xpath.XPath;
import org.xml.sax.SAXException;

import com.ctb.common.tools.ArtLocalizer;
import com.ctb.common.tools.DefaultPDFGenerator;
import com.ctb.common.tools.FlashGenerator;
import com.ctb.common.tools.SystemException;


import com.ctb.sax.util.XSLDocType;
import com.ctb.util.iknowxml.ElementNester;
import com.ctb.util.iknowxml.R2XmlTools;

/**
 * @author wmli
 */
public abstract class AbstractSubTestMediaGenerator {
    private static Logger logger =
        Logger.getLogger(AbstractSubTestMediaGenerator.class);

    public SubTestMedia generate(Element element) {
        Element copy = R2XmlTools.deepCopyElement(element);

        try {
            ArtLocalizer.localizeArt(copy);

        } catch (IOException e) {
            logger.error("", e);
        } catch (SAXException e) {
            logger.error("", e);
        }

        // validate the art files exist after the xml has been changed to local machine paths
 //       MediaGenerator.validateMedia(copy);

        copy.detach();
        Element assessmentRoot = ElementNester.nestInAssessment(copy);

        SubTestMedia media = new SubTestMedia();

        // validate the XML, and set the XML in the Media
        R2XmlTools.validateXml(R2XmlTools.xmlToCharArray(assessmentRoot));
        media.setPremadeTestXml(R2XmlTools.xmlToCharArray(assessmentRoot));

        generateMedia(assessmentRoot, media);
        return media;
    }

    abstract void generateMedia(Element assessmentRoot, SubTestMedia media);

    protected void generateFlashMedia(
        Element assessmentRoot,
        SubTestMedia media) {
        FlashGenerator flashGenerator = new FlashGenerator();
        try {
            media.setPremadeTestFlashMovie(
                flashGenerator.generate(
                    assessmentRoot,
                    new FileInputStream(new File("etc/cab_ib.swt"))));
            media.setPremadeTestFlashAnswer(
                flashGenerator.generate(
                    assessmentRoot,
                    new FileInputStream(new File("etc/cab_ak.swt"))));
        } catch (FileNotFoundException fnf) {
            throw new SystemException(fnf.getMessage(), fnf);
        }
    }

    protected void generatePDFMedia(
        Element assessmentRoot,
        SubTestMedia media) {
        DefaultPDFGenerator pdfGenerator =
            new DefaultPDFGenerator(XSLDocType.ANSWERKEY_TRANSFORM);

        media.setPremadeTestPDFAnswer(pdfGenerator.generatePDF(assessmentRoot));
        //new Stuff
        if (containsCRItems(assessmentRoot)) {
            addRubricAndCRQuestions(media, assessmentRoot);
        }

        pdfGenerator =
            new DefaultPDFGenerator(XSLDocType.SELECTIVE_RESPONSE_TRANSFORM);
        media.setPremadeTestPDFQuestions(pdfGenerator.generatePDF(assessmentRoot));
    }

    private boolean containsCRItems(Element assessmentRoot) {
        String xQueryExp = ".//Item/@ItemType";
        try {
            XPath xPath = XPath.newInstance(xQueryExp);
            List typeList = xPath.selectNodes(assessmentRoot);
            return hasCRNode(typeList);
        } catch (JDOMException e) {
            throw new SystemException(e.getMessage(),e);
        }
    }

    private boolean hasCRNode(List nodeList) {
        for (Iterator iter = nodeList.iterator();iter.hasNext();) {
            Attribute attribute = (Attribute)iter.next();
            if (attribute.getValue().equals("CR"))
                return true;
        }
        return false;  //To change body of created methods use File | Settings | File Templates.
    }

    private void addRubricAndCRQuestions(SubTestMedia media, Element assessmentRoot) {
        DefaultPDFGenerator pdfGenerator;
        pdfGenerator =
            new DefaultPDFGenerator(XSLDocType.CR_ONLY_TRANSFORM);
        media.setPremadeTestPDFCRQuestions(pdfGenerator.generatePDF(assessmentRoot));
        pdfGenerator =
            new DefaultPDFGenerator(XSLDocType.RUBRIC_ONLY_TRANSFORM);
        media.setPremadeTestPDFCRAnswer(pdfGenerator.generatePDF(assessmentRoot));
    }

}
