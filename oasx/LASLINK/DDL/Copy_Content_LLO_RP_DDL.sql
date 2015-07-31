alter table PRODUCT modify PRODUCT_TYPE VARCHAR2(6);
/
-- Add/modify columns 
alter table PRODUCT_TYPE_CODE modify PRODUCT_TYPE VARCHAR2(6);
/
CREATE TABLE LLO_RP_LOOKUP_item
(
OLD_ID VARCHAR2(60),
NEW_ID VARCHAR2(60)
)
/
CREATE TABLE LLO_RP_LOOKUP_ITEM_SET
(
OLD_ID VARCHAR2(60),
NEW_ID VARCHAR2(60)
)
/
CREATE TABLE LLO_RP_LOOKUP_ITEM_SET_RE
(
OLD_ID VARCHAR2(60),
NEW_ID VARCHAR2(60)
)
/
-- Create table
create table LLO_RP_LOOKUP_OAS_BMT_ITEM_MAP
(
  OLD_ID VARCHAR2(32),
  NEW_ID VARCHAR2(32)
)/
create or replace type c_td_id_list is varray(500) of number(15)
/
create or replace type c_ts_id_list is varray(500) of number(15)
/
create or replace type c_tc_id_list is varray(100) of number(15)
/
create or replace type c_re_id_list is varray(10000) of number(15)
/
create or replace type c_item_id_list is varray(10000) of varchar(64)
/


-- Create table
create table OAS_BMT_ITEM_MAP
(
  OAS_ITEM_ID       VARCHAR2(100),
  BMT_ITEM_ID       VARCHAR2(100),
  ACTIVATION_STATUS VARCHAR2(10),
  UPDATED_DATE      DATE default SYSDATE
)
/