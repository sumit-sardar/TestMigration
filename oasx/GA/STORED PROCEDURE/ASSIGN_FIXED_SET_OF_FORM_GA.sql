CREATE OR REPLACE PROCEDURE ASSIGN_FIXED_SET_OF_FORM_GA AS

  V_ROSTER_COUNT            INTEGER := 0;
  V_FORCE_ROSTER_COUNT      INTEGER := 0;
  V_ITEM_SET_ORDER          INTEGER := 0;
  V_MAX_INT_RANGE           INTEGER := 32767;
  V_FORM                    VARCHAR2(5);
  V_A3_FORM_NAME            VARCHAR2(5) := 'A3';
  V_B3_FORM_NAME            VARCHAR2(5) := 'B3';
  V_1T_FORM_NAME            VARCHAR2(5) := '1T';
  V_2T_FORM_NAME            VARCHAR2(5) := '2T';
  V_3T_FORM_NAME            VARCHAR2(5) := '3T';
  V_4T_FORM_NAME            VARCHAR2(5) := '4T';
  V_5T_FORM_NAME            VARCHAR2(5) := '5T';
  V_C3_FORM_NAME            VARCHAR2(5) := 'C3';
  V_D3_FORM_NAME            VARCHAR2(5) := 'D3';
  V_GA_PRODUCT_ID           VARCHAR2(4) := '32';
  V_GA_MID_MONTH_PRODUCT_ID VARCHAR2(4) := '35';
  V_GA_EOG_2015_PRODUCT_ID  VARCHAR2(4) := '37';
  V_GA_EOC_2015_PRODUCT_ID  VARCHAR2(4) := '39';
  V_MAX_ATTEMPT_COUNTER     INTEGER := 5;
  V_FORM_VALUE              VARCHAR2(10) := '';
  V_COUNT                   INTEGER := 0;

  /**Get all student which needs to be processed**/
  CURSOR CUR_ALL_STUDENT_DETAILS IS
    SELECT RFRI.STUDENT_ID, ATTEMPTED_COUNTER
      FROM ROSTER_FORM_REASSIGN_INPUT RFRI
     WHERE JOB_STATUS = 'Prepared'
       AND RFRI.ATTEMPTED_COUNTER < V_MAX_ATTEMPT_COUNTER
     ORDER BY STUDENT_ID;

  /**Get all student which needs to be processed**/
  CURSOR CUR_GET_GA_STUDENT_DETAILS(IN_STUDENT_ID INTEGER) IS
    SELECT STU.STUDENT_ID
      FROM STUDENT STU, CUSTOMER_CONFIGURATION CC
     WHERE STU.STUDENT_ID = IN_STUDENT_ID
       AND STU.CUSTOMER_ID = CC.CUSTOMER_ID
       AND CC.CUSTOMER_CONFIGURATION_NAME = 'GA_Customer'
       and CC.DEFAULT_VALUE = 'T'
     ORDER BY STUDENT_ID;

  /**Get all student which needs to be processed for force form values**/
  CURSOR CUR_GET_FORCE_STUDENT_DETAILS(IN_STUDENT_ID INTEGER) IS
    SELECT STU.STUDENT_ID
      FROM STUDENT STU, CUSTOMER_CONFIGURATION CC
     WHERE STU.STUDENT_ID = IN_STUDENT_ID
       AND STU.CUSTOMER_ID = CC.CUSTOMER_ID
       AND CC.CUSTOMER_CONFIGURATION_NAME = 'Force_Form_Values'
       and CC.DEFAULT_VALUE = 'T'
     ORDER BY STUDENT_ID;

  /*
  FETCH ONLY THOSE ROSTER_IDS OF THE STUDENT WHOSE PRODUCT_ID AND TEST_CATALOG_ID HAS AN ENTRY IN
  FORM_ASSIGNMENT_LOOKUP TABLE AND FORM_ASSIGNMENT_METHOD IS ROUND_ROBIN
  */

  CURSOR CUR_TEST_ROSTER_LOOKUP(V_STUDENT_ID TEST_ROSTER.STUDENT_ID%TYPE) IS
    SELECT TR.TEST_ROSTER_ID,
           TR.FORM_ASSIGNMENT,
           TA.TEST_ADMIN_ID as TEST_ADMIN_ID,
           FAL.PRODUCT_ID,
           FAL.TEST_CATALOG_ID,
           FAL.ACTIVATION_STATUS
      FROM TEST_ROSTER TR, TEST_ADMIN TA, FORM_ASSIGNMENT_LOOKUP FAL
     WHERE TA.PRODUCT_ID = FAL.PRODUCT_ID
       AND TA.TEST_CATALOG_ID = FAL.TEST_CATALOG_ID
       AND TR.TEST_COMPLETION_STATUS IN ('SC', 'NT')
       AND TR.TEST_ADMIN_ID = TA.TEST_ADMIN_ID
       AND TR.STUDENT_ID = V_STUDENT_ID
       AND TR.ACTIVATION_STATUS = 'AC'
       AND FAL.ACTIVATION_STATUS = 'AC'
       AND TA.FORM_ASSIGNMENT_METHOD = 'roundrobin'
       AND NOT EXISTS
     (SELECT 1
              FROM ITEM_RESPONSE IR
             WHERE IR.TEST_ROSTER_ID = TR.TEST_ROSTER_ID);

  /*
  SELECT THE SCREEN_READER_FORMS AVAILABLE FOR THE SPECIFIC PRODUCT_ID AND TEST_CATALOG_ID AND
  ALSO THE SAME FORMS SHOULD BE PRESENT FOR THE SPECIFIC ITEM_SET_ID.
  */

  CURSOR CUR_SCREEN_READER_FORMS(IN_TEST_ADMIN_ID NUMBER, IN_PRODUCT_ID NUMBER, IN_TEST_CATALOG_ID NUMBER) IS
    SELECT TR.FORM_ASSIGNMENT AS AVAILABLE_FORMS,
           COUNT(TR.FORM_ASSIGNMENT) AS COUNT_FORMS
      FROM TEST_ROSTER TR
     WHERE TR.TEST_ADMIN_ID = IN_TEST_ADMIN_ID
       AND TR.FORM_ASSIGNMENT IN
           (SELECT *
              FROM (SELECT DISTINCT TRIM(SUBSTR(',' || SCREEN_READER_FORMS || ',',
                                                INSTR(',' ||
                                                      SCREEN_READER_FORMS || ',',
                                                      ',',
                                                      1,
                                                      LEVEL) + 1,
                                                INSTR(',' ||
                                                      SCREEN_READER_FORMS || ',',
                                                      ',',
                                                      1,
                                                      LEVEL + 1) -
                                                INSTR(',' ||
                                                      SCREEN_READER_FORMS || ',',
                                                      ',',
                                                      1,
                                                      LEVEL) - 1)) AS SCREEN_READER_FORMS
                      FROM FORM_ASSIGNMENT_LOOKUP
                     WHERE PRODUCT_ID = IN_PRODUCT_ID
                       AND TEST_CATALOG_ID = IN_TEST_CATALOG_ID
                       AND ACTIVATION_STATUS = 'AC'
                    CONNECT BY LEVEL <=
                               LENGTH(',' || SCREEN_READER_FORMS || ',') -
                               LENGTH(REPLACE(',' || SCREEN_READER_FORMS || ',',
                                              ',',
                                              '')) - 1)
             WHERE SCREEN_READER_FORMS IN
                   (SELECT ISET.ITEM_SET_FORM AS FORM
                      FROM TEST_ADMIN ADM,
                           ITEM_SET ISET,
                           ITEM_SET_ANCESTOR ISA,
                           FORM_ASSIGNMENT_LOOKUP FAL,
                           (SELECT DISTINCT TEST_ROSTER_ID, FORM_ASSIGNMENT
                              FROM TEST_ROSTER
                             WHERE TEST_ADMIN_ID = IN_TEST_ADMIN_ID) ROS
                     WHERE ISA.ANCESTOR_ITEM_SET_ID = ADM.ITEM_SET_ID
                       AND ISET.ITEM_SET_ID = ISA.ITEM_SET_ID
                       AND ROS.FORM_ASSIGNMENT(+) = ISET.ITEM_SET_FORM
                       AND ISET.ITEM_SET_TYPE = 'TD'
                       AND ADM.TEST_ADMIN_ID = IN_TEST_ADMIN_ID
                     GROUP BY ISET.ITEM_SET_FORM))
     GROUP BY TR.FORM_ASSIGNMENT
     ORDER BY COUNT_FORMS;

  /**Get all roster details for a student**
  * Extra check is added here to be sure even if TEST_COMPLETION_STATUS in
  * Test Roster table is having problem due to any TMS issue, we should not
  * alter subtest form for those rosters for which response is present in DB
  **/
  CURSOR CUR_GET_TEST_ROSTER_FORM(V_STUDENT_ID TEST_ROSTER.STUDENT_ID%TYPE) IS
    SELECT TR.TEST_ROSTER_ID,
           TA.TEST_ADMIN_ID,
           TR.FORM_ASSIGNMENT,
           TA.PRODUCT_ID
      FROM TEST_ROSTER TR, TEST_ADMIN TA
     WHERE TA.PRODUCT_ID IN
           (V_GA_PRODUCT_ID, V_GA_MID_MONTH_PRODUCT_ID,
            V_GA_EOG_2015_PRODUCT_ID, V_GA_EOC_2015_PRODUCT_ID)
       AND TR.TEST_COMPLETION_STATUS IN ('SC', 'NT')
       AND TR.TEST_ADMIN_ID = TA.TEST_ADMIN_ID
       AND TR.STUDENT_ID = V_STUDENT_ID
       AND TR.ACTIVATION_STATUS = 'AC'
       AND NOT EXISTS
     (SELECT 1
              FROM ITEM_RESPONSE IR
             WHERE IR.TEST_ROSTER_ID = TR.TEST_ROSTER_ID)
     ORDER BY TR.TEST_ROSTER_ID DESC;

  /**Get all TS for a roster**/
  CURSOR CUR_GET_ALL_TS_FOR_ROSTER(V_TEST_ROSTER_ID TEST_ROSTER.TEST_ROSTER_ID%TYPE) IS
    SELECT ISET.ITEM_SET_ID
      FROM ITEM_SET        ISET,
           ITEM_SET_PARENT ISP,
           TEST_ADMIN      TA,
           TEST_ROSTER     TR
     WHERE ISET.ACTIVATION_STATUS = 'AC'
       AND ISET.ITEM_SET_ID = ISP.ITEM_SET_ID
       AND ISET.ITEM_SET_TYPE = 'TS'
       AND ISP.PARENT_ITEM_SET_ID = TA.ITEM_SET_ID /*-- TC ItemsetId*/
       AND TA.TEST_ADMIN_ID = TR.TEST_ADMIN_ID
       AND TR.TEST_ROSTER_ID = V_TEST_ROSTER_ID
     ORDER BY ISP.ITEM_SET_SORT_ORDER;

  /**Get all TD for a form**/
  CURSOR CUR_GET_ALL_TD_FOR_FORM(V_ITEM_SET_ID_TS ITEM_SET.ITEM_SET_ID%TYPE, V_FORM ITEM_SET.ITEM_SET_FORM%TYPE) IS
    SELECT ISET.ITEM_SET_ID
      FROM ITEM_SET ISET, ITEM_SET_PARENT ISP
     WHERE ISET.ITEM_SET_ID = ISP.ITEM_SET_ID
       AND ISP.PARENT_ITEM_SET_ID = V_ITEM_SET_ID_TS
       AND ISET.ITEM_SET_FORM = V_FORM
       AND ISET.ACTIVATION_STATUS = 'AC'
     ORDER BY ISET.ITEM_SET_FORM, ISP.ITEM_SET_SORT_ORDER;

  /**Get all form assignment count except B3 for GA Midmonth 2014**/
  CURSOR CUR_MID_MONTH_FORM_ASSIGNMENT(V_TEST_ADMIN_ID TEST_ADMIN.TEST_ADMIN_ID%TYPE) IS
    SELECT ISET.ITEM_SET_FORM AS FORM,
           COUNT(DISTINCT ROS.TEST_ROSTER_ID) AS COUNT
      FROM TEST_ADMIN ADM,
           ITEM_SET ISET,
           ITEM_SET_ANCESTOR ISA,
           (SELECT DISTINCT TEST_ROSTER_ID, FORM_ASSIGNMENT
              FROM TEST_ROSTER
             WHERE TEST_ADMIN_ID = V_TEST_ADMIN_ID) ROS
     WHERE ISA.ANCESTOR_ITEM_SET_ID = ADM.ITEM_SET_ID
       AND ISET.ITEM_SET_ID = ISA.ITEM_SET_ID
       AND ROS.FORM_ASSIGNMENT(+) = ISET.ITEM_SET_FORM
       AND ISET.ITEM_SET_FORM NOT IN (V_B3_FORM_NAME)
       AND ISET.ITEM_SET_TYPE = 'TD'
       AND ADM.TEST_ADMIN_ID = V_TEST_ADMIN_ID
     GROUP BY ISET.ITEM_SET_FORM;

  /**Get all form assignment count except A3 and B3**/
  CURSOR CUR_GET_FORM_ASSIGNMENT_COUNT(V_TEST_ADMIN_ID TEST_ADMIN.TEST_ADMIN_ID%TYPE) IS
    SELECT ISET.ITEM_SET_FORM AS FORM,
           COUNT(DISTINCT ROS.TEST_ROSTER_ID) AS COUNT
      FROM TEST_ADMIN ADM,
           ITEM_SET ISET,
           ITEM_SET_ANCESTOR ISA,
           (SELECT DISTINCT TEST_ROSTER_ID, FORM_ASSIGNMENT
              FROM TEST_ROSTER
             WHERE TEST_ADMIN_ID = V_TEST_ADMIN_ID) ROS
     WHERE ISA.ANCESTOR_ITEM_SET_ID = ADM.ITEM_SET_ID
       AND ISET.ITEM_SET_ID = ISA.ITEM_SET_ID
       AND ROS.FORM_ASSIGNMENT(+) = ISET.ITEM_SET_FORM
       AND ISET.ITEM_SET_FORM NOT IN (V_A3_FORM_NAME, V_B3_FORM_NAME)
       AND ISET.ITEM_SET_TYPE = 'TD'
       AND ADM.TEST_ADMIN_ID = V_TEST_ADMIN_ID
     GROUP BY ISET.ITEM_SET_FORM;

  /**Get all form assignment count except C3 and D3**/
  CURSOR CUR_EOC_2015_FORM_ASSIGNMENT(V_TEST_ADMIN_ID TEST_ADMIN.TEST_ADMIN_ID%TYPE) IS
    SELECT ISET.ITEM_SET_FORM AS FORM,
           COUNT(DISTINCT ROS.TEST_ROSTER_ID) AS COUNT
      FROM TEST_ADMIN ADM,
           ITEM_SET ISET,
           ITEM_SET_ANCESTOR ISA,
           (SELECT DISTINCT TEST_ROSTER_ID, FORM_ASSIGNMENT
              FROM TEST_ROSTER
             WHERE TEST_ADMIN_ID = V_TEST_ADMIN_ID) ROS
     WHERE ISA.ANCESTOR_ITEM_SET_ID = ADM.ITEM_SET_ID
       AND ISET.ITEM_SET_ID = ISA.ITEM_SET_ID
       AND ROS.FORM_ASSIGNMENT(+) = ISET.ITEM_SET_FORM
       AND ISET.ITEM_SET_FORM NOT IN (V_C3_FORM_NAME, V_D3_FORM_NAME)
       AND ISET.ITEM_SET_TYPE = 'TD'
       AND ADM.TEST_ADMIN_ID = V_TEST_ADMIN_ID
     GROUP BY ISET.ITEM_SET_FORM;

BEGIN

  /** 
  * Fetching student details in prepared state.
  **/
  FOR STU_DETAILS IN CUR_ALL_STUDENT_DETAILS LOOP
  
    /***
    ** This process is for GA customers where form value was mentioned hard-coded for SR forms
    ***/
    FOR COL IN CUR_GET_GA_STUDENT_DETAILS(STU_DETAILS.STUDENT_ID) LOOP
      BEGIN
        V_ROSTER_COUNT := 0;
      
        FOR REC_ROSTER_VAL IN CUR_GET_TEST_ROSTER_FORM(COL.STUDENT_ID) LOOP
        
          V_ITEM_SET_ORDER := 0;
          V_MAX_INT_RANGE  := 32767;
        
          IF (REC_ROSTER_VAL.PRODUCT_ID = V_GA_PRODUCT_ID AND
             (REC_ROSTER_VAL.FORM_ASSIGNMENT = V_A3_FORM_NAME OR
             REC_ROSTER_VAL.FORM_ASSIGNMENT = V_B3_FORM_NAME)) OR
             (REC_ROSTER_VAL.PRODUCT_ID = V_GA_MID_MONTH_PRODUCT_ID AND
             REC_ROSTER_VAL.FORM_ASSIGNMENT = V_B3_FORM_NAME) OR
             (REC_ROSTER_VAL.PRODUCT_ID = V_GA_EOG_2015_PRODUCT_ID AND
             (REC_ROSTER_VAL.FORM_ASSIGNMENT = V_1T_FORM_NAME OR
             REC_ROSTER_VAL.FORM_ASSIGNMENT = V_2T_FORM_NAME OR
             REC_ROSTER_VAL.FORM_ASSIGNMENT = V_4T_FORM_NAME OR
             REC_ROSTER_VAL.FORM_ASSIGNMENT = V_5T_FORM_NAME)) OR
             (REC_ROSTER_VAL.PRODUCT_ID = V_GA_EOC_2015_PRODUCT_ID AND
             (REC_ROSTER_VAL.FORM_ASSIGNMENT = V_C3_FORM_NAME OR
             REC_ROSTER_VAL.FORM_ASSIGNMENT = V_D3_FORM_NAME)) THEN
          
            V_ROSTER_COUNT := V_ROSTER_COUNT + 1;
          
            IF REC_ROSTER_VAL.PRODUCT_ID = V_GA_MID_MONTH_PRODUCT_ID THEN
              FOR REC_FORM_VALUES IN CUR_MID_MONTH_FORM_ASSIGNMENT(REC_ROSTER_VAL.TEST_ADMIN_ID) LOOP
                IF REC_FORM_VALUES.COUNT < V_MAX_INT_RANGE THEN
                  V_FORM          := REC_FORM_VALUES.FORM;
                  V_MAX_INT_RANGE := REC_FORM_VALUES.COUNT;
                END IF;
              END LOOP;
              /** New Else If block added for Story OAS-1888 :
              GA Spring '15 - EOG - Test Spiraling : Auto Assignment of 3T*/
            ELSIF REC_ROSTER_VAL.PRODUCT_ID = V_GA_EOG_2015_PRODUCT_ID THEN
              V_FORM := V_3T_FORM_NAME;
            
              /** New Else If block added for Story OAS-1875 :
              GA Spring '15 - EOC - Test Spiraling: Auto-Assignment of Forms*/
            ELSIF REC_ROSTER_VAL.PRODUCT_ID = V_GA_EOC_2015_PRODUCT_ID THEN
              FOR REC_FORM_VALUES IN CUR_EOC_2015_FORM_ASSIGNMENT(REC_ROSTER_VAL.TEST_ADMIN_ID) LOOP
                IF REC_FORM_VALUES.COUNT < V_MAX_INT_RANGE THEN
                  V_FORM          := REC_FORM_VALUES.FORM;
                  V_MAX_INT_RANGE := REC_FORM_VALUES.COUNT;
                END IF;
              END LOOP;
            ELSE
              FOR REC_FORM_VALUES IN CUR_GET_FORM_ASSIGNMENT_COUNT(REC_ROSTER_VAL.TEST_ADMIN_ID) LOOP
                IF REC_FORM_VALUES.COUNT < V_MAX_INT_RANGE THEN
                  V_FORM          := REC_FORM_VALUES.FORM;
                  V_MAX_INT_RANGE := REC_FORM_VALUES.COUNT;
                END IF;
              END LOOP;
            END IF;
          
            /** Updating form value for the roster **/
            UPDATE TEST_ROSTER TR
               SET TR.FORM_ASSIGNMENT = V_FORM
             WHERE TR.TEST_ROSTER_ID = REC_ROSTER_VAL.TEST_ROSTER_ID;
          
            /** Deleting alL the enrties for the roster **/
            DELETE FROM STUDENT_ITEM_SET_STATUS
             WHERE TEST_ROSTER_ID = REC_ROSTER_VAL.TEST_ROSTER_ID;
          
            /* Inserting new entries in siss table for the roster with TDs of new form */
            FOR REC_TS_VALUES IN CUR_GET_ALL_TS_FOR_ROSTER(REC_ROSTER_VAL.TEST_ROSTER_ID) LOOP
              FOR REC_TD_VALUES IN CUR_GET_ALL_TD_FOR_FORM(REC_TS_VALUES.ITEM_SET_ID,
                                                           V_FORM) LOOP
                INSERT INTO STUDENT_ITEM_SET_STATUS
                  (TEST_ROSTER_ID,
                   ITEM_SET_ID,
                   COMPLETION_STATUS,
                   START_DATE_TIME,
                   COMPLETION_DATE_TIME,
                   VALIDATION_STATUS,
                   VALIDATION_UPDATED_BY,
                   VALIDATION_UPDATED_DATE_TIME,
                   VALIDATION_UPDATED_NOTE,
                   TIME_EXPIRED,
                   ITEM_SET_ORDER,
                   RAW_SCORE,
                   MAX_SCORE,
                   UNSCORED,
                   RECOMMENDED_LEVEL,
                   SCRATCHPAD_CONTENT,
                   CUSTOMER_FLAG_STATUS,
                   EXEMPTIONS,
                   ABSENT,
                   ABILITY_SCORE,
                   SEM_SCORE,
                   OBJECTIVE_SCORE,
                   TMS_UPDATE,
                   INVALIDATION_REASON_ID)
                VALUES
                  (REC_ROSTER_VAL.TEST_ROSTER_ID,
                   REC_TD_VALUES.ITEM_SET_ID,
                   'SC',
                   NULL,
                   NULL,
                   'VA',
                   1,
                   NULL,
                   NULL,
                   'F',
                   V_ITEM_SET_ORDER,
                   NULL,
                   NULL,
                   NULL,
                   NULL,
                   NULL,
                   '',
                   'N',
                   'N',
                   NULL,
                   NULL,
                   NULL,
                   NULL,
                   NULL);
              
                V_ITEM_SET_ORDER := V_ITEM_SET_ORDER + 1;
              END LOOP;
            END LOOP;
          END IF;
          COMMIT; -- COMMITTING FOR EACH ROSTER     
        END LOOP;
      EXCEPTION
        WHEN OTHERS THEN
          ROLLBACK;
          UPDATE ROSTER_FORM_REASSIGN_INPUT
             SET UPDATED_DATE_TIME    = SYSDATE,
                 UPDATED_ROSTER_COUNT = V_ROSTER_COUNT,
                 REASON               = 'Exception Occurred.',
                 ATTEMPTED_COUNTER    = ATTEMPTED_COUNTER + 1
           WHERE STUDENT_ID = COL.STUDENT_ID
             AND JOB_STATUS = 'Prepared';
      END;
    END LOOP; -- LOOP END FOR GA LOGIC.
  
    -- Get all student details FOR FORCE FORM VALUES 
    FOR COL IN CUR_GET_FORCE_STUDENT_DETAILS(STU_DETAILS.STUDENT_ID) LOOP
      BEGIN
        V_FORCE_ROSTER_COUNT := 0;
        -- Get all roster associated with 
        FOR ROSTER IN CUR_TEST_ROSTER_LOOKUP(COL.STUDENT_ID) LOOP
          V_FORM_VALUE     := '';
          V_COUNT          := 0;
          V_ITEM_SET_ORDER := 0;
          FOR SCREEN_FORM IN CUR_SCREEN_READER_FORMS(ROSTER.TEST_ADMIN_ID,
                                                     ROSTER.PRODUCT_ID,
                                                     ROSTER.TEST_CATALOG_ID) LOOP
            --Assign form value of first form only.
            IF V_COUNT = 0 THEN
              V_COUNT      := V_COUNT + 1;
              V_FORM_VALUE := SCREEN_FORM.AVAILABLE_FORMS;
            END IF;
          
            /*
            * If available form is already matching with existing form assignment of roster
            * then do not change form value
            */
            IF SCREEN_FORM.AVAILABLE_FORMS = ROSTER.FORM_ASSIGNMENT THEN
              V_FORM_VALUE := NULL;
            END IF;
          
          END LOOP;
        
          /*
          * If form value is set.Then change the form assignment of test roster
          */
          IF V_FORM_VALUE IS NOT NULL THEN
          
            -- Track roster count for which form values are changed.
            V_FORCE_ROSTER_COUNT := V_FORCE_ROSTER_COUNT + 1;
          
            /** Updating form value for the roster **/
            UPDATE TEST_ROSTER TR
               SET TR.FORM_ASSIGNMENT = V_FORM_VALUE
             WHERE TR.TEST_ROSTER_ID = ROSTER.TEST_ROSTER_ID;
          
            /** Deleting alL the enrties for the roster **/
            DELETE FROM STUDENT_ITEM_SET_STATUS
             WHERE TEST_ROSTER_ID = ROSTER.TEST_ROSTER_ID;
          
            /* Inserting new entries in siss table for the roster with TDs of new form */
            FOR REC_TS_VALUES IN CUR_GET_ALL_TS_FOR_ROSTER(ROSTER.TEST_ROSTER_ID) LOOP
            
              -- Get all TDs for the Form value selected.
              FOR REC_TD_VALUES IN CUR_GET_ALL_TD_FOR_FORM(REC_TS_VALUES.ITEM_SET_ID,
                                                           V_FORM_VALUE) LOOP
                INSERT INTO STUDENT_ITEM_SET_STATUS
                  (TEST_ROSTER_ID,
                   ITEM_SET_ID,
                   COMPLETION_STATUS,
                   START_DATE_TIME,
                   COMPLETION_DATE_TIME,
                   VALIDATION_STATUS,
                   VALIDATION_UPDATED_BY,
                   VALIDATION_UPDATED_DATE_TIME,
                   VALIDATION_UPDATED_NOTE,
                   TIME_EXPIRED,
                   ITEM_SET_ORDER,
                   RAW_SCORE,
                   MAX_SCORE,
                   UNSCORED,
                   RECOMMENDED_LEVEL,
                   SCRATCHPAD_CONTENT,
                   CUSTOMER_FLAG_STATUS,
                   EXEMPTIONS,
                   ABSENT,
                   ABILITY_SCORE,
                   SEM_SCORE,
                   OBJECTIVE_SCORE,
                   TMS_UPDATE,
                   INVALIDATION_REASON_ID)
                VALUES
                  (ROSTER.TEST_ROSTER_ID,
                   REC_TD_VALUES.ITEM_SET_ID,
                   'SC',
                   NULL,
                   NULL,
                   'VA',
                   1,
                   NULL,
                   NULL,
                   'F',
                   V_ITEM_SET_ORDER,
                   NULL,
                   NULL,
                   NULL,
                   NULL,
                   NULL,
                   '',
                   'N',
                   'N',
                   NULL,
                   NULL,
                   NULL,
                   NULL,
                   NULL);
              
                V_ITEM_SET_ORDER := V_ITEM_SET_ORDER + 1;
              END LOOP; -- Td loop
            END LOOP; -- Ts loop
          END IF; -- Form not null block
        
          COMMIT; -- COMMIT FOR EACH ROSTER
        END LOOP; -- each roster loop
      EXCEPTION
        WHEN OTHERS THEN
          ROLLBACK;
          UPDATE ROSTER_FORM_REASSIGN_INPUT
             SET UPDATED_DATE_TIME    = SYSDATE,
                 UPDATED_ROSTER_COUNT = V_ROSTER_COUNT,
                 REASON               = 'Exception Occurred.',
                 ATTEMPTED_COUNTER    = ATTEMPTED_COUNTER + 1
           WHERE STUDENT_ID = COL.STUDENT_ID
             AND JOB_STATUS = 'Prepared';
      END;
    END LOOP; -- LOOP END FOR FORCE FORM VALUES LOGIC.
  
    IF V_ROSTER_COUNT > 0 OR V_FORCE_ROSTER_COUNT > 0 THEN
      /* Roster and SISS records are altered */
      UPDATE ROSTER_FORM_REASSIGN_INPUT
         SET UPDATED_DATE_TIME    = SYSDATE,
             UPDATED_ROSTER_COUNT = V_ROSTER_COUNT,
             JOB_STATUS           = 'Executed',
             REASON               = 'Re-assign form of all rosters those are in SC/NT and have non screen reader Test_forms',
             ATTEMPTED_COUNTER    = ATTEMPTED_COUNTER + 1
       WHERE STUDENT_ID = STU_DETAILS.STUDENT_ID
         AND JOB_STATUS = 'Prepared';
    ELSE
      /* Roster and SISS records remained unchanged */
      UPDATE ROSTER_FORM_REASSIGN_INPUT
         SET UPDATED_DATE_TIME    = SYSDATE,
             UPDATED_ROSTER_COUNT = V_ROSTER_COUNT,
             JOB_STATUS           = 'Blocked',
             REASON               = 'Some other Test_forms are assigned Blocked',
             ATTEMPTED_COUNTER    = ATTEMPTED_COUNTER + 1
       WHERE STUDENT_ID = STU_DETAILS.STUDENT_ID
         AND JOB_STATUS = 'Prepared';
    END IF;
    COMMIT; -- COMMITTING FOR STATUS CHANGE
  END LOOP; -- OUTER FOR LOOP
EXCEPTION
  WHEN OTHERS THEN
    ROLLBACK;
    DBMS_OUTPUT.PUT_LINE('EXCEPTION OCCURRED..');
END ASSIGN_FIXED_SET_OF_FORM_GA;
