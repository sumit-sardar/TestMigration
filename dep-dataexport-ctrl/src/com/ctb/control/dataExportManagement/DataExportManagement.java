
package com.ctb.control.dataExportManagement;


import org.apache.beehive.controls.api.bean.ControlInterface;

import com.ctb.exception.CTBBusinessException;

/** 
 *
 * @author John_Wang
 */ 
@ControlInterface()
public interface DataExportManagement 
{ 

    /**
     * Get user information including full name and system id for the specified
     * user name. If the specified user lies withing the requesting user's
     * visible hierarchy, all fields are returned - if not, only the first, last,
     * and middle names of the specified user are returned. Each user object
     * contains a Customer object, which contains information about the user's
     * customer, including a flag, hideAccommodations, which indicates whether
     * accommodation-related UI elements should be hidden for the specified user.
     * @param userName - identifies the calling user
     * @param detailUserName - identifies the user whose information is desired
     * @return User
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.User getUserDetails(java.lang.String userName, java.lang.String detailUserName) throws com.ctb.exception.CTBBusinessException;

    
   /**
     * Retrieves the no of reports available to a user's customer
     * @param userName - identifies the user
     * @param customerId - identifies the customer
     * @return boolean value
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    boolean userHasReports(java.lang.String userName, java.lang.Integer customerId) throws com.ctb.exception.CTBBusinessException;


	
} 	


