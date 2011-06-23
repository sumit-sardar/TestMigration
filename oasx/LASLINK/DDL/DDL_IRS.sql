-- Create sequence SEQ_LL_COMPOSITE_FACT_ID
create sequence SEQ_LL_COMPOSITE_FACT_ID
minvalue 0
maxvalue 999999999999999999999999999
start with 20000
increment by 1
nocache;



-- Create sequence SEQ_LL_CONTENT_AREA_FACT_ID
create sequence SEQ_LL_CONTENT_AREA_FACT_ID
minvalue 0
maxvalue 999999999999999999999999999
start with 20000
increment by 1
nocache
/

-- Create sequence SEQ_LL_ITEM_FACT_ID
create sequence SEQ_LL_ITEM_FACT_ID
minvalue 0
maxvalue 999999999999999999999999999
start with 20000
increment by 1
nocache
/



-- Create sequence SEQ_LL_PRIM_OBJ_FACT_ID
create sequence SEQ_LL_PRIM_OBJ_FACT_ID
minvalue 0
maxvalue 9999999999999999999999999
start with 20000
increment by 1
nocache
/



-- Create sequence SEQ_LL_SEC_OBJ_FACT_ID
create sequence SEQ_LL_SEC_OBJ_FACT_ID
minvalue 0
maxvalue 999999999999999999999999999
start with 20000
increment by 1
nocache
/


--CREATE TRIGGER TRIG_LL_SEQ_COMPOSITE_FACT
CREATE OR REPLACE TRIGGER TRIG_LL_SEQ_COMPOSITE_FACT BEFORE INSERT ON LASLINK_COMPOSITE_FACT
FOR EACH ROW
BEGIN
  SELECT SEQ_LL_COMPOSITE_FACT_ID.NEXTVAL INTO :NEW.FACTID FROM dual;
END
/



--CREATE TRIGGER TRIG_LL_SEQ_CONTENT_AREA_FACT
CREATE OR REPLACE TRIGGER TRIG_LL_SEQ_CONTENT_AREA_FACT BEFORE INSERT ON LASLINK_CONTENT_AREA_FACT
FOR EACH ROW
BEGIN
  SELECT SEQ_LL_CONTENT_AREA_FACT_ID.NEXTVAL INTO :NEW.FACTID FROM dual;
END
/



--CREATE TRIGGER TRIG_LL_SEQ_ITEM_FACT
CREATE OR REPLACE TRIGGER TRIG_LL_SEQ_ITEM_FACT BEFORE INSERT ON LASLINK_ITEM_FACT
FOR EACH ROW
BEGIN
  SELECT SEQ_LL_ITEM_FACT_ID.NEXTVAL INTO :NEW.FACTID FROM dual;
END
/



--CREATE TRIGGER TRIG_LL_SEQ_PRIM_OBJ_FACT
CREATE OR REPLACE TRIGGER TRIG_LL_SEQ_PRIM_OBJ_FACT BEFORE INSERT ON LASLINK_PRIM_OBJ_FACT
FOR EACH ROW
BEGIN
  SELECT SEQ_LL_PRIM_OBJ_FACT_ID.NEXTVAL INTO :NEW.FACTID FROM dual;
END
/



--CREATE TRIGGER TRIG_LL_SEQ_SEC_OBJ_FACT
CREATE OR REPLACE TRIGGER TRIG_LL_SEQ_SEC_OBJ_FACT BEFORE INSERT ON LASLINK_SEC_OBJ_FACT
FOR EACH ROW
BEGIN
  SELECT SEQ_LL_SEC_OBJ_FACT_ID.NEXTVAL INTO :NEW.FACTID FROM dual;
END
/


--create table LASLINK_COMPOSITE_FACT

create table LASLINK_COMPOSITE_FACT
(
  FACTID                    NUMBER not null,
  COMPOSITEID               NUMBER not null,
  STUDENTID                 NUMBER not null,
  SESSIONID                 NUMBER not null,
  ATTR1ID                   NUMBER,
  ATTR2ID                   NUMBER,
  ATTR3ID                   NUMBER,
  ATTR4ID                   NUMBER,
  ATTR5ID                   NUMBER,
  ATTR6ID                   NUMBER,
  ATTR7ID                   NUMBER,
  ATTR8ID                   NUMBER,
  ATTR9ID                   NUMBER,
  ATTR10ID                  NUMBER,
  ATTR11ID                  NUMBER,
  ATTR12ID                  NUMBER,
  ATTR13ID                  NUMBER,
  ATTR14ID                  NUMBER,
  ATTR15ID                  NUMBER,
  ATTR16ID                  NUMBER,
  GRADEID                   NUMBER,
  LEVELID                   NUMBER not null,
  ORG_NODEID                NUMBER not null,
  ASSESSMENTID              NUMBER not null,
  PROGRAMID                 NUMBER,
  CURRENT_RESULTID          NUMBER not null,
  SCALE_SCORE               NUMBER,
  PROFICENCY_LEVEL          NUMBER,
  NORMAL_CURVE_EQUIVALENT   NUMBER,
  NATIONAL_PERCENTILE       NUMBER,
  POINTS_ATTEMPTED          NUMBER,
  POINTS_OBTAINED           NUMBER,
  POINTS_POSSIBLE           NUMBER,
  TEST_COMPLETION_TIMESTAMP DATE,
  FORMID                    NUMBER not null
)
/

-- Create/Recreate primary, unique and foreign key constraints 
alter table LASLINK_COMPOSITE_FACT
  add constraint XPKLLCOMPOSITE_FACT primary key (FACTID)
/
alter table LASLINK_COMPOSITE_FACT
  add constraint FK_LL_CF_FORMID foreign key (FORMID)
  references FORM_DIM (FORMID)
/
alter table LASLINK_COMPOSITE_FACT
  add constraint FK_LL_CF_LEVELID foreign key (LEVELID)
  references LEVEL_DIM (LEVELID)
/
alter table LASLINK_COMPOSITE_FACT
  add constraint LL_COM_FACT_OTAADJC_FK1 foreign key (ATTR15ID)
  references ATTR15_DIM (ATTR15ID)
/
alter table LASLINK_COMPOSITE_FACT
  add constraint LL_COM_FACT_OTAADJFS_FK1 foreign key (ATTR16ID)
  references ATTR16_DIM (ATTR16ID)
/
alter table LASLINK_COMPOSITE_FACT
  add constraint LL_COM_FACT_OTAAP_FK1 foreign key (ATTR13ID)
  references ATTR13_DIM (ATTR13ID)
/
alter table LASLINK_COMPOSITE_FACT
  add constraint LL_COM_FACT_OTACALC_FK1 foreign key (ATTR12ID)
  references ATTR12_DIM (ATTR12ID)
/
alter table LASLINK_COMPOSITE_FACT
  add constraint LL_COM_FACT_OTASR_FK1 foreign key (ATTR11ID)
  references ATTR11_DIM (ATTR11ID)
/
alter table LASLINK_COMPOSITE_FACT
  add constraint LL_CO_FACT_COMPOSITE_DIM_FK foreign key (COMPOSITEID)
  references COMPOSITE_DIM (COMPOSITEID)
/
alter table LASLINK_COMPOSITE_FACT
  add constraint LL_CO_FACT_CURR_RES_DIM foreign key (CURRENT_RESULTID)
  references CURRENT_RESULT_DIM (CURRENT_RESULTID)
/
alter table LASLINK_COMPOSITE_FACT
  add constraint LL_CO_FACT_ORG_NODE_DIM_FK foreign key (ORG_NODEID)
  references ORG_NODE_DIM (ORG_NODEID)
/
alter table LASLINK_COMPOSITE_FACT
  add constraint LL_CO_FACT_SESSION_DIM_FK8 foreign key (SESSIONID)
  references SESSION_DIM (SESSIONID)
/
alter table LASLINK_COMPOSITE_FACT
  add constraint LL_FK_COMPOSITE_FACT_STUDENTID foreign key (STUDENTID)
  references STUDENT_DIM (STUDENTID)
/
alter table LASLINK_COMPOSITE_FACT
  add constraint LL_R6_10 foreign key (ATTR1ID)
  references ATTR1_DIM (ATTR1ID) on delete set null
/
alter table LASLINK_COMPOSITE_FACT
  add constraint LL_R6_15 foreign key (ATTR7ID)
  references ATTR7_DIM (ATTR7ID) on delete set null
/
alter table LASLINK_COMPOSITE_FACT
  add constraint LL_R6_16 foreign key (ATTR2ID)
  references ATTR2_DIM (ATTR2ID) on delete set null
/
alter table LASLINK_COMPOSITE_FACT
  add constraint LL_R6_17 foreign key (ATTR5ID)
  references ATTR5_DIM (ATTR5ID) on delete set null
/
alter table LASLINK_COMPOSITE_FACT
  add constraint LL_R6_18 foreign key (ATTR6ID)
  references ATTR6_DIM (ATTR6ID) on delete set null
/
alter table LASLINK_COMPOSITE_FACT
  add constraint LL_R6_39 foreign key (GRADEID)
  references GRADE_DIM (GRADEID) on delete set null
/
alter table LASLINK_COMPOSITE_FACT
  add constraint LL_R6_4 foreign key (ATTR4ID)
  references ATTR4_DIM (ATTR4ID) on delete set null
/
alter table LASLINK_COMPOSITE_FACT
  add constraint LL_R6_43 foreign key (ATTR8ID)
  references ATTR8_DIM (ATTR8ID) on delete set null
/
alter table LASLINK_COMPOSITE_FACT
  add constraint LL_R6_8 foreign key (ATTR3ID)
  references ATTR3_DIM (ATTR3ID) on delete set null
/
alter table LASLINK_COMPOSITE_FACT
  add constraint LL_R6_9 foreign key (ATTR10ID)
  references ATTR10_DIM (ATTR10ID) on delete set null
/


-- Create table LASLINK_CONTENT_AREA_FACT
create table LASLINK_CONTENT_AREA_FACT
(
  FACTID                    NUMBER not null,
  CONTENT_AREAID            NUMBER not null,
  STUDENTID                 NUMBER not null,
  FORMID                    NUMBER not null,
  SESSIONID                 NUMBER not null,
  ATTR1ID                   NUMBER,
  ATTR2ID                   NUMBER,
  ATTR3ID                   NUMBER,
  ATTR4ID                   NUMBER,
  ATTR5ID                   NUMBER,
  ATTR6ID                   NUMBER,
  ATTR7ID                   NUMBER,
  ATTR8ID                   NUMBER,
  ATTR9ID                   NUMBER,
  ATTR10ID                  NUMBER,
  ATTR11ID                  NUMBER,
  ATTR12ID                  NUMBER,
  ATTR13ID                  NUMBER,
  ATTR14ID                  NUMBER,
  ATTR15ID                  NUMBER,
  ATTR16ID                  NUMBER,
  GRADEID                   NUMBER,
  LEVELID                   NUMBER not null,
  ORG_NODEID                NUMBER not null,
  ASSESSMENTID              NUMBER not null,
  PROGRAMID                 NUMBER,
  CURRENT_RESULTID          NUMBER not null,
  SUBJECTID                 NUMBER,
  SCALE_SCORE               NUMBER,
  GRADE_EQUIVALENT          NUMBER,
  NORMAL_CURVE_EQUIVALENT   NUMBER,
  PERCENTAGE_MASTERY        NUMBER,
  NATIONAL_PERCENTILE       NUMBER,
  PROFICENCY_LEVEL          NUMBER,
  POINTS_ATTEMPTED          NUMBER,
  PERCENT_OBTAINED          NUMBER,
  POINTS_OBTAINED           NUMBER,
  POINTS_POSSIBLE           NUMBER,
  TEST_COMPLETION_TIMESTAMP DATE,
  TEST_START_TIMESTAMP      DATE
)
/
-- Create/Recreate primary, unique and foreign key constraints 
alter table LASLINK_CONTENT_AREA_FACT
  add constraint XPKLASLINK_CONTENT_AREA_FACT primary key (FACTID)
/
alter table LASLINK_CONTENT_AREA_FACT
  add constraint LL_CA_FACT_CONTENT_AREA_DIM_FK foreign key (CONTENT_AREAID)
  references CONTENT_AREA_DIM (CONTENT_AREAID)
/
alter table LASLINK_CONTENT_AREA_FACT
  add constraint LL_CA_FACT_CURR_RES_DIM foreign key (CURRENT_RESULTID)
  references CURRENT_RESULT_DIM (CURRENT_RESULTID)
/
alter table LASLINK_CONTENT_AREA_FACT
  add constraint LL_CA_FACT_FORM_DIM_FK7 foreign key (FORMID)
  references FORM_DIM (FORMID)
/
alter table LASLINK_CONTENT_AREA_FACT
  add constraint LL_CA_FACT_LEVEL_DIM_FK7 foreign key (LEVELID)
  references LEVEL_DIM (LEVELID)
/
alter table LASLINK_CONTENT_AREA_FACT
  add constraint LL_CA_FACT_ORG_NODE_DIM_FK foreign key (ORG_NODEID)
  references ORG_NODE_DIM (ORG_NODEID)
/
alter table LASLINK_CONTENT_AREA_FACT
  add constraint LL_CA_FACT_OTAADJC_FK2 foreign key (ATTR15ID)
  references ATTR15_DIM (ATTR15ID)
/
alter table LASLINK_CONTENT_AREA_FACT
  add constraint LL_CA_FACT_OTAADJFS_FK2 foreign key (ATTR16ID)
  references ATTR16_DIM (ATTR16ID)
/
alter table LASLINK_CONTENT_AREA_FACT
  add constraint LL_CA_FACT_OTAAL_FK2 foreign key (ATTR13ID)
  references ATTR13_DIM (ATTR13ID)
/
alter table LASLINK_CONTENT_AREA_FACT
  add constraint LL_CA_FACT_OTACALCULATOR_FK2 foreign key (ATTR12ID)
  references ATTR12_DIM (ATTR12ID)
/
alter table LASLINK_CONTENT_AREA_FACT
  add constraint LL_CA_FACT_OTASR_FK2 foreign key (ATTR11ID)
  references ATTR11_DIM (ATTR11ID)
/
alter table LASLINK_CONTENT_AREA_FACT
  add constraint LL_CA_FACT_OTAUT_FK2 foreign key (ATTR14ID)
  references ATTR14_DIM (ATTR14ID)
/
alter table LASLINK_CONTENT_AREA_FACT
  add constraint LL_CA_FACT_SESSION_DIM_FK8 foreign key (SESSIONID)
  references SESSION_DIM (SESSIONID)
/
alter table LASLINK_CONTENT_AREA_FACT
  add constraint LL_FK_CA_FACT_STUDENTID foreign key (STUDENTID)
  references STUDENT_DIM (STUDENTID)
/
alter table LASLINK_CONTENT_AREA_FACT
  add constraint LL_R5_10 foreign key (ATTR1ID)
  references ATTR1_DIM (ATTR1ID) on delete set null
/
alter table LASLINK_CONTENT_AREA_FACT
  add constraint LL_R5_15 foreign key (ATTR7ID)
  references ATTR7_DIM (ATTR7ID) on delete set null
/
alter table LASLINK_CONTENT_AREA_FACT
  add constraint LL_R5_16 foreign key (ATTR2ID)
  references ATTR2_DIM (ATTR2ID) on delete set null
/
alter table LASLINK_CONTENT_AREA_FACT
  add constraint LL_R5_17 foreign key (ATTR5ID)
  references ATTR5_DIM (ATTR5ID) on delete set null
/
alter table LASLINK_CONTENT_AREA_FACT
  add constraint LL_R5_18 foreign key (ATTR6ID)
  references ATTR6_DIM (ATTR6ID) on delete set null
/
alter table LASLINK_CONTENT_AREA_FACT
  add constraint LL_R5_39 foreign key (GRADEID)
  references GRADE_DIM (GRADEID) on delete set null
/
alter table LASLINK_CONTENT_AREA_FACT
  add constraint LL_R5_4 foreign key (ATTR4ID)
  references ATTR4_DIM (ATTR4ID) on delete set null
/
alter table LASLINK_CONTENT_AREA_FACT
  add constraint LL_R5_43 foreign key (ATTR8ID)
  references ATTR8_DIM (ATTR8ID) on delete set null
/
alter table LASLINK_CONTENT_AREA_FACT
  add constraint LL_R5_8 foreign key (ATTR3ID)
  references ATTR3_DIM (ATTR3ID) on delete set null
/
alter table LASLINK_CONTENT_AREA_FACT
  add constraint LL_R5_9 foreign key (ATTR10ID)
  references ATTR10_DIM (ATTR10ID) on delete set null
/



-- Create table LASLINK_ITEM_FACT
create table LASLINK_ITEM_FACT
(
  FACTID                    NUMBER not null,
  STUDENTID                 NUMBER not null,
  FORMID                    NUMBER not null,
  SESSIONID                 NUMBER not null,
  ATTR1ID                   NUMBER,
  ATTR2ID                   NUMBER,
  ATTR3ID                   NUMBER,
  ATTR4ID                   NUMBER,
  ATTR5ID                   NUMBER,
  ATTR6ID                   NUMBER,
  ATTR7ID                   NUMBER,
  ATTR8ID                   NUMBER,
  ATTR9ID                   NUMBER,
  ATTR10ID                  NUMBER,
  ATTR11ID                  NUMBER,
  ATTR12ID                  NUMBER,
  ATTR13ID                  NUMBER,
  ATTR14ID                  NUMBER,
  ATTR15ID                  NUMBER,
  ATTR16ID                  NUMBER,
  GRADEID                   NUMBER,
  LEVELID                   NUMBER not null,
  ITEMID                    NUMBER not null,
  ORG_NODEID                NUMBER not null,
  ASSESSMENTID              NUMBER not null,
  PROGRAMID                 NUMBER,
  CURRENT_RESULTID          NUMBER not null,
  POINTS_OBTAINED           NUMBER,
  ITEM_RESPONSE_TIMESTAMP   DATE,
  TEST_COMPLETION_TIMESTAMP DATE,
  TEST_START_TIMESTAMP      DATE,
  RESPONSEID                NUMBER,
  POINTS_POSSIBLE           NUMBER
)
/

-- Create/Recreate primary, unique and foreign key constraints 
alter table LASLINK_ITEM_FACT
  add constraint XPKLLITEM_FACT2 primary key (FACTID)
/
alter table LASLINK_ITEM_FACT
  add constraint LLITEM_FACT2_ETHNICITY_DIM_FK7 foreign key (ATTR2ID)
  references ATTR2_DIM (ATTR2ID) on delete set null
/
alter table LASLINK_ITEM_FACT
  add constraint LL_FK_ITEM_FACT2_STUDENTID foreign key (STUDENTID)
  references STUDENT_DIM (STUDENTID)
/
alter table LASLINK_ITEM_FACT
  add constraint LASLINK_ITEM_FACT2_ELL_DIM_FK7 foreign key (ATTR1ID)
  references ATTR1_DIM (ATTR1ID) on delete set null
/
alter table LASLINK_ITEM_FACT
  add constraint LASLINK_ITEM_FACT2_FORM_DIM_FK7 foreign key (FORMID)
  references FORM_DIM (FORMID)
/
alter table LASLINK_ITEM_FACT
  add constraint LASLINK_ITEM_FACT2_FRL_DIM_FK7 foreign key (ATTR3ID)
  references ATTR3_DIM (ATTR3ID) on delete set null
/
alter table LASLINK_ITEM_FACT
  add constraint LASLINK_ITEM_FACT2_GENDER_DIM_FK7 foreign key (ATTR4ID)
  references ATTR4_DIM (ATTR4ID) on delete set null
/
alter table LASLINK_ITEM_FACT
  add constraint LASLINK_ITEM_FACT2_GRADE_DIM_FK7 foreign key (GRADEID)
  references GRADE_DIM (GRADEID) on delete set null
/
alter table LASLINK_ITEM_FACT
  add constraint LASLINK_ITEM_FACT2_IEP_DIM_FK7 foreign key (ATTR5ID)
  references ATTR5_DIM (ATTR5ID) on delete set null
/
alter table LASLINK_ITEM_FACT
  add constraint LASLINK_ITEM_FACT2_ITEM_DIM_FK foreign key (ITEMID)
  references ITEM_DIM (ITEMID)
/
alter table LASLINK_ITEM_FACT
  add constraint LASLINK_ITEM_FACT2_LEP_DIM_FK7 foreign key (ATTR7ID)
  references ATTR7_DIM (ATTR7ID) on delete set null
/
alter table LASLINK_ITEM_FACT
  add constraint LASLINK_ITEM_FACT2_LEVEL_DIM_FK7 foreign key (LEVELID)
  references LEVEL_DIM (LEVELID)
/
alter table LASLINK_ITEM_FACT
  add constraint LASLINK_ITEM_FACT2_LFS_DIM_FK7 foreign key (ATTR6ID)
  references ATTR6_DIM (ATTR6ID) on delete set null
/
alter table LASLINK_ITEM_FACT
  add constraint LASLINK_ITEM_FACT2_MIGRANT_DIM_FK7 foreign key (ATTR8ID)
  references ATTR8_DIM (ATTR8ID) on delete set null
/
alter table LASLINK_ITEM_FACT
  add constraint LASLINK_ITEM_FACT2_ORG_NODE_DIM_FK foreign key (ORG_NODEID)
  references ORG_NODE_DIM (ORG_NODEID)
/
alter table LASLINK_ITEM_FACT
  add constraint LASLINK_ITEM_FACT2_OTAADJC_DIM_FK7 foreign key (ATTR15ID)
  references ATTR15_DIM (ATTR15ID)
/
alter table LASLINK_ITEM_FACT
  add constraint LASLINK_ITEM_FACT2_OTAADJFS_DIM_FK7 foreign key (ATTR16ID)
  references ATTR16_DIM (ATTR16ID)
/
alter table LASLINK_ITEM_FACT
  add constraint LASLINK_ITEM_FACT2_OTAAP_DIM_FK7 foreign key (ATTR13ID)
  references ATTR13_DIM (ATTR13ID)
/
alter table LASLINK_ITEM_FACT
  add constraint LASLINK_ITEM_FACT2_OTACAL_DIM_FK7 foreign key (ATTR12ID)
  references ATTR12_DIM (ATTR12ID)
/
alter table LASLINK_ITEM_FACT
  add constraint LASLINK_ITEM_FACT2_OTASM_DIM_FK7 foreign key (ATTR9ID)
  references ATTR9_DIM (ATTR9ID) on delete set null
/
alter table LASLINK_ITEM_FACT
  add constraint LASLINK_ITEM_FACT2_OTASR_DIM_FK7 foreign key (ATTR11ID)
  references ATTR11_DIM (ATTR11ID)
/
alter table LASLINK_ITEM_FACT
  add constraint LASLINK_ITEM_FACT2_OTAUT_DIM_FK7 foreign key (ATTR14ID)
  references ATTR14_DIM (ATTR14ID)
/
alter table LASLINK_ITEM_FACT
  add constraint LASLINK_ITEM_FACT2_RESPONSE_DIM_FK foreign key (RESPONSEID)
  references RESPONSE_DIM (RESPONSEID)
/
alter table LASLINK_ITEM_FACT
  add constraint LASLINK_ITEM_FACT2_S504_DIM_FK7 foreign key (ATTR10ID)
  references ATTR10_DIM (ATTR10ID) on delete set null
/
alter table LASLINK_ITEM_FACT
  add constraint LASLINK_ITEM_FACT2_SESSION_DIM_FK8 foreign key (SESSIONID)
  references SESSION_DIM (SESSIONID)
/
alter table LASLINK_ITEM_FACT
  add constraint LASLINK_ITEM_FACT_CURR_RES_DIM foreign key (CURRENT_RESULTID)
  references CURRENT_RESULT_DIM (CURRENT_RESULTID)
/



-- Create table LASLINK_PRIM_OBJ_FACT
create table LASLINK_PRIM_OBJ_FACT
(
  FACTID                    NUMBER not null,
  PRIM_OBJID                NUMBER not null,
  STUDENTID                 NUMBER not null,
  FORMID                    NUMBER not null,
  SESSIONID                 NUMBER not null,
  ATTR1ID                   NUMBER,
  ATTR2ID                   NUMBER,
  ATTR3ID                   NUMBER,
  ATTR4ID                   NUMBER,
  ATTR5ID                   NUMBER,
  ATTR6ID                   NUMBER,
  ATTR7ID                   NUMBER,
  ATTR8ID                   NUMBER,
  ATTR9ID                   NUMBER,
  ATTR10ID                  NUMBER,
  ATTR11ID                  NUMBER,
  ATTR12ID                  NUMBER,
  ATTR13ID                  NUMBER,
  ATTR14ID                  NUMBER,
  ATTR15ID                  NUMBER,
  ATTR16ID                  NUMBER,
  GRADEID                   NUMBER,
  LEVELID                   NUMBER not null,
  ORG_NODEID                NUMBER not null,
  ASSESSMENTID              NUMBER not null,
  PROGRAMID                 NUMBER,
  CURRENT_RESULTID          NUMBER not null,
  MASTERY_LEVELID           NUMBER,
  POINTS_ATTEMPTED          NUMBER,
  PERCENT_OBTAINED          NUMBER,
  POINTS_OBTAINED           NUMBER,
  TEST_COMPLETION_TIMESTAMP DATE,
  TEST_START_TIMESTAMP      DATE,
  POINTS_POSSIBLE           NUMBER
)
/

-- Create/Recreate primary, unique and foreign key constraints 
alter table LASLINK_PRIM_OBJ_FACT
  add constraint XPKLLPRIM_OBJ_FACT primary key (FACTID)
/
alter table LASLINK_PRIM_OBJ_FACT
  add constraint LL_FK_PO_FACT_MAST_LEVELID foreign key (MASTERY_LEVELID)
  references MASTERY_LEVEL_DIM (MASTERY_LEVELID)
/
alter table LASLINK_PRIM_OBJ_FACT
  add constraint LL_FK_PRIM_OBJ_FACT_STUDENTID foreign key (STUDENTID)
  references STUDENT_DIM (STUDENTID)
/
alter table LASLINK_PRIM_OBJ_FACT
  add constraint LL_PO_FACT_CURR_RES_DIM foreign key (CURRENT_RESULTID)
  references CURRENT_RESULT_DIM (CURRENT_RESULTID)
/
alter table LASLINK_PRIM_OBJ_FACT
  add constraint LL_PO_FACT_FORM_DIM_FK7 foreign key (FORMID)
  references FORM_DIM (FORMID)
/
alter table LASLINK_PRIM_OBJ_FACT
  add constraint LL_PO_FACT_ORG_NODE_DIM_FK foreign key (ORG_NODEID)
  references ORG_NODE_DIM (ORG_NODEID)
/
alter table LASLINK_PRIM_OBJ_FACT
  add constraint LL_PO_FACT_OTAADJC_FK1 foreign key (ATTR15ID)
  references ATTR15_DIM (ATTR15ID)
/
alter table LASLINK_PRIM_OBJ_FACT
  add constraint LL_PO_FACT_OTAADJFS_FK1 foreign key (ATTR16ID)
  references ATTR16_DIM (ATTR16ID)
/
alter table LASLINK_PRIM_OBJ_FACT
  add constraint LL_PO_FACT_OTAAP_FK1 foreign key (ATTR13ID)
  references ATTR13_DIM (ATTR13ID)
/
alter table LASLINK_PRIM_OBJ_FACT
  add constraint LL_PO_FACT_OTACALCULATOR_FK1 foreign key (ATTR12ID)
  references ATTR12_DIM (ATTR12ID)
/
alter table LASLINK_PRIM_OBJ_FACT
  add constraint LL_PO_FACT_OTASR_FK1 foreign key (ATTR11ID)
  references ATTR11_DIM (ATTR11ID)
/
alter table LASLINK_PRIM_OBJ_FACT
  add constraint LL_PO_FACT_OTAUT_FK1 foreign key (ATTR14ID)
  references ATTR14_DIM (ATTR14ID)
/
alter table LASLINK_PRIM_OBJ_FACT
  add constraint LL_PO_FACT_PRIM_OBJ_DIM_FK foreign key (PRIM_OBJID)
  references PRIM_OBJ_DIM (PRIM_OBJID)
/
alter table LASLINK_PRIM_OBJ_FACT
  add constraint LL_PO_FACT_SESSION_DIM_FK8 foreign key (SESSIONID)
  references SESSION_DIM (SESSIONID)
/
alter table LASLINK_PRIM_OBJ_FACT
  add constraint LL_PRIM_OBJ_FACT_LEVEL_DIM_FK7 foreign key (LEVELID)
  references LEVEL_DIM (LEVELID)
/
alter table LASLINK_PRIM_OBJ_FACT
  add constraint LL_R4_10 foreign key (ATTR1ID)
  references ATTR1_DIM (ATTR1ID) on delete set null
/
alter table LASLINK_PRIM_OBJ_FACT
  add constraint LL_R4_15 foreign key (ATTR7ID)
  references ATTR7_DIM (ATTR7ID) on delete set null
/
alter table LASLINK_PRIM_OBJ_FACT
  add constraint LL_R4_16 foreign key (ATTR2ID)
  references ATTR2_DIM (ATTR2ID) on delete set null
/
alter table LASLINK_PRIM_OBJ_FACT
  add constraint LL_R4_17 foreign key (ATTR5ID)
  references ATTR5_DIM (ATTR5ID) on delete set null
/
alter table LASLINK_PRIM_OBJ_FACT
  add constraint LL_R4_18 foreign key (ATTR6ID)
  references ATTR6_DIM (ATTR6ID) on delete set null
/
alter table LASLINK_PRIM_OBJ_FACT
  add constraint LL_R4_39 foreign key (GRADEID)
  references GRADE_DIM (GRADEID) on delete set null
/
alter table LASLINK_PRIM_OBJ_FACT
  add constraint LL_R4_4 foreign key (ATTR4ID)
  references ATTR4_DIM (ATTR4ID) on delete set null
/
alter table LASLINK_PRIM_OBJ_FACT
  add constraint LL_R4_43 foreign key (ATTR8ID)
  references ATTR8_DIM (ATTR8ID) on delete set null
/
alter table LASLINK_PRIM_OBJ_FACT
  add constraint LL_R4_8 foreign key (ATTR3ID)
  references ATTR3_DIM (ATTR3ID) on delete set null
/
alter table LASLINK_PRIM_OBJ_FACT
  add constraint LL_R4_9 foreign key (ATTR10ID)
  references ATTR10_DIM (ATTR10ID) on delete set null
/



-- Create table LASLINK_SEC_OBJ_FACT
create table LASLINK_SEC_OBJ_FACT
(
  FACTID                    NUMBER not null,
  SEC_OBJID                 NUMBER not null,
  STUDENTID                 NUMBER not null,
  FORMID                    NUMBER not null,
  SESSIONID                 NUMBER not null,
  ATTR1ID                   NUMBER,
  ATTR2ID                   NUMBER,
  ATTR3ID                   NUMBER,
  ATTR4ID                   NUMBER,
  ATTR5ID                   NUMBER,
  ATTR6ID                   NUMBER,
  ATTR7ID                   NUMBER,
  ATTR8ID                   NUMBER,
  ATTR9ID                   NUMBER,
  ATTR10ID                  NUMBER,
  ATTR11ID                  NUMBER,
  ATTR12ID                  NUMBER,
  ATTR13ID                  NUMBER,
  ATTR14ID                  NUMBER,
  ATTR15ID                  NUMBER,
  ATTR16ID                  NUMBER,
  GRADEID                   NUMBER,
  LEVELID                   NUMBER not null,
  ORG_NODEID                NUMBER not null,
  ASSESSMENTID              NUMBER not null,
  PROGRAMID                 NUMBER,
  CURRENT_RESULTID          NUMBER not null,
  MASTERY_LEVELID           NUMBER,
  POINTS_ATTEMPTED          NUMBER,
  PERCENT_OBTAINED          NUMBER,
  POINTS_OBTAINED           NUMBER,
  TEST_COMPLETION_TIMESTAMP DATE,
  TEST_START_TIMESTAMP      DATE,
  POINTS_POSSIBLE           NUMBER
)/


-- Create/Recreate primary, unique and foreign key constraints 
alter table LASLINK_SEC_OBJ_FACT
  add constraint XPKLLSEC_OBJ_FACT primary key (FACTID)
/
alter table LASLINK_SEC_OBJ_FACT
  add constraint LL_FK_SO_FACT_MAST_LEVELID foreign key (MASTERY_LEVELID)
  references MASTERY_LEVEL_DIM (MASTERY_LEVELID)
/
alter table LASLINK_SEC_OBJ_FACT
  add constraint LL_FK_SO_FACT_STUDENTID foreign key (STUDENTID)
  references STUDENT_DIM (STUDENTID)
/
alter table LASLINK_SEC_OBJ_FACT
  add constraint LL_R3_10 foreign key (ATTR1ID)
  references ATTR1_DIM (ATTR1ID) on delete set null
/
alter table LASLINK_SEC_OBJ_FACT
  add constraint LL_R3_15 foreign key (ATTR7ID)
  references ATTR7_DIM (ATTR7ID) on delete set null
/
alter table LASLINK_SEC_OBJ_FACT
  add constraint LL_R3_16 foreign key (ATTR2ID)
  references ATTR2_DIM (ATTR2ID) on delete set null
/
alter table LASLINK_SEC_OBJ_FACT
  add constraint LL_R3_17 foreign key (ATTR5ID)
  references ATTR5_DIM (ATTR5ID) on delete set null
/
alter table LASLINK_SEC_OBJ_FACT
  add constraint LL_R3_18 foreign key (ATTR6ID)
  references ATTR6_DIM (ATTR6ID) on delete set null
/
alter table LASLINK_SEC_OBJ_FACT
  add constraint LL_R3_39 foreign key (GRADEID)
  references GRADE_DIM (GRADEID) on delete set null
/
alter table LASLINK_SEC_OBJ_FACT
  add constraint LL_R3_4 foreign key (ATTR4ID)
  references ATTR4_DIM (ATTR4ID) on delete set null
/
alter table LASLINK_SEC_OBJ_FACT
  add constraint LL_R3_43 foreign key (ATTR8ID)
  references ATTR8_DIM (ATTR8ID) on delete set null
/
alter table LASLINK_SEC_OBJ_FACT
  add constraint LL_R3_8 foreign key (ATTR3ID)
  references ATTR3_DIM (ATTR3ID) on delete set null
/
alter table LASLINK_SEC_OBJ_FACT
  add constraint LL_R3_9 foreign key (ATTR10ID)
  references ATTR10_DIM (ATTR10ID) on delete set null
/
alter table LASLINK_SEC_OBJ_FACT
  add constraint LL_SO_FACT_CURR_RES_DIM foreign key (CURRENT_RESULTID)
  references CURRENT_RESULT_DIM (CURRENT_RESULTID)
/
alter table LASLINK_SEC_OBJ_FACT
  add constraint LL_SO_FACT_FORM_DIM_FK7 foreign key (FORMID)
  references FORM_DIM (FORMID)
/
alter table LASLINK_SEC_OBJ_FACT
  add constraint LL_SO_FACT_LEVEL_DIM_FK7 foreign key (LEVELID)
  references LEVEL_DIM (LEVELID)
/
alter table LASLINK_SEC_OBJ_FACT
  add constraint LL_SO_FACT_ORG_NODE_DIM_FK foreign key (ORG_NODEID)
  references ORG_NODE_DIM (ORG_NODEID)
/
alter table LASLINK_SEC_OBJ_FACT
  add constraint LL_SO_FACT_OTAADJC_FK1 foreign key (ATTR15ID)
  references ATTR15_DIM (ATTR15ID)
/
alter table LASLINK_SEC_OBJ_FACT
  add constraint LL_SO_FACT_OTAADJFS_FK1 foreign key (ATTR16ID)
  references ATTR16_DIM (ATTR16ID)
/
alter table LASLINK_SEC_OBJ_FACT
  add constraint LL_SO_FACT_OTAAP_FK1 foreign key (ATTR13ID)
  references ATTR13_DIM (ATTR13ID)
/
alter table LASLINK_SEC_OBJ_FACT
  add constraint LL_SO_FACT_OTACALCULATOR_FK1 foreign key (ATTR12ID)
  references ATTR12_DIM (ATTR12ID)
/
alter table LASLINK_SEC_OBJ_FACT
  add constraint LL_SO_FACT_OTASR_FK1 foreign key (ATTR11ID)
  references ATTR11_DIM (ATTR11ID)
/
alter table LASLINK_SEC_OBJ_FACT
  add constraint LL_SO_FACT_OTAUT_FK1 foreign key (ATTR14ID)
  references ATTR14_DIM (ATTR14ID)
/
alter table LASLINK_SEC_OBJ_FACT
  add constraint LL_SO_FACT_SEC_OBJ_DIM_FK foreign key (SEC_OBJID)
  references SEC_OBJ_DIM (SEC_OBJID)
/
alter table LASLINK_SEC_OBJ_FACT
  add constraint LL_SO_FACT_SESSION_DIM_FK8 foreign key (SESSIONID)
  references SESSION_DIM (SESSIONID)
/


--create table ATTR17_DIM
create table ATTR17_DIM
(
  ATTR17ID        NUMBER not NULL ,
  NAME           VARCHAR2(64) not null,
  TYPE           VARCHAR2(32),
  PRODUCT_TYPEID NUMBER default 1 not NULL REFERENCES PRODUCT_TYPE_DIM(PRODUCT_TYPEID),
  CONSTRAINT XPKHOME_LANGUAGE_DIM primary key (ATTR17ID)
)
/



--create table ATTR18_DIM
create table ATTR18_DIM
(
  ATTR18ID        NUMBER not NULL ,
  NAME           VARCHAR2(64) not null,
  TYPE           VARCHAR2(32),
  PRODUCT_TYPEID NUMBER default 1 not NULL REFERENCES PRODUCT_TYPE_DIM(PRODUCT_TYPEID),
  CONSTRAINT XPKMOBILITY_DIM primary key (ATTR18ID)
)
/



--create table ATTR19_DIM
create table ATTR19_DIM
(
  ATTR19ID        NUMBER not NULL ,
  NAME           VARCHAR2(64) not null,
  TYPE           VARCHAR2(32),
  PRODUCT_TYPEID NUMBER default 1 not NULL REFERENCES PRODUCT_TYPE_DIM(PRODUCT_TYPEID),
  CONSTRAINT XPKUSA_SCHOOL_ENROLLMENT_DIM primary key (ATTR19ID)
)
/



--create table ATTR20_DIM
create table ATTR20_DIM
(
  ATTR20ID        NUMBER not NULL ,
  NAME           VARCHAR2(64) not null,
  TYPE           VARCHAR2(32),
  PRODUCT_TYPEID NUMBER default 1 not NULL REFERENCES PRODUCT_TYPE_DIM(PRODUCT_TYPEID),
  CONSTRAINT XPKPROGRAM_PARTICIPATION_DIM primary key (ATTR20ID)
)
/


--create table ATTR21_DIM
create table ATTR21_DIM
(
  ATTR21ID        NUMBER not NULL ,
  NAME           VARCHAR2(64) not null,
  TYPE           VARCHAR2(32),
  PRODUCT_TYPEID NUMBER default 1 not NULL REFERENCES PRODUCT_TYPE_DIM(PRODUCT_TYPEID),
  CONSTRAINT XPKSPECIAL_EDUCATION_DIM primary key (ATTR21ID)
)
/



--create table ATTR22_DIM
create table ATTR22_DIM
(
  ATTR22ID        NUMBER not NULL ,
  NAME           VARCHAR2(64) not null,
  TYPE           VARCHAR2(32),
  PRODUCT_TYPEID NUMBER default 1 not NULL REFERENCES PRODUCT_TYPE_DIM(PRODUCT_TYPEID),
  CONSTRAINT XPKDISABILITY_DIM primary key (ATTR22ID)
)
/



--create table ATTR23_DIM
create table ATTR23_DIM
(
  ATTR23ID        NUMBER not NULL ,
  NAME           VARCHAR2(64) not null,
  TYPE           VARCHAR2(32),
  PRODUCT_TYPEID NUMBER default 1 not NULL REFERENCES PRODUCT_TYPE_DIM(PRODUCT_TYPEID),
  CONSTRAINT XPKACCOMMODATIONS_DIM primary key (ATTR23ID)
)
/


--create table ATTR24_DIM
create table ATTR24_DIM
(
  ATTR24ID        NUMBER not NULL ,
  NAME           VARCHAR2(64) not null,
  TYPE           VARCHAR2(32),
  PRODUCT_TYPEID NUMBER default 1 not NULL REFERENCES PRODUCT_TYPE_DIM(PRODUCT_TYPEID),
  CONSTRAINT XPKSPECIAL_CODES_DIM primary key (ATTR24ID)
)
/

--Tables for LasLink Accommodation
create table attr35_dim (attr35id number primary key, name varchar2(32), type varchar2(32), product_typeid references product_type_dim(product_typeid))
/
create table attr36_dim (attr36id number primary key, name varchar2(32), type varchar2(32), product_typeid references product_type_dim(product_typeid))
/
create table attr37_dim (attr37id number primary key, name varchar2(32), type varchar2(32), product_typeid references product_type_dim(product_typeid))
/

-- modifying columns of lasLink tables to accommodate new demographics and accommodations
alter table laslink_composite_fact drop column attr1id
/
alter table laslink_composite_fact drop column attr3id
/
alter table laslink_composite_fact drop column attr4id
/
alter table laslink_composite_fact drop column attr5id
/
alter table laslink_composite_fact drop column attr6id
/
alter table laslink_composite_fact drop column attr7id
/
alter table laslink_composite_fact drop column attr8id
/
alter table laslink_composite_fact drop column attr10id
/

alter table laslink_composite_fact add attr17id number references attr17_dim(attr17id)
/
alter table laslink_composite_fact add attr18id number references attr18_dim(attr18id)
/
alter table laslink_composite_fact add attr19id number references attr19_dim(attr19id)
/
alter table laslink_composite_fact add attr20id number references attr20_dim(attr20id)
/
alter table laslink_composite_fact add attr21id number references attr21_dim(attr21id)
/
alter table laslink_composite_fact add attr22id number references attr22_dim(attr22id)
/
alter table laslink_composite_fact add attr23id number references attr23_dim(attr23id)
/
alter table laslink_composite_fact add attr25id number references attr24_dim(attr24id)
/
alter table laslink_composite_fact add attr26id number references attr24_dim(attr24id)
/
alter table laslink_composite_fact add attr27id number references attr24_dim(attr24id)
/
alter table laslink_composite_fact add attr28id number references attr24_dim(attr24id)
/
alter table laslink_composite_fact add attr29id number references attr24_dim(attr24id)
/
alter table laslink_composite_fact add attr30id number references attr24_dim(attr24id)
/
alter table laslink_composite_fact add attr31id number references attr24_dim(attr24id)
/
alter table laslink_composite_fact add attr32id number references attr24_dim(attr24id)
/
alter table laslink_composite_fact add attr33id number references attr24_dim(attr24id)
/
alter table laslink_composite_fact add attr34id number references attr24_dim(attr24id)
/
alter table laslink_composite_fact add attr35id number references attr35_dim(attr35id)
/
alter table laslink_composite_fact add attr36id number references attr36_dim(attr36id)
/
alter table laslink_composite_fact add attr37id number references attr37_dim(attr37id)
/

alter table laslink_content_area_fact drop column attr1id
/
alter table laslink_content_area_fact drop column attr3id
/
alter table laslink_content_area_fact drop column attr4id
/
alter table laslink_content_area_fact drop column attr5id
/
alter table laslink_content_area_fact drop column attr6id
/
alter table laslink_content_area_fact drop column attr7id
/
alter table laslink_content_area_fact drop column attr8id
/
alter table laslink_content_area_fact drop column attr10id
/

alter table laslink_content_area_fact add attr17id number references attr17_dim(attr17id)
/
alter table laslink_content_area_fact add attr18id number references attr18_dim(attr18id)
/
alter table laslink_content_area_fact add attr19id number references attr19_dim(attr19id)
/
alter table laslink_content_area_fact add attr20id number references attr20_dim(attr20id)
/
alter table laslink_content_area_fact add attr21id number references attr21_dim(attr21id)
/
alter table laslink_content_area_fact add attr22id number references attr22_dim(attr22id)
/
alter table laslink_content_area_fact add attr23id number references attr23_dim(attr23id)
/
alter table laslink_content_area_fact add attr25id number references attr24_dim(attr24id)
/
alter table laslink_content_area_fact add attr26id number references attr24_dim(attr24id)
/
alter table laslink_content_area_fact add attr27id number references attr24_dim(attr24id)
/
alter table laslink_content_area_fact add attr28id number references attr24_dim(attr24id)
/
alter table laslink_content_area_fact add attr29id number references attr24_dim(attr24id)
/
alter table laslink_content_area_fact add attr30id number references attr24_dim(attr24id)
/
alter table laslink_content_area_fact add attr31id number references attr24_dim(attr24id)
/
alter table laslink_content_area_fact add attr32id number references attr24_dim(attr24id)
/
alter table laslink_content_area_fact add attr33id number references attr24_dim(attr24id)
/
alter table laslink_content_area_fact add attr34id number references attr24_dim(attr24id)
/
alter table laslink_content_area_fact add attr35id number references attr35_dim(attr35id)
/
alter table laslink_content_area_fact add attr36id number references attr36_dim(attr36id)
/
alter table laslink_content_area_fact add attr37id number references attr37_dim(attr37id)
/


alter table laslink_item_fact drop column attr1id
/
alter table laslink_item_fact drop column attr3id
/
alter table laslink_item_fact drop column attr4id
/
alter table laslink_item_fact drop column attr5id
/
alter table laslink_item_fact drop column attr6id
/
alter table laslink_item_fact drop column attr7id
/
alter table laslink_item_fact drop column attr8id
/
alter table laslink_item_fact drop column attr10id
/
alter table laslink_item_fact add attr17id number references attr17_dim(attr17id)
/
alter table laslink_item_fact add attr18id number references attr18_dim(attr18id)
/
alter table laslink_item_fact add attr19id number references attr19_dim(attr19id)
/
alter table laslink_item_fact add attr20id number references attr20_dim(attr20id)
/
alter table laslink_item_fact add attr21id number references attr21_dim(attr21id)
/
alter table laslink_item_fact add attr22id number references attr22_dim(attr22id)
/
alter table laslink_item_fact add attr23id number references attr23_dim(attr23id)
/
alter table laslink_item_fact add attr25id number references attr24_dim(attr24id)
/
alter table laslink_item_fact add attr26id number references attr24_dim(attr24id)
/
alter table laslink_item_fact add attr27id number references attr24_dim(attr24id)
/
alter table laslink_item_fact add attr28id number references attr24_dim(attr24id)
/
alter table laslink_item_fact add attr29id number references attr24_dim(attr24id)
/
alter table laslink_item_fact add attr30id number references attr24_dim(attr24id)
/
alter table laslink_item_fact add attr31id number references attr24_dim(attr24id)
/
alter table laslink_item_fact add attr32id number references attr24_dim(attr24id)
/
alter table laslink_item_fact add attr33id number references attr24_dim(attr24id)
/
alter table laslink_item_fact add attr34id number references attr24_dim(attr24id)
/
alter table laslink_item_fact add attr35id number references attr35_dim(attr35id)
/
alter table laslink_item_fact add attr36id number references attr36_dim(attr36id)
/
alter table laslink_item_fact add attr37id number references attr37_dim(attr37id)
/


alter table laslink_prim_obj_fact drop column attr1id
/
alter table laslink_prim_obj_fact drop column attr3id
/
alter table laslink_prim_obj_fact drop column attr4id
/
alter table laslink_prim_obj_fact drop column attr5id
/
alter table laslink_prim_obj_fact drop column attr6id
/
alter table laslink_prim_obj_fact drop column attr7id
/
alter table laslink_prim_obj_fact drop column attr8id
/
alter table laslink_prim_obj_fact drop column attr10id
/
alter table laslink_prim_obj_fact add attr17id number references attr17_dim(attr17id)
/
alter table laslink_prim_obj_fact add attr18id number references attr18_dim(attr18id)
/
alter table laslink_prim_obj_fact add attr19id number references attr19_dim(attr19id)
/
alter table laslink_prim_obj_fact add attr20id number references attr20_dim(attr20id)
/
alter table laslink_prim_obj_fact add attr21id number references attr21_dim(attr21id)
/
alter table laslink_prim_obj_fact add attr22id number references attr22_dim(attr22id)
/
alter table laslink_prim_obj_fact add attr23id number references attr23_dim(attr23id)
/
alter table laslink_prim_obj_fact add attr25id number references attr24_dim(attr24id)
/
alter table laslink_prim_obj_fact add attr26id number references attr24_dim(attr24id)
/
alter table laslink_prim_obj_fact add attr27id number references attr24_dim(attr24id)
/
alter table laslink_prim_obj_fact add attr28id number references attr24_dim(attr24id)
/
alter table laslink_prim_obj_fact add attr29id number references attr24_dim(attr24id)
/
alter table laslink_prim_obj_fact add attr30id number references attr24_dim(attr24id)
/
alter table laslink_prim_obj_fact add attr31id number references attr24_dim(attr24id)
/
alter table laslink_prim_obj_fact add attr32id number references attr24_dim(attr24id)
/
alter table laslink_prim_obj_fact add attr33id number references attr24_dim(attr24id)
/
alter table laslink_prim_obj_fact add attr34id number references attr24_dim(attr24id)
/
alter table laslink_prim_obj_fact add attr35id number references attr35_dim(attr35id)
/
alter table laslink_prim_obj_fact add attr36id number references attr36_dim(attr36id)
/
alter table laslink_prim_obj_fact add attr37id number references attr37_dim(attr37id)
/




alter table laslink_sec_obj_fact drop column attr1id
/
alter table laslink_sec_obj_fact drop column attr3id
/
alter table laslink_sec_obj_fact drop column attr4id
/
alter table laslink_sec_obj_fact drop column attr5id
/
alter table laslink_sec_obj_fact drop column attr6id
/
alter table laslink_sec_obj_fact drop column attr7id
/
alter table laslink_sec_obj_fact drop column attr8id
/
alter table laslink_sec_obj_fact drop column attr10id
/
alter table laslink_sec_obj_fact add attr17id number references attr17_dim(attr17id)
/
alter table laslink_sec_obj_fact add attr18id number references attr18_dim(attr18id)
/
alter table laslink_sec_obj_fact add attr19id number references attr19_dim(attr19id)
/
alter table laslink_sec_obj_fact add attr20id number references attr20_dim(attr20id)
/
alter table laslink_sec_obj_fact add attr21id number references attr21_dim(attr21id)
/
alter table laslink_sec_obj_fact add attr22id number references attr22_dim(attr22id)
/
alter table laslink_sec_obj_fact add attr23id number references attr23_dim(attr23id)
/
alter table laslink_sec_obj_fact add attr25id number references attr24_dim(attr24id)
/
alter table laslink_sec_obj_fact add attr26id number references attr24_dim(attr24id)
/
alter table laslink_sec_obj_fact add attr27id number references attr24_dim(attr24id)
/
alter table laslink_sec_obj_fact add attr28id number references attr24_dim(attr24id)
/
alter table laslink_sec_obj_fact add attr29id number references attr24_dim(attr24id)
/
alter table laslink_sec_obj_fact add attr30id number references attr24_dim(attr24id)
/
alter table laslink_sec_obj_fact add attr31id number references attr24_dim(attr24id)
/
alter table laslink_sec_obj_fact add attr32id number references attr24_dim(attr24id)
/
alter table laslink_sec_obj_fact add attr33id number references attr24_dim(attr24id)
/
alter table laslink_sec_obj_fact add attr34id number references attr24_dim(attr24id)
/
alter table laslink_sec_obj_fact add attr35id number references attr35_dim(attr35id)
/
alter table laslink_sec_obj_fact add attr36id number references attr36_dim(attr36id)
/
alter table laslink_sec_obj_fact add attr37id number references attr37_dim(attr37id)
/