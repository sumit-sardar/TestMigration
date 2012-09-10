-- For user story TN on Acuity - OAS - 036 – Retry Logic
create table SCHEDULED_SCORABLE_ROSTER
(
  TEST_ROSTER_ID    INTEGER not null,
  CREATED_DATE_TIME date default sysdate not null,
  UPDATED_DATE_TIME date default sysdate not null,
  RETRY_COUNT       INTEGER default 0 not null,
  VALIDATION_STATUS VARCHAR2(2) DEFAULT 'VA',
  STATE             VARCHAR2(100) default 'FAILED' not NULL,
  CONSTRAINT SCHEDULED_SCORABLE_ROSTER_PK PRIMARY KEY (TEST_ROSTER_ID)
)
/
