CREATE OR REPLACE PACKAGE PKG_BMTSYNC_FIXEXPIRED AS

  PROCEDURE FixExpired_Assignment_Messages;
  
  PROCEDURE FixExpired_Student_Messages;
  
  PROCEDURE FixExpired_TestAdmin_Messages;
  
  PROCEDURE FixExpired_Messages_Wrapper;
  
  vReadyState   NUMBER := 0;
  vExpiredState NUMBER := 3;
  vElaspsedTimeout  NUMBER := 15/1440; -- 15 Minutes

END PKG_BMTSYNC_FIXEXPIRED;
/

CREATE OR REPLACE PACKAGE BODY PKG_BMTSYNC_FIXEXPIRED AS
 
    /* 
	*  Finds all the expired/exception messages in the last 15 minutes,
	*  mark the request as 'New' in the sync status table,
	* so that they are tried again for syncing
    */	
	PROCEDURE FixExpired_Assignment_Messages AS
		CURSOR tempCursor IS SELECT aqt.user_data.test_admin_id as test_admin_id, aqt.user_data.student_id as student_id 
		    FROM aqt_assignment aqt
			WHERE aqt.enq_time >= sysdate - vElaspsedTimeout and state = vExpiredState ;
		vExistsInQueue NUMBER :=1;
		vFinalInBMT NUMBER :=1;
	BEGIN
		FOR rec in tempCursor LOOP
			SELECT count(*) INTO vExistsInQueue FROM aqt_assignment aqt 
			WHERE aqt.user_data.test_admin_id = rec.test_admin_id AND aqt.user_data.student_id = rec.student_id
			  AND state = vReadyState;
			IF (vExistsInQueue = 0) THEN
				SELECT count(*) INTO vFinalInBMT from bmtsync_assignment_status 
				WHERE test_admin_id = rec.test_admin_id AND student_id = rec.student_id
					AND export_status in ('Success', 'Failed', 'No Retry');
				IF (vFinalInBMT = 0) THEN
					--DBMS_OUTPUT.PUT_LINE('Updating TEST_ADMIN_ID ' || rec.test_admin_id || ', STUDENT_ID ' || rec.student_id);
					UPDATE bmtsync_assignment_status bas 
					SET bas.export_status = 'New', bas.next_retry_datetime = sysdate 
					WHERE bas.test_admin_id = rec.test_admin_id
					AND bas.student_id = rec.student_id;
				END IF;      
			END IF;
		END LOOP;
		COMMIT;
	END FixExpired_Assignment_Messages;
  
  
	PROCEDURE FixExpired_Student_Messages AS
		CURSOR tempCursor IS SELECT aqt.user_data.student_id AS student_id FROM aqt_student aqt
		WHERE aqt.enq_time >= sysdate - vElaspsedTimeout AND state = vExpiredState ;
		vExistsInQueue NUMBER :=1;
		vFinalInBMT NUMBER :=1;
	BEGIN
		FOR rec in tempCursor LOOP
		DBMS_OUTPUT.PUT_LINE('Processing STUDENT_ID ' || rec.student_id);
			SELECT COUNT(*) INTO vExistsInQueue 
			FROM aqt_student aqt 
			WHERE aqt.user_data.Student_Id = rec.Student_Id AND state = vReadyState;
				
			IF (vExistsInQueue = 0) then
				SELECT COUNT(*) INTO vFinalInBMT FROM BmtSync_Student_Status 
				WHERE Student_Id = rec.Student_Id
				AND Export_Status in ('Success', 'Failed', 'No Retry');
				IF (vFinalInBMT = 0) THEN
					--DBMS_OUTPUT.PUT_LINE('Updating STUDENT_ID ' || rec.student_id);
					UPDATE BmtSync_Student_status bas 
					SET bas.Export_Status = 'New', bas.Next_Retry_DateTime = sysdate 
					WHERE bas.Student_Id = rec.Student_Id;
				END IF;      
			END IF;
		END LOOP;
		COMMIT;
	END FixExpired_Student_Messages;
	
	PROCEDURE FixExpired_TestAdmin_Messages AS
		CURSOR tempCursor IS SELECT aqt.user_data.Test_Admin_Id AS Test_Admin_Id FROM Aqt_TestAdmin aqt
		where aqt.enq_time >= sysdate - vElaspsedTimeout and state = vExpiredState ;
          		
		vExistsInQueue NUMBER :=1;
		vFinalInBMT NUMBER :=1;
	BEGIN
		FOR rec in tempCursor LOOP
			SELECT COUNT(*) INTO vExistsInQueue 
			FROM aqt_testadmin aqt 
			WHERE aqt.user_data.Test_Admin_Id = rec.Test_Admin_Id AND state = vReadyState;
			IF (vExistsInQueue = 0) then
				SELECT COUNT(*) INTO vFinalInBMT FROM bmtsync_testadmin_status 
				WHERE Test_Admin_ID = rec.Test_Admin_Id
				AND export_status in ('Success', 'Failed', 'No Retry');
				IF (vFinalInBMT = 0) THEN
					--DBMS_OUTPUT.PUT_LINE('Updating TESTADMIN_ID ' || rec.Test_Admin_Id);
					UPDATE BmtSync_TestAdmin_Status bas 
					SET bas.Export_Status = 'New', bas.Next_Retry_DateTime = sysdate 
					WHERE bas.Test_Admin_Id = rec.Test_Admin_Id;
				END IF;      
			END IF;
		END LOOP;
		COMMIT;
	END FixExpired_TestAdmin_Messages;  	
	
	
	/*
	* Wrapper procedure, it will be scheduled to run 
	* as an Oracle job after every 10 minutes 
	*/
	PROCEDURE FixExpired_Messages_Wrapper AS
	
	BEGIN

		FixExpired_Assignment_Messages;
		FixExpired_Student_Messages;
		FixExpired_TestAdmin_Messages;	
		
	END FixExpired_Messages_Wrapper;

END PKG_BMTSYNC_FIXEXPIRED;
/