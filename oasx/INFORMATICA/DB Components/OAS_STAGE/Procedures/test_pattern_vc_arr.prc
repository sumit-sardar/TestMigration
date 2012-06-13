CREATE OR REPLACE PROCEDURE test_pattern_vc_arr
IS
   CURSOR in_cur
   IS
      SELECT level_data
        FROM stg_eiss_tb;

   components   owa_text.vc_arr;
BEGIN
   DBMS_OUTPUT.put_line (   'test_pattern START TIME : '
                         || TO_CHAR (SYSDATE, 'DD-MON-YYYY HH24:MI:SS')
                        );

   FOR in_rec IN in_cur
   LOOP
      IF (owa_pattern.match (in_rec.level_data, '.*,.*', components))
      THEN
         DBMS_OUTPUT.put_line ('test_pattern components (1) '
                               || components (1)
                              );
      END IF;
   END LOOP;

   COMMIT;
EXCEPTION
   WHEN OTHERS
   THEN
      raise_application_error (-20220, 'Error IN test_pattern: ' || SQLERRM);
END test_pattern_vc_arr;
/
