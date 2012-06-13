CREATE OR REPLACE PROCEDURE Sp_Eiss_Load_Fcat IS
   /*DESCRIPTION: This stored procedure is used to load the data into the STG_EISS_LOAD_TB staging table from the STG_EISS_TB table. The procedure also inserts correct serial numbers by which the final file creation can be done by ordering by the serial number. The procedure also appends in front of the records ‘*’ which will be replaced by spaces. */
   /*AUTHOR: Wipro Offshore Team*/
   /*CREATED DATE: 06th March 2006*/
   /*MODFIED DATE: 04TH April 2006*/
   /*Integer for creating Serial numbers*/
   i   INTEGER := 0;

   /*Cursor for extracting OU records*/
   CURSOR ou_rec IS
      SELECT ou_id, REPLACE (level_data, '\', '\\') level_data
        FROM STG_EISS_TB
       WHERE level_type = 'OU';

   /*Cursor for extracting SH records*/
   CURSOR sh_rec (id1 NUMBER) IS
      SELECT   sh_id, REPLACE (level_data, '\', '\\') level_data
          FROM STG_EISS_TB
         WHERE level_type = 'SH' AND ou_id = id1
      ORDER BY sh_id;

   /*Cursor for extracting GH records*/
   CURSOR gh_rec (id1 NUMBER) IS
      SELECT   gh_id, REPLACE (level_data, '\', '\\') level_data, grade
          FROM STG_EISS_TB
         WHERE level_type = 'GH' AND sh_id = id1
      ORDER BY gh_id;

   /*Cursor for extracting SR records*/
   CURSOR sr_rec (id1 NUMBER, v_grade VARCHAR2) IS
      SELECT   sr_id, REPLACE (level_data, '\', '\\') level_data
          FROM STG_EISS_TB
         WHERE level_type = 'SR' AND gh_id = id1 AND grade = v_grade
      ORDER BY sr_id;

   /*Cursor for extracting ST records*/
   CURSOR st_rec (id1 NUMBER, id2 NUMBER, v_grade VARCHAR2) IS
      SELECT   st_id, REPLACE (level_data, '\', '\\') level_data
          FROM STG_EISS_TB
         WHERE level_type = 'ST'
           AND sr_id = id1
           AND gh_id = id2
           AND grade = v_grade
      ORDER BY st_id;

   /*Cursor for extracting TS records*/
   CURSOR ts_rec (id1 NUMBER, id2 NUMBER, id3 NUMBER, v_grade VARCHAR2) IS
      SELECT   ts_id, REPLACE (level_data, '\', '\\') level_data
          FROM STG_EISS_TB
         WHERE level_type = 'TS'
           AND sr_id = id1
           AND st_id = id2
           AND gh_id = id3
           AND grade = v_grade
      ORDER BY sort_order;

   /*Cursor for extracting TD records*/
   CURSOR td_rec (id1 NUMBER, id2 NUMBER, id3 NUMBER, v_grade VARCHAR2) IS
      SELECT   td_id, REPLACE (level_data, '\', '\\') level_data
          FROM STG_EISS_TB
         WHERE level_type = 'TD'
           AND gh_id = id1
           AND sr_id = id2
           AND st_id = id3
           AND grade = v_grade
      ORDER BY sort_order;

   --Cursor for extracting GIrecords
   CURSOR gi_cur (v_gh_id NUMBER, v_sr_id NUMBER, v_grade VARCHAR2) IS
      SELECT   REPLACE (level_data, '\', '\\') level_data
          FROM STG_EISS_TB
         WHERE level_type = 'GI'
           AND gh_id = v_gh_id
           AND sr_id = v_sr_id
           AND grade = v_grade
      ORDER BY sort_order;
/*       ORDER BY TO_NUMBER(SUBSTR(level_data,
              INSTR(level_data, ',', 1, 2) + 1,
              INSTR(level_data, ',', 1, 3) - INSTR(level_data, ',', 1, 2)-1))
*/
BEGIN
   /*Truncate STG_EISS_LOAD_TB before loading it with records*/
   EXECUTE IMMEDIATE ('TRUNCATE TABLE STG_EISS_LOAD_TB');

   COMMIT;
   /* Initialize Serial number to zero */
   i := 0;

   FOR ou_rec1 IN ou_rec LOOP
      i := i + 1;

      /*Insert OU record into STG_EISS_LOAD_TB*/
      INSERT INTO STG_EISS_LOAD_TB
           VALUES (i, ou_rec1.level_data);

      COMMIT;

      FOR sh_rec1 IN sh_rec (ou_rec1.ou_id) LOOP
         i := i + 1;

         /*Insert SH record into STG_EISS_LOAD_TB, appended with an Spaces*/
         INSERT INTO STG_EISS_LOAD_TB
              VALUES (i, ' ' || sh_rec1.level_data);

         FOR gh_rec1 IN gh_rec (sh_rec1.sh_id) LOOP
            i := i + 1;

            /*Insert GH record into STG_EISS_LOAD_TB, appended with the correct number of Spaces*/
            INSERT INTO STG_EISS_LOAD_TB
                 VALUES (i, '  ' || gh_rec1.level_data);

            FOR sr_rec1 IN sr_rec (gh_rec1.gh_id, gh_rec1.grade) LOOP
               FOR st_rec1 IN st_rec (sr_rec1.sr_id,
                                      gh_rec1.gh_id,
                                      gh_rec1.grade
                                     ) LOOP
                  i := i + 1;

                  /*Insert SR record into STG_EISS_LOAD_TB, appended with the correct number of Spaces */
                  INSERT INTO STG_EISS_LOAD_TB
                       VALUES (i, '   ' || sr_rec1.level_data);

                  i := i + 1;

                  /*Insert ST record into STG_EISS_LOAD_TB, appended with the correct number of Spaces */
                  INSERT INTO STG_EISS_LOAD_TB
                       VALUES (i, '    ' || st_rec1.level_data);

                  FOR ts_rec1 IN ts_rec (sr_rec1.sr_id,
                                         st_rec1.st_id,
                                         gh_rec1.gh_id,
                                         gh_rec1.grade
                                        ) LOOP
                     i := i + 1;

                     /*Insert TS record into STG_EISS_LOAD_TB, appended with the correct number of Spaces */
                     INSERT INTO STG_EISS_LOAD_TB
                          VALUES (i, '     ' || ts_rec1.level_data);

                     FOR td_rec1 IN td_rec (gh_rec1.gh_id,
                                            sr_rec1.sr_id,
                                            st_rec1.st_id,
                                            gh_rec1.grade
                                           ) LOOP
                        i := i + 1;

                        /*Insert TD record into STG_EISS_LOAD_TB, appended with the correct number of Spaces */
                        INSERT INTO STG_EISS_LOAD_TB
                             VALUES (i, '      ' || td_rec1.level_data);
                     END LOOP;

                     /*Insert new EA record (the only 2 characters in that particular row is "EA" itself) after each SA record*/
-- commented for FCAT, since EA has been populated thru sp_response_fcat -- 
--                     i := i + 1;

  --                   INSERT INTO STG_EISS_LOAD_TB
    --                      VALUES (i, '      ' || 'EA ');
                  -- end of TS record
                  END LOOP;
               -- end of ST record
               END LOOP;

               -- Load GI records after ST TS TD (i.e RA, SA and EA)
               FOR gi_rec IN gi_cur (gh_rec1.gh_id,
                                     sr_rec1.sr_id,
                                     gh_rec1.grade
                                    ) LOOP
                  i := i + 1;

                  /*Insert TD record into STG_EISS_LOAD_TB, appended with the correct number of Spaces */
                  INSERT INTO STG_EISS_LOAD_TB
                       VALUES (i, '     ' || gi_rec.level_data);
               END LOOP;
            -- end of SR record
            END LOOP;
         -- end of GH record
         END LOOP;
      -- end of SH record
      END LOOP;
   -- end of OU record
   END LOOP;

   COMMIT;
END Sp_Eiss_Load_Fcat;
/
