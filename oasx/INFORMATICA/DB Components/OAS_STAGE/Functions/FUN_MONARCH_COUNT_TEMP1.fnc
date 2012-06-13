CREATE OR REPLACE FUNCTION FUN_MONARCH_COUNT_TEMP1(IN_ORG_NODE_ID IN TEST_ROSTER.ORG_NODE_ID%TYPE,
                                                     IN_GRADE       IN STG_EISS_TB.GRADE%TYPE,
                                                     IN_OPUNIT      IN STG_EISS_TB.OPUNIT%TYPE,
                                                     V_PARAMETER    IN VARCHAR2,
                                                     IN_TEST_ROSTER_ID IN Test_Roster.Test_Roster_Id%type)

 RETURN NUMBER IS
  V_COUNT      NUMBER;
  CR_COUNT     NUMBER;
  V_STUDENT_ID NUMBER;
  V_ADMIN_ID test_admin.test_admin_id%type;
  
  
  cursor REC_ADMIN_ID IS
  
  select test_admin_id from
  test_roster where test_roster_id = IN_TEST_ROSTER_ID;
  

  /*Cursor for extracting OU records*/
  CURSOR SCHOOL_CASE_COUNT IS
  -- passing parameter school_id and opunit
    SELECT COUNT(SE.ST_ID)
      FROM STG_EISS_TB SE
     WHERE SE.SH_ID = IN_ORG_NODE_ID
       AND SE.LEVEL_TYPE = 'ST'
       AND SE.OPUNIT = IN_OPUNIT
     GROUP BY OPUNIT;

     
      
     
  -------  NOTE : opunit column is added in all the cursors during ISTEP 2008  ----------------------------

  /*Cursor for extracting SH records*/
/*  CURSOR SGL_COUNT IS
    SELECT COUNT(TR.STUDENT_ID)
      FROM TEST_ROSTER  TR,
           TEST_ADMIN   TA,
           TEST_CATALOG TC,
           STUDENT      S
     WHERE TR.ORG_NODE_ID = IN_ORG_NODE_ID
       AND S.GRADE = IN_GRADE
       AND S.STUDENT_ID = TR.STUDENT_ID
       AND TR.TEST_ADMIN_ID = TA.TEST_ADMIN_ID
       AND TA.TEST_CATALOG_ID = TC.TEST_CATALOG_ID
       AND TC.PRODUCT_ID = 6103;*/
       
        CURSOR SGL_COUNT IS
        select count(S.STUDENT_ID) from student S where
    S.STUDENT_ID in (  select TR.STUDENT_ID
   
      FROM TEST_ROSTER  TR
    
          
     WHERE TR.ORG_NODE_ID = IN_ORG_NODE_ID
        AND TR.TEST_ADMIN_ID =  V_ADMIN_ID)
        and S.GRADE = IN_GRADE;
        
        

  CURSOR GIS_COUNT IS

    
     select count(S.STUDENT_ID) from student S where
    S.STUDENT_ID in (  select TR.STUDENT_ID
   
      FROM TEST_ROSTER  TR
    
          
     WHERE TR.ORG_NODE_ID = IN_ORG_NODE_ID
        AND TR.TEST_ADMIN_ID =  V_ADMIN_ID
         AND TR.TEST_COMPLETION_STATUS NOT IN ('SC', 'NT'))
        and S.GRADE = IN_GRADE;
     /* FROM TEST_ROSTER  TR,
           TEST_ADMIN   TA,
           TEST_CATALOG TC,
           STUDENT      S
     WHERE TR.ORG_NODE_ID = IN_ORG_NODE_ID
       AND S.GRADE = IN_GRADE
       AND S.STUDENT_ID = TR.STUDENT_ID
       AND TR.TEST_ADMIN_ID = TA.TEST_ADMIN_ID
       AND TA.TEST_CATALOG_ID = TC.TEST_CATALOG_ID
       AND TC.PRODUCT_ID = 6103
       AND TR.TEST_COMPLETION_STATUS NOT IN ('SC', 'NT');*/

  /* Cursor for opUnitCaseCount */

  CURSOR OPUNITCASE_COUNT IS
    SELECT COUNT(SE.SR_ID) OPUNITCASECOUNT
      FROM STG_EISS_TB SE
     WHERE LEVEL_TYPE = 'ST'
       AND SE.OU_ID = IN_ORG_NODE_ID
       AND SE.OPUNIT = IN_OPUNIT;

  /*  Cursor for getiing student st_id for OpunitMA_Doc_Handscore_Count */
  CURSOR HANDSCORE_COUNT IS
    SELECT SE.ST_ID          ST_ID,
           SE.TEST_ROSTER_ID TEST_ROSTER_ID,
           SR_ID             STUDENT_ID
      FROM STG_EISS_TB SE
     WHERE LEVEL_TYPE = 'ST'
       AND SE.OU_ID = IN_ORG_NODE_ID
       AND SE.OPUNIT = IN_OPUNIT
     ORDER BY SR_ID;
  /*  Cursor for OpunitMA_Doc_Handscore_Count */
  CURSOR OPUNITMA_DOCHANDSCORE_COUNT(IN_ITEM_SET_ID OAS.ITEM_SET_ITEM.ITEM_SET_ID%TYPE) IS
    SELECT COUNT(*) AS HANDSCORE_COUNT
      FROM OAS.ITEM_SET_ANCESTOR ISAN, OAS.ITEM_SET_ITEM IST, OAS.ITEM IT
     WHERE ISAN.ANCESTOR_ITEM_SET_ID = IN_ITEM_SET_ID
       AND ISAN.ITEM_SET_TYPE = 'TD'
       AND ISAN.ITEM_SET_ID = IST.ITEM_SET_ID
       AND IST.ITEM_ID = IT.ITEM_ID
       AND IST.ITEM_ID = IT.ITEM_ID
       AND IT.ITEM_TYPE = 'CR';
  /*SELECT distinct isan.item_set_id, it.item_id, it.item_type

   FROM item_set_ancestor isan, item_set_item ist, item it

  where isan.ancestor_item_set_id = in_item_set_id
        and isan.item_set_type = 'TD'
        and isan.item_set_id = ist.item_set_id
        and ist.item_id = it.item_id
        and ist.item_id = it.item_id
        and it.item_type = 'CR' ;*/
BEGIN
 
  /*
  * No of student test records exported for this school in the particular opUnit
  */

  IF (V_PARAMETER = 'school_case_count') THEN
    OPEN SCHOOL_CASE_COUNT;
    FETCH SCHOOL_CASE_COUNT
      INTO V_COUNT;

    IF SCHOOL_CASE_COUNT%NOTFOUND THEN
      RAISE_APPLICATION_ERROR(-20001,'The count for specified organization does not exist');
    END IF;

    CLOSE SCHOOL_CASE_COUNT;

  END IF;

  /*
  * No of students scheduled under this particular class for this particular Grade
  */

  IF (V_PARAMETER = 'sgl_count') THEN
  
  open REC_ADMIN_ID;
       fetch REC_ADMIN_ID
  into V_ADMIN_ID;
  
   IF REC_ADMIN_ID%NOTFOUND THEN
      RAISE_APPLICATION_ERROR(-20001,'The admin id  does not exist');
    END IF;

  CLOSE REC_ADMIN_ID;
    
    
    OPEN SGL_COUNT;
    FETCH SGL_COUNT
      INTO V_COUNT;

    IF SGL_COUNT%NOTFOUND THEN
      RAISE_APPLICATION_ERROR(-20001,'The count for specified organization does not exist');
    END IF;

    CLOSE SGL_COUNT;

  END IF;

  /*
  * No of students attempted for the test
  */

  IF (V_PARAMETER = 'gis_count') THEN
    OPEN GIS_COUNT;
    FETCH GIS_COUNT
      INTO V_COUNT;

    IF GIS_COUNT%NOTFOUND THEN
      RAISE_APPLICATION_ERROR(-20001,'The count for specified organization does not exist');
    END IF;

    CLOSE GIS_COUNT;

  END IF;

  /*
  * No of students attempted for the test
  */

  IF (V_PARAMETER = 'OpunitCASE_Count') THEN
    OPEN OPUNITCASE_COUNT;
    FETCH OPUNITCASE_COUNT
      INTO V_COUNT;

    IF OPUNITCASE_COUNT%NOTFOUND THEN
      RAISE_APPLICATION_ERROR(-20001,'The count for specified organization does not exist');
    END IF;

    CLOSE OPUNITCASE_COUNT;

  END IF;

  IF (V_PARAMETER = 'Handscore_Count') THEN
    V_COUNT := 0;

    FOR HANDSCORE_COUNT1 IN HANDSCORE_COUNT LOOP
      --As this one used for student count only not the student test records.
      --So it is possible that one student can be scheduled for more than one test.For this null check  for
      --v_student_id has done.

      OPEN OPUNITMA_DOCHANDSCORE_COUNT(HANDSCORE_COUNT1.ST_ID);

      FETCH OPUNITMA_DOCHANDSCORE_COUNT
        INTO CR_COUNT;

      IF (CR_COUNT <> 0) THEN

        IF (V_STUDENT_ID IS NULL) THEN

          V_STUDENT_ID := HANDSCORE_COUNT1.STUDENT_ID;
          V_COUNT      := V_COUNT + 1;

        ELSIF (V_STUDENT_ID <> HANDSCORE_COUNT1.STUDENT_ID) THEN
          V_STUDENT_ID := HANDSCORE_COUNT1.STUDENT_ID;
          V_COUNT      := V_COUNT + 1;
        END IF;

        V_STUDENT_ID := HANDSCORE_COUNT1.STUDENT_ID;
      END IF;

      IF OPUNITMA_DOCHANDSCORE_COUNT%NOTFOUND THEN
        RAISE_APPLICATION_ERROR(-20001,'The count for specified organization does not exist');
      END IF;

      CLOSE OPUNITMA_DOCHANDSCORE_COUNT;

    END LOOP;

  END IF;

  RETURN V_COUNT;
END FUN_MONARCH_COUNT_TEMP1;
/
