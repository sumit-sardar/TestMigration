CREATE OR REPLACE TRIGGER "LIC_CREATE_SUBTEST_ORGNODE"
BEFORE INSERT
ON OAS.STUDENT_ITEM_SET_STATUS 
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW
DECLARE
  SUBTESTCOUNT        NUMBER;
  LICENSECOUNT        NUMBER;
  PARENTID            NUMBER;
  DATARECEIVEDFLAG    BOOLEAN := FALSE;
  IS_LAS_LICENSE_MGMT VARCHAR2(1);
  V_CUSTOMER_ID       INTEGER;

  CURSOR CC_CURSOR IS
    SELECT DECODE(COUNT(1), 0, 'F', 'T') AS ISLICENSECONFIGURABLE
      FROM TEST_ROSTER TR, TEST_ADMIN TA, CUSTOMER_CONFIGURATION CC
     WHERE DEFAULT_VALUE = 'T'
       AND CUSTOMER_CONFIGURATION_NAME = 'Allow_Subscription'
       AND CC.CUSTOMER_ID = TA.CUSTOMER_ID
       AND TA.TEST_ADMIN_ID = TR.TEST_ADMIN_ID
       AND TR.TEST_ROSTER_ID = :NEW.TEST_ROSTER_ID;

  CURSOR TL_CURSOR IS
    SELECT DECODE(COUNT(1), 0, 'F', 'T') AS ISTUTORLOC
      FROM TEST_ROSTER TR, TEST_ADMIN ADM, PRODUCT PROD
     WHERE TR.TEST_ROSTER_ID = :NEW.TEST_ROSTER_ID
       AND ADM.TEST_ADMIN_ID = TR.TEST_ADMIN_ID
       AND PROD.PRODUCT_ID = ADM.PRODUCT_ID
       AND PROD.LICENSE_ENABLED = 'F';

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
       AND ROS.TEST_ROSTER_ID = :NEW.TEST_ROSTER_ID
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
       FOR UPDATE OF AVAILABLE, RESERVED /* WAIT 3*/; -- Commented for defect#74612

BEGIN

  SELECT CUSTOMER_ID
    INTO V_CUSTOMER_ID
    FROM TEST_ROSTER
   WHERE TEST_ROSTER_ID = :NEW.TEST_ROSTER_ID;

  OPEN CUR_IS_LAS_LICENSE_MGMT(V_CUSTOMER_ID);
  FETCH CUR_IS_LAS_LICENSE_MGMT
    INTO IS_LAS_LICENSE_MGMT;
  IF CUR_IS_LAS_LICENSE_MGMT%NOTFOUND THEN
    IS_LAS_LICENSE_MGMT := 'F';
  END IF;
  CLOSE CUR_IS_LAS_LICENSE_MGMT;

  /*Logic for Laslink Starts*/
  IF IS_LAS_LICENSE_MGMT != 'F' THEN
    DATARECEIVEDFLAG := FALSE;
    FOR COL IN COL_CURSOR LOOP
      IF (COL.AVAILABLE < 1) THEN
        -- customer doesn't have a license available for registration, throw error to application
        RAISE_APPLICATION_ERROR(-20100,
                                'Insufficient available license quantity.');
      
      ELSE
        PKG_LAS_LM_TRG.SP_CREATE_SUBTEST_LAS_LM_TRG(COL.CUSTOMER_ID,
                                                    COL.ORG_NODE_ID,
                                                    COL.PRODUCT_ID,
                                                    COL.SUBTEST_MODEL,
                                                    :NEW.ITEM_SET_ID,
                                                    :NEW.TEST_ROSTER_ID);
        DATARECEIVEDFLAG := TRUE;
      END IF;
    END LOOP;
    IF (NOT DATARECEIVEDFLAG) THEN
      FOR TL IN TL_CURSOR LOOP
        IF (TL.ISTUTORLOC != 'F') THEN
          DATARECEIVEDFLAG := TRUE;
        ELSE
          -- customer doesn't have a license available for registration, throw error to application
          RAISE_APPLICATION_ERROR(-20100,
                                  'Insufficient available license quantity.');
        END IF;
      END LOOP;
    END IF;
  
    /*Logic other than Laslink Starts*/
  ELSE
  
    FOR CC IN CC_CURSOR LOOP
      IF (CC.ISLICENSECONFIGURABLE != 'F') THEN
        DATARECEIVEDFLAG := FALSE;
        FOR COL IN COL_CURSOR LOOP
          -- only reserve here if we're using subtest pricing model, otherwise a test_roster trigger will handle this
          IF (COL.SUBTEST_MODEL = 'T') THEN
            SELECT COUNT(DISTINCT ISET.ITEM_SET_ID)
              INTO SUBTESTCOUNT
              FROM ITEM_SET ISET
             WHERE ISET.ITEM_SET_ID = :NEW.ITEM_SET_ID
               AND ISET.SAMPLE = 'F'
               AND ISET.ITEM_SET_LEVEL != 'L';
          
            -- only perform reservation for non-locator, non-sample subtests
            IF (SUBTESTCOUNT > 0) THEN
              -- subtest is licensable, but check whether reservation has already been made for this TS
              SELECT ISP.PARENT_ITEM_SET_ID
                INTO PARENTID
                FROM ITEM_SET_PARENT ISP
               WHERE ITEM_SET_ID = :NEW.ITEM_SET_ID;
            
              SELECT COUNT(DISTINCT TSU.LICENSABLE_ITEM_SET_ID)
                INTO LICENSECOUNT
                FROM TEMP_SISS_UPDATE TSU
               WHERE TSU.TEST_ROSTER_ID = :NEW.TEST_ROSTER_ID
                 AND TSU.LICENSABLE_ITEM_SET_ID = PARENTID;
            
              IF (LICENSECOUNT = 0) THEN
                -- no previous reservation for this TS, make a reservation
                IF (COL.AVAILABLE < 1) THEN
                  -- customer doesn't have a license available for registration, throw error to application
                  RAISE_APPLICATION_ERROR(-20100,
                                          'Insufficient available license quantity.');
                END IF;
                -- reserve a license for this roster, subtest
                UPDATE CUSTOMER_ORGNODE_LICENSE
                   SET AVAILABLE = AVAILABLE - 1, RESERVED = RESERVED + 1
                 WHERE CURRENT OF COL_CURSOR;
              
              END IF;
              SELECT COUNT(DISTINCT TSU.ITEM_SET_ID)
                INTO LICENSECOUNT
                FROM TEMP_SISS_UPDATE TSU
               WHERE TSU.TEST_ROSTER_ID = :NEW.TEST_ROSTER_ID
                 AND TSU.ITEM_SET_ID = :NEW.ITEM_SET_ID;
              IF (LICENSECOUNT = 0) THEN
                INSERT INTO TEMP_SISS_UPDATE TSU
                  (TEST_ROSTER_ID, LICENSABLE_ITEM_SET_ID, ITEM_SET_ID)
                VALUES
                  (:NEW.TEST_ROSTER_ID, PARENTID, :NEW.ITEM_SET_ID);
              END IF;
            END IF;
          
          END IF;
          DATARECEIVEDFLAG := TRUE;
        END LOOP;
      
        IF (NOT DATARECEIVEDFLAG) THEN
          FOR TL IN TL_CURSOR LOOP
            IF (TL.ISTUTORLOC != 'F') THEN
              DATARECEIVEDFLAG := TRUE;
            ELSE
              -- customer doesn't have a license available for registration, throw error to application
              RAISE_APPLICATION_ERROR(-20100,
                                      'Insufficient available license quantity.');
            END IF;
          END LOOP;
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
