CREATE OR REPLACE PACKAGE LM_DATA_TRACKING_PKG AS

  PROCEDURE LOG_MESSAGE(IN_CUSTOMER_ID INTEGER, V_LOG_MESSAGE VARCHAR2);
  
  PROCEDURE POPULATE_SESSION_MODEL_DATA(IN_CUSTOMER_ID     INTEGER,
                                        EXTRACT_END_DATE   DATE,
                                        LM_DOWM_DATE       DATE);

  PROCEDURE POPULATE_SUBTEST_MODEL_DATA(IN_CUSTOMER_ID     INTEGER,
                                      EXTRACT_END_DATE   DATE,
                                      LM_DOWM_DATE       DATE);                                        
   
                                                                                     
  PROCEDURE EXTRACT_LM_OAS_USAGE_DATA(IN_CUSTOMER_ID     VARCHAR2,
                                      EXTRACT_END_DATE   DATE,
                                      IN_LM_DOWM_DATE    DATE);

  PROCEDURE MERGE_LM_REPORT_DATA;                                   

END LM_DATA_TRACKING_PKG;
/
CREATE OR REPLACE PACKAGE BODY LM_DATA_TRACKING_PKG AS

  /*Global Variable Declaration*/
  V_EXTRACT_SEQ_NUM    INTEGER := 0;  
  V_CONTACT_PHONE	     VARCHAR2(32) := '';
  V_CONTACT	           VARCHAR2(64) := '';
  V_CONTACT_EMAIL	     VARCHAR2(64) := '';
  V_STATE_PR	         VARCHAR2(2) := '';
  V_STATE_PR_DESC	     VARCHAR2(64) := '';
  V_EXT_CUSTOMER_ID	   VARCHAR2(32) := '';
  V_PRODUCT_NAME	     VARCHAR2(64) := '';
  
  
  PROCEDURE LOG_MESSAGE(IN_CUSTOMER_ID INTEGER, V_LOG_MESSAGE VARCHAR2) AS
    PRAGMA AUTONOMOUS_TRANSACTION;
    F_CUST_ID INTEGER := 0;
  
  BEGIN
    F_CUST_ID := IN_CUSTOMER_ID;
    IF IN_CUSTOMER_ID IS NULL THEN
      F_CUST_ID := 0;
    END IF;
  
    INSERT INTO LM_DATA_TRACKING_LOG
      (CUSTOMER_ID, MESSAGE, LOG_TIME)
    VALUES
      (F_CUST_ID, V_LOG_MESSAGE, sysdate);
  
    COMMIT;
  END;
  
  /*
  * Data population for LM Type : Subtest Model
  */
  PROCEDURE POPULATE_SUBTEST_MODEL_DATA(IN_CUSTOMER_ID     INTEGER,
                                      EXTRACT_END_DATE   DATE,
                                      LM_DOWM_DATE       DATE) IS
  BEGIN
  
    INSERT INTO LM_USAGE_DATA_OAS_STG
	( CUSTOMER_ID,
    CONTACT_NAME,
    CONTACT_PHONE,
    CONTACT_EMAIL,
    STATE_PR,
    STATE_PR_DESC,
    EXT_CUSTOMER_ID,
    PRODUCT_NAME,
	  SUBTEST_MODEL,
	  ORG_NODE_ID,
	  ORG_NODE_NAME,
	  ORG_NODE_CATEGORY_LEVEL,
	  ORG_NODE_CATEGORY_NAME,
	  PARENT_ORG_NODE_ID,
	  PARENT_ORG_NODE_NAME,
	  LIC_AVL_IN_DB,
	  CUM_LIC_AVL_IN_DB,
	  LIC_TO_BE_RELEASED,
	  CUM_LIC_TO_BE_RELEASED,
	  LIC_RESERVED_AT_NODE,
	  CUM_LIC_RESERVED,
	  LIC_CONSUMED_AT_NODE,
	  CUM_LIC_CONSUMED,
	  NODE_LVL_AVAILABLE,
	  CUM_AVAILABLE,
	  NODE_NET_AVAILABLE,
	  CUM_NET_AVAILABLE,
	  LIC_MOD_CONSUMED_AT_NODE,
	  CUM_LIC_MOD_CONSUMED, 
	  LIC_MOD_RESERVED_AT_NODE,
	  CUM_LIC_MOD_RESERVED,
	  ACTIVATION_STATUS,
	  EXTRACT_DATE_TIME,
    EXTRACT_SEQ_NUM)
      (SELECT IN_CUSTOMER_ID,
              V_CONTACT,
              V_CONTACT_PHONE,
              V_CONTACT_EMAIL,
              V_STATE_PR,
              V_STATE_PR_DESC,
              V_EXT_CUSTOMER_ID,
              V_PRODUCT_NAME,
			        'T',
              D.ORG_NODE_ID,
              D.ORG_NODE_NAME,
              D.CATEGORY_LEVEL,
              D.CATEGORY_NAME,
              D.PARENT_ID,
              D.PARENT_NAME,
              D.COL_AVL,
              D.CUM_COL_AVAILABLE,
              D.RETURNED_AVL,
              D.CUM_RETURNED_AVL,
              D.RESERVED,
              D.CUM_RESERVED,
              D.CONSUMED,
              D.CUM_CONSUMED,
			        D.NODE_LVL_AVAILABLE,
              D.CUMULATIVE_AVAILABLE,              
              D.NODE_NET_AVAILABLE,
              D.CUMULATIVE_NET_AVL,
              D.MOD_CONSUMED,
              D.CUM_MOD_CONSUMED,
			        D.MOD_RESERVED, /*Mod Reserve Modification*/
              D.CUM_MOD_RESERVED, /*Mod Reserve Modification*/
              'AC',
			        SYSDATE,
              V_EXTRACT_SEQ_NUM
         FROM (SELECT DERIVED.CATEGORY_NAME,
                      DERIVED.CATEGORY_LEVEL,
                      DERIVED.PARENT_ID,
                      DERIVED.PARENT_NAME,
                      DERIVED.ORG_NODE_NAME,
                      DERIVED.ORG_NODE_ID,
                      DERIVED.COL_AVL,
                      DERIVED.CUM_COL_AVAILABLE,
                      DERIVED.RETURNED_AVL,
                      DERIVED.CUM_RETURNED_AVL,
                      DERIVED.RESERVED,
                      DERIVED.CUM_RESERVED,
                      DERIVED.CONSUMED,
                      DERIVED.CUM_CONSUMED,
                      DERIVED.MOD_CONSUMED,
                      DERIVED.CUM_MOD_CONSUMED,
					            DERIVED.MOD_RESERVED, /*Mod Reserve Modification*/
                      DERIVED.CUM_MOD_RESERVED, /*Mod Reserve Modification*/
                      (DERIVED.COL_AVL + DERIVED.RETURNED_AVL) AS NODE_LVL_AVAILABLE,
                      ((DERIVED.COL_AVL + DERIVED.RETURNED_AVL) 
					            - (DERIVED.MOD_RESERVED + DERIVED.MOD_CONSUMED)) AS NODE_NET_AVAILABLE, /*Mod Reserve Modification*/
                      (DERIVED.CUM_COL_AVAILABLE + DERIVED.CUM_RETURNED_AVL) AS CUMULATIVE_AVAILABLE,
                      ((DERIVED.CUM_COL_AVAILABLE + DERIVED.CUM_RETURNED_AVL) - 
					            (DERIVED.CUM_MOD_RESERVED + DERIVED.CUM_MOD_CONSUMED)) AS CUMULATIVE_NET_AVL /*Mod Reserve Modification*/
                 FROM (SELECT DERIVED4.*,
                              NVL(COL.AVAILABLE, 0) AS COL_AVL,
                              LEVEL LEV,
                              SUM(DERIVED4.RETURNED_AVL) OVER(PARTITION BY CONNECT_BY_ROOT(DERIVED4.ORG_NODE_ID)) CUM_RETURNED_AVL,
                              SUM(DERIVED4.RESERVED) OVER(PARTITION BY CONNECT_BY_ROOT(DERIVED4.ORG_NODE_ID)) CUM_RESERVED,
                              SUM(DERIVED4.CONSUMED) OVER(PARTITION BY CONNECT_BY_ROOT(DERIVED4.ORG_NODE_ID)) CUM_CONSUMED,
                              SUM(DERIVED4.MOD_CONSUMED) OVER(PARTITION BY CONNECT_BY_ROOT(DERIVED4.ORG_NODE_ID)) CUM_MOD_CONSUMED,
							                SUM(DERIVED4.MOD_RESERVED) OVER(PARTITION BY CONNECT_BY_ROOT(DERIVED4.ORG_NODE_ID)) CUM_MOD_RESERVED, /*Mod Reserve Modification*/
                              SUM(NVL(COL.AVAILABLE, 0)) OVER(PARTITION BY CONNECT_BY_ROOT(DERIVED4.ORG_NODE_ID)) CUM_COL_AVAILABLE
                         FROM (SELECT ONC.CATEGORY_NAME,
                                      ONC.CATEGORY_LEVEL,
                                      PORG.ORG_NODE_ID AS PARENT_ID,
                                      PORG.ORG_NODE_NAME AS PARENT_NAME,
                                      DERIVED1.ORG_NODE_NAME,
                                      DERIVED1.ORG_NODE_ID,
                                      DERIVED1.AVAILABLE AS RETURNED_AVL,
                                      DERIVED2.RESERVED,
                                      DERIVED3.CONSUMED,
                                      DERIVED5.MOD_CONSUMED,
									                    DERIVED6.MOD_RESERVED /*Mod Reserve Modification*/
                                 FROM (SELECT ORG.ORG_NODE_ID,
                                              ORG.ORG_NODE_NAME,
                                              ORG.ORG_NODE_CATEGORY_ID,
                                              NVL(SUM(LIC_DATA.AVAILABLE), 0) AS AVAILABLE
                                         FROM ORG_NODE ORG,
                                              (SELECT TR.TEST_ROSTER_ID,
                                                      TR.ORG_NODE_ID,
                                                      COUNT(DISTINCT
                                                            ISET.ITEM_SET_ID) AS AVAILABLE
                                                 FROM TEST_ADMIN              TA,
                                                      TEST_ROSTER             TR,
                                                      STUDENT_ITEM_SET_STATUS SISS,
                                                      ITEM_SET_PARENT         ISP,
                                                      ITEM_SET                ISET,
                                                      ITEM_SET                ISET_TD
                                                WHERE TA.TEST_ADMIN_ID =
                                                      TR.TEST_ADMIN_ID
                                                  AND TR.TEST_ROSTER_ID =
                                                      SISS.TEST_ROSTER_ID
                                                  AND TA.PRODUCT_ID NOT IN
                                                      (4008, 4013)
                                                  AND SISS.ITEM_SET_ID =
                                                      ISP.ITEM_SET_ID
                                                  AND ISET_TD.ITEM_SET_ID =
                                                      SISS.ITEM_SET_ID
                                                  AND ISET_TD.SAMPLE = 'F'
                                                  AND ISET_TD.ITEM_SET_LEVEL != 'L'
                                                  AND ISP.PARENT_ITEM_SET_ID =
                                                      ISET.ITEM_SET_ID
                                                  AND ISET.ITEM_SET_TYPE = 'TS'
                                                  AND ISET.SAMPLE = 'F'
                                                  AND ISET.ITEM_SET_LEVEL != 'L'
                                                  AND TA.TEST_ADMIN_STATUS = 'PA'
                                                  AND TA.ACTIVATION_STATUS = 'AC'
                                                  AND TR.ACTIVATION_STATUS = 'AC'
                                                  AND TR.CREATED_DATE_TIME <
                                                      LM_DOWM_DATE
                                                  AND TA.LOGIN_END_DATE >=
                                                      LM_DOWM_DATE
                                                  AND TA.LOGIN_END_DATE <= EXTRACT_END_DATE
                                                  AND TR.TEST_COMPLETION_STATUS IN
                                                      ('IC', 'NT')
                                                  AND SISS.COMPLETION_STATUS = 'SC'
                                                  AND TA.CUSTOMER_ID =
                                                      IN_CUSTOMER_ID
                                                GROUP BY TR.TEST_ROSTER_ID,
                                                         TR.ORG_NODE_ID) LIC_DATA
                                        WHERE ORG.ORG_NODE_ID =
                                              LIC_DATA.ORG_NODE_ID(+)
                                          AND ORG.CUSTOMER_ID = IN_CUSTOMER_ID
                                          AND ORG.ACTIVATION_STATUS = 'AC'
                                        GROUP BY ORG.ORG_NODE_ID,
                                                 ORG.ORG_NODE_NAME,
                                                 ORG.ORG_NODE_CATEGORY_ID) DERIVED1, -- Available Count (to be released)
                                      (SELECT ORG.ORG_NODE_ID,
                                              ORG.ORG_NODE_NAME,
                                              ORG.ORG_NODE_CATEGORY_ID,
                                              NVL(SUM(LIC_DATA.RESERVED), 0) AS RESERVED
                                         FROM ORG_NODE ORG,
                                              (SELECT TR.TEST_ROSTER_ID,
                                                      TR.ORG_NODE_ID,
                                                      COUNT(DISTINCT
                                                            ISET.ITEM_SET_ID) AS RESERVED
                                                 FROM TEST_ADMIN              TA,
                                                      TEST_ROSTER             TR,
                                                      STUDENT_ITEM_SET_STATUS SISS,
                                                      ITEM_SET_PARENT         ISP,
                                                      ITEM_SET                ISET,
                                                      ITEM_SET                ISET_TD
                                                WHERE TA.TEST_ADMIN_ID =
                                                      TR.TEST_ADMIN_ID
                                                  AND TR.TEST_ROSTER_ID =
                                                      SISS.TEST_ROSTER_ID
                                                  AND SISS.ITEM_SET_ID =
                                                      ISP.ITEM_SET_ID
                                                  AND ISET_TD.ITEM_SET_ID =
                                                      SISS.ITEM_SET_ID
                                                  AND ISET_TD.SAMPLE = 'F'
                                                  AND ISET_TD.ITEM_SET_LEVEL != 'L'
                                                  AND ISP.PARENT_ITEM_SET_ID =
                                                      ISET.ITEM_SET_ID
                                                  AND ISET.ITEM_SET_TYPE = 'TS'
                                                  AND ISET.SAMPLE = 'F'
                                                  AND ISET.ITEM_SET_LEVEL != 'L'
                                                  AND TA.TEST_ADMIN_STATUS IN
                                                      ('CU', 'FU')
                                                  AND TA.ACTIVATION_STATUS = 'AC'
                                                  AND TR.ACTIVATION_STATUS = 'AC'
                                                  AND TA.PRODUCT_ID NOT IN
                                                      (4008, 4013)
                                                  AND TR.TEST_COMPLETION_STATUS IN
                                                      ('SC', 'IC', 'IS', 'IN')  -- Modified for defect #83288, #83287 : condition added for interrupted state
                                                  AND SISS.COMPLETION_STATUS = 'SC'
                                                  AND TR.CREATED_DATE_TIME <= EXTRACT_END_DATE
                                                  AND TA.CUSTOMER_ID =
                                                      IN_CUSTOMER_ID
                                                GROUP BY TR.TEST_ROSTER_ID,
                                                         TR.ORG_NODE_ID) LIC_DATA
                                        WHERE ORG.ORG_NODE_ID =
                                              LIC_DATA.ORG_NODE_ID(+)
                                          AND ORG.CUSTOMER_ID = IN_CUSTOMER_ID
                                          AND ORG.ACTIVATION_STATUS = 'AC'
                                        GROUP BY ORG.ORG_NODE_ID,
                                                 ORG.ORG_NODE_NAME,
                                                 ORG.ORG_NODE_CATEGORY_ID) DERIVED2, -- Reserve Count
                                      (SELECT ORG.ORG_NODE_ID,
                                              ORG.ORG_NODE_NAME,
                                              ORG.ORG_NODE_CATEGORY_ID,
                                              NVL(SUM(LIC_DATA.CONSUMED), 0) AS CONSUMED
                                         FROM ORG_NODE ORG,
                                              (SELECT TR.TEST_ROSTER_ID,
                                                      TR.ORG_NODE_ID,
                                                      COUNT(DISTINCT
                                                            ISET.ITEM_SET_ID) AS CONSUMED
                                                 FROM TEST_ADMIN              TA,
                                                      TEST_ROSTER             TR,
                                                      STUDENT_ITEM_SET_STATUS SISS,
                                                      ITEM_SET_PARENT         ISP,
                                                      ITEM_SET                ISET,
                                                      ITEM_SET                ISET_TD
                                                WHERE TA.TEST_ADMIN_ID =
                                                      TR.TEST_ADMIN_ID
                                                  AND TR.TEST_ROSTER_ID =
                                                      SISS.TEST_ROSTER_ID
                                                  AND SISS.ITEM_SET_ID =
                                                      ISP.ITEM_SET_ID
                                                  AND ISET_TD.ITEM_SET_ID =
                                                      SISS.ITEM_SET_ID
                                                  AND ISET_TD.SAMPLE = 'F'
                                                  AND ISET_TD.ITEM_SET_LEVEL != 'L'
                                                  AND ISP.PARENT_ITEM_SET_ID =
                                                      ISET.ITEM_SET_ID
                                                  AND ISET.ITEM_SET_TYPE = 'TS'
                                                  AND ISET.SAMPLE = 'F'
                                                  AND ISET.ITEM_SET_LEVEL != 'L'
                                                  AND TA.TEST_ADMIN_STATUS IN
                                                      ('CU', 'PA')
                                                  AND TA.ACTIVATION_STATUS = 'AC'
                                                  AND TR.ACTIVATION_STATUS = 'AC'
                                                  AND TA.PRODUCT_ID NOT IN
                                                      (4008, 4013)
                                                  AND TR.TEST_COMPLETION_STATUS NOT IN
                                                      ('SC', 'NT')
                                                  AND SISS.COMPLETION_STATUS NOT IN
                                                      ('SC', 'NT')
                                                  AND SISS.START_DATE_TIME <= EXTRACT_END_DATE
                                                  AND TA.CUSTOMER_ID =
                                                      IN_CUSTOMER_ID
                                                GROUP BY TR.TEST_ROSTER_ID,
                                                         TR.ORG_NODE_ID) LIC_DATA
                                        WHERE ORG.ORG_NODE_ID =
                                              LIC_DATA.ORG_NODE_ID(+)
                                          AND ORG.CUSTOMER_ID = IN_CUSTOMER_ID
                                          AND ORG.ACTIVATION_STATUS = 'AC'
                                        GROUP BY ORG.ORG_NODE_ID,
                                                 ORG.ORG_NODE_NAME,
                                                 ORG.ORG_NODE_CATEGORY_ID) DERIVED3, -- Consumed Count
                                      (SELECT ORG.ORG_NODE_ID,
                                              ORG.ORG_NODE_NAME,
                                              ORG.ORG_NODE_CATEGORY_ID,
                                              NVL(SUM(LIC_DATA.MOD_CONSUMED), 0) AS MOD_CONSUMED
                                         FROM ORG_NODE ORG,
                                              (SELECT TR.TEST_ROSTER_ID,
                                                      TR.ORG_NODE_ID,
                                                      COUNT(DISTINCT
                                                            ISET.ITEM_SET_ID) AS MOD_CONSUMED
                                                 FROM TEST_ADMIN              TA,
                                                      TEST_ROSTER             TR,
                                                      STUDENT_ITEM_SET_STATUS SISS,
                                                      ITEM_SET_PARENT         ISP,
                                                      ITEM_SET                ISET,
                                                      ITEM_SET                ISET_TD
                                                WHERE TA.TEST_ADMIN_ID =
                                                      TR.TEST_ADMIN_ID
                                                  AND TR.TEST_ROSTER_ID =
                                                      SISS.TEST_ROSTER_ID
                                                  AND SISS.ITEM_SET_ID =
                                                      ISP.ITEM_SET_ID
                                                  AND ISET_TD.ITEM_SET_ID =
                                                      SISS.ITEM_SET_ID
                                                  AND ISET_TD.SAMPLE = 'F'
                                                  AND ISET_TD.ITEM_SET_LEVEL != 'L'
                                                  AND ISP.PARENT_ITEM_SET_ID =
                                                      ISET.ITEM_SET_ID
                                                  AND ISET.ITEM_SET_TYPE = 'TS'
                                                  AND ISET.SAMPLE = 'F'
                                                  AND ISET.ITEM_SET_LEVEL != 'L'
                                                  AND TA.TEST_ADMIN_STATUS IN
                                                      ('CU', 'PA')
                                                  AND TA.ACTIVATION_STATUS = 'AC'
                                                  AND TR.ACTIVATION_STATUS = 'AC'
                                                  AND TA.PRODUCT_ID NOT IN
                                                      (4008, 4013)
                                                  AND TR.TEST_COMPLETION_STATUS NOT IN
                                                      ('SC', 'NT')
                                                  AND SISS.COMPLETION_STATUS NOT IN
                                                      ('SC', 'NT')
                                                  AND SISS.START_DATE_TIME BETWEEN
                                                      LM_DOWM_DATE AND
                                                      EXTRACT_END_DATE
                                                  AND TR.CREATED_DATE_TIME >
                                                      LM_DOWM_DATE
                                                  AND TA.CUSTOMER_ID =
                                                      IN_CUSTOMER_ID
                                                GROUP BY TR.TEST_ROSTER_ID,
                                                         TR.ORG_NODE_ID) LIC_DATA
                                        WHERE ORG.ORG_NODE_ID =
                                              LIC_DATA.ORG_NODE_ID(+)
                                          AND ORG.CUSTOMER_ID = IN_CUSTOMER_ID
                                          AND ORG.ACTIVATION_STATUS = 'AC'
                                        GROUP BY ORG.ORG_NODE_ID,
                                                 ORG.ORG_NODE_NAME,
                                                 ORG.ORG_NODE_CATEGORY_ID) DERIVED5, -- Mod Consumed Count
								 /*Mod Reserve Start*/
								 (SELECT ORG.ORG_NODE_ID,
                                              ORG.ORG_NODE_NAME,
                                              ORG.ORG_NODE_CATEGORY_ID,
                                              NVL(SUM(LIC_DATA.RESERVED), 0) AS MOD_RESERVED
                                         FROM ORG_NODE ORG,
                                              (SELECT TR.TEST_ROSTER_ID,
                                                      TR.ORG_NODE_ID,
                                                      COUNT(DISTINCT
                                                            ISET.ITEM_SET_ID) AS RESERVED
                                                 FROM TEST_ADMIN              TA,
                                                      TEST_ROSTER             TR,
                                                      STUDENT_ITEM_SET_STATUS SISS,
                                                      ITEM_SET_PARENT         ISP,
                                                      ITEM_SET                ISET,
                                                      ITEM_SET                ISET_TD
                                                WHERE TA.TEST_ADMIN_ID =
                                                      TR.TEST_ADMIN_ID
                                                  AND TR.TEST_ROSTER_ID =
                                                      SISS.TEST_ROSTER_ID
                                                  AND SISS.ITEM_SET_ID =
                                                      ISP.ITEM_SET_ID
                                                  AND ISET_TD.ITEM_SET_ID =
                                                      SISS.ITEM_SET_ID
                                                  AND ISET_TD.SAMPLE = 'F'
                                                  AND ISET_TD.ITEM_SET_LEVEL != 'L'
                                                  AND ISP.PARENT_ITEM_SET_ID =
                                                      ISET.ITEM_SET_ID
                                                  AND ISET.ITEM_SET_TYPE = 'TS'
                                                  AND ISET.SAMPLE = 'F'
                                                  AND ISET.ITEM_SET_LEVEL != 'L'
                                                  AND TA.TEST_ADMIN_STATUS IN
                                                      ('CU', 'FU')
                                                  AND TA.ACTIVATION_STATUS = 'AC'
                                                  AND TR.ACTIVATION_STATUS = 'AC'
                                                  AND TA.PRODUCT_ID NOT IN
                                                      (4008, 4013)
                                                  AND TR.TEST_COMPLETION_STATUS IN
                                                      ('SC', 'IC', 'IS', 'IN') -- Modified for defect #83288, #83287 
                                                  AND SISS.COMPLETION_STATUS = 'SC'
                                                  AND TR.CREATED_DATE_TIME BETWEEN
                                                      LM_DOWM_DATE AND
                                                      EXTRACT_END_DATE
                                                  AND TA.CUSTOMER_ID =
                                                      IN_CUSTOMER_ID
                                                GROUP BY TR.TEST_ROSTER_ID,
                                                         TR.ORG_NODE_ID) LIC_DATA
                                        WHERE ORG.ORG_NODE_ID =
                                              LIC_DATA.ORG_NODE_ID(+)
                                          AND ORG.CUSTOMER_ID = IN_CUSTOMER_ID
                                          AND ORG.ACTIVATION_STATUS = 'AC'
                                        GROUP BY ORG.ORG_NODE_ID,
                                                 ORG.ORG_NODE_NAME,
                                                 ORG.ORG_NODE_CATEGORY_ID) DERIVED6, -- Mod Reserve Count
								 
								 /*Mod Reserve End*/
									  ORG_NODE_CATEGORY ONC,
                                      ORG_NODE_PARENT ONP,
                                      ORG_NODE PORG
                                WHERE DERIVED1.ORG_NODE_ID = DERIVED2.ORG_NODE_ID
                                  AND DERIVED3.ORG_NODE_ID = DERIVED2.ORG_NODE_ID
                                  AND DERIVED5.ORG_NODE_ID = DERIVED2.ORG_NODE_ID
								  AND DERIVED6.ORG_NODE_ID = DERIVED5.ORG_NODE_ID	  /*Mod Reserve Modification*/
                                  AND DERIVED1.ORG_NODE_CATEGORY_ID = ONC.ORG_NODE_CATEGORY_ID
                                  AND ONC.CUSTOMER_ID = IN_CUSTOMER_ID
                                  AND ONP.ORG_NODE_ID = DERIVED1.ORG_NODE_ID
                                  AND ONP.PARENT_ORG_NODE_ID = PORG.ORG_NODE_ID) DERIVED4
                         LEFT OUTER JOIN CUSTOMER_ORGNODE_LICENSE COL ON DERIVED4.ORG_NODE_ID =
                                                                         COL.ORG_NODE_ID
                                                                     AND COL.CUSTOMER_ID =
                                                                         IN_CUSTOMER_ID
                         /*LEFT OUTER JOIN CUSTOMER_PURCHASE_REPORT TIER2 ON DERIVED4.ORG_NODE_ID =
                                                                           TIER2.ORG_NODE_ID
                                                                       AND TIER2.ACTIVATION_STATUS = 'AC'
                                                                       AND TIER2.CUSTOMER_ID =
                                                                           IN_CUSTOMER_ID*/
                       CONNECT BY NOCYCLE PRIOR DERIVED4.ORG_NODE_ID =
                                   DERIVED4.PARENT_ID) DERIVED
                WHERE DERIVED.LEV = 1) D);
  
  END POPULATE_SUBTEST_MODEL_DATA;
  
  
  /*
  * Data population for LM Type : Session Model
  */
  PROCEDURE POPULATE_SESSION_MODEL_DATA(IN_CUSTOMER_ID     INTEGER,
                                      EXTRACT_END_DATE   DATE,
                                      LM_DOWM_DATE       DATE) IS
  BEGIN
  
    INSERT INTO LM_USAGE_DATA_OAS_STG
		( CUSTOMER_ID,
      CONTACT_NAME,
      CONTACT_PHONE,
      CONTACT_EMAIL,
      STATE_PR,
      STATE_PR_DESC,
      EXT_CUSTOMER_ID,
      PRODUCT_NAME,
		  SUBTEST_MODEL,
		  ORG_NODE_ID,
		  ORG_NODE_NAME,
		  ORG_NODE_CATEGORY_LEVEL,
		  ORG_NODE_CATEGORY_NAME,
		  PARENT_ORG_NODE_ID,
		  PARENT_ORG_NODE_NAME,
		  LIC_AVL_IN_DB,
		  CUM_LIC_AVL_IN_DB,
		  LIC_TO_BE_RELEASED,
		  CUM_LIC_TO_BE_RELEASED,
		  LIC_RESERVED_AT_NODE,
		  CUM_LIC_RESERVED,
		  LIC_CONSUMED_AT_NODE,
		  CUM_LIC_CONSUMED,
		  NODE_LVL_AVAILABLE,
		  CUM_AVAILABLE,
		  NODE_NET_AVAILABLE,
		  CUM_NET_AVAILABLE,
		  LIC_MOD_CONSUMED_AT_NODE,
		  CUM_LIC_MOD_CONSUMED, 
		  LIC_MOD_RESERVED_AT_NODE,
		  CUM_LIC_MOD_RESERVED,
		  ACTIVATION_STATUS,
		  EXTRACT_DATE_TIME,
      EXTRACT_SEQ_NUM)
      (SELECT IN_CUSTOMER_ID,
              V_CONTACT,
              V_CONTACT_PHONE,
              V_CONTACT_EMAIL,
              V_STATE_PR,
              V_STATE_PR_DESC,
              V_EXT_CUSTOMER_ID,
              V_PRODUCT_NAME,
			        'F',
              D.ORG_NODE_ID,
              D.ORG_NODE_NAME,
              D.CATEGORY_LEVEL,
              D.CATEGORY_NAME,
              D.PARENT_ID,
              D.PARENT_NAME,
              D.COL_AVL,
              D.CUM_COL_AVAILABLE,
              D.RETURNED_AVL,
              D.CUM_RETURNED_AVL,
              D.RESERVED,
              D.CUM_RESERVED,
              D.CONSUMED,
              D.CUM_CONSUMED,
			        D.NODE_LVL_AVAILABLE,
              D.CUMULATIVE_AVAILABLE,              
              D.NODE_NET_AVAILABLE,
              D.CUMULATIVE_NET_AVL,
              D.MOD_CONSUMED,
              D.CUM_MOD_CONSUMED,
			        D.MOD_RESERVED, /*Mod Reserve Modification*/
              D.CUM_MOD_RESERVED, /*Mod Reserve Modification*/
              'AC',
			        SYSDATE,
              V_EXTRACT_SEQ_NUM
         FROM (SELECT DERIVED.CATEGORY_NAME,
                      DERIVED.CATEGORY_LEVEL,
                      DERIVED.PARENT_ID,
                      DERIVED.PARENT_NAME,
                      DERIVED.ORG_NODE_NAME,
                      DERIVED.ORG_NODE_ID,
                      DERIVED.COL_AVL,
                      DERIVED.CUM_COL_AVAILABLE,
                      DERIVED.RETURNED_AVL,
                      DERIVED.CUM_RETURNED_AVL,
                      DERIVED.RESERVED,
                      DERIVED.CUM_RESERVED,
                      DERIVED.CONSUMED,
                      DERIVED.CUM_CONSUMED,
                      DERIVED.MOD_CONSUMED,
                      DERIVED.CUM_MOD_CONSUMED,
					            DERIVED.MOD_RESERVED, /*Mod Reserve Modification*/
                      DERIVED.CUM_MOD_RESERVED, /*Mod Reserve Modification*/
                      (DERIVED.COL_AVL + DERIVED.RETURNED_AVL) AS NODE_LVL_AVAILABLE,
                      ((DERIVED.COL_AVL + DERIVED.RETURNED_AVL) -
                      (DERIVED.MOD_RESERVED + DERIVED.MOD_CONSUMED)) AS NODE_NET_AVAILABLE, /*Mod Reserve Modification*/
                      (DERIVED.CUM_COL_AVAILABLE + DERIVED.CUM_RETURNED_AVL) AS CUMULATIVE_AVAILABLE,
                      ((DERIVED.CUM_COL_AVAILABLE + DERIVED.CUM_RETURNED_AVL) -
                      (DERIVED.CUM_MOD_RESERVED + DERIVED.CUM_MOD_CONSUMED)) AS CUMULATIVE_NET_AVL /*Mod Reserve Modification*/
                 FROM (SELECT DERIVED4.*,
                              NVL(COL.AVAILABLE, 0) AS COL_AVL,
                              LEVEL LEV,
                              SUM(DERIVED4.RETURNED_AVL) OVER(PARTITION BY CONNECT_BY_ROOT(DERIVED4.ORG_NODE_ID)) CUM_RETURNED_AVL,
                              SUM(DERIVED4.RESERVED) OVER(PARTITION BY CONNECT_BY_ROOT(DERIVED4.ORG_NODE_ID)) CUM_RESERVED,
                              SUM(DERIVED4.CONSUMED) OVER(PARTITION BY CONNECT_BY_ROOT(DERIVED4.ORG_NODE_ID)) CUM_CONSUMED,
                              SUM(DERIVED4.MOD_CONSUMED) OVER(PARTITION BY CONNECT_BY_ROOT(DERIVED4.ORG_NODE_ID)) CUM_MOD_CONSUMED,
							                SUM(DERIVED4.MOD_RESERVED) OVER(PARTITION BY CONNECT_BY_ROOT(DERIVED4.ORG_NODE_ID)) CUM_MOD_RESERVED, /*Mod Reserve Modification*/
                              SUM(NVL(COL.AVAILABLE, 0)) OVER(PARTITION BY CONNECT_BY_ROOT(DERIVED4.ORG_NODE_ID)) CUM_COL_AVAILABLE
                         FROM (SELECT ONC.CATEGORY_NAME,
                                      ONC.CATEGORY_LEVEL,
                                      PORG.ORG_NODE_ID AS PARENT_ID,
                                      PORG.ORG_NODE_NAME AS PARENT_NAME,
                                      DERIVED1.ORG_NODE_NAME,
                                      DERIVED1.ORG_NODE_ID,
                                      DERIVED1.AVAILABLE AS RETURNED_AVL,
                                      DERIVED2.RESERVED,
                                      DERIVED3.CONSUMED,
                                      DERIVED5.MOD_CONSUMED,
									                    DERIVED6.MOD_RESERVED
                                 FROM (SELECT ORG.ORG_NODE_NAME,
                                              ORG.ORG_NODE_ID,
                                              ORG.ORG_NODE_CATEGORY_ID,
                                              NVL(LIC_ORG.AVAILABLE, 0) AS AVAILABLE
                                         FROM (SELECT TR.ORG_NODE_ID AS ORG_NODE_ID,
                                                      COUNT(1) AS AVAILABLE
                                                 FROM TEST_ADMIN  TA,
                                                      TEST_ROSTER TR
                                                WHERE TA.TEST_ADMIN_ID =
                                                      TR.TEST_ADMIN_ID
                                                  AND TA.TEST_ADMIN_STATUS = 'PA'
                                                  AND TA.ACTIVATION_STATUS = 'AC'
                                                  AND TR.ACTIVATION_STATUS = 'AC'
                                                  AND TA.PRODUCT_ID NOT IN
                                                      (4008, 4013)
                                                  AND TA.CUSTOMER_ID =
                                                      IN_CUSTOMER_ID
                                                  AND TR.CREATED_DATE_TIME <
                                                      LM_DOWM_DATE
                                                  AND TA.LOGIN_END_DATE >=
                                                      LM_DOWM_DATE
                                                  AND TA.LOGIN_END_DATE BETWEEN
                                                      LM_DOWM_DATE AND
                                                      EXTRACT_END_DATE
                                                  AND TR.TEST_COMPLETION_STATUS IN
                                                      ('IC', 'NT')
                                                  AND NOT EXISTS
                                                (SELECT 1
                                                         FROM STUDENT_ITEM_SET_STATUS SISS,
                                                              ITEM_SET                ISET
                                                        WHERE SISS.ITEM_SET_ID =
                                                              ISET.ITEM_SET_ID
                                                          AND SISS.TEST_ROSTER_ID =
                                                              TR.TEST_ROSTER_ID
                                                          AND ISET.SAMPLE = 'F'
                                                          AND ISET.ITEM_SET_LEVEL != 'L'
                                                          AND SISS.COMPLETION_STATUS <> 'SC')
                                                GROUP BY TR.ORG_NODE_ID) LIC_ORG,
                                              ORG_NODE ORG
                                        WHERE ORG.CUSTOMER_ID = IN_CUSTOMER_ID
                                          AND ORG.ORG_NODE_ID =
                                              LIC_ORG.ORG_NODE_ID(+)
                                          AND ORG.ACTIVATION_STATUS = 'AC') DERIVED1, -- Available Count
                                      (SELECT ORG.ORG_NODE_NAME,
                                              ORG.ORG_NODE_ID,
                                              ORG.ORG_NODE_CATEGORY_ID,
                                              NVL(LIC_ORG.RESERVED, 0) AS RESERVED
                                         FROM (SELECT TR.ORG_NODE_ID,
                                                      COUNT(1) AS RESERVED
                                                 FROM TEST_ADMIN  TA,
                                                      TEST_ROSTER TR
                                                WHERE TA.TEST_ADMIN_ID =
                                                      TR.TEST_ADMIN_ID
                                                  AND TA.TEST_ADMIN_STATUS IN
                                                      ('CU', 'FU')
                                                  AND TA.ACTIVATION_STATUS = 'AC'
                                                  AND TR.ACTIVATION_STATUS = 'AC'
                                                  AND TA.PRODUCT_ID NOT IN
                                                      (4008, 4013)
                                                  AND TR.CREATED_DATE_TIME <= EXTRACT_END_DATE
                                                  AND TA.CUSTOMER_ID =
                                                      IN_CUSTOMER_ID
                                                  AND NOT EXISTS
                                                (SELECT 1
                                                         FROM STUDENT_ITEM_SET_STATUS SISS,
                                                              ITEM_SET                ISET
                                                        WHERE SISS.ITEM_SET_ID =
                                                              ISET.ITEM_SET_ID
                                                          AND SISS.TEST_ROSTER_ID =
                                                              TR.TEST_ROSTER_ID
                                                          AND ISET.SAMPLE = 'F'
                                                          AND ISET.ITEM_SET_LEVEL != 'L'
                                                          AND SISS.COMPLETION_STATUS <> 'SC')
                                                GROUP BY TR.ORG_NODE_ID) LIC_ORG,
                                              ORG_NODE ORG
                                        WHERE ORG.CUSTOMER_ID = IN_CUSTOMER_ID
                                          AND ORG.ORG_NODE_ID =
                                              LIC_ORG.ORG_NODE_ID(+)
                                          AND ORG.ACTIVATION_STATUS = 'AC') DERIVED2, -- Reserved Count
                                      (SELECT ORG.ORG_NODE_NAME,
                                              ORG.ORG_NODE_ID,
                                              ORG.ORG_NODE_CATEGORY_ID,
                                              NVL(LIC_ORG.CONSUMED, 0) AS CONSUMED
                                         FROM (SELECT TR.ORG_NODE_ID AS ORG_NODE_ID,
                                                      COUNT(1) AS CONSUMED
                                                 FROM TEST_ADMIN  TA,
                                                      TEST_ROSTER TR
                                                WHERE TA.TEST_ADMIN_ID =
                                                      TR.TEST_ADMIN_ID
                                                  AND TA.TEST_ADMIN_STATUS IN
                                                      ('CU', 'PA')
                                                  AND TA.ACTIVATION_STATUS = 'AC'
                                                  AND TR.ACTIVATION_STATUS = 'AC'
                                                  AND TA.PRODUCT_ID NOT IN
                                                      (4008, 4013)
                                                  AND TR.START_DATE_TIME <= EXTRACT_END_DATE
                                                  AND TA.CUSTOMER_ID =
                                                      IN_CUSTOMER_ID
                                                  AND EXISTS
                                                (SELECT 1
                                                         FROM STUDENT_ITEM_SET_STATUS SISS,
                                                              ITEM_SET                ISET
                                                        WHERE SISS.ITEM_SET_ID =
                                                              ISET.ITEM_SET_ID
                                                          AND SISS.TEST_ROSTER_ID =
                                                              TR.TEST_ROSTER_ID
                                                          AND ISET.SAMPLE = 'F'
                                                          AND ISET.ITEM_SET_LEVEL != 'L'
                                                          AND SISS.COMPLETION_STATUS <> 'SC')
                                                GROUP BY TR.ORG_NODE_ID) LIC_ORG,
                                              ORG_NODE ORG
                                        WHERE ORG.CUSTOMER_ID = IN_CUSTOMER_ID
                                          AND ORG.ORG_NODE_ID =
                                              LIC_ORG.ORG_NODE_ID(+)
                                          AND ORG.ACTIVATION_STATUS = 'AC') DERIVED3, -- Consumed Count
                                      (SELECT ORG.ORG_NODE_NAME,
                                              ORG.ORG_NODE_ID,
                                              ORG.ORG_NODE_CATEGORY_ID,
                                              NVL(LIC_ORG.MOD_CONSUMED, 0) AS MOD_CONSUMED
                                         FROM (SELECT TR.ORG_NODE_ID AS ORG_NODE_ID,
                                                      COUNT(1) AS MOD_CONSUMED
                                                 FROM TEST_ADMIN  TA,
                                                      TEST_ROSTER TR
                                                WHERE TA.TEST_ADMIN_ID =
                                                      TR.TEST_ADMIN_ID
                                                  AND TA.TEST_ADMIN_STATUS IN
                                                      ('CU', 'PA')
                                                  AND TA.ACTIVATION_STATUS = 'AC'
                                                  AND TR.ACTIVATION_STATUS = 'AC'
                                                  AND TA.PRODUCT_ID NOT IN
                                                      (4008, 4013)
                                                  AND TR.CREATED_DATE_TIME >
                                                      LM_DOWM_DATE
                                                  AND TR.START_DATE_TIME BETWEEN
                                                      LM_DOWM_DATE AND
                                                      EXTRACT_END_DATE
                                                  AND TA.CUSTOMER_ID =
                                                      IN_CUSTOMER_ID
                                                  AND EXISTS
                                                (SELECT 1
                                                         FROM STUDENT_ITEM_SET_STATUS SISS,
                                                              ITEM_SET                ISET
                                                        WHERE SISS.ITEM_SET_ID =
                                                              ISET.ITEM_SET_ID
                                                          AND SISS.TEST_ROSTER_ID =
                                                              TR.TEST_ROSTER_ID
                                                          AND ISET.SAMPLE = 'F'
                                                          AND ISET.ITEM_SET_LEVEL != 'L'
                                                          AND SISS.COMPLETION_STATUS <> 'SC')
                                                GROUP BY TR.ORG_NODE_ID) LIC_ORG,
                                              ORG_NODE ORG
                                        WHERE ORG.CUSTOMER_ID = IN_CUSTOMER_ID
                                          AND ORG.ORG_NODE_ID =
                                              LIC_ORG.ORG_NODE_ID(+)
                                          AND ORG.ACTIVATION_STATUS = 'AC') DERIVED5, -- Mod Consumed Count
										  
										  /*Mod Reserve Start*/
										  
										  (SELECT ORG.ORG_NODE_NAME,
                                              ORG.ORG_NODE_ID,
                                              ORG.ORG_NODE_CATEGORY_ID,
                                              NVL(LIC_ORG.MOD_RESERVED, 0) AS MOD_RESERVED
                                         FROM (SELECT TR.ORG_NODE_ID,
                                                      COUNT(1) AS MOD_RESERVED
                                                 FROM TEST_ADMIN  TA,
                                                      TEST_ROSTER TR
                                                WHERE TA.TEST_ADMIN_ID =
                                                      TR.TEST_ADMIN_ID
                                                  AND TA.TEST_ADMIN_STATUS IN
                                                      ('CU', 'FU')
                                                  AND TA.ACTIVATION_STATUS = 'AC'
                                                  AND TR.ACTIVATION_STATUS = 'AC'
                                                  AND TA.PRODUCT_ID NOT IN
                                                      (4008, 4013)
                                                  AND TR.CREATED_DATE_TIME BETWEEN
                                                      LM_DOWM_DATE AND
                                                      EXTRACT_END_DATE
                                                  AND TA.CUSTOMER_ID =
                                                      IN_CUSTOMER_ID
                                                  AND NOT EXISTS
                                                (SELECT 1
                                                         FROM STUDENT_ITEM_SET_STATUS SISS,
                                                              ITEM_SET                ISET
                                                        WHERE SISS.ITEM_SET_ID =
                                                              ISET.ITEM_SET_ID
                                                          AND SISS.TEST_ROSTER_ID =
                                                              TR.TEST_ROSTER_ID
                                                          AND ISET.SAMPLE = 'F'
                                                          AND ISET.ITEM_SET_LEVEL != 'L'
                                                          AND SISS.COMPLETION_STATUS <> 'SC')
                                                GROUP BY TR.ORG_NODE_ID) LIC_ORG,
                                              ORG_NODE ORG
                                        WHERE ORG.CUSTOMER_ID = IN_CUSTOMER_ID
                                          AND ORG.ORG_NODE_ID =
                                              LIC_ORG.ORG_NODE_ID(+)
                                          AND ORG.ACTIVATION_STATUS = 'AC') DERIVED6, -- Mod Reserve Count
										  
										  /*Mod Reserve End*/
                                      ORG_NODE_CATEGORY ONC,
                                      ORG_NODE_PARENT ONP,
                                      ORG_NODE PORG
                                WHERE DERIVED1.ORG_NODE_ID = DERIVED2.ORG_NODE_ID
                                  AND DERIVED3.ORG_NODE_ID = DERIVED2.ORG_NODE_ID
                                  AND DERIVED5.ORG_NODE_ID = DERIVED2.ORG_NODE_ID
								  AND DERIVED6.ORG_NODE_ID =  DERIVED5.ORG_NODE_ID	  /*Mod Reserve Modification*/
                                  AND DERIVED1.ORG_NODE_CATEGORY_ID = ONC.ORG_NODE_CATEGORY_ID
                                  AND ONC.CUSTOMER_ID = IN_CUSTOMER_ID
                                  AND ONP.ORG_NODE_ID = DERIVED1.ORG_NODE_ID
                                  AND ONP.PARENT_ORG_NODE_ID = PORG.ORG_NODE_ID) DERIVED4
                         LEFT OUTER JOIN CUSTOMER_ORGNODE_LICENSE COL ON DERIVED4.ORG_NODE_ID = COL.ORG_NODE_ID
                                                                     AND COL.CUSTOMER_ID = IN_CUSTOMER_ID
                         /*LEFT OUTER JOIN CUSTOMER_PURCHASE_REPORT TIER2 ON DERIVED4.ORG_NODE_ID = TIER2.ORG_NODE_ID
                                                                       AND TIER2.ACTIVATION_STATUS = 'AC'
                                                                       AND TIER2.CUSTOMER_ID =IN_CUSTOMER_ID*/
                       CONNECT BY NOCYCLE PRIOR DERIVED4.ORG_NODE_ID =
                                   DERIVED4.PARENT_ID) DERIVED
                WHERE DERIVED.LEV = 1) D);
  
  END POPULATE_SESSION_MODEL_DATA;



  /**
  * Procedure to extract OAS license usage data.
  */
  PROCEDURE EXTRACT_LM_OAS_USAGE_DATA(IN_CUSTOMER_ID     VARCHAR2,
                                      EXTRACT_END_DATE   DATE,
                                      IN_LM_DOWM_DATE    DATE) IS
    -- Exception declaration
    NO_CUSTOMER EXCEPTION;
    INPUT_INVALID EXCEPTION;
    SUBTEST_MODEL_MISMATCH EXCEPTION;
    SUBTEST_MODEL_INFO EXCEPTION;
  
    LM_DOWM_DATE DATE := IN_LM_DOWM_DATE; -- date from which license system went down.
  
    -- Variable declaration
    V_CUST_COUNT          INTEGER := 0;
    V_COL_COUNT           INTEGER := 0;
    V_TIER2_MODEL_COUNT   INTEGER := 0;
    V_FINAL_SUBTEST_MODEL VARCHAR2(4) := '';
    V_COL_MODEL           VARCHAR2(4) := '';
    V_TIER2_MODEL         VARCHAR2(4) := '';
    V_LOG_MESSAGE         VARCHAR2(500) := '';
    V_INPUTS              VARCHAR2(500) := '';
    V_CURR_CUSTOMER_ID    INTEGER := 0;
    
    -- Cursor declaration
    CURSOR CUR_GET_ALL_CUSTOMER_ID IS    
      select distinct c.customer_id as customer_id
        from customer c, customer_configuration cc
       where c.customer_id = cc.customer_id
         and cc.customer_configuration_name = 'TABE_Customer'
         and exists
       (select 1
                from customer_configuration cc1
               where cc1.customer_id = cc.customer_id
                 and cc1.customer_configuration_name like '%Allow_Subscription%')
      /*and exists (select count(1)
       from customer_orgnode_license
      where customer_id = c.customer_id
        and rownum = 1)*/
      Union
      select distinct lpds.customer_id as customer_id
        from lm_purchase_data_sf_stg lpds, customer_configuration cc
       where lpds.customer_id = cc.customer_id
         and cc.customer_configuration_name = 'TABE_Customer';
         
    CURSOR CUR_INPUT_CUSTOMER_ID (IN_CUSTOMER_ID_STR VARCHAR2) IS
      select regexp_substr(IN_CUSTOMER_ID_STR, '[^,]+', 1, level) as customer_id
        from dual
      connect by regexp_substr(IN_CUSTOMER_ID_STR, '[^,]+', 1, level) is not null;
      
    -- Type declaration
    TYPE ARRAY_OF_CUSTOMER_ID  IS TABLE OF CUR_GET_ALL_CUSTOMER_ID%ROWTYPE;
    V_ARRAY_OF_CUSTOMER_ID ARRAY_OF_CUSTOMER_ID;
    TYPE ARRAY_OF_INPUT_CUSTOMER_ID  IS TABLE OF CUR_INPUT_CUSTOMER_ID%ROWTYPE;
    V_ARRAY_OF_INPUT_CUSTOMER_ID ARRAY_OF_INPUT_CUSTOMER_ID;
    
  BEGIN
    
    /* Validation: Input Missing */
     IF EXTRACT_END_DATE IS NULL OR IN_LM_DOWM_DATE IS NULL THEN
        V_LOG_MESSAGE := 'Inputs EXTRACT_END_DATE and/or IN_LM_DOWM_DATE is missing. Process stopped.. ';
        RAISE INPUT_INVALID;
     END IF;
    
    -- Generate extract sequence number
    select seq_lm_extract_seq_num.nextval
      into V_EXTRACT_SEQ_NUM
      from dual;
    
    IF IN_CUSTOMER_ID IS NOT NULL THEN
       --dbms_output.put_line('Process input customer IDs');       
       
       OPEN CUR_INPUT_CUSTOMER_ID(IN_CUSTOMER_ID);
       LOOP
        FETCH CUR_INPUT_CUSTOMER_ID BULK COLLECT
          INTO V_ARRAY_OF_INPUT_CUSTOMER_ID LIMIT 500;
        
        EXIT WHEN(V_ARRAY_OF_INPUT_CUSTOMER_ID.COUNT = 0);

        FOR I IN 1 .. V_ARRAY_OF_INPUT_CUSTOMER_ID.COUNT LOOP
          
          V_CURR_CUSTOMER_ID := V_ARRAY_OF_INPUT_CUSTOMER_ID(I).CUSTOMER_ID;
          V_INPUTS := 'IN_CUSTOMER_ID:' || V_CURR_CUSTOMER_ID ||
                ' | EXTRACT_END_DATE:' || EXTRACT_END_DATE ||
                ' | IN_LM_DOWM_DATE:' || IN_LM_DOWM_DATE;
          --dbms_output.put_line('V_INPUTS >> '||V_INPUTS); 
          
            /*Validation: Input Customer ID is present in OAS database and is active or not.*/
            SELECT COUNT(CUSTOMER_ID)
              INTO V_CUST_COUNT
              FROM CUSTOMER
             WHERE CUSTOMER_ID = V_ARRAY_OF_INPUT_CUSTOMER_ID(I).CUSTOMER_ID
               AND ACTIVATION_STATUS = 'AC';
          
            IF V_CUST_COUNT = 0 THEN
              V_LOG_MESSAGE := 'No active customer present in oas database. Process stopped for this customer ID.. ';
              --RAISE NO_CUSTOMER; /*Commented to ensure process runs for other customer ID, instead log message and continue the loop*/
              LOG_MESSAGE(V_CURR_CUSTOMER_ID,
                  V_LOG_MESSAGE || '-' ||
                  DBMS_UTILITY.FORMAT_ERROR_BACKTRACE || '-' || V_INPUTS);
              CONTINUE;    
            ELSE
               /*Validation: Input Customer ID is TABE customer or not.*/

                SELECT COUNT(CUSTOMER_ID)
                  INTO V_CUST_COUNT
                  FROM Customer_Configuration
                 WHERE CUSTOMER_ID = V_ARRAY_OF_INPUT_CUSTOMER_ID(I).CUSTOMER_ID
                   AND CUSTOMER_CONFIGURATION_NAME = 'TABE_Customer';
              
                IF V_CUST_COUNT = 0 THEN
                  V_LOG_MESSAGE := 'TABE_Customer configuration not present in oas database for this customer ID..';
                  --RAISE NO_CUSTOMER;  /*Commented to ensure process runs for other customer IDS, instead log message and continue the loop*/
                  LOG_MESSAGE(V_CURR_CUSTOMER_ID,
                      V_LOG_MESSAGE || '-' ||
                      DBMS_UTILITY.FORMAT_ERROR_BACKTRACE || '-' || V_INPUTS);
                  CONTINUE;    
                END IF;      
            END IF;                  

            
            /* Determine LM Model for the customer: Start */
            -- CHECK IF COL DATA IS PRESENT IN OAS
            SELECT COUNT(1)
              INTO V_COL_COUNT
              FROM CUSTOMER_ORGNODE_LICENSE
             WHERE CUSTOMER_ID = V_ARRAY_OF_INPUT_CUSTOMER_ID(I).CUSTOMER_ID
               AND ROWNUM = 1;
          
            
            IF V_COL_COUNT <> 0 THEN
            -- IF COL DATA PRESENT IN OAS: SET LM MODEL FROM OAS  
              SELECT SUBTEST_MODEL
                INTO V_COL_MODEL
                FROM CUSTOMER_ORGNODE_LICENSE
               WHERE CUSTOMER_ID = V_ARRAY_OF_INPUT_CUSTOMER_ID(I).CUSTOMER_ID
                 AND ROWNUM = 1;
                 
              V_FINAL_SUBTEST_MODEL := V_COL_MODEL;
                 
            ELSIF V_COL_COUNT = 0 THEN   
            -- CHECK IF SalesForce DATA IS PRESENT FOR SUBTEST_MODEL
              SELECT COUNT(1)
                INTO V_TIER2_MODEL_COUNT
                FROM LM_PURCHASE_DATA_SF_STG
               WHERE CUSTOMER_ID = V_ARRAY_OF_INPUT_CUSTOMER_ID(I).CUSTOMER_ID
                 AND LICENSE_MODEL IS NOT NULL
                 AND ROWNUM = 1;
            
              IF V_TIER2_MODEL_COUNT <> 0 THEN
              -- IF COL DATA PRESENT IN OAS: SET LM MODEL FROM SalesForce 
                SELECT DECODE(LICENSE_MODEL,'Sub-Tests', 'T', 'Full','F')
                  INTO V_TIER2_MODEL
                  FROM LM_PURCHASE_DATA_SF_STG
                 WHERE CUSTOMER_ID = V_ARRAY_OF_INPUT_CUSTOMER_ID(I).CUSTOMER_ID
                   AND LICENSE_MODEL IS NOT NULL
                   AND ROWNUM = 1;
                 
                V_FINAL_SUBTEST_MODEL := V_TIER2_MODEL;
                   
              END IF;
            END IF;        
         
             /* Determine LM Model for the customer: End */
            
            /* Modification for additional fields: Start*/
            SELECT C.CONTACT_NAME,
                   C.CONTACT_PHONE,
                   C.CONTACT_EMAIL,
                   C.STATEPR,
                   SC.STATEPR_DESC,
                   C.EXT_CUSTOMER_ID,
                   DECODE(CC.CUSTOMER_CONFIGURATION_NAME,
                          'TABE_Customer',
                          'TABE',
                          'LL_Customer',
                          'LLO',
                          'TASC_Customer',
                          'TASC',
                          'TERRANOVA_Customer',
                          'TN')
              INTO V_CONTACT,
                   V_CONTACT_PHONE,
                   V_CONTACT_EMAIL,
                   V_STATE_PR,
                   V_STATE_PR_DESC,
                   V_EXT_CUSTOMER_ID,
                   V_PRODUCT_NAME
              FROM CUSTOMER C, STATEPR_CODE SC, CUSTOMER_CONFIGURATION CC
             WHERE C.STATEPR = SC.STATEPR
               AND C.CUSTOMER_ID = CC.CUSTOMER_ID
               AND C.CUSTOMER_ID = V_ARRAY_OF_INPUT_CUSTOMER_ID(I).CUSTOMER_ID
               AND CC.CUSTOMER_CONFIGURATION_NAME IN
                   ('TABE_Customer', 'LL_Customer', 'TASC_Customer',
                    'TERRANOVA_Customer');
            /* Modification for additional fields: End*/
            
            /**
             * FINAL PROCESS STARTS FOR LM CALCULATION.
            **/
            --dbms_output.put_line('V_FINAL_SUBTEST_MODEL >> '||V_FINAL_SUBTEST_MODEL);
            IF V_FINAL_SUBTEST_MODEL = 'T' THEN
              /* FOR SUBTEST MODEL */
              -- dbms_output.put_line('V_FINAL_SUBTEST_MODEL >> '||V_FINAL_SUBTEST_MODEL);
              --LOG_MESSAGE( V_ARRAY_OF_CUSTOMER_ID(I).CUSTOMER_ID, 'Subtest Model calculation is pending' || ' - ' || V_INPUTS);
              POPULATE_SUBTEST_MODEL_DATA(V_ARRAY_OF_INPUT_CUSTOMER_ID(I).CUSTOMER_ID,
                                            EXTRACT_END_DATE,
                                            LM_DOWM_DATE); 
            
            ELSIF V_FINAL_SUBTEST_MODEL = 'F' THEN
              /* FOR SESSION MODEL */
              --dbms_output.put_line('V_FINAL_SUBTEST_MODEL >> '||V_FINAL_SUBTEST_MODEL);
              POPULATE_SESSION_MODEL_DATA(V_ARRAY_OF_INPUT_CUSTOMER_ID(I).CUSTOMER_ID,
                                          EXTRACT_END_DATE,
                                          LM_DOWM_DATE);
            
            END IF;
            
            V_LOG_MESSAGE := 'Extract Process completed successfully..';
            COMMIT; -- Commit for each input customer
          
            --LOG THE INFORMATION
            LOG_MESSAGE(V_ARRAY_OF_INPUT_CUSTOMER_ID(I).CUSTOMER_ID, V_LOG_MESSAGE || ' - ' || V_INPUTS);
            --DBMS_OUTPUT.PUT_LINE('Extract Process completed successfully..');
            
            /* Reset additional field variables : Start */
            V_CONTACT_PHONE	     := '';
            V_CONTACT	           := '';
            V_CONTACT_EMAIL	     := '';
            V_STATE_PR	         := '';
            V_STATE_PR_DESC	     := '';
            V_EXT_CUSTOMER_ID	   := '';
            V_PRODUCT_NAME	     := '';
            /* Reset additional field variables : End */
            
        END LOOP;
        EXIT WHEN CUR_INPUT_CUSTOMER_ID%NOTFOUND;
     END LOOP;

     CLOSE CUR_INPUT_CUSTOMER_ID;
     
    ELSE
     --dbms_output.put_line('Process all TABE cust IDs'); 
    
     OPEN CUR_GET_ALL_CUSTOMER_ID;
     LOOP
        FETCH CUR_GET_ALL_CUSTOMER_ID BULK COLLECT
          INTO V_ARRAY_OF_CUSTOMER_ID LIMIT 500;
        
        EXIT WHEN(V_ARRAY_OF_CUSTOMER_ID.COUNT = 0);

        FOR I IN 1 .. V_ARRAY_OF_CUSTOMER_ID.COUNT LOOP
            V_CURR_CUSTOMER_ID := V_ARRAY_OF_CUSTOMER_ID(I).CUSTOMER_ID;
            V_INPUTS := 'IN_CUSTOMER_ID:' || V_CURR_CUSTOMER_ID ||
                    ' | EXTRACT_END_DATE:' || EXTRACT_END_DATE ||
                    ' | IN_LM_DOWM_DATE:' || IN_LM_DOWM_DATE;
            --dbms_output.put_line('V_INPUTS >> '||V_INPUTS); 
              
            --dbms_output.put_line('Process each TABE cust IDs one by one');
            --dbms_output.put_line('Count='|| V_ARRAY_OF_CUSTOMER_ID.COUNT||' I='||I||' Cust Id='||V_ARRAY_OF_CUSTOMER_ID(I).CUSTOMER_ID);
                          
             /* Determine LM Model for the customer: Start */
            -- CHECK IF COL DATA IS PRESENT IN OAS
            SELECT COUNT(1)
              INTO V_COL_COUNT
              FROM CUSTOMER_ORGNODE_LICENSE
             WHERE CUSTOMER_ID = V_ARRAY_OF_CUSTOMER_ID(I).CUSTOMER_ID
               AND ROWNUM = 1;
          
            
            IF V_COL_COUNT <> 0 THEN
            -- IF COL DATA PRESENT IN OAS: SET LM MODEL FROM OAS  
              SELECT SUBTEST_MODEL
                INTO V_COL_MODEL
                FROM CUSTOMER_ORGNODE_LICENSE
               WHERE CUSTOMER_ID =  V_ARRAY_OF_CUSTOMER_ID(I).CUSTOMER_ID
                 AND ROWNUM = 1;
                 
              V_FINAL_SUBTEST_MODEL := V_COL_MODEL;
                 
            ELSIF V_COL_COUNT = 0 THEN   
            -- CHECK IF SalesForce DATA IS PRESENT FOR SUBTEST_MODEL
              SELECT COUNT(1)
                INTO V_TIER2_MODEL_COUNT
                FROM LM_PURCHASE_DATA_SF_STG
               WHERE CUSTOMER_ID =  V_ARRAY_OF_CUSTOMER_ID(I).CUSTOMER_ID
                 AND LICENSE_MODEL IS NOT NULL
                 AND ROWNUM = 1;
            
              IF V_TIER2_MODEL_COUNT <> 0 THEN
              -- IF COL DATA PRESENT IN OAS: SET LM MODEL FROM SalesForce 
                SELECT DECODE(LICENSE_MODEL,'Sub-Tests', 'T', 'Full','F')
                  INTO V_TIER2_MODEL
                  FROM LM_PURCHASE_DATA_SF_STG
                 WHERE CUSTOMER_ID =  V_ARRAY_OF_CUSTOMER_ID(I).CUSTOMER_ID
                   AND LICENSE_MODEL IS NOT NULL
                   AND ROWNUM = 1;
                 
                V_FINAL_SUBTEST_MODEL := V_TIER2_MODEL;
                   
              END IF;
            END IF;        
         
             /* Determine LM Model for the customer: End */
            
            /* Modification for additional fields: Start*/
            SELECT C.CONTACT_NAME,
                   C.CONTACT_PHONE,
                   C.CONTACT_EMAIL,
                   C.STATEPR,
                   SC.STATEPR_DESC,
                   C.EXT_CUSTOMER_ID,
                   DECODE(CC.CUSTOMER_CONFIGURATION_NAME,
                          'TABE_Customer',
                          'TABE',
                          'LL_Customer',
                          'LLO',
                          'TASC_Customer',
                          'TASC',
                          'TERRANOVA_Customer',
                          'TN')
              INTO V_CONTACT,
                   V_CONTACT_PHONE,
                   V_CONTACT_EMAIL,
                   V_STATE_PR,
                   V_STATE_PR_DESC,
                   V_EXT_CUSTOMER_ID,
                   V_PRODUCT_NAME
              FROM CUSTOMER C, STATEPR_CODE SC, CUSTOMER_CONFIGURATION CC
             WHERE C.STATEPR = SC.STATEPR
               AND C.CUSTOMER_ID = CC.CUSTOMER_ID
               AND C.CUSTOMER_ID = V_ARRAY_OF_CUSTOMER_ID(I).CUSTOMER_ID
               AND CC.CUSTOMER_CONFIGURATION_NAME IN
                   ('TABE_Customer', 'LL_Customer', 'TASC_Customer',
                    'TERRANOVA_Customer');
            /* Modification for additional fields: End*/
            
           
            /**
             * FINAL PROCESS STARTS FOR LM CALCULATION.
            **/
            --dbms_output.put_line('V_FINAL_SUBTEST_MODEL >> '||V_FINAL_SUBTEST_MODEL);
            IF V_FINAL_SUBTEST_MODEL = 'T' THEN
              /* FOR SUBTEST MODEL */
              --dbms_output.put_line('V_FINAL_SUBTEST_MODEL >> '||V_FINAL_SUBTEST_MODEL);
              --LOG_MESSAGE( V_ARRAY_OF_CUSTOMER_ID(I).CUSTOMER_ID, 'Subtest Model calculation is pending' || ' - ' || V_INPUTS);
              POPULATE_SUBTEST_MODEL_DATA(V_ARRAY_OF_CUSTOMER_ID(I).CUSTOMER_ID,
                                          EXTRACT_END_DATE,
                                          LM_DOWM_DATE); 
            
            ELSIF V_FINAL_SUBTEST_MODEL = 'F' THEN
              /* FOR SESSION MODEL */
              --dbms_output.put_line('V_FINAL_SUBTEST_MODEL >> '||V_FINAL_SUBTEST_MODEL);
              POPULATE_SESSION_MODEL_DATA( V_ARRAY_OF_CUSTOMER_ID(I).CUSTOMER_ID,
                                           EXTRACT_END_DATE,
                                           LM_DOWM_DATE);
            
            END IF;
            
            V_LOG_MESSAGE := 'Extract Process completed successfully..';
            COMMIT; -- Commit for each input customer
          
            --LOG THE INFORMATION
            LOG_MESSAGE( V_ARRAY_OF_CUSTOMER_ID(I).CUSTOMER_ID, V_LOG_MESSAGE || ' - ' || V_INPUTS);
            --DBMS_OUTPUT.PUT_LINE('Extract Process completed successfully..');
            
            /* Reset additional field variables : Start */
            V_CONTACT_PHONE	     := '';
            V_CONTACT	           := '';
            V_CONTACT_EMAIL	     := '';
            V_STATE_PR	         := '';
            V_STATE_PR_DESC	     := '';
            V_EXT_CUSTOMER_ID	   := '';
            V_PRODUCT_NAME	     := '';
            /* Reset additional field variables : End */
            
        END LOOP;
        EXIT WHEN CUR_GET_ALL_CUSTOMER_ID%NOTFOUND;
     END LOOP;

     CLOSE CUR_GET_ALL_CUSTOMER_ID;
    END IF;  
    
  
  EXCEPTION
    WHEN INPUT_INVALID THEN
      LOG_MESSAGE(V_CURR_CUSTOMER_ID,
                  V_LOG_MESSAGE || '-' ||
                  DBMS_UTILITY.FORMAT_ERROR_BACKTRACE || '-' || V_INPUTS);
      RAISE_APPLICATION_ERROR(-20004, 'Input is missing');
    WHEN NO_CUSTOMER THEN
      LOG_MESSAGE(V_CURR_CUSTOMER_ID,
                  V_LOG_MESSAGE || '-' ||
                  DBMS_UTILITY.FORMAT_ERROR_BACKTRACE || '-' || V_INPUTS);
      RAISE_APPLICATION_ERROR(-20000,
                              'Customer_id NOT EXISTING IN OAS / Not A TABE Customer');
    WHEN OTHERS THEN
      LOG_MESSAGE(V_CURR_CUSTOMER_ID,
                  V_LOG_MESSAGE || '-' ||
                  DBMS_UTILITY.FORMAT_ERROR_BACKTRACE || '-' || V_INPUTS);
    ROLLBACK;
    
  END EXTRACT_LM_OAS_USAGE_DATA;
  
  /***
  *
  *Procedure to merge lm report data from OAS and SF raw staging tables
  *
  ***/
   PROCEDURE MERGE_LM_REPORT_DATA IS
    V_OAS_EXTRACT_SEQ_NUM    INTEGER := 0; 
    V_SF_LOAD_SEQ_NUM        INTEGER := 0;
    V_LM_MERGE_SEQ_NUM       INTEGER := 0;
    V_LOG_MESSAGE            VARCHAR2(1024) := '';
    V_INPUTS                 VARCHAR2(100) := '';
    V_CURR_CUSTOMER_ID       INTEGER := -77;
  
  BEGIN
    --DBMS_OUTPUT.PUT_LINE('Merge LM Report data started...');
    
    V_LOG_MESSAGE := 'Merge LM Report Data Process';
    -- Fetch max extract sequence number for OAS usage data
    select max(extract_seq_num)
      into V_OAS_EXTRACT_SEQ_NUM
      from lm_usage_data_oas_stg;
    
    -- Fetch max load sequence number for SF purchase data
    select max(load_seq_num)
      into V_SF_LOAD_SEQ_NUM
      from lm_purchase_data_sf_stg;
      
    -- Generate merge sequence number
    select SEQ_LM_MERGE_SEQ_NUM.nextval
      into V_LM_MERGE_SEQ_NUM
      from dual;  
    
   /* --Test Data for validation : Need to be commented
    V_OAS_EXTRACT_SEQ_NUM    := 68; 
    V_SF_LOAD_SEQ_NUM        := 16;*/
    
    V_INPUTS := 'For extract_seq_num = '||V_OAS_EXTRACT_SEQ_NUM||' : load_seq_num = '||V_SF_LOAD_SEQ_NUM|| ' : merge_seq_num = '||V_LM_MERGE_SEQ_NUM;  
    
    --DBMS_OUTPUT.PUT_LINE('Data will be merged for extract_seq_num = '||V_OAS_EXTRACT_SEQ_NUM||' :: load_seq_num = '||V_SF_LOAD_SEQ_NUM);
        INSERT INTO LM_MERGED_REPORT_DATA
      (OAS_CUSTOMER_ID,
       OAS_CONTACT_NAME,
       OAS_CONTACT_PHONE,
       OAS_CONTACT_EMAIL,
       OAS_STATE_PR,
       OAS_STATE_PR_DESC,
       OAS_EXT_CUSTOMER_ID,
       OAS_PRODUCT_NAME,
       OAS_SUBTEST_MODEL,
       OAS_ORG_NODE_ID,
       OAS_ORG_NODE_NAME,
       OAS_ORG_NODE_CATEGORY_LEVEL,
       OAS_ORG_NODE_CATEGORY_NAME,
       OAS_PARENT_ORG_NODE_ID,
       OAS_PARENT_ORG_NODE_NAME,
       OAS_LIC_AVL_IN_DB,
       OAS_CUM_LIC_AVL_IN_DB,
       OAS_LIC_TO_BE_RELEASED,
       OAS_CUM_LIC_TO_BE_RELEASED,
       OAS_LIC_RESERVED_AT_NODE,
       OAS_CUM_LIC_RESERVED,
       OAS_LIC_CONSUMED_AT_NODE,
       OAS_CUM_LIC_CONSUMED,
       OAS_NODE_LVL_AVAILABLE,
       OAS_CUM_AVAILABLE,
       OAS_NODE_NET_AVAILABLE,
       OAS_CUM_NET_AVAILABLE,
       OAS_LIC_MOD_CONSUMED_AT_NODE,
       OAS_CUM_LIC_MOD_CONSUMED,
       OAS_LIC_MOD_RESERVED_AT_NODE,
       OAS_CUM_LIC_MOD_RESERVED,
       OAS_ACTIVATION_STATUS,
       OAS_EXTRACT_DATE_TIME,
       OAS_EXTRACT_SEQ_NUM,
       SF_CUSTOMER_ID,
       SF_OAS_IMPLEMENTATION_ID,
       SF_IMPL_RECORD_TYPE,
       SF_CUSTOMER_ACCOUNT_NAME,
       SF_ACCOUNT_STATE,
       SF_ORG_NODE_ID,
       SF_ORG_NODE_NAME,
       SF_CONTACT_PHONE,
       SF_CONTACT,
       SF_CONTACT_EMAIL,
       SF_CATEGORY_NAME,
       SF_CATEGORY_LEVEL,
       SF_LICENSE_MODEL,
       SF_LICENSE_COUNT,
       SF_ORDER_QUANTITY,
       SF_LICENSE_DISTRIBUTED_TO,
       SF_CREATED_DATE,
       SF_INTERVAL_NAME,
       SF_LOAD_DATE_TIME,
       SF_LOAD_SEQ_NUM,
       MERGED_DATE_TIME,
       MERGED_SEQ_NUM)
      (select derived.oas_customer_id,
              derived.oas_contact_name,
              derived.oas_contact_phone,
              derived.oas_contact_email,
              derived.oas_state_pr,
              derived.oas_state_pr_desc,
              derived.oas_ext_customer_id,
              derived.oas_product_name,
              derived.oas_subtest_model,
              derived.oas_org_node_id,
              derived.oas_org_node_name,
              derived.oas_org_node_category_level,
              derived.oas_org_node_category_name,
              derived.oas_parent_org_node_id,
              derived.oas_parent_org_node_name,
              derived.oas_lic_avl_in_db,
              derived.oas_cum_lic_avl_in_db,
              derived.oas_lic_to_be_released,
              derived.oas_cum_lic_to_be_released,
              derived.oas_lic_reserved_at_node,
              derived.oas_cum_lic_reserved,
              derived.oas_lic_consumed_at_node,
              derived.oas_cum_lic_consumed,
              derived.oas_node_lvl_available,
              derived.oas_cum_available,
              derived.oas_node_net_available,
              derived.oas_cum_net_available,
              derived.oas_lic_mod_consumed_at_node,
              derived.oas_cum_lic_mod_consumed,
              derived.oas_lic_mod_reserved_at_node,
              derived.oas_cum_lic_mod_reserved,
              derived.oas_activation_status,
              derived.oas_extract_date_time,
              derived.oas_extract_seq_num,
              derived.sf_customer_id,
              derived.sf_oas_implementation_id,
              derived.sf_impl_record_type,
              derived.sf_customer_account_name,
              derived.sf_account_state,
              derived.sf_org_node_id,
              derived.sf_org_node_name,
              derived.sf_contact_phone,
              derived.sf_contact,
              derived.sf_contact_email,
              derived.sf_category_name,
              derived.sf_category_level,
              derived.sf_license_model,
              derived.sf_license_count,
              derived.sf_order_quantity,
              derived.sf_license_distributed_to,
              derived.sf_created_date,
              derived.sf_interval_name,
              derived.sf_load_date_time,
              derived.sf_load_seq_num,
              sysdate,
              V_LM_MERGE_SEQ_NUM
         from (select oaslmdata.oas_customer_id,
                      oaslmdata.oas_contact_name,
                      oaslmdata.oas_contact_phone,
                      oaslmdata.oas_contact_email,
                      oaslmdata.oas_state_pr,
                      oaslmdata.oas_state_pr_desc,
                      oaslmdata.oas_ext_customer_id,
                      oaslmdata.oas_product_name,
                      oaslmdata.oas_subtest_model,
                      oaslmdata.oas_org_node_id,
                      oaslmdata.oas_org_node_name,
                      oaslmdata.oas_org_node_category_level,
                      oaslmdata.oas_org_node_category_name,
                      oaslmdata.oas_parent_org_node_id,
                      oaslmdata.oas_parent_org_node_name,
                      oaslmdata.oas_lic_avl_in_db,
                      oaslmdata.oas_cum_lic_avl_in_db,
                      oaslmdata.oas_lic_to_be_released,
                      oaslmdata.oas_cum_lic_to_be_released,
                      oaslmdata.oas_lic_reserved_at_node,
                      oaslmdata.oas_cum_lic_reserved,
                      oaslmdata.oas_lic_consumed_at_node,
                      oaslmdata.oas_cum_lic_consumed,
                      oaslmdata.oas_node_lvl_available,
                      oaslmdata.oas_cum_available,
                      oaslmdata.oas_node_net_available,
                      oaslmdata.oas_cum_net_available,
                      oaslmdata.oas_lic_mod_consumed_at_node,
                      oaslmdata.oas_cum_lic_mod_consumed,
                      oaslmdata.oas_lic_mod_reserved_at_node,
                      oaslmdata.oas_cum_lic_mod_reserved,
                      oaslmdata.oas_activation_status,
                      oaslmdata.oas_extract_date_time,
                      oaslmdata.oas_extract_seq_num,
                      sflmdata.sf_customer_id,
                      sflmdata.sf_oas_implementation_id,
                      sflmdata.sf_impl_record_type,
                      sflmdata.sf_customer_account_name,
                      sflmdata.sf_account_state,
                      sflmdata.sf_org_node_id,
                      sflmdata.sf_org_node_name,
                      sflmdata.sf_contact_phone,
                      sflmdata.sf_contact,
                      sflmdata.sf_contact_email,
                      sflmdata.sf_category_name,
                      sflmdata.sf_category_level,
                      sflmdata.sf_license_model,
                      sflmdata.sf_license_count,
                      sflmdata.sf_order_quantity,
                      sflmdata.sf_license_distributed_to,
                      sflmdata.sf_created_date,
                      sflmdata.sf_interval_name,
                      sflmdata.sf_load_date_time,
                      sflmdata.sf_load_seq_num
                 from (select oasdata.customer_id              as oas_customer_id,
                              oasdata.contact_name             as oas_contact_name,
                              oasdata.contact_phone            as oas_contact_phone,
                              oasdata.contact_email            as oas_contact_email,
                              oasdata.state_pr                 as oas_state_pr,
                              oasdata.state_pr_desc            as oas_state_pr_desc,
                              oasdata.ext_customer_id          as oas_ext_customer_id,
                              oasdata.product_name             as oas_product_name,
                              oasdata.subtest_model            as oas_subtest_model,
                              oasdata.org_node_id              as oas_org_node_id,
                              oasdata.org_node_name            as oas_org_node_name,
                              oasdata.org_node_category_level  as oas_org_node_category_level,
                              oasdata.org_node_category_name   as oas_org_node_category_name,
                              oasdata.parent_org_node_id       as oas_parent_org_node_id,
                              oasdata.parent_org_node_name     as oas_parent_org_node_name,
                              oasdata.lic_avl_in_db            as oas_lic_avl_in_db,
                              oasdata.cum_lic_avl_in_db        as oas_cum_lic_avl_in_db,
                              oasdata.lic_to_be_released       as oas_lic_to_be_released,
                              oasdata.cum_lic_to_be_released   as oas_cum_lic_to_be_released,
                              oasdata.lic_reserved_at_node     as oas_lic_reserved_at_node,
                              oasdata.cum_lic_reserved         as oas_cum_lic_reserved,
                              oasdata.lic_consumed_at_node     as oas_lic_consumed_at_node,
                              oasdata.cum_lic_consumed         as oas_cum_lic_consumed,
                              oasdata.node_lvl_available       as oas_node_lvl_available,
                              oasdata.cum_available            as oas_cum_available,
                              oasdata.node_net_available       as oas_node_net_available,
                              oasdata.cum_net_available        as oas_cum_net_available,
                              oasdata.lic_mod_consumed_at_node as oas_lic_mod_consumed_at_node,
                              oasdata.cum_lic_mod_consumed     as oas_cum_lic_mod_consumed,
                              oasdata.lic_mod_reserved_at_node as oas_lic_mod_reserved_at_node,
                              oasdata.cum_lic_mod_reserved     as oas_cum_lic_mod_reserved,
                              oasdata.activation_status        as oas_activation_status,
                              oasdata.extract_date_time        as oas_extract_date_time,
                              oasdata.extract_seq_num          as oas_extract_seq_num
                         from lm_usage_data_oas_stg oasdata
                        where extract_seq_num = V_OAS_EXTRACT_SEQ_NUM) oaslmdata
                 full outer join (select distinct sfmetadata.customer_id            as sf_customer_id,
                                                 sfmetadata.oas_implementation_id  as sf_oas_implementation_id,
                                                 sfmetadata.impl_record_type       as sf_impl_record_type,
                                                 sfmetadata.customer_account_name  as sf_customer_account_name,
                                                 sfmetadata.account_state          as sf_account_state,
                                                 sfmetadata.org_node_id            as sf_org_node_id,
                                                 sfmetadata.org_node_name          as sf_org_node_name,
                                                 sfmetadata.contact_phone          as sf_contact_phone,
                                                 sfmetadata.contact                as sf_contact,
                                                 sfmetadata.contact_email          as sf_contact_email,
                                                 sfmetadata.category_name          as sf_category_name,
                                                 sfmetadata.category_level         as sf_category_level,
                                                 sfmetadata.license_model          as sf_license_model,
                                                 sflmcounts.license_count          as sf_license_count,
                                                 sflmcounts.order_quantity         as sf_order_quantity,
                                                 sfmetadata.license_distributed_to as sf_license_distributed_to,
                                                 sfmetadata.created_date           as sf_created_date,
                                                 sfmetadata.interval_name          as sf_interval_name,
                                                 sfmetadata.load_date_time         as sf_load_date_time,
                                                 sfmetadata.load_seq_num           as sf_load_seq_num
                                   from lm_purchase_data_sf_stg sfmetadata,
                                        (select customer_id,
                                                org_node_id,
                                                /*sum(decode(sign(license_count),
                                                           -1,
                                                           0,
                                                           license_count)) license_count,
                                                sum(decode(sign(order_quantity),
                                                           -1,
                                                           0,
                                                           order_quantity)) order_quantity*/ -- Commented due to change in SF data representation in scenarios having two transactions within a single SF export file -  1. Initial purchase of X/Y amount, and 2. Distribution of entire/partial licenses to other node. Check user story OAS-4688.
                                                sum(license_count) license_count,
                                                sum(order_quantity) order_quantity        
                                           from lm_purchase_data_sf_stg sftbl
                                          where sftbl.load_seq_num = V_SF_LOAD_SEQ_NUM
                                            and (select count(1)
                                                   from lm_purchase_data_sf_stg
                                                  where load_seq_num = V_SF_LOAD_SEQ_NUM
                                                    and org_node_id =
                                                        sftbl.org_node_id
                                                    and customer_id =
                                                        sftbl.customer_id) > 1
                                          group by customer_id, org_node_id
                                         
                                         union
                                         
                                         select customer_id,
                                                org_node_id,
                                                license_count,
                                                order_quantity
                                           from lm_purchase_data_sf_stg sftbl
                                          where sftbl.load_seq_num = V_SF_LOAD_SEQ_NUM
                                            and (select count(1)
                                                   from lm_purchase_data_sf_stg
                                                  where load_seq_num = V_SF_LOAD_SEQ_NUM
                                                    and org_node_id =
                                                        sftbl.org_node_id
                                                    and customer_id =
                                                        sftbl.customer_id) = 1
                                          order by customer_id, org_node_id) sflmcounts
                                 
                                  where sfmetadata.customer_id =
                                        sflmcounts.customer_id
                                    and sfmetadata.org_node_id =
                                        sflmcounts.org_node_id
                                    and sfmetadata.load_seq_num =  V_SF_LOAD_SEQ_NUM -- condition added for defect #83355  
                                    and sfmetadata.oas_implementation_id =
                                        (select max(oas_implementation_id)
                                           from lm_purchase_data_sf_stg
                                          where customer_id =
                                                sflmcounts.customer_id
                                            and org_node_id =
                                                sflmcounts.org_node_id
                                            and load_seq_num = V_SF_LOAD_SEQ_NUM)) sflmdata
               
               on oaslmdata.oas_customer_id = sflmdata.sf_customer_id
               and oaslmdata.oas_org_node_id = sflmdata.sf_org_node_id
                order by oaslmdata.oas_customer_id,
                         oaslmdata.oas_org_node_id,
                         sflmdata.sf_customer_id,
                         sflmdata.sf_org_node_id) derived);
         COMMIT;
         LOG_MESSAGE(V_CURR_CUSTOMER_ID,
                     V_LOG_MESSAGE || ' :: ' || V_INPUTS || ' :: SUCCESSFULL');
         --DBMS_OUTPUT.PUT_LINE('Merge LM Report data completed...');               
    EXCEPTION
        WHEN OTHERS THEN
        LOG_MESSAGE(V_CURR_CUSTOMER_ID,
                    V_LOG_MESSAGE || ' :: ' || V_INPUTS || ' :: FAILED - ' ||DBMS_UTILITY.FORMAT_ERROR_BACKTRACE);
        ROLLBACK;
  END MERGE_LM_REPORT_DATA;

END LM_DATA_TRACKING_PKG;
/
