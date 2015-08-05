create or replace package OAS_UTILS is

  -- Author  : TCS
  -- Created : 6/13/2011 12:39:19 IST
  -- updated : 2/03/2012 05:39:19 IST
  -- Purpose : For utility

  function GET_SCORING_STATUS_BY_ROSTER(P_ROSTER_ID TEST_ROSTER.TEST_ROSTER_ID%TYPE)
    return VARCHAR2;

  FUNCTION GET_STD_CAREA_SCORING_STATUS(P_ROSTER_ID      TEST_ROSTER.TEST_ROSTER_ID%TYPE,
                                        P_ITEM_SET_TD_ID ITEM_SET.ITEM_SET_ID%TYPE)
    RETURN VARCHAR2;

  FUNCTION GET_STDS_SCORING_STATUS(P_ROSTER_ID TEST_ROSTER.TEST_ROSTER_ID%TYPE)
    RETURN VARCHAR2;

  FUNCTION GET_STDS_ACADEMIC_SCORE(P_ROSTER_ID TEST_ROSTER.TEST_ROSTER_ID%TYPE)
    RETURN VARCHAR2;

  FUNCTION FILTER_COMPLETED_SUBTEST_STR(P_ROSTER_ID         TEST_ROSTER.TEST_ROSTER_ID%TYPE,
                                        IN_CONTENT_AREA_STR VARCHAR2)
    RETURN VARCHAR2;

  PROCEDURE getTestRosterId(pictProdId IN product.product_id%TYPE);

end OAS_UTILS;
/
CREATE OR REPLACE PACKAGE BODY OAS_UTILS IS

  -- Function and procedure implementations
  FUNCTION GET_SCORING_STATUS_BY_ROSTER(P_ROSTER_ID TEST_ROSTER.TEST_ROSTER_ID%TYPE)
    RETURN VARCHAR2 IS
    --<LocalVariable> <Datatype>;
    V_TC_ITEM_SET_ID    ITEM_SET.ITEM_SET_ID%TYPE;
    V_SCORING_COMP_STAT VARCHAR2(10);
  BEGIN
  
    SELECT TAD.ITEM_SET_ID
      INTO V_TC_ITEM_SET_ID
      FROM TEST_ROSTER ROSTER, TEST_ADMIN TAD
     WHERE ROSTER.TEST_ADMIN_ID = TAD.TEST_ADMIN_ID
       AND ROSTER.TEST_ROSTER_ID = P_ROSTER_ID;
  
    SELECT DECODE(COUNT(*), 0, 'CO', 'IN')
      INTO V_SCORING_COMP_STAT
      FROM (SELECT DERIVEDRESPOINT.DATAPOINT_ID,
                   DERIVEDRESPOINT.ITEM_RESPONSE_ID,
                   COUNT(RESPOINT.DATAPOINT_ID) RECORDCOUNT
              FROM (SELECT DISTINCT DP.DATAPOINT_ID     DATAPOINT_ID,
                                    IR.ITEM_RESPONSE_ID ITEM_RESPONSE_ID
                      FROM ITEM_RESPONSE_CR IRS,
                           STUDENT_ITEM_SET_STATUS SISS,
                           ITEM_SET_ANCESTOR ITA,
                           ITEM IT,
                           DATAPOINT DP,
                           ITEM_RESPONSE IR,
                           (SELECT MAX(RESPONSE_SEQ_NUM) SEQ_RESPONSE_ID,
                                   ITEM_SET_ID,
                                   TEST_ROSTER_ID,
                                   ITEM_ID
                              FROM ITEM_RESPONSE
                             WHERE TEST_ROSTER_ID = P_ROSTER_ID
                             GROUP BY ITEM_SET_ID, TEST_ROSTER_ID, ITEM_ID) DERIVEDRS,
                           (SELECT P.DELIVERY_CLIENT_ID
                              FROM PRODUCT P, TEST_ADMIN TA, TEST_ROSTER TR
                             WHERE TA.TEST_ADMIN_ID = TR.TEST_ADMIN_ID
                               AND P.PRODUCT_ID = TA.PRODUCT_ID
                               AND TR.TEST_ROSTER_ID = P_ROSTER_ID) PROD
                     WHERE SISS.TEST_ROSTER_ID = P_ROSTER_ID
                       AND ITA.ANCESTOR_ITEM_SET_ID = V_TC_ITEM_SET_ID
                       AND ITA.ITEM_SET_TYPE = 'TD'
                       AND SISS.TEST_ROSTER_ID = IRS.TEST_ROSTER_ID
                       AND SISS.COMPLETION_STATUS IN ('CO', 'IS', 'IC')
                       AND SISS.VALIDATION_STATUS = 'VA'
                       AND SISS.ABSENT <> 'Y'
                       AND SISS.EXEMPTIONS <> 'Y'
                       AND SISS.ITEM_SET_ID = ITA.ITEM_SET_ID
                       AND ITA.ITEM_SET_ID = IRS.ITEM_SET_ID
                       AND IT.ITEM_ID = IRS.ITEM_ID
                       AND ((UPPER(IT.ITEM_TYPE) = 'CR' AND
                           (IT.ANSWER_AREA IS NULL OR
                           UPPER(IT.ANSWER_AREA) = UPPER('AudioItem'))))
                       AND DP.ITEM_ID = IT.ITEM_ID
                       AND (DECODE(PROD.DELIVERY_CLIENT_ID,
                                   2,
                                   DECODE(IT.ANSWER_AREA,
                                          NULL,
                                          DECODE(DBMS_LOB.GETLENGTH(IRS.CONSTRUCTED_RESPONSE),
                                                 NULL,
                                                 0,
                                                 DECODE(INSTR(IRS.CONSTRUCTED_RESPONSE,
                                                              'CDATA'),
                                                        0,
                                                        0,
                                                        1)),
                                          DECODE(IRS.AUDIO_URL, NULL, 0, 1)),
                                   DECODE(DBMS_LOB.GETLENGTH(IRS.CONSTRUCTED_RESPONSE),
                                          NULL,
                                          0,
                                          DECODE(IT.ANSWER_AREA,
                                                 NULL,
                                                 DECODE(INSTR(IRS.CONSTRUCTED_RESPONSE,
                                                              'CDATA'),
                                                        0,
                                                        0,
                                                        1),
                                                 1)))) = 1
                       AND DERIVEDRS.ITEM_SET_ID = ITA.ITEM_SET_ID
                       AND DERIVEDRS.TEST_ROSTER_ID = SISS.TEST_ROSTER_ID
                       AND DERIVEDRS.ITEM_ID = IT.ITEM_ID
                       AND IR.RESPONSE_SEQ_NUM = DERIVEDRS.SEQ_RESPONSE_ID
                       AND IR.ITEM_SET_ID = ITA.ITEM_SET_ID
                       AND IR.TEST_ROSTER_ID = SISS.TEST_ROSTER_ID
                       AND IR.ITEM_ID = IT.ITEM_ID) DERIVEDRESPOINT,
                   ITEM_RESPONSE_POINTS RESPOINT
             WHERE DERIVEDRESPOINT.DATAPOINT_ID = RESPOINT.DATAPOINT_ID(+)
               AND DERIVEDRESPOINT.ITEM_RESPONSE_ID =
                   RESPOINT.ITEM_RESPONSE_ID(+)
             GROUP BY DERIVEDRESPOINT.DATAPOINT_ID,
                      DERIVEDRESPOINT.ITEM_RESPONSE_ID
            HAVING COUNT(RESPOINT.DATAPOINT_ID) = 0) DERIVED;
  
    RETURN V_SCORING_COMP_STAT;
  END;

  FUNCTION GET_STD_CAREA_SCORING_STATUS(P_ROSTER_ID      TEST_ROSTER.TEST_ROSTER_ID%TYPE,
                                        P_ITEM_SET_TD_ID ITEM_SET.ITEM_SET_ID%TYPE)
    RETURN VARCHAR2 IS
    V_SCORING_COMP_STAT VARCHAR2(3);
  BEGIN
    SELECT DECODE(COUNT(*), 0, 'CO', 'IN')
      INTO V_SCORING_COMP_STAT
      FROM (SELECT DERIVEDRESPOINT.DATAPOINT_ID,
                   DERIVEDRESPOINT.ITEM_RESPONSE_ID,
                   COUNT(RESPOINT.DATAPOINT_ID) RECORDCOUNT
              FROM (SELECT DISTINCT DP.DATAPOINT_ID     DATAPOINT_ID,
                                    IR.ITEM_RESPONSE_ID ITEM_RESPONSE_ID
                      FROM ITEM_RESPONSE_CR IRC,
                           STUDENT_ITEM_SET_STATUS SISS,
                           ITEM_SET ISET,
                           ITEM IT,
                           DATAPOINT DP,
                           ITEM_RESPONSE IR,
                           (SELECT MAX(RESPONSE_SEQ_NUM) SEQ_RESPONSE_ID,
                                   ITEM_SET_ID,
                                   TEST_ROSTER_ID,
                                   ITEM_ID
                              FROM ITEM_RESPONSE
                             WHERE TEST_ROSTER_ID = P_ROSTER_ID
                             GROUP BY ITEM_SET_ID, TEST_ROSTER_ID, ITEM_ID) DERIVEDRS,
                           (SELECT P.DELIVERY_CLIENT_ID
                              FROM PRODUCT P, TEST_ADMIN TA, TEST_ROSTER TR
                             WHERE TA.TEST_ADMIN_ID = TR.TEST_ADMIN_ID
                               AND P.PRODUCT_ID = TA.PRODUCT_ID
                               AND TR.TEST_ROSTER_ID = P_ROSTER_ID) PROD
                     WHERE SISS.TEST_ROSTER_ID = P_ROSTER_ID
                       AND SISS.TEST_ROSTER_ID = IRC.TEST_ROSTER_ID
                       AND IRC.ITEM_SET_ID = ISET.ITEM_SET_ID
                       AND ISET.ITEM_SET_ID = SISS.ITEM_SET_ID
                       AND ISET.ITEM_SET_ID = DERIVEDRS.ITEM_SET_ID
                       AND DERIVEDRS.ITEM_ID = IT.ITEM_ID
                       AND IT.ITEM_ID = IR.ITEM_ID
                       AND IR.ITEM_SET_ID = ISET.ITEM_SET_ID
                       AND IR.RESPONSE_SEQ_NUM = DERIVEDRS.SEQ_RESPONSE_ID
                       AND SISS.TEST_ROSTER_ID = DERIVEDRS.TEST_ROSTER_ID
                       AND SISS.TEST_ROSTER_ID = IR.TEST_ROSTER_ID
                       AND IRC.ITEM_ID = IT.ITEM_ID
                       AND DP.ITEM_ID = IT.ITEM_ID
                       AND ISET.ITEM_SET_ID = P_ITEM_SET_TD_ID
                       AND ISET.ITEM_SET_TYPE = 'TD'
                       AND SISS.COMPLETION_STATUS IN ('CO', 'IS', 'IC')
                       AND SISS.VALIDATION_STATUS = 'VA'
                       AND SISS.ABSENT <> 'Y'
                       AND SISS.EXEMPTIONS <> 'Y'
                       AND ((UPPER(IT.ITEM_TYPE) = 'CR' AND
                           (IT.ANSWER_AREA IS NULL OR
                           UPPER(IT.ANSWER_AREA) = UPPER('AudioItem'))))
                       AND (DECODE(PROD.DELIVERY_CLIENT_ID,
                                   2,
                                   DECODE(IT.ANSWER_AREA,
                                          NULL,
                                          DECODE(DBMS_LOB.GETLENGTH(IRC.CONSTRUCTED_RESPONSE),
                                                 NULL,
                                                 0,
                                                 DECODE(INSTR(IRC.CONSTRUCTED_RESPONSE,
                                                              'CDATA'),
                                                        0,
                                                        0,
                                                        1)),
                                          DECODE(IRC.AUDIO_URL, NULL, 0, 1)),
                                   DECODE(DBMS_LOB.GETLENGTH(IRC.CONSTRUCTED_RESPONSE),
                                          NULL,
                                          0,
                                          DECODE(IT.ANSWER_AREA,
                                                 NULL,
                                                 DECODE(INSTR(IRC.CONSTRUCTED_RESPONSE,
                                                              'CDATA'),
                                                        0,
                                                        0,
                                                        1),
                                                 1)))) = 1) DERIVEDRESPOINT,
                   ITEM_RESPONSE_POINTS RESPOINT
             WHERE DERIVEDRESPOINT.DATAPOINT_ID = RESPOINT.DATAPOINT_ID(+)
               AND DERIVEDRESPOINT.ITEM_RESPONSE_ID =
                   RESPOINT.ITEM_RESPONSE_ID(+)
             GROUP BY DERIVEDRESPOINT.DATAPOINT_ID,
                      DERIVEDRESPOINT.ITEM_RESPONSE_ID
            HAVING COUNT(RESPOINT.DATAPOINT_ID) = 0) DERIVED;
  
    RETURN V_SCORING_COMP_STAT;
  END;

  
  FUNCTION GET_STDS_SCORING_STATUS(P_ROSTER_ID TEST_ROSTER.TEST_ROSTER_ID%TYPE)
    RETURN VARCHAR2 IS
  
    V_ITEMSET_NAMES VARCHAR2(1000) := '';
  
  BEGIN
    SELECT LISTAGG(INITCAP(ISET.ITEM_SET_NAME), ', ') WITHIN GROUP(ORDER BY ISET.ITEM_SET_NAME)
      INTO V_ITEMSET_NAMES
      FROM STUDENT_ITEM_SET_STATUS SIS, ITEM_SET ISET
     WHERE SIS.COMPLETION_STATUS IN ('CO', 'IS', 'IC')
       AND SIS.VALIDATION_STATUS = 'VA'
       AND SIS.ABSENT <> 'Y'
       AND SIS.EXEMPTIONS <> 'Y'
       AND SIS.TEST_ROSTER_ID = P_ROSTER_ID
       AND SIS.ITEM_SET_ID = ISET.ITEM_SET_ID
       AND NOT EXISTS
     (SELECT DERIVEDRESPOINT.DATAPOINT_ID,
                   DERIVEDRESPOINT.ITEM_RESPONSE_ID,
                   COUNT(RESPOINT.DATAPOINT_ID) RECORDCOUNT
              FROM (SELECT DP.DATAPOINT_ID     DATAPOINT_ID,
                           IR.ITEM_RESPONSE_ID ITEM_RESPONSE_ID,
                           ISET.ITEM_SET_ID    ITEM_SET_ID
                      FROM ITEM_RESPONSE_CR IRC,
                           STUDENT_ITEM_SET_STATUS SISS,
                           ITEM_SET ISET,
                           ITEM IT,
                           DATAPOINT DP,
                           ITEM_RESPONSE IR,
                           (SELECT MAX(RESPONSE_SEQ_NUM) SEQ_RESPONSE_ID,
                                   ITEM_SET_ID,
                                   TEST_ROSTER_ID,
                                   ITEM_ID
                              FROM ITEM_RESPONSE
                             WHERE TEST_ROSTER_ID = P_ROSTER_ID
                             GROUP BY ITEM_SET_ID, TEST_ROSTER_ID, ITEM_ID) DERIVEDRS,
                           (SELECT P.DELIVERY_CLIENT_ID
                              FROM PRODUCT P, TEST_ADMIN TA, TEST_ROSTER TR
                             WHERE TA.TEST_ADMIN_ID = TR.TEST_ADMIN_ID
                               AND P.PRODUCT_ID = TA.PRODUCT_ID
                               AND TR.TEST_ROSTER_ID = P_ROSTER_ID) PROD
                     WHERE SISS.TEST_ROSTER_ID = P_ROSTER_ID
                       AND SISS.TEST_ROSTER_ID = IRC.TEST_ROSTER_ID
                       AND IRC.ITEM_SET_ID = ISET.ITEM_SET_ID
                       AND ISET.ITEM_SET_ID = SISS.ITEM_SET_ID
                       AND ISET.ITEM_SET_ID = DERIVEDRS.ITEM_SET_ID
                       AND DERIVEDRS.ITEM_ID = IT.ITEM_ID
                       AND IT.ITEM_ID = IR.ITEM_ID
                       AND IR.ITEM_SET_ID = ISET.ITEM_SET_ID
                       AND IR.RESPONSE_SEQ_NUM = DERIVEDRS.SEQ_RESPONSE_ID
                       AND SISS.TEST_ROSTER_ID = DERIVEDRS.TEST_ROSTER_ID
                       AND SISS.TEST_ROSTER_ID = IR.TEST_ROSTER_ID
                       AND IRC.ITEM_ID = IT.ITEM_ID
                       AND DP.ITEM_ID = IT.ITEM_ID
                       AND ISET.ITEM_SET_TYPE = 'TD'
                       AND SISS.COMPLETION_STATUS IN ('CO', 'IS', 'IC')
                       AND SISS.VALIDATION_STATUS = 'VA'
                       AND SISS.ABSENT <> 'Y'
                       AND SISS.EXEMPTIONS <> 'Y'
                       AND ((UPPER(IT.ITEM_TYPE) = 'CR' AND
                           (IT.ANSWER_AREA IS NULL OR
                           UPPER(IT.ANSWER_AREA) = UPPER('AudioItem'))))
                       AND (DECODE(PROD.DELIVERY_CLIENT_ID,
                                   2,
                                   DECODE(IT.ANSWER_AREA,
                                          NULL,
                                          DECODE(DBMS_LOB.GETLENGTH(IRC.CONSTRUCTED_RESPONSE),
                                                 NULL,
                                                 0,
                                                 DECODE(INSTR(IRC.CONSTRUCTED_RESPONSE,
                                                              'CDATA'),
                                                        0,
                                                        0,
                                                        1)),
                                          DECODE(IRC.AUDIO_URL, NULL, 0, 1)),
                                   DECODE(DBMS_LOB.GETLENGTH(IRC.CONSTRUCTED_RESPONSE),
                                          NULL,
                                          0,
                                          DECODE(IT.ANSWER_AREA,
                                                 NULL,
                                                 DECODE(INSTR(IRC.CONSTRUCTED_RESPONSE,
                                                              'CDATA'),
                                                        0,
                                                        0,
                                                        1),
                                                 1)))) = 1) DERIVEDRESPOINT,
                   ITEM_RESPONSE_POINTS RESPOINT
             WHERE DERIVEDRESPOINT.DATAPOINT_ID = RESPOINT.DATAPOINT_ID(+)
               AND DERIVEDRESPOINT.ITEM_RESPONSE_ID =
                   RESPOINT.ITEM_RESPONSE_ID(+)
               AND DERIVEDRESPOINT.ITEM_SET_ID = SIS.ITEM_SET_ID
             GROUP BY DERIVEDRESPOINT.DATAPOINT_ID,
                      DERIVEDRESPOINT.ITEM_RESPONSE_ID
            HAVING COUNT(RESPOINT.DATAPOINT_ID) = 0)
     GROUP BY SIS.TEST_ROSTER_ID;
  
    RETURN V_ITEMSET_NAMES;
  END;

  FUNCTION GET_STDS_ACADEMIC_SCORE(P_ROSTER_ID TEST_ROSTER.TEST_ROSTER_ID%TYPE)
    RETURN VARCHAR2 IS
    V_SCORING_COMP_STAT VARCHAR2(3) := 'IN';
    V_ITEMSET_NAMES     VARCHAR2(10000) := '';
    CURSOR CUR_GET_TD_FROM_SIS(CP_ROSTER_ID TEST_ROSTER.TEST_ROSTER_ID%TYPE) IS
      SELECT DISTINCT SIS.ITEM_SET_ID ITEM_SET_ID,
                      INITCAP(ISET.ITEM_SET_NAME) ITEM_SET_NAME
        FROM STUDENT_ITEM_SET_STATUS SIS, ITEM_SET ISET
       WHERE SIS.COMPLETION_STATUS IN ('CO', 'IS', 'IC')
         AND SIS.VALIDATION_STATUS = 'VA'
         AND SIS.ABSENT <> 'Y'
         AND SIS.EXEMPTIONS <> 'Y'
         AND SIS.TEST_ROSTER_ID = CP_ROSTER_ID
         AND SIS.ITEM_SET_ID = ISET.ITEM_SET_ID;
  
    CURSOR CUR_GET_OBJECTIVE_NAME(CURP_ROSTER_ID TEST_ROSTER.TEST_ROSTER_ID%TYPE, CURP_ITEM_SET_ID ITEM_SET.ITEM_SET_ID%TYPE) IS
      SELECT DISTINCT SEC.ITEM_SET_ID   AS SECONDARYOBJECTIVEID,
                      SEC.ITEM_SET_NAME AS SECONDARYOBJECTIVENAME
        FROM ITEM,
             ITEM_SET SEC,
             ITEM_SET_CATEGORY SECCAT,
             ITEM_SET_ANCESTOR SECISA,
             ITEM_SET PRIM,
             ITEM_SET_CATEGORY PRIMCAT,
             ITEM_SET_ANCESTOR PRIMISA,
             ITEM_SET_ITEM SECISI,
             ITEM_SET_ANCESTOR TCISA,
             ITEM_SET_ITEM TCISI,
             TEST_ROSTER ROS,
             TEST_ADMIN ADM,
             TEST_CATALOG TC,
             PRODUCT PROD,
             ITEM_SET TD
       WHERE ROS.TEST_ROSTER_ID = CURP_ROSTER_ID
         AND TD.ITEM_SET_ID = CURP_ITEM_SET_ID
         AND ADM.TEST_ADMIN_ID = ROS.TEST_ADMIN_ID
         AND TC.TEST_CATALOG_ID = ADM.TEST_CATALOG_ID
         AND PROD.PRODUCT_ID = TC.PRODUCT_ID
         AND ITEM.ACTIVATION_STATUS = 'AC'
         AND TC.ACTIVATION_STATUS = 'AC'
         AND SEC.ITEM_SET_ID = SECISA.ANCESTOR_ITEM_SET_ID
         AND SEC.ITEM_SET_TYPE = 'RE'
         AND SECISA.ITEM_SET_ID = SECISI.ITEM_SET_ID
         AND ITEM.ITEM_ID = SECISI.ITEM_ID
         AND TCISI.ITEM_ID = ITEM.ITEM_ID
         AND TCISA.ITEM_SET_ID = TCISI.ITEM_SET_ID
         AND ADM.ITEM_SET_ID = TCISA.ANCESTOR_ITEM_SET_ID
         AND SECCAT.ITEM_SET_CATEGORY_ID = SEC.ITEM_SET_CATEGORY_ID
         AND SECCAT.ITEM_SET_CATEGORY_LEVEL =
             PROD.SEC_SCORING_ITEM_SET_LEVEL
         AND PRIMISA.ITEM_SET_ID = SEC.ITEM_SET_ID
         AND PRIM.ITEM_SET_ID = PRIMISA.ANCESTOR_ITEM_SET_ID
         AND PRIMCAT.ITEM_SET_CATEGORY_ID = PRIM.ITEM_SET_CATEGORY_ID
         AND PRIMCAT.ITEM_SET_CATEGORY_LEVEL = PROD.SCORING_ITEM_SET_LEVEL
         AND TD.ITEM_SET_ID = TCISI.ITEM_SET_ID
         AND TD.SAMPLE = 'F'
         AND (TD.ITEM_SET_LEVEL != 'L' OR PROD.PRODUCT_TYPE = 'TL')
         AND SECCAT.FRAMEWORK_PRODUCT_ID = PROD.PARENT_PRODUCT_ID
       GROUP BY SEC.ITEM_SET_ID, SEC.ITEM_SET_NAME;
  
  BEGIN
  
    FOR R1 IN CUR_GET_TD_FROM_SIS(P_ROSTER_ID)
    LOOP
      V_SCORING_COMP_STAT := GET_STD_CAREA_SCORING_STATUS(P_ROSTER_ID,
                                                          R1.ITEM_SET_ID);
      IF (V_SCORING_COMP_STAT = 'CO')
      THEN
      
        FOR R2 IN CUR_GET_OBJECTIVE_NAME(P_ROSTER_ID, R1.ITEM_SET_ID)
        LOOP
          IF (LENGTH(V_ITEMSET_NAMES) <> 0)
          THEN
            V_ITEMSET_NAMES := V_ITEMSET_NAMES || ', ' ||
                               R2.SECONDARYOBJECTIVENAME;
          ELSE
            V_ITEMSET_NAMES := R2.SECONDARYOBJECTIVENAME;
          END IF;
        END LOOP;
      END IF;
    END LOOP;
    IF (LENGTH(V_ITEMSET_NAMES) <> 0)
    THEN
      IF INSTR(V_ITEMSET_NAMES, 'Reading', -1) > 0
      THEN
        V_ITEMSET_NAMES := V_ITEMSET_NAMES || ', Reading Academic';
      END IF;
      IF INSTR(V_ITEMSET_NAMES, 'Listening', -1) > 0
      THEN
        V_ITEMSET_NAMES := V_ITEMSET_NAMES || ', Listening Academic';
      END IF;
      IF INSTR(V_ITEMSET_NAMES, 'Speaking', -1) > 0
      THEN
        V_ITEMSET_NAMES := V_ITEMSET_NAMES || ', Speaking Academic';
      END IF;
      IF INSTR(V_ITEMSET_NAMES, 'Writing', -1) > 0
      THEN
        V_ITEMSET_NAMES := V_ITEMSET_NAMES || ', Writing Academic';
      END IF;
    END IF;
    RETURN V_ITEMSET_NAMES;
  END;

  FUNCTION FILTER_COMPLETED_SUBTEST_STR(P_ROSTER_ID         TEST_ROSTER.TEST_ROSTER_ID%TYPE,
                                        IN_CONTENT_AREA_STR VARCHAR2)
    RETURN VARCHAR2 IS
    V_ITEMSET_NAMES VARCHAR2(1000) := '';
  
    CURSOR CUR_GET_TD_FROM_SIS(CP_ROSTER_ID TEST_ROSTER.TEST_ROSTER_ID%TYPE) IS
      SELECT DISTINCT SISS.ITEM_SET_ID ITEM_SET_ID,
                      INITCAP(ISETTD.ITEM_SET_NAME) ITEM_SET_NAME,
                      (SELECT DECODE(COUNT(1), 0, 'N', 'Y')
                         FROM LASLINK_CONTENT_AREA_FACT@IRS LCAF
                        WHERE LCAF.SESSIONID = TA.TEST_ADMIN_ID
                          AND LCAF.STUDENTID = TR.STUDENT_ID
                          AND LCAF.CONTENT_AREAID =
                              TO_NUMBER(PROD.PRODUCT_ID || RE.ITEM_SET_ID)) AS REPORTPRESENT
        FROM TEST_ADMIN              TA,
             TEST_ROSTER             TR,
             STUDENT_ITEM_SET_STATUS SISS,
             ITEM_SET_CATEGORY       ISC,
             ITEM_SET_ITEM           SUBI,
             ITEM_SET_ITEM           REI,
             ITEM                    I,
             ITEM_SET                ISET,
             ITEM_SET_ANCESTOR       ISA,
             ITEM_SET                ISETTD,
             ITEM_SET                RE,
             PRODUCT                 PROD
       WHERE TR.TEST_ROSTER_ID = P_ROSTER_ID
         AND TA.TEST_ADMIN_ID = TR.TEST_ADMIN_ID
         AND TR.TEST_ROSTER_ID = SISS.TEST_ROSTER_ID
         AND SISS.COMPLETION_STATUS IN ('CO', 'IS', 'IC')
         AND SISS.VALIDATION_STATUS = 'VA'
         AND SISS.ABSENT <> 'Y'
         AND SISS.EXEMPTIONS <> 'Y'
         AND SISS.ITEM_SET_ID = ISETTD.ITEM_SET_ID
         AND ISETTD.ITEM_SET_ID = SUBI.ITEM_SET_ID
         AND SUBI.ITEM_ID = I.ITEM_ID
         AND I.ITEM_ID = REI.ITEM_ID
         AND REI.ITEM_SET_ID = ISET.ITEM_SET_ID
         AND ISET.ITEM_SET_TYPE = 'RE'
         AND ISA.ITEM_SET_ID = ISET.ITEM_SET_ID
         AND ISA.ANCESTOR_ITEM_SET_ID = RE.ITEM_SET_ID
         AND PROD.PRODUCT_ID = TA.PRODUCT_ID
         AND ISC.FRAMEWORK_PRODUCT_ID = PROD.PARENT_PRODUCT_ID
         AND ISC.ITEM_SET_CATEGORY_LEVEL = PROD.CONTENT_AREA_LEVEL
         AND RE.ITEM_SET_CATEGORY_ID = ISC.ITEM_SET_CATEGORY_ID
         AND UPPER(RE.ITEM_SET_NAME) NOT IN ('ORAL', 'COMPREHENSION');
  
    CURSOR CUR_GET_COMPLETED_CAREAS(CONTENT_AREA_STR VARCHAR2) IS
      select distinct trim(substr(CONTENT_AREA_STR,
                                  instr(CONTENT_AREA_STR, ',', 1, level) + 1,
                                  instr(CONTENT_AREA_STR, ',', 1, level + 1) -
                                  instr(CONTENT_AREA_STR, ',', 1, level) - 1)) as COMPLETED_SUBTEST
        from dual
      connect by level <= length(CONTENT_AREA_STR) -
                 length(replace(CONTENT_AREA_STR, ',', '')) - 1;
  
  BEGIN
  
    FOR R1 IN CUR_GET_TD_FROM_SIS(P_ROSTER_ID)
    LOOP
      FOR R2 IN CUR_GET_COMPLETED_CAREAS(',' || IN_CONTENT_AREA_STR || ',')
      LOOP
        IF (R2.COMPLETED_SUBTEST = R1.ITEM_SET_NAME) AND
           R1.REPORTPRESENT = 'Y'
        THEN
          IF (LENGTH(V_ITEMSET_NAMES) <> 0)
          THEN
            V_ITEMSET_NAMES := V_ITEMSET_NAMES || ', ' || R1.ITEM_SET_NAME;
          ELSE
            V_ITEMSET_NAMES := R1.ITEM_SET_NAME;
          END IF;
          EXIT;
        END IF;
      END LOOP;
    END LOOP;
  
    RETURN V_ITEMSET_NAMES;
  END;

  /*
  **---------------------------------------------------------------------
  ** purpose: Rescore
  ** require: %
  ** ensure : %
  ** special: %
  ** descr. : This procedure gets the roster ids that need to be rescored
  ** 10.07.2012 TCS Initial Implementation
  **---------------------------------------------------------------------
  */

  PROCEDURE GETTESTROSTERID(PICTPRODID IN PRODUCT.PRODUCT_ID%TYPE) IS
    /*Declaring error handling variables*/
    VCSTATEMENT VARCHAR2(200);
    OBJECTIVESCOREISNULL EXCEPTION;
    /*Declaring others variables*/
    OBJECTIVESCORE STUDENT_ITEM_SET_STATUS.OBJECTIVE_SCORE%TYPE;
    CNT            INTEGER;
    ROSTERID       TEST_ROSTER.TEST_ROSTER_ID%TYPE;
    ISRESCORED     INTEGER;
  
    /*The cursor select the testRosterIds for rescoring*/
    CURSOR CRSGETROSTERID IS
      SELECT DISTINCT TR.TEST_ROSTER_ID    ROSTERID,
                      SISS.OBJECTIVE_SCORE OBJSCORE
        FROM TEST_ROSTER TR, TEST_ADMIN ADM, STUDENT_ITEM_SET_STATUS SISS
       WHERE ADM.PRODUCT_ID = PICTPRODID
         AND ADM.TEST_ADMIN_ID = TR.TEST_ADMIN_ID
         AND TR.TEST_ROSTER_ID = SISS.TEST_ROSTER_ID
         AND SISS.COMPLETION_STATUS = 'CO'
         AND SISS.OBJECTIVE_SCORE IS NOT NULL;
  
    CURSOR CRSGETRESCORED IS
      SELECT COUNT(TR.RESCORED)
        FROM TEST_ROSTER_TEMP TR
       WHERE TR.TEST_ROSTER_ID = ROSTERID;
  
  BEGIN
  
    VCSTATEMENT := 'open crsGetRosterId';
  
    FOR RECOBJSCOREDATA IN CRSGETROSTERID
    LOOP
      CNT            := 0;
      OBJECTIVESCORE := RECOBJSCOREDATA.OBJSCORE;
      ROSTERID       := RECOBJSCOREDATA.ROSTERID;
      OPEN CRSGETRESCORED;
      FETCH CRSGETRESCORED
        INTO ISRESCORED;
      IF ISRESCORED = 0
      THEN
        IF OBJECTIVESCORE IS NULL
        THEN
          RAISE OBJECTIVESCOREISNULL;
        END IF;
      
        IF INSTR(OBJECTIVESCORE, '|') > 0
        THEN
          OBJECTIVESCORE := SUBSTR(OBJECTIVESCORE,
                                   1,
                                   INSTR(OBJECTIVESCORE, '|') - 1);
        END IF;
      
        WHILE INSTR(OBJECTIVESCORE, ',') > 0
        LOOP
          OBJECTIVESCORE := SUBSTR(OBJECTIVESCORE,
                                   INSTR(OBJECTIVESCORE, ',') + 1,
                                   LENGTH(OBJECTIVESCORE));
          CNT            := CNT + 1;
        END LOOP;
      
        IF CNT != 5
        THEN
          IF CNT IS NOT NULL
          THEN
            INSERT INTO TEST_ROSTER_TEMP TEMP
            VALUES
              (RECOBJSCOREDATA.ROSTERID, 'F');
            COMMIT;
          END IF;
        END IF;
      END IF;
      CLOSE CRSGETRESCORED;
    END LOOP;
  
  EXCEPTION
    WHEN OBJECTIVESCOREISNULL THEN
      IF CRSGETROSTERID%ISOPEN
      THEN
        CLOSE CRSGETROSTERID;
      END IF;
  END GETTESTROSTERID;

--begin
-- Initialization
--<Statement>;
END OAS_UTILS;
/