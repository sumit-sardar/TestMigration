CREATE OR REPLACE PACKAGE PKG_LAS_LM_TRG IS

  FUNCTION FN_GET_FRAMEWORK_PARENT_PROD(IN_PARENT_PRODUCT_ID INTEGER)
    RETURN INTEGER;

  FUNCTION FN_PO_ORDERINDEX_OLD_TO_NEW(V_CUSTOMER_ID INTEGER,
                                       V_ORG_NODE_ID INTEGER) RETURN INTEGER;

  FUNCTION FN_PO_ORDERINDEX_NEW_TO_OLD(V_CUSTOMER_ID INTEGER,
                                       V_ORG_NODE_ID INTEGER) RETURN INTEGER;

  FUNCTION FN_CHECK_PO_ORDER_EXIPY_STATUS(V_ORDER_INDEX INTEGER,
                                          V_CUSTOMER_ID INTEGER)
    RETURN VARCHAR2;

  FUNCTION FN_GET_PARENT_ITEM_SET_ID(IN_TD_ITEM_SET_ID INTEGER)
    RETURN INTEGER;

  FUNCTION FN_SUBTEST_TS_ITEM_RESERVED(IN_TEST_ROSTER_ID INTEGER,
                                       IN_TS_ITEM_SET_ID INTEGER)
    RETURN INTEGER;

  FUNCTION FN_SUBTEST_COUNT_FOR_ROSTER(IN_TEST_ROSTER_ID INTEGER)
    RETURN INTEGER;

  FUNCTION FN_RELEASE_LICENSE(IN_CUSTOMER_ID INTEGER,
                              IN_ORG_NODE_ID INTEGER,
                              IN_PRODUCT_ID  INTEGER,
                              V_SUBTESTCOUNT INTEGER) RETURN VARCHAR2;

  PROCEDURE SP_MANTAIN_AVAILABILTY(IN_TEST_ADMIN_ID INTEGER);

  PROCEDURE SP_MANTAIN_AVAILABILITY_JOB;

  PROCEDURE SP_CREATE_SESSION_LAS_LM_TRG(IN_CUSTOMER_ID   INTEGER,
                                         IN_ORG_NODE_ID   INTEGER,
                                         IN_PRODUCT_ID    INTEGER,
                                         IN_SUBTEST_MODEL VARCHAR2);

  PROCEDURE SP_CREATE_SUBTEST_LAS_LM_TRG(IN_CUSTOMER_ID    INTEGER,
                                         IN_ORG_NODE_ID    INTEGER,
                                         IN_PRODUCT_ID     INTEGER,
                                         IN_SUBTEST_MODEL  VARCHAR2,
                                         IN_TD_ITEM_SET_ID INTEGER,
                                         IN_TEST_ROSTER_ID INTEGER);

  PROCEDURE SP_DELETE_SESSION_LAS_LM_TRG(IN_CUSTOMER_ID                INTEGER,
                                         IN_TEST_ROSTER_ID             INTEGER,
                                         IN_ORG_NODE_ID                INTEGER,
                                         IN_OLD_TEST_COMPLETION_STATUS VARCHAR2,
                                         IN_PRODUCT_ID                 INTEGER,
                                         IN_SUBTEST_MODEL              VARCHAR2);

  PROCEDURE SP_DELETE_SUBTEST_LAS_LM_TRG(IN_CUSTOMER_ID    INTEGER,
                                         IN_ORG_NODE_ID    INTEGER,
                                         IN_PRODUCT_ID     INTEGER,
                                         IN_SUBTEST_MODEL  VARCHAR2,
                                         IN_TD_ITEM_SET_ID INTEGER,
                                         IN_TEST_ROSTER_ID INTEGER);

  PROCEDURE SP_UPDATE_SESSION_LAS_LM_TRG(IN_CUSTOMER_ID           INTEGER,
                                         IN_ORG_NODE_ID           INTEGER,
                                         IN_TEST_ROSTER_ID        INTEGER,
                                         IN_OLD_COMPLETION_STATUS VARCHAR2,
                                         IN_NEW_COMPLETION_STATUS VARCHAR2,
                                         IN_PRODUCT_ID            INTEGER,
                                         IN_SUBTEST_MODEL         VARCHAR2,
                                         IN_AVAILABLE             INTEGER,
                                         IN_RESERVED              INTEGER);

  PROCEDURE SP_UPDATE_SUBTEST_LAS_LM_TRG(IN_CUSTOMER_ID           INTEGER,
                                         IN_ORG_NODE_ID           INTEGER,
                                         IN_PRODUCT_ID            INTEGER,
                                         IN_SUBTEST_MODEL         VARCHAR2,
                                         IN_ITEM_SET_ID           INTEGER,
                                         IN_TEST_ROSTER_ID        INTEGER,
                                         IN_OLD_COMPLETION_STATUS VARCHAR2,
                                         IN_NEW_COMPLETION_STATUS VARCHAR2);
END PKG_LAS_LM_TRG;
/
CREATE OR REPLACE PACKAGE BODY PKG_LAS_LM_TRG IS

  /*This function is used to handle license logic for multiple product frameworks*/
  FUNCTION FN_GET_FRAMEWORK_PARENT_PROD(IN_PARENT_PRODUCT_ID INTEGER)
    RETURN INTEGER AS
    V_COUNT                INTEGER;
    V_FRAMEWORK_PRODUCT_ID INTEGER;
  
  BEGIN
  
    SELECT COUNT(FPP.FRAMEWORK_PARENT_PRODUCT_ID)
      INTO V_COUNT
      FROM FRAMEWORK_PRODUCT_PARENT FPP
     WHERE FPP.FRAMEWORK_PRODUCT_ID = IN_PARENT_PRODUCT_ID;
  
    IF V_COUNT <> 0 THEN
      SELECT FPP.FRAMEWORK_PARENT_PRODUCT_ID
        INTO V_FRAMEWORK_PRODUCT_ID
        FROM FRAMEWORK_PRODUCT_PARENT FPP
       WHERE FPP.FRAMEWORK_PRODUCT_ID = IN_PARENT_PRODUCT_ID;
    
      RETURN V_FRAMEWORK_PRODUCT_ID;
    
    ELSE
    
      RETURN - 1;
    
    END IF;
  
  END FN_GET_FRAMEWORK_PARENT_PROD;

  /*This function will get the order index associated with the leaf level node from which we have to deduct the available licenses.*/
  FUNCTION FN_PO_ORDERINDEX_OLD_TO_NEW(V_CUSTOMER_ID INTEGER,
                                       V_ORG_NODE_ID INTEGER) RETURN INTEGER IS
    V_ORDER_INDEX INTEGER := 0;
  
  BEGIN
    SELECT DECODE(ORDER_INDEX, NULL, -1, ORDER_INDEX)
      INTO V_ORDER_INDEX
      FROM (SELECT CPL.ORDER_INDEX AS ORDER_INDEX
              FROM CUSTOMER_PRODUCT_LICENSE CPL, ORGNODE_ORDER_LICENSE OOL
             WHERE CPL.CUSTOMER_ID = V_CUSTOMER_ID
               AND CPL.ORDER_INDEX = OOL.ORDER_INDEX
               AND OOL.ORG_NODE_ID = V_ORG_NODE_ID
               AND OOL.AVAILABLE > 0
             ORDER BY CPL.LICENSE_PERIOD_END, CPL.LICENSE_PERIOD_START) A
     WHERE ROWNUM = 1;
  
    IF V_ORDER_INDEX = -1 THEN
      RAISE_APPLICATION_ERROR(-20113, 'Order Index value not found..');
    END IF;
  
    RETURN V_ORDER_INDEX;
  
  EXCEPTION
    WHEN OTHERS THEN
      RAISE_APPLICATION_ERROR(-20115,
                              'Entry missing in Orgnode_order_license for the class org_node');
  END FN_PO_ORDERINDEX_OLD_TO_NEW;

  /*This function will get the order index associated with the leaf level node from which we have to deduct the available licenses.*/
  FUNCTION FN_PO_ORDERINDEX_NEW_TO_OLD(V_CUSTOMER_ID INTEGER,
                                       V_ORG_NODE_ID INTEGER) RETURN INTEGER IS
    V_ORDER_INDEX INTEGER := 0;
  
  BEGIN
  
    SELECT DECODE(ORDER_INDEX, NULL, -1, ORDER_INDEX)
      INTO V_ORDER_INDEX
      FROM (SELECT CPL.ORDER_INDEX AS ORDER_INDEX
              FROM CUSTOMER_PRODUCT_LICENSE CPL, ORGNODE_ORDER_LICENSE OOL
             WHERE CPL.CUSTOMER_ID = V_CUSTOMER_ID
               AND CPL.ORDER_INDEX = OOL.ORDER_INDEX
               AND OOL.ORG_NODE_ID = V_ORG_NODE_ID
               AND OOL.AVAILABLE < OOL.LAST_LICENSE_COUNT
             ORDER BY CPL.LICENSE_PERIOD_END   DESC,
                      CPL.LICENSE_PERIOD_START DESC) A
     WHERE ROWNUM = 1;
  
    IF V_ORDER_INDEX = -1 THEN
      RAISE_APPLICATION_ERROR(-20116, 'Order Index value not found..');
    END IF;
  
    RETURN V_ORDER_INDEX;
  
  EXCEPTION
    WHEN OTHERS THEN
      RAISE_APPLICATION_ERROR(-20117,
                              'Entry missing in Orgnode_order_license for the class org_node');
  END FN_PO_ORDERINDEX_NEW_TO_OLD;

  /*This Function checks whether the input order index is expired or not with respect to current date i.e. sysdate.
  Returns TRUE if Expired,else returns FALSE.*/
  FUNCTION FN_CHECK_PO_ORDER_EXIPY_STATUS(V_ORDER_INDEX INTEGER,
                                          V_CUSTOMER_ID INTEGER)
    RETURN VARCHAR2 IS
    PO_EXPIRY_DATE DATE := NULL;
  BEGIN
    SELECT CPL.LICENSE_PERIOD_END
      INTO PO_EXPIRY_DATE
      FROM CUSTOMER_PRODUCT_LICENSE CPL
     WHERE CPL.CUSTOMER_ID = V_CUSTOMER_ID
       AND CPL.ORDER_INDEX = V_ORDER_INDEX;
  
    IF (TRUNC(PO_EXPIRY_DATE) < TRUNC(SYSDATE)) THEN
      RETURN 'TRUE';
    ELSE
      RETURN 'FALSE';
    END IF;
  
  END FN_CHECK_PO_ORDER_EXIPY_STATUS;

  /*This function is to get the TS item_set_id of the TD item_set_id given as input.*/
  FUNCTION FN_GET_PARENT_ITEM_SET_ID(IN_TD_ITEM_SET_ID INTEGER)
    RETURN INTEGER IS
    V_TS_ITEM_SET_ID INTEGER := 0;
  BEGIN
  
    SELECT ISP.PARENT_ITEM_SET_ID
      INTO V_TS_ITEM_SET_ID
      FROM ITEM_SET_PARENT ISP
     WHERE ITEM_SET_ID = IN_TD_ITEM_SET_ID;
  
    RETURN V_TS_ITEM_SET_ID;
  END FN_GET_PARENT_ITEM_SET_ID;

  /*This function checks if a subtest license is already reserved for the said TS level item id. 
  returns : 1 = License already reserved.
            0 = License not reserved*/
  FUNCTION FN_SUBTEST_TS_ITEM_RESERVED(IN_TEST_ROSTER_ID INTEGER,
                                       IN_TS_ITEM_SET_ID INTEGER)
    RETURN INTEGER IS
  
    V_LICENSECOUNT INTEGER := 0;
  BEGIN
  
    SELECT COUNT(DISTINCT TSU.LICENSABLE_ITEM_SET_ID)
      INTO V_LICENSECOUNT
      FROM TEMP_SISS_UPDATE TSU
     WHERE TSU.TEST_ROSTER_ID = IN_TEST_ROSTER_ID
       AND TSU.LICENSABLE_ITEM_SET_ID = IN_TS_ITEM_SET_ID;
  
    RETURN V_LICENSECOUNT;
  END FN_SUBTEST_TS_ITEM_RESERVED;

  /*This function calculates the number of subtests for which license has to be 
  reserved.*/
  FUNCTION FN_SUBTEST_COUNT_FOR_ROSTER(IN_TEST_ROSTER_ID INTEGER)
    RETURN INTEGER IS
    V_SESSIONCOUNT INTEGER := NULL;
  BEGIN
    SELECT COUNT(DISTINCT ISET.ITEM_SET_ID)
      INTO V_SESSIONCOUNT
      FROM ITEM_SET ISET, STUDENT_ITEM_SET_STATUS SISS
     WHERE ISET.ITEM_SET_ID = SISS.ITEM_SET_ID
       AND SISS.TEST_ROSTER_ID = IN_TEST_ROSTER_ID
       AND ISET.SAMPLE = 'F'
       AND ISET.ITEM_SET_LEVEL != 'L'
       AND SISS.COMPLETION_STATUS IN ('CO', 'IP', 'IS', 'IN');
  
    RETURN V_SESSIONCOUNT;
  END FN_SUBTEST_COUNT_FOR_ROSTER;

  /*This function performs the update operation in OOL,COL tables in case of returning the licenses.*/
  FUNCTION FN_RELEASE_LICENSE(IN_CUSTOMER_ID INTEGER,
                              IN_ORG_NODE_ID INTEGER,
                              IN_PRODUCT_ID  INTEGER,
                              V_SUBTESTCOUNT INTEGER) RETURN VARCHAR2 IS
    V_ORDER_INDEX          INTEGER := NULL;
    V_OOL_AVAILABLE_COUNT  INTEGER := 0;
    V_OOL_LAST_COUNT       INTEGER := 0;
    V_OOL_DIFF             INTEGER := 0;
    V_REMAIN_SUBTEST_COUNT INTEGER := 0;
  
  BEGIN
  
    V_ORDER_INDEX := FN_PO_ORDERINDEX_NEW_TO_OLD(IN_CUSTOMER_ID,
                                                 IN_ORG_NODE_ID);
  
    IF FN_CHECK_PO_ORDER_EXIPY_STATUS(V_ORDER_INDEX, IN_CUSTOMER_ID) =
       'FALSE' THEN
    
      /*PO IS ACTIVE*/
      UPDATE CUSTOMER_ORGNODE_LICENSE COL
         SET COL.AVAILABLE = COL.AVAILABLE + V_SUBTESTCOUNT,
             COL.RESERVED  = COL.RESERVED - V_SUBTESTCOUNT
       WHERE COL.ORG_NODE_ID = IN_ORG_NODE_ID
         AND COL.CUSTOMER_ID = IN_CUSTOMER_ID
         AND COL.PRODUCT_ID = IN_PRODUCT_ID;
    
      SELECT OOL.AVAILABLE, OOL.LAST_LICENSE_COUNT
        INTO V_OOL_AVAILABLE_COUNT, V_OOL_LAST_COUNT
        FROM ORGNODE_ORDER_LICENSE OOL
       WHERE OOL.ORDER_INDEX = V_ORDER_INDEX
         AND OOL.ORG_NODE_ID = IN_ORG_NODE_ID;
    
      V_OOL_DIFF             := V_OOL_LAST_COUNT - V_OOL_AVAILABLE_COUNT;
      V_REMAIN_SUBTEST_COUNT := V_SUBTESTCOUNT - V_OOL_DIFF;
    
      IF (V_OOL_DIFF >= V_SUBTESTCOUNT) THEN
      
        UPDATE ORGNODE_ORDER_LICENSE OOL
           SET OOL.AVAILABLE = OOL.AVAILABLE + V_SUBTESTCOUNT
         WHERE OOL.ORDER_INDEX = V_ORDER_INDEX
           AND OOL.ORG_NODE_ID = IN_ORG_NODE_ID;
      ELSE
      
        UPDATE ORGNODE_ORDER_LICENSE OOL
           SET OOL.AVAILABLE = OOL.AVAILABLE + V_OOL_DIFF
         WHERE OOL.ORDER_INDEX = V_ORDER_INDEX
           AND OOL.ORG_NODE_ID = IN_ORG_NODE_ID;
      
        V_ORDER_INDEX := FN_PO_ORDERINDEX_NEW_TO_OLD(IN_CUSTOMER_ID,
                                                     IN_ORG_NODE_ID);
        UPDATE ORGNODE_ORDER_LICENSE OOL
           SET OOL.AVAILABLE = OOL.AVAILABLE + V_REMAIN_SUBTEST_COUNT
         WHERE OOL.ORDER_INDEX = V_ORDER_INDEX
           AND OOL.ORG_NODE_ID = IN_ORG_NODE_ID;
      
      END IF;
    
    ELSE
      /*PO has expired.*/
    
      UPDATE CUSTOMER_ORGNODE_LICENSE COL
         SET COL.RESERVED = COL.RESERVED - V_SUBTESTCOUNT
       WHERE COL.ORG_NODE_ID = IN_ORG_NODE_ID
         AND COL.CUSTOMER_ID = IN_CUSTOMER_ID
         AND COL.PRODUCT_ID = IN_PRODUCT_ID;
    
    END IF;
  
    RETURN 'TRUE';
  
  END FN_RELEASE_LICENSE;

  PROCEDURE SP_MANTAIN_AVAILABILTY(IN_TEST_ADMIN_ID INTEGER) IS
  
    V_CUSTOMER_ID       INTEGER := 0;
    IS_LAS_LICENSE_MGMT VARCHAR2(1);
  
    CURSOR CUR_ORG_NODE(V_CUSTOMER_ID INTEGER) IS
      SELECT COL.CUSTOMER_ID,
             COL.PRODUCT_ID,
             COL.SUBTEST_MODEL,
             COL.AVAILABLE,
             COL.ORG_NODE_ID
        FROM CUSTOMER_ORGNODE_LICENSE COL
       WHERE COL.CUSTOMER_ID = V_CUSTOMER_ID
         AND COL.AVAILABLE > COL.LICENSE_AFTER_LAST_PURCHASE;
  
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
  
  BEGIN
  
    SELECT CUSTOMER_ID
      INTO V_CUSTOMER_ID
      FROM TEST_ADMIN
     WHERE TEST_ADMIN_ID = IN_TEST_ADMIN_ID;
  
    OPEN CUR_IS_LAS_LICENSE_MGMT(V_CUSTOMER_ID);
    FETCH CUR_IS_LAS_LICENSE_MGMT
      INTO IS_LAS_LICENSE_MGMT;
    IF CUR_IS_LAS_LICENSE_MGMT%NOTFOUND THEN
      IS_LAS_LICENSE_MGMT := 'F';
    END IF;
    CLOSE CUR_IS_LAS_LICENSE_MGMT;
  
    IF IS_LAS_LICENSE_MGMT != 'F' THEN
      FOR REC IN CUR_ORG_NODE(V_CUSTOMER_ID) LOOP
      
        UPDATE CUSTOMER_ORGNODE_LICENSE COL
           SET COL.LICENSE_AFTER_LAST_PURCHASE = COL.AVAILABLE,
               COL.EMAIL_NOTIFY_FLAG           = 'T'
         WHERE COL.ORG_NODE_ID = REC.ORG_NODE_ID
           AND COL.CUSTOMER_ID = REC.CUSTOMER_ID
           AND COL.PRODUCT_ID = REC.PRODUCT_ID;
      
      END LOOP;
    END IF;
  
  END SP_MANTAIN_AVAILABILTY;

  PROCEDURE SP_MANTAIN_AVAILABILITY_JOB IS
  
    CURSOR CUR_COL IS
      SELECT COL.CUSTOMER_ID, COL.ORG_NODE_ID, COL.PRODUCT_ID
        FROM CUSTOMER_ORGNODE_LICENSE COL,
             (SELECT CUCON.CUSTOMER_ID
                FROM CUSTOMER_CONFIGURATION CUCON
               WHERE CUCON.CUSTOMER_CONFIGURATION_NAME =
                     'License_Yearly_Expiry'
                 AND EXISTS
               (SELECT 1
                        FROM CUSTOMER_CONFIGURATION CUCON1
                       WHERE CUCON1.CUSTOMER_ID = CUCON.CUSTOMER_ID
                         AND CUCON1.CUSTOMER_CONFIGURATION_NAME =
                             'Allow_Subscription')) DERIVED
       WHERE COL.CUSTOMER_ID = DERIVED.CUSTOMER_ID
         AND COL.AVAILABLE > COL.LICENSE_AFTER_LAST_PURCHASE;
  
  BEGIN
  
    FOR REC IN CUR_COL LOOP
    
      UPDATE CUSTOMER_ORGNODE_LICENSE COL
         SET COL.LICENSE_AFTER_LAST_PURCHASE = COL.AVAILABLE,
             COL.EMAIL_NOTIFY_FLAG           = 'T'
       WHERE COL.ORG_NODE_ID = REC.ORG_NODE_ID
         AND COL.CUSTOMER_ID = REC.CUSTOMER_ID
         AND COL.PRODUCT_ID = REC.PRODUCT_ID;
    
    END LOOP;
    
  END SP_MANTAIN_AVAILABILITY_JOB;

  /*This procedure is executed when a fresh entry is made into test roster table. i.e. a new test is scheduled */
  PROCEDURE SP_CREATE_SESSION_LAS_LM_TRG(IN_CUSTOMER_ID   INTEGER,
                                         IN_ORG_NODE_ID   INTEGER,
                                         IN_PRODUCT_ID    INTEGER,
                                         IN_SUBTEST_MODEL VARCHAR2) IS
  
    V_SUBTEST_MODEL VARCHAR2(2) := NULL;
    V_ORDER_INDEX   INTEGER := 0;
  
  BEGIN
    V_SUBTEST_MODEL := IN_SUBTEST_MODEL;
  
    /*We will be handling the license logic here if the licensing model is Session,otherwise trigger on SISS table will take care*/
    IF V_SUBTEST_MODEL != 'T' THEN
    
      V_ORDER_INDEX := FN_PO_ORDERINDEX_OLD_TO_NEW(IN_CUSTOMER_ID,
                                                   IN_ORG_NODE_ID);
    
      UPDATE ORGNODE_ORDER_LICENSE OOL
         SET OOL.AVAILABLE = OOL.AVAILABLE - 1
       WHERE OOL.ORDER_INDEX = V_ORDER_INDEX
         AND OOL.ORG_NODE_ID = IN_ORG_NODE_ID;
    
      UPDATE CUSTOMER_ORGNODE_LICENSE COL
         SET COL.AVAILABLE = COL.AVAILABLE - 1,
             COL.RESERVED  = COL.RESERVED + 1
       WHERE COL.ORG_NODE_ID = IN_ORG_NODE_ID
         AND COL.CUSTOMER_ID = IN_CUSTOMER_ID
         AND COL.PRODUCT_ID = IN_PRODUCT_ID;
    
      --  SP_MANTAIN_AVAILABILTY(IN_CUSTOMER_ID, IN_ORG_NODE_ID, IN_PRODUCT_ID);
    
    END IF; -- END OF SESSION MODEL LOGIC
  
  EXCEPTION
    WHEN OTHERS THEN
      RAISE_APPLICATION_ERROR(-20113,
                              'Session Mode License update failed..');
  END SP_CREATE_SESSION_LAS_LM_TRG;

  /*This procedure handles the logic for subtest model licensing during test scheduling.*/
  PROCEDURE SP_CREATE_SUBTEST_LAS_LM_TRG(IN_CUSTOMER_ID    INTEGER,
                                         IN_ORG_NODE_ID    INTEGER,
                                         IN_PRODUCT_ID     INTEGER,
                                         IN_SUBTEST_MODEL  VARCHAR2,
                                         IN_TD_ITEM_SET_ID INTEGER,
                                         IN_TEST_ROSTER_ID INTEGER) IS
    V_SUBTEST_MODEL VARCHAR2(2) := NULL;
    V_ORDER_INDEX   INTEGER := 0;
    V_SUBTESTCOUNT  INTEGER := 0;
    V_PARENT_TS_ID  INTEGER := 0;
    V_LICENSECOUNT  INTEGER := 0;
  
  BEGIN
    V_SUBTEST_MODEL := IN_SUBTEST_MODEL;
    IF V_SUBTEST_MODEL = 'T' THEN
      SELECT COUNT(DISTINCT ISET.ITEM_SET_ID)
        INTO V_SUBTESTCOUNT
        FROM ITEM_SET ISET
       WHERE ISET.ITEM_SET_ID = IN_TD_ITEM_SET_ID
         AND ISET.SAMPLE = 'F'
         AND ISET.ITEM_SET_LEVEL != 'L';
    
      IF V_SUBTESTCOUNT > 0 THEN
        V_PARENT_TS_ID := FN_GET_PARENT_ITEM_SET_ID(IN_TD_ITEM_SET_ID);
      
        IF FN_SUBTEST_TS_ITEM_RESERVED(IN_TEST_ROSTER_ID, V_PARENT_TS_ID) = 0 THEN
        
          V_ORDER_INDEX := FN_PO_ORDERINDEX_OLD_TO_NEW(IN_CUSTOMER_ID,
                                                       IN_ORG_NODE_ID);
        
          UPDATE ORGNODE_ORDER_LICENSE OOL
             SET OOL.AVAILABLE = OOL.AVAILABLE - 1
           WHERE OOL.ORDER_INDEX = V_ORDER_INDEX
             AND OOL.ORG_NODE_ID = IN_ORG_NODE_ID;
        
          UPDATE CUSTOMER_ORGNODE_LICENSE COL
             SET COL.AVAILABLE = COL.AVAILABLE - 1,
                 COL.RESERVED  = COL.RESERVED + 1
           WHERE COL.ORG_NODE_ID = IN_ORG_NODE_ID
             AND COL.CUSTOMER_ID = IN_CUSTOMER_ID
             AND COL.PRODUCT_ID = IN_PRODUCT_ID;
        
        END IF; -- END OF CHECKING IF LICENSE ALREADY RESERVED FOR PARENT TS ITEM ID
      
        SELECT COUNT(DISTINCT TSU.ITEM_SET_ID)
          INTO V_LICENSECOUNT
          FROM TEMP_SISS_UPDATE TSU
         WHERE TSU.TEST_ROSTER_ID = IN_TEST_ROSTER_ID
           AND TSU.ITEM_SET_ID = IN_TD_ITEM_SET_ID;
      
        IF (V_LICENSECOUNT = 0) THEN
          INSERT INTO TEMP_SISS_UPDATE TSU
            (TEST_ROSTER_ID, LICENSABLE_ITEM_SET_ID, ITEM_SET_ID)
          VALUES
            (IN_TEST_ROSTER_ID, V_PARENT_TS_ID, IN_TD_ITEM_SET_ID);
        END IF;
      END IF; -- END OF CHECKING IF LICENSE IS TO BE RESERVED FOR THE TD LEVEL ITEM ID   
    
      --SP_MANTAIN_AVAILABILTY(IN_CUSTOMER_ID, IN_ORG_NODE_ID, IN_PRODUCT_ID);
    
    END IF; -- END OF SUBTEST MODEL LOGIC
  EXCEPTION
    WHEN OTHERS THEN
      RAISE_APPLICATION_ERROR(-20114,
                              'Subtest Mode License update failed..');
  END SP_CREATE_SUBTEST_LAS_LM_TRG;

  /*This procedure handles the logic for session model licensing during deletion of tests.*/
  PROCEDURE SP_DELETE_SESSION_LAS_LM_TRG(IN_CUSTOMER_ID                INTEGER,
                                         IN_TEST_ROSTER_ID             INTEGER,
                                         IN_ORG_NODE_ID                INTEGER,
                                         IN_OLD_TEST_COMPLETION_STATUS VARCHAR2,
                                         IN_PRODUCT_ID                 INTEGER,
                                         IN_SUBTEST_MODEL              VARCHAR2) IS
    V_ORDER_INDEX INTEGER := 0;
  
  BEGIN
  
    DELETE FROM TEMP_SISS_UPDATE TSU
     WHERE TEST_ROSTER_ID = IN_TEST_ROSTER_ID;
  
    IF IN_OLD_TEST_COMPLETION_STATUS NOT IN ('IC', 'NT', 'CO') THEN
      IF IN_SUBTEST_MODEL != 'T' THEN
        V_ORDER_INDEX := FN_PO_ORDERINDEX_NEW_TO_OLD(IN_CUSTOMER_ID,
                                                     IN_ORG_NODE_ID);
      
        /*Now we will check if the order where the licenses are to be returned is expired or not,
        If Expired then we will only reduce the reserve count but available will not be increased.*/
        IF FN_CHECK_PO_ORDER_EXIPY_STATUS(V_ORDER_INDEX, IN_CUSTOMER_ID) =
           'FALSE' THEN
        
          UPDATE CUSTOMER_ORGNODE_LICENSE COL
             SET COL.AVAILABLE = COL.AVAILABLE + 1,
                 COL.RESERVED  = COL.RESERVED - 1
           WHERE COL.ORG_NODE_ID = IN_ORG_NODE_ID
             AND COL.CUSTOMER_ID = IN_CUSTOMER_ID
             AND COL.PRODUCT_ID = IN_PRODUCT_ID;
        
          UPDATE ORGNODE_ORDER_LICENSE OOL
             SET OOL.AVAILABLE = OOL.AVAILABLE + 1
           WHERE OOL.ORDER_INDEX = V_ORDER_INDEX
             AND OOL.ORG_NODE_ID = IN_ORG_NODE_ID;
        
        ELSE
          UPDATE CUSTOMER_ORGNODE_LICENSE COL
             SET COL.RESERVED = COL.RESERVED - 1
           WHERE COL.ORG_NODE_ID = IN_ORG_NODE_ID
             AND COL.CUSTOMER_ID = IN_CUSTOMER_ID
             AND COL.PRODUCT_ID = IN_PRODUCT_ID;
        
        END IF;
      
      END IF;
    END IF;
  
  EXCEPTION
    WHEN OTHERS THEN
      RAISE_APPLICATION_ERROR(-20115,
                              'Session mode license deletion failed..');
  END SP_DELETE_SESSION_LAS_LM_TRG;

  /*This procedure handles the logic for subtest model licensing during deletion of tests.*/
  PROCEDURE SP_DELETE_SUBTEST_LAS_LM_TRG(IN_CUSTOMER_ID    INTEGER,
                                         IN_ORG_NODE_ID    INTEGER,
                                         IN_PRODUCT_ID     INTEGER,
                                         IN_SUBTEST_MODEL  VARCHAR2,
                                         IN_TD_ITEM_SET_ID INTEGER,
                                         IN_TEST_ROSTER_ID INTEGER) IS
  
    V_ORDER_INDEX  INTEGER := 0;
    V_SUBTESTCOUNT INTEGER := 0;
    V_PARENT_TS_ID INTEGER := 0;
    V_LICENSECOUNT INTEGER := 0;
  BEGIN
  
    IF IN_SUBTEST_MODEL = 'T' THEN
    
      SELECT COUNT(DISTINCT ISET.ITEM_SET_ID)
        INTO V_SUBTESTCOUNT
        FROM ITEM_SET ISET
       WHERE ISET.ITEM_SET_ID = IN_TD_ITEM_SET_ID
         AND ISET.SAMPLE = 'F'
         AND ISET.ITEM_SET_LEVEL != 'L';
    
      IF V_SUBTESTCOUNT > 0 THEN
        V_PARENT_TS_ID := FN_GET_PARENT_ITEM_SET_ID(IN_TD_ITEM_SET_ID);
      
        SELECT COUNT(DISTINCT TSU.LICENSABLE_ITEM_SET_ID)
          INTO V_LICENSECOUNT
          FROM TEMP_SISS_UPDATE TSU
         WHERE TSU.TEST_ROSTER_ID = IN_TEST_ROSTER_ID
           AND TSU.LICENSABLE_ITEM_SET_ID = V_PARENT_TS_ID
           AND TSU.ITEM_SET_ID != IN_TD_ITEM_SET_ID;
      
        IF V_LICENSECOUNT = 0 THEN
          V_ORDER_INDEX := FN_PO_ORDERINDEX_NEW_TO_OLD(IN_CUSTOMER_ID,
                                                       IN_ORG_NODE_ID);
        
          IF FN_CHECK_PO_ORDER_EXIPY_STATUS(V_ORDER_INDEX, IN_CUSTOMER_ID) =
             'FALSE' THEN
          
            UPDATE CUSTOMER_ORGNODE_LICENSE COL
               SET COL.AVAILABLE = COL.AVAILABLE + 1,
                   COL.RESERVED  = COL.RESERVED - 1
             WHERE COL.ORG_NODE_ID = IN_ORG_NODE_ID
               AND COL.CUSTOMER_ID = IN_CUSTOMER_ID
               AND COL.PRODUCT_ID = IN_PRODUCT_ID;
          
            UPDATE ORGNODE_ORDER_LICENSE OOL
               SET OOL.AVAILABLE = OOL.AVAILABLE + 1
             WHERE OOL.ORDER_INDEX = V_ORDER_INDEX
               AND OOL.ORG_NODE_ID = IN_ORG_NODE_ID;
          
          ELSE
          
            UPDATE CUSTOMER_ORGNODE_LICENSE COL
               SET COL.RESERVED = COL.RESERVED - 1
             WHERE COL.ORG_NODE_ID = IN_ORG_NODE_ID
               AND COL.CUSTOMER_ID = IN_CUSTOMER_ID
               AND COL.PRODUCT_ID = IN_PRODUCT_ID;
          
          END IF;
        END IF;
        DELETE FROM TEMP_SISS_UPDATE TSU
         WHERE TEST_ROSTER_ID = IN_TEST_ROSTER_ID
           AND LICENSABLE_ITEM_SET_ID = V_PARENT_TS_ID
           AND ITEM_SET_ID = IN_TD_ITEM_SET_ID;
      END IF;
    
    END IF;
  EXCEPTION
    WHEN OTHERS THEN
      RAISE_APPLICATION_ERROR(-20115,
                              'Subtest mode license deletion failed..');
  END SP_DELETE_SUBTEST_LAS_LM_TRG;

  /*This procedure will be fired when test roster table is updated and
  session model license will be handled.*/
  PROCEDURE SP_UPDATE_SESSION_LAS_LM_TRG(IN_CUSTOMER_ID           INTEGER,
                                         IN_ORG_NODE_ID           INTEGER,
                                         IN_TEST_ROSTER_ID        INTEGER,
                                         IN_OLD_COMPLETION_STATUS VARCHAR2,
                                         IN_NEW_COMPLETION_STATUS VARCHAR2,
                                         IN_PRODUCT_ID            INTEGER,
                                         IN_SUBTEST_MODEL         VARCHAR2,
                                         IN_AVAILABLE             INTEGER,
                                         IN_RESERVED              INTEGER) IS
  
    V_SUBTESTCOUNT INTEGER := NULL;
    V_ORDER_INDEX  INTEGER := NULL;
    SESSIONCOUNT   INTEGER := NULL;
    V_STATUS       VARCHAR2(5) := NULL;
  
  BEGIN
  
    IF IN_SUBTEST_MODEL = 'T' THEN
      SELECT COUNT(DISTINCT TSU.LICENSABLE_ITEM_SET_ID)
        INTO V_SUBTESTCOUNT
        FROM ITEM_SET                ISET,
             STUDENT_ITEM_SET_STATUS SISS,
             TEMP_SISS_UPDATE        TSU
       WHERE ISET.ITEM_SET_ID = SISS.ITEM_SET_ID
         AND SISS.TEST_ROSTER_ID = IN_TEST_ROSTER_ID
         AND ISET.SAMPLE = 'F'
         AND ISET.ITEM_SET_LEVEL != 'L'
         AND SISS.COMPLETION_STATUS IN ('SC', 'NT')
         AND TSU.TEST_ROSTER_ID = IN_TEST_ROSTER_ID
         AND TSU.ITEM_SET_ID = SISS.ITEM_SET_ID;
    ELSE
      V_SUBTESTCOUNT := 1;
    END IF;
  
    -- If a test session is closed , then return the reserve license if PO is active
    IF (IN_RESERVED > 0 AND IN_NEW_COMPLETION_STATUS IN ('IC', 'NT') AND
       ((IN_SUBTEST_MODEL != 'T' AND IN_OLD_COMPLETION_STATUS = 'SC') OR
       (IN_SUBTEST_MODEL = 'T' AND
       IN_OLD_COMPLETION_STATUS NOT IN ('IC', 'NT')))) THEN
    
      V_STATUS := FN_RELEASE_LICENSE(IN_CUSTOMER_ID,
                                     IN_ORG_NODE_ID,
                                     IN_PRODUCT_ID,
                                     V_SUBTESTCOUNT);
    
    ELSE
      -- IF A TEST SESSION IS RE-OPENED, LICENSE QUANTITIES MUST BE RE-RESERVED
      IF (IN_OLD_COMPLETION_STATUS IN ('IC', 'NT') AND
         IN_NEW_COMPLETION_STATUS NOT IN ('IC', 'NT', 'CO')) THEN
        IF (IN_AVAILABLE < V_SUBTESTCOUNT) THEN
          -- CUSTOMER DOESN'T HAVE A LICENSE AVAILABLE FOR REGISTRATION, THROW ERROR TO APPLICATION
          RAISE_APPLICATION_ERROR(-20100,
                                  'INSUFFICIENT AVAILABLE LICENSE QUANTITY.');
        END IF;
      
        V_ORDER_INDEX := FN_PO_ORDERINDEX_OLD_TO_NEW(IN_CUSTOMER_ID,
                                                     IN_ORG_NODE_ID);
      
        UPDATE CUSTOMER_ORGNODE_LICENSE COL
           SET COL.AVAILABLE = COL.AVAILABLE - V_SUBTESTCOUNT,
               COL.RESERVED  = COL.RESERVED + V_SUBTESTCOUNT
         WHERE COL.ORG_NODE_ID = IN_ORG_NODE_ID
           AND COL.CUSTOMER_ID = IN_CUSTOMER_ID
           AND COL.PRODUCT_ID = IN_PRODUCT_ID;
      
        UPDATE ORGNODE_ORDER_LICENSE OOL
           SET OOL.AVAILABLE = OOL.AVAILABLE - V_SUBTESTCOUNT
         WHERE OOL.ORDER_INDEX = V_ORDER_INDEX
           AND OOL.ORG_NODE_ID = IN_ORG_NODE_ID;
      
        IF (IN_SUBTEST_MODEL != 'T') THEN
          -- REVERSE THE ABOVE RESERVATION IF THE LICENSE WAS ALREADY CONSUMED IN SESSION MODEL AND STAYED CONSUMED ON CLOSURE
          SESSIONCOUNT := FN_SUBTEST_COUNT_FOR_ROSTER(IN_TEST_ROSTER_ID);
        
          IF (SESSIONCOUNT != 0) THEN
            V_STATUS := FN_RELEASE_LICENSE(IN_CUSTOMER_ID,
                                           IN_ORG_NODE_ID,
                                           IN_PRODUCT_ID,
                                           V_SUBTESTCOUNT);
          END IF;
        END IF;
      ELSE
        -- IF A TEST SESSION IS CLOSED, AND A STUDENT HAS LOGGED IN BUT TAKEN ANY ACTUAL SUBTEST, RESERVED LICENSE SHOULD BE RELEASED.
        IF (IN_NEW_COMPLETION_STATUS = 'IC' AND IN_SUBTEST_MODEL != 'T' AND
           IN_OLD_COMPLETION_STATUS IN ('IP', 'IN', 'IS')) THEN
        
          SESSIONCOUNT := FN_SUBTEST_COUNT_FOR_ROSTER(IN_TEST_ROSTER_ID);
          IF (SESSIONCOUNT = 0) THEN
          
            V_STATUS := FN_RELEASE_LICENSE(IN_CUSTOMER_ID,
                                           IN_ORG_NODE_ID,
                                           IN_PRODUCT_ID,
                                           V_SUBTESTCOUNT);
          
          END IF;
        END IF;
      END IF;
    END IF;
  
  END SP_UPDATE_SESSION_LAS_LM_TRG;

  PROCEDURE SP_UPDATE_SUBTEST_LAS_LM_TRG(IN_CUSTOMER_ID           INTEGER,
                                         IN_ORG_NODE_ID           INTEGER,
                                         IN_PRODUCT_ID            INTEGER,
                                         IN_SUBTEST_MODEL         VARCHAR2,
                                         IN_ITEM_SET_ID           INTEGER,
                                         IN_TEST_ROSTER_ID        INTEGER,
                                         IN_OLD_COMPLETION_STATUS VARCHAR2,
                                         IN_NEW_COMPLETION_STATUS VARCHAR2) IS
  
    V_SUBTESTCOUNT INTEGER := NULL;
    V_LICENSECOUNT INTEGER := NULL;
    V_PARENT_TS_ID INTEGER := NULL;
  BEGIN
  
    SELECT COUNT(DISTINCT ISET.ITEM_SET_ID)
      INTO V_SUBTESTCOUNT
      FROM ITEM_SET ISET
     WHERE ISET.ITEM_SET_ID = IN_ITEM_SET_ID
       AND ISET.SAMPLE = 'F'
       AND ISET.ITEM_SET_LEVEL != 'L';
  
    IF (IN_NEW_COMPLETION_STATUS NOT IN ('SC', 'NT') AND
       IN_OLD_COMPLETION_STATUS IN ('SC', 'NT')) THEN
    
      IF (IN_SUBTEST_MODEL = 'T') THEN
      
        UPDATE CUSTOMER_ORGNODE_LICENSE COL
           SET COL.RESERVED = COL.RESERVED - V_SUBTESTCOUNT,
               COL.CONSUMED = COL.CONSUMED + V_SUBTESTCOUNT
         WHERE COL.ORG_NODE_ID = IN_ORG_NODE_ID
           AND COL.CUSTOMER_ID = IN_CUSTOMER_ID
           AND COL.PRODUCT_ID = IN_PRODUCT_ID;
      
      ELSE
        SELECT COUNT(DISTINCT TSU.LICENSABLE_ITEM_SET_ID)
          INTO V_LICENSECOUNT
          FROM TEMP_SISS_UPDATE TSU
         WHERE TSU.TEST_ROSTER_ID = IN_TEST_ROSTER_ID;
      
        IF (V_LICENSECOUNT = 0 AND V_SUBTESTCOUNT > 0) THEN
        
          UPDATE CUSTOMER_ORGNODE_LICENSE COL
             SET COL.RESERVED = COL.RESERVED - 1,
                 COL.CONSUMED = COL.CONSUMED + 1
           WHERE COL.ORG_NODE_ID = IN_ORG_NODE_ID
             AND COL.CUSTOMER_ID = IN_CUSTOMER_ID
             AND COL.PRODUCT_ID = IN_PRODUCT_ID;
        
          V_PARENT_TS_ID := FN_GET_PARENT_ITEM_SET_ID(IN_ITEM_SET_ID);
        
          INSERT INTO TEMP_SISS_UPDATE TSU
            (TEST_ROSTER_ID, LICENSABLE_ITEM_SET_ID, ITEM_SET_ID)
          VALUES
            (IN_TEST_ROSTER_ID, V_PARENT_TS_ID, IN_ITEM_SET_ID);
        
        END IF;
      END IF;
    
    END IF;
  
  END SP_UPDATE_SUBTEST_LAS_LM_TRG;

END PKG_LAS_LM_TRG;
/
