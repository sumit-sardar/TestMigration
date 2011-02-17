-----------------------------------------------------
-- Count ItemSets in WV
-----------------------------------------------------
select count(*)
from item_set 
where item_set_category_id in ( 
    select item_set_category_id 
    from item_set_category 
    where framework_product_id in ( 
        select product_id 
        from product 
        where internal_display_name = 'WV' 
    ) 
);

-----------------------------------------------------
-- Count ItemSets with item in WV
-----------------------------------------------------

select count(unique item_set_id)
from item_set_item
where item_set_id in (
    select item_set_id
    from item_set 
    where item_set_category_id in ( 
        select item_set_category_id 
        from item_set_category 
        where framework_product_id in ( 
            select product_id 
            from product 
            where internal_display_name = 'WV' 
        ) 
    ) 
);


-----------------------------------------------------
-- Sets all WV itemSet's activation status to Inactive
-----------------------------------------------------
update item_set
set activation_status = 'IN'
where item_set_category_id in ( 
    select item_set_category_id 
    from item_set_category 
    where framework_product_id in ( 
      select product_id 
      from product 
      where internal_display_name = 'WV' 
    )
);

-------------------------------------------------
-- Activates only those ItemSets with items in WV
-------------------------------------------------
update item_set
set activation_status = 'AC'
where item_set_type = 'RE'
and item_set_id in (
    --
    -- Find the WV item_set_ids that have items 
    -- i.e. have entries in the item_set_item_table and belongs to WV item_set_ids
    --
    select unique item_set_id    
    from item_set_item
    where item_set_id in (
    --
    -- Find the WV item_set_ids
    --  i.e. item_set_ids that are linked to item_set_categories of WV
    --
    select item_set_id
        from item_set 
        where item_set_category_id in ( 
            -- 
            -- Find the item_set_category_id s linked to WV
            --
            select item_set_category_id 
            from item_set_category 
            where framework_product_id in ( 
                select product_id 
                from product 
                where internal_display_name = 'WV' 
            ) 
        ) 
    )    
);


----------------------------------------------------
-- Update all ancestor ItemSets
----------------------------------------------------

update item_set
set activation_status = 'AC'
where item_set_id in (
    select unique ancestor_item_set_id
    from item_set_ancestor
    where item_set_id in (	
        select unique item_set_id    
        from item_set_item
        where item_set_id in (
            select item_set_id
            from item_set 
            where item_set_type = 'RE'
            and item_set_category_id in ( 
                 select item_set_category_id 
                 from item_set_category 
                 where framework_product_id in ( 
                    select product_id 
                    from product 
                    where internal_display_name = 'WV' 
                ) 
            ) 
        )
    )
);    

----------------------------------------------------
-- Categorize itemsets in WV by activation status
----------------------------------------------------

select activation_status, count(*)
from item_set
where item_set_id in (
  select item_set_id
  from item_set 
  where item_set_category_id in ( 
    select item_set_category_id 
    from item_set_category 
    where framework_product_id in ( 
      select product_id 
      from product 
      where internal_display_name = 'WV' 
    ) 
  )
)
group by activation_status;



commit;
