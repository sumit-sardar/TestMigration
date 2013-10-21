
package com.ctb.prism.web.controller;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for contentScoreDetailsTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="contentScoreDetailsTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="collContentScoreTO" type="{http://controller.web.prism.ctb.com/}contentScoreTO" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "contentScoreDetailsTO", propOrder = {
    "collContentScoreTO"
})
public class ContentScoreDetailsTO {

    @XmlElement(nillable = true)
    protected List<ContentScoreTO> collContentScoreTO;

    /**
     * Gets the value of the collContentScoreTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the collContentScoreTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCollContentScoreTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ContentScoreTO }
     * 
     * 
     */
    public List<ContentScoreTO> getCollContentScoreTO() {
        if (collContentScoreTO == null) {
            collContentScoreTO = new ArrayList<ContentScoreTO>();
        }
        return this.collContentScoreTO;
    }

}
