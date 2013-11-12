
package com.ctb.prism.web.controller;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for studentDataLoadTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="studentDataLoadTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dataLoadId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="errorMessages" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="objectiveDetailsId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="partitionName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="processId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="statusCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="studentBioDetailsId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="subtestDetailsId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="success" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="summary" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "studentDataLoadTO", propOrder = {
    "dataLoadId",
    "errorMessages",
    "objectiveDetailsId",
    "partitionName",
    "processId",
    "status",
    "statusCode",
    "studentBioDetailsId",
    "subtestDetailsId",
    "success",
    "summary"
})
public class StudentDataLoadTO {

    protected String dataLoadId;
    @XmlElement(nillable = true)
    protected List<String> errorMessages;
    protected long objectiveDetailsId;
    protected String partitionName;
    protected long processId;
    protected String status;
    protected int statusCode;
    protected long studentBioDetailsId;
    protected long subtestDetailsId;
    protected boolean success;
    protected String summary;

    /**
     * Gets the value of the dataLoadId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataLoadId() {
        return dataLoadId;
    }

    /**
     * Sets the value of the dataLoadId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataLoadId(String value) {
        this.dataLoadId = value;
    }

    /**
     * Gets the value of the errorMessages property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the errorMessages property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getErrorMessages().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getErrorMessages() {
        if (errorMessages == null) {
            errorMessages = new ArrayList<String>();
        }
        return this.errorMessages;
    }

    /**
     * Gets the value of the objectiveDetailsId property.
     * 
     */
    public long getObjectiveDetailsId() {
        return objectiveDetailsId;
    }

    /**
     * Sets the value of the objectiveDetailsId property.
     * 
     */
    public void setObjectiveDetailsId(long value) {
        this.objectiveDetailsId = value;
    }

    /**
     * Gets the value of the partitionName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPartitionName() {
        return partitionName;
    }

    /**
     * Sets the value of the partitionName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPartitionName(String value) {
        this.partitionName = value;
    }

    /**
     * Gets the value of the processId property.
     * 
     */
    public long getProcessId() {
        return processId;
    }

    /**
     * Sets the value of the processId property.
     * 
     */
    public void setProcessId(long value) {
        this.processId = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the statusCode property.
     * 
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Sets the value of the statusCode property.
     * 
     */
    public void setStatusCode(int value) {
        this.statusCode = value;
    }

    /**
     * Gets the value of the studentBioDetailsId property.
     * 
     */
    public long getStudentBioDetailsId() {
        return studentBioDetailsId;
    }

    /**
     * Sets the value of the studentBioDetailsId property.
     * 
     */
    public void setStudentBioDetailsId(long value) {
        this.studentBioDetailsId = value;
    }

    /**
     * Gets the value of the subtestDetailsId property.
     * 
     */
    public long getSubtestDetailsId() {
        return subtestDetailsId;
    }

    /**
     * Sets the value of the subtestDetailsId property.
     * 
     */
    public void setSubtestDetailsId(long value) {
        this.subtestDetailsId = value;
    }

    /**
     * Gets the value of the success property.
     * 
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Sets the value of the success property.
     * 
     */
    public void setSuccess(boolean value) {
        this.success = value;
    }

    /**
     * Gets the value of the summary property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Sets the value of the summary property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSummary(String value) {
        this.summary = value;
    }

}
