create table OAS_DAS_ITEM_MAPPING
(
  MAPPING_ID         NUMBER primary key,
  OAS_ITEM_ID        VARCHAR2(32) not null,
  DAS_ITEM_ID        VARCHAR2(32) not null,
  PARENT_DAS_ITEM_ID VARCHAR2(32),
  ITEM_ORDER         INTEGER,
  MAX_SCORE          INTEGER,
  SCORE_TYPE         VARCHAR2(32),
  ACTIVATION_STATUS  VARCHAR2(2) not null,
  CREATED_DATE_TIME  DATE default SYSDATE,
  UPDATED_DATE_TIME  DATE default SYSDATE
);

create index OAS_DAS_ITEM_MAPPING_OASID on OAS_DAS_ITEM_MAPPING(OAS_ITEM_ID);

create sequence SEQ_OAS_DAS_ITEM_MAPPING_ID
minvalue 1
maxvalue 999999999999999999999999999
start with 1000
increment by 1
cache 20;
