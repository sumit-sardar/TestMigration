CREATE OR REPLACE PROCEDURE POPULATE_SCORE_LOOKUP_ITEM_SET(PRODUCT_ID IN NUMBER) IS

   CURSOR CURSOR_GET_TD_INFO(V_PRODUCT_ID IN NUMBER) IS
    SELECT distinct SUBTEST_ISET.ITEM_SET_ID     as ITEM_SET_ID,
                    SUBTEST_ISET.GRADE           AS GRADE,
                    SUBTEST_ISET.ITEM_SET_FORM   AS ITEM_SET_FORM,
                    SUBTEST_ISET.ITEM_SET_LEVEL  AS ITEM_SET_LEVEL,
                    REPORTING_ISET.ITEM_SET_NAME as CONTENT_AREA_NAME
      FROM ITEM_SET          REPORTING_ISET,
           ITEM_SET          SUBTEST_ISET,
           ITEM_SET_ITEM     SUBTEST_ISI,
           ITEM_SET_ITEM     OBJECTIVE_ISI,
           ITEM_SET_ANCESTOR ISA,
           ITEM_SET_CATEGORY ISC,
           PRODUCT           PROD
     WHERE PROD.PRODUCT_ID = V_PRODUCT_ID
       AND SUBTEST_ISET.ITEM_SET_TYPE = 'TD'
       AND SUBTEST_ISI.ITEM_SET_ID = SUBTEST_ISET.ITEM_SET_ID
       AND ISC.FRAMEWORK_PRODUCT_ID = PROD.PRODUCT_ID
       AND ISC.ITEM_SET_CATEGORY_LEVEL = PROD.CONTENT_AREA_LEVEL
       AND OBJECTIVE_ISI.ITEM_ID = SUBTEST_ISI.ITEM_ID
       AND ISA.ITEM_SET_ID = OBJECTIVE_ISI.ITEM_SET_ID
       AND REPORTING_ISET.ITEM_SET_ID = ISA.ANCESTOR_ITEM_SET_ID
       AND REPORTING_ISET.ITEM_SET_CATEGORY_ID = ISC.ITEM_SET_CATEGORY_ID
       AND REPORTING_ISET.ITEM_SET_TYPE = 'RE'
       AND REPORTING_ISET.ACTIVATION_STATUS = 'AC'
       AND SUBTEST_ISET.ACTIVATION_STATUS = 'AC';

  V_SCORE_LOOKUP_ID SCORE_LOOKUP.SCORE_LOOKUP_ID%TYPE;
  V_PARAMETER VARCHAR2(20000);
BEGIN

  FOR R1 IN CURSOR_GET_TD_INFO(PRODUCT_ID) LOOP
  V_PARAMETER :='ITEM_SET_FORM:'||R1.ITEM_SET_FORM||'ITEM_SET_LEVEL:'||R1.ITEM_SET_LEVEL||'GRADE:'||R1.GRADE||'CONTENT_AREA_NAME:'||R1.CONTENT_AREA_NAME;
   
   IF( R1.GRADE IS NULL ) THEN 
    /*
    SELECT DISTINCT SCORE_LOOKUP_ID
      INTO V_SCORE_LOOKUP_ID
      FROM SCORE_LOOKUP
     WHERE TEST_FORM = decode (R1.ITEM_SET_FORM,'Español','S',R1.ITEM_SET_FORM)
       AND TEST_LEVEL = decode(upper(R1.ITEM_SET_LEVEL),
                              'K',  'K',
                              '1',  '1',
                              '2-3','2',
                              '4-5','3',
                              '6-8','4',
                              '9-12','5',
                              R1.ITEM_SET_LEVEL)
       AND GRADE = nvl(R1.GRADE,
                       decode(upper(R1.ITEM_SET_LEVEL),
                              'K',  'K',
                              '1',  '1',
                              '2-3','2',
                              '4-5','3',
                              '6-8','4',
                              '9-12','5',
                              R1.GRADE))
       AND CONTENT_AREA = R1.CONTENT_AREA_NAME;
  
    INSERT INTO SCORE_LOOKUP_ITEM_SET
      (SCORE_LOOKUP_ID, ITEM_SET_ID)
    VALUES
      (V_SCORE_LOOKUP_ID, R1.ITEM_SET_ID);
     */
     SELECT DISTINCT SCORE_LOOKUP_ID
       INTO V_SCORE_LOOKUP_ID
       FROM SCORE_LOOKUP
      WHERE TEST_FORM =
            decode(R1.ITEM_SET_FORM, 'Español', 'S', R1.ITEM_SET_FORM)
        AND TEST_LEVEL = decode(upper(R1.ITEM_SET_LEVEL),
                                'K',   'K',
                                '1',   '1',
                                '2-3', '2',
                                '4-5', '3',
                                '6-8', '4',
                                '9-12','5',
                                R1.ITEM_SET_LEVEL)
        AND GRADE IS NULL
        AND CONTENT_AREA = R1.CONTENT_AREA_NAME;
    IF SQL%FOUND THEN 
     INSERT INTO SCORE_LOOKUP_ITEM_SET
       (SCORE_LOOKUP_ID, ITEM_SET_ID)
     VALUES
       (V_SCORE_LOOKUP_ID, R1.ITEM_SET_ID);

    END IF;   
   
  
  
  ELSE
  
  SELECT DISTINCT SCORE_LOOKUP_ID
    INTO V_SCORE_LOOKUP_ID
    FROM SCORE_LOOKUP
   WHERE TEST_FORM =
         decode(R1.ITEM_SET_FORM, 'Español', 'S', R1.ITEM_SET_FORM)
     AND TEST_LEVEL = decode(upper(R1.ITEM_SET_LEVEL),
                             'K',  'K',
                             '1',  '1',
                             '2-3','2',
                             '4-5','3',
                             '6-8','4',
                             '9-12','5',
                             R1.ITEM_SET_LEVEL)
     AND GRADE = R1.GRADE 
     AND CONTENT_AREA = R1.CONTENT_AREA_NAME;
  
  INSERT INTO SCORE_LOOKUP_ITEM_SET
    (SCORE_LOOKUP_ID, ITEM_SET_ID)
  VALUES
    (V_SCORE_LOOKUP_ID, R1.ITEM_SET_ID);
      
  END IF;
  
  /*MERGE INTO SCORE_LOOKUP_ITEM_SET
       USING (SELECT V_SCORE_LOOKUP_ID V_SCORE_LOOKUP_ID,
                     R1.ITEM_SET_ID    ITEM_SET_ID
                FROM DUAL) src
       ON (src.ITEM_SET_ID = SCORE_LOOKUP_ITEM_SET_TEST.ITEM_SET_ID)
       WHEN MATCHED THEN
         UPDATE
            SET SCORE_LOOKUP_ITEM_SET_TEST.SCORE_LOOKUP_ID = src.V_SCORE_LOOKUP_ID
       WHEN NOT MATCHED THEN
         INSERT
           (SCORE_LOOKUP_ID, ITEM_SET_ID)
         VALUES
           (src.V_SCORE_LOOKUP_ID, src.ITEM_SET_ID);*/
  
  END LOOP;
  COMMIT;
EXCEPTION
  WHEN OTHERS THEN
    ROLLBACK;
    raise_application_error(-20001,
                            'An error was encountered - PARAMETER ['|| V_PARAMETER||'] ' || SQLCODE ||
                            ' -ERROR- ' || SQLERRM || 'PARAMETER');
  
END POPULATE_SCORE_LOOKUP_ITEM_SET;
/
