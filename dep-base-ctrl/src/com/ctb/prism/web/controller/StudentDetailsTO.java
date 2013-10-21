
package com.ctb.prism.web.controller;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for studentDetailsTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="studentDetailsTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="studentBioTO" type="{http://controller.web.prism.ctb.com/}studentBioTO" minOccurs="0"/>
 *         &lt;element name="studentDemoTO" type="{http://controller.web.prism.ctb.com/}studentDemoTO" minOccurs="0"/>
 *         &lt;element name="studentSurveyBioTO" type="{http://controller.web.prism.ctb.com/}studentSurveyBioTO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "studentDetailsTO", propOrder = {
    "studentBioTO",
    "studentDemoTO",
    "studentSurveyBioTO"
})
public class StudentDetailsTO {

    protected StudentBioTO studentBioTO;
    protected StudentDemoTO studentDemoTO;
    protected StudentSurveyBioTO studentSurveyBioTO;

    /**
     * Gets the value of the studentBioTO property.
     * 
     * @return
     *     possible object is
     *     {@link StudentBioTO }
     *     
     */
    public StudentBioTO getStudentBioTO() {
        return studentBioTO;
    }

    /**
     * Sets the value of the studentBioTO property.
     * 
     * @param value
     *     allowed object is
     *     {@link StudentBioTO }
     *     
     */
    public void setStudentBioTO(StudentBioTO value) {
        this.studentBioTO = value;
    }

    /**
     * Gets the value of the studentDemoTO property.
     * 
     * @return
     *     possible object is
     *     {@link StudentDemoTO }
     *     
     */
    public StudentDemoTO getStudentDemoTO() {
        return studentDemoTO;
    }

    /**
     * Sets the value of the studentDemoTO property.
     * 
     * @param value
     *     allowed object is
     *     {@link StudentDemoTO }
     *     
     */
    public void setStudentDemoTO(StudentDemoTO value) {
        this.studentDemoTO = value;
    }

    /**
     * Gets the value of the studentSurveyBioTO property.
     * 
     * @return
     *     possible object is
     *     {@link StudentSurveyBioTO }
     *     
     */
    public StudentSurveyBioTO getStudentSurveyBioTO() {
        return studentSurveyBioTO;
    }

    /**
     * Sets the value of the studentSurveyBioTO property.
     * 
     * @param value
     *     allowed object is
     *     {@link StudentSurveyBioTO }
     *     
     */
    public void setStudentSurveyBioTO(StudentSurveyBioTO value) {
        this.studentSurveyBioTO = value;
    }

}
