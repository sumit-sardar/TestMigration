package com.ctb.util;

import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

public class TransactionPersistenceUtil {
	
	static final long serialVersionUID = 1L;
	
	   /**
	 * This method return UserTransaction instance
	 * @return UserTransaction
	 */

    public UserTransaction getTransaction() {

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

    public void closeTransaction (UserTransaction userTransaction, boolean flag) {

		try {

			System.out.println("Close transaction");
			if(userTransaction != null && !flag) {
				System.out.println("Commit transaction");
				userTransaction.commit();
			}

		} catch (Exception e) {

			e.printStackTrace();
			try {
				if(userTransaction != null) {

					userTransaction.rollback();
				System.out.println("Rollback transaction");
				}
			} catch (Exception e1) {

				e1.printStackTrace();

			}

		} 

	}
    

}
