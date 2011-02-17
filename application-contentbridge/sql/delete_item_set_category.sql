delete item_set_parent where item_set_id in (
  select item_set_id from item_set where item_set_category_id in (
    select item_set_category_id from item_set_category where framework_product_id=2000
 )); 

delete item_set_ancestor where item_set_id in (
  select item_set_id from item_set where item_set_category_id in (
    select item_set_category_id from item_set_category where framework_product_id=2000
 )); 

delete item_set where item_set_category_id in (
  select item_set_category_id from item_set_category where framework_product_id=2000
);

delete item_set_category where framework_product_id=2000;

delete product where product_id >=2000 and product_id <= 2005;

commit;