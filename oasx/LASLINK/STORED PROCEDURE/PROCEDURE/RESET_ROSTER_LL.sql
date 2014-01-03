CREATE OR REPLACE PROCEDURE RESET_ROSTER(ROSTERS_IN IN ROSTERARRAY,  RESULTOUT  OUT VARCHAR2) AS

V_SEQ_VAL NUMBER := 0;

BEGIN

  SELECT SEQ_AUDIT_ID.NEXTVAL INTO V_SEQ_VAL FROM DUAL;

  FOR I IN 1 .. ROSTERS_IN.COUNT LOOP
  
    FOR J IN 1 .. ROSTERS_IN(I).SUBTEST_IDS.COUNT LOOP
    
      INSERT INTO ITEM_RESPONSE_AUDIT
        (ITEM_RESPONSE_ID,
         ITEM_SET_ID,
         TEST_ROSTER_ID,
         RESPONSE,
         RESPONSE_METHOD,
         RESPONSE_ELAPSED_TIME,
         RESPONSE_SEQ_NUM,
         CREATED_DATE_TIME,
         ITEM_ID,
         EXT_ANSWER_CHOICE_ID,
         STUDENT_MARKED,
         CREATED_BY,
         AUDIT_ID)
        (SELECT ITEM_RESPONSE_ID,
               ITEM_SET_ID,
               TEST_ROSTER_ID,
               RESPONSE,
               RESPONSE_METHOD,
               RESPONSE_ELAPSED_TIME,
               RESPONSE_SEQ_NUM,
               CREATED_DATE_TIME,
               ITEM_ID,
               EXT_ANSWER_CHOICE_ID,
               STUDENT_MARKED,
               CREATED_BY,
               V_SEQ_VAL
          FROM ITEM_RESPONSE
         WHERE TEST_ROSTER_ID = ROSTERS_IN(I)
        .ROSTER_ID
           AND ITEM_SET_ID = ROSTERS_IN(I).SUBTEST_IDS(J));
    
      INSERT INTO ITEM_RESPONSE_POINTS_AUDIT
        (POINTS,
         ITEM_RESPONSE_POINTS_SEQ_NUM,
         DATAPOINT_ID,
         CONDITION_CODE_ID,
         CREATED_BY,
         CREATED_DATE_TIME,
         ITEM_RESPONSE_ID,
         COMMENTS,
         AUDIT_ID)
        (SELECT POINTS,
               ITEM_RESPONSE_POINTS_SEQ_NUM,
               DATAPOINT_ID,
               CONDITION_CODE_ID,
               CREATED_BY,
               CREATED_DATE_TIME,
               ITEM_RESPONSE_ID,
               COMMENTS,
               V_SEQ_VAL
          FROM ITEM_RESPONSE_POINTS
         WHERE ITEM_RESPONSE_ID IN
               (SELECT ITEM_RESPONSE_ID
                  FROM ITEM_RESPONSE
                 WHERE TEST_ROSTER_ID = ROSTERS_IN(I)
                .ROSTER_ID
                   AND ITEM_SET_ID = ROSTERS_IN(I).SUBTEST_IDS(J)));
    
      DELETE FROM ITEM_RESPONSE_POINTS
       WHERE ITEM_RESPONSE_ID IN
             (SELECT ITEM_RESPONSE_ID
                FROM ITEM_RESPONSE
               WHERE TEST_ROSTER_ID = ROSTERS_IN(I)
              .ROSTER_ID
                 AND ITEM_SET_ID = ROSTERS_IN(I).SUBTEST_IDS(J));
    
      DELETE FROM ITEM_RESPONSE
       WHERE TEST_ROSTER_ID = ROSTERS_IN(I)
      .ROSTER_ID
         AND ITEM_SET_ID = ROSTERS_IN(I).SUBTEST_IDS(J);
    
      INSERT INTO ITEM_RESPONSE_CR_AUDIT
        (TEST_ROSTER_ID,
         ITEM_SET_ID,
         ITEM_ID,
         CONSTRUCTED_RESPONSE,
         AUDIT_ID)
        (SELECT TEST_ROSTER_ID,
               ITEM_SET_ID,
               ITEM_ID,
               CONSTRUCTED_RESPONSE,
               V_SEQ_VAL
          FROM ITEM_RESPONSE_CR
         WHERE TEST_ROSTER_ID = ROSTERS_IN(I)
        .ROSTER_ID
           AND ITEM_SET_ID = ROSTERS_IN(I).SUBTEST_IDS(J));
    
   
     /* The below queries are not working as
        ITEM_RESPONSE_CR table does not have ITEM_RESPONSE_ID column.
        So to fix Defect #74884 below two queries (insert and delete) are commented.
     */
   
     /* INSERT INTO ITEM_RESPONSE_POINTS_AUDIT
        (POINTS,
         ITEM_RESPONSE_POINTS_SEQ_NUM,
         DATAPOINT_ID,
         CONDITION_CODE_ID,
         CREATED_BY,
         CREATED_DATE_TIME,
         ITEM_RESPONSE_ID,
         COMMENTS,
         AUDIT_ID)
        SELECT POINTS,
               ITEM_RESPONSE_POINTS_SEQ_NUM,
               DATAPOINT_ID,
               CONDITION_CODE_ID,
               CREATED_BY,
               CREATED_DATE_TIME,
               ITEM_RESPONSE_ID,
               COMMENTS,
               V_SEQ_VAL
          FROM ITEM_RESPONSE_POINTS
         WHERE ITEM_RESPONSE_ID IN
               (SELECT ITEM_RESPONSE_ID
                  FROM ITEM_RESPONSE_CR
                 WHERE TEST_ROSTER_ID = ROSTERS_IN(I)
                .ROSTER_ID
                   AND ITEM_SET_ID = ROSTERS_IN(I).SUBTEST_IDS(J));
    
      DELETE FROM ITEM_RESPONSE_POINTS
       WHERE ITEM_RESPONSE_ID IN
             (SELECT ITEM_RESPONSE_ID
                FROM ITEM_RESPONSE_CR
               WHERE TEST_ROSTER_ID = ROSTERS_IN(I)
              .ROSTER_ID
                 AND ITEM_SET_ID = ROSTERS_IN(I).SUBTEST_IDS(J));*/
    
      DELETE FROM ITEM_RESPONSE_CR
       WHERE TEST_ROSTER_ID = ROSTERS_IN(I)
      .ROSTER_ID
         AND ITEM_SET_ID = ROSTERS_IN(I).SUBTEST_IDS(J);
    
      UPDATE STUDENT_ITEM_SET_STATUS
         SET COMPLETION_STATUS    = 'SC',
             START_DATE_TIME      = NULL,
             COMPLETION_DATE_TIME = NULL,
             RAW_SCORE            = NULL,
             MAX_SCORE            = NULL,
             UNSCORED             = NULL,
             EXEMPTIONS           = 'N',
             ABSENT               = 'N',
             ABILITY_SCORE        = NULL,
             SEM_SCORE            = NULL,
             OBJECTIVE_SCORE      = NULL,
             TMS_UPDATE           = 'F'
       WHERE TEST_ROSTER_ID = ROSTERS_IN(I)
      .ROSTER_ID
         AND ITEM_SET_ID = ROSTERS_IN(I).SUBTEST_IDS(J);
    
    /* UPDATE TEST_ROSTER
       SET TEST_COMPLETION_STATUS = 'IS',
           START_DATE_TIME        = NULL,
           COMPLETION_DATE_TIME   = NULL,
           UPDATED_DATE_TIME      = NULL,
           LAST_LOGIN_DATE_TIME   = NULL,
           RESTART_NUMBER         = NULL,
           LAST_MSEQ              = NULL,
           TMS_UPDATE             = NULL
     WHERE TEST_ROSTER_ID = ROSTERS_IN(I).ROSTER_ID;*/
     
    END LOOP;
  
    UPDATE TEST_ROSTER
       SET TEST_COMPLETION_STATUS = 'IS', UPDATED_DATE_TIME = SYSDATE
     WHERE TEST_ROSTER_ID = ROSTERS_IN(I).ROSTER_ID;
  
  END LOOP;
  RESULTOUT := 'Success:' || V_SEQ_VAL;
  COMMIT;

EXCEPTION

  WHEN OTHERS THEN
    RESULTOUT := SQLERRM;

  
END RESET_ROSTER;
/
