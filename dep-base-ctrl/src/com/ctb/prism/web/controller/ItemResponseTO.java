
package com.ctb.prism.web.controller;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for itemResponseTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="itemResponseTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="editedResponse" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="itemCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="itemSetType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="responseStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="scoreValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "itemResponseTO", propOrder = {
    "editedResponse",
    "itemCode",
    "itemSetType",
    "responseStatus",
    "scoreValue"
})
public class ItemResponseTO {

    protected String editedResponse;
    protected String itemCode;
    protected String itemSetType;
    protected String responseStatus;
    protected String scoreValue;

    /**
     * Gets the value of the editedResponse property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEditedResponse() {
        return editedResponse;
    }

    /**
     * Sets the value of the editedResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEditedResponse(String value) {
        this.editedResponse = value;
    }

    /**
     * Gets the value of the itemCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemCode() {
        return itemCode;
    }

    /**
     * Sets the value of the itemCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemCode(String value) {
        this.itemCode = value;
    }

    /**
     * Gets the value of the itemSetType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemSetType() {
        return itemSetType;
    }

    /**
     * Sets the value of the itemSetType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemSetType(String value) {
        this.itemSetType = value;
    }

    /**
     * Gets the value of the responseStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseStatus() {
        return responseStatus;
    }

    /**
     * Sets the value of the responseStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseStatus(String value) {
        this.responseStatus = value;
    }

    /**
     * Gets the value of the scoreValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScoreValue() {
        return scoreValue;
    }

    /**
     * Sets the value of the scoreValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScoreValue(String value) {
        this.scoreValue = value;
    }

}
