CREATE OR REPLACE PROCEDURE SP_TABE_LOCATOR_ERROR_MONITOR IS
  /*
  * This Procedure Monitors the TABE locator error and log the issue list in a error log table
  * Story : OAS - 1941
  * Updated By Story : OAS - 2311
  */

  /* 
  * This cursor fetches the list of those rosters and subtest details which are assigned with TABE tests and have either
  * logged in or completed any subtest before 2 hours of the job execution.
  * e.g. - If Job exectute at 10:00 am then fetch query time span 6:00 am - 8:00 am
  */

  CURSOR CUR_SUBTEST_DETAILS IS
    SELECT TR.TEST_ROSTER_ID AS TEST_ROSTER_ID,
           TR.TEST_COMPLETION_STATUS AS TEST_COMPLETION_STATUS,
           TR.CUSTOMER_ID AS CUSTOMER_ID,
           TR.STUDENT_ID AS STUDENT_ID,
           TR.TEST_ADMIN_ID AS TEST_ADMIN_ID,
           PR.PRODUCT_NAME AS PRODUCT_NAME,
           PR.PRODUCT_ID AS PRODUCT_ID,
           SISS.ITEM_SET_ID AS ITEM_SET,
           SISS.COMPLETION_STATUS AS SUBTEST_COMPLETION_STATUS,
           SISS.COMPLETION_DATE_TIME AS SUBTEST_COMPLETION_DATETIME,
           SISS.RAW_SCORE AS RAW_SCORE,
           SISS.RECOMMENDED_LEVEL AS RECOMMENDED_LEVEL,
           SISS.ITEM_SET_ORDER AS ITEM_SET_ORDER,
           ISET.ITEM_SET_NAME AS ITEM_SET_NAME,
           (SELECT DECODE(COUNT(1), 0, 'F', 'T')
              FROM CUSTOMER_CONFIGURATION CC
             WHERE CC.CUSTOMER_ID = TA.CUSTOMER_ID
               AND CC.CUSTOMER_CONFIGURATION_NAME = 'TAS_Revised_UI'
               AND CC.DEFAULT_VALUE = 'T') AS REVISED_UI
      FROM TEST_ADMIN              TA,
           TEST_ROSTER             TR,
           PRODUCT                 PR,
           STUDENT_ITEM_SET_STATUS SISS,
           ITEM_SET                ISET,
           ITEM_SET_ANCESTOR       ISA,
           TEST_CATALOG            TC
     WHERE TA.TEST_ADMIN_ID = TR.TEST_ADMIN_ID
       AND TA.TEST_CATALOG_ID = TC.TEST_CATALOG_ID
       AND TC.PRODUCT_ID = PR.PRODUCT_ID
       AND TA.PRODUCT_ID = PR.PRODUCT_ID
       AND PR.PARENT_PRODUCT_ID = 4000
       AND PR.PRODUCT_TYPE IN ('TB', 'TL')
       AND SISS.TEST_ROSTER_ID = TR.TEST_ROSTER_ID
       AND (TR.START_DATE_TIME BETWEEN (SYSDATE - 4 / 24) AND
           (SYSDATE - 2 / 24) OR TR.COMPLETION_DATE_TIME BETWEEN
           (SYSDATE - 4 / 24) AND (SYSDATE - 2 / 24) OR
           SISS.START_DATE_TIME BETWEEN (SYSDATE - 4 / 24) AND
           (SYSDATE - 2 / 24) OR SISS.COMPLETION_DATE_TIME BETWEEN
           (SYSDATE - 4 / 24) AND (SYSDATE - 2 / 24))
       AND TRUNC(TA.LOGIN_START_DATE) <= TRUNC(SYSDATE)
       AND TRUNC(TA.LOGIN_END_DATE) >= TRUNC(SYSDATE)
       AND TA.ACTIVATION_STATUS = 'AC'
       AND TR.ACTIVATION_STATUS = 'AC'
       AND PR.ACTIVATION_STATUS = 'AC'
       AND ISET.ITEM_SET_ID = SISS.ITEM_SET_ID
       AND ISET.ITEM_SET_TYPE = 'TD'
       AND ISET.ITEM_SET_LEVEL = 'L'
       AND ISET.SAMPLE = 'F'
       AND ISET.ACTIVATION_STATUS = 'AC'
       AND ISA.ANCESTOR_ITEM_SET_ID = TA.ITEM_SET_ID
       AND ISA.ITEM_SET_ID = ISET.ITEM_SET_ID
       AND ISA.ITEM_SET_TYPE = 'TD'
     ORDER BY TEST_ROSTER_ID, ITEM_SET_ORDER;

  V_MAIN_SUBTEST_PRSNT   INTEGER;
  V_MAIN_SUBTEST_COUNT   INTEGER;
  V_NXT_TD_STARTED       INTEGER;
  V_MAIN_TD_STARTED      INTEGER;
  V_TEST_ROSTER_REPORTED TEST_ROSTER.TEST_ROSTER_ID%TYPE := 0;
  V_ERROR_MSG            VARCHAR2(2000) := NULL;

BEGIN

  FOR REC_SUBTEST_DETAILS IN CUR_SUBTEST_DETAILS LOOP
    -- Reinitialize the temp variables
    V_MAIN_SUBTEST_PRSNT := 0;
    V_NXT_TD_STARTED     := 0;
    V_MAIN_TD_STARTED    := 0;
    V_MAIN_SUBTEST_COUNT := 0;
    V_ERROR_MSG          := NULL;
  
    IF V_TEST_ROSTER_REPORTED <> REC_SUBTEST_DETAILS.TEST_ROSTER_ID THEN
      /**
      * Checking other than locator only test and recommended level is null.
      */
      IF REC_SUBTEST_DETAILS.RECOMMENDED_LEVEL IS NOT NULL AND
         REC_SUBTEST_DETAILS.PRODUCT_ID <> 4008 THEN
        /**
        * Checking if original subtests are present or not at roster level
        */
        SELECT COUNT(1)
          INTO V_MAIN_SUBTEST_PRSNT
          FROM STUDENT_ITEM_SET_STATUS SISS
         WHERE SISS.TEST_ROSTER_ID = REC_SUBTEST_DETAILS.TEST_ROSTER_ID
           AND EXISTS (SELECT 1
                  FROM ITEM_SET IST
                 WHERE IST.ITEM_SET_ID = SISS.ITEM_SET_ID
                   AND IST.ITEM_SET_LEVEL <> 'L');
      
        IF V_MAIN_SUBTEST_PRSNT = 0 THEN
          /**
          * Log error if Locator level is set but original subtests are not present in SISS
          */
          V_ERROR_MSG := 'The original subtests are not present for the Roster';
        
        ELSE
          /**
          * Checking if unwanted subtests are not removed applicable for LEGACY UI only
          */
          IF REC_SUBTEST_DETAILS.REVISED_UI = 'F' THEN
            SELECT DECODE(IST.ITEM_SET_NAME,
                          'TABE Locator Reading',
                          CEIL(((SELECT DECODE(COUNT(1), 0, 1, COUNT(1))
                                   FROM ITEM_SET                IST,
                                        STUDENT_ITEM_SET_STATUS SISS
                                  WHERE IST.ITEM_SET_ID = SISS.ITEM_SET_ID
                                    AND SISS.TEST_ROSTER_ID =
                                        REC_SUBTEST_DETAILS.TEST_ROSTER_ID
                                    AND IST.ITEM_SET_NAME IN ('TABE Reading')) +
                               (SELECT DECODE(COUNT(1), 0, 1, COUNT(1))
                                   FROM ITEM_SET                IST,
                                        STUDENT_ITEM_SET_STATUS SISS
                                  WHERE IST.ITEM_SET_ID = SISS.ITEM_SET_ID
                                    AND SISS.TEST_ROSTER_ID =
                                        REC_SUBTEST_DETAILS.TEST_ROSTER_ID
                                    AND IST.ITEM_SET_NAME IN
                                        ('TABE Vocabulary'))) / 2),
                          'TABE Locator Mathematics Computation',
                          CEIL(((SELECT DECODE(COUNT(1), 0, 1, COUNT(1))
                                   FROM ITEM_SET                IST,
                                        STUDENT_ITEM_SET_STATUS SISS
                                  WHERE IST.ITEM_SET_ID = SISS.ITEM_SET_ID
                                    AND SISS.TEST_ROSTER_ID =
                                        REC_SUBTEST_DETAILS.TEST_ROSTER_ID
                                    AND IST.ITEM_SET_NAME IN
                                        ('TABE Mathematics Computation')) +
                               (SELECT DECODE(COUNT(1), 0, 1, COUNT(1))
                                   FROM ITEM_SET                IST,
                                        STUDENT_ITEM_SET_STATUS SISS
                                  WHERE IST.ITEM_SET_ID = SISS.ITEM_SET_ID
                                    AND SISS.TEST_ROSTER_ID =
                                        REC_SUBTEST_DETAILS.TEST_ROSTER_ID
                                    AND IST.ITEM_SET_NAME IN
                                        ('TABE Applied Mathematics'))) / 2),
                          'TABE Locator Applied Mathematics',
                          CEIL(((SELECT DECODE(COUNT(1), 0, 1, COUNT(1))
                                   FROM ITEM_SET                IST,
                                        STUDENT_ITEM_SET_STATUS SISS
                                  WHERE IST.ITEM_SET_ID = SISS.ITEM_SET_ID
                                    AND SISS.TEST_ROSTER_ID =
                                        REC_SUBTEST_DETAILS.TEST_ROSTER_ID
                                    AND IST.ITEM_SET_NAME IN
                                        ('TABE Mathematics Computation')) +
                               (SELECT DECODE(COUNT(1), 0, 1, COUNT(1))
                                   FROM ITEM_SET                IST,
                                        STUDENT_ITEM_SET_STATUS SISS
                                  WHERE IST.ITEM_SET_ID = SISS.ITEM_SET_ID
                                    AND SISS.TEST_ROSTER_ID =
                                        REC_SUBTEST_DETAILS.TEST_ROSTER_ID
                                    AND IST.ITEM_SET_NAME IN
                                        ('TABE Applied Mathematics'))) / 2),
                          'TABE Locator Language',
                          CEIL(((SELECT DECODE(COUNT(1), 0, 1, COUNT(1))
                                   FROM ITEM_SET                IST,
                                        STUDENT_ITEM_SET_STATUS SISS
                                  WHERE IST.ITEM_SET_ID = SISS.ITEM_SET_ID
                                    AND SISS.TEST_ROSTER_ID =
                                        REC_SUBTEST_DETAILS.TEST_ROSTER_ID
                                    AND IST.ITEM_SET_NAME IN
                                        ('TABE Language')) +
                               (SELECT DECODE(COUNT(1), 0, 1, COUNT(1))
                                   FROM ITEM_SET                IST,
                                        STUDENT_ITEM_SET_STATUS SISS
                                  WHERE IST.ITEM_SET_ID = SISS.ITEM_SET_ID
                                    AND SISS.TEST_ROSTER_ID =
                                        REC_SUBTEST_DETAILS.TEST_ROSTER_ID
                                    AND IST.ITEM_SET_NAME IN
                                        ('TABE Language Mechanics')) +
                               (SELECT DECODE(COUNT(1), 0, 1, COUNT(1))
                                   FROM ITEM_SET                IST,
                                        STUDENT_ITEM_SET_STATUS SISS
                                  WHERE IST.ITEM_SET_ID = SISS.ITEM_SET_ID
                                    AND SISS.TEST_ROSTER_ID =
                                        REC_SUBTEST_DETAILS.TEST_ROSTER_ID
                                    AND IST.ITEM_SET_NAME IN
                                        ('TABE Spelling'))) / 3),
                          2)
              INTO V_MAIN_SUBTEST_COUNT
              FROM ITEM_SET IST
             WHERE IST.ITEM_SET_ID = REC_SUBTEST_DETAILS.ITEM_SET;
          
            IF V_MAIN_SUBTEST_COUNT > 1 THEN
              /*
              * Log error if Locator level is set but unwanted subtests are not removed from SISS
              */
              V_ERROR_MSG := 'The original set of subtests are not correct for ' ||
                             REC_SUBTEST_DETAILS.ITEM_SET_NAME ||
                             ' subtest.';
            END IF;
          
          ELSE
            /**
            * Checking if wanted subtest are not present and
            * unwanted subtests are not removed applicable for TAS REVISED UI only
            */
            SELECT DECODE(IST.ITEM_SET_NAME,
                          'TABE Locator Reading',
                          FLOOR(((SELECT COUNT(1)
                                    FROM ITEM_SET                IST,
                                         STUDENT_ITEM_SET_STATUS SISS
                                   WHERE IST.ITEM_SET_ID = SISS.ITEM_SET_ID
                                     AND SISS.TEST_ROSTER_ID =
                                         REC_SUBTEST_DETAILS.TEST_ROSTER_ID
                                     AND IST.ITEM_SET_NAME IN
                                         ('TABE Reading')) +
                                (SELECT DECODE(COUNT(1), 0, 1, COUNT(1))
                                    FROM ITEM_SET                IST,
                                         STUDENT_ITEM_SET_STATUS SISS
                                   WHERE IST.ITEM_SET_ID = SISS.ITEM_SET_ID
                                     AND SISS.TEST_ROSTER_ID =
                                         REC_SUBTEST_DETAILS.TEST_ROSTER_ID
                                     AND IST.ITEM_SET_NAME IN
                                         ('TABE Vocabulary'))) / 2),
                          'TABE Locator Language',
                          FLOOR(((SELECT COUNT(1)
                                    FROM ITEM_SET                IST,
                                         STUDENT_ITEM_SET_STATUS SISS
                                   WHERE IST.ITEM_SET_ID = SISS.ITEM_SET_ID
                                     AND SISS.TEST_ROSTER_ID =
                                         REC_SUBTEST_DETAILS.TEST_ROSTER_ID
                                     AND IST.ITEM_SET_NAME IN
                                         ('TABE Language')) +
                                (SELECT DECODE(COUNT(1), 0, 1, COUNT(1))
                                    FROM ITEM_SET                IST,
                                         STUDENT_ITEM_SET_STATUS SISS
                                   WHERE IST.ITEM_SET_ID = SISS.ITEM_SET_ID
                                     AND SISS.TEST_ROSTER_ID =
                                         REC_SUBTEST_DETAILS.TEST_ROSTER_ID
                                     AND IST.ITEM_SET_NAME IN
                                         ('TABE Language Mechanics')) +
                                (SELECT DECODE(COUNT(1), 0, 1, COUNT(1))
                                    FROM ITEM_SET                IST,
                                         STUDENT_ITEM_SET_STATUS SISS
                                   WHERE IST.ITEM_SET_ID = SISS.ITEM_SET_ID
                                     AND SISS.TEST_ROSTER_ID =
                                         REC_SUBTEST_DETAILS.TEST_ROSTER_ID
                                     AND IST.ITEM_SET_NAME IN
                                         ('TABE Spelling'))) / 3),
                          'TABE Locator Mathematics Computation',
                          CEIL(((SELECT COUNT(1)
                                   FROM ITEM_SET                IST,
                                        STUDENT_ITEM_SET_STATUS SISS
                                  WHERE IST.ITEM_SET_ID = SISS.ITEM_SET_ID
                                    AND SISS.TEST_ROSTER_ID =
                                        REC_SUBTEST_DETAILS.TEST_ROSTER_ID
                                    AND IST.ITEM_SET_NAME IN
                                        ('TABE Mathematics Computation')) +
                               (SELECT COUNT(1)
                                   FROM ITEM_SET                IST,
                                        STUDENT_ITEM_SET_STATUS SISS
                                  WHERE IST.ITEM_SET_ID = SISS.ITEM_SET_ID
                                    AND SISS.TEST_ROSTER_ID =
                                        REC_SUBTEST_DETAILS.TEST_ROSTER_ID
                                    AND IST.ITEM_SET_NAME IN
                                        ('TABE Applied Mathematics'))) / 2),
                          'TABE Locator Applied Mathematics',
                          CEIL(((SELECT COUNT(1)
                                   FROM ITEM_SET                IST,
                                        STUDENT_ITEM_SET_STATUS SISS
                                  WHERE IST.ITEM_SET_ID = SISS.ITEM_SET_ID
                                    AND SISS.TEST_ROSTER_ID =
                                        REC_SUBTEST_DETAILS.TEST_ROSTER_ID
                                    AND IST.ITEM_SET_NAME IN
                                        ('TABE Mathematics Computation')) +
                               (SELECT COUNT(1)
                                   FROM ITEM_SET                IST,
                                        STUDENT_ITEM_SET_STATUS SISS
                                  WHERE IST.ITEM_SET_ID = SISS.ITEM_SET_ID
                                    AND SISS.TEST_ROSTER_ID =
                                        REC_SUBTEST_DETAILS.TEST_ROSTER_ID
                                    AND IST.ITEM_SET_NAME IN
                                        ('TABE Applied Mathematics'))) / 2),
                          0)
              INTO V_MAIN_SUBTEST_COUNT
              FROM ITEM_SET IST
             WHERE IST.ITEM_SET_ID = REC_SUBTEST_DETAILS.ITEM_SET;
          
            IF V_MAIN_SUBTEST_COUNT <> 1 THEN
              /*
              * Log error if Locator level is set but unwanted subtests are not removed from SISS
              */
              V_ERROR_MSG := 'The original set of subtests are not correct for ' ||
                             REC_SUBTEST_DETAILS.ITEM_SET_NAME ||
                             ' subtest.';
            END IF;
          
          END IF;
        END IF;
        IF V_ERROR_MSG IS NOT NULL THEN
          V_TEST_ROSTER_REPORTED := REC_SUBTEST_DETAILS.TEST_ROSTER_ID;
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
            (REC_SUBTEST_DETAILS.PRODUCT_NAME,
             REC_SUBTEST_DETAILS.STUDENT_ID,
             REC_SUBTEST_DETAILS.TEST_ADMIN_ID,
             REC_SUBTEST_DETAILS.TEST_ROSTER_ID,
             REC_SUBTEST_DETAILS.TEST_COMPLETION_STATUS,
             'TABE LOCATOR',
             'ERROR',
             'N',
             SYSDATE,
             V_ERROR_MSG,
             REC_SUBTEST_DETAILS.CUSTOMER_ID);
          COMMIT;
        END IF;
      
      ELSIF REC_SUBTEST_DETAILS.RECOMMENDED_LEVEL IS NULL THEN
      
        IF REC_SUBTEST_DETAILS.SUBTEST_COMPLETION_STATUS = 'CO' OR
           REC_SUBTEST_DETAILS.SUBTEST_COMPLETION_DATETIME IS NOT NULL THEN
          /*
          * Log error if SISS is in 'CO' status but Locator level is not set in DB for the TD level subtest
          */
        
          V_ERROR_MSG := 'The Recommended Level should be set for the completed subtest.';
        
        ELSE
          /**
          * Checking if the next locator subtest has started
          */
          SELECT COUNT(1)
            INTO V_NXT_TD_STARTED
            FROM STUDENT_ITEM_SET_STATUS SISS
           WHERE SISS.TEST_ROSTER_ID = REC_SUBTEST_DETAILS.TEST_ROSTER_ID
             AND SISS.ITEM_SET_ORDER > REC_SUBTEST_DETAILS.ITEM_SET_ORDER
             AND SISS.COMPLETION_STATUS <> 'SC'
             AND EXISTS
           (SELECT 1
                    FROM ITEM_RESPONSE IR
                   WHERE IR.TEST_ROSTER_ID = SISS.TEST_ROSTER_ID
                     AND IR.ITEM_SET_ID = SISS.ITEM_SET_ID);
        
          IF V_NXT_TD_STARTED > 0 THEN
            /*
            * Log error if SISS is not in 'CO' status but the next Locator subtest has started
            */
            V_ERROR_MSG := 'The Recommended Level should be set before starting the next test.';
          END IF;
        END IF;
      
        IF V_ERROR_MSG IS NOT NULL THEN
          V_TEST_ROSTER_REPORTED := REC_SUBTEST_DETAILS.TEST_ROSTER_ID;
        
          INSERT INTO TABE_ERROR_MONITOR
            (PRODUCT_NAME,
             STUDENT_ID,
             TEST_ADMIN_ID,
             TEST_ROSTER_ID,
             ROSTER_STATUS,
             TD_ITEM_SET,
             SISS_STATUS,
             RAW_SCORE,
             ERROR_TYPE_FLAG,
             ERROR_STATUS,
             EMAIL_FLAG,
             CREATED_DATETIME,
             COMMENTS,
             CUSTOMER_ID)
          VALUES
            (REC_SUBTEST_DETAILS.PRODUCT_NAME,
             REC_SUBTEST_DETAILS.STUDENT_ID,
             REC_SUBTEST_DETAILS.TEST_ADMIN_ID,
             REC_SUBTEST_DETAILS.TEST_ROSTER_ID,
             REC_SUBTEST_DETAILS.TEST_COMPLETION_STATUS,
             REC_SUBTEST_DETAILS.ITEM_SET,
             REC_SUBTEST_DETAILS.SUBTEST_COMPLETION_STATUS,
             REC_SUBTEST_DETAILS.RAW_SCORE,
             'TABE LOCATOR',
             'ERROR',
             'N',
             SYSDATE,
             V_ERROR_MSG,
             REC_SUBTEST_DETAILS.CUSTOMER_ID);
          COMMIT;
        END IF;
      
      END IF;
    END IF; -- Reported roster check end
  
  END LOOP;

EXCEPTION
  WHEN OTHERS THEN
    RAISE;
  
END SP_TABE_LOCATOR_ERROR_MONITOR;
/
