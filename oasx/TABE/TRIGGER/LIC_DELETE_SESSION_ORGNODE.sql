



/* LIC_DELETE_SESSION_ORGNODE */



CREATE OR REPLACE TRIGGER "LIC_DELETE_SESSION_ORGNODE"
BEFORE DELETE
ON OAS.TEST_ROSTER 
REFERENCING OLD AS OLD
FOR EACH ROW
DECLARE


  -- retrieve product licensure information for the customer and product associated w/ this test roster
  CURSOR COL_CURSOR IS
    SELECT COL.CUSTOMER_ID,
           COL.PRODUCT_ID,
           COL.SUBTEST_MODEL,
           COL.AVAILABLE
      FROM TEST_ADMIN ADM, CUSTOMER_ORGNODE_LICENSE COL, PRODUCT PROD
     WHERE ADM.TEST_ADMIN_ID = :OLD.TEST_ADMIN_ID
       AND COL.ORG_NODE_ID = :OLD.ORG_NODE_ID
       AND (COL.PRODUCT_ID = PROD.PRODUCT_ID OR
           COL.PRODUCT_ID = PROD.PARENT_PRODUCT_ID)
       AND PROD.PRODUCT_ID = ADM.PRODUCT_ID
       AND COL.CUSTOMER_ID = ADM.CUSTOMER_ID
       AND PROD.LICENSE_ENABLED = 'T'
       FOR UPDATE OF AVAILABLE, RESERVED wait 3;

BEGIN

  DELETE FROM TEMP_SISS_UPDATE TSU
   WHERE TEST_ROSTER_ID = :OLD.TEST_ROSTER_ID;

  IF (:OLD.TEST_COMPLETION_STATUS NOT IN ('IC', 'NT', 'CO')) THEN
    FOR COL IN COL_CURSOR LOOP
      -- ONLY RELEASE HERE IF WE'RE USING SESSION PRICING MODEL, OTHERWISE AN SISS TRIGGER WILL HANDLE THIS
      IF (COL.SUBTEST_MODEL != 'T') THEN
        -- RELEASE A LICENSE FOR THIS ROSTER
        UPDATE CUSTOMER_ORGNODE_LICENSE
           SET AVAILABLE = AVAILABLE + 1, RESERVED = RESERVED - 1
         WHERE CURRENT OF COL_CURSOR;
      END IF;
    END LOOP;
  END IF;

EXCEPTION
  WHEN OTHERS THEN
    IF COL_CURSOR%ISOPEN THEN
      CLOSE COL_CURSOR;
    END IF;
    RAISE;
END

/