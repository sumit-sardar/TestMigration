package com.ctb.control.testAdmin; 

import com.bea.control.annotations.TransactionAttribute;
import com.bea.control.annotations.TransactionAttributeType;
import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.ProgramData;
import com.ctb.exception.CTBBusinessException;
import org.apache.beehive.controls.api.bean.ControlInterface;

/**
 * Platform control provides functions related to program status.
 * 
 * @author John_Wang
 */

@ControlInterface()
public interface ProgramStatus 
{ 

    /**
     * Retrieves a filtered, sorted, paged list of tests under the specified
     * program.
     * @param userName - identifies the user
     * @param programId - identifies the program
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return TestElementData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.TestElementData getTestsForProgram(java.lang.String userName, java.lang.Integer programId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a list of subtest status for the specified item set id under the
     * specified program at/below specified organization.
     * @param userName - identifies the user
     * @param programId - identifies the program
     * @param orgNodeId - identifies the organization
     * @param itemSetId - identifies the item set (TC)
     * @return TestStatus []
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.TestStatus[] getSubtestStatusForProgram(java.lang.String userName, java.lang.Integer programId, java.lang.Integer orgNodeId, java.lang.Integer itemSetId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves test status for the specified item set id under the specified
     * program at/below specified organization.
     * @param userName - identifies the user
     * @param programId - identifies the program
     * @param orgNodeId - identifies the organization
     * @param itemSetId - identifies the item set (TC)
     * @return TestStatus []
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.TestStatus getTestStatusForProgram(java.lang.String userName, java.lang.Integer programId, java.lang.Integer orgNodeId, java.lang.Integer itemSetId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves the set of sessions for the specified item set id under the
     * specified program at/below specified organization.
     * @param userName - identifies the user
     * @param programId - identifies the program
     * @param orgNodeId - identifies the organization
     * @param itemSetIdTC - identifies the item set (TC)
     * @param itemSetIdTS - identifies the item set (TS)
     * @param status - identifies completion status (Scheduled, Started, Completed)
     * @return ProgramStatusSessionData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.ProgramStatusSessionData getProgramStatusSessions(java.lang.String userName, java.lang.Integer programId, java.lang.Integer orgNodeId, java.lang.Integer itemSetIdTC, java.lang.Integer itemSetIdTS, java.lang.String status, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves the bottom two org node category ids in an array
     * @param userName - identifies the user
     * @param customerId - identifies the customer
     * @return Integer []
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    java.lang.Integer[] getBottomTwoOrgNodeCategoryIdsForCustomer(java.lang.String userName, java.lang.Integer customerId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves the set of active programs available to a user's customer
     * @param userName - identifies the user
     * @return ProgramData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.ProgramData getActiveProgramsForUser(java.lang.String userName, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Determines whether user has program status configured
     * @param userName - identifies the user
     * @return Boolean
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    java.lang.Boolean hasProgramStatusConfig(java.lang.String userName) throws com.ctb.exception.CTBBusinessException;
} 
