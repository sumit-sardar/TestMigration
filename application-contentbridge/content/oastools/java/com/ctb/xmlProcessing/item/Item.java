package com.ctb.xmlProcessing.item;

import org.jdom.Element;

import com.ctb.common.tools.OASConstants;
import com.ctb.common.tools.media.Media;
import com.ctb.util.OnlineCRConfig;

public class Item {
    private String id;
    private String correctAnswer = "";
    private String type = "";
    private String fieldTest = "";
    private String suppressed = "";
    private String sample = "";
    private String history = "";
    private String description = "";
    private String extStimulusId = "";
    private String extStimulusTitle = "";
    private String version = "";
    private String templateId = "";
    private String activationStatus = "";
    private String subject = "";
    private boolean invisible = false;
    private int minPoints = 0;
    private int maxPoints = 1;
    private String objectiveId;
    private String displayId = "";
    private int frameworkId = 0;
    private String frameworkCode = "";
    private String thinkID = "";
    private String ScaleScore = "";
    private Media media;
    private Element layoutElement;
    private String answerArea;
    private Long  griddedColumns;

    private Element rootElement = null;
    public static final String SELECTED_RESPONSE = OASConstants.ITEM_TYPE_SR;
    // Item and ItemASsembler
    public static final String CONSTRUCTED_RESPONSE = OASConstants.ITEM_TYPE_CR;
    // Item and ItemASsembler
    public static final String SA_ITEM_TYPE = OASConstants.ITEM_TYPE_SA;
    // Item and ItemASsembler
    public static final String NOT_AN_ITEM = OASConstants.ITEM_TYPE_NI;
    // Item and ItemASsembler

    private int numAnswerChoices;
    private int choiceCount;
    private String ExternalID;
	private String ExternalSystem;

    public Item() {
    }

    public Item(String id) {
        this.setId(id);
        this.setType(CONSTRUCTED_RESPONSE);
        this.setFieldTest("F");
        this.setSuppressed("F");
    }
    
    public boolean isSample() {
        return getSample().equals(OASConstants.TRUE);
    }
    
    public String getAnswerArea() {
        return answerArea;
    }
    
    public void setAnswerArea(String answerArea) {
        this.answerArea = answerArea;
    }
    
    public Long getGriddedColumns() {
        return griddedColumns;
    }
    
    public void setGriddedColumns(Long griddedColumns) {
        this.griddedColumns = griddedColumns;
    }
    
    public String getExternalID() {
        return ExternalID;
    }

    public String getExternalSystem() {
        return ExternalSystem;
    }
    
    public void setExternalSystem( String ExternalSystem_) {
        this.ExternalSystem = ExternalSystem_;
    }

	public void setExternalID( String ExternalID_ ) {
        this.ExternalID = ExternalID_;
    }

    public Item(Element rootElement) {
        this.rootElement = rootElement;
    }
    
    public void setRootElement( Element rootElement )
    {
        this.rootElement = rootElement;
    }

    public boolean isSR() {
        return getType().equals(SELECTED_RESPONSE);
    }

    public boolean isCR() {
        return getType().equals(CONSTRUCTED_RESPONSE);
    }

    public boolean isSA() {
        return getType().equals(SA_ITEM_TYPE);
    }
    
    public boolean isRQ() {
        return getType().equals( "RQ" );
    }

    public boolean isInvisible() {
        return invisible;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFieldTest() {
        return fieldTest;
    }

    public void setFieldTest(String fieldTest) {
        this.fieldTest = fieldTest;
    }

    public String getSuppressed() {
        return suppressed;
    }

    public void setSuppressed(String suppressed) {
        this.suppressed = suppressed;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExtStimulusId() {
        return extStimulusId;
    }

    public void setExtStimulusId(String extStimulusId) {
        this.extStimulusId = extStimulusId;
    }

    public String getExtStimulusTitle() {
        return extStimulusTitle;
    }

    public void setExtStimulusTitle(String extStimulusTitle) {
        this.extStimulusTitle = extStimulusTitle;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getOnlineCR() {
        if (new OnlineCRConfig().getOnlineCRTemplates().contains(this.templateId))
            return "T";
        return "F";
    }
    public String getActivationStatus() {
        return activationStatus;
    }

    public void setActivationStatus(String activationStatus) {
        this.activationStatus = activationStatus;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }

    public int getMinPoints() {
        return minPoints;
    }

    public void setMinPoints(int minPoints) {
        this.minPoints = minPoints;
    }

    public String getObjectiveId() {
        return objectiveId;
    }

    public void setObjectiveId(String objectiveId) {
        this.objectiveId = objectiveId;
    }

    public String getDisplayId() {
        return displayId;
    }

    public void setDisplayId(String displayId) {
        this.displayId = displayId;
    }

    public int getFrameworkId() {
        return frameworkId;
    }

    public void setFrameworkId(int frameworkId) {
        this.frameworkId = frameworkId;
    }

    public String getFrameworkCode() {
        return frameworkCode;
    }

    public void setFrameworkCode(String frameworkCode) {
        this.frameworkCode = frameworkCode;
    }

    public Element getItemRootElement() {
        return rootElement;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;

        if (media != null) {
            media.setItemId(id);
        }
    }

    public int getChoiceCount() {
        return choiceCount;
    }

    public int getNumAnswerChoices() {
        return numAnswerChoices;
    }

    public void setChoiceCount(int i) {
        choiceCount = i;
    }

    public void setNumAnswerChoices(int i) {
        numAnswerChoices = i;
    }

    public String getThinkID() {
        return this.thinkID;
    }

    public void setThinkID(String thinkID) {
        this.thinkID = thinkID;
    }
    
    public String getScaleScore() {
        return this.ScaleScore;
    }

    public void setScaleScore( String ScaleScore_ ) {
        this.ScaleScore = ScaleScore_;
    }
    
    public void setLayoutElement( Element layoutElement_ )
    {
        this.layoutElement = layoutElement_;
    }
    
    public Element getLayoutElement()
    {
        return this.layoutElement;
    }
}
