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
