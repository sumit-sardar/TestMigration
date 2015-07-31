create or replace package PKG_REMOVE_DUPLICATE_ITEM_IDS is

  -- Author  : 763329
  -- Created : 7/29/2015 3:34:31 PM
  -- Purpose :

  PROCEDURE CLEANUP_DISCREPANT_DATA;

  PROCEDURE MAPPING_BMT_ITEM_ID;

  PROCEDURE UPDATE_DISCREPANT_ITEM_ID;

  PROCEDURE UPDATE_DAS_ITEM_ID;

end PKG_REMOVE_DUPLICATE_ITEM_IDS;
/
create or replace package body PKG_REMOVE_DUPLICATE_ITEM_IDS is

  PROCEDURE CLEANUP_DISCREPANT_DATA AS
    /*
    THIS PROCEDURE FETCHES THE DUPLICATE AS WELL AS DISCREPANT DAS_ITEM_IDS 
    DUPLICATE OAS-BMT ITEM PAIRS WILL BE REPLACED BY ONLY 1 ENTRY AND DISCREPANT
    ROWS WILL BE UPDATED TO 'DP' STATUS;
    */
  
    /* CURSOR TO FETCH ALL FOR DUPLICATE DATA */
  
    CURSOR CUR_REM_DUP_DATA IS
      SELECT OAS_ITEM_ID, BMT_ITEM_ID
        FROM OAS_BMT_ITEM_MAP
       WHERE BMT_ITEM_ID IN (SELECT BMT_ITEM_ID
                               FROM OAS_BMT_ITEM_MAP
                              WHERE ACTIVATION_STATUS = 'AC'
                              GROUP BY BMT_ITEM_ID
                             HAVING COUNT(BMT_ITEM_ID) > 1)
         AND ACTIVATION_STATUS = 'AC'
       GROUP BY BMT_ITEM_ID, OAS_ITEM_ID
      HAVING COUNT(OAS_ITEM_ID) > 1
       ORDER BY BMT_ITEM_ID;
  
    /*CURSOR TO FETCH ALL DISCREPANT DATA*/
  
    CURSOR CUR_FETCH_DUP_BMT_ITEM_ID IS
      SELECT BMT_ITEM_ID
        FROM OAS_BMT_ITEM_MAP
       WHERE ACTIVATION_STATUS = 'AC'
       GROUP BY BMT_ITEM_ID
      HAVING COUNT(BMT_ITEM_ID) > 1;
  
  BEGIN
    --FIRST DELETE DUPLICATE ROWS THEN INSERT SINGLE ROW
    FOR C_DUP_DATA IN CUR_REM_DUP_DATA LOOP
    
      DELETE FROM OAS_BMT_ITEM_MAP
       WHERE OAS_ITEM_ID = C_DUP_DATA.OAS_ITEM_ID
         AND BMT_ITEM_ID = C_DUP_DATA.BMT_ITEM_ID;
    
      INSERT INTO OAS_BMT_ITEM_MAP
        (OAS_ITEM_ID, BMT_ITEM_ID, ACTIVATION_STATUS)
      VALUES
        (C_DUP_DATA.OAS_ITEM_ID, C_DUP_DATA.BMT_ITEM_ID, 'AC');
    
    END LOOP;
  
    --NOW REMOVE DISCREPANCIES IN OAS_BMT_ITEM_MAP TABLE. MARK DISCREPANT DATAS WITH DP STATUS
    FOR DUP_BMT_ITEM_ID IN CUR_FETCH_DUP_BMT_ITEM_ID LOOP
      UPDATE OAS_BMT_ITEM_MAP
         SET ACTIVATION_STATUS = 'DP', UPDATED_DATE = SYSDATE
       WHERE BMT_ITEM_ID = DUP_BMT_ITEM_ID.BMT_ITEM_ID
         AND ACTIVATION_STATUS = 'AC';
    END LOOP;
  
  END CLEANUP_DISCREPANT_DATA;
  ---------------------------------------------------------------------------------------------------
  PROCEDURE MAPPING_BMT_ITEM_ID AS
  
    VOLD_DATABASE_ITEM_ID LLO_RP_LOOKUP_ITEM.NEW_ID%TYPE;
    VNEW_BMT_ITEM_ID      OAS_BMT_ITEM_MAP.BMT_ITEM_ID%TYPE;
    V_PRESENT             INTEGER := 0;
  
    CURSOR BMT_ID_LISTS IS
      SELECT LLI.NEW_ID, OBI.BMT_ITEM_ID
        FROM LLO_RP_LOOKUP_ITEM LLI, OAS_BMT_ITEM_MAP OBI
       WHERE OBI.OAS_ITEM_ID = LLI.OLD_ID
         AND ACTIVATION_STATUS = 'AC';
  
  BEGIN
  
    OPEN BMT_ID_LISTS;
    LOOP
      FETCH BMT_ID_LISTS
        INTO VOLD_DATABASE_ITEM_ID, VNEW_BMT_ITEM_ID;
      EXIT WHEN BMT_ID_LISTS%NOTFOUND;
    
      V_PRESENT := 0;
    
      -- CHECK IF THE NEW DAS ITEM IS ALREADY PRESENT OR NOT
      SELECT COUNT(1)
        INTO V_PRESENT
        FROM ITEM
       WHERE ITEM_ID = VNEW_BMT_ITEM_ID;
    
      -- PROCEED ONLY IF THE DAS ITEM IS NOT PRESENT ALREADY
      IF V_PRESENT = 0 THEN
      
        INSERT INTO ITEM
          (ITEM_ID,
           CORRECT_ANSWER,
           ITEM_TYPE,
           CREATED_BY,
           CREATED_DATE_TIME,
           UPDATED_BY,
           UPDATED_DATE_TIME,
           ACTIVATION_STATUS,
           DESCRIPTION,
           NAME,
           EXT_STIMULUS_ID,
           EXT_STIMULUS_TITLE,
           VERSION,
           ITEM_DISPLAY_NAME,
           TEMPLATE_ID,
           THINK_CODE,
           ONLINE_CR,
           PUBLISHED,
           LOCKED,
           IS_VALID,
           CUSTOMER_CREATED,
           CUSTOMER_ID,
           EXTERNAL_ID,
           EXTERNAL_SYSTEM,
           ANSWER_AREA,
           GRIDDED_COLUMNS,
           ADS_ITEM_ID)
          (SELECT VNEW_BMT_ITEM_ID, -- CHANGE
                  CORRECT_ANSWER,
                  ITEM_TYPE,
                  CREATED_BY,
                  CREATED_DATE_TIME,
                  UPDATED_BY,
                  SYSDATE,
                  ACTIVATION_STATUS,
                  DESCRIPTION,
                  NAME,
                  EXT_STIMULUS_ID,
                  EXT_STIMULUS_TITLE,
                  VERSION,
                  VNEW_BMT_ITEM_ID, -- CHANGE
                  TEMPLATE_ID,
                  THINK_CODE,
                  ONLINE_CR,
                  PUBLISHED,
                  LOCKED,
                  IS_VALID,
                  CUSTOMER_CREATED,
                  CUSTOMER_ID,
                  EXTERNAL_ID,
                  EXTERNAL_SYSTEM,
                  ANSWER_AREA,
                  GRIDDED_COLUMNS,
                  ADS_ITEM_ID
             FROM ITEM
            WHERE ITEM_ID = VOLD_DATABASE_ITEM_ID);
      
        UPDATE DATAPOINT
           SET ITEM_ID = VNEW_BMT_ITEM_ID
         WHERE ITEM_ID = VOLD_DATABASE_ITEM_ID;
      
        UPDATE ITEM_SET_ITEM
           SET ITEM_ID = VNEW_BMT_ITEM_ID
         WHERE ITEM_ID = VOLD_DATABASE_ITEM_ID;
      
        INSERT INTO ITEM_RUBRIC_DATA
          (ITEM_ID, SCORE, RUBRIC_DESCRIPTION)
          (SELECT VNEW_BMT_ITEM_ID, SCORE, RUBRIC_DESCRIPTION
             FROM ITEM_RUBRIC_DATA
            WHERE ITEM_ID = VOLD_DATABASE_ITEM_ID);
      
        UPDATE ITEM_RUBRIC_EXEMPLARS
           SET ITEM_ID = VNEW_BMT_ITEM_ID
         WHERE ITEM_ID = VOLD_DATABASE_ITEM_ID;
      
        DELETE FROM ITEM_RUBRIC_DATA WHERE ITEM_ID = VOLD_DATABASE_ITEM_ID;
      
        DELETE FROM ITEM WHERE ITEM_ID = VOLD_DATABASE_ITEM_ID;
      
        INSERT INTO LLO_RP_LOOKUP_OAS_BMT_ITEM_MAP
        VALUES
          (VOLD_DATABASE_ITEM_ID, VNEW_BMT_ITEM_ID);
      
        UPDATE OAS_BMT_ITEM_MAP
           SET ACTIVATION_STATUS = 'IN', UPDATED_DATE = SYSDATE
         WHERE BMT_ITEM_ID = VNEW_BMT_ITEM_ID;
      END IF;
    END LOOP;
    CLOSE BMT_ID_LISTS;
  
  END MAPPING_BMT_ITEM_ID;
  --------------------------------------------------------------------------------

  PROCEDURE UPDATE_DISCREPANT_ITEM_ID AS
    /*
    THIS PROCEDURE TAKES INPUT DP MARKED BMT_ITEM_IDS 
    AND FETCHES THEIR CORRESPONDING ITEM_ID IN ITEM_SET_ITEM TABLE
    AND ITEM_TABLE. IT THEN UPDATES IN ITEM_SET_ITEM TABLE WITH BMT_ITEM_ID
    AND DELETES THE PREVIOUS ITEM_IDS FROM ITEM TABLE.
    */
    V_COUNT   NUMBER := 0;
    V_PRESENT NUMBER := 0;
    /* FETCHES ALL BMT_ITEM_IDS THAT ARE IN DP STATUS IN OAS_BMT_ITEM_MAP TABLE*/
    CURSOR CUR_FETCH_DUP_BMT_ITEM_ID IS
      SELECT DISTINCT (BMT_ITEM_ID)
        FROM OAS_BMT_ITEM_MAP
       WHERE ACTIVATION_STATUS = 'DP';
  
    /*FOR EACH ABOVE FETCHED BMT_ITEM_ID , IT FETCHES THE CORRESPONDING ITEM_SET_ID , ITEM_ID*/
  
    CURSOR CUR_FETCH_NEW_ITEM_SET_ID(IN_BMT_ITEM_ID OAS_BMT_ITEM_MAP.BMT_ITEM_ID%TYPE) IS
      SELECT ISI.ITEM_SET_ID, ISI.ITEM_ID
        FROM ITEM_SET_ITEM      ISI,
             ITEM_SET           ISET,
             OAS_BMT_ITEM_MAP   OBI,
             LLO_RP_LOOKUP_ITEM LLO
       WHERE ISI.ITEM_SET_ID = ISET.ITEM_SET_ID
         AND ISI.ITEM_ID = LLO.NEW_ID
         AND LLO.OLD_ID = OBI.OAS_ITEM_ID
         AND ISET.ITEM_SET_TYPE = 'TD'
         AND ISET.ACTIVATION_STATUS = 'AC'
         AND OBI.ACTIVATION_STATUS = 'DP'
         AND OBI.BMT_ITEM_ID = IN_BMT_ITEM_ID;
  
    n_item   c_item_id_list := c_item_id_list();
    index_no integer := 1;
  
  BEGIN
  
    FOR C_BMT_ITEM_ID IN CUR_FETCH_DUP_BMT_ITEM_ID LOOP
    
      SELECT COUNT(1)
        INTO V_PRESENT
        FROM ITEM
       WHERE ITEM_ID = C_BMT_ITEM_ID.BMT_ITEM_ID;
    
      -- PROCEED ONLY IF THE DAS ITEM IS NOT PRESENT ALREADY
      IF V_PRESENT = 0 THEN
        V_COUNT := 0;
        FOR C_NEW_ITEM_ID IN CUR_FETCH_NEW_ITEM_SET_ID(C_BMT_ITEM_ID.BMT_ITEM_ID) LOOP
          IF V_COUNT = 0 THEN
            /* dbms_output.put_line('insert here -> ' ||
            C_BMT_ITEM_ID.BMT_ITEM_ID);*/
          
            INSERT INTO ITEM
              (ITEM_ID,
               CORRECT_ANSWER,
               ITEM_TYPE,
               CREATED_BY,
               CREATED_DATE_TIME,
               UPDATED_BY,
               UPDATED_DATE_TIME,
               ACTIVATION_STATUS,
               DESCRIPTION,
               NAME,
               EXT_STIMULUS_ID,
               EXT_STIMULUS_TITLE,
               VERSION,
               ITEM_DISPLAY_NAME,
               TEMPLATE_ID,
               THINK_CODE,
               ONLINE_CR,
               PUBLISHED,
               LOCKED,
               IS_VALID,
               CUSTOMER_CREATED,
               CUSTOMER_ID,
               EXTERNAL_ID,
               EXTERNAL_SYSTEM,
               ANSWER_AREA,
               GRIDDED_COLUMNS,
               ADS_ITEM_ID)
              (SELECT C_BMT_ITEM_ID.BMT_ITEM_ID, -- CHANGE
                      CORRECT_ANSWER,
                      ITEM_TYPE,
                      CREATED_BY,
                      CREATED_DATE_TIME,
                      UPDATED_BY,
                      SYSDATE,
                      ACTIVATION_STATUS,
                      DESCRIPTION,
                      NAME,
                      EXT_STIMULUS_ID,
                      EXT_STIMULUS_TITLE,
                      VERSION,
                      C_BMT_ITEM_ID.BMT_ITEM_ID, -- CHANGE
                      TEMPLATE_ID,
                      THINK_CODE,
                      ONLINE_CR,
                      PUBLISHED,
                      LOCKED,
                      IS_VALID,
                      CUSTOMER_CREATED,
                      CUSTOMER_ID,
                      EXTERNAL_ID,
                      EXTERNAL_SYSTEM,
                      ANSWER_AREA,
                      GRIDDED_COLUMNS,
                      ADS_ITEM_ID
                 FROM ITEM
                WHERE ITEM_ID = C_NEW_ITEM_ID.ITEM_ID);
          END IF;
          V_COUNT := V_COUNT + 1;
        
          UPDATE ITEM_SET_ITEM
             SET ITEM_ID = C_BMT_ITEM_ID.BMT_ITEM_ID
           WHERE ITEM_SET_ID = C_NEW_ITEM_ID.ITEM_SET_ID
             AND ITEM_ID = C_NEW_ITEM_ID.ITEM_ID;
        
          n_item.extend;
          n_item(index_no) := C_NEW_ITEM_ID.ITEM_ID;
          index_no := index_no + 1;
        
          INSERT INTO LLO_RP_LOOKUP_OAS_BMT_ITEM_MAP
          VALUES
            (C_NEW_ITEM_ID.ITEM_ID, C_BMT_ITEM_ID.BMT_ITEM_ID);
        
        END LOOP;
        --END OF INNER LOOP
        /***
        NOW CHANGE THE ACTIVATION_STATUS OF BMT_ITEM_ID IN OAS_BMT_ITEM_MAP TABLE
        FROM DP TO IN
        ***/
      
        UPDATE OAS_BMT_ITEM_MAP
           SET ACTIVATION_STATUS = 'IN', UPDATED_DATE = SYSDATE
         WHERE BMT_ITEM_ID = C_BMT_ITEM_ID.BMT_ITEM_ID;
      
      END IF;
    
    END LOOP;
  
    forall i in indices of n_item
      delete from item where item_id = n_item(i);
  
    dbms_output.put_line('END OF PROCEDURE');
  
  END UPDATE_DISCREPANT_ITEM_ID;
  ---------------------------------------------------------------------------------------  
  PROCEDURE UPDATE_DAS_ITEM_ID IS
  BEGIN
  
    dbms_output.put_line('Cleanup procedure started.');
    CLEANUP_DISCREPANT_DATA(); -- CLEAN DUPLICATE AND DISCREPANT TABLES
    dbms_output.put_line('Duplicates and disrepancies removed');
  
    MAPPING_BMT_ITEM_ID(); -- CREATE  ENTRIES FOR CORRECT BMT_ITEM_IDS
    dbms_output.put_line('All correct BMT_ITEM_IDS updated in item_set_item table');
  
    UPDATE_DISCREPANT_ITEM_ID(); -- CREATE ENTRIES FOR DISCREPANT BMT_ITEM_IDS
    dbms_output.put_line('Discrepants BMT_ITEM_IDS updated in item_set_item table');
    
      COMMIT;
    EXCEPTION
      WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('BEFORE ROLLBACK');
        ROLLBACK;
  
  END UPDATE_DAS_ITEM_ID;

end PKG_REMOVE_DUPLICATE_ITEM_IDS;
/
