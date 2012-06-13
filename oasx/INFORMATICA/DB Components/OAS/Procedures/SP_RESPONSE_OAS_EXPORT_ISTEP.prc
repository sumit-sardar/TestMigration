CREATE OR REPLACE PROCEDURE SP_RESPONSE_OAS_EXPORT_ISTEP IS
  --  This Procedure is Used to Load the concatenated string of all the TS records,
  --    Concatenated string of all the Response arrays and Score arrays for a particular Test Catalog Item_set_id of the Student --

  V_RA VARCHAR2(2000) := '';
  -- Variable to hold the Response Array --
  V_SA VARCHAR2(2000) := '';
  --Variable to hold the Score Array --
  V_EA VARCHAR2(2000) := '';
  --Variable to hold the Erasure Array --
  V_SUM INTEGER := 0;
  --Variable to hold the score of the student --
  --   v_ts_id         INTEGER         := 0;
  --Variable to hold the Test Section ID --
  V_SUBJECT VARCHAR2(100) := ''; --Variable to hold Subject
  --   v_subject1     VARCHAR2 (2000) := '';         --Variable to hold Subject
  V_SECTTYP VARCHAR2(2) := 'SR';
  -- Variable to hold Selection Type, BY Default is 'SR" --
  V_INVFLAG       VARCHAR2(1) := '0'; -- Variable to hold InvFlag --
  V_SUPPFLAG      VARCHAR2(1) := '0'; -- Variable to hold SuppFlag --
  V_OMITFLAG      VARCHAR2(1) := '0'; -- Variable to hold OmitFlag --
  V_FLIPFLAG      VARCHAR2(1) := '0'; -- Variable to hold FlipFlag --
  V_ERASURE_COUNT NUMBER := 0;
  V_GLOBAL_TS_ID  INTEGER := 0;
  V_CR_DECISION   VARCHAR2(1);
  
  -- added to see execution time duration performance check 
  nStartTime number(38) := 0;
  nEndTime   number(38) := 0;
  nTotalTimeTaken number(38) := 0;

  -- := '0';  -- Variable to hold ErasureCnt --
  TYPE SR_RESP_REC IS RECORD(
    TD_SUBJECT       VARCHAR2(60),
    QUES_NO          INTEGER,
    RESPONSE         VARCHAR2(1),
    CORRECT_ANSWER   VARCHAR2(1),
    SCORE            VARCHAR2(1),
    RESPONSE_SEQ_NUM INTEGER,
    ITEM_ID          VARCHAR2(32),
    ACTUAL_RESPONSE  VARCHAR2(1),
    TD_ITEM_SET_ID   INTEGER, ----/* Added to get the value from cursor c_td .It is used to insert subtest id in the ts_id column in stg_eiss_tb */
    TS_ORDER         INTEGER);

  TYPE SR_RESP_TAB IS TABLE OF SR_RESP_REC INDEX BY BINARY_INTEGER;

  V_SR_RESP_TAB SR_RESP_TAB;

  TYPE GI_RESP_REC IS RECORD(
    FORM_ELEMENT_ID VARCHAR2(30), --FCAT  Spring 2008  --- old field external_id       VARCHAR2 (30),
    GRIDDED_COLUMNS INTEGER,
    TD_ITEM_SET_ID  INTEGER,
    QUES_NO         INTEGER,
    OMIT            INTEGER,
    RESPONSE        VARCHAR2(10));

  TYPE GI_RESP_TAB IS TABLE OF GI_RESP_REC INDEX BY BINARY_INTEGER;

  V_GI_RESP_TAB GI_RESP_TAB;

  -- Cursor to select all ST records in STG_EISS_TB
  CURSOR C_ST IS
    SELECT CUSTOMER_ID,
           PRODUCT_ID,
           TEST_ROSTER_ID,
           OU_ID,
           SH_ID,
           GH_ID,
           SR_ID,
           ST_ID,
           GRADE,
           SORT_ORDER,
           /*ERASURE_COUNT(TEST_ROSTER_ID) ERASURE_CNT,*/ -- erasure count must be calculated at the subtest level
           OPUNIT
      FROM STG_EISS_TB
     WHERE LEVEL_TYPE = 'ST';

  --  Cursor to select all TD/RA,SA records

  --cursor for stg_export_parms table
  CURSOR CUR_EXP_CR_DECISION IS
    SELECT VALUE

      FROM OAS_STAGE.STG_EXPORT_PARMS PARAM, STG_PARAM_WINSCR_TB STG
     WHERE UPPER(PARAM.PARAMETER) = 'CR_DECISION'
       AND PARAM.CUSTOMER_ID = STG.CUSTOMER_ID;

  --cursor for test section name *** added for ISTEP Spring09
  CURSOR CUR_TEST_SECTION_NAME(P_CUSTOMER_ID INTEGER, P_OAS_TS_NAME VARCHAR2) IS
    SELECT VALUE
      FROM OAS_STAGE.STG_EXPORT_PARMS STG
     WHERE STG.CUSTOMER_ID = P_CUSTOMER_ID
       AND STG.PARAMETER = P_OAS_TS_NAME ; 

  --cursor rowtype declaration
  CURSOR_CR_DECISION CUR_EXP_CR_DECISION%ROWTYPE;

  CURSOR C_TD(IN_TEST_ROSTER_ID NUMBER, IN_STUDENT_ID NUMBER, IN_TC_ITEM_SET_ID NUMBER) IS WITH MAIN AS(
  --Extract all the Item_Set_Ids, Item_Ids along with their Correct answers and Order them as to how they were ordered at the test
    SELECT TR.TEST_ROSTER_ID AS TEST_ROSTER_ID,
           ISTD.ITEM_SET_ID TD_ITEM_SET_ID,
           I.ITEM_ID,
           ISTD.ITEM_SET_NAME TD_SUBJECT,
           CORRECT_ANSWER,
           ITEM_SORT_ORDER,
           ISP.ITEM_SET_SORT_ORDER TD_ITEM_SET_SORT_ORDER,
           SI.ITEM_SET_ORDER SI_ITEM_SET_ORDER
      FROM TEST_ADMIN              TA,
           TEST_CATALOG            TC,
           ITEM_SET_ITEM           ISI,
           ITEM                    I,
           ITEM_SET                ISTD,
           ITEM_SET_PARENT         ISP,
           STUDENT_ITEM_SET_STATUS SI,
           TEST_ROSTER             TR,
           STUDENT                 ST,
           STG_PARAM_WINSCR_TB     PARM,
           ORG_NODE_STUDENT        ONS
     WHERE TA.PRODUCT_ID = TC.PRODUCT_ID
       AND TA.TEST_CATALOG_ID = TC.TEST_CATALOG_ID
       AND TR.TEST_ADMIN_ID = TA.TEST_ADMIN_ID
       AND TR.CUSTOMER_ID = TA.CUSTOMER_ID
       AND ISI.ITEM_SET_ID = ISTD.ITEM_SET_ID
       AND ISI.ITEM_ID = I.ITEM_ID
       AND ISTD.ITEM_SET_ID = ISP.ITEM_SET_ID
       AND ISTD.SAMPLE = 'F'
       AND TR.TEST_ROSTER_ID = SI.TEST_ROSTER_ID
       AND TR.STUDENT_ID = ST.STUDENT_ID
       AND SI.ITEM_SET_ID = ISTD.ITEM_SET_ID
       AND TA.PRODUCT_ID = PARM.PRODUCT_ID
       AND TR.CUSTOMER_ID = PARM.CUSTOMER_ID
       AND ISP.PARENT_ITEM_SET_ID IN
           (SELECT ITEM_SET_PARENT.ITEM_SET_ID
              FROM ITEM_SET_PARENT, TEST_CATALOG
             WHERE ITEM_SET_PARENT.PARENT_ITEM_SET_ID =
                   TEST_CATALOG.ITEM_SET_ID
               AND TEST_CATALOG.ITEM_SET_ID = IN_TC_ITEM_SET_ID
               AND ITEM_SET_PARENT.ITEM_SET_TYPE = 'TS'
               AND TEST_CATALOG.PRODUCT_ID = PARM.PRODUCT_ID)
          -- in_ts_id
       AND TR.STUDENT_ID = IN_STUDENT_ID
       AND I.ITEM_TYPE = 'SR'
       AND TA.CUSTOMER_ID = PARM.CUSTOMER_ID
       AND TA.PRODUCT_ID = PARM.PRODUCT_ID
       AND TR.STUDENT_ID = ONS.STUDENT_ID
       AND TR.ORG_NODE_ID = ONS.ORG_NODE_ID
       AND TR.TEST_ROSTER_ID = IN_TEST_ROSTER_ID)
      SELECT TD_SUBJECT,
       ITEM_SORT_ORDER AS QUES_NO,
       DECODE(RESPONSE,
              'A',
              '1',
              'B',
              '2',
              'C',
              '3',
              'D',
              '4',
              'E',
              '5',
              '*') AS RESPONSE,
       DECODE(CORRECT_ANSWER,
              'A',
              '1',
              'B',
              '2',
              'C',
              '3',
              'D',
              '4',
              'E',
              '5',
              '*') AS CORRECT_ANSWER,
       (CASE
         WHEN (RESPONSE = CORRECT_ANSWER) THEN
          '1'
         ELSE
          '0'
       END) AS SCORE,
       IR.RESPONSE_SEQ_NUM,
       M.ITEM_ID,
       RESPONSE ACTUAL_RESPONSE,
       M.TD_ITEM_SET_ID,
       SI_ITEM_SET_ORDER AS TS_ORDER /* m.td_item_set_id is added to get the subtest_id from main virtual table*/
        FROM MAIN M, ITEM_RESPONSE IR
       WHERE M.TEST_ROSTER_ID = IR.TEST_ROSTER_ID(+)
            --Outer Joins performed to account for all Items, Item sets that were unanswered or omitted --
         AND M.TD_ITEM_SET_ID = IR.ITEM_SET_ID(+)
         AND M.ITEM_ID = IR.ITEM_ID(+)
         AND (IR.RESPONSE_SEQ_NUM IS NULL OR
             IR.RESPONSE_SEQ_NUM =
             (SELECT MAX(IRM.RESPONSE_SEQ_NUM)
                 FROM ITEM_RESPONSE IRM
                WHERE M.TEST_ROSTER_ID = IRM.TEST_ROSTER_ID
                  AND M.TD_ITEM_SET_ID = IRM.ITEM_SET_ID
                  AND M.ITEM_ID = IRM.ITEM_ID
                GROUP BY M.TEST_ROSTER_ID, M.TD_ITEM_SET_ID, M.ITEM_ID))
       ORDER BY SI_ITEM_SET_ORDER,
          TD_ITEM_SET_SORT_ORDER,
          QUES_NO,
          M.TD_ITEM_SET_ID; /*Get the records order by subtest id*/

  --  Cursor to select Grided Responses
  CURSOR C_GI(IN_TEST_ROSTER_ID NUMBER, IN_STUDENT NUMBER, IN_TC_ID NUMBER) IS WITH MAIN AS(
    SELECT TR.TEST_ROSTER_ID AS TEST_ROSTER_ID,
           ISTD.ITEM_SET_ID TD_ITEM_SET_ID,
           I.ITEM_ID,
           ISP_TD_TS.ITEM_SET_SORT_ORDER TD_ITEM_SET_SORT_ORDER,
           SI.ITEM_SET_ORDER SI_ITEM_SET_ORDER,
           ITEM_SORT_ORDER,
           GDE.FORM_ELEMENT_ID, --FCAT Spring 2008 -- OLD Field i.external_id,
           I.GRIDDED_COLUMNS
      FROM TEST_ADMIN              TA,
           TEST_CATALOG            TC,
           ITEM_SET_ITEM           ISI,
           ITEM                    I,
           ITEM_SET                ISTD,
           ITEM_SET_PARENT         ISP_TD_TS,
           STUDENT_ITEM_SET_STATUS SI,
           TEST_ROSTER             TR,
           STUDENT                 ST,
           STG_PARAM_WINSCR_TB     PARM,
           ORG_NODE_STUDENT        ONS,
           GR_DELIVERY_ELEMENT     GDE --Table added for FCAT spring 2008
     WHERE 1 = 1
       AND I.ITEM_ID = GDE.ITEM_ID --FCAT Sprin 2008
       AND TR.FORM_ASSIGNMENT = GDE.FORM_ASSIGNMENT --FCAT Sprin 2008
       AND TA.PRODUCT_ID = TC.PRODUCT_ID
       AND TA.TEST_CATALOG_ID = TC.TEST_CATALOG_ID
       AND TR.TEST_ADMIN_ID = TA.TEST_ADMIN_ID
       AND TR.CUSTOMER_ID = TA.CUSTOMER_ID
       AND ISI.ITEM_SET_ID = ISTD.ITEM_SET_ID
       AND ISI.ITEM_ID = I.ITEM_ID
       AND ISTD.SAMPLE = 'F'
       AND ISTD.ITEM_SET_ID = ISP_TD_TS.ITEM_SET_ID
       AND TR.TEST_ROSTER_ID = SI.TEST_ROSTER_ID
       AND TR.STUDENT_ID = ST.STUDENT_ID
       AND SI.ITEM_SET_ID = ISTD.ITEM_SET_ID
       AND I.ITEM_TYPE = 'CR'
       AND I.ANSWER_AREA = 'GRID'
       AND TA.CUSTOMER_ID = PARM.CUSTOMER_ID
       AND TA.PRODUCT_ID = PARM.PRODUCT_ID
       AND TR.STUDENT_ID = IN_STUDENT
       AND TC.ITEM_SET_ID = IN_TC_ID
       AND ONS.STUDENT_ID = TR.STUDENT_ID
       AND ONS.ORG_NODE_ID = TR.ORG_NODE_ID
       AND TR.TEST_ROSTER_ID = IN_TEST_ROSTER_ID)
      SELECT FORM_ELEMENT_ID, -- FCAT Spring 2008 external_id,
       GRIDDED_COLUMNS,
       TD_ITEM_SET_ID,
       ITEM_SORT_ORDER AS QUES_NO,
       CASE
         WHEN CONSTRUCTED_RESPONSE IS NULL THEN
          1
         ELSE
          0
       END AS OMIT,
       CASE
         WHEN CONSTRUCTED_RESPONSE IS NULL THEN
          '     '
         ELSE
          DECODE_HTML_URL(TO_CHAR(CONSTRUCTED_RESPONSE))
       END AS RESPONSE
        FROM MAIN M, ITEM_RESPONSE_CR IRC
       WHERE M.TEST_ROSTER_ID = IRC.TEST_ROSTER_ID(+)
         AND M.TD_ITEM_SET_ID = IRC.ITEM_SET_ID(+)
         AND M.ITEM_ID = IRC.ITEM_ID(+)
       ORDER BY M.SI_ITEM_SET_ORDER,
          M.TD_ITEM_SET_SORT_ORDER,
          QUES_NO;


  FUNCTION GET_EA_RESP(P_TEST_ROSTER_ID   INTEGER,
                       P_ITEM_ID          VARCHAR2,
                       P_ACT_RESP         VARCHAR2,
                       P_MAX_RESP_SEQ_NUM INTEGER) RETURN VARCHAR2 IS
    V_EA_RESP VARCHAR2(1);
  BEGIN
    SELECT DECODE(RESPONSE,
                  'A',
                  '1',
                  'B',
                  '2',
                  'C',
                  '3',
                  'D',
                  '4',
                  'E',
                  '5',
                  ' ') AS RESPONSE
      INTO V_EA_RESP
      FROM ITEM_RESPONSE
     WHERE RESPONSE_SEQ_NUM =
           (SELECT MAX(RESPONSE_SEQ_NUM)
              FROM ITEM_RESPONSE
             WHERE TEST_ROSTER_ID = P_TEST_ROSTER_ID
               AND ITEM_ID = P_ITEM_ID
               AND RESPONSE <> P_ACT_RESP
               AND RESPONSE_SEQ_NUM <> P_MAX_RESP_SEQ_NUM)
       AND TEST_ROSTER_ID = P_TEST_ROSTER_ID
       AND ITEM_ID = P_ITEM_ID;
    RETURN V_EA_RESP;

  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN ' ';
  END GET_EA_RESP;

  --Start actual execution of the procedure
BEGIN
  --Select ST records from STG_EISS_Tb table

  OPEN CUR_EXP_CR_DECISION;
  FETCH CUR_EXP_CR_DECISION
    INTO CURSOR_CR_DECISION;

  IF CUR_EXP_CR_DECISION%NOTFOUND THEN
    V_CR_DECISION := 'N';
  END IF;
  IF CUR_EXP_CR_DECISION%FOUND THEN
    V_CR_DECISION := CURSOR_CR_DECISION.VALUE;
  END IF;
  CLOSE CUR_EXP_CR_DECISION;

  IF (V_CR_DECISION = 'N') THEN
    FOR C_ST_REC IN C_ST LOOP
      --Initialize values before using them--
      V_RA  := NULL;
      V_SA  := NULL;
      V_EA  := NULL;
      V_SUM := 0;

      -- Select TS records for the given ST record --
      V_SUBJECT := '';
      
      -- To check the time taken by system
      nStartTime  :=  DBMS_UTILITY.GET_TIME;
      -- Select TD/RA, SA records for the given ST record --
      OPEN C_TD(C_ST_REC.TEST_ROSTER_ID,
                C_ST_REC.SR_ID,
                C_ST_REC.ST_ID

                );

      LOOP
       nEndTime  :=  DBMS_UTILITY.GET_TIME;
       --dbms_output.put_line(' V_SR_RESP_TAB.COUNT time taken in main' ||  (nEndTime - nStartTime));
        
       
        FETCH C_TD BULK COLLECT
          INTO V_SR_RESP_TAB;
           --nEndTime  :=  DBMS_UTILITY.GET_TIME;
          
        
        FOR QUESTION_NO IN 1 .. V_SR_RESP_TAB.COUNT LOOP
         
         -- To check the time taken by system
         
          nStartTime  :=  DBMS_UTILITY.GET_TIME;
          --Concatenate the response for that Item into the Response array -
          V_RA := V_RA || V_SR_RESP_TAB(QUESTION_NO).RESPONSE;
          --Concatenate the score for that Item into the Scoring array -
          V_SA := V_SA || V_SR_RESP_TAB(QUESTION_NO).SCORE;
          --Calculate the score of the student by adding the current score with the score obtained at that instance -
          V_SUM     := V_SUM + TO_NUMBER(V_SR_RESP_TAB(QUESTION_NO).SCORE);

          OPEN CUR_TEST_SECTION_NAME(C_ST_REC.CUSTOMER_ID,V_SR_RESP_TAB(QUESTION_NO).TD_SUBJECT);
          FETCH CUR_TEST_SECTION_NAME INTO V_SUBJECT;

          IF CUR_TEST_SECTION_NAME%NOTFOUND THEN
             V_SUBJECT:= 'TS NAME NOT FOUND';
          END IF;

          CLOSE  CUR_TEST_SECTION_NAME;

          --V_SUBJECT := V_SR_RESP_TAB(QUESTION_NO).TD_SUBJECT;

          IF V_SR_RESP_TAB(QUESTION_NO).RESPONSE_SEQ_NUM IS NULL THEN
            V_EA := V_EA || ' ';
          ELSE
            V_EA := V_EA ||
                    GET_EA_RESP(C_ST_REC.TEST_ROSTER_ID,
                                V_SR_RESP_TAB(QUESTION_NO).ITEM_ID,
                                V_SR_RESP_TAB(QUESTION_NO).ACTUAL_RESPONSE,
                                V_SR_RESP_TAB(QUESTION_NO).RESPONSE_SEQ_NUM);
          END IF;

          IF (QUESTION_NO < V_SR_RESP_TAB.COUNT) THEN
            /* This check is to avoid index out of bound
            exception when the question no value  is the size of v_sr_resp_tab.COUNT */
             
            IF (V_SR_RESP_TAB(QUESTION_NO)
               .TD_ITEM_SET_ID != V_SR_RESP_TAB(QUESTION_NO + 1)
               .TD_ITEM_SET_ID) THEN
              /* This check is to comapre subtest_id of the first record with the same of  next record.If different then inserts the ts ,ea,ra,sa record,
              If not same then the loop will continue and the variable v_ea,v_ra,v_sa,v_sum will be Added with value of the next record
              If different the value of these variable will be initialized*/

              V_GLOBAL_TS_ID := V_SR_RESP_TAB(QUESTION_NO + 1)
                               .TD_ITEM_SET_ID; /* In different case the value of subtest_id will be set in v_globa_ts_id variable.It is used when the last record of the loop will come */

              -- calculate erasure count for sub test
              V_ERASURE_COUNT := ERASURE_COUNT(C_ST_REC.TEST_ROSTER_ID,
                                               V_SR_RESP_TAB(QUESTION_NO)
                                               .TD_ITEM_SET_ID);

              -- Insert the TS record along with the concatenated data into STG_EISS_TB table --
              INSERT INTO STG_EISS_TB
                (CUSTOMER_ID,
                 PRODUCT_ID,
                 TEST_ROSTER_ID,
                 OU_ID,
                 SH_ID,
                 GH_ID,
                 SR_ID,
                 ST_ID,
                 TS_ID,
                 GRADE,
                 SORT_ORDER,
                 LEVEL_TYPE,
                 LEVEL_DATA)
              VALUES
                (C_ST_REC.CUSTOMER_ID,
                 C_ST_REC.PRODUCT_ID,
                 C_ST_REC.TEST_ROSTER_ID,
                 C_ST_REC.OU_ID,
                 C_ST_REC.SH_ID,
                 C_ST_REC.GH_ID,
                 C_ST_REC.SR_ID,
                 C_ST_REC.ST_ID,
                 V_SR_RESP_TAB(QUESTION_NO).TD_ITEM_SET_ID, /*insert the subtest_id*/
                 C_ST_REC.GRADE,
                 V_SR_RESP_TAB(QUESTION_NO).TS_ORDER,
                 'TS',
                 ('TS ' || V_SUBJECT || ',' || V_SECTTYP || ',' ||
                 TO_CHAR(V_SUM) || ',' || V_INVFLAG || ',' || V_SUPPFLAG || ',' ||
                 V_OMITFLAG || ',' || V_FLIPFLAG || ',' || V_ERASURE_COUNT

                 ));

              -- Insert the RA record along with the concatenated data into STG_EISS_TB table --

              INSERT INTO STG_EISS_TB
                (CUSTOMER_ID,
                 PRODUCT_ID,
                 TEST_ROSTER_ID,
                 OU_ID,
                 SH_ID,
                 GH_ID,
                 SR_ID,
                 ST_ID,
                 TS_ID,
                 GRADE,
                 SORT_ORDER,
                 LEVEL_TYPE,
                 LEVEL_DATA)
              VALUES
                (C_ST_REC.CUSTOMER_ID,
                 C_ST_REC.PRODUCT_ID,
                 C_ST_REC.TEST_ROSTER_ID,
                 C_ST_REC.OU_ID,
                 C_ST_REC.SH_ID,
                 C_ST_REC.GH_ID,
                 C_ST_REC.SR_ID,
                 C_ST_REC.ST_ID,
                 V_SR_RESP_TAB(QUESTION_NO).TD_ITEM_SET_ID, /*insert the subtest_id*/
                 C_ST_REC.GRADE,
                 '4',
                 'TD',
                 ('RA ' || V_RA));

              -- Insert the SA record along with the concatenated data into STG_EISS_TB table --
              INSERT INTO STG_EISS_TB
                (CUSTOMER_ID,
                 PRODUCT_ID,
                 TEST_ROSTER_ID,
                 OU_ID,
                 SH_ID,
                 GH_ID,
                 SR_ID,
                 ST_ID,
                 TS_ID,
                 GRADE,
                 SORT_ORDER,
                 LEVEL_TYPE,
                 LEVEL_DATA)
              VALUES
                (C_ST_REC.CUSTOMER_ID,
                 C_ST_REC.PRODUCT_ID,
                 C_ST_REC.TEST_ROSTER_ID,
                 C_ST_REC.OU_ID,
                 C_ST_REC.SH_ID,
                 C_ST_REC.GH_ID,
                 C_ST_REC.SR_ID,
                 C_ST_REC.ST_ID,
                 V_SR_RESP_TAB(QUESTION_NO).TD_ITEM_SET_ID, /*insert the subtest_id*/
                 C_ST_REC.GRADE,
                 '5',
                 'TD',
                 ('SA ' || V_SA));

              -- Insert the EA record along with the concatenated data into STG_EISS_TB table --
              INSERT INTO STG_EISS_TB
                (CUSTOMER_ID,
                 PRODUCT_ID,
                 TEST_ROSTER_ID,
                 OU_ID,
                 SH_ID,
                 GH_ID,
                 SR_ID,
                 ST_ID,
                 TS_ID,
                 GRADE,
                 SORT_ORDER,
                 LEVEL_TYPE,
                 LEVEL_DATA)
              VALUES
                (C_ST_REC.CUSTOMER_ID,
                 C_ST_REC.PRODUCT_ID,
                 C_ST_REC.TEST_ROSTER_ID,
                 C_ST_REC.OU_ID,
                 C_ST_REC.SH_ID,
                 C_ST_REC.GH_ID,
                 C_ST_REC.SR_ID,
                 C_ST_REC.ST_ID,
                 V_SR_RESP_TAB(QUESTION_NO).TD_ITEM_SET_ID, /*insert the subtest_id*/
                 C_ST_REC.GRADE,
                 '6',
                 'TD',
                 ('EA ' || V_EA));

              V_RA      := NULL; /* Initialize these varaible with default value*/
              V_SA      := NULL;
              V_EA      := NULL;
              V_SUM     := 0;
              V_SUBJECT := '';

            END IF; -- end of first inner if block

          ELSE
            -- calculate erasure count for sub test
            V_ERASURE_COUNT := ERASURE_COUNT(C_ST_REC.TEST_ROSTER_ID,
                                             V_SR_RESP_TAB(QUESTION_NO)
                                             .TD_ITEM_SET_ID);

            IF (V_GLOBAL_TS_ID = V_SR_RESP_TAB(QUESTION_NO).TD_ITEM_SET_ID) THEN
              /*When last record comes it checks whether the last subtest id is already parsed or not.By comparing with v_global_ts_id.
              If parsed, then the records are inserted*/

              INSERT INTO STG_EISS_TB
                (CUSTOMER_ID,
                 PRODUCT_ID,
                 TEST_ROSTER_ID,
                 OU_ID,
                 SH_ID,
                 GH_ID,
                 SR_ID,
                 ST_ID,
                 TS_ID,
                 GRADE,
                 SORT_ORDER,
                 LEVEL_TYPE,
                 LEVEL_DATA --,opunit
                 )
              VALUES
                (C_ST_REC.CUSTOMER_ID,
                 C_ST_REC.PRODUCT_ID,
                 C_ST_REC.TEST_ROSTER_ID,
                 C_ST_REC.OU_ID,
                 C_ST_REC.SH_ID,
                 C_ST_REC.GH_ID,
                 C_ST_REC.SR_ID,
                 C_ST_REC.ST_ID,
                 V_SR_RESP_TAB(QUESTION_NO).TD_ITEM_SET_ID, --v_global_ts_id,
                 C_ST_REC.GRADE,
                 V_SR_RESP_TAB(QUESTION_NO).TS_ORDER,
                 'TS',
                 ('TS ' || V_SUBJECT || ',' || V_SECTTYP || ',' ||
                 TO_CHAR(V_SUM) || ',' || V_INVFLAG || ',' || V_SUPPFLAG || ',' ||
                 V_OMITFLAG || ',' || V_FLIPFLAG || ',' || V_ERASURE_COUNT

                 ) /*,
                                                                  c_st_rec.opunit*/);

              -- Insert the RA record along with the concatenated data into STG_EISS_TB table --

              INSERT INTO STG_EISS_TB
                (CUSTOMER_ID,
                 PRODUCT_ID,
                 TEST_ROSTER_ID,
                 OU_ID,
                 SH_ID,
                 GH_ID,
                 SR_ID,
                 ST_ID,
                 TS_ID,
                 GRADE,
                 SORT_ORDER,
                 LEVEL_TYPE,
                 LEVEL_DATA --,opunit
                 )
              VALUES
                (C_ST_REC.CUSTOMER_ID,
                 C_ST_REC.PRODUCT_ID,
                 C_ST_REC.TEST_ROSTER_ID,
                 C_ST_REC.OU_ID,
                 C_ST_REC.SH_ID,
                 C_ST_REC.GH_ID,
                 C_ST_REC.SR_ID,
                 C_ST_REC.ST_ID,
                 V_SR_RESP_TAB(QUESTION_NO).TD_ITEM_SET_ID, --v_global_ts_id,
                 C_ST_REC.GRADE,
                 '4',
                 'TD',
                 ('RA ' || V_RA) /*,c_st_rec.opunit*/);

              -- Insert the SA record along with the concatenated data into STG_EISS_TB table --
              INSERT INTO STG_EISS_TB
                (CUSTOMER_ID,
                 PRODUCT_ID,
                 TEST_ROSTER_ID,
                 OU_ID,
                 SH_ID,
                 GH_ID,
                 SR_ID,
                 ST_ID,
                 TS_ID,
                 GRADE,
                 SORT_ORDER,
                 LEVEL_TYPE,
                 LEVEL_DATA --,opunit
                 )
              VALUES
                (C_ST_REC.CUSTOMER_ID,
                 C_ST_REC.PRODUCT_ID,
                 C_ST_REC.TEST_ROSTER_ID,
                 C_ST_REC.OU_ID,
                 C_ST_REC.SH_ID,
                 C_ST_REC.GH_ID,
                 C_ST_REC.SR_ID,
                 C_ST_REC.ST_ID,
                 V_SR_RESP_TAB(QUESTION_NO).TD_ITEM_SET_ID, --v_global_ts_id,
                 C_ST_REC.GRADE,
                 '5',
                 'TD',
                 ('SA ' || V_SA) /*,c_st_rec.opunit*/);

              -- Insert the EA record along with the concatenated data into STG_EISS_TB table --
              INSERT INTO STG_EISS_TB
                (CUSTOMER_ID,
                 PRODUCT_ID,
                 TEST_ROSTER_ID,
                 OU_ID,
                 SH_ID,
                 GH_ID,
                 SR_ID,
                 ST_ID,
                 TS_ID,
                 GRADE,
                 SORT_ORDER,
                 LEVEL_TYPE,
                 LEVEL_DATA --,opunit
                 )
              VALUES
                (C_ST_REC.CUSTOMER_ID,
                 C_ST_REC.PRODUCT_ID,
                 C_ST_REC.TEST_ROSTER_ID,
                 C_ST_REC.OU_ID,
                 C_ST_REC.SH_ID,
                 C_ST_REC.GH_ID,
                 C_ST_REC.SR_ID,
                 C_ST_REC.ST_ID,
                 V_SR_RESP_TAB(QUESTION_NO).TD_ITEM_SET_ID, --v_global_ts_id,
                 C_ST_REC.GRADE,
                 '6',
                 'TD',
                 ('EA ' || V_EA));

            ELSE
              /*When last record comes it checks whether the last subtest id is already parsed or not.By comparing with v_global_ts_id.
              If Not parsed previously , initialse the variables with default, set the variable and then the records are inserted*/

              V_RA      := NULL;
              V_SA      := NULL;
              V_EA      := NULL;
              V_SUM     := 0;
              V_SUBJECT := '';

              --Concatenate the response for that Item into the Response array -
              V_RA := V_RA || V_SR_RESP_TAB(QUESTION_NO).RESPONSE;
              --Concatenate the score for that Item into the Scoring array -
              V_SA := V_SA || V_SR_RESP_TAB(QUESTION_NO).SCORE;
              --Calculate the score of the student by adding the current score with the score obtained at that instance -
              V_SUM     := V_SUM +
                           TO_NUMBER(V_SR_RESP_TAB(QUESTION_NO).SCORE);
              OPEN CUR_TEST_SECTION_NAME(C_ST_REC.CUSTOMER_ID,V_SR_RESP_TAB(QUESTION_NO).TD_SUBJECT);
              FETCH CUR_TEST_SECTION_NAME INTO V_SUBJECT;

              IF CUR_TEST_SECTION_NAME%NOTFOUND THEN
                 V_SUBJECT:= 'TS NAME NOT FOUND';
              END IF;

              CLOSE  CUR_TEST_SECTION_NAME;
              --V_SUBJECT := V_SR_RESP_TAB(QUESTION_NO).TD_SUBJECT;

              IF V_SR_RESP_TAB(QUESTION_NO).RESPONSE_SEQ_NUM IS NULL THEN
                V_EA := V_EA || ' ';
              ELSE
                V_EA := V_EA ||
                        GET_EA_RESP(C_ST_REC.TEST_ROSTER_ID,
                                    V_SR_RESP_TAB(QUESTION_NO).ITEM_ID,
                                    V_SR_RESP_TAB(QUESTION_NO)
                                    .ACTUAL_RESPONSE,
                                    V_SR_RESP_TAB(QUESTION_NO)
                                    .RESPONSE_SEQ_NUM);
              END IF;

              INSERT INTO STG_EISS_TB
                (CUSTOMER_ID,
                 PRODUCT_ID,
                 TEST_ROSTER_ID,
                 OU_ID,
                 SH_ID,
                 GH_ID,
                 SR_ID,
                 ST_ID,
                 TS_ID,
                 GRADE,
                 SORT_ORDER,
                 LEVEL_TYPE,
                 LEVEL_DATA --,opunit
                 )
              VALUES
                (C_ST_REC.CUSTOMER_ID,
                 C_ST_REC.PRODUCT_ID,
                 C_ST_REC.TEST_ROSTER_ID,
                 C_ST_REC.OU_ID,
                 C_ST_REC.SH_ID,
                 C_ST_REC.GH_ID,
                 C_ST_REC.SR_ID,
                 C_ST_REC.ST_ID,
                 V_SR_RESP_TAB(QUESTION_NO).TD_ITEM_SET_ID, --v_global_ts_id,
                 C_ST_REC.GRADE,
                 V_SR_RESP_TAB(QUESTION_NO).TS_ORDER,
                 'TS',
                 ('TS ' || V_SUBJECT || ',' || V_SECTTYP || ',' ||
                 TO_CHAR(V_SUM) || ',' || V_INVFLAG || ',' || V_SUPPFLAG || ',' ||
                 V_OMITFLAG || ',' || V_FLIPFLAG || ',' || V_ERASURE_COUNT

                 ));

              -- Insert the RA record along with the concatenated data into STG_EISS_TB table --

              INSERT INTO STG_EISS_TB
                (CUSTOMER_ID,
                 PRODUCT_ID,
                 TEST_ROSTER_ID,
                 OU_ID,
                 SH_ID,
                 GH_ID,
                 SR_ID,
                 ST_ID,
                 TS_ID,
                 GRADE,
                 SORT_ORDER,
                 LEVEL_TYPE,
                 LEVEL_DATA)
              VALUES
                (C_ST_REC.CUSTOMER_ID,
                 C_ST_REC.PRODUCT_ID,
                 C_ST_REC.TEST_ROSTER_ID,
                 C_ST_REC.OU_ID,
                 C_ST_REC.SH_ID,
                 C_ST_REC.GH_ID,
                 C_ST_REC.SR_ID,
                 C_ST_REC.ST_ID,
                 V_SR_RESP_TAB(QUESTION_NO).TD_ITEM_SET_ID,
                 C_ST_REC.GRADE,
                 '4',
                 'TD',
                 ('RA ' || V_RA));

              -- Insert the SA record along with the concatenated data into STG_EISS_TB table --
              INSERT INTO STG_EISS_TB
                (CUSTOMER_ID,
                 PRODUCT_ID,
                 TEST_ROSTER_ID,
                 OU_ID,
                 SH_ID,
                 GH_ID,
                 SR_ID,
                 ST_ID,
                 TS_ID,
                 GRADE,
                 SORT_ORDER,
                 LEVEL_TYPE,
                 LEVEL_DATA --,opunit
                 )
              VALUES
                (C_ST_REC.CUSTOMER_ID,
                 C_ST_REC.PRODUCT_ID,
                 C_ST_REC.TEST_ROSTER_ID,
                 C_ST_REC.OU_ID,
                 C_ST_REC.SH_ID,
                 C_ST_REC.GH_ID,
                 C_ST_REC.SR_ID,
                 C_ST_REC.ST_ID,
                 V_SR_RESP_TAB(QUESTION_NO).TD_ITEM_SET_ID,
                 C_ST_REC.GRADE,
                 '5',
                 'TD',
                 ('SA ' || V_SA));

              -- Insert the EA record along with the concatenated data into STG_EISS_TB table --
              INSERT INTO STG_EISS_TB
                (CUSTOMER_ID,
                 PRODUCT_ID,
                 TEST_ROSTER_ID,
                 OU_ID,
                 SH_ID,
                 GH_ID,
                 SR_ID,
                 ST_ID,
                 TS_ID,
                 GRADE,
                 SORT_ORDER,
                 LEVEL_TYPE,
                 LEVEL_DATA)
              VALUES
                (C_ST_REC.CUSTOMER_ID,
                 C_ST_REC.PRODUCT_ID,
                 C_ST_REC.TEST_ROSTER_ID,
                 C_ST_REC.OU_ID,
                 C_ST_REC.SH_ID,
                 C_ST_REC.GH_ID,
                 C_ST_REC.SR_ID,
                 C_ST_REC.ST_ID,
                 V_SR_RESP_TAB(QUESTION_NO).TD_ITEM_SET_ID,
                 C_ST_REC.GRADE,
                 '6',
                 'TD',
                 ('EA ' || V_EA));

            END IF; /* End of if block in else part */
          END IF; /* End of first if outer block*/
          
          -- To end the time taken by one loop
          nEndTime  :=  DBMS_UTILITY.GET_TIME;
          
          --dbms_output.put_line('time taken in one loop' ||  (nEndTime - nStartTime));
          
        END LOOP; /* End of inner loop of getting  v_sr_resp_tab */

        EXIT WHEN C_TD%NOTFOUND;
      END LOOP; /* End of inner loop of getting  c_td */

      COMMIT;

      CLOSE C_TD;

      -- GI record processing --
      OPEN C_GI(C_ST_REC.TEST_ROSTER_ID, C_ST_REC.SR_ID, C_ST_REC.ST_ID);

      LOOP
        FETCH C_GI BULK COLLECT
          INTO V_GI_RESP_TAB;

        FOR QUESTION_NO IN 1 .. V_GI_RESP_TAB.COUNT LOOP
          -- Insert GI record
          INSERT INTO STG_EISS_TB
            (CUSTOMER_ID,
             PRODUCT_ID,
             TEST_ROSTER_ID,
             OU_ID,
             SH_ID,
             GH_ID,
             SR_ID,
             ST_ID,
             TS_ID,
             GRADE,
             SORT_ORDER,
             LEVEL_TYPE,
             LEVEL_DATA

             )
          VALUES
            (C_ST_REC.CUSTOMER_ID,
             C_ST_REC.PRODUCT_ID,
             C_ST_REC.TEST_ROSTER_ID,
             C_ST_REC.OU_ID,
             C_ST_REC.SH_ID,
             C_ST_REC.GH_ID,
             C_ST_REC.SR_ID,
             C_ST_REC.ST_ID,
             V_GI_RESP_TAB(QUESTION_NO).TD_ITEM_SET_ID,
             C_ST_REC.GRADE,
             '7' || TO_CHAR(V_GI_RESP_TAB(QUESTION_NO).QUES_NO, '099'),
             'GI',
             'GI ' || V_GI_RESP_TAB(QUESTION_NO)
             .FORM_ELEMENT_ID --FCAT Spring 2008        --old field .external_id
             || ',' || V_GI_RESP_TAB(QUESTION_NO)
             .RESPONSE || ',' || V_GI_RESP_TAB(QUESTION_NO)
             .QUES_NO || ',' || V_GI_RESP_TAB(QUESTION_NO)
             .RESPONSE || ',' || '0' || ',' || V_GI_RESP_TAB(QUESTION_NO)
             .GRIDDED_COLUMNS || ',' || V_GI_RESP_TAB(QUESTION_NO)
             .OMIT || ',' || '0'

             );
        END LOOP;

        EXIT WHEN C_GI%NOTFOUND;
      END LOOP;

      CLOSE C_GI;

      IF MOD(C_ST%ROWCOUNT, 1000) = 0 THEN
        COMMIT;
      END IF;
    END LOOP;

    COMMIT;

  END IF;
END SP_RESPONSE_OAS_EXPORT_ISTEP;
/
