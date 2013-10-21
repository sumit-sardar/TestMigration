
package com.ctb.prism.web.controller;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for studentSurveyBioTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="studentSurveyBioTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="collSurveyBioTO" type="{http://controller.web.prism.ctb.com/}surveyBioTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="dataChanged" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "studentSurveyBioTO", propOrder = {
    "collSurveyBioTO",
    "dataChanged"
})
public class StudentSurveyBioTO {

    @XmlElement(nillable = true)
    protected List<SurveyBioTO> collSurveyBioTO;
    protected boolean dataChanged;

    /**
     * Gets the value of the collSurveyBioTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the collSurveyBioTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCollSurveyBioTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SurveyBioTO }
     * 
     * 
     */
    public List<SurveyBioTO> getCollSurveyBioTO() {
        if (collSurveyBioTO == null) {
            collSurveyBioTO = new ArrayList<SurveyBioTO>();
        }
        return this.collSurveyBioTO;
    }

    /**
     * Gets the value of the dataChanged property.
     * 
     */
    public boolean isDataChanged() {
        return dataChanged;
    }

    /**
     * Sets the value of the dataChanged property.
     * 
     */
    public void setDataChanged(boolean value) {
        this.dataChanged = value;
    }

}
