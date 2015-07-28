CREATE OR REPLACE PACKAGE UPDATE_BMT_ITEM_ID IS

  -- AUTHOR  : TCS
  -- CREATED : 7/21/2015 4:29:19 PM
  -- PURPOSE : OAS - OAS-1806

  PROCEDURE MAPPING_BMT_ITEM_ID;

END UPDATE_BMT_ITEM_ID;
/
CREATE OR REPLACE PACKAGE BODY UPDATE_BMT_ITEM_ID IS

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
      
        UPDATE ITEM_RUBRIC_EXEMPLARS
           SET ITEM_ID = VNEW_BMT_ITEM_ID
         WHERE ITEM_ID = VOLD_DATABASE_ITEM_ID;
      
        UPDATE ITEM_RUBRIC_DATA
           SET ITEM_ID = VNEW_BMT_ITEM_ID
         WHERE ITEM_ID = VOLD_DATABASE_ITEM_ID;
      
        DELETE FROM ITEM WHERE ITEM_ID = VOLD_DATABASE_ITEM_ID;
      
        INSERT INTO LLO_RP_LOOKUP_OAS_BMT_ITEM_MAP
        VALUES
          (VOLD_DATABASE_ITEM_ID, VNEW_BMT_ITEM_ID);
      END IF;
    END LOOP;
    CLOSE BMT_ID_LISTS;
  
    COMMIT;
  EXCEPTION
    WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('BEFORE ROLLBACK');
      ROLLBACK;
    
  END MAPPING_BMT_ITEM_ID;

END UPDATE_BMT_ITEM_ID;
/
