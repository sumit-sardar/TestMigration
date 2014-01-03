CREATE OR REPLACE PROCEDURE INSERTLICENSEDATAFORLASLINK(INPUT_ORG_NODE_ID    INTEGER,
                                                        INPUT_CUSTOMER_ID    INTEGER,
                                                        INPUT_PRODUCT_ID     INTEGER,
                                                        INPUT_AVAILABLE      INTEGER,
                                                        INPUT_RESERVED       INTEGER,
                                                        INPUT_CONSUMED       INTEGER,
                                                        INPUT_SUBTESTMODEL   VARCHAR2) 
IS

  IS_LAS_LICENSE_MGMT     VARCHAR2(1); 
  IS_LAS_TOP_NODE         VARCHAR2(10) := 'FALSE';
  IS_DATA_EXIST_IN_COL    VARCHAR2(10) := 'FALSE';
  IS_LEAF_NODE            VARCHAR2(10) := 'FALSE';
  V_AVAILABLE_COUNT       NUMBER;
  V_PARENT_NODE_ID        NUMBER;    
       
  CURSOR CUR_IS_LAS_LICENSE_MGMT IS
    SELECT DECODE(COUNT(1), 0, 'F', 'T') AS IS_LAS_LICENSE_MGMT
      FROM CUSTOMER_CONFIGURATION
     WHERE CUSTOMER_ID = INPUT_CUSTOMER_ID
       AND CUSTOMER_CONFIGURATION_NAME = 'License_Yearly_Expiry';

  CURSOR CUR_GET_ALL_ACTIVE_PO IS
    SELECT *
      FROM CUSTOMER_PRODUCT_LICENSE
     WHERE CUSTOMER_ID = INPUT_CUSTOMER_ID
       AND TRUNC(LICENSE_PERIOD_END) >= TRUNC(SYSDATE)
       AND AVAILABLE > 0
     ORDER BY LICENSE_PERIOD_END, LICENSE_PERIOD_START, AVAILABLE;
  
  CURSOR CUR_GET_ACTIVE_PO_OF_PARENT IS
    SELECT OOL.*
      FROM CUSTOMER_PRODUCT_LICENSE CPL, ORGNODE_ORDER_LICENSE OOL, ORG_NODE_PARENT ONP
     WHERE OOL.ORG_NODE_ID = ONP.PARENT_ORG_NODE_ID
       AND OOL.ORDER_INDEX = CPL.ORDER_INDEX
       AND ONP.ORG_NODE_ID = INPUT_ORG_NODE_ID
       AND CPL.CUSTOMER_ID = INPUT_CUSTOMER_ID
       AND OOL.AVAILABLE > 0
       AND TRUNC(CPL.LICENSE_PERIOD_END) >= TRUNC(SYSDATE)
     ORDER BY CPL.LICENSE_PERIOD_END ASC, CPL.LICENSE_PERIOD_START ASC, CPL.AVAILABLE ASC;
        
  -- checks whether the node is top node or not
  FUNCTION CHECK_FOR_TOP_NODE(V_ORG_NODE_ID CUSTOMER_ORGNODE_LICENSE.ORG_NODE_ID%TYPE)
    RETURN VARCHAR2 IS
    V_IS_TOP_NODE   VARCHAR2(10);
  BEGIN
       SELECT DECODE(COUNT(1), 0, 'FALSE', 'TRUE')
         INTO V_IS_TOP_NODE
         FROM ORG_NODE                 ORG,
              ORG_NODE_CATEGORY        ONC
        WHERE ORG.ORG_NODE_CATEGORY_ID = ONC.ORG_NODE_CATEGORY_ID
          AND ONC.CATEGORY_LEVEL = 1
          AND ORG.ORG_NODE_ID = V_ORG_NODE_ID
          AND ORG.CUSTOMER_ID = INPUT_CUSTOMER_ID;
          
       RETURN V_IS_TOP_NODE;   
  END;
  
  -- checks whether the node is leaf node or not
  FUNCTION CHECK_FOR_LEAF_NODE(V_ORG_NODE_ID CUSTOMER_ORGNODE_LICENSE.ORG_NODE_ID%TYPE)
    RETURN VARCHAR2 IS
    V_IS_TOP_NODE   VARCHAR2(10);
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
    V_IS_IMMEDIATE_CHILD   VARCHAR2(10);
  BEGIN
       SELECT DECODE(COUNT(1), 0, 'FALSE', 'TRUE')
         INTO V_IS_IMMEDIATE_CHILD
         FROM ORG_NODE                 ORG,
              ORG_NODE_CATEGORY        ONC
        WHERE ORG.ORG_NODE_CATEGORY_ID = ONC.ORG_NODE_CATEGORY_ID
          AND ONC.CATEGORY_LEVEL = 2
          AND ORG.ORG_NODE_ID = V_ORG_NODE_ID
          AND ORG.CUSTOMER_ID = INPUT_CUSTOMER_ID;
       
       RETURN V_IS_IMMEDIATE_CHILD;
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
  
  -- checks whether there is an entry for the org in COL  
  FUNCTION IS_COL_ENTRY_EXIST_FOR_ORG(V_ORG_NODE_ID ORGNODE_ORDER_LICENSE.ORG_NODE_ID%TYPE)
    RETURN VARCHAR2 IS
    IS_EXIST   VARCHAR2(10);
  BEGIN
       SELECT DECODE(COUNT(1), 0, 'FALSE', 'TRUE')
         INTO IS_EXIST
         FROM CUSTOMER_ORGNODE_LICENSE
        WHERE ORG_NODE_ID = V_ORG_NODE_ID
          AND CUSTOMER_ID = INPUT_CUSTOMER_ID
          AND PRODUCT_ID = INPUT_PRODUCT_ID;
          
       RETURN IS_EXIST;   
  END; 
  
  BEGIN
  
   OPEN CUR_IS_LAS_LICENSE_MGMT;
   
        FETCH CUR_IS_LAS_LICENSE_MGMT INTO IS_LAS_LICENSE_MGMT;
        
        IF CUR_IS_LAS_LICENSE_MGMT%NOTFOUND THEN
           IS_LAS_LICENSE_MGMT := 'F';
        END IF;
        
   CLOSE CUR_IS_LAS_LICENSE_MGMT; 
   
   IF IS_LAS_LICENSE_MGMT != 'F' THEN
      --IS_LAS_TOP_NODE := CHECK_FOR_TOP_NODE(:NEW.ORG_NODE_ID);
      --dbms_output.put_line(CHECK_FOR_TOP_NODE(:NEW.ORG_NODE_ID));
      
      -- restricting insert for the org nodes with available = 0
      IF NOT (IS_COL_ENTRY_EXIST_FOR_ORG(INPUT_ORG_NODE_ID) = 'FALSE' AND INPUT_AVAILABLE = 0) THEN
              
              IS_LEAF_NODE := CHECK_FOR_LEAF_NODE(INPUT_ORG_NODE_ID);
          --IF CHECK_FOR_TOP_NODE(INPUT_ORG_NODE_ID) = 'FALSE' THEN   /** Not required handled in platform now **/        
              --need to check for immediate child of top node
              IF IS_IMMEDIATE_CHILD_OF_TOPNODE(INPUT_ORG_NODE_ID) = 'TRUE' THEN    
                  V_AVAILABLE_COUNT := INPUT_AVAILABLE;
                  V_PARENT_NODE_ID := FETCH_PARENT_NODE_ID(INPUT_ORG_NODE_ID);
                  
                  /** INSERT into COL for org node **/
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
                    (INPUT_ORG_NODE_ID,
                     INPUT_CUSTOMER_ID,
                     INPUT_PRODUCT_ID,
                     INPUT_AVAILABLE,
                     INPUT_RESERVED,
                     INPUT_CONSUMED,
                     INPUT_SUBTESTMODEL,
                     INPUT_AVAILABLE,
                     'T');
                      
                  /** UPDATE COL for parent node **/
                   UPDATE CUSTOMER_ORGNODE_LICENSE
                      SET AVAILABLE = AVAILABLE - INPUT_AVAILABLE
                    WHERE CUSTOMER_ID = INPUT_CUSTOMER_ID
                      AND ORG_NODE_ID = V_PARENT_NODE_ID
                      AND PRODUCT_ID = INPUT_PRODUCT_ID;
                      
                      
                  --Looping for POs from old one to new one
                  --FOR REC_ACTIVE_PO IN CUR_GET_ALL_ACTIVE_PO LOOP
                  FOR REC_ACTIVE_PO IN CUR_GET_ACTIVE_PO_OF_PARENT LOOP
                      dbms_output.put_line('LAS LM Distribution logic starts here....');  
                      IF V_AVAILABLE_COUNT > 0 THEN          
                        IF REC_ACTIVE_PO.AVAILABLE > V_AVAILABLE_COUNT THEN
                            dbms_output.put_line('CASE 1');
                           -- update CPL with new value for the PO used
                           UPDATE CUSTOMER_PRODUCT_LICENSE
                              SET AVAILABLE = AVAILABLE - V_AVAILABLE_COUNT
                            WHERE CUSTOMER_ID = INPUT_CUSTOMER_ID
                              AND ORDER_INDEX = REC_ACTIVE_PO.ORDER_INDEX;
                           
                           -- insert into OOL for current org_node_id
                          /* IF IS_LEAF_NODE = 'TRUE' THEN
                             INSERT INTO ORGNODE_ORDER_LICENSE
                             VALUES
                               (INPUT_ORG_NODE_ID,
                                REC_ACTIVE_PO.ORDER_INDEX,
                                V_AVAILABLE_COUNT,
                                V_AVAILABLE_COUNT);
                           ELSE   */                        
                             INSERT INTO ORGNODE_ORDER_LICENSE
                             VALUES
                               (INPUT_ORG_NODE_ID,
                                REC_ACTIVE_PO.ORDER_INDEX,
                                V_AVAILABLE_COUNT,
                                NULL);
                          /* END IF;  */
                            
                           -- update OOL for the parent_node_id                         
                           UPDATE ORGNODE_ORDER_LICENSE
                              SET AVAILABLE = AVAILABLE - V_AVAILABLE_COUNT
                            WHERE ORG_NODE_ID = V_PARENT_NODE_ID
                              AND ORDER_INDEX = REC_ACTIVE_PO.ORDER_INDEX;
                           
                           V_AVAILABLE_COUNT := 0;  
                        ELSIF REC_ACTIVE_PO.AVAILABLE <= V_AVAILABLE_COUNT THEN
                           dbms_output.put_line('CASE 2');
                           -- update CPL with new value for the PO used
                           UPDATE CUSTOMER_PRODUCT_LICENSE
                              SET AVAILABLE = 0
                            WHERE CUSTOMER_ID = INPUT_CUSTOMER_ID
                              AND ORDER_INDEX = REC_ACTIVE_PO.ORDER_INDEX; 
                           
                           --insert into OOL for current org_node_id
                          /* IF IS_LEAF_NODE = 'TRUE' THEN
                             INSERT INTO ORGNODE_ORDER_LICENSE
                             VALUES
                               (INPUT_ORG_NODE_ID,
                                REC_ACTIVE_PO.ORDER_INDEX,
                                REC_ACTIVE_PO.AVAILABLE,
                                REC_ACTIVE_PO.AVAILABLE);
                           ELSE*/
                             INSERT INTO ORGNODE_ORDER_LICENSE
                             VALUES
                               (INPUT_ORG_NODE_ID,
                                REC_ACTIVE_PO.ORDER_INDEX,
                                REC_ACTIVE_PO.AVAILABLE,
                                NULL);
                           /*END IF;*/
                           --update OOL for the parent_node_id
                           V_PARENT_NODE_ID := FETCH_PARENT_NODE_ID(INPUT_ORG_NODE_ID);
                           UPDATE ORGNODE_ORDER_LICENSE
                              SET AVAILABLE = 0
                            WHERE ORG_NODE_ID = V_PARENT_NODE_ID
                              AND ORDER_INDEX = REC_ACTIVE_PO.ORDER_INDEX;   
                           
                           V_AVAILABLE_COUNT := V_AVAILABLE_COUNT - REC_ACTIVE_PO.AVAILABLE;                      
                        END IF; 
                      END IF; 
                  END LOOP;
              ELSE
                  dbms_output.put_line('Logic for org_node_ids that are not immediate childs of top node');  
                  V_AVAILABLE_COUNT := INPUT_AVAILABLE;
                  V_PARENT_NODE_ID := FETCH_PARENT_NODE_ID(INPUT_ORG_NODE_ID);
                   
                  /** INSERT into COL for org node **/
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
                    (INPUT_ORG_NODE_ID,
                     INPUT_CUSTOMER_ID,
                     INPUT_PRODUCT_ID,
                     INPUT_AVAILABLE,
                     INPUT_RESERVED,
                     INPUT_CONSUMED,
                     INPUT_SUBTESTMODEL,
                     INPUT_AVAILABLE,
                     'T');
                      
                  /** UPDATE COL for parent node **/
                   UPDATE CUSTOMER_ORGNODE_LICENSE
                      SET AVAILABLE = AVAILABLE - INPUT_AVAILABLE
                    WHERE CUSTOMER_ID = INPUT_CUSTOMER_ID
                      AND ORG_NODE_ID = V_PARENT_NODE_ID
                      AND PRODUCT_ID = INPUT_PRODUCT_ID;
                      
                  -- Looping for POs from old one to new one
                  FOR REC_PO_OF_PARENT IN CUR_GET_ACTIVE_PO_OF_PARENT LOOP 
                  
                      IF V_AVAILABLE_COUNT > 0 THEN
                          IF REC_PO_OF_PARENT.AVAILABLE > V_AVAILABLE_COUNT THEN
                             dbms_output.put_line('CASE 1');
                             -- insert into OOL for current org_node_id
                             IF IS_LEAF_NODE = 'TRUE' THEN
                                INSERT INTO ORGNODE_ORDER_LICENSE
                               VALUES
                                 (INPUT_ORG_NODE_ID,
                                  REC_PO_OF_PARENT.ORDER_INDEX,
                                  V_AVAILABLE_COUNT,
                                  V_AVAILABLE_COUNT);
                             ELSE
                               INSERT INTO ORGNODE_ORDER_LICENSE
                               VALUES
                                 (INPUT_ORG_NODE_ID,
                                  REC_PO_OF_PARENT.ORDER_INDEX,
                                  V_AVAILABLE_COUNT,
                                  NULL);
                             END IF;   
                             -- update OOL for the parent_node_id  
                             UPDATE ORGNODE_ORDER_LICENSE
                                SET AVAILABLE = AVAILABLE - V_AVAILABLE_COUNT
                              WHERE ORG_NODE_ID = V_PARENT_NODE_ID
                                AND ORDER_INDEX = REC_PO_OF_PARENT.ORDER_INDEX;
                             
                             V_AVAILABLE_COUNT := 0;  
                          ELSIF REC_PO_OF_PARENT.AVAILABLE <= V_AVAILABLE_COUNT THEN
                             dbms_output.put_line('CASE 2');
                             --insert into OOL for current org_node_id
                             IF IS_LEAF_NODE = 'TRUE' THEN
                               INSERT INTO ORGNODE_ORDER_LICENSE
                               VALUES
                                 (INPUT_ORG_NODE_ID,
                                  REC_PO_OF_PARENT.ORDER_INDEX,
                                  REC_PO_OF_PARENT.AVAILABLE,
                                  REC_PO_OF_PARENT.AVAILABLE);
                             ELSE
                               INSERT INTO ORGNODE_ORDER_LICENSE
                               VALUES
                                 (INPUT_ORG_NODE_ID,
                                  REC_PO_OF_PARENT.ORDER_INDEX,
                                  REC_PO_OF_PARENT.AVAILABLE,
                                  NULL);
                             END IF;
                             --update OOL for the parent_node_id
                             V_PARENT_NODE_ID := FETCH_PARENT_NODE_ID(INPUT_ORG_NODE_ID);
                             UPDATE ORGNODE_ORDER_LICENSE
                                SET AVAILABLE = 0
                              WHERE ORG_NODE_ID = V_PARENT_NODE_ID
                                AND ORDER_INDEX = REC_PO_OF_PARENT.ORDER_INDEX;   
                             
                             V_AVAILABLE_COUNT := V_AVAILABLE_COUNT - REC_PO_OF_PARENT.AVAILABLE; 
                          END IF;
                      END IF;
                  END LOOP;   
              END IF;
          --END IF; 
      END IF;     
   END IF;

  EXCEPTION 
     WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20100,
                                'NO_DATA_FOUND !!!');
     WHEN TOO_MANY_ROWS THEN  
          RAISE_APPLICATION_ERROR(-20100,
                                'TOO_MANY_ROWS !!!'); 
     WHEN CURSOR_ALREADY_OPEN THEN     
          RAISE_APPLICATION_ERROR(-20100,
                                'CURSOR_ALREADY_OPEN !!!');     
                                                                                 
     WHEN OTHERS THEN 
           RAISE_APPLICATION_ERROR(-20100,
                                'OTHERS !!!');
          
     IF CUR_IS_LAS_LICENSE_MGMT%ISOPEN THEN
        CLOSE CUR_IS_LAS_LICENSE_MGMT; 
     END IF;
END INSERTLICENSEDATAFORLASLINK;
/
