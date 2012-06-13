CREATE OR REPLACE PROCEDURE SP_CRRESPONSE_OAS_EXPORT_ISTEP IS
  --  This Procedure is Used to Load the cconstructed response used to export cr item response,

  -- AUTHOR: TCS Offshore Team --

  -- CREATED DATE: 24th November 2008 --
  V_RA OAS.ITEM_RESPONSE_CR.CONSTRUCTED_RESPONSE%TYPE; --VARCHAR2 (2000) := '';
  -- Variable to hold the Response Array --
  V_ITEM_ID VARCHAR2(64);
  --it is used to insert the records into item_details table
  TYPE SR_RESP_REC IS RECORD(
    TEST_ROSTER_ID    INTEGER,
    TD_ITEM_SET_ID    INTEGER,
    ITEM_ID           VARCHAR2(32),
    EXTERNAL_ID       VARCHAR2(64),
    CREATED_DATE_TIME DATE
    );

  TYPE SR_RESP_TAB IS TABLE OF SR_RESP_REC INDEX BY BINARY_INTEGER;

  V_SR_RESP_TAB SR_RESP_TAB;

  -- Cursor to select all Student  records from  student_details
  CURSOR C_ST IS
    SELECT SD.STUDENT_ID AS STUDENT_ID,
           SD.CLASS_ORG_ID AS CLASS_ORG_ID,
           SD.OPUNIT AS OPUNIT,
           SD.GRADE AS STUDENT_GRADE,
           STD.GRADE AS TEST_GRADE,
           SUBSTR(STD.STUDENT_TEST_DETAILS_ID, 4) AS TEST_ROSTER_ID
      FROM STUDENT_DETAILS SD, STUDENT_TEST_DETAILS STD
     WHERE SD.STUDENT_ID = STD.STUDENT_ID
       AND SD.OPUNIT = STD.OPUNIT;

  --  Cursor to select CR records
  --Extract all the Item_Set_Ids, Item_Ids of cr item

  CURSOR C_TD(IN_TEST_ROSTER_ID NUMBER) IS WITH MAIN AS(
    SELECT IR.TEST_ROSTER_ID AS TEST_ROSTER_ID,
           IR.ITEM_SET_ID TD_ITEM_SET_ID,
           IR.ITEM_ID AS ITEM_ID,
           ITEM.EXTERNAL_ID,
           MAX(IR.ITEM_RESPONSE_ID) AS ITEM_RESPONSE_ID
      FROM OAS.ITEM_RESPONSE IR, OAS.ITEM
     WHERE TEST_ROSTER_ID = IN_TEST_ROSTER_ID
       AND IR.ITEM_ID = ITEM.ITEM_ID
       AND ITEM.ITEM_TYPE = 'CR'
     GROUP BY IR.TEST_ROSTER_ID,
              IR.ITEM_SET_ID,
              IR.ITEM_ID,
              ITEM.EXTERNAL_ID)
    SELECT M.TEST_ROSTER_ID,
           M.TD_ITEM_SET_ID,
           M.ITEM_ID,
           M.EXTERNAL_ID,
           IR.CREATED_DATE_TIME
      FROM MAIN M, OAS.ITEM_RESPONSE IR
     WHERE IR.ITEM_RESPONSE_ID = M.ITEM_RESPONSE_ID;

BEGIN

  /*Truncate ITEM_DETAILS before loading it with records*/
  EXECUTE IMMEDIATE ('TRUNCATE TABLE ITEM_DETAILS');

  COMMIT;
  FOR C_ST_REC IN C_ST LOOP
    --Initialize values before using them--
    V_RA := NULL;

    OPEN C_TD(C_ST_REC.TEST_ROSTER_ID

              );

    LOOP
      FETCH C_TD BULK COLLECT
        INTO V_SR_RESP_TAB;

      FOR QUESTION_NO IN 1 .. V_SR_RESP_TAB.COUNT LOOP

        V_RA := FN_GETCRRESPONSE(V_SR_RESP_TAB(QUESTION_NO).TEST_ROSTER_ID,
                                 V_SR_RESP_TAB(QUESTION_NO).TD_ITEM_SET_ID,
                                 V_SR_RESP_TAB(QUESTION_NO).ITEM_ID);

        V_ITEM_ID := V_SR_RESP_TAB(QUESTION_NO).EXTERNAL_ID;

        IF (V_ITEM_ID IS NULL) THEN

          V_ITEM_ID := V_SR_RESP_TAB(QUESTION_NO).ITEM_ID;

        END IF;

        INSERT INTO ITEM_DETAILS
          (STUDENT_TEST_DETAILS_ID,
           ITEM_ID,
           ITEM_ID_SOURCE,
           IGNORE_RESPONSE,
           LAST_UPDATED_DATE_TIME,
           ITEM_RESPONSE,
           RECORD_STATUS_FLAG)
        VALUES
          (C_ST_REC.TEST_ROSTER_ID,
           V_ITEM_ID,
           'E',
           'N',
           TO_CHAR(V_SR_RESP_TAB(QUESTION_NO).CREATED_DATE_TIME,'YYYYMMDDHHMMSS'),
           V_RA,
           'AC'
           );

      END LOOP;

      EXIT WHEN C_TD%NOTFOUND;
    END LOOP; /* End of loop after inserting the records  */

    COMMIT;

    CLOSE C_TD;
  END LOOP;
END SP_CRRESPONSE_OAS_EXPORT_ISTEP;
/
