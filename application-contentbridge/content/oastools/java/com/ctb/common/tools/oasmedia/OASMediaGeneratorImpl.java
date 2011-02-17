package com.ctb.common.tools.oasmedia;

import org.jdom.Element;
import com.ctb.util.iknowxml.ElementNester;
import com.ctb.util.iknowxml.R2XmlTools;
import com.ctb.common.tools.SystemException;
import com.ctb.common.tools.DefaultPDFGenerator;
import com.ctb.common.tools.FlashGenerator;
import com.ctb.sax.util.XSLDocType;


import java.util.List;
import java.util.Iterator;

/**
 * User: mwshort
 * Date: Dec 12, 2003
 * Time: 12:11:44 PM
 * 
 *
 */
public class OASMediaGeneratorImpl implements OASMediaGenerator {

    private OASMediaValidator validator = new OASMediaValidator();
    private static final String ITEM_TYPE = "ItemType";
    private static final String ITEM_ELEMENT = "Item";
    private static final String TYPE_CONSTRUCTIVE_RESPONSE = "CR";
    //PDF template files


    public OASMediaGeneratorImpl() {

    }

    public OASMediaGeneratorImpl(OASMediaValidator validator) {
        this.validator = validator;
    }

    public OASMedia generateMedia(Element assessmentElement) {
        if (!validator.validForMediaMediaGeneration(assessmentElement))
            assessmentElement = preProcessRootElement(assessmentElement);
        if (!validator.validForMediaMediaGeneration(assessmentElement))
                       throw new SystemException(OASMediaValidator.VALIDATION_MESSAGE);

        OASMedia media = new OASMedia();

        media.setAssessmentXML(R2XmlTools.xmlToCharArray(assessmentElement));
        generatePDFMedia(media,assessmentElement);
        generateFlashMedia(media,assessmentElement);
        return media;
    }

    private void generateFlashMedia(OASMedia media, Element assessmentElement) {
        FlashGenerator flashGenerator = new FlashGenerator();
        media.setAssessmentMovie(
            flashGenerator.generate(
                assessmentElement,MediaTemplate.FLASH_MOVIE_TEMPLATE.getDocumentStream()));
        media.setAssessmentMovieWithAnswerKeys(
            flashGenerator.generate(
                assessmentElement,MediaTemplate.FLASH_MOVIE_AK_TEMPLATE.getDocumentStream()));
    }

    private void generatePDFMedia(OASMedia media, Element assessmentElement) {

        DefaultPDFGenerator pdfGenerator = new DefaultPDFGenerator();
        media.setPDFAnswerKey(pdfGenerator.generatePDF(assessmentElement, XSLDocType.ANSWERKEY_TRANSFORM));
        media.setPDF(pdfGenerator.generatePDF(assessmentElement,XSLDocType.SELECTIVE_RESPONSE_TRANSFORM));
            if (containsCRItems(assessmentElement)) {
                media.setPDFAnswerKeyCROnly(pdfGenerator.generatePDF(assessmentElement,
                        XSLDocType.RUBRIC_ONLY_TRANSFORM));
                media.setPDFCROnly(pdfGenerator.generatePDF(assessmentElement,
                        XSLDocType.CR_ONLY_TRANSFORM));
            }
    }

    private boolean containsCRItems(Element assessmentRoot) 
    {
        boolean hasCR = false;
        List subtests = assessmentRoot.getChildren( "SubTest" );
        if ( subtests.isEmpty() )
        {
            List itemSet = assessmentRoot.getChildren( "ItemSet" );
            for ( Iterator iterset = itemSet.iterator(); iterset.hasNext() && !hasCR; )
            {
            	Element setElement = (Element) iterset.next();
            	List items = setElement.getChildren(ITEM_ELEMENT);
            	for (Iterator iter = items.iterator();iter.hasNext() && !hasCR;)
            	{
            		Element itemElement = (Element) iter.next();
            		if (itemElement != null && TYPE_CONSTRUCTIVE_RESPONSE.equals( itemElement.getAttributeValue(ITEM_TYPE)))
            		    hasCR = true;
            	}
            }
        }
        else
        {
	        for ( Iterator itersub = subtests.iterator(); itersub.hasNext() && !hasCR; )
	        {
	            Element subElement = (Element) itersub.next();
	            List itemsets = subElement.getChildren( "ItemSet" );
	            for ( Iterator iterset = itemsets.iterator(); iterset.hasNext() && !hasCR; )
	            {
	            	Element setElement = (Element) iterset.next();
	            	List items = setElement.getChildren(ITEM_ELEMENT);
	            	for (Iterator iter = items.iterator();iter.hasNext() && !hasCR;)
	            	{
	            		Element itemElement = (Element) iter.next();
	            		if (itemElement != null && TYPE_CONSTRUCTIVE_RESPONSE.equals( itemElement.getAttributeValue(ITEM_TYPE)))
	            		    hasCR = true;
	            	}
	            }
	    	}
        }      
        return hasCR;
    }

    private Element preProcessRootElement(Element assessmentElement) {
       //Sudha assessmentElement = assessmentElement.detach();
        assessmentElement = ElementNester.nestInAssessment(assessmentElement);
        return assessmentElement;
    }


}
