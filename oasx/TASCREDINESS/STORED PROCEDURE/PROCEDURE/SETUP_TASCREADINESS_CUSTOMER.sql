CREATE OR REPLACE PROCEDURE "SETUP_TASCREADINESS_CUSTOMER" (cust_id integer) as
  cc_id integer;
begin

  -- customer_configuration
  delete from customer_configuration_value
   where customer_configuration_id in
         (select customer_configuration_id
            from customer_configuration
           where customer_id = cust_id);

  delete from customer_configuration where customer_id = cust_id;

  Insert into CUSTOMER_CONFIGURATION
    (CUSTOMER_CONFIGURATION_ID,
     CUSTOMER_CONFIGURATION_NAME,
     CUSTOMER_ID,
     EDITABLE,
     DEFAULT_VALUE,
     CREATED_BY,
     CREATED_DATE_TIME)
  Values
    (seq_customer_configuration_id.nextval,
     'Screen_Reader',
     cust_id,
     'T',
     'F',
     1,
     TO_DATE('09/27/2013 07:46:39', 'MM/DD/YYYY HH24:MI:SS'));

  Insert into CUSTOMER_CONFIGURATION
    (CUSTOMER_CONFIGURATION_ID,
     CUSTOMER_CONFIGURATION_NAME,
     CUSTOMER_ID,
     EDITABLE,
     DEFAULT_VALUE,
     CREATED_BY,
     CREATED_DATE_TIME)
  Values
    (seq_customer_configuration_id.nextval,
     'Program_Status',
     cust_id,
     'T',
     'T',
     1,
     TO_DATE('09/27/2013 07:46:39', 'MM/DD/YYYY HH24:MI:SS'));

  Insert into CUSTOMER_CONFIGURATION
    (CUSTOMER_CONFIGURATION_ID,
     CUSTOMER_CONFIGURATION_NAME,
     CUSTOMER_ID,
     EDITABLE,
     DEFAULT_VALUE,
     CREATED_BY,
     CREATED_DATE_TIME)
  Values
    (seq_customer_configuration_id.nextval,
     'Highlighter',
     cust_id,
     'T',
     'T',
     1,
     TO_DATE('09/27/2013 07:46:39', 'MM/DD/YYYY HH24:MI:SS'));

  Insert into CUSTOMER_CONFIGURATION
    (CUSTOMER_CONFIGURATION_ID,
     CUSTOMER_CONFIGURATION_NAME,
     CUSTOMER_ID,
     EDITABLE,
     DEFAULT_VALUE,
     CREATED_BY,
     CREATED_DATE_TIME)
  Values
    (seq_customer_configuration_id.nextval,
     'Calculator',
     cust_id,
     'F',
     'T',
     1,
     TO_DATE('09/27/2013 07:46:39', 'MM/DD/YYYY HH24:MI:SS'));

  Insert into CUSTOMER_CONFIGURATION
    (CUSTOMER_CONFIGURATION_ID,
     CUSTOMER_CONFIGURATION_NAME,
     CUSTOMER_ID,
     EDITABLE,
     DEFAULT_VALUE,
     CREATED_BY,
     CREATED_DATE_TIME)
  Values
    (seq_customer_configuration_id.nextval,
     'Test_Pause',
     cust_id,
     'T',
     'F',
     1,
     TO_DATE('09/27/2013 07:46:39', 'MM/DD/YYYY HH24:MI:SS'));

  Insert into CUSTOMER_CONFIGURATION
    (CUSTOMER_CONFIGURATION_ID,
     CUSTOMER_CONFIGURATION_NAME,
     CUSTOMER_ID,
     EDITABLE,
     DEFAULT_VALUE,
     CREATED_BY,
     CREATED_DATE_TIME)
  Values
    (seq_customer_configuration_id.nextval,
     'Untimed_Test',
     cust_id,
     'F',
     'F',
     1,
     TO_DATE('09/27/2013 07:46:39', 'MM/DD/YYYY HH24:MI:SS'));

  Insert into CUSTOMER_CONFIGURATION
    (CUSTOMER_CONFIGURATION_ID,
     CUSTOMER_CONFIGURATION_NAME,
     CUSTOMER_ID,
     EDITABLE,
     DEFAULT_VALUE,
     CREATED_BY,
     CREATED_DATE_TIME)
  Values
    (seq_customer_configuration_id.nextval,
     'Force_Test_Break',
     cust_id,
     'T',
     'T',
     1,
     TO_DATE('09/27/2013 07:46:39', 'MM/DD/YYYY HH24:MI:SS'));

  -- TASC has 3 levels (State, County, Testing Site) -> State, County and Testing Site will be locked  
  -- Default value is 3.
  Insert into CUSTOMER_CONFIGURATION
    (CUSTOMER_CONFIGURATION_ID,
     CUSTOMER_CONFIGURATION_NAME,
     CUSTOMER_ID,
     EDITABLE,
     DEFAULT_VALUE,
     CREATED_BY,
     CREATED_DATE_TIME)
  Values
    (seq_customer_configuration_id.nextval,
     'Lock_Hierarchy_Edit',
     cust_id,
     'T',
     '3',
     1,
     TO_DATE('09/27/2013 07:46:39', 'MM/DD/YYYY HH24:MI:SS'));

  Insert into CUSTOMER_CONFIGURATION
    (CUSTOMER_CONFIGURATION_ID,
     CUSTOMER_CONFIGURATION_NAME,
     CUSTOMER_ID,
     EDITABLE,
     DEFAULT_VALUE,
     CREATED_BY,
     CREATED_DATE_TIME)
  Values
    (seq_customer_configuration_id.nextval,
     'TASCReadiness_Customer',
     cust_id,
     NULL,
     NULL,
     1,
     TO_DATE('09/27/2013 07:46:39', 'MM/DD/YYYY HH24:MI:SS'));

  Insert into CUSTOMER_CONFIGURATION
    (CUSTOMER_CONFIGURATION_ID,
     CUSTOMER_CONFIGURATION_NAME,
     CUSTOMER_ID,
     EDITABLE,
     DEFAULT_VALUE,
     CREATED_BY,
     CREATED_DATE_TIME)
  Values
    (seq_customer_configuration_id.nextval,
     'Allow_Upload_Download',
     cust_id,
     'T',
     'T',
     1,
     TO_DATE('09/27/2013 07:46:39', 'MM/DD/YYYY HH24:MI:SS'));

/*--Tasc readiness Licence
Insert into CUSTOMER_CONFIGURATION
   (CUSTOMER_CONFIGURATION_ID, 
   CUSTOMER_CONFIGURATION_NAME, 
   CUSTOMER_ID, 
   EDITABLE, 
   DEFAULT_VALUE, 
   CREATED_BY, 
   CREATED_DATE_TIME)
 Values
   (seq_customer_configuration_id.nextval, 
   'Allow_Subscription', 
   cust_id, 
   'T',
    'T', 
    1, 
    TO_DATE('08/07/2007 17:24:57', 'MM/DD/YYYY HH24:MI:SS'));

Insert into CUSTOMER_CONFIGURATION
   (CUSTOMER_CONFIGURATION_ID, 
   CUSTOMER_CONFIGURATION_NAME, 
   CUSTOMER_ID, 
   EDITABLE, 
   DEFAULT_VALUE, 
   CREATED_BY, 
   CREATED_DATE_TIME)
 Values
   (seq_customer_configuration_id.nextval, 
   'License_Email_Notification', 
   cust_id, 
   'T',
   '20', 
   1, TO_DATE('08/07/2007 17:24:57', 'MM/DD/YYYY HH24:MI:SS'));
*/

  Insert into CUSTOMER_CONFIGURATION
    (CUSTOMER_CONFIGURATION_ID,
     CUSTOMER_CONFIGURATION_NAME,
     CUSTOMER_ID,
     EDITABLE,
     DEFAULT_VALUE,
     CREATED_BY,
     CREATED_DATE_TIME)
  Values
    (seq_customer_configuration_id.nextval,
     'TAS_Revised_UI',
     cust_id,
     'T',
     'T',
     1,
     TO_DATE('09/27/2013 07:46:39', 'MM/DD/YYYY HH24:MI:SS'));

  --for grouping content-specific accomodations    
  Insert into CUSTOMER_CONFIGURATION
    (CUSTOMER_CONFIGURATION_ID,
     CUSTOMER_CONFIGURATION_NAME,
     CUSTOMER_ID,
     EDITABLE,
     DEFAULT_VALUE,
     CREATED_BY,
     CREATED_DATE_TIME)
  Values
    (seq_customer_configuration_id.nextval,
     'Demographic_Grouping',
     cust_id,
     'T',
     'T',
     1,
     TO_DATE('09/27/2013 07:46:39', 'MM/DD/YYYY HH24:MI:SS'));

  Insert into CUSTOMER_CONFIGURATION
    (CUSTOMER_CONFIGURATION_ID,
     CUSTOMER_CONFIGURATION_NAME,
     CUSTOMER_ID,
     EDITABLE,
     DEFAULT_VALUE,
     CREATED_BY,
     CREATED_DATE_TIME)
  Values
    (seq_customer_configuration_id.nextval,
     'Bulk_Move_Students',
     cust_id,
     'T',
     'T',
     1,
     TO_DATE('09/27/2013 07:46:39', 'MM/DD/YYYY HH24:MI:SS'));

  Insert into CUSTOMER_CONFIGURATION
    (CUSTOMER_CONFIGURATION_ID,
     CUSTOMER_CONFIGURATION_NAME,
     CUSTOMER_ID,
     EDITABLE,
     DEFAULT_VALUE,
     CREATED_BY,
     CREATED_DATE_TIME)
  Values
    (seq_customer_configuration_id.nextval,
     'Allow_Reopen_Subtest',
     cust_id,
     'T',
     'T',
     1,
     TO_DATE('09/27/2013 07:46:39', 'MM/DD/YYYY HH24:MI:SS'));

  Insert into CUSTOMER_CONFIGURATION
    (CUSTOMER_CONFIGURATION_ID,
     CUSTOMER_CONFIGURATION_NAME,
     CUSTOMER_ID,
     EDITABLE,
     DEFAULT_VALUE,
     CREATED_BY,
     CREATED_DATE_TIME)
  Values
    (seq_customer_configuration_id.nextval,
     'Allow_Reopen_Subtest_For_Admin',
     cust_id,
     'T',
     'T',
     1,
     TO_DATE('09/27/2013 07:46:39', 'MM/DD/YYYY HH24:MI:SS'));

  Insert into CUSTOMER_CONFIGURATION
    (CUSTOMER_CONFIGURATION_ID,
     CUSTOMER_CONFIGURATION_NAME,
     CUSTOMER_ID,
     EDITABLE,
     DEFAULT_VALUE,
     CREATED_BY,
     CREATED_DATE_TIME)
  Values
    (seq_customer_configuration_id.nextval,
     'Disable_Delete_Student',
     cust_id,
     'T',
     'T',
     1,
     TO_DATE('09/27/2013 07:46:39', 'MM/DD/YYYY HH24:MI:SS'));

  /*    
  Insert into CUSTOMER_CONFIGURATION
     (CUSTOMER_CONFIGURATION_ID, CUSTOMER_CONFIGURATION_NAME, CUSTOMER_ID, EDITABLE, DEFAULT_VALUE, CREATED_BY, CREATED_DATE_TIME)
   Values
     (seq_customer_configuration_id.nextval, 'Match_Upload_Org_Ids', cust_id, 'T',
      'T', 1, TO_DATE('09/27/2013 07:46:39', 'MM/DD/YYYY HH24:MI:SS'));
      
  Insert into CUSTOMER_CONFIGURATION
     (CUSTOMER_CONFIGURATION_ID, CUSTOMER_CONFIGURATION_NAME, CUSTOMER_ID, EDITABLE, DEFAULT_VALUE, CREATED_BY, CREATED_DATE_TIME)
   Values
     (seq_customer_configuration_id.nextval, 'Allow_Speech_Controller', cust_id, 'T',
      'T', 1, TO_DATE('09/27/2013 07:46:39', 'MM/DD/YYYY HH24:MI:SS'));    
  */

  Insert into CUSTOMER_CONFIGURATION
    (CUSTOMER_CONFIGURATION_ID,
     CUSTOMER_CONFIGURATION_NAME,
     CUSTOMER_ID,
     EDITABLE,
     DEFAULT_VALUE,
     CREATED_BY,
     CREATED_DATE_TIME)
  Values
    (seq_customer_configuration_id.nextval,
     'Masking_Ruler',
     cust_id,
     'T',
     'T',
     1,
     TO_DATE('09/27/2013 07:46:39', 'MM/DD/YYYY HH24:MI:SS'));

  Insert into CUSTOMER_CONFIGURATION
    (CUSTOMER_CONFIGURATION_ID,
     CUSTOMER_CONFIGURATION_NAME,
     CUSTOMER_ID,
     EDITABLE,
     DEFAULT_VALUE,
     CREATED_BY,
     CREATED_DATE_TIME)
  Values
    (seq_customer_configuration_id.nextval,
     'Masking_Tool',
     cust_id,
     'T',
     'F',
     1,
     TO_DATE('09/27/2013 07:46:39', 'MM/DD/YYYY HH24:MI:SS'));

  Insert into CUSTOMER_CONFIGURATION
    (CUSTOMER_CONFIGURATION_ID,
     CUSTOMER_CONFIGURATION_NAME,
     CUSTOMER_ID,
     EDITABLE,
     DEFAULT_VALUE,
     CREATED_BY,
     CREATED_DATE_TIME)
  Values
    (seq_customer_configuration_id.nextval,
     'Magnifying_Glass',
     cust_id,
     'T',
     'F',
     1,
     TO_DATE('09/27/2013 07:46:39', 'MM/DD/YYYY HH24:MI:SS'));

  Insert into CUSTOMER_CONFIGURATION
    (CUSTOMER_CONFIGURATION_ID,
     CUSTOMER_CONFIGURATION_NAME,
     CUSTOMER_ID,
     EDITABLE,
     DEFAULT_VALUE,
     CREATED_BY,
     CREATED_DATE_TIME)
  Values
    (seq_customer_configuration_id.nextval,
     'Auditory_Calming',
     cust_id,
     'T',
     'F',
     1,
     TO_DATE('09/27/2013 07:46:39', 'MM/DD/YYYY HH24:MI:SS'));

  Insert into CUSTOMER_CONFIGURATION
    (CUSTOMER_CONFIGURATION_ID,
     CUSTOMER_CONFIGURATION_NAME,
     CUSTOMER_ID,
     EDITABLE,
     DEFAULT_VALUE,
     CREATED_BY,
     CREATED_DATE_TIME)
  Values
    (seq_customer_configuration_id.nextval,
     'Allow_Subtest_Invalidation',
     cust_id,
     'T',
     'T',
     1,
     TO_DATE('09/27/2013 07:46:39', 'MM/DD/YYYY HH24:MI:SS'));

  /*    
  Insert into CUSTOMER_CONFIGURATION
     (CUSTOMER_CONFIGURATION_ID, CUSTOMER_CONFIGURATION_NAME, CUSTOMER_ID, EDITABLE, DEFAULT_VALUE, CREATED_BY, CREATED_DATE_TIME)
   Values
     (seq_customer_configuration_id.nextval, 'Allow_Print_SubtestName_Form', cust_id, 'T',
      'T', 1, TO_DATE('09/27/2013 07:46:39', 'MM/DD/YYYY HH24:MI:SS'));  
  */

  select seq_customer_configuration_id.nextval into cc_id from dual;

  Insert into CUSTOMER_CONFIGURATION
    (CUSTOMER_CONFIGURATION_ID,
     CUSTOMER_CONFIGURATION_NAME,
     CUSTOMER_ID,
     EDITABLE,
     DEFAULT_VALUE,
     CREATED_BY,
     CREATED_DATE_TIME)
  Values
    (cc_id,
     'Grade',
     cust_id,
     'T',
     'T',
     1,
     TO_DATE('09/27/2013 07:46:39', 'MM/DD/YYYY HH24:MI:SS'));

  Insert into CUSTOMER_CONFIGURATION_VALUE
    (CUSTOMER_CONFIGURATION_VALUE, CUSTOMER_CONFIGURATION_ID, SORT_ORDER)
  Values
    ('AD', cc_id, 1);

  --Extended Time configuration
  select seq_customer_configuration_id.nextval into cc_id from dual;

  Insert into CUSTOMER_CONFIGURATION
    (CUSTOMER_CONFIGURATION_ID,
     CUSTOMER_CONFIGURATION_NAME,
     CUSTOMER_ID,
     EDITABLE,
     DEFAULT_VALUE,
     CREATED_BY,
     CREATED_DATE_TIME)
  Values
    (cc_id,
     'Extended_Time',
     cust_id,
     'T',
     'F',
     1,
     TO_DATE('09/27/2013 07:46:39', 'MM/DD/YYYY HH24:MI:SS'));

  Insert into CUSTOMER_CONFIGURATION_VALUE
    (CUSTOMER_CONFIGURATION_VALUE, CUSTOMER_CONFIGURATION_ID, SORT_ORDER)
  Values
    ('1.5', cc_id, NULL);

  --Configurable_Student_ID configuration
  select seq_customer_configuration_id.nextval into cc_id from dual;

  Insert into CUSTOMER_CONFIGURATION
    (CUSTOMER_CONFIGURATION_ID,
     CUSTOMER_CONFIGURATION_NAME,
     CUSTOMER_ID,
     EDITABLE,
     DEFAULT_VALUE,
     CREATED_BY,
     CREATED_DATE_TIME)
  Values
    (cc_id,
     'Configurable_Student_ID',
     cust_id,
     'T',
     'T',
     1,
     TO_DATE('09/27/2013 07:46:39', 'MM/DD/YYYY HH24:MI:SS'));

  Insert into CUSTOMER_CONFIGURATION_VALUE
    (CUSTOMER_CONFIGURATION_VALUE, CUSTOMER_CONFIGURATION_ID, SORT_ORDER)
  Values
    ('TASC ID', cc_id, 1);
  Insert into CUSTOMER_CONFIGURATION_VALUE
    (CUSTOMER_CONFIGURATION_VALUE, CUSTOMER_CONFIGURATION_ID, SORT_ORDER)
  Values
    ('9', cc_id, 2);
  Insert into CUSTOMER_CONFIGURATION_VALUE
    (CUSTOMER_CONFIGURATION_VALUE, CUSTOMER_CONFIGURATION_ID, SORT_ORDER)
  Values
    ('T', cc_id, 3);
  Insert into CUSTOMER_CONFIGURATION_VALUE
    (CUSTOMER_CONFIGURATION_VALUE, CUSTOMER_CONFIGURATION_ID, SORT_ORDER)
  Values
    ('9', cc_id, 4);
  Insert into CUSTOMER_CONFIGURATION_VALUE
    (CUSTOMER_CONFIGURATION_VALUE, CUSTOMER_CONFIGURATION_ID, SORT_ORDER)
  Values
    ('NU', cc_id, 5);

  select seq_customer_configuration_id.nextval into cc_id from dual;

  Insert into CUSTOMER_CONFIGURATION
    (CUSTOMER_CONFIGURATION_ID,
     CUSTOMER_CONFIGURATION_NAME,
     CUSTOMER_ID,
     EDITABLE,
     DEFAULT_VALUE,
     CREATED_BY,
     CREATED_DATE_TIME)
  Values
    (cc_id,
     'Configurable_Student_ID_2',
     cust_id,
     'T',
     'T',
     1,
     TO_DATE('09/27/2013 07:46:39', 'MM/DD/YYYY HH24:MI:SS'));

  Insert into CUSTOMER_CONFIGURATION_VALUE
    (CUSTOMER_CONFIGURATION_VALUE, CUSTOMER_CONFIGURATION_ID, SORT_ORDER)
  Values
    ('TASC ID2', cc_id, 1);
  Insert into CUSTOMER_CONFIGURATION_VALUE
    (CUSTOMER_CONFIGURATION_VALUE, CUSTOMER_CONFIGURATION_ID, SORT_ORDER)
  Values
    ('15', cc_id, 2);
    
    --View_Response_Result configuration
  select seq_customer_configuration_id.nextval into cc_id from dual;

  Insert into CUSTOMER_CONFIGURATION
    (CUSTOMER_CONFIGURATION_ID,
     CUSTOMER_CONFIGURATION_NAME,
     CUSTOMER_ID,
     EDITABLE,
     DEFAULT_VALUE,
     CREATED_BY,
     CREATED_DATE_TIME)
  Values
    (cc_id,
     'View_Response_Result',
     cust_id,
     'T',
     'T',
     1,
     TO_DATE('01/12/2015 07:46:39', 'MM/DD/YYYY HH24:MI:SS'));

  -- customer_demographics
  delete from customer_demographic_value
   where customer_demographic_id in
         (select customer_demographic_id
            from customer_demographic
           where customer_id = cust_id);

  delete from customer_demographic where customer_id = cust_id;

  assign_tascreadiness_demog(cust_id);

  delete from customer_email_config where customer_id = cust_id;

  assign_tascreadiness_email_cnf(cust_id);

/* TASC Readiness customer does not need reports.
  -- customer_report_bridge
  delete from customer_report_bridge where customer_id = cust_id;

  Insert into CUSTOMER_REPORT_BRIDGE
    (CUSTOMER_ID,
     REPORT_NAME,
     DISPLAY_NAME,
     DESCRIPTION,
     SYSTEM_KEY,
     CUSTOMER_KEY,
     REPORT_URL,
     PRODUCT_ID)
  Values
    (cust_id,
     'Prism',
     'TASC Online Reports',
     'Access test results in TASC Online Reporting.',
     --'WPZguVF49hXaRuZfe9L29ItsC2I', 'WPZguVF49hXaRuZfe9L29ItsC2I', 'https://tascreports.ctb.com/tasc/reports.do', 4500);--PROD IP
     'WPZguVF49hXaRuZfe9L29ItsC2I',
     'WPZguVF49hXaRuZfe9L29ItsC2I',
     'http://10.160.23.51:8080/tasc/reports.do',
     4500); --QA IP
  --'WPZguVF49hXaRuZfe9L29ItsC2I', 'WPZguVF49hXaRuZfe9L29ItsC2I', 'http://10.160.23.12:8080/tasc/reports.do', 4500);--QA IP
  --'WPZguVF49hXaRuZfe9L29ItsC2I', 'WPZguVF49hXaRuZfe9L29ItsC2I', 'http://10.186.17.58:8080/tasc/reports.do', 4500);--DEV IP

  --Add Web Service URL
  declare
    cnt number;
  begin
    select count(*)
      into cnt
      from resource_type
     where resource_type_code = 'PRISMWSURL';
    if (cnt = 0) then
      insert into resource_type
        (resource_type_code, resource_type_desc)
      values
        ('PRISMWSURL', 'PRISM Web Service URL');
    end if;
  end;
  --insert into customer_resource (customer_id, resource_type_code, resource_uri) values (cust_id,'PRISMWSURL','https://tascreports.ctb.com/tasc/StudentDataloadService?wsdl');
  insert into customer_resource
    (customer_id, resource_type_code, resource_uri)
  values
    (cust_id,
     'PRISMWSURL',
     'http://10.160.23.50:8080/tasc/StudentDataloadService?wsdl');
*/

  -- org_node_test_catalog
  -- PRODUCT_ID 4512 , 4514 ADDED IN THE BELOW DELETE STATEMENT FOR OAS STORY# 1336 Form 3 English: Modify TASC Rdns Customer Set-Up
  delete from org_node_test_catalog
   where customer_id = cust_id
     and product_id in (4503, 4504, 4510, 4511,4513,4515,4512,4514);

  --TASC Readiness 2015 - Form 2 English
  insert into org_node_test_catalog
    (TEST_CATALOG_ID,
     ORG_NODE_ID,
     CUSTOMER_ID,
     PRODUCT_ID,
     CREATED_BY,
     CREATED_DATE_TIME,
     UPDATED_BY,
     UPDATED_DATE_TIME,
     ACTIVATION_STATUS,
     ITEM_SET_ID,
     OVERRIDE_NO_RETAKE,
     OVERRIDE_FORM_ASSIGNMENT,
     OVERRIDE_LOGIN_START_DATE,
     LIC_PURCHASED,
     LIC_RESERVED,
     LIC_USED,
     RANDOM_DISTRACTOR_ALLOWABLE)
    (select distinct (select test_catalog_id
                        from test_catalog
                       where product_id = 4513) as test_catalog_id,
                     org.org_node_id,
                     org.customer_id as customer_id,
                     4513 as product_id,
                     1 as created_by,
                     sysdate as created_date_time,
                     null as updated_by,
                     null as updated_date_time,
                     'AC' as activation_status,
                     (select item_set_id
                        from test_catalog
                       where product_id = 4513) as item_set_id,
                     'F' as override_no_retake,
                     null as override_form_assignment,
                     null as override_login_start_date,
                     -1 as lic_purchased,
                     -1 as lic_reserved,
                     -1 as lic_used,
                     null as random_distractor_allowable
       from org_node org
      where org.customer_id = cust_id);

  --TASC Readiness 2015 - Form 2 Spanish
  insert into org_node_test_catalog
    (TEST_CATALOG_ID,
     ORG_NODE_ID,
     CUSTOMER_ID,
     PRODUCT_ID,
     CREATED_BY,
     CREATED_DATE_TIME,
     UPDATED_BY,
     UPDATED_DATE_TIME,
     ACTIVATION_STATUS,
     ITEM_SET_ID,
     OVERRIDE_NO_RETAKE,
     OVERRIDE_FORM_ASSIGNMENT,
     OVERRIDE_LOGIN_START_DATE,
     LIC_PURCHASED,
     LIC_RESERVED,
     LIC_USED,
     RANDOM_DISTRACTOR_ALLOWABLE)
    (select distinct (select test_catalog_id
                        from test_catalog
                       where product_id = 4515) as test_catalog_id,
                     org.org_node_id,
                     org.customer_id as customer_id,
                     4515 as product_id,
                     1 as created_by,
                     sysdate as created_date_time,
                     null as updated_by,
                     null as updated_date_time,
                     'AC' as activation_status,
                     (select item_set_id
                        from test_catalog
                       where product_id = 4515) as item_set_id,
                     'F' as override_no_retake,
                     null as override_form_assignment,
                     null as override_login_start_date,
                     -1 as lic_purchased,
                     -1 as lic_reserved,
                     -1 as lic_used,
                     null as random_distractor_allowable
       from org_node org
      where org.customer_id = cust_id);
    
    /* CODE CHANGE FOR OAS STORY# 1336 - Form 3 English: Modify TASC Rdns Customer Set-Up */
    
  --TASC Readiness 2015 - Form 3 English
  insert into org_node_test_catalog
    (TEST_CATALOG_ID,
     ORG_NODE_ID,
     CUSTOMER_ID,
     PRODUCT_ID,
     CREATED_BY,
     CREATED_DATE_TIME,
     UPDATED_BY,
     UPDATED_DATE_TIME,
     ACTIVATION_STATUS,
     ITEM_SET_ID,
     OVERRIDE_NO_RETAKE,
     OVERRIDE_FORM_ASSIGNMENT,
     OVERRIDE_LOGIN_START_DATE,
     LIC_PURCHASED,
     LIC_RESERVED,
     LIC_USED,
     RANDOM_DISTRACTOR_ALLOWABLE)
    (select distinct (select test_catalog_id
                        from test_catalog
                       where product_id = 4512) as test_catalog_id,
                     org.org_node_id,
                     org.customer_id as customer_id,
                     4512 as product_id,
                     1 as created_by,
                     sysdate as created_date_time,
                     null as updated_by,
                     null as updated_date_time,
                     'AC' as activation_status,
                     (select item_set_id
                        from test_catalog
                       where product_id = 4512) as item_set_id,
                     'F' as override_no_retake,
                     null as override_form_assignment,
                     null as override_login_start_date,
                     -1 as lic_purchased,
                     -1 as lic_reserved,
                     -1 as lic_used,
                     null as random_distractor_allowable
       from org_node org
      where org.customer_id = cust_id);
    
    /*CODE CHANGE FOR OAS STORY# 1336 - Form 3 English: Modify TASC Rdns Customer Set-Up */
    
  --TASC Readiness 2015 - Form 3 Spanish
  insert into org_node_test_catalog
    (TEST_CATALOG_ID,
     ORG_NODE_ID,
     CUSTOMER_ID,
     PRODUCT_ID,
     CREATED_BY,
     CREATED_DATE_TIME,
     UPDATED_BY,
     UPDATED_DATE_TIME,
     ACTIVATION_STATUS,
     ITEM_SET_ID,
     OVERRIDE_NO_RETAKE,
     OVERRIDE_FORM_ASSIGNMENT,
     OVERRIDE_LOGIN_START_DATE,
     LIC_PURCHASED,
     LIC_RESERVED,
     LIC_USED,
     RANDOM_DISTRACTOR_ALLOWABLE)
    (select distinct (select test_catalog_id
                        from test_catalog
                       where product_id = 4514) as test_catalog_id,
                     org.org_node_id,
                     org.customer_id as customer_id,
                     4514 as product_id,
                     1 as created_by,
                     sysdate as created_date_time,
                     null as updated_by,
                     null as updated_date_time,
                     'AC' as activation_status,
                     (select item_set_id
                        from test_catalog
                       where product_id = 4514) as item_set_id,
                     'F' as override_no_retake,
                     null as override_form_assignment,
                     null as override_login_start_date,
                     -1 as lic_purchased,
                     -1 as lic_reserved,
                     -1 as lic_used,
                     null as random_distractor_allowable
       from org_node org
      where org.customer_id = cust_id);

  --Readiness Assessment - English
  /*insert into org_node_test_catalog
    (TEST_CATALOG_ID,
     ORG_NODE_ID,
     CUSTOMER_ID,
     PRODUCT_ID,
     CREATED_BY,
     CREATED_DATE_TIME,
     UPDATED_BY,
     UPDATED_DATE_TIME,
     ACTIVATION_STATUS,
     ITEM_SET_ID,
     OVERRIDE_NO_RETAKE,
     OVERRIDE_FORM_ASSIGNMENT,
     OVERRIDE_LOGIN_START_DATE,
     LIC_PURCHASED,
     LIC_RESERVED,
     LIC_USED,
     RANDOM_DISTRACTOR_ALLOWABLE)
    (select distinct (select test_catalog_id
                        from test_catalog
                       where product_id = 4510) as test_catalog_id,
                     org.org_node_id,
                     org.customer_id as customer_id,
                     4510 as product_id,
                     1 as created_by,
                     sysdate as created_date_time,
                     null as updated_by,
                     null as updated_date_time,
                     'AC' as activation_status,
                     (select item_set_id
                        from test_catalog
                       where product_id = 4510) as item_set_id,
                     'F' as override_no_retake,
                     null as override_form_assignment,
                     null as override_login_start_date,
                     -1 as lic_purchased,
                     -1 as lic_reserved,
                     -1 as lic_used,
                     null as random_distractor_allowable
       from org_node org
      where org.customer_id = cust_id);

  --Readiness Assessment - Spanish
  insert into org_node_test_catalog
    (TEST_CATALOG_ID,
     ORG_NODE_ID,
     CUSTOMER_ID,
     PRODUCT_ID,
     CREATED_BY,
     CREATED_DATE_TIME,
     UPDATED_BY,
     UPDATED_DATE_TIME,
     ACTIVATION_STATUS,
     ITEM_SET_ID,
     OVERRIDE_NO_RETAKE,
     OVERRIDE_FORM_ASSIGNMENT,
     OVERRIDE_LOGIN_START_DATE,
     LIC_PURCHASED,
     LIC_RESERVED,
     LIC_USED,
     RANDOM_DISTRACTOR_ALLOWABLE)
    (select distinct (select test_catalog_id
                        from test_catalog
                       where product_id = 4511) as test_catalog_id,
                     org.org_node_id,
                     org.customer_id as customer_id,
                     4511 as product_id,
                     1 as created_by,
                     sysdate as created_date_time,
                     null as updated_by,
                     null as updated_date_time,
                     'AC' as activation_status,
                     (select item_set_id
                        from test_catalog
                       where product_id = 4511) as item_set_id,
                     'F' as override_no_retake,
                     null as override_form_assignment,
                     null as override_login_start_date,
                     -1 as lic_purchased,
                     -1 as lic_reserved,
                     -1 as lic_used,
                     null as random_distractor_allowable
       from org_node org
      where org.customer_id = cust_id);
*/
  --ADS config
  /*Insert into TCM_CUSTOMER@ADS.CTB
     (CUSTOMER_ACCOUNT_ID, DATE_ACTIVATE, DATE_TERMINATE, CUSTOMER_NAME)
   Values
     (cust_id, NULL, NULL, (select substr(customer_name, 30) from customer where customer_id = cust_id));
  
  Insert into TCM_CUSTOMER_SDS@ADS.CTB
     (CUSTOMER_ACCOUNT_ID, SITE_SERVER_ID)
   Values
     (cust_id, 2);
  */
  -- program setup

  delete from program
   where customer_id = cust_id
     and product_id = 4500;

  Insert into PROGRAM
    (CUSTOMER_ID,
     PRODUCT_ID,
     PROGRAM_ID,
     PROGRAM_NAME,
     PROGRAM_START_DATE,
     PROGRAM_END_DATE,
     NORMS_GROUP,
     NORMS_YEAR,
     ACTIVATION_STATUS,
     CREATED_DATE_TIME,
     UPDATED_DATE_TIME)
  Values
    (cust_id,
     4500,
     cust_id,
     (select customer_name from customer where customer_id = cust_id) ||
     ' Program',
     sysdate,
     add_months(sysdate, 120),
     '19',
     '2000',
     'AC', --[IAA] 1/21/2014: Make programs expire after 10 years instead of 365 days. Story: TABE - 2014 Op - Program Validity Extension
     sysdate,
     sysdate);

  --[IAA] 1/21/2014: make active_program_end_date 100 years after start_date
  --update customer set active_program_end_date=add_months(active_program_start_date, 1200) where customer_id= cust_id;

  delete from customer_product_test_resource where customer_id = cust_id;

  insert into customer_product_test_resource
  values
    (cust_id,
     4500,
     'CF',
     'TASC Test Content',
     '/downloadfiles/CTB-McGraw-Hill_Test_Assessing_Secondary_Completion_and_Readiness_Assessment.zip',
     '36.5 MB');

  /*    
  Insert into customer_product_license
     (customer_id, product_id, available, reserved, consumed, email_notify_flag, license_after_last_purchase)
     values (cust_id, 4500, 0, 0, 0, 'T', 0);
  */

end;
/
