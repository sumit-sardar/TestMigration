
package com.ctb.prism.web.controller;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for studentBioTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="studentBioTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="birthDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="chrnlgclAge" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dataChanged" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="examineeId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="firstName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="gender" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="grade" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lastName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="middleInit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="oasStudentId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "studentBioTO", propOrder = {
    "birthDate",
    "chrnlgclAge",
    "dataChanged",
    "examineeId",
    "firstName",
    "gender",
    "grade",
    "lastName",
    "middleInit",
    "oasStudentId"
})
public class StudentBioTO {

    protected String birthDate;
    protected String chrnlgclAge;
    protected boolean dataChanged;
    protected String examineeId;
    protected String firstName;
    protected String gender;
    protected String grade;
    protected String lastName;
    protected String middleInit;
    protected String oasStudentId;

    /**
     * Gets the value of the birthDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the value of the birthDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBirthDate(String value) {
        this.birthDate = value;
    }

    /**
     * Gets the value of the chrnlgclAge property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChrnlgclAge() {
        return chrnlgclAge;
    }

    /**
     * Sets the value of the chrnlgclAge property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChrnlgclAge(String value) {
        this.chrnlgclAge = value;
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
     * Gets the value of the examineeId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExamineeId() {
        return examineeId;
    }

    /**
     * Sets the value of the examineeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExamineeId(String value) {
        this.examineeId = value;
    }

    /**
     * Gets the value of the firstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the value of the firstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstName(String value) {
        this.firstName = value;
    }

    /**
     * Gets the value of the gender property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the value of the gender property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGender(String value) {
        this.gender = value;
    }

    /**
     * Gets the value of the grade property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGrade() {
        return grade;
    }

    /**
     * Sets the value of the grade property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGrade(String value) {
        this.grade = value;
    }

    /**
     * Gets the value of the lastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the value of the lastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastName(String value) {
        this.lastName = value;
    }

    /**
     * Gets the value of the middleInit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMiddleInit() {
        return middleInit;
    }

    /**
     * Sets the value of the middleInit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMiddleInit(String value) {
        this.middleInit = value;
    }

    /**
     * Gets the value of the oasStudentId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOasStudentId() {
        return oasStudentId;
    }

    /**
     * Sets the value of the oasStudentId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOasStudentId(String value) {
        this.oasStudentId = value;
    }

}
