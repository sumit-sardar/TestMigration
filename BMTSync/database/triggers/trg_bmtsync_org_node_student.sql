CREATE OR REPLACE TRIGGER TRG_BMTSYNC_ORGNODE_STUDENT
AFTER INSERT OR UPDATE ON Org_Node_Student
FOR EACH ROW 
--WHEN (new.Customer_ID = 15357 OR new.Customer_ID = 16701)
DECLARE
   vCustomer_ID    Customer.Customer_ID%TYPE;
BEGIN

	SELECT Cust.Customer_ID 
	INTO vCustomer_ID
    FROM BMTSYNC_Customer Cust
    WHERE 
    Cust.Customer_Id = :new.Customer_ID;
	
    -- Add Student in the BMTSYNC_Student_Status table
    PKG_BMTSYNC_ONCHANGE.Student(
		:new.STUDENT_ID,
		:new.CUSTOMER_ID,
		'New'
    );
EXCEPTION		
WHEN NO_DATA_FOUND THEN
   NULL;
WHEN OTHERS THEN
   NULL;
END ;
/

