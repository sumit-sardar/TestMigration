
package com.ctb.prism.web.controller;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for contentDetailsTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="contentDetailsTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="collObjectiveScoreDetailsTO" type="{http://controller.web.prism.ctb.com/}objectiveScoreDetailsTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="contentCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="contentScoreDetailsTO" type="{http://controller.web.prism.ctb.com/}contentScoreDetailsTO" minOccurs="0"/>
 *         &lt;element name="dataChanged" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="dateTestTaken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="itemResponsesDetailsTO" type="{http://controller.web.prism.ctb.com/}itemResponsesDetailsTO" minOccurs="0"/>
 *         &lt;element name="scoringMethod" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="statusCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subtestAccommodationsTO" type="{http://controller.web.prism.ctb.com/}subtestAccommodationsTO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "contentDetailsTO", propOrder = {
    "collObjectiveScoreDetailsTO",
    "contentCode",
    "contentScoreDetailsTO",
    "dataChanged",
    "dateTestTaken",
    "itemResponsesDetailsTO",
    "scoringMethod",
    "statusCode",
    "subtestAccommodationsTO"
})
public class ContentDetailsTO {

    @XmlElement(nillable = true)
    protected List<ObjectiveScoreDetailsTO> collObjectiveScoreDetailsTO;
    protected String contentCode;
    protected ContentScoreDetailsTO contentScoreDetailsTO;
    protected boolean dataChanged;
    protected String dateTestTaken;
    protected ItemResponsesDetailsTO itemResponsesDetailsTO;
    protected String scoringMethod;
    protected String statusCode;
    protected SubtestAccommodationsTO subtestAccommodationsTO;

    /**
     * Gets the value of the collObjectiveScoreDetailsTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the collObjectiveScoreDetailsTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCollObjectiveScoreDetailsTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ObjectiveScoreDetailsTO }
     * 
     * 
     */
    public List<ObjectiveScoreDetailsTO> getCollObjectiveScoreDetailsTO() {
        if (collObjectiveScoreDetailsTO == null) {
            collObjectiveScoreDetailsTO = new ArrayList<ObjectiveScoreDetailsTO>();
        }
        return this.collObjectiveScoreDetailsTO;
    }

    /**
     * Gets the value of the contentCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContentCode() {
        return contentCode;
    }

    /**
     * Sets the value of the contentCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContentCode(String value) {
        this.contentCode = value;
    }

    /**
     * Gets the value of the contentScoreDetailsTO property.
     * 
     * @return
     *     possible object is
     *     {@link ContentScoreDetailsTO }
     *     
     */
    public ContentScoreDetailsTO getContentScoreDetailsTO() {
        return contentScoreDetailsTO;
    }

    /**
     * Sets the value of the contentScoreDetailsTO property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContentScoreDetailsTO }
     *     
     */
    public void setContentScoreDetailsTO(ContentScoreDetailsTO value) {
        this.contentScoreDetailsTO = value;
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

    /**
     * Gets the value of the dateTestTaken property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateTestTaken() {
        return dateTestTaken;
    }

    /**
     * Sets the value of the dateTestTaken property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateTestTaken(String value) {
        this.dateTestTaken = value;
    }

    /**
     * Gets the value of the itemResponsesDetailsTO property.
     * 
     * @return
     *     possible object is
     *     {@link ItemResponsesDetailsTO }
     *     
     */
    public ItemResponsesDetailsTO getItemResponsesDetailsTO() {
        return itemResponsesDetailsTO;
    }

    /**
     * Sets the value of the itemResponsesDetailsTO property.
     * 
     * @param value
     *     allowed object is
     *     {@link ItemResponsesDetailsTO }
     *     
     */
    public void setItemResponsesDetailsTO(ItemResponsesDetailsTO value) {
        this.itemResponsesDetailsTO = value;
    }

    /**
     * Gets the value of the scoringMethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScoringMethod() {
        return scoringMethod;
    }

    /**
     * Sets the value of the scoringMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScoringMethod(String value) {
        this.scoringMethod = value;
    }

    /**
     * Gets the value of the statusCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatusCode() {
        return statusCode;
    }

    /**
     * Sets the value of the statusCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatusCode(String value) {
        this.statusCode = value;
    }

    /**
     * Gets the value of the subtestAccommodationsTO property.
     * 
     * @return
     *     possible object is
     *     {@link SubtestAccommodationsTO }
     *     
     */
    public SubtestAccommodationsTO getSubtestAccommodationsTO() {
        return subtestAccommodationsTO;
    }

    /**
     * Sets the value of the subtestAccommodationsTO property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubtestAccommodationsTO }
     *     
     */
    public void setSubtestAccommodationsTO(SubtestAccommodationsTO value) {
        this.subtestAccommodationsTO = value;
    }

}
