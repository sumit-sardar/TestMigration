CREATE OR REPLACE PROCEDURE SP_OPUNIT_ROSTERCOUNT_GA IS

  V_DIST_ID          STG_DISTRICT_OPUNIT.DISTRICT_ORG_ID%TYPE;
  V_CUST_ID          STG_PARAM_WINSCR_TB.CUSTOMER_ID%TYPE;
  V_ROSTER_COUNT     STG_DISTRICT_OPUNIT.ROSTER_COUNT%TYPE;
  V_OPUNIT           STG_DISTRICT_OPUNIT.OPUNIT%TYPE;
  V_TEST_ROSTER_ID   STG_TEST_ROSTER.TEST_ROSTER_ID%TYPE;
  V_CALIBRATION_FLAG STG_DISTRICT_OPUNIT.CALIBRATION%TYPE;
  V_TEACHER_ID       STG_DISTRICT_OPUNIT.DISTRICT_ORG_ID%TYPE;
  V_CURRENT_OPUNIT   STG_DISTRICT_OPUNIT.CURRENT_OPUNIT%TYPE;
  FLAG               BOOLEAN;
  --cursor for extracting test_roster_id from stg_test_roster table
  CURSOR CUR_TESTROSTER IS
  --select test_roster_id,opunit from stg_test_roster where opunit is null;
    SELECT STR.TEST_ROSTER_ID AS TEST_ROSTER_ID,
           STR.STUDENT_ID,
           STR.OPUNIT,
           TR.ORG_NODE_ID AS CLASS_ID
      FROM STG_TEST_ROSTER STR, TEST_ROSTER TR
     WHERE STR.TEST_ROSTER_ID = TR.TEST_ROSTER_ID
       AND STR.OPUNIT IS NULL
     ORDER BY TR.ORG_NODE_ID;

  --cursor for  stg_district_opunit table
  CURSOR CUR_DISTRICT_OPUNIT(P_DISTRICT_ID STG_DISTRICT_OPUNIT.DISTRICT_ORG_ID%TYPE, P_CALIBRATION STG_DISTRICT_OPUNIT.CALIBRATION%TYPE) IS
    SELECT STG_DISTRICT_OPUNIT_ID, DISTRICT_ORG_ID, ROSTER_COUNT, OPUNIT, CURRENT_OPUNIT
      FROM STG_DISTRICT_OPUNIT
     WHERE DISTRICT_ORG_ID = P_DISTRICT_ID
       AND CALIBRATION = P_CALIBRATION
       AND OPUNIT = (SELECT MAX(OPUNIT)
                       FROM STG_DISTRICT_OPUNIT
                      WHERE DISTRICT_ORG_ID = P_DISTRICT_ID);

  --cursor rowtype declaration
  CURSOR_DOP_REC CUR_DISTRICT_OPUNIT%ROWTYPE;

  --cursor for  stg_param_winscr_tb table
  CURSOR CUR_CUST_ID IS
    SELECT CUSTOMER_ID FROM STG_PARAM_WINSCR_TB WHERE SNO = 1;

  --cursor for fetching the ditrict_id
  CURSOR CUR_DISTRICT_ORG_ID(P_TEST_ROSTER_ID STG_TEST_ROSTER.TEST_ROSTER_ID%TYPE) IS
    SELECT ONG.ORG_NODE_ID
      FROM TEST_ROSTER TR,

           ORG_NODE          ONS,
           ORG_NODE          ONG,
           OAS.ORG_NODE_ANCESTOR ONA,
           OAS.ORG_NODE_CATEGORY ONC

     WHERE TR.TEST_ROSTER_ID = P_TEST_ROSTER_ID
       AND ONS.ORG_NODE_ID = TR.ORG_NODE_ID
       AND ONA.ORG_NODE_ID = TR.ORG_NODE_ID
       AND ONG.ORG_NODE_ID = ONA.ANCESTOR_ORG_NODE_ID
       AND ONC.ORG_NODE_CATEGORY_ID = ONG.ORG_NODE_CATEGORY_ID
       AND ONC.CATEGORY_LEVEL =
           (SELECT VALUE
              FROM STG_EXPORT_PARMS
             WHERE UPPER(PARAMETER) = 'CATEGORY_LEVEL'
               AND CUSTOMER_ID = V_CUST_ID);

  --cursor rowtype declaration
  CURSOR_DIST_ORGID_REC CUR_DISTRICT_ORG_ID%ROWTYPE;

  --cursor for stg_export_parms table
  CURSOR CUR_EXP_OPUNIT IS
    SELECT VALUE
      FROM STG_EXPORT_PARMS
     WHERE UPPER(PARAMETER) = 'START_OPUNIT'
       AND CUSTOMER_ID = V_CUST_ID;

  --cursor rowtype declaration
  CURSOR_OPUNIT_REC CUR_EXP_OPUNIT%ROWTYPE;

  --cursor for stg_export_parms table TO get opunit range
  CURSOR CUR_EXP_LAST_OPUNIT IS
    SELECT VALUE
      FROM STG_EXPORT_PARMS
     WHERE UPPER(PARAMETER) = 'LAST_OPUNIT'
       AND CUSTOMER_ID = V_CUST_ID;

  --cursor rowtype declaration
  CURSOR_LAST_OPUNIT_REC CUR_EXP_LAST_OPUNIT%ROWTYPE;

  --CURSOR FOR ROSTER_COUNT

  CURSOR CUR_EXP_ROSTER_COUNT IS
    SELECT VALUE
      FROM STG_EXPORT_PARMS
     WHERE UPPER(PARAMETER) = 'ROSTER_COUNT'
       AND CUSTOMER_ID = V_CUST_ID;
  --cursor rowtype declaration
  CUR_EXP_ROSTER_COUNT_REC CUR_EXP_ROSTER_COUNT%ROWTYPE;

  --cursor for stg_export_parms table
  CURSOR CUR_EXP_CALIBRATION IS
    SELECT VALUE
      FROM STG_EXPORT_PARMS
     WHERE UPPER(PARAMETER) = 'CALIBRATION'
       AND CUSTOMER_ID = V_CUST_ID;

  --cursor rowtype declaration
  CURSOR_CALIBRATION_REC CUR_EXP_CALIBRATION%ROWTYPE;

BEGIN

  ---open the cursor of getting customer id
  FLAG := FALSE;
  OPEN CUR_CUST_ID;
  FETCH CUR_CUST_ID
    INTO V_CUST_ID;

  IF CUR_CUST_ID%NOTFOUND THEN
    RAISE_APPLICATION_ERROR(-20001,
                            'The specified customer_id does not exist');
  END IF;
  CLOSE CUR_CUST_ID;

  OPEN CUR_EXP_OPUNIT;
  FETCH CUR_EXP_OPUNIT
    INTO CURSOR_OPUNIT_REC;

  IF CUR_EXP_OPUNIT%NOTFOUND THEN
    RAISE_APPLICATION_ERROR(-20001,
                            'start_opunit value not set' || V_CUST_ID);
  END IF;
  CLOSE CUR_EXP_OPUNIT;

  OPEN CUR_EXP_LAST_OPUNIT;
  FETCH CUR_EXP_LAST_OPUNIT
    INTO CURSOR_LAST_OPUNIT_REC;

  IF CUR_EXP_LAST_OPUNIT%NOTFOUND THEN
    RAISE_APPLICATION_ERROR(-20001,
                            'last_opunit value not set' || V_CUST_ID);
  END IF;
  CLOSE CUR_EXP_LAST_OPUNIT;

  OPEN CUR_EXP_ROSTER_COUNT;
  FETCH CUR_EXP_ROSTER_COUNT
    INTO CUR_EXP_ROSTER_COUNT_REC;

  IF CUR_EXP_ROSTER_COUNT%NOTFOUND THEN
    RAISE_APPLICATION_ERROR(-20001,
                            'roster_count value not set' || V_CUST_ID);
  END IF;
  CLOSE CUR_EXP_ROSTER_COUNT;

  -- Assign the calibration value to a variable
  OPEN CUR_EXP_CALIBRATION;
  FETCH CUR_EXP_CALIBRATION
    INTO CURSOR_CALIBRATION_REC;

  IF CUR_EXP_CALIBRATION%NOTFOUND THEN
    V_CALIBRATION_FLAG := 'N';
  END IF;
  IF CUR_EXP_CALIBRATION%FOUND THEN
    V_CALIBRATION_FLAG := CURSOR_CALIBRATION_REC.VALUE;
  END IF;
  CLOSE CUR_EXP_CALIBRATION;

  FOR CUR_TESTROSTER1 IN CUR_TESTROSTER LOOP

    V_TEST_ROSTER_ID := CUR_TESTROSTER1.TEST_ROSTER_ID;

    /* CHANGE FOR No split of opunit across same class levels */
    -- First time assign the value of class_id into v_teacher_id variable
    IF (V_TEACHER_ID IS NULL) THEN
      V_TEACHER_ID := CUR_TESTROSTER1.CLASS_ID;
    ELSE

      --In 2nd Iteration of the loop compare with the v_teacher_id variable.If comparison results true,then flag value is true
      --If compariosn results false, then flag value is false and assign the class_id value in variable v_teacher_id
      IF (V_TEACHER_ID = CUR_TESTROSTER1.CLASS_ID) THEN
        FLAG := TRUE;
      ELSE
        FLAG         := FALSE;
        V_TEACHER_ID := CUR_TESTROSTER1.CLASS_ID;
      END IF;
    END IF;

    OPEN CUR_DISTRICT_ORG_ID(V_TEST_ROSTER_ID);
    FETCH CUR_DISTRICT_ORG_ID
      INTO CURSOR_DIST_ORGID_REC;

    IF CUR_DISTRICT_ORG_ID%NOTFOUND THEN
      RAISE_APPLICATION_ERROR(-20001,
                              'No organistaion_id exists for the test_roster_id ' ||
                              V_TEST_ROSTER_ID);
    END IF;

    CLOSE CUR_DISTRICT_ORG_ID;

    /*if cur_get_orgNode_id%No*/

    OPEN CUR_DISTRICT_OPUNIT(CURSOR_DIST_ORGID_REC.ORG_NODE_ID,
                             V_CALIBRATION_FLAG);
    FETCH CUR_DISTRICT_OPUNIT
      INTO CURSOR_DOP_REC; --v_STG_DISTRICT_OPUNIT_ID,v_dist_id ,v_roster_count,  v_opunit;

    IF CUR_DISTRICT_OPUNIT%NOTFOUND THEN
      --this part is executed if dist_id is not present in stg_dist_opunit table
      V_ROSTER_COUNT := 1;
      --      dbms_output.put_line ('inside cur_dist_opunit%notfound loop');
      V_CURRENT_OPUNIT := 'Y';
      
      INSERT INTO STG_DISTRICT_OPUNIT
      VALUES
        (SEQ_STG_DISTRICT_OPUNIT_ID.NEXTVAL,
         V_CUST_ID,
         CURSOR_DIST_ORGID_REC.ORG_NODE_ID,
         CURSOR_OPUNIT_REC.VALUE,
         V_ROSTER_COUNT,
         V_CALIBRATION_FLAG,
         V_CURRENT_OPUNIT);

      UPDATE STG_TEST_ROSTER
         SET OPUNIT = CURSOR_OPUNIT_REC.VALUE
       WHERE TEST_ROSTER_ID = CUR_TESTROSTER1.TEST_ROSTER_ID;

      UPDATE TEST_ROSTER
         SET OPUNIT = CURSOR_OPUNIT_REC.VALUE
       WHERE TEST_ROSTER_ID = CUR_TESTROSTER1.TEST_ROSTER_ID;

    ELSE

      IF (CURSOR_DOP_REC.CURRENT_OPUNIT = 'Y') THEN
      
          IF (CURSOR_DOP_REC.ROSTER_COUNT < CUR_EXP_ROSTER_COUNT_REC.VALUE) THEN
            --this part is executed if roster_count < 2999
    
            V_ROSTER_COUNT := CURSOR_DOP_REC.ROSTER_COUNT + 1;
    
            UPDATE STG_DISTRICT_OPUNIT
               SET ROSTER_COUNT = V_ROSTER_COUNT
             WHERE STG_DISTRICT_OPUNIT_ID =
                   CURSOR_DOP_REC.STG_DISTRICT_OPUNIT_ID;
    
            UPDATE STG_TEST_ROSTER
               SET OPUNIT = CURSOR_DOP_REC.OPUNIT
             WHERE TEST_ROSTER_ID = CUR_TESTROSTER1.TEST_ROSTER_ID;
    
            UPDATE TEST_ROSTER
               SET OPUNIT = CURSOR_DOP_REC.OPUNIT
             WHERE TEST_ROSTER_ID = CUR_TESTROSTER1.TEST_ROSTER_ID;
    
          ELSE
            --this part is executed if roster_count > 3000
    
            IF (FLAG) THEN
              CURSOR_OPUNIT_REC.VALUE := CURSOR_DOP_REC.OPUNIT;
              V_OPUNIT                := CURSOR_DOP_REC.OPUNIT;
              V_ROSTER_COUNT          := CURSOR_DOP_REC.ROSTER_COUNT + 1;
            ELSE
              --If flag value  is false , then the opunit value will be incremented to 1  and a new record will be inserted into stg_district_opunit table
              CURSOR_OPUNIT_REC.VALUE := CURSOR_DOP_REC.OPUNIT + 1;
              V_OPUNIT                := CURSOR_DOP_REC.OPUNIT + 1;
            END IF;
            --cursor_opunit_rec.value := cursor_dop_rec.opunit  +1;
            --v_opunit :=cursor_dop_rec.opunit  +1;
            --v_roster_count:=1;
            V_DIST_ID := CURSOR_DOP_REC.DISTRICT_ORG_ID;
    
            IF (FLAG) THEN
    
              UPDATE STG_TEST_ROSTER
                 SET OPUNIT = V_OPUNIT
               WHERE TEST_ROSTER_ID = CUR_TESTROSTER1.TEST_ROSTER_ID;
    
              UPDATE TEST_ROSTER
                 SET OPUNIT = V_OPUNIT
               WHERE TEST_ROSTER_ID = CUR_TESTROSTER1.TEST_ROSTER_ID;
    
              UPDATE STG_DISTRICT_OPUNIT
                 SET ROSTER_COUNT = V_ROSTER_COUNT
               WHERE STG_DISTRICT_OPUNIT_ID =
                     CURSOR_DOP_REC.STG_DISTRICT_OPUNIT_ID;
    
            END IF;
            --Check opunit should not exceed the range
            IF (V_OPUNIT <= CURSOR_LAST_OPUNIT_REC.VALUE) THEN
              DBMS_OUTPUT.PUT_LINE('v_opunit' || V_OPUNIT);
    
              IF (FLAG = FALSE) THEN
                DBMS_OUTPUT.PUT_LINE('v_opunit' || V_OPUNIT);
                V_ROSTER_COUNT := 1;
                V_CURRENT_OPUNIT := 'Y';
                
                --Update the existing current opunit record before creating a new one 
                UPDATE STG_DISTRICT_OPUNIT
                 SET CURRENT_OPUNIT = 'N'
                 WHERE STG_DISTRICT_OPUNIT_ID = CURSOR_DOP_REC.STG_DISTRICT_OPUNIT_ID
                AND CURRENT_OPUNIT ='Y';
                     
                INSERT INTO STG_DISTRICT_OPUNIT
                VALUES
                  (SEQ_STG_DISTRICT_OPUNIT_ID.NEXTVAL,
                   V_CUST_ID,
                   V_DIST_ID,
                   CURSOR_OPUNIT_REC.VALUE,
                   V_ROSTER_COUNT,
                   V_CALIBRATION_FLAG,
                   V_CURRENT_OPUNIT);
    
                UPDATE STG_TEST_ROSTER
                   SET OPUNIT = V_OPUNIT
                 WHERE TEST_ROSTER_ID = CUR_TESTROSTER1.TEST_ROSTER_ID;
    
                UPDATE TEST_ROSTER
                   SET OPUNIT = V_OPUNIT
                 WHERE TEST_ROSTER_ID = CUR_TESTROSTER1.TEST_ROSTER_ID;
    
              END IF;
            ELSE
    
              RAISE_APPLICATION_ERROR(-20001,
                                      'Invalid opunit range encountered - ' ||
                                      SQLCODE || ' -ERROR- ' || SQLERRM);
    
            END IF;
    
          END IF;
       
       ELSE  -- when CURSOR_DOP_REC.CURRENT_OPUNIT != 'Y'
             -- Create a new opunit
             
          V_ROSTER_COUNT   := 1;
          V_CURRENT_OPUNIT := 'Y';
          V_OPUNIT         := CURSOR_DOP_REC.OPUNIT + 1;
          
          INSERT INTO STG_DISTRICT_OPUNIT
          VALUES
            (SEQ_STG_DISTRICT_OPUNIT_ID.NEXTVAL,
             V_CUST_ID,
             CURSOR_DIST_ORGID_REC.ORG_NODE_ID,
             V_OPUNIT,
             V_ROSTER_COUNT,
             V_CALIBRATION_FLAG,
             V_CURRENT_OPUNIT);
    
          UPDATE STG_TEST_ROSTER
             SET OPUNIT = V_OPUNIT
           WHERE TEST_ROSTER_ID = CUR_TESTROSTER1.TEST_ROSTER_ID;
    
          UPDATE TEST_ROSTER
             SET OPUNIT = V_OPUNIT
           WHERE TEST_ROSTER_ID = CUR_TESTROSTER1.TEST_ROSTER_ID;   
       
       END IF; --end CURSOR_DOP_REC.CURRENT_OPUNIT = 'Y'
       
    END IF;

    --closing the cursors

    CLOSE CUR_DISTRICT_OPUNIT;

    COMMIT;

  --closing outer cursor
  END LOOP;
  
  --set all current opunit to 'N' so that the next run of the procedure creates new opunits
  UPDATE STG_DISTRICT_OPUNIT 
  SET CURRENT_OPUNIT = 'N'
  WHERE CURRENT_OPUNIT = 'Y';
  
  COMMIT;
  

EXCEPTION
  WHEN OTHERS THEN

    IF CUR_DISTRICT_OPUNIT%ISOPEN THEN
      CLOSE CUR_DISTRICT_OPUNIT;
    END IF;
    IF CUR_DISTRICT_ORG_ID%ISOPEN THEN
      CLOSE CUR_DISTRICT_ORG_ID;
    END IF;
    IF CUR_EXP_OPUNIT%ISOPEN THEN
      CLOSE CUR_EXP_OPUNIT;
    END IF;
    IF CUR_TESTROSTER%ISOPEN THEN
      CLOSE CUR_TESTROSTER;
    END IF;
    IF CUR_CUST_ID%ISOPEN THEN
      CLOSE CUR_CUST_ID;
    END IF;

    RAISE_APPLICATION_ERROR(-20001,
                            'An error was encountered - ' || SQLCODE ||
                            ' -ERROR- ' || SQLERRM);

END SP_OPUNIT_ROSTERCOUNT_GA;
/
