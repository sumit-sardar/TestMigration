create or replace package OAS_UTILS is

  -- Author  : TCS
  -- Created : 6/13/2011 12:39:19 IST
  -- updated : 2/03/2012 05:39:19 IST
  -- Purpose : For utility

  function GET_SCORING_STATUS_BY_ROSTER(P_ROSTER_ID TEST_ROSTER.TEST_ROSTER_ID%TYPE)
    return VARCHAR2;
    
    FUNCTION GET_STD_CAREA_SCORING_STATUS(P_ROSTER_ID TEST_ROSTER.TEST_ROSTER_ID%TYPE,
                                         P_ITEM_SET_TD_ID ITEM_SET.ITEM_SET_ID%TYPE)
    RETURN VARCHAR2;
    
    FUNCTION GET_STDS_SCORING_STATUS(P_ROSTER_ID TEST_ROSTER.TEST_ROSTER_ID%TYPE)
      RETURN VARCHAR2;

end OAS_UTILS;
/
CREATE OR REPLACE PACKAGE BODY OAS_UTILS IS



  -- Function and procedure implementations
  FUNCTION GET_SCORING_STATUS_BY_ROSTER(P_ROSTER_ID TEST_ROSTER.TEST_ROSTER_ID%TYPE)
    RETURN VARCHAR2 is
    --<LocalVariable> <Datatype>;
    V_TC_ITEM_SET_ID ITEM_SET.ITEM_SET_ID%TYPE;
    V_SCORING_COMP_STAT VARCHAR2(10);
  BEGIN

    SELECT TAD.ITEM_SET_ID
      INTO V_TC_ITEM_SET_ID
      FROM TEST_ROSTER ROSTER, TEST_ADMIN TAD
     WHERE ROSTER.TEST_ADMIN_ID = TAD.TEST_ADMIN_ID
       AND ROSTER.TEST_ROSTER_ID = P_ROSTER_ID;

    select decode(count(*), 0, 'CO', 'IN') INTO V_SCORING_COMP_STAT
      from (select derivedResPoint.datapoint_id,
                   derivedResPoint.item_response_id,
                   count(resPoint.datapoint_id) recordcount
              from (select distinct dp.datapoint_id     datapoint_id,
                                    ir.item_response_id item_response_id
                      from item_response_cr irs,
                           student_item_set_status siss,
                           item_set_ancestor ita,
                           item it,
                           datapoint dp,
                           item_response ir,
                           (select max(response_seq_num) seq_response_id,
                                   item_set_id,
                                   test_roster_id,
                                   item_id
                              from item_response
                             where test_roster_id = P_ROSTER_ID
                             group by item_set_id, test_roster_id, item_id) derivedrs
                     where siss.test_roster_id = P_ROSTER_ID
                       and ita.ancestor_item_set_id = V_TC_ITEM_SET_ID
                       and ita.item_set_type = 'TD'
                       and siss.test_roster_id = irs.test_roster_id
                       and siss.completion_status in ('CO', 'IS','IC')
                       and SISS.validation_status = 'VA'
                       AND SISS.ABSENT <> 'Y'
                       AND SISS.EXEMPTIONS <> 'Y'
                       AND SISS.ITEM_SET_ID = ita.item_set_id
                       and ita.item_set_id = irs.item_set_id
                       and it.item_id = irs.item_id
                       and ((upper(it.item_type) = 'CR' and
                           (it.answer_area is null or
                           upper(it.answer_area) = upper('AudioItem'))))
                       and dp.item_id = it.item_id
                       and (irs.constructed_response is not null and
                           (decode(it.answer_area,
                                    null,
                                    decode(instr(constructed_response, 'CDATA'),
                                           0,
                                           0,
                                           1),
                                    1)) = 1)
                       and derivedrs.item_set_id = ita.item_set_id
                       and derivedrs.test_roster_id = siss.test_roster_id
                       and derivedrs.item_id = it.item_id
                       and ir.response_seq_num = derivedrs.seq_response_id
                       and ir.item_set_id = ita.item_set_id
                       and ir.test_roster_id = siss.test_roster_id
                       and ir.item_id = it.item_id) derivedResPoint,
                   item_response_points resPoint
             where derivedResPoint.datapoint_id = resPoint.datapoint_id(+)
               and derivedResPoint.item_response_id =
                   resPoint.item_response_id(+)
             group by derivedResPoint.datapoint_id,
                      derivedResPoint.item_response_id
            having count(resPoint.datapoint_id) = 0) derived;


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
                             GROUP BY ITEM_SET_ID, TEST_ROSTER_ID, ITEM_ID) DERIVEDRS
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
                          
                       AND (IRC.CONSTRUCTED_RESPONSE IS NOT NULL AND
                           (DECODE(IT.ANSWER_AREA,
                                    NULL,
                                    DECODE(INSTR(CONSTRUCTED_RESPONSE, 'CDATA'),
                                           0,
                                           0,
                                           1),
                                    1)) = 1)
                    
                    ) DERIVEDRESPOINT,
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
      V_SCORING_COMP_STAT VARCHAR2(3) :='IN';
      V_ITEMSET_NAMES VARCHAR2(100) := '';
     CURSOR CUR_GET_TD_FROM_SIS(CP_ROSTER_ID TEST_ROSTER.TEST_ROSTER_ID%TYPE) IS SELECT DISTINCT SIS.ITEM_SET_ID  ITEM_SET_ID, INITCAP(ISET.ITEM_SET_NAME) ITEM_SET_NAME FROM STUDENT_ITEM_SET_STATUS SIS, ITEM_SET ISET WHERE SIS.COMPLETION_STATUS IN ('CO', 'IS', 'IC')   AND SIS.ABSENT <> 'Y'   AND SIS.EXEMPTIONS <> 'Y' AND SIS.TEST_ROSTER_ID = CP_ROSTER_ID AND SIS.ITEM_SET_ID = ISET.ITEM_SET_ID;
     
   BEGIN
        
        FOR R1 IN CUR_GET_TD_FROM_SIS(P_ROSTER_ID) LOOP
            V_SCORING_COMP_STAT := GET_STD_CAREA_SCORING_STATUS(P_ROSTER_ID, R1.ITEM_SET_ID );
            IF (V_SCORING_COMP_STAT = 'CO') THEN
              --/V_ITEMSET_NAMES := CONCAT(V_ITEMSET_NAMES, R1.ITEM_SET_NAME);
              --V_ITEMSET_NAMES := CONCAT(V_ITEMSET_NAMES, ',');
               IF(LENGTH(V_ITEMSET_NAMES)<> 0) THEN 
                  V_ITEMSET_NAMES := V_ITEMSET_NAMES|| ', ' ||R1.ITEM_SET_NAME;
               ELSE 
                V_ITEMSET_NAMES := R1.ITEM_SET_NAME;
               END IF;
            END IF;
        END LOOP;
        
     
        
   RETURN V_ITEMSET_NAMES;
   END;
  
  

--begin
-- Initialization
--<Statement>;
END OAS_UTILS;
/
