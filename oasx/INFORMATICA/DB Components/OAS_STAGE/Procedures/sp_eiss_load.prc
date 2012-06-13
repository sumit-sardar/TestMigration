CREATE OR REPLACE PROCEDURE sp_eiss_load
IS
   /*DESCRIPTION: This stored procedure is used to load the data into the STG_EISS_LOAD_TB staging table from the STG_EISS_TB table. The procedure also inserts correct serial numbers by which the final file creation can be done by ordering by the serial number. The procedure also appends in front of the records ‘*’ which will be replaced by spaces. */
   /*AUTHOR: Wipro Offshore Team*/
   /*CREATED DATE: 06th March 2006*/
   /*MODFIED DATE: 04TH April 2006*/
   /*Integer for creating Serial numbers*/
   i   INTEGER := 0;

   /*Cursor for extracting OU records*/
   CURSOR ou_rec
   IS
      SELECT ou_id, level_data
        FROM stg_eiss_tb
       WHERE level_type = 'OU';

   /*Cursor for extracting SH records*/
   CURSOR sh_rec (id1 NUMBER)
   IS
      SELECT   sh_id, level_data
          FROM stg_eiss_tb
         WHERE level_type = 'SH' AND ou_id = id1
      ORDER BY sh_id;

   /*Cursor for extracting GH records*/
   CURSOR gh_rec (id1 NUMBER)
   IS
      SELECT   gh_id, level_data
          FROM stg_eiss_tb
         WHERE level_type = 'GH' AND sh_id = id1
      ORDER BY gh_id;

   /*Cursor for extracting SR records*/
   CURSOR sr_rec (id1 NUMBER)
   IS
      SELECT   sr_id, level_data
          FROM stg_eiss_tb
         WHERE level_type = 'SR' AND gh_id = id1
      ORDER BY sr_id;

   /*Cursor for extracting ST records*/
   CURSOR st_rec (id1 NUMBER, id2 NUMBER)
   IS
      SELECT   st_id, level_data
          FROM stg_eiss_tb
         WHERE level_type = 'ST' AND sr_id = id1 AND gh_id = id2
      ORDER BY st_id;

   /*Cursor for extracting TS records*/
   CURSOR ts_rec (id1 NUMBER, id2 NUMBER, id3 NUMBER)
   IS
      SELECT ts_id, level_data
        FROM stg_eiss_tb
       WHERE level_type = 'TS' AND sr_id = id1 AND st_id = id2
             AND gh_id = id3;

   /*Cursor for extracting TD records*/
   CURSOR td_rec (id1 NUMBER, id2 NUMBER, id3 NUMBER)
   IS
      SELECT   td_id, level_data
          FROM stg_eiss_tb
         WHERE level_type = 'TD'
           AND gh_id = id1
           AND sr_id = id2
           AND st_id = id3
--           AND ts_id = id4
      ORDER BY SUBSTR (level_data, 1, 2);

   --Cursor for extracting GIrecords
   CURSOR gi_cur (v_gh_id NUMBER, v_sr_id NUMBER)
   IS
      SELECT level_data
        FROM stg_eiss_tb
       WHERE level_type = 'GI' AND gh_id = v_gh_id AND sr_id = v_sr_id;
BEGIN
   /*Truncate STG_EISS_LOAD_TB before loading it with records*/
   EXECUTE IMMEDIATE ('TRUNCATE TABLE STG_EISS_LOAD_TB');

   COMMIT;
   /* Initialize Serial number to zero */
   i := 0;

   FOR ou_rec1 IN ou_rec
   LOOP
      i := i + 1;

      /*Insert OU record into STG_EISS_LOAD_TB*/
      INSERT INTO stg_eiss_load_tb
           VALUES (i, ou_rec1.level_data);

      COMMIT;

      FOR sh_rec1 IN sh_rec (ou_rec1.ou_id)
      LOOP
         i := i + 1;

         /*Insert SH record into STG_EISS_LOAD_TB, appended with an Spaces*/
         INSERT INTO stg_eiss_load_tb
              VALUES (i, ' ' || sh_rec1.level_data);

         FOR gh_rec1 IN gh_rec (sh_rec1.sh_id)
         LOOP
            i := i + 1;

            /*Insert GH record into STG_EISS_LOAD_TB, appended with the correct number of Spaces*/
            INSERT INTO stg_eiss_load_tb
                 VALUES (i, '  ' || gh_rec1.level_data);

            FOR sr_rec1 IN sr_rec (gh_rec1.gh_id)
            LOOP
               FOR st_rec1 IN st_rec (sr_rec1.sr_id, gh_rec1.gh_id)
               LOOP
                  i := i + 1;

                  /*Insert SR record into STG_EISS_LOAD_TB, appended with the correct number of Spaces */
                  INSERT INTO stg_eiss_load_tb
                       VALUES (i, '   ' || sr_rec1.level_data);

                  i := i + 1;

                  /*Insert ST record into STG_EISS_LOAD_TB, appended with the correct number of Spaces */
                  INSERT INTO stg_eiss_load_tb
                       VALUES (i, '    ' || st_rec1.level_data);

                  FOR ts_rec1 IN ts_rec (sr_rec1.sr_id,
                                         st_rec1.st_id,
                                         gh_rec1.gh_id
                                        )
                  LOOP
                     i := i + 1;

                     /*Insert TS record into STG_EISS_LOAD_TB, appended with the correct number of Spaces */
                     INSERT INTO stg_eiss_load_tb
                          VALUES (i, '     ' || ts_rec1.level_data);

                     FOR td_rec1 IN td_rec (gh_rec1.gh_id,
                                            sr_rec1.sr_id,
                                            st_rec1.st_id)
                     LOOP
                        i := i + 1;

                        /*Insert TD record into STG_EISS_LOAD_TB, appended with the correct number of Spaces */
                        INSERT INTO stg_eiss_load_tb
                             VALUES (i, '      ' || td_rec1.level_data);
                     END LOOP;

                     /*Insert new EA record (the only 2 characters in that particular row is "EA" itself) after each SA record*/
                     i := i + 1;

                     INSERT INTO stg_eiss_load_tb
                          VALUES (i, '      ' || 'EA ');
                  -- end of TS record
                  END LOOP;
               -- end of ST record
               END LOOP;

               -- Load GI records after ST TS TD (i.e RA, SA and EA)
               FOR gi_rec IN gi_cur (gh_rec1.gh_id, sr_rec1.sr_id)
               LOOP
                  i := i + 1;

                  /*Insert TD record into STG_EISS_LOAD_TB, appended with the correct number of Spaces */
                  INSERT INTO stg_eiss_load_tb
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
END sp_eiss_load;
/
