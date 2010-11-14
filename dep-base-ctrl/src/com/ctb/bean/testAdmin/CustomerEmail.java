package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import java.sql.Clob;
import java.util.Date;

/**
 * CustomerEmail.java
 * @author Tata Consultancy Services
 * 
 * Data bean representing the contents of the OAS.CUSTOMER_EMAIL_CONFIG table
 */
public class CustomerEmail extends CTBBean { 
    
    static final long serialVersionUID = 1L;
    private Integer customerId;
    private Integer emailType;
    private String replyTo;
    private String subject;
    private transient Clob emailBody;
    private String emailBodyStr;
    private Date createdDateTime;
    private Integer createdBy;
    private String contactPhone;
    
    /**
     * @return Returns the customerId.
     */
    public Integer getCustomerId() {
        return this.customerId;
    }
    
    /**
     * @param customerId The customerId to set.
     */    
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    /**
     * @return Returns the email type.
     */
    public Integer getEmailType() {
        return this.emailType;
    }
    
    /**
     * @param emailType The email type. value should be one of 1,2,3.
     */    
    public void setEmailType(Integer emailType) {
        this.emailType = emailType;
    }

    /**
     * @return Returns the reply-to mail address for the customer.
     */
    public String getReplyTo() {
        return this.replyTo;
    }
    
    /**
     * @param replyTo reply-to mail address
     */    
    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }
    
    /**
     * @return Returns the subject of the mail.
     */
    public String getSubject() {
        return this.subject;
    }
    
    /**
     * @param subject subject of the mail
     */    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    /**
     * @return Returns the email clob body
     */
    public Clob getEmailBody() {
        return emailBody;
    }
    
    /**
     * @param emailBody sets the emailBody
     */
    public void setEmailBody(Clob emailBody) {
        this.emailBody = emailBody;
        setEmailBodyStr();
    }
    
    /**
     * @return Returns the email clob body in string value
     */
    public String getEmailBodyStr() {
        if (emailBodyStr == null && emailBody != null) {
            setEmailBodyStr();
        }
        return this.emailBodyStr;
    }

    /**
     * @param emailBodyStr sets the emailBodyStr
     */    
    public void setEmailBodyStr(String emailBodyStr) {
        this.emailBodyStr = emailBodyStr;
    }
    
    private void setEmailBodyStr() {
        if (emailBody == null) {
            emailBodyStr = null;
            return;
        }
        try {                                        
            int len = (int) emailBody.length();            
            String content = emailBody.getSubString(1, len);
            this.emailBodyStr = content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    /**
	 * @return Returns the createdDateTime.
	 */
	public Date getCreatedDateTime() {
		return createdDateTime;
	}
	/**
	 * @param createdDateTime The createdDateTime to set.
	 */
	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
    
    /**
	 * @return Returns the createdBy.
	 */
	public Integer getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy The createdBy to set.
	 */
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
    
    /**
	 * @return Returns the contactPhone.
	 */
	public String getContactPhone() {
		return contactPhone;
	}
	/**
	 * @param contactPhone The contactPhone to set.
	 */
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
    
} 
