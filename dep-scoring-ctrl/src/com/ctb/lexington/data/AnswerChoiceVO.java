package com.ctb.lexington.data;


/*
 * AnswerChoiceVO.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 */



/**
 *
 * @author  Kristie Kaneta
 * @version 
 */
public class AnswerChoiceVO extends Object implements java.io.Serializable {

    public static final String VO_LABEL = "com.ctb.lexington.valueobject.AnswerChoiceVO";
    public static final String VO_ARRAY_LABEL = "com.ctb.lexington.valueobject.AnswerChoiceVO.array";
    
    private Integer answerChoiceId;
    private String answerChoiceDescription;
    private String displayCode;
    private String answerChoiceValue;

    /** Creates new AnswerChoiceVO */
    public AnswerChoiceVO() 
    {    
    }
    
    //-- Get/Set Methods --//

    /**
     * Get the unique id for this bean instance.
     *
     * @return Integer The unique id for this answer choice.
     */
    public Integer getAnswerChoiceId()
    {
        return this.answerChoiceId;
    }
    
    /**
     * Set the value of this property.
     *
     * @param id_ The value to set the property to.
     * @return void
     */
    public void setAnswerChoiceId(Integer id_)
    {
        this.answerChoiceId = id_;
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getAnswerChoiceDescription()
    {
        return this.answerChoiceDescription;
    }
    
    /**
     * Set the value of this property.
     *
     * @param description_ The value to set the property to.
     * @return void
     */
    public void setAnswerChoiceDescription(String description_)
    {
        this.answerChoiceDescription = description_;
    }
    
    /**
     * Get the display code for this bean instance.
     *
     * @return String The display code for this answer choice.
     */
    public String getDisplayCode()
    {
        return this.displayCode;
    }
    
    /**
     * Set the value of this property.
     *
     * @param code_ The value to set the property to.
     * @return void
     */
    public void setDisplayCode(String code_)
    {
        this.displayCode = code_;
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getAnswerChoiceValue()
    {
        return this.answerChoiceValue;
    }
    
    /**
     * Set the value of this property.
     *
     * @param value_ The value to set the property to.
     * @return void
     */
    public void setAnswerChoiceValue(String value_)
    {
        this.answerChoiceValue = value_;
    }

}
