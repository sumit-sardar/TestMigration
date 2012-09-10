--Adding sem(standard error of measurement) column
ALTER TABLE tv_content_area_fact ADD SEM_SCORE NUMBER
/
-- Create table
create table IRT_SCORE_LOOKUP_FILES
(
  CONTENT_AREA VARCHAR2(30),
  TEST_LEVEL   VARCHAR2(5),
  PRODUCT_TYPE VARCHAR2(5),
  PRODUCT_ID   VARCHAR2(5),
  FILE_NAME    VARCHAR2(10) not null
)
/
alter table ITEM_P_VALUE
  drop constraint PK_ITEM_P_VALUE
/
ALTER TABLE item_p_value ADD product_id VARCHAR2(10) default '0'
/
alter table ITEM_P_VALUE
  add constraint PK_ITEM_P_VALUE primary key (TEST_FORM, TEST_LEVEL, GRADE, ITEM_DISPLAY_NAME, NORM_GROUP, PRODUCT_ID)
/