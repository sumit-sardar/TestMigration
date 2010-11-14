package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;

/**
 * Data bean representing a customer SDS assignment
 * as present in the ADS data store
 * 
 * @author Nate_Cohen
 */
public class CustomerSDS extends CTBBean
{ 
    private String name;
    private String token;
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getToken() {
        return this.token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
} 
