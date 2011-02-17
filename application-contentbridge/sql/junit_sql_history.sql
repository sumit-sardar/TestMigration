/*

update PRODUCT set INTERNAL_DISPLAY_NAME = 'CTB' where 
PRODUCT_ID = 1003;

update PRODUCT set INTERNAL_DISPLAY_NAME = 'WV' where 
PRODUCT_ID = 1004;

INSERT INTO TEMPLATE_CODE ( IS_READING_PASSAGE, IS_STIMULUS_GRAPHIC,
TEMPLATE_DESC, TEMPLATE_ID ) VALUES ( 'F', 'T', ' ', 'CR1');

INSERT INTO TEMPLATE_CODE ( IS_READING_PASSAGE, IS_STIMULUS_GRAPHIC,
TEMPLATE_DESC, TEMPLATE_ID ) VALUES ( 'T', 'T', ' ', 'CR2');

insert into item_set (ITEM_SET_ID, ITEM_SET_TYPE, ITEM_SET_NAME,
ITEM_SET_DISPLAY_NAME, GRADE, SAMPLE, EXT_CMS_ITEM_SET_ID,
ACTIVATION_STATUS, ITEM_SET_CATEGORY_ID, CREATED_BY) VALUES 
(seq_item_set_id.NextVal, 'RE','Scarcity and Choice', ' ', 7, 'F' , '7E.1.1', 'AC', 6, 999);

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


----------------------------------------------
-- start create CCSUNIT product and categories
----------------------------------------------
  
delete item_set_parent where item_set_id in (
  select item_set_id from item_set where item_set_category_id in (
    select item_set_category_id from item_set_category where framework_product_id=2000
 )); 

delete item_set_ancestor where item_set_id in (
  select item_set_id from item_set where item_set_category_id in (
    select item_set_category_id from item_set_category where framework_product_id=2000
 ));
 
delete item_set_product where item_set_id in  (
  select item_set_id from item_set where item_set_category_id in (
    select item_set_category_id from item_set_category where framework_product_id=2000
 ));  

delete item_set where item_set_category_id in (
  select item_set_category_id from item_set_category where framework_product_id=2000
);

delete item_set_category where framework_product_id=2000;

delete product where product_id >=2000 and product_id <= 2005;

insert into product (product_id, created_date_time, product_description, version, created_by, 
    activation_status, product_type, product_name, scoring_item_set_level, content_area_level, 
    preview_item_set_level, internal_display_name) values 
    (2000,sysdate,'Curriculum Framework for the Unit Testing','1.0',1,'AC','CF',
     'i-know Curriculum Framework for Unit Testing',4,3,4, 'CCSUNIT');

insert into product (product_id, created_date_time, product_description, parent_product_id, version, 
    created_by, activation_status, product_type, product_name, internal_display_name, 
    scoring_item_set_level, content_area_level, preview_item_set_level) values 
    (2001,sysdate,'CCSUNIT Reading',2000,'1.0',1,'AC','ST','READING', 'CCSUNIT Reading',4,3,4);
    
insert into product (product_id, created_date_time, product_description, parent_product_id, version, 
    created_by, activation_status, product_type, product_name, internal_display_name, 
    scoring_item_set_level, content_area_level, preview_item_set_level) values 
    (2002,sysdate,'CCSUNIT Writing',2000,'1.0',1,'AC','ST','WRITING', 'CCSUNIT Writing',4,3,4);
    
insert into product (product_id, created_date_time, product_description, parent_product_id, version, 
    created_by, activation_status, product_type, product_name, internal_display_name, 
    scoring_item_set_level, content_area_level, preview_item_set_level) values 
    (2003,sysdate,'CCSUNIT Mathematics',2000,'1.0',1,'AC','ST','MATHEMATICS','CCSUNIT Mathematics',4,3,4);
        

insert into product (product_id, created_date_time, product_description, parent_product_id, version, 
    created_by, activation_status, product_type, product_name, internal_display_name, 
    scoring_item_set_level, content_area_level, preview_item_set_level) values 
    (2004,sysdate,'CCSUNIT Science',2000,'1.0',1,'AC','ST','SCIENCE','CCSUNIT Science',4,3,4);

insert into product (product_id, created_date_time, product_description, parent_product_id, version, 
    created_by, activation_status, product_type, product_name, internal_display_name, 
    scoring_item_set_level, content_area_level, preview_item_set_level) values 
    (2005,sysdate,'CCSUNIT Language',2000,'1.0',1,'AC','ST','LANGUAGE','CCSUNIT Language',4,3,4);


insert into item_set_category (item_set_category_id, item_set_category_level, item_set_category_name, 
    created_by, created_date_time, framework_product_id) values (seq_item_set_category_id.nextval,
    1,'Root',1,sysdate,2000);
    
insert into item_set_category (item_set_category_id, item_set_category_level, item_set_category_name, 
    created_by, created_date_time, framework_product_id) values (seq_item_set_category_id.nextval,
    2,'Grade',1,sysdate,2000);

insert into item_set_category (item_set_category_id, item_set_category_level, item_set_category_name, 
    created_by, created_date_time, framework_product_id) values (seq_item_set_category_id.nextval,
    3,'Content Area',1,sysdate,2000);

insert into item_set_category (item_set_category_id, item_set_category_level, item_set_category_name, 
    created_by, created_date_time, framework_product_id) values (seq_item_set_category_id.nextval,
    4,'Strand',1,sysdate,2000);

insert into item_set_category (item_set_category_id, item_set_category_level, item_set_category_name, 
    created_by, created_date_time, framework_product_id) values (seq_item_set_category_id.nextval,
    5,'Standard',1,sysdate,2000);

insert into item_set_category (item_set_category_id, item_set_category_level, item_set_category_name, 
    created_by, created_date_time, framework_product_id) values (seq_item_set_category_id.nextval,
    6,'Benchmark',1,sysdate,2000);


insert into item_set_category (item_set_category_id, item_set_category_level, item_set_category_name, 
    created_by, created_date_time, framework_product_id) values (seq_item_set_category_id.nextval,
    7,'Skill',1,sysdate,2000);
    
commit;    


--------------------------------------------
-- end create CCSUNIT product and categories
--------------------------------------------  
  

update PRODUCT set INTERNAL_DISPLAY_NAME = 'CTB Reading' where PRODUCT_ID = 1000;
update PRODUCT set INTERNAL_DISPLAY_NAME = 'CTB Mathematics' where PRODUCT_ID = 1001;

--------------------------------------------
-- start create CCSUNT WV stored proc, prods, and categories
--------------------------------------------

CREATE OR REPLACE procedure SP_FRAMEWORK_IMPORT_WV (framework_product_id_in in number) 
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
        if (rec_content_tmp.item_set_category_level <> 1) then

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

--------------------------------------------
-- end create CCSUNT WV stored proc, prods, and categories
--------------------------------------------

---------------
-- start create WV UNIT TEST products & categories
---------------

insert into product (product_id, created_date_time, product_description, version, created_by, 
    activation_status, product_type, product_name, scoring_item_set_level, content_area_level, 
    preview_item_set_level, internal_display_name) values 
    (3000,sysdate,'Curriculum Framework for the Unit Testing WV','1.0',1,'AC','CF',
     'i-know Curriculum Framework for Unit Testing WV',4,3,4, 'CCSUNITWV');

insert into product (product_id, created_date_time, product_description, parent_product_id, version, 
    created_by, activation_status, product_type, product_name, internal_display_name, 
    scoring_item_set_level, content_area_level, preview_item_set_level) values 
    (3001,sysdate,'CCSUNITWV Reading Language Arts',3000,'1.0',1,'AC','ST','READING LANGUAGE ARTS', 'CCSUNITWV Reading Language Arts',4,3,4);
        
insert into product (product_id, created_date_time, product_description, parent_product_id, version, 
    created_by, activation_status, product_type, product_name, internal_display_name, 
    scoring_item_set_level, content_area_level, preview_item_set_level) values 
    (3002,sysdate,'CCSUNITWV Mathematics',3000,'1.0',1,'AC','ST','MATHEMATICS','CCSUNITWV Mathematics',4,3,4);
        

insert into product (product_id, created_date_time, product_description, parent_product_id, version, 
    created_by, activation_status, product_type, product_name, internal_display_name, 
    scoring_item_set_level, content_area_level, preview_item_set_level) values 
    (3003,sysdate,'CCSUNITWV Science',3000,'1.0',1,'AC','ST','SCIENCE','CCSUNITWV Science',4,3,4);

insert into product (product_id, created_date_time, product_description, parent_product_id, version, 
    created_by, activation_status, product_type, product_name, internal_display_name, 
    scoring_item_set_level, content_area_level, preview_item_set_level) values 
    (3004,sysdate,'CCSUNITWV Social Studies',3000,'1.0',1,'AC','ST','SOCIAL STUDIES','CCSUNITWV  Social Studies',4,3,4);


insert into item_set_category (item_set_category_id, item_set_category_level, item_set_category_name, 
    created_by, created_date_time, framework_product_id) values (seq_item_set_category_id.nextval,
    1,'Root',1,sysdate,3000);
    
insert into item_set_category (item_set_category_id, item_set_category_level, item_set_category_name, 
    created_by, created_date_time, framework_product_id) values (seq_item_set_category_id.nextval,
    2,'Content Area',1,sysdate,3000);

insert into item_set_category (item_set_category_id, item_set_category_level, item_set_category_name, 
    created_by, created_date_time, framework_product_id) values (seq_item_set_category_id.nextval,
    3,'Grade',1,sysdate,3000);

insert into item_set_category (item_set_category_id, item_set_category_level, item_set_category_name, 
    created_by, created_date_time, framework_product_id) values (seq_item_set_category_id.nextval,
    4,'Standard',1,sysdate,3000);

insert into item_set_category (item_set_category_id, item_set_category_level, item_set_category_name, 
    created_by, created_date_time, framework_product_id) values (seq_item_set_category_id.nextval,
    5,'Benchmark',1,sysdate,3000);

    
commit;

---------------
-- end create WV UNIT TEST products & categories
---------------


----------------------------------------------------------
-- start OAS/SOFA DM update
----------------------------------------------------------

ALTER TABLE ITEM
	ADD IBS_INVISIBLE	VARCHAR2(2);
	
ALTER TABLE ITEM_SET
	ADD EXT_TST_ITEM_SET_ID	VARCHAR2(32);
	
ALTER TABLE PRODUCT
	ADD SEC_SCORING_ITEM_SET_LEVEL	NUMBER;

CREATE TABLE SCORE_TYPE
(
	SCORE_TYPE_CODE	VARCHAR2(3) NOT NULL,
	SCORE_TYPE_NAME VARCHAR2(255) NOT NULL,
	SCORE_TYPE_DESCRIPTION VARCHAR2(255) NOT NULL,
	CONSTRAINT PK_SCORE_TYPE_CODE PRIMARY KEY (SCORE_TYPE_CODE )
);

CREATE TABLE SCORABLE_ITEM
(
 	SCORE_TYPE_CODE	VARCHAR2(3) NOT NULL,
	ITEM_SET_ID	NUMBER NOT NULL,
	ITEM_ID VARCHAR2(32) NOT NULL,
	CONSTRAINT PK_SCORABLE_ITEM PRIMARY KEY (SCORE_TYPE_CODE, ITEM_SET_ID, ITEM_ID ),
	CONSTRAINT FK_SCORE_TYPE_CODE
	  FOREIGN KEY (SCORE_TYPE_CODE)
	  REFERENCES SCORE_TYPE (SCORE_TYPE_CODE),
	CONSTRAINT FK_SCORABLE_ITEM_SET_ID
	  FOREIGN KEY (ITEM_SET_ID)
	  REFERENCES ITEM_SET (ITEM_SET_ID),
	CONSTRAINT FK_SCORABLE_ITEM_ID
	  FOREIGN KEY (ITEM_ID)
	  REFERENCES ITEM (ITEM_ID)
);

CREATE TABLE SCORE_LOOKUP
(
 	SOURCE_SCORE_TYPE_CODE	VARCHAR2(3) NOT NULL,
	DEST_SCORE_TYPE_CODE	VARCHAR2(3) NOT NULL,
	SCORE_LOOKUP_ID	VARCHAR2(32) NOT NULL,
	SOURCE_SCORE_VALUE NUMBER NOT NULL,
	DEST_SCORE_VALUE NUMBER NOT NULL,
	CONSTRAINT PK_SCORE_LOOKUP PRIMARY KEY (SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SCORE_LOOKUP_ID, SOURCE_SCORE_VALUE ),
	CONSTRAINT FK_SOURCE_SCORE_TYPE_CODE
	  FOREIGN KEY (SOURCE_SCORE_TYPE_CODE)
	  REFERENCES SCORE_TYPE (SCORE_TYPE_CODE),
	CONSTRAINT FK_DEST_SCORE_TYPE_CODE
	  FOREIGN KEY (DEST_SCORE_TYPE_CODE)
	  REFERENCES SCORE_TYPE (SCORE_TYPE_CODE)
);

CREATE TABLE SCORE_LOOKUP_ITEM_SET
(
	SCORE_LOOKUP_ID	VARCHAR2(32) NOT NULL,
	ITEM_SET_ID NUMBER NOT NULL,
	CONSTRAINT PK_SCORE_LOOKUP_ITEM_SET PRIMARY KEY ( ITEM_SET_ID ),
	CONSTRAINT FK_LOOKUP_ITEM_SET_ID
	  FOREIGN KEY (ITEM_SET_ID)
	  REFERENCES ITEM_SET (ITEM_SET_ID)
);

INSERT INTO SCORE_TYPE (SCORE_TYPE_CODE, SCORE_TYPE_NAME, SCORE_TYPE_DESCRIPTION) VALUES ('SRW', 'Raw', 'Sub-test Raw score');
INSERT INTO SCORE_TYPE (SCORE_TYPE_CODE, SCORE_TYPE_NAME, SCORE_TYPE_DESCRIPTION) VALUES ('SCL', 'Scale', 'Table-lookup scale score');

INSERT INTO PRODUCT_TYPE_CODE (PRODUCT_TYPE, PRODUCT_TYPE_DESC) VALUES ('SF', 'SCALED FORMATIVE');

commit;

CREATE OR REPLACE PROCEDURE SP_COPY_TO_OSR (
test_roster_id_ integer)
IS

v_count integer;

v_PARENT_ORG_NODE_ID ORG_NODE.ORG_NODE_ID%type;
v_GRANDPARENT_ORG_NODE_ID ORG_NODE.ORG_NODE_ID%type;
v_CREATOR_ORG_NODE_NAME ORG_NODE.ORG_NODE_NAME%type;
v_PARENT_ORG_NODE_NAME ORG_NODE.ORG_NODE_NAME%type;
v_GRANDPARENT_ORG_NODE_NAME ORG_NODE.ORG_NODE_NAME%type;

v_CONTACT_NAME     STUDENT_CONTACT.CONTACT_NAME%type;
v_CONTACT_TYPE	   STUDENT_CONTACT.CONTACT_TYPE%type;
v_CONTACT_EMAIL    STUDENT_CONTACT.CONTACT_EMAIL%type;
v_STREET_LINE1     STUDENT_CONTACT.STREET_LINE1%type;
v_STREET_LINE2     STUDENT_CONTACT.STREET_LINE2%type;
v_STREET_LINE3     STUDENT_CONTACT.STREET_LINE3%type;
v_CITY             STUDENT_CONTACT.CITY%type;
v_STATEPR          STUDENT_CONTACT.STATEPR%type;
v_COUNTRY          STUDENT_CONTACT.COUNTRY%type;
v_ZIPCODE          STUDENT_CONTACT.ZIPCODE%type;
v_PRIMARY_PHONE    STUDENT_CONTACT.PRIMARY_PHONE%type;
v_SECONDARY_PHONE  STUDENT_CONTACT.SECONDARY_PHONE%type;
v_FAX              STUDENT_CONTACT.FAX%type;


V_TEST_ROSTER_ID   			   TEST_ROSTER.TEST_ROSTER_ID%type;
V_CUSTOMER_ID	   			   TEST_ROSTER.CUSTOMER_ID%type;
V_STUDENT_ID	   			   TEST_ROSTER.STUDENT_ID%type;
V_TEST_ADMIN_ID	   			   TEST_ROSTER.TEST_ADMIN_ID%type;
V_PASSWORD	   				   TEST_ROSTER.PASSWORD%type;
V_CAPTURE_METHOD			   TEST_ROSTER.CAPTURE_METHOD%type;
V_TEST_COMPLETION_STATUS	   TEST_ROSTER.TEST_COMPLETION_STATUS%type;
V_SCORING_STATUS	   		   TEST_ROSTER.SCORING_STATUS%type;
V_START_DATE_TIME	   		   TEST_ROSTER.START_DATE_TIME%type;
V_COMPLETION_DATE_TIME	   	   TEST_ROSTER.COMPLETION_DATE_TIME%type;
V_VALIDATION_STATUS	   		   TEST_ROSTER.VALIDATION_STATUS%type;
V_VALIDATION_UPDATED_BY	   	   TEST_ROSTER.VALIDATION_UPDATED_BY%type;
V_VALIDATION_UPDATED_DATE_TIME TEST_ROSTER.VALIDATION_UPDATED_DATE_TIME%type;
V_VALIDATION_UPDATED_NOTE	   TEST_ROSTER.VALIDATION_UPDATED_NOTE%type;
V_OVERRIDE_TEST_WINDOW	   	   TEST_ROSTER.OVERRIDE_TEST_WINDOW%type;
V_CREATED_BY	   			   TEST_ROSTER.CREATED_BY%type;
V_CREATED_DATE_TIME	   		   TEST_ROSTER.CREATED_DATE_TIME%type;
V_UPDATED_BY	   	   		   TEST_ROSTER.UPDATED_BY%type;
V_UPDATED_DATE_TIME	   		   TEST_ROSTER.UPDATED_DATE_TIME%type;
V_ACTIVATION_STATUS	   		   TEST_ROSTER.ACTIVATION_STATUS%type;
V_TUTORIAL_TAKEN_DATE_TIME	   TEST_ROSTER.TUTORIAL_TAKEN_DATE_TIME%type;







begin
	-- when a student complete a test, TEST_ROSTER, STUDENT_ITEM_SET_STATUS,
	-- and ITEM_RESPONSE records are copied to OSR database.
	-- STUDENT_TEST_HISTORY record is created also.


-- set up test roster variables...
select   TEST_ROSTER_ID,
		  CUSTOMER_ID,
		  STUDENT_ID,
		  TEST_ADMIN_ID,
		  PASSWORD,
		  CAPTURE_METHOD,
		  TEST_COMPLETION_STATUS,
		  SCORING_STATUS,
		  START_DATE_TIME,
		  COMPLETION_DATE_TIME,
		  VALIDATION_STATUS,
		  VALIDATION_UPDATED_BY,
		  VALIDATION_UPDATED_DATE_TIME,
		  VALIDATION_UPDATED_NOTE,
		  OVERRIDE_TEST_WINDOW,
		  CREATED_BY,
		  CREATED_DATE_TIME,
		  UPDATED_BY,
		  UPDATED_DATE_TIME,
		  ACTIVATION_STATUS,
		  TUTORIAL_TAKEN_DATE_TIME
		into
		  V_TEST_ROSTER_ID,
		  V_CUSTOMER_ID,
		  V_STUDENT_ID,
		  V_TEST_ADMIN_ID,
		  V_PASSWORD,
		  V_CAPTURE_METHOD,
		  V_TEST_COMPLETION_STATUS,
		  V_SCORING_STATUS,
		  V_START_DATE_TIME,
		  V_COMPLETION_DATE_TIME,
		  V_VALIDATION_STATUS,
		  V_VALIDATION_UPDATED_BY,
		  V_VALIDATION_UPDATED_DATE_TIME,
		  V_VALIDATION_UPDATED_NOTE,
		  V_OVERRIDE_TEST_WINDOW,
		  V_CREATED_BY,
		  V_CREATED_DATE_TIME,
		  V_UPDATED_BY,
		  V_UPDATED_DATE_TIME,
		  V_ACTIVATION_STATUS,
		  V_TUTORIAL_TAKEN_DATE_TIME
		  from TEST_ROSTER
		  where TEST_ROSTER.TEST_ROSTER_ID = test_roster_id_;


		delete test_roster@osr
		where test_roster_id = V_TEST_ROSTER_ID;

		delete item_response@osr
		where test_roster_id = V_TEST_ROSTER_ID;


		delete student_item_set_status@osr
		where test_roster_id = V_TEST_ROSTER_ID;

		delete student_item_score@osr
		where student_test_history_id in (
			  select student_test_history_id
			  from student_test_history@osr
		      where test_roster_id = V_TEST_ROSTER_ID);

		delete item_response_points@osr
		where item_response_id in (select item_response_id
			  				   	  from item_response
								  where test_roster_id = V_TEST_ROSTER_ID);

		delete student_score_summary@osr
		where student_test_history_id in (
			  select student_test_history_id
			  from student_test_history@osr
		      where test_roster_id = V_TEST_ROSTER_ID);
			  
	    delete student_subtest_scores@osr
		where student_test_history_id in (
			  select student_test_history_id
			  from student_test_history@osr
		      where test_roster_id = V_TEST_ROSTER_ID);

		delete student_test_history@osr
		where test_roster_id = V_TEST_ROSTER_ID;


        insert into test_roster@osr
		  (TEST_ROSTER_ID,
		  CUSTOMER_ID,
		  STUDENT_ID,
		  TEST_ADMIN_ID,
		  PASSWORD,
		  CAPTURE_METHOD,
		  TEST_COMPLETION_STATUS,
		  SCORING_STATUS,
		  START_DATE_TIME,
		  COMPLETION_DATE_TIME,
		  VALIDATION_STATUS,
		  VALIDATION_UPDATED_BY,
		  VALIDATION_UPDATED_DATE_TIME,
		  VALIDATION_UPDATED_NOTE,
		  OVERRIDE_TEST_WINDOW,
		  CREATED_BY,
		  CREATED_DATE_TIME,
		  UPDATED_BY,
		  UPDATED_DATE_TIME,
		  ACTIVATION_STATUS,
		  TUTORIAL_TAKEN_DATE_TIME)
		values (
		  v_TEST_ROSTER_ID,
		  v_CUSTOMER_ID,
		  v_STUDENT_ID,
		  v_TEST_ADMIN_ID,
		  v_PASSWORD,
		  v_CAPTURE_METHOD,
		  v_TEST_COMPLETION_STATUS,
		  v_SCORING_STATUS,
		  v_START_DATE_TIME,
		  v_COMPLETION_DATE_TIME,
		  v_VALIDATION_STATUS,
		  v_VALIDATION_UPDATED_BY,
		  v_VALIDATION_UPDATED_DATE_TIME,
		  v_VALIDATION_UPDATED_NOTE,
		  v_OVERRIDE_TEST_WINDOW,
		  v_CREATED_BY,
		  v_CREATED_DATE_TIME,
		  v_UPDATED_BY,
		  v_UPDATED_DATE_TIME,
		  v_ACTIVATION_STATUS,
		  v_TUTORIAL_TAKEN_DATE_TIME);


        insert into student_item_set_status@osr
		  select * from student_item_set_status
		  where test_roster_id = V_TEST_ROSTER_ID;

        insert into item_response@osr (item_response_id, test_roster_id, item_set_id,
	   						  item_id, response, response_method,
							  response_elapsed_time, response_seq_num, created_date_time,
							  ext_answer_choice_id, student_marked, created_by)
		select item_response_id, test_roster_id, item_set_id,
	   						  item_id, response, response_method,
							  response_elapsed_time, response_seq_num, created_date_time,
							  ext_answer_choice_id, student_marked, created_by
		from item_response
         where test_roster_id = V_TEST_ROSTER_ID;

	insert into item_response_points@osr
		  select * from item_response_points
		  where item_response_id in (select item_response_id
			  				   	  from item_response
								  where test_roster_id = V_TEST_ROSTER_ID);


		begin
			select org_node_name
			into v_creator_org_node_name
			from org_node , test_admin
			where test_admin_id = v_test_admin_id
			  and creator_org_node_id = org_node_id;
		exception
			when no_data_found then
				v_creator_org_node_name := NULL;
		end;


		begin
			select parent_org_node_id, org_node_name
			into v_parent_org_node_id, v_parent_org_node_name
			from org_node_parent onp, org_node org, test_admin ta
			where onp.org_node_id = ta.creator_org_node_id
			  and org.org_node_id = onp.parent_org_node_id
			  and ta.test_admin_id = v_test_admin_id;
		exception
			when no_data_found then
				v_parent_org_node_id := NULL;
				v_parent_org_node_name := NULL;
		end;


		begin
			select onp.parent_org_node_id, org.org_node_name
			into v_grandparent_org_node_id,
				 v_grandparent_org_node_name
			from org_node_parent onp, org_node org
			where onp.org_node_id = v_parent_org_node_id
			  and onp.parent_org_node_id = org.org_node_id;
		exception
			when no_data_found then
				v_grandparent_org_node_id := NULL;
				v_grandparent_org_node_name := NULL;
		end;


		begin
			select
				stc.CONTACT_NAME,
				stc.CONTACT_TYPE,
				stc.CONTACT_EMAIL,
				stc.STREET_LINE1,
				stc.STREET_LINE2,
				stc.STREET_LINE3,
				stc.CITY,
				stc.STATEPR,
				stc.COUNTRY,
				stc.ZIPCODE,
				stc.PRIMARY_PHONE,
				stc.SECONDARY_PHONE,
				stc.FAX
			into
				v_CONTACT_NAME,
				v_CONTACT_TYPE,
				v_CONTACT_EMAIL,
				v_STREET_LINE1,
				v_STREET_LINE2,
				v_STREET_LINE3,
				v_CITY,
				v_STATEPR,
				v_COUNTRY,
				v_ZIPCODE,
				v_PRIMARY_PHONE,
				v_SECONDARY_PHONE,
				v_FAX
			from student_contact stc
			where student_id = v_student_id
			  and rownum = 1;
		exception
			when no_data_found then
				v_CONTACT_NAME   := NULL;
				v_CONTACT_TYPE   := NULL;
				v_CONTACT_EMAIL  := NULL;
				v_STREET_LINE1   := NULL;
				v_STREET_LINE2   := NULL;
				v_STREET_LINE3   := NULL;
				v_CITY           := NULL;
				v_STATEPR        := NULL;
				v_COUNTRY        := NULL;
				v_ZIPCODE        := NULL;
				v_PRIMARY_PHONE  := NULL;
				v_SECONDARY_PHONE:= NULL;
				v_FAX            := NULL;
		end;


		insert into student_test_history@osr
		(
		STUDENT_TEST_HISTORY_ID,
		TEST_ROSTER_ID,
		CUSTOMER_ID,
		TEST_ADMIN_ID,
		TEST_COMPLETION_STATUS,
		CAPTURE_METHOD,
		STUDENT_ID,
		START_DATE_TIME,
		COMPLETION_DATE_TIME,
		TEST_NAME,
		TEST_ADMIN_NAME,
		TEST_CATALOG_ID,
		TEST_ITEM_SET_ID,
		PRODUCT_ID,
		PRODUCT_NAME,
		TEST_VALIDATION_STATUS,
		TEST_VALIDATION_UPDATED_BY,
		TEST_VALIDATION_UPDATED_DATE,
		TEST_VALIDATION_UPDATED_NOTE,
		LEXINGTON_VERSION,
		TUTORIAL_ID,
		USER_NAME,
		LAST_NAME,
		FIRST_NAME,
		MIDDLE_NAME,
		PREFIX,
		SUFFIX,
		BIRTHDATE,
		GENDER,
		ETHNICITY,
		EMAIL,
		GRADE,
		AGE,
		EXT_PIN1,
		EXT_PIN2,
		EXT_PIN3,
		EXT_SCHOOL_ID,
		EXT_ELM_ID,
		CUSTOMER_NAME,
		CREATED_DATE_TIME,
		SCORING_STATUS,
		CREATOR_ORG_NODE_ID,
		PARENT_ORG_NODE_ID,
		GRANDPARENT_ORG_NODE_ID,
		CREATOR_ORG_NODE_NAME,
		PARENT_ORG_NODE_NAME,
		GRANDPARENT_ORG_NODE_NAME,
		CONTACT_NAME,
		CONTACT_TYPE,
		CONTACT_EMAIL,
		STREET_LINE1,
		STREET_LINE2,
		STREET_LINE3,
		CITY,
		STATEPR,
		COUNTRY,
		ZIPCODE,
		PRIMARY_PHONE,
		SECONDARY_PHONE,
		FAX,
		SCHEDULER_USER_ID,
		SCHEDULER_USER_NAME)
		select
		seq_student_test_history.nextval@osr,
		v_test_roster_id,
		v_customer_id,
		v_test_admin_id,
		v_test_completion_status,
		v_capture_method,
		v_student_id,
		v_start_date_time,
		v_completion_date_time,
		tc.TEST_NAME,
		ta.TEST_ADMIN_NAME,
		tc.TEST_CATALOG_ID,
		tc.ITEM_SET_ID,
		ta.PRODUCT_ID,
		pr.PRODUCT_NAME,
		v_VALIDATION_STATUS,
		v_VALIDATION_UPDATED_BY,
		v_VALIDATION_UPDATED_DATE_TIME,
		v_VALIDATION_UPDATED_NOTE,
		ta.LEXINGTON_VERSION,
		ta.TUTORIAL_ID,
		st.USER_NAME,
		st.LAST_NAME,
		st.FIRST_NAME,
		st.MIDDLE_NAME,
		st.PREFIX,
		st.SUFFIX,
		st.BIRTHDATE,
		st.GENDER,
		st.ETHNICITY,
		st.EMAIL,
		st.GRADE,
		round(months_between(sysdate,st.birthdate)/12),
		st.EXT_PIN1,
		st.EXT_PIN2,
		st.EXT_PIN3,
		st.EXT_SCHOOL_ID,
		st.EXT_ELM_ID,
		cust.CUSTOMER_NAME,
		sysdate,
		'IN',
		ta.CREATOR_ORG_NODE_ID,
		v_PARENT_ORG_NODE_ID,
		v_GRANDPARENT_ORG_NODE_ID,
		v_CREATOR_ORG_NODE_NAME,
		v_PARENT_ORG_NODE_NAME,
		v_GRANDPARENT_ORG_NODE_NAME,
		v_CONTACT_NAME,
		v_CONTACT_TYPE,
		v_CONTACT_EMAIL,
		v_STREET_LINE1,
		v_STREET_LINE2,
		v_STREET_LINE3,
		v_CITY,
		v_STATEPR,
		v_COUNTRY,
		v_ZIPCODE,
		v_PRIMARY_PHONE,
		v_SECONDARY_PHONE,
		v_FAX,
		ta.created_by,
		trim(users.first_name) || ' ' || trim(users.last_name)
		from test_catalog tc,
		     test_admin ta,
			 product pr,
			 student st,
			 customer cust,
			 users
	    where tc.test_catalog_id = ta.test_catalog_id
		  and ta.test_admin_id = v_test_admin_id
		  and ta.product_id = pr.product_id
		  and st.student_id = v_student_id
		  and cust.customer_id = ta.customer_id
		  and users.user_id = ta.created_by;

commit;

end;
/

commit;

insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13R','SRW','SCL',0,480);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13R','SRW','SCL',1,480);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13R','SRW','SCL',2,480);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13R','SRW','SCL',3,480);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13R','SRW','SCL',4,480);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13R','SRW','SCL',5,549);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13R','SRW','SCL',6,584);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13R','SRW','SCL',7,604);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13R','SRW','SCL',8,619);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13R','SRW','SCL',9,631);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13R','SRW','SCL',10,642);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13R','SRW','SCL',11,652);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13R','SRW','SCL',12,662);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13R','SRW','SCL',13,672);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13R','SRW','SCL',14,683);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13R','SRW','SCL',15,695);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13R','SRW','SCL',16,708);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13R','SRW','SCL',17,723);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13R','SRW','SCL',18,741);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13R','SRW','SCL',19,768);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13R','SRW','SCL',20,828);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13M','SRW','SCL',0,432);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13M','SRW','SCL',1,432);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13M','SRW','SCL',2,432);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13M','SRW','SCL',3,432);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13M','SRW','SCL',4,432);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13M','SRW','SCL',5,530);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13M','SRW','SCL',6,564);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13M','SRW','SCL',7,587);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13M','SRW','SCL',8,604);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13M','SRW','SCL',9,620);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13M','SRW','SCL',10,633);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13M','SRW','SCL',11,646);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13M','SRW','SCL',12,660);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13M','SRW','SCL',13,674);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13M','SRW','SCL',14,689);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13M','SRW','SCL',15,707);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13M','SRW','SCL',16,727);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13M','SRW','SCL',17,748);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13M','SRW','SCL',18,768);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13M','SRW','SCL',19,793);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A13M','SRW','SCL',20,845);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14R','SRW','SCL',0,528);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14R','SRW','SCL',1,528);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14R','SRW','SCL',2,528);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14R','SRW','SCL',3,528);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14R','SRW','SCL',4,528);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14R','SRW','SCL',5,556);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14R','SRW','SCL',6,610);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14R','SRW','SCL',7,633);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14R','SRW','SCL',8,648);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14R','SRW','SCL',9,659);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14R','SRW','SCL',10,669);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14R','SRW','SCL',11,678);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14R','SRW','SCL',12,687);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14R','SRW','SCL',13,697);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14R','SRW','SCL',14,707);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14R','SRW','SCL',15,717);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14R','SRW','SCL',16,729);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14R','SRW','SCL',17,741);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14R','SRW','SCL',18,756);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14R','SRW','SCL',19,779);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14R','SRW','SCL',20,836);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14M','SRW','SCL',0,496);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14M','SRW','SCL',1,496);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14M','SRW','SCL',2,496);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14M','SRW','SCL',3,496);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14M','SRW','SCL',4,496);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14M','SRW','SCL',5,587);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14M','SRW','SCL',6,621);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14M','SRW','SCL',7,643);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14M','SRW','SCL',8,660);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14M','SRW','SCL',9,675);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14M','SRW','SCL',10,688);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14M','SRW','SCL',11,701);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14M','SRW','SCL',12,713);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14M','SRW','SCL',13,725);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14M','SRW','SCL',14,737);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14M','SRW','SCL',15,748);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14M','SRW','SCL',16,760);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14M','SRW','SCL',17,773);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14M','SRW','SCL',18,787);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14M','SRW','SCL',19,807);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A14M','SRW','SCL',20,858);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15R','SRW','SCL',0,553);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15R','SRW','SCL',1,553);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15R','SRW','SCL',2,553);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15R','SRW','SCL',3,553);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15R','SRW','SCL',4,553);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15R','SRW','SCL',5,618);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15R','SRW','SCL',6,648);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15R','SRW','SCL',7,664);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15R','SRW','SCL',8,677);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15R','SRW','SCL',9,688);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15R','SRW','SCL',10,699);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15R','SRW','SCL',11,709);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15R','SRW','SCL',12,717);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15R','SRW','SCL',13,726);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15R','SRW','SCL',14,734);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15R','SRW','SCL',15,743);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15R','SRW','SCL',16,754);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15R','SRW','SCL',17,766);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15R','SRW','SCL',18,781);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15R','SRW','SCL',19,804);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15R','SRW','SCL',20,860);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15M','SRW','SCL',0,533);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15M','SRW','SCL',1,533);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15M','SRW','SCL',2,533);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15M','SRW','SCL',3,533);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15M','SRW','SCL',4,542);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15M','SRW','SCL',5,628);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15M','SRW','SCL',6,652);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15M','SRW','SCL',7,667);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15M','SRW','SCL',8,680);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15M','SRW','SCL',9,691);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15M','SRW','SCL',10,702);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15M','SRW','SCL',11,712);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15M','SRW','SCL',12,722);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15M','SRW','SCL',13,732);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15M','SRW','SCL',14,742);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15M','SRW','SCL',15,752);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15M','SRW','SCL',16,762);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15M','SRW','SCL',17,773);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15M','SRW','SCL',18,786);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15M','SRW','SCL',19,805);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A15M','SRW','SCL',20,881);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16R','SRW','SCL',0,576);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16R','SRW','SCL',1,576);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16R','SRW','SCL',2,576);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16R','SRW','SCL',3,576);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16R','SRW','SCL',4,576);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16R','SRW','SCL',5,653);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16R','SRW','SCL',6,675);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16R','SRW','SCL',7,690);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16R','SRW','SCL',8,700);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16R','SRW','SCL',9,710);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16R','SRW','SCL',10,718);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16R','SRW','SCL',11,725);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16R','SRW','SCL',12,732);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16R','SRW','SCL',13,739);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16R','SRW','SCL',14,746);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16R','SRW','SCL',15,754);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16R','SRW','SCL',16,763);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16R','SRW','SCL',17,773);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16R','SRW','SCL',18,786);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16R','SRW','SCL',19,805);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16R','SRW','SCL',20,870);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16M','SRW','SCL',0,561);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16M','SRW','SCL',1,561);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16M','SRW','SCL',2,561);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16M','SRW','SCL',3,561);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16M','SRW','SCL',4,561);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16M','SRW','SCL',5,625);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16M','SRW','SCL',6,655);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16M','SRW','SCL',7,674);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16M','SRW','SCL',8,690);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16M','SRW','SCL',9,703);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16M','SRW','SCL',10,715);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16M','SRW','SCL',11,725);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16M','SRW','SCL',12,735);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16M','SRW','SCL',13,744);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16M','SRW','SCL',14,753);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16M','SRW','SCL',15,763);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16M','SRW','SCL',16,772);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16M','SRW','SCL',17,783);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16M','SRW','SCL',18,796);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16M','SRW','SCL',19,816);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A16M','SRW','SCL',20,885);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17R','SRW','SCL',0,581);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17R','SRW','SCL',1,581);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17R','SRW','SCL',2,581);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17R','SRW','SCL',3,581);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17R','SRW','SCL',4,581);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17R','SRW','SCL',5,645);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17R','SRW','SCL',6,673);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17R','SRW','SCL',7,690);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17R','SRW','SCL',8,703);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17R','SRW','SCL',9,714);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17R','SRW','SCL',10,723);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17R','SRW','SCL',11,732);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17R','SRW','SCL',12,740);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17R','SRW','SCL',13,748);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17R','SRW','SCL',14,756);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17R','SRW','SCL',15,764);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17R','SRW','SCL',16,773);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17R','SRW','SCL',17,784);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17R','SRW','SCL',18,797);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17R','SRW','SCL',19,819);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17R','SRW','SCL',20,875);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17M','SRW','SCL',0,583);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17M','SRW','SCL',1,583);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17M','SRW','SCL',2,583);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17M','SRW','SCL',3,583);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17M','SRW','SCL',4,583);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17M','SRW','SCL',5,657);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17M','SRW','SCL',6,690);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17M','SRW','SCL',7,708);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17M','SRW','SCL',8,721);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17M','SRW','SCL',9,731);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17M','SRW','SCL',10,741);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17M','SRW','SCL',11,750);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17M','SRW','SCL',12,758);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17M','SRW','SCL',13,768);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17M','SRW','SCL',14,777);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17M','SRW','SCL',15,787);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17M','SRW','SCL',16,797);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17M','SRW','SCL',17,809);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17M','SRW','SCL',18,823);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17M','SRW','SCL',19,846);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4A17M','SRW','SCL',20,900);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13R','SRW','SCL',0,480);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13R','SRW','SCL',1,480);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13R','SRW','SCL',2,480);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13R','SRW','SCL',3,480);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13R','SRW','SCL',4,480);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13R','SRW','SCL',5,560);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13R','SRW','SCL',6,589);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13R','SRW','SCL',7,607);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13R','SRW','SCL',8,621);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13R','SRW','SCL',9,634);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13R','SRW','SCL',10,645);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13R','SRW','SCL',11,656);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13R','SRW','SCL',12,667);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13R','SRW','SCL',13,678);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13R','SRW','SCL',14,689);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13R','SRW','SCL',15,701);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13R','SRW','SCL',16,715);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13R','SRW','SCL',17,731);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13R','SRW','SCL',18,752);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13R','SRW','SCL',19,786);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13R','SRW','SCL',20,828);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13M','SRW','SCL',0,432);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13M','SRW','SCL',1,432);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13M','SRW','SCL',2,432);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13M','SRW','SCL',3,432);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13M','SRW','SCL',4,433);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13M','SRW','SCL',5,519);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13M','SRW','SCL',6,552);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13M','SRW','SCL',7,575);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13M','SRW','SCL',8,595);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13M','SRW','SCL',9,612);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13M','SRW','SCL',10,629);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13M','SRW','SCL',11,645);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13M','SRW','SCL',12,660);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13M','SRW','SCL',13,675);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13M','SRW','SCL',14,690);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13M','SRW','SCL',15,705);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13M','SRW','SCL',16,720);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13M','SRW','SCL',17,736);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13M','SRW','SCL',18,756);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13M','SRW','SCL',19,785);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B13M','SRW','SCL',20,845);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14R','SRW','SCL',0,528);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14R','SRW','SCL',1,528);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14R','SRW','SCL',2,528);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14R','SRW','SCL',3,528);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14R','SRW','SCL',4,528);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14R','SRW','SCL',5,579);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14R','SRW','SCL',6,616);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14R','SRW','SCL',7,635);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14R','SRW','SCL',8,650);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14R','SRW','SCL',9,662);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14R','SRW','SCL',10,673);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14R','SRW','SCL',11,683);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14R','SRW','SCL',12,692);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14R','SRW','SCL',13,701);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14R','SRW','SCL',14,709);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14R','SRW','SCL',15,718);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14R','SRW','SCL',16,728);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14R','SRW','SCL',17,739);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14R','SRW','SCL',18,754);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14R','SRW','SCL',19,779);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14R','SRW','SCL',20,836);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14M','SRW','SCL',0,496);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14M','SRW','SCL',1,496);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14M','SRW','SCL',2,496);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14M','SRW','SCL',3,496);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14M','SRW','SCL',4,496);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14M','SRW','SCL',5,579);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14M','SRW','SCL',6,608);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14M','SRW','SCL',7,628);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14M','SRW','SCL',8,644);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14M','SRW','SCL',9,658);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14M','SRW','SCL',10,671);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14M','SRW','SCL',11,685);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14M','SRW','SCL',12,698);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14M','SRW','SCL',13,711);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14M','SRW','SCL',14,724);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14M','SRW','SCL',15,737);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14M','SRW','SCL',16,749);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14M','SRW','SCL',17,761);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14M','SRW','SCL',18,775);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14M','SRW','SCL',19,795);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B14M','SRW','SCL',20,858);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15R','SRW','SCL',0,553);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15R','SRW','SCL',1,553);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15R','SRW','SCL',2,553);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15R','SRW','SCL',3,553);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15R','SRW','SCL',4,553);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15R','SRW','SCL',5,593);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15R','SRW','SCL',6,638);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15R','SRW','SCL',7,660);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15R','SRW','SCL',8,675);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15R','SRW','SCL',9,688);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15R','SRW','SCL',10,698);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15R','SRW','SCL',11,707);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15R','SRW','SCL',12,715);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15R','SRW','SCL',13,723);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15R','SRW','SCL',14,730);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15R','SRW','SCL',15,738);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15R','SRW','SCL',16,747);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15R','SRW','SCL',17,758);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15R','SRW','SCL',18,773);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15R','SRW','SCL',19,794);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15R','SRW','SCL',20,860);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15M','SRW','SCL',0,533);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15M','SRW','SCL',1,533);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15M','SRW','SCL',2,533);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15M','SRW','SCL',3,533);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15M','SRW','SCL',4,533);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15M','SRW','SCL',5,599);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15M','SRW','SCL',6,636);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15M','SRW','SCL',7,657);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15M','SRW','SCL',8,674);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15M','SRW','SCL',9,688);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15M','SRW','SCL',10,701);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15M','SRW','SCL',11,712);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15M','SRW','SCL',12,723);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15M','SRW','SCL',13,734);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15M','SRW','SCL',14,745);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15M','SRW','SCL',15,756);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15M','SRW','SCL',16,769);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15M','SRW','SCL',17,783);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15M','SRW','SCL',18,803);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15M','SRW','SCL',19,830);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B15M','SRW','SCL',20,881);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16R','SRW','SCL',0,576);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16R','SRW','SCL',1,576);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16R','SRW','SCL',2,576);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16R','SRW','SCL',3,576);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16R','SRW','SCL',4,576);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16R','SRW','SCL',5,652);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16R','SRW','SCL',6,681);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16R','SRW','SCL',7,697);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16R','SRW','SCL',8,707);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16R','SRW','SCL',9,716);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16R','SRW','SCL',10,724);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16R','SRW','SCL',11,732);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16R','SRW','SCL',12,739);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16R','SRW','SCL',13,746);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16R','SRW','SCL',14,754);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16R','SRW','SCL',15,763);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16R','SRW','SCL',16,773);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16R','SRW','SCL',17,785);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16R','SRW','SCL',18,799);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16R','SRW','SCL',19,820);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16R','SRW','SCL',20,870);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16M','SRW','SCL',0,561);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16M','SRW','SCL',1,561);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16M','SRW','SCL',2,561);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16M','SRW','SCL',3,561);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16M','SRW','SCL',4,561);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16M','SRW','SCL',5,605);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16M','SRW','SCL',6,657);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16M','SRW','SCL',7,678);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16M','SRW','SCL',8,692);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16M','SRW','SCL',9,704);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16M','SRW','SCL',10,714);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16M','SRW','SCL',11,723);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16M','SRW','SCL',12,732);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16M','SRW','SCL',13,741);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16M','SRW','SCL',14,750);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16M','SRW','SCL',15,760);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16M','SRW','SCL',16,770);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16M','SRW','SCL',17,782);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16M','SRW','SCL',18,798);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16M','SRW','SCL',19,822);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B16M','SRW','SCL',20,885);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17R','SRW','SCL',0,581);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17R','SRW','SCL',1,581);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17R','SRW','SCL',2,581);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17R','SRW','SCL',3,581);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17R','SRW','SCL',4,581);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17R','SRW','SCL',5,650);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17R','SRW','SCL',6,687);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17R','SRW','SCL',7,703);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17R','SRW','SCL',8,715);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17R','SRW','SCL',9,724);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17R','SRW','SCL',10,732);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17R','SRW','SCL',11,740);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17R','SRW','SCL',12,747);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17R','SRW','SCL',13,754);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17R','SRW','SCL',14,761);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17R','SRW','SCL',15,768);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17R','SRW','SCL',16,776);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17R','SRW','SCL',17,786);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17R','SRW','SCL',18,799);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17R','SRW','SCL',19,817);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17R','SRW','SCL',20,875);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17M','SRW','SCL',0,583);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17M','SRW','SCL',1,583);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17M','SRW','SCL',2,583);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17M','SRW','SCL',3,583);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17M','SRW','SCL',4,583);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17M','SRW','SCL',5,662);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17M','SRW','SCL',6,690);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17M','SRW','SCL',7,707);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17M','SRW','SCL',8,721);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17M','SRW','SCL',9,732);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17M','SRW','SCL',10,743);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17M','SRW','SCL',11,753);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17M','SRW','SCL',12,762);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17M','SRW','SCL',13,772);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17M','SRW','SCL',14,781);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17M','SRW','SCL',15,791);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17M','SRW','SCL',16,800);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17M','SRW','SCL',17,811);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17M','SRW','SCL',18,823);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17M','SRW','SCL',19,842);
insert into score_lookup (SCORE_LOOKUP_ID, SOURCE_SCORE_TYPE_CODE, DEST_SCORE_TYPE_CODE, SOURCE_SCORE_VALUE, DEST_SCORE_VALUE) values ('CTBS4B17M','SRW','SCL',20,900);

commit;


----------------------------------------------------------
-- end OAS/SOFA DM update
----------------------------------------------------------


----------------------------------------------------------
-- grade is a VARCHAR / update CONTENT_TMP
----------------------------------------------------------

DROP TABLE CONTENT_TMP CASCADE CONSTRAINTS ; 

CREATE TABLE CONTENT_TMP ( 
  GRADE                          VARCHAR2(4), 
  ITEM_SET_NAME                  VARCHAR2(512)  NOT NULL, 
  EXT_CMS_ITEM_SET_ID            VARCHAR2(32)  NOT NULL, 
  PARENT_EXT_CMS_ITEM_SET_ID     VARCHAR2(32)  NOT NULL, 
  ITEM_SET_CATEGORY_LEVEL        NUMBER(38)    NOT NULL, 
  INTERNAL_PRODUCT_DISPLAY_NAME  VARCHAR2(128));
  
----------------------------------------------------------
-- end grade is a VARCHAR / update CONTENT_TMP
----------------------------------------------------------  


 JUNIT2 UPDATED TO HERE */

----------------------------------------------------------
-- start of JUNIT test data for SOFA
----------------------------------------------------------


-- Create the 'SF' (Scaled Formative) Product Type

insert into product_type_code (product_type, product_type_desc)
values ('SF', 'SCALED FORMATIVE');

-- create some FL SOFA products for JUNIT

insert into product (
product_id, created_date_time, product_description, 
version, created_by, activation_status, product_type, product_name, scoring_item_set_level, content_area_level, 
preview_item_set_level, internal_display_name) values (
1400,sysdate,'Curriculum Framework for the State of Florida',
'1.0',1,'AC','CF',
'i-know Curriculum Framework for Florida',4,3,
4,'FL');

insert into product (
product_id, created_date_time, product_description, 
parent_product_id, version, created_by, activation_status,
product_type, product_name, internal_display_name, 
scoring_item_set_level, content_area_level, preview_item_set_level)
values (
1401, sysdate, 'FL English Language Arts',
1400, '1.0',1,'AC',
'ST','ENGLISH LANGUAGE ARTS', 'FL English Language Arts',
4,3,4);

insert into product (
product_id, created_date_time, product_description,
parent_product_id, version, 	created_by, activation_status, 
product_type, product_name, internal_display_name, 
	scoring_item_set_level, content_area_level, preview_item_set_level) values (
1402, sysdate,'FL Mathematics',
1400,'1.0', 1,'AC',
'ST','MATHEMATICS','FL Mathematics',
4,3,4);

insert into product (
product_id, created_date_time, product_description,
parent_product_id, version, 	created_by, activation_status, 
product_type, product_name, internal_display_name, 
	scoring_item_set_level, content_area_level, preview_item_set_level) values (
1403, sysdate,'FL Mathematics',
1400,'1.0', 1,'AC',
'ST','SCIENCE','FL Science',
4,3,4);


--- create item_sets_categories

insert into item_set_category (
      item_set_category_id, item_set_category_level, item_set_category_name, 
      created_by, created_date_time, framework_product_id)
values (
      seq_item_set_category_id.nextval, 1, 'Root',
      1, sysdate, 1400);

insert into item_set_category (
      item_set_category_id, item_set_category_level, item_set_category_name,
      created_by, created_date_time, framework_product_id) 
values (
      seq_item_set_category_id.nextval, 2, 'Grade',
      1, sysdate, 1400);

insert into item_set_category (
      item_set_category_id, item_set_category_level, item_set_category_name, 
      created_by, created_date_time, framework_product_id)
values (
      seq_item_set_category_id.nextval, 3, 'Content Area',
      1, sysdate, 1400);

insert into item_set_category (
      item_set_category_id, item_set_category_level, item_set_category_name,
      created_by, created_date_time, framework_product_id) 
values (
      seq_item_set_category_id.nextval, 4, 'Strand',
      1, sysdate, 1400);

insert into item_set_category (
      item_set_category_id, item_set_category_level, item_set_category_name, 
      created_by, created_date_time, framework_product_id)
values (
      seq_item_set_category_id.nextval, 5, 'Standard',
      1, sysdate, 1400);

insert into item_set_category (
      item_set_category_id, item_set_category_level, item_set_category_name,
      created_by, created_date_time, framework_product_id) 
values (
      seq_item_set_category_id.nextval, 6, 'Benchmark',
      1, sysdate, 1400);

insert into item_set_category (
      item_set_category_id, item_set_category_level, item_set_category_name, 
      created_by, created_date_time, framework_product_id)
values (
      seq_item_set_category_id.nextval, 7, 'Skill',
      1, sysdate, 1400);

-- Import the FL framework on the target database for JUNIT
-- "bin\fwktool.bat import frameworkfile=testdata\sofa\FL\levels.txt objectivesfile=testdata\sofa\FL\objectives.txt env=<target env>"



----------------------------------------------------------
-- end of JUNIT test data for SOFA
----------------------------------------------------------

----------------------------------------------------------
-- start of functional test data for import EOC items
----------------------------------------------------------
-- create CAB-EOC product
insert into product (
product_id, created_date_time, product_description, 
version, created_by, activation_status, product_type, product_name, scoring_item_set_level, content_area_level, 
preview_item_set_level, internal_display_name) values (
4000,sysdate,'CTB End Of Course',
'1.0',1,'AC','CF',
'CTB End Of Course',4,3,
4,'CTB-EOC');


insert into product (
product_id, created_date_time, product_description,
parent_product_id, version, 	created_by, activation_status, 
product_type, product_name, internal_display_name, 
	scoring_item_set_level, content_area_level, preview_item_set_level) values (
4001, sysdate,'CTB-EOC English I',
4000,'1.0', 1,'AC',
'ST','ENGLISH I','CTB-EOC English I',
4,3,4);

insert into product (
product_id, created_date_time, product_description,
parent_product_id, version, 	created_by, activation_status, 
product_type, product_name, internal_display_name, 
	scoring_item_set_level, content_area_level, preview_item_set_level) values (
4002, sysdate,'CTB-EOC English II',
4000,'1.0', 1,'AC',
'ST','ENGLISH II','CTB-EOC English II',
4,3,4);

insert into product (
product_id, created_date_time, product_description, 
parent_product_id, version, created_by, activation_status,
product_type, product_name, internal_display_name, 
scoring_item_set_level, content_area_level, preview_item_set_level)
values (
4003, sysdate, 'CTB-EOC Geography',
4000, '1.0',1,'AC',
'ST','GEOGRAPHY', 'CTB-EOC Geography',
4,3,4);

insert into product (
product_id, created_date_time, product_description,
parent_product_id, version, 	created_by, activation_status, 
product_type, product_name, internal_display_name, 
	scoring_item_set_level, content_area_level, preview_item_set_level) values (
4004, sysdate,'CTB-EOC United States History',
4000,'1.0', 1,'AC',
'ST','UNITED STATES HISTORY','CTB-EOC United States History',
4,3,4);


insert into product (
product_id, created_date_time, product_description,
parent_product_id, version, 	created_by, activation_status, 
product_type, product_name, internal_display_name, 
	scoring_item_set_level, content_area_level, preview_item_set_level) values (
4005, sysdate,'CTB-EOC Algebra I',
4000,'1.0', 1,'AC',
'ST','ALGEBRA I','CTB-EOC Algebra I',
4,3,4);

insert into product (
product_id, created_date_time, product_description,
parent_product_id, version, 	created_by, activation_status, 
product_type, product_name, internal_display_name, 
	scoring_item_set_level, content_area_level, preview_item_set_level) values (
4006, sysdate,'CTB-EOC Algebra II',
4000,'1.0', 1,'AC',
'ST','ALGEBRA II','CTB-EOC Algebra II',
4,3,4);

insert into product (
product_id, created_date_time, product_description,
parent_product_id, version, 	created_by, activation_status, 
product_type, product_name, internal_display_name, 
	scoring_item_set_level, content_area_level, preview_item_set_level) values (
4007, sysdate,'CTB-EOC Civics',
4000,'1.0', 1,'AC',
'ST','CIVICS','CTB-EOC Civics',
4,3,4);

insert into product (
product_id, created_date_time, product_description,
parent_product_id, version, 	created_by, activation_status, 
product_type, product_name, internal_display_name, 
	scoring_item_set_level, content_area_level, preview_item_set_level) values (
4008, sysdate,'CTB-EOC Economics',
4000,'1.0', 1,'AC',
'ST','ECONOMICS','CTB-EOC Economics',
4,3,4);

insert into product (
product_id, created_date_time, product_description,
parent_product_id, version, 	created_by, activation_status, 
product_type, product_name, internal_display_name, 
	scoring_item_set_level, content_area_level, preview_item_set_level) values (
4009, sysdate,'CTB-EOC World History',
4000,'1.0', 1,'AC',
'ST','WORLD HISTORY','CTB-EOC World History',
4,3,4);

insert into product (
product_id, created_date_time, product_description,
parent_product_id, version, 	created_by, activation_status, 
product_type, product_name, internal_display_name, 
	scoring_item_set_level, content_area_level, preview_item_set_level) values (
4010, sysdate,'CTB-EOC Biology',
4000,'1.0', 1,'AC',
'ST','BIOLOGY','CTB-EOC Biology',
4,3,4);

insert into product (
product_id, created_date_time, product_description,
parent_product_id, version, 	created_by, activation_status, 
product_type, product_name, internal_display_name, 
	scoring_item_set_level, content_area_level, preview_item_set_level) values (
4011, sysdate,'CTB-EOC Physics',
4000,'1.0', 1,'AC',
'ST','PHYSICS','CTB-EOC Physics',
4,3,4);


-- create item_sets_categories
insert into item_set_category (
      item_set_category_id, item_set_category_level, item_set_category_name, 
      created_by, created_date_time, framework_product_id)
values (
      seq_item_set_category_id.nextval, 1, 'Root',
      1, sysdate, 4000);

insert into item_set_category (
      item_set_category_id, item_set_category_level, item_set_category_name,
      created_by, created_date_time, framework_product_id) 
values (
      seq_item_set_category_id.nextval, 2, 'Grade',
      1, sysdate, 4000);

insert into item_set_category (
      item_set_category_id, item_set_category_level, item_set_category_name, 
      created_by, created_date_time, framework_product_id)
values (
      seq_item_set_category_id.nextval, 3, 'Content Area',
      1, sysdate, 4000);

insert into item_set_category (
      item_set_category_id, item_set_category_level, item_set_category_name,
      created_by, created_date_time, framework_product_id) 
values (
      seq_item_set_category_id.nextval, 4, 'Strand',
      1, sysdate, 4000);

insert into item_set_category (
      item_set_category_id, item_set_category_level, item_set_category_name, 
      created_by, created_date_time, framework_product_id)
values (
      seq_item_set_category_id.nextval, 5, 'Standard',
      1, sysdate, 4000);

insert into item_set_category (
      item_set_category_id, item_set_category_level, item_set_category_name, 
      created_by, created_date_time, framework_product_id)
values (
      seq_item_set_category_id.nextval, 6, 'Benchmark',
      1, sysdate, 4000);

insert into item_set_category (
      item_set_category_id, item_set_category_level, item_set_category_name, 
      created_by, created_date_time, framework_product_id)
values (
      seq_item_set_category_id.nextval, 7, 'Skill',
      1, sysdate, 4000);


-- Import the CAB-EOC framework on the target database for JUNIT
-- "bin\fwktool.bat import frameworkfile=../mappingdata/CAB-EOC/levels.txt objectivesfile=../mappingdata/CAB-EOC/Objectives.txt env=<target env>"

-- add test product for TERRANOVA
insert into product (
product_id, created_date_time, product_description,
parent_product_id, version, 	created_by, activation_status, 
product_type, product_name, internal_display_name, 
	scoring_item_set_level, content_area_level, preview_item_set_level) values (
3067, sysdate,'Terranova UnitTest Reading',
3060,'1.0', 1,'AC',
'TN','UnitTest Reading','TERRA UnitTest Reading',
4,3,4);

insert into item_type_code(item_type, item_type_desc) values('RQ', 'Research Question')

-- add think type code to table
ALTER TABLE ITEM
	ADD THINK_CODE	VARCHAR2(32);
	
ALTER TABLE PRODUCT
	ADD IBS_SHOW_CMS_ID	VARCHAR2(2) DEFAULT 'F' NOT NULL;
----------------------------------------------------------
-- end of functional test data for import EOC items
----------------------------------------------------------

CCSDEV UPDATED TO HERE */
insert into product_type_code(product_type,product_type_desc) values ('TN','TerraNova Curriculum');
insert into template_code (IS_READING_PASSAGE,IS_STIMULUS_GRAPHIC,TEMPLATE_ID) values('T','T','TP1');
insert into template_code (IS_READING_PASSAGE,IS_STIMULUS_GRAPHIC,TEMPLATE_ID) values('T','T','TP2');
insert into template_code (IS_READING_PASSAGE,IS_STIMULUS_GRAPHIC,TEMPLATE_ID) values('T','T','TP3');
insert into template_code (IS_READING_PASSAGE,IS_STIMULUS_GRAPHIC,TEMPLATE_ID) values('T','T','4a');
insert into template_code (IS_READING_PASSAGE,IS_STIMULUS_GRAPHIC,TEMPLATE_ID) values('T','T','8a');
insert into template_code (IS_READING_PASSAGE,IS_STIMULUS_GRAPHIC,TEMPLATE_ID) values('T','T','29');
insert into template_code (IS_READING_PASSAGE,IS_STIMULUS_GRAPHIC,TEMPLATE_ID) values('T','T','30');
insert into template_code (IS_READING_PASSAGE,IS_STIMULUS_GRAPHIC,TEMPLATE_ID) values('T','T','31');
insert into template_code (IS_READING_PASSAGE,IS_STIMULUS_GRAPHIC,TEMPLATE_ID) values('T','T','32');
insert into template_code (IS_READING_PASSAGE,IS_STIMULUS_GRAPHIC,TEMPLATE_ID) values('T','T','33');
insert into template_code (IS_READING_PASSAGE,IS_STIMULUS_GRAPHIC,TEMPLATE_ID) values('T','T','34');
insert into template_code (IS_READING_PASSAGE,IS_STIMULUS_GRAPHIC,TEMPLATE_ID) values('T','T','35');
insert into template_code (IS_READING_PASSAGE,IS_STIMULUS_GRAPHIC,TEMPLATE_ID) values('T','T','36');
insert into template_code (IS_READING_PASSAGE,IS_STIMULUS_GRAPHIC,TEMPLATE_ID) values('T','T','37');
insert into template_code (IS_READING_PASSAGE,IS_STIMULUS_GRAPHIC,TEMPLATE_ID) values('T','T','38');
insert into template_code (IS_READING_PASSAGE,IS_STIMULUS_GRAPHIC,TEMPLATE_ID) values('T','T','39');
insert into template_code (IS_READING_PASSAGE,IS_STIMULUS_GRAPHIC,TEMPLATE_ID) values('T','T','40');
insert into template_code (IS_READING_PASSAGE,IS_STIMULUS_GRAPHIC,TEMPLATE_ID) values('T','T','41');
insert into template_code (IS_READING_PASSAGE,IS_STIMULUS_GRAPHIC,TEMPLATE_ID) values('T','T','42');
insert into template_code (IS_READING_PASSAGE,IS_STIMULUS_GRAPHIC,TEMPLATE_ID) values('T','T','43');
insert into template_code (IS_READING_PASSAGE,IS_STIMULUS_GRAPHIC,TEMPLATE_ID) values('T','T','44');
insert into template_code (IS_READING_PASSAGE,IS_STIMULUS_GRAPHIC,TEMPLATE_ID) values('T','T','45');

insert into product (
product_id, created_date_time, product_description, 
parent_product_id, version, created_by, activation_status,
product_type, product_name, internal_display_name, 
scoring_item_set_level, content_area_level, preview_item_set_level)
values (
10001, sysdate, 'CTB Reading Premade',
1003, '1.0',1,'AC',
'ST','CTB Reading Premade', 'CTB Reading',
4,3,4);




insert into product (
product_id, created_date_time, product_description, 
parent_product_id, version, created_by, activation_status,
product_type, product_name, internal_display_name, 
scoring_item_set_level, content_area_level, preview_item_set_level)
values (
1404, sysdate, 'FL ENGLISH LANGUAGE ARTS SOFA',
1400, '1.0',1,'AC',
'SF','FL ENGLISH LANGUAGE ARTS SOFA', 'FL ENGLISH LANGUAGE ARTS SOFA',
4,3,4);

-- for oas 3.5
load TABE framework
load TERRA1 framework

insert into product (
product_id, created_date_time, product_description, 
parent_product_id, version, created_by, activation_status,
product_type, product_name, internal_display_name, 
scoring_item_set_level, content_area_level, preview_item_set_level)
values (
3520, sysdate, 'TerraNova Unit Test',
3500, '1.0',1,'AC',
'TV','Unit Test', 'TERRA1 Unit Test',
4,3,4);

insert into product (
product_id, created_date_time, product_description, 
parent_product_id, version, created_by, activation_status,
product_type, product_name, internal_display_name, 
scoring_item_set_level, content_area_level, preview_item_set_level)
values (
4020, sysdate, 'TABE Unit Test',
4000, '1.0',1,'AC',
'TB','Unit Test', 'TABE Unit Test',
4,2,4);

-- load WV
INSERT INTO TEMPLATE_CODE ( IS_READING_PASSAGE, IS_STIMULUS_GRAPHIC,
TEMPLATE_DESC, TEMPLATE_ID ) VALUES ( 'T', 'T', ' ', '25A');

INSERT INTO TEMPLATE_CODE ( IS_READING_PASSAGE, IS_STIMULUS_GRAPHIC,
TEMPLATE_DESC, TEMPLATE_ID ) VALUES ( 'T', 'T', ' ', '46');

INSERT INTO TEMPLATE_CODE ( IS_READING_PASSAGE, IS_STIMULUS_GRAPHIC,
TEMPLATE_DESC, TEMPLATE_ID ) VALUES ( 'T', 'T', ' ', '47');

INSERT INTO TEMPLATE_CODE ( IS_READING_PASSAGE, IS_STIMULUS_GRAPHIC,
TEMPLATE_DESC, TEMPLATE_ID ) VALUES ( 'T', 'T', ' ', '8A');

INSERT INTO TEMPLATE_CODE ( IS_READING_PASSAGE, IS_STIMULUS_GRAPHIC,
TEMPLATE_DESC, TEMPLATE_ID ) VALUES ( 'T', 'T', ' ', '40');

INSERT INTO TEMPLATE_CODE ( IS_READING_PASSAGE, IS_STIMULUS_GRAPHIC,
TEMPLATE_DESC, TEMPLATE_ID ) VALUES ( 'T', 'T', ' ', '44');

INSERT INTO TEMPLATE_CODE ( IS_READING_PASSAGE, IS_STIMULUS_GRAPHIC,
TEMPLATE_DESC, TEMPLATE_ID ) VALUES ( 'T', 'T', ' ', '45');
-- add template
