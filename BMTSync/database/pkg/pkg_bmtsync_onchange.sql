--DROP PACKAGE PKG_BMTSYNC;
CREATE OR REPLACE PACKAGE PKG_BMTSYNC_ONCHANGE AS

    /* PROCEDURE TO Keep Track of Student Status
	*  VALID Status are NEW,PENDING,INPROGRESS,SUCCESS, FAILED
	*  Entries will be NEW initially, will be changed to PENDING
	*  once a message in created in the QUEUE table, in PROGRESS
	*  once the message assigned/dequeued to the consumer,
	*  in SUCCESS when succesfully saved in BMT, FAILED when errored in BMT
	*/
	PROCEDURE Student(
		pStudentID    IN NUMBER,
        pCustomerID   IN NUMBER,
		pStatus       IN VARCHAR2
	) ;
	

    /* PROCEDURE TO Keep Track of Test Admin Status
	*  VALID Status are NEW,PENDING,INPROGRESS,SUCCESS, FAILED
	*  Entries will be NEW initially, will be changed to PENDING
	*  once a message in created in the QUEUE table, in PROGRESS
	*  once the message assigned/dequeued to the consumer,
	*  in SUCCESS when succesfully saved in BMT, FAILED when errored in BMT
	*/
	PROCEDURE TestAdmin(
	    pCustomer_id     IN NUMBER,	
		pTest_Admin_Id   IN NUMBER,
		pStatus          IN VARCHAR2
	);


        /* PROCEDURE TO Keep Track of Test Admin Item set Status
	*  VALID Status are NEW,PENDING,INPROGRESS,SUCCESS, FAILED
	*  Entries will be NEW initially, will be changed to PENDING
	*  once a message in created in the QUEUE table, in PROGRESS
	*  once the message assigned/dequeued to the consumer,
	*  in SUCCESS when succesfully saved in BMT, FAILED when errored in BMT
	*/

	PROCEDURE TestAdminItemSet(
	    pCustomer_id     IN NUMBER,
		pTest_Admin_Id   IN NUMBER,
		pStatus          IN VARCHAR2
	);

	
    /* PROCEDURE TO Keep Track of Test Roster Status
	*  VALID Status are NEW,PENDING,INPROGRESS,SUCCESS, FAILED
	*  Entries will be NEW initially, will be changed to PENDING
	*  once a message in created in the QUEUE table, in PROGRESS
	*  once the message assigned/dequeued to the consumer,
	*  in SUCCESS when succesfully saved in BMT, FAILED when errored in BMT
	*/

	PROCEDURE TestRoster(
	    pCustomerID      IN NUMBER,
		pTest_Admin_Id   IN NUMBER,
		pStudent_Id      IN NUMBER,
		pRosterID       IN NUMBER,
		pStatus          IN VARCHAR2
	);	

END PKG_BMTSYNC_ONCHANGE;
/


CREATE OR REPLACE PACKAGE BODY PKG_BMTSYNC_ONCHANGE AS

    /* PROCEDURE TO Keep Track of Student Status
	*  VALID Status are NEW,PENDING,INPROGRESS,SUCCESS, FAILED
	*  Entries will be NEW initially, will be changed to PENDING
	*  once a message in created in the QUEUE table, in PROGRESS
	*  once the message assigned/dequeued to the consumer,
	*  in SUCCESS when succesfully saved in BMT, FAILED when errored in BMT
	*/
	PROCEDURE Student(
		pStudentID    IN NUMBER,
        pCustomerID   IN NUMBER,
		pStatus       IN VARCHAR2
	) AS
	BEGIN
	    -- INSERT/UPDATE THE RECORD IN BMTSYNC_STUDENT_STATUS TABLE
		PKG_BMTSYNC_Students.AddUpdateStudentAPIStatus(
	    pStudentID,
	    pCustomerID,
		'BMT',
		pStatus);	
	END Student;

	
        /* PROCEDURE TO Keep Track of Test Admin Status
	*  VALID Status are NEW,PENDING,INPROGRESS,SUCCESS, FAILED
	*  Entries will be NEW initially, will be changed to PENDING
	*  once a message in created in the QUEUE table, in PROGRESS
	*  once the message assigned/dequeued to the consumer,
	*  in SUCCESS when succesfully saved in BMT, FAILED when errored in BMT
	*/

	PROCEDURE TestAdmin(
	    pCustomer_id     IN NUMBER,
		pTest_Admin_Id   IN NUMBER,
		pStatus          IN VARCHAR2
	) AS
	BEGIN
	
	    -- INSERT/UPDATE THE RECORD IN BMTSYNC_TESTADMIN_STATUS TABLE
	    PKG_BMTSYNC_TESTADMIN.AddTestAdminAPIStatus(pCustomer_id, pTest_Admin_Id, 'BMT', 'New');
		
	
	END TestAdmin;



        /* PROCEDURE TO Keep Track of Test Admin Item set Status
	*  VALID Status are NEW,PENDING,INPROGRESS,SUCCESS, FAILED
	*  Entries will be NEW initially, will be changed to PENDING
	*  once a message in created in the QUEUE table, in PROGRESS
	*  once the message assigned/dequeued to the consumer,
	*  in SUCCESS when succesfully saved in BMT, FAILED when errored in BMT
	*/

	PROCEDURE TestAdminItemSet(
	    pCustomer_id     IN NUMBER,
		pTest_Admin_Id   IN NUMBER,
		pStatus          IN VARCHAR2
	) AS
	BEGIN
	
		
	    --INSERT/UPDATE THE RECORD IN BMTSYNC_ASSIGNMENT_STATUS TABLE
	    PKG_BMTSYNC_ASSIGNMENT.AddUpdateTestAdminStudent(pTest_Admin_Id, pStatus);
		
	
	END TestAdminItemSet;


    /* PROCEDURE TO Keep Track of Test Roster Status
	*  VALID Status are NEW,PENDING,INPROGRESS,SUCCESS, FAILED
	*  Entries will be NEW initially, will be changed to PENDING
	*  once a message in created in the QUEUE table, in PROGRESS
	*  once the message assigned/dequeued to the consumer,
	*  in SUCCESS when succesfully saved in BMT, FAILED when errored in BMT
	*/

	PROCEDURE TestRoster(
	    pCustomerID      IN NUMBER,
		pTest_Admin_Id   IN NUMBER,
		pStudent_Id      IN NUMBER,
		pRosterID       IN NUMBER,
		pStatus          IN VARCHAR2
	) AS
	BEGIN
	
		-- INSERT/UPDATE THE RECORD IN BMTSYNC_STUDENT_STATUS TABLE
		PKG_BMTSYNC_ASSIGNMENT.AddUpdateTestRoaster(pCustomerID, pTest_Admin_Id, pStudent_Id, pRosterID, pStatus);
	
	END TestRoster;

	
	
END PKG_BMTSYNC_ONCHANGE;
/


set TERMOUT on
PROMPT PKG_BMTSYNC_ONCHANGE compiled

