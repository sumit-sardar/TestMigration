
package com.ctb.prism.web.controller;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for custHierarchyDetailsTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="custHierarchyDetailsTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="collOrgDetailsTO" type="{http://controller.web.prism.ctb.com/}orgDetailsTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="customerId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dataChanged" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="maxHierarchy" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "custHierarchyDetailsTO", propOrder = {
    "collOrgDetailsTO",
    "customerId",
    "dataChanged",
    "maxHierarchy"
})
public class CustHierarchyDetailsTO {

    @XmlElement(nillable = true)
    protected List<OrgDetailsTO> collOrgDetailsTO;
    protected String customerId;
    protected boolean dataChanged;
    protected String maxHierarchy;

    /**
     * Gets the value of the collOrgDetailsTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the collOrgDetailsTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCollOrgDetailsTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OrgDetailsTO }
     * 
     * 
     */
    public List<OrgDetailsTO> getCollOrgDetailsTO() {
        if (collOrgDetailsTO == null) {
            collOrgDetailsTO = new ArrayList<OrgDetailsTO>();
        }
        return this.collOrgDetailsTO;
    }

    /**
     * Gets the value of the customerId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * Sets the value of the customerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerId(String value) {
        this.customerId = value;
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
     * Gets the value of the maxHierarchy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxHierarchy() {
        return maxHierarchy;
    }

    /**
     * Sets the value of the maxHierarchy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxHierarchy(String value) {
        this.maxHierarchy = value;
    }

}
