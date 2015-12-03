CREATE OR REPLACE PACKAGE PKG_CREATE_LLO_RP_PT_CONTENT IS
  PROCEDURE CREATE_LLO_RP_PT_CONTENT(IN_OLD_PT_PRODUCT_ID INTEGER,
                                     IN_NEW_PT_PRODUCT_ID INTEGER);
END PKG_CREATE_LLO_RP_PT_CONTENT;
/
CREATE OR REPLACE PACKAGE BODY PKG_CREATE_LLO_RP_PT_CONTENT AS

  V_TD_COUNTER INTEGER := 0;
  V_CREATED_BY        CONSTANT INTEGER := 2;
  V_ACTIVATION_STATUS CONSTANT VARCHAR2(2) := 'AC';
  V_ITEM_SET_FORM_PT  CONSTANT VARCHAR2(2) := 'PT';

  NEW_PRODUCT_EXIST      EXCEPTION;
  PRODUCT_NOT_FOUND      EXCEPTION;
  CHECK_EXISTING_PRODUCT EXCEPTION;

  CURSOR CUR_GET_PRODUCTS(IN_PT_PRODUCT_ID INTEGER) IS
    SELECT DISTINCT LLPDL.NEW_PRODUCT_ID,
                    LLPDL.PARENT_PRODUCT_ID,
                    LLPDL.PRODUCT_NAME
      FROM LLRP_PRACTICE_DATA_LIST LLPDL
     WHERE LLPDL.ACTIVATION_STATUS = V_ACTIVATION_STATUS
       AND LLPDL.NEW_PRODUCT_ID = IN_PT_PRODUCT_ID;

  CURSOR CUR_TC_ITEM_SET(IN_PT_PRODUCT_ID INTEGER) IS
    SELECT DISTINCT LLPDL.TEST_NAME,
                    LLPDL.TEST_LEVEL,
                    SUBSTR(LLPDL.EXT_TST_ITEM_SET_ID,
                           1,
                           INSTR(LLPDL.EXT_TST_ITEM_SET_ID, '_', -1) - 5) AS EXT_TST_ITEM_SET_ID
      FROM LLRP_PRACTICE_DATA_LIST LLPDL
     WHERE LLPDL.ACTIVATION_STATUS = V_ACTIVATION_STATUS
       AND LLPDL.NEW_PRODUCT_ID = IN_PT_PRODUCT_ID;

  CURSOR CUR_TS_ITEM_SET(IN_TEST_NAME VARCHAR2, IN_PT_PRODUCT_ID INTEGER) IS
    SELECT DISTINCT SUBSTR(LLPDL.EXT_TST_ITEM_SET_ID,
                           1,
                           INSTR(LLPDL.EXT_TST_ITEM_SET_ID, '_', -1) - 1) AS EXT_TST_ITEM_SET_ID,
                    LLPDL.SCHEDULE_UNIT_NAME AS ITEM_SET_NAME,
                    LLPDL.SCHEDULE_UNIT_ORDER AS ITEM_SET_ORDER
      FROM LLRP_PRACTICE_DATA_LIST LLPDL
     WHERE LLPDL.ACTIVATION_STATUS = V_ACTIVATION_STATUS
       AND LLPDL.NEW_PRODUCT_ID = IN_PT_PRODUCT_ID
       AND LLPDL.TEST_NAME = IN_TEST_NAME
     ORDER BY LLPDL.SCHEDULE_UNIT_ORDER;

  CURSOR CUR_TD_ITEM_SET(IN_TEST_NAME VARCHAR2, IN_PT_PRODUCT_ID INTEGER) IS
    SELECT DISTINCT LLPDL.EXT_TST_ITEM_SET_ID, LLPDL.ITEM_SET_NAME
      FROM LLRP_PRACTICE_DATA_LIST LLPDL
     WHERE LLPDL.ACTIVATION_STATUS = V_ACTIVATION_STATUS
       AND LLPDL.NEW_PRODUCT_ID = IN_PT_PRODUCT_ID
       AND INSTR(LLPDL.EXT_TST_ITEM_SET_ID, IN_TEST_NAME) > 0;

  CURSOR CUR_GET_ITEM_LIST(IN_NEW_PRODUCT_ID INTEGER) IS
    SELECT DISTINCT ITEM_ID AS ITEM_ID
      FROM LLRP_PRACTICE_DATA_LIST LLPDL
     WHERE LLPDL.ACTIVATION_STATUS = V_ACTIVATION_STATUS
       AND LLPDL.NEW_PRODUCT_ID = IN_NEW_PRODUCT_ID;

  CURSOR CUR_GET_ITEM_SET_ITEM_ENTRIES(IN_NEW_PRODUCT_ID  INTEGER,
                                       IN_TST_ITEM_SET_ID VARCHAR2) IS
    SELECT ITEM_SET_NAME, ITEM_ID, ITEM_SORT_ORDER
      FROM LLRP_PRACTICE_DATA_LIST LLPDL
     WHERE LLPDL.ACTIVATION_STATUS = V_ACTIVATION_STATUS
       AND LLPDL.NEW_PRODUCT_ID = IN_NEW_PRODUCT_ID
       AND LLPDL.EXT_TST_ITEM_SET_ID = IN_TST_ITEM_SET_ID
     ORDER BY ITEM_SORT_ORDER;

  TYPE TD_SET_ARRAY IS VARRAY(2) OF VARCHAR2(512);
  TYPE ARR_TD_SET_TYPE IS TABLE OF TD_SET_ARRAY;
  TD_SET ARR_TD_SET_TYPE;

  TYPE V_CUR_TS_ITEM_SET IS TABLE OF CUR_TS_ITEM_SET%ROWTYPE INDEX BY BINARY_INTEGER;
  TYPE V_CUR_TD_ITEM_SET IS TABLE OF CUR_TD_ITEM_SET%ROWTYPE INDEX BY BINARY_INTEGER;
  TYPE V_CUR_GET_PRODUCTS IS TABLE OF CUR_GET_PRODUCTS%ROWTYPE INDEX BY BINARY_INTEGER;
  TS_DATA   V_CUR_TS_ITEM_SET;
  TD_DATA   V_CUR_TD_ITEM_SET;
  PROD_DATA V_CUR_GET_PRODUCTS;

  /**
  *  GENERATE AND RETURN A NEW ITEM SET ID FROM ITEM SET ID DB SEQUENCE
  **/
  FUNCTION GET_NEW_ITEM_SET_ID RETURN INTEGER IS
    V_NEW_ITEM_SET_ID INTEGER := 0;
  BEGIN
    SELECT SEQ_ITEM_SET_ID.NEXTVAL INTO V_NEW_ITEM_SET_ID FROM DUAL;
  
    RETURN V_NEW_ITEM_SET_ID;
  END;

  /**
  *  CREATES ITEM_SET ENTRIES AND ITEM_SET PARENT CHILD RELATIONS
  **/
  PROCEDURE INSERT_ITEM_SET_RELATION(IN_NEW_ITEM_SET_ID      INTEGER,
                                     IN_PARENT_ITEM_SET_ID   INTEGER,
                                     IN_SORT_ORDER           INTEGER,
                                     IN_ITEM_SET_NAME        VARCHAR2,
                                     IN_TST_ITEM_SET_ID      VARCHAR2,
                                     IN_ITEM_SET_LEVEL       VARCHAR2,
                                     IN_ITEM_SET_TYPE        VARCHAR2,
                                     IN_PARENT_ITEM_SET_TYPE VARCHAR2) AS
  
    V_SUBJECT       VARCHAR2(512) := NULL;
    V_SAMPLE        VARCHAR2(2) := 'F';
    V_ITEM_SET_FORM VARCHAR2(128) := NULL;
  
  BEGIN
    IF IN_ITEM_SET_TYPE = 'TD' THEN
      V_ITEM_SET_FORM := V_ITEM_SET_FORM_PT;
    END IF;
  
    INSERT INTO ITEM_SET
      (ITEM_SET_ID,
       ITEM_SET_TYPE,
       ITEM_SET_NAME,
       MIN_GRADE,
       VERSION,
       MAX_GRADE,
       ITEM_SET_LEVEL,
       SUBJECT,
       GRADE,
       SAMPLE,
       MEDIA_PATH,
       TIME_LIMIT,
       BREAK_TIME,
       EXT_EMS_ITEM_SET_ID,
       EXT_CMS_ITEM_SET_ID,
       ITEM_SET_DISPLAY_NAME,
       ITEM_SET_DESCRIPTION,
       ITEM_SET_RULE_ID,
       CREATED_DATE_TIME,
       CREATED_BY,
       ACTIVATION_STATUS,
       ITEM_SET_CATEGORY_ID,
       OWNER_CUSTOMER_ID,
       UPDATED_BY,
       UPDATED_DATE_TIME,
       ITEM_SET_FORM,
       PUBLISH_STATUS,
       ORIGINAL_CREATED_BY,
       EXT_TST_ITEM_SET_ID,
       CONTENT_SIZE,
       ADS_OB_ASMT_ID,
       ASMT_HASH,
       ASMT_ENCRYPTION_KEY,
       ITEM_ENCRYPTION_KEY,
       FORWARD_ONLY,
       ADAPTIVE,
       CONTENT_REPOSITORY_URI)
    VALUES
      (IN_NEW_ITEM_SET_ID, -- NEW ITEM SET
       IN_ITEM_SET_TYPE,
       IN_ITEM_SET_NAME,
       NULL,
       NULL,
       NULL,
       IN_ITEM_SET_LEVEL,
       V_SUBJECT,
       NULL,
       V_SAMPLE,
       '/oastd-web/versions/cab/1.0/',
       0,
       0,
       NULL,
       NULL,
       IN_ITEM_SET_NAME,
       NULL,
       NULL,
       SYSDATE,
       V_CREATED_BY,
       V_ACTIVATION_STATUS,
       NULL, -- THIS IS NOT NEEDED TO BE CHANGED AS FOR TC,TS & TD IT REMAINS NULL
       NULL,
       V_CREATED_BY,
       SYSDATE,
       V_ITEM_SET_FORM,
       NULL,
       NULL,
       IN_TST_ITEM_SET_ID, -- EXT_TST_ITEM_SET_ID IS GENERATED FROM PREVIOUS PROCEDURE
       0,
       NULL,
       NULL,
       NULL,
       NULL,
       'F',
       'F',
       'https://oas.ctb.com/content/');
  
    -- POPULATE ALL ITEM SET ID AND TST ITEM SET ID MAPPING FOR ALL LEVEL ITEM SET
    TD_SET.EXTEND;
    V_TD_COUNTER := V_TD_COUNTER + 1;
    TD_SET(V_TD_COUNTER) := TD_SET_ARRAY(IN_NEW_ITEM_SET_ID,
                                         IN_TST_ITEM_SET_ID);
  
    -- THIS IS FOR ITEM_SET_PARENT ENTRIES. TC LEVEL DOENOT REQUIRE ANY ENTRIES
    IF IN_ITEM_SET_TYPE <> 'TC' THEN
    
      INSERT INTO ITEM_SET_PARENT
        (PARENT_ITEM_SET_ID,
         CREATED_DATE_TIME,
         ITEM_SET_TYPE,
         ITEM_SET_ID,
         CREATED_BY,
         UPDATED_BY,
         UPDATED_DATE_TIME,
         ITEM_SET_SORT_ORDER,
         PARENT_ITEM_SET_TYPE,
         ITEM_SET_GROUP)
      VALUES
        (IN_PARENT_ITEM_SET_ID, -- NEW PARENT ITEMSET ID
         SYSDATE,
         IN_ITEM_SET_TYPE,
         IN_NEW_ITEM_SET_ID, -- NEW ITEM SET ID
         V_CREATED_BY,
         V_CREATED_BY,
         SYSDATE,
         IN_SORT_ORDER,
         IN_PARENT_ITEM_SET_TYPE,
         NULL);
    END IF;
  
  EXCEPTION
    WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('Exception in item set relation mapping for - TST_ITEM_SET_ID: [' ||
                           IN_TST_ITEM_SET_ID || '], ITEM_SET_TYPE: [' ||
                           IN_ITEM_SET_TYPE || '], ITEM_SET_LVL: [' ||
                           IN_ITEM_SET_LEVEL || '], IN_ITEM_SET_NAME: [' ||
                           IN_ITEM_SET_NAME || '].');
      RAISE_APPLICATION_ERROR(-20000,
                              'Unable to create item set relation mapping.');
  END INSERT_ITEM_SET_RELATION;

  /**
  * INSERTS ITEM RELATED TO EACH TD
  */
  PROCEDURE INSERT_ITEM(IN_NEW_PRODUCT_ID INTEGER) AS
  
    V_NEW_ITEM_ID VARCHAR2(50) := NULL;
    V_COUNT       INTEGER := 0;
  
  BEGIN
  
    -- GET ALL ITEMS FOR TD LEVEL ITEM SET IDS
    FOR ITEM_DATA IN CUR_GET_ITEM_LIST(IN_NEW_PRODUCT_ID) LOOP
    
      V_NEW_ITEM_ID := ITEM_DATA.ITEM_ID;
    
      SELECT COUNT(1) INTO V_COUNT FROM ITEM WHERE ITEM_ID = V_NEW_ITEM_ID;
    
      IF V_COUNT = 0 THEN
        INSERT INTO ITEM
          (ITEM_ID,
           CORRECT_ANSWER,
           ITEM_TYPE,
           CREATED_BY,
           CREATED_DATE_TIME,
           UPDATED_BY,
           UPDATED_DATE_TIME,
           ACTIVATION_STATUS,
           DESCRIPTION,
           NAME,
           EXT_STIMULUS_ID,
           EXT_STIMULUS_TITLE,
           VERSION,
           ITEM_DISPLAY_NAME,
           TEMPLATE_ID,
           THINK_CODE,
           ONLINE_CR,
           PUBLISHED,
           LOCKED,
           IS_VALID,
           CUSTOMER_CREATED,
           CUSTOMER_ID,
           EXTERNAL_ID,
           EXTERNAL_SYSTEM,
           ANSWER_AREA,
           GRIDDED_COLUMNS,
           ADS_ITEM_ID)
        VALUES
          (V_NEW_ITEM_ID, -- ITEM ID CREATED WITH '.BMT' EXTENSION
           NULL,
           'NI',
           V_CREATED_BY,
           SYSDATE,
           V_CREATED_BY,
           SYSDATE,
           V_ACTIVATION_STATUS,
           NULL,
           NULL,
           NULL,
           NULL,
           NULL,
           V_NEW_ITEM_ID, -- ITEM NAME CREATED WITH '.BMT' EXTENSION
           NULL,
           NULL,
           'F',
           'F',
           'F',
           'F',
           'F',
           NULL,
           NULL,
           NULL,
           NULL, -- ANSWER AREA
           NULL,
           V_NEW_ITEM_ID);
      END IF;
    END LOOP;
  
  EXCEPTION
    WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('Exception in item creation for - ITEM_ID: [' ||
                           V_NEW_ITEM_ID || '].');
      RAISE_APPLICATION_ERROR(-20000, 'Unable to create item.');
    
  END INSERT_ITEM;

  /**
  * THIS WILL CREATE ITEM_SET_ITEM ENTRIES
  */
  PROCEDURE INSERT_ITEM_SET_ITEM(IN_NEW_PRODUCT_ID INTEGER) AS
  
    V_NEW_ITEM_SET_ID INTEGER := 0;
    V_TST_ITEM_SET_ID VARCHAR2(512) := NULL;
    ITEM_SET_ITEM_EXP EXCEPTION;
  
  BEGIN
  
    -- INSERT FOR ALL ITEMS FOR THE TD LEVEL ITEM-SET
    FOR I IN 1 .. TD_SET.COUNT LOOP
      V_NEW_ITEM_SET_ID := TO_NUMBER(TD_SET(I) (1));
      V_TST_ITEM_SET_ID := TD_SET(I) (2);
    
      INSERT INTO ITEM_SET_PRODUCT
        (ITEM_SET_ID,
         PRODUCT_ID,
         CREATED_BY,
         CREATED_DATE_TIME,
         UPDATED_BY,
         UPDATED_DATE_TIME)
      VALUES
        (V_NEW_ITEM_SET_ID,
         IN_NEW_PRODUCT_ID,
         V_CREATED_BY,
         SYSDATE,
         NULL,
         SYSDATE);
    
      FOR CUR_DATA IN CUR_GET_ITEM_SET_ITEM_ENTRIES(IN_NEW_PRODUCT_ID,
                                                    V_TST_ITEM_SET_ID) LOOP
      
        /*dbms_output.put_line('item_set_item entry : Item set id : ' ||
        V_NEW_ITEM_SET_ID || ' tst_item_set_id : ' ||
        V_TST_ITEM_SET_ID || ' item_id : ' ||
        CUR_DATA.ITEM_ID);*/
        BEGIN
          INSERT INTO ITEM_SET_ITEM
            (ITEM_SET_ID,
             CREATED_DATE_TIME,
             ITEM_SORT_ORDER,
             CREATED_BY,
             UPDATED_BY,
             UPDATED_DATE_TIME,
             ITEM_ID,
             IBS_INVISIBLE,
             FIELD_TEST,
             SUPPRESSED)
          VALUES
            (V_NEW_ITEM_SET_ID,
             SYSDATE,
             CUR_DATA.ITEM_SORT_ORDER,
             V_CREATED_BY,
             V_CREATED_BY,
             SYSDATE,
             CUR_DATA.ITEM_ID,
             'F',
             'F',
             'T');
        EXCEPTION
          WHEN OTHERS THEN
            RAISE ITEM_SET_ITEM_EXP;
        END;
      END LOOP;
    END LOOP;
  EXCEPTION
  
    WHEN ITEM_SET_ITEM_EXP THEN
      DBMS_OUTPUT.PUT_LINE('Exception in item set item mappping creation.');
      RAISE_APPLICATION_ERROR(-20000,
                              'Unable to create tem set item mappping.');
    WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('Exception in item set product mapping creation.');
      RAISE_APPLICATION_ERROR(-20000,
                              'Unable to create tem set product mappping.');
    
  END INSERT_ITEM_SET_ITEM;

  /**
  * INSERTS TEST CATALOG ENTRIES FOR EACH PRODUCT
  */
  PROCEDURE CREATE_TEST_CATALOG(IN_NEW_PRODUCT_ID INTEGER,
                                IN_NEW_TC_ID      INTEGER,
                                IN_TEST_NAME      VARCHAR2,
                                IN_TEST_LEVEL     VARCHAR2) AS
  
  BEGIN
    INSERT INTO TEST_CATALOG
      (TEST_CATALOG_ID,
       PRODUCT_ID,
       TEST_NAME,
       TEST_DISPLAY_NAME,
       ITEM_SET_ID,
       EXT_CATALOG_ID,
       SUBJECT,
       TEST_GRADE,
       TEST_FORM,
       CREATED_DATE_TIME,
       TEST_LEVEL,
       VERSION,
       CREATED_BY,
       UPDATED_BY,
       UPDATED_DATE_TIME,
       ACTIVATION_STATUS,
       COMMODITY_CODE,
       EISS_TEST_LEVEL,
       BLOCK_DOWNLOAD_FLAG)
    VALUES
      (SEQ_TEST_CATALOG_ID.NEXTVAL,
       IN_NEW_PRODUCT_ID,
       IN_TEST_NAME,
       IN_TEST_NAME,
       IN_NEW_TC_ID,
       NULL,
       ' ',
       NULL,
       NULL,
       SYSDATE,
       IN_TEST_LEVEL,
       NULL,
       V_CREATED_BY,
       V_CREATED_BY,
       SYSDATE,
       V_ACTIVATION_STATUS,
       NULL,
       NULL,
       NULL);
  
  EXCEPTION
    WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('Exception in test catalog creation for - TEST_NAME: [' ||
                           IN_TEST_NAME || '], TEST_LVL: [' ||
                           IN_TEST_LEVEL || '].');
      RAISE_APPLICATION_ERROR(-20000, 'Unable to create test catalog');
    
  END CREATE_TEST_CATALOG;

  /**
  * CREATE TC ,TS ,TD ITEM SET RELATIONS.
  */
  PROCEDURE CREATE_ITEM_SET_SCHEDULABLE(IN_NEW_PRODUCT_ID INTEGER) AS
  
    V_TC_ITEM_SET_ID  INTEGER := 0;
    V_TS_ITEM_SET_ID  INTEGER := 0;
    V_TST_ITEM_SET_ID VARCHAR2(1024) := NULL;
    V_TEST_LEVEL      VARCHAR2(128) := NULL;
  BEGIN
  
    -- TC LEVEL ENTRIES
    FOR TC_DATA IN CUR_TC_ITEM_SET(IN_NEW_PRODUCT_ID) LOOP
    
      V_TST_ITEM_SET_ID := TC_DATA.EXT_TST_ITEM_SET_ID;
      V_TEST_LEVEL      := TC_DATA.TEST_LEVEL;
      V_TC_ITEM_SET_ID  := GET_NEW_ITEM_SET_ID();
      INSERT_ITEM_SET_RELATION(V_TC_ITEM_SET_ID,
                               NULL,
                               NULL,
                               TC_DATA.TEST_NAME,
                               V_TST_ITEM_SET_ID,
                               V_TEST_LEVEL,
                               'TC',
                               NULL);
    
      -- TS LEVEL ENTRIES
      OPEN CUR_TS_ITEM_SET(TC_DATA.TEST_NAME, IN_NEW_PRODUCT_ID);
      FETCH CUR_TS_ITEM_SET BULK COLLECT
        INTO TS_DATA;
      CLOSE CUR_TS_ITEM_SET;
    
      FOR I IN 1 .. TS_DATA.COUNT LOOP
        V_TST_ITEM_SET_ID := TS_DATA(I).EXT_TST_ITEM_SET_ID;
        V_TS_ITEM_SET_ID  := GET_NEW_ITEM_SET_ID();
        INSERT_ITEM_SET_RELATION(V_TS_ITEM_SET_ID,
                                 V_TC_ITEM_SET_ID,
                                 I,
                                 TS_DATA(I).ITEM_SET_NAME,
                                 V_TST_ITEM_SET_ID,
                                 V_TEST_LEVEL,
                                 'TS',
                                 'TC');
      
        --TD LEVEL ENTRIES
        OPEN CUR_TD_ITEM_SET(V_TST_ITEM_SET_ID, IN_NEW_PRODUCT_ID);
        FETCH CUR_TD_ITEM_SET BULK COLLECT
          INTO TD_DATA;
        CLOSE CUR_TD_ITEM_SET;
      
        FOR J IN 1 .. TD_DATA.COUNT LOOP
          V_TST_ITEM_SET_ID := TD_DATA(J).EXT_TST_ITEM_SET_ID;
          INSERT_ITEM_SET_RELATION(GET_NEW_ITEM_SET_ID(),
                                   V_TS_ITEM_SET_ID,
                                   J,
                                   TD_DATA(J).ITEM_SET_NAME,
                                   V_TST_ITEM_SET_ID,
                                   V_TEST_LEVEL,
                                   'TD',
                                   'TS');
        
        END LOOP; --TD LEVEL
      END LOOP; -- TS LEVEL
    
      -- CREATE TEST_CATALOG_ENTRIES.
      CREATE_TEST_CATALOG(IN_NEW_PRODUCT_ID,
                          V_TC_ITEM_SET_ID,
                          TC_DATA.TEST_NAME,
                          V_TEST_LEVEL);
    
    END LOOP; -- TC LEVEL
  
    -- ITEM TABLE INSERTION
    INSERT_ITEM(IN_NEW_PRODUCT_ID);
  
    -- ITEM_SET_ITEM TABLE INSERTION
    INSERT_ITEM_SET_ITEM(IN_NEW_PRODUCT_ID);
  
  END CREATE_ITEM_SET_SCHEDULABLE;

  /**
  * THIS PROCEDURE WILL TAKE 2 PRODUCT ID AS INPUTS AND WILL POPULATE ALL PRODUCTS
  */
  PROCEDURE CREATE_PRODUCT_TESTDATA(IN_OLD_PT_PRODUCT_ID INTEGER,
                                    IN_NEW_PT_PRODUCT_ID INTEGER) AS
  
    V_COUNTER INTEGER := 0;
  
  BEGIN
    /** GET LIST OF ALL PRODUCTS FOR THE SAID FRAMEWORK PRODUCT
    */
    --NEW PRODUCT ID CHECK
    SELECT COUNT(1)
      INTO V_COUNTER
      FROM PRODUCT PROD
     WHERE PROD.PRODUCT_ID = IN_NEW_PT_PRODUCT_ID;
  
    IF V_COUNTER = 0 THEN
      --OLD PRODUCT ID CHECK
      SELECT COUNT(1)
        INTO V_COUNTER
        FROM PRODUCT PROD
       WHERE PROD.PRODUCT_ID = IN_OLD_PT_PRODUCT_ID;
      IF V_COUNTER = 0 THEN
        RAISE CHECK_EXISTING_PRODUCT;
      END IF;
    
      --NEW PRODUCT DETAILS FETCH
      OPEN CUR_GET_PRODUCTS(IN_NEW_PT_PRODUCT_ID);
      FETCH CUR_GET_PRODUCTS BULK COLLECT
        INTO PROD_DATA;
      CLOSE CUR_GET_PRODUCTS;
    
      IF PROD_DATA.COUNT = 0 THEN
        RAISE PRODUCT_NOT_FOUND;
      END IF;
    
      FOR I IN 1 .. PROD_DATA.COUNT LOOP
      
        INSERT INTO PRODUCT
          (PRODUCT_ID,
           CREATED_DATE_TIME,
           PRODUCT_DESCRIPTION,
           CREATED_BY,
           VERSION,
           UPDATED_BY,
           UPDATED_DATE_TIME,
           ACTIVATION_STATUS,
           PRODUCT_TYPE,
           PRODUCT_NAME,
           SCORING_ITEM_SET_LEVEL,
           PREVIEW_ITEM_SET_LEVEL,
           PARENT_PRODUCT_ID,
           EXT_PRODUCT_ID,
           CONTENT_AREA_LEVEL,
           INTERNAL_DISPLAY_NAME,
           SEC_SCORING_ITEM_SET_LEVEL,
           IBS_SHOW_CMS_ID,
           PRINTABLE,
           SCANNABLE,
           KEYENTERABLE,
           BRANDING_TYPE_CODE,
           EISS_TESTING_PROGRAM,
           ACKNOWLEDGMENTS_URL,
           SHOW_STUDENT_FEEDBACK,
           STATIC_MANIFEST,
           SESSION_MANIFEST,
           SUBTESTS_SELECTABLE,
           SUBTESTS_ORDERABLE,
           SUBTESTS_LEVELS_VARY,
           OFF_GRADE_TESTING_DISABLED,
           LICENSE_ENABLED,
           SCORABLE,
           DELIVERY_CLIENT_ID)
          (SELECT PROD_DATA                 (I).NEW_PRODUCT_ID, -- NEW PRODUCT ID
                  SYSDATE,
                  PROD_DATA                 (I).PRODUCT_NAME,
                  CREATED_BY,
                  VERSION,
                  V_CREATED_BY,
                  SYSDATE,
                  ACTIVATION_STATUS,
                  PRODUCT_TYPE,
                  PROD_DATA                 (I).PRODUCT_NAME,
                  SCORING_ITEM_SET_LEVEL,
                  PREVIEW_ITEM_SET_LEVEL,
                  PROD_DATA                 (I).PARENT_PRODUCT_ID, -- PARENT ID OF NEW PRODUCT ID
                  EXT_PRODUCT_ID,
                  CONTENT_AREA_LEVEL,
                  PROD_DATA                 (I).PRODUCT_NAME,
                  SEC_SCORING_ITEM_SET_LEVEL,
                  IBS_SHOW_CMS_ID,
                  PRINTABLE,
                  SCANNABLE,
                  KEYENTERABLE,
                  BRANDING_TYPE_CODE,
                  EISS_TESTING_PROGRAM,
                  ACKNOWLEDGMENTS_URL,
                  SHOW_STUDENT_FEEDBACK,
                  STATIC_MANIFEST,
                  SESSION_MANIFEST,
                  SUBTESTS_SELECTABLE,
                  SUBTESTS_ORDERABLE,
                  SUBTESTS_LEVELS_VARY,
                  OFF_GRADE_TESTING_DISABLED,
                  LICENSE_ENABLED,
                  SCORABLE,
                  2
             FROM PRODUCT
            WHERE PRODUCT_ID = IN_OLD_PT_PRODUCT_ID);
      
        -- DECLARE THE TD ITEM SET LIST
        TD_SET := ARR_TD_SET_TYPE();
      
        -- INSERT TC,TS,TD ITEM-SETS FOR EACH PRODUCT AND INSERT ITEM,ITEM_SET_ITEM
        CREATE_ITEM_SET_SCHEDULABLE(PROD_DATA(I).NEW_PRODUCT_ID);
      END LOOP; -- LOOP FOR PRODUCTS. 
    ELSE
      RAISE NEW_PRODUCT_EXIST;
    END IF;
  
  END CREATE_PRODUCT_TESTDATA;

  --MAIN ENTRY POINT .Execute this procedure.
  PROCEDURE CREATE_LLO_RP_PT_CONTENT(IN_OLD_PT_PRODUCT_ID INTEGER,
                                     IN_NEW_PT_PRODUCT_ID INTEGER) IS
  
    NULL_VALUE_EXC EXCEPTION;
  BEGIN
  
    IF IN_OLD_PT_PRODUCT_ID IS NULL OR IN_NEW_PT_PRODUCT_ID IS NULL THEN
      RAISE NULL_VALUE_EXC;
    ELSE
      DBMS_OUTPUT.PUT_LINE('Practice test content population started.');
      V_TD_COUNTER := 0;
      CREATE_PRODUCT_TESTDATA(IN_OLD_PT_PRODUCT_ID, IN_NEW_PT_PRODUCT_ID);
      DBMS_OUTPUT.PUT_LINE('Completed for all test data.');
    END IF;
    COMMIT;
  EXCEPTION
  
    WHEN NULL_VALUE_EXC THEN
      RAISE_APPLICATION_ERROR(-20000, 'Please provide valid inputs.');
    
    WHEN PRODUCT_NOT_FOUND THEN
      RAISE_APPLICATION_ERROR(-20000,
                              'The new product ID [' ||
                              IN_NEW_PT_PRODUCT_ID ||
                              '] is not found in lookup table.');
    WHEN NEW_PRODUCT_EXIST THEN
      RAISE_APPLICATION_ERROR(-20000,
                              'The new product ID [' ||
                              IN_NEW_PT_PRODUCT_ID ||
                              '] is already exists in database.');
    WHEN CHECK_EXISTING_PRODUCT THEN
      RAISE_APPLICATION_ERROR(-20000,
                              'The old product ID [' ||
                              IN_OLD_PT_PRODUCT_ID || '] does not exists.');
    
    WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('Process completed with error.');
      ROLLBACK;
      RAISE;
    
  END CREATE_LLO_RP_PT_CONTENT;

END PKG_CREATE_LLO_RP_PT_CONTENT;
/
