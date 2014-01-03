CREATE OR REPLACE PROCEDURE UPDATELICENSEDATAFORLASLINK(INPUT_CUSTOMER_ID INTEGER,
                                                        INPUT_PRODUCT_ID  INTEGER,
                                                        INPUT_ORG_NODE_ID INTEGER,
                                                        INPUT_AVAILABLE   INTEGER) IS

  /** VARIABLE Declarations :: **/
  IS_LAS_LICENSE_MGMT           VARCHAR2(1);
  IS_LAS_TOP_NODE               VARCHAR2(10) := 'FALSE';
  IS_CHILD_OF_TOPNODE           VARCHAR2(10) := 'FALSE';
  IS_LEAF_NODE                  VARCHAR2(10) := 'FALSE';
  REC_CURRENT_NODE              CUSTOMER_ORGNODE_LICENSE%ROWTYPE;
  V_NEW_AVAILABLE_COUNT         NUMBER;
  V_OLD_AVAILABLE_COUNT         NUMBER;
  V_PARENT_NODE_ID              NUMBER;
  V_LC_DIFF                     NUMBER;
  V_INCREMENT_LC                NUMBER;
  V_AVAILABLE                   NUMBER;
  V_LICENSE_AFTER_LAST_PURCHASE NUMBER;
  V_AVAILABLE_CPL               NUMBER;
  V_LIC_AFTER_LAST_PURCHASE_CPL NUMBER;

  /** CURSOR Declarations :: **/
  CURSOR CUR_IS_LAS_LICENSE_MGMT IS
    SELECT DECODE(COUNT(1), 0, 'F', 'T') AS IS_LAS_LICENSE_MGMT
      FROM CUSTOMER_CONFIGURATION
     WHERE CUSTOMER_ID = INPUT_CUSTOMER_ID
       AND CUSTOMER_CONFIGURATION_NAME = 'License_Yearly_Expiry';

  CURSOR GET_OLD_COL_DATA_FOR_ORG(V_ORG_NODE_ID CUSTOMER_ORGNODE_LICENSE.ORG_NODE_ID%TYPE) IS
    SELECT *
      FROM CUSTOMER_ORGNODE_LICENSE
     WHERE ORG_NODE_ID = V_ORG_NODE_ID
       AND CUSTOMER_ID = INPUT_CUSTOMER_ID
       AND PRODUCT_ID = INPUT_PRODUCT_ID;

  -- **need to verify data is proper or not
  CURSOR GET_NEW_TO_OLD_PO_FOR_ORG(V_ORG_NODE_ID ORGNODE_ORDER_LICENSE.ORG_NODE_ID%TYPE) IS
    SELECT OOL.*
      FROM ORGNODE_ORDER_LICENSE OOL, CUSTOMER_PRODUCT_LICENSE CPL
     WHERE OOL.ORDER_INDEX = CPL.ORDER_INDEX
       AND OOL.ORG_NODE_ID = V_ORG_NODE_ID
       AND OOL.AVAILABLE > 0
       AND CPL.CUSTOMER_ID = INPUT_CUSTOMER_ID
       AND CPL.PRODUCT_ID = INPUT_PRODUCT_ID
       AND TRUNC(CPL.LICENSE_PERIOD_END) >= TRUNC(SYSDATE)
     ORDER BY TRUNC(CPL.LICENSE_PERIOD_END) DESC,
              TRUNC(CPL.LICENSE_PERIOD_START) DESC,
              CPL.AVAILABLE DESC;
  --ORDER BY CPL.LICENSE_PERIOD_END, CPL.LICENSE_PERIOD_START, CPL.AVAILABLE DESC;

  -- **need to verify data is proper or not
  CURSOR GET_OLD_TO_NEW_PO_FOR_PARENT(V_ORG_NODE_ID ORGNODE_ORDER_LICENSE.ORG_NODE_ID%TYPE) IS
    SELECT OOL.*
      FROM ORGNODE_ORDER_LICENSE OOL, CUSTOMER_PRODUCT_LICENSE CPL
     WHERE OOL.ORDER_INDEX = CPL.ORDER_INDEX
       AND OOL.ORG_NODE_ID = V_ORG_NODE_ID
       AND OOL.AVAILABLE > 0
       AND CPL.PRODUCT_ID = INPUT_PRODUCT_ID
       AND CPL.CUSTOMER_ID = INPUT_CUSTOMER_ID
       AND TRUNC(CPL.LICENSE_PERIOD_END) >= TRUNC(SYSDATE)
     ORDER BY CPL.LICENSE_PERIOD_END   ASC,
              CPL.LICENSE_PERIOD_START ASC,
              CPL.AVAILABLE            ASC;

  /** FUNCTION Declarations :: **/
  -- checks whether the node is top node or not
  FUNCTION CHECK_FOR_TOP_NODE(V_ORG_NODE_ID CUSTOMER_ORGNODE_LICENSE.ORG_NODE_ID%TYPE)
    RETURN VARCHAR2 IS
    IS_TOP_NODE VARCHAR2(10);
  BEGIN
    SELECT DECODE(COUNT(1), 0, 'FALSE', 'TRUE')
      INTO IS_TOP_NODE
      FROM ORG_NODE ORG, ORG_NODE_CATEGORY ONC
     WHERE ORG.ORG_NODE_CATEGORY_ID = ONC.ORG_NODE_CATEGORY_ID
       AND ONC.CATEGORY_LEVEL = 1
       AND ORG.ORG_NODE_ID = V_ORG_NODE_ID
       AND ORG.CUSTOMER_ID = INPUT_CUSTOMER_ID;
  
    RETURN IS_TOP_NODE;
  END;

  -- checks whether the node is leaf node or not
  FUNCTION CHECK_FOR_LEAF_NODE(V_ORG_NODE_ID CUSTOMER_ORGNODE_LICENSE.ORG_NODE_ID%TYPE)
    RETURN VARCHAR2 IS
    V_IS_TOP_NODE VARCHAR2(10);
  BEGIN
    SELECT DECODE(COUNT(1), 0, 'FALSE', 'TRUE')
      INTO V_IS_TOP_NODE
      FROM ORG_NODE ORG, ORG_NODE_CATEGORY ONC
     WHERE ORG.ORG_NODE_CATEGORY_ID = ONC.ORG_NODE_CATEGORY_ID
       AND ONC.CATEGORY_LEVEL =
           (SELECT MAX(CATEGORY_LEVEL)
              FROM ORG_NODE_CATEGORY
             WHERE CUSTOMER_ID = INPUT_CUSTOMER_ID
               AND ACTIVATION_STATUS = 'AC')
       AND ORG.ORG_NODE_ID = V_ORG_NODE_ID
       AND ORG.CUSTOMER_ID = INPUT_CUSTOMER_ID;
    RETURN V_IS_TOP_NODE;
  END;

  -- checks whether the node is immediate child of top node or not
  FUNCTION IS_IMMEDIATE_CHILD_OF_TOPNODE(V_ORG_NODE_ID CUSTOMER_ORGNODE_LICENSE.ORG_NODE_ID%TYPE)
    RETURN VARCHAR2 IS
    IS_IMMEDIATE_CHILD VARCHAR2(10);
  BEGIN
    SELECT DECODE(COUNT(1), 0, 'FALSE', 'TRUE')
      INTO IS_IMMEDIATE_CHILD
      FROM ORG_NODE ORG, ORG_NODE_CATEGORY ONC
     WHERE ORG.ORG_NODE_CATEGORY_ID = ONC.ORG_NODE_CATEGORY_ID
       AND ONC.CATEGORY_LEVEL = 2
       AND ORG.ORG_NODE_ID = V_ORG_NODE_ID
       AND ORG.CUSTOMER_ID = INPUT_CUSTOMER_ID;
  
    RETURN IS_IMMEDIATE_CHILD;
  END;

  -- get parent node 
  FUNCTION FETCH_PARENT_NODE_ID(V_ORG_NODE_ID ORG_NODE.ORG_NODE_ID%TYPE)
    RETURN NUMBER IS
    V_PARENT_NODE_ID NUMBER;
  BEGIN
    SELECT PARENT_ORG_NODE_ID
      INTO V_PARENT_NODE_ID
      FROM ORG_NODE_PARENT
     WHERE ORG_NODE_ID = V_ORG_NODE_ID;
  
    RETURN V_PARENT_NODE_ID;
  END;

  -- checks whether there is an entry of the PO for the org in OOL  
  FUNCTION IS_OOL_ENTRY_EXIST_FOR_PO_ORG(V_ORG_NODE_ID ORGNODE_ORDER_LICENSE.ORG_NODE_ID%TYPE,
                                         V_ORDER_INDEX ORGNODE_ORDER_LICENSE.ORDER_INDEX%TYPE)
    RETURN VARCHAR2 IS
    IS_EXIST VARCHAR2(10);
  BEGIN
    SELECT DECODE(COUNT(1), 0, 'FALSE', 'TRUE')
      INTO IS_EXIST
      FROM ORGNODE_ORDER_LICENSE
     WHERE ORG_NODE_ID = V_ORG_NODE_ID
       AND ORDER_INDEX = V_ORDER_INDEX;
  
    RETURN IS_EXIST;
  END;

BEGIN
  DBMS_OUTPUT.PUT_LINE('Proc called');
  OPEN CUR_IS_LAS_LICENSE_MGMT;

  FETCH CUR_IS_LAS_LICENSE_MGMT
    INTO IS_LAS_LICENSE_MGMT;

  IF CUR_IS_LAS_LICENSE_MGMT%NOTFOUND
  THEN
    IS_LAS_LICENSE_MGMT := 'F';
  END IF;

  CLOSE CUR_IS_LAS_LICENSE_MGMT;

  IF IS_LAS_LICENSE_MGMT != 'F'
  THEN
    DBMS_OUTPUT.PUT_LINE('LAS LM: Start...');
    OPEN GET_OLD_COL_DATA_FOR_ORG(INPUT_ORG_NODE_ID);
  
    FETCH GET_OLD_COL_DATA_FOR_ORG
      INTO REC_CURRENT_NODE;
    DBMS_OUTPUT.PUT_LINE(REC_CURRENT_NODE.ORG_NODE_ID);
  
    IF GET_OLD_COL_DATA_FOR_ORG%NOTFOUND
    THEN
      REC_CURRENT_NODE := NULL;
    END IF;
  
    CLOSE GET_OLD_COL_DATA_FOR_ORG;
  
    V_NEW_AVAILABLE_COUNT := INPUT_AVAILABLE;
    V_OLD_AVAILABLE_COUNT := REC_CURRENT_NODE.AVAILABLE;
  
    -- need to check for immediate child of top node
    IS_CHILD_OF_TOPNODE := IS_IMMEDIATE_CHILD_OF_TOPNODE(INPUT_ORG_NODE_ID);
    IS_LEAF_NODE        := CHECK_FOR_LEAF_NODE(INPUT_ORG_NODE_ID);
    V_PARENT_NODE_ID    := FETCH_PARENT_NODE_ID(INPUT_ORG_NODE_ID);
  
    IF (V_OLD_AVAILABLE_COUNT > V_NEW_AVAILABLE_COUNT)
    THEN
      DBMS_OUTPUT.PUT_LINE('CASE 1: LC decreased..');
      V_LC_DIFF := V_OLD_AVAILABLE_COUNT - V_NEW_AVAILABLE_COUNT;
    
      /** UPDATE COL for org node **/
      UPDATE CUSTOMER_ORGNODE_LICENSE
         SET AVAILABLE                   = AVAILABLE - V_LC_DIFF,
             LICENSE_AFTER_LAST_PURCHASE = LICENSE_AFTER_LAST_PURCHASE -
                                           V_LC_DIFF
       WHERE CUSTOMER_ID = INPUT_CUSTOMER_ID
         AND ORG_NODE_ID = INPUT_ORG_NODE_ID
         AND PRODUCT_ID = INPUT_PRODUCT_ID;
    
      /*FETCH COL INFORMATION OF THE PARENT ORG NODE*/
      SELECT COL.AVAILABLE, COL.LICENSE_AFTER_LAST_PURCHASE
        INTO V_AVAILABLE, V_LICENSE_AFTER_LAST_PURCHASE
        FROM CUSTOMER_ORGNODE_LICENSE COL
       WHERE COL.CUSTOMER_ID = INPUT_CUSTOMER_ID
         AND COL.ORG_NODE_ID = V_PARENT_NODE_ID
         AND COL.PRODUCT_ID = INPUT_PRODUCT_ID;
    
      /** UPDATE COL for parent node **/
      -- THIS CHECKING IS DONE TO MANTAIN 100% AVAILABILITY OF LICENSES.
      IF V_AVAILABLE + V_LC_DIFF > V_LICENSE_AFTER_LAST_PURCHASE
      THEN
        UPDATE CUSTOMER_ORGNODE_LICENSE
           SET AVAILABLE                   = AVAILABLE + V_LC_DIFF,
               LICENSE_AFTER_LAST_PURCHASE = AVAILABLE + V_LC_DIFF,
               EMAIL_NOTIFY_FLAG ='T'
         WHERE CUSTOMER_ID = INPUT_CUSTOMER_ID
           AND ORG_NODE_ID = V_PARENT_NODE_ID
           AND PRODUCT_ID = INPUT_PRODUCT_ID;
      ELSE
        UPDATE CUSTOMER_ORGNODE_LICENSE
           SET AVAILABLE = AVAILABLE + V_LC_DIFF,
               EMAIL_NOTIFY_FLAG='T'
         WHERE CUSTOMER_ID = INPUT_CUSTOMER_ID
           AND ORG_NODE_ID = V_PARENT_NODE_ID
           AND PRODUCT_ID = INPUT_PRODUCT_ID;
      END IF;
    
      /** Looping for POs from new one to old one :: as RETURN license case **/
      FOR REC_NEW_TO_OLD_PO IN GET_NEW_TO_OLD_PO_FOR_ORG(INPUT_ORG_NODE_ID)
      LOOP
        DBMS_OUTPUT.PUT_LINE(REC_NEW_TO_OLD_PO.ORDER_INDEX);
        IF V_LC_DIFF > 0
        THEN
          IF (REC_NEW_TO_OLD_PO.AVAILABLE >= V_LC_DIFF)
          THEN
            DBMS_OUTPUT.PUT_LINE('CASE 1.1.1 :: ');
          
            /** UPDATE Available count of this PO for the org_node_id in OOL **/
            IF IS_LEAF_NODE = 'TRUE'
            THEN
              UPDATE ORGNODE_ORDER_LICENSE
                 SET AVAILABLE          = AVAILABLE - V_LC_DIFF,
                     LAST_LICENSE_COUNT = LAST_LICENSE_COUNT - V_LC_DIFF
               WHERE ORG_NODE_ID = INPUT_ORG_NODE_ID
                 AND ORDER_INDEX = REC_NEW_TO_OLD_PO.ORDER_INDEX;
            ELSE
              UPDATE ORGNODE_ORDER_LICENSE
                 SET AVAILABLE = AVAILABLE - V_LC_DIFF
               WHERE ORG_NODE_ID = INPUT_ORG_NODE_ID
                 AND ORDER_INDEX = REC_NEW_TO_OLD_PO.ORDER_INDEX;
            END IF;
          
            /** UPDATE Available count of this PO for the parent org_node_id in OOL **/
            UPDATE ORGNODE_ORDER_LICENSE
               SET AVAILABLE = AVAILABLE + V_LC_DIFF
             WHERE ORG_NODE_ID = V_PARENT_NODE_ID
               AND ORDER_INDEX = REC_NEW_TO_OLD_PO.ORDER_INDEX;
          
            /** If immediate child of top node :: UPDATE Available count of this PO for the parent org_node_id in CPL **/
            IF IS_CHILD_OF_TOPNODE = 'TRUE'
            THEN
            
              SELECT CPL.AVAILABLE, CPL.LICENSE_AFTER_LAST_PURCHASE
                INTO V_AVAILABLE_CPL, V_LIC_AFTER_LAST_PURCHASE_CPL
                FROM CUSTOMER_PRODUCT_LICENSE CPL
               WHERE CPL.CUSTOMER_ID = INPUT_CUSTOMER_ID
                 AND CPL.PRODUCT_ID = INPUT_PRODUCT_ID
                 AND ORDER_INDEX = REC_NEW_TO_OLD_PO.ORDER_INDEX;
              -- THIS CHECKING IS DONE TO MANTAIN 100% AVAILABILITY OF LICENSES.
              IF V_AVAILABLE_CPL + V_LC_DIFF >
                 V_LIC_AFTER_LAST_PURCHASE_CPL
              THEN
                UPDATE CUSTOMER_PRODUCT_LICENSE
                   SET AVAILABLE                   = AVAILABLE + V_LC_DIFF,
                       LICENSE_AFTER_LAST_PURCHASE = AVAILABLE + V_LC_DIFF
                 WHERE ORDER_INDEX = REC_NEW_TO_OLD_PO.ORDER_INDEX
                   AND CUSTOMER_ID = INPUT_CUSTOMER_ID
                   AND PRODUCT_ID = INPUT_PRODUCT_ID;
              
              ELSE
                UPDATE CUSTOMER_PRODUCT_LICENSE
                   SET AVAILABLE = AVAILABLE + V_LC_DIFF
                 WHERE ORDER_INDEX = REC_NEW_TO_OLD_PO.ORDER_INDEX
                   AND CUSTOMER_ID = INPUT_CUSTOMER_ID
                   AND PRODUCT_ID = INPUT_PRODUCT_ID;
              
              END IF;
            
            END IF;
          
            V_LC_DIFF := 0;
          
          ELSIF (REC_NEW_TO_OLD_PO.AVAILABLE < V_LC_DIFF)
          THEN
            DBMS_OUTPUT.PUT_LINE('CASE 1.1.2 :: ');
            /** UPDATE Available count of this PO for the org_node_id in OOL **/
            IF IS_LEAF_NODE = 'TRUE'
            THEN
              UPDATE ORGNODE_ORDER_LICENSE
                 SET AVAILABLE          = 0,
                     LAST_LICENSE_COUNT = LAST_LICENSE_COUNT -
                                          REC_NEW_TO_OLD_PO.AVAILABLE
               WHERE ORG_NODE_ID = INPUT_ORG_NODE_ID
                 AND ORDER_INDEX = REC_NEW_TO_OLD_PO.ORDER_INDEX;
            ELSE
              UPDATE ORGNODE_ORDER_LICENSE
                 SET AVAILABLE = 0
               WHERE ORG_NODE_ID = INPUT_ORG_NODE_ID
                 AND ORDER_INDEX = REC_NEW_TO_OLD_PO.ORDER_INDEX;
            END IF;
          
            /** UPDATE Available count of this PO for the parent org_node_id in OOL **/
            UPDATE ORGNODE_ORDER_LICENSE
               SET AVAILABLE = AVAILABLE + REC_NEW_TO_OLD_PO.AVAILABLE
             WHERE ORG_NODE_ID = V_PARENT_NODE_ID
               AND ORDER_INDEX = REC_NEW_TO_OLD_PO.ORDER_INDEX;
          
            /** If immediate child of top node :: UPDATE Available count of this PO for the parent org_node_id in CPL **/
            IF IS_CHILD_OF_TOPNODE = 'TRUE'
            THEN
            
              SELECT CPL.AVAILABLE, CPL.LICENSE_AFTER_LAST_PURCHASE
                INTO V_AVAILABLE_CPL, V_LIC_AFTER_LAST_PURCHASE_CPL
                FROM CUSTOMER_PRODUCT_LICENSE CPL
               WHERE CPL.CUSTOMER_ID = INPUT_CUSTOMER_ID
                 AND CPL.PRODUCT_ID = INPUT_PRODUCT_ID
                 AND ORDER_INDEX = REC_NEW_TO_OLD_PO.ORDER_INDEX;
            
              -- THIS CHECKING IS DONE TO MANTAIN 100% AVAILABILITY OF LICENSES.
              IF V_AVAILABLE_CPL + V_LC_DIFF >
                 V_LIC_AFTER_LAST_PURCHASE_CPL
              THEN
                UPDATE CUSTOMER_PRODUCT_LICENSE
                   SET AVAILABLE                   = AVAILABLE +
                                                     REC_NEW_TO_OLD_PO.AVAILABLE,
                       LICENSE_AFTER_LAST_PURCHASE = AVAILABLE +
                                                     REC_NEW_TO_OLD_PO.AVAILABLE
                 WHERE ORDER_INDEX = REC_NEW_TO_OLD_PO.ORDER_INDEX
                   AND CUSTOMER_ID = INPUT_CUSTOMER_ID
                   AND PRODUCT_ID = INPUT_PRODUCT_ID;
              ELSE
                UPDATE CUSTOMER_PRODUCT_LICENSE
                   SET AVAILABLE = AVAILABLE + REC_NEW_TO_OLD_PO.AVAILABLE
                 WHERE ORDER_INDEX = REC_NEW_TO_OLD_PO.ORDER_INDEX
                   AND CUSTOMER_ID = INPUT_CUSTOMER_ID
                   AND PRODUCT_ID = INPUT_PRODUCT_ID;
              
              END IF;
            END IF;
          
            V_LC_DIFF := V_LC_DIFF - REC_NEW_TO_OLD_PO.AVAILABLE;
          END IF;
        END IF;
      END LOOP;
    
    ELSIF (V_OLD_AVAILABLE_COUNT < V_NEW_AVAILABLE_COUNT)
    THEN
      DBMS_OUTPUT.PUT_LINE('CASE 2: LC increased..');
      V_LC_DIFF := V_NEW_AVAILABLE_COUNT - V_OLD_AVAILABLE_COUNT;
    
      /** UPDATE COL for org node **/
      UPDATE CUSTOMER_ORGNODE_LICENSE
         SET AVAILABLE                   = AVAILABLE + V_LC_DIFF,
             LICENSE_AFTER_LAST_PURCHASE = AVAILABLE + V_LC_DIFF,
             EMAIL_NOTIFY_FLAG ='T'
       WHERE CUSTOMER_ID = INPUT_CUSTOMER_ID
         AND ORG_NODE_ID = INPUT_ORG_NODE_ID
         AND PRODUCT_ID = INPUT_PRODUCT_ID;
    
      /** UPDATE COL for parent node **/
      UPDATE CUSTOMER_ORGNODE_LICENSE
         SET AVAILABLE = AVAILABLE - V_LC_DIFF
       WHERE CUSTOMER_ID = INPUT_CUSTOMER_ID
         AND ORG_NODE_ID = V_PARENT_NODE_ID
         AND PRODUCT_ID = INPUT_PRODUCT_ID;
    
      /** Looping for POs from old one to new one :: as TAKE license case **/
      FOR REC_OLD_TO_NEW_PO IN GET_OLD_TO_NEW_PO_FOR_PARENT(V_PARENT_NODE_ID)
      LOOP
        DBMS_OUTPUT.PUT_LINE(REC_OLD_TO_NEW_PO.ORDER_INDEX);
        IF V_LC_DIFF > 0
        THEN
          IF (REC_OLD_TO_NEW_PO.AVAILABLE >= V_LC_DIFF)
          THEN
          
            -- **need to check whether there is an entry of this PO for the org_node_id : there can be this scenario
            IF IS_OOL_ENTRY_EXIST_FOR_PO_ORG(INPUT_ORG_NODE_ID,
                                             REC_OLD_TO_NEW_PO.ORDER_INDEX) =
               'TRUE'
            THEN
              /** UPDATE Available count of this PO for the org_node_id in OOL **/
              IF IS_LEAF_NODE = 'TRUE'
              THEN
                UPDATE ORGNODE_ORDER_LICENSE
                   SET AVAILABLE          = AVAILABLE + V_LC_DIFF,
                       LAST_LICENSE_COUNT = LAST_LICENSE_COUNT + V_LC_DIFF
                 WHERE ORG_NODE_ID = INPUT_ORG_NODE_ID
                   AND ORDER_INDEX = REC_OLD_TO_NEW_PO.ORDER_INDEX;
              ELSE
                UPDATE ORGNODE_ORDER_LICENSE
                   SET AVAILABLE = AVAILABLE + V_LC_DIFF
                 WHERE ORG_NODE_ID = INPUT_ORG_NODE_ID
                   AND ORDER_INDEX = REC_OLD_TO_NEW_PO.ORDER_INDEX;
              END IF;
            ELSE
              IF IS_LEAF_NODE = 'TRUE'
              THEN
                INSERT INTO ORGNODE_ORDER_LICENSE
                VALUES
                  (INPUT_ORG_NODE_ID,
                   REC_OLD_TO_NEW_PO.ORDER_INDEX,
                   V_LC_DIFF,
                   V_LC_DIFF);
              ELSE
                INSERT INTO ORGNODE_ORDER_LICENSE
                VALUES
                  (INPUT_ORG_NODE_ID,
                   REC_OLD_TO_NEW_PO.ORDER_INDEX,
                   V_LC_DIFF,
                   NULL);
              END IF;
            END IF;
          
            /** UPDATE Available count of this PO for the parent org_node_id in OOL **/
            UPDATE ORGNODE_ORDER_LICENSE
               SET AVAILABLE = AVAILABLE - V_LC_DIFF
             WHERE ORG_NODE_ID = V_PARENT_NODE_ID
               AND ORDER_INDEX = REC_OLD_TO_NEW_PO.ORDER_INDEX;
          
            IF IS_CHILD_OF_TOPNODE = 'TRUE'
            THEN
              UPDATE CUSTOMER_PRODUCT_LICENSE
                 SET AVAILABLE = AVAILABLE - V_LC_DIFF
               WHERE ORDER_INDEX = REC_OLD_TO_NEW_PO.ORDER_INDEX
                 AND CUSTOMER_ID = INPUT_CUSTOMER_ID
                 AND PRODUCT_ID = INPUT_PRODUCT_ID;
            END IF;
          
            V_LC_DIFF := 0;
          ELSIF (REC_OLD_TO_NEW_PO.AVAILABLE < V_LC_DIFF)
          THEN
            DBMS_OUTPUT.PUT_LINE('to do....');
          
            /** UPDATE Available count of this PO for the parent org_node_id in OOL **/
            UPDATE ORGNODE_ORDER_LICENSE
               SET AVAILABLE = 0
             WHERE ORG_NODE_ID = V_PARENT_NODE_ID
               AND ORDER_INDEX = REC_OLD_TO_NEW_PO.ORDER_INDEX;
          
           -- **need to check whether there is an entry of this PO for the org_node_id : there can be this scenario
           IF IS_OOL_ENTRY_EXIST_FOR_PO_ORG(INPUT_ORG_NODE_ID, REC_OLD_TO_NEW_PO.ORDER_INDEX) = 'TRUE' THEN                                
               /** UPDATE Available count of this PO for the org_node_id in OOL **/                     
               IF IS_LEAF_NODE = 'TRUE' THEN
                   UPDATE ORGNODE_ORDER_LICENSE
                      SET AVAILABLE = AVAILABLE + REC_OLD_TO_NEW_PO.AVAILABLE,
                          LAST_LICENSE_COUNT = LAST_LICENSE_COUNT + REC_OLD_TO_NEW_PO.AVAILABLE
                    WHERE ORG_NODE_ID = INPUT_ORG_NODE_ID
                      AND ORDER_INDEX = REC_OLD_TO_NEW_PO.ORDER_INDEX;   
               ELSE
                   UPDATE ORGNODE_ORDER_LICENSE
                      SET AVAILABLE = AVAILABLE + REC_OLD_TO_NEW_PO.AVAILABLE
                    WHERE ORG_NODE_ID = INPUT_ORG_NODE_ID
                      AND ORDER_INDEX = REC_OLD_TO_NEW_PO.ORDER_INDEX;                 
               END IF;    
           ELSE
               IF IS_LEAF_NODE = 'TRUE' THEN
                   INSERT INTO ORGNODE_ORDER_LICENSE
                   VALUES
                     (INPUT_ORG_NODE_ID,
                      REC_OLD_TO_NEW_PO.ORDER_INDEX,
                      REC_OLD_TO_NEW_PO.AVAILABLE,
                      REC_OLD_TO_NEW_PO.AVAILABLE);
               ELSE
                   INSERT INTO ORGNODE_ORDER_LICENSE
                   VALUES
                     (INPUT_ORG_NODE_ID,
                      REC_OLD_TO_NEW_PO.ORDER_INDEX,
                      REC_OLD_TO_NEW_PO.AVAILABLE,
                      NULL);
               END IF; 
           END IF;                    

           
          
            /** If immediate child of top node :: UPDATE Available count of this PO for the parent org_node_id in CPL **/
            IF IS_CHILD_OF_TOPNODE = 'TRUE'
            THEN
              UPDATE CUSTOMER_PRODUCT_LICENSE
                 SET AVAILABLE = 0
               WHERE ORDER_INDEX = REC_OLD_TO_NEW_PO.ORDER_INDEX
                 AND CUSTOMER_ID = INPUT_CUSTOMER_ID
                 AND PRODUCT_ID = INPUT_PRODUCT_ID;
            END IF;
          
            V_LC_DIFF := V_LC_DIFF - REC_OLD_TO_NEW_PO.AVAILABLE;
          END IF;
        
        END IF;
      END LOOP;
    
    END IF;
  
  END IF;

END UPDATELICENSEDATAFORLASLINK;
/
