CREATE OR REPLACE TRIGGER "LIC_CREATE_SESSION_ORGNODE"
AFTER INSERT
ON OAS.TEST_ROSTER 
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW
DECLARE

  DATARECEIVEDFLAG    BOOLEAN := FALSE;
  IS_LAS_LICENSE_MGMT VARCHAR2(1);

  CURSOR CC_CURSOR IS
    SELECT DECODE(COUNT(1), 0, 'F', 'T') AS ISLICENSECONFIGURABLE
      FROM CUSTOMER_CONFIGURATION
     WHERE CUSTOMER_ID = :NEW.CUSTOMER_ID
       AND CUSTOMER_CONFIGURATION_NAME = 'Allow_Subscription'
       AND DEFAULT_VALUE = 'T';

  CURSOR TL_CURSOR IS
    SELECT DECODE(COUNT(1), 0, 'F', 'T') AS ISTUTORLOC
      FROM TEST_ADMIN ADM, PRODUCT PROD
     WHERE ADM.TEST_ADMIN_ID = :NEW.TEST_ADMIN_ID
       AND PROD.PRODUCT_ID = ADM.PRODUCT_ID
       AND PROD.LICENSE_ENABLED = 'F';

  CURSOR CUR_IS_LAS_LICENSE_MGMT IS
    SELECT DECODE(COUNT(1), 0, 'F', 'T') AS IS_LAS_LICENSE_MGMT
      FROM CUSTOMER_CONFIGURATION
     WHERE CUSTOMER_ID = :NEW.CUSTOMER_ID
       AND CUSTOMER_CONFIGURATION_NAME = 'License_Yearly_Expiry'
       AND EXISTS
     (SELECT 1
              FROM CUSTOMER_CONFIGURATION
             WHERE CUSTOMER_ID = :NEW.CUSTOMER_ID
               AND CUSTOMER_CONFIGURATION_NAME = 'Allow_Subscription');

  CURSOR COL_CURSOR IS
    SELECT COL.CUSTOMER_ID,
           COL.PRODUCT_ID,
           COL.SUBTEST_MODEL,
           COL.AVAILABLE,
           COL.ORG_NODE_ID
      FROM TEST_ADMIN ADM, CUSTOMER_ORGNODE_LICENSE COL, PRODUCT PROD
     WHERE ADM.TEST_ADMIN_ID = :NEW.TEST_ADMIN_ID
       AND COL.ORG_NODE_ID = :NEW.ORG_NODE_ID
       AND (COL.PRODUCT_ID = PROD.PRODUCT_ID OR
           COL.PRODUCT_ID = PROD.PARENT_PRODUCT_ID OR
           COL.PRODUCT_ID =
           (SELECT FPP.FRAMEWORK_PARENT_PRODUCT_ID
               FROM FRAMEWORK_PRODUCT_PARENT FPP
              WHERE FPP.FRAMEWORK_PRODUCT_ID = PROD.PARENT_PRODUCT_ID))
       AND PROD.PRODUCT_ID = ADM.PRODUCT_ID
       AND COL.CUSTOMER_ID = ADM.CUSTOMER_ID
       AND PROD.LICENSE_ENABLED = 'T'
       FOR UPDATE OF AVAILABLE, RESERVED/* WAIT 3*/;-- Commented for defect#74612

BEGIN

  OPEN CUR_IS_LAS_LICENSE_MGMT;
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
        PKG_LAS_LM_TRG.SP_CREATE_SESSION_LAS_LM_TRG(:NEW.CUSTOMER_ID,
                                                    :NEW.ORG_NODE_ID,
                                                    COL.PRODUCT_ID,
                                                    COL.SUBTEST_MODEL);
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
          -- only reserve here if we're using session pricing model, otherwise an SISS trigger will handle this
          IF (COL.SUBTEST_MODEL != 'T') THEN
            IF (COL.AVAILABLE < 1) THEN
              -- customer doesn't have a license available for registration, throw error to application
              RAISE_APPLICATION_ERROR(-20100,
                                      'Insufficient available license quantity.');
            END IF;
            -- reserve a license for this roster
            UPDATE CUSTOMER_ORGNODE_LICENSE
               SET AVAILABLE = AVAILABLE - 1, RESERVED = RESERVED + 1
             WHERE CURRENT OF COL_CURSOR;
          
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
