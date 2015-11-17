CREATE OR REPLACE PACKAGE LLRP_SCORE_LOOKUP_1STEDITION AS
  /**
  * This procedure will be executed to cleanup score lookup data for LLO-RP 1st edition forms.
  **/
  PROCEDURE CLEANUP_SCORE_LOOKUP_DATA;

  /**
  * This procedure will be executed to load score lookup data for LLO-RP 1st edition forms.
  * The data will be cloned from LL 1st edition forms.Necessary ids will be changed as per form and will be loaded.
  **/
  PROCEDURE LOAD_SCORE_LOOKUP_DATA;

END LLRP_SCORE_LOOKUP_1STEDITION;
/
CREATE OR REPLACE PACKAGE BODY LLRP_SCORE_LOOKUP_1STEDITION AS

  /**
  * This procedure will be executed to cleanup score lookup data for LLO-RP 1st edition forms.
  **/
  PROCEDURE CLEANUP_SCORE_LOOKUP_DATA AS
  BEGIN
    DELETE FROM SCORE_LOOKUP_ITEM_SET
     where SCORE_LOOKUP_ID like 'LLEABBMT_2015_%';

    DELETE FROM SCORE_LOOKUP where FRAMEWORK_CODE = 'LLEABBMT';
    COMMIT;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      DBMS_OUTPUT.PUT_LINE('EXCEPTION In Cleanup .ROLLING BACK.');

  END CLEANUP_SCORE_LOOKUP_DATA;

  /**
  * This procedure will be executed to load score lookup data for LLO-RP 1st edition forms.
  * The data will be cloned from LL 1st edition forms.Necessary ids will be changed as per form and will be loaded.
  **/
  PROCEDURE LOAD_SCORE_LOOKUP_DATA AS
  BEGIN
    DBMS_OUTPUT.PUT_LINE('DATA LOADING STARTED');
    INSERT INTO SCORE_LOOKUP_ITEM_SET
      (SCORE_LOOKUP_ID, ITEM_SET_ID)
      (SELECT 'LLEABBMT_2015_' || SUBSTR(SLIS.SCORE_LOOKUP_ID, 12) AS SCORE_LOOKUP_ID,
              ISETRP.ITEM_SET_ID AS ITEM_SET_ID
         FROM SCORE_LOOKUP_ITEM_SET SLIS,
              ITEM_SET              ISETLL,
              TEST_CATALOG          TC,
              PRODUCT               P,
              ITEM_SET_ANCESTOR     ISA,
              ITEM_SET              ISETRP
        WHERE SLIS.SCORE_LOOKUP_ID LIKE '%LLEAB_2%'
          AND SLIS.ITEM_SET_ID = ISETLL.ITEM_SET_ID
          AND TC.PRODUCT_ID = P.PRODUCT_ID
          AND P.PARENT_PRODUCT_ID = 7200
          AND TC.ITEM_SET_ID = ISA.ANCESTOR_ITEM_SET_ID
          AND ISA.ITEM_SET_ID = ISETRP.ITEM_SET_ID
          AND ISETRP.EXT_TST_ITEM_SET_ID = ISETLL.EXT_TST_ITEM_SET_ID);

    DBMS_OUTPUT.PUT_LINE('SCORE_LOOKUP_ITEM_SET INSERTION COMPLETED');

    INSERT INTO SCORE_LOOKUP
      (SOURCE_SCORE_TYPE_CODE,
       DEST_SCORE_TYPE_CODE,
       SCORE_LOOKUP_ID,
       SOURCE_SCORE_VALUE,
       DEST_SCORE_VALUE,
       TEST_FORM,
       TEST_LEVEL,
       GRADE,
       CONTENT_AREA,
       NORM_GROUP,
       QUARTER_MONTH,
       NORM_YEAR,
       FRAMEWORK_CODE,
       PRODUCT_INTERNAL_DISPLAY_NAME,
       AGE_CATEGORY,
       EXTENDED_FLAG)
      (SELECT SOURCE_SCORE_TYPE_CODE,
              DEST_SCORE_TYPE_CODE,
              'LLEABBMT_2015_' || SUBSTR(SCORE_LOOKUP_ID, 12), --  Modify the score lookup id
              SOURCE_SCORE_VALUE,
              DEST_SCORE_VALUE,
              TEST_FORM,
              TEST_LEVEL,
              GRADE,
              CONTENT_AREA,
              NORM_GROUP,
              QUARTER_MONTH,
              '2015', -- Norm year is as per loading year
              'LLEABBMT', -- New framework code
              PRODUCT_INTERNAL_DISPLAY_NAME,
              AGE_CATEGORY,
              EXTENDED_FLAG
         FROM SCORE_LOOKUP
        WHERE FRAMEWORK_CODE = 'LLEAB'); -- Copy from existing Laslink data

    DBMS_OUTPUT.PUT_LINE('SCORE_LOOKUP INSERTION COMPLETED');
    COMMIT;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      DBMS_OUTPUT.PUT_LINE('EXCEPTION in Loading Data.ROLLING BACK.');
  END LOAD_SCORE_LOOKUP_DATA;

END LLRP_SCORE_LOOKUP_1STEDITION;
/
