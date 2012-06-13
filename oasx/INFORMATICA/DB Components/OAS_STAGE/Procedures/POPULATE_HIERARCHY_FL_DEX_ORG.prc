create or replace procedure POPULATE_HIERARCHY_FL_DEX_ORG(
                                               
                                                 in_customer_id in org_node.customer_id%type
                                                ) is

  

  

v_org_node_name   org_node.org_node_name%type;
state_code_id  org_node.org_node_name%type;

--ORGNODE_create boolean := false;
  cursor get_top_Org_Node_Id is
  
   SELECT org.org_node_id state_code_id,org.org_node_name orgNodename
FROM stg_param_verify_tb param,
org_node org WHERE 
org.org_node_id = param.org_node_id
and param.customer_id = in_customer_id;
   
   --cursor rowtype declaration
  get_top_Org_Node_Id_rec get_top_Org_Node_Id%rowtype;
  
  

begin


open get_top_Org_Node_Id;
  
      fetch get_top_Org_Node_Id into get_top_Org_Node_Id_rec;
      
      if get_top_Org_Node_Id%notfound then
      
        raise_application_error(-20001,
                                'The specified org_node_id does not exist');
      
      end if;
  
  close get_top_Org_Node_Id;
  
  state_code_id :=  get_top_Org_Node_Id_rec.State_Code_Id;
  v_org_node_name := get_top_Org_Node_Id_rec.Orgnodename;
  
   DBMS_OUTPUT.PUT_LINE('Create_level' || state_code_id);
   DBMS_OUTPUT.PUT_LINE('Create_level' || v_org_node_name);

--select org_node_name into v_org_node_name from org_node where org_node_id = v_top_org_node_id and customer_id=in_customer_id;

insert into stg_org_node_tb (CUSTOMER_ID,PARENT_ID,PARENT_NAME,CHILD_ID,CHILD_NAME,category_level,record_indicator) 

SELECT DISTINCT CUSTOMER_ID,PARENT_ID,PARENT,CHILD_ID,CHILD,LEVEL1,'U' FROM
(
SELECT CUSTOMER_ID,NVL(LEVEL_3_CODE,'0') CHILD_ID,LEVEL_3_NAME CHILD,NVL(LEVEL_4_CODE,NVL(LEVEL_5_CODE,NVL(LEVEL_6_CODE,state_code_id))) PARENT_ID,NVL(LEVEL_4_NAME,NVL(LEVEL_5_NAME,NVL(LEVEL_6_NAME,v_org_node_name))) PARENT,(SELECT HIERARCHY_LEVEL FROM PRECODE_OAS_MAP_TB WHERE UPPER(PRECODE_FIELD_NAME) =UPPER('Level 3 Code') AND CUSTOMER_ID =(SELECT CUSTOMER_ID FROM STG_PARAM_VERIFY_TB WHERE SNO=1)) LEVEL1 FROM STG_STUDENT_HIERARCHY_FL_dex WHERE LEVEL_3_CODE IS NOT NULL and record_indicator = 'U'
UNION
SELECT CUSTOMER_ID,NVL(LEVEL_2_CODE,'0') CHILD_ID,LEVEL_2_NAME CHILD,NVL(LEVEL_3_CODE,NVL(LEVEL_4_CODE,NVL(LEVEL_5_CODE,NVL(LEVEL_6_CODE,state_code_id)))) PARENT_ID,NVL(LEVEL_3_NAME,NVL(LEVEL_4_NAME,NVL(LEVEL_5_NAME,NVL(LEVEL_6_NAME,v_org_node_name)))) PARENT,(SELECT HIERARCHY_LEVEL FROM PRECODE_OAS_MAP_TB WHERE UPPER(PRECODE_FIELD_NAME) =UPPER('Level 2 Code') AND CUSTOMER_ID =(SELECT CUSTOMER_ID FROM STG_PARAM_VERIFY_TB WHERE SNO=1)) LEVEL1 FROM STG_STUDENT_HIERARCHY_FL_dex WHERE LEVEL_2_CODE IS NOT NULL and record_indicator = 'U'
UNION
SELECT CUSTOMER_ID,NVL(LEVEL_1_CODE,'0') CHILD_ID,LEVEL_1_NAME CHILD,NVL(LEVEL_2_CODE,NVL(LEVEL_3_CODE,NVL(LEVEL_4_CODE,NVL(LEVEL_5_CODE,NVL(LEVEL_6_CODE,state_code_id))))) PARENT_ID,NVL(LEVEL_2_NAME,NVL(LEVEL_3_NAME,NVL(LEVEL_4_NAME,NVL(LEVEL_5_NAME,NVL(LEVEL_6_NAME,v_org_node_name))))) PARENT,(SELECT HIERARCHY_LEVEL FROM PRECODE_OAS_MAP_TB WHERE UPPER(PRECODE_FIELD_NAME) =UPPER('Level 1 Code') AND CUSTOMER_ID =(SELECT CUSTOMER_ID FROM STG_PARAM_VERIFY_TB WHERE SNO=1)) LEVEL1 FROM STG_STUDENT_HIERARCHY_FL_dex WHERE LEVEL_1_CODE IS NOT NULL and record_indicator = 'U') 
START WITH PARENT_ID=state_code_id
CONNECT BY PRIOR CHILD_ID=PARENT_ID
ORDER BY CUSTOMER_ID;

commit;

end POPULATE_HIERARCHY_FL_DEX_ORG;
/
