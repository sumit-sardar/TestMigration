CREATE OR REPLACE PROCEDURE Sp_Upd_Dup_User_Name
IS
-- Informatica is ingnoring the duplicate user_names within in the same batch being processed even with commit interval = 1 --
-- So this procedure will be executed as Post Load which will re-create the user_names for duplicate user_names --
   CURSOR cur
   IS
      SELECT student_id, first_name, last_name, middle_name, birthdate
        FROM student s
       WHERE s.user_name IN (
                SELECT   s1.user_name
                    FROM STG_STUDENT_BIO_TB ss,
                         student s1,
                         STG_PARAM_VERIFY_TB p
                   WHERE ss.record_indicator = 'I'
                     AND s1.precode_id = ss.precode_id
                     AND s1.customer_id = p.customer_id
                     AND ss.customer_id = p.customer_id
                GROUP BY s1.user_name
                  HAVING COUNT (*) > 1)
         AND s.precode_id NOT IN (
                SELECT   MIN (ss.precode_id)
                    FROM STG_STUDENT_BIO_TB ss,
                         student s2,
                         STG_PARAM_VERIFY_TB p
                   WHERE ss.record_indicator = 'I'
                     AND s2.precode_id = ss.precode_id
                     AND s2.customer_id = p.customer_id
                     AND ss.customer_id = p.customer_id
                GROUP BY s2.user_name
                  HAVING COUNT (*) > 1);

/*
      SELECT student_id, first_name, last_name, middle_name, birthdate
        FROM student s
       WHERE s.user_name IN (
                SELECT   s1.user_name
                    FROM STG_STUDENT_BIO_TB ss,
                         student s1,
                         STG_PARAM_VERIFY_TB p
                   WHERE ss.record_indicator = 'I'
                     AND s1.precode_id = ss.precode_id
                     AND s1.customer_id = p.customer_id
                GROUP BY s1.user_name
                  HAVING COUNT (*) > 1)
         AND s.precode_id NOT IN (
                SELECT   MIN (ss.precode_id)
                    FROM STG_STUDENT_BIO_TB ss, student s2
                   WHERE ss.record_indicator = 'I'
                     AND s2.precode_id = ss.precode_id
                GROUP BY s2.user_name
                  HAVING COUNT (*) > 1);
*/
   v_user_name   VARCHAR2 (60);
BEGIN
   FOR rec IN cur
   LOOP
      v_user_name :=
         student_user_name (rec.first_name,
                            rec.middle_name,
                            rec.last_name,
                            rec.birthdate
                           );

      UPDATE student
         SET user_name = v_user_name
       WHERE student_id = rec.student_id;
   END LOOP;
END Sp_Upd_Dup_User_Name;
/
