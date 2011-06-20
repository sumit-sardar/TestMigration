create or replace procedure setup_laslink_customer (cust_id integer)
as
    cc_id integer;
    counter integer;
begin

    -- customer_configuration
    delete from customer_configuration_value where customer_configuration_id in (
        select customer_configuration_id from customer_configuration where customer_id = cust_id
    );
    
    delete from customer_configuration where customer_id = cust_id;
    
Insert into OAS.CUSTOMER_CONFIGURATION
   (CUSTOMER_CONFIGURATION_ID, CUSTOMER_CONFIGURATION_NAME, CUSTOMER_ID, EDITABLE, DEFAULT_VALUE, CREATED_BY, CREATED_DATE_TIME)
 Values
   (seq_customer_configuration_id.nextval, 'Screen_Reader', cust_id, 'F', 
    'F', 1, TO_DATE('04/12/2011 17:46:39', 'MM/DD/YYYY HH24:MI:SS'));
Insert into OAS.CUSTOMER_CONFIGURATION
   (CUSTOMER_CONFIGURATION_ID, CUSTOMER_CONFIGURATION_NAME, CUSTOMER_ID, EDITABLE, DEFAULT_VALUE, CREATED_BY, CREATED_DATE_TIME)
 Values
   (seq_customer_configuration_id.nextval, 'Highlighter', cust_id, 'T', 
    'T', 1, TO_DATE('04/12/2011 17:46:39', 'MM/DD/YYYY HH24:MI:SS'));
--Insert into OAS.CUSTOMER_CONFIGURATION
--   (CUSTOMER_CONFIGURATION_ID, CUSTOMER_CONFIGURATION_NAME, CUSTOMER_ID, EDITABLE, DEFAULT_VALUE, CREATED_BY, CREATED_DATE_TIME)
-- Values
--   (seq_customer_configuration_id.nextval, 'Session_Details_Show_Scores', cust_id, 'T', 
--    'T', 1, TO_DATE('04/12/2011 17:44:38', 'MM/DD/YYYY HH24:MI:SS'));
Insert into OAS.CUSTOMER_CONFIGURATION
   (CUSTOMER_CONFIGURATION_ID, CUSTOMER_CONFIGURATION_NAME, CUSTOMER_ID, EDITABLE, DEFAULT_VALUE, CREATED_BY, CREATED_DATE_TIME)
 Values
   (seq_customer_configuration_id.nextval, 'Calculator', cust_id, 'T', 
    'F', 1, TO_DATE('04/12/2011 17:41:50', 'MM/DD/YYYY HH24:MI:SS'));
Insert into OAS.CUSTOMER_CONFIGURATION
   (CUSTOMER_CONFIGURATION_ID, CUSTOMER_CONFIGURATION_NAME, CUSTOMER_ID, EDITABLE, DEFAULT_VALUE, CREATED_BY, CREATED_DATE_TIME)
 Values
   (seq_customer_configuration_id.nextval, 'Test_Pause', cust_id, 'T', 
    'F', 1, TO_DATE('04/12/2011 19:02:12', 'MM/DD/YYYY HH24:MI:SS'));
Insert into OAS.CUSTOMER_CONFIGURATION
   (CUSTOMER_CONFIGURATION_ID, CUSTOMER_CONFIGURATION_NAME, CUSTOMER_ID, EDITABLE, DEFAULT_VALUE, CREATED_BY, CREATED_DATE_TIME)
 Values
   (seq_customer_configuration_id.nextval, 'LASLINK_Customer', cust_id, 'T', 
    'T', 1, TO_DATE('04/12/2011 17:24:57', 'MM/DD/YYYY HH24:MI:SS'));
    
Insert into OAS.CUSTOMER_CONFIGURATION
   (CUSTOMER_CONFIGURATION_ID, CUSTOMER_CONFIGURATION_NAME, CUSTOMER_ID, EDITABLE, DEFAULT_VALUE, CREATED_BY, CREATED_DATE_TIME)
 Values
   (seq_customer_configuration_id.nextval, 'LL_Customer', cust_id, 'T', 
    'T', 1, TO_DATE('04/12/2011 17:24:57', 'MM/DD/YYYY HH24:MI:SS'));
    
Insert into OAS.CUSTOMER_CONFIGURATION
   (CUSTOMER_CONFIGURATION_ID, CUSTOMER_CONFIGURATION_NAME, CUSTOMER_ID, EDITABLE, DEFAULT_VALUE, CREATED_BY, CREATED_DATE_TIME)
 Values
   (seq_customer_configuration_id.nextval, 'Allow_Upload_Download', cust_id, 'T', 
    'T', 1, TO_DATE('04/12/2011 17:24:57', 'MM/DD/YYYY HH24:MI:SS'));
   
Insert into OAS.CUSTOMER_CONFIGURATION
   (CUSTOMER_CONFIGURATION_ID, CUSTOMER_CONFIGURATION_NAME, CUSTOMER_ID, EDITABLE, DEFAULT_VALUE, CREATED_BY, CREATED_DATE_TIME)
 Values
   (seq_customer_configuration_id.nextval, 'Session_Status_Student_Reports', cust_id, 'T', 
    'T', 1, TO_DATE('04/12/2011 17:24:57', 'MM/DD/YYYY HH24:MI:SS'));     
    
--Insert into OAS.CUSTOMER_CONFIGURATION
--   (CUSTOMER_CONFIGURATION_ID, CUSTOMER_CONFIGURATION_NAME, CUSTOMER_ID, EDITABLE, DEFAULT_VALUE, CREATED_BY, CREATED_DATE_TIME)
-- Values
--   (seq_customer_configuration_id.nextval, 'Allow_Subscription', cust_id, 'T', 
--    'T', 1, TO_DATE('04/12/2011 17:24:57', 'MM/DD/YYYY HH24:MI:SS'));  
    
--Insert into OAS.CUSTOMER_CONFIGURATION
--   (CUSTOMER_CONFIGURATION_ID, CUSTOMER_CONFIGURATION_NAME, CUSTOMER_ID, EDITABLE, DEFAULT_VALUE, CREATED_BY, CREATED_DATE_TIME)
-- Values
--   (seq_customer_configuration_id.nextval, 'License_Email_Notification', cust_id, 'T', 
--    '20', 1, TO_DATE('04/12/2011 17:24:57', 'MM/DD/YYYY HH24:MI:SS'));   
    
 Insert into OAS.CUSTOMER_CONFIGURATION
   (CUSTOMER_CONFIGURATION_ID, CUSTOMER_CONFIGURATION_NAME, CUSTOMER_ID, EDITABLE, DEFAULT_VALUE, CREATED_BY, CREATED_DATE_TIME)
 Values
   (seq_customer_configuration_id.nextval, 'Enable_Multiple_TestTicket', cust_id, 'T', 
    'T', 1, TO_DATE('04/12/2011 17:24:57', 'MM/DD/YYYY HH24:MI:SS'));  
      
Insert into OAS.CUSTOMER_CONFIGURATION
   (CUSTOMER_CONFIGURATION_ID, CUSTOMER_CONFIGURATION_NAME, CUSTOMER_ID, EDITABLE, DEFAULT_VALUE, CREATED_BY, CREATED_DATE_TIME)
 Values
   (seq_customer_configuration_id.nextval, 'Configurable_Hand_Scoring', cust_id, 'T', 
    'T', 1, TO_DATE('04/12/2011 17:24:57', 'MM/DD/YYYY HH24:MI:SS'));  
    
Insert into OAS.CUSTOMER_CONFIGURATION
   (CUSTOMER_CONFIGURATION_ID, CUSTOMER_CONFIGURATION_NAME, CUSTOMER_ID, EDITABLE, DEFAULT_VALUE, CREATED_BY, CREATED_DATE_TIME)
 Values
   (seq_customer_configuration_id.nextval, 'Configurable_Bulk_Accommodation', cust_id, 'T', 
    'T', 1, TO_DATE('04/12/2011 17:24:57', 'MM/DD/YYYY HH24:MI:SS'));        
 
 Insert into OAS.CUSTOMER_CONFIGURATION
   (CUSTOMER_CONFIGURATION_ID, CUSTOMER_CONFIGURATION_NAME, CUSTOMER_ID, EDITABLE, DEFAULT_VALUE, CREATED_BY, CREATED_DATE_TIME)
 Values
   (seq_customer_configuration_id.nextval, 'Allow_Reopen_Subtest', cust_id, 'T', 
    'T', 1, TO_DATE('04/12/2011 17:24:57', 'MM/DD/YYYY HH24:MI:SS'));  
    
 Insert into OAS.CUSTOMER_CONFIGURATION
   (CUSTOMER_CONFIGURATION_ID, CUSTOMER_CONFIGURATION_NAME, CUSTOMER_ID, EDITABLE, DEFAULT_VALUE, CREATED_BY, CREATED_DATE_TIME)
 Values
   (seq_customer_configuration_id.nextval, 'Allow_Subtest_Invalidation', cust_id, 'T', 
    'T', 1, TO_DATE('04/12/2011 17:24:57', 'MM/DD/YYYY HH24:MI:SS'));     
 
--Insert into OAS.CUSTOMER_CONFIGURATION
--   (CUSTOMER_CONFIGURATION_ID, CUSTOMER_CONFIGURATION_NAME, CUSTOMER_ID, EDITABLE, DEFAULT_VALUE, CREATED_BY, CREATED_DATE_TIME)
-- Values
--   (seq_customer_configuration_id.nextval, 'TMS_Url_Configurable', cust_id, 'T', 
--    'T', 1, TO_DATE('04/12/2011 17:24:57', 'MM/DD/YYYY HH24:MI:SS'));  
    
select seq_customer_configuration_id.nextval into cc_id from dual;

Insert into OAS.CUSTOMER_CONFIGURATION
   (CUSTOMER_CONFIGURATION_ID, CUSTOMER_CONFIGURATION_NAME, CUSTOMER_ID, EDITABLE, DEFAULT_VALUE, CREATED_BY, CREATED_DATE_TIME)
 Values
   (cc_id, 'Grade', cust_id, 'T', 
    'T', 1, TO_DATE('04/12/2011 23:43:32', 'MM/DD/YYYY HH24:MI:SS'));
    
  Insert into OAS.CUSTOMER_CONFIGURATION_VALUE
     (CUSTOMER_CONFIGURATION_VALUE, CUSTOMER_CONFIGURATION_ID, SORT_ORDER)
   Values
     ('KG', cc_id, NULL); 
     
  for counter in 1..12
  loop
      insert into OAS.CUSTOMER_CONFIGURATION_VALUE (CUSTOMER_CONFIGURATION_VALUE, CUSTOMER_CONFIGURATION_ID, SORT_ORDER) 
      values (counter, cc_id, NULL);   
  end loop;
   
    
-- customer_demographics
delete from customer_demographic_value where customer_demographic_id in (
    select customer_demographic_id from customer_demographic where customer_id = cust_id
);

delete from customer_demographic where customer_id = cust_id;
   

assign_laslink_demographics(cust_id);

delete from customer_email_config where customer_id = cust_id;
   
assign_laslink_email_config(cust_id);
 
  
   
-- 1 org_node_test_catalog for product 7001

delete from org_node_test_catalog where customer_id = cust_id and product_id in (7001);
   
insert into org_node_test_catalog (TEST_CATALOG_ID, ORG_NODE_ID, CREATED_BY, CREATED_DATE_TIME, UPDATED_BY, UPDATED_DATE_TIME,
 CUSTOMER_ID, ACTIVATION_STATUS, ITEM_SET_ID, PRODUCT_ID, OVERRIDE_NO_RETAKE, OVERRIDE_FORM_ASSIGNMENT, OVERRIDE_LOGIN_START_DATE,
  LIC_PURCHASED, LIC_RESERVED, LIC_USED, RANDOM_DISTRACTOR_ALLOWABLE) (
select distinct 
   13196 as test_catalog_id, org.org_node_id, 1 as created_by,
    sysdate as created_date_time, null as updated_by, null as updated_date_time,
    org.customer_id as customer_id, 'AC' as activation_status, 29077 as item_set_id, '7001' as product_id, 
    'F' as override_no_retake, null as override_form_assignment, null as override_login_start_date,
    -1 as lic_purchased, -1 as lic_reserved, -1 as lic_used , null as random_distractor_allowable
from 
    org_node org
where 
    org.customer_id = cust_id);
    
insert into org_node_test_catalog (TEST_CATALOG_ID, ORG_NODE_ID, CREATED_BY, CREATED_DATE_TIME, UPDATED_BY, UPDATED_DATE_TIME,
 CUSTOMER_ID, ACTIVATION_STATUS, ITEM_SET_ID, PRODUCT_ID, OVERRIDE_NO_RETAKE, OVERRIDE_FORM_ASSIGNMENT, OVERRIDE_LOGIN_START_DATE,
  LIC_PURCHASED, LIC_RESERVED, LIC_USED, RANDOM_DISTRACTOR_ALLOWABLE) (
select distinct 
   13297 as test_catalog_id, org.org_node_id, 1 as created_by,
    sysdate as created_date_time, null as updated_by, null as updated_date_time,
    org.customer_id as customer_id, 'AC' as activation_status, 29355 as item_set_id, '7001' as product_id, 
    'F' as override_no_retake, null as override_form_assignment, null as override_login_start_date,
    -1 as lic_purchased, -1 as lic_reserved, -1 as lic_used , null as random_distractor_allowable
from 
    org_node org
where 
    org.customer_id = cust_id);
    
 insert into org_node_test_catalog (TEST_CATALOG_ID, ORG_NODE_ID, CREATED_BY, CREATED_DATE_TIME, UPDATED_BY, UPDATED_DATE_TIME,
 CUSTOMER_ID, ACTIVATION_STATUS, ITEM_SET_ID, PRODUCT_ID, OVERRIDE_NO_RETAKE, OVERRIDE_FORM_ASSIGNMENT, OVERRIDE_LOGIN_START_DATE,
  LIC_PURCHASED, LIC_RESERVED, LIC_USED, RANDOM_DISTRACTOR_ALLOWABLE) (
select distinct 
   13177 as test_catalog_id, org.org_node_id, 1 as created_by,
    sysdate as created_date_time, null as updated_by, null as updated_date_time,
    org.customer_id as customer_id, 'AC' as activation_status, 27775 as item_set_id, '7001' as product_id, 
    'F' as override_no_retake, null as override_form_assignment, null as override_login_start_date,
    -1 as lic_purchased, -1 as lic_reserved, -1 as lic_used , null as random_distractor_allowable
from 
    org_node org
where 
    org.customer_id = cust_id);
    
insert into org_node_test_catalog (TEST_CATALOG_ID, ORG_NODE_ID, CREATED_BY, CREATED_DATE_TIME, UPDATED_BY, UPDATED_DATE_TIME,
 CUSTOMER_ID, ACTIVATION_STATUS, ITEM_SET_ID, PRODUCT_ID, OVERRIDE_NO_RETAKE, OVERRIDE_FORM_ASSIGNMENT, OVERRIDE_LOGIN_START_DATE,
  LIC_PURCHASED, LIC_RESERVED, LIC_USED, RANDOM_DISTRACTOR_ALLOWABLE) (
select distinct 
   13256 as test_catalog_id, org.org_node_id, 1 as created_by,
    sysdate as created_date_time, null as updated_by, null as updated_date_time,
    org.customer_id as customer_id, 'AC' as activation_status, 29255 as item_set_id, '7001' as product_id, 
    'F' as override_no_retake, null as override_form_assignment, null as override_login_start_date,
    -1 as lic_purchased, -1 as lic_reserved, -1 as lic_used , null as random_distractor_allowable
from 
    org_node org
where 
    org.customer_id = cust_id);
    
    
    insert into org_node_test_catalog (TEST_CATALOG_ID, ORG_NODE_ID, CREATED_BY, CREATED_DATE_TIME, UPDATED_BY, UPDATED_DATE_TIME,
 CUSTOMER_ID, ACTIVATION_STATUS, ITEM_SET_ID, PRODUCT_ID, OVERRIDE_NO_RETAKE, OVERRIDE_FORM_ASSIGNMENT, OVERRIDE_LOGIN_START_DATE,
  LIC_PURCHASED, LIC_RESERVED, LIC_USED, RANDOM_DISTRACTOR_ALLOWABLE) (
select distinct 
   13276 as test_catalog_id, org.org_node_id, 1 as created_by,
    sysdate as created_date_time, null as updated_by, null as updated_date_time,
    org.customer_id as customer_id, 'AC' as activation_status, 29304 as item_set_id, '7001' as product_id, 
    'F' as override_no_retake, null as override_form_assignment, null as override_login_start_date,
    -1 as lic_purchased, -1 as lic_reserved, -1 as lic_used , null as random_distractor_allowable
from 
    org_node org
where 
    org.customer_id = cust_id);
    
    
    insert into org_node_test_catalog (TEST_CATALOG_ID, ORG_NODE_ID, CREATED_BY, CREATED_DATE_TIME, UPDATED_BY, UPDATED_DATE_TIME,
 CUSTOMER_ID, ACTIVATION_STATUS, ITEM_SET_ID, PRODUCT_ID, OVERRIDE_NO_RETAKE, OVERRIDE_FORM_ASSIGNMENT, OVERRIDE_LOGIN_START_DATE,
  LIC_PURCHASED, LIC_RESERVED, LIC_USED, RANDOM_DISTRACTOR_ALLOWABLE) (
select distinct 
   13236 as test_catalog_id, org.org_node_id, 1 as created_by,
    sysdate as created_date_time, null as updated_by, null as updated_date_time,
    org.customer_id as customer_id, 'AC' as activation_status, 29126 as item_set_id, '7001' as product_id, 
    'F' as override_no_retake, null as override_form_assignment, null as override_login_start_date,
    -1 as lic_purchased, -1 as lic_reserved, -1 as lic_used , null as random_distractor_allowable
from 
    org_node org
where 
    org.customer_id = cust_id);
    
    
    insert into org_node_test_catalog (TEST_CATALOG_ID, ORG_NODE_ID, CREATED_BY, CREATED_DATE_TIME, UPDATED_BY, UPDATED_DATE_TIME,
 CUSTOMER_ID, ACTIVATION_STATUS, ITEM_SET_ID, PRODUCT_ID, OVERRIDE_NO_RETAKE, OVERRIDE_FORM_ASSIGNMENT, OVERRIDE_LOGIN_START_DATE,
  LIC_PURCHASED, LIC_RESERVED, LIC_USED, RANDOM_DISTRACTOR_ALLOWABLE) (
select distinct 
   13296 as test_catalog_id, org.org_node_id, 1 as created_by,
    sysdate as created_date_time, null as updated_by, null as updated_date_time,
    org.customer_id as customer_id, 'AC' as activation_status, 29346 as item_set_id, '7001' as product_id, 
    'F' as override_no_retake, null as override_form_assignment, null as override_login_start_date,
    -1 as lic_purchased, -1 as lic_reserved, -1 as lic_used , null as random_distractor_allowable
from 
    org_node org
where 
    org.customer_id = cust_id);
    
    
 -- 2. org_node_test_catalog for product 7002

delete from org_node_test_catalog where customer_id = cust_id and product_id in (7002);
   
insert into org_node_test_catalog (TEST_CATALOG_ID, ORG_NODE_ID, CREATED_BY, CREATED_DATE_TIME, UPDATED_BY, UPDATED_DATE_TIME,
 CUSTOMER_ID, ACTIVATION_STATUS, ITEM_SET_ID, PRODUCT_ID, OVERRIDE_NO_RETAKE, OVERRIDE_FORM_ASSIGNMENT, OVERRIDE_LOGIN_START_DATE,
  LIC_PURCHASED, LIC_RESERVED, LIC_USED, RANDOM_DISTRACTOR_ALLOWABLE) (
select distinct 
   13216 as test_catalog_id, org.org_node_id, 1 as created_by,
    sysdate as created_date_time, null as updated_by, null as updated_date_time,
    org.customer_id as customer_id, 'AC' as activation_status, 29086 as item_set_id, '7002' as product_id, 
    'F' as override_no_retake, null as override_form_assignment, null as override_login_start_date,
    -1 as lic_purchased, -1 as lic_reserved, -1 as lic_used , null as random_distractor_allowable
from 
    org_node org
where 
    org.customer_id = cust_id);
    
    
    insert into org_node_test_catalog (TEST_CATALOG_ID, ORG_NODE_ID, CREATED_BY, CREATED_DATE_TIME, UPDATED_BY, UPDATED_DATE_TIME,
 CUSTOMER_ID, ACTIVATION_STATUS, ITEM_SET_ID, PRODUCT_ID, OVERRIDE_NO_RETAKE, OVERRIDE_FORM_ASSIGNMENT, OVERRIDE_LOGIN_START_DATE,
  LIC_PURCHASED, LIC_RESERVED, LIC_USED, RANDOM_DISTRACTOR_ALLOWABLE) (
select distinct 
   13356 as test_catalog_id, org.org_node_id, 1 as created_by,
    sysdate as created_date_time, null as updated_by, null as updated_date_time,
    org.customer_id as customer_id, 'AC' as activation_status, 29502 as item_set_id, '7002' as product_id, 
    'F' as override_no_retake, null as override_form_assignment, null as override_login_start_date,
    -1 as lic_purchased, -1 as lic_reserved, -1 as lic_used , null as random_distractor_allowable
from 
    org_node org
where 
    org.customer_id = cust_id);
    
    insert into org_node_test_catalog (TEST_CATALOG_ID, ORG_NODE_ID, CREATED_BY, CREATED_DATE_TIME, UPDATED_BY, UPDATED_DATE_TIME,
 CUSTOMER_ID, ACTIVATION_STATUS, ITEM_SET_ID, PRODUCT_ID, OVERRIDE_NO_RETAKE, OVERRIDE_FORM_ASSIGNMENT, OVERRIDE_LOGIN_START_DATE,
  LIC_PURCHASED, LIC_RESERVED, LIC_USED, RANDOM_DISTRACTOR_ALLOWABLE) (
select distinct 
   13317 as test_catalog_id, org.org_node_id, 1 as created_by,
    sysdate as created_date_time, null as updated_by, null as updated_date_time,
    org.customer_id as customer_id, 'AC' as activation_status, 29382 as item_set_id, '7002' as product_id, 
    'F' as override_no_retake, null as override_form_assignment, null as override_login_start_date,
    -1 as lic_purchased, -1 as lic_reserved, -1 as lic_used , null as random_distractor_allowable
from 
    org_node org
where 
    org.customer_id = cust_id);
    
    insert into org_node_test_catalog (TEST_CATALOG_ID, ORG_NODE_ID, CREATED_BY, CREATED_DATE_TIME, UPDATED_BY, UPDATED_DATE_TIME,
 CUSTOMER_ID, ACTIVATION_STATUS, ITEM_SET_ID, PRODUCT_ID, OVERRIDE_NO_RETAKE, OVERRIDE_FORM_ASSIGNMENT, OVERRIDE_LOGIN_START_DATE,
  LIC_PURCHASED, LIC_RESERVED, LIC_USED, RANDOM_DISTRACTOR_ALLOWABLE) (
select distinct 
   13298 as test_catalog_id, org.org_node_id, 1 as created_by,
    sysdate as created_date_time, null as updated_by, null as updated_date_time,
    org.customer_id as customer_id, 'AC' as activation_status, 29364 as item_set_id, '7002' as product_id, 
    'F' as override_no_retake, null as override_form_assignment, null as override_login_start_date,
    -1 as lic_purchased, -1 as lic_reserved, -1 as lic_used , null as random_distractor_allowable
from 
    org_node org
where 
    org.customer_id = cust_id);
    
    insert into org_node_test_catalog (TEST_CATALOG_ID, ORG_NODE_ID, CREATED_BY, CREATED_DATE_TIME, UPDATED_BY, UPDATED_DATE_TIME,
 CUSTOMER_ID, ACTIVATION_STATUS, ITEM_SET_ID, PRODUCT_ID, OVERRIDE_NO_RETAKE, OVERRIDE_FORM_ASSIGNMENT, OVERRIDE_LOGIN_START_DATE,
  LIC_PURCHASED, LIC_RESERVED, LIC_USED, RANDOM_DISTRACTOR_ALLOWABLE) (
select distinct 
   13336 as test_catalog_id, org.org_node_id, 1 as created_by,
    sysdate as created_date_time, null as updated_by, null as updated_date_time,
    org.customer_id as customer_id, 'AC' as activation_status, 29418 as item_set_id, '7002' as product_id, 
    'F' as override_no_retake, null as override_form_assignment, null as override_login_start_date,
    -1 as lic_purchased, -1 as lic_reserved, -1 as lic_used , null as random_distractor_allowable
from 
    org_node org
where 
    org.customer_id = cust_id);
    
    insert into org_node_test_catalog (TEST_CATALOG_ID, ORG_NODE_ID, CREATED_BY, CREATED_DATE_TIME, UPDATED_BY, UPDATED_DATE_TIME,
 CUSTOMER_ID, ACTIVATION_STATUS, ITEM_SET_ID, PRODUCT_ID, OVERRIDE_NO_RETAKE, OVERRIDE_FORM_ASSIGNMENT, OVERRIDE_LOGIN_START_DATE,
  LIC_PURCHASED, LIC_RESERVED, LIC_USED, RANDOM_DISTRACTOR_ALLOWABLE) (
select distinct 
   13316 as test_catalog_id, org.org_node_id, 1 as created_by,
    sysdate as created_date_time, null as updated_by, null as updated_date_time,
    org.customer_id as customer_id, 'AC' as activation_status, 29373 as item_set_id, '7002' as product_id, 
    'F' as override_no_retake, null as override_form_assignment, null as override_login_start_date,
    -1 as lic_purchased, -1 as lic_reserved, -1 as lic_used , null as random_distractor_allowable
from 
    org_node org
where 
    org.customer_id = cust_id);


-- program setup

delete from program where customer_id = cust_id and product_id = 7000;

Insert into PROGRAM
   (CUSTOMER_ID, PRODUCT_ID, PROGRAM_ID, PROGRAM_NAME, PROGRAM_START_DATE, PROGRAM_END_DATE, NORMS_GROUP, NORMS_sYEAR, ACTIVATION_STATUS, CREATED_DATE_TIME, UPDATED_DATE_TIME)
 Values
   (cust_id, 7000, cust_id, (select customer_name from customer where customer_id = cust_id) || ' Program', 
    sysdate, sysdate + 365, NULL, '2011', 'AC', 
    sysdate, sysdate); 
    

end; 
