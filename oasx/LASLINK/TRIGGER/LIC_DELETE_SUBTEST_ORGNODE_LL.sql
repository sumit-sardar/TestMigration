CREATE OR REPLACE TRIGGER "LIC_DELETE_SUBTEST_ORGNODE"
BEFORE DELETE
ON OAS.STUDENT_ITEM_SET_STATUS 
REFERENCING OLD AS OLD
FOR EACH ROW
DECLARE
  SUBTESTCOUNT        NUMBER;
  LICENSECOUNT        NUMBER;
  PARENTID            NUMBER;
  IS_LAS_LICENSE_MGMT VARCHAR2(1);
  V_CUSTOMER_ID       INTEGER;

  CURSOR CUR_IS_LAS_LICENSE_MGMT(V_CUSTOMER_ID INTEGER) IS
    SELECT DECODE(COUNT(1), 0, 'F', 'T') AS IS_LAS_LICENSE_MGMT
      FROM CUSTOMER_CONFIGURATION
     WHERE CUSTOMER_ID = V_CUSTOMER_ID
       AND CUSTOMER_CONFIGURATION_NAME = 'License_Yearly_Expiry'
       AND EXISTS
     (SELECT 1
              FROM CUSTOMER_CONFIGURATION
             WHERE CUSTOMER_ID = V_CUSTOMER_ID
               AND CUSTOMER_CONFIGURATION_NAME = 'Allow_Subscription');

  -- retrieve product licensure information for the customer and product associated w/ this test roster, subtest
  CURSOR COL_CURSOR IS
    SELECT COL.CUSTOMER_ID,
           COL.PRODUCT_ID,
           COL.SUBTEST_MODEL,
           COL.AVAILABLE,
           COL.ORG_NODE_ID
      FROM TEST_ROSTER              ROS,
           TEST_ADMIN               ADM,
           CUSTOMER_ORGNODE_LICENSE COL,
           PRODUCT                  PROD
     WHERE ADM.TEST_ADMIN_ID = ROS.TEST_ADMIN_ID
       AND ROS.TEST_ROSTER_ID = :OLD.TEST_ROSTER_ID
       AND COL.ORG_NODE_ID = ROS.ORG_NODE_ID
       AND (COL.PRODUCT_ID = PROD.PRODUCT_ID OR
           COL.PRODUCT_ID = PROD.PARENT_PRODUCT_ID OR
           COL.PRODUCT_ID =
           (SELECT FPP.FRAMEWORK_PARENT_PRODUCT_ID
               FROM FRAMEWORK_PRODUCT_PARENT FPP
              WHERE FPP.FRAMEWORK_PRODUCT_ID = PROD.PARENT_PRODUCT_ID))
       AND PROD.PRODUCT_ID = ADM.PRODUCT_ID
       AND COL.CUSTOMER_ID = ADM.CUSTOMER_ID
       AND PROD.LICENSE_ENABLED = 'T'
       AND ROS.TEST_COMPLETION_STATUS NOT IN ('IC', 'NT', 'CO')
       FOR UPDATE OF AVAILABLE, RESERVED/* WAIT 3*/;-- Commented for defect#74612

BEGIN
 
  SELECT CUSTOMER_ID
    INTO V_CUSTOMER_ID
    FROM TEST_ROSTER
   WHERE TEST_ROSTER_ID = :OLD.TEST_ROSTER_ID;

  OPEN CUR_IS_LAS_LICENSE_MGMT(V_CUSTOMER_ID);
  FETCH CUR_IS_LAS_LICENSE_MGMT
    INTO IS_LAS_LICENSE_MGMT;
  IF CUR_IS_LAS_LICENSE_MGMT%NOTFOUND THEN
    IS_LAS_LICENSE_MGMT := 'F';
  END IF;
  CLOSE CUR_IS_LAS_LICENSE_MGMT;

  IF IS_LAS_LICENSE_MGMT != 'F' THEN
  
    FOR COL IN COL_CURSOR LOOP
      PKG_LAS_LM_TRG.SP_DELETE_SUBTEST_LAS_LM_TRG(COL.CUSTOMER_ID,
                                                  COL.ORG_NODE_ID,
                                                  COL.PRODUCT_ID,
                                                  COL.SUBTEST_MODEL,
                                                  :OLD.ITEM_SET_ID,
                                                  :OLD.TEST_ROSTER_ID);
    
    END LOOP;
  ELSE
    FOR COL IN COL_CURSOR LOOP
      -- only release here if we're using subtest pricing model, otherwise a test_roster trigger will handle this
      IF (COL.SUBTEST_MODEL = 'T') THEN
        SELECT COUNT(DISTINCT ISET.ITEM_SET_ID)
          INTO SUBTESTCOUNT
          FROM ITEM_SET ISET
         WHERE ISET.ITEM_SET_ID = :OLD.ITEM_SET_ID
           AND ISET.SAMPLE = 'F'
           AND ISET.ITEM_SET_LEVEL != 'L';
      
        -- only perform release for non-locator, non-sample subtests
        IF (SUBTESTCOUNT > 0) THEN
          -- subtest is licensable, but check whether all scheduled subtests for this TS have been removed before releasing license
          SELECT ISP.PARENT_ITEM_SET_ID
            INTO PARENTID
            FROM ITEM_SET_PARENT ISP
           WHERE ITEM_SET_ID = :OLD.ITEM_SET_ID;
        
          SELECT COUNT(DISTINCT TSU.LICENSABLE_ITEM_SET_ID)
            INTO LICENSECOUNT
            FROM TEMP_SISS_UPDATE TSU
           WHERE TSU.TEST_ROSTER_ID = :OLD.TEST_ROSTER_ID
             AND TSU.LICENSABLE_ITEM_SET_ID = PARENTID
             AND TSU.ITEM_SET_ID != :OLD.ITEM_SET_ID;
        
          IF (LICENSECOUNT = 0) THEN
            -- release a license for this roster, subtest
            UPDATE CUSTOMER_ORGNODE_LICENSE
               SET AVAILABLE = AVAILABLE + 1, RESERVED = RESERVED - 1
             WHERE CURRENT OF COL_CURSOR;
          END IF;
          DELETE FROM TEMP_SISS_UPDATE TSU
           WHERE TEST_ROSTER_ID = :OLD.TEST_ROSTER_ID
             AND LICENSABLE_ITEM_SET_ID = PARENTID
             AND ITEM_SET_ID = :OLD.ITEM_SET_ID;
        END IF;
      END IF;
    END LOOP;
  END IF;
EXCEPTION
  WHEN OTHERS THEN
    IF COL_CURSOR%ISOPEN THEN
      CLOSE COL_CURSOR;
    END IF;
    RAISE;
END;
/
