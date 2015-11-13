CREATE OR REPLACE PACKAGE PKG_BMTSYNC_TESTSTATUS AS
    TYPE REF_CURSOR IS REF CURSOR;
	
    /********************************************************
     * Function to compare original and a new StartDate 
     * and return accordinlgy per requirements in BMTOAS-2067
     * 
     *********************************************************/
    FUNCTION processStartDate (origStartDate IN DATE, newStartDate IN DATE) RETURN DATE;
    
    /********************************************************
     * Function to compare original and a new CompletionDate 
     * and return accordinlgy per requirements in BMTOAS-2092
     * 
     *********************************************************/
    FUNCTION processCompletionDate (origCompletionDate IN DATE, newCompletionDate IN DATE) RETURN DATE;
    
    /***************************************************
	*
	* Procedure to validate the Test Status Data
	* and update the Student Item Set Status table
	* with the completion Status
	***************************************************/
    PROCEDURE ValidateSaveTestStatus (
		pRosterId           IN NUMBER,
		pOasTestId          IN VARCHAR2,
		pDeliveryStatus     IN VARCHAR2,
		pStartedDate        IN VARCHAR2,
		pCompletedDate      IN VARCHAR2,
		pResultCursor       OUT REF_CURSOR); 
		
		

	/**********************************************************
	* PROCEDURE TO UPDATE THE TEST_ROSTER COMPLETION STATUS
	* BASED ON THE ROSTER SUB-TEST COMPLETION STATUS
	*
	* pCurrSubTestStatus = Current Sub-test status
	**********************************************************/
	PROCEDURE UpdateRosterTestStatus(
	   pRosterId           IN NUMBER,
	   pCurrSubTestStatus  IN VARCHAR2,
	   pStartDate          IN DATE,
	   pCompletionDate     IN DATE);

	   

END PKG_BMTSYNC_TESTSTATUS;
/


CREATE OR REPLACE PACKAGE BODY PKG_BMTSYNC_TESTSTATUS AS

	/********************************************************
     * Function to compare original and a new StartDate 
     * and return accordinlgy per requirements in BMTOAS-2067
     * 
     *********************************************************/
	FUNCTION processStartDate (origStartDate IN DATE, newStartDate IN DATE) RETURN DATE IS
	BEGIN
		IF origStartDate is NULL then RETURN newStartDate;
		end if;
		IF newStartDate > origStartDate then RETURN origStartDate;
		ELSE RETURN newStartDate;
		END IF;
	END processStartDate;
	
	/********************************************************
     * Function to compare original and a new CompletionDate 
     * and return accordinlgy per requirements in BMTOAS-2092
     * 
     *********************************************************/
    FUNCTION processCompletionDate (origCompletionDate IN DATE, newCompletionDate IN DATE) RETURN DATE IS
    BEGIN
		IF origCompletionDate is NULL then RETURN newCompletionDate;
		end if;
		IF newCompletionDate > origCompletionDate then RETURN newCompletionDate;
		ELSE RETURN origCompletionDate;
		END IF;
	END processCompletionDate;

    /***************************************************
	*
	* Procedure to validate the Test Status Data
	* and updates the Student Item Set Status table
	* with the completion Status
	*
	* Fixed Defect BMTOAS-1202    04-28-2015
	***************************************************/

    PROCEDURE ValidateSaveTestStatus (
		pRosterId           IN NUMBER,
		pOasTestId          IN VARCHAR2,
		pDeliveryStatus     IN VARCHAR2,
		pStartedDate        IN VARCHAR2,
		pCompletedDate      IN VARCHAR2,
		pResultCursor       OUT REF_CURSOR) AS

		vFound         NUMBER;
		vItemSet_Id    NUMBER;
		bSuccess       CHAR(1);
		bErrorCode     NUMBER;
		bErrorMsg      VARCHAR2(200);
		vStatusDesc    Test_Completion_Status_Code .TEST_COMPLETION_STATUS_DESC%TYPE;
		vStartDate     DATE;
		vCompletionDate   DATE;
	BEGIN
	    bSuccess := 'T';
		vStartDate := TO_DATE(pStartedDate, 'MM-DD-YYYY HH:MI:SS PM');
		vCompletionDate := TO_DATE(pCompletedDate, 'MM-DD-YYYY HH:MI:SS PM');

		bErrorMsg  := 'Missing required fields';
	    IF pRosterID = NULL OR pRosterID = 0 THEN
		    bSuccess := 'F';
			bErrorCode := 104;
			bErrorMsg  := bErrorMsg||', Roster ID is mandatory';
		END IF;

		IF pOasTestID IS NULL OR LENGTH(TRIM(pOasTestID)) = 0 THEN
		    bSuccess := 'F';
			bErrorCode := 104;
			bErrorMsg  := bErrorMsg||', Oas Test ID is mandatory';
		END IF;

		IF pDeliveryStatus IS NULL OR LENGTH(TRIM(pDeliveryStatus)) = 0 THEN
		    bSuccess := 'F';
			bErrorCode := 104;
			bErrorMsg  := bErrorMsg||', Delivery Status is mandatory';
		END IF;

		IF pStartedDate IS NULL OR LENGTH(TRIM(pStartedDate)) = 0 THEN
		    bSuccess := 'F';
			bErrorCode := 104;
			bErrorMsg  := bErrorMsg||', Started Date is mandatory';
		END IF;

		IF bSuccess = 'T' THEN
		    bErrorMsg  := '';
		    --Validate Roster ID
		    BEGIN
				SELECT Test_Roster_ID INTO vFound
				FROM Test_Roster
				WHERE Test_Roster_ID = pRosterID;
			EXCEPTION
			WHEN NO_DATA_FOUND THEN
				bSuccess := 'F';
				bErrorCode := 102;
				bErrorMsg  := 'Roster Id does not exist in OAS';
            WHEN OTHERS THEN
				bSuccess := 'F';
				bErrorCode := 199;
				bErrorMsg  := 'Oracle Error validating Test Roster ID: '||SQLCODE||', '||SUBSTR(SQLERRM, 1, 80);
			END;

			-- Find the Item Set ID
			BEGIN
			    SELECT IST.Item_Set_Id INTO vItemSet_Id
				FROM Student_Item_Set_Status SISS, Item_Set IST
				WHERE SISS.Item_Set_ID = IST.Item_set_Id AND
				   SISS.Test_Roster_ID = pRosterID AND
				   IST.Ext_Tst_Item_Set_Id = pOasTestId;
			EXCEPTION
			WHEN NO_DATA_FOUND THEN
				bSuccess := 'F';
				bErrorCode := 105;
				bErrorMsg  := 'Roster Id - OasTest ID  does not exist in OAS';
            WHEN OTHERS THEN
				bSuccess := 'F';
				bErrorCode := 199;
				bErrorMsg  := 'Oracle Error validating Item Set Id : '||SQLCODE||', '||SUBSTR(SQLERRM, 1, 80);
			END;

			-- Validate the Delivery Status Code
			BEGIN
			    SELECT Test_Completion_Status_Desc INTO vStatusDesc
				FROM Test_Completion_Status_Code
				WHERE Test_Completion_Status = pDeliveryStatus;
				--DBMS_OUTPUT.PUT_LINE('Updated Successfully');
			EXCEPTION
			WHEN NO_DATA_FOUND THEN
				bSuccess := 'F';
				bErrorCode := 103;
				bErrorMsg  := 'Invalid Delivery status code';
            WHEN OTHERS THEN
				bSuccess := 'F';
				bErrorCode := 199;
				bErrorMsg  := 'Oracle Error validating Delivery Status Code : '||SQLCODE||', '||SUBSTR(SQLERRM, 1, 80);
			END;

            -- The data is validatated successfully and will now be saved
			IF bSuccess = 'T' THEN
				BEGIN
					UPDATE Student_Item_Set_Status
					SET Completion_Status = pDeliveryStatus,
						Start_Date_Time = vStartDate,
						Completion_Date_Time = vCompletionDate
					WHERE Test_Roster_Id = pRosterId
					  AND Item_Set_ID = vItemSet_Id;

					bErrorCode := 0;
					bErrorMsg  := '';

					--Update Roaster Completion Status
					UpdateRosterTestStatus(pRosterId, pDeliveryStatus, vStartDate, vCompletionDate);

					COMMIT;
				EXCEPTION
				WHEN OTHERS THEN
					bSuccess := 'F';
					bErrorCode := 199;
					bErrorMsg  := 'Oracle Error updating Student_Item_Set_Status_table: '||SQLCODE||', '||SUBSTR(SQLERRM, 1, 80);
					ROLLBACK;
				END;

			END IF;

		END IF;
	    --DBMS_OUTPUT.PUT_LINE(bErrorMsg);
		OPEN pResultCursor FOR
		SELECT pRosterID RosterId, pOasTestId OasTestId, pDeliveryStatus DeliveryStatus,
   		       pStartedDate Started_Date, pCompletedDate Completed_Date,
			   bSuccess AS Success, bErrorCode AS ErrorCode, bErrorMsg AS ErrorMsg FROM DUAL;

	EXCEPTION
	WHEN OTHERS THEN
		bSuccess := 'F';
		bErrorCode := 199;
		bErrorMsg  := 'Oracle Error in ValidateSaveTestStatus: '||SQLCODE||', '||SUBSTR(SQLERRM, 1, 80);

		OPEN pResultCursor FOR
		SELECT pRosterID RosterId, pOasTestId OasTestId, pDeliveryStatus DeliveryStatus,
   		       pStartedDate Started_Date, pCompletedDate Completed_Date,
			   bSuccess AS Success, bErrorCode AS ErrorCode, bErrorMsg AS ErrorMsg FROM DUAL;

		--DBMS_OUTPUT.PUT_LINE(bErrorMsg);

	END ValidateSaveTestStatus;

	/**********************************************************
	* PROCEDURE TO UPDATE THE TEST_ROSTER COMPLETION STATUS
	* BASED ON THE ROSTER SUB-TEST COMPLETION STATUS
	*
	* pCurrSubTestStatus = Current Sub-test status
	**********************************************************/
	PROCEDURE UpdateRosterTestStatus(
	   pRosterId           IN NUMBER,
	   pCurrSubTestStatus  IN VARCHAR2,
	   pStartDate          IN DATE,
	   pCompletionDate     IN DATE) AS

	   --pRosterId NUMBER := 8662811;
	   --pDeliveryStatus VARCHAR2(2);

	   CURSOR curSISS IS
	   SELECT Completion_Status, Item_set_Order FROM Student_Item_Set_Status
	   WHERE Test_Roster_Id = pRosterId
       ORDER BY Item_set_Order;

	   vFinalStatus VARCHAR2(2) := null;
	BEGIN

		FOR rec_SISS IN curSISS LOOP
		   --DBMS_OUTPUT.PUT_LINE('rec_SISS.Completion_Status is:'||rec_SISS.Completion_Status);
		   CASE rec_SISS.Completion_Status
				WHEN 'IP' THEN
					vFinalStatus := 'IP';
					EXIT;
				WHEN 'IN' THEN
				    IF pCurrSubTestStatus = 'IP' THEN
					   vFinalStatus := 'IP';
					ELSE
					   vFinalStatus := 'IN';
					END IF;
					EXIT;
				WHEN 'CO' THEN
				    IF pCurrSubTestStatus = 'IP' THEN
					   vFinalStatus := 'IP';
					   EXIT;
					END IF;
				    IF vFinalStatus = 'SC' THEN
					   vFinalStatus := 'IS';
					   EXIT;
					ELSE
					   vFinalStatus := 'CO';
					END IF;
				WHEN 'SC' THEN
                    CASE pCurrSubTestStatus
					    WHEN 'IP' THEN
					       vFinalStatus := 'IP';
					       EXIT;
                      	WHEN 'IN' THEN
					       vFinalStatus := 'IN';
					       EXIT;
                        WHEN 'CO' THEN
                            vFinalStatus := 'IS';
                            EXIT;
						ELSE
					       vFinalStatus := 'SC';
                    END CASE;

				ELSE
                  NULL;
		   END CASE;
		END LOOP;

		-- Update the Roster Status,  Update with CO if all the subtest is complete
		--DBMS_OUTPUT.PUT_LINE('vFinalStatus is:'||NVL(vFinalStatus, 'ZZZ'));
		IF vFinalStatus IS NOT NULL   THEN

			IF vFinalStatus = 'CO' THEN
				UPDATE Test_Roster ROS
				SET Completion_Date_Time = processCompletionDate(Completion_Date_Time, pCompletionDate),
					Updated_Date_Time = SYSDATE,
					Test_Completion_Status = DECODE((SELECT count(*) FROM Student_Item_Set_Status
						WHERE Test_Roster_Id = ROS.Test_Roster_Id AND Completion_Status != 'CO'), 0, 'CO', Test_Completion_Status)
				WHERE Test_Roster_Id = pRosterId;
			ELSIF vFinalStatus = 'IS' THEN
				UPDATE Test_Roster SET
					Test_Completion_Status = vFinalStatus,
					Start_Date_Time = processStartDate(Start_Date_Time, pStartDate),
					-- Fix BMTOAS-1835
					Completion_Date_Time = processCompletionDate(Completion_Date_Time, pCompletionDate),
					Updated_Date_Time = SYSDATE
				WHERE Test_Roster_ID = pRosterId;

			ELSE
				UPDATE Test_Roster SET
				   Test_Completion_Status = vFinalStatus,
				   Start_Date_Time = processStartDate(Start_Date_Time, pStartDate),
				   Updated_Date_Time = SYSDATE
				WHERE Test_Roster_ID = pRosterId;
			END IF;

		END IF;

	EXCEPTION
	WHEN OTHERS THEN
		DBMS_OUTPUT.PUT_LINE('SQL Error: While updating Test_Roster table:'||SQLERRM(SQLCODE));
		ROLLBACK;
	END UpdateRosterTestStatus;



END PKG_BMTSYNC_TESTSTATUS;
/
