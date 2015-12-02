create or replace
PROCEDURE SETUP_LLO_RP_CUSTOMER_CONFIG(IN_CUSTOMER_ID     INTEGER,
                                       ADD_ENGRADE_CONFIG BOOLEAN,
                                       IS_NEW_CUSTOMER BOOLEAN) AS

  V_CHECK_CONFIG         NUMBER := 0;
  V_NEW_PRODUCT_ID       NUMBER := 0;
  V_NEW_PARENT_PRODUCT   NUMBER := 0;
  V_PROGRAM_ID           NUMBER := 0;
  V_CHECK_NEW_PRODUCT    NUMBER := 0;
  V_RESOURCE_ID          NUMBER := 0;
  V_CONFIGCOUNT          NUMBER := 0;
  V_ACTIVATION_STATUS  CONSTANT VARCHAR2(2) := 'AC';
  V_OVERRIDE_NO_RETAKE CONSTANT VARCHAR2(1) := 'F';
  COUNTER            INTEGER := 1;
  CUSTOMER_CONFIG_ID INTEGER := 0;

  CURSOR CUR_TEST_CATALOG_DETAILS(IN_PRODUCT_ID PRODUCT.PRODUCT_ID%TYPE) IS
    SELECT TEST_CATALOG_ID, PRODUCT_ID, ITEM_SET_ID
      FROM TEST_CATALOG
     WHERE PRODUCT_ID = IN_PRODUCT_ID;

  CURSOR CUR_ALL_PRODUCT_ID IS
    SELECT DISTINCT ONTC.PRODUCT_ID, PROD.PARENT_PRODUCT_ID
      FROM ORG_NODE_TEST_CATALOG ONTC, PRODUCT PROD
     WHERE ONTC.CUSTOMER_ID = IN_CUSTOMER_ID
       AND PROD.PRODUCT_ID = ONTC.PRODUCT_ID;

  CURSOR CUR_REPORT_BRIDGE(IN_PRODUCT_ID PRODUCT.PRODUCT_ID%TYPE) IS
    SELECT REPORT_NAME,
           DISPLAY_NAME,
           DESCRIPTION,
           SYSTEM_KEY,
           CUSTOMER_KEY,
           REPORT_URL
      FROM CUSTOMER_REPORT_BRIDGE
     WHERE CUSTOMER_ID = IN_CUSTOMER_ID
       AND PRODUCT_ID = IN_PRODUCT_ID;

BEGIN

  SELECT COUNT(1)
    INTO V_CHECK_CONFIG
    FROM CUSTOMER_CONFIGURATION
   WHERE CUSTOMER_ID = IN_CUSTOMER_ID
     AND CUSTOMER_CONFIGURATION_NAME = 'LASLINK_Customer';

  IF V_CHECK_CONFIG = 1 THEN
  
    update CUSTOMER_CONFIGURATION
       set CUSTOMER_CONFIGURATION_name = 'Allow_Subscription_BAK'
     where CUSTOMER_CONFIGURATION_name = 'Allow_Subscription'
       and customer_id = IN_CUSTOMER_ID;
  
    FOR ALL_PRODUCT_ID IN CUR_ALL_PRODUCT_ID LOOP
      IF ALL_PRODUCT_ID.PARENT_PRODUCT_ID = 7000 THEN
      
        /*START OF 1ST EDITION CHANGES*/
        V_NEW_PRODUCT_ID := ALL_PRODUCT_ID.PRODUCT_ID + 200;
        dbms_output.put_line('V_NEW_PRODUCT_ID -->>' || V_NEW_PRODUCT_ID);
        SELECT PARENT_PRODUCT_ID
          INTO V_NEW_PARENT_PRODUCT
          FROM PRODUCT
         WHERE PRODUCT_ID = V_NEW_PRODUCT_ID;
        dbms_output.put_line('V_NEW_PARENT_PRODUCT -->>' ||
                             V_NEW_PARENT_PRODUCT);
        /* Program start*/
      
        SELECT COUNT(1)
          INTO V_CONFIGCOUNT
          FROM PROGRAM
         WHERE CUSTOMER_ID = IN_CUSTOMER_ID
           AND PRODUCT_ID = V_NEW_PARENT_PRODUCT;
      
        IF V_CONFIGCOUNT = 0 THEN
           SELECT COUNT(1)
             INTO V_CONFIGCOUNT
             FROM PROGRAM PROG
            WHERE PROG.PRODUCT_ID = ALL_PRODUCT_ID.PARENT_PRODUCT_ID
              AND PROG.CUSTOMER_ID = IN_CUSTOMER_ID;
             
           IF V_CONFIGCOUNT > 0 THEN
             
            dbms_output.put_line('program start');
            IF (IS_NEW_CUSTOMER) THEN
               DELETE FROM PROGRAM PROG
               WHERE PROG.PRODUCT_ID = ALL_PRODUCT_ID.PARENT_PRODUCT_ID
                 AND PROG.CUSTOMER_ID = IN_CUSTOMER_ID;
            ELSE
              UPDATE PROGRAM PROG SET PROG.ACTIVATION_STATUS = 'DE', PROG.PROGRAM_END_DATE = add_months(SYSDATE, 120)
               WHERE PROG.PRODUCT_ID = ALL_PRODUCT_ID.PARENT_PRODUCT_ID
                 AND PROG.CUSTOMER_ID = IN_CUSTOMER_ID;             
            END IF;
            
            SELECT CAST(IN_CUSTOMER_ID || '72' AS INT)
              INTO V_PROGRAM_ID
              FROM DUAL;
          
            INSERT INTO PROGRAM
              (CUSTOMER_ID,
               PRODUCT_ID,
               PROGRAM_ID,
               PROGRAM_NAME,
               PROGRAM_START_DATE,
               PROGRAM_END_DATE,
               NORMS_GROUP,
               NORMS_YEAR,
               ACTIVATION_STATUS,
               CREATED_DATE_TIME,
               UPDATED_DATE_TIME)
            VALUES
              (IN_CUSTOMER_ID,
               V_NEW_PARENT_PRODUCT,
               V_PROGRAM_ID,
               (SELECT CUSTOMER_NAME
                  FROM CUSTOMER
                 WHERE CUSTOMER_ID = IN_CUSTOMER_ID) ||
               ' Program - Forms A/B Español A',
               SYSDATE,
               add_months(SYSDATE, 120),
               NULL,
               '2015',
               V_ACTIVATION_STATUS,
               SYSDATE,
               SYSDATE);
        
             END IF;
        END IF;
        /* Program end*/
        dbms_output.put_line('program end');
        /*ORG_NODE_TEST_CATALOG table start */
      
        dbms_output.put_line('ONTC start');
        IF (IS_NEW_CUSTOMER) THEN
          DELETE FROM ORG_NODE_TEST_CATALOG ONTC
           WHERE ONTC.PRODUCT_ID = ALL_PRODUCT_ID.PRODUCT_ID
             AND ONTC.CUSTOMER_ID = IN_CUSTOMER_ID;
        ELSE
          UPDATE ORG_NODE_TEST_CATALOG ONTC
             SET ONTC.ACTIVATION_STATUS = 'IN'
           WHERE ONTC.PRODUCT_ID = ALL_PRODUCT_ID.PRODUCT_ID
             AND ONTC.CUSTOMER_ID = IN_CUSTOMER_ID;
        END IF;
        
        SELECT COUNT(1)
          INTO V_CHECK_NEW_PRODUCT
          FROM ORG_NODE_TEST_CATALOG
         WHERE CUSTOMER_ID = IN_CUSTOMER_ID
           AND PRODUCT_ID = V_NEW_PRODUCT_ID;
      
        IF V_CHECK_NEW_PRODUCT = 0 THEN
        
          FOR C IN CUR_TEST_CATALOG_DETAILS(V_NEW_PRODUCT_ID) LOOP
            INSERT INTO ORG_NODE_TEST_CATALOG
              (TEST_CATALOG_ID,
               PRODUCT_ID,
               ITEM_SET_ID,
               ORG_NODE_ID,
               CUSTOMER_ID,
               CREATED_BY,
               CREATED_DATE_TIME,
               UPDATED_BY,
               UPDATED_DATE_TIME,
               ACTIVATION_STATUS,
               OVERRIDE_NO_RETAKE,
               OVERRIDE_FORM_ASSIGNMENT,
               OVERRIDE_LOGIN_START_DATE,
               LIC_PURCHASED,
               LIC_RESERVED,
               LIC_USED,
               RANDOM_DISTRACTOR_ALLOWABLE,
               OVERRIDE_LOGIN_END_DATE)
              (SELECT DISTINCT C.TEST_CATALOG_ID AS TEST_CATALOG_ID,
                               C.PRODUCT_ID AS PRODUCT_ID,
                               C.ITEM_SET_ID AS ITEM_SET_ID,
                               ORG.ORG_NODE_ID,
                               ORG.CUSTOMER_ID AS CUSTOMER_ID,
                               1 AS CREATED_BY,
                               SYSDATE AS CREATED_DATE_TIME,
                               NULL AS UPDATED_BY,
                               NULL AS UPDATED_DATE_TIME,
                               V_ACTIVATION_STATUS AS ACTIVATION_STATUS,
                               V_OVERRIDE_NO_RETAKE AS OVERRIDE_NO_RETAKE,
                               NULL AS OVERRIDE_FORM_ASSIGNMENT,
                               NULL AS OVERRIDE_LOGIN_START_DATE,
                               -1 AS LIC_PURCHASED,
                               -1 AS LIC_RESERVED,
                               -1 AS LIC_USED,
                               NULL AS RANDOM_DISTRACTOR_ALLOWABLE,
                               NULL AS OVERRIDE_LOGIN_END_DATE
                 FROM ORG_NODE ORG
                WHERE ORG.CUSTOMER_ID = IN_CUSTOMER_ID);
          END LOOP;
          V_CHECK_NEW_PRODUCT := 0;
          dbms_output.put_line('ONTC end');
        ELSE
          dbms_output.put_line('PRODUCT ID ->> ' || V_NEW_PRODUCT_ID ||
                               ' ALREADY EXIST');
        END IF;
        /*ORG_NODE_TEST_CATALOG table end */
      
        /*REPORT_BRIDGE START*/
        SELECT COUNT(1)
          INTO V_CONFIGCOUNT
          FROM CUSTOMER_REPORT_BRIDGE CRB
         WHERE CRB.CUSTOMER_ID = IN_CUSTOMER_ID
           AND CRB.PRODUCT_ID = V_NEW_PARENT_PRODUCT;
      
        IF V_CONFIGCOUNT = 0 THEN
          dbms_output.put_line('Report start');
          FOR R IN CUR_REPORT_BRIDGE(ALL_PRODUCT_ID.PARENT_PRODUCT_ID) LOOP
            INSERT INTO CUSTOMER_REPORT_BRIDGE
              (CUSTOMER_ID,
               REPORT_NAME,
               DISPLAY_NAME,
               DESCRIPTION,
               SYSTEM_KEY,
               CUSTOMER_KEY,
               REPORT_URL,
               PRODUCT_ID)
            VALUES
              (IN_CUSTOMER_ID,
               R.REPORT_NAME,
               R.DISPLAY_NAME,
               R.DESCRIPTION,
               R.SYSTEM_KEY,
               R.CUSTOMER_KEY,
               R.REPORT_URL,
               V_NEW_PARENT_PRODUCT);
          
          END LOOP;
          DELETE FROM CUSTOMER_REPORT_BRIDGE
           WHERE CUSTOMER_ID = IN_CUSTOMER_ID
             AND PRODUCT_ID = ALL_PRODUCT_ID.PARENT_PRODUCT_ID;
          dbms_output.put_line('Report end');
        END IF;
        /*REPORT_BRIDGE END*/
      
        /*END OF 1ST EDITION CHANGES*/
      
      ELSIF ALL_PRODUCT_ID.PARENT_PRODUCT_ID = 7500 THEN
      
        /*START OF 2ND EDITION CHANGES*/
        V_NEW_PRODUCT_ID := ALL_PRODUCT_ID.PRODUCT_ID + 300;
        dbms_output.put_line('V_NEW_PRODUCT_ID -->>' || V_NEW_PRODUCT_ID);
        SELECT PARENT_PRODUCT_ID
          INTO V_NEW_PARENT_PRODUCT
          FROM PRODUCT
         WHERE PRODUCT_ID = V_NEW_PRODUCT_ID;
        dbms_output.put_line('V_NEW_PARENT_PRODUCT -->>' ||
                             V_NEW_PARENT_PRODUCT);
        /* Program start*/
        SELECT COUNT(1)
          INTO V_CONFIGCOUNT
          FROM PROGRAM
         WHERE CUSTOMER_ID = IN_CUSTOMER_ID
           AND PRODUCT_ID = V_NEW_PARENT_PRODUCT;
      
        IF V_CONFIGCOUNT = 0 THEN
        
           SELECT COUNT(1)
             INTO V_CONFIGCOUNT
             FROM PROGRAM PROG
            WHERE PROG.PRODUCT_ID = ALL_PRODUCT_ID.PARENT_PRODUCT_ID
              AND PROG.CUSTOMER_ID = IN_CUSTOMER_ID;
             
           IF V_CONFIGCOUNT > 0 THEN
           
            dbms_output.put_line('Program start');
            IF (IS_NEW_CUSTOMER) THEN
               DELETE FROM PROGRAM PROG
               WHERE PROG.PRODUCT_ID = ALL_PRODUCT_ID.PARENT_PRODUCT_ID
                 AND PROG.CUSTOMER_ID = IN_CUSTOMER_ID;
            ELSE
              UPDATE PROGRAM PROG SET PROG.ACTIVATION_STATUS = 'DE', PROG.PROGRAM_END_DATE = add_months(SYSDATE, 120)
               WHERE PROG.PRODUCT_ID = ALL_PRODUCT_ID.PARENT_PRODUCT_ID
                 AND PROG.CUSTOMER_ID = IN_CUSTOMER_ID;             
            END IF;
          
            SELECT CAST(IN_CUSTOMER_ID || '78' AS INT)
              INTO V_PROGRAM_ID
              FROM DUAL;
          
            INSERT INTO PROGRAM
              (CUSTOMER_ID,
               PRODUCT_ID,
               PROGRAM_ID,
               PROGRAM_NAME,
               PROGRAM_START_DATE,
               PROGRAM_END_DATE,
               NORMS_GROUP,
               NORMS_YEAR,
               ACTIVATION_STATUS,
               CREATED_DATE_TIME,
               UPDATED_DATE_TIME)
            VALUES
              (IN_CUSTOMER_ID,
               V_NEW_PARENT_PRODUCT,
               V_PROGRAM_ID,
               (SELECT CUSTOMER_NAME
                  FROM CUSTOMER
                 WHERE CUSTOMER_ID = IN_CUSTOMER_ID) ||
               ' Program - Forms C/D Español B',
               SYSDATE,
               add_months(SYSDATE, 120),
               NULL,
               '2015',
               V_ACTIVATION_STATUS,
               SYSDATE,
               SYSDATE);
            dbms_output.put_line('Program end');
          END IF;
        END IF;
        /* Program end*/
      
        /*ORG_NODE_TEST_CATALOG table start */
 
        dbms_output.put_line('ONTC start');
        IF (IS_NEW_CUSTOMER) THEN
          DELETE FROM ORG_NODE_TEST_CATALOG ONTC
           WHERE ONTC.PRODUCT_ID = ALL_PRODUCT_ID.PRODUCT_ID
             AND ONTC.CUSTOMER_ID = IN_CUSTOMER_ID;
        ELSE
          UPDATE ORG_NODE_TEST_CATALOG ONTC
             SET ONTC.ACTIVATION_STATUS = 'IN'
           WHERE ONTC.PRODUCT_ID = ALL_PRODUCT_ID.PRODUCT_ID
             AND ONTC.CUSTOMER_ID = IN_CUSTOMER_ID;
        END IF;
      
        SELECT COUNT(1)
          INTO V_CHECK_NEW_PRODUCT
          FROM ORG_NODE_TEST_CATALOG
         WHERE CUSTOMER_ID = IN_CUSTOMER_ID
           AND PRODUCT_ID = V_NEW_PRODUCT_ID;
      
        IF V_CHECK_NEW_PRODUCT = 0 THEN
          FOR C IN CUR_TEST_CATALOG_DETAILS(V_NEW_PRODUCT_ID) LOOP
            INSERT INTO ORG_NODE_TEST_CATALOG
              (TEST_CATALOG_ID,
               PRODUCT_ID,
               ITEM_SET_ID,
               ORG_NODE_ID,
               CUSTOMER_ID,
               CREATED_BY,
               CREATED_DATE_TIME,
               UPDATED_BY,
               UPDATED_DATE_TIME,
               ACTIVATION_STATUS,
               OVERRIDE_NO_RETAKE,
               OVERRIDE_FORM_ASSIGNMENT,
               OVERRIDE_LOGIN_START_DATE,
               LIC_PURCHASED,
               LIC_RESERVED,
               LIC_USED,
               RANDOM_DISTRACTOR_ALLOWABLE,
               OVERRIDE_LOGIN_END_DATE)
              (SELECT DISTINCT C.TEST_CATALOG_ID AS TEST_CATALOG_ID,
                               C.PRODUCT_ID AS PRODUCT_ID,
                               C.ITEM_SET_ID AS ITEM_SET_ID,
                               ORG.ORG_NODE_ID,
                               ORG.CUSTOMER_ID AS CUSTOMER_ID,
                               1 AS CREATED_BY,
                               SYSDATE AS CREATED_DATE_TIME,
                               NULL AS UPDATED_BY,
                               NULL AS UPDATED_DATE_TIME,
                               V_ACTIVATION_STATUS AS ACTIVATION_STATUS,
                               V_OVERRIDE_NO_RETAKE AS OVERRIDE_NO_RETAKE,
                               NULL AS OVERRIDE_FORM_ASSIGNMENT,
                               NULL AS OVERRIDE_LOGIN_START_DATE,
                               -1 AS LIC_PURCHASED,
                               -1 AS LIC_RESERVED,
                               -1 AS LIC_USED,
                               NULL AS RANDOM_DISTRACTOR_ALLOWABLE,
                               NULL AS OVERRIDE_LOGIN_END_DATE
                 FROM ORG_NODE ORG
                WHERE ORG.CUSTOMER_ID = IN_CUSTOMER_ID);
          END LOOP;
          V_CHECK_NEW_PRODUCT := 0;
          dbms_output.put_line('ONTC end');
        ELSE
          dbms_output.put_line('PRODUCT ID ->> ' || V_NEW_PRODUCT_ID ||
                               ' ALREADY EXIST');
        END IF;
      
        /*ORG_NODE_TEST_CATALOG table end */
      
        /*REPORT_BRIDGE START*/
        SELECT COUNT(1)
          INTO V_CONFIGCOUNT
          FROM CUSTOMER_REPORT_BRIDGE CRB
         WHERE CRB.CUSTOMER_ID = IN_CUSTOMER_ID
           AND CRB.PRODUCT_ID = V_NEW_PARENT_PRODUCT;
      
        IF V_CONFIGCOUNT = 0 THEN
          dbms_output.put_line('Report start');
          FOR R IN CUR_REPORT_BRIDGE(ALL_PRODUCT_ID.PARENT_PRODUCT_ID) LOOP
            INSERT INTO CUSTOMER_REPORT_BRIDGE
              (CUSTOMER_ID,
               REPORT_NAME,
               DISPLAY_NAME,
               DESCRIPTION,
               SYSTEM_KEY,
               CUSTOMER_KEY,
               REPORT_URL,
               PRODUCT_ID)
            VALUES
              (IN_CUSTOMER_ID,
               R.REPORT_NAME,
               R.DISPLAY_NAME,
               R.DESCRIPTION,
               R.SYSTEM_KEY,
               R.CUSTOMER_KEY,
               R.REPORT_URL,
               V_NEW_PARENT_PRODUCT);
          
          END LOOP;
          DELETE FROM CUSTOMER_REPORT_BRIDGE
           WHERE CUSTOMER_ID = IN_CUSTOMER_ID
             AND PRODUCT_ID = ALL_PRODUCT_ID.PARENT_PRODUCT_ID;
          dbms_output.put_line('Report end');
        END IF;
        /*REPORT_BRIDGE END*/
        /*END OF 2ND EDITION CHANGES*/
      END IF;
    END LOOP;
  
    /*Customer_resource start*/
    SELECT COUNT(1)
      INTO V_RESOURCE_ID
      FROM CUSTOMER_RESOURCE
     WHERE CUSTOMER_ID = IN_CUSTOMER_ID;
  
    IF V_RESOURCE_ID = 0 THEN
      INSERT INTO CUSTOMER_RESOURCE
        (CUSTOMER_ID, RESOURCE_TYPE_CODE, RESOURCE_URI)
      VALUES
        (IN_CUSTOMER_ID,
         'BMTITEMAPI',
         'https://bmt-laslinks-batch-qa.ec2-ctb.com/api/v1/bmt/viewitem');
      INSERT INTO CUSTOMER_RESOURCE
        (CUSTOMER_ID, RESOURCE_TYPE_CODE, RESOURCE_URI)
      VALUES
        (IN_CUSTOMER_ID,
         'BMTROSTAPI',
         'https://bmt-laslinks-batch-qa.ec2-ctb.com/api/v1/bmt/deleteroster/');
      INSERT INTO CUSTOMER_RESOURCE
        (CUSTOMER_ID, RESOURCE_TYPE_CODE, RESOURCE_URI)
      VALUES
        (IN_CUSTOMER_ID,
         'BMTSESNAPI',
         'https://bmt-laslinks-batch-qa.ec2-ctb.com/api/v1/bmt/deletesession/');
      INSERT INTO CUSTOMER_RESOURCE
        (CUSTOMER_ID, RESOURCE_TYPE_CODE, RESOURCE_URI)
      VALUES
        (IN_CUSTOMER_ID,
         'BMTSTDAPI',
         'https://bmt-laslinks-batch-qa.ec2-ctb.com/api/v1/bmt/deletestudent/');
      dbms_output.put_line('customer_resource Inserted');
    END IF;
    /*Customer_resource end*/
  
    /*Customer configuration Add Start*/
    SELECT COUNT(1)
      INTO V_CONFIGCOUNT
      FROM OAS.CUSTOMER_CONFIGURATION
     WHERE CUSTOMER_CONFIGURATION_NAME = 'LLO_RP_Customer'
       AND CUSTOMER_ID = IN_CUSTOMER_ID;
  
    IF (V_CONFIGCOUNT = 0) THEN
      INSERT INTO OAS.CUSTOMER_CONFIGURATION
        (CUSTOMER_CONFIGURATION_ID,
         CUSTOMER_CONFIGURATION_NAME,
         CUSTOMER_ID,
         EDITABLE,
         DEFAULT_VALUE,
         CREATED_BY,
         CREATED_DATE_TIME)
      VALUES
        (SEQ_CUSTOMER_CONFIGURATION_ID.NEXTVAL,
         'LLO_RP_Customer',
         IN_CUSTOMER_ID,
         'T',
         'T',
         1,
         SYSDATE);
      dbms_output.put_line('LLO_RP_CUSTOMER Inserted');
    END IF;
    
    SELECT COUNT(1)
      INTO V_CONFIGCOUNT
      FROM OAS.CUSTOMER_CONFIGURATION
     WHERE CUSTOMER_CONFIGURATION_NAME = 'Allow_Hide_Tooltips'
       AND CUSTOMER_ID = IN_CUSTOMER_ID;
  
    IF (V_CONFIGCOUNT = 0) THEN
      INSERT INTO OAS.CUSTOMER_CONFIGURATION
        (CUSTOMER_CONFIGURATION_ID,
         CUSTOMER_CONFIGURATION_NAME,
         CUSTOMER_ID,
         EDITABLE,
         DEFAULT_VALUE,
         CREATED_BY,
         CREATED_DATE_TIME)
      VALUES
        (SEQ_CUSTOMER_CONFIGURATION_ID.NEXTVAL,
         'Allow_Hide_Tooltips',
         IN_CUSTOMER_ID,
         'T',
         'T',
         1,
         SYSDATE);
      dbms_output.put_line('Allow_Hide_Tooltips Inserted');
    END IF;
    
    SELECT COUNT(1)
      INTO V_CONFIGCOUNT
      FROM OAS.CUSTOMER_CONFIGURATION
     WHERE CUSTOMER_CONFIGURATION_NAME = 'Remove_IndvlTckt_Keyboard'
       AND CUSTOMER_ID = IN_CUSTOMER_ID;
  
    IF (V_CONFIGCOUNT = 0) THEN
      INSERT INTO OAS.CUSTOMER_CONFIGURATION
        (CUSTOMER_CONFIGURATION_ID,
         CUSTOMER_CONFIGURATION_NAME,
         CUSTOMER_ID,
         EDITABLE,
         DEFAULT_VALUE,
         CREATED_BY,
         CREATED_DATE_TIME)
      VALUES
        (SEQ_CUSTOMER_CONFIGURATION_ID.NEXTVAL,
         'Remove_IndvlTckt_Keyboard',
         IN_CUSTOMER_ID,
         'T',
         'T',
         1,
         SYSDATE);
      dbms_output.put_line('Remove_IndvlTckt_Keyboard Inserted');
    END IF;
    
    SELECT COUNT(1)
      INTO V_CONFIGCOUNT
      FROM OAS.CUSTOMER_CONFIGURATION
     WHERE CUSTOMER_CONFIGURATION_NAME = 'Disable_Test_Session_Edit'
       AND CUSTOMER_ID = IN_CUSTOMER_ID;
  
    IF (V_CONFIGCOUNT = 0) THEN
      INSERT INTO OAS.CUSTOMER_CONFIGURATION
        (CUSTOMER_CONFIGURATION_ID,
         CUSTOMER_CONFIGURATION_NAME,
         CUSTOMER_ID,
         EDITABLE,
         DEFAULT_VALUE,
         CREATED_BY,
         CREATED_DATE_TIME)
      VALUES
        (SEQ_CUSTOMER_CONFIGURATION_ID.NEXTVAL,
         'Disable_Test_Session_Edit',
         IN_CUSTOMER_ID,
         'T',
         'T',
         1,
         SYSDATE);
      dbms_output.put_line('Disable test session inserted');
    END IF;
  
    IF ADD_ENGRADE_CONFIG = TRUE THEN
      SELECT COUNT(1)
        INTO COUNTER
        FROM CUSTOMER_CONFIGURATION
       WHERE CUSTOMER_ID = IN_CUSTOMER_ID
         AND CUSTOMER_CONFIGURATION_NAME = 'Configurable_Student_ID';
    
      IF COUNTER = 0 THEN
        SELECT SEQ_CUSTOMER_CONFIGURATION_ID.NEXTVAL
          INTO CUSTOMER_CONFIG_ID
          FROM dual;
      
        INSERT INTO CUSTOMER_CONFIGURATION
          (CUSTOMER_CONFIGURATION_ID,
           CUSTOMER_CONFIGURATION_NAME,
           CUSTOMER_ID,
           EDITABLE,
           DEFAULT_VALUE,
           CREATED_BY,
           CREATED_DATE_TIME)
        VALUES
          (CUSTOMER_CONFIG_ID,
           'Configurable_Student_ID',
           IN_CUSTOMER_ID,
           'T',
           'T',
           1,
           SYSDATE);
      
        INSERT INTO CUSTOMER_CONFIGURATION_VALUE
          (CUSTOMER_CONFIGURATION_VALUE,
           CUSTOMER_CONFIGURATION_ID,
           SORT_ORDER)
        VALUES
          ('Student ID', CUSTOMER_CONFIG_ID, '1');
      
        INSERT INTO CUSTOMER_CONFIGURATION_VALUE
          (CUSTOMER_CONFIGURATION_VALUE,
           CUSTOMER_CONFIGURATION_ID,
           SORT_ORDER)
        VALUES
          ('10', CUSTOMER_CONFIG_ID, '2');
      
        INSERT INTO CUSTOMER_CONFIGURATION_VALUE
          (CUSTOMER_CONFIGURATION_VALUE,
           CUSTOMER_CONFIGURATION_ID,
           SORT_ORDER)
        VALUES
          ('T', CUSTOMER_CONFIG_ID, '3');
      
        INSERT INTO CUSTOMER_CONFIGURATION_VALUE
          (CUSTOMER_CONFIGURATION_VALUE,
           CUSTOMER_CONFIGURATION_ID,
           SORT_ORDER)
        VALUES
          ('10', CUSTOMER_CONFIG_ID, '4');
      
        INSERT INTO CUSTOMER_CONFIGURATION_VALUE
          (CUSTOMER_CONFIGURATION_VALUE,
           CUSTOMER_CONFIGURATION_ID,
           SORT_ORDER)
        VALUES
          ('AN', CUSTOMER_CONFIG_ID, '5');

      END IF;
      COUNTER            := 1;
      CUSTOMER_CONFIG_ID := 0;
    
      SELECT COUNT(1)
        INTO COUNTER
        FROM CUSTOMER_CONFIGURATION
       WHERE CUSTOMER_ID = IN_CUSTOMER_ID
         AND CUSTOMER_CONFIGURATION_NAME = 'ENGRADE_Customer';
    
      IF COUNTER = 0 THEN
      
        INSERT INTO CUSTOMER_CONFIGURATION
          (CUSTOMER_CONFIGURATION_ID,
           CUSTOMER_CONFIGURATION_NAME,
           CUSTOMER_ID,
           EDITABLE,
           DEFAULT_VALUE,
           CREATED_BY,
           CREATED_DATE_TIME)
        VALUES
          (SEQ_CUSTOMER_CONFIGURATION_ID.NEXTVAL,
           'ENGRADE_Customer',
           IN_CUSTOMER_ID,
           'T',
           'T',
           1,
           SYSDATE);
      END IF;
      COUNTER := 1;
    
      SELECT COUNT(1)
        INTO COUNTER
        FROM CUSTOMER_CONFIGURATION
       WHERE CUSTOMER_ID = IN_CUSTOMER_ID
         AND CUSTOMER_CONFIGURATION_NAME = 'EXT_SCHOOL_ID_Configurable';
    
      IF COUNTER = 0 THEN
      
        INSERT INTO CUSTOMER_CONFIGURATION
          (CUSTOMER_CONFIGURATION_ID,
           CUSTOMER_CONFIGURATION_NAME,
           CUSTOMER_ID,
           EDITABLE,
           DEFAULT_VALUE,
           CREATED_BY,
           CREATED_DATE_TIME)
        VALUES
          (SEQ_CUSTOMER_CONFIGURATION_ID.NEXTVAL,
           'EXT_SCHOOL_ID_Configurable',
           IN_CUSTOMER_ID,
           'T',
           'T',
           1,
           SYSDATE);
      END IF;
      COUNTER := 1;
    
      SELECT COUNT(1)
        INTO COUNTER
        FROM CUSTOMER_CONFIGURATION
       WHERE CUSTOMER_ID = IN_CUSTOMER_ID
         AND CUSTOMER_CONFIGURATION_NAME = 'Block_Student_Creation';
    
      IF COUNTER = 0 THEN
      
        INSERT INTO CUSTOMER_CONFIGURATION
          (CUSTOMER_CONFIGURATION_ID,
           CUSTOMER_CONFIGURATION_NAME,
           CUSTOMER_ID,
           EDITABLE,
           DEFAULT_VALUE,
           CREATED_BY,
           CREATED_DATE_TIME)
        VALUES
          (SEQ_CUSTOMER_CONFIGURATION_ID.NEXTVAL,
           'Block_Student_Creation',
           IN_CUSTOMER_ID,
           'T',
           'T',
           1,
           SYSDATE);
      END IF;
      COUNTER := 1;
    
      SELECT COUNT(1)
        INTO COUNTER
        FROM CUSTOMER_CONFIGURATION
       WHERE CUSTOMER_ID = IN_CUSTOMER_ID
         AND CUSTOMER_CONFIGURATION_NAME = 'Block_Student_Deletion';
    
      IF COUNTER = 0 THEN
      
        INSERT INTO CUSTOMER_CONFIGURATION
          (CUSTOMER_CONFIGURATION_ID,
           CUSTOMER_CONFIGURATION_NAME,
           CUSTOMER_ID,
           EDITABLE,
           DEFAULT_VALUE,
           CREATED_BY,
           CREATED_DATE_TIME)
        VALUES
          (SEQ_CUSTOMER_CONFIGURATION_ID.NEXTVAL,
           'Block_Student_Deletion',
           IN_CUSTOMER_ID,
           'T',
           'T',
           1,
           SYSDATE);
      END IF;
      COUNTER := 1;
    
      SELECT COUNT(1)
        INTO COUNTER
        FROM CUSTOMER_CONFIGURATION
       WHERE CUSTOMER_ID = IN_CUSTOMER_ID
         AND CUSTOMER_CONFIGURATION_NAME = 'SSO_Hide_User_Profile';
    
      IF COUNTER = 0 THEN
        INSERT INTO CUSTOMER_CONFIGURATION
          (CUSTOMER_CONFIGURATION_ID,
           CUSTOMER_CONFIGURATION_NAME,
           CUSTOMER_ID,
           EDITABLE,
           DEFAULT_VALUE,
           CREATED_BY,
           CREATED_DATE_TIME)
        VALUES
          (SEQ_CUSTOMER_CONFIGURATION_ID.NEXTVAL,
           'SSO_Hide_User_Profile',
           IN_CUSTOMER_ID,
           'T',
           'T',
           1,
           SYSDATE);
      END IF;
      COUNTER := 1;
                  
      SELECT COUNT(1)
        INTO COUNTER
        FROM CUSTOMER_CONFIGURATION
       WHERE CUSTOMER_ID = IN_CUSTOMER_ID
         AND CUSTOMER_CONFIGURATION_NAME = 'SSO_Block_User_Modifications';
    
      IF COUNTER = 0 THEN
      
        INSERT INTO CUSTOMER_CONFIGURATION
          (CUSTOMER_CONFIGURATION_ID,
           CUSTOMER_CONFIGURATION_NAME,
           CUSTOMER_ID,
           EDITABLE,
           DEFAULT_VALUE,
           CREATED_BY,
           CREATED_DATE_TIME)
        VALUES
          (SEQ_CUSTOMER_CONFIGURATION_ID.NEXTVAL,
           'SSO_Block_User_Modifications',
           IN_CUSTOMER_ID,
           'T',
           'T',
           1,
           SYSDATE);
      END IF;
      
      UPDATE CUSTOMER_CONFIGURATION
         SET CUSTOMER_CONFIGURATION_NAME = 'License_Email_Notification_BAK'
       WHERE CUSTOMER_ID = IN_CUSTOMER_ID
         AND CUSTOMER_CONFIGURATION_NAME = 'License_Email_Notification';

      UPDATE CUSTOMER_CONFIGURATION
         SET CUSTOMER_CONFIGURATION_NAME = 'License_Yearly_Expiry_BAK'
       WHERE CUSTOMER_ID = IN_CUSTOMER_ID
         AND CUSTOMER_CONFIGURATION_NAME = 'License_Yearly_Expiry';

      UPDATE CUSTOMER_CONFIGURATION
         SET CUSTOMER_CONFIGURATION_NAME = 'License_Subtest_Model_BAK'
       WHERE CUSTOMER_ID = IN_CUSTOMER_ID
         AND CUSTOMER_CONFIGURATION_NAME = 'License_Subtest_Model';

      UPDATE CUSTOMER_CONFIGURATION
         SET CUSTOMER_CONFIGURATION_NAME = 'Allow_Upload_Download_BAK'
       WHERE CUSTOMER_ID = IN_CUSTOMER_ID
         AND CUSTOMER_CONFIGURATION_NAME = 'Allow_Upload_Download';

      COUNTER := 1;
    
      SELECT COUNT(1)
        INTO COUNTER
        FROM CUSTOMER_DEMOGRAPHIC
       WHERE CUSTOMER_ID = IN_CUSTOMER_ID
         AND LABEL_NAME = 'Ethnicity'
         AND LABEL_CODE = 'Ethnicity';
      
      IF COUNTER <> 0 THEN
        CUSTOMER_CONFIG_ID := 0;
        
        SELECT CUSTOMER_DEMOGRAPHIC_ID
          INTO CUSTOMER_CONFIG_ID
          FROM CUSTOMER_DEMOGRAPHIC
         WHERE CUSTOMER_ID = IN_CUSTOMER_ID
           AND LABEL_NAME = 'Ethnicity'
           AND LABEL_CODE = 'Ethnicity';
      
        IF CUSTOMER_CONFIG_ID <> 0 THEN
          UPDATE CUSTOMER_DEMOGRAPHIC_VALUE
             SET VALUE_NAME = 'Other', VALUE_CODE = 'Other'
           WHERE CUSTOMER_DEMOGRAPHIC_ID = CUSTOMER_CONFIG_ID
             AND VALUE_NAME = 'Other(Applicable to Forms A/B/Esp. A only)'
             AND VALUE_CODE = 'Other(Applicable to Forms A/B/Esp. A only)';
        END IF;
      END IF;
    
      COUNTER := 1;
    
      SELECT COUNT(1)
        INTO COUNTER
        FROM SSO_CUSTOMER_INFO
       WHERE CUSTOMER_ID = IN_CUSTOMER_ID;
               
      IF COUNTER = 0 THEN
        INSERT INTO SSO_CUSTOMER_INFO
        (CUSTOMER_ID,
         SECRET_KEY,
         ERROR_RETURN_URL)
        VALUES
          (IN_CUSTOMER_ID,
           'oasctb123',
           'https://www.engradepro.com/app/oauth/return.php?lti_errormsg=ERROR_CODE');
      ELSE
          UPDATE SSO_CUSTOMER_INFO
          SET SECRET_KEY='oasctb123',
          ERROR_RETURN_URL='https://www.engradepro.com/app/oauth/return.php?lti_errormsg=ERROR_CODE'
          WHERE CUSTOMER_ID=IN_CUSTOMER_ID;
      END IF;
    END IF;
    /*Customer configuration Add End*/
    COMMIT;
  END IF;
EXCEPTION
  WHEN OTHERS THEN
    ROLLBACK;
  
END SETUP_LLO_RP_CUSTOMER_CONFIG;