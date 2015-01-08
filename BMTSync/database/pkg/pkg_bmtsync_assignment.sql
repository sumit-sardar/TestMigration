CREATE OR REPLACE PACKAGE PKG_BMTSYNC_ASSIGNMENT AS	

  	TYPE REF_CURSOR IS REF CURSOR;
	
    /************************************************************************
	*  ProcedUre with get a message from the AQT_ASSIGNMENT_QUEUE 
	*  and return the Roaster Test Admin, Sub Test and Test Delivery data
	************************************************************************/
    PROCEDURE getTestAssignment(pTestAdminID  IN NUMBER,
	                            pStudentId    IN  NUMBER,
	                            pResultCursor OUT REF_CURSOR);
								
	
	
    /************************************************************************
	*  Procedure to add all the students for a Test Admin to the
	*  BMTSYNC_Assignment_Status table.
	*  This procedure will be calle by procedure PKG_BMTSYNC_ONCHANGE.TESTADMIN
	*  via trigger TRG_BMTSYNC_TESTADMIN
	************************************************************************/
    PROCEDURE AddUpdateTestAdminStudent (pTestAdminID NUMBER, 
	                                     pStatus VARCHAR2);
										 
										 
    /************************************************************************
	*  Procedure to add all the students to the
	*  BMTSYNC_Assignment_Status table.
	*  This procedure will be calleD by PKG_BMTSYNC_ONCHANGE.TestRoster
	* via trigger TRG_BMTSYNC_TESTROSTER
	************************************************************************/
    PROCEDURE AddUpdateTestRoaster (pCustomerID NUMBER, 
	                                pTestAdminID NUMBER,
                                    pStudentID NUMBER,	
									pTestRosterId NUMBER,
	                                pStatus VARCHAR2);
									
									
	/************************************************************************
	*  Procedure to insert/update the Assigment API Status table
	*  This procedure is called by the PKG_BMTSYNC.onChange_Student
	*  via Trigger TRG_STUDENT_WS
	************************************************************************/
	PROCEDURE AddUpdateAssignmentAPIStatus(
	    pCustomerID    IN NUMBER,
	    pTestAdminID   IN NUMBER,
	    pStudentID     IN NUMBER,
		pRosterID      IN NUMBER,
		pAppName       IN VARCHAR2,
		pExportStatus  IN VARCHAR2);



	/*
	*  Procedure to update the BMTSYNC Assignment API export Status
	*/
	PROCEDURE updateAssignmentAPIStatus(
		pTestAdminID     IN NUMBER,
	    pStudentId       IN VARCHAR2,
		pAppName         IN VARCHAR2,
		pExportStatus    IN VARCHAR2,
		pErrorCode       IN VARCHAR2,
		pErrorMessage    IN VARCHAR2);
		
END PKG_BMTSYNC_ASSIGNMENT;
/


CREATE OR REPLACE PACKAGE BODY PKG_BMTSYNC_ASSIGNMENT AS

    /***********************************************************************
	*  This proceudre with get a message from the AQT_ASSIGNMENT_QUEUE 
	*  and return the Roaster Test Admin, Sub Test and Test Delivery data
	***********************************************************************/
    PROCEDURE getTestAssignment(pTestAdminID  IN NUMBER,
	                            pStudentId    IN  NUMBER,
	                            pResultCursor OUT REF_CURSOR) AS
	BEGIN
			
		OPEN pResultCursor FOR
		SELECT TA.Test_Admin_Id oasTestAdministrationID, 
			TA.Customer_ID  oasCustomerId,
			TA.Test_Catalog_Id oasTestCatalogId,
			TA.TEST_ADMIN_NAME name,
			PDT.PRODUCT_NAME productName,
			TO_CHAR(TA.Login_Start_Date, 'YYYY-MM-DD') startDate,
			TO_CHAR(TA.Daily_Login_Start_Time, 'hh24:mi') startHour,
			TO_CHAR(TA.Login_End_Date, 'YYYY-MM-DD') endDate,
			TO_CHAR(TA.Daily_Login_End_Time, 'hh24:mi') endHour,
			DECODE(TA.Enforce_Break, 'T', 'True', 'False') enforceBreak,
			DECODE(TA.Enforce_Tutorial, 'T', 'True', 'False')  enForceTutorial, 
			TR.TEST_ROSTER_ID AS oasRosterId,
			TR.Student_id AS oasStudentid, 
			Password, 
			SISS.ITEM_SET_ID, 
			SISS.COMPLETION_STATUS Delivery_Status, 
			TAIS.ACCESS_CODE Access_Code,
			IST.Ext_Tst_Item_Set_Id OasTestId,
			IST2.ITEM_SET_NAME oasSubTestName,
			DECODE(TA.Enforce_Time_Limit, 'T','True','False') Enforce_Time_Limit,
			IST.TIME_LIMIT TimeLimitInMins,
            (SISS.ITEM_SET_ORDER+1) Item_Order
		FROM Test_Admin TA, 
			 Product PDT, 
			 Test_Roster TR, 
			 Student_Item_Set_Status SISS, 
			 ITEM_SET IST, 
			 Item_Set_Parent ISP,
			 ITEM_SET IST2, 
			 Test_Admin_Item_Set TAIS
		WHERE TA.TEST_ADMIN_ID = TR.TEST_ADMIN_ID AND 
			TA.Product_Id = PDT.PRODUCT_ID AND
			TR.Test_Roster_Id = SISS.TEST_ROSTER_ID AND
			SISS.ITEM_SET_ID = IST.Item_Set_Id AND
			IST.Item_Set_Id = ISP.ITEM_SET_ID AND
			ISP.PARENT_ITEM_SET_ID = IST2.ITEM_SET_ID AND
			IST2.Item_Set_Id = TAIS.ITEM_SET_ID AND
			TR.TEST_ADMIN_ID = TAIS.TEST_ADMIN_ID AND
			TR.Test_Admin_Id = pTestAdminID AND 
			TR.Student_Id = pStudentId;

		UPDATE BMTSYNC_Assignment_Status
		SET Export_Status = 'Inprogress'
		WHERE Test_Admin_Id = pTestAdminID 
		  AND Student_ID = pStudentID;
		
		COMMIT;	
	END getTestAssignment;			
			

    /************************************************************************
	*  Procedure to add all the students for a Test Admin to the
	*  BMTSYNC_Assignment_Status table.
	*  This procedure will be calle by procedure PKG_BMTSYNC_ONCHANGE.TESTADMIN
	*  via trigger TRG_BMTSYNC_TESTADMIN
	************************************************************************/
    PROCEDURE AddUpdateTestAdminStudent (pTestAdminID NUMBER, 
	                                     pStatus VARCHAR2) AS
	
	    CURSOR studentCursor IS
		SELECT Distinct Customer_id, Test_Admin_Id, Student_Id, Test_Roster_Id FROM 
		Test_Roster WHERE Test_Admin_id = pTestAdminID;		
	BEGIN
        FOR studentRec IN studentCursor LOOP
		    AddUpdateAssignmentAPIStatus(studentRec.Customer_ID,
			                             studentRec.Test_Admin_ID, 
			                             studentRec.Student_ID,
                                         studentRec.Test_Roster_ID,							 
										 'BMTSYNC', pStatus);
		END LOOP;
	END AddUpdateTestAdminStudent;
	
    /************************************************************************
	*  Procedure to add all the students to the
	*  BMTSYNC_Assignment_Status table.
	*  This procedure will be calle by PKG_BMTSYNC_ONCHANGE.TestRoster
	* via TRG_BMTSYNC_TESTROSTER
	************************************************************************/
    PROCEDURE AddUpdateTestRoaster (pCustomerID NUMBER, 
	                                pTestAdminID NUMBER,
                                    pStudentID NUMBER,	
									pTestRosterId NUMBER,
	                                pStatus VARCHAR2) AS
	
	BEGIN
		AddUpdateAssignmentAPIStatus(pCustomerID, 
		                             pTestAdminID, 
									 pStudentID, 
									 pTestRosterId, 
									 'BMTSYNC', 
									 pStatus);
	END AddUpdateTestRoaster;	

	/*
	*  Procedure to insert/update the Assigment API Status table
	*  This procedure is called by the PKG_BMTSYNC.onChange_Student
	*  via Trigger TRG_STUDENT_WS
	*/
	PROCEDURE AddUpdateAssignmentAPIStatus(
	    pCustomerID    IN NUMBER,
	    pTestAdminID   IN NUMBER,
	    pStudentID     IN NUMBER,
		pRosterID      IN NUMBER,
		pAppName       IN VARCHAR2,
		pExportStatus  IN VARCHAR2) AS
		
		vNoOfAttempts   NUMBER := 0;
		vNext_Retry_DateTime  DATE;
		
    BEGIN
	    SELECT No_Of_Attempts INTO vNoOfAttempts FROM BMTSYNC_Assignment_Status 
		WHERE Test_Admin_ID = pTestAdminID AND Student_ID = pStudentID;
		
		IF UPPER(pExportStatus) = 'NEW' THEN 
		   vNoOfAttempts := 0;
		   vNext_Retry_DateTime := SYSDATE;
		END IF;
        
		UPDATE BMTSYNC_Assignment_Status
		SET Customer_id = pCustomerID,
		    Roster_Id = pRosterId,
		    App_Name = pAppName,
			Exported_On  = SYSDATE,
			Export_Status = pExportStatus,
			No_Of_Attempts = vNoOfAttempts,
			Error_Code    = '',
			Error_Message = '',
			Next_Retry_DateTime = vNext_Retry_DateTime
		WHERE Test_Admin_ID = pTestAdminID AND Student_ID = pStudentID;
		
		
    EXCEPTION 
	WHEN NO_DATA_FOUND THEN
	    DBMS_OUTPUT.PUT_LINE('Insert');
	    INSERT INTO BMTSYNC_Assignment_Status (Customer_id,Test_Admin_ID, Student_ID, Roster_Id, App_Name, Exported_ON, Export_Status, Error_Code, Error_Message, Next_Retry_DateTime)
	       VALUES (pCustomerID, pTestAdminID, pStudentID, pRosterId, pAppName, SYSDATE, pExportStatus, '', '', SYSDATE);

    WHEN OTHERS THEN	
		RAISE_APPLICATION_ERROR(-20001, 'PKG_BMTSYNC_ASSIGNMENT.AddUpdateAssignmentAPIStatus FAILURE :' || SQLERRM(SQLCODE));
      
    END AddUpdateAssignmentAPIStatus;

	
	/*
	*  Procedure to update the BMTSYNC Assignment API export Status
	*/
	PROCEDURE updateAssignmentAPIStatus(
		pTestAdminID     IN NUMBER,
	    pStudentId       IN VARCHAR2,
		pAppName         IN VARCHAR2,
		pExportStatus    IN VARCHAR2,
		pErrorCode       IN VARCHAR2,
		pErrorMessage    IN VARCHAR2) AS
		
		vNoOfAttempts         BMTSYNC_Assignment_Status.No_Of_Attempts%TYPE;
		vNext_Retry_DateTime  DATE; 
		vRetry                BMTSYNC_ERRORS.Retry_Error%TYPE;
		vExportStatus         BMTSYNC_Assignment_Status.Export_Status%TYPE;
		
	BEGIN
	    SELECT No_Of_Attempts INTO vNoOfAttempts 
		FROM BMTSYNC_Assignment_Status 
		WHERE Test_Admin_ID = pTestAdminID AND 
		      Student_ID = pStudentID;
			  
        vExportStatus := pExportStatus;			  
		
		IF UPPER(vExportStatus) = 'FAILED' THEN 
		
		    --Check if the error need to be re-tryed
		    BEGIN
				SELECT NVL(Retry_Error, 'Y') INTO vRetry
				FROM  BMTSYNC_ERRORS
				WHERE Error_Code = pErrorCode;
			EXCEPTION 
			WHEN OTHERS THEN
				vRetry := 'Y';
			END;
			
			vNoOfAttempts := vNoOfAttempts + 1;
			
			IF vRetry = 'Y' THEN
				-- Find the Next Retry time based on Retry latency
				BEGIN
					SELECT  SYSDATE +  Delay_in_Mins/1440 NEXT_RETRY_DATETIME INTO 
					vNext_Retry_DateTime
					FROM BMTSYNC_Retry_Frequency RF
					WHERE No_of_Attempt = vNoOfAttempts;
				EXCEPTION
				WHEN OTHERS THEN
				   vExportStatus := 'No Retry';
				   SELECT SYSDATE +  60/1440 INTO vNext_Retry_DateTime
					FROM DUAL;
				END;
			ELSE
			   vExportStatus := 'No Retry';
			END IF;
			
		END IF;
        
		   
		
	    IF UPPER(vExportStatus) = 'SUCCESS' THEN 
			UPDATE BMTSYNC_Assignment_Status
			SET App_Name = pAppName,
				Exported_On  = SYSDATE,
				Export_Status = vExportStatus,
				No_Of_Attempts = vNoOfAttempts,
				Error_Code    = pErrorCode,
				Error_Message = pErrorMessage,
				Next_Retry_DateTime = vNext_Retry_DateTime
			WHERE Test_Admin_ID = pTestAdminID 
			  AND Student_ID = pStudentID 
			  AND UPPER(Export_Status) = 'INPROGRESS';
		ELSE
			UPDATE BMTSYNC_Assignment_Status
			SET App_Name = pAppName,
				Exported_On  = SYSDATE,
				Export_Status = vExportStatus,
				No_Of_Attempts = vNoOfAttempts,
				Error_Code    = pErrorCode,
				Error_Message = pErrorMessage,
				Next_Retry_DateTime = vNext_Retry_DateTime				
			WHERE Test_Admin_ID = pTestAdminID 
			  AND Student_ID = pStudentID;
		END IF;
		COMMIT;
    EXCEPTION 
	WHEN NO_DATA_FOUND THEN
	    DBMS_OUTPUT.PUT_LINE('Insert');
	    INSERT INTO BMTSYNC_Assignment_Status (Test_Admin_ID, Student_ID, App_Name, Exported_ON, Export_Status, Error_Code, Error_Message, Next_Retry_DateTime)
	       VALUES (pTestAdminID, pStudentID, pAppName, SYSDATE, pExportStatus, '', '', SYSDATE);
		
		COMMIT;

    WHEN OTHERS THEN	
		RAISE_APPLICATION_ERROR(-20001, 'PKG_BMTSYNC_ASSIGNMENT.updateAssignmentAPIStatus FAILURE :' || SQLERRM(SQLCODE));
	END updateAssignmentAPIStatus;
	
		
END PKG_BMTSYNC_ASSIGNMENT;
/
