DROP TABLE CONTENT_TMP CASCADE CONSTRAINTS ; 

CREATE TABLE CONTENT_TMP ( 
  GRADE                          NUMBER(38), 
  ITEM_SET_NAME                  VARCHAR2(512)  NOT NULL, 
  EXT_CMS_ITEM_SET_ID            VARCHAR2(32)  NOT NULL, 
  PARENT_EXT_CMS_ITEM_SET_ID     VARCHAR2(32)  NOT NULL, 
  ITEM_SET_CATEGORY_LEVEL        NUMBER(38)    NOT NULL, 
  INTERNAL_PRODUCT_DISPLAY_NAME  VARCHAR2(128));

-----------------------------------
-- start create SP_FRAMEWORK_UPDATE
-----------------------------------

create or replace procedure SP_FRAMEWORK_IMPORT (framework_product_id_in in number)
is


/* declare variables */
v_count_same_item_set integer;

v_error varchar2(5);

v_delete_item_set_id item_set.item_set_id%type;
v_delete_item_set_parent_id  item_set.item_set_id%type;
v_product_id product.product_id%type;
v_framework_product_id product.product_id%type;


CURSOR c_content_tmp IS
       select *
       from content_tmp order by item_set_category_level asc;
       
CURSOR c_product IS
       select product_id from
       product where parent_product_id = v_framework_product_id;

begin

v_error := 'FALSE';
v_framework_product_id := framework_product_id_in;

/* loop through all rows in the content_tmp table */
for rec_content_tmp in c_content_tmp loop

    if rec_content_tmp.item_set_category_level = 1 then
    
        select count(*) into v_count_same_item_set 
           from item_set where ext_cms_item_set_id = rec_content_tmp.ext_cms_item_set_id and item_set_category_id in
           (select item_set_category_id from item_set_category where framework_product_id = v_framework_product_id);
    
        if v_count_same_item_set <> 0 then
    
           update item_set 
              set grade = rec_content_tmp.grade,
                  ext_cms_item_set_id = rec_content_tmp.ext_cms_item_set_id,
                  item_set_category_id = (select item_set_category_id 
                                         from item_set_category 
                                         where item_set_category.item_set_category_level = rec_content_tmp.item_set_category_level
                                         and item_set_category.framework_product_id = v_framework_product_id)
              where item_set.ext_cms_item_set_id = rec_content_tmp.ext_cms_item_set_id and item_set_category_id in
                (select item_set_category_id from item_set_category where framework_product_id = v_framework_product_id);
                
                
            select item_set_id into v_delete_item_set_id
               from item_set where item_set.EXT_CMS_ITEM_SET_ID = rec_content_tmp.ext_cms_item_set_id
               and item_set.item_set_id > 999 and item_set_category_id in (select item_set_category_id from 
               item_set_category where framework_product_id = v_framework_product_id);

            delete from item_set_product isprfr where isprfr.product_id = v_framework_product_id
               and isprfr.item_set_id = v_delete_item_set_id;
       
        else
    
          insert into item_set (item_set_id,
                              item_set_type,
                              item_set_name,
                              --min_grade,
                              --version,
                              --max_grade,
                              item_set_level,
                              --subject,
                              grade,
                              --sample,
                              --media_path,
                              --time_limit,
                              --break_time,
                              --ext_ems_item_set_id,
                              ext_cms_item_set_id,
                              item_set_display_name,
                              --item_set_description,
                              --item_set_rule_id,
                              created_date_time,
                              created_by,
                              activation_status,
                              item_set_category_id
                              --owner_customer_id,
                              --updated_by,
                              --updated_date_time,
                              --item_set_form,
                              --publish_status,
                              --original_created_by
                              ) values (
                              seq_item_set_id.nextval,
                              'RE',
                              rec_content_tmp.item_set_name,
                              rec_content_tmp.grade,
                              rec_content_tmp.grade,
                              rec_content_tmp.ext_cms_item_set_id,
                              rec_content_tmp.item_set_name,
                              sysdate,
                              2,
                              'AC',
                               (select item_set_category_id 
                                         from item_set_category 
                                         where item_set_category.item_set_category_level = rec_content_tmp.item_set_category_level
                                         and item_set_category.framework_product_id = v_framework_product_id));
        
        --commit;                                
        

    
       end if;

                                  
                                  
       /* insert into item_set_product for framework product, since we delete if it exists */                   
       insert into item_set_product (item_set_id,
                                  product_id,
                                  created_by,
                                  created_date_time
                                  --updated_by,
                                  --updated_date_time
                                  ) values (
                                  (select item_set_id from item_set
                                  where item_set.EXT_CMS_ITEM_SET_ID = rec_content_tmp.ext_cms_item_set_id
                                  and item_set.item_set_id > 999 and item_set_category_id in (select
                                  item_set_category_id from item_set_category where framework_product_id =
                                  v_framework_product_id)),
                                  v_framework_product_id,
                                  2,
                                  sysdate);
    
    end if;
    if rec_content_tmp.item_set_category_level = 2 then

         /* determine if the itemset of the same ext_cms_item_set_id exists
       and if it has the same name */
       select count(*) into v_count_same_item_set 
           from item_set where ext_cms_item_set_id = rec_content_tmp.ext_cms_item_set_id and item_set_category_id in 
           (select item_set_category_id from item_set_category where framework_product_id = v_framework_product_id);

       /* if item set row exists, then update */
       if v_count_same_item_set <> 0 then
    
           update item_set 
              set grade = rec_content_tmp.grade,
                  ext_cms_item_set_id = rec_content_tmp.ext_cms_item_set_id,
                  item_set_category_id = (select item_set_category_id 
                                         from item_set_category 
                                         where item_set_category.item_set_category_level = rec_content_tmp.item_set_category_level
                                         and item_set_category.framework_product_id = v_framework_product_id)
              where item_set.ext_cms_item_set_id = rec_content_tmp.ext_cms_item_set_id and item_set_category_id in
                (select item_set_category_id from item_set_category where framework_product_id = v_framework_product_id);
           --commit;
       

           /* now that the item_set row has been updated, we'll delete */ 
           /*the existing item_set_parent and item_set product rows for it */
           select item_set_id into v_delete_item_set_parent_id
               from item_set iset where iset.EXT_CMS_ITEM_SET_ID = rec_content_tmp.parent_ext_cms_item_set_id
               and iset.item_set_id > 999 and item_set_category_id in (select item_set_category_id from item_set_category 
               where framework_product_id = v_framework_product_id);

           select item_set_id into v_delete_item_set_id
               from item_set where item_set.EXT_CMS_ITEM_SET_ID = rec_content_tmp.ext_cms_item_set_id
               and item_set.item_set_id > 999 and item_set_category_id in (select item_set_category_id from
               item_set_category where framework_product_id = v_framework_product_id);

           --delete from item_set_parent ispa where ispa.PARENT_ITEM_SET_ID = v_delete_item_set_parent_id
           --and item_set_id = v_delete_item_set_id;
       
           delete from item_set_parent ispa where ispa.item_set_id = v_delete_item_set_id;
       
           delete from item_set_product ispr where ispr.item_set_id = v_delete_item_set_id;
       
           delete from item_set_product isprfr where isprfr.product_id = v_framework_product_id
               and isprfr.item_set_id = v_delete_item_set_id;
       
           --commit;
       
            
        
       /* if item_set row does not exist, then insert */
       else
    
          insert into item_set (item_set_id,
                              item_set_type,
                              item_set_name,
                              --min_grade,
                              --version,
                              --max_grade,
                              item_set_level,
                              --subject,
                              grade,
                              --sample,
                              --media_path,
                              --time_limit,
                              --break_time,
                              --ext_ems_item_set_id,
                              ext_cms_item_set_id,
                              item_set_display_name,
                              --item_set_description,
                              --item_set_rule_id,
                              created_date_time,
                              created_by,
                              activation_status,
                              item_set_category_id
                              --owner_customer_id,
                              --updated_by,
                              --updated_date_time,
                              --item_set_form,
                              --publish_status,
                              --original_created_by
                              ) values (
                              seq_item_set_id.nextval,
                              'RE',
                              rec_content_tmp.item_set_name,
                              rec_content_tmp.grade,
                              rec_content_tmp.grade,
                              rec_content_tmp.ext_cms_item_set_id,
                              rec_content_tmp.item_set_name,
                              sysdate,
                              2,
                              'AC',
                               (select item_set_category_id 
                                         from item_set_category 
                                         where item_set_category.item_set_category_level = rec_content_tmp.item_set_category_level
                                         and item_set_category.framework_product_id = v_framework_product_id));
        
          --commit;                              
        

    
       end if;

    
    
       /* always insert into item_set_parent, since we delete if it exists */
       insert into item_set_parent (parent_item_set_id,
                                 created_date_time,
                                 item_set_type,
                                 item_set_id,
                                 created_by,
                                 --updated_by,
                                 --updated_date_time,
                                 --item_set_sort_order,
                                 parent_item_set_type
                                 ) values (
                                 (select item_set_id from item_set
                                  where item_set.EXT_CMS_ITEM_SET_ID = rec_content_tmp.parent_ext_cms_item_set_id
                                  and item_set.item_set_id > 999 and item_set_category_id in (select
                                  item_set_category_id from item_set_category where framework_product_id =
                                  v_framework_product_id)),
                                 sysdate,
                                 'RE',
                                 (select item_set_id from item_set
                                  where item_set.EXT_CMS_ITEM_SET_ID = rec_content_tmp.ext_cms_item_set_id
                                  and item_set.item_set_id > 999 and item_set_category_id in (select
                                  item_set_category_id from item_set_category where framework_product_id =
                                  v_framework_product_id)),
                                 2,
                                 'RE');

       --commit;

    
        for rec_product in c_product loop
            

            /* insert into item_set_product for regular product, since we delete if it exists */
            insert into item_set_product (item_set_id,
                                  product_id,
                                  created_by,
                                  created_date_time
                                  --updated_by,
                                  --updated_date_time
                                  ) values (
                                  (select item_set_id from item_set
                                  where item_set.EXT_CMS_ITEM_SET_ID = rec_content_tmp.ext_cms_item_set_id
                                  and item_set.item_set_id > 999 and item_set_category_id in (select
                                  item_set_category_id from item_set_category where framework_product_id =
                                  v_framework_product_id)),
                                  rec_product.product_id,
                                  2,
                                  sysdate);
                                  
       end loop;
                                  
                                  
       /* insert into item_set_product for framework product, since we delete if it exists */                   
       insert into item_set_product (item_set_id,
                                  product_id,
                                  created_by,
                                  created_date_time
                                  --updated_by,
                                  --updated_date_time
                                  ) values (
                                  (select item_set_id from item_set
                                  where item_set.EXT_CMS_ITEM_SET_ID = rec_content_tmp.ext_cms_item_set_id
                                  and item_set.item_set_id > 999 and item_set_category_id in (select
                                  item_set_category_id from item_set_category where framework_product_id =
                                  v_framework_product_id)),
                                  v_framework_product_id,
                                  2,
                                  sysdate);

       --commit;    
    end if;         
    if (rec_content_tmp.item_set_category_level <> 1 and rec_content_tmp.item_set_category_level <> 2) then

        select product_id into v_product_id
            from product where product.internal_display_name = rec_content_tmp.internal_product_display_name;


       /* determine if the itemset of the same ext_cms_item_set_id exists
       and if it has the same name */
       select count(*) into v_count_same_item_set 
           from item_set where ext_cms_item_set_id = rec_content_tmp.ext_cms_item_set_id and item_set_category_id in 
           (select item_set_category_id from item_set_category where framework_product_id = v_framework_product_id);
    
    




       /* if item set row exists, then update */
       if v_count_same_item_set <> 0 then
    
           update item_set 
              set grade = rec_content_tmp.grade,
                  ext_cms_item_set_id = rec_content_tmp.ext_cms_item_set_id,
                  item_set_category_id = (select item_set_category_id 
                                         from item_set_category 
                                         where item_set_category.item_set_category_level = rec_content_tmp.item_set_category_level
                                         and item_set_category.framework_product_id = v_framework_product_id)
              where item_set.ext_cms_item_set_id = rec_content_tmp.ext_cms_item_set_id and item_set_category_id in
                (select item_set_category_id from item_set_category where framework_product_id = v_framework_product_id);
           --commit;
       

           /* now that the item_set row has been updated, we'll delete */ 
           /*the existing item_set_parent and item_set product rows for it */
           select item_set_id into v_delete_item_set_parent_id
               from item_set iset where iset.EXT_CMS_ITEM_SET_ID = rec_content_tmp.parent_ext_cms_item_set_id
               and iset.item_set_id > 999 and item_set_category_id in (select item_set_category_id from item_set_category 
               where framework_product_id = v_framework_product_id);

           select item_set_id into v_delete_item_set_id
               from item_set where item_set.EXT_CMS_ITEM_SET_ID = rec_content_tmp.ext_cms_item_set_id
               and item_set.item_set_id > 999 and item_set_category_id in (select item_set_category_id from
               item_set_category where framework_product_id = v_framework_product_id);

           --delete from item_set_parent ispa where ispa.PARENT_ITEM_SET_ID = v_delete_item_set_parent_id
           --and item_set_id = v_delete_item_set_id;
       
           delete from item_set_parent ispa where ispa.item_set_id = v_delete_item_set_id;
       
           delete from item_set_product ispr where ispr.product_id = v_product_id
               and ispr.item_set_id = v_delete_item_set_id;
       
           delete from item_set_product isprfr where isprfr.product_id = v_framework_product_id
               and isprfr.item_set_id = v_delete_item_set_id;
       
           --commit;
       
            
        
       /* if item_set row does not exist, then insert */
       else
    
          insert into item_set (item_set_id,
                              item_set_type,
                              item_set_name,
                              --min_grade,
                              --version,
                              --max_grade,
                              item_set_level,
                              --subject,
                              grade,
                              --sample,
                              --media_path,
                              --time_limit,
                              --break_time,
                              --ext_ems_item_set_id,
                              ext_cms_item_set_id,
                              item_set_display_name,
                              --item_set_description,
                              --item_set_rule_id,
                              created_date_time,
                              created_by,
                              activation_status,
                              item_set_category_id
                              --owner_customer_id,
                              --updated_by,
                              --updated_date_time,
                              --item_set_form,
                              --publish_status,
                              --original_created_by
                              ) values (
                              seq_item_set_id.nextval,
                              'RE',
                              rec_content_tmp.item_set_name,
                              rec_content_tmp.grade,
                              rec_content_tmp.grade,
                              rec_content_tmp.ext_cms_item_set_id,
                              rec_content_tmp.item_set_name,
                              sysdate,
                              2,
                              'AC',
                               (select item_set_category_id 
                                         from item_set_category 
                                         where item_set_category.item_set_category_level = rec_content_tmp.item_set_category_level
                                         and item_set_category.framework_product_id = v_framework_product_id));
        
          --commit;                              
        

    
       end if;

    
    
       /* always insert into item_set_parent, since we delete if it exists */
       insert into item_set_parent (parent_item_set_id,
                                 created_date_time,
                                 item_set_type,
                                 item_set_id,
                                 created_by,
                                 --updated_by,
                                 --updated_date_time,
                                 --item_set_sort_order,
                                 parent_item_set_type
                                 ) values (
                                 (select item_set_id from item_set
                                  where item_set.EXT_CMS_ITEM_SET_ID = rec_content_tmp.parent_ext_cms_item_set_id
                                  and item_set.item_set_id > 999 and item_set_category_id in (select
                                  item_set_category_id from item_set_category where framework_product_id =
                                  v_framework_product_id)),
                                 sysdate,
                                 'RE',
                                 (select item_set_id from item_set
                                  where item_set.EXT_CMS_ITEM_SET_ID = rec_content_tmp.ext_cms_item_set_id
                                  and item_set.item_set_id > 999 and item_set_category_id in (select
                                  item_set_category_id from item_set_category where framework_product_id =
                                  v_framework_product_id)),
                                 2,
                                 'RE');

       --commit;

    
    
       /* insert into item_set_product for regular product, since we delete if it exists */
       insert into item_set_product (item_set_id,
                                  product_id,
                                  created_by,
                                  created_date_time
                                  --updated_by,
                                  --updated_date_time
                                  ) values (
                                  (select item_set_id from item_set
                                  where item_set.EXT_CMS_ITEM_SET_ID = rec_content_tmp.ext_cms_item_set_id
                                  and item_set.item_set_id > 999 and item_set_category_id in (select
                                  item_set_category_id from item_set_category where framework_product_id =
                                  v_framework_product_id)),
                                  v_product_id,
                                  2,
                                  sysdate);
                                  
                                  
       /* insert into item_set_product for framework product, since we delete if it exists */                   
       insert into item_set_product (item_set_id,
                                  product_id,
                                  created_by,
                                  created_date_time
                                  --updated_by,
                                  --updated_date_time
                                  ) values (
                                  (select item_set_id from item_set
                                  where item_set.EXT_CMS_ITEM_SET_ID = rec_content_tmp.ext_cms_item_set_id
                                  and item_set.item_set_id > 999 and item_set_category_id in (select
                                  item_set_category_id from item_set_category where framework_product_id =
                                  v_framework_product_id)),
                                  v_framework_product_id,
                                  2,
                                  sysdate);

       --commit;
    end if;

end loop; /* end of rec_content_tmp loop */
--commit;
end;
/

---------------------------------
-- end create SP_FRAMEWORK_IMPORT
---------------------------------

-------------------------------------------------------
-- start create/delete item ancestor triggers and procs
-------------------------------------------------------

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

CREATE OR REPLACE TRIGGER DELETE_ITEM_SET_ANCESTOR
 AFTER DELETE ON ITEM_SET_PARENT
FOR EACH ROW
DECLARE
BEGIN
   DELETE_ITEM_SET_ANCESTORS(:OLD.ITEM_SET_ID);
   EXCEPTION
     WHEN OTHERS THEN
       -- Consider logging the error and then re-raise
       RAISE;
END DELETE_ITEM_SET_ANCESTOR;

/

CREATE OR REPLACE TRIGGER INSERT_ITEM_SET_ANCESTOR 
AFTER  INSERT  ON ITEM_SET_PARENT 
REFERENCING 
 NEW AS NEW
 OLD AS OLD
FOR EACH ROW
DECLARE
BEGIN
   INSERT_ITEM_SET_ANCESTORS(:NEW.PARENT_ITEM_SET_ID,
                             :NEW.ITEM_SET_ID,
                             :NEW.ITEM_SET_TYPE,
                             :NEW.PARENT_ITEM_SET_TYPE,
                             :NEW.CREATED_BY,
                             :NEW.ITEM_SET_SORT_ORDER);
   EXCEPTION
     WHEN OTHERS THEN
       -- Consider logging the error and then re-raise
       RAISE;
END INSERT_ITEM_SET_ANCESTOR;

/

-------------------------------------------------------
-- end create/delete item ancestor triggers and procs
-------------------------------------------------------