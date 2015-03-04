CREATE OR REPLACE PACKAGE PKG_BMTSYNC_STUDENTQUEUE AS
/*
*========================================================================================
* AUTHOR  : SANJEEV B
* CREATED : 11/12/2014
* PURPOSE : THIS PACKAGE WILL CONTAIN PROCEDURES ENQUEUING AND DEQUEUEING STUDENT QUEUE
*========================================================================================
*/
	TYPE REF_CURSOR_TYPE IS REF CURSOR;
	QUEUEPREFIX CONSTANT VARCHAR2(5)      := 'AQ_';
	QUEUETABLEPREFIX CONSTANT VARCHAR2(5) := 'AQT_';

	/*
	 * THIS PROCEDURE WILL ADD A STUDENT MESSAGE INTO THE QUEUE
	 */
	PROCEDURE ADD_STUDENT_TOQUEUE
	(
		PCUSTOMERID  IN NUMBER,
		PSTUDENTID   IN NUMBER,
		PDATE        IN DATE
	);
	
	/*
	 * THIS PROCEDURE WILL BE USED TO FETCH THE NEXT AVAILABLE STUDENT
	 *
	 * SEE COMMENTS INLINE
	 */
	PROCEDURE FETCHSTUDENT_MSG
	(
		PSTUDENTID OUT NUMBER
	);
	
END PKG_BMTSYNC_STUDENTQUEUE;	
/


CREATE OR REPLACE PACKAGE BODY PKG_BMTSYNC_STUDENTQUEUE AS

	/*
	 * THIS PROCEDURE WILL ADD A STUDENT MESSAGE INTO THE QUEUE
	 */
	PROCEDURE ADD_STUDENT_TOQUEUE
	(
		PCUSTOMERID  IN NUMBER,
		PSTUDENTID   IN NUMBER,
		PDATE        IN DATE
	) AS
	    PSTUDENTMESSAGE         STUDENT_MESSAGE_TYP;
		VENQUEUEOPTIONS	     	DBMS_AQ.ENQUEUE_OPTIONS_T;
		VMESSAGEPROPERTIES 		DBMS_AQ.MESSAGE_PROPERTIES_T;
		VQUEUENAME 				VARCHAR2(30) := 'AQ_STUDENT';
		VMSGID					RAW(16);
	BEGIN
        --Create student message
		PSTUDENTMESSAGE := STUDENT_MESSAGE_TYP( PSTUDENTID, PCUSTOMERID, PDATE);
		
		VMESSAGEPROPERTIES.EXPIRATION     := DBMS_AQ.NEVER;
		VMESSAGEPROPERTIES.PRIORITY := 9;

		-- PUSH THE MESSAGE ONTO THE QUEUE
		DBMS_AQ.ENQUEUE(VQUEUENAME, VENQUEUEOPTIONS, VMESSAGEPROPERTIES, PSTUDENTMESSAGE, VMSGID);
		--DBMS_OUTPUT.PUT_LINE(VMSGID);

	EXCEPTION
		WHEN OTHERS THEN
			RAISE_APPLICATION_ERROR(-20001, 'PKG_STUDENTQUEUE.ADD_STUDENT_TOQUEUE FAILURE :' || SQLERRM(SQLCODE));
	END ADD_STUDENT_TOQUEUE;


	/*
	 * THIS PROCEDURE WILL BE USED TO FETCH THE NEXT AVAILABLE STUDENT
	 *
	 * SEE COMMENTS INLINE
	 */
	PROCEDURE FETCHSTUDENT_MSG
	(
		PSTUDENTID OUT NUMBER
	)
	AS
		VQUEUENAME				    VARCHAR2(30);
		VDEQUEUEOPTIONS     	    DBMS_AQ.DEQUEUE_OPTIONS_T;
		VDQMESSAGEPROPERTIES        DBMS_AQ.MESSAGE_PROPERTIES_T;
		VMESSAGEID		      	    RAW(16);
		TYPSTUDENTMSG     		    STUDENT_MESSAGE_TYP;
		TYPSELECTEDSTUDENTMSG       STUDENT_MESSAGE_TYP;
		NO_MESSAGES                 EXCEPTION;
		PRAGMA EXCEPTION_INIT (NO_MESSAGES, -25228);
		VPASSEDSCOPINGRULES         CHAR(1) := 'N';
		vUpdateddateTime            DATE;
		

	BEGIN
		VQUEUENAME := QUEUEPREFIX || 'STUDENT';
		-- WE WANT TO LOCK THE MESSAGES AS WE LOOK AT THEM
		VDEQUEUEOPTIONS.DEQUEUE_MODE := DBMS_AQ.LOCKED;

		-- WE DO NOT WANT TO WAIT FOR A MESSAGE TO BECOME AVAILABLE.  IF THE QUEUE IS EMPTY, MOVE ON.
		VDEQUEUEOPTIONS.WAIT := DBMS_AQ.NO_WAIT;

		-- START AT THE FIRST MESSAGE IN THE QUEUE   (MESSAGES WILL ALREADY BE PRIORITIZED)
		VDEQUEUEOPTIONS.NAVIGATION := DBMS_AQ.FIRST_MESSAGE;

		--VDEQUEUEOPTIONS.CONSUMER_NAME := 'Student_Subscriber';

		/*
		 	FIRST WE WILL LOOP THROUGH THE STUDENT IN THIS STUDENT QUEUE TABLE, LOOKING FOR A STUDENT TO EXPORT.
		 */
		LOOP

			BEGIN
				--FETCH A STUDENT OFF THE QUEUE
				DBMS_AQ.DEQUEUE(VQUEUENAME, VDEQUEUEOPTIONS, VDQMESSAGEPROPERTIES, TYPSTUDENTMSG, VMESSAGEID);
				--DBMS_OUTPUT.PUT_LINE(vMessageID || '   ' || TYPSTUDENTMSG.Student_ID || ',' || TYPSTUDENTMSG.UPDATED_DATE_TIME);

			EXCEPTION
			  WHEN NO_MESSAGES THEN
			  	--DBMS_OUTPUT.PUT_LINE('NO_MESSAGES');
			  	--RETURN AN EMPTY CURSOR
				--OPEN PRESULTCURSOR FOR
				--SELECT STUDENT_MESSAGE_TYP(0, 0, NULL) FROM DUAL;
				--RETURN;
				--NULL;
				PSTUDENTID := NULL;
				VPASSEDSCOPINGRULES := 'N';
				EXIT;
			  WHEN NO_DATA_FOUND THEN
			  	-- DBMS_OUTPUT.PUT_LINE('NO_MESSAGES');
			  	--RETURN AN EMPTY CURSOR
				--OPEN PRESULTCURSOR FOR
				--SELECT STUDENT_MESSAGE_TYP(0, 0, NULL) FROM DUAL;
				--RETURN;
				--NULL;
				PSTUDENTID := NULL;
				VPASSEDSCOPINGRULES := 'N';
				EXIT;

			END;

			--Check if the updated_date_time in the message matches with the updated_date_time in database.
			--If they do not match ignore the message and move to the next one, 
			--it mean this message is old and there should be another message
			SELECT UPDATED_DATE_TIME INTO vUpdateddateTime FROM Student Where Student_ID = TYPSTUDENTMSG.Student_ID;
			--DBMS_OUTPUT.PUT_LINE(vUpdateddateTime||': '||TYPSTUDENTMSG.UPDATED_DATE_TIME);
			IF vUpdateddateTime = TYPSTUDENTMSG.UPDATED_DATE_TIME 
                          OR vUpdateddateTime IS NULL THEN
			   VPASSEDSCOPINGRULES := 'Y';
			   EXIT ;
			END IF;
			VDEQUEUEOPTIONS.NAVIGATION := DBMS_AQ.NEXT_MESSAGE;
		END LOOP;

		--###########################################################################
		-- REMOVE THE STUDENT WE FOUND TO EXPORT
		--###########################################################################
		IF VPASSEDSCOPINGRULES = 'Y' THEN
                        --DBMS_OUTPUT.PUT_LINE(VMESSAGEID||'...Removed');
			VDEQUEUEOPTIONS.DEQUEUE_MODE  := DBMS_AQ.REMOVE;
			VDEQUEUEOPTIONS.MSGID         := VMESSAGEID;
			VDEQUEUEOPTIONS.DEQ_CONDITION := NULL;

			DBMS_AQ.DEQUEUE(VQUEUENAME, VDEQUEUEOPTIONS, VDQMESSAGEPROPERTIES, TYPSELECTEDSTUDENTMSG, VMESSAGEID);
			COMMIT;
            
			PSTUDENTID := TYPSELECTEDSTUDENTMSG.STUDENT_ID;
			DBMS_OUTPUT.PUT_LINE(PSTUDENTID||'...Removed');
			--OPEN PRESULTCURSOR FOR
			--SELECT TYPSELECTEDSTUDENTMSG FROM DUAL;
		END IF;
		
		--INSERT INTO DEMO_QUEUE_TABLE (Message) VALUES ('Message Inserted on '||TO_CHAR(SYSTIMESTAMP, 'DD-MON-YYYY HH24:MI:SS.FF3'));
		--COMMIT;

	EXCEPTION
		WHEN NO_DATA_FOUND THEN
			  	DBMS_OUTPUT.PUT_LINE('NO_DATA');
			  	--RETURN AN EMPTY CURSOR
				--OPEN PRESULTCURSOR FOR
				--SELECT STUDENT_MESSAGE_TYP(0, 0, NULL) FROM DUAL;
				--RETURN;
				--NULL;
				PSTUDENTID := NULL;
				VPASSEDSCOPINGRULES := 'N';	
		WHEN OTHERS THEN
			RAISE_APPLICATION_ERROR(-20001, 'PKG_STUDENTQUEUE.FETCHSTUDENT_MSG FAILURE :' || SQLERRM(SQLCODE));
	END FETCHSTUDENT_MSG;
	
	
END PKG_BMTSYNC_STUDENTQUEUE;
/

set TERMOUT on
PROMPT PKG_BMTSYNC_STUDENTQUEUE compiled

