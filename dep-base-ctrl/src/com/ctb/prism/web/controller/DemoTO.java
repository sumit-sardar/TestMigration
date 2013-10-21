
package com.ctb.prism.web.controller;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for demoTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="demoTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="demoName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="demovalue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "demoTO", propOrder = {
    "demoName",
    "demovalue"
})
public class DemoTO {

    protected String demoName;
    protected String demovalue;

    /**
     * Gets the value of the demoName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDemoName() {
        return demoName;
    }

    /**
     * Sets the value of the demoName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDemoName(String value) {
        this.demoName = value;
    }

    /**
     * Gets the value of the demovalue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDemovalue() {
        return demovalue;
    }

    /**
     * Sets the value of the demovalue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDemovalue(String value) {
        this.demovalue = value;
    }

}
