CREATE OR REPLACE PROCEDURE SP_EISS_LOAD_GA IS
  /*DESCRIPTION: This stored procedure is used to load the data into the STG_EISS_LOAD_TB staging table from the STG_EISS_TB table. The procedure also inserts correct serial numbers by which the final file creation can be done by ordering by the serial number. The procedure also appends in front of the records ‘*’ which will be replaced by spaces. */

  /*Integer for creating Serial numbers*/
  I INTEGER := 0;
  --p_opunit stg_eiss_tb.opunit%type;

  /*Cursor for extracting OU records*/
  CURSOR OU_REC IS
    SELECT OU_ID, OPUNIT, REPLACE(LEVEL_DATA, '\', '\\') LEVEL_DATA
      FROM STG_EISS_TB
     WHERE LEVEL_TYPE = 'OU';

  -------  NOTE : opunit column is added in all the cursors during ISTEP 2008  ----------------------------

  /*Cursor for extracting SH records*/
  CURSOR SH_REC(ID1 NUMBER, P_OPUNIT STG_EISS_TB.OPUNIT%TYPE) IS
    SELECT SH_ID, REPLACE(LEVEL_DATA, '\', '\\') LEVEL_DATA
      FROM STG_EISS_TB
     WHERE LEVEL_TYPE = 'SH'
       AND OU_ID = ID1
       AND OPUNIT = P_OPUNIT
     ORDER BY SH_ID;

  /*Cursor for extracting GH records*/
  CURSOR GH_REC(ID1 NUMBER, P_OPUNIT STG_EISS_TB.OPUNIT%TYPE) IS
    SELECT GH_ID, REPLACE(LEVEL_DATA, '\', '\\') LEVEL_DATA, GRADE
      FROM STG_EISS_TB
     WHERE LEVEL_TYPE = 'GH'
       AND SH_ID = ID1
       AND OPUNIT = P_OPUNIT
     ORDER BY GH_ID;

  /*Cursor for extracting SR records*/
  CURSOR SR_REC(ID1 NUMBER, V_GRADE VARCHAR2, P_OPUNIT STG_EISS_TB.OPUNIT%TYPE) IS
    SELECT SR_ID, REPLACE(LEVEL_DATA, '\', '\\') LEVEL_DATA
      FROM STG_EISS_TB
     WHERE LEVEL_TYPE = 'SR'
       AND GH_ID = ID1
       AND GRADE = V_GRADE
       AND OPUNIT = P_OPUNIT
     ORDER BY SR_ID;

  /*Cursor for extracting ST records*/
  CURSOR ST_REC(ID1 NUMBER, ID2 NUMBER, V_GRADE VARCHAR2, P_OPUNIT STG_EISS_TB.OPUNIT%TYPE) IS
    SELECT ST_ID, REPLACE(LEVEL_DATA, '\', '\\') LEVEL_DATA
      FROM STG_EISS_TB
     WHERE LEVEL_TYPE = 'ST'
       AND SR_ID = ID1
       AND GH_ID = ID2
       AND GRADE = V_GRADE
       AND OPUNIT = P_OPUNIT
     ORDER BY ST_ID;

  /*Cursor for extracting TS records*/
  CURSOR TS_REC(ID1 NUMBER, ID2 NUMBER, ID3 NUMBER, V_GRADE VARCHAR2, P_OPUNIT STG_EISS_TB.OPUNIT%TYPE) IS
    SELECT TS_ID, REPLACE(LEVEL_DATA, '\', '\\') LEVEL_DATA,TEST_ROSTER_ID
      FROM STG_EISS_TB
     WHERE LEVEL_TYPE = 'TS'
       AND SR_ID = ID1
       AND ST_ID = ID2
       AND GH_ID = ID3
       AND GRADE = V_GRADE
    
     ORDER BY TO_NUMBER(SORT_ORDER);

  /*Cursor for extracting TD records*/
  CURSOR TD_REC(ID1 NUMBER, ID2 NUMBER, ID3 NUMBER, ID4 NUMBER, V_GRADE VARCHAR2, V_TEST_ROSTER_ID NUMBER, P_OPUNIT STG_EISS_TB.OPUNIT%TYPE) IS
    SELECT TD_ID, REPLACE(LEVEL_DATA, '\', '\\') LEVEL_DATA
      FROM STG_EISS_TB
     WHERE LEVEL_TYPE = 'TD'
       AND GH_ID = ID1
       AND SR_ID = ID2
       AND ST_ID = ID3
       AND GRADE = V_GRADE
       AND TS_ID = ID4 /*Added to get the TDs for a paricular subtest id*/
       AND TEST_ROSTER_ID = V_TEST_ROSTER_ID
    
     ORDER BY SORT_ORDER;

  --Cursor for extracting GIrecords
  CURSOR GI_CUR(V_GH_ID NUMBER, V_SR_ID NUMBER, V_TS_ID NUMBER, V_GRADE VARCHAR2, P_OPUNIT STG_EISS_TB.OPUNIT%TYPE) IS
    SELECT REPLACE(LEVEL_DATA, '\', '\\') LEVEL_DATA
      FROM STG_EISS_TB
     WHERE LEVEL_TYPE = 'GI'
       AND GH_ID = V_GH_ID
       AND SR_ID = V_SR_ID
       AND GRADE = V_GRADE
       AND TS_ID = V_TS_ID /*Added to get the TDs for a paricular subtest id*/
    
     ORDER BY SORT_ORDER;
  /*       ORDER BY TO_NUMBER(SUBSTR(level_data,
                INSTR(level_data, ',', 1, 2) + 1,
                INSTR(level_data, ',', 1, 3) - INSTR(level_data, ',', 1, 2)-1))
  */
BEGIN
  /*Truncate STG_EISS_LOAD_TB before loading it with records*/
  EXECUTE IMMEDIATE ('TRUNCATE TABLE STG_EISS_LOAD_TB');

  COMMIT;
  /* Initialize Serial number to zero */
  I := 0;

  FOR OU_REC1 IN OU_REC LOOP
    I := I + 1;
    --p_opunit := ou_rec1.opunit;
  
    /*Insert OU record into STG_EISS_LOAD_TB*/
    INSERT INTO STG_EISS_LOAD_TB
    VALUES
      (I, OU_REC1.LEVEL_DATA, OU_REC1.OPUNIT, OU_REC1.OU_ID);
  
    COMMIT;
  
    FOR SH_REC1 IN SH_REC(OU_REC1.OU_ID, OU_REC1.OPUNIT) LOOP
      I := I + 1;
    
      /*Insert SH record into STG_EISS_LOAD_TB, appended with an Spaces*/
      INSERT INTO STG_EISS_LOAD_TB
      VALUES
        (I, ' ' || SH_REC1.LEVEL_DATA, OU_REC1.OPUNIT, OU_REC1.OU_ID);
    
      FOR GH_REC1 IN GH_REC(SH_REC1.SH_ID, OU_REC1.OPUNIT) LOOP
        I := I + 1;
      
        /*Insert GH record into STG_EISS_LOAD_TB, appended with the correct number of Spaces*/
        INSERT INTO STG_EISS_LOAD_TB
        VALUES
          (I, '  ' || GH_REC1.LEVEL_DATA, OU_REC1.OPUNIT, OU_REC1.OU_ID);
      
        FOR SR_REC1 IN SR_REC(GH_REC1.GH_ID, GH_REC1.GRADE, OU_REC1.OPUNIT) LOOP
        
          FOR ST_REC1 IN ST_REC(SR_REC1.SR_ID,
                                GH_REC1.GH_ID,
                                GH_REC1.GRADE,
                                OU_REC1.OPUNIT) LOOP
            I := I + 1;
          
            /*Insert SR record into STG_EISS_LOAD_TB, appended with the correct number of Spaces */
            INSERT INTO STG_EISS_LOAD_TB
            VALUES
              (I,
               '   ' || SR_REC1.LEVEL_DATA,
               OU_REC1.OPUNIT,
               OU_REC1.OU_ID);
          
            I := I + 1;
          
            /*Insert ST record into STG_EISS_LOAD_TB, appended with the correct number of Spaces */
          
            INSERT INTO STG_EISS_LOAD_TB
            VALUES
              (I,
               '    ' || ST_REC1.LEVEL_DATA,
               OU_REC1.OPUNIT,
               OU_REC1.OU_ID);
          
            FOR TS_REC1 IN TS_REC(SR_REC1.SR_ID,
                                  ST_REC1.ST_ID,
                                  GH_REC1.GH_ID,
                                  GH_REC1.GRADE,
                                  OU_REC1.OPUNIT) LOOP
              I := I + 1;
            
              /*Insert TS record into STG_EISS_LOAD_TB, appended with the correct number of Spaces */
              INSERT INTO STG_EISS_LOAD_TB
              VALUES
                (I,
                 '     ' || TS_REC1.LEVEL_DATA,
                 OU_REC1.OPUNIT,
                 OU_REC1.OU_ID);
            
              FOR TD_REC1 IN TD_REC(GH_REC1.GH_ID,
                                    SR_REC1.SR_ID,
                                    ST_REC1.ST_ID,
                                    TS_REC1.TS_ID, /*Added to get TD s of a subtest id*/
                                    GH_REC1.GRADE,
                                    TS_REC1.TEST_ROSTER_ID,
                                    OU_REC1.OPUNIT) LOOP
                I := I + 1;
              
                /*Insert TD record into STG_EISS_LOAD_TB, appended with the correct number of Spaces */
                INSERT INTO STG_EISS_LOAD_TB
                VALUES
                  (I,
                   '      ' || TD_REC1.LEVEL_DATA,
                   OU_REC1.OPUNIT,
                   OU_REC1.OU_ID);
              END LOOP;
 
           -- Load GI records after ST TS TD (i.e RA, SA and EA)
          FOR GI_REC IN GI_CUR(GH_REC1.GH_ID,
                               SR_REC1.SR_ID,
                               TS_REC1.TS_ID, /*Added to get TD s of a subtest id*/
                               GH_REC1.GRADE,
                               OU_REC1.OPUNIT) LOOP
            I := I + 1;
          
            /*Insert TD record into STG_EISS_LOAD_TB, appended with the correct number of Spaces */
            INSERT INTO STG_EISS_LOAD_TB
            VALUES
              (I,
               '     ' || GI_REC.LEVEL_DATA,
               OU_REC1.OPUNIT,
               OU_REC1.OU_ID);
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
        
          -- end of SR record
        END LOOP;
        -- end of GH record
      END LOOP;
      -- end of SH record
    END LOOP;
    -- end of OU record
  END LOOP;

  COMMIT;
END SP_EISS_LOAD_GA;
/
