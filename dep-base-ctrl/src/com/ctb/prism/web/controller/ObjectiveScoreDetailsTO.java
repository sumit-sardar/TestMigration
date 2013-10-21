
package com.ctb.prism.web.controller;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for objectiveScoreDetailsTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="objectiveScoreDetailsTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="collObjectiveScoreTO" type="{http://controller.web.prism.ctb.com/}objectiveScoreTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="objectiveCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="objectiveName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "objectiveScoreDetailsTO", propOrder = {
    "collObjectiveScoreTO",
    "objectiveCode",
    "objectiveName"
})
public class ObjectiveScoreDetailsTO {

    @XmlElement(nillable = true)
    protected List<ObjectiveScoreTO> collObjectiveScoreTO;
    protected String objectiveCode;
    protected String objectiveName;

    /**
     * Gets the value of the collObjectiveScoreTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the collObjectiveScoreTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCollObjectiveScoreTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ObjectiveScoreTO }
     * 
     * 
     */
    public List<ObjectiveScoreTO> getCollObjectiveScoreTO() {
        if (collObjectiveScoreTO == null) {
            collObjectiveScoreTO = new ArrayList<ObjectiveScoreTO>();
        }
        return this.collObjectiveScoreTO;
    }

    /**
     * Gets the value of the objectiveCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObjectiveCode() {
        return objectiveCode;
    }

    /**
     * Sets the value of the objectiveCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObjectiveCode(String value) {
        this.objectiveCode = value;
    }

    /**
     * Gets the value of the objectiveName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObjectiveName() {
        return objectiveName;
    }

    /**
     * Sets the value of the objectiveName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObjectiveName(String value) {
        this.objectiveName = value;
    }

}
