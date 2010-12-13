package com.ctb.control.validation; 


import com.bea.control.annotations.TransactionAttribute;
import com.bea.control.annotations.TransactionAttributeType;
import org.apache.beehive.controls.api.bean.ControlInterface;

/**
 * Validator.java
 * @author Nate_Cohen
 *
 * Validates platform requests by confirming that the user is allowed
 * to make the request and that the operands, if any, specify data
 * which is properly visible to that user
 */
@ControlInterface()
public interface Validator 
{ 
    /**
	 * Validate the request
	 * @param operator - the request actor, typically a user name
	 * @param operand - the data on which the operation is requested
	 * @param action - the operation which the operator wants to perform
	 * @throws com.ctb.exception.validation.ValidationException
	 */
    
    void validate(java.lang.Object operator, java.lang.Object operand, java.lang.String action) throws com.ctb.exception.validation.ValidationException;

    
    void validateNode(java.lang.Object operator, java.lang.Object operand, java.lang.String action) throws com.ctb.exception.validation.ValidationException;

    
    void validateAdmin(java.lang.Object operator, java.lang.Object operand, java.lang.String action) throws com.ctb.exception.validation.ValidationException;

    
    void validateItemSet(java.lang.Object operator, java.lang.Object operand, java.lang.String action) throws com.ctb.exception.validation.ValidationException;

    
    void validateProduct(java.lang.Object operator, java.lang.Object operand, java.lang.String action) throws com.ctb.exception.validation.ValidationException;

    
    void validateRoster(java.lang.Object operator, java.lang.Object operand, java.lang.String action) throws com.ctb.exception.validation.ValidationException;

    
    void validateUser(java.lang.Object operator, java.lang.Object operand, java.lang.String action) throws com.ctb.exception.validation.ValidationException;

    
    void validateStudent(java.lang.Object operator, java.lang.Object operand, java.lang.String action) throws com.ctb.exception.validation.ValidationException;

    
    void validateCustomer(java.lang.Object operator, java.lang.Object operand, java.lang.String action) throws com.ctb.exception.validation.ValidationException;
    
    void validateStudentAcrossOrg(Object operator, Object operand, String action) throws com.ctb.exception.validation.ValidationException;
    
    void validateStuOrgIsInUserScope(Object operator, Object operand1, Object operand2, String action) throws com.ctb.exception.validation.ValidationException;
} 
