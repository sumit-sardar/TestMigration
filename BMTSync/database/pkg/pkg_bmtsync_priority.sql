create or replace PACKAGE PKG_BMTSYNC_PRIORITY AS 

  FUNCTION mode_name (mode_in IN INTEGER) RETURN VARCHAR2;

  PROCEDURE prioritizeStudent (
    pStudentID IN NUMBER,
    pCustomerID IN NUMBER
  );
  
  PROCEDURE prioritizeTestAssignment (
    pTestAdminId IN NUMBER,
    pStudentID IN NUMBER,
    pCustomerID IN NUMBER
  );
  
  PROCEDURE prioritizeTestAdmin (
    pTestAdminID IN NUMBER,
    pCustomerID IN NUMBER
  );
  
  PROCEDURE prioritizeCustomer (
    pCustomerID IN NUMBER
  );
END PKG_BMTSYNC_PRIORITY;
/

create or replace PACKAGE BODY PKG_BMTSYNC_PRIORITY AS

  /* Translate mode number to a name. */
  FUNCTION mode_name (mode_in IN INTEGER) RETURN VARCHAR2 IS
  BEGIN
    IF    mode_in = DBMS_AQ.REMOVE THEN RETURN 'REMOVE';
    ELSIF mode_in = DBMS_AQ.BROWSE THEN RETURN 'BROWSE';
    END IF;
  END;

  -- Procedures to add customers in at top priority.

  PROCEDURE prioritizeStudent (
    pStudentID IN NUMBER,
    pCustomerID IN NUMBER
  )
  AS
    VMESSAGE            STUDENT_MESSAGE_TYP;
		VENQUEUEOPTIONS	    DBMS_AQ.ENQUEUE_OPTIONS_T;
		VMESSAGEPROPERTIES 	DBMS_AQ.MESSAGE_PROPERTIES_T;
		VQUEUENAME 				  VARCHAR2(30) := 'AQ_STUDENT';
		VMSGID					    RAW(16);
    VNOW                timestamp;
    
    BEGIN
      select sysdate into VNOW from dual;
      VMESSAGE := student_message_typ(pStudentID, pCustomerID, VNOW);
      VMESSAGEPROPERTIES.EXPIRATION := DBMS_AQ.NEVER;
      VMESSAGEPROPERTIES.PRIORITY := 1;
      dbms_aq.enqueue(
        queue_name => VQUEUENAME,
        enqueue_options => VENQUEUEOPTIONS,
        message_properties => VMESSAGEPROPERTIES,
        payload => VMESSAGE,
        msgid => VMSGID
      );
    COMMIT;
  END prioritizeStudent;
  
  PROCEDURE prioritizeTestAssignment(
    pTestAdminId IN NUMBER,
    pStudentID IN NUMBER,
    pCustomerID IN NUMBER
  )
  AS
    VMESSAGE            BMTSYNC_ASSIGNMENT_TYP;
    VROSTERID           INTEGER;
		VENQUEUEOPTIONS	    DBMS_AQ.ENQUEUE_OPTIONS_T;
		VMESSAGEPROPERTIES 	DBMS_AQ.MESSAGE_PROPERTIES_T;
		VQUEUENAME 				  VARCHAR2(30) := 'AQ_ASSIGNMENT';
		VMSGID					    RAW(16);
    VNOW                timestamp;
    
    BEGIN
      select sysdate into VNOW from dual;
      select roster_id into VROSTERID from BMTSYNC_ASSIGNMENT_STATUS
          where test_admin_id = pTestAdminID and student_id = pStudentID;
      VMESSAGE := bmtsync_assignment_typ(pCustomerID, pTestAdminId, pStudentID, VROSTERID, VNOW);
      VMESSAGEPROPERTIES.EXPIRATION := DBMS_AQ.NEVER;
      VMESSAGEPROPERTIES.PRIORITY := 1;
      dbms_aq.enqueue(
        queue_name => VQUEUENAME,
        enqueue_options => VENQUEUEOPTIONS,
        message_properties => VMESSAGEPROPERTIES,
        payload => VMESSAGE,
        msgid => VMSGID
      );
    COMMIT;
  END prioritizeTestAssignment;

  PROCEDURE prioritizeTestAdmin (
    pTestAdminID IN NUMBER,
    pCustomerID IN NUMBER
  )
  AS
    VMESSAGE            BMTSYNC_TESTADMIN_TYP;
		VENQUEUEOPTIONS	    DBMS_AQ.ENQUEUE_OPTIONS_T;
		VMESSAGEPROPERTIES 	DBMS_AQ.MESSAGE_PROPERTIES_T;
		VQUEUENAME 				  VARCHAR2(30) := 'AQ_TESTADMIN';
		VMSGID					    RAW(16);
    
    BEGIN
      VMESSAGE := BMTSYNC_TESTADMIN_TYP(pTestAdminID, pCustomerID);
      VMESSAGEPROPERTIES.EXPIRATION := DBMS_AQ.NEVER;
      VMESSAGEPROPERTIES.PRIORITY := 1;
      dbms_aq.enqueue(
        queue_name => VQUEUENAME,
        enqueue_options => VENQUEUEOPTIONS,
        message_properties => VMESSAGEPROPERTIES,
        payload => VMESSAGE,
        msgid => VMSGID
      );
    COMMIT;
  END prioritizeTestAdmin;

  PROCEDURE prioritizeCustomer (
    pCustomerID IN NUMBER
  )
  AS
    CURSOR studentIds IS
      SELECT DISTINCT student_id from BMTSYNC_ASSIGNMENT_STATUS
      WHERE customer_id = pCustomerID;
    CURSOR rosterIds IS
      SELECT DISTINCT test_admin_id, student_id from BMTSYNC_ASSIGNMENT_STATUS
      WHERE customer_id = pCustomerID;  
    CURSOR testAdminIds IS
      SELECT DISTINCT test_admin_id from BMTSYNC_TESTADMIN_STATUS
      WHERE customer_id = pCustomerID;     

    BEGIN
    FOR student IN studentIds LOOP
      prioritizeStudent(student.student_id, pCustomerId);
    END LOOP;
    FOR roster IN rosterIds LOOP
      prioritizeTestAssignment(roster.test_admin_id, roster.student_id, pCustomerId);
    END LOOP;
    FOR admin IN testAdminIds LOOP
      prioritizeTestAdmin(admin.test_admin_id, pCustomerId);
    END LOOP;    
    
  END prioritizeCustomer;

END PKG_BMTSYNC_PRIORITY;