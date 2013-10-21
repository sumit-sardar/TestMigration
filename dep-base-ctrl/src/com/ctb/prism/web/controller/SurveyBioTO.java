
package com.ctb.prism.web.controller;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for surveyBioTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="surveyBioTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="surveyName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="surveyValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "surveyBioTO", propOrder = {
    "surveyName",
    "surveyValue"
})
public class SurveyBioTO {

    protected String surveyName;
    protected String surveyValue;

    /**
     * Gets the value of the surveyName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSurveyName() {
        return surveyName;
    }

    /**
     * Sets the value of the surveyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSurveyName(String value) {
        this.surveyName = value;
    }

    /**
     * Gets the value of the surveyValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSurveyValue() {
        return surveyValue;
    }

    /**
     * Sets the value of the surveyValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSurveyValue(String value) {
        this.surveyValue = value;
    }

}
