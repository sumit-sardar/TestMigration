CREATE OR REPLACE PROCEDURE Sp_Response_OAS_EXPORT
IS
   --  This Procedure is Used to Load the concatenated string of all the TS records,
   --    Concatenated string of all the Response arrays and Score arrays for a particular Test Catalog Item_set_id of the Student --
   -- AUTHOR: Wipro Offshore Team --
   -- Modifed: CTB Puru introduced bulk collect to improve performance durinf FCAT 2006 spring  --
   -- CREATED DATE: 06th March 2006 --
   v_ra            VARCHAR2 (2000) := '';
   -- Variable to hold the Response Array --
   v_sa            VARCHAR2 (2000) := '';
   --Variable to hold the Score Array --
   v_ea            VARCHAR2 (2000) := '';
   --Variable to hold the Erasure Array --
   v_sum           INTEGER         := 0;
   --Variable to hold the score of the student --
--   v_ts_id         INTEGER         := 0;
   --Variable to hold the Test Section ID --
   v_subject       VARCHAR2 (100)  := '';          --Variable to hold Subject
   --   v_subject1     VARCHAR2 (2000) := '';         --Variable to hold Subject
   v_secttyp       VARCHAR2 (2)    := 'SR';
   -- Variable to hold Selection Type, BY Default is 'SR" --
   v_invflag       VARCHAR2 (1)    := '0';     -- Variable to hold InvFlag --
   v_suppflag      VARCHAR2 (1)    := '0';    -- Variable to hold SuppFlag --
   v_omitflag      VARCHAR2 (1)    := '0';    -- Variable to hold OmitFlag --
   v_flipflag      VARCHAR2 (1)    := '0';    -- Variable to hold FlipFlag --
   v_erasurecnt    VARCHAR2 (1);

   -- := '0';  -- Variable to hold ErasureCnt --
   TYPE sr_resp_rec IS RECORD (
      td_subject         VARCHAR2 (60),
      ques_no            INTEGER,
      response           VARCHAR2 (1),
      correct_answer     VARCHAR2 (1),
      score              VARCHAR2 (1),
      response_seq_num   INTEGER,
      item_id            VARCHAR2 (32),
      actual_response    VARCHAR2 (1)
   );

   TYPE sr_resp_tab IS TABLE OF sr_resp_rec
      INDEX BY BINARY_INTEGER;

   v_sr_resp_tab   sr_resp_tab;

   TYPE gi_resp_rec IS RECORD (
      form_element_id   VARCHAR2 (30),	   	  --FCAT  Spring 2008  --- old field external_id       VARCHAR2 (30),
      gridded_columns   INTEGER,
      ques_no           INTEGER,
      omit              INTEGER,
      response          VARCHAR2 (10)
   );

   TYPE gi_resp_tab IS TABLE OF gi_resp_rec
      INDEX BY BINARY_INTEGER;

   v_gi_resp_tab   gi_resp_tab;

   -- Cursor to select all ST records in STG_EISS_TB
   CURSOR c_st
   IS
      SELECT customer_id, product_id, test_roster_id, ou_id, sh_id, gh_id,
             sr_id, st_id, grade, sort_order,
             Erasure_Count (test_roster_id) erasure_cnt
        FROM stg_eiss_tb
       WHERE level_type = 'ST';

  --  Cursor to select all TD/RA,SA records

  CURSOR c_td(in_test_roster_id NUMBER, in_student_id NUMBER, in_tc_item_set_id NUMBER) IS
  WITH main AS
     (
        --Extract all the Item_Set_Ids, Item_Ids along with their Correct answers and Order them as to how they were ordered at the test
        SELECT tr.test_roster_id AS test_roster_id,
               istd.item_set_id td_item_set_id, i.item_id,
               istd.subject td_subject, correct_answer, item_sort_order,
               isp.item_set_sort_order td_item_set_sort_order,
               si.item_set_order si_item_set_order
          FROM TEST_ADMIN ta,
               TEST_CATALOG tc,
               ITEM_SET_ITEM isi,
               ITEM i,
               ITEM_SET istd,
               ITEM_SET_PARENT isp,
               STUDENT_ITEM_SET_STATUS si,
               TEST_ROSTER tr,
               STUDENT st,
               stg_param_winscr_tb parm,
               ORG_NODE_STUDENT ons
         WHERE ta.product_id = tc.product_id
           AND ta.test_catalog_id = tc.test_catalog_id
           AND tr.test_admin_id = ta.test_admin_id
           AND tr.customer_id = ta.customer_id
           AND isi.item_set_id = istd.item_set_id
           AND isi.item_id = i.item_id
           AND istd.item_set_id = isp.item_set_id
           AND istd.SAMPLE = 'F'
           AND tr.test_roster_id = si.test_roster_id
           AND tr.student_id = st.student_id
           AND si.item_set_id = istd.item_set_id
           AND ta.product_id = parm.product_id
           AND tr.customer_id = parm.customer_id
           AND isp.parent_item_set_id IN
                                        (
                  SELECT ITEM_SET_PARENT.item_set_id
                    FROM ITEM_SET_PARENT, TEST_CATALOG
                   WHERE ITEM_SET_PARENT.parent_item_set_id =
                                                      TEST_CATALOG.item_set_id
                     AND TEST_CATALOG.item_set_id = in_tc_item_set_id
                     AND ITEM_SET_PARENT.item_set_type = 'TS'
                     AND TEST_CATALOG.product_id = parm.product_id
                                                                  )
           -- in_ts_id
           AND tr.student_id = in_student_id
           AND i.item_type = 'SR'
           AND ta.customer_id = parm.customer_id
           AND ta.product_id = parm.product_id
           AND tr.student_id = ons.student_id
           AND tr.org_node_id = ons.org_node_id
           AND tr.test_roster_id = in_test_roster_id
                                         )
SELECT   td_subject, item_sort_order AS ques_no,
         DECODE (response,
                 'A', '1',
                 'B', '2',
                 'C', '3',
                 'D', '4',
                 'E', '5',
                 '*'
                ) AS response,
         DECODE (correct_answer,
                 'A', '1',
                 'B', '2',
                 'C', '3',
                 'D', '4',
                 'E', '5',
                 '*'
                ) AS correct_answer,
         (CASE
             WHEN (response = correct_answer)
                THEN '1'
             ELSE '0'
          END) AS score, ir.response_seq_num, m.item_id, response actual_response
    FROM main m, ITEM_RESPONSE ir
   WHERE m.test_roster_id = ir.test_roster_id(+)
     --Outer Joins performed to account for all Items, Item sets that were unanswered or omitted --
     AND m.td_item_set_id = ir.item_set_id(+)
     AND m.item_id = ir.item_id(+)
     AND (   ir.response_seq_num IS NULL
          OR ir.response_seq_num =
                (SELECT   MAX (irm.response_seq_num)
                     FROM ITEM_RESPONSE irm
                    WHERE m.test_roster_id = irm.test_roster_id
                      AND m.td_item_set_id = irm.item_set_id
                      AND m.item_id = irm.item_id
                 GROUP BY m.test_roster_id, m.td_item_set_id, m.item_id)
         )
ORDER BY si_item_set_order, td_item_set_sort_order, ques_no;


  --  Cursor to select Grided Responses
  CURSOR c_gi(in_test_roster_id NUMBER, IN_STUDENT NUMBER, IN_TC_ID NUMBER) IS WITH main AS(
    SELECT tr.test_roster_id AS test_roster_id,
           istd.item_set_id td_item_set_id,
           i.item_id,
		              isp_td_ts.item_set_sort_order td_item_set_sort_order,
           si.item_set_order si_item_set_order,
           item_sort_order,
           gde.form_element_id,			   --FCAT Spring 2008 -- OLD Field i.external_id,
		   i.gridded_columns
      FROM TEST_ADMIN              ta,
           TEST_CATALOG            tc,
           ITEM_SET_ITEM           isi,
           ITEM                    i,
           ITEM_SET                istd,
           ITEM_SET_PARENT         isp_td_ts,
           STUDENT_ITEM_SET_STATUS si,
           TEST_ROSTER             tr,
           STUDENT                 st,
           stg_param_winscr_tb parm,
           ORG_NODE_STUDENT        ons,
		   GR_DELIVERY_ELEMENT  gde	   		          --Table added for FCAT spring 2008
     WHERE 1 = 1
       AND i.item_id=gde.item_id			 		 --FCAT Sprin 2008
	   AND tr.form_assignment=gde.form_assignment	 --FCAT Sprin 2008
	   AND ta.product_id = tc.product_id
       AND ta.test_catalog_id = tc.test_catalog_id
       AND tr.test_admin_id = ta.test_admin_id
       AND tr.customer_id = ta.customer_id
       AND isi.item_set_id = istd.item_set_id
       AND isi.item_id = i.item_id
       AND istd.SAMPLE = 'F'
       AND istd.item_set_id = isp_td_ts.item_set_id
       AND tr.test_roster_id = si.test_roster_id
       AND tr.student_id = st.student_id
       AND si.item_set_id = istd.item_set_id
       AND i.item_type = 'CR'
       AND i.answer_area = 'GRID'
       AND ta.customer_id = parm.customer_id
       AND ta.product_id = parm.product_id
       AND tr.student_id = in_student
       AND tc.item_set_id = in_tc_id
       AND ons.student_id = tr.student_id
       AND ons.org_node_id = tr.org_node_id AND tr.test_roster_id = in_test_roster_id)
      SELECT form_element_id, 					-- FCAT Spring 2008 external_id,
       gridded_columns,
       item_sort_order AS ques_no,
       CASE
         WHEN constructed_response IS NULL THEN
          1
         ELSE
          0
       END AS omit,
       CASE
         WHEN constructed_response IS NULL THEN
          '     '
         ELSE
          Decode_Html_Url(TO_CHAR(constructed_response))
       END AS response
        FROM main m, ITEM_RESPONSE_CR irc
       WHERE m.test_roster_id = irc.test_roster_id(+)
         AND m.td_item_set_id = irc.item_set_id(+)
         AND m.item_id = irc.item_id(+)
       ORDER BY m.si_item_set_order,
          m.td_item_set_sort_order,
          ques_no;

   FUNCTION get_ea_resp (
      p_test_roster_id     INTEGER,
      p_item_id            VARCHAR2,
      p_act_resp           VARCHAR2,
      p_max_resp_seq_num   INTEGER
   )
      RETURN VARCHAR2
   IS
      v_ea_resp   VARCHAR2 (1);
   BEGIN
      SELECT DECODE (response,
                     'A', '1',
                     'B', '2',
                     'C', '3',
                     'D', '4',
                     'E', '5',
                     ' '
                    ) AS response
        INTO v_ea_resp
        FROM ITEM_RESPONSE
       WHERE response_seq_num =
                (SELECT MAX (response_seq_num)
                   FROM ITEM_RESPONSE
                  WHERE test_roster_id = p_test_roster_id
                    AND item_id = p_item_id
                    AND response <> p_act_resp
                    AND response_seq_num <> p_max_resp_seq_num)
         AND test_roster_id = p_test_roster_id
         AND item_id = p_item_id;
      RETURN v_ea_resp;

   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN RETURN ' ';
   END get_ea_resp;
--Start actual execution of the procedure
BEGIN
   --Select ST records from STG_EISS_Tb table
   FOR c_st_rec IN c_st
   LOOP
      --Initialize values before using them--
      v_ra := NULL;
      v_sa := NULL;
      v_ea := NULL;
      v_sum := 0;
      --      v_subject := '';

      -- Select TS records for the given ST record --
      v_subject := '';

      -- Select TD/RA, SA records for the given ST record --
      OPEN c_td (c_st_rec.test_roster_id, c_st_rec.sr_id, c_st_rec.st_id
                                                                        --                    c_ts_rec.ts_id
                );

      LOOP
         FETCH c_td
         BULK COLLECT INTO v_sr_resp_tab;

         FOR question_no IN 1 .. v_sr_resp_tab.COUNT
         LOOP
            --Concatenate the response for that Item into the Response array -
            v_ra := v_ra || v_sr_resp_tab (question_no).response;
            --Concatenate the score for that Item into the Scoring array -
            v_sa := v_sa || v_sr_resp_tab (question_no).score;
            --Calculate the score of the student by adding the current score with the score obtained at that instance -
            v_sum := v_sum + TO_NUMBER (v_sr_resp_tab (question_no).score);
            v_subject := v_sr_resp_tab (question_no).td_subject;

            IF v_sr_resp_tab (question_no).response_seq_num IS NULL
            THEN
               v_ea := v_ea || ' ';
            ELSE
               v_ea :=
                     v_ea
                  || get_ea_resp (c_st_rec.test_roster_id,
                                  v_sr_resp_tab (question_no).item_id,
                                  v_sr_resp_tab (question_no).actual_response,
                                  v_sr_resp_tab (question_no).response_seq_num
                                 );
            END IF;
         END LOOP;

         EXIT WHEN c_td%NOTFOUND;
      END LOOP;

      CLOSE c_td;

      -- Insert the TS record along with the concatenated data into STG_EISS_TB table --
      INSERT INTO stg_eiss_tb
                  (customer_id, product_id,
                   test_roster_id, ou_id, sh_id,
                   gh_id, sr_id, st_id,
                   grade, sort_order, level_type,
                   level_data
                  )
           VALUES (c_st_rec.customer_id, c_st_rec.product_id,
                   c_st_rec.test_roster_id, c_st_rec.ou_id, c_st_rec.sh_id,
                   c_st_rec.gh_id, c_st_rec.sr_id, c_st_rec.st_id,
                   c_st_rec.grade, '3', 'TS',
                   (   'TS '
                    || v_subject
                    || ','
                    || v_secttyp
                    || ','
                    || TO_CHAR (v_sum)
                    || ','
                    || v_invflag
                    || ','
                    || v_suppflag
                    || ','
                    || v_omitflag
                    || ','
                    || v_flipflag
                    || ','
                    || c_st_rec.erasure_cnt
                   )
                  );

      -- Insert the RA record along with the concatenated data into STG_EISS_TB table --
      INSERT INTO stg_eiss_tb
                  (customer_id, product_id,
                   test_roster_id, ou_id, sh_id,
                   gh_id, sr_id, st_id,
                   grade, sort_order, level_type, level_data
                  )
           VALUES (c_st_rec.customer_id, c_st_rec.product_id,
                   c_st_rec.test_roster_id, c_st_rec.ou_id, c_st_rec.sh_id,
                   c_st_rec.gh_id, c_st_rec.sr_id, c_st_rec.st_id,
                   c_st_rec.grade, '4', 'TD', ('RA ' || v_ra)
                  );

      -- Insert the SA record along with the concatenated data into STG_EISS_TB table --
      INSERT INTO stg_eiss_tb
                  (customer_id, product_id,
                   test_roster_id, ou_id, sh_id,
                   gh_id, sr_id, st_id,
                   grade, sort_order, level_type, level_data
                  )
           VALUES (c_st_rec.customer_id, c_st_rec.product_id,
                   c_st_rec.test_roster_id, c_st_rec.ou_id, c_st_rec.sh_id,
                   c_st_rec.gh_id, c_st_rec.sr_id, c_st_rec.st_id,
                   c_st_rec.grade, '5', 'TD', ('SA ' || v_sa)
                  );

      -- Insert the EA record along with the concatenated data into STG_EISS_TB table --
      INSERT INTO stg_eiss_tb
                  (customer_id, product_id,
                   test_roster_id, ou_id, sh_id,
                   gh_id, sr_id, st_id,
                   grade, sort_order, level_type, level_data
                  )
           VALUES (c_st_rec.customer_id, c_st_rec.product_id,
                   c_st_rec.test_roster_id, c_st_rec.ou_id, c_st_rec.sh_id,
                   c_st_rec.gh_id, c_st_rec.sr_id, c_st_rec.st_id,
                   c_st_rec.grade, '6', 'TD', ('EA ' || v_ea)
                  );

      --      COMMIT;
      -- GI record processing --
      OPEN c_gi (c_st_rec.test_roster_id, c_st_rec.sr_id, c_st_rec.st_id);

      LOOP
         FETCH c_gi
         BULK COLLECT INTO v_gi_resp_tab;

         FOR question_no IN 1 .. v_gi_resp_tab.COUNT
         LOOP
            -- Insert GI record
            INSERT INTO stg_eiss_tb
                        (customer_id, product_id, test_roster_id,
                         ou_id, sh_id, gh_id,
                         sr_id, st_id, grade,
                         sort_order,
                         level_type,
                         level_data
                        )
                 VALUES (c_st_rec.customer_id, c_st_rec.product_id, c_st_rec.test_roster_id,
                         c_st_rec.ou_id, c_st_rec.sh_id, c_st_rec.gh_id,
                         c_st_rec.sr_id, c_st_rec.st_id, c_st_rec.grade,
                            '7'
                         || TO_CHAR (v_gi_resp_tab (question_no).ques_no,
                                     '099'
                                    ),
                         'GI',
                            'GI '
                         || v_gi_resp_tab (question_no).FORM_ELEMENT_ID    --FCAT Spring 2008        --old field .external_id
                         || ','
                         || v_gi_resp_tab (question_no).response
                         || ','
                         || v_gi_resp_tab (question_no).ques_no
                         || ','
                         || v_gi_resp_tab (question_no).response
                         || ','
                         || '0'
                         || ','
                         || v_gi_resp_tab (question_no).gridded_columns
                         || ','
                         || v_gi_resp_tab (question_no).omit
                         || ','
                         || '0'
                        );
         END LOOP;

         EXIT WHEN c_gi%NOTFOUND;
      END LOOP;

      CLOSE c_gi;

      IF MOD (c_st%ROWCOUNT, 1000) = 0
      THEN
         COMMIT;
      END IF;
   END LOOP;

   COMMIT;
END Sp_Response_OAS_EXPORT;
/
