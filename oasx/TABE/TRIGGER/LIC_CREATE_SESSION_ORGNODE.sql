

/* Trigger: LIC_CREATE_SESSION_ORGNODE */


CREATE OR REPLACE TRIGGER "LIC_CREATE_SESSION_ORGNODE"
AFTER INSERT
ON OAS.TEST_ROSTER 
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW
DECLARE

  DATARECEIVEDFLAG BOOLEAN := FALSE;

  CURSOR CC_CURSOR IS
    SELECT DECODE(COUNT(1), 0, 'F', 'T') AS ISLICENSECONFIGURABLE
      FROM CUSTOMER_CONFIGURATION
     WHERE CUSTOMER_ID = :NEW.CUSTOMER_ID
       AND CUSTOMER_CONFIGURATION_NAME = 'Allow_Subscription'
       AND DEFAULT_VALUE = 'T';

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
           COL.PRODUCT_ID = PROD.PARENT_PRODUCT_ID)
       AND PROD.PRODUCT_ID = ADM.PRODUCT_ID
       AND COL.CUSTOMER_ID = ADM.CUSTOMER_ID
       AND PROD.LICENSE_ENABLED = 'T'
       FOR UPDATE OF AVAILABLE, RESERVED wait 3;

BEGIN

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
        -- customer doesn't have a license available for registration, throw error to application
        RAISE_APPLICATION_ERROR(-20100,
                                'Insufficient available license quantity.');
      END IF;
    END IF;
  END LOOP;

EXCEPTION
  WHEN OTHERS THEN
    IF COL_CURSOR%ISOPEN THEN
      CLOSE COL_CURSOR;
    END IF;
    RAISE;
  
END

/
