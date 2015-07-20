create or replace package llo_rp_delete_procedure is

  -- Author  : TCS
  -- Created : 2/23/2015 4:29:19 PM
  -- Purpose : OAS - OAS-1806

  PROCEDURE cleanup_llo_content;

  /*
     This procedure is used to get TC,TS,TD and RE data from product id and store this ids in array.
  */
  PROCEDURE delete_llo_rp_testdata(productId IN number);

  /*
    This procedure is used to delete all TD data from the database.
  */
  PROCEDURE delete_td_data_llo_rp(td_id c_td_id_list);

  /*
    This procdedure is used to delete all TS data from database.
  */
  PROCEDURE delete_ts_data_llo_rp(ts_id c_ts_id_list);

  /*
     This procedure is used to delete all TC data from database.
  */
  PROCEDURE delete_tc_data_llo_rp(tc_id c_tc_id_list);

  /*
     This procedure is used to delete all RE data from database.
  */
  PROCEDURE delete_re_data_llo_rp(re_id c_re_id_list);

end llo_rp_delete_procedure;
/
create or replace package body llo_rp_delete_procedure is
  v_item_id_array  c_item_id_list := c_item_id_list();
  v_count_item     integer := 0;
  check_product_id number := 0;

  procedure cleanup_llo_content as
  
  begin
  
    delete_llo_rp_testdata(7200);
    delete_llo_rp_testdata(7800);
    delete from product_type_code where product_type in ('LLBMT', 'STBMT');
  
    commit;
  
    EXCEPTION
    when others then
    dbms_output.put_line('before rollback');
    rollback;
  
  end;

  procedure delete_llo_rp_testdata(productId IN number) IS
    --temp variable to store test_catalog id
  
    temp_for_td               number := 0;
    temp_tc_item_set_id       test_catalog.item_set_id%type := 0;
    temp_for_ts               number := 0;
    temp_for_tc               number := 0;
    temp_for_re               number := 0;
    vItem_set_id              item_set.item_set_id%type;
    vItem_set_type            item_set.item_set_type%type;
    vRe_Item_Set_Id           llo_rp_lookup_item_set_re.new_id%type;
    vRe_Item_Set_Category_Lvl item_set_category.item_set_category_level%type;
  
    -- user defined exception
    ex_invalid_test_catalog_id EXCEPTION;
  
    -- cursor  for item_set_id
    cursor id_lists is
      select its.item_set_id as id, its.item_set_type as id_type
        from item_set          its,
             item_set_ancestor isa,
             test_catalog      tc,
             product           p
       where its.item_set_id = isa.item_set_id
         and tc.item_set_id = isa.ancestor_item_set_id
         and tc.product_id = p.product_id
         and p.parent_product_id = check_product_id;
  
    n_td c_td_id_list := c_td_id_list();
    n_ts c_ts_id_list := c_ts_id_list();
    n_tc c_tc_id_list := c_tc_id_list();
    n_re c_re_id_list := c_re_id_list();
  
    /*
    cursor defined to gather all re level item_set_ids
    */
    cursor re_id_lists is
      select distinct lre.new_id                  as new_id,
                      isc.item_set_category_level as item_set_category_level
        from llo_rp_lookup_item_set_re lre,
             item_set_product          isp,
             item_set                  iset,
             item_set_category         isc
      
       where lre.new_id = isp.item_set_id
         and lre.new_id = iset.item_set_id
         and isc.item_set_category_id = iset.item_set_category_id
         and isp.product_id = check_product_id
      union
      select iset.item_set_id || '' as new_id,
             isc.item_set_category_level as item_set_category_level
        from item_set iset, item_set_category isc
       where isc.framework_product_id = check_product_id
         and isc.item_set_category_level = 1
         and isc.item_set_category_id = iset.item_set_category_id
       order by item_set_category_level desc;
  
  BEGIN
    check_product_id := productId;
    dbms_output.put_line('1');
    open id_lists;
    LOOP
      Fetch id_lists
        into vItem_set_id, vItem_set_type;
      exit when id_lists%notfound;
    
      if vItem_set_type = 'TD' then
        temp_for_td := temp_for_td + 1;
        n_td.extend;
        n_td(temp_for_td) := vItem_set_id;
        --dbms_output.put_line('TD ID:' || n_td(temp_for_td));
      
      elsif (vItem_set_type = 'TS') then
      
        temp_for_ts := temp_for_ts + 1;
        n_ts.extend;
        n_ts(temp_for_ts) := vItem_set_id;
        --dbms_output.put_line('TS ID:' || n_ts(temp_for_ts));
      
      elsif (vItem_set_type = 'TC') then
        temp_for_tc := temp_for_tc + 1;
        n_tc.extend;
        n_tc(temp_for_tc) := vItem_set_id;
        --dbms_output.put_line('TC ID:' || n_tc(temp_for_tc));
      end if;
    END LOOP;
    close id_lists;
    dbms_output.put_line('2');
    open re_id_lists;
  
    LOOP
      Fetch re_id_lists
        into vRe_Item_Set_Id, vRe_Item_Set_Category_Lvl;
      exit when re_id_lists%notfound;
    
      temp_for_re := temp_for_re + 1;
      n_re.extend;
      n_re(temp_for_re) := vRe_Item_Set_Id;
      --dbms_output.put_line('RE ID:' || n_re(temp_for_re));
    
    END LOOP;
  
    close re_id_lists;
  
    dbms_output.put_line('3');
    delete_td_data_llo_rp(n_td);
    dbms_output.put_line('4');
    delete_ts_data_llo_rp(n_ts);
    dbms_output.put_line('5');
    delete_tc_data_llo_rp(n_tc);
    dbms_output.put_line('6');
    delete_re_data_llo_rp(n_re);
    dbms_output.put_line('7');
  
    -- delete child products
    delete from PRODUCT where parent_product_id = check_product_id;
    dbms_output.put_line('8');
  
    delete from ITEM_SET_CATEGORY
     where FRAMEWORK_PRODUCT_ID = check_product_id;
  
    delete from FRAMEWORK_PRODUCT_PARENT
     where framework_product_id = check_product_id;
    dbms_output.put_line('9');
    delete from PRODUCT where product_id = check_product_id;
    --dbms_output.put_line('before commit');
  
  END delete_llo_rp_testdata;

  PROCEDURE delete_td_data_llo_rp(td_id c_td_id_list) IS
  
    v_item_id        item_set_item.item_id%type;
    temp_for_item_no number := 0;
  
    cursor item_id_list(td_id number) is
      select isi.item_id as item_id
        from item_set_item isi, item_set its
       where isi.item_set_id = its.item_set_id
         and its.item_set_id = td_id;
  BEGIN
    FOR i IN 1 .. td_id.count LOOP
    
      open item_id_list(td_id(i));
      LOOP
        Fetch item_id_list
          into v_item_id;
        exit when item_id_list%notfound;
      
        temp_for_item_no := temp_for_item_no + 1;
        v_item_id_array.extend;
        v_item_id_array(temp_for_item_no) := v_item_id;
      
      END LOOP;
      close item_id_list;
    
      delete from datapoint_condition_code
       where datapoint_id in
             (select datapoint_id
                from datapoint
               where exists
               (select 1
                        from item_set_item
                       where item_set_item.item_set_id = td_id(i)
                         and item_set_item.item_id = datapoint.item_id));
    
      delete from datapoint
       where exists
       (select 1
                from item_set_item
               where item_set_item.item_set_id = td_id(i)
                 and item_set_item.item_id = datapoint.item_id);
    
      delete from item_Set_item where item_set_id = td_id(i);
    
      delete from item_set_product where item_set_id = td_id(i);
    
      delete from item_set_parent where item_set_id = td_id(i);
    
      delete from item_set_ancestor where item_set_id = td_id(i);
    
      delete from item_set where item_set_id = td_id(i);
    
    END LOOP;
  
    --dbms_output.put_line('All td lists are deleted:' ||   temp_td_deleted_rows);
  END delete_td_data_llo_rp;

  PROCEDURE delete_ts_data_llo_rp(ts_id c_ts_id_list) IS
  
    temp_ts_deleted_rows number := 0;
  BEGIN
    FOR i IN 1 .. ts_id.count LOOP
    
      delete from item_set_product where item_set_id = ts_id(i);
    
      delete from item_set_parent where item_set_id = ts_id(i);
    
      delete from item_set_ancestor where item_set_id = ts_id(i);
    
      delete from item_set where item_set_id = ts_id(i);
    
    END LOOP;
  
  END delete_ts_data_llo_rp;

  PROCEDURE delete_tc_data_llo_rp(tc_id c_tc_id_list) IS
  
    temp_tc_deleted_rows number := 0;
  BEGIN
    FOR i IN 1 .. tc_id.count LOOP
    
      delete from item_set_product where item_set_id = tc_id(i);
    
      delete from item_set_parent where item_set_id = tc_id(i);
    
      delete from item_set_ancestor where item_set_id = tc_id(i);
    
      delete from test_catalog where item_set_id = tc_id(i);
    
      delete from item_set where item_set_id = tc_id(i);
    
    END LOOP;
  
  END delete_tc_data_llo_rp;

  PROCEDURE delete_re_data_llo_rp(re_id c_re_id_list) IS
  
  BEGIN
    dbms_output.put_line('re lists size:' || re_id.count);
    FOR i IN 1 .. re_id.count LOOP
      --dbms_output.put_line('re lists:' || re_id(i));
    
      delete from datapoint_condition_code
       where datapoint_id in
             (select datapoint_id
                from datapoint
               where exists
               (select 1
                        from item_set_item
                       where item_set_item.item_set_id = re_id(i)
                         and item_set_item.item_id = datapoint.item_id));
    
      delete from datapoint
       where exists
       (select 1
                from item_set_item
               where item_set_item.item_set_id = re_id(i)
                 and item_set_item.item_id = datapoint.item_id);
    
      delete from item_Set_item where item_set_id = re_id(i);
    
      delete from item_set_product where item_set_id = re_id(i);
    
      delete from item_set_parent where item_set_id = re_id(i);
    
      delete from item_set_ancestor where item_set_id = re_id(i);
    
      delete from item_set where item_set_id = re_id(i);
    
    END LOOP;
  
    dbms_output.put_line('item lists size:' || v_item_id_array.count);
  
    FOR j IN 1 .. v_item_id_array.count LOOP
      v_count_item := 0;
      select count(1)
        into v_count_item
        from item_set_item
       where item_id = v_item_id_array(j);
    
      if v_count_item = 0 then
        delete from ITEM_RUBRIC_EXEMPLARS
         where item_id = v_item_id_array(j);
      
        delete from ITEM_RUBRIC_DATA where item_id = v_item_id_array(j);
        delete from item where item_id = v_item_id_array(j);
      end if;
    
    END LOOP;
  
    --exception
    --when others then
    --dbms_output.put_line('delete_re_data_llo_rp --> re id');
  
  END delete_re_data_llo_rp;

end llo_rp_delete_procedure;
/
