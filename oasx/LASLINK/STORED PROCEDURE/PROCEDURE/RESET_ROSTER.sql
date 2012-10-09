create or replace PROCEDURE RESET_ROSTER ( ROSTERS_IN IN ROSTERARRAY, RESULTOUT OUT VARCHAR2 ) AS

V_SC_SUBTEST_COUNT NUMBER := 0;
v_total_subtest number := 0;
V_SEQ_VAL number := 0;

begin
  
  select seq_audit_id.nextval into V_SEQ_VAL from dual;
  
  FOR i IN 1..ROSTERS_IN.COUNT LOOP
  
    FOR j IN 1..ROSTERS_IN(I).SUBTEST_IDS.COUNT LOOP
     
    --FOR cur_student_item_status_rec IN cur_student_item_set_status (ROSTERS_IN(i).roster_id, V_ITEM_SET_ID) LOOP
      INSERT 
        into item_response_audit 
             (item_response_id, item_set_id, test_roster_id, response
             , response_method, response_elapsed_time, response_seq_num
             , created_date_time, item_id, ext_answer_choice_id
             , student_marked, created_by, audit_id)
      select item_response_id, item_set_id, test_roster_id, response
             , response_method, response_elapsed_time, response_seq_num
             , created_date_time, item_id, ext_answer_choice_id
             , student_marked, created_by, V_SEQ_VAL
        FROM item_response 
       WHERE test_roster_id = rosters_in(i).roster_id 
         AND item_set_id = ROSTERS_IN(i).SUBTEST_IDS(j);
         
      INSERT 
        into item_response_points_audit 
             (points, item_response_points_seq_num, datapoint_id, condition_code_id
             , created_by, created_date_time, item_response_id, comments, audit_id)
      select points, item_response_points_seq_num, datapoint_id, condition_code_id
            , created_by, created_date_time, item_response_id, comments, V_SEQ_VAL
        FROM item_response_points 
       WHERE item_response_id IN (
      SELECT item_response_id FROM item_response 
       where test_roster_id = rosters_in(i).roster_id 
          AND item_set_id = ROSTERS_IN(i).SUBTEST_IDS(j));
      
      DELETE 
        FROM item_response_points
       WHERE item_response_id IN (
      SELECT item_response_id FROM item_response 
       where test_roster_id = rosters_in(i).roster_id 
        AND item_set_id = ROSTERS_IN(i).SUBTEST_IDS(j));
      
      DELETE 
        FROM Item_Response 
       WHERE test_roster_id = rosters_in(i).roster_id 
         AND item_set_id = ROSTERS_IN(i).SUBTEST_IDS(j);
      
      INSERT 
        into item_response_cr_audit 
             (test_roster_id, item_set_id, item_id, constructed_response, audit_id)
      select test_roster_id, item_set_id, item_id, constructed_response, V_SEQ_VAL
        FROM item_response_cr 
       WHERE test_roster_id = rosters_in(i).roster_id 
         AND item_set_id = ROSTERS_IN(I).SUBTEST_IDS(j);
      
      INSERT 
        into item_response_points_audit 
             (points, item_response_points_seq_num, datapoint_id, condition_code_id
             , created_by, created_date_time, item_response_id, comments, audit_id)
      select points, item_response_points_seq_num, datapoint_id, condition_code_id
            , created_by, created_date_time, item_response_id, comments, V_SEQ_VAL
        FROM item_response_points 
       WHERE item_response_id IN (
      SELECT item_response_id FROM item_response_cr
       where test_roster_id = rosters_in(i).roster_id 
          AND item_set_id = ROSTERS_IN(i).SUBTEST_IDS(j));
      
      DELETE 
        FROM item_response_points
       WHERE item_response_id IN (
      SELECT item_response_id FROM item_response_cr
       WHERE test_roster_id = rosters_in(i).roster_id 
         AND item_set_id = ROSTERS_IN(I).SUBTEST_IDS(j));
       
      DELETE 
        FROM item_response_cr 
       WHERE test_roster_id = rosters_in(i).roster_id 
         AND item_set_id = ROSTERS_IN(i).SUBTEST_IDS(j);
          
    UPDATE student_item_set_status 
       SET completion_status = 'SC' 
         , start_date_time = NULL 
         , completion_date_time = NULL
         , raw_score = NULL
         , max_score = NULL
         , unscored = NULL
         , exemptions = NULL
         , absent = NULL
         , ability_score = NULL
         , sem_score = NULL
         , objective_score = NULL
         , tms_update = NULL
    WHERE test_roster_id = rosters_in(i).roster_id 
      AND item_set_id = ROSTERS_IN(i).SUBTEST_IDS(j);
       
   -- END LOOP;
   
    /* UPDATE test_roster 
         SET test_completion_status = 'IS' 
          -- , start_date_time = NULL 
           --, completion_date_time = NULL
          -- , updated_date_time = NULL
          -- , last_login_date_time = NULL
          -- , restart_number = NULL
          -- , last_mseq = NULL
           --, tms_update = NULL
      WHERE test_roster_id = ROSTERS_IN(i).roster_id;*/
   END LOOP;  
   
    UPDATE test_roster 
       SET test_completion_status = 'IS' 
     WHERE test_roster_id = rosters_in(i).roster_id ;
  
  end loop;
  resultout := 'Success:' || V_SEQ_VAL;
  COMMIT;   
  
EXCEPTION
  
  WHEN OTHERS THEN
  RESULTOUT := SQLERRM;
  
END RESET_ROSTER;