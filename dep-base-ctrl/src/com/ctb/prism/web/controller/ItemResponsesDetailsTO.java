
package com.ctb.prism.web.controller;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for itemResponsesDetailsTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="itemResponsesDetailsTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="itemResponseTO" type="{http://controller.web.prism.ctb.com/}itemResponseTO" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "itemResponsesDetailsTO", propOrder = {
    "itemResponseTO"
})
public class ItemResponsesDetailsTO {

    @XmlElement(nillable = true)
    protected List<ItemResponseTO> itemResponseTO;

    /**
     * Gets the value of the itemResponseTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the itemResponseTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getItemResponseTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ItemResponseTO }
     * 
     * 
     */
    public List<ItemResponseTO> getItemResponseTO() {
        if (itemResponseTO == null) {
            itemResponseTO = new ArrayList<ItemResponseTO>();
        }
        return this.itemResponseTO;
    }

}
