CREATE OR REPLACE TRIGGER TRG_BMTSYNC_ORGNODE_STUDENT
AFTER INSERT OR UPDATE ON Org_Node_Student
FOR EACH ROW 
DECLARE
   vCustomer_ID    Customer.Customer_ID%TYPE;
BEGIN

	SELECT Cust.Customer_ID 
	INTO vCustomer_ID
    FROM BMTSYNC_Customer Cust
    WHERE 
    Cust.Customer_Id = :new.Customer_ID;
	
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

