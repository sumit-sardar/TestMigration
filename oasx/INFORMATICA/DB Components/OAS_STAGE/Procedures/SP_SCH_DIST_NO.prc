CREATE OR REPLACE PROCEDURE SP_SCH_DIST_NO(in_CUST_ID IN INTEGER,in_STUD_ID IN INTEGER,o_DST_CODE OUT VARCHAR2,o_SCH_CODE OUT VARCHAR2 ,o_DST_NAME OUT VARCHAR2,o_SCH_NAME OUT VARCHAR2)
IS
V_SCHOOL_CD  VARCHAR2(32);
V_SCHOOL_NAME  VARCHAR2(64);
V_DIST_CD  VARCHAR2(32);
V_DIST_NAME  VARCHAR2(64);
V_DIST_CATG_ID NUMBER(32);
V_SCH_CATG_ID  NUMBER(32);
V_ORG_NODE_ID  NUMBER(32);
BEGIN
 V_SCHOOL_CD:=NULL;
 V_SCHOOL_NAME:=NULL;
 V_DIST_CD:=NULL;
 V_DIST_NAME:=NULL;
 select COUNT(*) INTO V_DIST_CATG_ID from org_node_category where UPPER(category_name)='DISTRICT' and
 customer_id=in_CUST_ID;
 select COUNT(*) INTO V_SCH_CATG_ID from org_node_category where UPPER(category_name)='SCHOOL' and  customer_id=in_CUST_ID;
 select COUNT(*) INTO V_ORG_NODE_ID FROM org_node_student where student_id=in_STUD_ID AND CUSTOMER_ID=in_CUST_ID;
 IF (V_ORG_NODE_ID>=1 AND V_SCH_CATG_ID=1) THEN
  select org_node_code,org_node_name INTO V_SCHOOL_CD ,V_SCHOOL_NAME from org_node where org_node_id in (select ORG_NODE_ID from org_node_parent connect by prior PARENT_ORG_NODE_ID=ORG_NODE_ID start with ORG_NODE_ID IN (select ORG_NODE_ID from org_node_student where student_id=in_STUD_ID AND CUSTOMER_ID=in_CUST_ID))
and org_node_category_id = (select org_node_category_id from org_node_category where UPPER(category_name)='SCHOOL' and
customer_id=in_CUST_ID);
 END IF;
 IF (V_ORG_NODE_ID>=1 AND V_DIST_CATG_ID=1) THEN
  select org_node_code,org_node_name INTO V_DIST_CD ,V_DIST_NAME from org_node where org_node_id in (select ORG_NODE_ID from org_node_parent connect by prior PARENT_ORG_NODE_ID=ORG_NODE_ID start with ORG_NODE_ID IN (select ORG_NODE_ID from org_node_student where student_id=in_STUD_ID AND CUSTOMER_ID=in_CUST_ID))
and org_node_category_id = (select org_node_category_id from org_node_category where UPPER(category_name)='DISTRICT' and
customer_id=in_CUST_ID);
 END IF;
o_DST_CODE:=V_DIST_CD;
o_SCH_CODE:=V_SCHOOL_CD;
o_DST_NAME:=V_DIST_NAME;
o_SCH_NAME:=V_SCHOOL_NAME;
END SP_SCH_DIST_NO;
/
