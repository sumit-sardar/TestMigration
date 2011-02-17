SELECT ITEM_SET_ID FROM ITEM_SET, ITEM_SET_CATEGORY, PRODUCT WHERE
	   ITEM_SET.ITEM_SET_CATEGORY_ID = ITEM_SET_CATEGORY.ITEM_SET_CATEGORY_ID AND
	   ITEM_SET_CATEGORY.FRAMEWORK_PRODUCT_ID = PRODUCT.PRODUCT_ID AND
	   ITEM_SET.ITEM_SET_TYPE = 'RE' AND
	   ITEM_SET.EXT_CMS_ITEM_SET_ID = '7R.2.2.2' AND
	   UPPER(PRODUCT.INTERNAL_DISPLAY_NAME) = 'CTB'
	   
select * from item_set where item_set_id = 2602	   
	   
select * from item where item_id='4H.1.1.02M'	   
	   
select * from item_set_category, product where 
	   ITEM_SET_CATEGORY.FRAMEWORK_PRODUCT_ID = PRODUCT.PRODUCT_ID and
	   UPPER(PRODUCT.INTERNAL_DISPLAY_NAME) = 'CTB'
	   
select * from item_set, item_set_category, product where 
	   ext_cms_item_set_id like '7%' and
	   ITEM_SET.ITEM_SET_CATEGORY_ID = ITEM_SET_CATEGORY.ITEM_SET_CATEGORY_ID AND 
	   ITEM_SET_CATEGORY.FRAMEWORK_PRODUCT_ID = PRODUCT.PRODUCT_ID and
	   UPPER(PRODUCT.INTERNAL_DISPLAY_NAME) = 'CTB'  
	   
select item_set.* from item_set_item, item_set where 
	   item_set.item_set_type='RE' and
	   item_set.item_set_id = item_set_item.item_set_id and
	   item_set_item.item_id = '4SC.4.2.38M'	
	   
select * from item_set where grade = '10'
	  
select * from item_set_item where item_id='4H.1.1.02M'

select unique(grade) from item    

select * from product where internal_display_name = 'CTB Mathematics'

select * from item_set_product where product_id = 1014