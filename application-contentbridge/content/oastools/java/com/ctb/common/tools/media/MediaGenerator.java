package com.ctb.common.tools.media;

import org.jdom.*;

import com.ctb.common.tools.ArtLocalizer;
import com.ctb.common.tools.FlashGenerator;
import com.ctb.common.tools.ImageValidation;
import com.ctb.common.tools.PDFGenerator;
import com.ctb.common.tools.PDFGeneratorFactory;
import com.ctb.common.tools.SystemException;
import com.ctb.xmlProcessing.item.Item;
import com.ctb.xmlProcessing.item.ItemMediaGenerator;
import com.ctb.sax.util.*;
import com.ctb.util.iknowxml.*;

public class MediaGenerator implements ItemMediaGenerator {
    private String swtFilename = "etc/cab_iak.swt";

    public MediaGenerator() {
    }

    public Media generate(Item item, Element element) throws Exception {

        Media media = new Media();

        // validate the XML, and set the XML in the Media
        R2XmlTools.validateXml(R2XmlTools.xmlToCharArray(element));
        media.setXml(R2XmlTools.xmlToCharArray(element));

        // now make a copy of the element or XML stream output of the element for local media generation
        // todo - mws - long term, use the ImagePathMover to switch patch to localimagearea, then back to remote
        // instead of making copies
        Element elementCopy = R2XmlTools.deepCopyElement(element);
        //todo - mws - replace with ImagePathMover that is in the ImagePathMover thread registry
        ArtLocalizer.localizeArt(elementCopy);
        // validate the art files exist after the xml has been changed to local machine paths
//        validateMedia(elementCopy);

        // Art is there!! GOGO GADGET MEDIA GENERATION!
        PDFGenerator generator =
            PDFGeneratorFactory.create(XSLDocType.SELECTIVE_RESPONSE_TRANSFORM);

        media.setPdf(generator.generatePDF(elementCopy));
        FlashGenerator flashGenerator = new FlashGenerator(swtFilename);

//        media.setAkSwf(flashGenerator.generate(elementCopy));

        if (item != null && item.isCR()) {
            generator = PDFGeneratorFactory.create(XSLDocType.ANSWERKEY_TRANSFORM);
            media.setAkPdf(generator.generatePDF(elementCopy));
        }
        return media;
    }

    // todo - mws - this method is only used by the MediaGenerator test. maybe go away?
    public Media generate(Element element) throws Exception {
        return generate(null, element);
    }

    public Media generateMedia(Item item) {
        try {
            return generate(item, item.getItemRootElement());
        } catch (Exception e) {
			throw new SystemException(e.getMessage(), e);
        }
    }

    /**
     * Validates the art can be found for local media generation.
     * @param element
     */
    public static void validateMedia(Element element) {
        ImageValidation imv = new ImageValidation(element);

        if (!imv.validateArt()) {
            throw new SystemException(
                "Art missing, can not perform media generation\n" + imv);
        }
    }

}
