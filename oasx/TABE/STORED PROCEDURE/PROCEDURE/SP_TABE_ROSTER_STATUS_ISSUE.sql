CREATE OR REPLACE PROCEDURE SP_TABE_ROSTER_STATUS_ISSUE IS

  /*
  * This cursor fetches the list of those rosters which are assigned with TABE tests and have either 
  * login or completed any subtest during the last 30 minutes of the job execution.
  * OAS - 1942
  */
  CURSOR CUR_ROSTER_LIST IS
    SELECT TR.TEST_ROSTER_ID         AS TEST_ROSTER_ID,
           TR.TEST_COMPLETION_STATUS AS TEST_COMPLETION_STATUS,
           TR.CUSTOMER_ID            AS CUSTOMER_ID,
           TR.STUDENT_ID             AS STUDENT_ID,
           TR.TEST_ADMIN_ID          AS TEST_ADMIN_ID,
           TR.START_DATE_TIME        AS START_DATE_TIME,
           TR.COMPLETION_DATE_TIME   AS COMPLETION_DATE_TIME,
           PR.PRODUCT_NAME           AS PRODUCT_NAME
      FROM TEST_ADMIN TA, TEST_ROSTER TR, PRODUCT PR, TEST_CATALOG TC
     WHERE TR.TEST_ADMIN_ID = TA.TEST_ADMIN_ID
       AND TA.PRODUCT_ID = PR.PRODUCT_ID
       AND TA.TEST_CATALOG_ID = TC.TEST_CATALOG_ID
       AND TC.PRODUCT_ID = PR.PRODUCT_ID
       AND (TR.START_DATE_TIME > (SYSDATE - 30 / 1440) OR
           TR.COMPLETION_DATE_TIME > (SYSDATE - 30 / 1440) OR EXISTS
            (SELECT 1
               FROM STUDENT_ITEM_SET_STATUS SISS
              WHERE SISS.TEST_ROSTER_ID = TR.TEST_ROSTER_ID
                AND (SISS.START_DATE_TIME > (SYSDATE - 30 / 1440) OR
                    SISS.COMPLETION_DATE_TIME > (SYSDATE - 30 / 1440))))
       AND TRUNC(TA.LOGIN_START_DATE) <= TRUNC(SYSDATE)
       AND TRUNC(TA.LOGIN_END_DATE) >= TRUNC(SYSDATE)
       AND PR.PARENT_PRODUCT_ID = 4000
       AND PR.PRODUCT_TYPE = 'TB'
       AND TA.TEST_ADMIN_STATUS = 'CU'
       AND TA.ACTIVATION_STATUS = 'AC'
       AND TR.ACTIVATION_STATUS = 'AC'
       AND PR.ACTIVATION_STATUS = 'AC';

  V_RESPONSE_PRESENT INTEGER := 0;
  V_TD_COUNT         INTEGER := 0;
  V_ERROR_MESSAGE    VARCHAR2(800) := NULL;

BEGIN

  FOR CUR_DATA IN CUR_ROSTER_LIST LOOP
    -- Reinitialize the temp variables
    V_RESPONSE_PRESENT := 0;
    V_TD_COUNT         := 0;
    V_ERROR_MESSAGE    := NULL;
  
    SELECT COUNT(1)
      INTO V_RESPONSE_PRESENT
      FROM ITEM_RESPONSE
     WHERE TEST_ROSTER_ID = CUR_DATA.TEST_ROSTER_ID;
    /*
    * Log error if Roster is in 'SC' status but response is present in DB for the said roster
    */
    IF V_RESPONSE_PRESENT <> 0 AND (CUR_DATA.TEST_COMPLETION_STATUS = 'SC' OR
       CUR_DATA.START_DATE_TIME IS NULL) THEN
      V_ERROR_MESSAGE := 'The roster status should not be SC,Start_date_time should be populated as response is present in DB for the said roster';
    END IF;
  
    IF V_ERROR_MESSAGE IS NOT NULL THEN
      SELECT COUNT(1)
        INTO V_TD_COUNT
        FROM STUDENT_ITEM_SET_STATUS SISS
       WHERE SISS.TEST_ROSTER_ID = CUR_DATA.TEST_ROSTER_ID
         AND SISS.COMPLETION_STATUS <> 'CO';
    
      IF V_TD_COUNT = 0 AND CUR_DATA.TEST_COMPLETION_STATUS <> 'CO' THEN
        /*
        * Log error if all TDs have been 'CO' but roster is still other than 'CO'
        */
        V_ERROR_MESSAGE := 'The roster status should be CO as all TDs are completed';
      ELSIF V_TD_COUNT <> 0 AND CUR_DATA.TEST_COMPLETION_STATUS = 'CO' THEN
        /*
        * Log error if all TDs have not been 'CO' but roster has been 'CO'
        */
        V_ERROR_MESSAGE := 'The roster status should NOT be CO as all TDs are NOT completed';
      END IF;
    
    END IF;
  
    IF V_ERROR_MESSAGE IS NOT NULL THEN
      IF CUR_DATA.TEST_COMPLETION_STATUS = 'CO' AND
         CUR_DATA.COMPLETION_DATE_TIME IS NULL THEN
        /*
        * Log error if all Roster is Completed but Completion_Date_Time is not populated.
        */
        V_ERROR_MESSAGE := 'The roster completion_time should be populated as roster is CO';
      END IF;
    END IF;
  
    /*
    * INSERT ERROR DATA IF ONLY ERROR HAS OCCURED.
    */
    IF V_ERROR_MESSAGE IS NOT NULL THEN
      INSERT INTO TABE_ERROR_MONITOR
        (PRODUCT_NAME,
         STUDENT_ID,
         TEST_ADMIN_ID,
         TEST_ROSTER_ID,
         ROSTER_STATUS,
         ERROR_TYPE_FLAG,
         ERROR_STATUS,
         EMAIL_FLAG,
         CREATED_DATETIME,
         COMMENTS,
         CUSTOMER_ID)
      VALUES
        (CUR_DATA.PRODUCT_NAME,
         CUR_DATA.STUDENT_ID,
         CUR_DATA.TEST_ADMIN_ID,
         CUR_DATA.TEST_ROSTER_ID,
         CUR_DATA.TEST_COMPLETION_STATUS,
         'ROSTER STATUS',
         'ERROR',
         'N',
         SYSDATE,
         V_ERROR_MESSAGE,
         CUR_DATA.CUSTOMER_ID);
    END IF;
  END LOOP;

  COMMIT; -- commit point

EXCEPTION
  WHEN OTHERS THEN
    ROLLBACK; -- rollback point
    RAISE;
  
END;
/
