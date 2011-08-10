-- Create sequence SEQ_LL_COMPOSITE_FACT_ID
create sequence SEQ_LL_COMPOSITE_FACT_ID
minvalue 0
maxvalue 999999999999999999999999999
start with 20000
increment by 1
nocache
/

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

--create table ATTR25_DIM
create table ATTR25_DIM
(
  ATTR25ID        NUMBER not NULL ,
  NAME           VARCHAR2(64) not null,
  TYPE           VARCHAR2(32),
  PRODUCT_TYPEID NUMBER default 4 not NULL REFERENCES PRODUCT_TYPE_DIM(PRODUCT_TYPEID),
  CONSTRAINT XPKSPECIALCODEK_DIM primary key (ATTR25ID)
)
/

--create table ATTR26_DIM
create table ATTR26_DIM
(
  ATTR26ID        NUMBER not NULL ,
  NAME           VARCHAR2(64) not null,
  TYPE           VARCHAR2(32),
  PRODUCT_TYPEID NUMBER default 4 not NULL REFERENCES PRODUCT_TYPE_DIM(PRODUCT_TYPEID),
  CONSTRAINT XPKSPECIALCODEL_DIM primary key (ATTR26ID)
)
/

--create table ATTR27_DIM
create table ATTR27_DIM
(
  ATTR27ID        NUMBER not NULL ,
  NAME           VARCHAR2(64) not null,
  TYPE           VARCHAR2(32),
  PRODUCT_TYPEID NUMBER default 4 not NULL REFERENCES PRODUCT_TYPE_DIM(PRODUCT_TYPEID),
  CONSTRAINT XPKSPECIALCODEM_DIM primary key (ATTR27ID)
)
/

--create table ATTR28_DIM
create table ATTR28_DIM
(
  ATTR28ID        NUMBER not NULL ,
  NAME           VARCHAR2(64) not null,
  TYPE           VARCHAR2(32),
  PRODUCT_TYPEID NUMBER default 4 not NULL REFERENCES PRODUCT_TYPE_DIM(PRODUCT_TYPEID),
  CONSTRAINT XPKSPECIALCODEN_DIM primary key (ATTR28ID)
)
/

--create table ATTR29_DIM
create table ATTR29_DIM
(
  ATTR29ID        NUMBER not NULL ,
  NAME           VARCHAR2(64) not null,
  TYPE           VARCHAR2(32),
  PRODUCT_TYPEID NUMBER default 4 not NULL REFERENCES PRODUCT_TYPE_DIM(PRODUCT_TYPEID),
  CONSTRAINT XPKSPECIALCODEO_DIM primary key (ATTR29ID)
)
/

--create table ATTR30_DIM
create table ATTR30_DIM
(
  ATTR30ID        NUMBER not NULL ,
  NAME           VARCHAR2(64) not null,
  TYPE           VARCHAR2(32),
  PRODUCT_TYPEID NUMBER default 4 not NULL REFERENCES PRODUCT_TYPE_DIM(PRODUCT_TYPEID),
  CONSTRAINT XPKSPECIALCODEP_DIM primary key (ATTR30ID)
)
/

--create table ATTR31_DIM
create table ATTR31_DIM
(
  ATTR31ID        NUMBER not NULL ,
  NAME           VARCHAR2(64) not null,
  TYPE           VARCHAR2(32),
  PRODUCT_TYPEID NUMBER default 4 not NULL REFERENCES PRODUCT_TYPE_DIM(PRODUCT_TYPEID),
  CONSTRAINT XPKSPECIALCODEQ_DIM primary key (ATTR31ID)
)
/

--create table ATTR32_DIM
create table ATTR32_DIM
(
  ATTR32ID        NUMBER not NULL ,
  NAME           VARCHAR2(64) not null,
  TYPE           VARCHAR2(32),
  PRODUCT_TYPEID NUMBER default 4 not NULL REFERENCES PRODUCT_TYPE_DIM(PRODUCT_TYPEID),
  CONSTRAINT XPKSPECIALCODER_DIM primary key (ATTR32ID)
)
/

--create table ATTR33_DIM
create table ATTR33_DIM
(
  ATTR33ID        NUMBER not NULL ,
  NAME           VARCHAR2(64) not null,
  TYPE           VARCHAR2(32),
  PRODUCT_TYPEID NUMBER default 4 not NULL REFERENCES PRODUCT_TYPE_DIM(PRODUCT_TYPEID),
  CONSTRAINT XPKSPECIALCODES primary key (ATTR33ID)
)
/

--Table for New Accommodation Auditory Calming
create table attr35_dim 
(
 attr35id number primary key , 
 name varchar2(32) , 
 type varchar2(32) , 
 product_typeid references product_type_dim(product_typeid)
)
/

--Table for New Accommodation Masking Ruler
create table attr36_dim 
(
 attr36id number primary key , 
 name varchar2(32) , 
 type varchar2(32) , 
 product_typeid references product_type_dim(product_typeid)
)
/

--Table for New Accommodation Magnifying Glass
create table attr37_dim 
(
 attr37id number primary key , 
 name varchar2(32) , 
 type varchar2(32) , 
 product_typeid references product_type_dim(product_typeid)
)
/

--create table LASLINK_COMPOSITE_FACT
create table LASLINK_COMPOSITE_FACT
(
  FACTID                    NUMBER not null,
  COMPOSITEID               NUMBER not null,
  STUDENTID                 NUMBER not null,
  SESSIONID                 NUMBER not null,
  ATTR2ID                   NUMBER default 34,
  ATTR9ID                   NUMBER default 6,
  ATTR11ID                  NUMBER default 6,
  ATTR12ID                  NUMBER default 6,
  ATTR13ID                  NUMBER default 6,
  ATTR14ID                  NUMBER default 6,
  ATTR15ID                  NUMBER default 6,
  ATTR16ID                  NUMBER default 6,
  ATTR17ID                  NUMBER default 101 references ATTR17_DIM(ATTR17ID),
  ATTR18ID                  NUMBER default 14 references ATTR18_DIM(ATTR18ID),
  ATTR19ID                  NUMBER default 122 references ATTR19_DIM(ATTR19ID),
  ATTR20ID                  varchar2(60),
  ATTR21ID                  varchar2(60),
  ATTR22ID                  NUMBER default 13 references ATTR22_DIM(ATTR22ID),
  ATTR23ID                  varchar2(60),
  ATTR34ID                  NUMBER default 11 references ATTR24_DIM(ATTR24ID),
  ATTR25ID                  NUMBER default 11 references ATTR25_DIM(ATTR25ID),
  ATTR26ID                  NUMBER default 11 references ATTR26_DIM(ATTR26ID),
  ATTR27ID                  NUMBER default 11 references ATTR27_DIM(ATTR27ID),
  ATTR28ID                  NUMBER default 11 references ATTR28_DIM(ATTR28ID),
  ATTR29ID                  NUMBER default 11 references ATTR29_DIM(ATTR29ID),
  ATTR30ID                  NUMBER default 11 references ATTR30_DIM(ATTR30ID),
  ATTR31ID                  NUMBER default 11 references ATTR31_DIM(ATTR31ID),
  ATTR32ID                  NUMBER default 11 references ATTR32_DIM(ATTR32ID),
  ATTR33ID                  NUMBER default 11 references ATTR33_DIM(ATTR33ID),
  ATTR35ID                  NUMBER default 2 references ATTR35_DIM(ATTR35ID),
  ATTR36ID                  NUMBER default 2 references ATTR36_DIM(ATTR36ID),
  ATTR37ID                  NUMBER default 2 references ATTR37_DIM(ATTR37ID),
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
  FORMID                    NUMBER not null,
  CONSTRAINT		    XPKLLCOMPOSITE_FACT primary key (FACTID),
  CONSTRAINT		    FK_LL_CF_FORMID foreign key (FORMID) references FORM_DIM (FORMID),
  CONSTRAINT		    FK_LL_CF_LEVELID foreign key (LEVELID) references LEVEL_DIM (LEVELID),
  CONSTRAINT		    LL_COM_FACT_OTAADJC_FK1 foreign key (ATTR15ID) references ATTR15_DIM (ATTR15ID),
  CONSTRAINT		    LL_COM_FACT_OTAADJFS_FK1 foreign key (ATTR16ID) references ATTR16_DIM (ATTR16ID),
  CONSTRAINT		    LL_COM_FACT_OTAAP_FK1 foreign key (ATTR13ID) references ATTR13_DIM (ATTR13ID),
  CONSTRAINT		    LL_COM_FACT_OTACALC_FK1 foreign key (ATTR12ID) references ATTR12_DIM (ATTR12ID),
  CONSTRAINT		    LL_COM_FACT_OTASR_FK1 foreign key (ATTR11ID) references ATTR11_DIM (ATTR11ID),
  CONSTRAINT		    LL_CO_FACT_COMPOSITE_DIM_FK foreign key (COMPOSITEID) references COMPOSITE_DIM (COMPOSITEID),
  CONSTRAINT		    LL_CO_FACT_CURR_RES_DIM foreign key (CURRENT_RESULTID) references CURRENT_RESULT_DIM (CURRENT_RESULTID),
  CONSTRAINT		    LL_CO_FACT_ORG_NODE_DIM_FK foreign key (ORG_NODEID) references ORG_NODE_DIM (ORG_NODEID),
  CONSTRAINT		    LL_CO_FACT_SESSION_DIM_FK8 foreign key (SESSIONID) references SESSION_DIM (SESSIONID),
  CONSTRAINT		    LL_FK_COMPOSITE_FACT_STUDENTID foreign key (STUDENTID) references STUDENT_DIM (STUDENTID),
  CONSTRAINT		    LL_R6_16 foreign key (ATTR2ID) references ATTR2_DIM (ATTR2ID) on delete set null,
  CONSTRAINT		    LL_R6_39 foreign key (GRADEID) references GRADE_DIM (GRADEID) on delete set null
)
/

-- Create table LASLINK_CONTENT_AREA_FACT
create table LASLINK_CONTENT_AREA_FACT
(
  FACTID                    NUMBER not null,
  CONTENT_AREAID            NUMBER not null,
  STUDENTID                 NUMBER not null,
  FORMID                    NUMBER not null,
  SESSIONID                 NUMBER not null,
  ATTR2ID                   NUMBER default 34,
  ATTR9ID                   NUMBER default 6,
  ATTR11ID                  NUMBER default 6,
  ATTR12ID                  NUMBER default 6,
  ATTR13ID                  NUMBER default 6,
  ATTR14ID                  NUMBER default 6,
  ATTR15ID                  NUMBER default 6,
  ATTR16ID                  NUMBER default 6,
  ATTR17ID                  NUMBER default 101 references ATTR17_DIM(ATTR17ID),
  ATTR18ID                  NUMBER default 14 references ATTR18_DIM(ATTR18ID),
  ATTR19ID                  NUMBER default 122 references ATTR19_DIM(ATTR19ID),
  ATTR20ID                  varchar2(60),
  ATTR21ID                  varchar2(60),
  ATTR22ID                  NUMBER default 13 references ATTR22_DIM(ATTR22ID),
  ATTR23ID                  varchar2(60),
  ATTR34ID                  NUMBER default 11 references ATTR24_DIM(ATTR24ID),
  ATTR25ID                  NUMBER default 11 references ATTR25_DIM(ATTR25ID),
  ATTR26ID                  NUMBER default 11 references ATTR26_DIM(ATTR26ID),
  ATTR27ID                  NUMBER default 11 references ATTR27_DIM(ATTR27ID),
  ATTR28ID                  NUMBER default 11 references ATTR28_DIM(ATTR28ID),
  ATTR29ID                  NUMBER default 11 references ATTR29_DIM(ATTR29ID),
  ATTR30ID                  NUMBER default 11 references ATTR30_DIM(ATTR30ID),
  ATTR31ID                  NUMBER default 11 references ATTR31_DIM(ATTR31ID),
  ATTR32ID                  NUMBER default 11 references ATTR32_DIM(ATTR32ID),
  ATTR33ID                  NUMBER default 11 references ATTR33_DIM(ATTR33ID),
  ATTR35ID                  NUMBER default 2 references ATTR35_DIM(ATTR35ID),
  ATTR36ID                  NUMBER default 2 references ATTR36_DIM(ATTR36ID),
  ATTR37ID                  NUMBER default 2 references ATTR37_DIM(ATTR37ID),
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
  TEST_START_TIMESTAMP      DATE,
  CONSTRAINT 		    XPKLL_CONTENT_AREA_FACT primary key (FACTID),
  CONSTRAINT 		    LL_CA_FACT_CONTENT_AREA_DIM_FK foreign key (CONTENT_AREAID) references CONTENT_AREA_DIM (CONTENT_AREAID),
  CONSTRAINT 		    LL_CA_FACT_CURR_RES_DIM foreign key (CURRENT_RESULTID) references CURRENT_RESULT_DIM (CURRENT_RESULTID),
  CONSTRAINT 		    LL_CA_FACT_FORM_DIM_FK7 foreign key (FORMID) references FORM_DIM (FORMID),
  CONSTRAINT 		    LL_CA_FACT_LEVEL_DIM_FK7 foreign key (LEVELID) references LEVEL_DIM (LEVELID),
  CONSTRAINT 		    LL_CA_FACT_ORG_NODE_DIM_FK foreign key (ORG_NODEID) references ORG_NODE_DIM (ORG_NODEID),
  CONSTRAINT 		    LL_CA_FACT_OTAADJC_FK2 foreign key (ATTR15ID) references ATTR15_DIM (ATTR15ID),
  CONSTRAINT 		    LL_CA_FACT_OTAADJFS_FK2 foreign key (ATTR16ID) references ATTR16_DIM (ATTR16ID),
  CONSTRAINT 		    LL_CA_FACT_OTAAL_FK2 foreign key (ATTR13ID) references ATTR13_DIM (ATTR13ID),
  CONSTRAINT 		    LL_CA_FACT_OTACALCULATOR_FK2 foreign key (ATTR12ID) references ATTR12_DIM (ATTR12ID),
  CONSTRAINT 		    LL_CA_FACT_OTASR_FK2 foreign key (ATTR11ID) references ATTR11_DIM (ATTR11ID),
  CONSTRAINT 		    LL_CA_FACT_OTAUT_FK2 foreign key (ATTR14ID) references ATTR14_DIM (ATTR14ID),
  CONSTRAINT 		    LL_CA_FACT_SESSION_DIM_FK8 foreign key (SESSIONID) references SESSION_DIM (SESSIONID),
  CONSTRAINT 		    LL_FK_CA_FACT_STUDENTID foreign key (STUDENTID) references STUDENT_DIM (STUDENTID),
  CONSTRAINT 		    LL_R5_16 foreign key (ATTR2ID) references ATTR2_DIM (ATTR2ID) on delete set null,
  CONSTRAINT 		    LL_R5_39 foreign key (GRADEID) references GRADE_DIM (GRADEID) on delete set null
)
/

-- Create table LASLINK_ITEM_FACT
create table LASLINK_ITEM_FACT
(
  FACTID                    NUMBER not null,
  STUDENTID                 NUMBER not null,
  FORMID                    NUMBER not null,
  SESSIONID                 NUMBER not null,
  ATTR2ID                   NUMBER default 34,
  ATTR9ID                   NUMBER default 6,
  ATTR11ID                  NUMBER default 6,
  ATTR12ID                  NUMBER default 6,
  ATTR13ID                  NUMBER default 6,
  ATTR14ID                  NUMBER default 6,
  ATTR15ID                  NUMBER default 6,
  ATTR16ID                  NUMBER default 6,
  ATTR17ID                  NUMBER default 101 references ATTR17_DIM(ATTR17ID),
  ATTR18ID                  NUMBER default 14 references ATTR18_DIM(ATTR18ID),
  ATTR19ID                  NUMBER default 122 references ATTR19_DIM(ATTR19ID),
  ATTR20ID                  varchar2(60),
  ATTR21ID                  varchar2(60),
  ATTR22ID                  NUMBER default 13 references ATTR22_DIM(ATTR22ID),
  ATTR23ID                  varchar2(60),
  ATTR34ID                  NUMBER default 11 references ATTR24_DIM(ATTR24ID),
  ATTR25ID                  NUMBER default 11 references ATTR25_DIM(ATTR25ID),
  ATTR26ID                  NUMBER default 11 references ATTR26_DIM(ATTR26ID),
  ATTR27ID                  NUMBER default 11 references ATTR27_DIM(ATTR27ID),
  ATTR28ID                  NUMBER default 11 references ATTR28_DIM(ATTR28ID),
  ATTR29ID                  NUMBER default 11 references ATTR29_DIM(ATTR29ID),
  ATTR30ID                  NUMBER default 11 references ATTR30_DIM(ATTR30ID),
  ATTR31ID                  NUMBER default 11 references ATTR31_DIM(ATTR31ID),
  ATTR32ID                  NUMBER default 11 references ATTR32_DIM(ATTR32ID),
  ATTR33ID                  NUMBER default 11 references ATTR33_DIM(ATTR33ID),
  ATTR35ID                  NUMBER default 2 references ATTR35_DIM(ATTR35ID),
  ATTR36ID                  NUMBER default 2 references ATTR36_DIM(ATTR36ID),
  ATTR37ID                  NUMBER default 2 references ATTR37_DIM(ATTR37ID),
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
  POINTS_POSSIBLE           NUMBER,
  CONSTRAINT 		    XPKLLITEM_FACT2 primary key (FACTID),
  CONSTRAINT 		    LLITEM_FACT2_ETHNICITY_DIM_FK7 foreign key (ATTR2ID) references ATTR2_DIM (ATTR2ID) on delete set null,
  CONSTRAINT 		    LL_FK_ITEM_FACT2_STUDENTID foreign key (STUDENTID) references STUDENT_DIM (STUDENTID),
  CONSTRAINT 		    LL_ITEM_FACT2_FORM_DIM_FK7 foreign key (FORMID) references FORM_DIM (FORMID),
  CONSTRAINT 		    LL_ITEM_FACT2_GRADE_DIM_FK7 foreign key (GRADEID) references GRADE_DIM (GRADEID) on delete set null,
  CONSTRAINT 		    LL_ITEM_FACT2_ITEM_DIM_FK foreign key (ITEMID) references ITEM_DIM (ITEMID),
  CONSTRAINT 		    LL_ITEM_FACT2_LEVEL_DIM_FK7 foreign key (LEVELID) references LEVEL_DIM (LEVELID),
  CONSTRAINT 		    LL_ITEM_FACT2_ORG_NODE_DIM_FK foreign key (ORG_NODEID) references ORG_NODE_DIM (ORG_NODEID),
  CONSTRAINT 		    LL_ITEM_FACT2_OTAADJC_DIM_FK7 foreign key (ATTR15ID) references ATTR15_DIM (ATTR15ID),
  CONSTRAINT 		    LL_ITEM_FACT2_OTAADJFS_DIM_FK7 foreign key (ATTR16ID) references ATTR16_DIM (ATTR16ID),
  CONSTRAINT 		    LL_ITEM_FACT2_OTAAP_DIM_FK7 foreign key (ATTR13ID) references ATTR13_DIM (ATTR13ID),
  CONSTRAINT 		    LL_ITEM_FACT2_OTACAL_DIM_FK7 foreign key (ATTR12ID) references ATTR12_DIM (ATTR12ID),
  CONSTRAINT 		    LL_ITEM_FACT2_OTASM_DIM_FK7 foreign key (ATTR9ID) references ATTR9_DIM (ATTR9ID) on delete set null,
  CONSTRAINT 		    LL_ITEM_FACT2_OTASR_DIM_FK7 foreign key (ATTR11ID) references ATTR11_DIM (ATTR11ID),
  CONSTRAINT 		    LL_ITEM_FACT2_OTAUT_DIM_FK7 foreign key (ATTR14ID) references ATTR14_DIM (ATTR14ID),
  CONSTRAINT 		    LL_ITEM_FACT2_RESPONSE_DIM_FK foreign key (RESPONSEID) references RESPONSE_DIM (RESPONSEID),
  CONSTRAINT 		    LL_ITEM_FACT2_SESSION_DIM_FK8 foreign key (SESSIONID) references SESSION_DIM (SESSIONID),
  CONSTRAINT 		    LL_ITEM_FACT_CURR_RES_DIM foreign key (CURRENT_RESULTID) references CURRENT_RESULT_DIM (CURRENT_RESULTID)
)
/

-- Create table LASLINK_PRIM_OBJ_FACT
create table LASLINK_PRIM_OBJ_FACT
(
  FACTID                    NUMBER not null,
  PRIM_OBJID                NUMBER not null,
  STUDENTID                 NUMBER not null,
  FORMID                    NUMBER not null,
  SESSIONID                 NUMBER not null,
  ATTR2ID                   NUMBER default 34,
  ATTR9ID                   NUMBER default 6,
  ATTR11ID                  NUMBER default 6,
  ATTR12ID                  NUMBER default 6,
  ATTR13ID                  NUMBER default 6,
  ATTR14ID                  NUMBER default 6,
  ATTR15ID                  NUMBER default 6,
  ATTR16ID                  NUMBER default 6,
  ATTR17ID                  NUMBER default 101 references ATTR17_DIM(ATTR17ID),
  ATTR18ID                  NUMBER default 14 references ATTR18_DIM(ATTR18ID),
  ATTR19ID                  NUMBER default 122 references ATTR19_DIM(ATTR19ID),
  ATTR20ID                  varchar2(60),
  ATTR21ID                  varchar2(60),
  ATTR22ID                  NUMBER default 13 references ATTR22_DIM(ATTR22ID),
  ATTR23ID                  varchar2(60),
  ATTR34ID                  NUMBER default 11 references ATTR24_DIM(ATTR24ID),
  ATTR25ID                  NUMBER default 11 references ATTR25_DIM(ATTR25ID),
  ATTR26ID                  NUMBER default 11 references ATTR26_DIM(ATTR26ID),
  ATTR27ID                  NUMBER default 11 references ATTR27_DIM(ATTR27ID),
  ATTR28ID                  NUMBER default 11 references ATTR28_DIM(ATTR28ID),
  ATTR29ID                  NUMBER default 11 references ATTR29_DIM(ATTR29ID),
  ATTR30ID                  NUMBER default 11 references ATTR30_DIM(ATTR30ID),
  ATTR31ID                  NUMBER default 11 references ATTR31_DIM(ATTR31ID),
  ATTR32ID                  NUMBER default 11 references ATTR32_DIM(ATTR32ID),
  ATTR33ID                  NUMBER default 11 references ATTR33_DIM(ATTR33ID),
  ATTR35ID                  NUMBER default 2 references ATTR35_DIM(ATTR35ID),
  ATTR36ID                  NUMBER default 2 references ATTR36_DIM(ATTR36ID),
  ATTR37ID                  NUMBER default 2 references ATTR37_DIM(ATTR37ID),
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
  POINTS_POSSIBLE           NUMBER,
  CONSTRAINT		    XPKLLPRIM_OBJ_FACT primary key (FACTID),
  CONSTRAINT		    LL_FK_PO_FACT_MAST_LEVELID foreign key (MASTERY_LEVELID) references MASTERY_LEVEL_DIM (MASTERY_LEVELID),
  CONSTRAINT		    LL_FK_PRIM_OBJ_FACT_STUDENTID foreign key (STUDENTID) references STUDENT_DIM (STUDENTID),
  CONSTRAINT		    LL_PO_FACT_CURR_RES_DIM foreign key (CURRENT_RESULTID) references CURRENT_RESULT_DIM (CURRENT_RESULTID),
  CONSTRAINT		    LL_PO_FACT_FORM_DIM_FK7 foreign key (FORMID) references FORM_DIM (FORMID),
  CONSTRAINT		    LL_PO_FACT_ORG_NODE_DIM_FK foreign key (ORG_NODEID) references ORG_NODE_DIM (ORG_NODEID),
  CONSTRAINT		    LL_PO_FACT_OTAADJC_FK1 foreign key (ATTR15ID) references ATTR15_DIM (ATTR15ID),
  CONSTRAINT		    LL_PO_FACT_OTAADJFS_FK1 foreign key (ATTR16ID) references ATTR16_DIM (ATTR16ID),
  CONSTRAINT		    LL_PO_FACT_OTAAP_FK1 foreign key (ATTR13ID) references ATTR13_DIM (ATTR13ID),
  CONSTRAINT		    LL_PO_FACT_OTACALCULATOR_FK1 foreign key (ATTR12ID) references ATTR12_DIM (ATTR12ID),
  CONSTRAINT		    LL_PO_FACT_OTASR_FK1 foreign key (ATTR11ID) references ATTR11_DIM (ATTR11ID),
  CONSTRAINT		    LL_PO_FACT_OTAUT_FK1 foreign key (ATTR14ID) references ATTR14_DIM (ATTR14ID),
  CONSTRAINT		    LL_PO_FACT_PRIM_OBJ_DIM_FK foreign key (PRIM_OBJID) references PRIM_OBJ_DIM (PRIM_OBJID),
  CONSTRAINT		    LL_PO_FACT_SESSION_DIM_FK8 foreign key (SESSIONID) references SESSION_DIM (SESSIONID),
  CONSTRAINT		    LL_PRIM_OBJ_FACT_LEVEL_DIM_FK7 foreign key (LEVELID) references LEVEL_DIM (LEVELID),
  CONSTRAINT		    LL_R4_16 foreign key (ATTR2ID) references ATTR2_DIM (ATTR2ID) on delete set null,
  CONSTRAINT		    LL_R4_39 foreign key (GRADEID) references GRADE_DIM (GRADEID) on delete set null
)
/

-- Create table LASLINK_SEC_OBJ_FACT
create table LASLINK_SEC_OBJ_FACT
(
  FACTID                    NUMBER not null,
  SEC_OBJID                 NUMBER not null,
  STUDENTID                 NUMBER not null,
  FORMID                    NUMBER not null,
  SESSIONID                 NUMBER not null,
  ATTR2ID                   NUMBER default 34,
  ATTR9ID                   NUMBER default 6,
  ATTR11ID                  NUMBER default 6,
  ATTR12ID                  NUMBER default 6,
  ATTR13ID                  NUMBER default 6,
  ATTR14ID                  NUMBER default 6,
  ATTR15ID                  NUMBER default 6,
  ATTR16ID                  NUMBER default 6,
  ATTR17ID                  NUMBER default 101 references ATTR17_DIM(ATTR17ID),
  ATTR18ID                  NUMBER default 14 references ATTR18_DIM(ATTR18ID),
  ATTR19ID                  NUMBER default 122 references ATTR19_DIM(ATTR19ID),
  ATTR20ID                  varchar2(60),
  ATTR21ID                  varchar2(60),
  ATTR22ID                  NUMBER default 13 references ATTR22_DIM(ATTR22ID),
  ATTR23ID                  varchar2(60),
  ATTR34ID                  NUMBER default 11 references ATTR24_DIM(ATTR24ID),
  ATTR25ID                  NUMBER default 11 references ATTR25_DIM(ATTR25ID),
  ATTR26ID                  NUMBER default 11 references ATTR26_DIM(ATTR26ID),
  ATTR27ID                  NUMBER default 11 references ATTR27_DIM(ATTR27ID),
  ATTR28ID                  NUMBER default 11 references ATTR28_DIM(ATTR28ID),
  ATTR29ID                  NUMBER default 11 references ATTR29_DIM(ATTR29ID),
  ATTR30ID                  NUMBER default 11 references ATTR30_DIM(ATTR30ID),
  ATTR31ID                  NUMBER default 11 references ATTR31_DIM(ATTR31ID),
  ATTR32ID                  NUMBER default 11 references ATTR32_DIM(ATTR32ID),
  ATTR33ID                  NUMBER default 11 references ATTR33_DIM(ATTR33ID),
  ATTR35ID                  NUMBER default 2 references ATTR35_DIM(ATTR35ID),
  ATTR36ID                  NUMBER default 2 references ATTR36_DIM(ATTR36ID),
  ATTR37ID                  NUMBER default 2 references ATTR37_DIM(ATTR37ID),
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
  POINTS_POSSIBLE           NUMBER,
  CONSTRAINT		    XPKLLSEC_OBJ_FACT primary key (FACTID),
  CONSTRAINT		    LL_FK_SO_FACT_MAST_LEVELID foreign key (MASTERY_LEVELID) references MASTERY_LEVEL_DIM (MASTERY_LEVELID),
  CONSTRAINT		    LL_FK_SO_FACT_STUDENTID foreign key (STUDENTID) references STUDENT_DIM (STUDENTID),
  CONSTRAINT		    LL_R3_16 foreign key (ATTR2ID) references ATTR2_DIM (ATTR2ID) on delete set null,
  CONSTRAINT		    LL_R3_39 foreign key (GRADEID) references GRADE_DIM (GRADEID) on delete set null,
  CONSTRAINT		    LL_SO_FACT_CURR_RES_DIM foreign key (CURRENT_RESULTID) references CURRENT_RESULT_DIM (CURRENT_RESULTID),
  CONSTRAINT		    LL_SO_FACT_FORM_DIM_FK7 foreign key (FORMID) references FORM_DIM (FORMID),
  CONSTRAINT		    LL_SO_FACT_LEVEL_DIM_FK7 foreign key (LEVELID) references LEVEL_DIM (LEVELID),
  CONSTRAINT		    LL_SO_FACT_ORG_NODE_DIM_FK foreign key (ORG_NODEID) references ORG_NODE_DIM (ORG_NODEID),
  CONSTRAINT		    LL_SO_FACT_OTAADJC_FK1 foreign key (ATTR15ID) references ATTR15_DIM (ATTR15ID),
  CONSTRAINT		    LL_SO_FACT_OTAADJFS_FK1 foreign key (ATTR16ID) references ATTR16_DIM (ATTR16ID),
  CONSTRAINT		    LL_SO_FACT_OTAAP_FK1 foreign key (ATTR13ID) references ATTR13_DIM (ATTR13ID),
  CONSTRAINT		    LL_SO_FACT_OTACALCULATOR_FK1 foreign key (ATTR12ID) references ATTR12_DIM (ATTR12ID),
  CONSTRAINT		    LL_SO_FACT_OTASR_FK1 foreign key (ATTR11ID) references ATTR11_DIM (ATTR11ID),
  CONSTRAINT		    LL_SO_FACT_OTAUT_FK1 foreign key (ATTR14ID) references ATTR14_DIM (ATTR14ID),
  CONSTRAINT		    LL_SO_FACT_SEC_OBJ_DIM_FK foreign key (SEC_OBJID) references SEC_OBJ_DIM (SEC_OBJID),
  CONSTRAINT		    LL_SO_FACT_SESSION_DIM_FK8 foreign key (SESSIONID) references SESSION_DIM (SESSIONID)
)
/

--CREATE TRIGGER TRIG_LL_SEQ_COMPOSITE_FACT
CREATE OR REPLACE TRIGGER TRIG_LL_SEQ_COMPOSITE_FACT BEFORE INSERT ON LASLINK_COMPOSITE_FACT
FOR EACH ROW
BEGIN
  SELECT SEQ_LL_COMPOSITE_FACT_ID.NEXTVAL INTO :NEW.FACTID FROM dual;
END;
/

--CREATE TRIGGER TRIG_LL_SEQ_CONTENT_AREA_FACT
CREATE OR REPLACE TRIGGER TRIG_LL_SEQ_CONTENT_AREA_FACT BEFORE INSERT ON LASLINK_CONTENT_AREA_FACT
FOR EACH ROW
BEGIN
  SELECT SEQ_LL_CONTENT_AREA_FACT_ID.NEXTVAL INTO :NEW.FACTID FROM dual;
END;
/

--CREATE TRIGGER TRIG_LL_SEQ_ITEM_FACT
CREATE OR REPLACE TRIGGER TRIG_LL_SEQ_ITEM_FACT BEFORE INSERT ON LASLINK_ITEM_FACT
FOR EACH ROW
BEGIN
  SELECT SEQ_LL_ITEM_FACT_ID.NEXTVAL INTO :NEW.FACTID FROM dual;
END;
/

--CREATE TRIGGER TRIG_LL_SEQ_PRIM_OBJ_FACT
CREATE OR REPLACE TRIGGER TRIG_LL_SEQ_PRIM_OBJ_FACT BEFORE INSERT ON LASLINK_PRIM_OBJ_FACT
FOR EACH ROW
BEGIN
  SELECT SEQ_LL_PRIM_OBJ_FACT_ID.NEXTVAL INTO :NEW.FACTID FROM dual;
END;
/

--CREATE TRIGGER TRIG_LL_SEQ_SEC_OBJ_FACT
CREATE OR REPLACE TRIGGER TRIG_LL_SEQ_SEC_OBJ_FACT BEFORE INSERT ON LASLINK_SEC_OBJ_FACT
FOR EACH ROW
BEGIN
  SELECT SEQ_LL_SEC_OBJ_FACT_ID.NEXTVAL INTO :NEW.FACTID FROM dual;
END;
/