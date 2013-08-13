package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import java.util.Date;

/**
 * Data bean representing the partial contents of the OAS.User and OAS.Password_History table 
 * 
 * 
 * @author Tata Consultency Services
 */
public class PasswordHistory extends CTBBean
{ 
	static final long serialVersionUID = 1L;
    private Integer userId;
    private String password;
    private Date createdDate;
    
    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    public Integer getUserId()
    {
        return this.userId;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getPassword()
    {
        return this.password;
    }
    
    public void setCreatedDate(Date createdDate)
    {
        this.createdDate = createdDate;
    }

    public Date getCreatedDate()
    {
        return this.createdDate;
    }
} 
