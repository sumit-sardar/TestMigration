/* New Columns were added in student_item_set_status table */
/* Two new columns exemptions and absent were added  in table student_item_set_status */

ALTER TABLE student_item_set_status ADD exemptions VARCHAR2(1)
/
ALTER TABLE student_item_set_status ADD absent VARCHAR2(1)
/



/* New Column was added in org_node table  */
/*One new column org_node_mdr_number was added */

ALTER TABLE org_node ADD org_node_mdr_number VARCHAR2(32)
/


/* New Table was created for keeping the list of music files in database */

CREATE TABLE Music_File_List(
file_id INTEGER PRIMARY KEY,
audio_file_name VARCHAR2(100),
audio_file BLOB 
);
/



/* Two new columns music_file_id and masking_ruler were added in table student_accomodation */

ALTER TABLE student_accomodation ADD music_file_id VARCHAR2(200)
/
ALTER TABLE student_accomodation ADD masking_ruler VARCHAR2(2)
/


/* coloum 'student exported 'added for process scores  */

ALTER TABLE test_roster ADD student_exported VARCHAR2(1)
/

/* New Table was created for keeping the Decrypted Item Xml in ADS Database for 
Item Player */
create table AA_ITEM_DECRYPTED
(
  AA_ITEM_ID         VARCHAR2(32) not null,
  ITEM_RENDITION_XML BLOB,
  CREATED_DATE_TIME  DATE default sysdate not null
)
/

alter table student_accommodation add magnifying_glass varchar2(2)
/

/* New cloumn added for storing the test purpose of student*/

ALTER TABLE STUDENT ADD TEST_PURPOSE VARCHAR2(4)
/
/* Primary Key of TEM_RESPONSE_POINTS is altered by adding new column DATAPOINT_ID, to support multiple objective of an item */

DECLARE
  V_CONSTRAINT_NAME VARCHAR2(100);
BEGIN
  SELECT DISTINCT CONSTRAINT_NAME
    INTO V_CONSTRAINT_NAME
    FROM ALL_CONSTRAINTS
   WHERE UPPER(TABLE_NAME) = UPPER('ITEM_RESPONSE_POINTS')
     AND CONSTRAINT_TYPE = 'P';
  EXECUTE IMMEDIATE 'ALTER TABLE ITEM_RESPONSE_POINTS DROP CONSTRAINT ' ||
                    V_CONSTRAINT_NAME;
  EXECUTE IMMEDIATE 'ALTER TABLE ITEM_RESPONSE_POINTS ADD CONSTRAINT PK_ITEM_RESPONSE_POINTS  PRIMARY KEY(ITEM_RESPONSE_ID, ITEM_RESPONSE_POINTS_SEQ_NUM, DATAPOINT_ID)';
END;
/


/*Creating table ITEM_RUBRIC_DATA and ITEM_RUBRIC_EXEMPLARS for insrting rubric datas for handScoring*/

create table ITEM_RUBRIC_DATA
(
  ITEM_ID            VARCHAR2(32) not null,
  SCORE              INTEGER not null,
  RUBRIC_DESCRIPTION VARCHAR2(1000),
  primary key (ITEM_ID, SCORE)
)
/


create table ITEM_RUBRIC_EXEMPLARS
(
  ITEM_ID            VARCHAR2(32) not null,
  SCORE              INTEGER not null,
  SAMPLE_RESPONSE    VARCHAR2(1000),
  RUBRIC_EXPLANATION VARCHAR2(1000)
)
/

-- Create/Recreate primary, unique and foreign key constraints 
alter table ITEM_RUBRIC_EXEMPLARS
  add primary key (ITEM_ID, SCORE)
/

alter table ITEM_RUBRIC_EXEMPLARS
  add foreign key (ITEM_ID, SCORE)
  references ITEM_RUBRIC_DATA (ITEM_ID, SCORE)
/

CREATE BITMAP INDEX IDX_STUDENT_EXPORTED ON TEST_ROSTER(NVL(STUDENT_EXPORTED, 'F'))
/

/*student_accommodation table altered to add new accommodation maskingTool*/
alter table student_accommodation add masking_tool varchar2(2)
/