package com.ctb.control.validation; 

import java.io.Serializable;

import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

import org.apache.beehive.controls.api.bean.ControlImplementation;

import com.ctb.exception.validation.ValidationException;

/**
 * @editor-info:code-gen control-interface="true"
 */
@ControlImplementation(isTransient=true)
public class ValidatorImpl implements Validator
{ 
	/**
	 * @common:control
	 */
	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.Customer customers;

	/**
	 * @common:control
	 */
	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.Students students;

	/**
	 * @common:control
	 */
	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.Users user;

	/**
	 * @common:control
	 */
	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.TestRoster roster;

	/**
	 * @common:control
	 */
	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.Product product;

	/**
	 * @common:control
	 */
	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.ItemSet itemSet;

	/**
	 * @common:control
	 */
	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.TestAdmin admin;

	/**
	 * @common:control
	 */
	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.OrgNode node;

	///**
	// * @common:control
	// */
	//private com.ctb.control.db.SecurityPolicy policy;

	static final long serialVersionUID = 1L;

	/**
	 * @common:operation
	 */
	public void validate(Object operator, Object operand, String action) throws ValidationException
	{
		return;
		try {
			//policy.checkPolicy(operator, action);
		} catch (Exception e) {
			ValidationException ve = new ValidationException("ValidatorImpl: validate: failed validation for user: " + operator);
			ve.setStackTrace(e.getStackTrace());
			throw ve;
		}
	}

	/**
	 * @common:operation
	 */
	public void validateNode(Object operator, Object operand, String action) throws ValidationException
	{
		return;
		try {
			String userName = (String) operator;
			if(operand != null) {
				Integer orgNodeId = (Integer) operand;
				/*//Programmatic Transaction
				UserTransaction userTrans= getTransaction();
				userTrans.begin();*/
				if(!"true".equals(node.checkVisibility(userName, orgNodeId)))
					throw new ValidationException("ValidatorImpl: validateNode: failed validation for user: " + operator + " on: " + operand);;
					//Close Transaction
					//closeTransaction(userTrans); 
			}
			//policy.checkPolicy(userName, action);
		} catch (Exception e) {
			ValidationException ve = new ValidationException("ValidatorImpl: validateNode: failed validation for user: " + operator + " on: " + operand);
			ve.setStackTrace(e.getStackTrace());
			throw ve;
		}
	}

	/**
	 * @common:operation
	 */
	public void validateAdmin(Object operator, Object operand, String action) throws ValidationException
	{ 
		return;
		try {
			String userName = (String) operator;
			if(operand != null) {
				Integer testAdminId = (Integer) operand;
				/*//Programmatic Transaction
				UserTransaction userTrans= getTransaction();
				userTrans.begin();*/
				if(!"true".equals(admin.checkVisibility(userName, testAdminId)))
					throw new ValidationException("ValidatorImpl: validateAdmin: failed validation for user: " + operator + " on: " + operand);;
					//Close Transaction
					//closeTransaction(userTrans); 
			}

			//policy.checkPolicy(userName, action);
		} catch (Exception e) {
			ValidationException ve = new ValidationException("ValidatorImpl: validateAdmin: failed validation for user: " + operator + " on: " + operand);
			ve.setStackTrace(e.getStackTrace());
			throw ve;
		}
	}

	/**
	 * @common:operation
	 */
	public void validateItemSet(Object operator, Object operand, String action) throws ValidationException
	{
		return;
		try {
			String userName = (String) operator;
			if(operand != null) {
				Integer itemSetId = (Integer) operand;
				/*//Programmatic Transaction
				UserTransaction userTrans= getTransaction();
				userTrans.begin();*/
				if (!"true".equals(itemSet.checkVisibility(userName, itemSetId)))
					throw new ValidationException("ValidatorImpl: validateItemSet: failed validation for user: " + operator + " on: " + operand);;
					//Close Transaction
					//closeTransaction(userTrans); 
			}
			//policy.checkPolicy(userName, action);
		} catch (Exception e) {
			ValidationException ve = new ValidationException("ValidatorImpl: validateItemSet: failed validation for user: " + operator + " on: " + operand);
			ve.setStackTrace(e.getStackTrace());
			throw ve;
		}
	}

	/**
	 * @common:operation
	 */
	public void validateProduct(Object operator, Object operand, String action) throws ValidationException
	{
		return;
		try {
			String userName = (String) operator;
			if(operand != null) {
				Integer productId = (Integer) operand;
				/*//Programmatic Transaction
				UserTransaction userTrans= getTransaction();
				userTrans.begin();*/
				if(!"true".equals(product.checkVisibility(userName, productId)))
					throw new ValidationException("ValidatorImpl: validateProduct: failed validation for user: " + operator + " on: " + operand);;
					//Close Transaction
					//closeTransaction(userTrans); 
			}
			//policy.checkPolicy(userName, action);
		} catch (Exception e) {
			ValidationException ve = new ValidationException("ValidatorImpl: validateProduct: failed validation for user: " + operator + " on: " + operand);
			ve.setStackTrace(e.getStackTrace());
			throw ve;
		}
	}

	/**
	 * @common:operation
	 */
	public void validateRoster(Object operator, Object operand, String action) throws ValidationException
	{
		return;
		try {
			String userName = (String) operator;
			if(operand != null) {
				Integer testRosterId = (Integer) operand;
				/*//Programmatic Transaction
				UserTransaction userTrans= getTransaction();
				userTrans.begin();*/
				if(!"true".equals(roster.checkVisibility(userName, testRosterId)))
					throw new ValidationException("ValidatorImpl: validateRoster: failed validation for user: " + operator + " on: " + operand);;
					//Close Transaction
				//closeTransaction(userTrans); 
			}
			//policy.checkPolicy(userName, action);
		} catch (Exception e) {
			ValidationException ve = new ValidationException("ValidatorImpl: validateRoster: failed validation for user: " + operator + " on: " + operand);
			ve.setStackTrace(e.getStackTrace());
			throw ve;
		}
	}

	/**
	 * @common:operation
	 */
	public void validateUser(Object operator, Object operand, String action) throws ValidationException
	{
		return;
		try {
			String userName = (String) operator;

			if(operand != null) {
				String otherName = (String) operand;
				
				//Programmatic Transaction
				//UserTransaction userTrans= getTransaction();
				//userTrans.begin();
				
				if(!"true".equals(user.checkVisibility(userName, otherName)))

					throw new ValidationException("ValidatorImpl: validateUser: failed validation for user: " + operator + " on: " + operand);;
					//Close Transaction
				//closeTransaction(userTrans);   
			}
			//policy.checkPolicy(userName, action);
		} catch (Exception e) {
			ValidationException ve = new ValidationException("ValidatorImpl: validateUser: failed validation for user: " + operator + " on: " + operand);
			ve.setStackTrace(e.getStackTrace());
			throw ve;
		}
	}

	/**
	 * @common:operation
	 */
	public void validateStudent(Object operator, Object operand, String action) throws ValidationException
	{
		return;
		try {
			String userName = (String) operator;
			if(operand != null) {
				Integer studentId  = (Integer) operand;
				if(!"true".equals(students.checkVisibility(userName, studentId)))
					throw new ValidationException("ValidatorImpl: validateStudent: failed validation for student: " + operator + " on: " + operand);;

			}
			//policy.checkPolicy(userName, action);
		} catch (Exception e) {
			ValidationException ve = new ValidationException("ValidatorImpl: validateStudent: failed validation for student: " + operator + " on: " + operand);
			ve.setStackTrace(e.getStackTrace());
			throw ve;
		}
	}

	/**
	 * @common:operation
	 */
	public void validateCustomer(Object operator, Object operand, String action) throws ValidationException
	{
		return;
		try {
			String userName = (String) operator;
			if(operand != null) {
				Integer customerId  = (Integer) operand;
				//Programmatic Transaction
				/*UserTransaction userTrans= getTransaction();
				userTrans.begin();*/
				if(!"true".equals(customers.checkVisibility(userName, customerId)))
					throw new ValidationException("ValidatorImpl: validateCustomer: failed validation for customer: " + operator + " on: " + operand);;
					//Close Transaction
					//closeTransaction(userTrans);     
			}
			//policy.checkPolicy(userName, action);
		} catch (Exception e) {
			ValidationException ve = new ValidationException("ValidatorImpl: validateCustomer: failed validation for customer: " + operator + " on: " + operand);
			ve.setStackTrace(e.getStackTrace());
			throw ve;
		}
	}

	/**
	 * This method return UserTransaction instance
	 * @return UserTransaction
	 */

	private UserTransaction getTransaction () {

		UserTransaction userTransaction = null;
		try {

			InitialContext init = new InitialContext ();
			userTransaction = (UserTransaction)init.lookup("javax.transaction.UserTransaction");

		} catch (Exception e) {

			e.printStackTrace();

		}

		return userTransaction;
	}

	/**
	 * This method is used to close the transaction
	 * @param userTransaction
	 */

	private void closeTransaction (UserTransaction userTransaction) {

		try {

			userTransaction.commit();

		} catch (Exception e) {

			e.printStackTrace();
			try {
				userTransaction.rollback();

			} catch (Exception e1) {

				e1.printStackTrace();

			}

		}



	}

} 
