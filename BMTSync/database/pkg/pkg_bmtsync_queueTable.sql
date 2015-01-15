CREATE OR REPLACE PACKAGE PKG_BMTSYNC_QUEUETABLE AS
/*
*========================================================================================
* AUTHOR  : SANJEEV B
* CREATED : 11/12/2014
* PURPOSE : THIS PACKAGE WILL CONTAIN PROCEDURES FOR WORKING WITH ORACLE QUEUE TABLES
*========================================================================================
*/
	TYPE REF_CURSOR_TYPE IS REF CURSOR;
	QUEUEPREFIX CONSTANT VARCHAR2(5)      := 'AQ';
	QUEUETABLEPREFIX CONSTANT VARCHAR2(5) := 'AQT';
	
	/*
	 * THIS PROCEDURE WILL ADD A NEW QUEUE TABLE IF IT DOESN'T ALREADY EXIST
	 * eg. 
	 * PQUEUETABLE       = 'AQT_STUDENT'
	 * PSORTLIST         = 'PRIORITY,ENQ_TIME'
	 * PQUEUEPAYLOADTYPE = 'STUDENT_MESSAGE_TYP'
	 */
	PROCEDURE CREATEQUEUETABLE
	(
		PQUEUETABLE IN VARCHAR2,
		PSORTLIST IN VARCHAR2,
		PQUEUEPAYLOADTYPE IN VARCHAR2,
		PRESULTCURSOR OUT REF_CURSOR_TYPE
	);

	/*
	 * THIS PROCEDURE WILL ADD A QUEUE, AND START IT,  IF IT DOESN'T ALREADY EXIST
	 * eg.
	 * PQUEUETABLE = 'AQT_ATUDENT'
	 * PQUEUENAME  = 'AQ_STUDENT'
	 * PCOMMENT    = Any comment, describing the queue
	 * PSTARTIT    = 1, IF YOU WANT TO START THE QUEUE ONCE ITS CREATED
	 */	
	PROCEDURE CREATEQUEUE
	(
		PQUEUETABLE IN VARCHAR2,
		PQUEUENAME IN VARCHAR2,
		PCOMMENT IN VARCHAR2,
		PSTARTIT IN CHAR,
		PRESULTCURSOR OUT REF_CURSOR_TYPE
	);

	/*
	 * THIS PROCEDURE WILL CREATE A NEW QUEUE TABLE AND QUEUE, AND START IT
	 */
	PROCEDURE CREATEQUEUEWITHTABLE
	(
        PQUEUENAME IN VARCHAR2,
	    PQUEUETABLE IN VARCHAR2,
		PSORTLIST IN VARCHAR2,
		PQUEUEPAYLOADTYPE IN VARCHAR2,
		PCOMMENT IN VARCHAR2,
		PSTARTIT IN CHAR,
		PRESULTCURSOR OUT REF_CURSOR_TYPE
	);

	/*
	 * THIS PROCEDURE WILL ADD QUEUE FOR THE SPECIFIED API IN THE SYSTEM
	*/ 
	PROCEDURE CREATEQUEUESFORAPI
	(
	    pAPI          IN VARCHAR2,
		pMessageType  IN  VARCHAR2,
		PRESULTCURSOR OUT REF_CURSOR_TYPE
	);

END PKG_BMTSYNC_QUEUETABLE;
/


CREATE OR REPLACE PACKAGE BODY PKG_BMTSYNC_QUEUETABLE AS

	/*
	 * THIS PROCEDURE WILL ADD A NEW QUEUE TABLE IF IT DOESN'T ALREADY EXIST
	 * eg. 
	 * PQUEUETABLE       = 'AQT_STUDENT'
	 * PSORTLIST         = 'PRIORITY,ENQ_TIME'
	 * PQUEUEPAYLOADTYPE = 'STUDENT_MESSAGE_TYP'
	 */
	PROCEDURE CREATEQUEUETABLE
	(
		PQUEUETABLE IN VARCHAR2,
		PSORTLIST   IN VARCHAR2,
		PQUEUEPAYLOADTYPE IN VARCHAR2, 
		PRESULTCURSOR OUT REF_CURSOR_TYPE
	) AS
		VEXISTS NUMBER := 0;
		VSCHEMANAME VARCHAR2(50);
	BEGIN
		-- GRAB THE SCHEMA NAME
		SELECT SYS_CONTEXT('USERENV','SESSION_USER') || '.' INTO VSCHEMANAME FROM DUAL;

		-- MAKE SURE IT DOESN'T ALREADY EXIST   REMOVE THE SCHEMA NAME FROM THE FRONT OF THE QUEUE TABLE NAME
		-- WE PASS IN SCHEMA.TABLE BUT THE USER_TABLES VIEW ONLY HOLDS TABLE
		SELECT COUNT(*) INTO VEXISTS FROM USER_TABLES WHERE LOWER(TABLE_NAME) = LOWER(REPLACE(PQUEUETABLE, VSCHEMANAME, ''));
		DBMS_OUTPUT.PUT_LINE('SANJEEV PQUEUETABLE:'||PQUEUETABLE);
		IF (VEXISTS = 0) THEN
			DBMS_AQADM.CREATE_QUEUE_TABLE (
				QUEUE_TABLE        	=> PQUEUETABLE,
				QUEUE_PAYLOAD_TYPE 	=> PQUEUEPAYLOADTYPE,
				SORT_LIST		=> PSORTLIST
			);
			OPEN PRESULTCURSOR FOR SELECT 'SUCCESS' "RESULT" FROM DUAL;
		ELSE
			OPEN PRESULTCURSOR FOR SELECT 'QUEUE ALREADY EXISTS' "RESULT" FROM DUAL;
		END IF;

	EXCEPTION
		WHEN OTHERS THEN
			RAISE_APPLICATION_ERROR(-20001, 'PKG_BMTSYNC_QUEUETABLE.CREATEQUEUETABLE FAILURE :' || SQLERRM(SQLCODE));
	END CREATEQUEUETABLE;
	
	/*
	 * THIS PROCEDURE WILL ADD A QUEUE, AND START IT,  IF IT DOESN'T ALREADY EXIST
	 * eg.
	 * PQUEUETABLE = 'AQT_ATUDENT'
	 * PQUEUENAME  = 'AQ_STUDENT'
	 * PCOMMENT    = Any comment, describing the queue
	 * PSTARTIT    = 1, IF YOU WANT TO START THE QUEUE ONCE ITS CREATED
	 */
	PROCEDURE CREATEQUEUE
	(
		PQUEUETABLE IN VARCHAR2,
		PQUEUENAME IN VARCHAR2,
		PCOMMENT IN VARCHAR2,
		PSTARTIT IN CHAR,
		PRESULTCURSOR OUT REF_CURSOR_TYPE
	) AS
		VEXISTS NUMBER := 0;
	BEGIN

		--MAKE SURE IT DOESN'T ALREADY EXIST
		SELECT COUNT(*) INTO VEXISTS FROM USER_QUEUES WHERE QUEUE_TABLE = PQUEUENAME;

		IF (VEXISTS = 0) THEN
			DBMS_AQADM.CREATE_QUEUE (
				QUEUE_NAME         => PQUEUENAME,
				QUEUE_TABLE        => PQUEUETABLE,
				COMMENT            => PCOMMENT
			);

			IF PSTARTIT = '1' THEN
				DBMS_AQADM.START_QUEUE (PQUEUENAME);
			END IF;

			OPEN PRESULTCURSOR FOR SELECT 'SUCCESS' "RESULT" FROM DUAL;

		ELSE
			OPEN PRESULTCURSOR FOR SELECT 'QUEUE ALREADY EXISTS' "RESULT" FROM DUAL;
		END IF;

	EXCEPTION
		WHEN OTHERS THEN
			RAISE_APPLICATION_ERROR(-20001, 'PKG_BMTSYNC_QUEUETABLE.ADDQUEUE FAILURE :' || SQLERRM(SQLCODE));
	END CREATEQUEUE;
	
	/*
	 * THIS PROCEDURE WILL CREATE A NEW QUEUE TABLE AND QUEUE, AND START IT
	 */
	PROCEDURE CREATEQUEUEWITHTABLE
	(
        PQUEUENAME IN VARCHAR2,
	    PQUEUETABLE IN VARCHAR2,
		PSORTLIST IN VARCHAR2,
		PQUEUEPAYLOADTYPE IN VARCHAR2,
		PCOMMENT IN VARCHAR2,
		PSTARTIT IN CHAR,
		PRESULTCURSOR OUT REF_CURSOR_TYPE
	) AS

	BEGIN

		CREATEQUEUETABLE(PQUEUETABLE, PSORTLIST, PQUEUEPAYLOADTYPE, PRESULTCURSOR);
		CLOSE PRESULTCURSOR;
		CREATEQUEUE(PQUEUETABLE, PQUEUENAME, PCOMMENT, PSTARTIT, PRESULTCURSOR);
		CLOSE PRESULTCURSOR;

		OPEN PRESULTCURSOR FOR SELECT 'SUCCESS' "RESULT" FROM DUAL;

	EXCEPTION
		WHEN OTHERS THEN
			RAISE_APPLICATION_ERROR(-20001, 'PKG_BMTSYNC_QUEUETABLE.CREATEQUEUEWITHTABLE FAILURE :' || SQLERRM(SQLCODE));
	END CREATEQUEUEWITHTABLE;

	/*
	 * THIS PROCEDURE WILL ADD QUEUE FOR THE SPECIFIED API IN THE SYSTEM
	 */
	PROCEDURE CREATEQUEUESFORAPI
	(
	    pAPI          IN VARCHAR2,
		pMessageType  IN  VARCHAR2,
		PRESULTCURSOR OUT REF_CURSOR_TYPE
	) AS
		VQUEUENAME VARCHAR2(30);
		VQUEUETABLE VARCHAR2(50);
		VSCHEMANAME VARCHAR2(30);
		VRESULTCURSOR REF_CURSOR_TYPE;

	BEGIN

		SELECT SYS_CONTEXT('USERENV','SESSION_USER') INTO VSCHEMANAME FROM DUAL;



		--ADD A QUEUE TABLE AND QUEUE 
		VQUEUENAME := QUEUEPREFIX||'_'|| pAPI;
		VQUEUETABLE := VSCHEMANAME || '.' || QUEUETABLEPREFIX ||'_'|| pAPI;
		
        DBMS_OUTPUT.PUT_LINE('VQUEUENAME:'||VQUEUENAME||', VQUEUETABLE:'||VQUEUETABLE);
		
		CREATEQUEUEWITHTABLE(
        VQUEUENAME,		
        VQUEUETABLE, 
        'PRIORITY,ENQ_TIME', 
        pMessageType, 
        'THIS IS A QUEUE FOR API : '||pAPI, 
        '1', 
        VRESULTCURSOR);

		OPEN PRESULTCURSOR FOR SELECT 'SUCCESS' "RESULT" FROM DUAL;

	EXCEPTION
		WHEN OTHERS THEN
			RAISE_APPLICATION_ERROR(-20001, 'PKG_BMTSYNC_QUEUETABLE.CREATEQUEUESFORAPI FAILURE :' || SQLERRM(SQLCODE));
	END CREATEQUEUESFORAPI;
	
END PKG_BMTSYNC_QUEUETABLE;
/

set TERMOUT on
PROMPT PKG_BMTSYNC_QUEUETABLE compiled



