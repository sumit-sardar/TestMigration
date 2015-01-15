CREATE OR REPLACE PACKAGE PKG_BMTSYNC_TESTADMIN AS	

  	TYPE REF_CURSOR IS REF CURSOR;
	
    /***********************************************************************
	*  This proceudre returns the Test Administration details
	*  
	***********************************************************************/
    PROCEDURE getTestAdmin(pTestAdminID  IN NUMBER,
                            pResultCursor OUT REF_CURSOR);

							
	/*
	*  Procedure to insert/update the Assigment API Status table
	*  This procedure is called by the PKG_BMTSYNC.onChange_TestAdmin
	*  via Trigger 
	*/
	PROCEDURE AddTestAdminAPIStatus(
	    pCustomerID    IN NUMBER,
	    pTestAdminID   IN NUMBER,
		pAppName       IN VARCHAR2,
		pExportStatus  IN VARCHAR2);
										 
										 
	/*
	*  Procedure to update the BMTSYNC TestAdmin API export Status
	*/
	PROCEDURE updateTestAdminAPIStatus(
		pTestAdminID     IN NUMBER,
		pAppName         IN VARCHAR2,
		pExportStatus    IN VARCHAR2,
		pErrorCode       IN VARCHAR2,
		pErrorMessage    IN VARCHAR2);
		
										 
END PKG_BMTSYNC_TESTADMIN;
/


CREATE OR REPLACE PACKAGE BODY PKG_BMTSYNC_TESTADMIN AS	

    /***********************************************************************
	*  This proceudre returns the Test Administration details
	*  
	***********************************************************************/
    PROCEDURE getTestAdmin(pTestAdminID  IN NUMBER,
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
			TO_CHAR(TA.Daily_Login_End_Time, 'hh24:mi') endHour			
		FROM Test_Admin TA, 
			 Product PDT 
		WHERE TA.Product_Id = PDT.PRODUCT_ID AND
			TA.Test_Admin_Id = pTestAdminID;
			
		UPDATE BMTSYNC_TestAdmin_Status
		SET Export_Status = 'Inprogress'
		WHERE Test_Admin_Id = pTestAdminID; 
		
		COMMIT;
	END getTestAdmin;

	/*
	*  Procedure to insert/update the TestAdmin API Status table
	*  This procedure is called by the PKG_BMTSYNC.onChange_TestAdmin
	*  via Trigger 
	*/
	PROCEDURE AddTestAdminAPIStatus(
	    pCustomerID    IN NUMBER,
	    pTestAdminID   IN NUMBER,
		pAppName       IN VARCHAR2,
		pExportStatus  IN VARCHAR2) AS
		
		vNoOfAttempts   NUMBER := 0;
		vNext_Retry_DateTime  DATE;
		
    BEGIN
	    SELECT No_Of_Attempts INTO vNoOfAttempts FROM BMTSYNC_TestAdmin_Status 
		WHERE Test_Admin_ID = pTestAdminID;
		
		IF UPPER(pExportStatus) = 'NEW' THEN 
		   vNoOfAttempts := 0;
		   vNext_Retry_DateTime := SYSDATE;
		END IF;
        
		UPDATE BMTSYNC_TestAdmin_Status
		SET Customer_id = pCustomerID,
		    App_Name = pAppName,
			Exported_On  = SYSDATE,
			Export_Status = pExportStatus,
			No_Of_Attempts = vNoOfAttempts,
			Error_Code    = '',
			Error_Message = '',
			Next_Retry_DateTime = vNext_Retry_DateTime
		WHERE Test_Admin_ID = pTestAdminID;
		

		
    EXCEPTION 
	WHEN NO_DATA_FOUND THEN
	    INSERT INTO BMTSYNC_TestAdmin_Status (Customer_id,Test_Admin_ID, App_Name, Exported_ON, Export_Status, Error_Code, Error_Message, Next_Retry_DateTime)
	       VALUES (pCustomerID, pTestAdminID, pAppName, SYSDATE, pExportStatus, '', '', SYSDATE);

    WHEN OTHERS THEN	
		RAISE_APPLICATION_ERROR(-20001, 'PKG_BMTSYNC_TESTADMIN.AddTestAdminAPIStatus FAILURE :' || SQLERRM(SQLCODE));

    END AddTestAdminAPIStatus;
	
	/*
	*  Procedure to update the BMTSYNC TestAdmin API export Status
	*/
	PROCEDURE updateTestAdminAPIStatus(
		pTestAdminID     IN NUMBER,
		pAppName         IN VARCHAR2,
		pExportStatus    IN VARCHAR2,
		pErrorCode       IN VARCHAR2,
		pErrorMessage    IN VARCHAR2) AS
		
		vNoOfAttempts         BMTSYNC_TestAdmin_Status.No_Of_Attempts%TYPE;
		vNext_Retry_DateTime  DATE;
        pCustomerID           NUMBER;
     	vRetry                BMTSYNC_ERRORS.Retry_Error%TYPE;
		vExportStatus         BMTSYNC_Assignment_Status.Export_Status%TYPE;
		
	BEGIN
	    SELECT No_Of_Attempts INTO vNoOfAttempts 
		FROM BMTSYNC_TestAdmin_Status 
		WHERE Test_Admin_ID = pTestAdminID;
		
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
        
		   
		
	    IF UPPER(pExportStatus) = 'SUCCESS' THEN 
			UPDATE BMTSYNC_TestAdmin_Status
			SET App_Name = pAppName,
				Exported_On  = SYSDATE,
				Export_Status = pExportStatus,
				No_Of_Attempts = vNoOfAttempts,
				Error_Code    = pErrorCode,
				Error_Message = pErrorMessage,
				Next_Retry_DateTime = vNext_Retry_DateTime
			WHERE Test_Admin_ID = pTestAdminID 
			  AND UPPER(Export_Status) = 'INPROGRESS';
		ELSE
			UPDATE BMTSYNC_TestAdmin_Status
			SET App_Name = pAppName,
				Exported_On  = SYSDATE,
				Export_Status = pExportStatus,
				No_Of_Attempts = vNoOfAttempts,
				Error_Code    = pErrorCode,
				Error_Message = pErrorMessage,
				Next_Retry_DateTime = vNext_Retry_DateTime				
			WHERE Test_Admin_ID = pTestAdminID;

		END IF;
		COMMIT;
    EXCEPTION 
	WHEN NO_DATA_FOUND THEN
	    --DBMS_OUTPUT.PUT_LINE('Insert');
		SELECT Customer_id INTO pCustomerID
		FROM Test_Admin 
		WHERE Test_Admin_ID = pTestAdminID;
		
	    INSERT INTO BMTSYNC_TestAdmin_Status (Customer_id, Test_Admin_ID, App_Name, Exported_ON, Export_Status, Error_Code, Error_Message, Next_Retry_DateTime)
	       VALUES (pCustomerID,pTestAdminID, pAppName, SYSDATE, pExportStatus, pErrorCode, pErrorMessage, SYSDATE);
		
		
		COMMIT;

    WHEN OTHERS THEN	
		RAISE_APPLICATION_ERROR(-20001, 'PKG_BMTSYNC_TESTADMIN.updateTestAdminAPIStatus FAILURE :' || SQLERRM(SQLCODE));
	END updateTestAdminAPIStatus;
	

	
END PKG_BMTSYNC_TESTADMIN;
/

set TERMOUT on
PROMPT PKG_BMTSYNC_TESTADMIN compiled