
package com.ctb.prism.web.controller;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for loadStudentData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="loadStudentData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="StudentListTO" type="{http://controller.web.prism.ctb.com/}studentListTO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "loadStudentData", propOrder = {
    "studentListTO"
})
public class LoadStudentData {

    @XmlElement(name = "StudentListTO")
    protected StudentListTO studentListTO;

    /**
     * Gets the value of the studentListTO property.
     * 
     * @return
     *     possible object is
     *     {@link StudentListTO }
     *     
     */
    public StudentListTO getStudentListTO() {
        return studentListTO;
    }

    /**
     * Sets the value of the studentListTO property.
     * 
     * @param value
     *     allowed object is
     *     {@link StudentListTO }
     *     
     */
    public void setStudentListTO(StudentListTO value) {
        this.studentListTO = value;
    }

}
