CREATE OR REPLACE PROCEDURE SP_RESPONSE_OAS_EXPORT_GA IS
  --  THIS PROCEDURE IS USED TO LOAD THE CONCATENATED STRING OF ALL THE TS RECORDS,
  --    CONCATENATED STRING OF ALL THE RESPONSE ARRAYS AND SCORE ARRAYS FOR A PARTICULAR TEST CATALOG ITEM_SET_ID OF THE STUDENT --
  V_RA VARCHAR2(2000) := '';
  -- VARIABLE TO HOLD THE RESPONSE ARRAY --
  V_SA VARCHAR2(2000) := '';
  --VARIABLE TO HOLD THE SCORE ARRAY --
  V_EA VARCHAR2(2000) := '';
  --VARIABLE TO HOLD THE ERASURE ARRAY --
  V_SUM INTEGER := 0;
  --VARIABLE TO HOLD THE SCORE OF THE STUDENT --
  --   V_TS_ID         INTEGER         := 0;
  --VARIABLE TO HOLD THE TEST SECTION ID --
  V_SUBJECT VARCHAR2(100) := ''; --VARIABLE TO HOLD SUBJECT
  --   V_SUBJECT1     VARCHAR2 (2000) := '';         --VARIABLE TO HOLD SUBJECT
  V_SECTTYP VARCHAR2(2) := 'SR';
  -- VARIABLE TO HOLD SELECTION TYPE, BY DEFAULT IS 'SR" --
  V_INVFLAG  VARCHAR2(1) := '0'; -- VARIABLE TO HOLD INVFLAG --
  V_SUPPFLAG VARCHAR2(1) := '0'; -- VARIABLE TO HOLD SUPPFLAG --
  V_OMITFLAG VARCHAR2(1) := '0'; -- VARIABLE TO HOLD OMITFLAG --
  V_FLIPFLAG VARCHAR2(1) := '0'; -- VARIABLE TO HOLD FLIPFLAG --
  --V_ERASURECNT    VARCHAR2 (1);
  V_ERASURE_COUNT NUMBER := 0;
  V_GLOBAL_TS_ID  INTEGER := 0;
  V_CR_DECISION   VARCHAR2(1);

  -- := '0';  -- VARIABLE TO HOLD ERASURECNT --
  TYPE SR_RESP_REC IS RECORD(
    TD_SUBJECT       VARCHAR2(60),
    QUES_NO          INTEGER,
    RESPONSE         VARCHAR2(1),
    CORRECT_ANSWER   VARCHAR2(1),
    SCORE            VARCHAR2(1),
    RESPONSE_SEQ_NUM INTEGER,
    ITEM_ID          VARCHAR2(32),
    ACTUAL_RESPONSE  VARCHAR2(1),
    TD_ITEM_SET_ID   INTEGER, ----/* ADDED TO GET THE VALUE FROM CURSOR C_TD .IT IS USED TO INSERT SUBTEST ID IN THE TS_ID COLUMN IN STG_EISS_TB */
    TS_ORDER         INTEGER,
    TS_ID            INTEGER);/*The ‘TS” records need to aling with the content areas and not TD item sets.*/

  TYPE SR_RESP_TAB IS TABLE OF SR_RESP_REC INDEX BY BINARY_INTEGER;

  V_SR_RESP_TAB SR_RESP_TAB;

  TYPE GI_RESP_REC IS RECORD(
    FORM_ELEMENT_ID VARCHAR2(30), --FCAT  SPRING 2008  --- OLD FIELD EXTERNAL_ID       VARCHAR2 (30),
    GRIDDED_COLUMNS INTEGER,
    TD_ITEM_SET_ID  INTEGER,
    QUES_NO         INTEGER,
    OMIT            INTEGER,
    RESPONSE        VARCHAR2(10));

  TYPE GI_RESP_TAB IS TABLE OF GI_RESP_REC INDEX BY BINARY_INTEGER;

  V_GI_RESP_TAB GI_RESP_TAB;

  -- CURSOR TO SELECT ALL ST RECORDS IN STG_EISS_TB
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
           --ERASURE_COUNT(TEST_ROSTER_ID) ERASURE_CNT,
           OPUNIT
      FROM STG_EISS_TB
     WHERE LEVEL_TYPE = 'ST';

  --  CURSOR TO SELECT ALL TD/RA,SA RECORDS

  --CURSOR FOR STG_EXPORT_PARMS TABLE
  CURSOR CUR_EXP_CR_DECISION IS
    SELECT VALUE
    
      FROM OAS_STAGE.STG_EXPORT_PARMS PARAM, STG_PARAM_WINSCR_TB STG
     WHERE UPPER(PARAM.PARAMETER) = 'CR_DECISION'
       AND PARAM.CUSTOMER_ID = STG.CUSTOMER_ID;

  --CURSOR ROWTYPE DECLARATION
  CURSOR_CR_DECISION CUR_EXP_CR_DECISION%ROWTYPE;

  --cursor for test section name *** added for GA
  CURSOR CUR_TEST_SECTION_NAME(P_CUSTOMER_ID INTEGER, P_OAS_TS_NAME VARCHAR2) IS
    SELECT VALUE
      FROM OAS_STAGE.STG_EXPORT_PARMS STG
     WHERE STG.CUSTOMER_ID = P_CUSTOMER_ID
       AND STG.PARAMETER = P_OAS_TS_NAME;

  CURSOR C_TD(IN_TEST_ROSTER_ID NUMBER, IN_STUDENT_ID NUMBER, IN_TC_ITEM_SET_ID NUMBER) IS WITH MAIN AS(
  --EXTRACT ALL THE ITEM_SET_IDS, ITEM_IDS ALONG WITH THEIR CORRECT ANSWERS AND ORDER THEM AS TO HOW THEY WERE ORDERED AT THE TEST
    SELECT TR.TEST_ROSTER_ID AS TEST_ROSTER_ID,
           ISTD.ITEM_SET_ID TD_ITEM_SET_ID,
           I.ITEM_ID,
           --ISTD.ITEM_SET_NAME TD_SUBJECT,
           ISTD.SUBJECT TD_SUBJECT,
           CORRECT_ANSWER,
           ITEM_SORT_ORDER,
           ISP.ITEM_SET_SORT_ORDER TD_ITEM_SET_SORT_ORDER,
           SI.ITEM_SET_ORDER SI_ITEM_SET_ORDER,
           ISP.PARENT_ITEM_SET_ID TS_ID /*The ‘TS” records need to aling with the content areas and not TD item sets.*/
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
          -- IN_TS_ID
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
       SI_ITEM_SET_ORDER AS TS_ORDER,
       M.TS_ID /* M.TD_ITEM_SET_ID IS ADDED TO GET THE SUBTEST_ID FROM MAIN VIRTUAL TABLE*/
        FROM MAIN M, ITEM_RESPONSE IR
       WHERE M.TEST_ROSTER_ID = IR.TEST_ROSTER_ID(+)
            --OUTER JOINS PERFORMED TO ACCOUNT FOR ALL ITEMS, ITEM SETS THAT WERE UNANSWERED OR OMITTED --
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
          M.TS_ID,/*The ‘TS” records need to aling with the content areas and not TD item sets.*/
          M.TD_ITEM_SET_ID; /*GET THE RECORDS ORDER BY SUBTEST ID*/

  --  CURSOR TO SELECT GRIDED RESPONSES
  CURSOR C_GI(IN_TEST_ROSTER_ID NUMBER, IN_STUDENT NUMBER, IN_TC_ID NUMBER) IS WITH MAIN AS(
    SELECT TR.TEST_ROSTER_ID AS TEST_ROSTER_ID,
           ISTD.ITEM_SET_ID TD_ITEM_SET_ID,
           I.ITEM_ID,
           ISP_TD_TS.ITEM_SET_SORT_ORDER TD_ITEM_SET_SORT_ORDER,
           SI.ITEM_SET_ORDER SI_ITEM_SET_ORDER,
           ITEM_SORT_ORDER,
           GDE.FORM_ELEMENT_ID, --FCAT SPRING 2008 -- OLD FIELD I.EXTERNAL_ID,
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
           GR_DELIVERY_ELEMENT     GDE --TABLE ADDED FOR FCAT SPRING 2008
     WHERE 1 = 1
       AND I.ITEM_ID = GDE.ITEM_ID --FCAT SPRING 2008
       AND TR.FORM_ASSIGNMENT = GDE.FORM_ASSIGNMENT --FCAT SPRING 2008
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
      SELECT FORM_ELEMENT_ID, -- FCAT SPRING 2008 EXTERNAL_ID,
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

  --START ACTUAL EXECUTION OF THE PROCEDURE
BEGIN
  --SELECT ST RECORDS FROM STG_EISS_TB TABLE

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
      --INITIALIZE VALUES BEFORE USING THEM--
      V_RA  := NULL;
      V_SA  := NULL;
      V_EA  := NULL;
      V_SUM := 0;
    
      -- SELECT TS RECORDS FOR THE GIVEN ST RECORD --
      V_SUBJECT := '';
    
      -- SELECT TD/RA, SA RECORDS FOR THE GIVEN ST RECORD --
      OPEN C_TD(C_ST_REC.TEST_ROSTER_ID,
                C_ST_REC.SR_ID,
                C_ST_REC.ST_ID
                
                );
    
      LOOP
        FETCH C_TD BULK COLLECT
          INTO V_SR_RESP_TAB;
      
        FOR QUESTION_NO IN 1 .. V_SR_RESP_TAB.COUNT LOOP
          --CONCATENATE THE RESPONSE FOR THAT ITEM INTO THE RESPONSE ARRAY -
          V_RA := V_RA || V_SR_RESP_TAB(QUESTION_NO).RESPONSE;
          --CONCATENATE THE SCORE FOR THAT ITEM INTO THE SCORING ARRAY -
          V_SA := V_SA || V_SR_RESP_TAB(QUESTION_NO).SCORE;
          --CALCULATE THE SCORE OF THE STUDENT BY ADDING THE CURRENT SCORE WITH THE SCORE OBTAINED AT THAT INSTANCE -
          V_SUM     := V_SUM + TO_NUMBER(V_SR_RESP_TAB(QUESTION_NO).SCORE);
          V_SUBJECT := V_SR_RESP_TAB(QUESTION_NO).TD_SUBJECT;
        
          /* OPEN CUR_TEST_SECTION_NAME(C_ST_REC.CUSTOMER_ID,V_SR_RESP_TAB(QUESTION_NO).TD_SUBJECT);
          FETCH CUR_TEST_SECTION_NAME INTO V_SUBJECT;
          
          IF CUR_TEST_SECTION_NAME%NOTFOUND THEN
             V_SUBJECT:= 'TS NAME NOT FOUND';
          END IF;
          
          CLOSE  CUR_TEST_SECTION_NAME;*/
        
          IF V_SR_RESP_TAB(QUESTION_NO).RESPONSE_SEQ_NUM IS NULL THEN
            V_EA := V_EA || ' ';
          ELSE
            V_EA := V_EA ||
                    GET_EA_RESP(C_ST_REC.TEST_ROSTER_ID,
                                V_SR_RESP_TAB(QUESTION_NO).ITEM_ID,
                                V_SR_RESP_TAB(QUESTION_NO).ACTUAL_RESPONSE,
                                V_SR_RESP_TAB(QUESTION_NO).RESPONSE_SEQ_NUM);
          END IF;
        
          V_ERASURE_COUNT := erasure_count_GA(C_ST_REC.TEST_ROSTER_ID,
                                           V_SR_RESP_TAB(QUESTION_NO)
                                           .TS_ID);
        
          IF (QUESTION_NO < V_SR_RESP_TAB.COUNT) THEN
            /* THIS CHECK IS TO AVOID INDEX OUT OF BOUND 
            EXCEPTION WHEN THE QUESTION NO VALUE  IS THE SIZE OF V_SR_RESP_TAB.COUNT */
            V_GLOBAL_TS_ID := V_SR_RESP_TAB(QUESTION_NO).TS_ID;
            IF (V_SR_RESP_TAB(QUESTION_NO)
               .TS_ID != V_SR_RESP_TAB(QUESTION_NO + 1).TS_ID) THEN
              /* THIS CHECK IS TO COMAPRE SUBTEST_ID OF THE FIRST RECORD WITH THE SAME OF  NEXT RECORD.IF DIFFERENT THEN INSERTS THE TS ,EA,RA,SA RECORD,   
              IF NOT SAME THEN THE LOOP WILL CONTINUE AND THE VARIABLE V_EA,V_RA,V_SA,V_SUM WILL BE ADDED WITH VALUE OF THE NEXT RECORD 
              IF DIFFERENT THE VALUE OF THESE VARIABLE WILL BE INITIALIZED*/
            
              V_GLOBAL_TS_ID := V_SR_RESP_TAB(QUESTION_NO + 1).TS_ID; /* IN DIFFERENT CASE THE VALUE OF SUBTEST_ID WILL BE SET IN V_GLOBA_TS_ID VARIABLE.IT IS USED WHEN THE LAST RECORD OF THE LOOP WILL COME */
           
              --V_ERASURE_COUNT := erasure_count(C_ST_REC.TEST_ROSTER_ID,V_SR_RESP_TAB(QUESTION_NO).TD_ITEM_SET_ID); 
            
              -- INSERT THE TS RECORD ALONG WITH THE CONCATENATED DATA INTO STG_EISS_TB TABLE --
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
                 V_SR_RESP_TAB(QUESTION_NO).TS_ID, /*INSERT THE SUBTEST_ID*/
                 C_ST_REC.GRADE,
                 V_SR_RESP_TAB(QUESTION_NO).TS_ORDER,
                 'TS',
                 ('TS ' || V_SUBJECT || ',' || V_SECTTYP || ',' ||
                 TO_CHAR(V_SUM) || ',' || V_INVFLAG || ',' || V_SUPPFLAG || ',' ||
                 V_OMITFLAG || ',' || V_FLIPFLAG || ',' || V_ERASURE_COUNT --C_ST_REC.ERASURE_CNT
                 
                 ));
            
              -- INSERT THE RA RECORD ALONG WITH THE CONCATENATED DATA INTO STG_EISS_TB TABLE --
            
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
                 V_SR_RESP_TAB(QUESTION_NO).TS_ID, /*INSERT THE SUBTEST_ID*/
                 C_ST_REC.GRADE,
                 '4',
                 'TD',
                 ('RA ' || V_RA));
            
              -- INSERT THE SA RECORD ALONG WITH THE CONCATENATED DATA INTO STG_EISS_TB TABLE --
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
                 V_SR_RESP_TAB(QUESTION_NO).TS_ID, /*INSERT THE SUBTEST_ID*/
                 C_ST_REC.GRADE,
                 '5',
                 'TD',
                 ('SA ' || V_SA));
            
              -- INSERT THE EA RECORD ALONG WITH THE CONCATENATED DATA INTO STG_EISS_TB TABLE --
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
                 V_SR_RESP_TAB(QUESTION_NO).TS_ID, /*INSERT THE SUBTEST_ID*/
                 C_ST_REC.GRADE,
                 '6',
                 'TD',
                 ('EA ' || V_EA));
            
              V_RA      := NULL; /* INITIALIZE THESE VARAIBLE WITH DEFAULT VALUE*/
              V_SA      := NULL;
              V_EA      := NULL;
              V_SUM     := 0;
              V_SUBJECT := '';
            
            END IF; -- END OF FIRST INNER IF BLOCK
          
          ELSE
          
            -- calculate erasure count for sub test
            --V_ERASURE_COUNT := V_ERASURE_COUNT + erasure_count(C_ST_REC.TEST_ROSTER_ID,V_SR_RESP_TAB(QUESTION_NO).TD_ITEM_SET_ID); 
          
            IF (V_GLOBAL_TS_ID = V_SR_RESP_TAB(QUESTION_NO).TS_ID) THEN
              /*WHEN LAST RECORD COMES IT CHECKS WHETHER THE LAST SUBTEST ID IS ALREADY PARSED OR NOT.BY COMPARING WITH V_GLOBAL_TS_ID.
              IF PARSED, THEN THE RECORDS ARE INSERTED*/
            
             
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
                 LEVEL_DATA --,OPUNIT
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
                 V_SR_RESP_TAB(QUESTION_NO).TS_ID, /*The ‘TS” records need to aling with the content areas and not TD item sets.*/
                 C_ST_REC.GRADE,
                 V_SR_RESP_TAB(QUESTION_NO).TS_ORDER,
                 'TS',
                 ('TS ' || V_SUBJECT || ',' || V_SECTTYP || ',' ||
                 TO_CHAR(V_SUM) || ',' || V_INVFLAG || ',' || V_SUPPFLAG || ',' ||
                 V_OMITFLAG || ',' || V_FLIPFLAG || ',' || V_ERASURE_COUNT --C_ST_REC.ERASURE_CNT
                 
                 ) /*,
                                                                  C_ST_REC.OPUNIT*/);
            
              -- INSERT THE RA RECORD ALONG WITH THE CONCATENATED DATA INTO STG_EISS_TB TABLE --
            
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
                 LEVEL_DATA --,OPUNIT
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
                 V_SR_RESP_TAB(QUESTION_NO).TS_ID, /*The ‘TS” records need to aling with the content areas and not TD item sets.*/
                 C_ST_REC.GRADE,
                 '4',
                 'TD',
                 ('RA ' || V_RA) /*,C_ST_REC.OPUNIT*/);
            
              -- INSERT THE SA RECORD ALONG WITH THE CONCATENATED DATA INTO STG_EISS_TB TABLE --
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
                 LEVEL_DATA --,OPUNIT
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
                 V_SR_RESP_TAB(QUESTION_NO).TS_ID, --V_GLOBAL_TS_ID,
                 C_ST_REC.GRADE,
                 '5',
                 'TD',
                 ('SA ' || V_SA) /*,C_ST_REC.OPUNIT*/);
            
              -- INSERT THE EA RECORD ALONG WITH THE CONCATENATED DATA INTO STG_EISS_TB TABLE --
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
                 LEVEL_DATA --,OPUNIT
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
                 V_SR_RESP_TAB(QUESTION_NO).TS_ID, --V_GLOBAL_TS_ID,
                 C_ST_REC.GRADE,
                 '6',
                 'TD',
                 ('EA ' || V_EA));
            
            ELSE
              /*WHEN LAST RECORD COMES IT CHECKS WHETHER THE LAST SUBTEST ID IS ALREADY PARSED OR NOT.BY COMPARING WITH V_GLOBAL_TS_ID.
              IF NOT PARSED PREVIOUSLY , INITIALSE THE VARIABLES WITH DEFAULT, SET THE VARIABLE AND THEN THE RECORDS ARE INSERTED*/
            
              V_RA  := NULL;
              V_SA  := NULL;
              V_EA  := NULL;
              V_SUM := 0;
              --V_SUBJECT := '';
            
              --CONCATENATE THE RESPONSE FOR THAT ITEM INTO THE RESPONSE ARRAY -
              V_RA := V_RA || V_SR_RESP_TAB(QUESTION_NO).RESPONSE;
              --CONCATENATE THE SCORE FOR THAT ITEM INTO THE SCORING ARRAY -
              V_SA := V_SA || V_SR_RESP_TAB(QUESTION_NO).SCORE;
              --CALCULATE THE SCORE OF THE STUDENT BY ADDING THE CURRENT SCORE WITH THE SCORE OBTAINED AT THAT INSTANCE -
              V_SUM     := V_SUM +
                           TO_NUMBER(V_SR_RESP_TAB(QUESTION_NO).SCORE);
              V_SUBJECT := V_SR_RESP_TAB(QUESTION_NO).TD_SUBJECT;
            
              V_ERASURE_COUNT := erasure_count_GA(C_ST_REC.TEST_ROSTER_ID,
                                               V_SR_RESP_TAB(QUESTION_NO)
                                               .TS_ID);
            
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
                 LEVEL_DATA --,OPUNIT
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
                 V_SR_RESP_TAB(QUESTION_NO).TS_ID, --V_GLOBAL_TS_ID,
                 C_ST_REC.GRADE,
                 V_SR_RESP_TAB(QUESTION_NO).TS_ORDER,
                 'TS',
                 ('TS ' || V_SUBJECT || ',' || V_SECTTYP || ',' ||
                 TO_CHAR(V_SUM) || ',' || V_INVFLAG || ',' || V_SUPPFLAG || ',' ||
                 V_OMITFLAG || ',' || V_FLIPFLAG || ',' || V_ERASURE_COUNT --C_ST_REC.ERASURE_CNT
                 
                 ));
            
              -- INSERT THE RA RECORD ALONG WITH THE CONCATENATED DATA INTO STG_EISS_TB TABLE --
            
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
                 V_SR_RESP_TAB(QUESTION_NO).TS_ID,
                 C_ST_REC.GRADE,
                 '4',
                 'TD',
                 ('RA ' || V_RA));
            
              -- INSERT THE SA RECORD ALONG WITH THE CONCATENATED DATA INTO STG_EISS_TB TABLE --
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
                 LEVEL_DATA --,OPUNIT
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
                 V_SR_RESP_TAB(QUESTION_NO).TS_ID,
                 C_ST_REC.GRADE,
                 '5',
                 'TD',
                 ('SA ' || V_SA));
            
              -- INSERT THE EA RECORD ALONG WITH THE CONCATENATED DATA INTO STG_EISS_TB TABLE --
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
                 V_SR_RESP_TAB(QUESTION_NO).TS_ID,
                 C_ST_REC.GRADE,
                 '6',
                 'TD',
                 ('EA ' || V_EA));
            
            END IF; /* END OF IF BLOCK IN ELSE PART */
          END IF; /* END OF FIRST IF OUTER BLOCK*/
        
        END LOOP; /* END OF INNER LOOP OF GETTING  V_SR_RESP_TAB */
      
        EXIT WHEN C_TD%NOTFOUND;
      END LOOP; /* END OF INNER LOOP OF GETTING  C_TD */
    
      COMMIT;
    
      CLOSE C_TD;
    
      -- GI RECORD PROCESSING --
      OPEN C_GI(C_ST_REC.TEST_ROSTER_ID, C_ST_REC.SR_ID, C_ST_REC.ST_ID);
    
      LOOP
        FETCH C_GI BULK COLLECT
          INTO V_GI_RESP_TAB;
      
        FOR QUESTION_NO IN 1 .. V_GI_RESP_TAB.COUNT LOOP
          -- INSERT GI RECORD
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
             .FORM_ELEMENT_ID --FCAT SPRING 2008        --OLD FIELD .EXTERNAL_ID
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
END SP_RESPONSE_OAS_EXPORT_GA;
/
