create or replace type NUM_ARRAY as table of number;

CREATE OR REPLACE TYPE ROSTER AS OBJECT (roster_id NUMBER,  subtest_ids num_array);

CREATE OR REPLACE TYPE ROSTERARRAY AS VARRAY(1000) OF ROSTER;

DROP TABLE item_response_audit;
create table item_response_audit as (select * from item_response where 1=2);
alter table item_response_audit add audit_id number not null;

DROP TABLE item_response_cr_audit;
create table item_response_cr_audit as (select * from item_response_cr where 1=2);
alter table item_response_cr_audit add audit_id number not null;

DROP TABLE item_response_points_audit;
create table item_response_points_audit as (select * from item_response_points where 1=2);
alter table item_response_points_audit add audit_id number not null;

create table AUDIT_FILE_WIPEOUT_SUBTEST
(
  AUDIT_ID              NUMBER not null,
  TEST_ROSTER_ID                NUMBER not null,
  ITEM_SET_TD_ID                NUMBER not null,
  CUSTOMER_ID                   NUMBER not null,
  ORG_NODE_ID                   NUMBER not null,
  TEST_ADMIN_ID                 NUMBER not null,
  STUDENT_ID                    NUMBER not null,
  TICKET_ID                     VARCHAR2(32),
  REQUESTOR_NAME                VARCHAR2(32),
  REASON_FOR_REQUEST            VARCHAR2(255),
  CREATED_BY                    NUMBER not null,
  CREATED_DATE_TIME             DATE not null,
  ITEM_SET_TS_ID                NUMBER not null,
  OLD_SUBTEST_COMPLETION_STATUS VARCHAR2(2) not null,
  NEW_SUBTEST_COMPLETION_STATUS VARCHAR2(2) not null,
  OLD_ROSTER_COMPLETION_STATUS  VARCHAR2(2) not null,
  NEW_ROSTER_COMPLETION_STATUS  VARCHAR2(2) not null,
  COMPLETION_DATE_TIME          DATE,
  START_DATE_TIME               DATE,
  ITEM_ANSWERED                 VARCHAR2(32),
  TIME_SPENT                    VARCHAR2(32),
  CONSTRAINT PK_WIPEOUT_ID PRIMARY KEY (AUDIT_ID,TEST_ROSTER_ID,ITEM_SET_TD_ID)
);