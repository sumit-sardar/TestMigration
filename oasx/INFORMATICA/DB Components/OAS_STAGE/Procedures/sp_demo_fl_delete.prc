CREATE OR REPLACE PROCEDURE sp_demo_fl_delete
IS
BEGIN
   DELETE FROM stg_student_demo_fl;

   COMMIT;

   DELETE FROM stg_student_acco_fl;

   COMMIT;
END sp_demo_fl_delete;
/
