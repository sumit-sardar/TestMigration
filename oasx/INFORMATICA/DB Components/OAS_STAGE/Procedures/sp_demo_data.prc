CREATE OR REPLACE PROCEDURE sp_demo_data (
   in_precode_data   IN       VARCHAR2,
   in_student_id     IN       INTEGER,
   in_customer_id    IN       NUMBER,
   o_no_of_records   OUT      NUMBER
) IS
   v_in_precode_data       VARCHAR2 (4000);
   v_start_pos             INTEGER;
   v_end_pos               INTEGER;
   v_next_demo_value       VARCHAR2 (50);
   v_trans_value           VARCHAR2 (64);
   v_data_value            VARCHAR2 (64);
   v_data_name             VARCHAR2 (32);
   v_cust_resrch_name_id   NUMBER (10);
   v_sql_str               VARCHAR2 (2000);
   v_sql_cols              VARCHAR2 (1000);
   v_sql_vals              VARCHAR2 (1000);

   CURSOR demo_acco_cur IS
      SELECT   UPPER (precode_field_name) demo_acco_name,
               UPPER (column_type) demo_acco_type, field_position
          FROM precode_oas_map_tb
         WHERE customer_id = in_customer_id
           AND UPPER (column_type) IN ('DEMOGRAPHIC', 'ACCOMMODATION')
      ORDER BY field_position;
BEGIN
   v_in_precode_data := ',' || in_precode_data || ',';
   o_no_of_records := 0;

   FOR demo_acco_rec IN demo_acco_cur LOOP
      -- NUll for all projects. -- Modified for FCAT as below
      -- v_sql_cols := NULL;
      -- v_sql_vals := NULL;
      v_sql_cols := ',TEST_PAUSE';
      v_sql_vals := ',''T''';
      v_start_pos :=
           INSTR (v_in_precode_data, ',', 1, demo_acco_rec.field_position)
           + 1;
      v_end_pos :=
          INSTR (v_in_precode_data, ',', 1, demo_acco_rec.field_position + 1);

      IF v_start_pos = 0 OR v_end_pos = 0 THEN
         raise_application_error
            (-20001,
             'CSV data error as per the field positions mentioned in the table PRECODE_OAS_MAP_TB.  Pls check'
            );
      END IF;

      v_next_demo_value :=
         TRIM (SUBSTR (v_in_precode_data, v_start_pos,
                       v_end_pos - v_start_pos)
              );

      IF demo_acco_rec.demo_acco_type = 'DEMOGRAPHIC' THEN
         IF v_next_demo_value IS NULL THEN
            BEGIN
               SELECT null_transf_data_value, data_name
                 INTO v_trans_value, v_data_name
                 FROM customer_research_name
                WHERE customer_id = in_customer_id
                  AND UPPER (data_name) = demo_acco_rec.demo_acco_name;

               IF v_trans_value IS NOT NULL THEN
                  INSERT INTO stg_student_demo_fl
                       VALUES (in_student_id, in_customer_id, v_data_name,
                               v_trans_value);

                  o_no_of_records := o_no_of_records + 1;
               END IF;
            EXCEPTION
               WHEN NO_DATA_FOUND THEN
                  NULL;
            END;
         ELSE
            BEGIN
               SELECT customer_research_name_id, data_name
                 INTO v_cust_resrch_name_id, v_data_name
                 FROM customer_research_name
                WHERE customer_id = in_customer_id
                  AND UPPER (data_name) = demo_acco_rec.demo_acco_name;

               IF v_cust_resrch_name_id IS NOT NULL THEN
                  SELECT data_value
                    INTO v_data_value
                    FROM customer_research_value
                   WHERE customer_research_name_id = v_cust_resrch_name_id
                     AND udf_value = v_next_demo_value;

                  IF v_data_value IS NOT NULL THEN
                     INSERT INTO stg_student_demo_fl
                          VALUES (in_student_id, in_customer_id, v_data_name,
                                  v_data_value);

                     o_no_of_records := o_no_of_records + 1;
                  END IF;
               END IF;
            EXCEPTION
               WHEN NO_DATA_FOUND THEN
                  NULL;
            END;
         END IF;
      ELSIF demo_acco_rec.demo_acco_type = 'ACCOMMODATION' THEN
         IF v_next_demo_value IS NOT NULL THEN
            v_sql_cols := v_sql_cols || ',' || demo_acco_rec.demo_acco_name;
            v_sql_vals := v_sql_vals || ',''' || v_next_demo_value || '''';
         END IF;
      END IF;
   END LOOP;

   IF v_sql_cols IS NOT NULL THEN
      v_sql_str :=
            'INSERT INTO STG_STUDENT_ACCO_FL (PRECODE_ID, CUSTOMER_ID'
         || v_sql_cols
         || ') VALUES ('
         || in_student_id
         || ','
         || in_customer_id
         || v_sql_vals
         || ')';

      EXECUTE IMMEDIATE v_sql_str;

      o_no_of_records := o_no_of_records + 1;
   END IF;

   COMMIT;
END sp_demo_data;
/
