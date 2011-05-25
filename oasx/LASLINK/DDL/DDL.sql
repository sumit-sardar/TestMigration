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