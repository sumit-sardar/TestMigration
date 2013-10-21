
package com.ctb.prism.web.controller;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for rosterDetailsTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="rosterDetailsTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="collContentDetailsTO" type="{http://controller.web.prism.ctb.com/}contentDetailsTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="custHierarchyDetailsTO" type="{http://controller.web.prism.ctb.com/}custHierarchyDetailsTO" minOccurs="0"/>
 *         &lt;element name="rosterId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="studentDetailsTO" type="{http://controller.web.prism.ctb.com/}studentDetailsTO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rosterDetailsTO", propOrder = {
    "collContentDetailsTO",
    "custHierarchyDetailsTO",
    "rosterId",
    "studentDetailsTO"
})
public class RosterDetailsTO {

    @XmlElement(nillable = true)
    protected List<ContentDetailsTO> collContentDetailsTO;
    protected CustHierarchyDetailsTO custHierarchyDetailsTO;
    protected String rosterId;
    protected StudentDetailsTO studentDetailsTO;

    /**
     * Gets the value of the collContentDetailsTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the collContentDetailsTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCollContentDetailsTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ContentDetailsTO }
     * 
     * 
     */
    public List<ContentDetailsTO> getCollContentDetailsTO() {
        if (collContentDetailsTO == null) {
            collContentDetailsTO = new ArrayList<ContentDetailsTO>();
        }
        return this.collContentDetailsTO;
    }

    /**
     * Gets the value of the custHierarchyDetailsTO property.
     * 
     * @return
     *     possible object is
     *     {@link CustHierarchyDetailsTO }
     *     
     */
    public CustHierarchyDetailsTO getCustHierarchyDetailsTO() {
        return custHierarchyDetailsTO;
    }

    /**
     * Sets the value of the custHierarchyDetailsTO property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustHierarchyDetailsTO }
     *     
     */
    public void setCustHierarchyDetailsTO(CustHierarchyDetailsTO value) {
        this.custHierarchyDetailsTO = value;
    }

    /**
     * Gets the value of the rosterId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRosterId() {
        return rosterId;
    }

    /**
     * Sets the value of the rosterId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRosterId(String value) {
        this.rosterId = value;
    }

    /**
     * Gets the value of the studentDetailsTO property.
     * 
     * @return
     *     possible object is
     *     {@link StudentDetailsTO }
     *     
     */
    public StudentDetailsTO getStudentDetailsTO() {
        return studentDetailsTO;
    }

    /**
     * Sets the value of the studentDetailsTO property.
     * 
     * @param value
     *     allowed object is
     *     {@link StudentDetailsTO }
     *     
     */
    public void setStudentDetailsTO(StudentDetailsTO value) {
        this.studentDetailsTO = value;
    }

}
