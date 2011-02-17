CREATE OR REPLACE procedure delete_item_set_ancestors (
 item_set_id_ integer)
as

  hasChildren integer := 0;
  current_item_set_id item_set_parent.item_set_id%type;
  type temp is ref cursor;
  type temp1 is ref cursor;
  temp_var temp;
  child_var temp1;

begin

select count(*) into hasChildren
from item_set_ancestor
where ancestor_item_set_id = item_set_id_
and degrees > 0;

if (hasChildren = 0) then
  -- is a leaf, therefore go ahead and delete
  delete from item_set_ancestor
  where item_set_id = item_set_id_;

else
  -- is not a leaf
  open temp_var for
  select item_set_id
  from item_set_ancestor
  where ancestor_item_set_id = item_set_id_
  and degrees > 0;

  fetch temp_var into current_item_set_id;
  while temp_var%found loop
 delete_item_set_ancestors( current_item_set_id );
    fetch temp_var into current_item_set_id;
  end loop;

  close temp_var;
  delete from item_set_ancestor
  where item_set_id = item_set_id_;

end if;

end;
/

