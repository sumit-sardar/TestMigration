CREATE OR REPLACE TRIGGER TRG_BMTSYNC_STUDENT
AFTER INSERT OR UPDATE ON STUDENT
FOR EACH ROW 
DECLARE
   vCustomer_ID    Customer.Customer_ID%TYPE;
BEGIN

	SELECT ONS.Customer_ID 
	INTO vCustomer_ID
    FROM Org_Node_Student ONS , BMTSYNC_Customer Cust
    WHERE 
    ONS.Customer_Id = Cust.Customer_Id AND 
    ONS.Student_ID = :new.STUDENT_ID AND ROWNUM = 1; 

    /*	
     * REMOVE PKG_BMTSYNC_ONCHANGE PACKAGE 
    -- Add Student in the BMTSYNC_Student_Status table
    PKG_BMTSYNC_ONCHANGE.Student(
		:new.STUDENT_ID,
		:new.CUSTOMER_ID,
		'New'
    );
    */

    PKG_BMTSYNC_Students.AddUpdateStudentAPIStatus(
        :new.STUDENT_ID,
	:new.CUSTOMER_ID,
	'BMT',
	'New');

EXCEPTION		
WHEN NO_DATA_FOUND THEN
   NULL;
WHEN OTHERS THEN
   NULL;
END ;
/

