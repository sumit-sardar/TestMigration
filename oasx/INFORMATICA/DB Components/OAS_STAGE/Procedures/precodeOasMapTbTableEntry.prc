create or replace procedure precodeOasMapTbTableEntry (Customer_id) as
 
declare
    v_customer_id integer;
begin

    v_customer_id :=35502;



insert into PRECODE_OAS_MAP_TB values( v_customer_id,'Section 504','Demographic','STUDENT_DEMOGRAPHIC_VALUE','VALUE_NAME','',1,1);

insert into PRECODE_OAS_MAP_TB values( v_customer_id,'Primary Exceptionality','Demographic','STUDENT_DEMOGRAPHIC_VALUE','VALUE_NAME','',2,13);


insert into PRECODE_OAS_MAP_TB values( v_customer_id,'Pre-ID Ethnicity','Demographic','STUDENT_DEMOGRAPHIC_VALUE','VALUE_NAME','',1,14);

insert into PRECODE_OAS_MAP_TB values( v_customer_id,'Special Education (IEP)','Demographic','STUDENT_DEMOGRAPHIC_VALUE','VALUE_NAME','',1,19);

insert into PRECODE_OAS_MAP_TB values( v_customer_id,'English Learner (EL) Student','Demographic','STUDENT_DEMOGRAPHIC_VALUE','VALUE_NAME','',1,20);

insert into PRECODE_OAS_MAP_TB values( v_customer_id,'Free or Reduced Price Meals','Demographic','STUDENT_DEMOGRAPHIC_VALUE','VALUE_NAME','',1,21);

insert into PRECODE_OAS_MAP_TB values( v_customer_id,'Migrant','Demographic','STUDENT_DEMOGRAPHIC_VALUE','VALUE_NAME','',1,22);

insert into PRECODE_OAS_MAP_TB values( v_customer_id,'English/Language Arts Accommodations','Demographic','STUDENT_DEMOGRAPHIC_VALUE','VALUE_NAME','',1,23);

insert into PRECODE_OAS_MAP_TB values( v_customer_id,'Mathematics Accommodations','Demographic','STUDENT_DEMOGRAPHIC_VALUE','VALUE_NAME','',1,24);

insert into PRECODE_OAS_MAP_TB values( v_customer_id,'Science Accommodations','Demographic','STUDENT_DEMOGRAPHIC_VALUE','VALUE_NAME','',1,25);

insert into PRECODE_OAS_MAP_TB values( v_customer_id,'Social Studies Accommodations','Demographic','STUDENT_DEMOGRAPHIC_VALUE','VALUE_NAME','',1,26);

insert into PRECODE_OAS_MAP_TB values( v_customer_id,'Special Codes: R (Local Use Optional)','Demographic','STUDENT_DEMOGRAPHIC_VALUE','VALUE_NAME','',1,27);

insert into PRECODE_OAS_MAP_TB values( v_customer_id,'Special Codes: S (Local Use Optional)','Demographic','STUDENT_DEMOGRAPHIC_VALUE','VALUE_NAME','',1,28);

insert into PRECODE_OAS_MAP_TB values( v_customer_id,'Special Codes: T (Local Use Optional)','Demographic','STUDENT_DEMOGRAPHIC_VALUE','VALUE_NAME','',1,29);


insert into PRECODE_OAS_MAP_TB values( v_customer_id,'Level 1 Code','Hierarchy','ORG_NODE','ORG_NODE_CODE',4,38,'');
insert into PRECODE_OAS_MAP_TB values( v_customer_id,'Level 1 Name','Hierarchy','ORG_NODE','ORG_NODE_NAME',4,64,'');


insert into PRECODE_OAS_MAP_TB values( v_customer_id,'Level 2 Code','Hierarchy','ORG_NODE','ORG_NODE_CODE',3,38,'');
insert into PRECODE_OAS_MAP_TB values( v_customer_id,'Level 2 Name','Hierarchy','ORG_NODE','ORG_NODE_NAME',3,64,'');


insert into PRECODE_OAS_MAP_TB values( v_customer_id,'Level 3 Code','Hierarchy','ORG_NODE','ORG_NODE_CODE',2,38,'');
insert into PRECODE_OAS_MAP_TB values( v_customer_id,'Level 3 Name','Hierarchy','ORG_NODE','ORG_NODE_NAME',2,64,'');



END;
/
