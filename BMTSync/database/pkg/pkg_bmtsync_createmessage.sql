CREATE OR REPLACE PACKAGE PKG_BMTSYNC_CREATEMESSAGE AS

	/*
	*  PROCEDURE TO CREATE QUEUES FOR STUDENT
	*  WHOSE STATUS IS 'NEW' OR 'FAILED'
	*
	*  This procedure is schedule to run as  DBMS_JOB
	*/	
	PROCEDURE createStudentMessage;
	

	/*
	*  PROCEDURE TO CREATE QUEUES FOR ASSIGNMENT
	*  WHOSE STATUS IS 'NEW' OR 'FAILED'
	*
	*  This procedure is schedule to run as  DBMS_JOB
	*/
	PROCEDURE createAssignmentMessage;
	
	
	/*
	*  PROCEDURE TO CREATE QUEUES FOR TEST ADMIN
	*  WHOSE STATUS IS 'NEW' OR 'FAILED'
	*
	*  This procedure is schedule to run as  DBMS_JOB
	*/
	PROCEDURE createTestAdminMessage;
	

END PKG_BMTSYNC_CREATEMESSAGE;
/


CREATE OR REPLACE PACKAGE BODY PKG_BMTSYNC_CREATEMESSAGE AS

	/*
	*  PROCEDURE TO CREATE QUEUES FOR STUDENT
	*  WHOSE STATUS IS 'NEW' OR 'FAILED'
	*
	*  This procedure is schedule to run as  DBMS_JOB
	*/
	PROCEDURE createStudentMessage AS

		CURSOR StudentCursor IS
			SELECT SAS.Export_Status, SAS.Student_ID, STD.Customer_ID, STD.Updated_Date_Time, SAS.Exported_ON, SAS.No_Of_Attempts
			FROM BMTSYNC_Student_Status SAS,  Student STD
			WHERE 
			SAS.Student_ID = STD.Student_ID AND
			SAS.NEXT_RETRY_DATETIME <= SYSDATE AND
			UPPER(SAS.Export_Status) IN ('NEW', 'FAILED');   
	BEGIN
	    FOR StudentRecord IN StudentCursor LOOP
		    
			PKG_BMTSYNC_STUDENTQUEUE.ADD_STUDENT_TOQUEUE(StudentRecord.Customer_ID, 
			                                     StudentRecord.Student_ID, 
												 StudentRecord.Updated_Date_Time);
		    
			--Change the Status to Pending
			UPDATE BMTSYNC_Student_Status 
			SET Export_Status = 'Pending'
			WHERE Student_ID = StudentRecord.Student_ID;
			
		END LOOP;
		COMMIT;
		
	END createStudentMessage;


	/*
	*  PROCEDURE TO CREATE QUEUES FOR ASSIGNMENT
	*  WHOSE STATUS IS 'NEW' OR 'FAILED'
	*
	*  This procedure is schedule to run as  DBMS_JOB
	*/
	PROCEDURE createAssignmentMessage AS

		CURSOR assignmentCursor IS
			SELECT BAS.Export_Status, BAS.Test_Admin_Id, BAS.Student_ID, 
			   STD.Customer_ID, STD.Test_Roster_ID, 
                           NVL(STD.UPDATED_DATE_TIME, STD.CREATED_DATE_TIME) AS UPDATED_DATE_TIME
			FROM BMTSYNC_Assignment_Status BAS, Test_Roster STD
			WHERE 
			BAS.Test_Admin_ID = STD.Test_Admin_ID AND
			BAS.Student_ID = STD.Student_ID AND 
			BAS.NEXT_RETRY_DATETIME <= SYSDATE AND
			UPPER(BAS.Export_Status) IN ('NEW', 'FAILED');   
	BEGIN
	    FOR assignmentRecord IN assignmentCursor LOOP
		    
			PKG_BMTSYNC_ASSIGNMENTQUEUE.ADD_ASSIGNMENT_TOQUEUE(
                assignmentRecord.Customer_Id,
			    assignmentRecord.Test_Admin_Id, 
			    assignmentRecord.Student_ID,
				assignmentRecord.Test_Roster_ID,
                            assignmentRecord.Updated_Date_Time
		    );
		    
			--Change the Status to Pending
			UPDATE BMTSYNC_Assignment_Status 
			SET Export_Status = 'Pending'
			WHERE Test_Admin_ID = assignmentRecord.Test_Admin_Id 
			  AND Student_ID = assignmentRecord.Student_ID;
			
		END LOOP;
		COMMIT;
		
	END createAssignmentMessage;


	/*
	*  PROCEDURE TO CREATE QUEUES FOR TEST ADMIN
	*  WHOSE STATUS IS 'NEW' OR 'FAILED'
	*
	*  This procedure is schedule to run as  DBMS_JOB
	*/
	PROCEDURE createTestAdminMessage AS

		CURSOR testAdminCursor IS
			SELECT BAS.Customer_ID, BAS.Test_Admin_Id
			FROM BMTSYNC_TestAdmin_Status BAS
			WHERE 
			BAS.NEXT_RETRY_DATETIME <= SYSDATE AND
			UPPER(BAS.Export_Status) IN ('NEW', 'FAILED'); 
			
	BEGIN
	    FOR testAdminRecord IN testAdminCursor LOOP
		    
			PKG_BMTSYNC_TESTADMINQUEUE.ADD_TESTADMIN_TOQUEUE(
                testAdminRecord.Customer_Id,
			    testAdminRecord.Test_Admin_Id
		    );
		    
			--Change the Status to Pending
			UPDATE BMTSYNC_TestAdmin_Status 
			SET Export_Status = 'Pending'
			WHERE Customer_id = testAdminRecord.Customer_ID AND 
			      Test_Admin_ID = testAdminRecord.Test_Admin_Id;
			  
			
		END LOOP;
		COMMIT;
		
	END createTestAdminMessage;
	
END PKG_BMTSYNC_CREATEMESSAGE;
/

set TERMOUT on
PROMPT PKG_BMTSYNC_CREATEMESSAGE compiled