CREATE OR REPLACE PROCEDURE SP_UDF_OAS_EXPORT_ISTEP IS

  /*DESCRIPTION: This stored procedure is used to Update the UDF field in the STYUDENT table with data from the STUDENT
  _RESEARCH_DATA table. */

  /*AUTHOR: Wipro Offshore Team*/
  /*CREATED DATE: 06th March 2006*/
  /*MODIFIED by: Puru Naidu for FCAT on 07 July 2007*/

  /* TO GET THE PATH FOR FILE */
  V_UDF                    VARCHAR2(256) := '';
  V_UDF_REC_NO             INTEGER := 0;
  V_UDF_LAST_REC_NO        INTEGER := 0;
  V_STUD_COL_NO            INTEGER := 0;
  V_SA_COL_NO              INTEGER := 0;
  V_SRD_ROW_NO             INTEGER := 0;
  V_CUST_ID                INTEGER := 0;
  V_UDF_LENGTH             INTEGER := 0;
  V_STUD_SQL_STR           VARCHAR2(2000);
  V_STUD_SQL_VAL           VARCHAR2(2000);
  V_SA_SQL_STR             VARCHAR2(2000);
  V_SA_SQL_VAL             VARCHAR2(2000);
  V_SQL_STR                VARCHAR2(2000);
  V_UDF_CURR_VAL           VARCHAR2(100);
  V_UDF_CURR_VAL_INVALIDATION VARCHAR2(100);
  V_ORG_NODE_CATEGORY_NAME VARCHAR2(20);
  CUSTOMER_SHORT_NAME      VARCHAR(25); -- added 10/02/2007
  V_END_POS1 INTEGER;

  TYPE UDF_REC IS RECORD(
    OAS_TABLE_NAME   VARCHAR2(50),
    OAS_COLUMN_NAME  VARCHAR2(65),
    SUBJECT          VARCHAR2(32),
    FIELD_LENGTH     INTEGER,
    START_POSITION   INTEGER,
    FILLER_CHARACTER VARCHAR2(10),
    JUSTIFY          VARCHAR2(1),
    DECODE_STRING    VARCHAR2(200),
    COL_POS_IN_TABLE INTEGER);

  TYPE UDF_TAB IS TABLE OF UDF_REC INDEX BY BINARY_INTEGER;

  V_UDF_TABLE UDF_TAB;

  TYPE ARRAY IS TABLE OF VARCHAR2(20);

  V_ARR_SRD_UDF_VAL ARRAY;
  --v_arr_srd_udf_val_pre      ARRAY;

  -- for STUDENT_RESEARCH_DATA_Check_box --
  TYPE VTAB IS TABLE OF VARCHAR2(20) INDEX BY BINARY_INTEGER;

  V_SRD_TAB VTAB;
  --v_pre_srd_tab              vtab;
  --v_stud_pre_rec             stg_student_bio_tb%ROWTYPE;

  /****************TO GET DATA FOR OAS_COLUMN_NAME,LENGTH,START_POSITION FROM UDF TABLE **********************/

  CURSOR C_UDF(V_CUSTOMER_ID NUMBER) IS
    SELECT OAS_TABLE_NAME,
           OAS_COLUMN_NAME,
           TRIM(UPPER(SUBJECT)) SUBJECT,
           LENGTH,
           START_POSITION,
           FILLER_CHARACTER,
           DECODE_STRING,
           JUSTIFY
      FROM UDF_MAPPING
     WHERE CUSTOMER_ID = V_CUSTOMER_ID
     ORDER BY OAS_TABLE_NAME, OAS_COLUMN_NAME, START_POSITION;

  /****************IDENTIFICATION OF STUDENTS IN TEST_ROSTER TABLE PER PARTICUALR CUSTOMER_ID ****************/

  CURSOR CUR_TR IS
    SELECT TR.STUDENT_ID,
           TR.TEST_ROSTER_ID,
           TR.ORG_NODE_ID,
           TR.TEST_ADMIN_ID,
           TR.VALIDATION_STATUS,
           TA.LOCATION,
           TRIM(UPPER(TC.SUBJECT)) SUBJECT,
           TR.FORM_ASSIGNMENT,
           ST.PRECODE_ID
      FROM STUDENT             ST,
           TEST_ROSTER         TR,
           TEST_ADMIN          TA,
           TEST_CATALOG        TC,
           STG_TEST_ROSTER     STR,
           STG_PARAM_WINSCR_TB STG
     WHERE ST.STUDENT_ID = TR.STUDENT_ID
       AND TR.TEST_ADMIN_ID = TA.TEST_ADMIN_ID
       AND TA.TEST_CATALOG_ID = TC.TEST_CATALOG_ID
       AND TR.CUSTOMER_ID = STG.CUSTOMER_ID
       AND TR.CUSTOMER_ID = TA.CUSTOMER_ID
       AND TA.PRODUCT_ID = TC.PRODUCT_ID
       AND TA.PRODUCT_ID = STG.PRODUCT_ID
       AND STR.TEST_ROSTER_ID = TR.TEST_ROSTER_ID;

  /****************/

  FUNCTION TEXT_BTW_PAT(TEXT VARCHAR2, PAT VARCHAR2, POS INTEGER)
    RETURN VARCHAR2 IS
    V_START_POS INTEGER;
    V_END_POS   INTEGER;
  BEGIN
    V_START_POS := INSTR(TEXT, PAT, 1, POS) + 1;
    V_END_POS   := INSTR(TEXT, PAT, 1, POS + 1);
    RETURN TRIM(SUBSTR(TEXT, V_START_POS, V_END_POS - V_START_POS));
  END TEXT_BTW_PAT;

  /****************/

  PROCEDURE DECODE_THRU_DUAL(P_DECODE_STRING    VARCHAR2,
                             P_REPLACE_COL_NAME VARCHAR2)
  --      RETURN VARCHAR2
   IS
  BEGIN
    V_SQL_STR := V_UDF;

    V_SQL_STR := 'select ' ||
                 REPLACE(P_DECODE_STRING,
                         P_REPLACE_COL_NAME,
                         '''' || V_UDF_CURR_VAL || '''') || ' from dual';
                         
                       

    EXECUTE IMMEDIATE V_SQL_STR
      INTO V_UDF_CURR_VAL;
  END DECODE_THRU_DUAL;

  /****************/

BEGIN
  --TO GET CUSTOMER_ID FROM STAGING TABLE

  SELECT CUSTOMER_ID INTO V_CUST_ID FROM STG_PARAM_WINSCR_TB WHERE SNO = 1;

  -- Read the UDF_MAPPING table and set up environment for process --

  /****************/

  FOR UDF_REC IN C_UDF(V_CUST_ID) LOOP
    V_UDF_REC_NO := V_UDF_REC_NO + 1;
    V_UDF_TABLE(V_UDF_REC_NO).OAS_TABLE_NAME := UDF_REC.OAS_TABLE_NAME;
    V_UDF_TABLE(V_UDF_REC_NO).OAS_COLUMN_NAME := UDF_REC.OAS_COLUMN_NAME;
    V_UDF_TABLE(V_UDF_REC_NO).SUBJECT := UDF_REC.SUBJECT;
    V_UDF_TABLE(V_UDF_REC_NO).FIELD_LENGTH := UDF_REC.LENGTH;
    V_UDF_TABLE(V_UDF_REC_NO).START_POSITION := UDF_REC.START_POSITION;
    V_UDF_TABLE(V_UDF_REC_NO).DECODE_STRING := UDF_REC.DECODE_STRING;
    V_UDF_TABLE(V_UDF_REC_NO).JUSTIFY := UDF_REC.JUSTIFY;

    SELECT DECODE(UDF_REC.FILLER_CHARACTER,
                  'ZERO',
                  '0',
                  'BLANK',
                  ' ',
                  NULL,
                  ' ',
                  UDF_REC.FILLER_CHARACTER)
      INTO V_UDF_TABLE(V_UDF_REC_NO) .FILLER_CHARACTER
      FROM DUAL;

    IF V_UDF_TABLE(V_UDF_REC_NO).START_POSITION = 0 THEN
      --added 09-02-2007 to capture customer short name

      CUSTOMER_SHORT_NAME := V_UDF_TABLE(V_UDF_REC_NO).OAS_TABLE_NAME;

    END IF; --END OF capture customer short name

    IF UPPER(UDF_REC.OAS_TABLE_NAME) = 'STUDENT' THEN
      V_STUD_COL_NO := V_STUD_COL_NO + 1;
      V_UDF_TABLE(V_UDF_REC_NO).COL_POS_IN_TABLE := V_STUD_COL_NO;
      V_STUD_SQL_STR := V_STUD_SQL_STR || ' || ''~'' || ' ||
                        NVL(UDF_REC.DECODE_STRING,
                            UPPER(UDF_REC.OAS_COLUMN_NAME));
      -- Keep it for FCAT --
    ELSIF UPPER(V_UDF_TABLE(V_UDF_REC_NO).OAS_TABLE_NAME) LIKE
          'STUDENT_ACCOMMODATION%' THEN
      V_SA_COL_NO := V_SA_COL_NO + 1;
      V_UDF_TABLE(V_UDF_REC_NO).COL_POS_IN_TABLE := V_SA_COL_NO;
      V_SA_SQL_STR := V_SA_SQL_STR || ' || ''~'' || ' ||
                      NVL(UDF_REC.DECODE_STRING,
                          UPPER(UDF_REC.OAS_COLUMN_NAME));
      --
    ELSIF UPPER(V_UDF_TABLE(V_UDF_REC_NO).OAS_TABLE_NAME) =
          'STUDENT_DEMOGRAPHIC_DATA' THEN
      V_SRD_ROW_NO := V_SRD_ROW_NO + 1;
      V_UDF_TABLE(V_UDF_REC_NO).COL_POS_IN_TABLE := V_SRD_ROW_NO;
    END IF;

    -- To get the max udf length --
    IF V_UDF_LENGTH < V_UDF_TABLE(V_UDF_REC_NO)
    .START_POSITION + V_UDF_TABLE(V_UDF_REC_NO).FIELD_LENGTH THEN
      V_UDF_LENGTH := V_UDF_TABLE(V_UDF_REC_NO)
                     .START_POSITION + V_UDF_TABLE(V_UDF_REC_NO)
                     .FIELD_LENGTH;
    END IF;

  END LOOP;

  /****************/
  --dbms_output.put_line(v_stud_sql_str);

  -- Prepare select sql script for STUDENT --

  IF V_STUD_SQL_STR IS NOT NULL THEN

    V_STUD_SQL_STR := 'select ' || SUBSTR(V_STUD_SQL_STR, 5) ||
                      ' || ''~'' from student where student_id = :student_id';

  END IF;

  -- Prepare select sql script for STUDENT_ACCOMMODATION --

  IF V_SA_SQL_STR IS NOT NULL THEN

    V_SA_SQL_STR := 'select ' || SUBSTR(V_SA_SQL_STR, 5) ||
                    ' || ''~'' from student_accommodation where student_id = :student_id';

  END IF;

  V_UDF_LAST_REC_NO := V_UDF_REC_NO;

  -- Process Test Roster records --
  FOR CUR_TR_REC IN CUR_TR LOOP

    V_UDF := LPAD(' ', V_UDF_LENGTH - 1);

    -- Get demo check box into array --
    V_SRD_TAB.DELETE;

    IF NVL(V_SRD_ROW_NO, 0) > 0 THEN

      /********** Get udf_values in an array by STUDENT_DEMOGRAPHIC_DATA.column_name order
      Used 2 sqls to get outer join with more than one table ******/
      SELECT MAX(B.VALUE_CODE) VALUE_CODE BULK COLLECT
        INTO V_ARR_SRD_UDF_VAL
     FROM (SELECT CD.CUSTOMER_DEMOGRAPHIC_ID,
                     UM.OAS_COLUMN_NAME,
                     CDV.VALUE_CODE,
                     CD.VALUE_CARDINALITY,
                     UM.MULTIPLE_DEMO_VALUE_CODE,
                     UM.START_POSITION
                FROM UDF_MAPPING                UM,
                     CUSTOMER_DEMOGRAPHIC       CD,
                     CUSTOMER_DEMOGRAPHIC_VALUE CDV
               WHERE UM.OAS_TABLE_NAME = 'STUDENT_DEMOGRAPHIC_DATA'
                 AND UM.CUSTOMER_ID = V_CUST_ID
                 AND UM.OAS_COLUMN_NAME = CD.LABEL_NAME
                 AND CD.CUSTOMER_ID = UM.CUSTOMER_ID
                 AND CD.CUSTOMER_DEMOGRAPHIC_ID =
                     CDV.CUSTOMER_DEMOGRAPHIC_ID
                 AND DECODE(CD.VALUE_CARDINALITY,
                            'SINGLE',
                            CDV.VALUE_CODE,
                            'MULTIPLE',
                            UM.MULTIPLE_DEMO_VALUE_CODE) = CDV.VALUE_CODE) A,
             (SELECT CDV.VALUE_CODE, SDD.CUSTOMER_DEMOGRAPHIC_ID
                FROM STUDENT_DEMOGRAPHIC_DATA   SDD,
                     CUSTOMER_DEMOGRAPHIC_VALUE CDV
               WHERE SDD.STUDENT_ID = CUR_TR_REC.STUDENT_ID
                 AND SDD.CUSTOMER_DEMOGRAPHIC_ID =
                     CDV.CUSTOMER_DEMOGRAPHIC_ID
                 AND SDD.VALUE_NAME = CDV.VALUE_NAME) B
       WHERE A.CUSTOMER_DEMOGRAPHIC_ID = B.CUSTOMER_DEMOGRAPHIC_ID(+)
         AND A.VALUE_CODE = B.VALUE_CODE(+)
       GROUP BY A.OAS_COLUMN_NAME,
                DECODE(A.VALUE_CARDINALITY, 'SINGLE', NULL, A.VALUE_CODE),
                A.START_POSITION
       ORDER BY A.OAS_COLUMN_NAME, A.START_POSITION;

    END IF;
    /*******************************************/
    IF V_STUD_SQL_STR IS NOT NULL THEN
      --dbms_output.put_line(v_stud_sql_str);
      EXECUTE IMMEDIATE V_STUD_SQL_STR
        INTO V_STUD_SQL_VAL
        USING CUR_TR_REC.STUDENT_ID;

    END IF;
    /**********************************/
    BEGIN

      IF V_SA_SQL_STR IS NOT NULL THEN
        EXECUTE IMMEDIATE V_SA_SQL_STR
          INTO V_SA_SQL_VAL
          USING CUR_TR_REC.STUDENT_ID;
      END IF;

    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        V_SA_SQL_VAL := NULL;
    END;
    /************************************************/

    -- OPENS CUR_UDF CURSOR FOR OAS_COLUMN_NAME VALUE

    FOR V_UDF_REC_NO IN 1 .. V_UDF_LAST_REC_NO LOOP
      V_UDF_CURR_VAL := NULL;
      V_ARR_SRD_UDF_VAL.extend;
      
      /************************START FIRST IF *************/

      -- STUDENT TABLE --
      IF UPPER(V_UDF_TABLE(V_UDF_REC_NO).OAS_TABLE_NAME) = 'STUDENT' THEN
        V_UDF_CURR_VAL := TEXT_BTW_PAT(V_STUD_SQL_VAL,
                                       '~',
                                       V_UDF_TABLE(V_UDF_REC_NO)
                                       .COL_POS_IN_TABLE);
        --

        -- STUDENT_DEMOGRAPHIC_DATA --
      ELSIF UPPER(V_UDF_TABLE(V_UDF_REC_NO).OAS_TABLE_NAME) =
            'STUDENT_DEMOGRAPHIC_DATA' THEN
           -- dbms_output.put_line('col name');
            --dbms_output.put_line((V_UDF_TABLE(V_UDF_REC_NO).OAS_COLUMN_NAME));
                                 IF upper(V_UDF_TABLE(V_UDF_REC_NO).OAS_COLUMN_NAME) =
            'PRE-ID ETHNICITY' THEN
                          IF(CUR_TR_REC.PRECODE_ID)is not null then
            
                                V_UDF_CURR_VAL := V_ARR_SRD_UDF_VAL(V_UDF_TABLE(V_UDF_REC_NO)
                                            .COL_POS_IN_TABLE);

                              IF V_UDF_TABLE(V_UDF_REC_NO).DECODE_STRING IS NOT NULL THEN
          -- v_sql_str :=
                                        DECODE_THRU_DUAL(V_UDF_TABLE(V_UDF_REC_NO).DECODE_STRING,
                                           'VALUE_CODE');
                           
                                end if;
            
                             end if;
            
                     
            else

        V_UDF_CURR_VAL := V_ARR_SRD_UDF_VAL(V_UDF_TABLE(V_UDF_REC_NO)
                                            .COL_POS_IN_TABLE);

        IF V_UDF_TABLE(V_UDF_REC_NO).DECODE_STRING IS NOT NULL THEN
          -- v_sql_str :=
          DECODE_THRU_DUAL(V_UDF_TABLE(V_UDF_REC_NO).DECODE_STRING,
                           'VALUE_CODE');
                           
                           
                           
                          /* ELSIF UPPER(V_UDF_TABLE(V_UDF_REC_NO).OAS_TABLE_NAME) = 'STUDENT_DEMOGRAPHIC_DATA' AND
            UPPER(V_UDF_TABLE(V_UDF_REC_NO).OAS_COLUMN_NAME) =
            'PRE-ID ETHNICITY' THEN
            IF(CUR_TR_REC.PRECODE_ID)is not null then
            V_UDF_CURR_VAL := */

        END IF;
        
        END IF;

        --- NEW CODE ADDED ON 05/16/2007 TO CATCH THE STUDENT SUBJECT
      ELSIF UPPER(V_UDF_TABLE(V_UDF_REC_NO).OAS_TABLE_NAME) =
            'ORG_NODE_STUDENT' THEN

        BEGIN

          SELECT DECODE(COUNT(*),
                        1,
                        SUBSTR(MAX(ORN.ORG_NODE_NAME), 1, 1),
                        'B') SUBJECT_TAKEN
            INTO V_UDF_CURR_VAL
            FROM ORG_NODE_STUDENT ONS, ORG_NODE ORN
           WHERE ORN.CUSTOMER_ID = V_CUST_ID
             AND ONS.CUSTOMER_ID = ORN.CUSTOMER_ID
             AND ORN.ORG_NODE_ID = ONS.ORG_NODE_ID
             AND ONS.STUDENT_ID = CUR_TR_REC.STUDENT_ID;

        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            V_UDF_CURR_VAL := NULL;
        END;
        ---   END OF NEW CODE ADDED ON 05/16/2007 TO CATCH THE STUDENT SUBJECT

        --- NEW CODE ADDED ON 09/24/2007 TO CATCH THE ORG_NODE_CLASS FOR GEORGIA SPECIFIC

      ELSIF UPPER(V_UDF_TABLE(V_UDF_REC_NO).OAS_TABLE_NAME) =
            'ORG_NODE_CLASS' THEN

        V_ORG_NODE_CATEGORY_NAME := REPLACE(UPPER(V_UDF_TABLE(V_UDF_REC_NO)
                                                  .OAS_TABLE_NAME),
                                            'ORG_NODE_');

        BEGIN
          SELECT SUBSTR(ORG_NODE_NAME, 1, 24)
            INTO V_UDF_CURR_VAL
            FROM (SELECT N.ORG_NODE_NAME,
                         UPPER(NC.CATEGORY_NAME) CATEGORY_NAME
                    FROM ORG_NODE          N,
                         ORG_NODE_PARENT   NP,
                         ORG_NODE_CATEGORY NC
                   WHERE N.ORG_NODE_ID = NP.ORG_NODE_ID
                     AND N.ORG_NODE_CATEGORY_ID = NC.ORG_NODE_CATEGORY_ID
                  CONNECT BY PRIOR NP.PARENT_ORG_NODE_ID = NP.ORG_NODE_ID
                   START WITH NP.ORG_NODE_ID = CUR_TR_REC.ORG_NODE_ID)
            WHERE CATEGORY_NAME = V_ORG_NODE_CATEGORY_NAME;

        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            V_UDF_CURR_VAL := NULL;
        END;

        --dbms_output.put_line('ORG_NAME=' || v_udf_curr_val);

        ---   END OF NEW CODE ADDED ON 09/24/2007 TO CATCH THE ORG_NODE_CLASS FOR GEORGIA SPECIFIC

      ELSIF UPPER(V_UDF_TABLE(V_UDF_REC_NO).OAS_TABLE_NAME) LIKE
            'ORG_NODE%' THEN

        V_ORG_NODE_CATEGORY_NAME := REPLACE(UPPER(V_UDF_TABLE(V_UDF_REC_NO)
                                                  .OAS_TABLE_NAME),
                                            'ORG_NODE-');

        BEGIN
          SELECT ORG_NODE_CODE
            INTO V_UDF_CURR_VAL
            FROM (SELECT N.ORG_NODE_CODE,
                         UPPER(NC.CATEGORY_NAME) CATEGORY_NAME
                    FROM ORG_NODE          N,
                         ORG_NODE_PARENT   NP,
                         ORG_NODE_CATEGORY NC
                   WHERE N.ORG_NODE_ID = NP.ORG_NODE_ID
                     AND N.ORG_NODE_CATEGORY_ID = NC.ORG_NODE_CATEGORY_ID
                  CONNECT BY PRIOR NP.PARENT_ORG_NODE_ID = NP.ORG_NODE_ID
                   START WITH NP.ORG_NODE_ID = CUR_TR_REC.ORG_NODE_ID)
           WHERE CATEGORY_NAME = V_ORG_NODE_CATEGORY_NAME;

        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            V_UDF_CURR_VAL := NULL;
        END;

        --dbms_output.put_line('org_node='|| v_udf_curr_val );

        -- CONSTANT VALUES --
      ELSIF UPPER(V_UDF_TABLE(V_UDF_REC_NO).OAS_TABLE_NAME) = 'CONSTANT' THEN
        V_UDF_CURR_VAL := V_UDF_TABLE(V_UDF_REC_NO).DECODE_STRING;
        -- STUDENT_ACCOMMODATION --
      ELSIF UPPER(V_UDF_TABLE(V_UDF_REC_NO).OAS_TABLE_NAME) =
            'STUDENT_ACCOMMODATION' THEN
        V_UDF_CURR_VAL := TEXT_BTW_PAT(V_SA_SQL_VAL,
                                       '~',
                                       V_UDF_TABLE(V_UDF_REC_NO)
                                       .COL_POS_IN_TABLE);
        -- TEST_ADMIN --
      ELSIF UPPER(V_UDF_TABLE(V_UDF_REC_NO).OAS_TABLE_NAME) = 'TEST_ADMIN' AND
            UPPER(V_UDF_TABLE(V_UDF_REC_NO).OAS_COLUMN_NAME) =
            'TEST_ADMIN_ID' THEN
        V_UDF_CURR_VAL := CUR_TR_REC.TEST_ADMIN_ID;

        IF V_UDF_TABLE(V_UDF_REC_NO).DECODE_STRING IS NOT NULL THEN
          DECODE_THRU_DUAL(V_UDF_TABLE(V_UDF_REC_NO).DECODE_STRING,
                           UPPER(V_UDF_TABLE(V_UDF_REC_NO).OAS_COLUMN_NAME));
        END IF;

        -- TEST_ROSTER --
      ELSIF UPPER(V_UDF_TABLE(V_UDF_REC_NO).OAS_TABLE_NAME) = 'TEST_ROSTER' AND
            UPPER(V_UDF_TABLE(V_UDF_REC_NO).OAS_COLUMN_NAME) =
            'VALIDATION_STATUS' THEN

        V_UDF_CURR_VAL := CUR_TR_REC.VALIDATION_STATUS;

        IF V_UDF_TABLE(V_UDF_REC_NO).DECODE_STRING IS NOT NULL THEN
          DECODE_THRU_DUAL(V_UDF_TABLE(V_UDF_REC_NO).DECODE_STRING,
                           UPPER(V_UDF_TABLE(V_UDF_REC_NO).OAS_COLUMN_NAME));
        END IF;
        
        
       /* ----Added for Istep2011
        ELSIF UPPER(V_UDF_TABLE(V_UDF_REC_NO).OAS_TABLE_NAME) = 'STUDENT_DEMOGRAPHIC_DATA' AND
            UPPER(V_UDF_TABLE(V_UDF_REC_NO).OAS_COLUMN_NAME) =
            'PRE-ID ETHNICITY' THEN
            IF(CUR_TR_REC.PRECODE_ID)is not null then
            V_UDF_CURR_VAL := */
        
        
        --Added  for ISTEP
      ELSIF UPPER(V_UDF_TABLE(V_UDF_REC_NO).OAS_TABLE_NAME) =
            'UDF_INVALIDATION' AND
            UPPER(V_UDF_TABLE(V_UDF_REC_NO).OAS_COLUMN_NAME) =
            'INVALIDATION' THEN

        IF CUSTOMER_SHORT_NAME = 'ISTEP' THEN
          V_UDF_CURR_VAL := FN_UDF_VALIDATION_EXPORT_ISTEP(CUR_TR_REC.STUDENT_ID,
          
                                                           CUR_TR_REC.TEST_ROSTER_ID);
          --Modified   for ISTEP  Spring 10                                               
          V_UDF_CURR_VAL_INVALIDATION := V_UDF_CURR_VAL;
          --dbms_output.put_line('V_UDF_CURR_VAL_INVALIDATION ' || V_UDF_CURR_VAL_INVALIDATION);
           
          V_END_POS1   := INSTR(V_UDF_CURR_VAL, '~', 1, 1);
          --dbms_output.put_line('col pso' || V_END_POS1);
         
          V_UDF_CURR_VAL := TRIM(SUBSTR(V_UDF_CURR_VAL, 0, V_END_POS1-1));
          --dbms_output.put_line('V_UDF_CURR_VAL_ ' || V_UDF_CURR_VAL);
        END IF;
        
        -- Added for ISTEP Spring 2010 to have the contents for position 94 to 100
        ELSIF UPPER(V_UDF_TABLE(V_UDF_REC_NO).OAS_TABLE_NAME) =
            'UDF_INVALIDATION_SC_SS' AND
            UPPER(V_UDF_TABLE(V_UDF_REC_NO).OAS_COLUMN_NAME) =
            'UDF_INVALIDATION_SC_SS' THEN
            
        IF CUSTOMER_SHORT_NAME = 'ISTEP' THEN
         /*  V_UDF_CURR_VAL := SUBSTR(V_UDF_CURR_VAL_INVALIDATION,  V_UDF_TABLE(V_UDF_REC_NO)
                                       .COL_POS_IN_TABLE, V_UDF_TABLE(V_UDF_REC_NO)
                                       .COL_POS_IN_TABLE + V_UDF_TABLE(V_UDF_REC_NO).FIELD_LENGTH);*/
                                       --dbms_output.put_line('col pso' || V_END_POS1);
             V_UDF_CURR_VAL := SUBSTR(V_UDF_CURR_VAL_INVALIDATION, V_END_POS1+1 );   
             --dbms_output.put_line('V_UDF_CURR_VAL_INVALIDATION... ' || V_UDF_CURR_VAL);                        
        END IF;
      END IF;
      /************************END FIRST IF *************/

      -- JUSTIFY --
      IF V_UDF_TABLE(V_UDF_REC_NO).JUSTIFY = 'R' THEN
        V_UDF_CURR_VAL := LPAD(NVL(V_UDF_CURR_VAL,
                                   V_UDF_TABLE(V_UDF_REC_NO)
                                   .FILLER_CHARACTER),
                               V_UDF_TABLE(V_UDF_REC_NO).FIELD_LENGTH,
                               V_UDF_TABLE(V_UDF_REC_NO).FILLER_CHARACTER);
      ELSE
        V_UDF_CURR_VAL := RPAD(NVL(V_UDF_CURR_VAL,
                                   V_UDF_TABLE(V_UDF_REC_NO)
                                   .FILLER_CHARACTER),
                               V_UDF_TABLE(V_UDF_REC_NO).FIELD_LENGTH,
                               V_UDF_TABLE(V_UDF_REC_NO).FILLER_CHARACTER);
      END IF;
      -- Check for Subject dependency --
      IF NVL(V_UDF_TABLE(V_UDF_REC_NO).SUBJECT, CUR_TR_REC.SUBJECT) =
         CUR_TR_REC.SUBJECT THEN
        -- Place the  v_udf_curr_val in proper udf position with the given length --
        V_UDF := SUBSTR(V_UDF,
                        1,
                        V_UDF_TABLE(V_UDF_REC_NO).START_POSITION - 1) ||
                 V_UDF_CURR_VAL ||
                 SUBSTR(V_UDF,
                        V_UDF_TABLE(V_UDF_REC_NO)
                        .START_POSITION + V_UDF_TABLE(V_UDF_REC_NO)
                        .FIELD_LENGTH);
      END IF;
      ---Added for ISTEP
      IF CUSTOMER_SHORT_NAME = 'ISTEP' THEN

        -- Place the  v_udf_curr_val in proper udf position with the given length --
        V_UDF := SUBSTR(V_UDF,
                        1,
                        V_UDF_TABLE(V_UDF_REC_NO).START_POSITION - 1) ||
                 V_UDF_CURR_VAL ||
                 SUBSTR(V_UDF,
                        V_UDF_TABLE(V_UDF_REC_NO)
                        .START_POSITION + V_UDF_TABLE(V_UDF_REC_NO)
                        .FIELD_LENGTH);
      END IF;

    END LOOP;

    -- FCAT Specific UDF position 60 has to be hard coded, since it is from   -- two sources.

    --FCAT spring 2008 code change
    IF CUSTOMER_SHORT_NAME = 'FCAT' AND
       TRIM(SUBSTR(V_UDF, 187, 1)) IS NOT NULL THEN
      V_UDF := SUBSTR(V_UDF, 1, 59) || SUBSTR(V_UDF, 187, 1) ||
               SUBSTR(V_UDF, 61);
    END IF;
    --FCAT spring 2008 code change
    IF CUSTOMER_SHORT_NAME = 'FCAT' THEN
      -- FCAT Specific UDF
      V_UDF := SUBSTR(V_UDF, 1, 186);
    END IF;

    --Added  for ISTEP. As for ISTEP the udf field length is 147. from 91 to 147 we have to hard code with space
    IF CUSTOMER_SHORT_NAME = 'ISTEP' THEN
      --CTB Use Only: OAS Tested Indicator Multiple Choice Test
     -- V_UDF := V_UDF || '0';
     V_UDF := SUBSTR(V_UDF, 1, 91) || '0' || ' ' || SUBSTR(V_UDF, 94);
      IF LENGTH(V_UDF) < 256 THEN
        V_UDF := V_UDF || RPAD(' ', 256 - LENGTH(V_UDF), ' ');
      END IF;
      IF LENGTH(V_UDF) > 256 THEN
        V_UDF := SUBSTR(V_UDF, 1, 256);
      END IF;
    END IF;
    dbms_output.put_line(v_udf);
    -- UPDATE TEST_ROSTER --
    UPDATE TEST_ROSTER
       SET UDF = V_UDF
     WHERE TEST_ROSTER_ID = CUR_TR_REC.TEST_ROSTER_ID;

    COMMIT;
  END LOOP;

END SP_UDF_OAS_EXPORT_ISTEP;
/
