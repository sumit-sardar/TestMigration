package com.ctb.contentBridge.core.publish.xml.item;

import java.util.*;

import org.apache.commons.lang.*;
import org.jdom.*;
import org.jdom.xpath.*;
import org.apache.log4j.*;

import com.ctb.contentBridge.core.exception.BusinessException;
import com.ctb.contentBridge.core.publish.tools.OASConstants;


public class ItemAssembler implements ItemBuilder, ItemValidator {
    private static Logger logger = Logger.getLogger(ItemAssembler.class);

    // these options are additive
    public static int NO_DEFAULTS_VALIDATE_STRICT = 0;
    public static int PARSE_ALLOW_DEFAULTS = 1;
    public static int PARSE_SKIP_EXTRA_VALIDATION = 2;

    protected String DEFAULT_FRAMEWORK = "CTB";

    protected String ANSWER_CORRECT = "Correct";
    protected String ANSWER_DISTRACTOR = "Distractor";

    protected int validationMode;

    private Item item;
    private int choiceCount = 0;
    private int numAnswerChoices = 0;

    public ItemAssembler() {
        this.validationMode = NO_DEFAULTS_VALIDATE_STRICT;
    }

    public ItemAssembler(Integer validationMode) {
        this(validationMode.intValue());
    }

    public ItemAssembler(int validationMode) {
        this.validationMode = validationMode;
    }

    public Item parseItem(Element rootElement) {
        item = new Item(rootElement);
        parseNodes(rootElement);
        item.setChoiceCount(choiceCount);
        item.setNumAnswerChoices(numAnswerChoices);

        if ( (validationMode & PARSE_SKIP_EXTRA_VALIDATION) == 0) {
            extraValidation(item);
        }
        return item;
    }

    public Item build(Element rootElement) {
        choiceCount = 0;
        numAnswerChoices = 0;
        return parseItem(rootElement);
    }

    public void validate(Item item) {
        if ( (validationMode & PARSE_SKIP_EXTRA_VALIDATION) == 0) {
            extraValidation(item);
        }
    }

    public static void extraValidation(Item item) {
        validateAnswerChoicesMatch(item);
        validateCorrectAnswerExists(item);
     //   validateHierarchyMatchesObjective(item);
    }

    // todo: this should either use a SAX parser or XPATH expressions to extract items

    /* ========================== TRAVERSE XML TREE =========================== */

    private void parseNodes(Element element) {
        String name = element.getName();

        if (name.equalsIgnoreCase("Item")) {
            parseItemElement(element);
       /* } else if (name.equalsIgnoreCase("Hierarchy")) {
            parseHierarchy(element);*/
        } else if (name.equalsIgnoreCase("Stimulus")) {
            parseStimulus(element);
        }  else if (name.equalsIgnoreCase("Grid")) {
            parseGrid(element);
        } else if (name.equals("ConstructedResponse")) {
            parseConstructedResponse(element);
        } else if (name.equalsIgnoreCase("SelectedResponse")) {
            parseSelectedResponse(element);
        } else if (name.equalsIgnoreCase("AnswerChoice")) {
            parseAnswerChoice(element);
        } else if (name.equalsIgnoreCase("Stem")) {
            parseStem(element);
        } else if (name.equalsIgnoreCase("ThinkCode")) {
            parseThinkID(element);
        } else {
        }
        parseChildren(element);
    }

    private void parseChildren(Element element) {
        List children = element.getChildren();
        Iterator iterator = children.iterator();

        while (iterator.hasNext()) {
            Element child = (Element) iterator.next();

            parseNodes(child);
        }
    }

    private void parseItemElement(Element element) {
        item.setId(extractItemId(element));

        // the DTD specifies that SR is the default if no item type is specified
        item.setType(extractAttributeOptional(element, "ItemType", OASConstants.ITEM_TYPE_SR,
                validationMode));

        item.setVersion(extractAttributeOptional(element, "Version"));
        item.setHistory(extractAttributeOptional(element, "ItemHistory"));
        item.setExternalID( extractAttributeOptional( element, "ExternalID") );
        item.setExternalSystem( extractAttributeOptional( element, "ExternalSystem") );
        item.setFieldTest(extractBooleanAttribute(element, "FieldTest", OASConstants.FALSE));
        item.setSuppressed(extractBooleanAttribute(element, "SuppressScore", OASConstants.FALSE));
        item.setSample(extractBooleanAttribute(element, "Sample", OASConstants.FALSE));
        //        if (item.getSample().equals(TRUE)) {
        //            item.setType(Item.SA_ITEM_TYPE);
        //        }

        item.setObjectiveId(extractAttributeOptional(element, "ObjectiveID" ).trim());

        String displayId = element.getAttributeValue("DisplayID");
        item.setDisplayId(StringUtils.isNotEmpty(displayId) ? displayId : item.getId());

        item.setFrameworkCode(getFrameworkName(element));
    }

    /*private void parseHierarchy(Element element) {
        String hierarchyType = element.getAttributeValue("Type");
        String hierarchyName = element.getAttributeValue("Name");
        String hierarchyCurriculum = element.getAttributeValue("CurriculumID");

        if (hierarchyType.equals("Content Area")) {
            item.setSubject(hierarchyName);
        }
    }*/
    
    private void parseGrid(Element element) {
        item.setAnswerArea("GRID");
        List gridColumn = element.getChildren();
        item.setGriddedColumns(new Long(new Integer(gridColumn.size()).longValue()));
    }

    private void parseStimulus(Element element) {
        item.setExtStimulusId(element.getAttributeValue("ID"));
        item.setExtStimulusTitle(element.getAttributeValue("DisplayID"));
    }

    private void parseStem(Element element) {
        String result = getFullText(element);
        item.setDescription(result);
    }

    private void parseThinkID(Element element) {
        String thinkID = element.getAttributeValue("ThinkID");
        item.setThinkID(thinkID);
    }

    /*
     * Get the all string from Text Data Type such as <P> , <I>, <B> Eg: <P> This is a <B> Very
     * Important Message </B> for you! </P> will return --- This is a Very Important Message For
     * you!
     */
    private String getFullText(Element element) {
        StringBuffer result = new StringBuffer();
        List content = element.getContent();

        if (content.size() > 0) {
            Iterator iterator = content.iterator();

            while (iterator.hasNext()) {
                Object o = iterator.next();

                if (o instanceof Text) {
                    Text t = (Text) o;

                    if (!result.toString().endsWith(".")) {
                        result.append(" ");
                    }
                    result.append(t.getTextTrim());
                    // if(!result.toString().endsWith("."))
                    // result.append(" ");
                } else if (o instanceof Element) {
                    Element child = (Element) o;

                    result.append(getFullText(child));
                }
            }
        }
        return result.toString();
    }

    private void parseSelectedResponse(Element element) {
        if (item.getType().equals(Item.CONSTRUCTED_RESPONSE)) {
            throw new BusinessException("SelectedResponse element not expected for CR Item");
        }

        String attValue = element.getAttributeValue("NumberAnswerChoices", "4");

        if (attValue != null) {
            numAnswerChoices = Integer.parseInt(attValue);
        }
    }

    private void parseAnswerChoice(Element element) {
        //String answerId = element.getAttributeValue("ID");
        String answerType = extractAttributeOptional(element, "Type", ANSWER_DISTRACTOR,
                validationMode);

        choiceCount++;

        if (answerType.equals(ANSWER_CORRECT)) {
            item.setCorrectAnswer(getCorrectAnswer(choiceCount));
        }
    }

    private void parseConstructedResponse(Element element) {
        if (item.getType().equals(Item.SELECTED_RESPONSE)) {
            throw new BusinessException("ConstructedResponse element not expected for SR Item");
        }

        String maxPointsValue = element.getAttributeValue("MaxScorePts");
        String minPointsValue = element.getAttributeValue("MinScorePts");

        item.setMaxPoints(Integer.parseInt(maxPointsValue));
        item.setMinPoints(Integer.parseInt(minPointsValue));
    }

    private String getFrameworkName(Element element) {
        String value = getXPathValue(element, ".//Hierarchy[@Type='Root']/@CurriculumID");

        if (value == null) {
            // todo: decide whether this is mandatory
            // if ((validationMode & PARSE_ALLOW_DEFAULTS) != 0) {
            value = DEFAULT_FRAMEWORK;
            // }
            // else {
            // throw new BusinessException("No framework specified. Validation mode does not allow
            // defaults");
            // }
            logger
                    .debug("Warning: Using default CAB framework since no Root hierarchy node specified!");
        }
        return value;
    }

    private String extractItemId(Element element) {
        String itemId = element.getAttributeValue("ID");

        if (itemId == null) {
            throw new RuntimeException("Missing Item ID");
        }

        if (itemId.indexOf("EMS_") == 0) {
            itemId = itemId.substring("EMS_".length());
        }
        return itemId;
    }

    /* ========================== VALIDATION FUNCTIONS =========================== */

    private static void validateAnswerChoicesMatch(Item item) {
        if (item.getNumAnswerChoices() != item.getChoiceCount()) {
            throw new BusinessException("Item " + item.getId() + ": <SelectedResponse> specifies "
                    + item.getNumAnswerChoices() + " choices but " + item.getChoiceCount()
                    + " <AnswerChoice> elements were found");
        }
    }

    private static void validateCorrectAnswerExists(Item item) {
        if (item.isSR() || item.isSA()) {
            if (item.getCorrectAnswer() == null || item.getCorrectAnswer().equals("")) {
                throw new BusinessException("Item " + item.getId()
                        + " does not have a correct answer identified.");
            }
        }
    }

   /* private static void validateHierarchyMatchesObjective(Item item) {
        Element element = item.getItemRootElement();
        // get the value of the innermost hierarchy node
        while (element.getChild("Hierarchy") != null) {
            element = element.getChild("Hierarchy");
        }

        String objective = element.getAttributeValue("CurriculumID");

        if (StringUtils.isEmpty(objective)) {
            throw new BusinessException("Item " + item.getId()
                    + ": No objective ID detected in hierarchy");
        }
        objective = objective.trim();
        if (!objective.equals(item.getObjectiveId())) {
            throw new BusinessException("Item " + item.getId() + ": Objective ID "
                    + item.getObjectiveId() + " specified in item element does not match "
                    + " objective " + objective + " specified in hierarchy.");
        }
    }*/

    /* ========================== HELPER FUNCTIONS =========================== */

    /*private static String extractAttributeMandatory(Element element, String attributeName) {
        String value = element.getAttributeValue(attributeName);

        if (value == null) {
            throw new BusinessException("Attribute " + attributeName + " of element <"
                    + element.getName() + "> is mandatory.");
        }
        return value;
    }*/

    private static String extractAttributeOptional(Element element, String attributeName,
            String defaultValue, int validationMode) {
        String value = element.getAttributeValue(attributeName);

        if (value == null) {
            if ( (validationMode & PARSE_ALLOW_DEFAULTS) != 0) {
                value = defaultValue;
            } else {
                throw new BusinessException("No value specified for attribute " + attributeName
                        + ". Validation mode does not allow defaults.");
            }
        }
        return value;
    }

    private static String extractAttributeOptional(Element element, String attributeName) {
        String value = element.getAttributeValue(attributeName);

        if (value == null) {
            value = "";
        }
        return value;
    }

    private static String extractBooleanAttribute(Element element, String attributeName,
            String defaultValue) {
        String value = element.getAttributeValue(attributeName);

        if (value == null || value.equals("")) {
            return defaultValue;
        }
        value = value.substring(0, 1);
        if (value.equals(OASConstants.NO)) {
            value = OASConstants.FALSE;
        } else if (value.equals(OASConstants.YES)) {
            value = OASConstants.TRUE;
        }
        return value;
    }

    private static String getXPathValue(Element element, String expression) {
        String value = null;

        Object node = null;

        try {
            XPath xpath = XPath.newInstance(expression);

            node = xpath.selectSingleNode(element);
        } catch (JDOMException e) {
            throw new BusinessException("Invalid XPath expression: " + expression);
        }

        if (node != null) {
            if (node instanceof Element) {
                value = ((Element) node).getText();
            } else if (node instanceof Attribute) {
                value = ((Attribute) node).getValue();
            }
        }
        return value;
    }

    public static String getCorrectAnswer(int choice_no) {
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};

        return letters[choice_no - 1];
    }

}