CREATE OR REPLACE PROCEDURE Sp_Udf_Fcat
IS
   /*DESCRIPTION: This stored procedure is used to Update the UDF field in the STYUDENT table with data from the STUDENT_RESEARCH_DATA table. */
   /*AUTHOR: Wipro Offshore Team*/
   /*CREATED DATE: 06th March 2006*/
   /*MODIFIED by: Puru Naidu for FCAT on 07 July 2007*/

   /* TO GET THE PATH FOR FILE */
   v_udf                      VARCHAR2 (256)               := '';
   v_udf_rec_no               INTEGER                      := 0;
   v_udf_last_rec_no          INTEGER                      := 0;
   v_stud_col_no              INTEGER                      := 0;
   v_sa_col_no                INTEGER                      := 0;
   v_srd_row_no               INTEGER                      := 0;
   v_cust_id                  INTEGER                      := 0;
   v_udf_length               INTEGER                      := 0;
   v_stud_sql_str             VARCHAR2 (2000);
   v_stud_sql_val             VARCHAR2 (2000);
   v_sa_sql_str               VARCHAR2 (2000);
   v_sa_sql_val               VARCHAR2 (2000);
   v_sql_str                  VARCHAR2 (2000);
   v_udf_curr_val             VARCHAR2 (60);
   v_org_node_category_name   VARCHAR2 (20);

   TYPE udf_rec IS RECORD (
      oas_table_name     VARCHAR2 (50),
      oas_column_name    VARCHAR2 (50),
      subject            VARCHAR2 (32),
      field_length       INTEGER,
      start_position     INTEGER,
      filler_character   VARCHAR2 (10),
      justify            VARCHAR2 (1),
      decode_string      VARCHAR2 (200),
      col_pos_in_table   INTEGER
   );

   TYPE udf_tab IS TABLE OF udf_rec
      INDEX BY BINARY_INTEGER;

   v_udf_table                udf_tab;

   TYPE ARRAY IS TABLE OF VARCHAR2 (10);

   v_arr_srd_udf_val          ARRAY;
   v_arr_srd_udf_val_pre      ARRAY;

-- for STUDENT_RESEARCH_DATA_Check_box --
   TYPE vtab IS TABLE OF VARCHAR2 (20)
      INDEX BY BINARY_INTEGER;

   v_srd_tab                  vtab;
   v_pre_srd_tab              vtab;
   v_stud_pre_rec             stg_student_bio_tb%ROWTYPE;

   --TO GET DATA FOR OAS_COLUMN_NAME,LENGTH,START_POSITION FROM UDF TABLE
   CURSOR c_udf (v_customer_id NUMBER)
   IS
      SELECT   oas_table_name, oas_column_name, TRIM (UPPER (subject))
                                                                      subject,
               LENGTH, start_position, filler_character, decode_string,
               justify
          FROM udf_mapping
         WHERE customer_id = v_customer_id
      ORDER BY oas_table_name, oas_column_name, start_position;

   --IDENTIFICATION OF STUDENTS IN TEST_ROSTER TABLE PER PARTICUALR CUSTOMER_ID
   CURSOR cur_tr
   IS
      SELECT tr.student_id, tr.test_roster_id, tr.org_node_id,
             tr.test_admin_id, tr.validation_status, ta.LOCATION,
             TRIM (UPPER (tc.subject)) subject, tr.form_assignment,
             st.precode_id
        FROM STUDENT st,
             TEST_ROSTER tr,
             TEST_ADMIN ta,
             TEST_CATALOG tc,
             stg_test_roster str,
             stg_param_winscr_tb stg
       WHERE st.student_id = tr.student_id
         AND tr.test_admin_id = ta.test_admin_id
         AND ta.test_catalog_id = tc.test_catalog_id
         AND tr.customer_id = stg.customer_id
         AND tr.customer_id = ta.customer_id
         AND ta.product_id = tc.product_id
         AND ta.product_id = stg.product_id
         AND str.test_roster_id = tr.test_roster_id;

		 /*
   CURSOR cur_srd_cb (in_customer_id NUMBER, in_student_id NUMBER)
   IS
      SELECT um.start_position, udf_value
        FROM udf_mapping um,
             STUDENT_RESEARCH_DATA srd,
             CUSTOMER_RESEARCH_NAME crn,
             CUSTOMER_RESEARCH_VALUE crv
       WHERE srd.student_id = in_student_id
         AND UPPER (um.oas_table_name) =
                                     UPPER ('STUDENT_RESEARCH_DATA_Check_Box')
         AND srd.data_key = um.oas_column_name
         AND um.customer_id = in_customer_id
         AND um.customer_id = crn.customer_id
         AND oas_column_name = crn.data_name
         AND crn.multiple_allowed = 'T'
         AND crn.customer_research_name_id = crv.customer_research_name_id
         AND INSTR (srd.data_value, crv.data_value) <> 0
         AND INSTR (decode_string, udf_value) <> 0;

   CURSOR cur_pre_srd_cb (in_customer_id NUMBER, in_precode_id NUMBER)
   IS
      SELECT um.start_position, udf_value
        FROM udf_mapping um,
             stg_student_demo_tb srd,
             CUSTOMER_RESEARCH_NAME crn,
             CUSTOMER_RESEARCH_VALUE crv
       WHERE srd.precode_id = in_precode_id
         AND UPPER (um.oas_table_name) =
                                     UPPER ('STUDENT_RESEARCH_DATA_Check_Box')
         AND srd.data_key = um.oas_column_name
         AND um.customer_id = in_customer_id
         AND um.customer_id = crn.customer_id
         AND um.customer_id = srd.customer_id
         AND oas_column_name = crn.data_name
         AND crn.multiple_allowed = 'T'
         AND crn.customer_research_name_id = crv.customer_research_name_id
         AND INSTR (srd.data_value, crv.data_value) <> 0
         AND INSTR (decode_string, udf_value) <> 0;
*/
   FUNCTION text_btw_pat (text VARCHAR2, pat VARCHAR2, pos INTEGER)
      RETURN VARCHAR2
   IS
      v_start_pos   INTEGER;
      v_end_pos     INTEGER;
   BEGIN
      v_start_pos := INSTR (text, pat, 1, pos) + 1;
      v_end_pos := INSTR (text, pat, 1, pos + 1);
      RETURN TRIM (SUBSTR (text, v_start_pos, v_end_pos - v_start_pos));
   END text_btw_pat;

   PROCEDURE decode_thru_dual (
      p_decode_string      VARCHAR2,
      p_replace_col_name   VARCHAR2
   )
   --      RETURN VARCHAR2
   IS
   BEGIN
      v_sql_str := v_udf;
      v_sql_str :=
            'select '
         || REPLACE (p_decode_string,
                     p_replace_col_name,
                     '''' || v_udf_curr_val || ''''
                    )
         || ' from dual';

      EXECUTE IMMEDIATE v_sql_str
                   INTO v_udf_curr_val;
   END decode_thru_dual;
BEGIN
   --TO GET CUSTOMER_ID FROM STAGING TABLE
   SELECT customer_id
     INTO v_cust_id
     FROM stg_param_winscr_tb
    WHERE sno = 1;

   -- Read the UDF_MAPPING table and set up environment for process --
   FOR udf_rec IN c_udf (v_cust_id)
   LOOP
      v_udf_rec_no := v_udf_rec_no + 1;
      v_udf_table (v_udf_rec_no).oas_table_name := udf_rec.oas_table_name;
      v_udf_table (v_udf_rec_no).oas_column_name := udf_rec.oas_column_name;
      v_udf_table (v_udf_rec_no).subject := udf_rec.subject;
      v_udf_table (v_udf_rec_no).field_length := udf_rec.LENGTH;
      v_udf_table (v_udf_rec_no).start_position := udf_rec.start_position;
      v_udf_table (v_udf_rec_no).decode_string := udf_rec.decode_string;
      v_udf_table (v_udf_rec_no).justify := udf_rec.justify;

      SELECT DECODE (udf_rec.filler_character,
                     'ZERO', '0',
                     'BLANK', ' ',
                     NULL, ' ',
                     udf_rec.filler_character
                    )
        INTO v_udf_table (v_udf_rec_no).filler_character
        FROM DUAL;

      IF UPPER (udf_rec.oas_table_name) = 'STUDENT'
      THEN
         v_stud_col_no := v_stud_col_no + 1;
         v_udf_table (v_udf_rec_no).col_pos_in_table := v_stud_col_no;
         v_stud_sql_str :=
               v_stud_sql_str
            || ' || ''~'' || '
            || NVL (udf_rec.decode_string, UPPER (udf_rec.oas_column_name));
-- Keep it for FCAT --
      ELSIF UPPER (v_udf_table (v_udf_rec_no).oas_table_name) LIKE
                                                      'STUDENT_ACCOMMODATION%'
      THEN
         v_sa_col_no := v_sa_col_no + 1;
         v_udf_table (v_udf_rec_no).col_pos_in_table := v_sa_col_no;
         v_sa_sql_str :=
               v_sa_sql_str
            || ' || ''~'' || '
            || NVL (udf_rec.decode_string, UPPER (udf_rec.oas_column_name));
--
      ELSIF UPPER (v_udf_table (v_udf_rec_no).oas_table_name) =
                                                       'STUDENT_RESEARCH_DATA'
      THEN
         v_srd_row_no := v_srd_row_no + 1;
         v_udf_table (v_udf_rec_no).col_pos_in_table := v_srd_row_no;
      END IF;

      -- To get the max udf length --
      IF v_udf_length <
              v_udf_table (v_udf_rec_no).start_position
            + v_udf_table (v_udf_rec_no).field_length
      THEN
         v_udf_length :=
              v_udf_table (v_udf_rec_no).start_position
            + v_udf_table (v_udf_rec_no).field_length;
      END IF;
   END LOOP;

   -- Prepare select sql script for STUDENT --
   IF v_stud_sql_str IS NOT NULL
   THEN
      v_stud_sql_str :=
            'select '
         || SUBSTR (v_stud_sql_str, 5)
         || ' || ''~'' from student where student_id = :student_id';
   END IF;

   -- Prepare select sql script for STUDENT_ACCOMMODATION --
   IF v_sa_sql_str IS NOT NULL
   THEN
      v_sa_sql_str :=
            'select '
         || SUBSTR (v_sa_sql_str, 5)
         || ' || ''~'' from student_accommodation where student_id = :student_id';
   END IF;

   v_udf_last_rec_no := v_udf_rec_no;

   -- Process Test Roster records --
   FOR cur_tr_rec IN cur_tr
   LOOP
/*
-- Get Precode Bio dat for roll back CR --
      BEGIN
         SELECT *
           INTO v_stud_pre_rec
           FROM stg_student_bio_tb
          WHERE precode_id = cur_tr_rec.precode_id AND customer_id = v_cust_id;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_stud_pre_rec := NULL;
      END;

      IF cur_tr_rec.subject IS NULL
      THEN
         RAISE_APPLICATION_ERROR (-20001,
                                     'Subject is null for student_id='
                                  || cur_tr_rec.student_id
                                  || ' and test_roster_id='
                                  || cur_tr_rec.test_roster_id
                                 );
      END IF;
*/

      v_udf := LPAD (' ', v_udf_length - 1);
      -- Get demo check box into array --
      v_srd_tab.DELETE;
/*
      FOR rec_srd_cb IN cur_srd_cb (v_cust_id, cur_tr_rec.student_id)
      LOOP
         v_srd_tab (rec_srd_cb.start_position) := rec_srd_cb.udf_value;
      END LOOP;

      -- Get pre demo check box into array --
      v_pre_srd_tab.DELETE;

      FOR rec_pre_srd_cb IN cur_pre_srd_cb (v_cust_id, cur_tr_rec.precode_id)
      LOOP
         v_pre_srd_tab (rec_pre_srd_cb.start_position) :=
                                                     rec_pre_srd_cb.udf_value;
      END LOOP;
*/
      IF NVL (v_srd_row_no, 0) > 0
      THEN
         -- Get udf_values in an array by STUDENT_RESEARCH_DATA.column_name order
         -- Used 2 sqls to get outer join with more than one table
         SELECT   b.udf_value
         BULK COLLECT INTO v_arr_srd_udf_val
             FROM (SELECT srd.data_value, oas_column_name,
                          crn.customer_research_name_id
                     FROM udf_mapping um,
                          STUDENT_RESEARCH_DATA srd,
                          CUSTOMER_RESEARCH_NAME crn
                    WHERE srd.student_id(+) = cur_tr_rec.student_id
                      AND um.oas_table_name = 'STUDENT_RESEARCH_DATA'
                      AND srd.data_key(+) = um.oas_column_name
                      AND um.customer_id = v_cust_id
                      AND um.customer_id = crn.customer_id
                      AND oas_column_name = crn.data_name
                      AND crn.multiple_allowed = 'F') a,
                  (SELECT customer_research_name_id, data_value, udf_value
                     FROM CUSTOMER_RESEARCH_VALUE) b
            WHERE a.customer_research_name_id = b.customer_research_name_id(+)
                  AND a.data_value = b.data_value(+)
         ORDER BY a.oas_column_name;

/*		 
         -- Read the above from precode --
         SELECT   b.udf_value
         BULK COLLECT INTO v_arr_srd_udf_val_pre
             FROM (SELECT srd.data_value, oas_column_name,
                          crn.customer_research_name_id
                     FROM udf_mapping um,
                          stg_student_demo_tb srd,
                          CUSTOMER_RESEARCH_NAME crn
                    WHERE srd.precode_id(+) = cur_tr_rec.precode_id
                      AND srd.customer_id(+) = v_cust_id
                      AND um.oas_table_name = 'STUDENT_RESEARCH_DATA'
                      AND srd.data_key(+) = um.oas_column_name
                      AND um.customer_id = v_cust_id
                      AND um.customer_id = crn.customer_id
                      AND oas_column_name = crn.data_name
                      AND crn.multiple_allowed = 'F') a,
                  (SELECT customer_research_name_id, data_value, udf_value
                     FROM CUSTOMER_RESEARCH_VALUE) b
            WHERE a.customer_research_name_id = b.customer_research_name_id(+)
                  AND a.data_value = b.data_value(+)
        ORDER BY a.oas_column_name;
 */      END IF;

      IF v_stud_sql_str IS NOT NULL
      THEN
         EXECUTE IMMEDIATE v_stud_sql_str
                      INTO v_stud_sql_val
                     USING cur_tr_rec.student_id;
      END IF;

      BEGIN
         IF v_sa_sql_str IS NOT NULL
         THEN
            EXECUTE IMMEDIATE v_sa_sql_str
                         INTO v_sa_sql_val
                        USING cur_tr_rec.student_id;
         END IF;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_sa_sql_val := NULL;
      END;

      -- OPENS CUR_UDF CURSOR FOR OAS_COLUMN_NAME VALUE
      FOR v_udf_rec_no IN 1 .. v_udf_last_rec_no
      LOOP
         v_udf_curr_val := NULL;

         -- STUDENT TABLE --
         IF UPPER (v_udf_table (v_udf_rec_no).oas_table_name) = 'STUDENT'
         THEN
/*		 
            -- Hard code for rol back CR from Student bio --
            IF UPPER (v_udf_table (v_udf_rec_no).oas_column_name) = 'GRADE'
            THEN
               v_udf_curr_val := v_stud_pre_rec.grade_enrolled;
            ELSIF UPPER (v_udf_table (v_udf_rec_no).oas_column_name) = 'UDF_2'
            THEN
               v_udf_curr_val := v_stud_pre_rec.class_code;
            ELSIF     UPPER (v_udf_table (v_udf_rec_no).oas_column_name) =
                                                                    'EXT_PIN2'
                  AND v_udf_table (v_udf_rec_no).start_position = 114
            THEN
               v_udf_curr_val := v_stud_pre_rec.student_id;
            END IF;
*/
--            IF v_udf_curr_val IS NULL
--            THEN
               v_udf_curr_val :=
                  text_btw_pat (v_stud_sql_val,
                                '~',
                                v_udf_table (v_udf_rec_no).col_pos_in_table
                               );
--            END IF;
         -- STUDENT_RESEARCH_DATA --
         ELSIF UPPER (v_udf_table (v_udf_rec_no).oas_table_name) =
                                                       'STUDENT_RESEARCH_DATA'
         THEN
--            v_udf_curr_val :=
--               v_arr_srd_udf_val_pre
  --                                (v_udf_table (v_udf_rec_no).col_pos_in_table
    --                              );

--            IF    v_udf_curr_val IS NULL
  --             OR v_udf_table (v_udf_rec_no).oas_column_name =
        --                                                  '1. Retake Subjects'
    --        THEN
               v_udf_curr_val :=
                  v_arr_srd_udf_val
                                  (v_udf_table (v_udf_rec_no).col_pos_in_table
                                  );
      --      END IF;

            IF v_udf_table (v_udf_rec_no).decode_string IS NOT NULL
            THEN
               -- v_sql_str :=
               decode_thru_dual (v_udf_table (v_udf_rec_no).decode_string,
                                 'UDF_VALUE'
                                );
            END IF;
/*
         ELSIF UPPER (v_udf_table (v_udf_rec_no).oas_table_name) =
                                     UPPER ('STUDENT_RESEARCH_DATA_Check_Box')
         THEN
            IF v_pre_srd_tab.EXISTS (v_udf_table (v_udf_rec_no).start_position
                                    )
            THEN
               v_udf_curr_val :=
                    v_pre_srd_tab (v_udf_table (v_udf_rec_no).start_position);
            END IF;

            -- Put OAS value is pre is null --
            IF     v_udf_curr_val IS NULL
               AND v_srd_tab.EXISTS (v_udf_table (v_udf_rec_no).start_position)
            THEN
               v_udf_curr_val :=
                        v_srd_tab (v_udf_table (v_udf_rec_no).start_position);
            END IF;

            IF v_udf_table (v_udf_rec_no).decode_string IS NOT NULL
            THEN
               -- v_sql_str :=
               decode_thru_dual (v_udf_table (v_udf_rec_no).decode_string,
                                 'UDF_VALUE'
                                );
            END IF;
*/
         ELSIF UPPER (v_udf_table (v_udf_rec_no).oas_table_name) LIKE
                                                                   'ORG_NODE%'
         THEN
            v_org_node_category_name :=
               REPLACE (UPPER (v_udf_table (v_udf_rec_no).oas_table_name),
                        'ORG_NODE-'
                       );

            BEGIN
               SELECT org_node_code
                 INTO v_udf_curr_val
                 FROM (SELECT     n.org_node_code,
                                  UPPER (nc.category_name) category_name
                             FROM ORG_NODE n,
                                  ORG_NODE_PARENT np,
                                  ORG_NODE_CATEGORY nc
                            WHERE n.org_node_id = np.org_node_id
                              AND n.org_node_category_id =
                                                       nc.org_node_category_id
                       CONNECT BY PRIOR np.parent_org_node_id = np.org_node_id
                       START WITH np.org_node_id = cur_tr_rec.org_node_id)
                WHERE category_name = v_org_node_category_name;
            EXCEPTION
               WHEN NO_DATA_FOUND
               THEN
                  v_udf_curr_val := NULL;
            END;
         -- CONSTANT VALUES --
         ELSIF UPPER (v_udf_table (v_udf_rec_no).oas_table_name) = 'CONSTANT'
         THEN
            v_udf_curr_val := v_udf_table (v_udf_rec_no).decode_string;
         -- STUDENT_ACCOMMODATION --
         ELSIF UPPER (v_udf_table (v_udf_rec_no).oas_table_name) =
                                                       'STUDENT_ACCOMMODATION'
         THEN
            v_udf_curr_val :=
               text_btw_pat (v_sa_sql_val,
                             '~',
                             v_udf_table (v_udf_rec_no).col_pos_in_table
                            );
         -- TEST_ADMIN --
         ELSIF     UPPER (v_udf_table (v_udf_rec_no).oas_table_name) =
                                                                  'TEST_ADMIN'
               AND UPPER (v_udf_table (v_udf_rec_no).oas_column_name) =
                                                               'TEST_ADMIN_ID'
         THEN
            v_udf_curr_val := cur_tr_rec.test_admin_id;

            IF v_udf_table (v_udf_rec_no).decode_string IS NOT NULL
            THEN
               decode_thru_dual
                            (v_udf_table (v_udf_rec_no).decode_string,
                             UPPER (v_udf_table (v_udf_rec_no).oas_column_name)
                            );
            END IF;
         -- TEST_ROSTER --
         ELSIF     UPPER (v_udf_table (v_udf_rec_no).oas_table_name) =
                                                                 'TEST_ROSTER'
               AND UPPER (v_udf_table (v_udf_rec_no).oas_column_name) =
                                                           'VALIDATION_STATUS'
         THEN
            v_udf_curr_val := cur_tr_rec.validation_status;

            IF v_udf_table (v_udf_rec_no).decode_string IS NOT NULL
            THEN
               decode_thru_dual
                            (v_udf_table (v_udf_rec_no).decode_string,
                             UPPER (v_udf_table (v_udf_rec_no).oas_column_name)
                            );
            END IF;
/* Not required, since the CR was amended --
         ELSIF UPPER (v_udf_table (v_udf_rec_no).oas_table_name) LIKE
                                                            'ITEM_RESPONSE_CR'
         THEN
            v_sql_str :=
                  'select '
               || REPLACE (v_udf_table (v_udf_rec_no).decode_string,
                           '##TEST_ROSTER_ID##',
                           cur_tr_rec.test_roster_id
                          )
               || ' from dual';

            EXECUTE IMMEDIATE v_sql_str
                         INTO v_udf_curr_val;
*/
         END IF;

         -- JUSTIFY --
         IF v_udf_table (v_udf_rec_no).justify = 'R'
         THEN
            v_udf_curr_val :=
               LPAD (NVL (v_udf_curr_val,
                          v_udf_table (v_udf_rec_no).filler_character
                         ),
                     v_udf_table (v_udf_rec_no).field_length,
                     v_udf_table (v_udf_rec_no).filler_character
                    );
         ELSE
            v_udf_curr_val :=
               RPAD (NVL (v_udf_curr_val,
                          v_udf_table (v_udf_rec_no).filler_character
                         ),
                     v_udf_table (v_udf_rec_no).field_length,
                     v_udf_table (v_udf_rec_no).filler_character
                    );
         END IF;

         -- Check for Subject dependency --
         IF NVL (v_udf_table (v_udf_rec_no).subject, cur_tr_rec.subject) =
                                                            cur_tr_rec.subject
         THEN
            -- Place the  v_udf_curr_val in proper udf position with the given length --
            v_udf :=
                  SUBSTR (v_udf,
                          1,
                          v_udf_table (v_udf_rec_no).start_position - 1
                         )
               || v_udf_curr_val
               || SUBSTR (v_udf,
                            v_udf_table (v_udf_rec_no).start_position
                          + v_udf_table (v_udf_rec_no).field_length
                         );
         END IF;
      END LOOP;

      -- UDF position 60 has to be hard coded, since it is from two sources. --
      IF TRIM (SUBSTR (v_udf, 153, 1)) IS NOT NULL
      THEN
         v_udf :=
               SUBSTR (v_udf, 1, 59)
            || SUBSTR (v_udf, 153, 1)
            || SUBSTR (v_udf, 61);
      END IF;

      v_udf := SUBSTR (v_udf, 1, 152);

      -- UPDATE TEST_ROSTER --
      UPDATE TEST_ROSTER
         SET udf = v_udf
       WHERE test_roster_id = cur_tr_rec.test_roster_id;

      COMMIT;
   END LOOP;
END Sp_Udf_Fcat;
/
