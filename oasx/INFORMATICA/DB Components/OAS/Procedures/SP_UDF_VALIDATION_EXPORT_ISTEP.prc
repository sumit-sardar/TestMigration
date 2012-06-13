CREATE OR REPLACE PROCEDURE SP_UDF_VALIDATION_EXPORT_ISTEP(STUDENT_ID          IN INTEGER,
                                                           P_TEST_ROSTER_ID    IN INTEGER,
                                                           UDF_VALIDATION_FLAG OUT VARCHAR2) AS
  --  This Procedure is Used to Load the concatenated string of all the TS records,
  --    Concatenated string of all the Response arrays and Score arrays for a particular Test Catalog Item_set_id of the Student --
  
  V_ITEM_ID            ITEM_SET.ITEM_SET_ID%TYPE;
  V_STATUS             STUDENT_ITEM_SET_STATUS.VALIDATION_STATUS%TYPE;
  ISET_NAME            ITEM_SET.ITEM_SET_NAME%TYPE;
  V_FLAG               BOOLEAN := TRUE;
  UDF_VALIDATION_FLAG1 VARCHAR2(100);

  CURSOR C_ITEMS_CUR IS
    SELECT SISS.ITEM_SET_ID, SISS.VALIDATION_STATUS
    
      FROM STG_EISS_TB SE, STUDENT_ITEM_SET_STATUS SISS
     WHERE SE.TEST_ROSTER_ID = SISS.TEST_ROSTER_ID
       AND SE.TEST_ROSTER_ID = P_TEST_ROSTER_ID
       AND SE.SR_ID = STUDENT_ID;

  CURSOR C_CHECK_VALIDITY_CUR(ITEM_ID NUMBER) IS
    SELECT ISET.ITEM_SET_NAME
      FROM ITEM_SET ISET
     WHERE ISET.ITEM_SET_ID = ITEM_ID;

BEGIN

  --OPEN c_items_cur;

  FOR R_ITEMS_CUR IN C_ITEMS_CUR
  
   LOOP
  
    V_STATUS := R_ITEMS_CUR.VALIDATION_STATUS;
    FOR R_CHECK_VALIDITY_CUR IN C_CHECK_VALIDITY_CUR(R_ITEMS_CUR.ITEM_SET_ID)
    
     LOOP
    
      ISET_NAME := R_CHECK_VALIDITY_CUR.ITEM_SET_NAME;
    
      IF ISET_NAME = 'Mathematics: Session 1' THEN
      
        IF V_STATUS = 'VA' THEN
        
          UDF_VALIDATION_FLAG1 := UDF_VALIDATION_FLAG1 || ' ';
        
        ELSE
        
          UDF_VALIDATION_FLAG1 := UDF_VALIDATION_FLAG1 || '1';
          V_FLAG               := FALSE;
        
        END IF;
      
      ELSIF ISET_NAME = 'Mathematics: Session 2' THEN
      
        IF V_STATUS = 'VA' THEN
        
          UDF_VALIDATION_FLAG1 := UDF_VALIDATION_FLAG1 || ' ';
        
        ELSE
        
          UDF_VALIDATION_FLAG1 := UDF_VALIDATION_FLAG1 || '1';
          V_FLAG               := FALSE;
        
        END IF;
      
      ELSIF ISET_NAME = 'Mathematics: Session 3' THEN
      
        IF V_STATUS = 'VA' THEN
        
          UDF_VALIDATION_FLAG1 := UDF_VALIDATION_FLAG1 || ' ';
        
        ELSE
        
          UDF_VALIDATION_FLAG1 := UDF_VALIDATION_FLAG1 || '1';
          V_FLAG               := FALSE;
        
        END IF;
      
      ELSIF ISET_NAME = 'English/Language Arts: Session 1' THEN
      
        IF V_STATUS = 'VA' THEN
        
          UDF_VALIDATION_FLAG1 := UDF_VALIDATION_FLAG1 || ' ';
        
        ELSE
        
          UDF_VALIDATION_FLAG1 := UDF_VALIDATION_FLAG1 || '1';
          V_FLAG               := FALSE;
        
        END IF;
      
      ELSIF ISET_NAME = 'English/Language Arts: Session 2' THEN
      
        IF V_STATUS = 'VA' THEN
        
          UDF_VALIDATION_FLAG1 := UDF_VALIDATION_FLAG1 || ' ';
        
        ELSE
        
          UDF_VALIDATION_FLAG1 := UDF_VALIDATION_FLAG1 || '1';
          V_FLAG               := FALSE;
        
        END IF;
      
      ELSIF ISET_NAME = 'English/Language Arts: Session 3' THEN
      
        IF V_STATUS = 'VA' THEN
        
          UDF_VALIDATION_FLAG1 := UDF_VALIDATION_FLAG1 || ' ';
        
        ELSE
        
          UDF_VALIDATION_FLAG1 := UDF_VALIDATION_FLAG1 || '1';
          V_FLAG               := FALSE;
        
        END IF;
      
      ELSIF ISET_NAME = 'Science: Session 1' THEN
      
        IF V_STATUS = 'VA' THEN
        
          UDF_VALIDATION_FLAG1 := UDF_VALIDATION_FLAG1 || ' ';
        
        ELSE
        
          UDF_VALIDATION_FLAG1 := UDF_VALIDATION_FLAG1 || '1';
          V_FLAG               := FALSE;
        
        END IF;
      
      ELSIF ISET_NAME = 'Science: Session 2' THEN
      
        IF V_STATUS = 'VA' THEN
        
          UDF_VALIDATION_FLAG1 := UDF_VALIDATION_FLAG1 || ' ';
        
        ELSE
        
          UDF_VALIDATION_FLAG1 := UDF_VALIDATION_FLAG1 || '1';
          V_FLAG               := FALSE;
        
        END IF;
      
      ELSIF ISET_NAME = 'Science: Session 3' THEN
      
        IF V_STATUS = 'VA' THEN
        
          UDF_VALIDATION_FLAG1 := UDF_VALIDATION_FLAG1 || ' ';
        
        ELSE
        
          UDF_VALIDATION_FLAG1 := UDF_VALIDATION_FLAG1 || '1';
          V_FLAG               := FALSE;
        
        END IF;
      
      ELSE
      
        UDF_VALIDATION_FLAG1 := UDF_VALIDATION_FLAG1 || ' ';
      
      END IF;
    END LOOP;
  
  END LOOP;

  IF V_FLAG = TRUE THEN
  
    UDF_VALIDATION_FLAG1 := ' ' || UDF_VALIDATION_FLAG1;
  
  ELSE
  
    UDF_VALIDATION_FLAG1 := '1' || UDF_VALIDATION_FLAG1;
  
  END IF;
  UDF_VALIDATION_FLAG := UDF_VALIDATION_FLAG1;
  COMMIT;

END;
/
