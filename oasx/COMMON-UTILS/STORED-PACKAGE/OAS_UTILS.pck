create or replace package OAS_UTILS is

  -- Author  : TCS
  -- Created : 6/13/2011 12:39:19 PM
  -- Purpose : For utility

  function GET_SCORING_STATUS_BY_ROSTER(P_ROSTER_ID TEST_ROSTER.TEST_ROSTER_ID%TYPE)
    return VARCHAR2;

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

--begin
-- Initialization
--<Statement>;
END OAS_UTILS;
/
