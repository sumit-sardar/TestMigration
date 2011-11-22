/***
* New column added in item_set table for forward only
*/

ALTER TABLE item_set ADD FORWARD_ONLY VARCHAR2(2) DEFAULT 'F' NOT NULL
/

--Table for objectives of tabe cat
create table tabe_cat_objective (objective_id integer primary key, objective_name varchar2(200), content_area_id integer not null references item_set(item_set_id), items integer)
/