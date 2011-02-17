select * from item_set, item_set_category, item_set_product
where item_set.ITEM_SET_CATEGORY_ID = item_set_category.ITEM_SET_CATEGORY_ID
and item_set.item_set_id = item_set_product.item_set_id
and ...
