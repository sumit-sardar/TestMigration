
  CREATE TABLE "OAS"."STUDENT_SURVEY_QUESTION_OPTION" 
   (	"QUESTION_ID" VARCHAR2(32 BYTE) NOT NULL ENABLE, 
	"QUESTION_ORDER" NUMBER NOT NULL ENABLE, 
	"QUESTION_OPTION_OAS" VARCHAR2(20 BYTE) NOT NULL ENABLE, 
	"QUESTION_OPTION_PRISM" VARCHAR2(20 BYTE), 
	 CONSTRAINT "STUDENT_SURVEY_QUESTION_O_PK" PRIMARY KEY ("QUESTION_ID", "QUESTION_ORDER", "QUESTION_OPTION_OAS")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DATA1"  ENABLE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DATA1" ;
