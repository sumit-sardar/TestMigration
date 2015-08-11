	CREATE OR REPLACE TRIGGER TRG_BMTSYNC_TESTADMIN
AFTER INSERT OR UPDATE OF 
Test_Admin_Id, 
Customer_ID,
Test_Catalog_Id,
TEST_ADMIN_NAME,
Login_Start_Date,
Daily_Login_Start_Time,
Login_End_Date,
Daily_Login_End_Time
ON TEST_ADMIN
FOR EACH ROW 
DECLARE
   vCustomer_ID    Customer.Customer_ID%TYPE;
BEGIN

	SELECT Cust.Customer_ID 
	INTO vCustomer_ID
    FROM BMTSYNC_Customer Cust
    WHERE 
    Cust.Customer_Id = :new.Customer_ID; 
	

    -- INSERT/UPDATE THE RECORD IN BMTSYNC_TESTADMIN_STATUS TABLE 
    PKG_BMTSYNC_TESTADMIN.AddTestAdminAPIStatus(:new.CUSTOMER_ID, :new.TEST_ADMIN_ID, 'BMT', 'New');

EXCEPTION		
WHEN NO_DATA_FOUND THEN
   NULL;
WHEN OTHERS THEN
   NULL;
END ;
/


CREATE OR REPLACE TRIGGER TRG_BMTSYNC_TESTROSTER
AFTER INSERT OR UPDATE ON TEST_ROSTER
FOR EACH ROW 
WHEN (NEW.Test_Completion_Status = 'SC' OR (OLD.Test_Completion_Status = 'CO' AND NEW.Test_Completion_Status = 'IN'))
DECLARE
   vCustomer_ID    Customer.Customer_ID%TYPE;
BEGIN

	SELECT Cust.Customer_ID 
	INTO vCustomer_ID
    FROM BMTSYNC_Customer Cust
    WHERE 
    Cust.Customer_Id = :new.Customer_ID; 
	

    -- INSERT/UPDATE THE RECORD IN BMTSYNC_STUDENT_STATUS TABLE
    PKG_BMTSYNC_ASSIGNMENT.AddUpdateTestRoaster(:new.CUSTOMER_ID, 
                                                :new.TEST_ADMIN_ID, 
                                                :new.STUDENT_ID, :new.TEST_ROSTER_ID, 
                                                'New');

	
	
EXCEPTION		
WHEN NO_DATA_FOUND THEN
   NULL;
WHEN OTHERS THEN
   NULL;
END ;
/


CREATE OR REPLACE TRIGGER TRG_BMTSYNC_TESTADMIN_ITEMSET
AFTER INSERT OR UPDATE ON TEST_ADMIN_ITEM_SET
FOR EACH ROW 
DECLARE
   vCustomer_ID    Customer.Customer_ID%TYPE;
BEGIN
    
	SELECT Cust.Customer_ID 
	INTO vCustomer_ID
    FROM Test_Admin TA, BMTSYNC_Customer Cust
    WHERE 
	TA.Customer_ID = Cust.Customer_Id AND 
	Test_Admin_ID = :new.TEST_ADMIN_ID;
     
	
    --INSERT/UPDATE THE RECORD IN BMTSYNC_ASSIGNMENT_STATUS TABLE
    PKG_BMTSYNC_ASSIGNMENT.AddUpdateTestAdminStudent(:new.TEST_ADMIN_ID, 'New');

EXCEPTION		
WHEN NO_DATA_FOUND THEN
   NULL;
WHEN OTHERS THEN
   NULL;	
END ;
/


CREATE OR REPLACE TRIGGER TRG_BMTSYNC_SISS
AFTER INSERT OR UPDATE ON STUDENT_ITEM_SET_STATUS
FOR EACH ROW
WHEN (NEW.Completion_Status = 'SC' OR (OLD.Completion_Status = 'CO' AND NEW.Completion_Status = 'IN')) 
DECLARE
    vCustomer_ID     Customer.Customer_ID%TYPE;
	vTestAdminID     Test_Roster.Test_Admin_ID%TYPE;
	vStudentID       Test_Roster.Student_ID%TYPE;
	vTestRosterID    Test_Roster.Test_Roster_Id%TYPE;
	
BEGIN

    -- Add Student in the BMTSYNC_Assignment_Status table
	SELECT DISTINCT TR.Customer_Id, TR.Test_admin_ID, TR.Student_ID, TR.Test_Roster_Id
	INTO vCustomer_ID, vTestAdminID, vStudentID, vTestRosterID
	FROM Test_Roster TR, BMTSYNC_Customer Cust
	WHERE TR.Customer_ID = Cust.Customer_ID AND 
	Test_Roster_ID = :new.TEST_ROSTER_ID;

	-- INSERT/UPDATE THE RECORD IN BMTSYNC_STUDENT_STATUS TABLE
	PKG_BMTSYNC_ASSIGNMENT.AddUpdateTestRoaster(vCustomer_ID, vTestAdminID, vStudentID, vTestRosterID, 'New');


EXCEPTION
WHEN NO_DATA_FOUND THEN
   NULL;	
WHEN OTHERS THEN
   NULL;
END ;
/
