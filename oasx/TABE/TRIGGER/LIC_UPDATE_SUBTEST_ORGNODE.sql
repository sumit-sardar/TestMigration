

/* Trigger: LIC_UPDATE_SUBTEST_ORGNODE */


CREATE OR REPLACE TRIGGER LIC_UPDATE_SUBTEST_ORGNODE
AFTER UPDATE
ON STUDENT_ITEM_SET_STATUS
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW
DECLARE
  SUBTESTCOUNT NUMBER;
  LICENSECOUNT NUMBER;
  PARENTID     NUMBER;

  DEADLOCK_DETECTED EXCEPTION;
  PRAGMA EXCEPTION_INIT(DEADLOCK_DETECTED, -60);
  RESOURCE_BUSY EXCEPTION;
  PRAGMA EXCEPTION_INIT(RESOURCE_BUSY, -54);

  -- retrieve product licensure information for the customer and product associated w/ this test roster, subtest
  -- we only need to do anything here if subtest pricing is in use.
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
           COL.PRODUCT_ID = PROD.PARENT_PRODUCT_ID)
       AND PROD.PRODUCT_ID = ADM.PRODUCT_ID
       AND COL.CUSTOMER_ID = ADM.CUSTOMER_ID
       AND PROD.LICENSE_ENABLED = 'T'
       FOR UPDATE OF AVAILABLE, RESERVED, CONSUMED WAIT 3;

BEGIN

  FOR COL IN COL_CURSOR LOOP
    -- determine whether this subtest is non-practice, non-locator
    SELECT COUNT(DISTINCT ISET.ITEM_SET_ID)
      INTO SUBTESTCOUNT
      FROM ITEM_SET ISET
     WHERE ISET.ITEM_SET_ID = :NEW.ITEM_SET_ID
       AND ISET.SAMPLE = 'F'
       AND ISET.ITEM_SET_LEVEL != 'L';
  
    -- we only have to handle consumption here, the session trigger on test roster handles reservation/dereservation.
    -- a license is consumed when the student takes each subtest.
    IF (:NEW.COMPLETION_STATUS NOT IN ('SC', 'NT') AND
       :OLD.COMPLETION_STATUS IN ('SC', 'NT')) THEN
      IF (COL.SUBTEST_MODEL = 'T') THEN
        UPDATE CUSTOMER_ORGNODE_LICENSE
           SET RESERVED = RESERVED - SUBTESTCOUNT,
               CONSUMED = CONSUMED + SUBTESTCOUNT
         WHERE CURRENT OF COL_CURSOR;
      ELSE
        -- for session model, note which item sets are taken, consume license on first 'real' item set
        SELECT COUNT(DISTINCT TSU.LICENSABLE_ITEM_SET_ID)
          INTO LICENSECOUNT
          FROM TEMP_SISS_UPDATE TSU
         WHERE TSU.TEST_ROSTER_ID = :NEW.TEST_ROSTER_ID;
        IF (LICENSECOUNT = 0 AND SUBTESTCOUNT > 0) THEN
          -- no previous item set delivered, consume
          UPDATE CUSTOMER_ORGNODE_LICENSE
             SET RESERVED = RESERVED - 1, CONSUMED = CONSUMED + 1
           WHERE CURRENT OF COL_CURSOR;
          SELECT ISP.PARENT_ITEM_SET_ID
            INTO PARENTID
            FROM ITEM_SET_PARENT ISP
           WHERE ITEM_SET_ID = :NEW.ITEM_SET_ID;
          INSERT INTO TEMP_SISS_UPDATE TSU
            (TEST_ROSTER_ID, LICENSABLE_ITEM_SET_ID, ITEM_SET_ID)
          VALUES
            (:NEW.TEST_ROSTER_ID, PARENTID, :NEW.ITEM_SET_ID);
        END IF;
      END IF;
    END IF;
  END LOOP;

EXCEPTION
  WHEN DEADLOCK_DETECTED THEN
    IF COL_CURSOR%ISOPEN THEN
      CLOSE COL_CURSOR;
    END IF;
  WHEN RESOURCE_BUSY THEN
    IF COL_CURSOR%ISOPEN THEN
      CLOSE COL_CURSOR;
    END IF;
  WHEN OTHERS THEN
    IF COL_CURSOR%ISOPEN THEN
      CLOSE COL_CURSOR;
    END IF;
    RAISE;
  
END

/
