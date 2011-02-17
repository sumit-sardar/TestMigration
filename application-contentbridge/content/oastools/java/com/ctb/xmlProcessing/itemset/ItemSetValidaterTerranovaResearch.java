package com.ctb.xmlProcessing.itemset;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;
import org.jdom.xpath.XPath;

import com.ctb.common.tools.SystemException;
import com.ctb.xmlProcessing.BuilderUtils;
import com.ctb.xmlProcessing.ValidaterUtils;
import com.ctb.xmlProcessing.XMLConstants;

public class ItemSetValidaterTerranovaResearch extends ItemSetValidater {
    public void validate(Element itemSetElement) {
        cumulateErrors(errorList, validateItemsSuppressScore(itemSetElement, XMLConstants.YES));
        cumulateErrors(errorList, validateItemsAnswerChoiceOfDistractorType(itemSetElement));
    }

    private List validateItemsAnswerChoiceOfDistractorType(Element subsetElement) {
        List researchQuestionMissingDistractor = new ArrayList();
        for (Iterator iter = ValidaterUtils.getItemElements(subsetElement).iterator(); iter
                .hasNext();) {
            Element itemElement = (Element) iter.next();
            for (Iterator answerChoiceIter = getAnswerChoiceElement(itemElement).iterator(); answerChoiceIter
                    .hasNext();) {
                Element answerChoiceElement = (Element) answerChoiceIter.next();
                String itemId = itemElement.getAttributeValue(XMLConstants.ID);
                try {
                    if (!BuilderUtils.extractAttributeOptional(answerChoiceElement,
                            XMLConstants.ANSWER_CHOICE_TYPE,
                            XMLConstants.ANSWER_CHOICE_TYPE_DISTRACTOR).equals(
                            XMLConstants.ANSWER_CHOICE_TYPE_DISTRACTOR)) {
                        researchQuestionMissingDistractor.add("Research item [" + itemId
                                + "] <AnswerChoice> type should be [Distractor].");
                    }
                } catch (SystemException e) {
                    researchQuestionMissingDistractor.add(e.getMessage());
                }
            }
        }
        return researchQuestionMissingDistractor;
    }

    private List getAnswerChoiceElement(Element itemElement) {
        try {
            XPath answerChoiceXPath = XPath.newInstance(".//AnswerChoice");
            return answerChoiceXPath.selectNodes(itemElement);
        } catch (Exception e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

}