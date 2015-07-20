create table MSS_WS_AUDIT_LOG
(
  INVOKE_ID             NUMBER primary key,
  ROSTER_ID             NUMBER not null,
  STUDENT_ID            NUMBER not null,
  SESSION_ID            NUMBER not null,
  WS_INVOKE_TIMESTAMP   DATE default SYSDATE,
  WS_RESPONSE_TIMESTAMP DATE,
  RETRY_PROCESS         VARCHAR2(10),
  RETRY_ERROR_LOG_ID    NUMBER,
  OAS_ITEM_ID           VARCHAR2(32),
  DAS_ITEM_ID           VARCHAR2(32),
  WS_REQUEST            CLOB,
  WS_RESPONSE           CLOB
);

create index MSS_WS_AUDIT_LOG_ID on MSS_WS_AUDIT_LOG (ROSTER_ID);

create sequence SEQ_MSS_WS_AUDIT_LOG_ID
minvalue 1
maxvalue 999999999999999999999999999
start with 1000
increment by 1
cache 20;