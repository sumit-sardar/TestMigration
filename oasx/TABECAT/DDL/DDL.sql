/***
* New column added in item_set table for forward only
*/

ALTER TABLE item_set ADD FORWARD_ONLY VARCHAR2(2) DEFAULT 'F' NOT NULL
/