CREATE OR REPLACE PACKAGE OAS_GA_UPDATE_SR_STUDENTS_2015 IS

  /**
  * Main process to update students assigned form 
  * based on screen reader accommodation.
  */
  PROCEDURE UPDATE_SR_STUDENTS_FORM(VCUSTOMERID INTEGER,
                                    VPRODUCTID  INTEGER);
END OAS_GA_UPDATE_SR_STUDENTS_2015;
/
CREATE OR REPLACE PACKAGE BODY OAS_GA_UPDATE_SR_STUDENTS_2015 IS

  V_ACTIVATION_STATUS      CONSTANT VARCHAR2(2) := 'AC';
  V_VALIDATION_STATUS      CONSTANT VARCHAR2(2) := 'VA';
  V_SCHEDULED_STATUS       CONSTANT VARCHAR2(2) := 'SC';
  V_SCREEN_READER_VALID    CONSTANT VARCHAR2(2) := 'T';
  V_TEST_TIME_EXPIRED      CONSTANT VARCHAR2(1) := 'F';
  V_FORM_GA_EOG_1T         CONSTANT VARCHAR2(2) := '1T';
  V_FORM_GA_EOG_3T         CONSTANT VARCHAR2(2) := '3T';
  V_GA_EOG_2015_PRODUCT_ID CONSTANT INTEGER := 37;

  --PRESCHEDULING CONFIGURATION
  VROSTER_STATUS_FLAG VARCHAR2(300) := 'Roster_Status_Flag';

  /**
  * Retrieve all scheduled test session for given customer and product
  */
  CURSOR GET_SCHEDULEDTEST_FOR_CUSTPROD(VCUSTOMERID CUSTOMER.CUSTOMER_ID%TYPE, VPRODUCTID PRODUCT.PRODUCT_ID%TYPE) IS
    SELECT TEST_ADMIN_ID
      FROM TEST_ADMIN ADM
     WHERE ADM.CUSTOMER_ID = VCUSTOMERID
       AND ADM.PRODUCT_ID = VPRODUCTID
       AND ADM.ACTIVATION_STATUS = V_ACTIVATION_STATUS;

  /**
  * Retrieve all roster details for given test sesions
  */
  CURSOR GET_ROSTERS_FOR_SESSION(VTESTADMINID TEST_ADMIN.TEST_ADMIN_ID%TYPE) IS
    SELECT DISTINCT TR.TEST_ROSTER_ID
      FROM TEST_ROSTER TR, TEST_ADMIN TA, STUDENT_ACCOMMODATION STDACCO
     WHERE TA.TEST_ADMIN_ID = VTESTADMINID
       AND TR.TEST_ADMIN_ID = TA.TEST_ADMIN_ID
       AND TR.ACTIVATION_STATUS = V_ACTIVATION_STATUS
       AND TR.TEST_COMPLETION_STATUS = V_SCHEDULED_STATUS
       /*AND (TR.FORM_ASSIGNMENT <> V_FORM_GA_EOG_1T OR
           TR.FORM_ASSIGNMENT <> V_FORM_GA_EOG_3T)*/
       AND TR.FORM_ASSIGNMENT <> V_FORM_GA_EOG_3T    -- Modified on 03/19 
       AND STDACCO.STUDENT_ID = TR.STUDENT_ID
       AND STDACCO.SCREEN_READER = V_SCREEN_READER_VALID
       AND EXISTS
     (SELECT 1
              FROM CUSTOMER_CONFIGURATION CC
             WHERE CC.CUSTOMER_ID = TR.CUSTOMER_ID
               AND CC.CUSTOMER_CONFIGURATION_NAME = VROSTER_STATUS_FLAG);

  /**
  * Get all TS for a roster
  */
  CURSOR CUR_GET_ALL_TS_FOR_ROSTER(V_TEST_ADMIN_ID TEST_ADMIN.TEST_ADMIN_ID%TYPE) IS
    SELECT ISET.ITEM_SET_ID
      FROM ITEM_SET ISET, ITEM_SET_PARENT ISP, TEST_ADMIN TA
     WHERE ISET.ACTIVATION_STATUS = 'AC'
       AND ISET.ITEM_SET_ID = ISP.ITEM_SET_ID
       AND ISET.ITEM_SET_TYPE = 'TS'
       AND ISP.PARENT_ITEM_SET_ID = TA.ITEM_SET_ID /*-- TC ItemsetId*/
       AND TA.TEST_ADMIN_ID = V_TEST_ADMIN_ID
     ORDER BY ISP.ITEM_SET_SORT_ORDER;

  /**
  * Get all TD for a TS and assigned form
  */
  CURSOR CUR_GET_ALL_TD_FOR_FORM(V_ITEM_SET_ID_TS ITEM_SET.ITEM_SET_ID%TYPE, V_FORM ITEM_SET.ITEM_SET_FORM%TYPE) IS
    SELECT ISET.ITEM_SET_ID
      FROM ITEM_SET ISET, ITEM_SET_PARENT ISP
     WHERE ISET.ITEM_SET_ID = ISP.ITEM_SET_ID
       AND ISP.PARENT_ITEM_SET_ID = V_ITEM_SET_ID_TS
       AND ISET.ITEM_SET_FORM = V_FORM
       AND ISET.ACTIVATION_STATUS = 'AC'
     ORDER BY ISET.ITEM_SET_FORM, ISP.ITEM_SET_SORT_ORDER;

  /**
  * Main procedure to execute the update query for all scheduled roster
  **/
  PROCEDURE UPDATE_SR_STUDENTS_FORM(VCUSTOMERID INTEGER,
                                    VPRODUCTID  INTEGER) IS
  
    V_REC_COUNT               NUMBER(10) := 0;
    V_ITEM_SET_ORDER          INTEGER := 0;
    VPRODUCTNAME              PRODUCT.PRODUCT_NAME%TYPE := NULL;
    V_IS_CURSOR_RETURNS_VALUE BOOLEAN := FALSE;
  
  BEGIN
  
    /**
    * Validation of given CUSTOMER_ID
    */
    BEGIN
    
      SELECT 1
        INTO V_REC_COUNT
        FROM CUSTOMER C
       WHERE C.CUSTOMER_ID = VCUSTOMERID
         AND EXISTS
       (SELECT 1
                FROM CUSTOMER_CONFIGURATION CC
               WHERE CC.CUSTOMER_CONFIGURATION_NAME = 'GA_Customer'
                 AND CC.CUSTOMER_ID = C.CUSTOMER_ID);
    
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20000,
                                'Customer id [' || VCUSTOMERID ||
                                '] is not valid GA Customer. Give Existing GA Customer Id.');
    END;
  
    /**
    * Validation of given PRODUCT_ID
    */
    BEGIN
    
      SELECT PRO.PRODUCT_NAME
        INTO VPRODUCTNAME
        FROM PRODUCT PRO
       WHERE PRO.PRODUCT_ID = VPRODUCTID;
    
      IF VPRODUCTID <> V_GA_EOG_2015_PRODUCT_ID THEN
        RAISE_APPLICATION_ERROR(-20000,
                                'Product id [' || VPRODUCTID ||
                                '] is not valid GA EOG product. Give GA EOG Product Id.');
      END IF;
    
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20000,
                                'Product id [' || VPRODUCTID ||
                                '] is not valid. Give Existing Id.');
    END;
  
    -----------------------------
    ------- Process START -------
    -----------------------------
    DBMS_OUTPUT.PUT_LINE('Process STARTED...');
    ------- LOOP 1.0 START ------
  
    FOR RECADMIN IN GET_SCHEDULEDTEST_FOR_CUSTPROD(VCUSTOMERID, VPRODUCTID) LOOP
    
      /** Initialize item set order for each roster **/
      V_ITEM_SET_ORDER := 0;
    
      ------- LOOP 1.0.1 START ------
      V_IS_CURSOR_RETURNS_VALUE := FALSE;
      FOR REC_ROSTER IN GET_ROSTERS_FOR_SESSION(RECADMIN.TEST_ADMIN_ID) LOOP
        V_IS_CURSOR_RETURNS_VALUE := TRUE;
      
        /** Updating form value for the roster **/
        UPDATE TEST_ROSTER TR
           SET TR.FORM_ASSIGNMENT = V_FORM_GA_EOG_3T
         WHERE TR.TEST_ROSTER_ID = REC_ROSTER.TEST_ROSTER_ID;
      
        /** Deleting alL the enrties for the roster **/
        DELETE FROM STUDENT_ITEM_SET_STATUS
         WHERE TEST_ROSTER_ID = REC_ROSTER.TEST_ROSTER_ID;
      
      END LOOP;
      ------- LOOP 1.0.1 END ------
    
      /* Inserting new entries in siss table for the roster with TDs of new form */
      ------- LOOP 1.0.2 START ------
      IF (V_IS_CURSOR_RETURNS_VALUE) THEN
        FOR REC_TS_VALUES IN CUR_GET_ALL_TS_FOR_ROSTER(RECADMIN.TEST_ADMIN_ID) LOOP
          ------- LOOP 1.0.2.1 START ------
          FOR REC_TD_VALUES IN CUR_GET_ALL_TD_FOR_FORM(REC_TS_VALUES.ITEM_SET_ID,
                                                       V_FORM_GA_EOG_3T) LOOP
          
            EXECUTE IMMEDIATE 'INSERT INTO STUDENT_ITEM_SET_STATUS
                (TEST_ROSTER_ID,
                 ITEM_SET_ID,
                 COMPLETION_STATUS,
                 VALIDATION_STATUS,
                 TIME_EXPIRED,
                 ITEM_SET_ORDER,
                 CUSTOMER_FLAG_STATUS)
                (SELECT TEST_ROSTER_ID,' ||
                              REC_TD_VALUES.ITEM_SET_ID || ',' || '''' ||
                              V_SCHEDULED_STATUS || '''' || ',' || '''' ||
                              V_VALIDATION_STATUS || '''' || ',' || '''' ||
                              V_TEST_TIME_EXPIRED || '''' || ',' ||
                              V_ITEM_SET_ORDER || ',' ||
                              'DEFAULT_VALUE
                      FROM (SELECT DISTINCT TR.TEST_ROSTER_ID, CC.DEFAULT_VALUE 
                            FROM TEST_ROSTER TR, 
                            TEST_ADMIN TA, 
                            STUDENT_ACCOMMODATION STDACCO,
                            CUSTOMER_CONFIGURATION CC
                            WHERE TA.TEST_ADMIN_ID = ' ||
                              RECADMIN.TEST_ADMIN_ID || '
                            AND TR.TEST_ADMIN_ID = TA.TEST_ADMIN_ID
                            AND TR.CUSTOMER_ID = ' ||
                              VCUSTOMERID || '
                            AND TR.ACTIVATION_STATUS = ' || '''' ||
                              V_ACTIVATION_STATUS || '''' || '
                            AND TR.TEST_COMPLETION_STATUS = ' || '''' ||
                              V_SCHEDULED_STATUS || '''' || '
                            AND TR.STUDENT_ID = STDACCO.STUDENT_ID
                            AND STDACCO.SCREEN_READER = ' || '''' ||
                              V_SCREEN_READER_VALID || '''' || '
                            AND CC.CUSTOMER_ID = TR.CUSTOMER_ID 
                            AND CC.CUSTOMER_CONFIGURATION_NAME = ' || '''' ||
                              VROSTER_STATUS_FLAG || '''' || '
                            AND NOT EXISTS
                            ( SELECT 1
                                FROM STUDENT_ITEM_SET_STATUS SISS
                               WHERE SISS.TEST_ROSTER_ID = TR.TEST_ROSTER_ID
                               AND SISS.ITEM_SET_ID = ' ||
                              REC_TD_VALUES.ITEM_SET_ID || ')))';
          
            V_ITEM_SET_ORDER := V_ITEM_SET_ORDER + 1;
          END LOOP;
          ------- LOOP 1.0.2.1 END ------
        END LOOP;
        ------- LOOP 1.0.2 END ------
      END IF;
      COMMIT;
    END LOOP;
    -------- LOOP 1.0 END -------
  
    /*IF (NOT V_IS_CURSOR_RETURNS_VALUE) THEN
      RAISE_APPLICATION_ERROR(-20000,
                              'No scheduled assessment is found for Customer id [' ||
                              VCUSTOMERID || '].');
    END IF;*/
    DBMS_OUTPUT.PUT_LINE('Process COMPLETED Successfully');
  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK; -- ROLLBACK ALL DATA MODIFIED EXPLICITELY
      RAISE;
    
  END UPDATE_SR_STUDENTS_FORM;

END OAS_GA_UPDATE_SR_STUDENTS_2015;
/
