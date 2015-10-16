CREATE OR REPLACE PACKAGE LM_OUTAGE_REPORT_EXTRACT AS

  PROCEDURE LOG_MESSAGE(IN_CUSTOMER_ID INTEGER, V_LOG_MESSAGE VARCHAR2);

  PROCEDURE INSERT_SESSION_LEVEL_DATA(IN_CUSTOMER_ID     INTEGER,
                                      EXTRACT_START_DATE DATE,
                                      EXTRACT_END_DATE   DATE,
                                      LM_DOWM_DATE       DATE);

  PROCEDURE INSERT_SUBTEST_LEVEL_DATA(IN_CUSTOMER_ID     INTEGER,
                                      EXTRACT_START_DATE DATE,
                                      EXTRACT_END_DATE   DATE,
                                      LM_DOWM_DATE       DATE);

  PROCEDURE EXTRACT_CUST_LM_COUNT(IN_CUSTOMER_ID     INTEGER,
                                  EXTRACT_START_DATE DATE,
                                  EXTRACT_END_DATE   DATE,
                                  IN_LM_DOWM_DATE    DATE,
                                  IN_SUBTEST_MODEL   VARCHAR2);

END LM_OUTAGE_REPORT_EXTRACT;
/
CREATE OR REPLACE PACKAGE BODY LM_OUTAGE_REPORT_EXTRACT AS

  PROCEDURE LOG_MESSAGE(IN_CUSTOMER_ID INTEGER, V_LOG_MESSAGE VARCHAR2) AS
    PRAGMA AUTONOMOUS_TRANSACTION;
    F_CUST_ID INTEGER := 0;
  
  BEGIN
    F_CUST_ID := IN_CUSTOMER_ID;
    IF IN_CUSTOMER_ID IS NULL THEN
      F_CUST_ID := 0;
    END IF;
  
    INSERT INTO LM_OUTAGE_EXTRACT_LOG
      (CUSTOMER_ID, MESSAGE, LOG_TIME)
    VALUES
      (F_CUST_ID, V_LOG_MESSAGE, sysdate);
  
    COMMIT;
  END;

  /*
  * Session level insertion
  */
  PROCEDURE INSERT_SESSION_LEVEL_DATA(IN_CUSTOMER_ID     INTEGER,
                                      EXTRACT_START_DATE DATE,
                                      EXTRACT_END_DATE   DATE,
                                      LM_DOWM_DATE       DATE) IS
  BEGIN
  
    INSERT INTO LM_OUTAGE_EXTRACT_DATA
      (CUSTOMER_ID,
       ORG_NODE_ID,
       ORG_NODE_NAME,
       ORG_NODE_LEVEL,
       ORG_NODE_CATEGORY_NAME,
       PARENT_ORG_NODE_ID,
       PARENT_ORG_NODE_NAME,
       LIC_AVL_IN_DB,
       CUM_LIC_AVL_IN_DB,
       TIER2_PURCHASE,
       CUM_TIER2_PURCHASE,
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
       EXTRACT_DATE_TIME,
       ACTIVATION_STATUS,
       SUBTEST_MODEL)
      (SELECT IN_CUSTOMER_ID,
              D.ORG_NODE_ID,
              D.ORG_NODE_NAME,
              D.CATEGORY_LEVEL,
              D.CATEGORY_NAME,
              D.PARENT_ID,
              D.PARENT_NAME,
              D.COL_AVL,
              D.CUM_COL_AVAILABLE,
              D.TIER2_PURCHASE,
              D.CUM_TIER2_PURCHASE,
              D.RETURNED_AVL,
              D.CUM_RETURNED_AVL,
              D.RESERVED,
              D.CUM_RESERVED,
              D.CONSUMED,
              D.CUM_CONSUMED,
              D.NODE_LVL_AVAILABLE,
              D.CUMULATIVE_AVAILABLE,
              DECODE(GREATEST(D.NODE_NET_AVAILABLE, 0),
                     0,
                     0,
                     D.NODE_NET_AVAILABLE) AS NODE_NET_AVAILABLE,
              DECODE(GREATEST(D.CUMULATIVE_NET_AVL, 0),
                     0,
                     0,
                     D.CUMULATIVE_NET_AVL) AS CUMULATIVE_NET_AVL,
              /* DECODE(GREATEST(D.NODE_NET_AVAILABLE, 0),
                                                               0,
                                                               (0 - D.NODE_NET_AVAILABLE),
                                                               0) AS NODE_LVL_LIC_NEEDED,
                                                        DECODE(GREATEST(D.CUMULATIVE_NET_AVL, 0),
                                                               0,
                                                               (0 - D.CUMULATIVE_NET_AVL),
                                                               0) AS CUM_NODE_LIC_NEEDED,*/
              SYSDATE,
              'AC',
              'F'
         FROM (SELECT DERIVED.CATEGORY_NAME,
                      DERIVED.CATEGORY_LEVEL,
                      DERIVED.PARENT_ID,
                      DERIVED.PARENT_NAME,
                      DERIVED.ORG_NODE_NAME,
                      DERIVED.ORG_NODE_ID,
                      DERIVED.COL_AVL,
                      DERIVED.CUM_COL_AVAILABLE,
                      DERIVED.TIER2_PURCHASE,
                      DERIVED.CUM_TIER2_PURCHASE,
                      DERIVED.RETURNED_AVL,
                      DERIVED.CUM_RETURNED_AVL,
                      DERIVED.RESERVED,
                      DERIVED.CUM_RESERVED,
                      DERIVED.CONSUMED,
                      DERIVED.CUM_CONSUMED,
                      (DERIVED.COL_AVL + DERIVED.TIER2_PURCHASE +
                      DERIVED.RETURNED_AVL) AS NODE_LVL_AVAILABLE,
                      ((DERIVED.COL_AVL + DERIVED.TIER2_PURCHASE +
                      DERIVED.RETURNED_AVL) -
                      (DERIVED.RESERVED + DERIVED.CONSUMED)) AS NODE_NET_AVAILABLE,
                      (DERIVED.CUM_COL_AVAILABLE +
                      DERIVED.CUM_TIER2_PURCHASE + DERIVED.CUM_RETURNED_AVL) AS CUMULATIVE_AVAILABLE,
                      ((DERIVED.CUM_COL_AVAILABLE +
                      DERIVED.CUM_TIER2_PURCHASE +
                      DERIVED.CUM_RETURNED_AVL) -
                      (DERIVED.CUM_RESERVED + DERIVED.CUM_CONSUMED)) AS CUMULATIVE_NET_AVL
                 FROM (SELECT DERIVED4.*,
                              NVL(COL.AVAILABLE, 0) AS COL_AVL,
                              NVL(TIER2.PURCHASE_COUNT, 0) AS TIER2_PURCHASE,
                              LEVEL LEV,
                              SUM(DERIVED4.RETURNED_AVL) OVER(PARTITION BY CONNECT_BY_ROOT(DERIVED4.ORG_NODE_ID)) CUM_RETURNED_AVL,
                              SUM(DERIVED4.RESERVED) OVER(PARTITION BY CONNECT_BY_ROOT(DERIVED4.ORG_NODE_ID)) CUM_RESERVED,
                              SUM(DERIVED4.CONSUMED) OVER(PARTITION BY CONNECT_BY_ROOT(DERIVED4.ORG_NODE_ID)) CUM_CONSUMED,
                              SUM(NVL(COL.AVAILABLE, 0)) OVER(PARTITION BY CONNECT_BY_ROOT(DERIVED4.ORG_NODE_ID)) CUM_COL_AVAILABLE,
                              SUM(NVL(TIER2.PURCHASE_COUNT, 0)) OVER(PARTITION BY CONNECT_BY_ROOT(DERIVED4.ORG_NODE_ID)) CUM_TIER2_PURCHASE
                         FROM (SELECT ONC.CATEGORY_NAME,
                                      ONC.CATEGORY_LEVEL,
                                      PORG.ORG_NODE_ID AS PARENT_ID,
                                      PORG.ORG_NODE_NAME AS PARENT_NAME,
                                      DERIVED1.ORG_NODE_NAME,
                                      DERIVED1.ORG_NODE_ID,
                                      DERIVED1.AVAILABLE AS RETURNED_AVL,
                                      DERIVED2.RESERVED,
                                      DERIVED3.CONSUMED
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
                                                  AND TA.CUSTOMER_ID =
                                                      IN_CUSTOMER_ID
                                                  AND TRUNC(TA.CREATED_DATE_TIME) <
                                                      LM_DOWM_DATE
                                                  AND TRUNC(TA.LOGIN_END_DATE) BETWEEN
                                                      EXTRACT_START_DATE AND
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
                                          AND ORG.ACTIVATION_STATUS = 'AC') DERIVED1,
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
                                                  AND TRUNC(TA.CREATED_DATE_TIME) BETWEEN
                                                      EXTRACT_START_DATE AND
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
                                                          AND SISS.COMPLETION_STATUS <> 'SC')
                                                GROUP BY TR.ORG_NODE_ID) LIC_ORG,
                                              ORG_NODE ORG
                                        WHERE ORG.CUSTOMER_ID = IN_CUSTOMER_ID
                                          AND ORG.ORG_NODE_ID =
                                              LIC_ORG.ORG_NODE_ID(+)
                                          AND ORG.ACTIVATION_STATUS = 'AC') DERIVED2,
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
                                                  AND TRUNC(TR.START_DATE_TIME) BETWEEN
                                                      EXTRACT_START_DATE AND
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
                                                          AND SISS.COMPLETION_STATUS <> 'SC')
                                                GROUP BY TR.ORG_NODE_ID) LIC_ORG,
                                              ORG_NODE ORG
                                        WHERE ORG.CUSTOMER_ID = IN_CUSTOMER_ID
                                          AND ORG.ORG_NODE_ID =
                                              LIC_ORG.ORG_NODE_ID(+)
                                          AND ORG.ACTIVATION_STATUS = 'AC') DERIVED3,
                                      ORG_NODE_CATEGORY ONC,
                                      ORG_NODE_PARENT ONP,
                                      ORG_NODE PORG
                                WHERE DERIVED1.ORG_NODE_ID =
                                      DERIVED2.ORG_NODE_ID
                                  AND DERIVED3.ORG_NODE_ID =
                                      DERIVED2.ORG_NODE_ID
                                  AND DERIVED1.ORG_NODE_CATEGORY_ID =
                                      ONC.ORG_NODE_CATEGORY_ID
                                  AND ONC.CUSTOMER_ID = IN_CUSTOMER_ID
                                  AND ONP.ORG_NODE_ID = DERIVED1.ORG_NODE_ID
                                  AND ONP.PARENT_ORG_NODE_ID =
                                      PORG.ORG_NODE_ID) DERIVED4
                         LEFT OUTER JOIN CUSTOMER_ORGNODE_LICENSE COL ON DERIVED4.ORG_NODE_ID =
                                                                         COL.ORG_NODE_ID
                                                                     AND COL.CUSTOMER_ID =
                                                                         IN_CUSTOMER_ID
                         LEFT OUTER JOIN CUSTOMER_PURCHASE_REPORT TIER2 ON DERIVED4.ORG_NODE_ID =
                                                                           TIER2.ORG_NODE_ID
                                                                       AND TIER2.ACTIVATION_STATUS = 'AC'
                                                                       AND TIER2.CUSTOMER_ID =
                                                                           IN_CUSTOMER_ID
                       CONNECT BY NOCYCLE PRIOR DERIVED4.ORG_NODE_ID =
                                   DERIVED4.PARENT_ID) DERIVED
                WHERE DERIVED.LEV = 1) D);
  
  END INSERT_SESSION_LEVEL_DATA;

  /*
  * Subtest level insertion
  */
  PROCEDURE INSERT_SUBTEST_LEVEL_DATA(IN_CUSTOMER_ID     INTEGER,
                                      EXTRACT_START_DATE DATE,
                                      EXTRACT_END_DATE   DATE,
                                      LM_DOWM_DATE       DATE) IS
  BEGIN
    INSERT INTO LM_OUTAGE_EXTRACT_DATA
      (CUSTOMER_ID,
       ORG_NODE_ID,
       ORG_NODE_NAME,
       ORG_NODE_LEVEL,
       ORG_NODE_CATEGORY_NAME,
       PARENT_ORG_NODE_ID,
       PARENT_ORG_NODE_NAME,
       LIC_AVL_IN_DB,
       CUM_LIC_AVL_IN_DB,
       TIER2_PURCHASE,
       CUM_TIER2_PURCHASE,
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
       EXTRACT_DATE_TIME,
       ACTIVATION_STATUS,
       SUBTEST_MODEL)
      (SELECT IN_CUSTOMER_ID,
              D.ORG_NODE_ID,
              D.ORG_NODE_NAME,
              D.CATEGORY_LEVEL,
              D.CATEGORY_NAME,
              D.PARENT_ID,
              D.PARENT_NAME,
              D.COL_AVL,
              D.CUM_COL_AVAILABLE,
              D.TIER2_PURCHASE,
              D.CUM_TIER2_PURCHASE,
              D.RETURNED_AVL,
              D.CUM_RETURNED_AVL,
              D.RESERVED,
              D.CUM_RESERVED,
              D.CONSUMED,
              D.CUM_CONSUMED,
              D.NODE_LVL_AVAILABLE,
              D.CUMULATIVE_AVAILABLE,
              DECODE(GREATEST(D.NODE_NET_AVAILABLE, 0),
                     0,
                     0,
                     D.NODE_NET_AVAILABLE) AS NODE_NET_AVAILABLE,
              DECODE(GREATEST(D.CUMULATIVE_NET_AVL, 0),
                     0,
                     0,
                     D.CUMULATIVE_NET_AVL) AS CUMULATIVE_NET_AVL,
              /* DECODE(GREATEST(D.NODE_NET_AVAILABLE, 0),
                                                               0,
                                                               (0 - D.NODE_NET_AVAILABLE),
                                                               0) AS NODE_LVL_LIC_NEEDED,
                                                        DECODE(GREATEST(D.CUMULATIVE_NET_AVL, 0),
                                                               0,
                                                               (0 - D.CUMULATIVE_NET_AVL),
                                                               0) AS CUM_NODE_LIC_NEEDED,*/
              SYSDATE,
              'AC',
              'T'
         FROM (SELECT DERIVED.CATEGORY_NAME,
                      DERIVED.CATEGORY_LEVEL,
                      DERIVED.PARENT_ID,
                      DERIVED.PARENT_NAME,
                      DERIVED.ORG_NODE_NAME,
                      DERIVED.ORG_NODE_ID,
                      DERIVED.COL_AVL,
                      DERIVED.CUM_COL_AVAILABLE,
                      DERIVED.TIER2_PURCHASE,
                      DERIVED.CUM_TIER2_PURCHASE,
                      DERIVED.RETURNED_AVL,
                      DERIVED.CUM_RETURNED_AVL,
                      DERIVED.RESERVED,
                      DERIVED.CUM_RESERVED,
                      DERIVED.CONSUMED,
                      DERIVED.CUM_CONSUMED,
                      (DERIVED.COL_AVL + DERIVED.TIER2_PURCHASE +
                      DERIVED.RETURNED_AVL) AS NODE_LVL_AVAILABLE,
                      ((DERIVED.COL_AVL + DERIVED.TIER2_PURCHASE +
                      DERIVED.RETURNED_AVL) -
                      (DERIVED.RESERVED + DERIVED.CONSUMED)) AS NODE_NET_AVAILABLE,
                      (DERIVED.CUM_COL_AVAILABLE +
                      DERIVED.CUM_TIER2_PURCHASE + DERIVED.CUM_RETURNED_AVL) AS CUMULATIVE_AVAILABLE,
                      ((DERIVED.CUM_COL_AVAILABLE +
                      DERIVED.CUM_TIER2_PURCHASE +
                      DERIVED.CUM_RETURNED_AVL) -
                      (DERIVED.CUM_RESERVED + DERIVED.CUM_CONSUMED)) AS CUMULATIVE_NET_AVL
                 FROM (SELECT DERIVED4.*,
                              NVL(COL.AVAILABLE, 0) AS COL_AVL,
                              NVL(TIER2.PURCHASE_COUNT, 0) AS TIER2_PURCHASE,
                              LEVEL LEV,
                              SUM(DERIVED4.RETURNED_AVL) OVER(PARTITION BY CONNECT_BY_ROOT(DERIVED4.ORG_NODE_ID)) CUM_RETURNED_AVL,
                              SUM(DERIVED4.RESERVED) OVER(PARTITION BY CONNECT_BY_ROOT(DERIVED4.ORG_NODE_ID)) CUM_RESERVED,
                              SUM(DERIVED4.CONSUMED) OVER(PARTITION BY CONNECT_BY_ROOT(DERIVED4.ORG_NODE_ID)) CUM_CONSUMED,
                              SUM(NVL(COL.AVAILABLE, 0)) OVER(PARTITION BY CONNECT_BY_ROOT(DERIVED4.ORG_NODE_ID)) CUM_COL_AVAILABLE,
                              SUM(NVL(TIER2.PURCHASE_COUNT, 0)) OVER(PARTITION BY CONNECT_BY_ROOT(DERIVED4.ORG_NODE_ID)) CUM_TIER2_PURCHASE
                         FROM (SELECT ONC.CATEGORY_NAME,
                                      ONC.CATEGORY_LEVEL,
                                      PORG.ORG_NODE_ID AS PARENT_ID,
                                      PORG.ORG_NODE_NAME AS PARENT_NAME,
                                      DERIVED1.ORG_NODE_NAME,
                                      DERIVED1.ORG_NODE_ID,
                                      DERIVED1.AVAILABLE AS RETURNED_AVL,
                                      DERIVED2.RESERVED,
                                      DERIVED3.CONSUMED
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
                                                      ITEM_SET                ISET
                                                WHERE TA.TEST_ADMIN_ID =
                                                      TR.TEST_ADMIN_ID
                                                  AND TR.TEST_ROSTER_ID =
                                                      SISS.TEST_ROSTER_ID
                                                  AND SISS.ITEM_SET_ID =
                                                      ISP.ITEM_SET_ID
                                                  AND ISP.PARENT_ITEM_SET_ID =
                                                      ISET.ITEM_SET_ID
                                                  AND ISET.ITEM_SET_TYPE = 'TS'
                                                  AND ISET.SAMPLE = 'F'
                                                  AND ISET.ITEM_SET_LEVEL != 'L'
                                                  AND TA.TEST_ADMIN_STATUS = 'PA'
                                                  AND TA.ACTIVATION_STATUS = 'AC'
                                                  AND TR.ACTIVATION_STATUS = 'AC'
                                                  AND TRUNC(TA.CREATED_DATE_TIME) <
                                                      LM_DOWM_DATE
                                                  AND TRUNC(TA.LOGIN_END_DATE) BETWEEN
                                                      EXTRACT_START_DATE AND
                                                      EXTRACT_END_DATE
                                                  AND TR.TEST_COMPLETION_STATUS IN
                                                      ('IC', 'NT')
                                                  AND SISS.COMPLETION_STATUS = 'SC'
                                                  AND TA.CUSTOMER_ID =
                                                      IN_CUSTOMER_ID
                                                GROUP BY TR.TEST_ROSTER_ID,
                                                         tr.org_node_id) LIC_DATA
                                        WHERE ORG.ORG_NODE_ID =
                                              LIC_DATA.ORG_NODE_ID(+)
                                          AND ORG.CUSTOMER_ID = IN_CUSTOMER_ID
                                          AND ORG.ACTIVATION_STATUS = 'AC'
                                        GROUP BY ORG.ORG_NODE_ID,
                                                 ORG.ORG_NODE_NAME,
                                                 ORG.ORG_NODE_CATEGORY_ID) DERIVED1,
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
                                                      ITEM_SET                ISET
                                                WHERE TA.TEST_ADMIN_ID =
                                                      TR.TEST_ADMIN_ID
                                                  AND TR.TEST_ROSTER_ID =
                                                      SISS.TEST_ROSTER_ID
                                                  AND SISS.ITEM_SET_ID =
                                                      ISP.ITEM_SET_ID
                                                  AND ISP.PARENT_ITEM_SET_ID =
                                                      ISET.ITEM_SET_ID
                                                  AND ISET.ITEM_SET_TYPE = 'TS'
                                                  AND ISET.SAMPLE = 'F'
                                                  AND ISET.ITEM_SET_LEVEL != 'L'
                                                  AND TA.TEST_ADMIN_STATUS IN
                                                      ('CU', 'FU')
                                                  AND TA.ACTIVATION_STATUS = 'AC'
                                                  AND TR.ACTIVATION_STATUS = 'AC'
                                                  AND TR.TEST_COMPLETION_STATUS IN
                                                      ('SC', 'IC')
                                                  AND SISS.COMPLETION_STATUS = 'SC'
                                                  AND TRUNC(TA.CREATED_DATE_TIME) BETWEEN
                                                      EXTRACT_START_DATE AND
                                                      EXTRACT_END_DATE
                                                  AND TA.CUSTOMER_ID =
                                                      IN_CUSTOMER_ID
                                                GROUP BY TR.TEST_ROSTER_ID,
                                                         tr.org_node_id) LIC_DATA
                                        WHERE ORG.ORG_NODE_ID =
                                              LIC_DATA.ORG_NODE_ID(+)
                                          AND ORG.CUSTOMER_ID = IN_CUSTOMER_ID
                                          AND ORG.ACTIVATION_STATUS = 'AC'
                                        GROUP BY ORG.ORG_NODE_ID,
                                                 ORG.ORG_NODE_NAME,
                                                 ORG.ORG_NODE_CATEGORY_ID) DERIVED2,
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
                                                      ITEM_SET                ISET
                                                WHERE TA.TEST_ADMIN_ID =
                                                      TR.TEST_ADMIN_ID
                                                  AND TR.TEST_ROSTER_ID =
                                                      SISS.TEST_ROSTER_ID
                                                  AND SISS.ITEM_SET_ID =
                                                      ISP.ITEM_SET_ID
                                                  AND ISP.PARENT_ITEM_SET_ID =
                                                      ISET.ITEM_SET_ID
                                                  AND ISET.ITEM_SET_TYPE = 'TS'
                                                  AND ISET.SAMPLE = 'F'
                                                  AND ISET.ITEM_SET_LEVEL != 'L'
                                                  AND TA.TEST_ADMIN_STATUS IN
                                                      ('CU', 'PA')
                                                  AND TA.ACTIVATION_STATUS = 'AC'
                                                  AND TR.ACTIVATION_STATUS = 'AC'
                                                  AND TR.TEST_COMPLETION_STATUS NOT IN
                                                      ('SC', 'NT')
                                                  AND SISS.COMPLETION_STATUS NOT IN
                                                      ('SC', 'NT')
                                                  AND TRUNC(SISS.START_DATE_TIME) BETWEEN
                                                      EXTRACT_START_DATE AND
                                                      EXTRACT_END_DATE
                                                  AND TA.CUSTOMER_ID =
                                                      IN_CUSTOMER_ID
                                                GROUP BY TR.TEST_ROSTER_ID,
                                                         tr.org_node_id) LIC_DATA
                                        WHERE ORG.ORG_NODE_ID =
                                              LIC_DATA.ORG_NODE_ID(+)
                                          AND ORG.CUSTOMER_ID = IN_CUSTOMER_ID
                                          AND ORG.ACTIVATION_STATUS = 'AC'
                                        GROUP BY ORG.ORG_NODE_ID,
                                                 ORG.ORG_NODE_NAME,
                                                 ORG.ORG_NODE_CATEGORY_ID) DERIVED3,
                                      ORG_NODE_CATEGORY ONC,
                                      ORG_NODE_PARENT ONP,
                                      ORG_NODE PORG
                                WHERE DERIVED1.ORG_NODE_ID =
                                      DERIVED2.ORG_NODE_ID
                                  AND DERIVED3.ORG_NODE_ID =
                                      DERIVED2.ORG_NODE_ID
                                  AND DERIVED1.ORG_NODE_CATEGORY_ID =
                                      ONC.ORG_NODE_CATEGORY_ID
                                  AND ONC.CUSTOMER_ID = IN_CUSTOMER_ID
                                  AND ONP.ORG_NODE_ID = DERIVED1.ORG_NODE_ID
                                  AND ONP.PARENT_ORG_NODE_ID =
                                      PORG.ORG_NODE_ID) DERIVED4
                         LEFT OUTER JOIN CUSTOMER_ORGNODE_LICENSE COL ON DERIVED4.ORG_NODE_ID =
                                                                         COL.ORG_NODE_ID
                                                                     AND COL.CUSTOMER_ID =
                                                                         IN_CUSTOMER_ID
                         LEFT OUTER JOIN CUSTOMER_PURCHASE_REPORT TIER2 ON DERIVED4.ORG_NODE_ID =
                                                                           TIER2.ORG_NODE_ID
                                                                       AND TIER2.ACTIVATION_STATUS = 'AC'
                                                                       AND TIER2.CUSTOMER_ID =
                                                                           IN_CUSTOMER_ID
                       CONNECT BY NOCYCLE PRIOR DERIVED4.ORG_NODE_ID =
                                   DERIVED4.PARENT_ID) DERIVED
                WHERE DERIVED.LEV = 1) D);
  
  END INSERT_SUBTEST_LEVEL_DATA;

  /**
  * Creates framework product.
  */
  PROCEDURE EXTRACT_CUST_LM_COUNT(IN_CUSTOMER_ID     INTEGER,
                                  EXTRACT_START_DATE DATE,
                                  EXTRACT_END_DATE   DATE,
                                  IN_LM_DOWM_DATE    DATE,
                                  IN_SUBTEST_MODEL   VARCHAR2) IS
    -- exception declare
    NO_CUSTOMER EXCEPTION;
    INPUT_INVALID EXCEPTION;
    SUBTEST_MODEL_MISMATCH EXCEPTION;
    SUBTEST_MODEL_INFO EXCEPTION;
  
    LM_DOWM_DATE DATE := IN_LM_DOWM_DATE; -- date from which license system went down.
  
    --variable declare
    V_CUST_COUNT          INTEGER := 0;
    V_COL_COUNT           INTEGER := 0;
    V_TIER2_MODEL_COUNT   INTEGER := 0;
    V_FINAL_SUBTEST_MODEL VARCHAR2(4) := '';
    V_COL_MODEL           VARCHAR2(4) := '';
    V_TIER2_MODEL         VARCHAR2(4) := '';
    V_LOG_MESSAGE         VARCHAR2(450) := '';
    V_INPUTS              VARCHAR2(500) := '';
  BEGIN
  
    V_INPUTS := 'IN_CUSTOMER_ID:' || IN_CUSTOMER_ID ||
                ' | EXTRACT_START_DATE:' || EXTRACT_START_DATE ||
                ' | EXTRACT_END_DATE:' || EXTRACT_END_DATE ||
                ' | IN_LM_DOWM_DATE:' || IN_LM_DOWM_DATE ||
                ' | IN_SUBTEST_MODEL:' || IN_SUBTEST_MODEL;
  
    --dbms_output.put_line('V_INPUTS >> '||V_INPUTS); 
    /* INPUT MISSING */
    IF IN_CUSTOMER_ID IS NULL OR EXTRACT_START_DATE IS NULL OR
       EXTRACT_END_DATE IS NULL OR IN_LM_DOWM_DATE IS NULL THEN
      V_LOG_MESSAGE := 'Proper input is missing.Process stopped.. ';
      RAISE INPUT_INVALID;
    END IF;
  
    /**
    * CHECK IF CUSTOMER IS EXISTING IN OAS DATABASE.IF NOT RAISE An EXCEPTION
    **/
    SELECT COUNT(CUSTOMER_ID)
      INTO V_CUST_COUNT
      FROM CUSTOMER
     WHERE CUSTOMER_ID = IN_CUSTOMER_ID;
  
    IF V_CUST_COUNT = 0 THEN
      V_LOG_MESSAGE := 'No customer present in oas database.Process stopped.. ';
      RAISE NO_CUSTOMER;
    END IF;
  
    /**
    * CHECK IF CUSTOMER IS a TABE Customer
    **/
  
    SELECT COUNT(CUSTOMER_ID)
      INTO V_CUST_COUNT
      FROM Customer_Configuration
     WHERE CUSTOMER_ID = IN_CUSTOMER_ID
       AND CUSTOMER_CONFIGURATION_NAME = 'TABE_Customer';
  
    IF V_CUST_COUNT = 0 THEN
      V_LOG_MESSAGE := 'TABE_Customer configuration not present in oas database.Process stopped.. ';
      RAISE NO_CUSTOMER;
    END IF;
  
    /**
    * DETERMINE THE LICENSE MODEL FOR THE CUSTOMER
    **/
    -- CHECK IF COL DATA IS PRESENT
    SELECT COUNT(1)
      INTO V_COL_COUNT
      FROM CUSTOMER_ORGNODE_LICENSE
     WHERE CUSTOMER_ID = IN_CUSTOMER_ID
       AND ROWNUM = 1;
  
    --  GET SUBTEST MODEL FROM COL
    IF V_COL_COUNT <> 0 THEN
      SELECT SUBTEST_MODEL
        INTO V_COL_MODEL
        FROM CUSTOMER_ORGNODE_LICENSE
       WHERE CUSTOMER_ID = IN_CUSTOMER_ID
         AND ROWNUM = 1;
    END IF;
  
    -- CHECK IF TIER2 DATA IS PRESENT FOR SUBTEST_MODEL
    SELECT COUNT(1)
      INTO V_TIER2_MODEL_COUNT
      FROM CUSTOMER_PURCHASE_REPORT
     WHERE CUSTOMER_ID = IN_CUSTOMER_ID
       AND SUBTEST_MODEL IS NOT NULL
       AND ROWNUM = 1;
  
    --GET SUBTEST MODEL FROM TIER2 DATA
    IF V_TIER2_MODEL_COUNT <> 0 THEN
      SELECT SUBTEST_MODEL
        INTO V_TIER2_MODEL
        FROM CUSTOMER_PURCHASE_REPORT
       WHERE CUSTOMER_ID = IN_CUSTOMER_ID
         AND SUBTEST_MODEL IS NOT NULL
         AND ROWNUM = 1;
    END IF;
  
    IF V_COL_COUNT <> 0 AND V_TIER2_MODEL_COUNT <> 0 THEN
      /*If license model is present in col and tier2 data.Then both should be same. */
      --dbms_output.put_line('V_COL_MODEL>> '||V_COL_MODEL); 
      --dbms_output.put_line('V_TIER2_MODEL>> '||V_TIER2_MODEL); 
      
      IF V_COL_MODEL <> V_TIER2_MODEL THEN
        RAISE SUBTEST_MODEL_MISMATCH;
      ELSE
        V_FINAL_SUBTEST_MODEL := V_COL_MODEL;  
      END IF;
    
    ELSIF V_COL_COUNT = 0 AND V_TIER2_MODEL_COUNT = 0 THEN
      /*If license model is not present in COL and Tier2 data.Then get the model from input.
      * If in this case input value is not present, then raise an error.
      */
      IF IN_SUBTEST_MODEL IS NULL THEN
        V_LOG_MESSAGE := 'Subtest model info mismatch.Process stopped.. ';
        RAISE SUBTEST_MODEL_INFO;
      ELSE
        V_FINAL_SUBTEST_MODEL := IN_SUBTEST_MODEL;
      END IF;
    
    ELSIF V_COL_COUNT <> 0 THEN
      V_FINAL_SUBTEST_MODEL := V_COL_MODEL;
    ELSIF V_TIER2_MODEL_COUNT <> 0 THEN
      V_FINAL_SUBTEST_MODEL := V_TIER2_MODEL;
    END IF;
    /** 
    ** DETERMINE THE LICENSE MODEL FOR THE CUSTOMER completed
    **/
  
    ------
    /**
     * FINAL PROCESS STARTS FOR CALCULATION.
    **/
    --dbms_output.put_line('V_FINAL_SUBTEST_MODEL >> '||V_FINAL_SUBTEST_MODEL);
    IF V_FINAL_SUBTEST_MODEL = 'T' THEN
      /**
       * FOR SUBTEST MODEL
      **/
    --dbms_output.put_line('V_FINAL_SUBTEST_MODEL >> '||V_FINAL_SUBTEST_MODEL);
      INSERT_SUBTEST_LEVEL_DATA(IN_CUSTOMER_ID,
                                EXTRACT_START_DATE,
                                EXTRACT_END_DATE,
                                LM_DOWM_DATE);
    
    ELSIF V_FINAL_SUBTEST_MODEL = 'F' THEN
      /**
       * FOR SESSION MODEL
      **/
      --dbms_output.put_line('V_FINAL_SUBTEST_MODEL >> '||V_FINAL_SUBTEST_MODEL);
      INSERT_SESSION_LEVEL_DATA(IN_CUSTOMER_ID,
                                EXTRACT_START_DATE,
                                EXTRACT_END_DATE,
                                LM_DOWM_DATE);
    
    END IF;
    V_LOG_MESSAGE := 'Extract Process completed successfully..';
    COMMIT;
  
    --LOG THE INFORMATION
    LOG_MESSAGE(IN_CUSTOMER_ID, V_LOG_MESSAGE || ' - ' || V_INPUTS);
    DBMS_OUTPUT.PUT_LINE('Extract Process completed successfully..');
  
  EXCEPTION
    WHEN INPUT_INVALID THEN
      LOG_MESSAGE(IN_CUSTOMER_ID,
                  V_LOG_MESSAGE || '-' ||
                  DBMS_UTILITY.FORMAT_ERROR_BACKTRACE || '-' || V_INPUTS);
      RAISE_APPLICATION_ERROR(-20004, 'Input is missing');
    WHEN NO_CUSTOMER THEN
      LOG_MESSAGE(IN_CUSTOMER_ID,
                  V_LOG_MESSAGE || '-' ||
                  DBMS_UTILITY.FORMAT_ERROR_BACKTRACE || '-' || V_INPUTS);
      RAISE_APPLICATION_ERROR(-20000,
                              'Customer_id NOT EXISTING IN OAS / Not A TABE Customer');
    WHEN SUBTEST_MODEL_MISMATCH THEN
      LOG_MESSAGE(IN_CUSTOMER_ID,
                  V_LOG_MESSAGE || '-' ||
                  DBMS_UTILITY.FORMAT_ERROR_BACKTRACE || '-' || V_INPUTS);
      RAISE_APPLICATION_ERROR(-20001,
                              'Subtset model from COL table and Tier2 data is not same.');
    WHEN SUBTEST_MODEL_INFO THEN
      LOG_MESSAGE(IN_CUSTOMER_ID,
                  V_LOG_MESSAGE || '-' ||
                  DBMS_UTILITY.FORMAT_ERROR_BACKTRACE || '-' || V_INPUTS);
      RAISE_APPLICATION_ERROR(-20002,
                              'Subtset model input value is needed.As data is not present in COL and Tier2 data.');
    WHEN OTHERS THEN
      LOG_MESSAGE(IN_CUSTOMER_ID,
                  V_LOG_MESSAGE || '-' ||
                  DBMS_UTILITY.FORMAT_ERROR_BACKTRACE || '-' || V_INPUTS);
      ROLLBACK;
    
  END EXTRACT_CUST_LM_COUNT;

END LM_OUTAGE_REPORT_EXTRACT;
/
