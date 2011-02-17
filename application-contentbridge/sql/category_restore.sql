update item_set_category 
       set item_set_category_level = mod(item_set_category_level, 1000)   
       where framework_product_id = 1003
       and item_set_category_id > 8;
commit;       