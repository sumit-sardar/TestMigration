CREATE OR REPLACE PROCEDURE sp_insert_data_history (in_session_name VARCHAR2)
IS
   v_cust      NUMBER (38);
   v_cnt       NUMBER (3);
   v_user_id   NUMBER (38);
BEGIN
   SELECT customer_id
     INTO v_cust
     FROM stg_param_verify_tb
    WHERE sno = 1;

   SELECT user_id
     INTO v_user_id
     FROM users
    WHERE upper(USER_NAME)= 'SYSTEM'; /* changed user name 04/19/07 */

   SELECT COUNT (*)
     INTO v_cnt
     FROM data_import_history
    WHERE import_filename = in_session_name AND updated_by IS NULL;

   IF v_cnt > 0
   THEN
      DELETE FROM data_import_history
            WHERE import_filename = in_session_name AND updated_by IS NULL;
   END IF;

   INSERT INTO data_import_history
               (created_date_time, created_by, activation_status, customer_id,
                import_filename, data_import_history_id, load_code)
      SELECT SYSDATE, v_user_id, 'AC', v_cust, in_session_name,
             seq_data_import_history_id.NEXTVAL, 120
        FROM DUAL;

   COMMIT;
END;
/
