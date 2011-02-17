CREATE OR REPLACE procedure insert_item_set_ancestors (
 parent_item_set_id_ integer,
 item_set_id_ integer,
 item_set_type_ varchar2,
 parent_item_set_type varchar2,
 created_by_ integer,
 item_set_sort_order_ integer)
as

type parent_ancestors is ref cursor;
v_parent parent_ancestors;
v_child parent_ancestors;
v_new parent_ancestors;

parent_row item_set_ancestor%rowtype;
child_row item_set_ancestor%rowtype;
new_row item_set_ancestor%rowtype;
parent_cur item_set_ancestor.ancestor_item_set_id%type;
counter integer := 0;
already_exists integer := 0;

begin

  -- does parent item set already exist?
   select count(*) into counter
  from item_set_ancestor
  where item_set_id = parent_item_set_id_;
  -- if it doesn't exist, add row for parent with 0 degrees
  if ( counter = 0 ) then
     insert into item_set_ancestor (ancestor_item_set_id, item_set_id, item_set_type, ancestor_item_set_type, degrees, created_by)
       values (parent_item_set_id_, parent_item_set_id_, parent_item_set_type, parent_item_set_type, 0, created_by_);
  end if;

  -- does child item set already exist?
     select count(*) into already_exists
	 from item_set_ancestor
	 where ancestor_item_set_id = item_set_id_
	 and item_set_id = item_set_id_
	 and degrees = 0;
  -- if it doesn't exist, add row for child with 0 degrees
     if ( already_exists = 0 )
	 then
  		insert into item_set_ancestor (ancestor_item_set_id, item_set_id, item_set_type, ancestor_item_set_type, degrees, created_by)
    	values (item_set_id_, item_set_id_, item_set_type_, item_set_type_, 0, created_by_);
	 end if;

 -- add parent's ancestors as ancestors to new node.
 -- ancestor_item_set_id, item_set_id, item_set_type, created_by, created_date_time, updated_by, updated_date_time, item_set_sort_order, degrees
   open v_child for
  select *
  from item_set_ancestor
  where item_set_id = parent_item_set_id_;

  fetch v_child into child_row;
  while v_child%found loop

     -- no additions if it already exists
     select count(*) into already_exists
	 from item_set_ancestor
	 where ancestor_item_set_id = child_row.ancestor_item_set_id
	 and item_set_id = item_set_id_
	 and degrees = (child_row.degrees + 1);

     if ( already_exists = 0 )
	 then
	 	 if ( child_row.degrees = 0 )
		 then
  	 	 	insert into item_set_ancestor (ancestor_item_set_id, item_set_id, item_set_type, ancestor_item_set_type, created_by, created_date_time, updated_by, updated_date_time, item_set_sort_order, degrees)
    	 	values (child_row.ancestor_item_set_id, item_set_id_, item_set_type_, child_row.ancestor_item_set_type, created_by_, child_row.created_date_time, child_row.updated_by, child_row.updated_date_time, item_set_sort_order_, (child_row.degrees + 1 ));
		 else
  	 	 	insert into item_set_ancestor (ancestor_item_set_id, item_set_id, item_set_type, ancestor_item_set_type, created_by, created_date_time, updated_by, updated_date_time, item_set_sort_order, degrees)
    	 	values (child_row.ancestor_item_set_id, item_set_id_, item_set_type_, child_row.ancestor_item_set_type, created_by_, child_row.created_date_time, child_row.updated_by, child_row.updated_date_time, child_row.item_set_sort_order, (child_row.degrees + 1 ));
		 end if;
	 end if;
  fetch v_child into child_row;

  end loop;
  close v_child;


  -- add parent's ancestors as ancestors to child's children.
   open v_child for
  select *
  from item_set_ancestor
  where ancestor_item_set_id = item_set_id_
  and degrees > 0;

  fetch v_child into child_row;
  while v_child%found loop

     -- no additions if it already exists
     select count(*) into already_exists
	 from item_set_ancestor
	 where ancestor_item_set_id = parent_item_set_id_
	 and item_set_id = child_row.item_set_id
	 and degrees = (child_row.degrees + 1);

     if ( already_exists = 0 )
	 then
	 	 if ( child_row.degrees = 0 )
		 then
  	 	 	insert into item_set_ancestor (ancestor_item_set_id, item_set_id, item_set_type, ancestor_item_set_type, created_by, created_date_time, updated_by, updated_date_time, item_set_sort_order, degrees)
    	 	values (parent_item_set_id_, child_row.item_set_id, child_row.item_set_type, parent_item_set_type, created_by_, child_row.created_date_time, child_row.updated_by, child_row.updated_date_time, item_set_sort_order_, (child_row.degrees + 1 ));
		 else
  	 	 	insert into item_set_ancestor (ancestor_item_set_id, item_set_id, item_set_type, ancestor_item_set_type, created_by, created_date_time, updated_by, updated_date_time, item_set_sort_order, degrees)
    	 	values (parent_item_set_id_, child_row.item_set_id, child_row.item_set_type, parent_item_set_type, created_by_, child_row.created_date_time, child_row.updated_by, child_row.updated_date_time, child_row.item_set_sort_order, (child_row.degrees + 1 ));
		 end if;
	 end if;
  fetch v_child into child_row;

  end loop;
  close v_child;




end;
/

