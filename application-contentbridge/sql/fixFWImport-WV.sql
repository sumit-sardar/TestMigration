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
              set item_set_name = rec_content_tmp.item_set_name,
                  item_set_display_name = rec_content_tmp.item_set_name,
                  updated_by = 2,
                  updated_date_time = sysdate,
                  activation_status = 'AC',
                  grade = rec_content_tmp.grade,
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
            from product where product.internal_display_name = rec_content_tmp.internal_product_display_name
            and product.product_type='ST';


       /* determine if the itemset of the same ext_cms_item_set_id exists
       and if it has the same name */
       select count(*) into v_count_same_item_set
           from item_set where ext_cms_item_set_id = rec_content_tmp.ext_cms_item_set_id and item_set_category_id in
           (select item_set_category_id from item_set_category where framework_product_id = v_framework_product_id);






       /* if item set row exists, then update */
       if v_count_same_item_set <> 0 then

           update item_set 
              set item_set_name = rec_content_tmp.item_set_name,
                  item_set_display_name = rec_content_tmp.item_set_name,
                  updated_by = 2,
                  updated_date_time = sysdate,
                  activation_status = 'AC',
                  grade = rec_content_tmp.grade,
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
