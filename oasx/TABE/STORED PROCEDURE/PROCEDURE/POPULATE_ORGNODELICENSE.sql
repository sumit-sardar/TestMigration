CREATE OR REPLACE PROCEDURE POPULATE_ORGNODELICENSE IS

  V_REC_COUNT          NUMBER(10) := 0;
  TOPORGNODECATEGORYID INTEGER;
  AVAILABLELIC         INTEGER := 0;
  SESSIONMODELCOUNT    NUMBER(10) := 0;
  SUBTESTMODELCOUNT    NUMBER(10) := 0;
  RESERVEDLICENSECOUNT INTEGER := 0;
  CONSUMEDLICENSECOUNT INTEGER := 0;
  VCUSTOMERID          INTEGER := 0;

  -- Get list of customer from customer_product_license table
  CURSOR GETALLCUSTOMERID IS
    SELECT DISTINCT (CUSTOMER_ID) AS CUSTOMERID
      FROM CUSTOMER_PRODUCT_LICENSE;

  --To get list of all product assigned to a customer
  CURSOR GETCUSTOMERPRODUCTLICENSE(VCUSTOMERID INTEGER) IS
    SELECT *
      FROM CUSTOMER_PRODUCT_LICENSE
     WHERE CUSTOMER_ID = VCUSTOMERID
       AND PRODUCT_ID = 4000;

  -- To get all Orgnode of customer along with childnode count to identify leaf node
  CURSOR GETCUSTOMERORGNODE(VCUSTOMERID INTEGER, TOPORGNODECATEGORYID ORG_NODE_CATEGORY.ORG_NODE_CATEGORY_ID%TYPE) IS
    SELECT DISTINCT NODE.ORG_NODE_ID AS ORGNODEID,
                    NODE.CUSTOMER_ID AS CUSTOMERID,
                    NODE.ORG_NODE_CATEGORY_ID AS ORGNODECATEGORYID,
                    NODE.ORG_NODE_NAME AS ORGNODENAME,
                    CAT.CATEGORY_NAME AS ORGNODECATEGORYNAME,
                    COUNT(DISTINCT DESCENDANT_NODE.ORG_NODE_ID) - 1 AS CHILDNODECOUNT
      FROM ORG_NODE          NODE,
           ORG_NODE_CATEGORY CAT,
           ORG_NODE_CATEGORY CUSTOMERCAT,
           ORG_NODE_ANCESTOR DESCENDANTS,
           ORG_NODE          DESCENDANT_NODE,
           ORG_NODE_ANCESTOR DESCENDANTSCHILD
     WHERE DESCENDANTS.ORG_NODE_ID = NODE.ORG_NODE_ID
       AND CAT.ORG_NODE_CATEGORY_ID = NODE.ORG_NODE_CATEGORY_ID
       AND CAT.ACTIVATION_STATUS = 'AC'
       AND DESCENDANTS.ANCESTOR_ORG_NODE_ID =
           (SELECT ORN.ORG_NODE_ID AS ORGNODEID
              FROM ORG_NODE ORN
             WHERE ORN.ORG_NODE_CATEGORY_ID = TOPORGNODECATEGORYID)
       AND NODE.ORG_NODE_ID = DESCENDANTSCHILD.ANCESTOR_ORG_NODE_ID
       AND DESCENDANTSCHILD.ORG_NODE_ID = DESCENDANT_NODE.ORG_NODE_ID
       AND DESCENDANTSCHILD.NUMBER_OF_LEVELS >= 0
       AND DESCENDANT_NODE.ACTIVATION_STATUS = 'AC'
       AND NODE.ACTIVATION_STATUS = 'AC'
     GROUP BY NODE.ORG_NODE_ID,
              NODE.CUSTOMER_ID,
              NODE.ORG_NODE_CATEGORY_ID,
              NODE.ORG_NODE_NAME,
              CAT.CATEGORY_NAME;

BEGIN

  FOR CUSTOMERIDLIST IN GETALLCUSTOMERID() LOOP
    IF (CUSTOMERIDLIST.CUSTOMERID IS NOT NULL) THEN
      VCUSTOMERID := CUSTOMERIDLIST.CUSTOMERID;
      -- validate existance of a customer
      BEGIN
        SELECT 1
          INTO V_REC_COUNT
          FROM CUSTOMER
         WHERE CUSTOMER.CUSTOMER_ID = VCUSTOMERID;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          RAISE_APPLICATION_ERROR(-20000,
                                  'Customer id [' || VCUSTOMERID ||
                                  '] is not valid. Give Existing Id.');
      END;
    
      BEGIN
        -- To get categoryid for top orgnode of a customer
        SELECT ONC.ORG_NODE_CATEGORY_ID
          INTO TOPORGNODECATEGORYID
          FROM ORG_NODE_CATEGORY ONC
         WHERE ONC.CUSTOMER_ID = VCUSTOMERID
           AND ONC.CATEGORY_LEVEL =
               (SELECT MIN(ONC1.CATEGORY_LEVEL)
                  FROM ORG_NODE_CATEGORY ONC1
                 WHERE ONC.CUSTOMER_ID = ONC1.CUSTOMER_ID
                   AND ONC1.ACTIVATION_STATUS = 'AC'
                   AND ONC1.IS_GROUP = 'F')
           AND ONC.IS_GROUP = 'F'
           AND ONC.ACTIVATION_STATUS = 'AC'
           AND ROWNUM < 2;
      EXCEPTION
        WHEN OTHERS THEN
          DBMS_OUTPUT.PUT_LINE(VCUSTOMERID);
      END;
    
      FOR PRODUCTLICENSE IN GETCUSTOMERPRODUCTLICENSE(VCUSTOMERID) LOOP
        IF (PRODUCTLICENSE.PRODUCT_ID IS NOT NULL) THEN
        
          FOR NODE IN GETCUSTOMERORGNODE(VCUSTOMERID, TOPORGNODECATEGORYID) LOOP
            IF (NODE.ORGNODEID IS NOT NULL) THEN
            
              -- check for the excistance of record for a customer orgnode and product. 
              V_REC_COUNT := 0;
              BEGIN
                SELECT 1
                  INTO V_REC_COUNT
                  FROM CUSTOMER_ORGNODE_LICENSE COL
                 WHERE COL.CUSTOMER_ID = VCUSTOMERID
                   AND COL.ORG_NODE_ID = NODE.ORGNODEID
                   AND COL.PRODUCT_ID = PRODUCTLICENSE.PRODUCT_ID;
              EXCEPTION
                WHEN NO_DATA_FOUND THEN
                  V_REC_COUNT := 0;
              END;
            
              --If record already exist then delete.
              IF (V_REC_COUNT = 1) THEN
                DELETE FROM CUSTOMER_ORGNODE_LICENSE COL
                 WHERE COL.CUSTOMER_ID = VCUSTOMERID
                   AND COL.ORG_NODE_ID = NODE.ORGNODEID
                   AND COL.PRODUCT_ID = PRODUCTLICENSE.PRODUCT_ID;
              END IF;
            
              IF (NODE.ORGNODECATEGORYID = TOPORGNODECATEGORYID AND
                 NODE.CHILDNODECOUNT > 0) THEN
                -- Insert record for Tor org node of a customer.
                INSERT INTO CUSTOMER_ORGNODE_LICENSE
                  (ORG_NODE_ID,
                   CUSTOMER_ID,
                   PRODUCT_ID,
                   AVAILABLE,
                   SUBTEST_MODEL,
                   LICENSE_AFTER_LAST_PURCHASE,
                   EMAIL_NOTIFY_FLAG)
                VALUES
                  (NODE.ORGNODEID,
                   VCUSTOMERID,
                   PRODUCTLICENSE.PRODUCT_ID,
                   PRODUCTLICENSE.AVAILABLE,
                   PRODUCTLICENSE.SUBTEST_MODEL,
                   PRODUCTLICENSE.LICENSE_AFTER_LAST_PURCHASE,
                   PRODUCTLICENSE.EMAIL_NOTIFY_FLAG);
              END IF;
            
              IF (NODE.ORGNODECATEGORYID <> TOPORGNODECATEGORYID AND
                 NODE.CHILDNODECOUNT > 0) THEN
                -- Insert record for non leaf nodes.
                INSERT INTO CUSTOMER_ORGNODE_LICENSE
                  (ORG_NODE_ID,
                   CUSTOMER_ID,
                   PRODUCT_ID,
                   SUBTEST_MODEL,
                   LICENSE_AFTER_LAST_PURCHASE,
                   EMAIL_NOTIFY_FLAG)
                VALUES
                  (NODE.ORGNODEID,
                   VCUSTOMERID,
                   PRODUCTLICENSE.PRODUCT_ID,
                   PRODUCTLICENSE.SUBTEST_MODEL,
                   PRODUCTLICENSE.LICENSE_AFTER_LAST_PURCHASE,
                   PRODUCTLICENSE.EMAIL_NOTIFY_FLAG);
              END IF;
            
              IF (NODE.CHILDNODECOUNT = 0) THEN
              
                IF (NODE.ORGNODECATEGORYID = TOPORGNODECATEGORYID) THEN
                  AVAILABLELIC := PRODUCTLICENSE.AVAILABLE;
                END IF;
              
                IF (PRODUCTLICENSE.SUBTEST_MODEL = 'T') THEN
                  --Calulate reserved license count  for subtest model based on customerId, orgnodeId and customer ProductId.
                  SELECT COUNT(1)
                    INTO SUBTESTMODELCOUNT
                    FROM (SELECT DISTINCT ISP.PARENT_ITEM_SET_ID,
                                          TR.TEST_ROSTER_ID
                            FROM TEST_ADMIN              TADMIN,
                                 TEST_ROSTER             TR,
                                 PRODUCT                 PROD,
                                 STUDENT_ITEM_SET_STATUS SISS,
                                 ITEM_SET                ISET,
                                 ITEM_SET_PARENT         ISP
                          
                           WHERE TADMIN.CUSTOMER_ID = VCUSTOMERID
                             AND TADMIN.TEST_ADMIN_STATUS IN ('CU', 'FU')
                             AND TR.TEST_ADMIN_ID = TADMIN.TEST_ADMIN_ID
                             AND TR.TEST_COMPLETION_STATUS NOT IN
                                 ('NT', 'IC', 'CO')
                             AND TR.ORG_NODE_ID = NODE.ORGNODEID
                             AND SISS.TEST_ROSTER_ID = TR.TEST_ROSTER_ID
                             AND SISS.COMPLETION_STATUS = 'SC'
                             AND SISS.ITEM_SET_ID = ISET.ITEM_SET_ID
                             AND ISET.ITEM_SET_LEVEL != 'L'
                             AND ISET.SAMPLE = 'F'
                             AND ISET.ITEM_SET_ID = ISP.ITEM_SET_ID
                             AND PROD.PRODUCT_ID = TADMIN.PRODUCT_ID
                             AND (PRODUCTLICENSE.PRODUCT_ID =
                                 PROD.PRODUCT_ID OR PRODUCTLICENSE.PRODUCT_ID =
                                 PROD.PARENT_PRODUCT_ID)
                             AND PROD.LICENSE_ENABLED = 'T');
                
                  RESERVEDLICENSECOUNT := SUBTESTMODELCOUNT;
                  SUBTESTMODELCOUNT    := 0;
                
                  --Calulate consumed license count  for subtest model based on customerId, orgnodeId and customer ProductId.
                
                  SELECT COUNT(1)
                    INTO SUBTESTMODELCOUNT
                    FROM (SELECT TR.TEST_ROSTER_ID, ISP.PARENT_ITEM_SET_ID
                            FROM TEST_ADMIN              TADMIN,
                                 TEST_ROSTER             TR,
                                 PRODUCT                 PROD,
                                 STUDENT_ITEM_SET_STATUS SISS,
                                 ITEM_SET                ISET,
                                 ITEM_SET_PARENT         ISP
                           WHERE TADMIN.CUSTOMER_ID = VCUSTOMERID
                             AND TR.TEST_ADMIN_ID = TADMIN.TEST_ADMIN_ID
                             AND TR.TEST_COMPLETION_STATUS NOT IN
                                 ('SC', 'NT')
                             AND TR.ORG_NODE_ID = NODE.ORGNODEID
                             AND SISS.TEST_ROSTER_ID = TR.TEST_ROSTER_ID
                             AND SISS.COMPLETION_STATUS NOT IN ('SC', 'NT')
                             AND SISS.ITEM_SET_ID = ISET.ITEM_SET_ID
                             AND ISET.ITEM_SET_LEVEL != 'L'
                             AND ISET.SAMPLE = 'F'
                             AND ISET.ITEM_SET_ID = ISP.ITEM_SET_ID
                             AND PROD.PRODUCT_ID = TADMIN.PRODUCT_ID
                             AND (PRODUCTLICENSE.PRODUCT_ID =
                                 PROD.PRODUCT_ID OR PRODUCTLICENSE.PRODUCT_ID =
                                 PROD.PARENT_PRODUCT_ID)
                             AND PROD.LICENSE_ENABLED = 'T');
                
                  CONSUMEDLICENSECOUNT := SUBTESTMODELCOUNT;
                ELSE
                  --Calulate reserved license count  for session model based on customerId, orgnodeId and customer ProductId.
                  SELECT COUNT(DISTINCT TEST_ROSTER_ID)
                    INTO SESSIONMODELCOUNT
                    FROM TEST_ADMIN TADMIN, TEST_ROSTER TR, PRODUCT PROD
                   WHERE TADMIN.CUSTOMER_ID = VCUSTOMERID
                     AND TADMIN.TEST_ADMIN_STATUS IN ('CU', 'FU')
                     AND TR.TEST_ADMIN_ID = TADMIN.TEST_ADMIN_ID
                     AND TR.TEST_COMPLETION_STATUS IN
                         ('SC', 'IP', 'IN', 'IS')
                     AND 0 =
                         (SELECT COUNT(DISTINCT SISS.ITEM_SET_ID)
                            FROM STUDENT_ITEM_SET_STATUS SISS, ITEM_SET ISET
                           WHERE SISS.TEST_ROSTER_ID = TR.TEST_ROSTER_ID
                             AND ISET.ITEM_SET_ID = SISS.ITEM_SET_ID
                             AND ISET.ITEM_SET_LEVEL != 'L'
                             AND ISET.SAMPLE = 'F'
                             AND SISS.COMPLETION_STATUS NOT IN ('NT', 'SC'))
                     AND TR.ORG_NODE_ID = NODE.ORGNODEID
                     AND PROD.PRODUCT_ID = TADMIN.PRODUCT_ID
                     AND (PRODUCTLICENSE.PRODUCT_ID = PROD.PRODUCT_ID OR
                         PRODUCTLICENSE.PRODUCT_ID =
                         PROD.PARENT_PRODUCT_ID)
                     AND PROD.LICENSE_ENABLED = 'T';
                
                  RESERVEDLICENSECOUNT := SESSIONMODELCOUNT;
                  SESSIONMODELCOUNT    := 0;
                
                  --Calulate consumed license count  for session model based on customerId, orgnodeId and customer ProductId
                  SELECT COUNT(DISTINCT TR.TEST_ROSTER_ID)
                    INTO SESSIONMODELCOUNT
                    FROM TEST_ADMIN              TADMIN,
                         TEST_ROSTER             TR,
                         PRODUCT                 PROD,
                         STUDENT_ITEM_SET_STATUS SISS,
                         ITEM_SET                ISET
                  
                   WHERE TADMIN.CUSTOMER_ID = VCUSTOMERID
                     AND TR.TEST_ADMIN_ID = TADMIN.TEST_ADMIN_ID
                     AND TR.TEST_COMPLETION_STATUS NOT IN ('SC', 'NT')
                     AND TR.ORG_NODE_ID = NODE.ORGNODEID
                     AND PROD.PRODUCT_ID = TADMIN.PRODUCT_ID
                     AND (PRODUCTLICENSE.PRODUCT_ID = PROD.PRODUCT_ID OR
                         PRODUCTLICENSE.PRODUCT_ID =
                         PROD.PARENT_PRODUCT_ID)
                     AND PROD.LICENSE_ENABLED = 'T'
                     AND SISS.TEST_ROSTER_ID = TR.TEST_ROSTER_ID
                     AND ISET.ITEM_SET_ID = SISS.ITEM_SET_ID
                     AND ISET.ITEM_SET_LEVEL != 'L'
                     AND ISET.SAMPLE = 'F'
                     AND SISS.COMPLETION_STATUS NOT IN ('NT', 'SC');
                
                  CONSUMEDLICENSECOUNT := SESSIONMODELCOUNT;
                
                END IF;
              
                -- Insert record for leaf node
                INSERT INTO CUSTOMER_ORGNODE_LICENSE
                  (ORG_NODE_ID,
                   CUSTOMER_ID,
                   PRODUCT_ID,
                   AVAILABLE,
                   RESERVED,
                   CONSUMED,
                   SUBTEST_MODEL,
                   LICENSE_AFTER_LAST_PURCHASE,
                   EMAIL_NOTIFY_FLAG)
                VALUES
                  (NODE.ORGNODEID,
                   VCUSTOMERID,
                   PRODUCTLICENSE.PRODUCT_ID,
                   AVAILABLELIC,
                   RESERVEDLICENSECOUNT,
                   CONSUMEDLICENSECOUNT,
                   PRODUCTLICENSE.SUBTEST_MODEL,
                   PRODUCTLICENSE.LICENSE_AFTER_LAST_PURCHASE,
                   PRODUCTLICENSE.EMAIL_NOTIFY_FLAG);
              END IF;
            END IF;
          END LOOP;
        END IF;
      END LOOP;
    END IF;
  END LOOP;

END POPULATE_ORGNODELICENSE;
