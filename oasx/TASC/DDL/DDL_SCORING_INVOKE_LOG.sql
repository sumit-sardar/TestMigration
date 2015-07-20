CREATE TABLE SCORING_INVOKE_LOG
(
  SCORING_INVOKE_LOG_KEY            NUMBER PRIMARY KEY,
  STUDENT_ID                        INTEGER not null,
  ROSTER_ID                         INTEGER not null,
  SESSION_ID                        INTEGER not null,
  STATUS                            VARCHAR2(32) not null,
  INVOKE_COUNT                      INTEGER NOT NULL,
  SCORER_TYPE                       VARCHAR2(32) not null,
  ADDITIONAL_INFO                   VARCHAR2(250),
  MESSAGE                           VARCHAR2(4000),
  CREATED_DATE                      DATE default (sysdate),
  UPDATED_DATE                      DATE default (sysdate)
);

create sequence SEQ_SCORING_INVOKE_LOG_KEY
minvalue 1
maxvalue 999999999999999999999999999
start with 1000
increment by 1
cache 20;