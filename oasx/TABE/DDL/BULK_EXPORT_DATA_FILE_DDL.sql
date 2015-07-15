create table BULK_EXPORT_DATA_FILE
(
  export_request_id NUMBER not null,
  user_id           NUMBER not null,
  customer_id       NUMBER not null,
  export_date       DATE default sysdate not null,
  file_name         VARCHAR2(300),
  status            VARCHAR2(50),
  file_content      BLOB,
  message           VARCHAR2(500)
)
/
create index BULK_EXPORT_USER_ID on BULK_EXPORT_DATA_FILE (USER_ID)
/
alter table BULK_EXPORT_DATA_FILE
  add primary key (EXPORT_REQUEST_ID)
/
alter table BULK_EXPORT_DATA_FILE
  add foreign key (USER_ID)
  references USERS (USER_ID)
/
alter table BULK_EXPORT_DATA_FILE
  add foreign key (CUSTOMER_ID)
  references CUSTOMER (CUSTOMER_ID)
/
create sequence SEQ_EXPORT_REQUEST_ID
minvalue 1000
maxvalue 99999999999999999999999
start with 1000
increment by 1
cache 20
/